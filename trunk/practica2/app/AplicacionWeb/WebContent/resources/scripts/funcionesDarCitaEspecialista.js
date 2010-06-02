		var peticion = null;
		peticion = nuevoAjax();
		
		// Por defecto, el d�a elegido es el d�a de hoy
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
							"<br><br>Elija el d�a y la hora de la cita:<br>";
							document.getElementById("campofecha").style.visibility = "visible";
							// Al mostrar el calendario, se muestran, por defecto, las horas disponibles del d�a actual
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
					alert("El d�a seleccionado no es laborable.\nPor favor, elija otro d�a.");
				else if (horaSeleccionada == -2)
					alert("La hora seleccionada para la cita ya est� ocupada.\nPor favor, elija otra hora.");
				else {
					var parametros = "dia=" + stringDiaSeleccionado + "&hora=" + horaSeleccionada;
					peticion.onreadystatechange=function() {
						if (peticion.readyState==4) {	
							if (peticion.responseText.indexOf("Error")==-1){
								// Se muestra la p�gina de �xito para las citas, si no hay errores
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
		
		// Consulta la lista de horas disponibles para el m�dico
		// (p�gina 'ajaxObtenerHorasEspecialista.jsp')
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
	
		// Consulta el m�dico real que dar� la cita seleccionada
		// (p�gina 'ajaxConsultarMedicoCita.jsp')
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