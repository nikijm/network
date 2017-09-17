package com.jk.ndt.etl.utility;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 
 * @ClassName: WebUtils
 * @Description: web工具包
 * @author fangwei
 * @date 2017年5月15日 下午3:27:48
 *
 */
public class WebUtils {

	/**
	 * 获取一个键值对的map
	 * 
	 * @param keys
	 *            键数据
	 * @param values
	 *            值数组
	 * @return 返回一个完整的map
	 */
	public static Map<String, Object> getMap(String[] keys, Object[] values) {
		if (keys != null && values != null && keys.length == values.length) {
			Map<String, Object> map = new HashMap<String, Object>();
			for (int i = 0, len = keys.length; i < len; i++) {
				map.put(keys[i], values[i]);
			}
			return map;
		}
		return null;
	}

	// 获得指定文件夹在服务器的真是路径，通过纯粹的java方法获取
	public static String getRealPath(String dir) {
		return Thread.currentThread().getContextClassLoader().getResource("/").getPath().replace("WEB-INF/classes/",
				dir);
	}

	// 获取文件基础路径,以http模式的路径，浏览器可以直接访问
	public static String getBasePath(HttpServletRequest request) {
		String contextPath = request.getContextPath().equals("/") ? "" : request.getContextPath();
		String url = request.getScheme() + "://" + request.getServerName();
		if (Integer.valueOf(request.getServerPort()) != 80) {
			url = url + ":" + Integer.valueOf(request.getServerPort()) + contextPath;
		} else {
			url = url + contextPath;
		}
		return url;
	}

	/** 获取给定文件名称的后缀 */
	public static String getSuffix(String fileName) {
		return fileName.substring(fileName.lastIndexOf("."));
	}

	/**
	 * 获取UUID 2015年9月11日 上午10:22:45
	 * 
	 * @return
	 */
	public static String getUUID() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString().replaceAll("-", "");
	}

	public static void rollback() {
		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
	}

	public static String getLocalIP() {
		InetAddress addr = null;
		try {
			addr = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		byte[] ipAddr = addr.getAddress();
		String ipAddrStr = "";
		for (int i = 0; i < ipAddr.length; i++) {
			if (i > 0) {
				ipAddrStr += ".";
			}
			ipAddrStr += ipAddr[i] & 0xFF;
		}
		// System.out.println(ipAddrStr);
		return ipAddrStr;
	}

	/**
	 * 得到request对象
	 */
	public static HttpServletRequest getRequest() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();

		return request;
	}

	/**
	 * 得到response对象
	 */
	public static HttpServletResponse getResponse() {
		RequestAttributes a = RequestContextHolder.getRequestAttributes();
		HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getResponse();

		return response;
	}

	// 获取时间
	public static String getTime(int time) throws Exception {
		String ret = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		if (time > 0) {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.MINUTE, time);
			ret = sdf.format(calendar.getTime());
		} else {
			Date date = new Date();
			ret = sdf.format(date);
		}
		return ret;
	}

}
