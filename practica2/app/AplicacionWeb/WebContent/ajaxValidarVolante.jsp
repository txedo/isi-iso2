<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" %>
<%@ page import="comunicaciones.ServidorFrontend" %>
<%@ page import="dominio.conocimiento.Volante" %>
<%@ page import="dominio.conocimiento.ISesion" %>
<%@ page import="dominio.conocimiento.Beneficiario" %>
<%@ page import="dominio.UtilidadesDominio" %>
<%@ page import="excepciones.VolanteNoValidoException" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.rmi.RemoteException" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>

<script type="text/javascript" src="resources/scripts/funciones.js"></script>

<%

	//Si se accede directamente en el navegador a esta p�gina, se redirecciona al index.jsp
	if (request.getHeader("referer")==null) { 
	%>
		<script type="text/javascript" >setLocation("index.jsp");</script>
	<%
	}
	ServidorFrontend servidor;
	ISesion sesion;
	Beneficiario beneficiario;
	Volante volante;
	SimpleDateFormat formatoFecha;
	String mensaje;
	long nVolante;
	
	// Tomamos la sesi�n del cliente de la sesi�n HTTP
	sesion = (ISesion)session.getAttribute("SesionFrontend");
	// Tomamos el beneficiario que est� usando el sistema de la sesi�n HTTP
	beneficiario = (Beneficiario)session.getAttribute("Beneficiario");

	try {
		// Tomamos el id del volante de los par�metros pasados al JSP
		nVolante = Long.parseLong(request.getParameter("nVolante"));
		// Consultamos los datos del volante indicado
		servidor = ServidorFrontend.getServidor();
		volante = servidor.consultarVolante(sesion.getId(), nVolante);
		// Comprobamos si el volante est� asociado al beneficiario
		// que ha iniciado sesi�n, no se ha usado ya y no ha expirado
		if(!volante.getBeneficiario().getNif().equals(beneficiario.getNif())) {
			mensaje = "Error: El volante " + nVolante + " no fue emitido para usted.";
		} else if(volante.getCita() != null) {
			mensaje = "Error: El volante " + nVolante + " ya se ha utilizado."; 
		} else if(UtilidadesDominio.fechaAnterior(volante.getFechaCaducidad(), new Date(), false)) {
			formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
			mensaje = "Error: El volante " + nVolante + " expir� el " + formatoFecha.format(volante.getFechaCaducidad()) + "."; 
		} else {
			// Ponemos el volante en la sesi�n HTTP del beneficiario para
			// no tener que volver a consultar el especialista cuando se
			// quieran mostrar las horas laborales en cada dia seleccionado.
			// Asi, se ahorran consultas, ya que si no, cada vez que se
			// cambie el dia del calendario y se tengan que refrescar las
			// horas, se tendr�a que consultar el volante y su receptor
			session.setAttribute("volante", volante);
			mensaje = "Volante para el Dr./Dra. " + volante.getReceptor().getApellidos() + ".";
		}
	} catch(RemoteException e) {
		mensaje = "Error: " + e.getMessage();
	} catch(SQLException e) {
		mensaje = "Error: " + e.getMessage();
	} catch(VolanteNoValidoException e) {
		mensaje = "Error: " + e.getMessage();
	} catch(NumberFormatException e) {
		mensaje = "Error: El n�mero de volante no es v�lido.";
	} catch(Exception e) {
		mensaje = "Error: " + e.getMessage();
	}

	// Mostramos el resultado de la operaci�n o el mensaje de error
	if(!mensaje.equals("")) {
		%> <%= mensaje %> <%
	}

%>
