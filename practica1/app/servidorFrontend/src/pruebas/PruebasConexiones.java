package pruebas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import persistencia.AgenteFrontend;
import persistencia.ComandoSQL;
import persistencia.ComandoSQLSentencia;
import presentacion.JFServidorFrontend;
import comunicaciones.ConexionBDFrontend;
import comunicaciones.ConexionLogBD;
import comunicaciones.ConexionLogVentana;
import comunicaciones.GestorConexionesBD;
import comunicaciones.GestorConexionesLog;
import comunicaciones.ProxyServidorRespaldo;
import dominio.conocimiento.ITiposMensajeLog;
import junit.framework.TestCase;

/**
 * Pruebas de los Gestores de conexiones.
 */
public class PruebasConexiones extends TestCase {

	private ConexionBDFrontend conexionBD;
	private ConexionLogBD conexionLogBD;
	private ConexionLogVentana conexionLogVentana;
	private ProxyServidorRespaldo conexionRespaldo;
	
	protected void setUp() {
		JFServidorFrontend ventana;
		
		try {
			// Inicializamos las conexiones con las bases de datos
			// y las ventanas de estado de los servidores
			conexionBD = new ConexionBDFrontend();
			conexionBD.getAgente().setIP(IDatosPruebas.IP_BASEDATOS_PRINCIPAL);
			conexionBD.getAgente().setPuerto(IDatosPruebas.PUERTO_BASEDATOS_PRINCIPAL);
			conexionLogBD = new ConexionLogBD();
			conexionLogVentana = new ConexionLogVentana();
			ventana = new JFServidorFrontend(null);
			conexionLogVentana.ponerVentana(ventana);
			conexionRespaldo = new ProxyServidorRespaldo();
			conexionRespaldo.conectar(IDatosPruebas.IP_SERVIDOR_RESPALDO, IDatosPruebas.PUERTO_SERVIDOR_RESPALDO);
			// Abrimos las bases de datos
			conexionBD.abrir();
			conexionRespaldo.abrir();
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	protected void tearDown() {
		try {
			// Cerramos las bases de datos
			conexionBD.cerrar();
			conexionRespaldo.cerrar();
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	/** Pruebas de las conexiones con las bases de datos */
	public void testConexionBD() {
		ResultSet resultados;
		ComandoSQL comando;
		
		try {
			// Intentamos ejecutar un comando sin ninguna base de datos configurada
			GestorConexionesBD.consultar(new ComandoSQLSentencia("SELECT * FROM usuarios"));
			fail("Se esperaba una excepci�n SQLException");
		} catch(SQLException e) {
			assertEquals("La lista de conexiones est� vac�a.", e.getMessage());
		} catch(Exception e) {
			fail("Se esperaba una excepci�n SQLException");
		}

		try {
			// Intentamos ejecutar un comando sin ninguna base de datos configurada
			GestorConexionesBD.ejecutar(new ComandoSQLSentencia("DELETE FROM centros"));
			fail("Se esperaba una excepci�n SQLException");
		} catch(SQLException e) {
			assertEquals("La lista de conexiones est� vac�a.", e.getMessage());
		} catch(Exception e) {
			fail("Se esperaba una excepci�n SQLException");
		}

		try {
			// Configuramos y borramos la base de datos principal
			GestorConexionesBD.quitarConexiones();
			GestorConexionesBD.ponerConexion(conexionBD);
			borrarBaseDatos();
			// Ejecutamos varias sentencias SQL
			comando = new ComandoSQLSentencia("INSERT INTO beneficiarios (nif, nss, nombre, apellidos, fechaNacimiento, nifMedico, idCentro) VALUES (\"11223344O\", \"112233009988\", \"A\", \"B\", \"1980-01-01\", null, null)");
			GestorConexionesBD.ejecutar(comando);
			comando = new ComandoSQLSentencia("UPDATE beneficiarios SET apellidos = \"Nuevo\" WHERE nif = \"11223344O\"");
			GestorConexionesBD.ejecutar(comando);
			comando = new ComandoSQLSentencia("SELECT apellidos FROM beneficiarios WHERE nss = \"112233009988\"");
			resultados = GestorConexionesBD.consultar(comando);
			resultados.next();
			assertTrue(resultados.getString("apellidos").equals("Nuevo"));
		} catch(Exception e) {
			fail(e.toString());
		}

		try {
			// Configuramos y borramos la base de datos secundaria
			GestorConexionesBD.quitarConexiones();
			GestorConexionesBD.ponerConexion(conexionRespaldo);
			borrarBaseDatos();
			// Ejecutamos varias sentencias SQL para comprobar que
			// se tienen permisos para modificar la BD secundaria
			comando = new ComandoSQLSentencia("INSERT INTO beneficiarios (nif, nss, nombre, apellidos, fechaNacimiento, nifMedico, idCentro) VALUES (\"11223344O\", \"112233009988\", \"A\", \"B\", \"1980-01-01\", null, null)");
			GestorConexionesBD.ejecutar(comando);
			comando = new ComandoSQLSentencia("UPDATE beneficiarios SET apellidos = \"Nuevo\" WHERE nif = \"11223344O\"");
			GestorConexionesBD.ejecutar(comando);
			// (no se pueden hacer consultas porque la clase ResultSet
			// no es serializable y no se puede enviar por RMI)
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Configuramos y borramos las dos bases de datos
			GestorConexionesBD.quitarConexiones();
			GestorConexionesBD.ponerConexion(conexionBD);
			GestorConexionesBD.ponerConexion(conexionRespaldo);
			borrarBaseDatos();
			// Ejecutamos varias sentencias SQL sobre las dos bases de datos
			comando = new ComandoSQLSentencia("INSERT INTO usuarios (nif, login, password, rol, nombre, apellidos, idCentro) VALUES (\"88776655O\", \"login\", \"pass\", 0, \"C\", \"D\", null)");
			GestorConexionesBD.ejecutar(comando);
			comando = new ComandoSQLSentencia("UPDATE usuarios SET password = \"nuevapassword\" WHERE nif = \"88776655O\"");
			GestorConexionesBD.ejecutar(comando);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Vemos si los cambios se han aplicado en la BD principal
			GestorConexionesBD.quitarConexiones();
			GestorConexionesBD.ponerConexion(conexionBD);
			comando = new ComandoSQLSentencia("SELECT password, rol, apellidos FROM usuarios WHERE nif = \"88776655O\"");
			resultados = GestorConexionesBD.consultar(comando);
			resultados.next();
			assertTrue(resultados.getString("password").equals("nuevapassword"));
			assertTrue(resultados.getInt("rol") == 0);
			assertTrue(resultados.getString("apellidos").equals("D"));
		} catch(Exception e) {
			fail(e.toString());
		}

		try {
			// Vemos si los cambios se han aplicado en la BD secundaria
			// (como no se pueden hacer consultas, intentamos repetir una clave primaria)
			GestorConexionesBD.quitarConexiones();
			GestorConexionesBD.ponerConexion(conexionRespaldo);
			comando = new ComandoSQLSentencia("INSERT INTO usuarios (nif, login, password, rol, nombre, apellidos, idCentro) VALUES (\"88776655O\", \"aaa\", \"bbb\", 0, \"C\", \"D\", null)");
			GestorConexionesBD.ejecutar(comando);
			fail("Se esperaba una excepci�n SQLException");
		} catch(SQLException e) {
			assertEquals("Error en el acceso a las bases de datos.", e.getMessage());
		} catch(Exception e) {
			fail("Se esperaba una excepci�n SQLException");
		}
		
		try {
			// Desincronizamos las dos bases de datos
			GestorConexionesBD.quitarConexiones();
			GestorConexionesBD.ponerConexion(conexionRespaldo);
			comando = new ComandoSQLSentencia("INSERT INTO usuarios (nif, login, password, rol, nombre, apellidos, idCentro) VALUES (\"44004400Z\", \"otrologin\", \"otrapass\", 0, \"C\", \"D\", null)");
			GestorConexionesBD.ejecutar(comando);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Intentamos ejecutar una sentencia que funciona bien sobre
			// la primera base de datos pero falla en la segunda, porque
			// el login del usuario est� repetido
			GestorConexionesBD.quitarConexiones();
			GestorConexionesBD.ponerConexion(conexionBD);
			GestorConexionesBD.ponerConexion(conexionRespaldo);
			comando = new ComandoSQLSentencia("INSERT INTO usuarios (nif, login, password, rol, nombre, apellidos, idCentro) VALUES (\"33333333P\", \"otrologin\", \"otrapass\", 0, \"C\", \"D\", null)");
			GestorConexionesBD.ejecutar(comando);
			fail("Se esperaba una excepci�n SQLException");
		} catch(SQLException e) {
			assertEquals("Error en el acceso a las bases de datos.", e.getMessage());
		} catch(Exception e) {
			fail("Se esperaba una excepci�n SQLException");
		}
		
		try {
			// Comprobamos que la sentencia no se ha llegado a ejecutar
			// en la primera base de datos
			comando = new ComandoSQLSentencia("SELECT * FROM usuarios WHERE nif = \"33333333P\"");
			resultados = GestorConexionesBD.consultar(comando);
			assertFalse(resultados.next());
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	/** Pruebas de las conexiones para actualizar el estado del servidor */
	public void testConexionLog() {
		Connection bd;
		PreparedStatement sentencia;
		ResultSet resultados;
		
		try {
			// Configuramos el almacenamiento de los mensajes en la base de datos
			GestorConexionesLog.quitarConexiones();
			GestorConexionesLog.ponerConexion(conexionLogBD);
			// Comprobamos que ahora no hay ning�n mensaje en la BD
			bd = AgenteFrontend.getAgente().getConexion();
			sentencia = bd.prepareStatement("SELECT * FROM entradaslog");
			resultados = sentencia.executeQuery();
			assertFalse(resultados.next());
			// Generamos un nuevo mensaje
			GestorConexionesLog.ponerMensaje("prueba", ITiposMensajeLog.TIPO_INFO, "Mensaje de prueba");
			// Comprobamos que ahora hay un �nico mensaje en la BD
			sentencia = bd.prepareStatement("SELECT * FROM entradaslog");
			resultados = sentencia.executeQuery();
			assertTrue(resultados.next());
			assertEquals(resultados.getString("mensaje"), "Mensaje de prueba");
			assertFalse(resultados.next());
		} catch(Exception e) {
			fail(e.toString());
		}
		
		// TODO: conexion log en ventana y remota (que no se puede comprobar)
	}
	
	private void borrarBaseDatos() {
		ComandoSQL comando;
		
		try {
			comando = new ComandoSQLSentencia("DELETE FROM beneficiarios");
			GestorConexionesBD.ejecutar(comando);
			comando = new ComandoSQLSentencia("DELETE FROM centros");
			GestorConexionesBD.ejecutar(comando);
			comando = new ComandoSQLSentencia("DELETE FROM citas");
			GestorConexionesBD.ejecutar(comando);
			comando = new ComandoSQLSentencia("DELETE FROM direcciones");
			GestorConexionesBD.ejecutar(comando);
			comando = new ComandoSQLSentencia("DELETE FROM entradasLog");
			GestorConexionesBD.ejecutar(comando);
			comando = new ComandoSQLSentencia("DELETE FROM periodosTrabajo");
			GestorConexionesBD.ejecutar(comando);
			comando = new ComandoSQLSentencia("DELETE FROM sustituciones");
			GestorConexionesBD.ejecutar(comando);
			comando = new ComandoSQLSentencia("DELETE FROM tiposMedico");
			GestorConexionesBD.ejecutar(comando);
			comando = new ComandoSQLSentencia("DELETE FROM usuarios");
			GestorConexionesBD.ejecutar(comando);
			comando = new ComandoSQLSentencia("DELETE FROM volantes");
			GestorConexionesBD.ejecutar(comando);
		} catch(Exception e) {
			fail(e.toString());
		}
	}

}
