package dominio.conocimiento;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encriptacion {
	
	public static String encriptarPasswordSHA1(String password) throws NoSuchAlgorithmException {
		MessageDigest clave;
		StringBuffer bufClave;
		byte[] bytesClave;
		int valor;
		int i;
		 
		// Generamos la clave encriptada con el algoritmo SHA-1
		clave = java.security.MessageDigest.getInstance("SHA-1");
		clave.reset();
		clave.update(password.getBytes());
		bytesClave = clave.digest();

		// Convertimos la clave en una cadena hexadecimal
		bufClave = new StringBuffer(bytesClave.length * 2);
		for(i = 0; i < bytesClave.length; i++) {
			valor = bytesClave[i] & 0xff;
			if(valor < 16) {
				bufClave.append('0');
			}
			bufClave.append(Integer.toHexString(valor));
		}

		return bufClave.toString();
	}	
	
}
