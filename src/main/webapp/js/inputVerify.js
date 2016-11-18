/**
 * 
 */
//判断输入的字符串是否大于指定长度
function checkuser(str){ 
	var patrn=/^[a-zA-Z]{1}([a-zA-Z0-9]){5,11}$/;   
	if (!patrn.exec(str)) return false   
	return true 
}
function checkpsw(str){ 
	var patrn=/^([a-zA-Z0-9]){6,12}$/;   
	if (!patrn.exec(str)) return false   
	return true 
}
//验证E-mail地址
function checkemail(email){
	var str=email;
	 //在JavaScript中，正则表达式只能使用"/"开头和结束，不能使用双引号
	var Expression=/\w+([-+.']\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*/; 
	var objExp=new RegExp(Expression);		//创建正则表达式对象
	if(objExp.test(str)==true){				//通过正则表达式进行验证
		return true;
	}else{
		return false;
	}
}
var flag_user=true;		//记录用户是否合法
var flag_pwd=true;			//记录密码是否合法
var flag_repwd=true;		//确认密码是否通过
var flag_email=true;		//记录E-mail地址是否合法
//验证用户名是否合法，并且未被注册
function checkUser(str){
	if(str==""){			//当用户名为空时
		document.getElementById("hint_userName").innerText="请输入用户名！";//设置提示文字
		flag_user=false;
	}else if(!checkuser(str)){	//判断用户名是否符合要求
		document.getElementById("hint_userName").innerText="您输入的用户名不合法！";	//设置提示文字
		flag_user=false;
	}else{		//进行异步操作，判断用户名是否被注册
		document.getElementById("hint_userName").innerText="用户名可用！";	//设置提示文字
		//var loader=new net.AjaxRequest("UserServlet?action=checkUser&username="+str+"&nocache="+new Date().getTime(),deal,onerror,"GET");
	}	
}
//验证密码
function checkPwd(str){
	if(str==""){		//当密码为空时
		document.getElementById("hint_password").innerText="请输入密码！";	//设置提示文字
		flag_pwd=false;
	}else if(!checkpsw(str)){		//当密码不合法时
		document.getElementById("hint_password").innerText="您输入的密码不合法！";	//设置提示文字
		flag_pwd=false;
	}else{		//当密码合法时
		document.getElementById("hint_password").innerText="密码可用";	//清空提示文字
		flag_pwd=true;
	}
}
//验证确认密码是否正确
function checkRepwd(str){
	if(str==""){		//当确认密码为空时
		document.getElementById("hint_rePassword").innerText="请确认密码！";	//设置提示文字
		flag_repwd=false;
	}else if(form1.password.value!=str){		//当确认密码与输入的密码不一致时
		document.getElementById("hint_rePassword").innerText="两次输入的密码不一致！";	//设置提示文字
		flag_repwd=false;
	}else{	//当两次输入的密码一致时
		document.getElementById("hint_rePassword").innerText="两次密码一致";	//清空提示文字
		flag_repwd=true;
	}
}

//保存用户注册信息
function register(){
	if(form1.userName.value==""){		//当用户名为空时
		alert("请输入用户名！");form1.user.focus();return;
	}
	if(form1.password.value==""){		//当密码为空时
		alert("请输入密码！");form1.pwd.focus();return;
	}
	if(form1.rePassWord.value==""){		//当没有输入确认密码时
		alert("请确认密码！");form1.rePassWord.focus();return;
	}
	if(form1.email.value==""){		//当E-mail地址为空时
		alert("请输入E-mail地址！");form1.email.focus();return;
	}
	if(flag_user && flag_pwd && flag_repwd && flag_email){	//所有数据都符合要求时
		//注册申请代码
	}else{
		alert("您填写的注册信息不合法，请确认！");
	}
}
