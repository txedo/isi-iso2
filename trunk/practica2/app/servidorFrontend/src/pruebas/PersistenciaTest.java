package pruebas;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class PersistenciaTest extends TestCase {

	public static Test suite() {
		TestSuite suite = new
		TestSuite("Tests para la capa de persistencia");
		suite.addTestSuite(PruebasPersistencia.class);
		return suite;
	}
}
