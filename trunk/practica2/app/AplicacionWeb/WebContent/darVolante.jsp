<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="dominio.conocimiento.Medico" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<%@ include file="resources/templates/header.htm" %>
	<script type="text/javascript" src="resources/scripts/ajax.js"></script>
	<title>SSCAWeb - Dar Volante</title>
	<script language="JavaScript" type="text/javascript"> 
		var peticion = null;
		peticion = nuevoAjax();	
		function cargarReceptor(url, select) {
			var especialidad = select.options[select.selectedIndex].value;
			if (peticion) {
				peticion.open('post',url,true);
				peticion.onreadystatechange=function() {
					if (peticion.readyState==4) {
						document.getElementById("especialistasCargados").innerHTML=peticion.responseText;
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
						document.getElementById("volanteEmitido").innerHTML=peticion.responseText;
					}	
				}
				var selectBene = document.getElementById("beneficiario");
				var bene = selectBene.options[selectBene.selectedIndex].value;
				var selReceptor = document.getElementById("especialista");
				var nifReceptor = selReceptor.options[selReceptor.selectedIndex].value;
				if (bene!=null && nifReceptor!="-1") {	
					peticion.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
					peticion.send("nifBeneficiario="+bene+"&nifReceptor="+nifReceptor);
					
				}
			}
			
		}
		
	</script>
</head>
<body> 
	<%@ include file="resources/templates/top.htm" %>
	
	<%
		// En la sesion, se guarda el objeto que haya colocado la accion en la ValueStack,
		// para luego usarlo en las otras páginas para sacar el nombre del médico, por ejemplo.
		if (request.getSession(false) == null || request.getSession(false).getAttribute("Medico") == null) {
			session=request.getSession(true);
			session.setAttribute("Medico",request.getAttribute("medico"));
			session.setAttribute("SesionFrontend", request.getAttribute("sesion"));
		}
		Medico med = (Medico) request.getSession(false).getAttribute("Medico");
	%>
	
		
	<div id="contenido">
    	<div class="textoCuerpo">
			Medico emisor: <input type="text" value="Dr./Dra. <%= med.getApellidos()%>" readonly="readonly"/>
			<!-- El "select" para mostrar la especialidad es un select normal donde se asociará al evento "onchange" la función de AJAX -->
			
			<br><br><s:select id="beneficiario" name="beneficiario" list="beneficiarios" listKey="nif" listValue="apellidos, nombre" label="Beneficiario"></s:select>
			<br>
			<s:select name="especialidad" list="especialidades" label="Especialidad" onchange="cargarReceptor('ajaxObtenerEspecialistas.jsp',this)">
			</s:select>
			<span id="especialistasCargados">
			<br>	Dr./Dra.: <select id="especialista" name="especialista"><option value="-1">Seleccione especialidad...</option>
							  </select>
			</span>
			<br><input type="button" onclick="darVolante('ajaxEmitirVolante.jsp')" value="Aceptar">
			<span id="volanteEmitido">
			<br><br><br> Introduce los datos para pedir un volante ...
			</span>
		</div>
		<div class="volver">
			<input type="button" onclick="history.go(-1)" value="Volver atrás">
		</div>
	</div>
	
	<%@ include file="resources/templates/foot.htm" %>
</body>
</html>
