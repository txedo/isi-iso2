<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" %>
<%@ page errorPage="error.jsp" %>

<%
Object attr;

// Comprobamos si el cliente no tiene ninguna sesi�n activa
if(request.getSession(false) == null || request.getSession(false).getAttribute("Medico") == null) {
	// Creamos una nueva sesi�n HTTP
	session = request.getSession(true);
	// Obtenemos la sesi�n y el m�dico pasados a la p�gina JSP desde
	// la acci�n 'loginMedico' de Struts 2; si alg�n valor es null,
	// es porque se ha accedido incorrectamente a esta p�gina
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