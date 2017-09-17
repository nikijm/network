package com.jk.ndtetl.controller.rule;/**
 * Created by polite on 2017/6/24.
 */

import com.alibaba.druid.support.console.TabledDataPrinter;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageInfo;
import com.jk.ndtetl.BaseSystemEntity;
import com.jk.ndtetl.controller.BaseController;
import com.jk.ndtetl.controller.BasePage;
import com.jk.ndtetl.dbmeta.ColumnDef;
import com.jk.ndtetl.dbmeta.TableDef;
import com.jk.ndtetl.dbmeta.service.ITableDefService;
import com.jk.ndtetl.etl.EtlOp;
import com.jk.ndtetl.exception.CustomException;
import com.jk.ndtetl.rule.RuleUse;
import com.jk.ndtetl.rule.service.IRuleOpService;
import com.jk.ndtetl.rule.service.IRuleSuitService;
import com.jk.ndtetl.rule.service.IRuleUseService;
import com.jk.ndtetl.util.Checker;
import com.jk.ndtetl.util.ErrorUtil;
import com.jk.ndtetl.util.PageData;
import com.jk.ndtetl.util.StringUtils;
import com.jk.ndtetl.util.etl.DataCleanUtil;
import com.jk.ndtetl.util.etl.OptionUtil;
import com.jk.ndtetl.util.promission.LoginSessionUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 朱生
 * 规则映射use维护
 * @create 2017-06-24 17:09
 **/
@Controller
@RequestMapping("/api")
public class RuleUseController extends BaseController {


    @Autowired
    private IRuleUseService iRuleUseService;
    @Autowired
    private ITableDefService iTableDefService;
    @Autowired
    private IRuleSuitService iRuleSuitService;

    @Autowired
    private IRuleUseService ruleUseService;

    /**
     * 修改
     * @param request
     * @return
     */
    @RequestMapping(value = "/ruleUses/{id}",method = RequestMethod.PUT)
    @ResponseBody
    public Object updateRuleUse(HttpServletRequest request, @PathVariable("id") Integer id, @RequestBody @Valid List<RuleUse> ruleUses, BindingResult result) {
        if (Checker.isNotNullOrEmpty(ruleUses)) {
            for (RuleUse ruleUse:ruleUses) {
                ruleUse.setId(id);
            }
        }
//        iRuleUseService.update(ruleUses);
        return null;
    }
    /**
     * 保存
     * @param request
     * @return
     */
    @RequestMapping(value = "/ruleUses",method = RequestMethod.POST)
    @ResponseBody
    public Object saveRuleUse(HttpServletRequest request, @RequestBody @Valid RuleUse ruleUse, BindingResult result) {
        JSONObject errors=new JSONObject();
        String msg=null;
        if (Checker.isNullOrEmpty(ruleUse.getRuleUseColumnsList())) {
            errors.put("ruleUseColumnsList", "请选择规则模版");
            msg = "请选择规则模版";
        }
        if (null != msg) {
            return ErrorUtil.getRequestError(errors, msg);
        }
        Integer loginUserId=LoginSessionUtil.getLoginUserId(request);
        resetRuleUseValue(ruleUse, loginUserId);
        List<RuleUse> ruleUseList = iRuleUseService.listRuleSuitByTableId(ruleUse);
        String waitDelIds=null;
        if (Checker.isNotNullOrEmpty(ruleUseList)) {
            List<RuleUse> compareRuleUseColumns=new ArrayList<>();
            for (RuleUse ruleUseColumn:ruleUse.getRuleUseColumnsList()) {
                if (null == ruleUseColumn.getId()) {
                    compareRuleUseColumns.add(ruleUseColumn);
                    continue;
                }
                boolean compareRs=false;
                for (RuleUse queryRuleUse:ruleUseList) {
                    if (queryRuleUse.getId() == ruleUseColumn.getId()) {
                        compareRs=true;
                        break;
                    }
                }
                if (!compareRs) {
                    waitDelIds += ruleUseColumn.getId() + ",";
                }
            }
            ruleUse.setRuleUseColumnsList(compareRuleUseColumns);
        }
        if (null!=waitDelIds)
        waitDelIds = waitDelIds.substring(0, waitDelIds.length() - 1);
        iRuleUseService.batchSave(ruleUse.getRuleUseColumnsList(),waitDelIds);
        return null;
    }
    /**
     * 修改
     * @param request
     * @return
     */
    @RequestMapping(value = "/ruleUses",method = RequestMethod.PUT)
    @ResponseBody
    public Object updateRuleUse(HttpServletRequest request, @RequestBody @Valid RuleUse ruleUse, BindingResult result) {

        Integer loginUserId=LoginSessionUtil.getLoginUserId(request);
        resetRuleUseValue(ruleUse, loginUserId);
        iRuleUseService.updateRuleUseByTableId(ruleUse);
        return null;
    }

