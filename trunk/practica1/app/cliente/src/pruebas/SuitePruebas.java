package pruebas;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Suite de pruebas para el cliente.
 */
public class SuitePruebas {
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(SuitePruebas.suite());
	}

	public static Test suite() {
		TestSuite suite;
		
		suite = new TestSuite("Pruebas completas para el cliente");
		suite.addTestSuite(PruebasValidacion.class);
		suite.addTestSuite(PruebasJFLogin.class);
		suite.addTestSuite(PruebasJPBeneficiarioRegistrar.class);
		suite.addTestSuite(PruebasJPBeneficiarioConsultar.class);
		suite.addTestSuite(PruebasJPUsuarioRegistrar.class);
		suite.addTestSuite(PruebasJPUsuarioConsultar.class);
		suite.addTestSuite(PruebasJPCitaTramitar.class);
		suite.addTestSuite(PruebasJPEmitirVolante.class);
		suite.addTestSuite(PruebasJPCitaConsultarBeneficiario.class);
		suite.addTestSuite(PruebasJPCitaConsultarMedico.class);
		suite.addTestSuite(PruebasJPCitaConsultarPropias.class);
		return suite;
	}
	
}
