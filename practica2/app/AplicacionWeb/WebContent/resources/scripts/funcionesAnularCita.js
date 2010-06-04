var peticion = null;
peticion = nuevoAjax();

function anularCita(url) {
	if(peticion) {
		var selectCita = document.getElementById("citas");
		var idCita = selectCita.options[selectCita.selectedIndex].value;
		if(idCita == -2) {
			alert("No tiene ninguna cita registrada.");
		} else if(idCita == -1) {
			alert("Sólo se puede anular una cita posterior al día de hoy.");
		} else {
			if(confirm("¿Realmente desea anular la cita seleccionada?")) {
				peticion.open('POST', url, true);
				peticion.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
				var parametros = "idCita=" + idCita;
				peticion.onreadystatechange=function() {
					if (peticion.readyState==4) {					
						document.getElementById("mensaje").innerHTML = peticion.responseText;
						// Recargamos las citas, si no hay error
						if (peticion.responseText.indexOf("Error")==-1) {
							obtenerCitas('ajaxObtenerCitas.jsp');
						}
					}
				}
				peticion.send(parametros);
			}
		}
	}
}

function obtenerCitas(url){
	if(peticion) {
		peticion.open('POST', url, true);
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
		peticion.send(null);
	}
}