<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="comunicaciones.ServidorFrontend"%>
<%@page import="dominio.conocimiento.ISesion"%>
<%@page import="dominio.conocimiento.Cita"%>
<%@page import="java.util.Vector"%>
<%@page import="java.util.Date"%>

<%

	ServidorFrontend servidor;
	Vector<Cita> citas;
	Vector<String> options;
	ISesion sesion;
	String nifBeneficiario;
	boolean selec;
	
	// Tomamos la sesión del cliente de la sesión HTTP
	sesion = (ISesion)session.getAttribute("SesionFrontend");
	// Tomamos el NIF del beneficiario para el que se quieren obtener
	// las citas de los parámetros pasados al JSP
	nifBeneficiario = request.getParameter("nif");
	
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
	
%>

<select id="citas" name="citas" size="4" style="width:250px">
	<% for(int i = 0; i < options.size(); i++) { %>
		<%= options.get(i) %>
	<% } %>
</select>
