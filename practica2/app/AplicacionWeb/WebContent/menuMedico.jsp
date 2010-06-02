<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" %>
<%@ page import="dominio.conocimiento.Medico" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<%@ include file="resources/templates/header.htm" %>
	<title>SSCAWeb - Menú Médico</title>
	<script type="text/javascript">
		function setLocation(url) {
			document.location = url;
		}
	</script>
</head>
<body>
	<%@ include file="resources/templates/top.htm" %>
	
	<%
		Medico medico;
		Object attr;
		boolean sesionInvalida;
		
		// Comprobamos si el cliente no tiene ninguna sesión activa
		sesionInvalida = false;
		if(request.getSession(false) == null || request.getSession(false).getAttribute("Medico") == null) {
			// Creamos una nueva sesión HTTP
			session = request.getSession(true);
			// Obtenemos la sesión y el médico pasados a la página JSP desde
			// la acción 'loginMedico' de Struts 2; si algún valor es null,
			// es porque se ha accedido incorrectamente a esta página
			attr = request.getAttribute("medico");
			if(attr == null) {
				sesionInvalida = true;
			} else {
				session.setAttribute("Medico", attr);
			}
			attr = request.getAttribute("sesion");
			if(attr == null) {
				sesionInvalida = true;
			} else {
				session.setAttribute("SesionFrontend", attr);
			}
		}
	
		if(!sesionInvalida) {
			
			// Obtenemos los datos del médico
			medico = (Medico)session.getAttribute("Medico");
	%>
	
			<!-- Sesión válida -->
		    <div id="contenido">
				<div class="textoCuerpo">
					Bienvenido/a, <%= medico.getNombre() %>.<br><br>
					Elija una opción: <br>
				</div>
				<div style="padding-left:250px;">
					<a href="darVolante">Emitir Volante</a>
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
	
	<%@ include file="resources/templates/foot.htm" %>
</body>
</html>
