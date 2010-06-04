<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<script type="text/javascript" src="resources/scripts/funciones.js"></script>

<%
//Si se accede directamente en el navegador a esta página, se redirecciona al index.jsp
if (request.getHeader("referer")==null) { 
%>
	<script type="text/javascript" >setLocation("index.jsp");</script>
<%
}
%>

<s:form method="post" action="loginBeneficiario" namespace="/">
	<s:textfield name="nss" label="Escriba su número de S. Social" />
	<s:submit value="Iniciar sesión" />
</s:form>
