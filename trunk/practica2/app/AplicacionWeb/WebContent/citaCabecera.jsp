<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="dominio.conocimiento.Beneficiario" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Cita con su médico</title>
</head>
<body>
<%= ((Beneficiario) request.getSession(false).getAttribute("Beneficiario")).getNombre() %>, elija su día y hora: <br> 
<span id="nombreMedico">
	Le atenderá
</span>
</body>
</html>