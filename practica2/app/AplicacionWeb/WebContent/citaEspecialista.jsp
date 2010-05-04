<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="dominio.conocimiento.Beneficiario" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="dominio.conocimiento.ISesion"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Cita con especialista</title>	
</head>

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
	 	
<body>
	<%= ((Beneficiario) request.getSession(false).getAttribute("Beneficiario")).getNombre() %>, escriba su número de volante: <br>
	<br>Volante: <input type="text" name="nVolante"/>
	<input type="submit" value="Aceptar" onclick="recibeDatos('validarVolante.jsp')"/>
	
	<br><br><br>
	<span id="spanEspecialista">
		Introduzca un numero de volante para cargar el calendario del especialista ...
	</span>
	
	
	
	<!-- <s:form action="citaEspecialista">
		<s:textfield name="nVolante" label="Volante"></s:textfield>
		<s:submit value="Aceptar" onclick="cargarSpan()"></s:submit>
	</s:form> -->

</body>
</html>