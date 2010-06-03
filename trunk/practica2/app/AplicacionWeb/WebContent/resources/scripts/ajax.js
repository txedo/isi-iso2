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
