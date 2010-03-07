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
		suite.addTestSuite(PruebasPersistencia.class);
		suite.addTestSuite(PruebasSesiones.class);
		suite.addTestSuite(PruebasBeneficiarios.class);
		suite.addTestSuite(PruebasCitas.class);
		suite.addTestSuite(PruebasUsuarios.class);
		suite.addTestSuite(PruebasMedicos.class);
		suite.addTestSuite(PruebasSustituciones.class);
		suite.addTestSuite(PruebasVolantes.class);
		suite.addTestSuite(PruebasRemotoServidor.class);
		suite.addTestSuite(PruebasControlador.class);
		suite.addTestSuite(PruebasConexiones.class);
		suite.addTestSuite(PruebasValidacion.class);
		suite.addTestSuite(PruebasJFServidorFrontend.class);
		suite.addTestSuite(PruebasJFConfigFrontend.class);
		return suite;
	}
	
}
