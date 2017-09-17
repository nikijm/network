package com.jk.ndt.etl.controller.access;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.jk.ndt.etl.constant.Constant;
import com.jk.ndt.etl.controller.BaseController;
import com.jk.ndt.etl.entity.Page;
import com.jk.ndt.etl.entity.Paging;
import com.jk.ndt.etl.entity.access.SheetInfo;
import com.jk.ndt.etl.entity.access.UploadInfo;
import com.jk.ndt.etl.service.access.SheetInfoService;
import com.jk.ndt.etl.service.access.UploadInfoService;
import com.jk.ndt.etl.utility.Checker;
import com.jk.ndt.etl.utility.CommUtil;
import com.jk.ndt.etl.utility.ErrorUtil;
import com.jk.ndt.etl.utility.FileSafeCode;
import com.jk.ndt.etl.utility.TransStatusUtil;
import com.jk.ndt.etl.utility.promission.LoginSessionUtil;

/**
 * 
 * @ClassName: UploadController
 * @Description: 文件上传控制器
 * @author lianhanwen
 * @date 2017年5月20日 下午5:35:40
 *
 */
@Controller

@RequestMapping(value = "/api")
public class UploadController extends BaseController {

    private final static String SAVE_URL = "upload";
    @Autowired
    private UploadInfoService uploadInfoService;
    @Autowired
    private SheetInfoService sheetInfoService;

