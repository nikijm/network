package com.jk.ndt.etl.service.monitor;

import ch.ethz.ssh2.Session;

public class ClearMemoryInfo {

	/**
	 * 执行同步命令
	 * 
	 * @param session
	 */
	public void syncInfo(Session session) {
		Session sess = null;
		try {
			sess = session;
			sess.execCommand("sync");
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	/**
	 * 执行清除缓存命令
	 * 
	 * @param session
	 */
	public void clearMemoryInfo(Session session) {
		Session sess = null;
		try {
			sess = session;
			sess.execCommand("echo 3 > /proc/sys/vm/drop_caches");
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	/**
	 * 执行回滚文件的命令
	 * 
	 * @param session
	 */
	public void rollbackFile(Session session) {
		Session sess = null;
		try {
			sess = session;
			sess.execCommand("echo 0 > /proc/sys/vm/drop_caches");
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
}
