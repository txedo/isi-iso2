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

		function cargarFechaInicial() {
			// Al mostrar el calendario, se muestran, por defecto, las horas disponibles del día actual
			consultarHoras('ajaxObtenerHorasCabecera.jsp', stringDiaSeleccionado);
		}
		
		function darCita(url) {
			if (peticion){
				peticion.open("post", url, true);
				var selectHoras = document.getElementById("horas");
				var horaSeleccionada = selectHoras.options[selectHoras.selectedIndex].value;
				if (horaSeleccionada == -1)
					alert("El día seleccionado no es laborable para el médico.\nPor favor, elija otro día.");
				else if (horaSeleccionada == -2)
					alert("La hora seleccionada para la cita ya está ocupada.\nPor favor, elija otra hora.");
				else {
					var parametros = "dia=" + stringDiaSeleccionado + "&hora=" + horaSeleccionada;
					peticion.onreadystatechange = function() {
						if(peticion.readyState == 4) {	
							document.getElementById("mod").innerHTML=peticion.responseText;
						}						
					}
					peticion.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
					peticion.send(parametros);
				}
			}
		}
		
		// Consulta la lista de horas disponibles para el médico
		// (página 'ajaxObtenerHorasCabecera.jsp')
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
		

