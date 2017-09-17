package com.jk.ndt.etl.controller.presystem;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.jk.ndt.etl.controller.bank.BankSystemThread;

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
		// 启动监控线程
		new PreSystemThread().start();

		new BankSystemThread().start();
	}
}