    /**
     * 重置RuleUse 值
     * @param ruleUse
     * @param loginUserId
     */
    private void resetRuleUseValue(RuleUse ruleUse,Integer loginUserId) {
        if (Checker.isNotNullOrEmpty(ruleUse.getRuleUseColumnsList())) {
            for (RuleUse ruleUserObject:ruleUse.getRuleUseColumnsList()) {
                ruleUserObject.setEtlTableId(ruleUse.getEtlTableId());
                ruleUserObject.setEtlTableTargetId(ruleUse.getEtlTableTargetId());
                ruleUserObject.setEtlOp(ruleUse.getEtlOp());
                ruleUserObject.setCreatedBy(loginUserId);
                ruleUserObject.setCreated(new Date());
            }
        }
    }

    /**
     * 列表
     * @param request
     * @return
     */
    @RequestMapping(value = "/ruleUses",method = RequestMethod.GET)
    @ResponseBody
    public Object listRuleUse(HttpServletRequest request,RuleUse ruleUse) {

        JSONObject errors = new JSONObject();
        String msg=null;

        Map<String,Object> param=PageData.getParamMap(request);
        JSONObject rs = new JSONObject();

        rs.put("tableDefs", "");
        rs.put("targetTableDefs", "");
        if (param.containsKey("category")) {

            rs.put("tableDefs", DataCleanUtil.excludeEmptyObject(iTableDefService.listByParam(param)));
            if (StringUtils.equals(param.get("category").toString(), TableDef.CATEGORY_CACHE)) {
                param.put("category", TableDef.CATEGORY_CLEAN);
                rs.put("targetTableDefs", DataCleanUtil.excludeEmptyObject(iTableDefService.listByParam(param)));
            }else if (StringUtils.equals(param.get("category").toString(), TableDef.CATEGORY_CLEAN)) {
                param.put("category", TableDef.CATEGORY_CONVERT);
                rs.put("targetTableDefs", DataCleanUtil.excludeEmptyObject(iTableDefService.listByParam(param)));
            }else if (StringUtils.equals(param.get("category").toString(), TableDef.CATEGORY_CONVERT)) {
                param.put("category", TableDef.CATEGORY_VERIFY);
                rs.put("targetTableDefs", DataCleanUtil.excludeEmptyObject(iTableDefService.listByParam(param)));
            }
        }
        rs.put("ruleUses", "");
        if (null != ruleUse.getEtlTableId() && null!=ruleUse.getEtlTableTargetId()) {
            rs.put("ruleUses",iRuleUseService.listRuleSuitByTableId(ruleUse));
        }
        rs.put("ruleSuits", iRuleSuitService.listAll());
        return rs;
    }
    /**
     * 删除
     * @param request
     * @return
     */
    @RequestMapping(value = "/ruleUses/{id}",method = RequestMethod.DELETE)
    @ResponseBody
    public Object deleteRuleUse(HttpServletRequest request,@PathVariable("id") Integer id) {

        iRuleUseService.deleteById(id);
        return null;
    }


}
