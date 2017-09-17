package com.jk.ndt.etl.utility;

import com.jk.ndt.etl.entity.system.Admin;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

/**
 * Created by 朱生 on 2017/5/23.
 */
public class SessionUtil {

//    public static Admin getAdmin(){
//        Subject currentAdmin = SecurityUtils.getSubject();
//        Session session = currentAdmin.getSession();
//        Object adminObj = session.getAttribute(Const.LOGIN_ADMIN_SESSION);
//        if (null == adminObj) {
//            return null;
//        }
//        return  (Admin) session.getAttribute(Const.LOGIN_ADMIN_SESSION);
//    }
}
