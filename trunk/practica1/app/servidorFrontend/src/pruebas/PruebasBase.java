package pruebas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import persistencia.AgenteFrontend;
import comunicaciones.ConexionBDFrontend;
import comunicaciones.GestorConexionesBD;
import junit.framework.TestCase;

/**
 * Clase base para las pruebas, que prepara y abre la conexión con la
 * base de datos al iniciar las pruebas y la cierra al finalizar.
 */
public class PruebasBase extends TestCase {
	
	protected void setUp() {
		Connection bd;
		PreparedStatement sentencia;
		ConexionBDFrontend conexion;
		AgenteFrontend agente;
		
		try {
			// Borramos la base de datos
			agente = AgenteFrontend.getAgente();
			agente.setIP(IDatosPruebas.IP_BASEDATOS_PRINCIPAL);
			agente.setPuerto(IDatosPruebas.PUERTO_BASEDATOS_PRINCIPAL);
			agente.abrir();
			bd = agente.getConexion();
			sentencia = bd.prepareStatement("DELETE FROM beneficiarios");
			sentencia.executeUpdate();
			sentencia = bd.prepareStatement("DELETE FROM centros");
			sentencia.executeUpdate();
			sentencia = bd.prepareStatement("DELETE FROM citas");
			sentencia.executeUpdate();
			sentencia = bd.prepareStatement("DELETE FROM direcciones");
			sentencia.executeUpdate();
			sentencia = bd.prepareStatement("DELETE FROM entradasLog");
			sentencia.executeUpdate();
			sentencia = bd.prepareStatement("DELETE FROM periodosTrabajo");
			sentencia.executeUpdate();
			sentencia = bd.prepareStatement("DELETE FROM sustituciones");
			sentencia.executeUpdate();
			sentencia = bd.prepareStatement("DELETE FROM tiposMedico");
			sentencia.executeUpdate();
			sentencia = bd.prepareStatement("DELETE FROM usuarios");
			sentencia.executeUpdate();
			sentencia = bd.prepareStatement("DELETE FROM volantes");
			sentencia.executeUpdate();
			// Ponemos la conexión local con la base de datos
			conexion = new ConexionBDFrontend();
			GestorConexionesBD.ponerConexion(conexion);
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	protected void tearDown() {
		try {
			// Cerramos la conexión local con la base de datos
			GestorConexionesBD.cerrarConexiones();
			GestorConexionesBD.quitarConexiones();
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
}
