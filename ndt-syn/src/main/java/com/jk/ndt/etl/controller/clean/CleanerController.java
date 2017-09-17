package com.jk.ndt.etl.controller.clean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jk.ndt.etl.constant.Constant;
import com.jk.ndt.etl.controller.BaseController;
import com.jk.ndt.etl.entity.access.SheetInfo;
import com.jk.ndt.etl.entity.rule.CleanResult;
import com.jk.ndt.etl.entity.rule.CleanerPO;
import com.jk.ndt.etl.entity.rule.CleanerSheetPO;
import com.jk.ndt.etl.rule.CleanerUtils;
import com.jk.ndt.etl.service.access.SheetInfoService;
import com.jk.ndt.etl.service.rule.CleanService;
import com.jk.ndt.etl.utility.Checker;
import com.jk.ndt.etl.utility.ErrorUtil;

/**
 * 
 * @ClassName: CleanerController
 * @Description: 清洗相关控制器
 * @author fangwei
 * @date 2017年5月26日 下午1:34:41
 *
 */
@RequestMapping("/api")
@Controller
public class CleanerController extends BaseController {

    private static final String PARAM = "param";
    private static final String MESSAGE = "传入的参数不正确";
    private static final String INPUT_STRING = "input";
    private static final String OUTPUT_STRING = "output";
    private static final String OPERATIONS = "operations";

    @Autowired
    private CleanService cleanService;
    @Autowired
    private SheetInfoService sheetInfoService;

    /**
     * 
     * @Description: 获取清洗器列表
     * @author fangwei
     * @date 2017年5月26日 下午2:19:34
     * @return
     */
    @RequestMapping(value = "/rules/operations", method = RequestMethod.GET)
    @ResponseBody
    public List<CleanerPO> listCleaners() {
        return CleanerUtils.getCleanerList();
    }

    /**
     * 
     * @Description: 按照清洗规则清洗sheet表数据
     * @author fangwei
     * @date 2017年6月2日 上午11:28:26
     * @param cleanerSheetPO
     * @param sheetId
     * @return
     */
    @RequestMapping(value = "/sheets/{id}/clean", method = RequestMethod.POST)
    @ResponseBody
    public Object clean(@RequestBody CleanerSheetPO cleanerSheetPO, @PathVariable("id") Integer sheetId,
            HttpServletResponse response) {
        if (cleanerSheetPO == null || sheetId == null) {
            JSONObject errorMessage = new JSONObject();
            errorMessage.put(PARAM, MESSAGE);
            return ErrorUtil.getRequestError(errorMessage);
        }

        //判断sheet表状态，只有在已验证状态才可以清洗
        CleanResult result = null;
        SheetInfo sheetInfo = sheetInfoService.getById(sheetId);
        if(sheetInfo == null || Integer.valueOf(sheetInfo.getStatus().toString()).intValue() != Constant.SHEET_STATUS_VALIDATED){
            result = new CleanResult();
            result.setIsCleaned(false);
            response.setStatus(Constant.FAILED_400);
            return result;
        }
        
        cleanerSheetPO.setSheet_id(sheetId);
        result = cleanService.clean(cleanerSheetPO);
        // 清洗失败返回400状态
        if (!result.getIsCleaned()) {
            response.setStatus(Constant.FAILED_400);
        }
        return result;
    }

    /**
     * 
     * @Description: 测试清洗规则
     * @author fangwei
     * @date 2017年6月2日 上午11:28:56
     * @param map
     * @return
     */
    @RequestMapping(value = "/rules/operations/test", method = RequestMethod.POST)
    @ResponseBody
    public Object operationTest(@RequestBody Map<String, Object> map) {
        if (Checker.isNullOrEmpty(map)) {
            JSONObject errorMessage = new JSONObject();
            errorMessage.put(PARAM, MESSAGE);
            return ErrorUtil.getRequestError(errorMessage);
        }

        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            String inputString = map.get(INPUT_STRING).toString();
            String cleanerJson = JSON.toJSONString(map.get(OPERATIONS));
            List<CleanerPO> cleanerPOList = JSON.parseArray(cleanerJson, CleanerPO.class);
            List<String> resultList = cleanService.operationTest(inputString, cleanerPOList);
            resultMap.put(OUTPUT_STRING, resultList);
        } catch (Exception e) {
            logger.error("清洗测试失败", e);
        }
        return resultMap;
    }
}
