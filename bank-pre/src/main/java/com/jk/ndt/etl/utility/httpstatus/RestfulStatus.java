package com.jk.ndt.etl.utility.httpstatus;

/**
 * Created by 朱生 on 2017/5/17.
 */
public class RestfulStatus {

	//////////////////////////////////// 200 系列
	/**
	 * 请求成功
	 */
	public static final int SUCCESS = 200;

	////////////////////////////////// 300 系列
	/**
	 * 资源未有更新
	 */
	public static final int MULTIPLE_CHOICES = 300;

	//////////////////////////////////// 400 系列
	/**
	 * 请求格式错误
	 */
	public static final int FAILED = 400;
	/**
	 * 无权限
	 */
	public static final int JURISDICTION = 401;

	////////////////////////////////////////// 500 系列
	/**
	 * 服务器错误
	 */
	public static final int SERVER_ERROR = 500;

}
