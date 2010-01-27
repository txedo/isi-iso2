package pruebas;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Suite de pruebas para el servidor de respaldo.
 */
public class SuitePruebas {
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(SuitePruebas.suite());
	}

	public static Test suite() {
		TestSuite suite;
		
		suite = new TestSuite("Pruebas completas para el servidor de respaldo");
		suite.addTestSuite(PruebasConexiones.class);
		suite.addTestSuite(PruebasControlador.class);
		suite.addTestSuite(PruebasJFServidorRespaldo.class);
		suite.addTestSuite(PruebasJFConfiguracion.class);
		return suite;
	}
	
}
