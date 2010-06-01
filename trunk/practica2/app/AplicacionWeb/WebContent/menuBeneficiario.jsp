<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" %>
<%@ page import="dominio.conocimiento.Beneficiario" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<%@ include file="header.jsp" %>
	<title>SSCAWeb - Men� Beneficiario</title>
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
					<input type="radio" name="grupoRadio" value="citaCabecera" onclick="document.location='citaCabecera.jsp'">
					Cita con su m�dico <%= tipoMedico %><br>
					<input type="radio" name="grupoRadio" value="citaEspecialista" onclick="document.location='citaEspecialista.jsp'">
					Cita con un m�dico especialista<br>
					<input type="radio" name="grupoRadio" value="anularCita" onclick="document.location='anularCita.jsp'">
					Anular cita
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
	
	<%@ include file="foot.jsp" %>
</body>
</html>
