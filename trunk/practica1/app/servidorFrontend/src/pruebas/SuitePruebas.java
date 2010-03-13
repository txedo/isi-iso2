package pruebas;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Suite de pruebas para el servidor front-end.
 */
public class SuitePruebas {
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(SuitePruebas.suite());
	}

	public static Test suite() {
		TestSuite suite;
		
		suite = new TestSuite("Pruebas completas para el servidor frontend");
		suite.addTest(DominioTest.suite());
		suite.addTest(PersistenciaTest.suite());
		suite.addTest(ComunicacionesTest.suite());
		suite.addTest(PresentacionTest.suite());
		return suite;
	}
	
}
