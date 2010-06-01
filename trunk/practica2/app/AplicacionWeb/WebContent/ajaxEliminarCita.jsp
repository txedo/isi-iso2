<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" %>
<%@ page import="comunicaciones.ServidorFrontend" %>
<%@ page import="dominio.conocimiento.ISesion" %>
<%@ page import="dominio.conocimiento.Cita" %>
<%@ page import="excepciones.CitaNoValidaException" %>
<%@page import="java.rmi.RemoteException"%>
<%@page import="java.sql.SQLException"%>

<%

	ServidorFrontend servidor;
	ISesion sesion;
	Cita cita;
	long idCita;
	boolean noExiste;
	
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
		%>
			La cita del d�a <%= Cita.cadenaDiaCita(cita .getFechaYHora()) %>, a las <%= Cita.cadenaHoraCita(cita.getFechaYHora()) %>, se ha anulado correctamente.
		<%
	}
	catch (RemoteException e) { %>
		Error: <%=e.getMessage()%>
<%
	} catch (SQLException e) {  %>
		Error: <%=e.getMessage()%>
<%
	} catch (CitaNoValidaException e) { %>
		Error: <%=e.getMessage()%>
<%		
	} catch (Exception e) { %>
		Error: <%=e.getMessage()%>
<%		
	}
		
%>
