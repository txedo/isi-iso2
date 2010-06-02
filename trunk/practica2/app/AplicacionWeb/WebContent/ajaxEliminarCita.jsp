<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" %>
<%@ page import="comunicaciones.ServidorFrontend" %>
<%@ page import="dominio.conocimiento.ISesion" %>
<%@ page import="dominio.conocimiento.Cita" %>
<%@ page import="excepciones.CitaNoValidaException" %>
<%@ page import="java.rmi.RemoteException" %>
<%@ page import="java.sql.SQLException" %>

<%

	ServidorFrontend servidor;
	ISesion sesion;
	Cita cita;
	String mensaje;
	long idCita;
	
	// Tomamos la sesi�n del cliente de la sesi�n HTTP
	sesion = (ISesion)session.getAttribute("SesionFrontend");
	
	// Tomamos el id de la cita a anular de los par�metros pasados al JSP
	idCita = Long.parseLong(request.getParameter("idCita"));
	
	// Obtenemos los datos de la cita
	try {
		servidor = ServidorFrontend.getServidor();
		cita = null;
		cita = servidor.consultarCitaBeneficiario(sesion.getId(), idCita);
		servidor.anularCita(sesion.getId(), cita);
		mensaje = "La cita del d�a " + Cita.cadenaDiaCita(cita .getFechaYHora()) +
		          ", a las " + Cita.cadenaHoraCita(cita.getFechaYHora()) +
		          ", se ha anulado correctamente.";
	} catch(RemoteException e) {
		mensaje = "Error: " + e.getMessage();
	} catch(SQLException e) {
		mensaje = "Error: " + e.getMessage();
	} catch(CitaNoValidaException e) {
		mensaje = "Error: " + e.getMessage();
	} catch(Exception e) {
		mensaje = "Error: " + e.getMessage();
	}
	
	// Mostramos el resultado de la operaci�n o el mensaje de error
	if(!mensaje.equals("")) {
		%> <%= mensaje %> <%
	}
	
%>
