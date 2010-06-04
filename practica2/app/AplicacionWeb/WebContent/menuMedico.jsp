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

// Comprobamos si el cliente no tiene ninguna sesi�n activa
sesionInvalida = false;
if(request.getSession(false) == null || request.getSession(false).getAttribute("Medico") == null) {
	// Creamos una nueva sesi�n HTTP
	session = request.getSession(true);
	// Obtenemos la sesi�n y el m�dico pasados a la p�gina JSP desde
	// la acci�n 'loginMedico' de Struts 2; si alg�n valor es null,
	// es porque se ha accedido incorrectamente a esta p�gina
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