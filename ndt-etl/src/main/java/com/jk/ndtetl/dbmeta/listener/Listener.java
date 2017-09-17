package com.jk.ndtetl.dbmeta.listener;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import net.contentobjects.jnotify.JNotifyListener;

/**
 * Listener implement
 * JDK: 1.6.0_19
 * JAR: jnotify-0.94.jar
 * @author Dennis Zhao
 * @createdTime：2012-09-28
 * Technology from website: http://jnotify.sourceforge.net/
 */
public class Listener implements JNotifyListener {

    public void fileRenamed(int wd, String rootPath, String oldName, String newName) {
        writeTextAppend("Renamed==1==File path== [" + rootPath + File.separator + oldName + "] --> [" + rootPath + File.separator + newName + "]==time==" + System.currentTimeMillis());
        //print("Renamed " + rootPath + " : " + oldName + " -> " + newName);
        operateDataDB2(wd,rootPath + File.separator + newName,"Renamed",rootPath,rootPath + File.separator + oldName,rootPath + File.separator + newName);
    }

    public void fileModified(int wd, String rootPath, String name) {
        writeTextAppend("Modified==2==File path==  [" + rootPath + File.separator + name + "]==time==" + System.currentTimeMillis());
        //print("Modified " + rootPath + " : " + name);
        operateDataDB2(wd,rootPath + File.separator + name,"Modified",rootPath,"","");
    }

    public void fileDeleted(int wd, String rootPath, String name) {
        writeTextAppend("Deleted==3==File path==  [" + rootPath + File.separator + name + "]==time==" + System.currentTimeMillis());
        //print("Deleted " + rootPath + " : " + name);
        operateDataDB2(wd,rootPath + File.separator + name,"Deleted",rootPath,"","");
    }

    public void fileCreated(int wd, String rootPath, String name) {
        writeTextAppend("Created==4==File path==  [" + rootPath + File.separator + name + "]==time==" + System.currentTimeMillis());
        //print("Created " + rootPath + " : " + name);
        operateDataDB2(wd,rootPath + File.separator + name,"Created",rootPath,"","");
    }

    void print(String msg) {
        System.out.println(msg);
    }

    /**
     *
     * writeText
     * @param record
     * @return the void
     */
    private void writeTextAppend(final String record) {
        try {
            FileWriter writer = new FileWriter("d:\\File_log.txt", true);
            writer.write(record + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * oracle database test
     * operateDataOracle
     * @param wd
     * @param operatName
     * @param operateType
     * @param rootPath
     * @param oldName
     * @param newName
     * @return  void
     */
    private void operateDataOracle(int wd, String operatName, String operateType, String rootPath, String oldName, String newName) {
        Connection conn = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = java.sql.DriverManager.getConnection("jdbc:oracle:thin:@10.199.130.221:1522:ORCL", "scott", "tiger");
            String sql = "insert into t_file_log (ID, OP_NAME, OP_DATETIME, OP_ID, OP_TYPE, ROOT_PATH, OLD_NAME, NEW_NAME) "
                + "values (seq_file_log.nextval, ?, sysdate, ?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, operatName);
            pst.setInt(2, wd);
            pst.setString(3, operateType);
            pst.setString(4, rootPath);
            pst.setString(5, oldName);
            pst.setString(6, newName);
            pst.executeUpdate();
            pst.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     *
     * DB2 database test
     * operateDataDB2
     * @param wd
     * @param operatName
     * @param operateType
     * @param rootPath
     * @param oldName
     * @param newName
     * @return  void
     */
    private void operateDataDB2(int wd, String operatName, String operateType, String rootPath, String oldName, String newName) {
        Connection conn = null;
        try {
            Class.forName("com.ibm.db2.jcc.DB2Driver");
            conn = java.sql.DriverManager.getConnection(
                "jdbc:db2://10.199.30.249:50000/DB902:currentSchema=ORCL;", "SCOTT", "TIGER");
            String sql = "insert into TRSUAT.t_file_log (ID, OP_NAME, OP_DATETIME, OP_ID, OP_TYPE, ROOT_PATH, OLD_NAME, NEW_NAME) "
                + "values (seq_file_log.nextval, ?, sysdate, ?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, operatName);
            pst.setInt(2, wd);
            pst.setString(3, operateType);
            pst.setString(4, rootPath);
            pst.setString(5, oldName);
            pst.setString(6, newName);
            pst.executeUpdate();
            pst.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
