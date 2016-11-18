<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@  taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>比例统计</title>
<link rel="stylesheet" href="CSS/mydevice.css" />
<script type="text/javascript" src="js/dataProcess.js"></script>
<script src="echarts.min.js"></script>
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
		option = {
			title : {
				text : '数据比例统计',
				subtext : '',
				x : 'center'
			},
			tooltip : {
				trigger : 'item',
				formatter : "{a} <br/>{b} : {c} ({d}%)"
			},
			legend : {
				orient : 'vertical',
				left : 'left',
				data : []
			},
			series : [ {
				name : '',
				type : 'pie',
				radius : '55%',
				center : [ '50%', '60%' ],
				data : [ {
					value : 0,
					name : ''
				}, {
					value : 0,
					name : ''
				}, {
					value : 0,
					name : ''
				}],
				itemStyle : {
					emphasis : {
						shadowBlur : 10,
						shadowOffsetX : 0,
						shadowColor : 'rgba(0, 0, 0, 0.5)'
					}
				}
			} ]
		};
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
		<li class="myTag">比例统计</li>
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

				<th><b>正常范围</b>&nbsp;&nbsp;<input id="start_scope" type="text"
					style="width: 49px" /><b>--</b><input id="end_scope" type="text"
					style="width: 49px" /></th>
				<th><input type="button" name="distribution" class="button"
					style="height: 30px; width: 49px" value="查询" onclick="getProportion()" /></th>
			</tr>
		</tbody>
	</table>
	<div id="container"
		style="min-width: 310px; height: 400px; margin: 0 auto"></div>
</body>
</html>