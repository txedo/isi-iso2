<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" %>
<%@ page errorPage="error.jsp" %>

<%
Object attr;

// Comprobamos si el cliente no tiene ninguna sesión activa
if(request.getSession(false) == null || request.getSession(false).getAttribute("Medico") == null) {
	// Creamos una nueva sesión HTTP
	session = request.getSession(true);
	// Obtenemos la sesión y el médico pasados a la página JSP desde
	// la acción 'loginMedico' de Struts 2; si algún valor es null,
	// es porque se ha accedido incorrectamente a esta página
	attr = request.getAttribute("medico");
	if(attr != null) {
		session.setAttribute("Medico", attr);
	}
	attr = request.getAttribute("sesion");
	if(attr != null) {
		session.setAttribute("SesionFrontend", attr);
	}
%>
	<script type="text/javascript">document.location = "index.jsp";</script>
<%
}
%>