package com.family_financial_system;

//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;


import com.baidu.aip.ocr.AipOcr;
import com.baidubce.auth.DefaultBceCredentials;
import com.baidubce.services.bos.BosClient;
import com.baidubce.services.bos.BosClientConfiguration;
import com.baidubce.services.bos.model.BosObjectSummary;
import com.baidubce.services.bos.model.BucketSummary;
import com.baidubce.services.bos.model.ListObjectsResponse;
import com.family_financial_system.bean.bill;

import com.family_financial_system.dao.userDao;
import com.family_financial_system.util.parseDate;
import org.json.JSONArray;
import org.json.JSONException;
import com.family_financial_system.service.userService;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FamilyFinancialSystemApplicationTests {
	@Autowired
	userService userService;
	@Autowired
	userDao userDao;




	public static final String APP_ID = "28068422";
	public static final String API_KEY = "SIBjehEscIjOzv9qNIhuaGR5";
	public static final String SECRET_KEY = "3G6PtLIFwK6Ha5VGOw5Kb7g04Apt8tNQ";
//	public static final String APP_ID = "739d63b94c1e468286bc0459851f3841";
//	public static final String API_KEY = "EC30jaFdCiCxGEnXXZLbWOrw";
//	public static final String SECRET_KEY = "yKBDmUm2yX8mYCXwH1ftm2bu75CGe7Bu";
	@Test
	void contextLoads() throws ParseException {
		long startTime=System.currentTimeMillis();

		AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);

		HashMap<String, String> options = new HashMap<String, String>();
		options.put("detect_direction", "true");
		options.put("probability", "true");
		String image = "C:\\Users\\ROG\\Desktop\\半字4.jpg";
		JSONObject res = client.accurateGeneral(image, options);
		JSONArray result = res.getJSONArray("words_result");
		System.out.println(result.toString(2));
		List<String> typeList=userService.getType();
		List<Integer> indexList=new ArrayList<>();


		for(int i=6;i<result.length();i++){
			for (int j=0;j<typeList.size();j++){
				if((result.getJSONObject(i).get("words").toString()).equals(typeList.get(j))){
					indexList.add(i);
				}
			}
		}

		parseDate parseDate=new parseDate();


		String reg="[^0-9]+(.[^0-9])+[^0-9]";
		String testReg="-?[0-9]+.?[0-9]*";
		String title = null;
		String money=null;
		String type=null;
		String time=null;
		String remark=null;

		if(Double.valueOf(result.getJSONObject(indexList.get(0)-2).getJSONObject("probability").get("min").toString())<0.9||
				Double.valueOf(result.getJSONObject(indexList.get(0)-1).getJSONObject("probability").get("min").toString())<0.9||
				Double.valueOf(result.getJSONObject(indexList.get(0)).getJSONObject("probability").get("min").toString())<0.9){
			indexList.remove(0);
		}
		for(int i=0;i<indexList.size();i++){
			title=result.getJSONObject(indexList.get(i)-2).get("words").toString();
			if(parseDate.parseDate(result.getJSONObject(indexList.get(i)+1).get("words").toString().substring(2))==null){
				time=result.getJSONObject(indexList.get(i)+2).get("words").toString();
				remark=result.getJSONObject(indexList.get(i)+1).get("words").toString();
			}else{
				time=result.getJSONObject(indexList.get(i)+1).get("words").toString();
				remark="";
			}
			money=result.getJSONObject(indexList.get(i)-1).get("words").toString();
			type=result.getJSONObject(indexList.get(i)).get("words").toString();
			if(parseDate.parseDate(time.substring(2))!=null){
				if(!money.matches(testReg)) {
					String select = result.getJSONObject(indexList.get(i) - 1).get("words").toString().replace("…","");
					Pattern p = Pattern.compile(reg);
					Matcher m = p.matcher(select);
					money = select.charAt(select.indexOf(m.replaceAll("").trim().substring(0, 1)) - 1) + m.replaceAll("").trim();
					title = select.substring(0, select.indexOf(m.replaceAll("").trim().substring(0, 1)) - 1);
				}
				System.out.println(type + " " + title + " " + money + " " + timeTranform(time)+" "+remark);

			}
		}




		long endTime=System.currentTimeMillis();
		System.out.println("运行时间:"+(endTime-startTime)+"ms");

//		Jedis jedis= JedisPoolUtils.getJedis();
//		List<message> messages = null;
//		System.out.println(messages);

//		Jedis jedis= JedisPoolUtils.getJedis();
//		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String msgTime = df.format(new Timestamp(System.currentTimeMillis()));
//		jedis.set("time",msgTime);
//
//		System.out.println(new Timestamp(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(jedis.get("time").toString()).getTime()));


//		JSONArray jsonArray=new JSONArray(jedis.get("messageList"));
//
//		List<message> messages=new ArrayList<>();
//
//		for(int i=0;i<jsonArray.length();i++){
//			message mess=new message(null,jsonArray.getJSONObject(i).get("message").toString(),Integer.valueOf(jsonArray.getJSONObject(i).get("Sender").toString()),Integer.valueOf(jsonArray.getJSONObject(i).get("Receiver").toString()));
//			messages.add(mess);
//		}
//		System.out.println(messages);
//		jedis.close();
//		userDao.messageData(messages);

//		HashMap<Integer,String> userState=new HashMap<Integer,String>();
//		Jedis jedis= JedisPoolUtils.getJedis();
//		userState.put(1,"online");
//		jedis.set("userState",JSON.toJSONString(userState));
//		HashMap<Integer,String> hashMap= JSON.parseObject(jedis.get("userState"),HashMap.class);
//		System.out.println(hashMap.get(4));

//        Jedis jedis= JedisPoolUtils.getJedis();
//        HashMap<Integer,String> hashTempMap= JSON.parseObject(jedis.get("userState"),HashMap.class);
//		org.json.JSONObject jsonObject=new org.json.JSONObject(hashTempMap);
//		System.out.println(jsonObject.toString());
//
//
//		String mess="{"+"\"id\":1,"+"\"message\":"+"\"online\""+"}";




	}

	public Timestamp timeTranform(String time){
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
		String dateNow=null;
		StringBuffer stringBuffer=new StringBuffer();
		switch (time.substring(0,2)){
			case "今天":dateNow=simpleDateFormat.format(new Timestamp(System.currentTimeMillis()));break;
			case "昨天":dateNow=String.valueOf(cal.get(Calendar.YEAR))+"-"+String.valueOf(cal.get(Calendar.MONTH)+1)+"-"+String.valueOf(cal.get(Calendar.DATE)-1);break;
			default:stringBuffer.append(time.substring(0,5)+" ");stringBuffer.append(time.substring(5));dateNow=stringBuffer.toString();break;
		}
		if(time.substring(0,2).equals("今天")||time.substring(0,2).equals("昨天")){
			return Timestamp.valueOf(dateNow+" "+time.substring(2).replace("：",":")+":"+"00");
		}
		return Timestamp.valueOf((cal.get(Calendar.YEAR)+"-").concat(dateNow)+":"+"00");

	}


	public void sample(AipOcr client) {
		// 传入可选参数调用接口
		HashMap<String, String> options = new HashMap<String, String>();
		options.put("recognize_granularity", "big");
		options.put("detect_direction", "true");
		options.put("vertexes_location", "true");
		options.put("probability", "true");


		// 参数为本地图片路径
		String image = "";
		JSONObject res = client.accurateGeneral(image, options);
		System.out.println(res.toString(2));

		// 参数为本地图片二进制数组
//		byte[] file = readImageFile(image);
//		res = client.accurateGeneral(file, options);
//		System.out.println(res.toString(2));

	}
	@Test
	public void testJsonWriteFile() throws IOException, JSONException {

//		JsonConfig jsonConfig = new JsonConfig();
//		jsonConfig.registerJsonValueProcessor(Date.class,new com.family_financial_system.util.JsonDateValueProcessor());
//		JSONArray json = JSONArray.fromObject(bill,jsonConfig);
//		System.out.println(JSON.parse(JSONObject.toJSONStringWithDateFormat(bill,"yyyy-MM-dd HH:mm:ss")).toString());
	}

