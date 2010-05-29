<%@page import="excepciones.VolanteNoValidoException"%>
<%@page import="dominio.conocimiento.Volante"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.rmi.RemoteException"%>
<%@page import="dominio.conocimiento.ISesion"%>
<%@page import="comunicaciones.ProxyServidorFrontend"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<% 
	long idSesion = ((ISesion) request.getSession(false).getAttribute("SesionFrontend")).getId();
	long nVolante = Long.parseLong(request.getParameter("nVolante"));
	ProxyServidorFrontend p;
	Volante v = null;
	try {
		p = ProxyServidorFrontend.getProxy();
		v = p.getVolante(idSesion, nVolante);
		// Se pone el volante en la sesión del beneficiario para no tener que volver a consultar el
		// especialista cuando se quieran mostrar las horas laborales en cada dia seleccionado. Asi, se
		// ahorran consultas, ya que si no, cada vez que se cambie el dia del calendario y se tengan que refrescar
		// las horas, se tendría que consultar el volante y su receptor
		session = request.getSession(false);
		session.setAttribute("volante", v);
%>
		Le atenderá el Dr. <%= v.getReceptor().getApellidos() %>
<%
	} catch (RemoteException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch(VolanteNoValidoException e) {%>
		Error: <%=e.getMessage()%>
<%
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
%>