function nuevoAjax(){
   	var xmlHttpReq = false;
       // Mozilla/Safari/Opera/Chrome
       if (window.XMLHttpRequest) {
               xmlHttpReq = new XMLHttpRequest();
       }
       // IE (versiones anteriores a la 8)
       else if (window.ActiveXObject) {
       	var versionesObj = new Array('Msxml2.XMLHTTP.6.0', 'Msxml2.XMLHTTP.5.0', 'Msxml2.XMLHTTP.4.0', 'Msxml2.XMLHTTP.3.0', 'Msxml2.XMLHTTP', 'Microsoft.XMLHTTP');
       	for (var i = 0; i < versionesObj.length; i++ ) {
       		try {
               	xmlHttpReq = new ActiveXObject(versionesObj[i]);
               } catch (e) {}
           }
       }
	return xmlHttpReq;
}

function cargarModulo (mod) {
	var peticion = nuevoAjax();
	var fichero=mod;
	if (peticion) {
		peticion.open('GET',fichero,true);
		peticion.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
		peticion.onreadystatechange=function() {
			if (peticion.readyState==4) {
				document.getElementById("mod").innerHTML=peticion.responseText;
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
					});
				}
				else if(mod == "anularCita.jsp") {
					obtenerCitas('ajaxObtenerCitas.jsp');
				}
			}
		}
		peticion.send(null);
	}
	
}