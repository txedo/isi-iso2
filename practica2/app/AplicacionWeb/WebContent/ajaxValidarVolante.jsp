<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" %>
<%@ page import="comunicaciones.ServidorFrontend" %>
<%@ page import="dominio.conocimiento.Volante" %>
<%@ page import="dominio.conocimiento.ISesion" %>
<%@ page import="excepciones.VolanteNoValidoException" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.rmi.RemoteException" %>

<%

	ServidorFrontend servidor;
	ISesion sesion;
	Volante volante;
	String mensaje;
	long nVolante;
	
	// Tomamos la sesión del cliente de la sesión HTTP
	sesion = (ISesion)session.getAttribute("SesionFrontend");
	
	// Tomamos el id del volante de los parámetros pasados al JSP
	nVolante = Long.parseLong(request.getParameter("nVolante"));
	
	try {
		// Consultamos los datos del volante indicado
		servidor = ServidorFrontend.getServidor();
		volante = servidor.consultarVolante(sesion.getId(), nVolante);
		// Ponemos el volante en la sesión HTTP del beneficiario para
		// no tener que volver a consultar el especialista cuando se
		// quieran mostrar las horas laborales en cada dia seleccionado.
		// Asi, se ahorran consultas, ya que si no, cada vez que se
		// cambie el dia del calendario y se tengan que refrescar las
		// horas, se tendría que consultar el volante y su receptor
		session.setAttribute("volante", volante);
		mensaje = "Le atenderá el Dr. " + volante.getReceptor().getApellidos() +
		          ", " + volante.getReceptor().getNombre() + ".";
	} catch(RemoteException e) {
		mensaje = "Error: " + e.getMessage();
	} catch(SQLException e) {
		mensaje = "Error: " + e.getMessage();
	} catch(VolanteNoValidoException e) {
		mensaje = "Error: " + e.getMessage();
	} catch(Exception e) {
		mensaje = "Error: " + e.getMessage();
	}

	// Mostramos el resultado de la operación o el mensaje de error
	if(!mensaje.equals("")) {
		%> <%= mensaje %> <%
	}

%>
