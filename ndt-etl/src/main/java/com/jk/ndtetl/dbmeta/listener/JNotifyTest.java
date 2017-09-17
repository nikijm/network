package com.jk.ndtetl.dbmeta.listener;

import net.contentobjects.jnotify.JNotify;

/**
 * Monitor file directory file (folder ) is created, modified, deleted, renamed files[folders]
 * (To adapt to a sub-folders).
 * JDK: 1.6.0_19
 * JAR: jnotify-0.94.jar
 * @author Dennis Zhao
 * @createdTime：2012-09-28
 * Technology from website: http://jnotify.sourceforge.net/
 */

public class JNotifyTest {

    public static void main(String[] args) {
        JNotifyTest test = new JNotifyTest();
        try {
            test.sample();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sample() throws Exception {
        // path to watch
        String path = "D:\\abc";
        // watch mask, specify events you care about,
        // or JNotify.FILE_ANY for all events.
        int mask = JNotify.FILE_CREATED | JNotify.FILE_DELETED | JNotify.FILE_MODIFIED | JNotify.FILE_RENAMED;
        // watch subtree?
        boolean watchSubtree = true;
        // add actual watch
        int watchID = JNotify.addWatch(path, mask, watchSubtree, new Listener());
        // sleep a little, the application will exit if you
        // don't (watching is asynchronous), depending on your
        // application, this may not be required
        Thread.sleep(15000);
        //Thread.sleep(5000);
        // to remove watch the watch
        boolean res = JNotify.removeWatch(watchID);
        if (!res) {
            // invalid watch ID specified.
            System.out.println("Delete from data : " + watchID);
        }
    }

}
