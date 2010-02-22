package pruebas;

import org.uispec4j.Button;
import org.uispec4j.Trigger;
import org.uispec4j.Window;
import org.uispec4j.interception.WindowHandler;
import org.uispec4j.interception.WindowInterceptor;

import excepciones.TituloIncorrectoException;

public class OperacionesAuxiliares {
	
	public static void interceptarDialogo (Button btnLanzador, final String labelBottonQueCierra) {
		WindowInterceptor.init(btnLanzador.triggerClick()).process(new WindowHandler() {
			public Trigger process (Window window) {
				return window.getButton(labelBottonQueCierra).triggerClick();
			}
		}).run();
	}
	
	public static void comprobarTituloDialogo (Button btnLanzador, final String labelBottonQueCierra, final String titulo) {
		WindowInterceptor.init(btnLanzador.triggerClick()).process(new WindowHandler() {
			public Trigger process (Window window) throws TituloIncorrectoException {
				if (!(window.getTitle()).equals(titulo)) {
					throw new TituloIncorrectoException();
				}
				return window.getButton(labelBottonQueCierra).triggerClick();
			}
		}).run();
	}
}
