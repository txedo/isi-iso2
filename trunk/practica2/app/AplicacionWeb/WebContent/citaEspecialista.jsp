<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="dominio.conocimiento.Beneficiario" %>
<%@ page import="dominio.conocimiento.ISesion" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Cita con especialista</title>	
</head>

<!--  Esta parte de AJAX no es necesaria, pues se va a realizar con Struts
<script language="JavaScript" type="text/javascript"> 
		function nuevoAjax(){
			var xmlhttp=false;
		 	try {
		 		xmlhttp = new ActiveXObject("Msxml2.XMLHTTP");
		 	} catch (e) {
		 		try {
		 			xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
		 		} catch (E) {
		 			xmlhttp = false;
		 		}
		  	}
		
			if (!xmlhttp && typeof XMLHttpRequest!='undefined') {
		 		xmlhttp = new XMLHttpRequest();
			}
			return xmlhttp;
		}
		
		function recibeDatos(url) {
			var peticion = nuevoAjax();
			if (peticion){
				var volante = document.getElementById("nVolante").value;
				peticion.open("post", url, true);
				var parametros = "nVolante=" + volante;
				peticion.onReadyStateChange=function() {
					if (peticion.readyState==4) {
						document.getElementById("spanEspecialista").innerHTML=peticion.responseText;
					}						
				}
				peticion.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
				peticion.send(parametros);
			}
		}
	
	</script>
	
	-->
		 	
<body>
	<%= ((Beneficiario) request.getSession(false).getAttribute("Beneficiario")).getNombre() %>, escriba su n�mero de volante: <br>
 	
	<s:form action="citaEspecialista">
		<s:textfield name="nVolante" label="Volante"></s:textfield>
		<s:submit value="Aceptar"></s:submit>
	</s:form>

	<!-- Esta parte es si no se utiliza Struts y se hace con AJAX de manera convencional
		<br>Volante: <input type="text" name="nVolante"/>
		<input type="submit" value="Aceptar" onclick="recibeDatos('validarVolante.jsp')"/>
	
	-->
	
	<br><br><br>
	<span id="spanEspecialista">
		<%
			// Cuando la acci�n haya colocado el m�dico en la ValueStack, se actualiza el span
			// con el calendario y el nombre del especialista
			boolean cargado= Boolean.parseBoolean(request.getParameter("cargado"));
			if (cargado) {
		%>
				Le atender� el Dr. <s:property value="volante.receptor.apellidos"/>
		<%
			}
			else  {
		%>
				Introduzca un numero de volante para cargar el calendario del especialista ...
		<%
			}
		%>	
	</span>
	
</body>
</html>