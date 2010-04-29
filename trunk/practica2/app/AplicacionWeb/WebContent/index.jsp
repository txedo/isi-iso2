<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="dominio.conocimiento.*"%>
<%@page import="comunicaciones.*"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<%
ProxyServidorFrontend s;
s = new ProxyServidorFrontend();
s.conectar("127.0.0.1", 2995);
ISesion ses = s.identificar("admin", "admin123");
//s.crear(ses.getId(), new Medico("88998899A", "medico", "medicoasdgha", "", "", "", "", "", new Cabecera()));
Medico m = s.getMedico(ses.getId(), "88998899A");
%> <%= m.getLogin() %>
</body>
</html>