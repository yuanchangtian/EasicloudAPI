<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%String path = request.getContextPath(); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"><base>
<title>欢迎访问物联网云平台系统</title>

<script type="text/javascript">
function getDevice() {
	 alert('go');
	 /* $.ajax({  
         type : "get",  //提交方式  
         url : "v1/go",//路径  
         data : {  
         },//数据，这里使用的是Json格式进行传输  
         success : function(result) {//返回数据根据结果进行相应的处理  
        	 alert('huilai');
         }  
     });   */
     window.location.href ="<%=path%>/v1/go";
	
}
</script>
<script type="text/javascript" src="js/jquery-1.8.3.min.js">
</script>
</head>
<body onload="getDevice()">
aaa
<%-- <form name="form1"action="<%=path %>/devices" method="get">
用户ID:<input name="user_id"type="text"id="user_id"maxlength="20">
设备ID:<input name="device_id"type="text"id="device_id"maxlength="20">
传感器ID:<input name="sensor_id"type="text"id="sensor_id"maxlength="20">
开始时间:<input name="begin"type="text"id="begin"maxlength="20">
结束时间:<input name="end"type="text"id="end"maxlength="20">
<input name="button"type="submit"value="显示">
</form> --%>
</body>
</html>