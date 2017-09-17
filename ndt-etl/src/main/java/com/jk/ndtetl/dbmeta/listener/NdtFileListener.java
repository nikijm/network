package com.jk.ndtetl.dbmeta.listener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jk.ndtetl.dbmeta.DataFile;
import com.jk.ndtetl.dbmeta.service.IDataFileService;
import com.jk.ndtetl.util.ExcelUtil;
import com.jk.ndtetl.util.SpringContextHolder;
public class NdtFileListener implements FileAlterationListener, ServletContextListener {
    Logger logger = LoggerFactory.getLogger(getClass());
    @Override
    public void onStart(FileAlterationObserver observer) {
        logger.info("onStart");
    }

    @Override
    public void onDirectoryCreate(File directory) {
        logger.info("onDirectoryCreate:" + directory.getName());
    }

    @Override
    public void onDirectoryChange(File directory) {
        logger.info("onDirectoryChange:" + directory.getName());
    }

    @Override
    public void onDirectoryDelete(File directory) {
        logger.info("onDirectoryDelete:" + directory.getName());
    }

    @Override
    public void onFileCreate(File file) {
        IDataFileService dataFileService = SpringContextHolder.getBean("dataFileService");
        String parent = file.getParent();
        String name = file.getName();
        List<DataFile> dataFiles = new ArrayList<>();
        String absolutePath = file.getAbsolutePath();
        List<Integer> sheets = new ArrayList<>();
        try {
            sheets = ExcelUtil.getIndexOfSheets(absolutePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < sheets.size(); i++) {
            DataFile dataFile = new DataFile();
            dataFile.setFileName(file.getName());
            dataFile.setCreated(new Date());
            // dataFile.setCreatedBy(createdBy);
            dataFile.setPath(DataFile.UPLOAD_URL+ File.separator + file.getName());
            dataFile.setSheetIndex(i);
            dataFile.setStatusUpload(1);
            dataFiles.add(dataFile);

        }
        dataFileService.saveDatafiles(dataFiles);
        logger.info("onFileCreate:" + file.getName());
    }

    @Override
    public void onFileChange(File file) {
        logger.info("onFileCreate : " + file.getName());
    }

    @Override
    public void onFileDelete(File file) {
        System.out.println("onFileDelete :" + file.getName());
    }

    @Override
    public void onStop(FileAlterationObserver observer) {
        System.out.println("onStop");
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
//        NdtFileMonitor ndtFileMonitor = WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext())
//                .getBean(NdtFileMonitor.class);
//        ApplicationContext parent = SpringContextHolder.getApplicationContext().getParent();
//        ndtFileMonitor.monitor("E:\\logs", this);
//        try {
//            ndtFileMonitor.start();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

}
