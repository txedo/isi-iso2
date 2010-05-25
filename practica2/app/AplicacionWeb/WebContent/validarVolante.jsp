<%@page import="java.util.Vector"%>
<%@page import="java.util.Date"%>
<%@page import="dominio.conocimiento.ICodigosMensajeAuxiliar"%>
<%@page import="dominio.conocimiento.Volante"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.rmi.NotBoundException"%>
<%@page import="java.net.MalformedURLException"%>
<%@page import="java.rmi.RemoteException"%>
<%@page import="comunicaciones.IConexion"%>
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
	Vector<Date> fechas = null;
	Volante v = null;
	try {
		p = ProxyServidorFrontend.getProxy();
		v = p.getVolante(idSesion, nVolante);
		/* Se pone el especialista en la sesión del beneficiario para no tener que volver a consultar el
		especialista cuando se quieran mostrar las horas laborales en cada dia seleccionado. Asi, se
		ahorran consultas, ya que si no, cada vez que se cambie el dia del calendario y se tengan que refrescar
		las horas, se tendría que consultar el volante y su receptor */
		session = request.getSession(false);
		session.setAttribute("especialista", v.getReceptor());
		} catch (RemoteException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (MalformedURLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (NotBoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
%>

Le atenderá el Dr. <%= v.getReceptor().getApellidos() %>