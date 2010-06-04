var peticion = null;
peticion = nuevoAjax();	
function cargarReceptor(url) {
	var select = document.getElementById("especialidad");
	var especialidad = select.options[select.selectedIndex].value;
	if (peticion) {
		peticion.open('post',url,true);
		peticion.onreadystatechange=function() {
			if (peticion.readyState==4) {
				if (peticion.responseText.indexOf("Error")==-1){
					document.getElementById("especialistasCargados").innerHTML=peticion.responseText;
				} else {
					document.getElementById("mod").innerHTML=peticion.responseText;
				}
			}						
		}
		peticion.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
		peticion.send("especialidad="+especialidad);
	}
}

function darVolante(url) {
	if (peticion) {
		peticion.open('post',url,true);
		peticion.onreadystatechange=function() {
			if (peticion.readyState==4) {
				document.getElementById("mod").innerHTML=peticion.responseText;
			}	
		}
		var selectBene = document.getElementById("beneficiario");
		var bene = selectBene.options[selectBene.selectedIndex].value;
		var selReceptor = document.getElementById("especialista");
		var nifReceptor = selReceptor.options[selReceptor.selectedIndex].value;
		if (bene==null || bene=="-1")
			alert("Seleccione un beneficiario.");
		else if (nifReceptor=="-1") 
			alert("Seleccione una especialidad en la que exista algún especialista.");
		else if (bene!=null && nifReceptor!="-1"){	
			peticion.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
			peticion.send("nifBeneficiario="+bene+"&nifReceptor="+nifReceptor);
			
		}
	}
	
}
