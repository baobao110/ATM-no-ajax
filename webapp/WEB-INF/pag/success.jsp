<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="content-type" content="text/html;charset=utf-8">
	<title></title>

</head>
<body>
<h1>ATM系统-业务办理成功</h1>
卡号 	${card.getNumber()} <br>
金额   ${card.getMoney() }  <br>

<a href="/card/toUsercenter.do">返回个人中心</a>
</body>

</html>