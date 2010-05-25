<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="dominio.conocimiento.ISesion" %>
<%@ page import="dominio.conocimiento.Medico" %>
<%@ page import="dominio.conocimiento.DiaSemana" %>
<%@ page import="comunicaciones.ProxyServidorFrontend" %>
<%@ page import="java.util.Hashtable" %>
<%@ page import="java.util.Vector" %>
<%@ page import="java.util.Date" %>
<%@page import="dominio.conocimiento.ICodigosMensajeAuxiliar"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%
	// Recuperamos las horas en las que trabaja el especialista ese dia y se muestran en un select (si trabaja alguna hora) 
	ProxyServidorFrontend p;
	// Tomamos el idSesion del HTTPSession
	ISesion s = (ISesion) session.getAttribute("SesionFrontend");
	// Se coge tambien el especialista, pues a esta pagina se llega si el volante se ha validado
	Medico e = (Medico) session.getAttribute("especialista");
	String dia = request.getParameter("dia");
	Date diaHoy = new Date();
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	Date diaSeleccionado = df.parse(dia); ;
	// Si hoy es sabado o domingo, no se hace nada mas y se muestra el select con un mensaje
	if (diaHoy.getDay() == 0 || diaHoy.getDay() == 6) {
%>
	<select id="horas" name="horas">
		<option selected value="-2">El día actual (<%=dia%>) no es laborable</option>
	</select>
	
<%
	} else {
		p = ProxyServidorFrontend.getProxy();
		Hashtable<Date, Vector<String>> citasOcupadas = (Hashtable<Date, Vector<String>>) p.mensajeAuxiliar(s.getId(), ICodigosMensajeAuxiliar.CONSULTAR_HORAS_CITAS_MEDICO, e.getNif());
		Hashtable<DiaSemana, Vector<String>> horasCitas = (Hashtable<DiaSemana, Vector<String>>)p.mensajeAuxiliar(s.getId(), ICodigosMensajeAuxiliar.CONSULTAR_HORARIO_MEDICO, e.getNif());
		// Se toman las citas ocupadas del días pasado como parametro
		Vector<String> citasOcupadasDia = citasOcupadas.get(diaSeleccionado);
		// Si no existen citas ocupadas, se inicializa a la lista vacia, ya que lo anterior devuelve null
		if (citasOcupadasDia == null)
			citasOcupadasDia = new Vector<String>();
		// Se toman las horas donde trabaja el medico en el dia seleccionado
		Vector<String> horas = new Vector<String>();
		switch(diaSeleccionado.getDay()) {
		case 1:
			horas.addAll(horasCitas.get(DiaSemana.Lunes));
			break;
		case 2:
			horas.addAll(horasCitas.get(DiaSemana.Martes));
			break;
		case 3:
			horas.addAll(horasCitas.get(DiaSemana.Miercoles));
			break;
		case 4:
			horas.addAll(horasCitas.get(DiaSemana.Jueves));
			break;
		case 5:
			horas.addAll(horasCitas.get(DiaSemana.Viernes));
			break;
		default:
			// Los médicos no trabajan los fines de semana
			break;
		}		
%>
		<select id="horas" name="horas">
<%
		// Si no hay horas disponibles, se muestra un mensaje
		if (horas.size()==0) {
%>
			<option selected value="-1">El día seleccionado no es laborable para este especialista></option>
<%
		} else {
			for (int i=0; i<horas.size(); i++) {
				if (citasOcupadasDia.contains(horas.get(i))) {
%>
					<option class="color" value="<%=horas.get(i)%>"><%=horas.get(i)%></option>
<%
				} else {
%>
					<option value="<%=horas.get(i)%>"><%=horas.get(i)%></option>
<%
				}
			}
		}
%>
		</select>
<%
	}
%>

