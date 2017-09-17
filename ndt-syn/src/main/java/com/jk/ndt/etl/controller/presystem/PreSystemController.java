package com.jk.ndt.etl.controller.presystem;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;
import com.jk.ndt.etl.entity.presystem.MBusinessProcessSync;
import com.jk.ndt.etl.entity.vo.ApplyCreditVo;
import com.jk.ndt.etl.entity.vo.application.ApplicationResBean;
import com.jk.ndt.etl.service.presystem.PreSystemService;

@Controller
@RequestMapping(value = "/plat")
public class PreSystemController {
	private Long seq = 1L;

	@Autowired
	PreSystemService preSystemService;

	/**
	 * 查询所有申请贷款信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/home")
	public String home() {
		return "home";
	}

	/**
	 * 转到贷款申请页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/apply/credit")
	public String applyCreditInit() {
		return "applycredit";
	}

	@RequestMapping(value = "/apply/credit/add", method = RequestMethod.POST)
	public String insert(ApplyCreditVo applyCreditVo, Model model) {
		// 设置对象值
		MBusinessProcessSync mBusinessProcessSync = new MBusinessProcessSync();
		mBusinessProcessSync.setmBusinessProcesssyncId(System.currentTimeMillis() * 1000L + seq);
		mBusinessProcessSync.setProcessed("N");
		mBusinessProcessSync.setmBusinesssyncId(applyCreditVo.getmBusinesssyncId());
		mBusinessProcessSync.setRecordId(applyCreditVo.getRecordId());
		mBusinessProcessSync.setOperatorCode(applyCreditVo.getOperatorCode());
		mBusinessProcessSync.setOperatorName(applyCreditVo.getOperatorName());
		mBusinessProcessSync.setActionDescription(applyCreditVo.getActionDescription());
		mBusinessProcessSync.setRequest(applyCreditVo.getRequest());
		mBusinessProcessSync.setRequestType("Application");
		mBusinessProcessSync.setCreated(new Date());
		mBusinessProcessSync.setNodeCode("9100");
		// 代表农贷通存入数据
		mBusinessProcessSync.setDocStatus("1");
		mBusinessProcessSync.setNodeName("提交申请");
		// 序列号递增
		this.seq++;
		// 存入数据库
		preSystemService.saveOneMBusinessProcessSync(mBusinessProcessSync);
		model.addAttribute("result", "success");
		return "result";
	}

	@RequestMapping("/jinrong/apply/receive")
	public void uploadProduct(HttpServletRequest request, HttpServletResponse res) {

		ApplicationResBean applicationResBean = new ApplicationResBean();
		applicationResBean.setDocNo("DocNo" + System.currentTimeMillis());
		applicationResBean.setErrorMsg("error message");
		applicationResBean.setRequestType("Application");
		applicationResBean.setTransactionID("");
		try {
			// 读取请求内容
			BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
			String line = null;
			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			// 将资料解码
			String reqBody = sb.toString();
			reqBody = URLDecoder.decode(reqBody, "utf-8");
			System.out.println("接收数据：");
			System.out.println("----------------------------");
			System.out.println(reqBody);
			System.out.println("----------------------------");

			// 返回信息
			applicationResBean.setErrorCode(0);
			String encoderJson = URLEncoder.encode(JSONObject.toJSONString(applicationResBean), "utf-8");

			// 解析json数据，存入数据库
			// 设置对象值
			MBusinessProcessSync mBusinessProcessSync = new MBusinessProcessSync();
			mBusinessProcessSync.setmBusinessProcesssyncId(System.currentTimeMillis() * 1000L + seq);
			mBusinessProcessSync.setRequestType("Application");
			mBusinessProcessSync.setCreated(new Date());
			mBusinessProcessSync.setNodeCode("9100");
			// 代表金融机构存入数据
			mBusinessProcessSync.setDocStatus("2");
			mBusinessProcessSync.setNodeName("提交申请");
			mBusinessProcessSync.setDocumentNo(applicationResBean.getDocNo());
			mBusinessProcessSync.setRequest(reqBody);
			mBusinessProcessSync.setResponse(encoderJson);
			mBusinessProcessSync.setCreated(new Date());
			mBusinessProcessSync.setProcessed("N");
			// 序列号递增
			this.seq++;
			// 存入数据库
			preSystemService.saveOneMBusinessProcessSync(mBusinessProcessSync);

			//发送返回信息
			res.getWriter().write(encoderJson);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				applicationResBean.setErrorCode(-1);
				String encoderJson = URLEncoder.encode(JSONObject.toJSONString(applicationResBean), "utf-8");
				res.getWriter().write(encoderJson);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

	}
}
