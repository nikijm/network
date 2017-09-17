package com.jk.ndt.etl.controller.monitor;

import java.util.List;
import java.util.Map;

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
import com.jk.ndt.etl.entity.monitor.AlarmRuleBean;
import com.jk.ndt.etl.service.monitor.AlarmRuleService;
import com.jk.ndt.etl.utility.ErrorUtil;
import com.jk.ndt.etl.utility.WebUtils;

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
	public Object addOneAlarmRule(@RequestBody AlarmRuleBean alarmRuleBean) {

		JSONObject jo = new JSONObject();
		Map<String, Object> requestError = null;
		// String username = LoginSessionUtil.getUsername(request);
		alarmRuleBean.setCreateUser("xxx");
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
