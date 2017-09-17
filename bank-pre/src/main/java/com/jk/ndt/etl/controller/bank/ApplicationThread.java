package com.jk.ndt.etl.controller.bank;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.Properties;

import com.alibaba.fastjson.JSON;
import com.jk.ndt.etl.entity.bank.BankProcessSync;
import com.jk.ndt.etl.entity.vo.businessoperate.BusinessOperateResBean;
import com.jk.ndt.etl.service.bank.BankSystemService;
import com.jk.ndt.etl.utility.SpringContextHolder;

public class ApplicationThread extends Thread {
	private String getSendUrl() {
		String sendUrl = null;

		InputStream inpStream = null;
		try {
			String path = this.getClass().getResource("/").getPath() + "setting.properties";
			Properties prop = new Properties();
			inpStream = new FileInputStream(new File(path));
			prop.load(inpStream);
			sendUrl = prop.getProperty("bank.business.system.url");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != inpStream) {
				try {
					inpStream.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
		return sendUrl;
	}

	public void run() {
		// 获取发送的url地址
		String sendUrl = getSendUrl();

		// 获取service对象
		BankSystemService bankSystemService = null;
		// 后台监控数据库处理线程，需要一直运行
		while (true) {
			if (null == sendUrl) {
				System.out.println("没有配置url地址");
				return;
			}

			if (null == bankSystemService) {
				// 获取service对象
				bankSystemService = SpringContextHolder.getBean("bankSystemService");
			}

			BankProcessSync bankProcessSync = null;

			try {

				// 获取第一个未处理记录进行处理
				bankProcessSync = bankSystemService.getNTopOneUnProcessedApplication();
				System.out.println("---------------------------------------------------------------");
				System.out.println(bankProcessSync);
				if (null != bankProcessSync) {
					// 获取需要发送的json数据
					String reqJson = bankProcessSync.getRequest();
					System.out.println(reqJson);

					// 先更改状态为已处理，出异常再修改为未处理
					bankProcessSync.setProcessed("Y");
					bankProcessSync.setUpdated(new Date());
					bankSystemService.updateBankProcessSync(bankProcessSync);

					// 处理未处理记录发送到银行业务系统
					URL url = new URL(sendUrl);
					URLConnection con = url.openConnection();
					con.setDoOutput(true);
					// con.setRequestProperty("Pragma:", "no-cache");
					con.setRequestProperty("Cache-Control", "no-cache");
					con.setRequestProperty("Content-Type", "text/html");
					OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream(), "utf-8");
					System.out.println("客户端发出的报文：" + reqJson);
					out.write(reqJson);
					out.flush();
					out.close();

					// 接收返回信息
					BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
					String line = "";
					StringBuffer buf = new StringBuffer();
					while ((line = br.readLine()) != null) {
						buf.append(line);
					}
					String resStr = buf.toString();
					System.out.println("客户端收到的回复：" + resStr);

					BusinessOperateResBean businessOperateResBean = JSON.parseObject(resStr,
							BusinessOperateResBean.class);
					// 返回处理成功消息，更改数据库状态信息
					if (null != businessOperateResBean && null != businessOperateResBean.getErrorCode()
							&& businessOperateResBean.getErrorCode() >= 0) {
						bankProcessSync.setProcessed("Y");
						bankProcessSync.setUpdated(new Date());
						bankProcessSync.setResponse(resStr);
						bankSystemService.updateBankProcessSync(bankProcessSync);
					} else {
						bankProcessSync.setProcessed("N");
						bankProcessSync.setUpdated(new Date());
						bankSystemService.updateBankProcessSync(bankProcessSync);
					}

				}
				System.out.println("更新状态e---------------------------------------------------------------");
			} catch (Exception e) {
				try {
					// 出异常再修改为未处理
					if (null != bankProcessSync) {
						bankProcessSync.setProcessed("N");
						bankProcessSync.setUpdated(new Date());
						bankSystemService.updateBankProcessSync(bankProcessSync);
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}
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
