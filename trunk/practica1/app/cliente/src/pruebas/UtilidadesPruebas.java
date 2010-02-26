package pruebas;

import java.util.Date;
import java.util.Random;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.uispec4j.Button;
import org.uispec4j.Trigger;
import org.uispec4j.Window;
import org.uispec4j.interception.WindowHandler;
import org.uispec4j.interception.WindowInterceptor;

import presentacion.auxiliar.Validacion;

public class UtilidadesPruebas {

	private static Random rnd = new Random(new Date().getTime());

	private static Window ventanaMostrada;
	
	public static String obtenerTextoDialogo (Button btn) {
		String mensaje = "";
		JOptionPane optionPane = null;
		JDialog dialogo = null;
		WindowInterceptor.init(btn.triggerClick()).process(
				new WindowHandler() {
					public Trigger process(Window window) {
						ventanaMostrada = window;
						return Trigger.DO_NOTHING;
					}
				}).run();
		// El JOptionPane está encapsulado dentro del dialogo (JDialog) que devuelve el WindowInterceptor
		dialogo = ((JDialog)ventanaMostrada.getAwtComponent()); 
		optionPane = (JOptionPane) dialogo.getContentPane().getComponents()[0];
		mensaje = optionPane.getMessage().toString();
		return mensaje;
	}
	
	public static String obtenerTextoDialogo (Button btn, final String btnQueCierra) {
		String mensaje = "";
		JOptionPane optionPane = null;
		JDialog dialogo = null;
		WindowInterceptor.init(btn.triggerClick()).process(
				new WindowHandler() {
					public Trigger process(Window window) {
						ventanaMostrada = window;
						return window.getButton(btnQueCierra).triggerClick();
					}
				}).run();
		// El JOptionPane está encapsulado dentro del dialogo (JDialog) que devuelve el WindowInterceptor
		dialogo = ((JDialog)ventanaMostrada.getAwtComponent()); 
		optionPane = (JOptionPane) dialogo.getContentPane().getComponents()[0];
		mensaje = optionPane.getMessage().toString();
		return mensaje;
	}
	
	public static String generarLogin() {
		return ("usuario" + rnd.nextInt(999999999));
	}			
	
	public static String generarNIF() {
		String nif = "";
		String letra = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		
		// Generamos un NIF aleatorio
		for(int i = 0; i < Validacion.NIF_LONGITUD - 1; i++) {
			nif = nif + String.valueOf(rnd.nextInt(10));
		}
		nif = nif + letra.charAt(rnd.nextInt(letra.length()-1));
		
		return nif;
	}
	
	public static String generarNSS() {
		String nss = "";
		
		// Generamos un NSS aleatorio
		for(int i = 0; i < Validacion.NSS_LONGITUD; i++) {
			nss = nss + String.valueOf(rnd.nextInt(10));
		}
		
		return nss;
	}
	
}
