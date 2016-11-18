package com.ictwsn.api.datapoint;

import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;

import com.mongodb.DBObject;

public interface DatapointService {
	public int insertToMongodb(String user_id,DBObject dbObject);
	public int getDatapointsCount(int user_id,int sensor_id,Date start,Date end);
	public int getAllDatapointsCount(int user_id);
	public String getDatapoints(int user_id,int sensor_id,Date start,Date end,String object_id);
	public int judgeUserIdSensorId(int user_id,int device_id,int sensor_id);
	public String datapointShow(int user_id,int sensor_id,Date timestamp);
	public String insertBatchToMongodb(String user_id,List<DBObject> list_dbObject);
	public String datapointUpdate(String user_id,Date timestamp,DBObject dbObject);
	public String datapointDelete(String user_id,int sensor_id,Date timestamp);
	//新增加的数据点接口
	public int judgeUserIdSensorId(int user_id,int sensor_id);
	public String datapointShow(int user_id,String object_id,String flag);
	public String datapointUpdate(String user_id,String object_id,DBObject dbObject);
	public String datapointDelete(String user_id,String object_id);
	//外接条件查询
	public JSONArray getDatapointsCondition(String con,String exp,String val,String user_id,String object_id,int sensor_id);
}
