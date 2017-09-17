package com.jk.ndtetl.mon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.StringTokenizer;

import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

/**
 * 
 * @ClassName: HostConfigurationInfo
 * @Description: 获取主机配置信息
 * @author wangzhi
 * @date 2017年5月25日 下午3:43:25
 *
 */
public class HostConfigurationInfo {

    public Integer getCpuSize(Session session) {
        InputStream is = null;
        BufferedReader brStat = null;
        StringTokenizer tokenStat = null;
        Session sess = null;
        String str = "";
        try {
            sess = session;
            sess.execCommand("cat /proc/cpuinfo | grep \"cpu cores\" | uniq");
            is = new StreamGobbler(sess.getStdout());
            brStat = new BufferedReader(new InputStreamReader(is));
            str = brStat.readLine();
            tokenStat = new StringTokenizer(str);
            for (int m = 0; m < 3; m++) {
                tokenStat.nextToken();
            }
            int cpuSize = new Integer(tokenStat.nextToken()).intValue();
            return cpuSize;
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
        return null;
    }

    public Double getMemorySize(Session session) {
        InputStream is = null;
        BufferedReader brStat = null;
        StringTokenizer tokenStat = null;
        Session sess = null;
        String str = "";
        try {
            sess = session;
            sess.execCommand("free -m");
            is = new StreamGobbler(sess.getStdout());
            brStat = new BufferedReader(new InputStreamReader(is));
            brStat.readLine();
            str = brStat.readLine();
            tokenStat = new StringTokenizer(str);
            for (int m = 0; m < 1; m++) {
                tokenStat.nextToken();
            }
            int memorySize = new Integer(tokenStat.nextToken()).intValue();
            double memorySizeD = (memorySize * 1.0) / 1024;
            BigDecimal b = new BigDecimal(memorySizeD);
            double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            return f1;
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
        return null;
    }

    public Double getDiskSize(Session session) {
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
            str = brStat.readLine();
            tokenStat = new StringTokenizer(str);
            for (int m = 0; m < 1; m++) {
                tokenStat.nextToken();
            }
            String nextToken = tokenStat.nextToken().replace("T", "");
            double doubleValue = new Double(nextToken).doubleValue();
            BigDecimal b = new BigDecimal(doubleValue);
            double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            return f1;
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
        return null;
    }
}
