<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" %>

<script type="text/javascript" src="resources/scripts/funciones.js"></script>

<%
//Si se accede directamente en el navegador a esta página, se redirecciona al index.jsp
if (request.getHeader("referer")==null) { 
%>
	<script type="text/javascript">setLocation("index.jsp");</script>
<%
}

HttpSession sesionHTTP;

// Obtenemos la sesión HTTP y borramos todos sus objetos
sesionHTTP = request.getSession(true);
sesionHTTP.invalidate();
%>

Sesión cerrada correctamente.
<br><br>
En unos segundos será redirigido a la página principal.
