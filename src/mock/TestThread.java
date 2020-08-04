package mock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.capitalbio.common.util.JsonUtil;



public class TestThread extends Thread{

	public static List<String> configList; //配置文件登录用户名、密码、病毒编码、病毒类型、模拟随机数最小值、模拟随机数最大值、日期集合
	public static List<String> hospitalList; //医院列表
	public static String pathogenCode; //病毒编码
	public static String pathogenType; //病毒类型
	public static String[] time;  //日期集合年月日
	public static int minCount; //模拟随机数最小值
	public static int maxCount; //模拟随机数最大值
	public static String logingName; //登录用户名
	public static String password; //密码
	public static Map<String,String> mapHeader; //token信息
	
	//测试环境西安
//	public static final String REMOVE_DATA_URL="http://skynet-china.capitalbiobigdata.com:8084/skynetapi/rest/data/dropAllRecord";
//	public static final String SAVE_DATA_URL="http://skynet-china.capitalbiobigdata.com:8084/skynetapi/rest/data/saveTestingResult";
//	public static final String LOGIN_URL="http://skynet-china.capitalbiobigdata.com:8084/skynetapi/rest/user/login";
	
	//测试环境全国
//	public static final String REMOVE_DATA_URL="http://skynet-china.capitalbiobigdata.com:8085/skynetapi/rest/data/dropAllRecord";
//	public static final String SAVE_DATA_URL="http://skynet-china.capitalbiobigdata.com:8085/skynetapi/rest/data/saveTestingResult";
//	public static final String LOGIN_URL="http://skynet-china.capitalbiobigdata.com:8085/skynetapi/rest/user/login";
	
	//正式环境全国
	public static final String REMOVE_DATA_URL="https://skynet.capitalbiobigdata.com/skynetapi/rest/data/dropAllRecord";
	public static final String SAVE_DATA_URL="https://skynet.capitalbiobigdata.com/skynetapi/rest/data/saveTestingResult";
	public static final String LOGIN_URL="https://skynet.capitalbiobigdata.com/skynetapi/rest/user/login";
	
	//正式环境北京
//	public static final String REMOVE_DATA_URL="https://skynet-bj.capitalbiobigdata.com/skynetapi/rest/data/dropAllRecord";
//	public static final String SAVE_DATA_URL="https://skynet-bj.capitalbiobigdata.com/skynetapi/rest/data/saveTestingResult";
//	public static final String LOGIN_URL="https://skynet-bj.capitalbiobigdata.com/skynetapi/rest/user/login";
	
