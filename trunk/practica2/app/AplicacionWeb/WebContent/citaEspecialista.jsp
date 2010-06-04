<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="dominio.conocimiento.Beneficiario" %>
<%@ page errorPage= "error.jsp" %>
<%@page import="excepciones.SesionNoIniciadaException"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<script type="text/javascript" src="resources/scripts/funciones.js"></script>

<%  Beneficiario b = null;
	if(request.getSession(false)==null) {
		throw new SesionNoIniciadaException("No se puede acceder a una p�gina interna si no se inicia sesi�n previamente");
	} else {
		b = (Beneficiario) request.getSession(false).getAttribute("Beneficiario");
		if (b==null) 
			throw new SesionNoIniciadaException("No se puede acceder a una p�gina interna si no se inicia sesi�n previamente");
	}
	
	// Si se accede directamente en el navegador a esta p�gina, se redirecciona al index.jsp
	if (request.getHeader("referer")==null) { 
	%>
		<script type="text/javascript" >setLocation("index.jsp");</script>
	<%
	}
%>
Introduzca el identificador de su volante: <input type="text" id="nVolante" name="nVolante"></input>
<input type="submit" value="Aceptar" onclick="validarVolante('ajaxValidarVolante.jsp')"/>

<br><br>
<span id="spanEspecialista">
</span>

<!-- En este div se carga el datepicker -->
<div id="campofecha" style="visibility:hidden"></div>
<div id="campohoras">
	<span id="spanHoras"></span>
	<br>
	<span id="mensaje"></span>
</div>
