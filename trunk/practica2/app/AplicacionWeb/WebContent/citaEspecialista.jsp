<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="dominio.conocimiento.Beneficiario" %>
<%@ page import="java.util.Date" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/themes/base/jquery-ui.css" type="text/css" />
<link rel="stylesheet" href="resources/styles/style.css" type="text/css" />
<title>Cita con especialista</title>	
</head>

<!--  Se cargan los scripts necesarios para el Datepicker -->
<script type="text/javascript" src="resources/scripts/jquery-1.4.2.js" ></script>
<script type="text/javascript" src="resources/scripts/jquery.ui.core.js" ></script>
<script type="text/javascript"  src="resources/scripts/jquery.ui.datepicker-es.js"></script>
<script type="text/javascript" src="resources/scripts/jquery.ui.datepicker.js"></script>

<!--  scripts necesarios para AJAX -->
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
		
		var peticion = nuevoAjax();
		function validarVolante(url) {
			if (peticion){
				var volante = document.getElementById("nVolante").value;
				peticion.open("post", url, true);
				var parametros = "nVolante=" + volante;
				peticion.onReadyStateChange=function() {
					if (peticion.readyState==4) {
						document.getElementById("spanEspecialista").innerHTML=peticion.responseText + 
						"<br><br>Seleccione un día laboral del especialista: ";
						document.getElementById("campofecha").style.visibility = "visible";
					}						
				}
				peticion.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
				peticion.send(parametros);
			}
		}
		
		function consultarHoras (url, dia) {
			alert("llega");
			if (peticion){
				peticion.open("post", url, true);
				var parametros = "dia=" + dia;
				peticion.onReadyStateChange=function() {
					if (peticion.readyState==4) {					
						document.getElementById("spanHoras").innerHTML=peticion.responseText;
					}						
				}
				peticion.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
				peticion.send(parametros);
			}
		}
	
	</script>
	
	<!-- script necesario para mostrar el Datepicker y darle formato. En este caso, se 
	deshabilitan días ocupados -->
	<script type="text/javascript"> 
			/* Días ocupados del especialista */
			var disabledDays = ["10/07/2010"];

			/* Desactivar los dias no laborables del especialista */
			function desactivarDias(date) {
				var mes = date.getMonth(), dia = date.getDate(), año = date.getFullYear();
				for (i = 0; i < disabledDays.length; i++) {
					if(ArrayContains(disabledDays, dia + '/' + (mes+1) + '/' + año) || new Date() > date) {
						return [false];
					}
				}
				return [true];
			}
			
			function ArrayIndexOf(array,item,from){
				var len = array.length;
				for (var i = (from < 0) ? Math.max(0, len + from) : from || 0; i < len; i++){
					if (array[i] === item) return i;
				}
				return -1;
			}
			
			function ArrayContains(array,item,from){
				return ArrayIndexOf(array,item,from) != -1;
			}
		
		// La fecha seleccionada se muestra en una label y además se pasa el valor
		// al campo "fechaElegida", que es un campo de texto de struts2
		$(function(){
			$("#campofecha").datepicker({
							changeYear: true,
							defaultDate: new Date(),
							minDate: new Date(),
							beforeShowDay: $.datepicker.noWeekends,
							onSelect: function(textoFecha, objDatepicker){
								$("#mensaje").html("Ha elegido el día: " + textoFecha);
								// Cargamos las horas del medico al cambiar el dia
								if (peticion){
									peticion.open("post", 'obtenerHoras.jsp', true);
									var parametros = "dia=" + textoFecha;
									peticion.onReadyStateChange=function() {
										if (peticion.readyState==4) {	
											document.getElementById("spanHoras").innerHTML=peticion.responseText;
										}						
									}
									peticion.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
									peticion.send(parametros);
								}
							}
			});
				
		});

	</script>
		
<body>
	
	<div id="header">
        <div class="textoCabecera">Título de la cabecera</div>
	</div>
	
    <div id="contenido">

		<div class="textoCuerpo">

			<%= ((Beneficiario) request.getSession(false).getAttribute("Beneficiario")).getNombre() %>, escriba su número de volante <br>		
			<br>Volante: <s:textfield id="nVolante" name="nVolante"></s:textfield>
			<input type="submit" value="Aceptar" onclick="validarVolante('validarVolante.jsp')"/>

			<br><br><br>
			<span id="spanEspecialista">
			</span>
			
			<!-- En este div se carga el datepicker -->
			<div id="campofecha" style="visibility:hidden"></div>
			<div id="mensaje"></div>
			<br> <br>
			<span id="spanHoras"></span>
		</div>	
	</div>
	<div id="pie">
        Texto del pie
    </div>
</body>
</html>