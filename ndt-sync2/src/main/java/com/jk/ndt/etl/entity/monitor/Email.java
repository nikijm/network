package com.jk.ndt.etl.entity.monitor;
/**
 * 
 * @ClassName: Email
 * @Description: 发送邮件的实体类
 * @author wangzhi
 * @date 2017年5月25日 下午3:43:25
 *
 */
public class Email {
	
	private String mailHost;   //邮件服务器的地址
	  
	private String protocol;   //邮件的协议
	
	private Integer isAuth;    //是否需要认证
	
	private String password;   //发件人的密码
	
    private String fromAddress;  //发件人
      
    private String toAddress;    //收件人的地址
  
    private String subject;      //邮件的主题
  
    private String content;     //邮件的内容
    

	public String getMailHost() {
		return mailHost;
	}

	public void setMailHost(String mailHost) {
		this.mailHost = mailHost;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public Integer getIsAuth() {
		return isAuth;
	}

	public void setIsAuth(Integer isAuth) {
		this.isAuth = isAuth;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public String getToAddress() {
		return toAddress;
	}

	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	} 

}
