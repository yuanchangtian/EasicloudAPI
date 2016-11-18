<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>欢迎访问互联网云平台</title>
<link rel="stylesheet" href="CSS/main_style.css"/>
<script language="javascript" src="js/dataProcess.js"></script>
<script type="text/javascript" src="http://cdn.hcharts.cn/jquery/jquery-1.8.3.min.js"></script>
<script src="http://cdn.hcharts.cn/highcharts/highcharts.js"></script>
<script src="http://cdn.hcharts.cn/highcharts/highcharts-more.js"></script>
<script src="http://cdn.hcharts.cn/highcharts/modules/exporting.js"></script>
<script type="text/javascript">
	function showRight(id) {
		$(".mylist").removeClass("current");
		$("#mylist" + id).addClass("current");
	}
</script>
</head>
<body>
	<%@ include file="top.jsp"%>
	<div class="main">
		<div class="bd">
			<div class="sidebar">
				<h2
					style="text-align: center; width: 100%; margin-top: 10px; border-bottom: solid 1px gray; color: #5080D8; padding-bottom: 7px;">
					菜单列表</h2>
				<ul type="circle">
					<li id="mylist001" class="mylist"><a href="<%=path%>/showdevices"
						style="font-size: 20px;" target="main_content"
						onClick="showRight('001')"> 我的设备 </a></li>
					<li id="mylist002" class="mylist"><a href="<%=path%>/showsensors"
						style="font-size: 20px;" target="main_content"
						onclick="showRight('002')"> 我的传感器</a></li>
				</ul>
			</div>
			<div class="content">
				<iframe name="main_content" id="main_content" src="/EasiCloudAPI/mydevice.jsp"
					style="margin-top: 5px; width: 100%; height: 800px;"
					frameborder="0"></iframe>
			</div>
		</div>
	</div>
</body>
</html>