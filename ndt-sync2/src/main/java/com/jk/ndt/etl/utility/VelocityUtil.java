package com.jk.ndt.etl.utility;

import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

/**
 * 
 * @ClassName: VelocityUtil
 * @Description: 模板工具类
 * @author fangwei
 * @date 2017年6月3日 上午9:02:22
 *
 */
public class VelocityUtil {
	/**
	 * 
	 * @Description: 通过替换的map加载模板的内容
	 * @author fangwei
	 * @date 2017年6月3日 上午9:02:43
	 * @param content
	 * @param replaceMap
	 * @return
	 */
	public static String loadTemplateContent(String content, Map<String, Object> replaceMap) {
		VelocityContext context = new VelocityContext();
		Set<String> keySet = replaceMap.keySet();
		for (String key : keySet) {
			context.put(key, replaceMap.get(key));
		}
		StringWriter stringWriter = new StringWriter();
		Velocity.evaluate(context, stringWriter, "mystring", content);
		content = stringWriter.toString();
		return content;
	}
	
	public static String getContent(){
		String content = "<div class='title'>${title}</div>"+
				  "<div class='subtitle'>${now}</div>"+
				  "<div class='subtitle'>模块：${module}</div>"+
				  "<div class='subtitle'>机器名：${hostname}</div>"+
				  "<div class='subtitle'>机器IP：${IPAddress}</div>"+
				  "<div class='subtitle'>报警内容：${content}</div>";
		return content;
	}
	
	public static Map<String, Object> getMap(String title,String module,String hostname,String IPAddress,String content){
		 String dateToString = DateUtils.getDateToString(new Date());
		 Map<String, Object> map = new HashMap<String, Object>();
	     map.put("title", title);
	     map.put("now", dateToString);
	     map.put("module", module);
	     map.put("hostname", hostname);
	     map.put("IPAddress", IPAddress);
	     map.put("content", content);
		return map;
	}

}