    /**
     * 
     * @Description: 处理上传信息
     * @author lianhanwen
     * @date 2017年5月20日 上午9:25:58
     * @param request
     * @param model
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/uploads", method = RequestMethod.POST)
    @ResponseBody
    public Object savefile(HttpServletRequest request, String source, String type) {

        // 文件保存目录路径
        String savePath = request.getSession().getServletContext().getRealPath("/") + SAVE_URL;
        String[] ext = new String[] { "xls", "xlsx", "csv" };
        Map<String, Object> map = new HashMap<String, Object>();

        if (request instanceof MultipartHttpServletRequest) {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest.getFile("file");
            DiskFileItem dfi = (DiskFileItem) file.getFileItem();
            File dfile = dfi.getStoreLocation();
            try {
                String sha1 = FileSafeCode.getSha1(dfile);
                Boolean isRepeat = uploadInfoService.isRepeat(sha1);
                if (isRepeat) {
                    JSONObject errorMessage = new JSONObject();
                    errorMessage.put("file", "相同文件内容不能重复提交!!");
                    return ErrorUtil.getRequestError(errorMessage);
                }
                map = CommUtil.saveFileToServer(request, file, savePath, null, ext);

            } catch (Exception e) {
                e.printStackTrace();
                JSONObject errorMessage = new JSONObject();
                errorMessage.put("file", "文件上传失败,请检查文件是否正常!!");
                return ErrorUtil.getRequestError(errorMessage);
            }
        }
        // {fileName=c0cd3f4f-24af-4f9c-b335-979877d1318c.xls, fileSize=15360.0,
        // mime=xls, oldName=test.xls}
        String oldName = (String) map.get("oldName");

        if (Checker.isNotNullOrEmpty(map.get("error"))) {
            JSONObject errorMessage = new JSONObject();
            errorMessage.put("file", map.get("error").toString());
            return ErrorUtil.getRequestError(errorMessage);
        }
        UploadInfo uploadInfo = new UploadInfo();
        if (map.get("fileName") != null) {
            String filePath = savePath + File.separator + map.get("fileName");
            // 保存上传信息
            if (oldName != null) {
                uploadInfo.setFileName(oldName);
            }
            uploadInfo.setPath(filePath);
            uploadInfo.setStatus(Constant.FILE_STATUS_UNCACHE);
            uploadInfo.setUploadDate(new Date());
            try {
                uploadInfo.setSha1(FileSafeCode.getSha1(new File(filePath)));
            } catch (Exception e) {
                e.printStackTrace();
                JSONObject errorMessage = new JSONObject();
                errorMessage.put("file", e.getMessage());
                return ErrorUtil.getRequestError(errorMessage);
            }
            // TODO 暂时写死,待权限做完整合
            // Admin admin = new Admin();
            // admin.setId(new BigDecimal(1));
            // uploadInfo.setUser(admin);
            uploadInfo.setUser(LoginSessionUtil.getAdmin(request));
            if (Checker.isNotNullOrEmpty(source)) {
                uploadInfo.setSource(source);
            }
            if (Checker.isNotNullOrEmpty(type)) {
                uploadInfo.setType(type);
            }
            uploadInfoService.save(uploadInfo);

        }

        return uploadInfo;
    }

    /**
     * 
     * @Description: 上传文档列表
     * @author lianhanwen
     * @date 2017年6月1日 下午6:57:02
     * @param page
     * @param search
     * @param filter
     * @return
     */
    @RequestMapping(value = "/uploads", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> uploadList(Integer page, String search, String filter) {
        Page pageObj = new Page();
        if (Checker.isNotNullOrEmpty(page)) {
            pageObj.setCurrent(page);
        }
        Map<String, Object> param = new HashMap<>();
        if ("uncached".equals(filter)) {
            param.put("status", 0);
        }
        if (Checker.isNotNullOrEmpty(search)) {
            param.put("keyword", search.toLowerCase());
        }
        pageObj.setParam(param);
        List<UploadInfo> listByPage = uploadInfoService.listByPage(pageObj);
        for (UploadInfo uploadInfo : listByPage) {
            TransStatusUtil.uploadsStatusToString(uploadInfo);
        }
        Map<String, Object> map = new HashMap<String, Object>();
        PageInfo pageInfo = new PageInfo(listByPage);

        Paging paging = new Paging();
        paging.setCurrent(pageInfo.getPageNum());
        paging.setPage_size(pageInfo.getPageSize());
        paging.setTotal(pageInfo.getTotal());
        map.put("uploads", listByPage);
        map.put(Constant.BASE_PAGE, paging);
        return map;

    }
    /**
     * 
     * @Description: 根据upload_id查询sheet列表 
     * @author lianhanwen
     * @date 2017年6月7日 下午12:25:40 
     * @param id
     * @return
     */
    @RequestMapping(value = "/uploads/{id}/sheets", method = RequestMethod.GET)
    @ResponseBody
    public List<SheetInfo> sheetListByUploadId(@PathVariable Integer id) {
        Page page = new Page();
        Map<String, Object> param = new HashMap<>();
        param.put("upload_id", id);
        page.setParam(param);
        List<SheetInfo> listByPage = sheetInfoService.listByPage(page);
        for (SheetInfo sheetInfo : listByPage) {
            TransStatusUtil.sheetsStatusToString(sheetInfo);
        }
        return listByPage;
    }

    /**
     * 
     * @Description: 下载文件
     * @author lianhanwen
     * @date 2017年5月31日 下午6:01:48
     * @param id
     * @return
     */

    @RequestMapping(value = "/uploads/{id}/download", method = RequestMethod.GET)
    public ResponseEntity<?> download(@PathVariable Integer id) {
        UploadInfo uploadInfo = uploadInfoService.getById(id);
        String fileName = uploadInfo.getFileName();
        String path = uploadInfo.getPath();
        File file = new File(path);
        ResponseEntity<byte[]> responseEntity = null;
        try {
            if (!file.exists()) {
                JSONObject errorMessage = new JSONObject();
                errorMessage.put("file", "文件不存在,请检查!!");
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(new MediaType("application", "json", Charset.forName("utf-8")));
                String jsonString = JSONObject.toJSONString(ErrorUtil.getRequestError(errorMessage));
                return new ResponseEntity<String>(jsonString, headers, HttpStatus.BAD_REQUEST);
            }

            String dfileName = new String(fileName.getBytes("UTF-8"), "iso8859-1");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", dfileName);
            responseEntity = new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers,
                    HttpStatus.CREATED);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseEntity;
    }

}
