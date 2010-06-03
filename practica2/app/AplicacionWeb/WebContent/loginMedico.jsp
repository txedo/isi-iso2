<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<s:form action="javascript:cargarModulo('loginMedico')" validate="true" method="post">
	<s:textfield name="username" label="Nombre de usuario" />
	<s:password name="pass" label="Contraseña" />
	<s:submit value="Iniciar sesión" />
</s:form>