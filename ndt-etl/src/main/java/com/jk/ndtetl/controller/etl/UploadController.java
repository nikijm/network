package com.jk.ndtetl.controller.etl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.jk.ndtetl.BaseSystemEntity;
import com.jk.ndtetl.controller.BaseController;
import com.jk.ndtetl.controller.BasePage;
import com.jk.ndtetl.dbmeta.DataFile;
import com.jk.ndtetl.dbmeta.DataFileType;
import com.jk.ndtetl.dbmeta.service.IDataFileService;
import com.jk.ndtetl.dbmeta.service.IDataFileTypeService;
import com.jk.ndtetl.etl.EtlOp;
import com.jk.ndtetl.etl.Org;
import com.jk.ndtetl.system.User;
import com.jk.ndtetl.system.service.IOrganizationService;
import com.jk.ndtetl.system.service.IUserService;
import com.jk.ndtetl.util.*;
import com.jk.ndtetl.util.promission.LoginSessionUtil;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author lianhanwen
 * @ClassName: UploadController
 * @Description: 文件上传控制器
 * @date 2017年5月20日 下午5:35:40
 */
@Controller

@RequestMapping(value = "/api")
public class UploadController extends BaseController {

    // 每天上传文件个数限制
    private static final int uploadedFileNumber = 30;
    // 上传文件类型限制
    private static final String[] ext = new String[]{"xls", "xlsx", "csv"};
    public static final String SAVE_URL = "upload";

    @Autowired
    private IDataFileService iDataFileService;
    @Autowired
    private IDataFileTypeService iDataFileTypeService;
    @Autowired
    private IOrganizationService iOrganizationService;
    @Autowired
    private IUserService iUserService;


    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Object ExceptionHandler(Exception exceededException) {
        JSONObject errors = new JSONObject();
        String msg = null;
        if (Checker.isNotNullOrEmpty(exceededException)
                && (exceededException instanceof MaxUploadSizeExceededException)) {
            long maxSize = ((MaxUploadSizeExceededException) exceededException).getMaxUploadSize();
            errors.put("maxSize", "文件导入超过" + maxSize + "字节限制，请联系管理员");
            msg = "文件导入超过" + maxSize + "字节限制，请分配次导入";
        }
        if (Checker.isNotNullOrEmpty(errors)) {
            return ErrorUtil.getRequestError(errors, msg);
        }
        return null;
    }

    /**
     * @param request
     * @return
     * @throws Exception
     * @Description: 处理上传信息
     * @author lianhanwen
     * @date 2017年5月20日 上午9:25:58
     */
    @RequestMapping(value = "/uploads", method = RequestMethod.POST)
    @ResponseBody
    public Object savefile(HttpServletRequest request, DataFile dataFile) {

        User loginUser = LoginSessionUtil.getLoginUser(request);
        JSONObject errors = new JSONObject();
        String msg = checkParam(errors, dataFile, loginUser);
        if (Checker.isNotNullOrEmpty(errors)) {
            return ErrorUtil.getRequestError(errors, msg);
        }
//            0  文件上传 1 文件夹链接 2 文件链接
        switch (dataFile.getUploadType()) {
            case "0":
                msg = uploadFile(errors, dataFile, loginUser, request);
                break;
            case "1":
                File filez = new File(dataFile.getFolderLink());
                File[] files = filez.listFiles();
                int size = files.length;
                for (int i = 0; i < size; i++) {
                    try {
                        saveManualDataFile(files[i], loginUser, dataFile);
                    } catch (Exception e) {
                        if (i == 0) {
                            msg = "以下文件保存失败：";
                        }
                        msg += files[i].getName() + ",";
                        errors.put(files[i].getName(), e.getCause());
                        logger.debug(files[i].getName() + ":" + e.getMessage());
                        e.printStackTrace();
                    }
                }
                break;
            case "2":
                try {
                    File file2 = new File(dataFile.getFileLink());
                    saveManualDataFile(file2, loginUser, dataFile);
                } catch (Exception e) {
                    errors.put("file", e.getCause());
                    if (null == msg) {
                        msg = "文件保存失败";
                    }
                    e.printStackTrace();
                }
                break;
        }
        if (Checker.isNotNullOrEmpty(errors)) {
            return ErrorUtil.getRequestError(errors, msg);
        }
        return null;
    }

