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
 * @ClassName: SysMemInfo
 * @Description: 获取系统memory信息的类
 * @author wangzhi
 * @date 2017年5月25日 下午3:43:25
 *
 */
public class SysMemInfo {

    private int MEM_INFO = 0;

    private int useValue = 0;

    /**
     * 获取监控内存的信息
     * 
     * @param session
     */
    public SysMemInfo(Session session) {
        init(session);
    }

    public void init(Session session) {
        InputStream is = null;
        BufferedReader brStat = null;
        StringTokenizer tokenStat = null;
        Session sess = null;
        String str = "";
        try {
            sess = session;
            sess.execCommand("free");
            is = new StreamGobbler(sess.getStdout());
            brStat = new BufferedReader(new InputStreamReader(is));
            str = brStat.readLine();
            str = brStat.readLine();
            tokenStat = new StringTokenizer(str);
            tokenStat.nextToken();
            int total = new Integer(tokenStat.nextToken()).intValue();
            str = brStat.readLine();
            tokenStat = new StringTokenizer(str);
            for (int i = 0; i < 2; i++) {
                tokenStat.nextToken();
            }
            String nextToken = tokenStat.nextToken();
            useValue = new Integer(nextToken).intValue();
            MEM_INFO = (int) ((useValue * 1.0 / total) * 100);
        }
        catch (Exception e) {
            e.printStackTrace();
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

    public int getMEMInfo() {
        return MEM_INFO;
    }

    public int getUseValue() {
        return useValue;
    }

}
