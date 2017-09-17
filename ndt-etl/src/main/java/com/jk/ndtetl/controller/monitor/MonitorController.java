package com.jk.ndtetl.controller.monitor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jk.ndtetl.mon.MemoryClear;
import com.jk.ndtetl.mon.ServerHost;
import com.jk.ndtetl.mon.service.MonitorService;
import com.jk.ndtetl.mon.service.ServerHostService;
import com.jk.ndtetl.util.ErrorUtil;
import com.jk.ndtetl.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;
import java.util.Map;

/**
 *
 * @ClassName: MonitorController
 * @Description: 监控的controller
 * @author wangzhi
 * @date 2017年5月20日 下午3:43:25
 *
 */
@Controller
@RequestMapping("/api/monitor")
public class MonitorController {

	@Autowired
	MonitorService monitorService;

	@Autowired
	ServerHostService serverHostService;

	@RequestMapping(value = "/machines/{id}/{module}", method = RequestMethod.GET)
	@ResponseBody
	public Object getServerInfo(@PathVariable("id") Integer serverId, @PathVariable("module") String monitorCompunent,
			String period) {
		JSONObject jo = new JSONObject();
		Map<String, Object> requestError = null;
		Object parse = null;
		try {
			if (serverId == null) {
				jo.put("error", "服务主机不能为空");
				requestError = ErrorUtil.getRequestError(jo);
				return requestError;
			}
			if (monitorCompunent == null) {
				jo.put("error", "服务模块不能为空");
				requestError = ErrorUtil.getRequestError(jo);
				return requestError;
			}
			if (period == null) {
				jo.put("error", "时间段不能为空");
				requestError = ErrorUtil.getRequestError(jo);
				return requestError;
			}
			ServerHost oneServerHost = serverHostService.getOneServerHost(serverId);
			oneServerHost.setId(serverId);
			List<Long> monitorInfoCpu = monitorService.getMonitorInfo(oneServerHost.getTablename(), monitorCompunent,period);
			jo.put(monitorCompunent, monitorInfoCpu);
			List<String> monitorInfoTime = monitorService.getMonitorInfoTime(oneServerHost.getTablename(),monitorCompunent, period);
			jo.put("createtime", monitorInfoTime);
			long InfoCount = monitorService.selectMonitorInfoCount(oneServerHost.getTablename(), monitorCompunent,period);
			jo.put("count", InfoCount);
			String jsonString = JSON.toJSONString(jo);
			parse = JSON.parse(jsonString);
			WebUtils.getResponse().setStatus(200);
			return parse;
		} catch (Exception e) {
			jo.put("error", "获取数据失败");
			requestError = ErrorUtil.getRequestError(jo);
			e.printStackTrace();
		}
		return requestError;
	}

	@RequestMapping(value = "/machines/{id}/memory", method = RequestMethod.DELETE)
	@ResponseBody
	public Object clearMemoryInfo(@PathVariable("id") Integer serverId) {
		JSONObject jo = new JSONObject();
		Map<String, Object> requestError = null;
		try {
			ServerHost oneServerHost = serverHostService.getOneServerHost(serverId);
			MemoryClear clearMemoryInfo = monitorService.clearMemoryInfo(oneServerHost);
			jo.put("usage", clearMemoryInfo);
			String jsonString = JSON.toJSONString(jo);
			Object parse = JSON.parse(jsonString);
			WebUtils.getResponse().setStatus(200);
			return parse;
		} catch (Exception e) {
			e.printStackTrace();
			jo.put("error", "清理失败");
			requestError = ErrorUtil.getRequestError(jo);
		}
		return requestError;
	}

}
