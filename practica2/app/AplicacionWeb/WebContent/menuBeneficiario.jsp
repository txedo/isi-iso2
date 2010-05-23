<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="dominio.conocimiento.Beneficiario" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="resources/styles/style.css" type="text/css" />
<title>Menu Beneficiario</title>
</head>
<body>
	<div id="header">
        <div class="textoCabecera">Título de la cabecera</div>
	</div>
    <div id="contenido">
	<%
		// En la sesion, se podria guardar el objeto que haya colocado la accion en la ValueStack,
		// para luego usarlo en las otras páginas para sacar el nombre, por ejemplo.
		session=request.getSession(true);
		session.setAttribute("Beneficiario",request.getAttribute("beneficiario"));
		session.setAttribute("SesionFrontend", request.getAttribute("sesion"));
		Beneficiario bene = (Beneficiario) request.getSession(false).getAttribute("Beneficiario"); 
	%>
		<div class="textoCuerpo">
			Bienvenido, <%= bene.getNombre() %> <br><br>
			Elija una opción: <br>
		</div>
		<div style="padding-left:130px;">
			<input type="radio" name="grupoRadio" value="citaCabecera" onclick="document.location='citaCabecera.jsp'" >Cita con su médico 
			<%  // Se muestra el tipo de médico adecuado en la opción, según la edad del beneficiario
				String mensaje; 
				if (bene.getEdad()> 14)
					mensaje = "de cabecera";
				else
					mensaje = "pediatra";
			%> <%= mensaje %>
			<br>
			<input type="radio" name="grupoRadio" value="citaEspecialista" onclick="document.location='citaEspecialista.jsp'" >Cita con un médico especialista<br>
			<input type="radio" name="grupoRadio" value="anularCita" onclick="document.location='anularCita.jsp'">Anular cita
		</div>
	</div>
	<div id="pie">
        Texto del pie
    </div>
</body>
</html>