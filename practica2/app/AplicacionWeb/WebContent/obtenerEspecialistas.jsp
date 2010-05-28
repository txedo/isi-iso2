<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="comunicaciones.ProxyServidorFrontend" %>
<%@ page import="dominio.conocimiento.ISesion" %>
<%@ page import="dominio.conocimiento.Medico" %>
<%@ page import="dominio.conocimiento.Especialista" %>
<%@ page import="dominio.conocimiento.ICodigosMensajeAuxiliar" %>
<%@ page import="java.util.Vector" %>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%
	ProxyServidorFrontend p;
	// Tomamos el idSesion del HTTPSession
	ISesion s = (ISesion) session.getAttribute("SesionFrontend");
	p = ProxyServidorFrontend.getProxy();
	String especialidad = request.getParameter("especialidad");
	Vector<Medico> especialistas = (Vector<Medico>) p.mensajeAuxiliar(s.getId(), ICodigosMensajeAuxiliar.OBTENER_MEDICOS_TIPO, new Especialista(especialidad));
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
