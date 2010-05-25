<%@page import="dominio.conocimiento.ICodigosMensajeAuxiliar"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="comunicaciones.ProxyServidorFrontend" %>
<%@ page import="dominio.conocimiento.ISesion" %>
<%@ page import="dominio.conocimiento.Cita" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%
	// Consultamos la cita, dado su id
	ProxyServidorFrontend p;
	//Tomamos el idSesion del HTTPSession
	ISesion s = (ISesion) session.getAttribute("SesionFrontend");
	long idCita = Long.parseLong(request.getParameter("idCita"));
	p = ProxyServidorFrontend.getProxy();
	Cita c = (Cita) p.mensajeAuxiliar(s.getId(), ICodigosMensajeAuxiliar.CONSULTAR_CITA_BENEFICIARIO, idCita);
	p.anularCita(s.getId(), c);	
%>

La cita del día <%=c.cadenaDiaCita(c.getFechaYHora())%>, a las <%=c.cadenaHoraCita(c.getFechaYHora())%>, se ha anulado correctamente.