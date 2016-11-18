<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>统计分析</title>
<link rel="stylesheet" href="CSS/main_style.css" />
<script language="javascript" src="js/dataProcess.js"></script>
<script type="text/javascript"
	src="http://cdn.hcharts.cn/jquery/jquery-1.8.3.min.js"></script>
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
					<li id="mylist001" class="mylist"><a
						href="<%=path%>/monitor.jsp" style="font-size: 20px;"
						target="main_content" onClick="showRight('001')">实时设备监控</a></li>
					<li id="mylist002" class="mylist"><a
						href="<%=path%>/historyData.jsp" style="font-size: 20px;"
						target="main_content" onclick="showRight('002')">历史设备查询</a></li>
					<li id="mylist003" class="mylist"><a
						href="<%=path%>/distributionStatistics.jsp" style="font-size: 20px;"
						target="main_content" onclick="showRight('003')">分布统计</a></li>
					<li id="mylist004" class="mylist"><a
						href="<%=path%>/proportionStatistical.jsp" style="font-size: 20px;"
						target="main_content" onclick="showRight('004')">比例统计</a></li>
					<li id="mylist005" class="mylist"><a
						href="<%=path%>/sensorComparision.jsp" style="font-size: 20px;"
						target="main_content" onclick="showRight('005')">传感器分析对比</a></li>
				</ul>
			</div>
			<div class="content">
				<iframe name="main_content" id="main_content"
					src="/EasiCloudAPI/monitor.jsp"
					style="margin-top: 5px; width: 100%; height: 800px;"
					frameborder="0"></iframe>
			</div>
		</div>
	</div>
</body>
</html>