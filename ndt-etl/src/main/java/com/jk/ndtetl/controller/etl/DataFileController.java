package com.jk.ndtetl.controller.etl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageInfo;
import com.jk.ndtetl.BaseSystemEntity;
import com.jk.ndtetl.controller.BaseController;
import com.jk.ndtetl.controller.BasePage;
import com.jk.ndtetl.controller.etl.task.TaskRun;
import com.jk.ndtetl.dbmeta.DataFile;
import com.jk.ndtetl.dbmeta.DataFileType;
import com.jk.ndtetl.dbmeta.TableDef;
import com.jk.ndtetl.dbmeta.service.IBusinessTypeService;
import com.jk.ndtetl.dbmeta.service.IDataFileService;
import com.jk.ndtetl.dbmeta.service.IDataFileTypeService;
import com.jk.ndtetl.dbmeta.service.IDbMetaService;
import com.jk.ndtetl.etl.EtlOp;
import com.jk.ndtetl.etl.Org;
import com.jk.ndtetl.exception.CustomException;
import com.jk.ndtetl.rule.service.IRuleUseService;
import com.jk.ndtetl.schedule.*;
import com.jk.ndtetl.schedule.conf.TaskDetailPojo;
import com.jk.ndtetl.system.User;
import com.jk.ndtetl.system.service.IOrganizationService;
import com.jk.ndtetl.util.*;
import com.jk.ndtetl.util.etl.OptionUtil;
import com.jk.ndtetl.util.promission.LoginSessionUtil;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author 朱生
 * @ClassName: UploadController
 * @Description: 文件上传控制器
 * @date 2017年5月20日 下午5:35:40
 */
