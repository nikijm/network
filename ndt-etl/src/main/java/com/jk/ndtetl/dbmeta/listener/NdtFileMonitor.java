package com.jk.ndtetl.dbmeta.listener;

import java.io.File;

import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

public class NdtFileMonitor {

    FileAlterationMonitor monitor = null;

    public NdtFileMonitor(long interval) throws Exception {
        monitor = new FileAlterationMonitor(interval);
    }

    public void monitor(String path, FileAlterationListener listener) {
        FileAlterationObserver observer = new FileAlterationObserver(new File(path));
        monitor.addObserver(observer);
        observer.addListener(listener);
    }

    public void stop() throws Exception {
        monitor.stop();
    }

    public void start() throws Exception {
        monitor.start();
    }

//    public static void main(String[] args) throws Exception {
//        NdtFileMonitor m = new NdtFileMonitor(5000);
//        m.monitor("E:\\filepath", new NdtFileListener());
//        m.start();
//    }

}
