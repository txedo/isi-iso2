<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<script type="text/javascript" src="resources/scripts/funciones.js"></script>

<s:form action="javascript:cargarModulo('loginMedico')" validate="true" method="post">
	<s:textfield name="username" label="Nombre de usuario" />
	<s:password name="pass" label="Contrase�a" />
	<s:submit value="Iniciar sesi�n" />
</s:form>

<%
	// Si se ejecuta el validador, el login o la contrase�a son incorrectos
	if (Boolean.parseBoolean(request.getParameter("invalido"))) {
%>
		<script type="text/javascript">setLocation('index.jsp?invalidoMedico=true');</script>
<%  }
%>