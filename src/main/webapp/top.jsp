<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%String paths = request.getContextPath(); %>
<link rel="stylesheet" href="CSS/top.css" />
<link rel="stylesheet" href="CSS/style.css" />
<div class="top"></div>
<div class="navigation">
	<div style="float:left;color:#006700;">
	<a href="index.jsp">我的物联网</a> 
	<a href="statisticalAnalysis.jsp">统计分析</a> 
	</div>
	<div style="float:right;text-align: right;">		
	<c:if test="${empty sessionScope.userlogin.user_name}">	
		&nbsp; | &nbsp;<a href="login.jsp">登录</a>
		&nbsp; | &nbsp;<a href="register.jsp" >注册</a>
	</c:if>
	<c:if test="${!empty sessionScope.userlogin.user_name}">		
		&nbsp; 用户：${sessionScope.userlogin.user_name}| &nbsp;<a href="<%=paths%>/exitAdmin.do">退出登录</a>
	</c:if>
	</div>
</div>