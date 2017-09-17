package com.jk.ndtetl.controller.monitor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jk.ndtetl.Constant;
import com.jk.ndtetl.mon.AlarmRuleBean;
import com.jk.ndtetl.mon.service.AlarmRuleService;
import com.jk.ndtetl.system.User;
import com.jk.ndtetl.util.ErrorUtil;
import com.jk.ndtetl.util.WebUtils;
import com.jk.ndtetl.util.promission.LoginSessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 *
 * @ClassName: AlarmRuleController
 * @Description: 告警的controller
 * @author wangzhi
 * @date 2017年5月25日 下午3:43:25
 *
 */
@Controller
@RequestMapping("/api")
public class AlarmRuleController {

	@Autowired
	AlarmRuleService alarmRuleService;

	@RequestMapping(value="/alarmrules",method=RequestMethod.GET)
	@ResponseBody
	public Object getAlarmRuleList(Integer serverId, Integer type, Integer status) {
		JSONObject jo = new JSONObject();
		Map<String, Object> requestError = null;
		try {
			List<AlarmRuleBean> alarmRuleList = alarmRuleService.getAlarmRuleList(serverId, type, status);
			jo.put("rule_list", alarmRuleList);
			Object object = JSON.toJSON(jo);
			WebUtils.getResponse().setStatus(200);
			return object;
		} catch (Exception e) {
			jo.put("error", "获取数据失败");
			requestError = ErrorUtil.getRequestError(jo);
			e.printStackTrace();
		}
		return requestError;
	}

	@RequestMapping(value="/alarmrules/{id}",method=RequestMethod.GET)
	@ResponseBody
	public Object getAlarmRuleById(@PathVariable("id") Integer ruleId) {
		JSONObject jo = new JSONObject();
		Map<String, Object> requestError = null;
		try {
			AlarmRuleBean alarmRuleBean = alarmRuleService.getAlarmRuleById(ruleId);
			jo.put("rule", alarmRuleBean);
			Object object = JSON.toJSON(jo);
			WebUtils.getResponse().setStatus(200);
			return object;
		} catch (Exception e) {
			jo.put("error", "获取数据失败");
			requestError = ErrorUtil.getRequestError(jo);
			e.printStackTrace();
		}
		return requestError;
	}

	@RequestMapping(value = "/alarmrules", method = RequestMethod.POST)
	@ResponseBody
	public Object addOneAlarmRule(@RequestBody AlarmRuleBean alarmRuleBean,HttpServletRequest request) {

		JSONObject jo = new JSONObject();
		Map<String, Object> requestError = null;
		User loginAccount = LoginSessionUtil.getLoginUser(request);
		if(loginAccount!=null&&loginAccount.getName()!=null){
			String loginName = loginAccount.getName();
			alarmRuleBean.setCreateUser(loginName);
		}else{
			alarmRuleBean.setCreateUser(null);
		}
		try {
			if (alarmRuleBean.getType() != null) {
				if (alarmRuleBean.getExpressionType() != null) {
					if (alarmRuleBean.getMachineType() != null) {
						for (Integer machineId : alarmRuleBean.getMachineType()) {
							alarmRuleBean.setMachineAndId(machineId);
							AlarmRuleBean alarmRuleByModuleAndType = alarmRuleService.getAlarmRuleByModuleAndType(alarmRuleBean);
							if(alarmRuleByModuleAndType!=null){
								if (alarmRuleByModuleAndType.getType() == Constant.HOSTNAME_TYPE_LOG) {
									if (alarmRuleBean.getContent() != null) {
										AlarmRuleBean alarmRuleBeanC = alarmRuleService.getAlarmRuleByContent(alarmRuleBean.getContent());
										if (alarmRuleBeanC != null) {
											jo.put("error", "规则已经存在");
											requestError = ErrorUtil.getRequestError(jo);
											return requestError;
										}
									} else {
										jo.put("error", "内容不能为空");
										requestError = ErrorUtil.getRequestError(jo);
										return requestError;
									}
								} else {
									jo.put("error", "规则已经存在");
									requestError = ErrorUtil.getRequestError(jo);
									return requestError;
								}
							}
						}
						alarmRuleService.saveOneAlarmRule(alarmRuleBean, alarmRuleBean.getMachineType());
						jo.put("success", "创建成功");
						WebUtils.getResponse().setStatus(200);
					} else {
						jo.put("error", "主机不能为空");
						requestError = ErrorUtil.getRequestError(jo);
						return requestError;
					}
				} else {
					jo.put("error", "表达式不能为空");
					requestError = ErrorUtil.getRequestError(jo);
					return requestError;
				}
			} else {
				jo.put("error", "资源不能为空");
				requestError = ErrorUtil.getRequestError(jo);
				return requestError;
			}
		} catch (Exception e) {
			jo.put("error", "获取数据失败");
			requestError = ErrorUtil.getRequestError(jo);
			e.printStackTrace();
		}
		return requestError;
	}

	@RequestMapping(value = "/alarmrules/{idparam}", method = RequestMethod.PUT)
	@ResponseBody
	public Object updateOneAlarmRule(@RequestBody AlarmRuleBean alarmRuleBean,@PathVariable("idparam") Integer idparam) {

		JSONObject jo = new JSONObject();
//		alarmRuleBean.setCreateUser(alarmRuleBean.getToken());
		Map<String, Object> requestError = null;
		try {
			if (alarmRuleBean.getType() != null) {
				if (alarmRuleBean.getExpressionType() != null) {
					if(alarmRuleBean.getMachineAndId()!=null){
						alarmRuleBean.setId(idparam);
						alarmRuleService.updateOneAlarmRule(alarmRuleBean, alarmRuleBean.getMachineType());
					}else{
						jo.put("error", "主机不能为空");
						requestError = ErrorUtil.getRequestError(jo);
						return requestError;
					}
				} else {
					jo.put("error", "表达式不能为空");
					requestError = ErrorUtil.getRequestError(jo);
					return requestError;
				}
			} else {
				jo.put("error", "资源不能为空");
				requestError = ErrorUtil.getRequestError(jo);
				return requestError;
			}
			jo.put("success", "修改规则成功");
			WebUtils.getResponse().setStatus(200);
			return jo;
		} catch (Exception e) {
			jo.put("error", "获取数据失败");
			e.printStackTrace();
			requestError = ErrorUtil.getRequestError(jo);
			return requestError;
		}
	}

	@RequestMapping(value = "/alarmrules/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public Object deleteAlarmRuleById(@PathVariable("id") Integer id) {
		Map<String, Object> requestError = null;
		JSONObject jo = new JSONObject();
		try {
			alarmRuleService.deleteAlarmRule(id);
			jo.put("success", "修改规则成功");
			WebUtils.getResponse().setStatus(200);
			return jo;
		} catch (Exception e) {
			jo.put("error", "删除失败");
			requestError = ErrorUtil.getRequestError(jo);
		}
		return requestError;
	}

}