@Controller
@RequestMapping(value = "/api")
public class
DataFileController extends BaseController {

    @Autowired
    private IDataFileService iDataFileService;
    @Autowired
    private IDataFileTypeService iDataFileTypeService;
    @Autowired
    private IOrganizationService iOrganizationService;
    @Autowired
    private IBusinessTypeService iBusinessTypeService;
    @Autowired
    private CacheAutoExecutor cacheAutoExecutor;
    @Autowired
    private CleanAutoExecutor cleanAutoExecutor;
    @Autowired
    private TransFormAutoExecutor transFormAutoExecutor;
    @Autowired
    private IDbMetaService iDbMetaService;
    @Autowired
    private VerifyAutoExecutor verifyAutoExecutor;
    @Autowired
    private IRuleUseService iRuleUseService;
    @Resource(name = "autoExecutor")
    AutoExecutor autoExecutor;

    /**
     * 调度任务查询数量
     */
    public static final int DATAFILE_DISPATCH_NUM = 5;

    private final static String DISPATCH_STATUS = DataFile.DATA_STATUS_STARTED + "," + DataFile.DATA_STATUS_PAUSE;

    /**
     * 调度获取某个文件的规则
     * @param dataFileid 文件id
     * @param category 类型：cache clean等  EtlOp
     * @return
     */
    @RequestMapping(value = "/dataFile/{id}/ruleUses",method = RequestMethod.GET)
    @ResponseBody
    public Object testGetRulesByFile(@PathVariable("id") Integer dataFileid,String category) {
        JSONObject errors = new JSONObject();
        JSONObject rs = new JSONObject();
        String msg=null;
        try {
            rs.put("dataFileRuleUses",iRuleUseService.getRulesByFile(dataFileid, category));
            System.out.println("est");
        } catch (CustomException e) {
            e.printStackTrace();
            errors.put("detailMsg", e.getMessage());
            msg=e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            errors.put("detailMsg", e.getMessage());
            msg="未找到该文件相关规则";
        }
        if (Checker.isNotNullOrEmpty(errors)) {
            return ErrorUtil.getRequestError(errors, msg);
        }
        return rs;
    }



    /**
     * 获取某文件的缓存数据
     *
     * @param dataFileId      datafile主键
     * @param request
     * @return
     */
    @RequestMapping(value = "/dataFile/{id}/caches", method = RequestMethod.GET)
    @ResponseBody
    public Object analyzeAndCache(@PathVariable("id") Integer dataFileId,BasePage basePage, HttpServletRequest request) {

        JSONObject rs = new JSONObject();
        JSONObject errors = new JSONObject();
        String msg=null;
        basePage.setParam(PageData.getParamMap(request));
        try {
            List<Map<String,Object>> queryCacheData=iDbMetaService.getCacheDatas(dataFileId, basePage);
            if (Checker.isNotNullOrEmpty(queryCacheData)) {
                rs.put("header", queryCacheData.get(0));
                queryCacheData.remove(0);
            }
            PageInfo pageInfo=new PageInfo(queryCacheData);
            rs.put("cacheDatas", pageInfo.getList());
            basePage.setTotal(pageInfo.getTotal());
            rs.put(BaseSystemEntity.BASE_PAGE, basePage);
        } catch (CustomException e) {
            e.printStackTrace();
            errors.put("detailMsg", e.getMessage());
            msg = e.getMessage();
        } catch (Exception e) {
            errors.put("detailMsg", e.getMessage());
            msg = "未找到该文件对应的缓存结果";
        }
        if (Checker.isNotNullOrEmpty(errors)) {
            return ErrorUtil.getRequestError(errors, msg);
        }
        return rs;
    }

    /**
     * @param request
     * @return
     * @Description: 所有工作表
     * @author 朱生
     * @date 2017年6月1日 下午6:57:02
     */
    @RequestMapping(value = "/dataFiles", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> listDataFiles(HttpServletRequest request, BasePage basePage) {

        Map<String, Object> param = PageData.getParamMap(request);
        param.put("keyword", CommUtil.getSpecialCharQuery(param.get("keyword")));
        basePage.setParam(param);

        List<DataFile> listByPage = iDataFileService.listByPage(basePage);
        Map<String, Object> rs = new HashMap<>();
        PageInfo pageInfo = new PageInfo(listByPage);
        basePage.setCurrent(pageInfo.getPageNum());
        basePage.setTotal(pageInfo.getTotal());
        rs.put("dataFiles", assemble(listByPage, null));
//        map.put("dataFileTypes", listDataFileTypeOptions(iDataFileTypeService,null));
//        rs.put("businessTypeOptions", listBusinessTypeOptions(iBusinessTypeService,null));
        rs.put("orgs", listOrgOptions(iOrganizationService, null));
        rs.put("dataFileStatusOptions", OptionUtil.listDataFileStatusOptions());
        rs.put(BaseSystemEntity.BASE_PAGE, basePage);
        return rs;
    }

    /**
     * @return
     * @Description: 调度列表
     * @author 朱生
     * @date 2017年6月1日 下午6:57:02
     */
    @RequestMapping(value = "/dataFile/dispatchs", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> listDataFileDispatchs() {

        Map<String, Object> rs = new HashMap<>();

        rs.put("cacheDataFiles", parseTaskDetailPojo(cacheAutoExecutor.getCacheMap(), TableDef.CATEGORY_CACHE));
        rs.put("cacheHingeStatus", cacheAutoExecutor.isCacheOn());
        rs.put("cleanDataFiles", parseTaskDetailPojo(cleanAutoExecutor.getCleanMap(),TableDef.CATEGORY_CLEAN));
        rs.put("cleanHingeStatus", cleanAutoExecutor.isCleanOn());
        rs.put("convertDataFiles",  parseTaskDetailPojo(transFormAutoExecutor.getTransFormMap(),TableDef.CATEGORY_CONVERT));
        rs.put("convertHingeStatus", transFormAutoExecutor.isConvertOn());
        rs.put("verifyDataFiles",  parseTaskDetailPojo(verifyAutoExecutor.getVerifyMap(),TableDef.CATEGORY_VERIFY));
        rs.put("verifyHingeStatus", verifyAutoExecutor.isVerifyOn());
        return rs;
    }

    /**
     *
     * @param mapResult
     * @param category
     * @return
     */
    private Object parseTaskDetailPojo(Map<String, TaskDetailPojo> mapResult,String category) {

        JSONArray rs = new JSONArray();
        if (mapResult.isEmpty()) {
            return rs;
        }
        for (TaskDetailPojo taskDetailPojo:mapResult.values()) {
            JSONObject dataFileObject = new JSONObject();
            dataFileObject.put("etlDatafileId", taskDetailPojo.getFileId());
            dataFileObject.put("fileName", taskDetailPojo.getFileName());
            dataFileObject.put("percents", Integer.valueOf(taskDetailPojo.getPercents()));
            dataFileObject.put("suspend", Boolean.valueOf(taskDetailPojo.isSuspend()));
            dataFileObject.put("stop", Boolean.valueOf(taskDetailPojo.isStop()));
            if (taskDetailPojo.getPercents() == 100) {
                switch (category) {
                    case TableDef.CATEGORY_CACHE:
                        cacheAutoExecutor.removeMap(taskDetailPojo.getFileId());
                        break;
                    case TableDef.CATEGORY_CLEAN:
                        cleanAutoExecutor.removeMap(taskDetailPojo.getFileId());
                        break;
                    case TableDef.CATEGORY_CONVERT:
                        transFormAutoExecutor.removeMap(taskDetailPojo.getFileId());
                        break;
                    case TableDef.CATEGORY_VERIFY:
                        verifyAutoExecutor.removeMap(taskDetailPojo.getFileId());
                        break;
                }
            }
            rs.add(dataFileObject);
        }
        return rs;
    }

    /**
     * 组合返回值
     *
     * @param listDataFiles
     * @param category      cache clean convert verify
     * @return
     */
    private Object assemble(List<DataFile> listDataFiles, String category) {
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
                TaskRun taskRun = null;
                if (null != category) {
                    switch (category) {
                        case TableDef.CATEGORY_CACHE:
                            taskRun = autoExecutor.getTaskRun("1");
                            break;
                        case TableDef.CATEGORY_CLEAN:
                            taskRun = autoExecutor.getTaskRun("2");
                            break;
                        case TableDef.CATEGORY_CONVERT:
                            taskRun = autoExecutor.getTaskRun("3");
                            break;
                        case TableDef.CATEGORY_VERIFY:
                            taskRun = autoExecutor.getTaskRun("4");
                            break;
                    }
                    dataFileObject.put("percents", "");
                    TaskDetailPojo taskDetailPojo = taskRun.getVo(dataFile.getDataFileTypeId());
                    if (null != taskDetailPojo) {
                        dataFileObject.put("percents", taskDetailPojo.getPercents());
                    }
                }
                rs.add(dataFileObject);
            }
        }
        return rs;
    }

}
