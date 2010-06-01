<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" %>
<%@ page import="comunicaciones.ServidorFrontend" %>
<%@ page import="dominio.conocimiento.ISesion" %>
<%@ page import="dominio.conocimiento.Medico" %>
<%@ page import="dominio.conocimiento.Beneficiario" %>
<%@page import="java.rmi.RemoteException"%>
<%@page import="java.sql.SQLException"%>

<%

	ServidorFrontend servidor;
	Medico emisor, receptor;
	Beneficiario beneficiario;
	ISesion sesion;
	String nifBeneficiario, nifReceptor;
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
%>	
		<br><br><br>El numero de volante emitido es el: <%= idVolante %>.
		
<%  }
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


