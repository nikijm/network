package com.jk.ndtetl.controller.dbmeta;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jk.ndtetl.BaseSystemEntity;
import com.jk.ndtetl.Constant;
import com.jk.ndtetl.controller.BaseController;
import com.jk.ndtetl.controller.BasePage;
import com.jk.ndtetl.dbmeta.ColumnDef;
import com.jk.ndtetl.dbmeta.DataFile;
import com.jk.ndtetl.dbmeta.DataFileType;
import com.jk.ndtetl.dbmeta.TableDef;
import com.jk.ndtetl.dbmeta.service.IBusinessTypeService;
import com.jk.ndtetl.dbmeta.service.IDataFileService;
import com.jk.ndtetl.dbmeta.service.IDataFileTypeService;
import com.jk.ndtetl.dbmeta.service.ITableDefService;
import com.jk.ndtetl.etl.EtlOp;
import com.jk.ndtetl.etl.Org;
import com.jk.ndtetl.mon.AlarmRuleBean;
import com.jk.ndtetl.mon.service.AlarmRuleService;
import com.jk.ndtetl.system.User;
import com.jk.ndtetl.system.service.IOrganizationService;
import com.jk.ndtetl.util.*;
import com.jk.ndtetl.util.promission.LoginSessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sun.rmi.server.InactiveGroupException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
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
public class DataFileTypeController extends BaseController{

	@Autowired
	IDataFileTypeService iDataFileTypeService;
	@Autowired
	IDataFileService iDataFileService;
	@Autowired
	IOrganizationService iOrganizationService;
	@Autowired
	ITableDefService iTableDefService;
	@Autowired
	IBusinessTypeService iBusinessTypeService;

    /**
     * 创建缺省
     * @param basePage
     * @param id 表id
     * @param request
     * @return
     */
	@RequestMapping(value="/createDefaultFieldsAlign/{id}",method=RequestMethod.GET)
	@ResponseBody
	public Object createDataFileType(BasePage basePage,@PathVariable("id") Integer id, HttpServletRequest request) {
		JSONObject errors = new JSONObject();
		JSONObject rs = new JSONObject();
		String msg=null;

		TableDef tableDef=iTableDefService.getById(id);
		List<ColumnDef> columnDefList =null;
		if (null == tableDef) {
			errors.put("tableNotExist", "不存在该表");
			msg = "不存在该表";
		}else {
			columnDefList = tableDef.getColumns();
			if (Checker.isNullOrEmpty(tableDef.getColumns())) {
				errors.put("ColumnsNotExist", "该表不存在字段");
				msg = "该表不存在字段";
			}
		}
		if (Checker.isNotNullOrEmpty(errors)) {
			return ErrorUtil.getRequestError(errors, msg);
		}

//		StringBuffer ss=new StringBuffer();
//		ss.append("{");
		for (int i=0;i<columnDefList.size();i++) {
			ColumnDef columnDef = columnDefList.get(i);
			if (Checker.isNotNullOrEmpty(columnDef.getName())) {
				rs.put(i+"", columnDef.getColumnName());
			}
//			ColumnDef columnDef = columnDefList.get(i);
//			if (i < columnDefList.size()-1) {
//				ss.append("'"+i+"':" +"'"+ columnDef.getColumnName()+"',");
//			}else {
//				ss.append("'"+(i+1)+"':" +"'"+ columnDef.getColumnName()+"'");
//			}
		}
//		ss.append("}");
//		return ss.toString().replace("'","\"");
		JSONObject result = new JSONObject();
		result.put("defaultFieldsAlign", rs.toJSONString());
		return result;
	}

