package pruebas;

import java.util.Random;

import presentacion.auxiliar.Validacion;

public class UtilidadesPruebas {
	
	public static String generarNIF() {
		Random r = new Random();
		String nif = "";
		String letra = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		
		// Generamos un NIF aleatorio
		for(int i = 0; i < Validacion.NIF_LONGITUD - 1; i++) {
			nif = nif + String.valueOf(r.nextInt(10));
		}
		nif = nif + letra.charAt(r.nextInt(letra.length()-1));
		
		return nif;
	}
	
	public static String generarNSS() {
		Random r = new Random();
		String nss = "";
		
		// Generamos un NSS aleatorio
		for(int i = 0; i < Validacion.NSS_LONGITUD; i++) {
			nss = nss + String.valueOf(r.nextInt(10));
		}
		
		return nss;
	}
}
