package dominio;

import java.util.ArrayList;
import java.util.Date;

import presentacion.IVentanaLog;

public class ObservadorPresentacion {
	private ArrayList<IVentanaLog> guis;
	
	public ObservadorPresentacion () {
		guis = new ArrayList<IVentanaLog>();
	}
	
	public void add(IVentanaLog ventana){
		guis.add(ventana);
	}

	public void actualizarVentanas(String mensaje) {
		String fecha = (new Date()).toString();
		for (IVentanaLog v : guis) {
			v.actualizarTexto(fecha + ": " + mensaje);
		}
	}
}
