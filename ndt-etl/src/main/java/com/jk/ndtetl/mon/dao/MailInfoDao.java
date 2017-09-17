package com.jk.ndtetl.mon.dao;

import com.jk.ndtetl.mon.Email;

/**
 * 
 * @ClassName: MailInfoDao
 * @Description: 获取发送邮件信息的dao
 * @author wangzhi
 * @date 2017年5月25日 下午3:43:25
 *
 */
public interface MailInfoDao {

    /**
     * 获取发送邮件的信息
     * 
     * @return
     */
    public Email getSendMailInfo();

}
