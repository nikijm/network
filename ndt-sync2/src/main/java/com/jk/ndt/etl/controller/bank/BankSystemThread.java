package com.jk.ndt.etl.controller.bank;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.jk.ndt.etl.entity.bank.BankProcessSync;
import com.jk.ndt.etl.entity.vo.businessoperate.BusinessOperateResBean;
import com.jk.ndt.etl.service.bank.BankSystemService;
import com.jk.ndt.etl.utility.SpringContextHolder;

public class BankSystemThread extends Thread {
	private static final String APPLICATION_JSON = "application/json";

	private static final String CONTENT_TYPE_TEXT_JSON = "text/json";

	public void run() {
		// 获取service对象
		BankSystemService bankSystemService = SpringContextHolder.getBean("bankSystemService");
		// 后台监控数据库处理线程，需要一直运行
		while (true) {

			try {
				// 获取第一个未处理记录进行处理
				BankProcessSync bankProcessSync = bankSystemService.getNTopOneUnProcessedM();
				System.out.println("---------------------------------------------------------------");
				System.out.println(bankProcessSync);
				if (null != bankProcessSync) {
					System.out.println(bankProcessSync.getRequest());
					// 处理未处理记录发送到金融机构
					String encoderJson = URLEncoder.encode(bankProcessSync.getRequest(), "utf-8");

					DefaultHttpClient httpClient = new DefaultHttpClient();
					HttpPost httpPost = new HttpPost("http://localhost:8080/etl/plat/apply/receive");
					httpPost.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);

					StringEntity se = new StringEntity(encoderJson);
					se.setContentType(CONTENT_TYPE_TEXT_JSON);
					se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON));
					httpPost.setEntity(se);
					// 发送请求
					System.out.println("发送数据s：-----------------------------");
					HttpResponse httpRes = httpClient.execute(httpPost);
					System.out.println("发送数据e：-----------------------------");
					// 输入返回信息
					String resStr = URLDecoder.decode(EntityUtils.toString(httpRes.getEntity()), "utf-8");
					System.out.println("返回结果：" + resStr);
					BusinessOperateResBean businessOperateResBean = JSON.parseObject(resStr,
							BusinessOperateResBean.class);
					// 返回处理成功消息，更改数据库状态信息
					if (null != businessOperateResBean && null != businessOperateResBean.getErrorCode()
							&& businessOperateResBean.getErrorCode() >= 0) {
						bankProcessSync.setProcessed("Y");
						bankProcessSync.setUpdated(new Date());
						bankProcessSync.setResponse(resStr);
						bankSystemService.updateBankProcessSync(bankProcessSync);
					}
				}
				System.out.println("更新状态e---------------------------------------------------------------");
			} catch (Exception e) {
				e.printStackTrace();
			}

			// 暂停一段时间
			try {
				Thread.sleep(10000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
