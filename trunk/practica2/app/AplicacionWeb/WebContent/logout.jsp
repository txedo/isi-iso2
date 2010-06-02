<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<%@ include file="resources/templates/header.htm" %>
	<title>SSCAWeb - Cierre de Sesión</title>
	<script type="text/javascript">
		function setLocationTimeout(url, timeout) {
			setTimeout("document.location='" + url + "'", timeout * 1000);
		}
	</script>
</head>
<body>
	<%@ include file="resources/templates/top.htm" %>

   	<%
   		HttpSession sesionHTTP;
   	
   		// Obtenemos la sesión HTTP y borramos todos sus objetos
   		sesionHTTP = request.getSession(true);
   		sesionHTTP.invalidate();
   	%>
	
    <div id="contenido">
    	<div class="textoCuerpo">
	    	Sesión cerrada correctamente.
	    	<br><br>
	    	En unos segundos será redirigido a la página principal.
	    </div>
	</div>

	<script type="text/javascript">
		setLocationTimeout('index.jsp', 3);
	</script>
    
	<%@ include file="resources/templates/foot.htm" %>
</body>
</html>
