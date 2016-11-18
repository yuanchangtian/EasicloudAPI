<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%String path = request.getContextPath(); %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@  taglib  uri="http://java.sun.com/jsp/jstl/functions"   prefix="fn"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>我的传感器</title>
<script type="text/javascript" src="js/dataProcess.js"></script>
<script type="text/javascript"
	src="http://cdn.hcharts.cn/jquery/jquery-1.8.3.min.js"></script>
<link rel="stylesheet" href="CSS/mydevice.css" />
<script language="javascript"
	src="https://webapi.amap.com/maps?v=1.3&key=bd154ab42bd3eee40121cf6d81fdd749"></script>
<script type="text/javascript">
	//全局状态存储
	var all_status = "update";
	
	
	//公用数据
	var tbody_data_count = 1;
	//数据值
	var tbody_value_count = 1;
	function addItemDatas() {

		tbody_data_count += 1;
		var fileDiv = document.all['tbody_datas'];
		var strHtml = "<tr><td>名称：</td> <td><input id='node_data_name"+tbody_data_count+"' size='15' type='text' /></td><td>单位：</td><td><input id='node_data_unit"+tbody_data_count+"' size='15' type='text' /></td><td><a href='#' onClick='addItemDatas()'>添加</a></td><td><a href='#'  onClick='removeDataRow()'>删除</a></td></tr>";
		//保存断点数据
		//设置当前数量
		var temp_data_count = tbody_data_count;
		//设置二维数组
		var tArray_data = new Array();  //先声明一维
		for(var k=0;k<temp_data_count;k++){    //一维长度为i,i为变量，可以根据实际情况改变
 
			tArray_data[k]=new Array();  //声明二维，每一个一维数组里面的一个元素都是一个数组；
 
		for(var j=0;j<2;j++){   //一维数组里面每个元素数组可以包含的数量p，p也是一个变量；
 
			tArray_data[k][j]="";    //这里将变量初始化，我这边统一初始化为空，后面在用所需的值覆盖里面的值
 			}
		}
		//依次获取input内的值
		for(var j=1;j<=temp_data_count;j++)
		{
			tArray_data[j-1][0] = $("#node_data_name"+j).val();
			tArray_data[j-1][1] = $("#node_data_unit"+j).val();
		}
		//添加后存入断点数据
		fileDiv.innerHTML += strHtml;
		//恢复数据
		for(var j=1;j<=temp_data_count;j++)
		{
			$("#node_data_name"+j).val(tArray_data[j-1][0]);
			$("#node_data_unit"+j).val(tArray_data[j-1][1]);
		}
	}
	function addItemValues() {

		tbody_value_count += 1;
		var fileDiv = document.all['tbody_values'];
		var strHtml = "<tr><td>名称：</td> <td><input id='node_value_name"+tbody_value_count+"' size='15' type='text' /></td><td>单位：</td><td><input id='node_value_unit"+tbody_value_count+"' size='15' type='text' /></td><td><a href='#' onClick='addItemValues()'>添加</a></td><td><a href='#'  onClick='removeValueRow()'>删除</a></td></tr>";
		
		//设置当前数量
		var temp_data_count = tbody_value_count;
		//设置二维数组
		var tArray_value = new Array();  //先声明一维
		for(var k=0;k<temp_data_count;k++){    //一维长度为i,i为变量，可以根据实际情况改变
 
			tArray_value[k]=new Array();  //声明二维，每一个一维数组里面的一个元素都是一个数组；
 
		for(var j=0;j<2;j++){   //一维数组里面每个元素数组可以包含的数量p，p也是一个变量；
 
			tArray_value[k][j]="";    //这里将变量初始化，我这边统一初始化为空，后面在用所需的值覆盖里面的值
 			}
		}
		//依次获取input内的值
		for(var j=1;j<=temp_data_count;j++)
		{
			tArray_value[j-1][0] = $("#node_value_name"+j).val();
			tArray_value[j-1][1] = $("#node_value_unit"+j).val();
		}
		//添加后存入断点数据
		fileDiv.innerHTML += strHtml;
		
		//恢复数据
		for(var j=1;j<=temp_data_count;j++)
		{
			$("#node_value_name"+j).val(tArray_value[j-1][0]);
			$("#node_value_unit"+j).val(tArray_value[j-1][1]);
		}
	}
	
	function deleteSensor(sensor_id,device_id)
	{
		 if(confirm('您确定要删除该传感器的所有信息吗？'))
			{
			 window.location.href="<%=path%>/deletesensor?sensor_id="+sensor_id+"&device_id="+device_id+"&currentPage=${page.currentPage}";
			}
	}
	
	function removeDataRow() {
		
		//obj.parentNode.parentNode.parentNode.deleteRow(tbody_data_count);
		tbody_data_count--;
		document.getElementById('tbody_datas').deleteRow(tbody_data_count);
		
	}
	function removeValueRow(obj) {
		
		//obj.parentNode.parentNode.parentNode.deleteRow(tbody_value_count);
		tbody_value_count--;
		document.getElementById('tbody_values').deleteRow(tbody_value_count);
		
	}
	
	function MySensorSubmit()
	{
			//获取data值
			//依次获取input内的值
			var data_str = "";
			for(var j=1;j<=tbody_data_count;j++)
			{
				
					data_str += $("#node_data_name"+j).val();
					if($("#node_data_unit"+j).val()==""||$("#node_data_unit"+j).val()==null)
					{
						//不做处理				
					}
					else
					{
						data_str += "|";
						data_str += $("#node_data_unit"+j).val();
					}
					if(j!=tbody_data_count)
					{
						data_str += ";";
					}
			}
			var  value_str = "";
			for(var j=1;j<=tbody_value_count;j++)
			{
				
					value_str += $("#node_value_name"+j).val();
					if($("#node_value_unit"+j).val()==""||$("#node_value_unit"+j).val()==null)
					{
						//不做处理				
					}
					else
					{
						value_str += "|";
						value_str += $("#node_value_unit"+j).val();
					}
					if(j!=tbody_value_count)
					{
						value_str += ";";
					}
			}
			formSubmit(all_status,data_str,value_str);
			<%-- window.location.href="<%=path%>/updatesensors?device_id="+$("#deviceid").val()+"&currentPage=${page.currentPage}"+
					"&sensor_data="+data_str+"&sensor_values="+value_str;  --%>
			/* alert(data_str);
			alert(value_str); */
	}
	
	 function formSubmit(sta,data_str,value_str)
	 {
		
		 var ExportForm = document.createElement("FORM"); 
		    document.body.appendChild(ExportForm);  
		    ExportForm.method = "POST";  
		    var newElement = document.createElement("input");  
		    newElement.setAttribute("name", "device_id");  
		    newElement.setAttribute("type", "hidden"); 
		    newElement.setAttribute("value", document.getElementById("device_id").value);
		   
		    var newElement2 = document.createElement("input");  
		    newElement2.setAttribute("name", "sensor_name");  
		    newElement2.setAttribute("type", "hidden");  
		    newElement2.setAttribute("value", document.getElementById("sensor_name").value);
		    var newElement3 = document.createElement("input");
		    newElement3.setAttribute("name", "sensor_imgurl");  
		    newElement3.setAttribute("type", "hidden");  
		    newElement3.setAttribute("value", document.getElementById("sensor_imgurl").getAttribute("src"));
		    var newElement4 = document.createElement("input");
		    newElement4.setAttribute("name", "sensor_description");  
		    newElement4.setAttribute("type", "hidden");  
		    newElement4.setAttribute("value", document.getElementById("sensor_description").value);
		   
		    var newElement5 = document.createElement("input"); 
		    newElement5.setAttribute("name", "sensor_data");  
		    newElement5.setAttribute("type", "hidden");  
		    newElement5.setAttribute("value", data_str);
		    var newElement6 = document.createElement("input"); 
		    newElement6.setAttribute("name", "sensor_values");  
		    newElement6.setAttribute("type", "hidden");  
		    newElement6.setAttribute("value",value_str);
		    var newElement7 = document.createElement("input");
		    newElement7.setAttribute("name", "currentPage");  
		    newElement7.setAttribute("type", "hidden");
		    newElement7.setAttribute("value", ""+${page.currentPage});
		    var newElement8 = document.createElement("input");
		    newElement8.setAttribute("name", "sensor_id");  
		    newElement8.setAttribute("type", "hidden");
		    newElement8.setAttribute("value", document.getElementById("sensor_id").value);
		    ExportForm.appendChild(newElement);  
		    ExportForm.appendChild(newElement2);  
		    ExportForm.appendChild(newElement3);
		    ExportForm.appendChild(newElement4);
		    ExportForm.appendChild(newElement5);
		    ExportForm.appendChild(newElement6);
		    ExportForm.appendChild(newElement7);
		    ExportForm.appendChild(newElement8);
		    ExportForm.action = "<%=path%>/"+sta+"sensor";  
		    ExportForm.submit();  
	 }
	
	function myClose(divID) {
		
		divID.style.display = 'none'; //设置id为login的层隐藏
		//设置id为notClickDiv的层隐藏
		document.getElementById("notClickDiv").style.display = 'none';
	}
	function Myopen(divID,status,sensor_id,device_id,sensor_imgurl,sensor_name,sensor_description,sensor_data,sensor_values) { //根据传递的参数确定显示的层
		
		var notClickDiv = document.getElementById("notClickDiv"); //获取id为notClickDiv的层
		notClickDiv.style.display = 'block'; //设置层显示
		document.getElementById("notClickDiv").style.width = document.body.clientWidth;
		document.getElementById("notClickDiv").style.height = document.body.clientHeight;
		document.getElementById(divID).style.display = 'block'; //设置由divID所指定的层显示	 
		document.getElementById(divID).style.left = (document.body.clientWidth - 1000) / 2; //设置由divID所指定的层的左边距
		document.getElementById(divID).style.top = (document.body.clientHeight - 700) / 2; //设置由divID所指定的层的顶边框
		
		if(status=='update')
		{
			all_status = "update";
			$("#sensor_id").val(sensor_id);
			$("#device_id").val(device_id);
			$("#sensor_imgurl").attr("src",sensor_imgurl);
			$("#sensor_name").val(sensor_name);
			$("#sensor_description").val(sensor_description);
			//用于名称和单位的切割变量
			var str_temp = new Array();
			
			//切割公用数据
			var str_sensor_data = new Array();
			str_sensor_data=sensor_data.split(";"); //字符分割 
			//无单位以*结束
			 for(var i=1;i<=str_sensor_data.length;i++)
			{
				 if(i==1)
				{
					 //判断是否有单位
					 //包含单位时
					 if(str_sensor_data[i-1].indexOf('|')>0)
					{
						 str_temp = str_sensor_data[i-1].split('|');
						 $("#node_data_name1").val(str_temp[0]);
						 $("#node_data_unit1").val(str_temp[1]);
					}
					 //不含单位时
					 else
					{
						 $("#node_data_name1").val(str_sensor_data[i-1]);
						 $("#node_data_unit1").val('');
					}
				}
				 //当不止一个变量时
				 else
				{ 
					 //公用数据自加1
					 addItemDatas();
					 //判断是否有单位
					 //包含单位时
					 if(str_sensor_data[i-1].indexOf('|')>0)
					{
						 str_temp = str_sensor_data[i-1].split('|');
						 $("#node_data_name"+tbody_data_count).val(str_temp[0]);
						 $("#node_data_unit"+tbody_data_count).val(str_temp[1]);
					}
					 //不含单位时
					 else
					{
						 $("#node_data_name"+tbody_data_count).val(str_sensor_data[i-1]);
						 $("#node_data_unit"+tbody_data_count).val('');
					}
				}
			} 
			
			//切割数据值
			var str_value_data = new Array();
			str_value_data=sensor_values.split(";"); //字符分割 
			//无单位以*结束
			 for(var i=1;i<=str_value_data.length;i++)
			{
				 if(i==1)
				{
					 //判断是否有单位
					 //包含单位时
					 if(str_value_data[i-1].indexOf('|')>0)
					{
						 str_temp = str_value_data[i-1].split('|');
						 $("#node_value_name1").val(str_temp[0]);
						 $("#node_value_unit1").val(str_temp[1]);
					}
					 //不含单位时
					 else
					{
						 $("#node_value_name1").val(str_value_data[i-1]);
						 $("#node_value_unit1").val('');
					}
				}
				 //当不止一个变量时
				 else
				{ 
					 //公用数据自加1
					 addItemValues();
					 //判断是否有单位
					 //包含单位时
					 if(str_value_data[i-1].indexOf('|')>0)
					{
						 str_temp = str_value_data[i-1].split('|');
						 $("#node_value_name"+tbody_value_count).val(str_temp[0]);
						 $("#node_value_unit"+tbody_value_count).val(str_temp[1]);
					}
					 //不含单位时
					 else
					{
						 $("#node_value_name"+tbody_value_count).val(str_value_data[i-1]);
						 $("#node_value_unit"+tbody_value_count).val('');
					}
				}
			} 	
		}
		//否则为添加,清空所有数据
		else
		{
			all_status = "add";
			//清空公用数据
			tbody_data_count = 1;
			var fileDiv = document.all['tbody_datas'];
			var strHtml = "<tr><td>名称：</td> <td><input id='node_data_name1' size='15' type='text' value='timestamp'/></td><td>单位：</td><td><input id='node_data_unit1' size='15' type='text' value='s'/></td><td><a href='#' onClick='addItemDatas()'>添加</a></td><td><a href='#'  onClick='removeRow(this)'>删除</a></td></tr>";
			fileDiv.innerHTML = strHtml;
			//清空数据值
			tbody_value_count = 1;
			fileDiv = document.all['tbody_values'];
			strHtml = "<tr><td>名称：</td> <td><input id='node_value_name1' size='15' type='text' /></td><td>单位：</td><td><input id='node_value_unit1' size='15' type='text' /></td><td><a href='#' onClick='addItemValues()'>添加</a></td><td><a href='#'  onClick='removeRow(this)'>删除</a></td></tr>";
			fileDiv.innerHTML = strHtml;
			//其它数据
			$("#sensor_id").val('系统生成');
			$("#device_id").removeAttr('disabled');
			$("#sensor_imgurl").attr("src",'http://img53.chem17.com/2/20121107/634878914726718750180.gif');
			$("#sensor_name").val('');
			$("#sensor_description").val('');
			
		}
		
	}
	
	
	
	
	function myClose(divID) {
		document.getElementById(divID).style.display = 'none';
		var notClickDiv = document.getElementById("notClickDiv"); //获取id为notClickDiv的层
		notClickDiv.style.display = 'none'; //设置层显示
	}
	
	/*  function Myopen(divID) { //根据传递的参数确定显示的层
		
		var notClickDiv = document.getElementById("notClickDiv"); //获取id为notClickDiv的层
		notClickDiv.style.display = 'block'; //设置层显示
		document.getElementById("notClickDiv").style.width = document.body.clientWidth;
		document.getElementById("notClickDiv").style.height = document.body.clientHeight;
		document.getElementById(divID).style.display = 'block'; //设置由divID所指定的层显示	 
		document.getElementById(divID).style.left = (document.body.clientWidth - 1000) / 2; //设置由divID所指定的层的左边距
		document.getElementById(divID).style.top = (document.body.clientHeight - 700) / 2; //设置由divID所指定的层的顶边框
		
	}  */
	
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

