package com.jk.ndtetl.controller.system;/**
 * Created by polite on 2017/6/23.
 */

import com.jk.ndtetl.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.filechooser.FileSystemView;
import java.io.*;

/**
 * 朱生
 * 保存网站快捷方式到桌面
 *
 * @create 2017-06-23 10:04
 **/
@Controller
@RequestMapping("/api")
public class DesktopLinkController extends BaseController {

    /**
     * 创建网站桌面快捷方式
     * @return
     */
    @RequestMapping(value = "/desktopUrl", method = RequestMethod.GET)
    @ResponseBody
    public void createDesktopLink(HttpServletResponse response, HttpServletRequest request) {

        String downpath_dec=request.getSession().getServletContext().getRealPath("desktopUrl/");

        OutputStream os =null;
        BufferedInputStream bis =null;
        try {

            File file_des = new File(downpath_dec);
            File[] files=file_des.listFiles();
            File file = files[0];
            String realfilename = file.getName();
            bis = new BufferedInputStream(new FileInputStream(file));
            byte[] buffer = new byte[111000];
            realfilename = java.net.URLEncoder.encode(realfilename,
                    "UTF-8");
            response.reset();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/x-download");//不同类型的文件对应不同的MIME类型
            response.setHeader("Content-Disposition",
                    "attachment; filename=" + realfilename);
            os = response.getOutputStream();
            while (bis.read(buffer) > 0) {
                os.write(buffer);
            }
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (null != bis) {
                    bis.close();
                }
                if (null != os) {
                    os.close();
                }
            } catch (Exception e) {

            }
        }
    }
}
