package com.ictwsn;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ictwsn.utils.BaseDao;
import com.ictwsn.utils.Tools;
import com.ictwsn.utils.redis.RedisOperation;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

@Controller
@RequestMapping("/mvc")
public class mvcController extends BaseDao{
	
	@SuppressWarnings("restriction")
	@Resource RedisOperation redisOperation;
	private static Logger logger = LoggerFactory.getLogger(mvcController.class);
 
	@RequestMapping("/jsontest")
    public String hello(){   
//    	System.out.println(this.mongoTemplate==null);
    	/*User u = new User();
    	u.setUsername("ruo");
    	u.setPassword("ran");
    	UserMapper mapper = this.sqlSessionTemplate.getMapper(UserMapper.class);
    	User user = mapper.selectUser(u);
    	System.out.println(user.getUsername());*/
    	
    	redisOperation.save("5a6bb6c65d70ae8bafc10b8bafd302e3ecadf023",10001);
    	redisOperation.save("f26a429471a59d48abf090e622d4484caf355410",10018);
    	int i = redisOperation.get("5a6bb6c65d70ae8bafc10b8bafd302e3ecadf023", Integer.class);
    	logger.info("用户值为{}",i);
//    	apiDao.run();
        return "index";
    }
	@RequestMapping("/insert")
    public String Test(){
//		this.redisOperation.save("5a6bb6c65d70ae8bafc10b8bafd302e3ecadf023", 10001);
		int i = this.redisOperation.get("5a6bb6c65d70ae8bafc10b8bafd302e3ecadf023", Integer.class);
		System.out.println(i);
        return null;
    }
    
    /*
     * MyBatis测试
     */
    @RequestMapping(value="/Test.do",method=RequestMethod.GET)
	public String TestUser(HttpServletRequest request,HttpServletResponse response)
//			@RequestParam(value="username",required=false)String username,
//			@RequestParam(value="password",required=false)String password)
	{
    	
    	
    	UserMapper mapper = this.sqlSessionTemplate.getMapper(UserMapper.class);
    	
    	String s = "",line = "";
    	BufferedReader br;
		try {
			br = new BufferedReader(new FileReader("C:\\Users\\Jhon\\Desktop\\JSON.txt"));
			try {
				while((line=br.readLine())!=null)
				{
					s += line;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		/*String strJson="{ '_id' : { '$oid' : '52fdbe36e97fee8601000000'}}";
    	strJson=s.replaceAll("\'", "\""); 
    	JSONObject jsonobject = JSONObject.fromObject(strJson);
    	jsonobject.put("lalala", 88888888);
    	 DBObject query = (BasicDBObject) JSON.parse(jsonobject.toString());*/
		
    	DBCollection coll = this.mongoTemplate.getCollection("runoob");
    	String time = "2016-04-24 10:23:56";
    	Date date = Tools.changeToDate8(time);
    	
    	DBObject ob_insert = new BasicDBObject();
    	ob_insert.put("date_time", date);
    	coll.insert(ob_insert);
    	 
    	Date fromDate = Tools.changeToDate8("2016-04-24 10:23:54");
    	Date toDate = Tools.changeToDate8("2016-04-24 23:23:54");
    	
    	BasicDBObject query = new BasicDBObject();
    	query.put("date_time", BasicDBObjectBuilder.start("$gte", fromDate).add("$lte", toDate).get());
    	DBCursor cursor = coll.find(query).sort(new BasicDBObject("date_time", -1));
    	while(cursor.hasNext()){
    	System.out.println(cursor.next());
    	}
    	logger.info("长度为："+cursor.size());
		return "index";
	}
    
    
}
