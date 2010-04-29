<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="dominio.conocimiento.Beneficiario" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Cita con especialista</title>
</head>
<body>
<%= ((Beneficiario) request.getSession(false).getAttribute("Beneficiario")).getNombre() %>, escriba su número de volante: <br>
<s:form>
	<s:textfield name="nVolante" label="Volante"></s:textfield>
	<s:submit value="Aceptar"></s:submit>
</s:form>

<span id="resultadoEspecialista">
	Especialista
</span>
</body>
</html>