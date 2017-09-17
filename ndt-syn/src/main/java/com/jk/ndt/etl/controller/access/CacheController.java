package com.jk.ndt.etl.controller.access;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.jk.ndt.etl.controller.BaseController;
import com.jk.ndt.etl.converter.FileConverter;
import com.jk.ndt.etl.converter.csv.CSVFileConverter;
import com.jk.ndt.etl.converter.excel.ExcelConverter;
import com.jk.ndt.etl.converter.model.DataTable;
import com.jk.ndt.etl.entity.Page;
import com.jk.ndt.etl.entity.access.SheetInfo;
import com.jk.ndt.etl.entity.access.UploadInfo;
import com.jk.ndt.etl.entity.system.Admin;
import com.jk.ndt.etl.service.access.SheetInfoService;
import com.jk.ndt.etl.service.access.UploadInfoService;
import com.jk.ndt.etl.utility.Checker;
import com.jk.ndt.etl.utility.ErrorUtil;
import com.jk.ndt.etl.utility.TransStatusUtil;
import com.jk.ndt.etl.utility.promission.LoginSessionUtil;

/**
 * 
 * @ClassName: AnalysisController
 * @Description: 解析文件的controller
 * @author lianhanwen
 * @date 2017年5月20日 下午3:43:25
 *
 */
@Controller
@RequestMapping(value = "/api")
public class CacheController extends BaseController {

    @Autowired
    private CSVFileConverter cSVConverter;
    @Autowired
    private ExcelConverter excelConverter;
    @Autowired
    private UploadInfoService uploadInfoService;
    @Autowired
    private SheetInfoService sheetInfoService;

    /**
     * 
     * @Description: 处理上传信息
     * @author lianhanwen
     * @param request
     * @date 2017年5月20日 上午9:25:58
     * @param request
     * @param model
     * @return
     * @throws JsonProcessingException
     * 
     */
    @RequestMapping(value = "/uploads/{id}/cache", method = RequestMethod.POST)
    @ResponseBody
    public Object cache(@PathVariable Integer id, HttpServletRequest request) {
        Admin user = LoginSessionUtil.getAdmin(request);
        List<SheetInfo> byUploadId = sheetInfoService.getByUploadId(id);
        if (Checker.isNotNullOrEmpty(byUploadId)) {
            JSONObject errorMessage = new JSONObject();
            errorMessage.put("file", "文件已经缓存,请不要重复操作!!");
            return ErrorUtil.getRequestError(errorMessage);
        }
        UploadInfo uploadInfo = uploadInfoService.getById(id);

        String path = uploadInfo.getPath();
        String fileName = uploadInfo.getFileName();
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
        // 解析文件
        List<DataTable> list;
        try {
            list = handleFile(path, ext);
        } catch (Exception e1) {
            e1.printStackTrace();
            JSONObject errorMessage = new JSONObject();
            errorMessage.put("file", e1.getMessage());
            return ErrorUtil.getRequestError(errorMessage);
        }
        // 缓存文件
        try {
            uploadInfoService.cache(id, user, list);
        } catch (Exception e) {
            e.printStackTrace();
            JSONObject errorMessage = new JSONObject();
            errorMessage.put("cache", e.getMessage());
            return ErrorUtil.getRequestError(errorMessage);
        }
        // 返回结果
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

    private List<DataTable> handleFile(String filePath, String extend) throws Exception {
        FileConverter converter = null;
        switch (extend) {
        case "xls":
            converter = excelConverter;
            break;
        case "xlsx":
            converter = excelConverter;
            break;
        case "csv":
            converter = cSVConverter;
            break;
        default:
            converter = excelConverter;
            break;
        }
        return converter.convert(filePath);
    }
}
