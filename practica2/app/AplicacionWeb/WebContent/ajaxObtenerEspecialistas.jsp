<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" %>
<%@ page import="comunicaciones.ServidorFrontend" %>
<%@ page import="dominio.conocimiento.ISesion" %>
<%@ page import="dominio.conocimiento.Medico" %>
<%@ page import="dominio.conocimiento.Especialista" %>
<%@ page import="java.util.Vector" %>
<%@ page import="java.rmi.RemoteException" %>
<%@ page import="java.sql.SQLException" %>

<%

	ServidorFrontend servidor;
	Vector<Medico> especialistas;
	Vector<String> options = null;
	ISesion sesion;
	String especialidad, mensaje;
	boolean selec;
	
	// Tomamos la sesión del cliente de la sesión HTTP
	sesion = (ISesion)session.getAttribute("SesionFrontend");

	// Tomamos la especialidad de los parámetros pasados al JSP
	especialidad = request.getParameter("especialidad");

	try {
		// Consultamos los médicos de la especialidad indicada
		servidor = ServidorFrontend.getServidor();
		especialistas = servidor.obtenerMedicosTipo(sesion.getId(), new Especialista(especialidad));
		// Generamos la lista HTML con los nombres de los especialistas
		options = new Vector<String>();
		if(especialistas.size() == 0) {
			options.add("<option style=\"color:#AAAAAA;\" value=\"-1\">No existe ningun especialista de esa especialidad</option>");
		} else {
			selec = false;
			for(int i = 0; i < especialistas.size(); i++) {
				if(selec) {
					options.add("<option value=\"" + especialistas.get(i).getNif() + "\">" + especialistas.get(i).getApellidos() + ", " + especialistas.get(i).getNombre() + "</option>");
				} else {
					options.add("<option selected value=\"" + especialistas.get(i).getNif() + "\">" + especialistas.get(i).getApellidos() + ", " + especialistas.get(i).getNombre() + "</option>");
					selec = true;
				}
			}
		}
		mensaje = "";
	} catch(RemoteException e) {
		mensaje = "Error: " + e.getMessage();
	} catch(SQLException e) {
		mensaje = "Error: " + e.getMessage();
	} catch(Exception e) {
		mensaje = "Error: " + e.getMessage();
	}

	// Mostramos la lista de especialistas o el mensaje de error
	if(mensaje.equals("")) { %>
		<select id="especialista" name="especialistas">
			<% for(int i = 0; i < options.size(); i++) { %>
				<%= options.get(i) %>
			<% } %>
		</select> <%
	} else {
		%> <%= mensaje %> <%
	}
	
%>
