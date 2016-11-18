<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%String path=request.getContextPath(); %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>注册</title>
<script src="js/jquery-1.8.3.min.js"></script>
<script language="javascript" src="js/inputVerify.js"></script>

<link rel="stylesheet" href="CSS/register.css"/>

<script type="text/javascript">
//验证E-mail地址
function checkEmail(){
	var str=document.getElementById("email").value;
	if(str==""){//当E-mail地址为空时
		document.getElementById("hint_email").innerText="请输入E-mail地址！";//设置提示信息
		flag_email=false;
	}else if(!checkemail(str)){//当E-mail地址不合法时
		document.getElementById("hint_email").innerText="您输入的E-mail地址不正确！";//设置提示信息
		flag_email=false;
	}else{
		document.getElementById("hint_email").innerText="";//清空提示信息
		flag_email=true;	
		//发送邮箱验证
		//获取邮箱数据
		sendEmail(str);
	}
}

function sendEmail(str)
{
	 $.ajax( {  
	       url:'user/emailVerifaction',// 跳转到 action  
	       data:{
	    	   email : str
	       },
	       type:'get',  
	       cache:false,  
	       dataType:'json',  
	       success:function(data) {
	    	  //alert(data.msg);
	    	  if(data.msg=='验证码已发送')
	    	  {
	    		 // alert('已经发送');
	    		 //  var getCode = $('#getCode');
	    		  //settime(getCode); 
	    	  }
	    	  else document.getElementById("hint_email").innerText=data.msg;
	        },  
	        error : function() {  
	            alert("发送出现异常！");  
	        }  
	   }); 
	  //倒计时
	   countDown();
	   
}
var countdown=60;
function countDown()
{
	if(countdown == 0)
	{
		//按钮可用
		$("#getCode").removeAttr("disabled");
		$('#getCode').val('点击再次发送');
		countdown = 60; 
	}
	else
	{
		$('#getCode').attr({"disabled":"disabled"});
		$('#getCode').val(countdown+'s后可重新发送');
		countdown--;
		setTimeout(function() { 
			countDown(); 
			},1000);
	}
}


</script>
</head>
<body>
<div>
<%@ include file="top.jsp" %>
<div>
<div class="register">
    <form name="form1" id="form1" action="<%=path%>/user/create" method="post">
        <h1>用户注册 ${message }</h1>
        <div>
            <label class="input_label" for="userName">用户名：</label>
            <input class="input_text" name="user_name" id="userName" placeholder="请输入6~12位的英文数字组合" onBlur="checkUser(this.value)"/>
            <span class="hint_span" id="hint_userName"></span>
        </div>
        <div id="board">
            <label class="input_label" for="password">密码：</label>
            <input class="input_text" type="password" name="user_password" id="password" placeholder="请输入密码"onBlur="checkPwd(this.value)"/>
            <span class="hint_span" id="hint_password"></span>
        </div>
        <div>
            <label class="input_label" for="rePassWord">确认密码：</label>
            <input class="input_text" type="password" name="rePassWord" id="rePassWord" placeholder="请输入确认密码"onBlur="checkRepwd(this.value)"/>
            <span class="hint_span" id="hint_rePassword"></span>
        </div>
        <div>
            <label class="input_label" for="email">邮箱：</label>
            <input class="input_text" type="text" name="user_email" id="email"placeholder="请输入注册邮箱"/>
            <input class="getcode_button" type="button" name="getCode" id="getCode" value="获取邮箱验证码" onClick="checkEmail()"/>
            <span  class="hint_span" id="hint_email"></span>
        </div>
        <div>
            <label class="input_label" for="emailCode">验证码：</label>
            <input class="input_text" type="text" name="user_emailCode" id="emailCode" placeholder="请输入邮箱验证码"/>
            <span class="hint_span" id="hint_emailCode"></span>
        </div>
        <div>
            <input class="register_button" type="submit" name="register" id="register" value="注册账号"/>
        </div>
        <div>
            <a class="have_account" href="login.jsp">已有账号？点击登录</a>
        </div>
        
    </form>

</div>
</div>
<%@ include file="bottom.jsp" %>
</div>
</body>
</html>