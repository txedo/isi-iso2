<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<script type="text/javascript" src="resources/scripts/funciones.js"></script>

<%
//Si se accede directamente en el navegador a esta p�gina, se redirecciona al index.jsp
if (request.getHeader("referer")==null) { 
%>
	<script type="text/javascript" >setLocation("index.jsp");</script>
<%
}
%>

<s:form method="post" action="loginBeneficiario" namespace="/">
	<s:textfield name="nss" label="Escriba su n�mero de S. Social" />
	<s:submit value="Iniciar sesi�n" />
</s:form>
