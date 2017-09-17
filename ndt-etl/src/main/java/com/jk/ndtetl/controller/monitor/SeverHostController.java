package com.jk.ndtetl.controller.monitor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jk.ndtetl.Constant;
import com.jk.ndtetl.mon.ServerHost;
import com.jk.ndtetl.mon.service.MonitorService;
import com.jk.ndtetl.mon.service.ServerHostService;
import com.jk.ndtetl.util.DesUtils;
import com.jk.ndtetl.util.ErrorUtil;
import com.jk.ndtetl.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 *
 * @ClassName: SeverHostController
 * @Description: 服务主机的controller
 * @author wangzhi
 * @date 2017年5月20日 下午3:43:25
 *
 */
@Controller
@RequestMapping("/api")
public class SeverHostController {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	ServerHostService serverHostService;
	@Autowired
	MonitorService monitorService;

	@RequestMapping(value = "/serverHosts", method = RequestMethod.GET)
	@ResponseBody
	public Object listServerHostInfo() {
		JSONObject jo = new JSONObject();
		Map<String, Object> requestError = null;
		try {
			List<ServerHost> listServerhostInfo = serverHostService.listServerhostInfoNotStatu();
			jo.put("hostlist", listServerhostInfo);
			WebUtils.getResponse().setStatus(200);
			return jo;
		} catch (Exception e) {
			jo.put("error", "查询错误");
			requestError = ErrorUtil.getRequestError(jo);
			e.printStackTrace();
		}
		return requestError;
	}

	@RequestMapping(value = "/serverHosts/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Object getOneServerHostInfo(@PathVariable("id") Integer serverId) {
		JSONObject jo = new JSONObject();
		Map<String, Object> requestError = null;
		try {
			ServerHost listServerhostInfo = serverHostService.getOneServerhostInfo(serverId);
			String decoded = DesUtils.decoded(listServerhostInfo.getPassword(), Constant.encodeKey);
			listServerhostInfo.setPassword(decoded);
			jo.put("data", listServerhostInfo);
			String jsonString = JSON.toJSONString(jo);
			Object parse = JSON.parse(jsonString);
			WebUtils.getResponse().setStatus(200);
			return parse;
		} catch (Exception e) {
			jo.put("error", "获取数据失败");
			requestError = ErrorUtil.getRequestError(jo);
			e.printStackTrace();
		}
		return requestError;
	}

	@RequestMapping(value = "/serverHosts/{idparam}", method = RequestMethod.PUT)
	@ResponseBody
	public Object updateServerHostInfo(@RequestBody ServerHost serverHost, @PathVariable("idparam") Integer idparam) {
		Map<String, Object> requestError = null;
		JSONObject jo = new JSONObject();
		try {
			if (serverHost.getUsername() != null && serverHost.getUsername() != "") {
				if (serverHost.getPassword() != null && serverHost.getPassword() != "") {
					serverHost.setId(idparam);
					serverHostService.updateServerhostInfo(serverHost);
				} else {
					jo.put("error", "密码不能为空");
					requestError = ErrorUtil.getRequestError(jo);
					return requestError;
				}
			} else {
				jo.put("error", "用户名不能为空");
				requestError = ErrorUtil.getRequestError(jo);
				return requestError;
			}
			jo.put("success", "修改成功");
			WebUtils.getResponse().setStatus(200);
			return jo;
		} catch (Exception e) {
			jo.put("error", "修改失败");
			requestError = ErrorUtil.getRequestError(jo);
		}
		return requestError;
	}

	/**
	 * -------------------------------------------------------------------------------------------------
	 * 阅读：朱生
	 * 功效：保存host信息
	 * 参数要求：主机的标识不为空;表名不重复;Ipaddress、Username、Password不为空;
	 * 现有界面解析：identification主机标识->模块名，机器名key->description，备注key->serverNote,状态key->statu
	 * 时间：2017年7月27日 13:39:50
	 * -------------------------------------------------------------------------------------------------
	 * @param serverHost
	 * @return
	 */
	@RequestMapping(value = "/serverHosts", method = RequestMethod.POST)
	@ResponseBody
	public Object addServerHost(@RequestBody ServerHost serverHost) {
		Map<String, Object> requestError = null;
		JSONObject jo = new JSONObject();
		if(serverHost.getIdentification()==null){
			jo.put("error", "该主机的标识不能为空");
			requestError = ErrorUtil.getRequestError(jo);
			return requestError;
		}
		try {
			String tableName = "OM_SYSINFO" + serverHost.getIdentification().toUpperCase();
			serverHost.setTablename(tableName);
			List<ServerHost> listServerhostInfo = serverHostService.listServerhostInfoNotStatu();
			for (ServerHost serverHost2 : listServerhostInfo) {
				if (tableName.equals(serverHost2.getTablename())) {
					jo.put("error", "表名已经存在");
					requestError = ErrorUtil.getRequestError(jo);
					return requestError;
				}
			}
			if (serverHost.getIpaddress() != null && serverHost.getUsername() != null
					&& serverHost.getPassword() != null) {
				ServerHost configurationInfo = monitorService.getConfigurationInfo(serverHost.getIpaddress(), serverHost.getUsername(),
						serverHost.getPassword());
				if(configurationInfo!=null){
					serverHost.setCpusize(configurationInfo.getCpusize());
					serverHost.setMemorysize(configurationInfo.getMemorysize());
					serverHost.setDisksize(configurationInfo.getDisksize());
					serverHostService.saveServerhostInfo(serverHost);
					jo.put("success", "添加成功");
					WebUtils.getResponse().setStatus(200);
					return jo;
				}else{
					jo.put("error", "该主机不存在");
					requestError = ErrorUtil.getRequestError(jo);
					return requestError;
				}
			}else{
				jo.put("error", "用户名或密码或IP地址不你为空");
				requestError = ErrorUtil.getRequestError(jo);
				return requestError;
			}
		} catch (Exception e) {
			jo.put("error", "添加失败");
			requestError = ErrorUtil.getRequestError(jo);
			return requestError;
		}
	}

	@RequestMapping(value = "/serverHosts/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public Object deleteServerHostInfo(@PathVariable("id") Integer id) {
		JSONObject jo = new JSONObject();
		Map<String, Object> requestError = null;
		try {
			serverHostService.deleteServerHostInfo(id);
			jo.put("success", "删除成功");
			return jo;
		} catch (Exception e) {
			jo.put("error", "删除失败");
			requestError = ErrorUtil.getRequestError(jo);
			e.printStackTrace();
		}
		return requestError;
	}

}
