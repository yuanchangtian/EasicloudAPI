<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<div class="content">
        <br />
        <table style="width: 75%;background-color:#E2F1F9;" align="center" cellpadding="1" cellspacing="1">
            <tr>
                <th align="left" style="text-align:center;padding:5px;color:#0E90D2;">
                     提示信息
                </th>
            </tr>
            <tr class="tr1">
                <td style="background-color:white;padding:5px;">
                    <span style="font-size: 14px;">
                        错误的Id</span>
                    <br />
                    <br />
                    <a href="javascript:GoBack()" target="">确定
                    </a>
                    <br />
                </td>
            </tr>
        </table>

        <script type="text/javascript"></script>

    </div>

    <div id="window">
        <div id="windowTop">
            <div id="windowTopContent">
                Loading...
            </div>
            <img alt="关闭窗口" src="/admin/Content/images/window/window_close.jpg" id="windowClose" onclick="closeWindow()">
        </div>
        <div id="windowBottom">
            <div id="windowBottomContent">
                &nbsp;</div>
        </div>
        <div id="windowContent">
            <div class="windowLoading">
                &nbsp;</div>
        </div>
        <img src="/admin/Content/images/window/window_resize.gif" id="windowResize" alt="拖拽">
    </div>

</body>
</html>