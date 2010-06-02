<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" %>
<%@ page import="dominio.conocimiento.Medico" %>
<%@ page import="dominio.conocimiento.ISesion" %>
<%@ page import="comunicaciones.ServidorFrontend" %>
<%@ page import="dominio.conocimiento.Cita" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.rmi.RemoteException" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.text.ParseException" %>

<%

	ServidorFrontend servidor;
	ISesion sesion;
	Medico medico, medicoReal;
	SimpleDateFormat formatoFecha;
	Calendar cal1, cal2;
	Date diaSeleccionado, horaCita;
	String dia, hora, mensaje;
	
	// Tomamos la sesión del cliente de la sesión HTTP
	sesion = (ISesion)session.getAttribute("SesionFrontend");
	// Tomamos el médico que daría la cita de la sesión HTTP
	medico = (Medico)session.getAttribute("medicoCitas");

	// Tomamos el día para el que se pretende dar la cita de los parámetros JSP
	dia = request.getParameter("dia");
	// Tomamos la hora para la que se pretende dar la cita de los parámetros JSP
	hora = request.getParameter("hora");
	
	try {
		// Convertimos las cadena con el día y hora en instancias de Date
		formatoFecha = new SimpleDateFormat("dd/M/yyyy");
		diaSeleccionado = formatoFecha.parse(dia);
		horaCita = Cita.horaCadenaCita(hora);
		
		// Unimos el día y la hora en una única fecha
		cal1 = Calendar.getInstance();
		cal2 = Calendar.getInstance();
		cal1.setTime(diaSeleccionado);
		cal2.setTime(horaCita);
		cal1.set(Calendar.HOUR_OF_DAY, cal2.get(Calendar.HOUR_OF_DAY));
		cal1.set(Calendar.MINUTE, cal2.get(Calendar.MINUTE));

		// Consultamos el médico que daría realmente la cita
		servidor = ServidorFrontend.getServidor();
		medicoReal = servidor.consultarMedicoCita(sesion.getId(), medico.getNif(), cal1.getTime());
		// Comprobamos si el médico obtenido es diferente del teórico
		if(!medico.getNif().equals(medicoReal.getNif())) {
			mensaje = "<br>Le atenderá el Dr./Dra. " + medicoReal.getApellidos() +
			          " en sustitución del Dr./Dra. " + medico.getApellidos() + ".";
		} else {
			mensaje = "";
		}
	} catch(ParseException e) {
		// La hora de la cita no es válida
		mensaje = "";
	} catch(RemoteException e) {
		mensaje = "Error: " + e.getMessage();
	} catch(SQLException e) {
		mensaje = "Error: " + e.getMessage();
	} catch(Exception e) {
		mensaje = "Error: " + e.getMessage();
	}

	// Mostramos el resultado de la operación o el mensaje de error
	if(!mensaje.equals("")) {
		%> <%= mensaje %> <%
	}
	
%>
