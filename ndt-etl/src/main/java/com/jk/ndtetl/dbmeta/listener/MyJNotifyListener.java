package com.jk.ndtetl.dbmeta.listener;
import net.contentobjects.jnotify.JNotifyListener;
 
public class MyJNotifyListener implements JNotifyListener{
 
    @Override
    public void fileCreated(int wd, String rootPath, String name) {
        System.err.println("create: --->" + wd + "--->" + rootPath + "--->" + name);
    }
 
    @Override
    public void fileDeleted(int wd, String rootPath, String name) {
        System.err.println("delete: --->" + wd + "--->" + rootPath + "--->" + name);
    }
 
    @Override
    public void fileModified(int wd, String rootPath, String name) {
        System.err.println("modified: --->" + wd + "--->" + rootPath + "--->" + name);
    }
 
    @Override
    public void fileRenamed(int wd, String rootPath, String oldName, String newName) {
        System.err.println("rename: --->" + wd + "--->" + rootPath + "--->" + oldName + "--->" + newName);
    }
}
