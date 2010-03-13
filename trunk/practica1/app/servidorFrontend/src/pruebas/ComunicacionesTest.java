package pruebas;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ComunicacionesTest extends TestCase {

	public static Test suite() {
		TestSuite suite = new
		TestSuite("Tests para la capa de comunicación");
		suite.addTestSuite(PruebasRemotoServidor.class);
		suite.addTestSuite(PruebasConexiones.class);
		return suite;
	}
}
