package com.jk.ndtetl.util.etl;/**
 * Created by polite on 2017/7/3.
 */

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jk.ndtetl.dbmeta.DataFile;
import com.jk.ndtetl.rule.service.IRuleOpService;
import com.jk.ndtetl.system.service.IOrganizationService;
import com.jk.ndtetl.system.service.IRoleService;
import com.jk.ndtetl.util.EHCacheUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 朱生
 * 页面下拉选项内容
 * @create 2017-07-03 14:56
 **/
public class OptionUtil {

    public static final String OPTYPE= "opType";
    /**
     * 节点类型列表
     * @return
     */
//    public static Object listRuleType() {
//
//        Map<String, String> ruleTypeMap = new HashMap<>();
//        ruleTypeMap.put("cache", "缓存");
//        ruleTypeMap.put("clean", "清洗");
//        ruleTypeMap.put("convert", "转换");
//        ruleTypeMap.put("validate", "验证");
//        return ruleTypeMap;
//    }

    /**
     * 规则类型选项列表
     *
     * @return
     */
//    public static Object listRuleTypeOption() {
//
//        Map<String, String> ruleTypeMap = new HashMap<>();
//        ruleTypeMap.put("1", "缓存");
//        ruleTypeMap.put("2", "清洗");
//        ruleTypeMap.put("3", "校验");
//        return ruleTypeMap;
//    }
    /**
     * 任务调度，文件状态选项列表
     * @return
     */
    public static Object listDataFileStatusOptions() {

        JSONArray rs = new JSONArray();
        Map<String, String> DataFileStatus1 = new HashMap<>();
        Map<String, String> DataFileStatus2 = new HashMap<>();
        Map<String, String> DataFileStatus3 = new HashMap<>();
        DataFileStatus1.put("id", "statusCache_" + DataFile.DATA_STATUS_FINISHED);
        DataFileStatus1.put("name", "已缓存");
        rs.add(DataFileStatus1);
        DataFileStatus2.put("id", "statusClean_"+ DataFile.DATA_STATUS_FINISHED);
        DataFileStatus2.put("name", "已清洗");
        rs.add(DataFileStatus2);
        DataFileStatus3.put("id", "statusConvert_"+ DataFile.DATA_STATUS_FINISHED);
        DataFileStatus3.put("name", "已转换");
        rs.add(DataFileStatus3);

        return rs;
    }

}
