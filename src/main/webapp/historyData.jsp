<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@  taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>历史数据查询</title>
<link rel="stylesheet" href="CSS/mydevice.css" />
<script type="text/javascript" src="js/dataProcess.js"></script>
<script
	src="http://cdn.static.runoob.com/libs/jquery/2.1.1/jquery.min.js"></script>
<script src="http://cdn.hcharts.cn/highcharts/modules/exporting.js"></script>
<script src="http://cdn.hcharts.cn/highstock/highstock.js"></script>
<link rel="stylesheet"
	href="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/css/bootstrap.min.css">

<script
	src="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<style type="text/css">
${
demo


.css


}
</style>
</head>
<body onload="getDeviceList()">
	<script type="text/javascript">
		$(function() {
			// Create the chart
			$('#container').highcharts('StockChart', {
				rangeSelector : {
					selected : 1
				},
				title : {
					text : 'EasiCloud'
				},
				series : [ {
					name : 'EasiCloud',
					data : [],
					tooltip : {
						valueDecimals : 2
					}
				} ]
			});
		});
	</script>
	<ul>
		<li class="myTag">历史数据查询</li>
	</ul>
	<table class="data">
		<tbody>
			<tr>
				<th>设备ID</th>
				<th><select id="device_id" onchange="getSensorList()"><option
							value="">请选择</option></select></th>
				<th>传感器ID</th>
				<th><select id="sensor_id" onchange="getDataType()"><option
							value="">请选择</option></select></th>
				<th>传感器类型</th>
				<th><select id="data_type" style="width: 130px"><option
							value="">请选择</option></select></th>
				<th><select id="select_date"><option value="0">自定义时间范围</option>
						<option value="1">最近一天</option>
						<option value="2">最近一周</option>
						<option value="3">最近一月</option></select>&nbsp;&nbsp;<input id="start"
					value="2016-11-10 23:21:53" type="text" /><b>--</b><input id="end"
					type="text" value="2016-11-10 23:22:11" /></th>
				<th><input type="button" name="search" class="button"
					value="查询" onclick="getSingleData()" /></th>
			</tr>
		</tbody>
	</table>
	<div id="container"
		style="min-width: 310px; height: 400px; margin: 0 auto"></div>
	<input type="button" name="analysis" class="button" value="统计分析"
		onclick="dataAnalysis()" />
	<div class="table-responsive">
		<table class="table table-striped" id="myTable" style="width:400px">
			<caption>统计分析结果</caption>
			<thead>
				<tr>
					<th>算法类型</th>
					<th>统计结果</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td>maxValue</td>
					<td id="maxValue"></td>
				</tr>
				<tr>
					<td>minValue</td>
					<td id="minValue"></td>
				</tr>
				<tr>
					<td>meanValue</td>
					<td id="meanValue"></td>
				</tr>
			</tbody>
		</table>
	</div>

	<input type="button" name="predict" class="button" value="预测"
		onclick="prediction()" />


</body>
</html>