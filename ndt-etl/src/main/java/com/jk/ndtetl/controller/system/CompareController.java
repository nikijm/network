package com.jk.ndtetl.controller.system;
/**
 * Created by 朱生 on 2017/6/9.
 */

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.jk.ndtetl.BaseSystemEntity;
import com.jk.ndtetl.controller.BaseController;
import com.jk.ndtetl.dbmeta.service.IDbMetaService;
import com.jk.ndtetl.exception.CustomException;
import com.jk.ndtetl.system.service.ICompareService;
import com.jk.ndtetl.util.Checker;
import com.jk.ndtetl.util.ErrorUtil;
import com.jk.ndtetl.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 朱生
 *
 * @create 2017-06-09 14:42
 **/

@Controller
@RequestMapping("/api")
public class CompareController extends BaseController {

    @Autowired
    private ICompareService ICompareService;
    @Autowired
    private IDbMetaService iDbMetaService;

    @RequestMapping(value = "/compares/{id}", method = RequestMethod.GET)
    @ResponseBody

    public Object compareTables(@PathVariable("id") Integer etlFileId,String category, com.jk.ndtetl.controller.BasePage basePage, HttpServletRequest request) {
        JSONObject rs = new JSONObject();
        JSONObject errors = new JSONObject();
        String msg=null;
        try {
            PageInfo pageInfo = new PageInfo(iDbMetaService.getContrastDatas(etlFileId, category, basePage));
            basePage.setTotal(pageInfo.getTotal());
            rs.put("compareList", pageInfo.getList());
            rs.put("header", iDbMetaService.getContrastHeader(etlFileId,category));
        } catch (CustomException e) {
            e.printStackTrace();
            errors.put("compareDetailMsg", e.getMessage());
            msg=e.getMessage();
        }
        if (null!=msg) {
            return ErrorUtil.getRequestError(errors, msg);
        }
        rs.put(BaseSystemEntity.BASE_PAGE, basePage);
        return rs;
    }

}
