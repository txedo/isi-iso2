<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="dominio.conocimiento.Medico" %>
<%@page import="dominio.conocimiento.Especialidades"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.Vector"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Dar Volante</title>

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
		
		var nifReceptor = -1;
		function almacenarEspecialista(select) {
			nifReceptor = select.options[select.selectedIndex].value;
		}
		
		
		function cargarReceptor(url, select) {
			var especialidad = select.options[select.selectedIndex].value;
			var peticion = nuevoAjax();
			if (peticion) {
				peticion.open('post',url,true);
				peticion.onReadyStateChange=function() {
					if (peticion.readyState==4) {
						document.getElementById("especialista").innerHTML=peticion.responseText;
					}						
				}
				peticion.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
				peticion.send("especialidad="+especialidad);
			}
		}
		
		function darVolante(url) {
			var peticion = nuevoAjax();
			if (peticion) {
				peticion.open('post',url,true);
				peticion.onReadyStateChange=function() {
					if (peticion.readyState==4) {
						document.getElementById("volanteEmitido").innerHTML=peticion.responseText;
					}	
				}
				var selectBene = document.getElementById("beneficiario");
				bene = selectBene.options[selectBene.selectedIndex].value;
				if (bene!=null && nifReceptor!="-1") {	
					peticion.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
					peticion.send("nifBeneficiario="+bene+"&nifReceptor="+nifReceptor);
					
				}
			}
			
		}
		
	</script>

</head>
<body> 
	<%
		// En la sesion, se guarda el objeto que haya colocado la accion en la ValueStack,
		// para luego usarlo en las otras páginas para sacar el nombre del médico, por ejemplo.
		if (session!=null) {
			session=request.getSession(true);
			session.setAttribute("Medico",request.getAttribute("medico"));
			session.setAttribute("SesionFrontend", request.getAttribute("sesion"));
		}
		Medico med = (Medico) request.getSession(false).getAttribute("Medico");
	%>
	
		
	
	Medico emisor: <input type="text" value="Dr./Dra. <%= med.getApellidos()%>" readonly="readonly"/>
	<!-- El "select" para mostrar la especialidad es un select normal donde se asociará al evento "onchange" la función de AJAX -->
	
	<br><br><s:select id="beneficiario" name="beneficiario" list="beneficiarios" listKey="nif" listValue="apellidos, nombre" label="Beneficiario"></s:select>
	<br>
	<s:select name="especialidad" list="especialidades" label="Especialidad" onchange="cargarReceptor('obtenerEspecialistas.jsp',this)">
	</s:select>
	<span id="especialista">
	<br>	Dr./Dra.: <select name="especialista" onchange="almacenarEspecialista(this)"><option value="-1">Seleccione especialidad...</option>
		<select>
	</span>
	<br><input type="button" onclick="darVolante('emitirVolante.jsp')" value="Aceptar">
	<span id="volanteEmitido">
	<br><br><br> Introduce los datos para pedir un volante ...
	</span>
</body>
</html>