<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="comunicaciones.ProxyServidorFrontend" %>
<%@ page import="dominio.conocimiento.ISesion" %>
<%@ page import="dominio.conocimiento.Cita" %>
<%@page import="java.util.Vector;"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%
	ProxyServidorFrontend p;
	//Tomamos el idSesion del HTTPSession
	ISesion s = (ISesion) session.getAttribute("SesionFrontend");
	String nifBeneficiario = request.getParameter("nif");
	p = ProxyServidorFrontend.getProxy();
	Vector<Cita> citas = p.getCitas(s.getId(), nifBeneficiario);	
%>

<select id="citas" name="citas">
	<% for(int i=0; i<citas.size(); i++) { 
		if (i==0 && citas.size()>0) {
	%>
			<option selected value="<%=citas.get(i).getId() %>"><%= citas.get(i).toString()%></option>
	<%
		} else if (citas.size()>0) {
	%>
			<option value="<%=citas.get(i).getId() %>"><%= citas.get(i).toString()%></option>
	<%  }
	   } // Fin for
	   if (citas.size()==0) {
	%>
			<option value="-1">No tiene ninguna cita asignada</option>
	<%
	   }
	%>
<select>