	/**
	 * 列表
	 * @param basePage
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/dataFileTypes", method = RequestMethod.GET)
	@ResponseBody
	public Object listDataFileType(BasePage basePage, HttpServletRequest request) {

		JSONObject errors = new JSONObject();
		String msg = null;
		basePage.setParam(PageData.getParamMap(request));
		JSONObject rs = new JSONObject();
		rs.put(BaseSystemEntity.BASE_PAGE, basePage);
		rs.put("dataFileTypes", assembleResult(iDataFileTypeService.listByPage(basePage)));
		Org org=new Org();
		org.setIsActive('Y');
		rs.put("orgs", listOrgOptions(iOrganizationService,null));

		Map<String, Object> param = new HashMap<>();
		param.put("category", TableDef.CATEGORY_CACHE);
		rs.put("cacheTables", iTableDefService.listByParam(param));
		param.clear();
		param.put("category", TableDef.CATEGORY_CLEAN);
		rs.put("cleanTables", iTableDefService.listByParam(param));
		param.clear();
		param.put("category", TableDef.CATEGORY_CONVERT);
		rs.put("convertTables", iTableDefService.listByParam(param));
		param.clear();
		param.put("category", TableDef.CATEGORY_VERIFY);
		rs.put("validateTables", iTableDefService.listByParam(param));
//		rs.put("businessTypes", iBusinessTypeService.listAll());
		return rs;
	}

	/**
	 * 组装返回结果
	 * @param dataFileTypes
	 * @return
	 */
	public Object assembleResult(List<DataFileType> dataFileTypes) {
		JSONArray jsonArray=new JSONArray();
		if (Checker.isNotNullOrEmpty(dataFileTypes)) {
			for (DataFileType dataFileType:dataFileTypes) {
				JSONObject fileTypeObject = new JSONObject();
				fileTypeObject.put("etlFileTypeId", dataFileType.getEtlFileTypeId());
				fileTypeObject.put("description", dataFileType.getDescription());
				fileTypeObject.put("fieldsAlign", dataFileType.getFieldsAlign());
				fileTypeObject.put("isActive", dataFileType.getIsActive());
				fileTypeObject.put("name", dataFileType.getName());

				fileTypeObject.put("etlOrgId", "");
				fileTypeObject.put("orgName", "");
				if (null != dataFileType.getOrg()) {
					fileTypeObject.put("etlOrgId", dataFileType.getOrg().getOrgId());
					fileTypeObject.put("orgName", dataFileType.getOrg().getName());
				}
//				fileTypeObject.put("etlBusinessTypeId", "");
//				fileTypeObject.put("businessTypeName", "");
//				if (null != dataFileType.getBusinessType()) {
//					fileTypeObject.put("etlBusinessTypeId", dataFileType.getBusinessType().getEtlBusinessTypeId());
//					fileTypeObject.put("businessTypeName", dataFileType.getBusinessType().getName());
//				}

				fileTypeObject.put("tableCacheId", "");
				fileTypeObject.put("tableCacheName", "");
				if (null != dataFileType.getTableCache()) {
					fileTypeObject.put("tableCacheId", dataFileType.getTableCache().getEtlTableId());
					fileTypeObject.put("tableCacheName", dataFileType.getTableCache().getTableName());
				}

				fileTypeObject.put("tableCleanId", "");
				fileTypeObject.put("tableCleanName", "");
				if (null != dataFileType.getTableClean()) {
					fileTypeObject.put("tableCleanId", dataFileType.getTableClean().getEtlTableId());
					fileTypeObject.put("tableCleanName", dataFileType.getTableClean().getTableName());
				}

				fileTypeObject.put("tableConvertId", "");
				fileTypeObject.put("tableConvertName", "");
				if (null != dataFileType.getTableConvert()) {
					fileTypeObject.put("tableConvertId", dataFileType.getTableConvert().getEtlTableId());
					fileTypeObject.put("tableConvertName", dataFileType.getTableConvert().getTableName());
				}

				fileTypeObject.put("tableValidateId", "");
				fileTypeObject.put("tableValidateName", "");
				if (null != dataFileType.getTableValidate()) {
					fileTypeObject.put("tableValidateId", dataFileType.getTableValidate().getEtlTableId());
					fileTypeObject.put("tableValidateName", dataFileType.getTableValidate().getTableName());
				}
				jsonArray.add(fileTypeObject);
			}
		}
		return jsonArray;
	}
	/**
	 * 保存
	 * @param dataFileType
	 * @param result
	 * @return
	 */
	@RequestMapping(value="/dataFileTypes",method=RequestMethod.POST)
	@ResponseBody
	public Object saveDataFileType(@RequestBody @Valid DataFileType dataFileType,BindingResult result) {
		JSONObject errors = new JSONObject();
		String msg=null;

		msg=resetDataFileTypeValue(dataFileType,errors);
		if (Checker.isNotNullOrEmpty(errors)) {
			return ErrorUtil.getRequestError(errors, msg);
		}
		iDataFileTypeService.save(dataFileType);
		EHCacheUtil.remove(BaseController.OPTION_DATA_FILE_TYPE);
		return dataFileType.getEtlFileTypeId();
	}

