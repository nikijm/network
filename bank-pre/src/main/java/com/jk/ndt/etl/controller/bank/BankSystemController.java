package com.jk.ndt.etl.controller.bank;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jk.ndt.etl.entity.bank.BankProcessSync;
import com.jk.ndt.etl.entity.vo.ApplicationResBean;
import com.jk.ndt.etl.entity.vo.application.ApplicationBean;
import com.jk.ndt.etl.entity.vo.businessoperate.BusinessOperateBean;
import com.jk.ndt.etl.entity.vo.businessoperate.BusinessOperateResBean;
import com.jk.ndt.etl.service.bank.BankSystemService;
import com.jk.ndt.etl.utility.DateUtils;

@Controller
@RequestMapping(value = "/bank")
public class BankSystemController {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	private static Long seq = 1L;

	@Autowired
	private BankSystemService bankSystemService;

	/**
	 * 
	 * @Description: 接收农贷通平台传递过来的数据
	 * @author fangwei
	 * @date 2017年6月7日 下午9:20:21
	 * @param request
	 * @param res
	 */
	@RequestMapping("/ntd/receive")
	public void receiveNdtData(HttpServletRequest request, HttpServletResponse res) {
		try {
			// 读取请求内容
			BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
			String line = null;
			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			// 将资料解码，已经通过的过滤器进行utf-8处理
			String reqBody = sb.toString();
			System.out.println("接收数据：");
			System.out.println("----------------------------");
			System.out.println(reqBody);
			System.out.println("----------------------------");

			// 将json转化为Map对象，取头固定字段进行判断处理
			Map dataMap = JSON.parseObject(reqBody, HashMap.class);
			if ("Apply".equals(dataMap.get("RequestType").toString())) {
				receiveNtdApplicationData(reqBody, res);
				return;
			}

			try {
				res.getWriter().write("-1");
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 返回错误字符串"-1"
			try {
				res.getWriter().write("-1");
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

	}

	/**
	 * 接收农贷通平台Application数据
	 * 
	 * @param request
	 * @param res
	 */
	private void receiveNtdApplicationData(String reqJson, HttpServletResponse res) {
		ApplicationResBean applicationResBean = new ApplicationResBean();
		applicationResBean.setDocNo("DocNo" + System.currentTimeMillis());
		applicationResBean.setErrorMsg("error message");
		applicationResBean.setRequestType("Apply");
		try {
			// 转化json字符串为对应对象
			ApplicationBean applicationBean = JSON.parseObject(reqJson, ApplicationBean.class);

			applicationResBean.setTransactionID(applicationBean.getTransactionID());
			applicationResBean.setErrorCode(0);
			String resJson = JSONObject.toJSONString(applicationResBean);

			// 解析json数据，存入数据库
			// 设置对象值
			BankProcessSync mBankProcessSync = new BankProcessSync();
			mBankProcessSync.setmBusinessProcesssyncId(System.currentTimeMillis() * 1000L + seq);
			mBankProcessSync.setRequestType("Apply");
			mBankProcessSync.setTransactionId(applicationBean.getTransactionID());
			mBankProcessSync.setCreated(new Date());
			mBankProcessSync.setNodeCode("9100");
			mBankProcessSync.setNodeName("提交申请");
			mBankProcessSync.setDocumentNo(applicationResBean.getDocNo());
			mBankProcessSync.setRequest(reqJson);
			mBankProcessSync.setResponse(resJson);
			mBankProcessSync.setCreated(new Date());
			mBankProcessSync.setProcessed("N");
			// 序列号递增
			BankSystemController.seq++;
			// 存入数据库
			bankSystemService.saveOneBankProcessSync(mBankProcessSync);

			// 发送返回信息
			res.getWriter().write(resJson);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				applicationResBean.setErrorCode(-1);
				String resJson = JSONObject.toJSONString(applicationResBean);
				res.getWriter().write(resJson);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

	}

	/**
	 * 
	 * @Description: 接受金融机构前置机传递过来的数据(需要根据金融机构编码和操作类型进行不同分发处理)
	 *               先解析动作类型，再解析编码对象具体金融机构
	 * @author fangwei
	 * @date 2017年6月7日 下午9:20:21
	 * @param request
	 * @param res
	 */
	@RequestMapping("/finance/receive")
	public void receiveFinanceData(HttpServletRequest request, HttpServletResponse res) {
		try {
			// 读取请求内容
			BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
			String line = null;
			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			// 将资料解码，已经通过的过滤器进行utf-8处理
			String reqBody = sb.toString();
			System.out.println("接收数据：");
			System.out.println("----------------------------");
			System.out.println(reqBody);
			System.out.println("----------------------------");
			// 将json转化为Map对象，取头固定字段进行判断处理
			Map dataMap = JSON.parseObject(reqBody, HashMap.class);
			if ("BusinessOperate".equals(dataMap.get("RequestType").toString())) {
				receiveFinanceBusinessOperateData(reqBody, res);
				return;
			}

			try {
				res.getWriter().write("-1");
			} catch (Exception e2) {
				e2.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
			// 返回错误字符串"-1"
			try {
				res.getWriter().write("-1");
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	/**
	 * 接收金融机构BusinessOperate数据(该步骤暂时认为各个金融机构处理一致)
	 * 
	 * @param request
	 * @param res
	 */
	private void receiveFinanceBusinessOperateData(String reqJson, HttpServletResponse res) {
		BusinessOperateResBean businessOperateResBean = new BusinessOperateResBean();
		businessOperateResBean.setErrorMsg("error message");
		businessOperateResBean.setRequestType("BusinessOperate");
		try {

			// 转化json字符串为对应对象
			BusinessOperateBean businessOperateBean = JSON.parseObject(reqJson, BusinessOperateBean.class);

			// 返回信息
			businessOperateResBean.setErrorCode(0);
			businessOperateResBean.setTransactionID(businessOperateBean.getTransactionID());
			String resJson = JSONObject.toJSONString(businessOperateResBean);

			// 解析json数据，存入数据库
			// 设置对象值
			BankProcessSync bankProcessSync = new BankProcessSync();
			bankProcessSync.setmBusinessProcesssyncId(System.currentTimeMillis() * 1000L + seq);
			bankProcessSync.setRequestType("BusinessOperate");
			bankProcessSync.setTransactionId(businessOperateBean.getTransactionID());
			bankProcessSync.setCreated(new Date());
			bankProcessSync.setNodeCode(businessOperateBean.getWorkflow().getNodeCode());
			bankProcessSync.setNodeName(businessOperateBean.getWorkflow().getNodeName());
			bankProcessSync.setDocumentNo(businessOperateBean.getDocumentNo());
			bankProcessSync.setRequest(reqJson);
			bankProcessSync.setResponse(resJson);
			bankProcessSync.setProcessed("N");
			bankProcessSync.setOperatorCode(businessOperateBean.getWorkflow().getOperatorCode());
			bankProcessSync.setOperatorName(businessOperateBean.getWorkflow().getOperatorName());
			bankProcessSync.setDocAction(businessOperateBean.getWorkflow().getDocAction());
			bankProcessSync.setActionDescription(businessOperateBean.getWorkflow().getActionDescription());

			// 序列号递增
			BankSystemController.seq++;
			// 存入数据库
			bankSystemService.saveOneBankProcessSync(bankProcessSync);

			// 发送返回信息
			res.getWriter().write(resJson);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				businessOperateResBean.setErrorCode(-1);
				String resJson = JSONObject.toJSONString(businessOperateResBean);
				res.getWriter().write(resJson);
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
		String transactionID = System.currentTimeMillis() + "";

		StringBuffer request = new StringBuffer();
		request.append("{");
		request.append(" \"RequestType\": \"BusinessOperate\",");
		request.append("\"TransactionID\": \"" + transactionID + "\",");
		request.append("\"ReqestTime\": \"" + DateUtils.format(new Date(), DateUtils.FOMTER_TIMES) + "\",");
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
		request.append(" \"DocAction\": \"AP\",");
		request.append(" \"ActionDescription\": \"操作说明\",");
		request.append("\"GrantAmout\": 324321432");
		request.append(" }");
		request.append("}");
		bankProcessSync.setRequest(request.toString());
		bankProcessSync.setTransactionId(transactionID);
		// 序列号递增
		this.seq++;
		// 存入数据库
		bankSystemService.saveOneBankProcessSync(bankProcessSync);
		return mv;
	}
}
