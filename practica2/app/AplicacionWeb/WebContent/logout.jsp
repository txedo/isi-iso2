<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%
HttpSession sesionHTTP;

// Obtenemos la sesión HTTP y borramos todos sus objetos
sesionHTTP = request.getSession(true);
sesionHTTP.invalidate();
%>

Sesión cerrada correctamente.
<br /><br />
En unos segundos será redirigido a la página principal.
