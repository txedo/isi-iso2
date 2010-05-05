<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="dominio.conocimiento.Medico" %>
<%@page import="dominio.conocimiento.Especialidades"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Dar Volante</title>
</head>
<body> 
	<%
		// En la sesion, se guarda el objeto que haya colocado la accion en la ValueStack,
		// para luego usarlo en las otras páginas para sacar el nombre del médico, por ejemplo.
		session=request.getSession(true);
		session.setAttribute("Medico",request.getAttribute("medico"));
		session.setAttribute("SesionFrontend", request.getAttribute("sesion"));
		Medico med = (Medico) request.getSession(false).getAttribute("Medico");
	%>
	
	<s:form action="darVolante">
		Medico emisor: <input type="text" value="Dr./Dra. <%= med.getApellidos()%>"/>
		<s:select name="beneficiario" list="beneficiarios" listKey="nif" listValue="apellidos, nombre" label="Beneficiario:"></s:select>
		<s:select name="especialidad" list="{
			<%
				// Generamos la lista de especialidades, con la clave y el valor
				int i=0;
				for (i=0; i<Especialidades.values().length-1; i++) {		
			%>
					'<%=i%>':'<%=Especialidades.values()[i]%>', 
			<%
				}
			%>
				'<%=i+1%>':'<%=Especialidades.values()[i+1]%>'} " label="Especialidad:"></s:select>
		<s:select name="especialista" list="especialistas" listKey="nif" listValue="apellidos, nombre" label="Dr./Dra.:"> </s:select>
		<s:submit value="Aceptar"></s:submit>
		</s:form>
		
		<span id="volanteDado">
			<%
				// Cuando la acción haya colocado el médico en la ValueStack, se actualiza el span
				// con el calendario y el nombre del especialista
				boolean cargado= Boolean.parseBoolean(request.getParameter("cargado"));
				if (cargado) {
			%>
					Volante nº <s:property value="volante.id"/>
			<%
				}
				else  {
			%>
					Introduzca los datos para poder emitir un volante ...
			<%
				}
			%>	
		</span>
</body>
</html>