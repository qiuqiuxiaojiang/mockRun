package mock;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;



@SuppressWarnings("deprecation")
public class FileUtils {
	
	
	public static List<String> FileToList(String filename) {
		List<String> list = new ArrayList<String>();
		String line = null;
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(new File(filename).getAbsoluteFile()),
					"UTF-8"));
			while ((line = br.readLine()) != null) {
				list.add(line.toLowerCase());
			}
			br.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	
	
	public static List<String> LocalFileToList(String name) {
		List<String> list =new ArrayList<String>();
		
		BufferedReader br = null;
		try {
			InputStream resourceAsStream = FileUtils.class.getClassLoader().getResourceAsStream(name);
			br = new BufferedReader(new InputStreamReader(resourceAsStream,"UTF-8"));
			String line = null;
			while ((line = br.readLine()) != null) {
				list.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(list.toString());
		return list;
	}
	
	@SuppressWarnings("rawtypes")
	public static void writeFile(String filePath, List content){
		BufferedWriter bw = null ;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath, true)));
			bw.write("");
			bw.flush();
			for (Object object : content) {
				bw.write(object+"\r\n");
			}
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static long getCurrentTime(String dataTime) {
		return new SimpleDateFormat("yyyy-MM-dd").parse(dataTime,new ParsePosition(0)).getTime();
	}
	
	@SuppressWarnings({ "resource" })
	public static String sendRequest(String url, String jsonStr)
			throws Exception {
		HttpPost httpPost = new HttpPost(url);
		StringEntity se = new StringEntity(jsonStr, "UTF-8");
		httpPost.setEntity(se);
		httpPost.addHeader(HTTP.CONTENT_TYPE, "application/json");
		se.setContentType("text/json");
		se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
				"application/json"));
		HttpResponse response = new DefaultHttpClient().execute(httpPost);
		int code = response.getStatusLine().getStatusCode();
		System.out.println("code:" + code);
		if (code == 200) {
			String result = EntityUtils.toString(response.getEntity(), Charset.forName("utf-8"));
			return "返回结果："+result;
		} else {
			System.out.println("HTTP error code:" + code);
			return "访问结果：HTTP ERROR "+code;
		}
	}

	@SuppressWarnings({ "resource" })
	public static String sendRequest(String url, String jsonStr,Map<String,String> mapHeader)
			throws Exception {
		HttpPost httpPost = new HttpPost(url);
		StringEntity se = new StringEntity(jsonStr, "UTF-8");
		httpPost.setEntity(se);
		httpPost.addHeader(HTTP.CONTENT_TYPE, "application/json");
		if (mapHeader != null && mapHeader.size() > 0) {
         	for (String key : mapHeader.keySet()) {
         		httpPost.setHeader(key, mapHeader.get(key));
         	}
         }
		
		se.setContentType("text/json");
		se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
				"application/json"));
		HttpResponse response = new DefaultHttpClient().execute(httpPost);
		int code = response.getStatusLine().getStatusCode();
		System.out.println("code:" + code);
		if (code == 200) {
			String result = EntityUtils.toString(response.getEntity(), Charset.forName("utf-8"));
			return "返回结果："+result;
		} else {
			System.out.println("HTTP error code:" + code);
			return "访问结果：HTTP ERROR "+code;
		}
	}

	
	
	@SuppressWarnings({ "resource" })
	public static String removeData(String url)
			throws Exception {
		HttpGet httpGet = new HttpGet(url);
	
		HttpResponse response = new DefaultHttpClient().execute(httpGet);
		int code = response.getStatusLine().getStatusCode();
		System.out.println("code:" + code);
		if (code == 200) {
			String result = EntityUtils.toString(response.getEntity(), Charset.forName("utf-8"));
			return "返回结果："+result;
		} else {
			System.out.println("HTTP error code:" + code);
			return "访问结果：HTTP ERROR "+code;
		}
	}
	
	public static void main(String[] args) {
		//LocalFileToList("\\resources\\hospital.txt");
		//LocalFileToList("\\resources\\config.txt");
		//getCurrentTime("2020-03-01");
	}

}
