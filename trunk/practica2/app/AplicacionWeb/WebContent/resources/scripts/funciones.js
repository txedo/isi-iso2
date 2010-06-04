function setLocation(url) {
		document.location = url;
	}

function cargarModulo (mod) {
	var peticion = nuevoAjax();
	if (peticion) {
		peticion.open('POST',mod,true);
		peticion.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
		peticion.onreadystatechange=function() {
			if (peticion.readyState==4) {
				actualizarNavegacion (mod);
				if (mod == "logout.jsp") {
					document.getElementById("contenido").innerHTML=peticion.responseText;
					document.getElementById("auth").innerHTML="";
				} else {
					document.getElementById("mod").innerHTML=peticion.responseText;
				}
				
				// Al ejecutar la accion de obtener beneficiarios, se toma por defcto los especialistas de la primera especialidad
				if (mod == "obtenerBeneficiariosMedico"){
					cargarReceptor('ajaxObtenerEspecialistas.jsp');
				}
				else if (mod == "citaCabecera.jsp") {
					// script necesario para mostrar el Datepicker y darle formato. En este caso, se deshabilitan los fines de semana
					$(function(){
						$("#campofecha").datepicker({
							changeYear: true,
							defaultDate: new Date(),
							minDate: new Date(),
							beforeShowDay: $.datepicker.noWeekends,
							onSelect: function(textoFecha, objDatepicker){
								// Cargamos las horas del medico al cambiar el dia
								stringDiaSeleccionado = textoFecha;
								consultarHoras('ajaxObtenerHorasCabecera.jsp', textoFecha);
							}
						});
						
						// Se cambia el tema al datepicker
							$('#dialog_link, ul#icons li').hover(
								function() { $(this).addClass('ui-state-hover'); }, 
								function() { $(this).removeClass('ui-state-hover'); }
							);
					});
					cargarFechaInicial();
				}
				else if (mod == "citaEspecialista.jsp") {
					// script necesario para mostrar el Datepicker y darle formato. En este caso, se deshabilitan los fines de semana
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
						
							// Se cambia el tema al datepicker
							$('#dialog_link, ul#icons li').hover(
								function() { $(this).addClass('ui-state-hover'); }, 
								function() { $(this).removeClass('ui-state-hover'); }
							);
					});
					
				
				}
				else if(mod == "anularCita.jsp") {
					obtenerCitas('ajaxObtenerCitas.jsp');
				}
				else if(mod == "logout.jsp") {
					setLocationTimeout('index.jsp', 3);
				}
			}
		}
		peticion.send(null);
	}
}

function actualizarNavegacion (mod)
{
	if (mod != "error.jsp" && mod != "loginBeneficiario" && mod != "loginMedico") {
		var localizacion = "<a href=\"index.jsp\" title=\"Ir a la página de inicio\">Inicio</a>";
		var barraNavegacion = document.getElementById("nav")
		if (mod == "loginMedico.jsp") {
			localizacion += " > Inicar sesión como médico";
		} else if (mod == "loginBeneficiario.jsp") {
			localizacion += " > Inicar sesión como beneficiario";
		} else if (mod == "obtenerBeneficiariosMedico") {
			localizacion += " > Emitir volante";
		} else if (mod == "citaCabecera.jsp") {
			localizacion += " > Tramitar cita";
		} else if (mod == "citaEspecialista.jsp") {
			localizacion += " > Tramitar cita con un especialista";
		} else if (mod == "anularCita.jsp") {
			localizacion += " > Anular una cita";
		}
		barraNavegacion.innerHTML = localizacion;
	}
}

function setLocationTimeout(url, timeout) {
	setTimeout("document.location='" + url + "'", timeout * 1000);
}
