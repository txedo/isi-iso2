package pruebas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import comunicaciones.ConexionBDFrontend;
import comunicaciones.GestorConexionesBD;
import dominio.Administrador;
import dominio.CentroSalud;
import dominio.Citador;
import dominio.DiaSemana;
import dominio.EntradaLog;
import dominio.Medico;
import dominio.PeriodoTrabajo;
import dominio.Usuario;
import excepciones.CentroSaludIncorrectoException;
import excepciones.UsuarioIncorrectoException;
import persistencia.AgenteFrontend;
import persistencia.FPCentroSalud;
import persistencia.FPEntradaLog;
import persistencia.FPPeriodoTrabajo;
import persistencia.FPUsuario;
import junit.framework.TestCase;

public class PruebasPersistencia extends TestCase {

	private CentroSalud centro1, centro2, centro3;
	private EntradaLog entrada1, entrada2, entrada3;
	private Medico medico1, medico2;
	private Citador citador1, citador2;
	private Administrador administrador1;
	private PeriodoTrabajo periodo1, periodo2;
	private ConexionBDFrontend conexionF;
	
	protected void setUp() {
		Connection bd;
		PreparedStatement sentencia;
		
		try {
			// Borramos la base de datos
			bd = AgenteFrontend.getAgente().getConexion();
			sentencia = bd.prepareStatement("DELETE FROM tiposMedico");
			sentencia.executeUpdate();
			sentencia = bd.prepareStatement("DELETE FROM periodosTrabajo");
			sentencia.executeUpdate();
			sentencia = bd.prepareStatement("DELETE FROM usuarios");
			sentencia.executeUpdate();
			sentencia = bd.prepareStatement("DELETE FROM entradasLog");
			sentencia.executeUpdate();
			sentencia = bd.prepareStatement("DELETE FROM citas");
			sentencia.executeUpdate();
			sentencia = bd.prepareStatement("DELETE FROM beneficiarios");
			sentencia.executeUpdate();
			sentencia = bd.prepareStatement("DELETE FROM centros");
			sentencia.executeUpdate();
			// Ponemos la conexi�n local con la base de datos
			conexionF = new ConexionBDFrontend();
			GestorConexionesBD.ponerConexion(conexionF);
			// Creamos objetos de prueba
			centro1 = new CentroSalud("Centro A", "Calle Toledo, 44");
			centro2 = new CentroSalud("Centro B", null);
			centro3 = new CentroSalud("Centro C", "Otra calle");
			entrada1 = new EntradaLog("juan", new Timestamp(109, 11, 1, 10, 10, 10, 0), "create", "Entrada de prueba 1.");
			entrada2 = new EntradaLog("luis", new Timestamp(109, 5, 25, 7, 30, 0, 0), "update", "Entrada de prueba 2.");
			entrada3 = new EntradaLog("mal", new Timestamp(109, 9, 10, 8, 0, 0, 0), "mal", "Entrada con errores.");
			medico1 = new Medico("12345678", "medPrueba", "abcdef", "Eduardo", "P. C.", centro1);
			medico2 = new Medico("12345678", "medYaExisto", "abcdef", "Eduardo", "P. C", centro1);
			citador1 = new Citador("1112223", "citador", "abcdef", "Luis", "E. G.", centro3);
			citador2 = new Citador("9998887", "citador", "abcdef", "Ana", "B. E.", centro1);
			administrador1 = new Administrador("12121212", "admin", "nimda", "Administrador", "", centro1);
			periodo1 = new PeriodoTrabajo(10, 12, DiaSemana.Lunes);
			periodo2 = new PeriodoTrabajo(16, 20, DiaSemana.Jueves);
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	protected void tearDown() {
		// Quitamos la conexi�n local con la base de datos
		GestorConexionesBD.quitarConexiones();
	}
	
	/** Pruebas de la tabla de centros de salud */
	public void testCentrosSalud() {
		CentroSalud centro;
		
		try {
			// Intentamos buscar un centro aleatorio sin haber uno
			centro = FPCentroSalud.consultarAleatorio();
			fail("Se esperaba una excepci�n CentroSaludIncorrectoException");
		} catch(CentroSaludIncorrectoException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n CentroSaludIncorrectoException");
		}
		
		try {
			// Insertamos varios centros correctos
			FPCentroSalud.insertar(centro1);
			FPCentroSalud.insertar(centro3);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Comprobamos que los centros se han a�adido correctamente
			centro = FPCentroSalud.consultarAleatorio();
			assertTrue(centro1.equals(centro) || centro3.equals(centro));
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Insertamos un nuevo centro con errores
			FPCentroSalud.insertar(centro2);
			fail("Se esperaba una excepci�n SQLException");
		} catch(SQLException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n SQLException");
		}
		
		try {
			// Intentamos insertar el mismo centro para ver si falla
			// (no puede haber dos centros con el mismo nombre)
			FPCentroSalud.insertar(centro1);
			fail("Se esperaba una excepci�n SQLException");
		} catch(SQLException e) {
		} catch(Exception e) {
			fail(e.toString());
		}

		try {
			// Intentamos buscar un centro inexistente
			centro = FPCentroSalud.consultar(1000);
			fail("Se esperaba una excepci�n CentroSaludIncorrectoException");
		} catch(CentroSaludIncorrectoException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n CentroSaludIncorrectoException");
		}
	}

	/** Pruebas de la tabla de entradas de log */
	public void testEntradasLog() {
		ArrayList<EntradaLog> log;
		
		try {
			// Leemos las entradas para ver si devuelve una lista vac�a
			log = FPEntradaLog.consultarLog();
			assertTrue(log != null && log.size() == 0);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Insertamos nuevas entradas v�lidas (y repetidas, que se permite)
			FPEntradaLog.insertar(entrada1);
			FPEntradaLog.insertar(entrada2);
			FPEntradaLog.insertar(entrada1);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Intentamos insertar una entrada con errores
			FPEntradaLog.insertar(entrada3);
			fail("Se esperaba una excepci�n SQLException");
		} catch(SQLException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n SQLException");
		}

		try {
			// Comprobamos que las entradas se hayan a�adido bien
			log = FPEntradaLog.consultarLog();
			assertTrue((log.get(0).equals(entrada1) && log.get(1).equals(entrada2)
			           || (log.get(0).equals(entrada2) && log.get(1).equals(entrada1))));
		} catch(Exception e) {
			fail(e.toString());
		}
	}

	/** Pruebas de la tabla de usuarios */
	public void testUsuarios() {
		Usuario usuario;
		
		try {
			// Intentamos buscar un usuario sin haber ninguno
			usuario = FPUsuario.consultar("1234567");
			fail("Se esperaba una excepci�n UsuarioIncorrectoException");
		} catch(UsuarioIncorrectoException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n UsuarioIncorrectoException");
		}
		
		try {
			// A�adimos los centros de salud asociados a los usuarios
			FPCentroSalud.insertar(centro1);
			FPCentroSalud.insertar(centro3);
			// Insertamos varios usuarios correctos
			FPUsuario.insertar(medico1);
			FPUsuario.insertar(citador1);
			FPUsuario.insertar(administrador1);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Intentamos insertar un usuario con un DNI existente
			FPUsuario.insertar(medico2);
			fail("Se esperaba una excepci�n SQLException");
		} catch(SQLException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n SQLException");
		}

		try {
			// Intentamos insertar un usuario con un login existente
			FPUsuario.insertar(citador2);
			fail("Se esperaba una excepci�n SQLException");
		} catch(SQLException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n SQLException");
		}
		
		try {
			// Intentamos buscar un usuario que no existe
			usuario = FPUsuario.consultar("login", "password");
			fail("Se esperaba una excepci�n UsuarioIncorrectoException");
		} catch(UsuarioIncorrectoException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n UsuarioIncorrectoException");
		}
		
		try {
			// Recuperamos los usuarios insertados de las dos formas posibles
			usuario = FPUsuario.consultar(medico1.getDni());
			assertEquals(medico1, usuario);
			usuario = FPUsuario.consultar(citador1.getDni());
			assertEquals(citador1, usuario);
			usuario = FPUsuario.consultar(administrador1.getDni());
			assertEquals(administrador1, usuario);
			usuario = FPUsuario.consultar(medico1.getLogin(), medico1.getPassword());
			assertEquals(medico1, usuario);
			usuario = FPUsuario.consultar(citador1.getLogin(), citador1.getPassword());
			assertEquals(citador1, usuario);
			usuario = FPUsuario.consultar(administrador1.getLogin(), administrador1.getPassword());
			assertEquals(administrador1, usuario);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Modificamos un usuario
			citador1.setNombre("Ram�n");
			citador1.setApellidos("P. V.");
			FPUsuario.modificar(citador1);
			// Comprobamos si los cambios han tenido efecto
			usuario = FPUsuario.consultar(citador1.getDni());
			assertEquals(citador1, usuario);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Modificamos un usuario de forma incorrecta (login repetido)
			medico1.setLogin("admin");
			FPUsuario.modificar(medico1);
			fail("Se esperaba una excepci�n SQLException");
		} catch(SQLException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n SQLException");
		}
		
		try {
			// Eliminamos un usuario
			FPUsuario.eliminar(administrador1);
			// Comprobamos si los cambios han tenido efecto
			usuario = FPUsuario.consultar(administrador1.getDni());
			fail("Se esperaba una excepci�n UsuarioIncorrectoException");
		} catch(UsuarioIncorrectoException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n UsuarioIncorrectoException");
		}
	}
	
	/** Pruebas de la tabla de per�odos de trabajo */
	public void testPeriodosTrabajo() {
		ArrayList<PeriodoTrabajo> periodos;
			
		try {
			// A�adimos un m�dico a la base de datos
			FPCentroSalud.insertar(centro1);
			FPUsuario.insertar(medico1);
			// Insertamos varios per�odos de trabajo correctos
			FPPeriodoTrabajo.insertar(medico1.getDni(), periodo1);
			FPPeriodoTrabajo.insertar(medico1.getDni(), periodo2);
		} catch(Exception e) {
			fail(e.toString());
		}

		try {
			// Modificamos un per�odo de trabajo existente
			periodo1.setHoraInicio(9);
			periodo1.setHoraFinal(11);
			FPPeriodoTrabajo.modificar(periodo1);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Recuperamos los per�odos de trabajo almacenados
			periodos = FPPeriodoTrabajo.consultarCalendario(medico1.getDni());
			assertTrue((periodos.get(0).equals(periodo1) && periodos.get(1).equals(periodo2)
			           || (periodos.get(0).equals(periodo2) && periodos.get(1).equals(periodo1))));
			// Comprobamos si la modificaci�n tuvo efecto
			assertTrue(periodos.get(0).getHoraFinal() == 11 || periodos.get(1).getHoraFinal() == 11);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Eliminamos los per�odos de trabajo
			FPPeriodoTrabajo.eliminar(periodo1);
			FPPeriodoTrabajo.eliminar(periodo2);
			// Comprobamos si los cambios han tenido efecto
			periodos = FPPeriodoTrabajo.consultarCalendario(medico1.getDni());
			assertTrue(periodos.size() == 0);
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
}