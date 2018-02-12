<%@ page language="java" pageEncoding="UTF-8" %>
<%
	String contextPath = request.getContextPath();
%>

<!DOCTYPE html>
<html>
<head>
    <title>index Page</title>
    <link type="text/css" rel="stylesheet" href="index.css">
    <script type="text/javascript">
		var projectPath = '<%=contextPath %>';
	</script>
</head>
<body>
	<div class="bd">
		<div class="item aja">
			<span class="title">Ajax访问：</span>
			<span><input type="text" value="Hello Ajax!"/></span>
	    	<span><button>发送消息</button></span>
		</div>
		
		<div class="item ws">
			<span class="title">Websocket访问：</span>
			<span><input type="text" value="Hello Websocket!"/></span>
	    	<span><button>发送消息</button></span>
		</div>
		
	</div>
</body>
<script type="text/javascript" src="jquery.min.js"></script>
<script type="text/javascript" src="index.js"></script>
</html>