package mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import com.alibaba.fastjson.JSONObject;


//mock.putData("C:\Users\daweichen\Desktop\天网\脚本\data\63.txt");
//vars.put("params",jsonList.size());
public class TestMock{

	public static List<String> configList;
	public static List<String> hospitalList;
	public static String pathogenCode;
	public static String pathogenType;
	public static int minCount;
	public static int maxCount;
	public void test() {
		List<String> list=getListData();
		List<JSONObject> jsonList=creatorTestingData(list);
		System.out.println(jsonList.size());
		try {
			FileUtils.removeData("http://49.4.3.52:8085/skynetapi/rest/data/dropAllRecord");
//			Thread.sleep(8000);
			for(int i=0;i<1;i++) {
//				for(int i=0;i<jsonList.size();i++) {
	            FileUtils.sendRequest("http://49.4.3.52:8085/skynetapi/rest/data/saveTestingResult", jsonList.get(i).toJSONString());    
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
    }
	
	public void putData(String path) {
		List<String> list=getListData();
		List<JSONObject> jsonList=creatorTestingData(list);
		FileUtils.writeFile(path, jsonList);
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
				data.put("patientGender", (random.nextInt(3)+1));
				data.put("sampleTypeCode", (random.nextInt(9)+1));
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
				data.put("patientGender", (random.nextInt(3)+1));
				data.put("sampleTypeCode", (random.nextInt(9)+1));
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
		data.put("hospitalCode", 20832);
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
			configList=FileUtils.FileToList("D:\\eclipse-workspace\\mockRun\\src\\params\\config.txt");
			hospitalList=FileUtils.FileToList("D:\\eclipse-workspace\\mockRun\\src\\params\\hospital.txt");
			String datastr="";
			if(configList!=null&&configList.size()>=0&&hospitalList!=null&&hospitalList.size()>=0) {
				String[] split = configList.get(0).split(":");
				String[] time=split[4].split(",");
				minCount=Integer.parseInt(split[2]);
				maxCount=Integer.parseInt(split[3]);
				pathogenCode=split[0];
				pathogenType=split[1];
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
		new TestMock().test();
		//new TestMock().putData("D:\\eclipse-workspace\\mockTest\\src\\resources\\data.txt");
	}
	
	

}
