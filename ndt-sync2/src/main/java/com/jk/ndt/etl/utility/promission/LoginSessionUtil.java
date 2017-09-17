package com.jk.ndt.etl.utility.promission;/**
 * Created by polite on 2017/5/31.
 */

import com.jk.ndt.etl.constant.Constant;
import com.jk.ndt.etl.entity.system.Admin;
import com.jk.ndt.etl.entity.system.User;
import com.jk.ndt.etl.utility.EHCacheUtil;
import com.jk.ndt.etl.utility.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Map;

/**
 * 朱生
 * 获取登录用户
 * @create  2017-05-31 14:10
 **/
public class LoginSessionUtil {


    /**
     * 无法区别admin或user，故返回Object，在实际应用中进行判断
     * @param request
     * @return
     */
    public static Object getLoginAccount(HttpServletRequest request) {
        String clientToken = request.getHeader(Constant.LOGIN_USER_CLIENT_TOKEN);
        Map<String, Object> sessionLoginMap =(Map<String, Object>) EHCacheUtil.get(clientToken);
        if (null == sessionLoginMap) {
            return null;
        }
        return sessionLoginMap.get(Constant.LOGIN_USER_SESSION);
    }

    /**
     * 获取admin对象，如果admin为空或者不是admin对象，返回空
     * @param request
     * @return admin对象
     */
    public static Admin getAdmin(HttpServletRequest request) {
        String clientToken = request.getHeader(Constant.LOGIN_USER_CLIENT_TOKEN);
        Map<String, Object> sessionLoginMap =(Map<String, Object>) EHCacheUtil.get(clientToken);
        if (null == sessionLoginMap) {
            return null;
        }
        try {
           return (Admin) sessionLoginMap.get(Constant.LOGIN_USER_SESSION);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 确保调用次方法的地方所登录的用户是admin，否则抛出转换异常
     * @param request
     * @return admin_id
     */
    public static BigDecimal getAdminId(HttpServletRequest request) {
        String clientToken = request.getParameter(Constant.LOGIN_USER_CLIENT_TOKEN);
        // 测试代码↓
        if (StringUtils.isBlank(clientToken)) {
            return new BigDecimal(1);
        }
        // 测试代码↑
        Map<String, Object> sessionLoginMap =(Map<String, Object>) EHCacheUtil.get(clientToken);
        if (null == sessionLoginMap) {
            return null;
        }
        Admin admin=(Admin) sessionLoginMap.get(Constant.LOGIN_USER_SESSION);
        return  admin.getId();
    }

    /**
     * 获取user对象，如果不是user对象或者user对象为空，返回空
     * @param request
     * @return  user对象
     */
    public static User getUser(HttpServletRequest request) {
        String clientToken = request.getParameter(Constant.LOGIN_USER_CLIENT_TOKEN);
        // 测试代码
        if (StringUtils.isBlank(clientToken)) {
            User user=new User();
            user.setId(new BigDecimal(29));
            user.setName("user");
            return  user;
        }
        // 测试代码
        Map<String, Object> sessionLoginMap =(Map<String, Object>) EHCacheUtil.get(clientToken);
        if (null == sessionLoginMap) {
            return null;
        }
        try {
            return  (User) sessionLoginMap.get(Constant.LOGIN_USER_SESSION);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
