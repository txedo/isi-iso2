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