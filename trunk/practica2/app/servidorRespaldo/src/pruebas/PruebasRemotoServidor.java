package pruebas;

import java.sql.ResultSet;
import comunicaciones.RemotoServidorRespaldo;
import dominio.conocimiento.ITiposMensajeLog;
import dominio.control.ServidorRespaldo;
import persistencia.ComandoSQL;
import persistencia.ComandoSQLSentencia;
import presentacion.JFServidorRespaldo;
import junit.framework.TestCase;

/**
 * Pruebas del objeto remoto exportado por el servidor de respaldo para
 * conectarse con la base de datos y la ventana de estado.
 */
public class PruebasRemotoServidor extends TestCase {
	
	private RemotoServidorRespaldo conexion;
	private final int PUERTO_CONEXION = 1098;
	
	public void setUp() {
		try {
			// Creamos el objeto remoto exportado por el servidor de respaldo
			conexion = RemotoServidorRespaldo.getServidor();		
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	public void tearDown() {
		// No se necesita c�digo de finalizaci�n 
	}
	
	/** Pruebas de conexi�n y desconexi�n */
	public void testConectarDesconectar() {
		try {
			// Activamos la conexi�n varias veces para ver si no hay fallos
			conexion.activar("127.0.0.1", PUERTO_CONEXION);
			conexion.activar("127.0.0.1", PUERTO_CONEXION);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Desactivamos la conexi�n varias veces para ver si no hay fallos
			conexion.desactivar("127.0.0.1", PUERTO_CONEXION);
			conexion.desactivar("127.0.0.1", PUERTO_CONEXION);
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	/** Pruebas de la conexi�n con la base de datos */
	public void testConexionBD() {
		ComandoSQL comando;
		ResultSet resultado;
		
		try {
			// Configuramos la base de datos
			ServidorRespaldo.getServidor().getConexionBD().getAgente().setIP("127.0.0.1");
			ServidorRespaldo.getServidor().getConexionBD().getAgente().setPuerto(3306);
			// Activamos la conexi�n y abrimos la base de datos
			conexion.activar("127.0.0.1", PUERTO_CONEXION);
			conexion.abrir();
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Realizamos una modificaci�n
			comando = new ComandoSQLSentencia("DELETE FROM centros");
			conexion.ejecutar(comando);
			conexion.commit();
			comando = new ComandoSQLSentencia("INSERT INTO centros (nombre, direccion) VALUES (?, ?)", "Centro de prueba", "C\\Ninguna S/N");
			conexion.ejecutar(comando);
			conexion.commit();
			// Realizamos una consulta para probar la modificaci�n
			comando = new ComandoSQLSentencia("SELECT direccion FROM centros WHERE nombre = ?", "Centro de prueba");
			resultado = conexion.consultar(comando);
			resultado.next();
			assertEquals(resultado.getString("direccion"), "C\\Ninguna S/N");
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Ejecutamos una modificaci�n y luego la deshacemos
			comando = new ComandoSQLSentencia("INSERT INTO centros (nombre, direccion) VALUES (?, ?)", "Centro no a�adido", "C\\Ninguna S/N");
			conexion.ejecutar(comando);
			conexion.rollback();
			// Comprobamos que no se ha ejecutado la inserci�n
			comando = new ComandoSQLSentencia("SELECT * FROM centros WHERE nombre = ?", "Centro no a�adido");
			resultado = conexion.consultar(comando);
			assertFalse(resultado.next());
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Desactivamos la conexi�n y cerramos la base de datos
			conexion.cerrar();
			conexion.desactivar("127.0.0.1", PUERTO_CONEXION);
		} catch(Exception e) {
			fail(e.toString());
		}
	}

	/** Pruebas de la conexi�n con la ventana de estado */
	public void testConexionLog() {
		JFServidorRespaldo ventana = null;
		
		try {
			// Configuramos la ventana de estado
			ventana = new JFServidorRespaldo(null);
			ServidorRespaldo.getServidor().getConexionEstado().ponerVentana(ventana);
			// Activamos la conexi�n
			conexion.activar("127.0.0.1", PUERTO_CONEXION);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Actualizamos el estado de la ventana
			conexion.ponerMensaje("user", ITiposMensajeLog.TIPO_INFO, "Mensaje de prueba");
			assertEquals(ventana.getMensajes().substring(ventana.getMensajes().lastIndexOf(':')), ": Mensaje de prueba\n");
			conexion.ponerMensaje(ITiposMensajeLog.TIPO_INFO, "Otro mensaje de prueba");
			assertEquals(ventana.getMensajes().substring(ventana.getMensajes().lastIndexOf(':')), ": Otro mensaje de prueba\n");
			conexion.actualizarClientesEscuchando(2);
			assertEquals(ventana.getClientesEscuchando(), 2);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Desactivamos la conexi�n y cerramos la ventana
			conexion.desactivar("127.0.0.1", PUERTO_CONEXION);
			ventana.dispose();
		} catch(Exception e) {
			fail(e.toString());
		}
	}

}
