<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<script type="text/javascript" src="resources/scripts/funciones.js"></script>

<%
//Si se accede directamente en el navegador a esta p�gina, se redirecciona al index.jsp
if (request.getHeader("referer")==null) { 
%>
	<script type="text/javascript">setLocation("index.jsp");</script>
<%
}

HttpSession sesionHTTP;

// Obtenemos la sesi�n HTTP y borramos todos sus objetos
sesionHTTP = request.getSession(true);
sesionHTTP.invalidate();
%>

Sesi�n cerrada correctamente.
<br /><br />
En unos segundos ser� redirigido a la p�gina principal.
