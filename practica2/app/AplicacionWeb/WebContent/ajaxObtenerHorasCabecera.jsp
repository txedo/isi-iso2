<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" %>
<%@ page import="dominio.conocimiento.ISesion" %>
<%@ page import="dominio.conocimiento.Medico" %>
<%@ page import="dominio.conocimiento.DiaSemana" %>
<%@ page import="dominio.conocimiento.Beneficiario" %>
<%@ page import="dominio.UtilidadesDominio" %>
<%@ page import="comunicaciones.ServidorFrontend" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.Hashtable" %>
<%@ page import="java.util.Vector" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.rmi.RemoteException" %>
<%@ page import="java.sql.SQLException" %>

<%

	ServidorFrontend servidor;
	Hashtable<Date, Vector<String>> citasOcupadas;
	Hashtable<DiaSemana, Vector<String>> horasCitas;
	Vector<String> citasOcupadasDia, horas;
	Vector<String> options = null;
	ISesion sesion;
	Medico cabecera;
	SimpleDateFormat formatoFecha;
	Calendar cal;
	Date diaHoy, diaSeleccionado;
	String dia, mensaje;
	
	// Tomamos la sesi�n del cliente de la sesi�n HTTP
	sesion = (ISesion)session.getAttribute("SesionFrontend");
	
	// Tomamos el m�dico de cabecera asociado al beneficiario
	// pasado como par�metro al JSP
	cabecera = ((Beneficiario)session.getAttribute("Beneficiario")).getMedicoAsignado();
	// Tomamos el d�a para el que se pretende dar la cita
	dia = request.getParameter("dia");
	
	// Convertimos la cadena con la fecha en una instancia de Date
	formatoFecha = new SimpleDateFormat("dd/M/yyyy");
	diaSeleccionado = formatoFecha.parse(dia);
	diaHoy = new Date();
	cal = Calendar.getInstance();
	cal.setTime(diaHoy);

	// Si hoy es s�bado o domingo y no es el d�a seleccionado, no se
	// hace nada m�s y se muestra un mensaje de error
	if(UtilidadesDominio.fechaIgual(diaSeleccionado, diaHoy, false)
	  && (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)) {
		mensaje = "El d�a " + formatoFecha.format(diaHoy) + " no es laborable por ser";
		if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			mensaje += "domingo.";
		} else {
			mensaje += "s�bado.";
		}
	} else {
		try {
			// Recuperamos las horas en las que trabaja el especialista
			// el d�a seleccionado y las citas que tiene ocupadas ese d�a
			servidor = ServidorFrontend.getServidor();
			citasOcupadas = servidor.consultarHorasCitasMedico(sesion.getId(), cabecera.getNif());
			horasCitas = servidor.consultarHorarioMedico(sesion.getId(), cabecera.getNif());
			citasOcupadasDia = citasOcupadas.get(diaSeleccionado);
			// Si el m�dico no tiene citas ocupadas, se inicializa la
			// lista, ya que el m�todo anterior devuelve null
			if(citasOcupadasDia == null) {
				citasOcupadasDia = new Vector<String>();
			}
			// Se toman las horas donde trabaja el medico en el dia seleccionado
			horas = new Vector<String>();
			cal.setTime(diaSeleccionado);
			if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
				// Los m�dicos no trabajan los fines de semana
			} else {
				horas.addAll(horasCitas.get(UtilidadesDominio.diaFecha(diaSeleccionado)));
			}
			// Generamos la lista HTML con las horas en las que el m�dico pasa
			// cita, marcando de color rojo las horas ocupadas
			options = new Vector<String>();
			if(horas.size() == 0) {
				options.add("<option style=\"color:#AAAAAA;\" selected value =\"-1\">El d�a seleccionado no es laborable para este m&eacute;dico</option>");
			} else {
				for(int i = 0; i < horas.size(); i++) {
					if(citasOcupadasDia.contains(horas.get(i))) {
						options.add("<option class=\"ocupado\" value=\"-2\">" + horas.get(i) + "</option>");
					} else {
						options.add("<option value=\"" + horas.get(i) + "\">" + horas.get(i) + "</option>");
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
	}
	
	if(mensaje.equals("")) { %>
		Horario del m&eacute;dico <%= cabecera.getApellidos() %> para el d�a <%= dia %>
		<select id="horas" name="horas">
			<% for(int i = 0; i < options.size(); i++) { %>
				<%= options.get(i) %>
			<% } %>
		</select>
		<!-- Se coloca tambi�n el bot�n para obtener la cita -->
		<br> <input type="submit" value="Obtener Cita" onclick="darCita('ajaxDarCitaCabecera.jsp')" /> <%
	} else {
		%> <%= mensaje %> <%
	}
	
%>
