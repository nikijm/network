package com.jk.ndt.etl.controller.presystem;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jk.ndt.etl.entity.presystem.BusinessProcessSync;
import com.jk.ndt.etl.entity.presystem.BusinessSync;
import com.jk.ndt.etl.entity.vo.ApplyCreditVo;
import com.jk.ndt.etl.entity.vo.application.ApplicationBean;
import com.jk.ndt.etl.entity.vo.application.AttnBean;
import com.jk.ndt.etl.entity.vo.application.AttributeBean;
import com.jk.ndt.etl.entity.vo.application.BPartnerBean;
import com.jk.ndt.etl.entity.vo.application.BusinessBean;
import com.jk.ndt.etl.entity.vo.businessoperate.BusinessOperateBean;
import com.jk.ndt.etl.entity.vo.businessoperate.BusinessOperateResBean;
import com.jk.ndt.etl.service.presystem.PreSystemService;

@Controller
@RequestMapping(value = "/plat")
public class PreSystemController {
	private static Long seq = 1L;

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
	public String applyCreditInit(Model model) {
		List<BusinessSync> businessList = preSystemService.getBusinessSyncList();
		model.addAttribute("businessList", businessList);
		return "applycredit";
	}

	@RequestMapping(value = "/apply/credit/add", method = RequestMethod.POST)
	public String insert(ApplyCreditVo applyCreditVo, Model model) {
		String transactionId = System.currentTimeMillis() + "";
		// 查询业务配置表
		BusinessSync businessSync = preSystemService.getBusinessSyncById(applyCreditVo.getmBusinesssyncId());
		// 设置对象值
		BusinessProcessSync mBusinessProcessSync = new BusinessProcessSync();
		mBusinessProcessSync.setmBusinessProcesssyncId(System.currentTimeMillis() * 1000L + seq);
		mBusinessProcessSync.setProcessed("N");
		mBusinessProcessSync.setmBusinesssyncId(applyCreditVo.getmBusinesssyncId());
		mBusinessProcessSync.setRecordId(applyCreditVo.getRecordId());
		mBusinessProcessSync.setOperatorCode(applyCreditVo.getOperatorCode());
		mBusinessProcessSync.setOperatorName(applyCreditVo.getOperatorName());
		mBusinessProcessSync.setActionDescription(applyCreditVo.getActionDescription());

		ApplicationBean applicationBean = new ApplicationBean();
		applicationBean.setRequestType("Apply");
		applicationBean.setTransactionID(transactionId);
		applicationBean.setOrgCode(businessSync.getOrgCode());
		applicationBean.setOrgName(businessSync.getOrgName());

		BPartnerBean bPartnerBean = new BPartnerBean();
		bPartnerBean.setbPCode(applyCreditVo.getbPCode());
		bPartnerBean.setbPName(applyCreditVo.getbPName());
		bPartnerBean.setAddress(applyCreditVo.getAddress());

		AttnBean attnBean = new AttnBean();
		attnBean.setiD(applyCreditVo.getApplyUserID());
		attnBean.setName(applyCreditVo.getApplyUserName());
		attnBean.setPhone(applyCreditVo.getApplyUserPhone());

		bPartnerBean.setAttn(attnBean);

		BusinessBean businessBean = new BusinessBean();
		businessBean.setBusinessCode(businessSync.getBusinessCode());
		businessBean.setBusinessName(businessSync.getBusinessName());

		AttributeBean attributeBean = new AttributeBean();
		attributeBean.setMaxAmt(Long.valueOf(applyCreditVo.getAmount()));
		attributeBean.setAnnualRate(applyCreditVo.getAnnualRate());
		attributeBean.setAssetCodes(applyCreditVo.getAssetCodes());
		attributeBean.setAssetNames(applyCreditVo.getAssetNames());
		attributeBean.setCorpusPayMethod(applyCreditVo.getCorpusPayMethod());
		attributeBean.setFinancingMode(applyCreditVo.getFinancingMode());
		attributeBean.setInvestmentIndustry(applyCreditVo.getInvestmentIndustry());
		attributeBean.setProjectDoc(applyCreditVo.getProjectDoc());
		attributeBean.setProjectType(applyCreditVo.getProjectType());
		attributeBean.setTermMonth(applyCreditVo.getTermMonth());

		businessBean.setAttribute(attributeBean);

		applicationBean.setbPartner(bPartnerBean);
		applicationBean.setBusiness(businessBean);

		mBusinessProcessSync.setRequest(JSONObject.toJSONString(applicationBean));
		mBusinessProcessSync.setRequestType("Apply");
		mBusinessProcessSync.setTransactionId(transactionId);
		mBusinessProcessSync.setCreated(new Date());
		mBusinessProcessSync.setNodeCode("9100");
		mBusinessProcessSync.setNodeName("提交申请");
		// 序列号递增
		PreSystemController.seq++;
		// 存入数据库
		preSystemService.saveOneMBusinessProcessSync(mBusinessProcessSync);
		return "redirect:/plat/list";
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView apply_list() {
		// 设置对象值
		ModelAndView mv = new ModelAndView("apply_list");
		List<BusinessProcessSync> list = preSystemService.applyList();
		mv.addObject("list", list);
		return mv;
	}

	@RequestMapping(value = "/apply/detail", method = RequestMethod.GET)
	public ModelAndView apply_detail(String documentNo) {
		// 设置对象值
		ModelAndView mv = new ModelAndView("apply_detail");
		List<BusinessProcessSync> list = preSystemService.listByDocumentNo(documentNo);
		mv.addObject("list", list);
		return mv;
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
			BusinessProcessSync businessProcessSync = new BusinessProcessSync();
			businessProcessSync.setmBusinessProcesssyncId(System.currentTimeMillis() * 1000L + seq);
			businessProcessSync.setRequestType("BusinessOperate");
			businessProcessSync.setTransactionId(businessOperateBean.getTransactionID());
			businessProcessSync.setCreated(new Date());
			businessProcessSync.setNodeCode(businessOperateBean.getWorkflow().getNodeCode());
			businessProcessSync.setNodeName(businessOperateBean.getWorkflow().getNodeName());
			businessProcessSync.setDocumentNo(businessOperateBean.getDocumentNo());
			businessProcessSync.setRequest(reqJson);
			businessProcessSync.setResponse(resJson);
			businessProcessSync.setProcessed("Y");
			businessProcessSync.setOperatorCode(businessOperateBean.getWorkflow().getOperatorCode());
			businessProcessSync.setOperatorName(businessOperateBean.getWorkflow().getOperatorName());
			businessProcessSync.setDocAction(businessOperateBean.getWorkflow().getDocAction());
			businessProcessSync.setActionDescription(businessOperateBean.getWorkflow().getActionDescription());

			// 序列号递增
			PreSystemController.seq++;
			// 存入数据库
			preSystemService.saveOneMBusinessProcessSync(businessProcessSync);

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
}
