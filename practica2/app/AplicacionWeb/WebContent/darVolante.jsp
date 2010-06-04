<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="dominio.conocimiento.Medico" %>
<%@ page import="dominio.conocimiento.Beneficiario" %>
<%@ page import="java.util.Vector"%>
<%@ page errorPage="error.jsp"%>
<%@page import="excepciones.SesionNoIniciadaException"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<script type="text/javascript" src="resources/scripts/funciones.js"></script>
<%
Medico med;
if (request.getSession(false)==null)
	throw new SesionNoIniciadaException("No se puede acceder a una página interna si no se inicia sesión previamente");
else {
	med = (Medico) request.getSession(false).getAttribute("Medico");
	if (med==null)
		throw new SesionNoIniciadaException("No se puede acceder a una página interna si no se inicia sesión previamente");
}

//Si se accede directamente en el navegador a esta página, se redirecciona al index.jsp
if (request.getHeader("referer")==null) { 
%>
	<script type="text/javascript" >setLocation("index.jsp");</script>
<%
}

// Accedemos a los valores de la ValueStack
Vector<Beneficiario> beneficiarios = (Vector<Beneficiario>)request.getAttribute("beneficiarios");
Vector<String> especialidades = (Vector<String>)request.getAttribute("especialidades");
%>
	
<!-- El "select" para mostrar la especialidad es un select normal donde se asociará al evento "onchange" la función de AJAX -->
<%@page import="java.util.Collections"%>
<table style="margin:0 auto;" border="0" cellpadding="2" cellspacing="2">
	<tr>
		<td align="left">Beneficiario:</td>
		<td align="left"><select id="beneficiario">
<%
			if (beneficiarios.size() == 0) {
				out.println("<option selected value=\"-1\">No existen beneficiarios</option>");
			}
			else {
				out.println("<option selected value=\"" + beneficiarios.get(0).getNif() +"\">" + beneficiarios.get(0).getNombre() + " " + beneficiarios.get(0).getApellidos()+ "</option>");
				for (int i=1; i<beneficiarios.size(); i++) {
					out.println("<option value=\"" + beneficiarios.get(i).getNif() +"\">" + beneficiarios.get(i).getNombre() + " " + beneficiarios.get(i).getApellidos()+ "</option>");
				}
			}
%>
		</select></td>
	</tr>
	<tr>
		<td align="left">Especialidad:</td>
		<td align="left"><select id="especialidad" onchange="cargarReceptor('ajaxObtenerEspecialistas.jsp')">
<%
			if (especialidades.size() == 0) {
				out.println("<option selected value=\"-1\">No existen especialidades</option>");
			}
			else {
				out.println("<option selected value=\"" + especialidades.get(0) +"\">" + especialidades.get(0) + "</option>");
				for (int i=1; i<especialidades.size(); i++) {
					out.println("<option value=\"" + especialidades.get(i) +"\">" + especialidades.get(i) + "</option>");
				}
			}
%>
		</select></td>
	</tr>
	<tr>
		<td align="left">Especialista:</td>
		<td align="left"><span id="especialistasCargados"></span></td>
	</tr>
	<tr>
		<td colspan="2" align="right"><input type="button" onclick="darVolante('ajaxEmitirVolante.jsp')" value="Emitir volante"></td>
	</tr>
</table>



