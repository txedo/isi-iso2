<%@page import="excepciones.VolanteNoValidoException"%>
<%@page import="dominio.conocimiento.Volante"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.rmi.RemoteException"%>
<%@page import="dominio.conocimiento.ISesion"%>
<%@page import="comunicaciones.ServidorFrontend"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<% 
	long idSesion = ((ISesion) request.getSession(false).getAttribute("SesionFrontend")).getId();
	long nVolante = Long.parseLong(request.getParameter("nVolante"));
	ServidorFrontend p;
	Volante v = null;
	try {
		p = ServidorFrontend.getServidor();
		v = p.consultarVolante(idSesion, nVolante);
		// Se pone el volante en la sesi�n del beneficiario para no tener que volver a consultar el
		// especialista cuando se quieran mostrar las horas laborales en cada dia seleccionado. Asi, se
		// ahorran consultas, ya que si no, cada vez que se cambie el dia del calendario y se tengan que refrescar
		// las horas, se tendr�a que consultar el volante y su receptor
		session = request.getSession(false);
		session.setAttribute("volante", v);
%>
		Le atender� el Dr. <%= v.getReceptor().getApellidos() %>
<%
	}
	catch (RemoteException e) { %>
		Error: <%=e.getMessage()%>
<%
	} catch (SQLException e) {  %>
		Error: <%=e.getMessage()%>
<%	} catch(VolanteNoValidoException e) {%>
		Error: <%=e.getMessage()%>
<%
	} catch (Exception e) {%>
		Error: <%=e.getMessage()%>
<%
	}
%>