//	@Test
//	public void testLogin(){
////		Scanner scanner=new Scanner(System.in);
////		System.out.println("请输入账户名:");
////		String username=scanner.nextLine();
////		System.out.println("请输入密码:");
////		String password=scanner.nextLine();
//		String username="伦剑曦";
//		String password="123";
//		System.out.println("请输入账户名:");
//		System.out.println(username);
//		System.out.println("请输入密码:");
//		System.out.println(password);
//		testUser testUser=userDao.loginIn(username,password);
//		if(testUser!=null){
//			System.out.println("用户登陆成功");
//			System.out.println(testUser.getUsername()+"是Java班的");
//		}else {
//			System.out.println("用户名或密码错误");
//		}
//
//
//	}

	@Test
	public void test(){
		System.out.println(userService.getUserAvatar("小曾"));

//		String ACCESS_KEY_ID = "97246a67c14d46dfa52e24f6fcb5c41b";                   // 用户的Access Key ID
//		String SECRET_ACCESS_KEY = "c4e6bb8743284f01a2d739dbe6ba40b8";           // 用户的Secret Access Key
//		String ENDPOINT = "gz.bcebos.com";                               // 用户自己指定的域名
//
//		BosClientConfiguration config = new BosClientConfiguration();
//		config.setCredentials(new DefaultBceCredentials(ACCESS_KEY_ID,SECRET_ACCESS_KEY));
//		config.setEndpoint(ENDPOINT);
//		BosClient client = new BosClient(config);
//		listObjects(client,"user-avatar-bucket","小曾");
	}
	public void listObjects(BosClient client, String bucketName,String userName) {

		// 获取指定Bucket下的所有Object信息
		ListObjectsResponse listing = client.listObjects(bucketName);

		// 遍历所有Object
		for (BosObjectSummary objectSummary : listing.getContents()) {
			if(Objects.equals(objectSummary.getKey(), ""+userName+".gif")){
				System.out.println(generatePresignedUrl(client,bucketName,objectSummary.getKey()));
				return;
			}
		}

	}
	public String generatePresignedUrl(BosClient client, String bucketName, String objectKey) {

		//指定用户需要获取的Object所在的Bucket名称、该Object名称、URL的有效时长
		URL url = client.generatePresignedUrl(bucketName, objectKey, -1);


		return url.toString();
	}
}
