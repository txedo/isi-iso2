<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<s:form action="loginBeneficiario" validate="true" method="post">
	<s:textfield name="nss" label="Escriba su n�mero de S. Social" />
	<s:submit value="Iniciar sesi�n" />
</s:form>