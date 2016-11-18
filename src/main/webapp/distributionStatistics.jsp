<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@  taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>分布统计</title>
<link rel="stylesheet" href="CSS/mydevice.css" />
<script type="text/javascript" src="js/dataProcess.js"></script>
<script type="text/javascript"
	src="http://cdn.hcharts.cn/jquery/jquery-1.8.3.min.js"></script>
<style type="text/css">
${
demo
.css
}
</style>
<script type="text/javascript">
	function getData() {
		$.ajax({
			url : "v1/devices",// 跳转到 action  
			data : {},
			type : 'get',
			cache : false,
			dataType : 'json',
			success : function(data) {
				//alert(data[0].device_location.lng);
				$("#haha").val(data[0].device_location.lng);
			},
			error : function() {
				alert("异常！");
			},
			headers : {
				"user_key" : "74mpksQf+4iUFZiHBRrrPQ==",
			}
		});
	}
</script>
<script type="text/javascript">
	$(function() {
		var dom = document.getElementById("container");
		var myChart = echarts.init(dom);
		var app = {};
		option = null;
		app.title = '坐标轴刻度与标签对齐';

		option = {
			color : [ '#3398DB' ],
			tooltip : {
				trigger : 'axis',
				axisPointer : { // 坐标轴指示器，坐标轴触发有效
					type : 'shadow' // 默认为直线，可选为：'line' | 'shadow'
				}
			},
			grid : {
				left : '3%',
				right : '4%',
				bottom : '3%',
				containLabel : true
			},
			xAxis : [ {
				type : 'category',
				data : [],
				axisTick : {
					alignWithLabel : true
				}
			} ],
			yAxis : [ {
				type : 'value'
			} ],
			series : [ {
				name : '数据点数',
				type : 'bar',
				barWidth : '60%',
				data : []
			} ]
		};
		;
		if (option && typeof option === "object") {
			myChart.setOption(option, true);
		}
	});
</script>
</head>
<body onload="getDeviceList()">
	<script type="text/javascript"
		src="http://echarts.baidu.com/gallery/vendors/echarts/echarts-all-3.js"></script>
	<script type="text/javascript"
		src="http://echarts.baidu.com/gallery/vendors/echarts/extension/dataTool.min.js"></script>
	<script type="text/javascript"
		src="http://echarts.baidu.com/gallery/vendors/echarts/map/js/china.js"></script>
	<script type="text/javascript"
		src="http://echarts.baidu.com/gallery/vendors/echarts/map/js/world.js"></script>
	<script type="text/javascript"
		src="http://api.map.baidu.com/api?v=2.0&ak=ZUONbpqGBsYGXNIYHicvbAbM"></script>
	<script type="text/javascript"
		src="http://echarts.baidu.com/gallery/vendors/echarts/extension/bmap.min.js"></script>
	<ul>
		<li class="myTag">分布统计</li>
	</ul>
	<table class="data">
		<tbody>
			<tr>
				<th>设备ID</th>
				<th><select id="device_id" onchange="getSensorList()"><option value="">请选择</option></select></th>
				<th>传感器ID</th>
				<th><select id="sensor_id" onchange="getDataType()"><option value="">请选择</option></select></th>
				<th>传感器类型</th>
				<th><select id="data_type"style="width: 130px"><option value="">请选择</option></select></th>
				<th><select id="selectSensor"><option value="0">自定义时间范围</option>
						<option value="1">最近一天</option>
						<option value="2">最近一周</option>
						<option value="3">最近一月</option></select>&nbsp;&nbsp;<input id="startTime"
					type="text" value="2016-11-10 23:21:53"/><b>--</b><input id="endTime" type="text" value="2016-11-10 23:22:11"/></th>

				<th><b>统计范围</b>&nbsp;&nbsp;<input id="start_scope" type="text"
					style="width: 49px" /><b>--</b><input id="end_scope" type="text"
					style="width: 49px" /></th>
				<th><input type="button" name="distribution" class="button"
					style="height: 30px; width: 49px" value="统计" onclick="showDistribution()" /></th>
			</tr>
		</tbody>
	</table>
	<div id="container"
		style="min-width: 310px; height: 400px; margin: 0 auto"></div>
</body>
</html>