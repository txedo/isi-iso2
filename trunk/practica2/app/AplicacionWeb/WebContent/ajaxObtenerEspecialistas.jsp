<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="comunicaciones.ServidorFrontend" %>
<%@ page import="dominio.conocimiento.ISesion" %>
<%@ page import="dominio.conocimiento.Medico" %>
<%@ page import="dominio.conocimiento.Especialista" %>
<%@ page import="dominio.conocimiento.ICodigosMensajeAuxiliar" %>
<%@ page import="java.util.Vector" %>
<%@ taglib prefix="s" uri="/struts-tags" %> 

<%
	ServidorFrontend p;
	// Tomamos el idSesion del HTTPSession
	ISesion s = (ISesion) session.getAttribute("SesionFrontend");
	p = ServidorFrontend.getServidor();
	String especialidad = request.getParameter("especialidad");
	Vector<Medico> especialistas = p.obtenerMedicosTipo(s.getId(), new Especialista(especialidad));
	int i=0;
%>

<br>Dr./Dra.: <select id="especialista" name="especialistas">
	<% for(i=0; i<especialistas.size(); i++) { 
		if (i==0 && especialistas.size()>0) {
	%>
			<option selected value="<%=especialistas.get(i).getNif() %>"><%= especialistas.get(i).getApellidos() +", "+ especialistas.get(i).getNombre()%></option>
	<%
		} else if (especialistas.size()>0) {
	%>
			<option value="<%= especialistas.get(i).getNif() %>"><%= especialistas.get(i).getApellidos() +", "+ especialistas.get(i).getNombre()%></option>
	<%  }
	   } // Fin for
	   if (especialistas.size()==0) {
	%>
			<option value="-1">No existe ningun especialista</option>
	<%
	   }
	%>
</select>
