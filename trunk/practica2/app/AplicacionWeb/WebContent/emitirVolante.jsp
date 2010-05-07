<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="comunicaciones.ProxyServidorFrontend" %>
<%@ page import="dominio.conocimiento.ISesion" %>
<%@ page import="dominio.conocimiento.Medico" %>
<%@ page import="dominio.conocimiento.Beneficiario" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%
	ProxyServidorFrontend p;
	// Tomamos el idSesion del HTTPSession
	ISesion s = (ISesion) session.getAttribute("SesionFrontend");
	p = ProxyServidorFrontend.getProxy();
	Beneficiario bene = p.getBeneficiario(s.getId(), request.getParameter("nifBeneficiario"));
	Medico emisor = (Medico) session.getAttribute("Medico");
	Medico receptor = p.getMedico(s.getId(), request.getParameter("nifReceptor"));
	long idVolante = p.emitirVolante(s.getId(), bene, emisor, receptor);
%>

<br><br><br>El numero de volante emitido es el: <%= idVolante %> 