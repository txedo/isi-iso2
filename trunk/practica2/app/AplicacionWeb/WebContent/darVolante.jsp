<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="dominio.conocimiento.Medico" %>
<%@ page errorPage= "error.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="excepciones.SesionNoIniciadaException"%>
<html>
<head>
	<%@ include file="resources/templates/header.htm" %>
	<script type="text/javascript" src="resources/scripts/ajax.js"></script>
	<title>SSCAWeb - Dar Volante</title>
	<script language="JavaScript" type="text/javascript"> 
		var peticion = null;
		peticion = nuevoAjax();	
		function cargarReceptor(url) {
			var select = document.getElementById("especialidad");
			var especialidad = select.options[select.selectedIndex].value;
			if (peticion) {
				document.getElementById("mensaje").innerHTML='';
				peticion.open('post',url,true);
				peticion.onreadystatechange=function() {
					if (peticion.readyState==4) {
						if (peticion.responseText.indexOf("Error")==-1){
							document.getElementById("especialistasCargados").innerHTML=peticion.responseText;
						} else {
							document.getElementById("mensaje").innerHTML=peticion.responseText;
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
						document.getElementById("mensaje").innerHTML=peticion.responseText;
					}	
				}
				var selectBene = document.getElementById("beneficiario");
				var bene = selectBene.options[selectBene.selectedIndex].value;
				var selReceptor = document.getElementById("especialista");
				var nifReceptor = selReceptor.options[selReceptor.selectedIndex].value;
				if (bene==null)
					alert("Seleccione un beneficiario");
				else if (nifReceptor=="-1") 
					alert("Seleccione una especialidad en la que exista algún especialista");
				else if (bene!=null && nifReceptor!="-1"){	
					peticion.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
					peticion.send("nifBeneficiario="+bene+"&nifReceptor="+nifReceptor);
					
				}
			}
			
		}
		
	</script>
</head>
<body onload="javascript:cargarReceptor('ajaxObtenerEspecialistas.jsp')"> 
	<%@ include file="resources/templates/top.htm" %>
	
	<%
		Medico med;
		if (request.getSession(false)==null)
			throw new SesionNoIniciadaException("No se puede acceder a una página interna si no se inicia sesión previamente");
		else {
			med = (Medico) request.getSession(false).getAttribute("Medico");
			if (med==null)
				throw new SesionNoIniciadaException("No se puede acceder a una página interna si no se inicia sesión previamente");
		}
	%>
	
		
	<div id="contenido">
    	<div class="textoCuerpo">
			Medico emisor: <input type="text" value="Dr./Dra. <%= med.getApellidos()%>" readonly="readonly"/>
			<!-- El "select" para mostrar la especialidad es un select normal donde se asociará al evento "onchange" la función de AJAX -->
			
			<br><br><s:select id="beneficiario" name="beneficiario" list="beneficiarios" listKey="nif" listValue="apellidos, nombre" label="Beneficiario"></s:select>
			<br>
			<s:select id="especialidad" name="especialidad" list="especialidades" label="Especialidad" onchange="cargarReceptor('ajaxObtenerEspecialistas.jsp')">
			</s:select>
			<span id="especialistasCargados">
			</span>
			<br><input type="button" onclick="darVolante('ajaxEmitirVolante.jsp')" value="Aceptar">
			<br>
			<span id="mensaje">
			</span>
		</div>
		<div class="volver">
			<input type="button" onclick="history.go(-1)" value="Volver atrás">
		</div>
	</div>
	
	<%@ include file="resources/templates/foot.htm" %>
</body>
</html>
