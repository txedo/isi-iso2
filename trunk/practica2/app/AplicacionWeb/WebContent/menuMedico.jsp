<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="dominio.conocimiento.Medico" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<%@ include file="header.jsp" %>
	<title>SSCAWeb - Menú Médico</title>
</head>
<body>
	<%@ include file="top.jsp" %>
    <div id="contenido">
	<%
		// En la sesion, se podria guardar el objeto que haya colocado la accion en la ValueStack,
		// para luego usarlo en las otras páginas para sacar el nombre, por ejemplo.
		if (request.getSession(false) == null || request.getSession(false).getAttribute("Medico") == null) {
			session=request.getSession(true);
			session.setAttribute("Medico",request.getAttribute("medico"));
			session.setAttribute("SesionFrontend", request.getAttribute("sesion"));
		}
		Medico medi = (Medico) request.getSession(false).getAttribute("Medico"); 
	%>
		<div class="textoCuerpo">
			Bienvenido/a, <%= medi.getNombre() %>.<br><br>
			Elija una opción: <br>
		</div>
		<div style="padding-left:250px;">
			<!-- <input type="radio" name="grupoRadio" value="darVolante" onclick="document.location='darVolante'" >Emitir volante-->
			<!-- <s:action name="darVolante" executeResult="true"/> -->
			<a href="darVolante">Emitir Volante</a>
		</div>
	</div>
	
	<%@ include file="foot.jsp" %>
</body>
</html>