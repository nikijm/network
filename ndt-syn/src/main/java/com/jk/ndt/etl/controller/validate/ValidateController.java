package com.jk.ndt.etl.controller.validate;

import java.util.List;

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
import com.jk.ndt.etl.entity.rule.ValidatePO;
import com.jk.ndt.etl.entity.rule.ValidateResult;
import com.jk.ndt.etl.entity.rule.ValidateSheetPO;
import com.jk.ndt.etl.rule.ValidateUtils;
import com.jk.ndt.etl.service.access.SheetInfoService;
import com.jk.ndt.etl.service.rule.ValidateService;
import com.jk.ndt.etl.utility.ErrorUtil;

/**
 * 
 * @ClassName: ValidateController
 * @Description: 验证器操作类
 * @author fangwei
 * @date 2017年5月23日 上午10:34:31
 *
 */
@RequestMapping("/api")
@Controller
public class ValidateController extends BaseController {

    private static final String PARAM = "param";
    private static final String MESSAGE = "传入的参数不正确";

    @Autowired
    private ValidateService validateService;
    @Autowired
    private SheetInfoService sheetInfoService;

    /**
     * 
     * @Description: 获取验证器接口
     * @author fangwei
     * @date 2017年5月24日 下午4:44:50
     * @return
     */
    @RequestMapping(value = "/rules/validators", method = RequestMethod.GET)
    @ResponseBody
    public List<ValidatePO> listValidates() {
        return ValidateUtils.getValidateList();
    }

    /**
     * 
     * @Description: 验证前台提交过来的数据
     * @author fangwei
     * @date 2017年5月24日 下午5:07:46
     * @param validateSheetPO
     * @return
     */
    @RequestMapping(value = "/sheets/{id}/validate", method = RequestMethod.POST)
    @ResponseBody
    public Object validate(@RequestBody ValidateSheetPO validateSheetPO, @PathVariable("id") Integer sheetId,
            HttpServletResponse response) {
        if (validateSheetPO == null || sheetId == null) {
            JSONObject errorMessage = new JSONObject();
            errorMessage.put(PARAM, MESSAGE);
            return ErrorUtil.getRequestError(errorMessage);
        }
        
        //判断sheet表状态，只有在未验证状态才可以验证
        ValidateResult result = null;
        SheetInfo sheetInfo = sheetInfoService.getById(sheetId);
        if(sheetInfo == null || Integer.valueOf(sheetInfo.getStatus().toString()).intValue() != Constant.SHEET_STATUS_UNVALIDATED){
            result = new ValidateResult();
            result.setIsValidate(false);
            response.setStatus(Constant.FAILED_400);
            return result;
        }
        
        validateSheetPO.setSheet_id(sheetId);
        result = validateService.validate(validateSheetPO);
        // 验证不通过，返回400状态码
        if (!result.getIsValidate()) {
            response.setStatus(Constant.FAILED_400);
        }
        return result;
    }

}
