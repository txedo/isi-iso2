<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<script type="text/javascript" src="resources/scripts/funciones.js"></script>

<s:form action="loginBeneficiario" validate="true" method="post">
	<s:textfield name="nss" label="Escriba su número de S. Social" />
	<s:submit value="Iniciar sesión" />
</s:form>

<%
	// Si se ejecuta el validador, el nss introducido es incorrecto
	if (Boolean.parseBoolean(request.getParameter("invalido"))) {
%>
		<script type="text/javascript">setLocation('index.jsp?invalidoBeneficiario=true');</script>
<%  }
%>