<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="dominio.conocimiento.Medico" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="excepciones.SesionNoIniciadaException"%>

<%
	Medico med;
	if (request.getSession(false)==null)
		throw new SesionNoIniciadaException("No se puede acceder a una página interna si no se inicia sesión previamente");
	else {
		med = (Medico) request.getSession(false).getAttribute("Medico");
		if (med==null)
			throw new SesionNoIniciadaException("No se puede acceder a una página interna si no se inicia sesión previamente");
	}
%>
	
Medico emisor: <input type="text" value="Dr./Dra. <%= med.getApellidos()%>" readonly="readonly"/>
<!-- El "select" para mostrar la especialidad es un select normal donde se asociará al evento "onchange" la función de AJAX -->

<br><br><s:select id="beneficiario" name="beneficiario" list="beneficiarios" listKey="nif" listValue="apellidos, nombre" label="Beneficiario"></s:select>
<br>
<s:select id="especialidad" name="especialidad" list="especialidades" label="Especialidad" onchange="cargarReceptor('ajaxObtenerEspecialistas.jsp')">
</s:select>
<span id="especialistasCargados">
</span>
<br><input type="button" onclick="darVolante('ajaxEmitirVolante.jsp')" value="Aceptar">
<br>
<span id="mensaje">
</span>

<div class="volver">
	<input type="button" onclick="history.go(-1)" value="Volver atrás">
</div>


