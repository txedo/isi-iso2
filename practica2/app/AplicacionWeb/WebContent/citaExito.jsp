<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<%@ include file="header.jsp"%>
	<title>SSCAWeb - Cita tramitada</title>
</head>
<body>
	<%@ include file="top.jsp"%>
	
	<div id="contenido">
    	<div class="textoCuerpo">
    		Se ha emitido la cita el d&iacute;a <%=request.getParameter("dia")%>, a las <%=request.getParameter("hora")%>
		</div>
	</div>
	
	<%@ include file="foot.jsp"%>
</body>
</html>