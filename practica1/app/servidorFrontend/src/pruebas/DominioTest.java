package pruebas;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class DominioTest extends TestCase {
	
	public static Test suite() {
		TestSuite suite = new
		TestSuite("Tests para la capa de dominio");
		suite.addTestSuite(PruebasBeneficiarios.class);
		suite.addTestSuite(PruebasCitas.class);
		suite.addTestSuite(PruebasMedicos.class);
		suite.addTestSuite(PruebasSesiones.class);
		suite.addTestSuite(PruebasSustituciones.class);
		suite.addTestSuite(PruebasUsuarios.class);
		suite.addTestSuite(PruebasVolantes.class);
		suite.addTestSuite(PruebasControlador.class); 
		return suite;
	}

}
