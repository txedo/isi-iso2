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
	
		<ul class="menu">
			<div class="navegacion">Inicio</div>
		</ul>
		<!-- Menú izquierdo -->
		<div id="izquierdo">
			<%@ include file="menuIzquierdo.jsp" %>
		</div>
		<!-- Menú de selección de rol -->
	    <div id="contenido">
	        <div class="textoCuerpo" style="text-align:center;">		         
				<span id="mod">
				</span>
	        </div>
	    </div>
	    
	<%@ include file="resources/templates/foot.htm" %>
</body>
</html>
