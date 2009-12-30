package dominio;

import java.util.ArrayList;
import java.util.Date;
import presentacion.IVentanaLog;

public class ObservadorPresentacion {
	
	private ArrayList<IVentanaLog> ventanas;
	
	public ObservadorPresentacion() {
		ventanas = new ArrayList<IVentanaLog>();
	}
	
	public void add(IVentanaLog ventana){
		ventanas.add(ventana);
	}

	public void actualizarVentanas(String mensaje) {
		String fecha;
		
		fecha = (new Date()).toString();
		for(IVentanaLog ventana : ventanas) {
			ventana.actualizarTexto(fecha + ": " + mensaje);
		}
	}
	
	public void actualizarVentanas(String login, String mensaje) {
		String fecha;
		
		fecha = (new Date()).toString();
		for(IVentanaLog ventana : ventanas) {
			ventana.actualizarTexto(fecha + " (" + login + "): " + mensaje);
		}
	}
	
}
