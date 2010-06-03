<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page isErrorPage="true" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<script type="text/javascript" src="resources/scripts/ajax.js"></script>
<script type="text/javascript" src="resources/scripts/funciones.js"></script>

<!-- Excepciones lanzadas al aaceder a una página interna sin iniciar sesión (provocadas en paginas JSP) -->
<% 
// Si el objeto exception es nulo, es poruqe se trataba de un error en una acción de struts, por lo que no se hace nada más
String error = "";
if ((Exception)request.getAttribute("exception") != null) {
	error = ((Exception)request.getAttribute("exception")).getMessage();
}
else {
	if (exception != null) {
		if (exception.getClass().getSimpleName().equals("NullPointerException")) {
			error = "No se puede acceder a una p&aacute;gina interna si no se inicia sesi&oacute;n previamente";
	    } else {
			error = exception.getMessage();
	    } 
	}
}
%>

<script type="text/javascript" >setLocation("index.jsp?error=<%=error%>");</script>