package com.jk.ndt.etl.controller.monitor;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.jk.ndt.etl.entity.monitor.ServerHost;
import com.jk.ndt.etl.service.monitor.ServerHostService;
import com.jk.ndt.etl.utility.DesUtils;
import com.jk.ndt.etl.utility.ErrorUtil;
import com.jk.ndt.etl.utility.WebUtils;

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

	@RequestMapping(value="/serverHosts",method=RequestMethod.GET)
	@ResponseBody
	public Object listServerHostInfo() {
		JSONObject jo = new JSONObject();
		Map<String, Object> requestError = null;
		try {
			List<ServerHost> listServerhostInfo = serverHostService.listServerhostInfo();
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

	@RequestMapping(value="/serverHosts/{id}",method=RequestMethod.GET)
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

	@RequestMapping(value="/serverHosts/{idparam}",method=RequestMethod.PUT)
	@ResponseBody
	public Object updateServerHostInfo(@RequestBody ServerHost serverHost,@PathVariable("idparam") Integer idparam){
		Map<String, Object> requestError=null;
		JSONObject jo=new JSONObject();
		try {
			if(serverHost.getUsername()!=null&&serverHost.getUsername()!=""){
				if(serverHost.getPassword()!=null&&serverHost.getPassword()!=""){
					serverHost.setId(idparam);
					serverHostService.updateServerhostInfo(serverHost);
				}else{
					jo.put("error", "密码不能为空");
					requestError = ErrorUtil.getRequestError(jo);
					return requestError;
				}
			}else{
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

	@RequestMapping(value="/serverHosts",method=RequestMethod.POST)
	@ResponseBody
	public Object addServerHost(@RequestBody ServerHost serverHost) {
		Map<String, Object> requestError = null;
		JSONObject jo = new JSONObject();
		try {
			String tableName="OM_SYSINFO"+serverHost.getIdentification().toUpperCase();
			serverHost.setTablename(tableName);
			List<ServerHost> listServerhostInfo = serverHostService.listServerhostInfo();
			for (ServerHost serverHost2 : listServerhostInfo) {
				if(tableName.equals(serverHost2.getTablename())){
					jo.put("error", "表名已经存在");
					requestError = ErrorUtil.getRequestError(jo);
					return requestError;
				}
			}
			serverHostService.saveServerhostInfo(serverHost);
			jo.put("success", "添加成功");
			WebUtils.getResponse().setStatus(200);
			return jo;
		} catch (Exception e) {
			jo.put("error", "修改失败");
			requestError = ErrorUtil.getRequestError(jo);
		}
		return requestError;
	}
	
	
	@RequestMapping(value="/serverHosts/{id}",method=RequestMethod.DELETE)
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
