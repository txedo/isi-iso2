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

<s:form action="loginMedico" method="post" namespace="/">
	<s:textfield name="username" label="Nombre de usuario" />
	<s:password name="pass" label="Contrase�a" />
	<s:submit value="Iniciar sesi�n" />
</s:form>

