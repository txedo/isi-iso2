<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" %>
<%@ page errorPage= "error.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<%@ include file="resources/templates/header.htm" %>
	<title>SSCAWeb - Cita tramitada</title>
</head>
<body>
	<%@ include file="resources/templates/top.htm" %>
	
	<div id="contenido">
    	<div class="textoCuerpo">
    		Se ha tramitado correctamente la cita para el d&iacute;a <%=request.getParameter("dia")%>, a las <%=request.getParameter("hora")%>.
		</div>
	</div>
	
	<%@ include file="resources/templates/foot.htm" %>
</body>
</html>
