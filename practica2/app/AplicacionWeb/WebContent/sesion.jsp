<%@page import="dominio.conocimiento.Beneficiario"%>
<%@page import="dominio.conocimiento.Medico"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
if (request.getSession() == null || (request.getSession(false).getAttribute("Medico") == null && request.getSession(false).getAttribute("Beneficiario") == null)) {
	// si no existe sesi�n
	out.println("Usted no ha iniciado sesi&oacute;n");
}
else {
	String usuario = "";
	// si existe, diferenciamos si es m�dico o beneficario
	if (request.getSession(false).getAttribute("Medico") != null) {
		//es m�dico
		usuario = ((Medico)request.getSession(false).getAttribute("Medico")).getNombre();
	}
	else if (request.getSession(false).getAttribute("Beneficiario") != null) {
		//es beneficiario
		usuario = ((Beneficiario)request.getSession(false).getAttribute("Beneficiario")).getNombre();
	}
	out.println("Sesi&oacute;n iniciada como "+usuario+" (<a href=\"javascript:cargarModulo('logout.jsp')\" title=\"Terminar la sesi�n activa\">Cerrar sesi&oacute;n</a>)");
}
%>