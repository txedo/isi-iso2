package pruebas;

import junit.framework.Test;
import junit.framework.TestSuite;

public class SuitePruebas {
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(SuitePruebas.suite());
	}

	public static Test suite() {
		TestSuite suite;
		
		suite = new TestSuite("Pruebas completas para el servidor frontend");
		suite.addTestSuite(PruebasPersistencia.class);
		suite.addTestSuite(PruebasSesiones.class);
		return suite;
	}
	
}
