<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@  taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>传感器分析对比</title>
<link rel="stylesheet" href="CSS/mydevice.css" />
<script type="text/javascript" src="js/dataProcess.js"></script>	
<script type="text/javascript" src="js/jquery-1.8.3.min.js"></script>
<script src="js/highcharts/highcharts.js"></script>
<script src="js/highcharts/modules/exporting.js"></script>
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
	/* Highcharts.setOptions({global: {useUTC: false}}); */
	$(function() {
		$('#container').highcharts(
				{
					chart : {
						type : 'spline'
					},
					title : {
						text : 'Snow depth at Vikjafjellet, Norway'
					},
					subtitle : {
						text : 'Irregular time data in Highcharts JS'
					},
					xAxis : {
						categories : [ '10.10', '10.11', '10.12', '10.13',
								'10.14', '10.15', '10.16', '10.17', '10.18' ]
					},
					yAxis : [ {
						lineWidth : 1,
						title : {
							text : '温度 ℃'
						}
					}, {
						title : {
							text : '湿度 %rh'
						},
						lineWidth : 1,
						opposite : false
					}, {
						title : {
							text : '光照度 lux'
						},
						lineWidth : 1,
						opposite : false
					} ],
					plotOptions : {
						spline : {
							marker : {
								enabled : true
							}
						},
						line : {
							dataLabels : {
								enabled : true
							},
							enableMouseTracking : true
						}
					},
					exporting : {
						enabled : true
					},
					//去除右下角Highcharts.com图样
					credits : {
						enabled : true,
						href : "http://www.easinet.cn/cn/products.htm",
						text : "计算技术研究所传感器网络实验室"
					},
					series : [ {
						data : [ 3, 2, 3, 4, 5, 5, 7, 7, 9 ],
						name : '温度',
						yAxis : 0
					}, {
						data : [ 4, 1, 5, 8, 7, 10, 13, 11, 11 ],
						yAxis : 1,
						name : '湿度'
					}, {
						data : [ 19, 15, 11, 12, 10, 14, 12, 15, 17 ],
						step : 'left',
						yAxis : 2,
						name : '光照度'
					} ]

				});
	});
</script>
</head>
<body onload="getDeviceList()">
	<script src="http://code.highcharts.com/highcharts.js"></script>
	<ul>
		<li class="myTag">传感器分析对比</li>
	</ul>
	<table class="data">
		<tbody>
			<tr>
			<th>设备ID</th>
				<th><select id="device_id" onchange="getSensorList()"><option value="">请选择</option></select></th>
				<th>传感器ID</th>
				<th><select id="sensor_id" onchange="createCheckbox()"><option value="">请选择</option></select></th>
				<th align="left"><select id="selectData"><option
							value="0">自定义时间范围</option>
						<option value="1">最近一天</option>
						<option value="2">最近一周</option>
						<option value="3">最近一月</option></select>&nbsp;&nbsp;<input id="start"
					type="text"value="2016-11-10 23:21:53" /><b>--</b><input id="end" type="text" value="2016-11-10 23:22:11"/></th>
				<th><input type="button" name="sensor_comparision"
					class="button" value="查询" onclick="getMultiData()" /></th>
			</tr>
		</tbody>
	</table>
	<div id="container"
		style="min-width: 310px; height: 400px; margin: 0 auto"></div>
	<table>
		<tbody>
			<tr>
				<th><input type="button" name="correlation_analysis"
					class="button" value="相关性分析" onclick="getCorrelationResult()" /></th>
				<th><textarea rows="2" cols="60" id="correlation_result"></textarea></th>
			</tr>
		</tbody>
	</table>
</body>
</html>