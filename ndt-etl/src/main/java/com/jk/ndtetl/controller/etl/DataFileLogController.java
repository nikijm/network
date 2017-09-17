package com.jk.ndtetl.controller.etl;/**
 * Created by polite on 2017/7/25.
 */

import com.alibaba.fastjson.JSONObject;
import com.jk.ndtetl.controller.BaseController;
import com.jk.ndtetl.dbmeta.service.IDataFileLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 朱生
 * 文件日志
 *
 * @create 2017-07-25 9:20
 **/
@Controller
@RequestMapping("/api")
public class DataFileLogController extends BaseController {

    @Autowired
    private IDataFileLogService iDataFileLogService;

    /**
     * 获取文件错误信息
     * @param dataFileId
     * @return
     */
    @RequestMapping("/dataFileLog/{id}")
    @ResponseBody
    public Object getFileLogById(@PathVariable("id") Integer dataFileId) {

        JSONObject rs = new JSONObject();
        rs.put("fileLog", iDataFileLogService.getErrorLogByFileId(dataFileId));
        return rs;
    }


}
