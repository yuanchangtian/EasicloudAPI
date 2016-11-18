<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%String path=request.getContextPath(); %>
<!DOCTYPE html><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>登录</title>
<link rel="stylesheet" href="CSS/login.css"/>
</head>
<body>
<%@ include file="top.jsp" %>
<div class="login">
    <div class="login_alpha"></div>
    <div class="login_wrapper">
        <div class="purple_bar"></div>
        <div class="err_info">
            <div class="err_text"><span class="fixed_text">提示信息：</span><span class="err_constant">请填写用户名和密码  ${message }</span></div>
        </div>
        <div class="head" title="头像"></div>
        <div class="divider"></div>
        <form class="myForm" action="<%=path%>/login.do" method="post">
            <p class="username_w">
                <label>用户名：</label>
                <input class="import" type="text" name="user_name" id="userName" placeholder="请输入用户名"/>
            </p>
            <p class="password_w">
                <label>密码：</label>
                <input class="import" type="password" name="user_password" id="password" placeholder="请输入密码"/>
            </p>
            <div class="extra">
                <div class="extra_l">
                    <a href="register.jsp">我要注册</a>
                </div>
                <div class="extra_r">
                    <input type="checkbox" name="remember_password" style="vertical-align: middle;"/>
                    <span class="text_instruction">记住密码</span>
                </div>
            </div>
            <input type="submit" class="submit" name="login" value="登录">
            <!--<input type="submit"/>-->
            <br>
            <br>
            <br>
           ${checkResult }
        </form>
    </div>
     
</div>
<%@ include file="bottom.jsp" %>
</body>
</html>