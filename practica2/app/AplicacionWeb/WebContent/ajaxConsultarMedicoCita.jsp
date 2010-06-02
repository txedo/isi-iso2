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
	
	// Tomamos la sesi�n del cliente de la sesi�n HTTP
	sesion = (ISesion)session.getAttribute("SesionFrontend");
	// Tomamos el m�dico que dar�a la cita de la sesi�n HTTP
	medico = (Medico)session.getAttribute("medicoCitas");

	// Tomamos el d�a para el que se pretende dar la cita de los par�metros JSP
	dia = request.getParameter("dia");
	// Tomamos la hora para la que se pretende dar la cita de los par�metros JSP
	hora = request.getParameter("hora");
	
	try {
		// Convertimos las cadena con el d�a y hora en instancias de Date
		formatoFecha = new SimpleDateFormat("dd/M/yyyy");
		diaSeleccionado = formatoFecha.parse(dia);
		horaCita = Cita.horaCadenaCita(hora);
		
		// Unimos el d�a y la hora en una �nica fecha
		cal1 = Calendar.getInstance();
		cal2 = Calendar.getInstance();
		cal1.setTime(diaSeleccionado);
		cal2.setTime(horaCita);
		cal1.set(Calendar.HOUR_OF_DAY, cal2.get(Calendar.HOUR_OF_DAY));
		cal1.set(Calendar.MINUTE, cal2.get(Calendar.MINUTE));

		// Consultamos el m�dico que dar�a realmente la cita
		servidor = ServidorFrontend.getServidor();
		medicoReal = servidor.consultarMedicoCita(sesion.getId(), medico.getNif(), cal1.getTime());
		// Comprobamos si el m�dico obtenido es diferente del te�rico
		if(!medico.getNif().equals(medicoReal.getNif())) {
			mensaje = "<br>Le atender� el Dr./Dra. " + medicoReal.getApellidos() +
			          " en sustituci�n del Dr./Dra. " + medico.getApellidos() + ".";
		} else {
			mensaje = "";
		}
	} catch(ParseException e) {
		// La hora de la cita no es v�lida
		mensaje = "";
	} catch(RemoteException e) {
		mensaje = "Error: " + e.getMessage();
	} catch(SQLException e) {
		mensaje = "Error: " + e.getMessage();
	} catch(Exception e) {
		mensaje = "Error: " + e.getMessage();
	}

	// Mostramos el resultado de la operaci�n o el mensaje de error
	if(!mensaje.equals("")) {
		%> <%= mensaje %> <%
	}
	
%>
