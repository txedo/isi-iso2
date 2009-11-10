package pruebas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import dominio.Administrador;
import dominio.CentroSalud;
import dominio.Citador;
import dominio.EntradaLog;
import dominio.Medico;
import dominio.Usuario;
import excepciones.CentroSaludIncorrectoException;
import excepciones.UsuarioIncorrectoException;
import persistencia.AgenteLocal;
import persistencia.GestorConexiones;
import junit.framework.TestCase;

public class PruebasPersistencia extends TestCase {

	CentroSalud centro1, centro2;
	EntradaLog entrada1, entrada2, entrada3;
	Medico medico1, medico2;
	Citador citador1, citador2;
	Administrador administrador1;
	
	protected void setUp() {
		Connection bd;
		PreparedStatement sentencia;
		
		try {
			// Borramos la base de datos
			bd = AgenteLocal.getAgente().getConexion();
			sentencia = bd.prepareStatement("DELETE FROM centros");
			sentencia.executeUpdate();
			sentencia = bd.prepareStatement("DELETE FROM usuarios");
			sentencia.executeUpdate();
			sentencia = bd.prepareStatement("DELETE FROM tipoMedico");
			sentencia.executeUpdate();
			sentencia = bd.prepareStatement("DELETE FROM entradasLog");
			sentencia.executeUpdate();
			// Ponemos la conexión local con la base de datos
			GestorConexiones.ponerConexion(AgenteLocal.getAgente());
			// Creamos objetos de prueba
			centro1 = new CentroSalud(-1, "Centro A", "Calle Toledo, 44");
			centro2 = new CentroSalud(-1, "Centro B", null);
			entrada1 = new EntradaLog("juan", new Timestamp(109, 11, 1, 10, 10, 10, 0), "create", "Entrada de prueba 1.");
			entrada2 = new EntradaLog("luis", new Timestamp(109, 5, 25, 7, 30, 0, 0), "update", "Entrada de prueba 2.");
			entrada3 = new EntradaLog("mal", new Timestamp(109, 9, 10, 8, 0, 0, 0), "mal", "Entrada con errores.");
			medico1 = new Medico("12345678", "medPrueba", "abcdef", "Eduardo", "P. C.", centro1);
			medico2 = new Medico("12345678", "medYaExisto", "abcdef", "Eduardo", "P. C", centro1);
			citador1 = new Citador("1112223", "citador", "abcdef", "Luis", "E. G.", centro1);
			citador2 = new Citador("9998887", "citador", "abcdef", "Ana", "B. E.", centro1);
			administrador1 = new Administrador("12121212", "admin", "nimda", "Administrador", "", centro1);
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	protected void tearDown() {
		try {
			// Quitamos la conexión local con la base de datos
			GestorConexiones.quitarConexion(AgenteLocal.getAgente());
		} catch(SQLException e) {
			fail(e.toString());
		}
	}
	
	/** Pruebas de la tabla de centros de salud */
	public void testCentrosSalud() {
		CentroSalud centro;
		
		try {
			// Intentamos buscar un centro aleatorio sin haber uno
			centro = CentroSalud.consultarAleatorio();
			fail("Se esperaba una excepción CentroSaludIncorrectoException");
		} catch(SQLException e) {
			fail("Se esperaba una excepción CentroSaludIncorrectoException");
		} catch(CentroSaludIncorrectoException e) {
		}
		
		try {
			// Insertamos un nuevo centro
			centro1.insertar();
		} catch(SQLException e) {
			fail(e.toString());
		}
		
		try {
			// Comprobamos que el centro se ha añadido correctamente
			centro = CentroSalud.consultarAleatorio();
			assertEquals(centro1, centro);
		} catch(CentroSaludIncorrectoException e) {
			fail(e.toString());
		} catch(SQLException e) {
			fail(e.toString());
		}
		
		try {
			// Insertamos un nuevo centro con errores
			centro2.insertar();
			fail("Se esperaba una excepción SQLException");
		} catch(SQLException e) {
		}
		
		try {
			// Intentamos insertar el mismo centro para ver si falla
			// (no puede haber dos centros con el mismo nombre)
			centro1.insertar();
			fail("Se esperaba una excepción SQLException");
		} catch(SQLException e) {
		}

		try {
			// Insertamos un nuevo centro con un nombre no utilizado
			centro1.setNombre("Centro B");
			centro1.insertar();
		} catch(SQLException e) {
			fail(e.toString());
		}

		try {
			// Intentamos buscar un centro inexistente
			centro = CentroSalud.consultar(1000);
			fail("Se esperaba una excepción CentroSaludIncorrectoException");
		} catch(SQLException e) {
			fail("Se esperaba una excepción CentroSaludIncorrectoException");
		} catch(CentroSaludIncorrectoException e) {
		}
	}

	/** Pruebas de la tabla de entradas de log */
	public void testEntradasLog() {
		ArrayList<EntradaLog> log;
		
		try {
			// Leemos las entradas para ver si devuelve una lista vacía
			log = EntradaLog.consultarLog();
			assertTrue(log != null && log.size() == 0);
		} catch(SQLException e) {
			fail(e.toString());
		}
		
		try {
			// Insertamos nuevas entradas válidas (y repetidas, que se permite)
			entrada1.insertar();
			entrada2.insertar();
			entrada1.insertar();
		} catch(SQLException e) {
			fail(e.toString());
		}
		
		try {
			// Intentamos insertar una entrada con errores
			entrada3.insertar();
			fail("Se esperaba una excepción SQLException");
		} catch(SQLException e) {
		}

		try {
			// Comprobamos que las entradas se hayan añadido bien
			log = EntradaLog.consultarLog();
			assertTrue((log.get(0).equals(entrada1) && log.get(1).equals(entrada2)
			           || (log.get(0).equals(entrada2) && log.get(1).equals(entrada1))));
		} catch(SQLException e) {
			fail(e.toString());
		}
	}

	/** Pruebas de la tabla de usuarios */
	public void testUsuarios() {
		Usuario usuario;
		
		try {
			// Intentamos buscar un usuario sin haber ninguno
			usuario = Usuario.consultar("1234567");
			fail("Se esperaba una excepción UsuarioIncorrectoException");
		} catch(SQLException e) {
			fail("Se esperaba una excepción UsuarioIncorrectoException");
		} catch(CentroSaludIncorrectoException e) {
			fail("Se esperaba una excepción UsuarioIncorrectoException");
		} catch(UsuarioIncorrectoException e) {
		}
		
		try {
			// Añadimos el centro de salud asociado a los usuarios
			centro1.insertar();
			// Insertamos varios usuarios correctos
			medico1.insertar();
			citador1.insertar();
			administrador1.insertar();
		} catch(SQLException e) {
			fail(e.toString());
		}
		
		try {
			// Intentamos insertar un usuario con un DNI existente
			medico2.insertar();
			fail("Se esperaba una excepción SQLException");
		} catch(SQLException e) {
		}

		try {
			// Intentamos insertar un usuario con un login existente
			citador2.insertar();
			fail("Se esperaba una excepción SQLException");
		} catch(SQLException e) {
		}
		
		try {
			// Intentamos buscar un usuario que no existe
			usuario = Usuario.consultar("login", "password");
			fail("Se esperaba una excepción UsuarioIncorrectoException");
		} catch(SQLException e) {
			fail("Se esperaba una excepción UsuarioIncorrectoException");
		} catch(CentroSaludIncorrectoException e) {
			fail("Se esperaba una excepción UsuarioIncorrectoException");
		} catch(UsuarioIncorrectoException e) {
		}
		
		try {
			// Recuperamos los usuarios insertados de las dos formas posibles
			usuario = Usuario.consultar(medico1.getDni());
			assertEquals(medico1, usuario);
			usuario = Usuario.consultar(citador1.getDni());
			assertEquals(citador1, usuario);
			usuario = Usuario.consultar(administrador1.getDni());
			assertEquals(administrador1, usuario);
			usuario = Usuario.consultar(medico1.getLogin(), medico1.getPassword());
			assertEquals(medico1, usuario);
			usuario = Usuario.consultar(citador1.getLogin(), citador1.getPassword());
			assertEquals(citador1, usuario);
			usuario = Usuario.consultar(administrador1.getLogin(), administrador1.getPassword());
			assertEquals(administrador1, usuario);
		} catch(UsuarioIncorrectoException e) {
			fail(e.toString());
		} catch(CentroSaludIncorrectoException e) {
			fail(e.toString());
		} catch(SQLException e) {
			fail(e.toString());
		}
		
		try {
			// Modificamos un usuario
			citador1.setNombre("Ramón");
			citador1.setApellidos("P. V.");
			citador1.actualizar();
			// Comprobamos si los cambios han tenido efecto
			usuario = Usuario.consultar(citador1.getDni());
			assertEquals(citador1, usuario);
		} catch(UsuarioIncorrectoException e) {
			fail(e.toString());
		} catch(CentroSaludIncorrectoException e) {
			fail(e.toString());
		} catch(SQLException e) {
			fail(e.toString());
		}
		
		try {
			// Modificamos un usuario de forma incorrecta (login repetido)
			medico1.setLogin("admin");
			medico1.actualizar();
			fail("Se esperaba una excepción SQLException");
		} catch(SQLException e) {
		}
		
		try {
			// Eliminamos un usuario
			administrador1.eliminar();
			// Comprobamos si los cambios han tenido efecto
			usuario = Usuario.consultar(administrador1.getDni());
			fail("Se esperaba una excepción UsuarioIncorrectoException");
		} catch(CentroSaludIncorrectoException e) {
			fail("Se esperaba una excepción UsuarioIncorrectoException");
		} catch(SQLException e) {
			fail("Se esperaba una excepción UsuarioIncorrectoException");
		} catch(UsuarioIncorrectoException e) {
		}
	}
	
}