    /**
     * 上传文档参数校验
     *
     * @param errors   错误封装
     * @param dataFile 参数
     * @return
     */
    private String checkParam(JSONObject errors, DataFile dataFile, User loginUser) {
        String msg = null;
        if (Checker.isNullOrEmpty(dataFile.getOrgId())) {
            errors.put("orgId", "请选择机构");
            msg = "请选择机构";
        }
        if (null == dataFile.getDataFileTypeId()) {
            errors.put("dataFileTypeId", "请选择文件类型");
            if (null == msg) {
                msg = "请选择文件类型";
            }
        }
        if (StringUtils.isBlank(dataFile.getUploadType())) {
            errors.put("uploadType", "请选择上传类型");
            if (null == msg) {
                msg = "请选择上传类型";
            }
        }
        Integer uploadedFileNumber = iDataFileService.getFileNumFromTodayByUserId(loginUser.getId());
        if (uploadedFileNumber >= UploadController.uploadedFileNumber) {
            errors.put("file", "今天文件上传个数达到上限，请联系管理员处理");
            if (null == msg) {
                msg = "今天文件上传个数达到上限，请联系管理员处理";
            }
        }
        switch (dataFile.getUploadType()) {
            case "0":
                if (null == dataFile.getFile()) {
                    errors.put("file", "请选择上传文件");
                    msg = "请选择上传文件";
                }
                break;
            case "1":
                if (StringUtils.isBlank(dataFile.getFolderLink())) {
                    errors.put("folderLink", "请输入文件夹链接");
                    msg = "请输入文件夹链接";
                }
                File filez = null;
                if (null == msg) {
                    filez = new File(dataFile.getFolderLink());
                    if (!filez.isDirectory()) {
                        errors.put("notExist", "请输入正确的文件夹路径");
                        msg = "请输入正确的文件夹路径";
                    }
                }
                if (filez != null && !filez.exists()) {
                    errors.put("notExist", "不存在该文件夹");
                    if (null == msg) {
                        msg = "不存在该文件夹";
                    }
                }
                if (null != filez) {
                    File[] files = filez.listFiles();
                    if (files.length < 1) {
                        errors.put("notFiles", "该文件夹为空");
                        if (null == msg) {
                            msg = "该文件夹为空";
                        }
                    }
                }
                break;
            case "2":
                if (StringUtils.isBlank(dataFile.getFileLink())) {
                    errors.put("fileLink", "请输入文件链接");
                    msg = "请输入文件链接";
                } else {
                    File file2 = new File(dataFile.getFileLink());
                    if (!file2.exists()) {
                        errors.put("notFiles", "请输入正确的文件路径");
                        if (null == msg) {
                            msg = "请输入正确的文件路径";
                        }
                    }
                }
                break;
        }
        return msg;
    }

    /**
     * 保存运维手动上传的文件（单
     *
     * @param file
     * @param loginUser
     * @param dataFile
     * @throws Exception
     */
    private void saveManualDataFile(File file, User loginUser, DataFile dataFile) throws Exception {
        List<Integer> sheets = ExcelUtil.getIndexOfSheets(file.getAbsolutePath());
        if (Checker.isNullOrEmpty(sheets)) {
            return;
        }
        Map<String, Object> map = new HashedMap();
        map.put("oldName", file.getName());
        map.put("fileName", file.getName());
        map.put("maualFilePath", File.separator + file.getParentFile().getName() + File.separator + file.getName());
        String sha1 = FileSafeCode.getSha1(file);
        if (!iDataFileService.isRepeat(sha1)) {

            List<DataFile> dataFiles = saveDataFiles(map, sheets, sha1, loginUser, dataFile, true);
            iDataFileService.saveDatafiles(dataFiles);
        }
    }

