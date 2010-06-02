<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" %>
<%@ page import="dominio.conocimiento.Beneficiario" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
	
<script type="text/javascript">
	function setLocation(url) {
		document.location = url;
	}
</script>

<%
Object attr;

// Comprobamos si el cliente no tiene ninguna sesi�n activa
if(request.getSession(false) == null || request.getSession(false).getAttribute("Beneficiario") == null) {
	// Creamos una nueva sesi�n HTTP
	session = request.getSession(true);
	// Obtenemos la sesi�n y el beneficiario pasados a la p�gina JSP
	// desde la acci�n 'loginBeneficiario' de Struts 2; si alg�n valor
	// es null, es porque se ha accedido incorrectamente a esta p�gina
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