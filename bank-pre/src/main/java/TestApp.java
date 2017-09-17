import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Properties;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;
import com.jk.ndt.etl.entity.bank.BankProcessSync;
import com.jk.ndt.etl.entity.vo.ApplicationResBean;

public class TestApp {
	private static final String APPLICATION_JSON = "application/json";

	private static final String CONTENT_TYPE_TEXT_JSON = "text/json;";

	private String getSendUrl() {
		String sendUrl = null;

		InputStream inpStream = null;
		try {
			String path = this.getClass().getResource("/").getPath() + "setting.properties";
			Properties prop = new Properties();
			inpStream = new FileInputStream(new File(path));
			prop.load(inpStream);
			sendUrl = prop.getProperty("face.system.url");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != inpStream) {
				try {
					inpStream.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
		return sendUrl;
	}

	public static void httpService() throws Exception {

		ApplicationResBean applicationResBean = new ApplicationResBean();
		applicationResBean.setDocNo("DocNo" + System.currentTimeMillis());
		applicationResBean.setErrorMsg("error message测试");
		applicationResBean.setRequestType("Application");
		applicationResBean.setTransactionID("");
		applicationResBean.setRequestType("Application");
		applicationResBean.setErrorCode(0);
		String reJson = JSONObject.toJSONString(applicationResBean);
		String result = "";

		String urlStr = "http://30.3.5.122:8890/ASWS/httpjson-srv";
		System.out.println("1");
		URL url = new URL(urlStr);
		System.out.println("2");
		URLConnection con = url.openConnection();
		System.out.println("3");
		con.setDoOutput(true);
		System.out.println("4");
		//con.setRequestProperty("Pragma:", "no-cache");
		con.setRequestProperty("Cache-Control", "no-cache");
		con.setRequestProperty("Content-Type", "text/html");
		OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream(), "utf-8");
		System.out.println("5");
		System.out.println("客户端发出的报文：" + reJson);
		out.write(reJson);
		System.out.println("6");
		out.flush();
		out.close();

		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
		String line = "";
		StringBuffer buf = new StringBuffer();
		while ((line = br.readLine()) != null) {
			buf.append(line);
		}
		result = buf.toString();
		System.out.println("客户端收到的回复：" + result);

	}

	public void test() throws Exception {
		ApplicationResBean applicationResBean = new ApplicationResBean();
		applicationResBean.setDocNo("DocNo" + System.currentTimeMillis());
		applicationResBean.setErrorMsg("error message测试");
		applicationResBean.setRequestType("Application");
		applicationResBean.setTransactionID("");
		applicationResBean.setRequestType("Application");
		applicationResBean.setErrorCode(0);
		String reJson = JSONObject.toJSONString(applicationResBean);
		// 获取发送的url地址
		String sendUrl = getSendUrl();

		BankProcessSync bankProcessSync = null;

		bankProcessSync = new BankProcessSync();
		bankProcessSync.setRequest("12345");
		// 获取第一个未处理记录进行处理
		// bankProcessSync = bankSystemService.getNTopOneUnProcessedM();
		System.out.println("---------------------------------------------------------------");
		System.out.println(reJson);
		// 处理未处理记录发送到金融机构
		String encoderJson = URLEncoder.encode(bankProcessSync.getRequest(), "utf-8");

		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost("http://30.3.5.122:8890/ASWS/httpjson-srv");
		// httpPost.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);

		StringEntity se = new StringEntity(reJson);
		// se.setContentType(CONTENT_TYPE_TEXT_JSON);
		// se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
		// APPLICATION_JSON));
		se.setContentEncoding("UTF-8");
		httpPost.setEntity(se);

		// 先更改状态为已处理，出异常再修改为未处理
		// bankProcessSync.setProcessed("Y");
		// bankSystemService.updateBankProcessSync(bankProcessSync);

		// 发送请求
		System.out.println("发送数据s：-----------------------------");
		HttpResponse httpRes = httpClient.execute(httpPost);
		System.out.println("发送数据e：-----------------------------");
		// 输入返回信息
		// String resStr =
		// URLDecoder.decode(EntityUtils.toString(httpRes.getEntity()),
		// "utf-8");
		String resStr = EntityUtils.toString(httpRes.getEntity());
		System.out.println("返回结果：" + resStr);
	}

	public static void main(String[] args) throws Exception {
		new TestApp().httpService();
	}
}
