package com.jk.ndtetl.controller.rule;/**
 * Created by polite on 2017/6/24.
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.jk.ndtetl.BaseSystemEntity;
import com.jk.ndtetl.controller.BaseController;
import com.jk.ndtetl.controller.BasePage;
import com.jk.ndtetl.dbmeta.TableDef;
import com.jk.ndtetl.rule.RuleOp;
import com.jk.ndtetl.rule.RuleSuit;
import com.jk.ndtetl.rule.RuleUse;
import com.jk.ndtetl.rule.service.IRuleOpService;
import com.jk.ndtetl.rule.service.IRuleSuitService;
import com.jk.ndtetl.rule.service.IRuleUseService;
import com.jk.ndtetl.util.Checker;
import com.jk.ndtetl.util.ErrorUtil;
import com.jk.ndtetl.util.PageData;
import com.jk.ndtetl.util.StringUtils;
import com.jk.ndtetl.util.etl.OptionUtil;
import com.jk.ndtetl.util.promission.LoginSessionUtil;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 朱生
 * 规则模版维护
 * @create 2017-06-24 17:09
 **/
@Controller
@RequestMapping("/api")
public class RuleSuitController extends BaseController {


    @Autowired
    private IRuleSuitService iRuleSuitService;
    @Autowired
    private IRuleUseService iRuleUseService;
    @Autowired
    private IRuleOpService iRuleOpService;
    /**
     * 模版修改
     * @param request
     * @return
     */
    @RequestMapping(value = "/ruleSuits/{id}",method = RequestMethod.PUT)
    @ResponseBody
    public Object updateRuleSuit(HttpServletRequest request,@PathVariable("id") Integer id, @RequestBody @Valid RuleSuit ruleSuit,BindingResult result) {
        ruleSuit.setId(id);
        ruleSuit.setRules(JSON.toJSONString(ruleSuit.getRuleOps()));
        ruleSuit.setUpdatedBy(LoginSessionUtil.getLoginUserId(request));
        ruleSuit.setUpdated(new Date());
        iRuleSuitService.update(ruleSuit);
        return null;
    }
    /**
     * 保存规则
     * @param request
     * @return
     */
    @RequestMapping(value = "/ruleSuits",method = RequestMethod.POST)
    @ResponseBody
    public Object saveRuleSuit(HttpServletRequest request,@Valid @RequestBody RuleSuit ruleSuit,BindingResult result) {

        ruleSuit.setRules(JSON.toJSONString(ruleSuit.getRuleOps()));
        ruleSuit.setCreatedBy(LoginSessionUtil.getLoginUserId(request));
        ruleSuit.setCreated(new Date());
        iRuleSuitService.save(ruleSuit);
        return ruleSuit.getId();
    }

    /**
     * 规则列表
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/ruleSuits", method = RequestMethod.GET)
    @ResponseBody
    public Object listRuleSuit(HttpServletRequest request, BasePage basePage) {

        JSONObject rs = new JSONObject();

        JSONObject errors = new JSONObject();
        String msg = null;
        basePage.setParam(PageData.getParamMap(request));
        List<RuleSuit> ruleSuitList = iRuleSuitService.listByPage(basePage);
        if (Checker.isNotNullOrEmpty(ruleSuitList)) {
            for (RuleSuit ruleSuit : ruleSuitList) {
                if (!StringUtils.isBlank(ruleSuit.getRules())) {
                    try {
                        ruleSuit.setRuleOps(JSONArray.parseArray(ruleSuit.getRules(), RuleOp.class));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        PageInfo pageInfo = new PageInfo(ruleSuitList);
        basePage.setTotal(pageInfo.getTotal());

        rs.put(BaseSystemEntity.BASE_PAGE, basePage);
        rs.put("ruleSuits", pageInfo.getList());
        rs.put("ruleOps", iRuleOpService.listAll());
//        rs.put(OptionUtil.OPTYPE, OptionUtil.listRuleType());

        return rs;
    }
    /**
     * 删除规则模版
     * @param request
     * @return
     */
    @RequestMapping(value = "/ruleSuits/{id}",method = RequestMethod.DELETE)
    @ResponseBody
    public Object deleteRuleSuit(HttpServletRequest request,@PathVariable("id") Integer id) {
        JSONObject errors = new JSONObject();
        String msg=null;
        Map<String, Object> param = new HashedMap();
        param.put("ruleSuitId", id);
        List<RuleUse> ruleUseList=iRuleUseService.listByParam(param);
        if (Checker.isNotNullOrEmpty(ruleUseList)) {
            errors.put("using", "当前规则模版正在使用中，不能删除");
            msg = "当前规则模版正在使用中，不能删除";
        }
        if (null != msg) {
            return ErrorUtil.getRequestError(errors, msg);
        }
        iRuleSuitService.deleteById(id);
        return null;
    }


}
