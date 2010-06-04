<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" %>
<%@ page import="dominio.conocimiento.Beneficiario" %>
<%@ page import="dominio.conocimiento.Medico" %>
<%@ page import="dominio.conocimiento.CategoriasMedico" %>
<%@ page errorPage= "error.jsp" %>
<%@page import="excepciones.SesionNoIniciadaException"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%
if (request.getSession(false)==null) {
	throw new SesionNoIniciadaException("No se puede acceder a una p�gina interna si no se inicia sesi�n previamente");
}
// Si se accede directamente en el navegador a esta p�gina, se redirecciona al index.jsp
else if (request.getHeader("referer")==null) {
	
}

Beneficiario beneficiario = null;
Medico medico = null;
String tipoMedico = null;
boolean tieneMedico = true;
beneficiario = (Beneficiario)request.getSession(false).getAttribute("Beneficiario");
if (beneficiario == null)
	throw new SesionNoIniciadaException("No se puede acceder a una p�gina interna si no se inicia sesi�n previamente");
medico = beneficiario.getMedicoAsignado();
if (medico == null)
	tieneMedico = false;
else 
	tipoMedico = (medico.getTipoMedico().getCategoria() == CategoriasMedico.Cabecera) ? "m�dico de cabecera" : "pediatra";

  		if (tieneMedico) { %>
  			Su <%= tipoMedico %> es el Dr./Dra. <%= medico.getApellidos() %>.
		<%@page import="excepciones.AccesoInvalidoException"%>
<br /><br />
		Seleccione el d&iacute;a y hora de la cita:
		<br /><br />
		<!-- En este div se carga el datepicker -->
		<div id="campofecha"></div>
		<div id="campohoras">
			<span id="spanHoras"></span>
			<br>
			<span id="mensaje"></span>
		</div>
<%  } else { %>
	<%= beneficiario.getNombre() %>, no tiene m&eacute;dico asignado, por lo que no puede pedir una cita
<%  } %>
