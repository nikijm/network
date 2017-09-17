package com.jk.ndt.etl.controller.bank;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.jk.ndt.etl.entity.bank.BankProcessSync;
import com.jk.ndt.etl.entity.vo.ApplicationResBean;
import com.jk.ndt.etl.service.bank.BankSystemService;

@Controller
@RequestMapping(value = "/bank")
public class BankSystemController {
	private Long seq = 1L;

	@Autowired
	private BankSystemService bankSystemService;

	/**
	 * 
	 * @Description: 接受业务服务器传递过来的数据
	 * @author fangwei
	 * @date 2017年6月7日 下午9:20:21
	 * @param request
	 * @param res
	 */
	@RequestMapping("/apply/receive")
	public void uploadProduct(HttpServletRequest request, HttpServletResponse res) {
		ApplicationResBean applicationResBean = new ApplicationResBean();
		applicationResBean.setDocNo("DocNo" + System.currentTimeMillis());
		applicationResBean.setErrorMsg("error message");
		applicationResBean.setRequestType("Application");
		applicationResBean.setTransactionID("");
		applicationResBean.setRequestType("Application");
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
			String resJson = JSONObject.toJSONString(applicationResBean);
			String encoderJson = URLEncoder.encode(resJson, "utf-8");

			// 解析json数据，存入数据库
			// 设置对象值
			BankProcessSync mBankProcessSync = new BankProcessSync();
			mBankProcessSync.setmBusinessProcesssyncId(System.currentTimeMillis() * 1000L + seq);
			mBankProcessSync.setRequestType("Application");
			mBankProcessSync.setCreated(new Date());
			mBankProcessSync.setNodeCode("9100");
			mBankProcessSync.setNodeName("提交申请");
			mBankProcessSync.setDocumentNo(applicationResBean.getDocNo());
			mBankProcessSync.setRequest(reqBody);
			mBankProcessSync.setResponse(resJson);
			mBankProcessSync.setCreated(new Date());
			mBankProcessSync.setProcessed("Y");
			// 序列号递增
			this.seq++;
			// 存入数据库
			bankSystemService.saveOneBankProcessSync(mBankProcessSync);

			// 发送返回信息
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

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView apply_list() {
		// 设置对象值
		ModelAndView mv = new ModelAndView("bank_list");
		List<BankProcessSync> list = bankSystemService.applyList();
		mv.addObject("list", list);
		return mv;
	}

	@RequestMapping(value = "/apply/save")
	public ModelAndView apply_detail(String nodeCode, String documentNo) {
		// 设置对象值
		ModelAndView mv = new ModelAndView("redirect:/bank/list");
		// 设置对象值
		BankProcessSync bankProcessSync = new BankProcessSync();
		bankProcessSync.setCreated(new Date());
		bankProcessSync.setNodeCode(nodeCode);
		bankProcessSync.setDocumentNo(documentNo);
		bankProcessSync.setmBusinessProcesssyncId(System.currentTimeMillis() * 1000L + seq);
		bankProcessSync.setRequestType("BusinessOperate");

		switch (nodeCode) {
		case "9200":
			bankProcessSync.setNodeName("申请受理");
			break;
		case "9300":
			bankProcessSync.setNodeName("贷前尽职调查");
			break;
		case "9400":
			bankProcessSync.setNodeName("银行初审");
			break;
		case "9500":
			bankProcessSync.setNodeName("银行复审");
			break;
		case "9600":
			bankProcessSync.setNodeName("合同签订");
			break;
		default:
			break;
		}
		bankProcessSync.setProcessed("N");

		StringBuffer request = new StringBuffer();
		request.append("{");
		request.append(" \"RequestType\": \"BusinessOperate\",");
		request.append("\"TransactionID\": \"消息交换唯一编号\",");
		request.append(" \"OrgCode\": \"bank00001\",");
		request.append("\"OrgName\": \"bank00001\",");
		request.append(" \"BusinessCode\": \"业务编码\",");
		request.append(" \"BusinessName\": \"业务名称\",");
		request.append("\"DocumentNo\": \"");
		request.append(documentNo + "\",");
		request.append("\"Workflow\": {");
		request.append("\"NodeCode\": \"");
		request.append(nodeCode + "\",");
		request.append("\"NodeName\": \"");
		request.append(bankProcessSync.getNodeName() + "\",");
		request.append(" \"OperatorCode\": \"操作人编码\",");
		request.append(" \"OperatorName\": \"操作人名字\",");
		request.append(" \"DocAction\": \"单据操作\",");
		request.append(" \"ActionDescription\": \"操作说明\",");
		request.append("\"GrantAmout\": 324321432");
		request.append(" }");
		request.append("}");
		bankProcessSync.setRequest(request.toString());
		// 序列号递增
		this.seq++;
		// 存入数据库
		bankSystemService.saveOneBankProcessSync(bankProcessSync);
		return mv;
	}
}
