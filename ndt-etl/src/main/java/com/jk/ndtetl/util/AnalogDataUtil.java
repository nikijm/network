package com.jk.ndtetl.util;/**
 * Created by polite on 2017/6/23.
 */

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.jk.ndtetl.util.promission.ResourceMenusUtil;

/**
 * 朱生
 *
 * @create 2017-06-23 10:52
 **/
public class AnalogDataUtil {

    private static  final String baseUrl="/api/";
    private static  final String fileSuffixName=".json";

    /**
     * 未开发接口模拟数据返回
     * json文件名格式说明：/api/auth/session,文件名为auth_session,不传文件后缀名
     * @param jsonFileName 对应的json文件
     * @param getType get方法的类型 ：  list 集合  one  详情
     * @param request
     * @return
     */
    public static Object getAnalogData(String jsonFileName,String getType, HttpServletRequest request) {
        if (StringUtils.equals("GET", request.getMethod())) {
            jsonFileName = jsonFileName + "_" + request.getMethod().toLowerCase()+"_"+getType+ fileSuffixName;
        } else {
            jsonFileName=jsonFileName+"_"+request.getMethod().toLowerCase()+fileSuffixName;
        }
        String realPath = request.getServletContext().getRealPath(baseUrl + jsonFileName);
        try {
            return  JSON.parseObject(ResourceMenusUtil.readFile(realPath));
        } catch (Exception e) {
            return  JSON.parseArray(ResourceMenusUtil.readFile(realPath));
        }
    }
}
