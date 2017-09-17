package com.jk.ndt.etl.extend;

import com.alibaba.druid.sql.dialect.oracle.ast.OracleDataTypeTimestamp;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jk.ndt.etl.constant.Constant;
import com.jk.ndt.etl.entity.system.Admin;
import com.jk.ndt.etl.entity.system.Menu;
import com.jk.ndt.etl.entity.system.Resource;
import com.jk.ndt.etl.entity.system.User;
import com.jk.ndt.etl.utility.EHCacheUtil;
import com.jk.ndt.etl.utility.ErrorUtil;
import com.jk.ndt.etl.utility.StringUtils;
import com.jk.ndt.etl.utility.TransforUtil;
import com.jk.ndt.etl.utility.promission.ResourceMenusUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.apache.shiro.io.ResourceUtils;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.DataInput;
import java.security.Key;
import java.util.*;

/**
 * Created by 朱生 on 2017/5/18.
 * 权限校验
 */
public class EtlPermissionsAuthorizationFilter extends AccessControlFilter {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    private static final String cacaheName="ndt_etl_cache";
    /**
     * 验证 jjTW token签名 权限控制
     *
     * @param servletRequest
     * @param servletResponse
     * @param o
     * @return true 验证通过
     * @throws Exception
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

// 获取token对于的用户信息
        String compactJws = request.getHeader(Constant.LOGIN_USER_CLIENT_TOKEN);
        if (StringUtils.isBlank(compactJws) || null == EHCacheUtil.get(compactJws)) {
            ErrorUtil.getOverdueError(null, response);
            return false;
        }
        Map<String, Object> loginSessionMap = (Map<String, Object>) EHCacheUtil.get(compactJws);
        Object obj = loginSessionMap.get(Constant.LOGIN_USER_SESSION);
        String name="";
        try {
            User user = (User) obj;
            name=user.getName();
        } catch (Exception e) {
            Admin admin = (Admin) obj;
            name=admin.getName();
        }
////验证jjtw签名
        if (!ResourceMenusUtil.jjtwValid(loginSessionMap, compactJws, name, response)) {
            ErrorUtil.getOverdueError(null, response);
            EHCacheUtil.remove(compactJws);
            return false;
        }
//验证权限
        if (!ResourceMenusUtil.permissionValid(loginSessionMap, response, request)) {
            ErrorUtil.getPermissionError(null, "无权限", response);
            return false;
        }
        return true;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {

        return false;
    }

}
