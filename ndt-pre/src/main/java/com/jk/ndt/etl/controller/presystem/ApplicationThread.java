package com.jk.ndt.etl.controller.presystem;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

import com.alibaba.fastjson.JSON;
import com.jk.ndt.etl.entity.presystem.BusinessProcessSync;
import com.jk.ndt.etl.entity.presystem.BusinessSync;
import com.jk.ndt.etl.entity.vo.ApplicationResBean;
import com.jk.ndt.etl.service.presystem.PreSystemService;
import com.jk.ndt.etl.utility.SpringContextHolder;

public class ApplicationThread extends Thread {

	public void run() {

		// 获取发送的url地址
		String sendUrl = "";
		PreSystemService preSystemService = null;
		// 后台监控数据库处理线程，需要一直运行
		while (true) {
			if (null == preSystemService) {
				// 获取service对象
				preSystemService = SpringContextHolder.getBean("preSystemService");
			}
			BusinessProcessSync mBusinessProcessSync = null;

			try {
				// 获取第一个未处理记录进行处理
				mBusinessProcessSync = preSystemService.getNTopOneUnProcessedApplicationM();
				System.out.println("---------------------------------------------------------------");
				System.out.println(mBusinessProcessSync);
				if (null != mBusinessProcessSync) {

					// 获取配置信息
					BusinessSync businessSync = preSystemService
							.getBusinessSyncById(mBusinessProcessSync.getmBusinesssyncId());
					// 获取配置的对象服务url
					sendUrl = businessSync.getServiceUrl();

					// 获取需要发送的json数据
					String reqJson = mBusinessProcessSync.getRequest();
					System.out.println(reqJson);

					// 先更改状态为已处理，出异常再修改为未处理
					mBusinessProcessSync.setProcessed("Y");
					mBusinessProcessSync.setUpdated(new Date());
					preSystemService.updateMBusinessProcessSync(mBusinessProcessSync);

					// 处理未处理记录发送到金融机构
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

					ApplicationResBean applicationResBean = JSON.parseObject(resStr, ApplicationResBean.class);
					// 返回处理成功消息，更改数据库状态信息
					if (null != applicationResBean && null != applicationResBean.getErrorCode()
							&& applicationResBean.getErrorCode() >= 0) {
						mBusinessProcessSync.setProcessed("Y");
						mBusinessProcessSync.setUpdated(new Date());
						mBusinessProcessSync.setDocumentNo(applicationResBean.getDocNo());
						mBusinessProcessSync.setResponse(resStr);
						preSystemService.updateMBusinessProcessSync(mBusinessProcessSync);
					}else{
						mBusinessProcessSync.setProcessed("N");
						mBusinessProcessSync.setUpdated(new Date());
						preSystemService.updateMBusinessProcessSync(mBusinessProcessSync);
					}
				}
				System.out.println("更新状态e---------------------------------------------------------------");
			} catch (Exception e) {
				try {
					// 出异常再修改为未处理
					if (null != mBusinessProcessSync) {
						mBusinessProcessSync.setProcessed("N");
						mBusinessProcessSync.setUpdated(new Date());
						preSystemService.updateMBusinessProcessSync(mBusinessProcessSync);
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
