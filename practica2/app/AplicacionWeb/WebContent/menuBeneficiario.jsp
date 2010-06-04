<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" %>
<%@ page errorPage="error.jsp" %>

<script type="text/javascript" src="resources/scripts/funciones.js"></script>

<%
Object attr;

// Comprobamos si el cliente no tiene ninguna sesión activa
if(request.getSession(false) == null || request.getSession(false).getAttribute("Beneficiario") == null) {
	// Creamos una nueva sesión HTTP
	session = request.getSession(true);
	// Obtenemos la sesión y el beneficiario pasados a la página JSP
	// desde la acción 'loginBeneficiario' de Struts 2; si algún valor
	// es null, es porque se ha accedido incorrectamente a esta página
	attr = request.getAttribute("beneficiario");
	if(attr != null) {
		session.setAttribute("Beneficiario", attr);
	}
	attr = request.getAttribute("sesion");
	if(attr != null) {
		session.setAttribute("SesionFrontend", attr);
	}
}
%>
<script type="text/javascript">setLocation('index.jsp');</script>	