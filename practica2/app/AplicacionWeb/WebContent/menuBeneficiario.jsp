<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="dominio.conocimiento.Beneficiario" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Menu Beneficiario</title>
</head>
<body>
	<%
		// En la sesion, se podria guardar el objeto que haya colocado la accion en la ValueStack,
		// para luego usarlo en las otras p�ginas para sacar el nombre, por ejemplo.
		session=request.getSession(true);
		session.setAttribute("Beneficiario",request.getAttribute("beneficiario"));
	%>
	Bienvenido, <%= ((Beneficiario) request.getSession(false).getAttribute("Beneficiario")).getNombre() %> <br>
	Elija una opci�n: <br>
	<input type="radio" name="grupoRadio" value="citaCabecera" onclick="window.location.href('citaCabecera.jsp')" >Cita con su m�dico de cabecera<br>
	<input type="radio" name="grupoRadio" value="citaEspecialista" onclick="window.location.href('citaEspecialista.jsp')" >Cita con un m�dico especialista<br>
	<input type="radio" name="grupoRadio" value="anularCita" onclick="window.location.href('anularCita.jsp')" >Anular cita
	
</body>
</html>