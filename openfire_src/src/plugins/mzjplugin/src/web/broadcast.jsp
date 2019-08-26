<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
<title>测试</title>
<meta name="pageID" content="broadcast"/><!-- 这个要对应plugin.xml里面的item的id -->
</head>
<body>
	<form action="/plugins/mzjplugin/broadcast">
		username:<input type="text" name="username">
		<br/>
		password:<input type="password" name="password">
		<br/>
		body:<input type="text" name="body">
		<br/>
		subject:<input type="text" name="subject">
		<br/>
		<input type="submit" value="提交">
	</form>
</body>
</html>
