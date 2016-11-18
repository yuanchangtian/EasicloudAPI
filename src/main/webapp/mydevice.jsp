<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%String path = request.getContextPath(); %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@  taglib  uri="http://java.sun.com/jsp/jstl/functions"   prefix="fn"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>我的设备</title>
<script type="text/javascript"
	src="http://cdn.hcharts.cn/jquery/jquery-1.8.3.min.js"></script>
<link rel="stylesheet" href="CSS/mydevice.css" />
<script language="javascript"
	src="https://webapi.amap.com/maps?v=1.3&key=bd154ab42bd3eee40121cf6d81fdd749"></script>
<script type="text/javascript">
	//设置全局变量的位置
	var temp_location = "";
	var pos;
	
	var status = "";
	var deleteTip = '您确定要删除该设备的所有信息吗？';
	function deleteDevice(deviceid) {
		 if(confirm(deleteTip))
		{
			 window.location.href="<%=path%>/deletedevice?device_id="+deviceid+"&currentPage=${page.currentPage}"; 
		}
	}
	function myClose(divID) {
		divID.style.display = 'none'; //设置id为login的层隐藏
		//设置id为notClickDiv的层隐藏
		document.getElementById("notClickDiv").style.display = 'none';
	}
	function Myadd(divID) { //根据传递的参数确定显示的层
		var notClickDiv = document.getElementById("notClickDiv"); //获取id为notClickDiv的层
		notClickDiv.style.display = 'block'; //设置层显示
		document.getElementById("notClickDiv").style.width = document.body.clientWidth;
		document.getElementById("notClickDiv").style.height = document.body.clientHeight;
		document.getElementById(divID).style.display = 'block'; //设置由divID所指定的层显示	 
		document.getElementById(divID).style.left = (document.body.clientWidth - 1000) / 2; //设置由divID所指定的层的左边距
		document.getElementById(divID).style.top = (document.body.clientHeight - 700) / 2; //设置由divID所指定的层的顶边框
		$("#device_id").val('系统生成');
		$("#device_name").val('');
		$("#device_description").val('');
		$("#device_location").val("lng:116.31992635|lat:39.983567321");
		status = "add";
		dealMap();
	}
	function Myopen(divID,device_id,device_name,device_active,device_description,device_publicity,device_location) { //根据传递的参数确定显示的层
		var notClickDiv = document.getElementById("notClickDiv"); //获取id为notClickDiv的层
		notClickDiv.style.display = 'block'; //设置层显示
		document.getElementById("notClickDiv").style.width = document.body.clientWidth;
		document.getElementById("notClickDiv").style.height = document.body.clientHeight;
		document.getElementById(divID).style.display = 'block'; //设置由divID所指定的层显示	 
		document.getElementById(divID).style.left = (document.body.clientWidth - 1000) / 2; //设置由divID所指定的层的左边距
		document.getElementById(divID).style.top = (document.body.clientHeight - 700) / 2; //设置由divID所指定的层的顶边框
		temp_location = device_location;
		statue = "update";
		dealMap();
		$("#device_id").val(device_id);
		$("#device_name").val(device_name);
		$("#device_description").val(device_description);
		$("#device_active").val(device_active);
		$("#device_publicity").val(device_publicity);
		$("#device_location").val(device_location);
		
		//$("#device_id_update").val(device_id);
	}
	function Myclose(divID)
	{
		document.getElementById(divID).style.display = 'none';
		var notClickDiv = document.getElementById("notClickDiv"); //获取id为notClickDiv的层
		notClickDiv.style.display = 'none'; //设置层显示
	}
	 function dealDevice()
	{
	
		//判断更新或添加
		if(status=="update")
		{	//更新
			formSubmit("update");
		}
		//添加
		if(status=="add")
		{
			//更新
			formSubmit("add");
		}
		
	}
	 function formSubmit(sta)
	 {
		 var ExportForm = document.createElement("FORM"); 
		    document.body.appendChild(ExportForm);  
		    ExportForm.method = "POST";  
		    var newElement = document.createElement("input");  
		    newElement.setAttribute("name", "device_id");  
		    newElement.setAttribute("type", "hidden"); 
		    newElement.setAttribute("value", document.getElementById("device_id").value); 
		    var newElement2 = document.createElement("input");  
		    newElement2.setAttribute("name", "device_name");  
		    newElement2.setAttribute("type", "hidden");  
		    newElement2.setAttribute("value", document.getElementById("device_name").value);
		    var newElement3 = document.createElement("input");
		    newElement3.setAttribute("name", "device_active");  
		    newElement3.setAttribute("type", "hidden");  
		    newElement3.setAttribute("value", document.getElementById("device_active").value);
		    var newElement4 = document.createElement("input");
		    newElement4.setAttribute("name", "device_description");  
		    newElement4.setAttribute("type", "hidden");  
		    newElement4.setAttribute("value", document.getElementById("device_description").value);
		    var newElement5 = document.createElement("input"); 
		    newElement5.setAttribute("name", "device_location");  
		    newElement5.setAttribute("type", "hidden");  
		    newElement5.setAttribute("value", document.getElementById("device_location").value);
		    var newElement6 = document.createElement("input"); 
		    newElement6.setAttribute("name", "device_publicity");  
		    newElement6.setAttribute("type", "hidden");  
		    newElement6.setAttribute("value", document.getElementById("device_publicity").value);
		    var newElement7 = document.createElement("input");
		    newElement7.setAttribute("name", "currentPage");  
		    newElement7.setAttribute("type", "hidden");
		    newElement7.setAttribute("value", ""+${page.currentPage});
		    ExportForm.appendChild(newElement);  
		    ExportForm.appendChild(newElement2);  
		    ExportForm.appendChild(newElement3);
		    ExportForm.appendChild(newElement4);
		    ExportForm.appendChild(newElement5);
		    ExportForm.appendChild(newElement6);
		    ExportForm.appendChild(newElement7);
		    ExportForm.action = "<%=path%>/"+sta+"device";  
		    ExportForm.submit();  
	 }
	var mapObj;
	var marker;
	var str_lng;
	var str_lat;
	//var pos;
	function dealMap() {
		
		if(status=="update")
		{
		var strs= new Array(); //定义一数组 
		strs=temp_location.split("|"); //字符分割 
		str_lng = strs[0].substring(4);
		str_lat = strs[1].substring(4);
		}
		else
		{
			//默认为北京中关村坐标
			str_lng = 116.31992635;
			str_lat = 39.983567321;
		}
		pos = new AMap.LngLat(str_lng,str_lat);
		mapObj = new AMap.Map("mapContainer", {

			//二维地图显示视口
			view : new AMap.View2D({
				center : pos,//地图中心点
				zoom : 4
			//地图显示的缩放级别
			})
		});
		mapObj.plugin([ "AMap.ToolBar" ], function() {
			var toolBar = new AMap.ToolBar();
			mapObj.addControl(toolBar);
		});
		if (false) {
			pos = new AMap.LngLat(0, 0);
			mapObj.setZoom(12);
		} else {
			///showCityInfo();

		}
		mapObj.setCenter(pos);
		addMarker();
		var clickEventListener = AMap.event.addListener(mapObj, 'click',
				function(e) {
					var lng = e.lnglat.getLng();
					var lat = e.lnglat.getLat();
					addPoint(lng, lat);
				});
	}

	function addPoint(lng, lat) {
		var point = new AMap.LngLat(lng, lat); // 创建标注
		marker.setPosition(point); // 将标注添加到地图中
		$("#device_location").val("lng:"+lng + "|" +"lat:"+ lat);
	}
	function addMarker() {
		marker = new AMap.Marker({
			position : pos,
			draggable : false, //点标记可拖拽
			cursor : 'move', //鼠标悬停点标记时的鼠标样式
			raiseOnDrag : true
		//鼠标拖拽点标记时开启点标记离开地图的效果

		});
		marker.setMap(mapObj);
	}
	///显示当前城市
	function showCityInfo() {

		mapObj.plugin([ "AMap.CitySearch" ], function() {

			var citysearch = new AMap.CitySearch();

			citysearch.getLocalCity();

			AMap.event.addListener(citysearch, "complete", function(result) {
				if (result && result.city && result.bounds) {
					var cityinfo = result.city;
					var citybounds = result.bounds;

					mapObj.setBounds(citybounds);
					addMarker();
				} else {

				}
			});
			AMap.event.addListener(citysearch, "error", function(result) {
				alert(result.info);
			});
		});
	}
