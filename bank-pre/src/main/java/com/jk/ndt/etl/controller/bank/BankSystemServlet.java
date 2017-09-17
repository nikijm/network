package com.jk.ndt.etl.controller.bank;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public class BankSystemServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BankSystemServlet() {
		super();
	}

	@Override
	public void init() throws ServletException {
		// 启动申请推送业务系统线程
		//new ApplicationThread().start();
		// 启动状态同步农贷通线程
		new BusinessOperateThread().start();
	}
}
