package com.jk.ndtetl.controller.dbmeta;

import com.alibaba.fastjson.JSONObject;
import com.jk.ndtetl.BaseSystemEntity;
import com.jk.ndtetl.controller.BaseController;
import com.jk.ndtetl.controller.BasePage;
import com.jk.ndtetl.dbmeta.BusinessType;
import com.jk.ndtetl.dbmeta.TableDef;
import com.jk.ndtetl.dbmeta.service.IBusinessTypeService;
import com.jk.ndtetl.dbmeta.service.ITableDefService;
import com.jk.ndtetl.etl.Org;
import com.jk.ndtetl.util.Checker;
import com.jk.ndtetl.util.EHCacheUtil;
import com.jk.ndtetl.util.ErrorUtil;
import com.jk.ndtetl.util.PageData;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 
 * 组织机构文件类型管理
 * @author 朱生
 * @date 2017年5月25日 下午3:43:25
 *
 */
@Controller
@RequestMapping("/api")
public class BusinessTypeController {

	@Autowired
	IBusinessTypeService iBusinessTypeService;
	@Autowired
	ITableDefService iTableDefService;

	/**
	 * 列表
	 * @param basePage
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/businessTypes", method = RequestMethod.GET)
	@ResponseBody
	public Object listBusinessType(BasePage basePage, HttpServletRequest request) {

		JSONObject errors = new JSONObject();
		String msg = null;
		basePage.setParam(PageData.getParamMap(request));
		JSONObject rs = new JSONObject();
		rs.put(BaseSystemEntity.BASE_PAGE, basePage);
		rs.put("businessTypes", iBusinessTypeService.listByPage(basePage));
		return rs;
	}

	/**
	 * 保存
	 * @param businessType
	 * @param result
	 * @return
	 */
	@RequestMapping(value="/businessTypes",method=RequestMethod.POST)
	@ResponseBody
	public Object saveBusinessType(@RequestBody @Valid BusinessType businessType, BindingResult result) {
		JSONObject errors = new JSONObject();
		String msg=null;
//		iBusinessTypeService.listByParam()
		iBusinessTypeService.save(businessType);
		EHCacheUtil.remove(BaseController.OPTION_BUSINESS_TYPE);
		return businessType.getEtlBusinessTypeId();
	}

	/**
	 * 更新
	 * @param businessType
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/businessTypes/{id}",method=RequestMethod.PUT)
	@ResponseBody
	public Object updateBusinessType(@PathVariable("id") Integer id,@RequestBody @Valid BusinessType businessType,BindingResult result) {
		JSONObject errors = new JSONObject();
		String msg=null;

		businessType.setEtlBusinessTypeId(id);
		iBusinessTypeService.update(businessType);
		EHCacheUtil.remove(BaseController.OPTION_BUSINESS_TYPE);
		return null;
	}

	/**
	 * 删除
	 *
	 * @param id 主键
	 * @return
	 */
	@RequestMapping(value = "/businessTypes/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public Object deleteBusinessType(@PathVariable("id") Integer id) {
		JSONObject errors = new JSONObject();
		String msg=null;
		Map<String, Object> param = new HashedMap();
		param.put("businessTypeId", id);
		List<TableDef> tableDefList=iTableDefService.listByParam(param);
		if (Checker.isNotNullOrEmpty(tableDefList)) {
			errors.put("using", "该业务类型正在使用中，不能删除");
			msg = "该业务类型正在使用中，不能删除";
		}
		if (null != msg) {
			return ErrorUtil.getRequestError(errors, msg);
		}
		iBusinessTypeService.deleteById(id);
		EHCacheUtil.remove(BaseController.OPTION_BUSINESS_TYPE);
		return null;
	}
}
