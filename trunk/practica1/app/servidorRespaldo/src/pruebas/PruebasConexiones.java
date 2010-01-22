package pruebas;

import java.sql.ResultSet;
import comunicaciones.ConexionBDRespaldo;
import comunicaciones.ConexionEstadoRespaldo;
import persistencia.ComandoSQL;
import persistencia.ComandoSQLSentencia;
import presentacion.JFServidorRespaldo;
import junit.framework.TestCase;

/**
 * Pruebas de las conexiones con la base de datos y la ventana de
 * estado del servidor de respaldo.
 */
public class PruebasConexiones extends TestCase {
	
	protected void setUp() {
		// No se necesita c�digo de inicializaci�n
	}
	
	protected void tearDown() {
		// No se necesita c�digo de finalizaci�n 
	}
	
	/** Pruebas de la conexi�n con la base de datos */
	public void testConexionBD() {
		ConexionBDRespaldo conexionBD = null;
		ComandoSQL comando;
		ResultSet resultado;
		
		try {
			// Activamos la conexi�n a la base de datos de respaldo
			// varias veces para ver si no hay fallos 
			conexionBD = ConexionBDRespaldo.getConexion();
			conexionBD.activar("127.0.0.1");
			conexionBD.activar("127.0.0.1");
			// Abrimos la base de datos
			conexionBD.getAgente().setIP("127.0.0.1");
			conexionBD.abrir();
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Realizamos una modificaci�n
			comando = new ComandoSQLSentencia("DELETE FROM centros");
			conexionBD.ejecutar(comando);
			conexionBD.commit();
			comando = new ComandoSQLSentencia("INSERT INTO centros (nombre, direccion) VALUES (?, ?)", "Centro de prueba", "C\\Ninguna S/N");
			conexionBD.ejecutar(comando);
			conexionBD.commit();
			// Realizamos una consulta para probar la modificaci�n
			comando = new ComandoSQLSentencia("SELECT direccion FROM centros WHERE nombre = ?", "Centro de prueba");
			resultado = conexionBD.consultar(comando);
			resultado.next();
			assertEquals(resultado.getString("direccion"), "C\\Ninguna S/N");
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Ejecutamos una modificaci�n y luego la deshacemos
			comando = new ComandoSQLSentencia("INSERT INTO centros (nombre, direccion) VALUES (?, ?)", "Centro no a�adido", "C\\Ninguna S/N");
			conexionBD.ejecutar(comando);
			conexionBD.rollback();
			// Comprobamos que no se ha ejecutado la inserci�n
			comando = new ComandoSQLSentencia("SELECT * FROM centros WHERE nombre = ?", "Centro no a�adido");
			resultado = conexionBD.consultar(comando);
			assertFalse(resultado.next());
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Cerramos la base de datos
			conexionBD.cerrar();
			// Desactivamos la conexi�n varias veces para ver si no hay fallos
			conexionBD.desactivar("127.0.0.1");
			conexionBD.desactivar("127.0.0.1");
		} catch(Exception e) {
			fail(e.toString());
		}
	}

	/** Pruebas de la conexi�n con la ventana de estado */
	public void testConexionEstado() {
		ConexionEstadoRespaldo conexionEstado = null;
		JFServidorRespaldo ventana = null;
		
		try {
			// Creamos la ventana de estado
			ventana = new JFServidorRespaldo();
			// Activamos la conexi�n a la ventana de estado varias
			// veces para ver si no hay fallos
			conexionEstado = ConexionEstadoRespaldo.getConexion();
			conexionEstado.activar("127.0.0.1");
			conexionEstado.activar("127.0.0.1");
			// A�adimos la ventana a la conexi�n
			conexionEstado.ponerVentana(ventana);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Actualizamos el estado de la ventana
			conexionEstado.ponerMensaje("Mensaje de prueba");
			conexionEstado.actualizarClientesEscuchando(2);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Desactivamos la conexi�n para ver si no hay fallos
			conexionEstado.desactivar("127.0.0.1");
			conexionEstado.desactivar("127.0.0.1");
			// Cerramos la ventana
			ventana.dispose();
		} catch(Exception e) {
			fail(e.toString());
		}
	}

}
