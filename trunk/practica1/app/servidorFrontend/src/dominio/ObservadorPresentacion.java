package dominio;

import java.util.ArrayList;
import java.util.Date;

import presentacion.IVentana;

public class ObservadorPresentacion {
	private ArrayList<IVentana> guis;
	
	public ObservadorPresentacion () {
		guis = new ArrayList<IVentana>();
	}
	
	public void add(IVentana ventana){
		guis.add(ventana);
	}

	public void actualizarVentanas(String mensaje) {
		String fecha = (new Date()).toString();
		for (IVentana v : guis) {
			v.actualizarTexto(fecha + ": " + mensaje + '\n');
		}
	}
}
