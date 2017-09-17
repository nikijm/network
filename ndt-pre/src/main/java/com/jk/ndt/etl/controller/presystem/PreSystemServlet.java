package com.jk.ndt.etl.controller.presystem;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public class PreSystemServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PreSystemServlet() {
		super();
	}

	@Override
	public void init() throws ServletException {
		// 启动贷款申请处理线程
		new ApplicationThread().start();
	}
}
