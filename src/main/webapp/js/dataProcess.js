function getSingleData() {
	var sensor_id = $("#sensor_id").val();
	var para = {
		start : $("#start").val(),
		end : $("#end").val(),
		data_type : $("#data_type").val()
	};
	$.ajax({
		url : "v1/sensor/" + sensor_id + "/datapoints/history",// 跳转到 action
		data : para,
		type : 'get',
		cache : false,
		dataType : 'json',
		success : function(data) {
			var temp = [];
			var date = [];
			var temp2 = [];
			var data_type = $("#data_type").val()
			$.each(data, function(n, value) {
				var dataSet = value.values[0];
				temp.push(Date.parse(value.timestamp));
				temp.push(parseFloat(dataSet[data_type]));
				temp2.push(temp);
				temp = [];
			});
			showSingleData2(temp2, data_type);
			// $("#predict_result").val(temperature);
		},
		error : function() {
			alert("异常！");
		},
		headers : {

			"user_key" : "5a6bb6c65d70ae8bafc10b8bafd302e3ecadf023",
		}
	});
}
function showSingleData2(temp, data_type) {
	$('#container').highcharts('StockChart', {
		rangeSelector : {
			selected : 1
		},
		title : {
			text : data_type
		},
		credits : {
			enabled : true,
			href : "http://www.easinet.cn/cn/products.htm",
			text : "计算技术研究所传感器网络实验室"
		},
		series : [ {
			name : data_type,
			data : temp,
			tooltip : {
				valueDecimals : 2
			},
		} ],
	});

}
function dataAnalysis() {
	var sensor_id = $("#sensor_id").val();
	var para = {
		start : $("#start").val(),
		end : $("#end").val(),
		data_type : $("#data_type").val()
	};
	$.ajax({
		url : "v1/sensor/" + sensor_id + "/datapoints/dataAnalysis",// 跳转到
		// action
		data : para,
		type : 'get',
		cache : false,
		dataType : 'json',
		success : function(data) {
			var a = document.getElementById("myTable");
			a.rows[1].cells[1].innerHTML = data.maxValue;
			a.rows[2].cells[1].innerHTML = data.minValue;
			a.rows[3].cells[1].innerHTML = data.meanValue.toFixed(3);
			// $("#haha").val(data[0].device_location.lng);
		},
		error : function() {
			alert("异常！");
		},
		headers : {

			"user_key" : "5a6bb6c65d70ae8bafc10b8bafd302e3ecadf023",
		}
	});
}

