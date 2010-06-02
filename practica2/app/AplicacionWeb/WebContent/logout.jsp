<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<%@ include file="resources/templates/header.htm" %>
	<title>SSCAWeb - Cierre de Sesi�n</title>
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
   	
   		// Obtenemos la sesi�n HTTP y borramos todos sus objetos
   		sesionHTTP = request.getSession(true);
   		sesionHTTP.invalidate();
   	%>
	
    <div id="contenido">
    	<div class="textoCuerpo">
	    	Sesi�n cerrada correctamente.
	    	<br><br>
	    	En unos segundos ser� redirigido a la p�gina principal.
	    </div>
	</div>

	<script type="text/javascript">
		setLocationTimeout('index.jsp', 3);
	</script>
    
	<%@ include file="resources/templates/foot.htm" %>
</body>
</html>
