<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" %>
<%@ page import="dominio.conocimiento.Beneficiario" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<%@ include file="resources/templates/header.htm" %>
	<title>SSCAWeb - Men� Beneficiario</title>
	<script type="text/javascript">
		function setLocation(url) {
			document.location = url;
		}
	</script>
</head>
<body>
	<%@ include file="resources/templates/top.htm" %>

	<%
		Beneficiario beneficiario;
		Object attr;
		String tipoMedico;
		boolean sesionInvalida;
		
		// Comprobamos si el cliente no tiene ninguna sesi�n activa
		sesionInvalida = false;
		if(request.getSession(false) == null || request.getSession(false).getAttribute("Beneficiario") == null) {
			// Creamos una nueva sesi�n HTTP
			session = request.getSession(true);
			// Obtenemos la sesi�n y el beneficiario pasados a la p�gina JSP
			// desde la acci�n 'loginBeneficiario' de Struts 2; si alg�n valor
			// es null, es porque se ha accedido incorrectamente a esta p�gina
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
			// Comprobamos si el beneficiario tiene m�dico de cabecera o pediatra
			if(beneficiario.getEdad() > 14) {
				tipoMedico = "de cabecera";
			} else {
				tipoMedico = "pediatra";
			}
	%>
	
			<!-- Sesi�n v�lida -->
		    <div id="contenido">
				<div class="textoCuerpo">
					Bienvenido/a, <%= beneficiario.getNombre() %>.<br><br>
					Elija una opci�n: <br>
				</div>
				<div style="padding-left:250px;">
			<a href="citaCabecera.jsp" title="Pedir cita con su m�dico <%= tipoMedico %>">Pedir cita con su m&eacute;dico <%= tipoMedico %></a><br>
			<a href="citaEspecialista.jsp" title="Pedir cita con un m�dico especialista">Pedir cita con un m&eacute;dico especialista</a><br>
			<a href="anularCita.jsp" title="Anular una cita">Anular una cita</a>
				</div>
				<br>
				<div style="padding-left:250px;">
					<button onclick="setLocation('logout.jsp');">Cerrar sesi�n</button>
				</div>
			</div>
	
	<%
		} else {
	%>
	
			<!-- Sesi�n inv�lida -->
			<script type="text/javascript">
				setLocation('index.jsp');
			</script>
	
	<%
		} // Fin if(!sesionInvalida)
	%>
	
	<%@ include file="resources/templates/foot.htm" %>
</body>
</html>