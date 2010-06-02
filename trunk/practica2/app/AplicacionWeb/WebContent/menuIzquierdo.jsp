<%@page import="dominio.conocimiento.Beneficiario"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
if (request.getSession() == null || (request.getSession(false).getAttribute("Medico") == null && request.getSession(false).getAttribute("Beneficiario") == null)) {
	// si no existe sesión
	out.println("<a href=\"javascript:cargarModulo('loginMedico.jsp')\">Iniciar sesi&oacute;n como m&eacute;dico</a>");
	out.println("<br />");
	out.println("<a href=\"javascript:cargarModulo('loginBeneficiario.jsp')\">Iniciar sesi&oacute;n como beneficiario</a>");
}
else {
	out.println("<a href=\"logout.jsp\">Cerrar sesi&oacute;n</a>");
	out.println("<br />");
	// si existe, diferenciamos si es médico o beneficario
	if (request.getSession(false).getAttribute("Medico") != null) {
		//es médico
		out.println("<a href=\"javascript:cargarModulo('obtenerBeneficiariosMedico')\" title=\"Emitir volante\" >Emitir Volante</a>");
	}
	else if (request.getSession(false).getAttribute("Beneficiario") != null) {
		//es beneficiario
		String mensaje = "de cabecera";
		if (((Beneficiario)request.getSession(false).getAttribute("Beneficiario")).getEdad()<14) 
			mensaje = "pediatra";
		out.println("<a href=\"javascript:cargarModulo('citaCabecera.jsp')\" title=\"Pedir cita con su médico " + mensaje + "\">Pedir cita con su m&eacute;dico " + mensaje + "</a><br/>");
		out.println("<a href=\"javascript:cargarModulo('citaEspecialista.jsp')\" title=\"Pedir cita con su médico especialista\">Pedir cita con su m&eacute;dico especalista </a><br/>");
		out.println("<a href=\"javascript:cargarModulo('anularCita.jsp')\" title=\"Anular una cita\">Anular una cita</a>");
		
	}
}
%>