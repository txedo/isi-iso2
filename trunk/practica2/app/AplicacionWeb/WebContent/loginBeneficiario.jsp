<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<%@ include file="header.jsp" %>
	<title>SSCAWeb - Login beneficiario</title>
</head>
<body>
	<%@ include file="top.jsp" %>

	<div id="contenido">
		<div class="textoCuerpo">
			<s:form action="loginBeneficiario">
				<s:textfield name="nss" label="Escriba su número de S. Social" />
				<s:submit value="Aceptar" />
			</s:form>
		</div>
	</div>
	
	<%@ include file="foot.jsp" %>
	
</body>
</html>
