<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page isErrorPage="true" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<%@ include file="resources/templates/header.htm" %>
	<title>SSCAWeb - Error</title>
</head>
<body>
	<%@ include file="resources/templates/top.htm" %>
	
	<div id="contenido">
    	<div class="textoCuerpo">
    	
    		<!-- Excepci�n provocada en una acci�n de struts -->
			<s:property value="exception.message"/> <br>
			
			<!-- Excepciones lanzadas al aaceder a una p�gina interna sin iniciar sesi�n (provocadas en paginas JSP) -->
			<% 
			// Si el objeto exception es nulo, es poruqe se trataba de un error en una acci�n de struts, por lo que no se hace nada m�s
			if (exception != null) {
				if (exception.getClass().getSimpleName().equals("NullPointerException")) { %>
					No se puede acceder a una p&aacute;gina interna si no se inicia sesi&oacute;n previamente
			 <% } else { %>
					<%=exception.getMessage() %>
			<%  } 
			}
			%>
			<button onclick="javascript:history.go(-1)">Atr&aacute;s</button>
			
		</div>
	</div>
	
	<%@ include file="resources/templates/foot.htm" %>
</body>
</html>