	/**
	 * 更新
	 * @param dataFileType
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/dataFileTypes/{id}",method=RequestMethod.PUT)
	@ResponseBody
	public Object updateDataFileType(@PathVariable("id") Integer id,@RequestBody @Valid DataFileType dataFileType,BindingResult result) {
		JSONObject errors = new JSONObject();
		String msg=null;

		dataFileType.setEtlFileTypeId(id);
		msg=resetDataFileTypeValue(dataFileType,errors);

		if (Checker.isNotNullOrEmpty(errors)) {
			return ErrorUtil.getRequestError(errors, msg);
		}

		iDataFileTypeService.update(dataFileType);
		EHCacheUtil.remove(BaseController.OPTION_DATA_FILE_TYPE);
		return null;
	}

	/**
	 * 分布文件类型值，为保存做准备
	 * @param dataFileType
	 */
	private String resetDataFileTypeValue(DataFileType dataFileType,JSONObject errors) {
		String msg=null;

		Org org=new Org();
		org.setOrgId(dataFileType.getEtlOrgId());
		dataFileType.setOrg(org);

		TableDef tableDefCache=new TableDef();
		tableDefCache.setEtlTableId(dataFileType.getTableCacheId());
		dataFileType.setTableCache(tableDefCache);
//		//  新增 dataFileType.getEtlFileTypeId()  判断重复 排除自己
		DataFileType dataFileTypeCache=iTableDefService.getDataFileTypeByTableAndOrg(TableDef.CATEGORY_CACHE,dataFileType.getEtlFileTypeId(),dataFileType.getEtlOrgId(), dataFileType.getTableCacheId());
		if (null != dataFileTypeCache) {
			errors.put("tableCache", "请重新选择缓存后存入表");
			msg = "请重新选择缓存后存入表";
		}
//
		TableDef tableDefClean=new TableDef();
		tableDefClean.setEtlTableId(dataFileType.getTableCleanId());
		dataFileType.setTableClean(tableDefClean);
		DataFileType dataFileTypeClean=iTableDefService.getDataFileTypeByTableAndOrg(TableDef.CATEGORY_CLEAN,dataFileType.getEtlFileTypeId(),dataFileType.getEtlOrgId(), dataFileType.getTableCleanId());
		if (null != dataFileTypeClean) {
			errors.put("tableClean", "请重新选择清洗后存入表");
			if (null == msg) {
				msg = "请重新选择清洗后存入表";
			}
		}
//
		TableDef tableDefConvert=new TableDef();
		tableDefConvert.setEtlTableId(dataFileType.getTableConvertId());
		dataFileType.setTableConvert(tableDefConvert);
//		DataFileType dataFileTypeConvert=iTableDefService.getDataFileTypeByTableAndOrg(TableDef.CATEGORY_CONVERT,dataFileType.getEtlFileTypeId(),dataFileType.getEtlOrgId(), dataFileType.getTableConvertId());
//		if (null != dataFileTypeConvert) {
//			errors.put("tableConvert", "请重新选择转换后存入表");
//			if (null == msg) {
//				msg = "请重新选择转换后存入表";
//			}
//		}
//
		TableDef tableDefValidate=new TableDef();
		tableDefValidate.setEtlTableId(dataFileType.getTableValidateId());
		dataFileType.setTableValidate(tableDefValidate);
//		DataFileType dataFileTypeVerify=iTableDefService.getDataFileTypeByTableAndOrg(TableDef.CATEGORY_VERIFY,dataFileType.getEtlFileTypeId(),dataFileType.getEtlOrgId(), dataFileType.getTableValidateId());
//		if (null != dataFileTypeVerify) {
//			errors.put("tableVerify", "请重新选择校验后存入表");
//			if (null == msg) {
//				msg = "请重新选择校验后存入表";
//			}
//		}
		return msg;
	}
	/**
	 * 删除
	 * @param id 主键
	 * @return
	 */
	@RequestMapping(value="/dataFileTypes/{id}",method=RequestMethod.DELETE)
	@ResponseBody
	public Object deleteDataFileType(@PathVariable("id") Integer id) {
		JSONObject errors = new JSONObject();
		String msg=null;

		Map<String,Object> param=new HashMap<>();
		param.put("etlFileTypeId", id);
		List<DataFile> dataFileList=iDataFileService.listByParam(param);
		if (Checker.isNotNullOrEmpty(dataFileList)) {
			errors.put("using", "该类型正在使用中，不能删除");
			msg = "该类型正在使用中，不能删除";
		}
		if (null != msg) {
			return ErrorUtil.getRequestError(errors, msg);
		}
		iDataFileTypeService.deleteById(id);
		EHCacheUtil.remove(BaseController.OPTION_DATA_FILE_TYPE);
		return null;
	}
}
