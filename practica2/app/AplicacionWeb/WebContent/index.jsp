<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<%@ include file="header.jsp"%>
	<title>SSCA-Web</title>
</head>
<body>
	<%@ include file="top.jsp"%>
	
    <div id="contenido">
        <div class="imagenesLogin">
            <div class="textoCuerpo" style="text-align:center;">
                Seleccione su rol:
            </div>
            <table width="300px" cellspacing="1" cellpadding="60" border="0" align="center">
                    <tbody>
                        <tr>
                            <td align="center">
                            	<a href="loginMedico.jsp"><img src="resources/images/doctor.png" alt="Seleccione el rol M&eacute;dico" />M�dico</a>
                            </td>
                            <td align="center">
                            	<a href="loginBeneficiario.jsp"><img src="resources/images/patient.png" alt="Seleccoine el rol Beneficiario" />Beneficiario</a>
                            </td>
	                    </tr>
                    </tbody>  
            </table>
        </div>
    </div>
    
	<%@ include file="foot.jsp"%>
</html>