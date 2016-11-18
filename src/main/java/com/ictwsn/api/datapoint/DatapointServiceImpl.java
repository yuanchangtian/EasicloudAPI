package com.ictwsn.api.datapoint;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONArray;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.ictwsn.api.sensor.SensorDao;
import com.ictwsn.utils.BaseDao;
import com.ictwsn.utils.Tools;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

@Service
public class DatapointServiceImpl extends BaseDao implements DatapointService{
	
	public int insertToMongodb(String user_id,DBObject dbObject) {
		//插入到Mongodb数据库
		System.out.println(this.mongoTemplate==null);
		System.out.println(this.mongoTemplate.getCollectionNames());
		this.mongoTemplate.insert(dbObject, user_id);
		return 0;
	}
	//创建初始查询条件
	public int getDatapointsCount(int user_id,int sensor_id,Date start,Date end){
		Criteria criatira = new Criteria();
		if(sensor_id>0&&start!=null&&end!=null)
			criatira.andOperator(Criteria.where("sensor_id").is(sensor_id), 
			Criteria.where("timestamp").gte(start).andOperator(Criteria.where("timestamp").lte(end)));
		else if(sensor_id>0&&start==null&&end==null)
			criatira.andOperator(Criteria.where("sensor_id").is(sensor_id));
		else if(sensor_id>0&&start!=null)
			criatira.andOperator(Criteria.where("sensor_id").is(sensor_id), Criteria.where("timestamp").gte(start));
		else if(sensor_id>0&&end!=null)
			criatira.andOperator(Criteria.where("sensor_id").is(sensor_id), Criteria.where("timestamp").lte(end));
		else criatira = null;
		return (int)this.mongoTemplate.count(new Query(criatira), String.valueOf(user_id));
	}
	
	public int getAllDatapointsCount(int user_id)
	{
		return (int)this.mongoTemplate.count(null, String.valueOf(user_id));
	}
	
