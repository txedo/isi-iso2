<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="dominio.conocimiento.ISesion" %>
<%@ page import="dominio.conocimiento.Medico" %>
<%@ page import="dominio.conocimiento.DiaSemana" %>
<%@ page import="comunicaciones.ServidorFrontend" %>
<%@ page import="java.util.Hashtable" %>
<%@ page import="java.util.Vector" %>
<%@ page import="java.util.Date" %>
<%@page import="dominio.conocimiento.ICodigosMensajeAuxiliar"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="dominio.conocimiento.Volante"%>
<%@page import="java.rmi.RemoteException"%>
<%@page import="java.sql.SQLException"%>

<%
	// Recuperamos las horas en las que trabaja el especialista ese dia y se muestran en un select (si trabaja alguna hora) 
	ServidorFrontend p;
	// Tomamos el idSesion del HTTPSession
	ISesion s = (ISesion) session.getAttribute("SesionFrontend");
	// Se coge tambien el especialista, pues a esta pagina se llega si el volante se ha validado
	Medico e = (Medico) ((Volante) session.getAttribute("volante")).getReceptor();
	String dia = request.getParameter("dia");
	Date diaHoy = new Date();
	SimpleDateFormat df = new SimpleDateFormat("dd/M/yyyy");
	Date diaSeleccionado = df.parse(dia);
	// Si hoy es sabado o domingo y no es el dia seleccionado, no se hace nada mas y se muestra un mensaje
	if (dia.equals(df.format(diaHoy)) && (diaHoy.getDay() == 0 || diaHoy.getDay() == 6)) {
%>
		El día <%=df.format(diaHoy)%> no es laborable por ser
		<% if (diaHoy.getDay()==0) { %> Domingo. 
		<% }else{ %> Sábado. <%} %>
<%
	} else {
		try {
			p = ServidorFrontend.getServidor();
			Hashtable<Date, Vector<String>> citasOcupadas = p.consultarHorasCitasMedico(s.getId(), e.getNif());
			Hashtable<DiaSemana, Vector<String>> horasCitas = p.consultarHorarioMedico(s.getId(), e.getNif());
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
			Horario del especialista <%=e.getApellidos()%> para el día <%=df.format(diaSeleccionado)%>
			<select id="horas" name="horas">
	<%
			// Si no hay horas disponibles, se muestra un mensaje
			if (horas.size()==0) {
	%>
				<option selected value="-1">El día seleccionado no es laborable para este especialista</option>
	<%
			} else {
				for (int i=0; i<horas.size(); i++) {
					if (citasOcupadasDia.contains(horas.get(i))) {
						// Las horas ocupadas se colorean de rojo y se pone el valor como -2, para mostrar un error si se elige una cita en esa hora
	%>
						<option class="ocupado" value="-2"><%=horas.get(i)%></option>
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
			<!-- Se coloca también el botón para obtener la cita -->
			<br> <input type="submit" value="Obtener Cita" onclick="darCita('ajaxDarCitaEspecialista.jsp')" />
	<%
		}
		catch (RemoteException ex) { %>
			Error: <%=ex.getMessage()%>
<%
		} catch (SQLException ex) {  %>
			Error: <%=ex.getMessage()%>
<%
		} catch (Exception ex) { %>
			Error: <%=ex.getMessage()%>
<%		
		}
	}
				
%>