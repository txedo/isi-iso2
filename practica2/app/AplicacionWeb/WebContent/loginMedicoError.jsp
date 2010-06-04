<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<%@ include file="resources/templates/header.htm" %>
	<title>SSCAWeb</title>
	<sx:head />
</head>
<body>	
	<%@ include file="resources/templates/top.htm" %>
	
	<div class="menu">
		<span id="nav" class="navegacion">Inicio</span>
		<span id="auth" class="autenticacion">
			<%@ include file="sesion.jsp" %>
		</span>
	</div>
	
	<!-- El contenido es el men� y el texto de la p�gina -->
	<div id="contenido">
		<!-- Men� izquierdo -->
		<div id="izquierdo">
			<%@ include file="menuIzquierdo.jsp" %>
		</div>
		<!-- Texto de la p�gina -->
        <div id="textoCuerpo">         
			<span id="mod">
				<s:form action="loginMedico" method="post" namespace="/">
					<s:textfield name="username" label="Nombre de usuario" />
					<s:password name="pass" label="Contrase�a" />
					<s:submit value="Iniciar sesi�n" />
				</s:form>
			</span>
		</div>
    </div>
	    
	<%@ include file="resources/templates/foot.htm" %>
</body>
</html>
