<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Login Medico</title>
</head>
<body>
	<s:form action="loginMedico">
		<s:textfield name="username" label="Login"></s:textfield>
		<s:password name="pass" label="Contrase�a"></s:password>
		<s:submit value="Aceptar"></s:submit>
	</s:form>
</body>
</html>