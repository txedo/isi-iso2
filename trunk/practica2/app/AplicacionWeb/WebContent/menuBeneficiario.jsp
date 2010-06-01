<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" %>
<%@ page import="dominio.conocimiento.Beneficiario" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<%@ include file="header.jsp" %>
	<title>SSCAWeb - Menú Beneficiario</title>
	<script type="text/javascript">
		function setLocation(url) {
			document.location = url;
		}
	</script>
</head>
<body>
	<%@ include file="top.jsp" %>

	<%
		Beneficiario beneficiario;
		Object attr;
		String tipoMedico;
		boolean sesionInvalida;
		
		// Comprobamos si el cliente no tiene ninguna sesión activa
		sesionInvalida = false;
		if(request.getSession(false) == null || request.getSession(false).getAttribute("Beneficiario") == null) {
			// Creamos una nueva sesión HTTP
			session = request.getSession(true);
			// Obtenemos la sesión y el beneficiario pasados a la página JSP
			// desde la acción 'loginBeneficiario' de Struts 2; si algún valor
			// es null, es porque se ha accedido incorrectamente a esta página
			attr = request.getAttribute("beneficiario");
			if(attr == null) {
				sesionInvalida = true;
			} else {
				session.setAttribute("Beneficiario", attr);
			}
			attr = request.getAttribute("sesion");
			if(attr == null) {
				sesionInvalida = true;
			} else {
				session.setAttribute("SesionFrontend", attr);
			}
		}
	
	%>
	
	<%
		if(!sesionInvalida) {
			
			// Obtenemos los datos del beneficiario
			beneficiario = (Beneficiario)session.getAttribute("Beneficiario");
			// Comprobamos si el beneficiario tiene médico de cabecera o pediatra
			if(beneficiario.getEdad() > 14) {
				tipoMedico = "de cabecera";
			} else {
				tipoMedico = "pediatra";
			}
	%>
	
			<!-- Sesión válida -->
		    <div id="contenido">
				<div class="textoCuerpo">
					Bienvenido/a, <%= beneficiario.getNombre() %>.<br><br>
					Elija una opción: <br>
				</div>
				<div style="padding-left:250px;">
					<input type="radio" name="grupoRadio" value="citaCabecera" onclick="document.location='citaCabecera.jsp'">
					Cita con su médico <%= tipoMedico %><br>
					<input type="radio" name="grupoRadio" value="citaEspecialista" onclick="document.location='citaEspecialista.jsp'">
					Cita con un médico especialista<br>
					<input type="radio" name="grupoRadio" value="anularCita" onclick="document.location='anularCita.jsp'">
					Anular cita
				</div>
				<br>
				<div style="padding-left:250px;">
					<button onclick="setLocation('logout.jsp');">Cerrar sesión</button>
				</div>
			</div>
	
	<%
		} else {
	%>
	
			<!-- Sesión inválida -->
			<script type="text/javascript">
				setLocation('index.jsp');
			</script>
	
	<%
		} // Fin if(!sesionInvalida)
	%>
	
	<%@ include file="foot.jsp" %>
</body>
</html>
