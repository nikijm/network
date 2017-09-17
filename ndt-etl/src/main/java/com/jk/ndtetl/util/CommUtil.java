package com.jk.ndtetl.util;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import net.coobird.thumbnailator.Thumbnails;

/**
 * @author fangwei
 * @ClassName: CommUtil
 * @Description: 公共工具类，主要对文件上传和文件操作
 * @date 2017年5月15日 下午3:38:02
 */
public class CommUtil {

    /**
     * @param request
     * @param file
     * @param saveFilePathName
     * 文件保存的路径名
     * @param saveFileName
     * 文件保存名
     * @param extendes
     * 可以保存的文件扩展名
     * @return
     * @throws IOException
     * @throws OutOfMemoryError
     * @throws NoSuchAlgorithmException
     * @Description: 将文件保存到服务器制定位置的路径，并返回文件信息到map中， 如文件的类型，文件大小，文件的保存名称，文件的原名等
     * @author lianhanwen
     * @date 2017年6月13日 下午6:56:08
     */
    public static Map<String, Object> saveFileToServer(HttpServletRequest request, CommonsMultipartFile file,
            String saveFilePathName, String saveFileName, String[] extendes)
            throws IOException, OutOfMemoryError, NoSuchAlgorithmException {
        System.out.println("#########saveFilePathName " + saveFilePathName);
        Map<String, Object> map = new HashMap<String, Object>();
        if ((file != null) && (!file.isEmpty())) {
            // 获得文件的扩展名
            String extend = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1)
                    .toLowerCase();

            // 对保存文件名的处理，如果没有的给值就使用uuid+扩展名的方式
            if (Checker.isNullOrEmpty(saveFileName)) {
                saveFileName = UUID.randomUUID().toString() + "." + extend;
            }
            if (saveFileName.lastIndexOf(".") < 0) {
                saveFileName = saveFileName + "." + extend;
            }

            // 获得文件的大小
            float fileSize = (float) file.getSize();

            List<String> errors = new ArrayList<String>();
            boolean flag = false;
            // 检查文件的扩展名是否满足要求
            if (extendes != null) {
                for (String s : extendes) {
                    if (extend.toLowerCase().equals(s)) {
                        flag = true;
                        break;
                    }
                }
            }
            else {
                flag = true;
            }
            if (flag) {
                File path = new File(saveFilePathName);
                // 检查保存文件的路径是否存在，不存在则创建文件夹
                if (!path.exists()) {
                    path.mkdirs();
                }

                // 输出流
                DataOutputStream out = new DataOutputStream(
                        new FileOutputStream(saveFilePathName + File.separator + saveFileName));

                // 输入流，可能需要优化
                InputStream is = null;
                try {
                    is = file.getInputStream();
                    int size = (int) fileSize;
                    byte[] buffer = new byte[size];
                    while (is.read(buffer) > 0) {
                        out.write(buffer);
                    }
                }
                catch (IOException exception) {
                    exception.printStackTrace();
                    throw exception;
                }
                finally {
                    if (is != null) {
                        is.close();
                    }
                    if (out != null) {
                        out.close();
                    }
                }
                // 如果是图片类型，获得图片的宽高
                if (isImg(extend)) {
                    File img = new File(saveFilePathName + "/" + saveFileName);
                    try {
                        BufferedImage bis = ImageIO.read(img);
                        int w = bis.getWidth();
                        int h = bis.getHeight();
                        map.put("width", Integer.valueOf(w));
                        map.put("height", Integer.valueOf(h));

                        String thumbnailFilePathName = saveFilePathName + File.separator
                                + saveFileName.substring(0, saveFileName.indexOf(".")) + "small.png";
                        Thumbnails.of(saveFilePathName + "/" + saveFileName).size(32, 32).toFile(thumbnailFilePathName);
                    }
                    catch (Exception localException) {
                        localException.printStackTrace();
                        throw localException;
                    }
                }
                map.put("mime", extend);
                map.put("fileName", saveFileName);
                map.put("fileSize", Float.valueOf(fileSize));
                map.put("oldName", file.getOriginalFilename());
            }
            else {
                map.put("error", errors);
                errors.add("不允许的扩展名");
            }
        }
        else {
            map.put("width", Integer.valueOf(0));
            map.put("height", Integer.valueOf(0));
            map.put("mime", "");
            map.put("fileName", "");
            map.put("fileSize", Float.valueOf(0.0F));
            map.put("oldName", "");
        }
        return map;
    }

    /**
     * @param extend
     * 文件后缀
     * @return
     * @Description: 检查文件是否是图片类型
     * @author lianhanwen
     * @date 2017年6月13日 下午6:56:32
     */
    public static boolean isImg(String extend) {
        boolean ret = false;
        List<String> list = new ArrayList<String>();
        list.add("jpg");
        list.add("jpeg");
        list.add("bmp");
        list.add("gif");
        list.add("png");
        list.add("tif");
        for (String s : list) {
            if (s.equals(extend))
                ret = true;
        }
        return ret;
    }

    /**
     * @param folderPath
     * @return
     * @Description:创建文件夹
     * @author lianhanwen
     * @date 2017年6月13日 下午6:56:49
     */
    public static boolean createFolder(String folderPath) {
        boolean ret = true;
        try {
            File myFilePath = new File(folderPath);
            if ((!myFilePath.exists()) && (!myFilePath.isDirectory())) {
                ret = myFilePath.mkdirs();
            }
        }
        catch (Exception e) {
            ret = false;
        }
        return ret;
    }

    /**
     * @param list
     * @param begin
     * @param end
     * @return
     * @Description: 拷贝List中的内容到一个新的List
     * @author lianhanwen
     * @date 2017年6月13日 下午6:57:16
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static List copyList(List list, int begin, int end) {
        List l = new ArrayList();
        if (list == null)
            return l;
        if (end > list.size())
            end = list.size();
        for (int i = begin; i < end; i++) {
            l.add(list.get(i));
        }
        return l;
    }

    /**
     * 拷贝文件 <一句话功能简述> <功能详细描述>
     *
     * @param oldPath
     * @param newPath
     * @see [类、类#方法、类#成员]
     */
    public static void copyFile(String oldPath, String newPath) {
        try {
            File oldfile = new File(oldPath);
            File newFile = new File(newPath);
            FileUtils.copyFile(oldfile, newFile);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除制定路径的文件夹
     *
     * @param path
     * @return
     */
    public static boolean deleteFolder(String path) {
        boolean flag = false;
        File file = new File(path);

        if (!file.exists()) {
            return flag;
        }

        if (file.isFile()) {
            return deleteFile(path);
        }
        return deleteDirectory(path);
    }

    /**
     * 删除制定路径的文件
     *
     * @param path
     * @return
     */
    public static boolean deleteFile(String path) {
        boolean flag = false;
        File file = new File(path);

        if ((file.isFile()) && (file.exists())) {
            file.delete();
            flag = true;
        }
        return flag;
    }

    /**
     * 删除目录（文件夹）以及目录下的文件
     *
     * @param sPath
     * 被删除目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String sPath) {
        // 如果sPath不以文件分隔符结尾，自动添加文件分隔符
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        Boolean flag = true;
        // 删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            } // 删除子目录
            else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag)
            return false;
        // 删除当前目录
        if (dirFile.delete()) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * 文件流转换成byte[]
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    private static byte[] toByteArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        BufferedInputStream in = null;
        try {
            in = new BufferedInputStream(inputStream);
            int buf_size = 1024;
            byte[] buffer = new byte[buf_size];
            int len = 0;
            while (-1 != (len = in.read(buffer, 0, buf_size))) {
                bos.write(buffer, 0, len);
            }
            return bos.toByteArray();
        }
        catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            bos.close();
        }
    }

    /**
     * 获取文件下载地址
     * @param request
     * @param url
     * @return
     */
    public static String getFileDownloadUrl(HttpServletRequest request, String url) {
        if (!StringUtils.isBlank(url)) {
            return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                    + request.getContextPath() + "/" + url;
        }
        return "";
    }

    /**
     * @param oldString
     * @return
     * @Description: 处理数据库查询下划线特殊字符
     * @author fangwei
     * @date 2017年6月20日 下午3:54:44
     */
    public static String getSpecialCharQuery(Object oldString) {
        if (Checker.isNullOrEmpty(oldString)) {
            return "";
        }
        return oldString.toString().replaceAll("_", "/_").replaceAll("%", "/%").toLowerCase();
    }

}
