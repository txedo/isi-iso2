<%@page import="dominio.conocimiento.Beneficiario"%>
<%@page import="dominio.conocimiento.Medico"%>
<%@ page errorPage="error.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%
if (request.getSession() == null || (request.getSession(false).getAttribute("Medico") == null && request.getSession(false).getAttribute("Beneficiario") == null)) {
	// si no existe sesión
	out.println("Usted no ha iniciado sesi&oacute;n");
}
else {
	String usuario = "";
	// si existe, diferenciamos si es médico o beneficario
	if (request.getSession(false).getAttribute("Medico") != null) {
		//es médico
		Medico m = (Medico)request.getSession(false).getAttribute("Medico");
		usuario = m.getNombre() + " " + m.getApellidos();
	}
	else if (request.getSession(false).getAttribute("Beneficiario") != null) {
		//es beneficiario
		Beneficiario b = (Beneficiario)request.getSession(false).getAttribute("Beneficiario");
		usuario = b.getNombre() + " " + b.getApellidos();
	}
	out.println("Sesi&oacute;n iniciada como "+usuario+" (<a href=\"javascript:cargarModulo('logout.jsp')\" title=\"Terminar la sesión activa\">Cerrar sesi&oacute;n</a>)");
}
%>