<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="dominio.conocimiento.ISesion" %>
<%@ page import="comunicaciones.ProxyServidorFrontend" %>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="dominio.conocimiento.Volante"%>
<%@page import="dominio.conocimiento.Beneficiario"%>
<%@page import="dominio.conocimiento.IConstantes"%>
<%@page import="dominio.conocimiento.Cita"%>
<%@page import="java.util.Date"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%
	ProxyServidorFrontend p;
	// Tomamos el idSesion del HTTPSession
	ISesion s = (ISesion) session.getAttribute("SesionFrontend");
	// Tomamos el dia y la hora de la cita
	String dia = request.getParameter("dia");
	String hora = request.getParameter("hora");
	// Tomamos el volante validado
	Volante v = (Volante) session.getAttribute("volante");
	// Tomamos el beneficiario
	Beneficiario b = (Beneficiario) session.getAttribute("Beneficiario");
	// Establecemos el formato para los días y las horas
	SimpleDateFormat formatoDia = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm");
	// Tomamos la fecha de la cita como un objeto Date
	Date diaElegido = formatoDia.parse(dia);
	Date horaElegida = formatoHora.parse(hora);
	Date fechaCita = new Date(diaElegido.getYear(), diaElegido.getMonth(), diaElegido.getDate(), horaElegida.getHours(), horaElegida.getMinutes());
	// Emitimos la cita para el volante dado
	p = ProxyServidorFrontend.getProxy();	
	p.pedirCita(s.getId(), b, v.getId(), fechaCita, IConstantes.DURACION_CITA);
%>

