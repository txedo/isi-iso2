<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<%@ include file="header.jsp" %>
	<title>SSCAWeb - Login Médico</title>
</head>
<body>
	<%@ include file="top.jsp" %>

	<div id="contenido">
		<div class="textoCuerpo">
			<s:form action="loginMedico">
				<s:textfield name="username" label="Login" />
				<s:password name="pass" label="Contraseña" />
				<s:submit value="Aceptar" />
				<s:reset value="Restablecer" />
			</s:form>
		</div>
	</div>
	
	<%@ include file="foot.jsp" %>
</body>
</html>
