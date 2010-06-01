<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="comunicaciones.ServidorFrontend" %>
<%@ page import="dominio.conocimiento.ISesion" %>
<%@ page import="dominio.conocimiento.Medico" %>
<%@ page import="dominio.conocimiento.Especialista" %>
<%@ page import="dominio.conocimiento.ICodigosMensajeAuxiliar" %>
<%@ page import="java.util.Vector" %>
<%@page import="java.rmi.RemoteException"%>
<%@page import="java.sql.SQLException"%>

<%
	ServidorFrontend p;
	// Tomamos el idSesion del HTTPSession
	ISesion s = (ISesion) session.getAttribute("SesionFrontend");
	p = ServidorFrontend.getServidor();
	String especialidad = request.getParameter("especialidad");
	try {
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
					<option value="-1">No existe ningun especialista de esa especialidad</option>
			<%
			   }
			%>
		</select>
<%
	}
	catch (RemoteException e) { %>
		Error: <%=e.getMessage()%>
<%
	} catch (SQLException e) {  %>
		Error: <%=e.getMessage()%>
<%
	} catch (Exception e) { %>
		Error: <%=e.getMessage()%>
<%		
	}
	
%>
