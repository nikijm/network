package com.jk.ndt.etl.service.monitor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

public class SysCpuInfo {

	private int CPU_IDLE = 0;
	private int CPU_VALUE = 0;

	int processStatus = 0;

	/**
	 * 获取监控cpu的信息
	 * 
	 * @param session
	 */
	public SysCpuInfo(Session session) {
		InputStream is = null;
		BufferedReader brStat = null;
		StringTokenizer tokenStat = null;
		Session sess = null;
		String str = "";
		int i = 0, j = 0, cpuidle = 0;
		try {
			sess = session;
			sess.execCommand("vmstat 2 2 ");
			is = new StreamGobbler(sess.getStdout());
			brStat = new BufferedReader(new InputStreamReader(is));
			for (int m = 0; m < 2; m++) {
				str = brStat.readLine();
			}
			for (j = 0; j < 2; j++) {
				str = brStat.readLine();
				if (str == null)
					break;
				tokenStat = new StringTokenizer(str);
				for (i = 0; i < 14; i++) {
					tokenStat.nextToken();
				}
				cpuidle = cpuidle + new Integer(tokenStat.nextToken()).intValue();
			}
			CPU_VALUE = new Double(cpuidle / 2).intValue();
			CPU_IDLE = 100 - CPU_VALUE;

		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			if (brStat != null) {
				try {
					brStat.close();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (is != null) {
						try {
							is.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	public int getCPUInfo() {
		return CPU_IDLE;
	}

}
