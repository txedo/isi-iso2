<%@page import="dominio.conocimiento.Volante"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.rmi.NotBoundException"%>
<%@page import="java.net.MalformedURLException"%>
<%@page import="java.rmi.RemoteException"%>
<%@page import="comunicaciones.IConexion"%>
<%@page import="dominio.conocimiento.ISesion"%>
<%@page import="comunicaciones.ProxyServidorFrontend"%>
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
		p.conectar(IConexion.IP, IConexion.PUERTO);
		v = p.getVolante(idSesion, nVolante);
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