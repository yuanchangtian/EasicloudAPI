<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新增设备</title>
</head>
<body>
	<div id="addDevice"
		style="width: 663; height: 421; background-color: #546B51; padding: 4px; position: absolute; z-index: 11; display: none;">
		<form name="form1" action="" method="post">
			<table width="100%" height="100%" border="0" cellpadding="0"
				cellspacing="0" bgcolor="#FEFEFC">
				<tr>
					<td height="408" align="center" valign="top"><table
							width="100%" border="0" cellpadding="0" cellspacing="0">
							<tr>
								<td height="50" style="color: #1B7F5D; font-size: 14px;"><b>清爽夏日九宫格日记网--用户注册</b></td>
							</tr>
						</table>
						<table width="94%" height="331" border="0" cellpadding="0"
							cellspacing="1" bgcolor="#CCCCCC">
							<tr>
								<td height="310" valign="top" bgcolor="#FFFFFF"><table
										border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFEF9">
										<tr id="tr_user" style="display: none">
											<td height="40" colspan="2" align="center"><div
													id="div_user"
													style="border: #FF6600 1px solid; color: #FF0000; width: 90%; height: 29px; padding-top: 8px;"></div></td>
										</tr>
										<tr>
											<td width="93" height="40" align="right">用户名：</td>
											<td height="40" align="left"><input name="user"
												type="text" onBlur="checkUser(this.value)">
												&nbsp;*长度限制为20个字母或10个汉字</td>
										</tr>
										<tr id="tr_pwd" style="display: none">
											<td height="40" colspan="2" align="center"><div
													id="div_pwd"
													style="border: #FF6600 1px solid; color: #FF0000; width: 90%; height: 29px; padding-top: 8px; background-image: url(images/div_bg.jpg)"></div></td>
										</tr>
										<tr>
											<td height="40" align="right">密码：</td>
											<td height="40" align="left"><input name="pwd"
												type="password" onBlur="checkPwd(this.value)">
												&nbsp;* 密码由字母开头的字母、数字或下划线组成，并且密码的长度大于6位小于30位</td>
										</tr>
										<tr>
											<td height="40" align="right">确认密码：</td>
											<td height="40" align="left"><input name="repwd"
												type="password" onBlur="checkRepwd(this.value)">
												&nbsp;* 请确认密码</td>
										</tr>
										<tr id="tr_email" style="display: none">
											<td height="40" colspan="2" align="center"><div
													id="div_email"
													style="border: #FF6600 1px solid; color: #FF0000; width: 90%; height: 29px; padding-top: 8px; background-image: url(images/div_bg.jpg)"></div></td>
										</tr>
										<tr>
											<td height="40" align="right">E-mail地址：</td>
											<td height="40" align="left"><input name="email"
												type="text" size="35" onBlur="checkEmail(this.value)">
												&nbsp;* 请输入有效的E-mail地址，在找回密码时应用</td>
										</tr>
										<tr>
											<td height="40" align="right">所在地：</td>
											<td height="40" align="left"><select name="province"
												id="province" onChange="getCity(this.value)">
											</select> - <select name="city" id="city">
											</select></td>
										</tr>
										<tr>
											<td height="40" colspan="2" align="center">以下两个选项，只要有任何一个没有输入，将不可以通过答案问题重新设置密码。</td>
										</tr>
										<tr id="tr_question" style="display: none">
											<td height="40" colspan="2" align="center"><div
													id="div_question"
													style="border: #FF6600 1px solid; color: #FF0000; width: 90%; height: 29px; padding-top: 8px; background-image: url(images/div_bg.jpg)"></div></td>
										</tr>
										<tr>
											<td height="40" align="right">密码提示问题：</td>
											<td height="40" align="left"><input name="question"
												type="text" id="question" size="35"
												onBlur="checkQuestion(this.value,this.form.answer.value)">
												如：我的工作单位</td>
										</tr>
										<tr id="tr_answer" style="display: none">
											<td height="40" colspan="2" align="center"><div
													id="div_answer"
													style="border: #FF6600 1px solid; color: #FF0000; width: 90%; height: 29px; padding-top: 8px; background-image: url(images/div_bg.jpg)"></div></td>
										</tr>
										<tr>
											<td height="40" align="right">提示问题答案：</td>
											<td height="40" align="left"><input name="answer"
												type="text" id="answer" size="35"
												onBlur="checkQuestion(this.form.question.value,this.value)">
												如：明日科技</td>
										</tr>
										<tr>
											<td height="40">&nbsp;</td>
											<td height="40" align="center"><input name="btn_sumbit"
												type="button" class="btn_grey" value="提交" onClick="save()">
												&nbsp; <input name="btn_reset" type="button"
												class="btn_grey" value="重置" onClick="form_reset(this.form)">
												&nbsp; <input name="btn_close" type="button"
												class="btn_grey" value="关闭" onClick="Myclose('register')"></td>
										</tr>

									</table></td>
							</tr>
						</table></td>
				</tr>
				<tr>
					<td height="10" align="center" valign="top" bgcolor="#FEFEFC">&nbsp;</td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>