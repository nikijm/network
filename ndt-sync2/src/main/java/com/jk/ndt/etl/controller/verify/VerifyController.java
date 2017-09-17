package com.jk.ndt.etl.controller.verify;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jk.ndt.etl.constant.Constant;
import com.jk.ndt.etl.entity.access.SheetInfo;
import com.jk.ndt.etl.service.access.SheetInfoService;
import com.jk.ndt.etl.utility.Checker;

/**
 * 
 * @ClassName: VerifyController
 * @Description: 校验处理器
 * @author fangwei
 * @date 2017年6月5日 上午10:13:20
 *
 */
@Controller
@RequestMapping("/api")
public class VerifyController {

    private static final String IS_VERIFIED = "isVerified";

    @Autowired
    private SheetInfoService sheetInfoService;

    /**
     * 
     * @Description: 验证接口
     * @author fangwei
     * @date 2017年6月5日 上午10:36:44
     * @param sheetId
     * @return
     */
    @RequestMapping(value = "/sheets/{id}/verify", method = RequestMethod.POST)
    @ResponseBody
    public Object verify(@PathVariable("id") Integer sheetId, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<String, Object>();
        SheetInfo sheetInfo = sheetInfoService.getById(sheetId);
        if (Checker.isNotNullOrEmpty(sheetInfo)) {
            if (Integer.valueOf(sheetInfo.getStatus().toString()).intValue() == Constant.SHEET_STATUS_CLEANED) {
                sheetInfo.setStatus(Constant.SHEET_STATUS_VALIDATED);
                sheetInfoService.update(sheetInfo);
                result.put(IS_VERIFIED, true);
            }
        } else {
            response.setStatus(Constant.FAILED_400);
            result.put(IS_VERIFIED, false);
        }
        return result;
    }
}
