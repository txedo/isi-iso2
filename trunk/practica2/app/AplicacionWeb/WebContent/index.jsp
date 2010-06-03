<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<%@ include file="resources/templates/header.htm" %>
	<title>SSCAWeb</title>
</head>
<body>	
	<%@ include file="resources/templates/top.htm" %>
	
	<div class="menu">
		<div class="navegacion">Inicio</div>
	</div>
	
	<!-- El contenido es el men� y el texto de la p�gina -->
	<div id="contenido">
		<!-- Men� izquierdo -->
		<div id="izquierdo">
			<%@ include file="menuIzquierdo.jsp" %>
		</div>
		<!-- Texto de la p�gina -->
        <div id="textoCuerpo">         
			<span id="mod">
			</span>
		</div>
    </div>
	    
	<%@ include file="resources/templates/foot.htm" %>
</body>
</html>