</script>
<style>
#notClickDiv {
	filter: alpha(Opacity = 35);
	opacity: 0.35; /*设置不透明度为35%*/
	background: #000000; /*设置背景为黑色*/
	position: absolute; /*设置定位方式为绝对位置*/
	display: none; /*设置该<div>标记显示*/
	z-index: 9; /*设置层叠顺序*/
	top: 0px; /*设置顶边距*/
	left: 0px; /*设置左边距*/
	margin: 0px;
	padding: 0px;
}

#adddevice {
	position: absolute; /*设置布局方式*/
	width: 1000px; /*设置宽度*/
	padding: 4px; /*设置内边距*/
	display: none; /*设置显示方式*/
	z-index: 10; /*设置层叠顺序*/
	background-color: #ffffff; /*设置背景颜色*/
	over-flow: auto;
}

table.data2 {
	background: #dddddd none repeat scroll 0%;
	width: 100%;
}

table.data2 td {
	padding: 3px 5px;
	align: left;
}

table.data2 tr.tr2 {
	background: #ffffff none repeat scroll 0%;
}
</style>
</head>
<body>
	<div id="notClickDiv"></div>
	<div>
		<div class="wrap">
			<ul>
				<li class="myTag">我的设备列表</li>
			</ul>
			<div id="tagContent" class="content">
				<div class="textbox" style="font-size: 14px; line-height: 24px;">
					<table class="data">
						<tbody>
							<tr>
								<th>设备ID</th>
								<th>名称</th>
								<th>是否激活</th>
								<th>创建日期</th>
								<th>描述</th>
								<th>位置</th>
								<th>是否公开</th>
								<th>操作</th>
							</tr>
							<% int i=1; %>
							<c:forEach var="l" items="${list }">
							<tr class="tr1">
								<td>${l.device_id }</td>
								<td>${l.device_name }</td>
								<td>
								<c:if test="${l.device_active=='0'}">已激活</c:if>
								<c:if test="${l.device_active=='1'}">未激活</c:if>
								</td>
								<td>${l.device_created_format }</td>
								<td>${l.device_description }</td>
								<!-- ${fn:substringBefore(l.device_location,"|")} -->
								<td>
								<c:set var="lng" value="${fn:substringBefore(l.device_location,'|')}"></c:set>
								经度：${fn:substring(lng,4,11)},
								<c:set var="lat" value="${fn:substringAfter(l.device_location,'|')}"></c:set>
								经度：${fn:substring(lat,4,11)}
								</td>
								<td>
								<c:if test="${l.device_publicity=='0'}">公开</c:if>
								<c:if test="${l.device_publicity=='1'}">私有</c:if>
								</td>
								<td><!-- <a href="">查看</a>| -->
								<a href="#" onClick="Myopen('adddevice','${l.device_id }',
								'${l.device_name }','${l.device_active }','${l.device_description }',
								'${l.device_publicity }','${l.device_location }')">编辑</a>|<span>|</span> <a href="#"
									onclick="deleteDevice('${l.device_id }')">删除</a></td>
							</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
				<div class="pagination">
					<table width="100%">
						<tr>
							<td>
								<div id="pager">
								<!-- 页码范围区 -->
									<span>
									<c:choose>
		<c:when test="${page.hasPrePage}">
			<a href="<%=path %>/showdevices?currentPage=1">首页</a> | 
	<a href="<%=path %>/showdevices?currentPage=${page.currentPage -1 }">上一页</a>
		</c:when>
		<c:otherwise>
			首页 | 上一页
		</c:otherwise>
	</c:choose>
	<c:choose>
		<c:when test="${page.hasNextPage}">
			<a href="<%=path %>/showdevices?currentPage=${page.currentPage + 1 }">下一页</a> | 
	<a href="<%=path %>/showdevices?currentPage=${page.totalPage }">尾页</a>
		</c:when>
		<c:otherwise>
			下一页 | 尾页
		</c:otherwise>
	</c:choose>
	当前第${page.currentPage}页,共${page.totalPage}页
									
									</span>
								</div>
							</td>
							<td align="right"><a href="#" onClick="Myadd('adddevice')">添加新设备</a>
							</td>
						</tr>

					</table>
				</div>
			</div>
		</div>
	</div>

	<div id="adddevice">
	<!-- <form method="post" id="device" name="myFormId" id="myFormId" action=""> -->
		<table class="data2">
			<tbody>
				<tr class="tr2">
					<td width="100">设备ID</td>
					<td><input id="device_id" type="text" name="device_id" disabled="true"/></td>
				</tr>
				<tr class="tr2">
					<td>是否激活</td>
					<td><select id="device_active" name="device_active"><option
								value="0">是</option>
							<option value="1">否</option></select></td>
				</tr>
				<tr class="tr2">
					<td>名称</td>
					<td><input id="device_name" size="100" type="text" name="device_name"/></td>
				</tr>
				<tr class="tr2">
					<td>设备描述</td>
					<td><input id="device_description" size="100" type="text" name="device_description"/></td>
				</tr>
				<tr class="tr2">
					<td>设备位置</td>
					<td>地理坐标：<input id="device_location" type="text" name="device_location" disabled="true" style="width:200px;"/>
						<div style="width: 100%; height: 300px; border: 1px solid gray"
							id="mapContainer"></div></td>
				</tr>
				<tr class="tr2">
					<td>是否公开</td>
					<td><select id="device_publicity" name="device_publicity">
					<option	value="0">是</option>
					<option value="1">否</option></select></td>
				</tr>
				<tr class="tr2">
					<td colspan="2" align="center"><input type="button"
						name="save" class="button" value="保  存" onclick="dealDevice()"/> <input
						type="button" name="cancel" class="button" value="返回"
						onclick="Myclose('adddevice')" /></td>
				</tr>
				
			</tbody>
		</table>
		<!-- </form> -->
	</div>
</body>