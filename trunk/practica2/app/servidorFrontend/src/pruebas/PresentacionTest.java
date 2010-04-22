package pruebas;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class PresentacionTest extends TestCase {

	public static Test suite() {
		TestSuite suite = new
		TestSuite("Tests para la capa de presentación");
		suite.addTestSuite(PruebasJFConfigFrontend.class);;
		suite.addTestSuite(PruebasJFServidorFrontend.class);
		suite.addTestSuite(PruebasValidacion.class);
		return suite;
	}
}
