<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" %>
<%@ page import="comunicaciones.ServidorFrontend" %>
<%@ page import="dominio.conocimiento.ISesion" %>
<%@ page import="dominio.conocimiento.Volante" %>
<%@ page import="dominio.conocimiento.Beneficiario" %>
<%@ page import="dominio.conocimiento.IConstantes" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.Calendar" %>

<%

	ServidorFrontend servidor;
	Beneficiario beneficiario;
	ISesion sesion;
	Volante volante;
	SimpleDateFormat formatoDia, formatoHora;
	Calendar cal1, cal2;
	Date fechaCita, horaCita;
	String dia, hora;

	// Tomamos la sesi�n del cliente de la sesi�n HTTP
	sesion = (ISesion)session.getAttribute("SesionFrontend");
	// Tomamos el beneficiario que est� usando el sistema de la sesi�n HTTP
	beneficiario = (Beneficiario)session.getAttribute("Beneficiario");
	// Tomamos el volante validado de la sesi�n HTTP
	volante = (Volante)session.getAttribute("volante");
	
	// Tomamos el d�a y la hora de la cita de los par�metros pasados al JSP
	dia = request.getParameter("dia");
	hora = request.getParameter("hora");
	
	// Establecemos el formato para los d�as y las horas
	formatoDia = new SimpleDateFormat("dd/MM/yyyy");
	formatoHora = new SimpleDateFormat("HH:mm");
	
	// Generamos la fecha de la cita a partir del d�a y la hora
	fechaCita = formatoDia.parse(dia);
	horaCita = formatoHora.parse(hora);
	cal1 = Calendar.getInstance();
	cal1.setTime(fechaCita);
	cal2 = Calendar.getInstance();
	cal2.setTime(horaCita);
	cal1.set(Calendar.HOUR_OF_DAY, cal2.get(Calendar.HOUR_OF_DAY));
	cal1.set(Calendar.MINUTE, cal2.get(Calendar.MINUTE));
	
	// Emitimos la cita para el volante dado
	servidor = ServidorFrontend.getServidor();	
	servidor.pedirCita(sesion.getId(), beneficiario, volante.getId(), fechaCita, IConstantes.DURACION_CITA);
	
%>
