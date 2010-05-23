<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="resources/styles/style.css" type="text/css" />
<title>Login beneficiario</title>
</head>
<body>
	<div id="header">
		<div class="textoCabecera">Título de la cabecera</div>
	</div>

	<div id="contenido">
		<div class="textoCuerpo">
			<s:form action="loginBeneficiario">
				<s:textfield name="Nss" label="Escriba su número de S. Social"></s:textfield>
				<s:submit value="Aceptar"></s:submit>
			</s:form>
		</div>
	</div>
	<div id="pie">
        Texto del pie
    </div>
</body>
</html>