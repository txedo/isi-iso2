<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="comunicaciones.ProxyServidorFrontend" %>
<%@ page import="dominio.conocimiento.ISesion" %>
<%@ page import="dominio.conocimiento.Cita" %>
<%@page import="java.util.Vector"%>
<%@page import="java.util.Date"%>

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
			// Si la cita es anterior a la fecha actual, se pone como id -1, para luego validarlo
			if (citas.get(i).getFechaYHora().before(new Date())) {
	%>
				<option selected value="-1"><%= citas.get(i).toString()%></option>
	<%
			} else {
	%>
				<option selected value="<%=citas.get(i).getId() %>"><%= citas.get(i).toString()%></option>
	<%
			}
		} else if (citas.size()>0) {
			if (citas.get(i).getFechaYHora().before(new Date())) {
	%>
				<option value="-1"><%= citas.get(i).toString()%></option>
	<%
			} else {
	%>
				<option value="<%=citas.get(i).getId() %>"><%= citas.get(i).toString()%></option>
	<%
			}
	%>
	<%  }
	   } // Fin for
	   if (citas.size()==0) {
	%>
			<option value="-1">No tiene ninguna cita asignada</option>
	<%
	   }
	%>
</select>