    /**
     * 外部用户文件上传
     *
     * @param errors
     * @param dataFile
     * @param loginUser
     * @param request
     * @return
     */
    private String uploadFile(JSONObject errors, DataFile dataFile, User loginUser, HttpServletRequest request) {
        String msg = null;
        // 文件保存目录
        String savePath = request.getSession().getServletContext().getRealPath("/") + SAVE_URL;
        try {
            CommonsMultipartFile commonsMultipartFile = (CommonsMultipartFile) dataFile.getFile();
            DiskFileItem diskFileItem = (DiskFileItem) commonsMultipartFile.getFileItem();
            String sha1 = FileSafeCode.getSha1(diskFileItem.getStoreLocation());
            if (!iDataFileService.isRepeat(sha1)) {
                Map<String, Object> map = CommUtil.saveFileToServer(request, commonsMultipartFile, savePath, null, ext);
                if (Checker.isNotNullOrEmpty(map.get("error"))) {
                    errors.put("file", map.get("error").toString());
                    msg = map.get("error") == null ? "" : map.get("error").toString();
                } else {
                    List<Integer> sheets = ExcelUtil.getIndexOfSheets(savePath + File.separator + map.get("fileName"));
                    if (Checker.isNullOrEmpty(sheets)) {
                        errors.put("nullFile", "请勿上传空文件");
                        if (null == msg) {
                            msg = "请勿上传空文件";
                        }
                    } else {
                        List<DataFile> dataFiles = saveDataFiles(map, sheets, sha1, loginUser, dataFile, false);
                        iDataFileService.saveDatafiles(dataFiles);
                    }
                }
            } else {
                errors.put("fileDouble", "相同文件内容不能重复提交");
                if (null == msg) {
                    msg = "相同文件内容不能重复提交";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            errors.put("file", "文件上传失败,请检查文件是否正常");
            if (null == msg) {
                msg = "文件上传失败,请检查文件是否正常";
            }
        }
        return msg;
    }

    /**
     * 获取机构
     *
     * @param loginUser
     * @param dataFile
     * @return
     */
    private Org getOrgByUserAndFile(User loginUser, DataFile dataFile) {
        if (LoginSessionUtil.isOuterUser(loginUser)) {
            return iOrganizationService.getOrganization(loginUser.getOrgId());
        } else {
            return iOrganizationService.getById(dataFile.getOrgId());
        }
    }

    /**
     * 保存DataFile
     *
     * @param map        文件名
     * @param sheets     某个dataFile的sheet
     * @param sha1       文件哈希码
     * @param loginUser  登录用户
     * @param dataFile   文件对象
     * @param firstSheet 是否只取第一个sheet true 是
     * @return dataFiles 待保存的dataFile集合
     */
    private List<DataFile> saveDataFiles(Map<String, Object> map, List<Integer> sheets, String sha1
            , User loginUser, DataFile dataFile, boolean firstSheet) {
        List<DataFile> dataFiles = new ArrayList<>();
        if (map.get("fileName") != null) {
            Org org = getOrgByUserAndFile(loginUser, dataFile);
            // 存储机构code
            if (null != org) {
                dataFile.setSourceOrgCode(org.getCode());
            }
            for (int i = 0; i < sheets.size(); i++) {
                DataFile dataFile_n = new DataFile();
                dataFile_n.setFileName((String) map.get("oldName"));
                dataFile_n.setPath(SAVE_URL + File.separator + map.get("fileName"));
                if (map.containsKey("maualFilePath")) {
                    dataFile_n.setPath(map.get("maualFilePath") == null ? "" : map.get("maualFilePath").toString());
                }
                dataFile_n.setCreated(new Date());
                dataFile_n.setCreatedBy(loginUser.getId());
                dataFile_n.setUploadDate(new Date());
                dataFile_n.setUploadUser(loginUser);
                dataFile_n.setSha1(sha1);
                dataFile_n.setStatusUpload(DataFile.DATA_STATUS_FINISHED);
                if (null != org) {
                    dataFile_n.setSourceOrgCode(org.getCode());
                }
                dataFile_n.setSheetIndex(sheets.get(i));
                if (i == 0) {
                    DataFileType dataFileType = new DataFileType();
                    dataFileType.setEtlFileTypeId(dataFile.getDataFileTypeId());
                    dataFile_n.setDataFileType(dataFileType);
                }
                dataFiles.add(dataFile_n);
                if (firstSheet) {
                    break;
                }
            }
        }
        return dataFiles;
    }

    /**
     * @param request
     * @return
     * @Description: 文档列表
     * @author lianhanwen
     * @date 2017年6月1日 下午6:57:02
     */
    @RequestMapping(value = "/uploads", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> listUploads(HttpServletRequest request, BasePage basePage) {

        // 判断当前登录的用户,如果为user,就只显示当前用户上传的文件
        Map<String, Object> param = PageData.getParamMap(request);

        User user = LoginSessionUtil.getLoginUser(request);
        if (param.containsKey("searchKey")) {
            param.put("keyword", CommUtil.getSpecialCharQuery(param.get("searchKey")));
        }
        if (LoginSessionUtil.isOuterUser(user)) {
            param.put("orgId", user.getOrgId());
        }
        basePage.setParam(param);
        List<DataFile> listByPage = iDataFileService.listByPage(basePage);
        Map<String, Object> map = new HashMap<String, Object>();
        PageInfo pageInfo = new PageInfo(listByPage);
        basePage.setCurrent(pageInfo.getPageNum());
        basePage.setTotal(pageInfo.getTotal());
        map.put("dataFiles", assemble(listByPage));
        map.put("orgs", listOrgOptions(iOrganizationService, null));
        map.put("dataFileTypes", listDataFileTypeOptions(iDataFileTypeService, null));
        map.put("user", LoginSessionUtil.getLoginUser(request));
        map.put(BaseSystemEntity.BASE_PAGE, basePage);
        return map;
    }
    /**
     * 组合返回值
     *
     * @param listDataFiles
     * @return
     */
    private Object assemble(List<DataFile> listDataFiles) {
        JSONArray rs = new JSONArray();
        if (Checker.isNotNullOrEmpty(listDataFiles)) {
            Iterator var = listDataFiles.iterator();
            while (var.hasNext()) {
                DataFile dataFile = (DataFile) var.next();
                JSONObject dataFileObject = new JSONObject();
                dataFileObject.put("etlDatafileId", dataFile.getEtlDatafileId());
                dataFileObject.put("dataFileTypeId", dataFile.getDataFileType() == null ? "" : dataFile.getDataFileType().getEtlFileTypeId());
                dataFileObject.put("dataFileTypeName", dataFile.getDataFileType() == null ? "" : dataFile.getDataFileType().getName());
                dataFileObject.put("fileName", dataFile.getFileName());
                dataFileObject.put("sourceOrgCode", dataFile.getSourceOrgCode());
                dataFileObject.put("uploadUserId", dataFile.getUploadUser() == null ? "" : dataFile.getUploadUser().getId());
                dataFileObject.put("uploadUserName", dataFile.getUploadUser() == null ? "" : dataFile.getUploadUser().getName());
                dataFileObject.put("uploadDate", DateUtils.format(dataFile.getUploadDate(), EtlOp.TIME_FORMAT_SECOND));
                dataFileObject.put("message", dataFile.getMessage());
                dataFileObject.put("statusValidate", dataFile.getStatusValidate());
                dataFileObject.put("statusConvert", dataFile.getStatusConvert());
                dataFileObject.put("statusClean", dataFile.getStatusClean());
                dataFileObject.put("statusCache", dataFile.getStatusCache());
                dataFileObject.put("statusUpload", dataFile.getStatusUpload());
                dataFileObject.put("unId", dataFile.getUnId());
                dataFileObject.put("orgName", dataFile.getOrgName());
                rs.add(dataFileObject);
            }
        }
        return rs;
    }

    /**
     * @return
     * @Description: 根据upload_id查询sheet列表
     * @author lianhanwen
     * @date 2017年6月7日 下午12:25:40
     */
//    @RequestMapping(value = "/uploads/{id}/sheets", method = RequestMethod.GET)
//    @ResponseBody
//    public List<SheetInfo> listSheetByUploadId(@PathVariable Integer id) {
//        BasePage page = new BasePage();
//        Map<String, Object> param = new HashMap<>();
//        param.put("upload_id", id);
//        page.setParam(param);
//        List<SheetInfo> listByPage = sheetInfoService.listByPage(page);
//        if (Checker.isNotNullOrEmpty(listByPage)) {
//            for (SheetInfo sheetInfo : listByPage) {
//                TransStatusUtil.sheetsStatusToString(sheetInfo);
//            }
//
//        }
//        return listByPage;
//    }
    @RequestMapping(value = "/uploadsWithSheets", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> listUploadsWithSheets(Integer page, String search) {

//        BasePage pageObj = new BasePage();
//        if (Checker.isNotNullOrEmpty(page)) {
//            pageObj.setCurrent(page);
//        }
//        Map<String, Object> param = new HashMap<>();
//        if (Checker.isNotNullOrEmpty(search)) {
//            param.put("keyword", CommUtil.getSpecialCharQuery(search).toLowerCase());
//        }
//        pageObj.setParam(param);
//        List<UploadInfo> listByPage = uploadInfoService.listUploadsWithSheets(pageObj);
//        if (Checker.isNotNullOrEmpty(listByPage)) {
//            for (UploadInfo uploadInfo : listByPage) {
//                TransStatusUtil.uploadsStatusToString(uploadInfo);
//                List<SheetInfo> sheets = uploadInfo.getSheets();
//                for (SheetInfo sheetInfo : sheets) {
//                    if (Checker.isNotNullOrEmpty(sheetInfo.getId())) {
//                        TransStatusUtil.sheetsStatusToString(sheetInfo);
//                    }
//                }
//            }
//        }

//        Map<String, Object> map = new HashMap<String, Object>();
//        PageInfo pageInfo = new PageInfo(listByPage);
//        pageObj.setCurrent(pageInfo.getPageNum());
////        pageObj.setPage_size(pageInfo.getPageSize());
//        pageObj.setTotal(pageInfo.getTotal());
//        map.put("uploads", listByPage);
//        map.put(BaseSystemEntity.BASE_PAGE, pageObj);
        return null;
    }

    /**
     * @param id
     * @param request
     * @return
     * @Description: 下载文件
     * @author lianhanwen
     * @date 2017年5月31日 下午6:01:48
     */

    @RequestMapping(value = "/uploads/{id}/download", method = RequestMethod.GET)
    @ResponseBody
    public Object download(HttpServletRequest request, @PathVariable("id") Integer id) {

        JSONObject errors = new JSONObject();
        String msg = null;

        DataFile dataFile = iDataFileService.getById(id);
        User loginUser = LoginSessionUtil.getLoginUser(request);

        // 可下载同机构下的文档
        if (LoginSessionUtil.isOuterUser(loginUser)) {
            if (!StringUtils.isBlank(dataFile.getSourceOrgCode())) {
                Org org = iOrganizationService.getOrganizationByCode(dataFile.getSourceOrgCode());
                if (null != org && org.getOrgId()!=loginUser.getOrgId()) {
                    errors.put("downloadFailed", "你没有权限下载该文件");
                    msg = "你没有权限下载该文件";
                }
            }
        }
        String fileName = dataFile.getFileName();
        String path = request.getSession().getServletContext().getRealPath(dataFile.getPath());
        File file = new File(path);
        ResponseEntity<byte[]> responseEntity = null;
        try {
            if (!file.exists()) {
                errors.put("fileNotFound", "该文件不存在");
                if (null == msg) {
                    msg = "该文件不存在";
                }
            } else {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                headers.setContentDispositionFormData("attachment", new String(fileName.getBytes("UTF-8"), "iso8859-1"));
                responseEntity = new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers,
                        HttpStatus.CREATED);
            }
        } catch (IOException e) {
            errors.put("fileDownFailed", "文件下载失败");
            if (null == msg) {
                msg = "文件下载失败";
            }
            e.printStackTrace();
        }
        if (Checker.isNotNullOrEmpty(errors)) {
            return ErrorUtil.getRequestError(errors, msg);
        }
        return responseEntity;
    }

}
