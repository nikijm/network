package com.jk.ndt.etl.controller.transform;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.jk.ndt.etl.constant.Constant;
import com.jk.ndt.etl.controller.BaseController;
import com.jk.ndt.etl.entity.access.SheetInfo;
import com.jk.ndt.etl.entity.rule.TransformResult;
import com.jk.ndt.etl.entity.rule.TransformSheetPO;
import com.jk.ndt.etl.service.access.SheetInfoService;
import com.jk.ndt.etl.service.rule.TransformService;
import com.jk.ndt.etl.utility.ErrorUtil;

/**
 * 
 * @ClassName: TransformController
 * @Description: 转换器控制类
 * @author fangwei
 * @date 2017年5月31日 下午4:45:38
 *
 */
@RequestMapping("api")
@Controller
public class TransformController extends BaseController {

    private static final String PARAM = "param";
    private static final String MESSAGE = "传入的参数不正确";

    @Autowired
    private TransformService transformService;
    @Autowired
    private SheetInfoService sheetInfoService;

    /**
     * 
     * @Description: 数据转换
     * @author fangwei
     * @date 2017年5月31日 下午4:49:51
     * @param transformSheetPO
     * @param sheetId
     * @return
     */
    @RequestMapping(value = "/sheets/{id}/transform", method = RequestMethod.POST)
    @ResponseBody
    public Object transform(@RequestBody TransformSheetPO transformSheetPO, @PathVariable("id") Integer sheetId,
            HttpServletResponse response) {
        if (transformSheetPO == null || sheetId == null) {
            JSONObject errorMessage = new JSONObject();
            errorMessage.put(PARAM, MESSAGE);
            return ErrorUtil.getRequestError(errorMessage);
        }
        
        //判断sheet表状态，只有在已清洗状态才可以转换
        TransformResult result = null;
        SheetInfo sheetInfo = sheetInfoService.getById(sheetId);
        if(sheetInfo == null || Integer.valueOf(sheetInfo.getStatus().toString()).intValue() != Constant.SHEET_STATUS_CLEANED){
            result = new TransformResult();
            result.setIsTransformed(false);
            response.setStatus(Constant.FAILED_400);
            return result;
        }
        transformSheetPO.setSheetId(sheetId);
        result = transformService.transform(transformSheetPO);
        if (!result.getIsTransformed()) {
            response.setStatus(Constant.FAILED_400);
        }
        return result;
    }

}
