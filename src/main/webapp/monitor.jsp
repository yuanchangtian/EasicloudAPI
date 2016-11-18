<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@  taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>实时监控</title>
<link rel="stylesheet" href="CSS/mydevice.css" />
<script type="text/javascript" src="js/dataProcess.js"></script>
<script type="text/javascript"
	src="js/jquery-1.8.3.min.js"></script>
	<script src="js/highcharts/highcharts.js"></script>
<script src="js/highcharts/highcharts-more.js"></script>
<script src="js/highcharts/modules/exporting.js"></script>
<style type="text/css">
${
demo
.css
}
</style>

<script type="text/javascript">
	/* function getData() {
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
	} */
</script>
<script type="text/javascript">

$(function () {
	
	 $(document).ready(function () {
	        Highcharts.setOptions({
	            global: {
	                useUTC: false
	            }
	        });
	
   // $.getJSON('<%=path%>/monitoringData', function (data) {
	   
        //$('#container').highcharts({
        	
        	var chart = new Highcharts.Chart({
            chart: {
            	renderTo: 'container',
                zoomType: 'x'
            },
            title: {
                text: '传感器数据趋势'
            },
            subtitle: {
                text: document.ontouchstart === undefined ?
                '鼠标拖动可以进行缩放' : '手势操作进行缩放'
            },
            xAxis: {
                type: 'datetime',
                dateTimeLabelFormats: {
                    millisecond: '%H:%M:%S.%L',
                    second: '%H:%M:%S',
                    minute: '%H:%M',
                    hour: '%H:%M',
                    day: '%m-%d',
                    week: '%m-%d',
                    month: '%Y-%m',
                    year: '%Y'
                }
            },
          //去除右下角Highcharts.com图样
            credits: {
            	  enabled: true,
                  href :"http://www.easinet.cn/cn/products.htm",
                  text : "计算技术研究所传感器网络实验室"
            },
            tooltip: {
                valueSuffix: ' ℃',
                dateTimeLabelFormats: {
                    millisecond: '%H:%M:%S.%L',
                    second: '%H:%M:%S',
                    minute: '%H:%M',
                    hour: '%H:%M',
                    day: '%Y-%m-%d',
                    week: '%m-%d',
                    month: '%Y-%m',
                    year: '%Y'
                }
            }, 
            yAxis: {
                title: {
                    text: 'Exchange rate'
                }
            },
            legend: {
                enabled: false
            },
            /* tooltip: {
                valueSuffix: null
            }, */
            plotOptions: {
                area: {
                    fillColor: {
                        linearGradient: {
                            x1: 0,
                            y1: 0,
                            x2: 0,
                            y2: 1
                        },
                        stops: [
                            [0, Highcharts.getOptions().colors[0]],
                            [1, Highcharts.Color(Highcharts.getOptions().colors[0]).setOpacity(0).get('rgba')]
                        ]
                    },
                    marker: {
                        radius: 2
                    },
                    lineWidth: 1,
                    states: {
                        hover: {
                            lineWidth: 1
                        }
                    },
                    threshold: null
                }
            },
            series: [{
                type: 'area',
                name: '该点数值',
                data: [],
                /* tooltip: {
                    valueSuffix: ' mm'
                } */
            }]
        });
        	$.ajax({
       			url : "<%=path%>/monitoringData",// 跳转到 action  
       			data : {},
       			type : 'get',
       			cache : false,
       			dataType : 'json',
       			success : function(data) {
       				chart.series[0].setData(data.data);
       				//设置类型值
       				var s = "";
       				 var jsonarray = data.unit;
       				for(var i=0;i<jsonarray.length;i++)
       				{
       					var temp = jsonarray[i].split("|");
       					s += "<option value='"+temp[i]+"'>"+temp[i]+"</option>";
       				}
       				document.getElementById("dataType").innerHTML = s;
       				
       			},
       			error : function() {
       				alert("异常！");
       			}
       		});
        	 function remainTime()
             {
        		 //alert('haha');
          		$.ajax({
          			url : "<%=path%>/monitoringData",// 跳转到 action  
          			data : {},
          			type : 'get',
          			cache : false,
          			dataType : 'json',
          			success : function(data) {
          				chart.series[0].setData(data.data);
          			},
          			error : function() {
          				alert("异常！");
          			}
          		});
             }
        	 window.setInterval(remainTime,2000); 
             //setInterval(remainTime(),5000); 
      
     
       
       //chart.series[0].data = [[1478342749000,34.56],[1478343749000,43.767],[1478344749000,53.767]];
      // chart.redraw();
        	
    });
});

/* window.setInterval(monitoring(),5000);  */

	
</script>
</head>
<body onload="getDeviceList()">
	<script type="text/javascript" src="http://echarts.baidu.com/gallery/vendors/echarts/echarts-all-3.js"></script>
       <script type="text/javascript" src="http://echarts.baidu.com/gallery/vendors/echarts/extension/dataTool.min.js"></script>
       <script type="text/javascript" src="http://echarts.baidu.com/gallery/vendors/echarts/map/js/china.js"></script>
       <script type="text/javascript" src="http://echarts.baidu.com/gallery/vendors/echarts/map/js/world.js"></script>
       <script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=ZUONbpqGBsYGXNIYHicvbAbM"></script>
       <script type="text/javascript" src="http://echarts.baidu.com/gallery/vendors/echarts/extension/bmap.min.js"></script>
       <ul>
		<li class="myTag">传感器数据实时监测</li>
	</ul>
	<table class="data">
		<tbody>
			<tr>
			<th>设备ID</th>
				<th><select id="device_id" onchange="getSensorList()"><option value="">请选择</option></select></th>
				<th>传感器ID</th>
				<th><select id="sensor_id" onchange="getDataType()"><option value="">请选择</option></select></th>
				<th>数据类型</th>
				<th><select id="data_type"style="width: 130px">
				<option value="">请选择</option>
				</select></th>
				<th><input type="button" name="sensor_monitor" class="button"
					value="确定" onclick="getData()" />
					</th>
			</tr>
		</tbody>
	</table>
	<div id="container"
		style="min-width: 310px; height: 400px; margin: 0 auto"></div>
</body>
</html>