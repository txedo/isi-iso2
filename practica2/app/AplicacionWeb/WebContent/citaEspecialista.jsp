<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="dominio.conocimiento.Beneficiario" %>
<%@ page import="java.util.Date" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<%@ include file="header.jsp"%>
	<script type="text/javascript" src="resources/scripts/ajax.js"></script>
	<link rel="stylesheet" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/themes/base/jquery-ui.css" type="text/css" />
	<title>Cita con especialista</title>	
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
		function validarVolante(url) {
			if (peticion){
				var volante = document.getElementById("nVolante").value;
				peticion.open("post", url, true);
				var parametros = "nVolante=" + volante;
				peticion.onreadystatechange=function() {
					if (peticion.readyState==4) {
						document.getElementById("spanEspecialista").innerHTML=peticion.responseText;
						if (!peticion.responseText.contains("Error")) {
							document.getElementById("spanEspecialista").innerHTML=peticion.responseText + 
							"<br><br>Seleccione un d�a laboral del especialista: ";
							document.getElementById("campofecha").style.visibility = "visible";
							// Al mostrar el calendario, se muestran, por defecto, las horas disponibles del d�a actual
							var fecha = new Date();
							var stringDia = fecha.getDate()+"/"+(fecha.getMonth()+1)+"/"+(fecha.getFullYear());
							consultarHoras('obtenerHoras.jsp', stringDia);
						}
					}					
				}
				peticion.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
				peticion.send(parametros);
			}
		}
		
		// Por defecto, el d�a elegido es el d�a de hoy
		var diaSeleccionado = new Date();
		var stringDiaSeleccionado = diaSeleccionado.getDate()+"/"+(diaSeleccionado.getMonth()+1)+"/"+diaSeleccionado.getYear();		
		function darCita(url) {
			if (peticion){
				peticion.open("post", url, true);
				var selectHoras = document.getElementById("horas");
				var horaSeleccionada = selectHoras.options[selectHoras.selectedIndex].value;
				if (horaSeleccionada == -1)
					alert("El d�a seleccionado no es laborable.\nPor favor, elija otro d�a.");
				else if (horaSeleccionada == -2)
					alert("La hora seleccionada para la cita ya est� ocupada.\nPor favor, elija otra hora.");
				else {
					var parametros = "dia=" + stringDiaSeleccionado + "&hora=" + horaSeleccionada;
					peticion.onreadystatechange=function() {
						if (peticion.readyState==4) {	
							// Se muestra la p�gina de �xito para las citas
							document.location='citaExito.jsp';
						}						
					}
					peticion.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
					peticion.send(parametros);
				}
			}
		}
		
		function consultarHoras (url, dia) {			
			if (peticion){
				peticion.open("post", url, true);
				var parametros = "dia=" + dia;
				peticion.onreadystatechange=function() {
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
								consultarHoras('obtenerHoras.jsp', textoFecha);
							}
			});
				
		});

	</script>
		
<body>
	<%@ include file="top.jsp"%>
	
    <div id="contenido">
		<div class="textoCuerpo">
			<%= ((Beneficiario) request.getSession(false).getAttribute("Beneficiario")).getNombre() %>, escriba su n�mero de volante <br>		
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
	
	<%@ include file="foot.jsp"%>
</body>
</html>