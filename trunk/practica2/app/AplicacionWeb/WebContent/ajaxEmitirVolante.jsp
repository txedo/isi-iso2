<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" %>
<%@ page import="comunicaciones.ServidorFrontend" %>
<%@ page import="dominio.conocimiento.ISesion" %>
<%@ page import="dominio.conocimiento.Medico" %>
<%@ page import="dominio.conocimiento.Beneficiario" %>
<%@ page import="dominio.conocimiento.Volante" %>
<%@ page import="excepciones.BeneficiarioInexistenteException" %>
<%@ page import="excepciones.MedicoInexistenteException" %>
<%@ page import="java.rmi.RemoteException" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.text.SimpleDateFormat" %>

<script type="text/javascript" src="resources/scripts/funciones.js"></script>

<%

	//Si se accede directamente en el navegador a esta página, se redirecciona al index.jsp
	if (request.getHeader("referer")==null) { 
	%>
		<script type="text/javascript" >setLocation("index.jsp");</script>
	<%
	}
	ServidorFrontend servidor;
	Medico emisor, receptor;
	Beneficiario beneficiario;
	Volante volante;
	ISesion sesion;
	SimpleDateFormat formatoFecha;
	String nifBeneficiario, nifReceptor, mensaje;
	long idVolante;

	// Tomamos la sesión del cliente de la sesión HTTP
	sesion = (ISesion)session.getAttribute("SesionFrontend");
	// Tomamos el médico que está usando el sistema de la sesión HTTP
	emisor = (Medico) session.getAttribute("Medico");
	
	// Tomamos el NIF del beneficiario de los parámetros pasados al JSP
	nifBeneficiario = request.getParameter("nifBeneficiario");
	// Tomamos el NIF del especialista de los parámetros pasados al JSP
	nifReceptor = request.getParameter("nifReceptor");
	
	try {
		// Consultamos los datos del beneficiario y el médico receptor del volante
		servidor = ServidorFrontend.getServidor();
		beneficiario = servidor.consultarBeneficiarioPorNIF(sesion.getId(), nifBeneficiario);
		receptor = servidor.consultarMedico(sesion.getId(), nifReceptor);
		// Emitimos el volante
		idVolante = servidor.emitirVolante(sesion.getId(), beneficiario, emisor, receptor);
		// Consultamos los datos del volante recién emitido
		volante = servidor.consultarVolante(sesion.getId(), idVolante);
		formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
		mensaje = "El número del volante emitido es el " + idVolante +
		          " y se podrá utilizar hasta el " + formatoFecha.format(volante.getFechaCaducidad()) + ".";
	} catch(RemoteException e) {
		mensaje = "Error: " + e.getMessage();
	} catch(SQLException e) {
		mensaje = "Error: " + e.getMessage();
	} catch(BeneficiarioInexistenteException e) {
		mensaje = "Error: " + e.getMessage();
	} catch(MedicoInexistenteException e) {
		mensaje = "Error: " + e.getMessage();
	} catch(Exception e) {
		mensaje = "Error: " + e.getMessage();		
	}

	// Mostramos el resultado de la operación o el mensaje de error
	if(!mensaje.equals("")) {
		%> <%= mensaje %> <%
	}
	
%>
