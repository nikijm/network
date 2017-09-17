package com.jk.ndt.etl.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;

import com.alibaba.fastjson.JSONObject;

import oracle.sql.CLOB;

/**
 * 转换工具类 Created by 朱生 on 2017/5/21.
 */
public class TransforUtil {

	public static <T> List<T> asList(T... a) {
		if (null == a)
			return new ArrayList<T>();
		return Arrays.asList(a);
	}

	/**
	 * 将CLOB转换成字串
	 * 
	 * @param clob
	 * @return
	 */
	public static String convertClobToString(CLOB clob) {
		String reString = "";
		try {
			Reader is = clob.getCharacterStream();// 得到流
			BufferedReader br = new BufferedReader(is);
			String s = br.readLine();
			StringBuffer sb = new StringBuffer();
			while (s != null) {
				sb.append(s);
				sb.append("/n");
				s = br.readLine();
			}
			reString = sb.toString().trim();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reString;
	}

	/**
	 * 转json参数为jsonObject对象
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public static JSONObject handleMapToString(HttpServletRequest request) {

		JSONObject jsonObject = new JSONObject();
		try {
			BufferedReader reader = request.getReader();
			String input = null;
			StringBuffer requestBody = new StringBuffer();
			while ((input = reader.readLine()) != null) {
				requestBody.append(input);
			}
			jsonObject = JSONObject.parseObject(requestBody.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}

	/**
	 * 前提条件 删除 @requestbody
	 * 
	 * @param request
	 * @param classz
	 *            要转换成的对象的class Role.clas
	 * @return 失败返回空
	 */
	public static Object handleMapToObject(HttpServletRequest request, Class classz) {

		ServletInputStream reader = null;
		try {

			byte[] bytes = IOUtils.toByteArray(request.getInputStream());
			String params = new String(bytes, request.getCharacterEncoding());
			return JSONObject.parseObject(params, classz);
		} catch (Exception e) {
			System.out.println(e.getCause());
		} finally {
			if (null != reader) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

}