function prediction() {
	var sensor_id = $("#sensor_id").val();
	var para = {
		start : $("#start").val(),
		end : $("#end").val(),
		data_type : $("#data_type").val(),
		exponent_number : $("#exponent_number").val(),
		step_number : $("#step_number").val()

	};
	$.ajax({
		url : "v1/sensor/" + sensor_id + "/datapoints/prediction",// 跳转到
		// action
		data : para,
		type : 'get',
		cache : false,
		dataType : 'json',
		success : function(data) {
			var predict_data=predict_data=data.predict_value;
			//alert(predict_data[predict_data.length-1]);
			var temp = [];
			var date = [];
			var temp2 = [];
			var data_type = $("#data_type").val()
			var interval=Date.parse(data.history[1].timestamp)-Date.parse(data.history[0].timestamp);
			//alert(interval);
			$.each(data.history, function(n, value) {
				var dataSet = value.values[0];
				temp.push(Date.parse(value.timestamp));
				temp.push(parseFloat(dataSet[data_type]));
				temp2.push(temp);
				temp = [];
			});
			temp=[];
			var lastTimeStamp=temp2[temp2.length-1][0];
			var nextTimeStamp=lastTimeStamp;
			for(var i=0;i<predict_data.length;i++){
				//alert(parseFloat(predict_data[i]));
				nextTimeStamp=nextTimeStamp+interval;
				temp.push(nextTimeStamp);
				temp.push(parseFloat(predict_data[i].toFixed(3)));
				temp2.push(temp);
				temp=[];
			}
			showPredictResult(temp2,lastTimeStamp,data_type);
		},
		error : function() {
			alert("异常！");
		},
		headers : {

			"user_key" : "5a6bb6c65d70ae8bafc10b8bafd302e3ecadf023",
		}
	});
}
function showPredictResult(temp, lastTimeStamp,data_type) {
	$('#container').highcharts('StockChart', {
		rangeSelector : {
			selected : 1
		},
		title : {
			text : data_type
		},
		credits : {
			enabled : true,
			href : "http://www.easinet.cn/cn/products.htm",
			text : "计算技术研究所传感器网络实验室"
		},
		series : [ {
			name : data_type,
			data : temp,
			zoneAxis : 'x',
			zones : [{
				value : lastTimeStamp},
				{
				//dashStyle : 'dot',
				color:'#90ed7d'
			}],
			tooltip : {
				valueDecimals : 2
			},
		} ],
	});

}
function getCorrelationResult() {
	var sensor_id = $("#sensor_id").val();
	var para = {
		start : $("#start").val(),
		end : $("#end").val(),

	};
	$.ajax({
		url : "v1/sensor/" + sensor_id + "/datapoints/correlation",// 跳转到
		// action
		data : para,
		type : 'get',
		cache : false,
		dataType : 'json',
		success : function(data) {
			$("#correlation_result").val(
					"Pearson Correlation:" + data.pearsons + "\n"
							+ "Spearman Correaltion:" + data.spearmans);
			// alert(data.analysis_result);
			// $("#haha").val(data[0].device_location.lng);
		},
		error : function() {
			alert("异常！");
		},
		headers : {

			"user_key" : "5a6bb6c65d70ae8bafc10b8bafd302e3ecadf023",
		}
	});
}
function getDeviceList() {
	$.ajax({
		url : "v1/devices",// 跳转到 action
		type : 'get',
		cache : false,
		dataType : 'json',
		success : function(data) {
			var deviceList = [];
			$.each(data, function(n, value) {
				// alert(JSON.stringify(data));
				// alert(data[0].device_id);

				deviceList.push(value.device_id);
			});
			for (var i = 0; i < deviceList.length; i++) {
				var temp = deviceList[i];
				jQuery("#device_id").append(
						"<option value=" + temp + ">" + temp + "</option>");
			}
		},
		error : function() {
			alert("异常！");
		},
		headers : {

			"user_key" : "5a6bb6c65d70ae8bafc10b8bafd302e3ecadf023",
		}
	});
}
function getSensorList() {
	var device_id = $("#device_id").val();
	if (device_id == '') {
		jQuery("#sensor_id").empty();
		jQuery("#data_type").empty();
		jQuery("#sensor_id").prepend("<option value=''>请选择</option>");
		jQuery("#data_type").prepend("<option value=''>请选择</option>");
	} else {
		$
				.ajax({
					url : "v1/device/" + device_id + "/sensors",// 跳转到 action
					type : 'get',
					cache : false,
					dataType : 'json',
					success : function(data) {
						// alert(JSON.stringify(data));
						var sensorList = [];
						$.each(data.sensors, function(n, value) {
							sensorList.push(value.sensor_id);
						});
						for (var i = 0; i < sensorList.length; i++) {
							var temp = sensorList[i];
							jQuery("#sensor_id").append(
									"<option value=" + temp + ">" + temp
											+ "</option>");
						}
					},
					error : function() {
						alert("异常！");
					},
					headers : {
						"user_key" : "5a6bb6c65d70ae8bafc10b8bafd302e3ecadf023",
					}
				});
	}

}
function getDataType() {
	var device_id = $("#device_id").val();
	var sensor_id = $("#sensor_id").val();
	if (device_id == '' || sensor_id == '') {
		jQuery("#data_type").empty();
		jQuery("#data_type").prepend("<option value=''>请选择</option>");
	} else {
		$
				.ajax({
					url : "v1/device/" + device_id + "/sensor/" + sensor_id
							+ "",// 跳转到
					// action
					type : 'get',
					cache : false,
					dataType : 'json',
					success : function(data) {
						// alert(JSON.stringify(data));
						var dataType = [];
						$.each(data.sensor_values, function(n, value) {
							dataType.push(value.unit);
						});
						for (var i = 0; i < dataType.length; i++) {
							var temp = dataType[i];
							jQuery("#data_type").append(
									"<option value=" + temp + ">" + temp
											+ "</option>");
						}

					},
					error : function() {
						alert("异常！");
					},
					headers : {
						"user_key" : "5a6bb6c65d70ae8bafc10b8bafd302e3ecadf023",
					}
				});
	}

}
function showDistribution(){
	var sensor_id = $("#sensor_id").val();
	var para = {
		start : $("#start").val(),
		end : $("#end").val(),
		data_type : $("#data_type").val()
	};
	$.ajax({
		url : "v1/sensor/" + sensor_id + "/datapoints/history",// 跳转到 action
		data : para,
		type : 'get',
		cache : false,
		dataType : 'json',
		success : function(data) {
			var temp = [];
			var data_type = $("#data_type").val()
			$.each(data, function(n, value) {
				var dataSet = value.values[0];
				temp.push(parseFloat(dataSet[data_type]));
			});
			var start_scope=parseInt($("#start_scope").val());
			var end_scope=parseInt($("#end_scope").val());
			var interval=parseInt((end_scope-start_scope)/7);
			if((end_scope-start_scope)%7>0)interval+=1;
			var one=start_scope+interval;
			var two=one+interval;
			var three=two+interval;
			var four=three+interval;
			var five=four+interval;
			var six=five+interval;
			var seven=six+interval;
			var intervals=[start_scope+'~'+one,one+'~'+two,two+'~'+three,three+'~'+four,four+'~'+five,five+'~'+six,six+'~'+seven];
			var oneCount=0
			var twoCount=0;
			var threeCount=0;
			var fourCount=0;
			var fiveCount=0;
			var sixCount=0;
			var sevenCount=0;
			for(var i=0;i<temp.length;i++){
				if(temp[i]>=start_scope&&temp[i]<one)oneCount++;
				else if(temp[i]>=one&&temp[i]<two)twoCount++;
				else if(temp[i]>=two&&temp[i]<three)threeCount++;
				else if(temp[i]>=three&&temp[i]<four)fourCount++;
				else if(temp[i]>=four&&temp[i]<five)fiveCount++;
				else if(temp[i]>=five&&temp[i]<six)sixCount++;
				else if(temp[i]>=six&&temp[i]<=seven)sevenCount++;
			}
			var data2=[oneCount,twoCount,threeCount,fourCount,fiveCount,sixCount,sevenCount];
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
					data :intervals,
					axisTick : {
						alignWithLabel : true
					}
				} ],
				yAxis : [ {
					type : 'value'
				} ],
				credits : {
					enabled : true,
					href : "http://www.easinet.cn/cn/products.htm",
					text : "计算技术研究所传感器网络实验室"
				},
				series : [ {
					name : '数据点数',
					type : 'bar',
					barWidth : '60%',
					data : data2
				} ]
			};
			;
			if (option && typeof option === "object") {
				myChart.setOption(option, true);
			}
		},
		error : function() {
			alert("异常！");
		},
		headers : {

			"user_key" : "5a6bb6c65d70ae8bafc10b8bafd302e3ecadf023",
		}
	});
}
function getProportion(){
	var sensor_id = $("#sensor_id").val();
	var para = {
		start : $("#start").val(),
		end : $("#end").val(),
		data_type : $("#data_type").val()
	};
	$.ajax({
		url : "v1/sensor/" + sensor_id + "/datapoints/history",// 跳转到 action
		data : para,
		type : 'get',
		cache : false,
		dataType : 'json',
		success : function(data) {
			var temp = [];
			var data_type = $("#data_type").val()
			$.each(data, function(n, value) {
				var dataSet = value.values[0];
				temp.push(parseFloat(dataSet[data_type]));
			});
			var below=$("#start_scope").val();
			var high=$("#end_scope").val();
			var belowNumber=0;
			var normalNumber=0;
			var highNumber=0;
			for(var i=0;i<temp.length;i++){
				if(temp[i]<below)belowNumber++;
				else if(temp[i]>=below&&temp[i]<=high)normalNumber++;
				else highNumber++;
			}
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
					data : [ '低于'+below, '高于'+high, '正常值' ]
				},
				credits : {
					enabled : true,
					href : "http://www.easinet.cn/cn/products.htm",
					text : "计算技术研究所传感器网络实验室"
				},
				series : [ {
					name : data_type,
					type : 'pie',
					radius : '55%',
					center : [ '50%', '60%' ],
					data : [ {
						value : belowNumber,
						name : '低于'+below
					}, {
						value : highNumber,
						name : '高于'+high
					}, {
						value : normalNumber,
						name : '正常值'
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
			
		},
		error : function() {
			alert("异常！");
		},
		headers : {

			"user_key" : "5a6bb6c65d70ae8bafc10b8bafd302e3ecadf023",
		}
	});
}

function getMultiData() {
	var sensor_id = $("#sensor_id").val();
	var para = {
		start : $("#start").val(),
		end : $("#end").val(),
	};
	$.ajax({
		url : "v1/sensor/" + sensor_id + "/datapoints/history",// 跳转到 action
		data : para,
		type : 'get',
		cache : false,
		dataType : 'json',
		success : function(data) {
			var temp = [];
			var date = [];
			var temp2 = [];
			var data_type = $("#data_type").val()
			$.each(data, function(n, value) {
				var dataSet = value.values[0];
				temp.push(Date.parse(value.timestamp));
				temp.push(parseFloat(dataSet[data_type]));
				temp2.push(temp);
				temp = [];
			});
			showMultiData(temp2, data_type);
			// $("#predict_result").val(temperature);
		},
		error : function() {
			alert("异常！");
		},
		headers : {

			"user_key" : "5a6bb6c65d70ae8bafc10b8bafd302e3ecadf023",
		}
	});
}
function showMultiData(temp2, data_type){
	
}
