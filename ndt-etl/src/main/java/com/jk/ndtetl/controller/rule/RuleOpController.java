package com.jk.ndtetl.controller.rule;/**
 * Created by polite on 2017/6/24.
 */

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.jk.ndtetl.BaseSystemEntity;
import com.jk.ndtetl.controller.BaseController;
import com.jk.ndtetl.controller.BasePage;
import com.jk.ndtetl.rule.RuleOp;
import com.jk.ndtetl.rule.service.IRuleOpService;
import com.jk.ndtetl.util.PageData;
import com.jk.ndtetl.util.etl.OptionUtil;
import com.jk.ndtetl.util.promission.LoginSessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sun.rmi.runtime.Log;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;

/**
 * 朱生
 * 规则实现类（规则注册器、又名处理器o(︶︿︶)o ）
 * @create 2017-06-24 17:09
 **/
@Controller
@RequestMapping("/api")
public class RuleOpController extends BaseController {

    @Autowired
    private IRuleOpService iRuleOpService;
    /**
     * 修改规则
     * @param request
     * @return
     */
    @RequestMapping(value = "/ruleOps/{id}",method = RequestMethod.PUT)
    @ResponseBody
    public Object updateRuleOp(HttpServletRequest request,@PathVariable("id") Integer id, @RequestBody @Valid RuleOp ruleOp,BindingResult result) {
        ruleOp.setId(id);
        ruleOp.setUpdated(new Date());
        ruleOp.setUpdatedBy(LoginSessionUtil.getLoginUserId(request));
        iRuleOpService.update(ruleOp);
        return null;
    }
    /**
     * 保存规则
     * @param request
     * @return
     */
    @RequestMapping(value = "/ruleOps",method = RequestMethod.POST)
    @ResponseBody
    public Object saveRuleOp(HttpServletRequest request, @RequestBody @Valid RuleOp ruleOp,BindingResult result) {
        
        ruleOp.setCreated(new Date());
        ruleOp.setCreatedBy(LoginSessionUtil.getLoginUserId(request));
        iRuleOpService.save(ruleOp);
        return ruleOp.getId();
    }

    /**
     * 规则列表
     * @param request
     * @return
     */
    @RequestMapping(value = "/ruleOps",method = RequestMethod.GET)
    @ResponseBody
    public Object listRuleOp(HttpServletRequest request, BasePage basePage) {

        JSONObject rs = new JSONObject();
        JSONObject errors = new JSONObject();
        String msg=null;

//        basePage.setParam(PageData.getParamMap(request));
//        PageInfo pageInfo=new PageInfo(iRuleOpService.listByParam(PageData.getParamMap(request)));
//        basePage.setTotal(pageInfo.getTotal());
//
//        rs.put(BaseSystemEntity.BASE_PAGE, basePage);
//        rs.put("ruleOps", pageInfo.getList());
        rs.put("ruleOps",iRuleOpService.listAll());
//        rs.put(OptionUtil.OPTYPE, OptionUtil.listRuleTypeOption());
        return rs;
    }
    /**
     * 规则列表（全部）
     * @param request
     * @return
     */
    @RequestMapping(value = "/ruleOpsAll",method = RequestMethod.GET)
    @ResponseBody
    public Object listRuleOpAll(HttpServletRequest request, BasePage basePage) {
        return iRuleOpService.listByParam(PageData.getParamMap(request));
    }
    /**
     * 修改规则
     * @param request
     * @return
     */
    @RequestMapping(value = "/ruleOps/{id}",method = RequestMethod.DELETE)
    @ResponseBody
    public Object deleteRuleOp(HttpServletRequest request,@PathVariable("id") Integer id) {

        iRuleOpService.deleteById(id);
        return null;
    }

}
