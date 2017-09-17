package com.jk.ndtetl.mon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

/**
 * 
 * @ClassName: DiskUseInfo
 * @Description: 获取磁盘使用率的类
 * @author wangzhi
 * @date 2017年5月25日 下午3:43:25
 *
 */
public class DiskUseInfo {

    private int DISK_IDLE;

    int processStatus = 0;

    /**
     * 获取监控磁盘的信息
     * 
     * @param session
     */
    public DiskUseInfo(Session session) {
        InputStream is = null;
        BufferedReader brStat = null;
        StringTokenizer tokenStat = null;
        Session sess = null;
        String str = "";
        try {
            sess = session;
            sess.execCommand("df -hl /home");
            is = new StreamGobbler(sess.getStdout());
            brStat = new BufferedReader(new InputStreamReader(is));
            brStat.readLine();
            if ((str = brStat.readLine()) != null) {
                tokenStat = new StringTokenizer(str);
            }
            for (int i = 0; i < 4; i++) {
                tokenStat.nextToken();
            }
            DISK_IDLE = new Integer(tokenStat.nextToken().replace("%", "")).intValue();
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
        finally {
            if (brStat != null) {
                try {
                    brStat.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                finally {
                    if (is != null) {
                        try {
                            is.close();
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

    }

    public int getDiskInfo() {
        return DISK_IDLE;
    }

}
