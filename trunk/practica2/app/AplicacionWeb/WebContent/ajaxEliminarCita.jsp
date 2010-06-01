<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" %>
<%@ page import="comunicaciones.ServidorFrontend" %>
<%@ page import="dominio.conocimiento.ISesion" %>
<%@ page import="dominio.conocimiento.Cita" %>
<%@ page import="excepciones.CitaNoValidaException" %>

<%

	ServidorFrontend servidor;
	ISesion sesion;
	Cita cita;
	long idCita;
	boolean noExiste;
	
	// Tomamos la sesión del cliente de la sesión HTTP
	sesion = (ISesion)session.getAttribute("SesionFrontend");
	
	// Tomamos el id de la cita a anular de los parámetros pasados al JSP
	idCita = Long.parseLong(request.getParameter("idCita"));
	
	// Obtenemos los datos de la cita
	servidor = ServidorFrontend.getServidor();
	cita = null;
	try {
		cita = servidor.consultarCitaBeneficiario(sesion.getId(), idCita);
		noExiste = false;
	} catch(CitaNoValidaException ex) {
		noExiste = true;
	}
	
	// Anulamos la cita
	if(!noExiste) {
		try {
			servidor.anularCita(sesion.getId(), cita);
			noExiste = false;
		} catch(CitaNoValidaException ex) {
			noExiste = true;
		}
	}
	
	// Mostramos los resultados de la anulación
	if(noExiste) {
		%>
			La cita seleccionada no existe o no se puede anular.
		<%
	} else {
		%>
			La cita del día <%= Cita.cadenaDiaCita(cita .getFechaYHora()) %>, a las <%= Cita.cadenaHoraCita(cita.getFechaYHora()) %>, se ha anulado correctamente.
		<%
	}
	
%>
