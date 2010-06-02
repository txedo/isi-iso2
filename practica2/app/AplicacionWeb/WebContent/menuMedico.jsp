<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" %>
<%@ page import="dominio.conocimiento.Medico" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<%@ include file="resources/templates/header.htm" %>
	<title>SSCAWeb - Men� M�dico</title>
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
		
		// Comprobamos si el cliente no tiene ninguna sesi�n activa
		sesionInvalida = false;
		if(request.getSession(false) == null || request.getSession(false).getAttribute("Medico") == null) {
			// Creamos una nueva sesi�n HTTP
			session = request.getSession(true);
			// Obtenemos la sesi�n y el m�dico pasados a la p�gina JSP desde
			// la acci�n 'loginMedico' de Struts 2; si alg�n valor es null,
			// es porque se ha accedido incorrectamente a esta p�gina
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
			
			// Obtenemos los datos del m�dico
			medico = (Medico)session.getAttribute("Medico");
	%>
	
			<!-- Sesi�n v�lida -->
		    <div id="contenido">
				<div class="textoCuerpo">
					Bienvenido/a, <%= medico.getNombre() %>.<br><br>
					Elija una opci�n: <br>
				</div>
				<div style="padding-left:250px;">
					<a href="darVolante">Emitir Volante</a>
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
