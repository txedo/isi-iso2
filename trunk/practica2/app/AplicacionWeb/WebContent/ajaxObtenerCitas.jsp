<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" %>
<%@ page import="comunicaciones.ServidorFrontend" %>
<%@ page import="dominio.conocimiento.ISesion" %>
<%@ page import="dominio.conocimiento.Cita" %>
<%@ page import="excepciones.BeneficiarioInexistenteException" %>
<%@ page import="java.util.Vector" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.rmi.RemoteException" %>
<%@ page import="java.sql.SQLException" %>

<script type="text/javascript" src="resources/scripts/funciones.js"></script>

<%

	//Si se accede directamente en el navegador a esta página, se redirecciona al index.jsp
	if (request.getHeader("referer")==null) { 
	%>
		<script type="text/javascript" >setLocation("index.jsp");</script>
	<%
	}
	ServidorFrontend servidor;
	Vector<Cita> citas;
	Vector<String> options = null;
	ISesion sesion;
	String nifBeneficiario, mensaje;
	boolean selec;
	
	// Tomamos la sesión del cliente de la sesión HTTP
	sesion = (ISesion)session.getAttribute("SesionFrontend");
	
	// Tomamos el NIF del beneficiario para el que se quieren obtener
	// las citas
	nifBeneficiario = ((Beneficiario)session.getAttribute("Beneficiario")).getNif();
	
	try {
		// Consultamos las citas del beneficiario
		servidor = ServidorFrontend.getServidor();
		citas = servidor.consultarHistoricoCitas(sesion.getId(), nifBeneficiario);
		// Generamos la lista HTML con los datos de las citas (se marcan en
		// gris las citas pasadas y se selecciona la primera cita no pasada)
		options = new Vector<String>();
		if(citas.size() == 0) {
			options.add("<option style=\"color:#AAAAAA;\" value=\"-2\">No tiene ninguna cita asignada.</option>");
		} else {
			selec = false;
			for(int i = 0; i < citas.size(); i++) {
				if(citas.get(i).getFechaYHora().before(new Date())) {
					options.add("<option style=\"color:#AAAAAA;\" value=\"-1\">" + citas.get(i).toString() + "</option>");
				} else {
					if(selec) {
						options.add("<option value=\"" + citas.get(i).getId() + "\">" + citas.get(i).toString() + "</option>");
					} else {
						options.add("<option selected value=\"" + citas.get(i).getId() + "\">" + citas.get(i).toString() + "</option>");
						selec = true;
					}
				}
			}
		}
		mensaje = "";
	} catch(RemoteException e) {
		mensaje = "Error: " + e.getMessage();
	} catch(SQLException e) {
		mensaje = "Error: " + e.getMessage();
	} catch(BeneficiarioInexistenteException e) {
		mensaje = "Error: " + e.getMessage();
	} catch(Exception e) {
		mensaje = "Error: " + e.getMessage();
	}

	// Mostramos la lista de citas o el mensaje de error
	if(mensaje.equals("")) { %>
		<%@page import="dominio.conocimiento.Beneficiario"%>
<select id="citas" name="citas" size="4" style="width:250px">
			<% for(int i = 0; i < options.size(); i++) { %>
				<%= options.get(i) %>
			<% } %>
		</select>
		<br><br>
		<input type="submit" value="Anular cita" onclick="anularCita('ajaxEliminarCita.jsp')" />
		<br><br> <%
	} else {
		%> <%= mensaje %> <%
	}
	
%>
