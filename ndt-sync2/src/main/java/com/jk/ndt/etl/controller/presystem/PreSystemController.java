package com.jk.ndt.etl.controller.presystem;

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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jk.ndt.etl.entity.presystem.BusinessProcessSync;
import com.jk.ndt.etl.entity.vo.ApplyCreditVo;
import com.jk.ndt.etl.entity.vo.businessoperate.BusinessOperateBean;
import com.jk.ndt.etl.entity.vo.businessoperate.BusinessOperateResBean;
import com.jk.ndt.etl.service.presystem.PreSystemService;
import com.jk.ndt.etl.utility.Checker;

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
		BusinessProcessSync mBusinessProcessSync = new BusinessProcessSync();
		mBusinessProcessSync.setmBusinessProcesssyncId(System.currentTimeMillis() * 1000L + seq);
		mBusinessProcessSync.setProcessed("N");
		mBusinessProcessSync.setmBusinesssyncId(applyCreditVo.getmBusinesssyncId());
		mBusinessProcessSync.setRecordId(applyCreditVo.getRecordId());
		mBusinessProcessSync.setOperatorCode(applyCreditVo.getOperatorCode());
		mBusinessProcessSync.setOperatorName(applyCreditVo.getOperatorName());
		mBusinessProcessSync.setActionDescription(applyCreditVo.getActionDescription());
		StringBuffer request = new StringBuffer();
		request.append("{");
		request.append(" \"RequestType\": \"Application\",");
		request.append("\"TransactionID\": \"消息交换唯一编号\",");
		request.append("\"OrgCode\": \"bank00001\",");
		request.append("\"OrgName\": \"bank00001\",");
		request.append("\"BPartner\": {");
		request.append(" \"BPCode\": \"\",");
		request.append("\"BPName\": \"\",");
		request.append(" \"Address\": \"区县乡镇村社区\",");
		request.append("\"Attn\": {");
		request.append(" \"ID\": \"5111221978110961987\",");
		request.append(" \"Phone\": \"13981875275\"");
		request.append("  },");
		request.append("\"Supervisor\": {");
		request.append(" \"ID\": \"51112219722310961953\",");
		request.append("\"Phone\": \"13981875275\"");
		request.append(" }\"Legal\": {");
		request.append("\"ID\": \"5111221972110961913\",");
		request.append("\"Phone\": \"13981875275\"");
		request.append(" }");
		request.append(" },");
		request.append("\"Business\": {");
		request.append("\"BusinessCode\": \"业务编码\",");
		request.append("\"BusinessName\": \"业务名称\",");
		request.append(" \"Attribute\": {");
		request.append(" \"FinancingMode\": \"融资方式（CreditLoad,MortgageLoad,PledgeLoad,EquityFinancing）\",");
		request.append("\"AnnualRate\": \"贷款年利率\",");
		request.append("\"InvestmentIndustry\": \"投资行业()\",");
		request.append("\"ProjectType\": \"项目类型\",");
		request.append(" \"MinAmt\": 1000,");
		request.append("\"MaxAmt\": 99999999999,");
		request.append("\"AssetCodes\": \"多种抵质押资产编码\",");
		request.append("\"AssetNames\": \"抵质押资产名称\",");
		request.append("\"ProjectDoc\": \"项目材料\"");
		request.append("}}}");

		mBusinessProcessSync.setRequest(request.toString());
		mBusinessProcessSync.setRequestType("Application");
		mBusinessProcessSync.setCreated(new Date());
		mBusinessProcessSync.setNodeCode("9100");
		mBusinessProcessSync.setNodeName("提交申请");
		// 序列号递增
		this.seq++;
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
		if (Checker.isNotNullOrEmpty(documentNo)) {
			List<BusinessProcessSync> list = preSystemService.listByDocumentNo(documentNo);
			mv.addObject("list", list);
		}
		return mv;
	}

	/**
	 * 
	 * @Description: 接受业务服务器传递过来的数据
	 * @author fangwei
	 * @date 2017年6月7日 下午9:20:21
	 * @param request
	 * @param res
	 */
	@RequestMapping("/apply/receive")
	public void BusinessOperate(HttpServletRequest request, HttpServletResponse res) {
		BusinessOperateResBean businessOperateResBean = new BusinessOperateResBean();
		businessOperateResBean.setErrorMsg("error message");
		businessOperateResBean.setRequestType("Application");
		businessOperateResBean.setTransactionID("");
		businessOperateResBean.setRequestType("BusinessOperate");
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

			BusinessOperateBean businessOperateBean = JSON.parseObject(reqBody, BusinessOperateBean.class);

			// 返回信息
			businessOperateResBean.setErrorCode(0);
			String resJson = JSONObject.toJSONString(businessOperateResBean);
			String encoderJson = URLEncoder.encode(resJson, "utf-8");

			// 解析json数据，存入数据库
			// 设置对象值
			BusinessProcessSync businessProcessSync = new BusinessProcessSync();
			businessProcessSync.setmBusinessProcesssyncId(System.currentTimeMillis() * 1000L + seq);
			businessProcessSync.setRequestType("Application");
			businessProcessSync.setCreated(new Date());
			businessProcessSync.setNodeCode(businessOperateBean.getWorkflow().getNodeCode());
			businessProcessSync.setNodeName(businessOperateBean.getWorkflow().getNodeName());
			businessProcessSync.setDocumentNo(businessOperateBean.getDocumentNo());
			businessProcessSync.setRequest(reqBody);
			businessProcessSync.setResponse(resJson);
			businessProcessSync.setProcessed("Y");
			// 序列号递增
			this.seq++;
			// 存入数据库
			preSystemService.saveOneMBusinessProcessSync(businessProcessSync);

			// 发送返回信息
			res.getWriter().write(encoderJson);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				businessOperateResBean.setErrorCode(-1);
				String encoderJson = URLEncoder.encode(JSONObject.toJSONString(businessOperateResBean), "utf-8");
				res.getWriter().write(encoderJson);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

	}
}
