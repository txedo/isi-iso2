<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" %>
<%@ page import="comunicaciones.ServidorFrontend" %>
<%@ page import="dominio.conocimiento.ISesion" %>
<%@ page import="dominio.conocimiento.Beneficiario" %>
<%@ page import="dominio.conocimiento.IConstantes" %>
<%@ page import="excepciones.FechaNoValidaException" %>
<%@ page import="excepciones.MedicoInexistenteException" %>
<%@ page import="excepciones.BeneficiarioInexistenteException" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.rmi.RemoteException" %>
<%@ page import="java.sql.SQLException" %>

<script type="text/javascript" src="resources/scripts/funciones.js"></script>

<%
	//Si se accede directamente en el navegador a esta p�gina, se redirecciona al index.jsp
	if (request.getHeader("referer")==null) { 
	%>
		<script type="text/javascript" >setLocation("index.jsp");</script>
	<%
	}

	ServidorFrontend servidor;
	Beneficiario beneficiario;
	ISesion sesion;
	SimpleDateFormat formatoDia, formatoHora;
	Calendar cal1, cal2;
	Date diaCita, horaCita, fechaCita;
	String dia, hora, mensaje;

	// Tomamos la sesi�n del cliente de la sesi�n HTTP
	sesion = (ISesion)session.getAttribute("SesionFrontend");
	// Tomamos el beneficiario que est� usando el sistema de la sesi�n HTTP
	beneficiario = (Beneficiario)session.getAttribute("Beneficiario");
	
	// Tomamos el d�a y la hora de la cita de los par�metros pasados al JSP
	dia = request.getParameter("dia");
	hora = request.getParameter("hora");
	
	// Establecemos el formato para los d�as y las horas
	formatoDia = new SimpleDateFormat("dd/MM/yyyy");
	formatoHora = new SimpleDateFormat("HH:mm");
	
	// Generamos la fecha de la cita a partir del d�a y la hora
	diaCita = formatoDia.parse(dia);
	horaCita = formatoHora.parse(hora);
	cal1 = Calendar.getInstance();
	cal1.setTime(diaCita);
	cal2 = Calendar.getInstance();
	cal2.setTime(horaCita);
	cal1.set(Calendar.HOUR_OF_DAY, cal2.get(Calendar.HOUR_OF_DAY));
	cal1.set(Calendar.MINUTE, cal2.get(Calendar.MINUTE));
	cal1.set(Calendar.SECOND, 0);
	fechaCita = cal1.getTime();
	
	try {
		// Emitimos la cita para el m�dico de cabecera
		servidor = ServidorFrontend.getServidor();
		servidor.pedirCita(sesion.getId(), beneficiario, beneficiario.getMedicoAsignado().getNif(), fechaCita, IConstantes.DURACION_CITA);
		mensaje = "Se ha tramitado correctamente la cita para el d�a " + formatoDia.format(fechaCita) + ", a las " + formatoHora.format(fechaCita) + ".";
	} catch(RemoteException e) {
		mensaje = "Error: " + e.getMessage();
	} catch(SQLException e) {
		mensaje = "Error: " + e.getMessage();
	} catch(BeneficiarioInexistenteException e) {
		mensaje = "Error: " + e.getMessage();
	} catch(MedicoInexistenteException e) {
		mensaje = "Error: " + e.getMessage();
	} catch(FechaNoValidaException e) {
		mensaje = "Error: " + e.getMessage();
	} catch(Exception e) {
		mensaje = "Error: " + e.getMessage();
	}
	
	// Mostramos el resultado de la operaci�n o el mensaje de error
	if(!mensaje.equals("")) {
		%> <%= mensaje %> <%
	}
	
%>