#addsensor {
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
<body onload="getDeviceList()">
	<div id="notClickDiv"></div>
	<ul>
		<li class="myTag">条件查询</li>
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
				<th><input type="button" name="search_sensor" class="button"
					value="查询" onclick="" /></th>
			</tr>
		</tbody>
	</table>
	<div>
		<div class="wrap">
			<ul>
				<li class="myTag">我的传感器列表</li>
			</ul>
			<div id="tagContent" class="content">
				<div class="textbox" style="font-size: 14px; line-height: 24px;">
					<table class="data">
						<tbody>
							<tr>
								<th>图片地址</th>
								<th>传感器ID</th>
								<th>所属设备ID</th>
								<th>传感器名</th>
								<th>传感器描述</th>
								<!-- <th>传感器数据</th>
								<th>传感器值</th> -->
								<th>操作</th>
							</tr>
							<% int i=1; %>
						<c:forEach var="l" items="${list }">
							<tr class="tr1">
								<td><img src="${l.sensor_imgurl }" height=60" width="100"/></td>
								<td>${l.sensor_id }</td>
								<td>${l.device_id }</td>
								<td>${l.sensor_name }</td>
								<td>${l.sensor_description }</td>
								<%-- <td>${l.sensor_data }</td>
								<td>>${l.sensor_values }</td> --%>
								<td><a href="#" onClick="Myopen('addsensor','update','${l.sensor_id }','${l.device_id }','${l.sensor_imgurl }','${l.sensor_name }','${l.sensor_description }','${l.sensor_data }','${l.sensor_values }')">
								编辑</a> <span>|</span><a href="javascript:void(0)" onclick="deleteSensor('${l.sensor_id }','${l.device_id }')">删除</a></td>
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
			<a href="<%=path %>/showsensors?currentPage=1">首页</a> | 
	<a href="<%=path %>/showsensors?currentPage=${page.currentPage -1 }">上一页</a>
		</c:when>
		<c:otherwise>
			首页 | 上一页
		</c:otherwise>
	</c:choose>
	<c:choose>
		<c:when test="${page.hasNextPage}">
			<a href="<%=path %>/showsensors?currentPage=${page.currentPage + 1 }">下一页</a> | 
	<a href="<%=path %>/showsensors?currentPage=${page.totalPage }">尾页</a>
		</c:when>
		<c:otherwise>
			下一页 | 尾页
		</c:otherwise>
	</c:choose>
	当前第${page.currentPage}页,共${page.totalPage}页
									
									</span>
								</div>
							</td>
							<td align="right"><a href="#" onClick="Myopen('addsensor','add')">添加新传感器</a>
							</td>
						</tr>
					</table>
				</div>
			</div>
		</div>
	</div>

	<div id="addsensor">
		<table class="data2">
			<tbody>
			<tr class="tr2">
					<td width="100">传感器ID</td>
					<td><input id="sensor_id" type="text" disabled="true"/></td>
				</tr>
				<tr class="tr2">
					<td width="100">所属设备ID</td>
					<td><input id="device_id" type="text" disabled="true" placeholder="请输入已有设备id号" /></td>
				</tr>
				<tr class="tr2">
					<td width="100">传感器名</td>
					<td><input id="sensor_name" type="text" /></td>
				</tr>
				<tr class="tr2">
					<td>传感器描述</td>
					<td><input id="sensor_description" size="100" type="text" /></td>
				</tr>
				<tr class="tr2">
					<td>传感器图片url</td>
					<td><img height=45" width="75" id="sensor_imgurl"/>&nbsp;&nbsp;&nbsp;&nbsp;<input size="100" type="button" value="选择图片"/></td>
				</tr>
				<tr class="tr2">
					<td>公用数据</td>
					<td>
						<table>
							<tbody id="tbody_datas">
								<tr>
									<td>名称：</td>
									<td><input id="node_data_name1" size="15" type="text" value="timestamp"/></td>
									<td>单位：</td>
									<td><input id="node_data_unit1" size="15" type="text" value="s"/></td>
									<td><a href="#" onClick="addItemDatas()">添加</a></td>
									<td><a href="#" onClick="removeDataRow()">删除</a></td>
								</tr>
							</tbody>

						</table>

					</td>
				</tr>
				<tr class="tr2">
					<td>数据值</td>
					<td>
						<table>
							<tbody id="tbody_values">
								<tr>
									<td>名称：</td>
									<td><input id="node_value_name1" size="15" type="text" /></td>
									<td>单位：</td>
									<td><input id="node_value_unit1" size="15" type="text" /></td>
									<td><a href="#" onClick="addItemValues()">添加</a></td>
									<td><a href="#" onClick="removeValueRow()">删除</a></td>
								</tr>
							</tbody>

						</table>

					</td>
				</tr>
				<tr class="tr2">
					<td colspan="2" align="center"><input type="button"
						name="save" class="button" value="保  存" onclick="MySensorSubmit()" /> <input
						type="button" name="cancel" class="button" value="返回"
						onclick="myClose('addsensor')" /></td>
				</tr>
			</tbody>
		</table>
	</div>
</body>