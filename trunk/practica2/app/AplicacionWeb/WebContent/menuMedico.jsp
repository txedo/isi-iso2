<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" %>
<%@ page import="dominio.conocimiento.Medico" %>
<%@ page errorPage="error.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<script type="text/javascript">
	function setLocation(url) {
		document.location = url;
	}
</script>


	
<%
Medico medico;
Object attr;
boolean sesionInvalida;

// Comprobamos si el cliente no tiene ninguna sesión activa
sesionInvalida = false;
if(request.getSession(false) == null || request.getSession(false).getAttribute("Medico") == null) {
	// Creamos una nueva sesión HTTP
	session = request.getSession(true);
	// Obtenemos la sesión y el médico pasados a la página JSP desde
	// la acción 'loginMedico' de Struts 2; si algún valor es null,
	// es porque se ha accedido incorrectamente a esta página
	attr = request.getAttribute("medico");
	if(attr == null) {
		sesionInvalida = true;
	} else {
		session.setAttribute("Medico", attr);
	}
	attr = request.getAttribute("sesion");
	if(attr == null) {
		sesionInvalida = true;
	} else {
		session.setAttribute("SesionFrontend", attr);
	}
%>
	<script type="text/javascript">setLocation('index.jsp');</script>
<%
}
%>