	public String getDatapoints(int user_id, int sensor_id,
			Date start, Date end,String object_id) {
		Criteria criatira = new Criteria();
		if(sensor_id>0&&start!=null&&end!=null)
		{
			criatira.andOperator(Criteria.where("sensor_id").is(sensor_id), 
			Criteria.where("timestamp").gte(start).andOperator(Criteria.where("timestamp").lte(end)));
		}
		else if(sensor_id>0&&start==null&&end==null)
			criatira.andOperator(Criteria.where("sensor_id").is(sensor_id));
		else if(sensor_id>0&&start!=null)
			criatira.andOperator(Criteria.where("sensor_id").is(sensor_id), Criteria.where("timestamp").gte(start));
		else if(sensor_id>0&&end!=null)
			criatira.andOperator(Criteria.where("sensor_id").is(sensor_id), Criteria.where("timestamp").lte(end));
		else criatira = null;
		
		List<DBObject> list = this.mongoTemplate.find(new Query(criatira),DBObject.class,String.valueOf(user_id));
		
		System.out.println(list.size());
		JSONArray jsonArray = new JSONArray();
		for(DBObject d : list)
		{
			//不返回数据id
			if(object_id==null||object_id.equals("no"))
			d.removeField("_id");
			d.put("timestamp", Tools.dateToString((Date)d.get("timestamp")));
			d.removeField("_class");
			jsonArray.add(d.toString());
		}
		return jsonArray.toString();
	}
	public int judgeUserIdSensorId(int user_id, int device_id, int sensor_id) {
		DatapointDao datapointDao = this.sqlSessionTemplate.getMapper(DatapointDao.class);
		return datapointDao.judgeUserIdSensorId(user_id, device_id, sensor_id);
	}
	public String datapointShow(int user_id, int sensor_id, Date timestamp) {
		Criteria criatira = new Criteria();
		criatira.andOperator(Criteria.where("sensor_id").is(sensor_id), Criteria.where("timestamp").is(timestamp));
		
		List<DBObject> list = this.mongoTemplate.find(new Query(criatira),DBObject.class,String.valueOf(user_id));
		JSONArray jsonArray = new JSONArray();
		for(DBObject d : list)
		{
			d.removeField("_id");
			d.put("timestamp", Tools.dateToString((Date)d.get("timestamp")));
			jsonArray.add(d.toString());
		}
		return jsonArray.toString();
	}
	public String insertBatchToMongodb(String user_id,List<DBObject> list_dbObject) {
		//批量插入到Mongodb数据库
		for(DBObject dbObject : list_dbObject)
		{
			this.mongoTemplate.insert(dbObject, user_id);
		}
		return null;
	}
	public String datapointUpdate(String user_id,Date timestamp,DBObject dbObject) {
		Criteria criatira = new Criteria();
		criatira.andOperator(Criteria.where("timestamp").is(timestamp));
		Update update = Update.fromDBObject(dbObject);  
		this.mongoTemplate.upsert(new Query(criatira),
				update,DBObject.class,user_id);
		return "已执行操作";
	}
	public String datapointDelete(String user_id, int sensor_id, Date timestamp) {
		Criteria criatira = new Criteria();
		criatira.andOperator(Criteria.where("sensor_id").is(sensor_id), Criteria.where("timestamp").is(timestamp));
		this.mongoTemplate.remove(new Query(criatira), user_id);
		return "已执行操作";
	}
	public int judgeUserIdSensorId(int user_id, int sensor_id) {
		DatapointDao datapointDao = this.sqlSessionTemplate.getMapper(DatapointDao.class);
		return datapointDao.judgeUserIdSensorIdSimple(user_id, sensor_id);
	}
	public String datapointShow(int user_id, String object_id,String flag) {
		Criteria criatira = new Criteria();
		criatira.andOperator(Criteria.where("_id").is(new ObjectId(object_id)));
		List<DBObject> list = this.mongoTemplate.find(new Query(criatira),DBObject.class,String.valueOf(user_id));
		if(list.size()<=0) return "未查询到相关数据";
		DBObject d = list.get(0);
		d.put("timestamp", Tools.dateToString((Date)d.get("timestamp")));
		//当flag为空或者为no时，不返回对象id
		if(flag==null||flag.equals("no")) 
			d.removeField("_id");
		return d.toString();
	}
	public String datapointUpdate(String user_id, String object_id,DBObject dbObject) {
		Criteria criatira = new Criteria();
		criatira.andOperator(Criteria.where("_id").is(new ObjectId(object_id)));
		Update update = Update.fromDBObject(dbObject);  
		this.mongoTemplate.upsert(new Query(criatira),
				update,DBObject.class,user_id);
		return "已执行操作";
	}
	public String datapointDelete(String user_id, String object_id) {
		Criteria criatira = new Criteria();
		criatira.andOperator(Criteria.where("_id").is(new ObjectId(object_id)));
		this.mongoTemplate.remove(new Query(criatira), user_id);
		return "已执行操作";
	}
	public JSONArray getDatapointsCondition(String cons, String exps, String vals,String user_id,String object_id,int sensor_id) {
		String[] con = cons.split(";");
		String[] exp = exps.split(";");
		String[] val = vals.split(";");
		if(con.length!=exp.length) return null;
		if(exp.length!=val.length) return null;
		//条件查询
		DBCollection dbCollection = this.mongoTemplate.getCollection(user_id);  
		//判断是不是属于公用信息
		SensorDao sensorDao = this.sqlSessionTemplate.getMapper(SensorDao.class);
		String public_data = sensorDao.sensorDataShow(Integer.valueOf(user_id), sensor_id);
		
		//填充查询条件
		BasicDBObject query =new BasicDBObject();
		String con_values = "values.";
		for(int i=0;i<con.length;i++)
		{
			//条件查询公有信息
			if(public_data.contains(con[i]))
			{
				if(exp[i].equals("lt"))
					query.put(con[i], new BasicDBObject("$lt", Double.valueOf(val[i])));
				else if(exp[i].equals("lte"))
					query.put(con[i], new BasicDBObject("$lte", Double.valueOf(val[i])));
				else if(exp[i].equals("gt"))
					query.put(con[i], new BasicDBObject("$gt", Double.valueOf(val[i])));
				else if(exp[i].equals("gte"))
					query.put(con[i], new BasicDBObject("$gte", Double.valueOf(val[i])));
				else if(exp[i].equals("ne"))
					query.put(con[i], new BasicDBObject("$ne", Double.valueOf(val[i])));
				else if(exp[i].equals("is"))
				{
					System.out.println("wozhixingle"+Integer.valueOf(val[i]));
					query.put(con[i], Integer.valueOf(val[i]));
				}
					
			}
			//条件查询值信息
			else
			{
				if(exp[i].equals("lt"))
					query.put(con_values+con[i], new BasicDBObject("$lt", Double.valueOf(val[i])));
				else if(exp[i].equals("lte"))
					query.put(con_values+con[i], new BasicDBObject("$lte", Double.valueOf(val[i])));
				else if(exp[i].equals("gt"))
					query.put(con_values+con[i], new BasicDBObject("$gt", Double.valueOf(val[i])));
				else if(exp[i].equals("gte"))
					query.put(con_values+con[i], new BasicDBObject("$gte", Double.valueOf(val[i])));
				else if(exp[i].equals("ne"))
					query.put(con_values+con[i], new BasicDBObject("$ne", Double.valueOf(val[i])));
				else if(exp[i].equals("is"))
					query.put(con_values+con[i], Double.valueOf(val[i]));
			}
		}
		DBCursor cursor = dbCollection.find(query);
		List<DBObject> list_dbobject = new ArrayList<DBObject>();
		while(cursor.hasNext()){
			list_dbobject.add(cursor.next());
		}
		/*
		Criteria criatira = new Criteria();
		String external = "values.";
		for(int i=0;i<con.length;i++)
		{
			if(exp[i].equals("lt"))
			criatira.andOperator(Criteria.where(external+con[i]).lt(Double.valueOf(val[i])));
			else if(exp[i].equals("lte"))
			criatira.andOperator(Criteria.where(external+con[i]).lte(Double.valueOf(val[i])));
			else if(exp[i].equals("gt"))
			{
				criatira.
				criatira.andOperator(Criteria.where(external+con[i]).gt(Double.valueOf(val[i])));
			}
			else if(exp[i].equals("gte"))
			criatira.andOperator(Criteria.where(external+con[i]).gte(Double.valueOf(val[i])));
			else if(exp[i].equals("ne"))
			criatira.andOperator(Criteria.where(external+con[0]).ne(Double.valueOf(val[i])));
		}*/
//		List<DBObject> list = this.mongoTemplate.find(new Query(criatira),DBObject.class,String.valueOf(user_id));
		JSONArray jsonArray = new JSONArray();
		for(DBObject d : list_dbobject)
		{
			//不返回数据id
			if(object_id==null||object_id.equals("no"))
			d.removeField("_id");
			d.put("timestamp", Tools.dateToString((Date)d.get("timestamp")));
			jsonArray.add(d.toString());
		}
		return jsonArray;
	}
	
	

}
