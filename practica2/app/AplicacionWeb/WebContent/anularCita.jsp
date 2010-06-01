<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="dominio.conocimiento.Beneficiario" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<%@ include file="resources/templates/header.htm" %>
	<script type="text/javascript" src="resources/scripts/ajax.js"></script>
	<title>SSCAWeb - Anular Cita</title>
	<script language="JavaScript" type="text/javascript"> 	
		var peticion = null;
		peticion = nuevoAjax();
		
		function anularCita(nif, url) {
			if(peticion) {
				peticion.open('POST', url, true);
				peticion.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
				var selectCita = document.getElementById("citas");
				var idCita = selectCita.options[selectCita.selectedIndex].value;
				if (idCita != -1) {
					var parametros = "idCita=" + idCita;
					peticion.onreadystatechange=function() {
						if (peticion.readyState==4) {					
							document.getElementById("mensaje").innerHTML = peticion.responseText;
							// Recargamos las citas, si no hay error
							if (peticion.responseText.indexOf("Error")==-1) {
								obtenerCitas(nif, 'ajaxObtenerCitas.jsp');
							}
						}
					}
					peticion.send(parametros);
				} else if(idCita == -2) {
					alert("No tiene ninguna cita registrada.");
				} else {
					alert("Sólo se puede anular una cita posterior al día de hoy.");
				}
			}
		}
		
		function obtenerCitas(nif, url){
			if(peticion) {
				peticion.open('POST', url, true);
				var parametros = "nif=" + nif;
				peticion.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
				peticion.onreadystatechange = function() {
					if (peticion.readyState == 4) {
						if (peticion.responseText.indexOf("Error")==-1) {
							document.getElementById("areaCitas").innerHTML = peticion.responseText;
						} else {
							document.getElementById("mensaje").innerHTML = peticion.responseText;
						}
					}						
				}
				peticion.send(parametros);
			}
		}
	</script>
</head>
<% Beneficiario b = (Beneficiario) request.getSession(false).getAttribute("Beneficiario"); %>
			
<body onload="javascript:obtenerCitas('<%=b.getNif()%>', 'ajaxObtenerCitas.jsp')">
	<%@ include file="resources/templates/top.htm" %>
	
    <div id="contenido">
    	<div class="textoCuerpo">
		  <%= b.getNombre() %>, elija la cita que desee anular: <br>	
			<br>
			<span id="areaCitas">
			</span>
			<span id="mensaje">
			</span>
		</div>
		<div class="volver">
			<input type="button" onclick="history.go(-1)" value="Volver atrás">
		</div>
	</div>
	
	<%@ include file="resources/templates/foot.htm" %>
</body>
</html>