	//正式环境西安
//	public static final String REMOVE_DATA_URL="https://skynet-xa.capitalbiobigdata.com/skynetapi/rest/data/dropAllRecord";
//	public static final String SAVE_DATA_URL="https://skynet-xa.capitalbiobigdata.com/skynetapi/rest/data/saveTestingResult";
//	public static final String LOGIN_URL="https://skynet-xa.capitalbiobigdata.com/skynetapi/rest/user/login";
	
	
	//正式环境成都
//	public static final String REMOVE_DATA_URL="https://skynet-cd.capitalbiobigdata.com/skynetapi/rest/data/dropAllRecord";
//	public static final String SAVE_DATA_URL="https://skynet-cd.capitalbiobigdata.com/skynetapi/rest/data/saveTestingResult";
//	public static final String LOGIN_URL="https://skynet-cd.capitalbiobigdata.com/skynetapi/rest/user/login";

	
	public void run() {
		List<String> list=getListData();
		List<JSONObject> jsonList=creatorTestingData(list);
		try {
			FileUtils.removeData("http://49.4.3.52:8084/skynetapi/rest/data/dropAllRecord");
			TestThread.sleep(8000);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		for(int i=0;i<jsonList.size();i++) {
			 try {
	               FileUtils.sendRequest("http://49.4.3.52:8084/skynetapi/rest/data/saveTestingResult", jsonList.get(i).toJSONString());
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            } catch (Exception e) {
					e.printStackTrace();
				}
	             
		}
		
    }
	
	
	/**
	 * 初始化数据
	 */
	public void initData() {
		configList=FileUtils.LocalFileToList("\\resources\\config.txt");
		hospitalList=FileUtils.LocalFileToList("\\resources\\hospital.txt");
		if(configList!=null&&configList.size()>=0) {
			String[] split = configList.get(0).split(":");
			logingName=split[0];
			password=split[1];
			pathogenCode=split[2];
			pathogenType=split[3];
			minCount=Integer.parseInt(split[4]);
			maxCount=Integer.parseInt(split[5]);
			time=split[6].split(",");
		}
		mapHeader=getHeader();
		System.out.println(mapHeader.toString());
	}
	
	/** 获取该帐号的userId和token信息 **/
	@SuppressWarnings("unchecked")
	public  Map<String,Object> getToken(String url,Map<String, Object> map) {
		Map<String, Object> mapResult = new HashMap<String,Object>();
		try {
			String sendGetRequest = HttpClientUtil.post1(LOGIN_URL, map, null);
			Map<String, Object> mapSendGetRequest = JsonUtil.jsonToMap(sendGetRequest);
			if (mapSendGetRequest == null || mapSendGetRequest.size() < 1) {
				mapResult.put("msg", "获取token失败！");
			} else if (mapSendGetRequest.get("dataMap") == null ) {
				mapResult.put("msg", "获取token失败！");
			}
			Map<String, Object> dataMap = (Map<String, Object>) mapSendGetRequest.get("data");
			if (dataMap.get("userId") != null && StringUtils.isNotEmpty(dataMap.get("userId").toString())
					&& dataMap.get("token") != null && StringUtils.isNotEmpty(dataMap.get("token").toString())) {
				mapResult.put("userId", dataMap.get("userId"));
				mapResult.put("token", dataMap.get("token"));
			} else {
				if (mapResult.get("message") != null) {
					mapResult.put("msg", mapResult.get("message"));
				} else {
					mapResult.put("msg", "获取token失败！");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			mapResult.put("msg", "获取token失败！");
		}
		return mapResult;
	}
	
	/*
	 * 获取token信息
	 * 将token放入header
	 */
	public  Map<String,String> getHeader(){
		Map<String,Object> paramsMap= new HashMap<String,Object>();
		paramsMap.put("userName", logingName);
		paramsMap.put("password", password);
		Map<String,Object> mapTokenResult=getToken(LOGIN_URL,paramsMap);
		Map<String, String> mapHeader = new HashMap<String,String>();
		mapHeader.put("userId", mapTokenResult.get("userId").toString());
		mapHeader.put("token", mapTokenResult.get("token").toString());
		return mapHeader;
	}
	
	/**
	 * 删除数据
	 */
	public void removeData() {
		try {
			HttpClientUtil.get(REMOVE_DATA_URL, null, mapHeader);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	
	/**
	 * 新增数据
	 */
	public void saveData() {
		List<String> list=getListData();
		List<JSONObject> jsonList=creatorTestingData(list);
		for(int i=0;i<jsonList.size();i++) {
			 try {
	               FileUtils.sendRequest(SAVE_DATA_URL, jsonList.get(i).toJSONString(),mapHeader);
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            } catch (Exception e) {
					e.printStackTrace();
				}
	             
		}
	}
	
	public static  List<JSONObject> creatorTestingData(List<String> dataList) {
		List<JSONObject> array = new ArrayList<>();
		Random random=new java.util.Random();
		for (String string : dataList) {
			int count=(int)(minCount+Math.random()*((int)maxCount-(int)minCount+1));
			String[] split = string.split(":");
			for(int i=0;i<count;i++) {
				JSONObject data = new JSONObject(true);
				List<JSONObject> arrayList = new ArrayList<JSONObject>();
				JSONObject pathogenResult=new JSONObject(true);
				pathogenResult.put("pathogenCode", pathogenCode);
				pathogenResult.put("pathogenType", pathogenType);
				arrayList.add(pathogenResult);
				data.put("pathogenResult", arrayList);
				data.put("deviceId", "bioeh");
				data.put("chipId", "20200224");
				data.put("hospitalCode", split[1]);
				data.put("patientId", "BJ-"+pathogenCode+"-"+UUID.randomUUID().toString());
				data.put("patientAge", (random.nextInt(100)+1));
				data.put("idType", (random.nextInt(4)+1));
				data.put("idNumber", UUID.randomUUID().toString());
				//data.put("patientGender", (random.nextInt(2)+1));
				data.put("patientGender", 1);
				data.put("sampleTypeCode", (random.nextInt(8)+1));
				data.put("diagnosisDate", FileUtils.getCurrentTime(split[0]));
				array.add(data);
			}
		}
		return array;
	}
	
	public static  List<String> creatorTestingData2(List<String> dataList) {
		List<String> array = new ArrayList<>();
		Random random=new java.util.Random();
		for (String string : dataList) {
			int count=(int)(minCount+Math.random()*((int)maxCount-(int)minCount+1));
			String[] split = string.split(":");
			for(int i=0;i<count;i++) {
				JSONObject data = new JSONObject(true);
				List<JSONObject> arrayList = new ArrayList<JSONObject>();
				JSONObject pathogenResult=new JSONObject(true);
				pathogenResult.put("pathogenCode", pathogenCode);
				pathogenResult.put("pathogenType", pathogenType);
				arrayList.add(pathogenResult);
				data.put("pathogenResult", arrayList);
				data.put("deviceId", "bioeh");
				data.put("chipId", "20200224");
				data.put("hospitalCode", split[1]);
				data.put("patientId", "BJ-"+pathogenCode+"-"+UUID.randomUUID().toString());
				data.put("patientAge", (random.nextInt(100)+1));
				data.put("idType", (random.nextInt(4)+1));
				data.put("idNumber", UUID.randomUUID().toString());
				data.put("patientGender", (random.nextInt(2)+1));
				data.put("sampleTypeCode", (random.nextInt(8)+1));
				data.put("diagnosisDate", FileUtils.getCurrentTime(split[0]));
				array.add(data.toJSONString());
			}
		}
		return array;
	}
	
	public static  List<JSONObject> creatorTestingData() {
		List<JSONObject> array = new ArrayList<>();
		JSONObject data = new JSONObject(true);
		JSONObject pathogenResult=new JSONObject(true);
		pathogenResult.put("pathogenCode", pathogenCode);
		pathogenResult.put("pathogenType", pathogenType);
		data.put("pathogenResult", pathogenResult);
		data.put("deviceId", "bioeh");
		data.put("chipId", "20200224");
		data.put("hospitalCode", 104);
		data.put("patientId", "BJ-"+pathogenCode+"-"+System.currentTimeMillis());
		data.put("patientAge", new Random().nextInt(100)+1);
		data.put("idType", new Random().nextInt(4)+1);
		data.put("idNumber", System.currentTimeMillis()+new Random().nextInt(1000));
		data.put("patientGender", new Random().nextInt(3)+1);
		data.put("sampleTypeCode", new Random().nextInt(9)+1);
		data.put("diagnosisDate", System.currentTimeMillis());
		array.add(data);
		System.out.println("数组："+array.size());
		return array;
	}
	
	public static List<String> getListData(){
		List<String> dataList=new ArrayList<>();
		try {
			String datastr="";
			if(hospitalList!=null&&hospitalList.size()>=0) {
				for(String str:time) {
					for(String hospital:hospitalList) {
						datastr=str+":"+hospital;
						dataList.add(datastr);
					}
				}
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return dataList;
	}
	
	public static void main(String[] args) throws Exception {
		TestThread test=new TestThread();
		test.initData();
		test.removeData();
		//test.saveData();
	}
}
