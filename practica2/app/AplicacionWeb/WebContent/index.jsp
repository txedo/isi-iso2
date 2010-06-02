<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<%@ include file="resources/templates/header.htm" %>
	<title>SSCAWeb - Inicio</title>
	<script type="text/javascript">
		function setLocation(url) {
			document.location = url;
		}
	</script>
</head>
<body>	
	<%
		String paginaMenu;
	
		// Comprobamos si ya hay un cliente (beneficiario o médico) que ha
		// iniciado sesión, y en ese caso le redirigimos a su menú principal
		paginaMenu = "";
		if(request.getSession(false) != null) {
			if(request.getSession(false).getAttribute("Beneficiario") != null) {
				paginaMenu = "menuBeneficiario.jsp";
			} else if(request.getSession(false).getAttribute("Medico") != null) {
				paginaMenu = "menuMedico.jsp";
			}
		}
	%>
	
	<%
		if(paginaMenu.equals("")) {
	%>
	<%@ include file="resources/templates/top.htm" %>
	
		<ul class="menu">
			<div class="navegacion">Inicio</div>
		</ul>

			<!-- Menú de selección de rol -->
		    <div id="contenido">
		            <div class="textoCuerpo" style="text-align:center;">		         
		            	<div style="text-align:left;margin-top:30px;margin-left:50px;">Bienvenido/a a la p&aacute;gina Web del SSCA.<br /><br />Seleccione su rol:</div>
		           		<div class="imagenesLogin">
			            	<table width="300px" cellspacing="1" cellpadding="25" border="0" align="left">
		                        <tr>
		                            <td align="center">
		                            	<a href="loginMedico.jsp"><img src="resources/images/doctor.png" alt="Rol M&eacute;dico" /></a>
		                            	<a href="loginMedico.jsp">Médico</a>
		                            </td>
		                            <td align="center">
		                            	<a href="loginBeneficiario.jsp"><img src="resources/images/patient.png" alt="Rol Beneficiario" /></a>
		                            	<a href="loginBeneficiario.jsp">Beneficiario</a>
		                            </td>
			                    </tr>
			            	</table>
		            </div>
		        </div>
		    </div>

	<%
		} else {
	%>

			<!-- Carga del menú principal -->
			<script type="text/javascript">
				setLocation('<%= paginaMenu %>');
			</script>

	<%
		} // Fin if(redirigir.equals(""))
	%>
    
	<%@ include file="resources/templates/foot.htm" %>
</body>
</html>
