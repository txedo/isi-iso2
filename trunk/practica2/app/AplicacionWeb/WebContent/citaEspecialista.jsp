<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="dominio.conocimiento.Beneficiario" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<%@ include file="resources/templates/header.htm" %>
	<title>SSCAWeb - Cita con Especialista</title>	
	<script type="text/javascript" src="resources/scripts/ajax.js"></script>
	<link rel="stylesheet" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/themes/base/jquery-ui.css" type="text/css" />
</head>

<!--  Se cargan los scripts necesarios para el Datepicker -->
<script type="text/javascript" src="resources/scripts/jquery-1.4.2.js" ></script>
<script type="text/javascript" src="resources/scripts/jquery.ui.core.js" ></script>
<script type="text/javascript"  src="resources/scripts/jquery.ui.datepicker-es.js"></script>
<script type="text/javascript" src="resources/scripts/jquery.ui.datepicker.js"></script>

<!--  scripts necesarios para AJAX -->
<script language="JavaScript" type="text/javascript"> 
		var peticion = null;
		peticion = nuevoAjax();
		
		// Por defecto, el día elegido es el día de hoy
		var diaSeleccionado = new Date();
		var stringDiaSeleccionado;
		if(diaSeleccionado.getDate()<10) {
			stringDiaSeleccionado = "0" + diaSeleccionado.getDate();
		} else {
			stringDiaSeleccionado = diaSeleccionado.getDate();
		}
		if(diaSeleccionado.getMonth()<9) {
			stringDiaSeleccionado = stringDiaSeleccionado+"/0"+(diaSeleccionado.getMonth()+1)+"/"+(1900 + diaSeleccionado.getYear());
		} else {
			stringDiaSeleccionado = stringDiaSeleccionado+"/"+(diaSeleccionado.getMonth()+1)+"/"+(1900 + diaSeleccionado.getYear());
		}
		
		function validarVolante(url) {
			if (peticion){
				// Se limpia el mensaje de error, por si lo hubiera
				document.getElementById("mensaje").innerHTML='';
				var volante = document.getElementById("nVolante").value;
				peticion.open("post", url, true);
				var parametros = "nVolante=" + volante;
				peticion.onreadystatechange=function() {
					if (peticion.readyState==4) {
						if (peticion.responseText.indexOf("Error")==-1) {
							document.getElementById("spanEspecialista").innerHTML=peticion.responseText + 
							"<br><br>Elija el día y la hora de la cita:<br>";
							document.getElementById("campofecha").style.visibility = "visible";
							// Al mostrar el calendario, se muestran, por defecto, las horas disponibles del día actual
							consultarHoras('ajaxObtenerHorasEspecialista.jsp', stringDiaSeleccionado);
						}
						else {
							document.getElementById("spanEspecialista").innerHTML=peticion.responseText;
							document.getElementById("campofecha").style.visibility = "hidden";
							document.getElementById("spanHoras").innerHTML='';
						}
					}					
				}
				peticion.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
				peticion.send(parametros);
			}
		}
		
		function darCita(url) {
			if (peticion){
				peticion.open("post", url, true);
				var selectHoras = document.getElementById("horas");
				var horaSeleccionada = selectHoras.options[selectHoras.selectedIndex].value;
				if (horaSeleccionada == -1)
					alert("El día seleccionado no es laborable.\nPor favor, elija otro día.");
				else if (horaSeleccionada == -2)
					alert("La hora seleccionada para la cita ya está ocupada.\nPor favor, elija otra hora.");
				else {
					var parametros = "dia=" + stringDiaSeleccionado + "&hora=" + horaSeleccionada;
					peticion.onreadystatechange=function() {
						if (peticion.readyState==4) {	
							if (peticion.responseText.indexOf("Error")==-1){
								// Se muestra la página de éxito para las citas, si no hay errores
								document.location='citaExito.jsp?'+parametros;
							} else {
								document.getElementById("mensaje").innerHTML=peticion.responseText;
							}
						}						
					}
					peticion.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
					peticion.send(parametros);
				}
			}
		}
		
		// Consulta la lista de horas disponibles para el médico
		// (página 'ajaxObtenerHorasEspecialista.jsp')
		function consultarHoras(url, dia) {			
			if(peticion) {
				peticion.open("post", url, true);
				var parametros = "dia=" + dia;
				peticion.onreadystatechange = function() {
					if(peticion.readyState == 4) {	
						document.getElementById("spanHoras").innerHTML = peticion.responseText;
						ponerMedicoReal("ajaxConsultarMedicoCita.jsp");
					}
				}
				peticion.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
				peticion.send(parametros);
			}
		}
	
		// Consulta el médico real que dará la cita seleccionada
		// (página 'ajaxConsultarMedicoCita.jsp')
		function ponerMedicoReal(url) {
			var selectHoras = document.getElementById("horas");
			var hora = selectHoras.options[selectHoras.selectedIndex].value;
			
			if(peticion) {
				peticion.open("post", url, true);
				var parametros = "dia=" + stringDiaSeleccionado + "&hora=" + hora;
				peticion.onreadystatechange = function() {
					if(peticion.readyState == 4) {
						document.getElementById("mensaje").innerHTML = peticion.responseText;
					}						
				}
				peticion.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
				peticion.send(parametros);
			}
		}
		
	</script>
	
	<!-- script necesario para mostrar el Datepicker y darle formato. En este caso, se 
	deshabilitan los fines de semana -->
	<script type="text/javascript"> 		
		$(function(){
			$("#campofecha").datepicker({
							changeYear: true,
							defaultDate: new Date(),
							minDate: new Date(),
							beforeShowDay: $.datepicker.noWeekends,
							onSelect: function(textoFecha, objDatepicker){
								// Cargamos las horas del medico al cambiar el dia
								stringDiaSeleccionado = textoFecha;
								consultarHoras('ajaxObtenerHorasEspecialista.jsp', textoFecha);
							}
			});
		});

	</script>
		
<body>
	<%@ include file="resources/templates/top.htm" %>
	
    <div id="contenido">
		<div class="textoCuerpo">
			<%= ((Beneficiario) request.getSession(false).getAttribute("Beneficiario")).getNombre() %>, escriba su número de volante:<br>		
			<br>Volante: <s:textfield id="nVolante" name="nVolante"></s:textfield>
			<input type="submit" value="Aceptar" onclick="validarVolante('ajaxValidarVolante.jsp')"/>

			<br><br>
			<span id="spanEspecialista">
			</span>
			
			<!-- En este div se carga el datepicker -->
			<div id="campofecha" style="visibility:hidden"></div>
			<br>
			<span id="spanHoras"></span>
			<br>
			<span id="mensaje"></span>
		</div>
		<div class="volver">
			<input type="button" onclick="history.go(-1)" value="Volver atrás">
		</div>
	</div>
	
	<%@ include file="resources/templates/foot.htm" %>
</body>
</html>
