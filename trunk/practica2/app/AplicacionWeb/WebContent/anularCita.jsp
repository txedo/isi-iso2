<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="dominio.conocimiento.Beneficiario" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="resources/styles/style.css" type="text/css" />
<title>Anular Cita</title>

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
		
		function obtenerCitas(nif, url){
			var peticion = nuevoAjax();
			if (peticion){
				peticion.open("post", url, true);
				var parametros = "nif=" + nif;
				peticion.onReadyStateChange=function() {
					if (peticion.readyState==4) {					
						document.getElementById("areaCitas").innerHTML=peticion.responseText;
					}						
				}
				peticion.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
				peticion.send(parametros);
			}
		}
	</script>
	
	
</head>
<% Beneficiario b = (Beneficiario) request.getSession(false).getAttribute("Beneficiario"); %>
			
<body onload="javascript:obtenerCitas('<%=b.getNif()%>', 'obtenerCitas.jsp')">
	<div id="header">
        <div class="textoCabecera">Título de la cabecera</div>
	</div>
    <div id="contenido">
    	<div class="textoCuerpo">
		  <%= b.getNombre() %>, elija la cita que desee anular: <br>	
			<br>
			<span id="areaCitas">
			</span>
		</div>
	</div>
	
	<div id="pie">
        Texto del pie
    </div>
</body>
</html>