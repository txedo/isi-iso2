<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<s:form action="loginMedico">
	<s:textfield name="username" label="Login" />
	<s:password name="pass" label="Contraseña" />
	<s:submit value="Aceptar" />
	<s:reset value="Restablecer" />
</s:form>
