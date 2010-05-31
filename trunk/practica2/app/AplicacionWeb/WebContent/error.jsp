<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<%@ include file="header.jsp"%>
	<title>SSCAWeb - Error</title>
</head>
<body>
	<%@ include file="top.jsp"%>
	
	<div id="contenido">
    	<div class="textoCuerpo">
    		<!-- Excepci�n provocada en una acci�n de struts -->
			<s:property value="exception.message"/> <br>
			<button onclick="javascript:history.go(-1)">Atr&aacute;s</button>
		</div>
	</div>
	
	<%@ include file="foot.jsp"%>
</body>
</html>
