package pruebas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import comunicaciones.ConexionBDFrontend;
import comunicaciones.GestorConexionesBD;
import dominio.conocimiento.Administrador;
import dominio.conocimiento.Cabecera;
import dominio.conocimiento.CentroSalud;
import dominio.conocimiento.Citador;
import dominio.conocimiento.DiaSemana;
import dominio.conocimiento.EntradaLog;
import dominio.conocimiento.Medico;
import dominio.conocimiento.Pediatra;
import dominio.conocimiento.PeriodoTrabajo;
import dominio.conocimiento.Sustitucion;
import dominio.conocimiento.Usuario;
import excepciones.CentroSaludIncorrectoException;
import excepciones.UsuarioIncorrectoException;
import persistencia.AgenteFrontend;
import persistencia.FPCentroSalud;
import persistencia.FPEntradaLog;
import persistencia.FPPeriodoTrabajo;
import persistencia.FPSustitucion;
import persistencia.FPUsuario;
import junit.framework.TestCase;

public class PruebasPersistencia extends TestCase {

	private CentroSalud centro1, centro2, centro3;
	private EntradaLog entrada1, entrada2, entrada3;
	private Medico medico1, medico2;
	private Citador citador1, citador2;
	private Administrador administrador1;
	private PeriodoTrabajo periodo1, periodo2;
	private Sustitucion sustitucion1, sustitucion2;
	private ConexionBDFrontend conexionF;
	private Pediatra pediatra;
	private Cabecera cabecera;
	
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
			// Ponemos la conexión local con la base de datos
			conexionF = new ConexionBDFrontend();
			GestorConexionesBD.ponerConexion(conexionF);
			//Inicializamos los tipos de medicos
			pediatra = new Pediatra();
			cabecera = new Cabecera();
			// Creamos objetos de prueba
			centro1 = new CentroSalud("Centro A", "Calle Toledo, 44");
			centro2 = new CentroSalud("Centro B", null);
			centro3 = new CentroSalud("Centro C", "Otra calle");
			entrada1 = new EntradaLog("juan", new Timestamp(109, 11, 1, 10, 10, 10, 0), "create", "Entrada de prueba 1.");
			entrada2 = new EntradaLog("luis", new Timestamp(109, 5, 25, 7, 30, 0, 0), "update", "Entrada de prueba 2.");
			entrada3 = new EntradaLog("mal", new Timestamp(109, 9, 10, 8, 0, 0, 0), "mal", "Entrada con errores.");
			medico1 = new Medico("12345678", "medPrueba", "abcdef", "Eduardo", "P. C.", centro1, pediatra);
			medico2 = new Medico("87654321", "medico2", "xxx", "Carmen", "G. G.", centro1, cabecera);
			citador1 = new Citador("1112223", "citador", "abcdef", "Luis", "E. G.", centro3);
			citador2 = new Citador("9998887", "citador", "abcdef", "Ana", "B. E.", centro1);
			administrador1 = new Administrador("12121212", "admin", "nimda", "Administrador", "", centro1);
			periodo1 = new PeriodoTrabajo(10, 12, DiaSemana.Lunes);
			periodo2 = new PeriodoTrabajo(16, 20, DiaSemana.Jueves);
			sustitucion1 = new Sustitucion(new Date(2009 - 1900, 11, 1), medico1, medico2);
			sustitucion2 = new Sustitucion(new Date(2009 - 1900, 11, 2), medico1, medico2);
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	protected void tearDown() {
		// Quitamos la conexión local con la base de datos
		GestorConexionesBD.quitarConexiones();
	}
	
	/** Pruebas de la tabla de centros de salud */
	public void testCentrosSalud() {
		CentroSalud centro;
		
		try {
			// Intentamos buscar un centro aleatorio sin haber uno
			centro = FPCentroSalud.consultarAleatorio();
			fail("Se esperaba una excepción CentroSaludIncorrectoException");
		} catch(CentroSaludIncorrectoException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción CentroSaludIncorrectoException");
		}
		
		try {
			// Insertamos varios centros correctos
			FPCentroSalud.insertar(centro1);
			FPCentroSalud.insertar(centro3);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Comprobamos que los centros se han añadido correctamente
			centro = FPCentroSalud.consultarAleatorio();
			assertTrue(centro1.equals(centro) || centro3.equals(centro));
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Insertamos un nuevo centro con errores
			FPCentroSalud.insertar(centro2);
			fail("Se esperaba una excepción SQLException");
		} catch(SQLException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción SQLException");
		}
		
		try {
			// Intentamos insertar el mismo centro para ver si falla
			// (no puede haber dos centros con el mismo nombre)
			FPCentroSalud.insertar(centro1);
			fail("Se esperaba una excepción SQLException");
		} catch(SQLException e) {
		} catch(Exception e) {
			fail(e.toString());
		}

		try {
			// Intentamos buscar un centro inexistente
			centro = FPCentroSalud.consultar(1000);
			fail("Se esperaba una excepción CentroSaludIncorrectoException");
		} catch(CentroSaludIncorrectoException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción CentroSaludIncorrectoException");
		}
	}

	/** Pruebas de la tabla de entradas de log */
	public void testEntradasLog() {
		ArrayList<EntradaLog> log;
		
		try {
			// Leemos las entradas para ver si devuelve una lista vacía
			log = FPEntradaLog.consultarLog();
			assertTrue(log != null && log.size() == 0);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Insertamos nuevas entradas válidas (y repetidas, que se permite)
			FPEntradaLog.insertar(entrada1);
			FPEntradaLog.insertar(entrada2);
			FPEntradaLog.insertar(entrada1);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Intentamos insertar una entrada con errores
			FPEntradaLog.insertar(entrada3);
			fail("Se esperaba una excepción SQLException");
		} catch(SQLException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción SQLException");
		}

		try {
			// Comprobamos que las entradas se hayan añadido bien
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
			fail("Se esperaba una excepción UsuarioIncorrectoException");
		} catch(UsuarioIncorrectoException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción UsuarioIncorrectoException");
		}
		
		try {
			// Añadimos los centros de salud asociados a los usuarios
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
			FPUsuario.insertar(medico1);
			fail("Se esperaba una excepción SQLException");
		} catch(SQLException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción SQLException");
		}

		try {
			// Intentamos insertar un usuario con un login existente
			FPUsuario.insertar(citador2);
			fail("Se esperaba una excepción SQLException");
		} catch(SQLException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción SQLException");
		}
		
		try {
			// Intentamos buscar un usuario que no existe
			usuario = FPUsuario.consultar("login", "password");
			fail("Se esperaba una excepción UsuarioIncorrectoException");
		} catch(UsuarioIncorrectoException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción UsuarioIncorrectoException");
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
			citador1.setNombre("Ramón");
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
			fail("Se esperaba una excepción SQLException");
		} catch(SQLException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción SQLException");
		}
		
		try {
			// Eliminamos un usuario
			FPUsuario.eliminar(administrador1);
			// Comprobamos si los cambios han tenido efecto
			usuario = FPUsuario.consultar(administrador1.getDni());
			fail("Se esperaba una excepción UsuarioIncorrectoException");
		} catch(UsuarioIncorrectoException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción UsuarioIncorrectoException");
		}
	}
	
	/** Pruebas de la tabla de períodos de trabajo */
	public void testPeriodosTrabajo() {
		ArrayList<PeriodoTrabajo> periodos;
			
		try {
			// Añadimos un médico a la base de datos
			FPCentroSalud.insertar(centro1);
			FPUsuario.insertar(medico1);
			// Insertamos varios períodos de trabajo correctos
			FPPeriodoTrabajo.insertar(medico1.getDni(), periodo1);
			FPPeriodoTrabajo.insertar(medico1.getDni(), periodo2);
		} catch(Exception e) {
			fail(e.toString());
		}

		try {
			// Modificamos un período de trabajo existente
			periodo1.setHoraInicio(9);
			periodo1.setHoraFinal(11);
			FPPeriodoTrabajo.modificar(periodo1);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Recuperamos los períodos de trabajo almacenados
			periodos = FPPeriodoTrabajo.consultarCalendario(medico1.getDni());
			assertTrue((periodos.get(0).equals(periodo1) && periodos.get(1).equals(periodo2)
			           || (periodos.get(0).equals(periodo2) && periodos.get(1).equals(periodo1))));
			// Comprobamos si la modificación tuvo efecto
			assertTrue(periodos.get(0).getHoraFinal() == 11 || periodos.get(1).getHoraFinal() == 11);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Eliminamos los períodos de trabajo
			FPPeriodoTrabajo.eliminar(periodo1);
			FPPeriodoTrabajo.eliminar(periodo2);
			// Comprobamos si los cambios han tenido efecto
			periodos = FPPeriodoTrabajo.consultarCalendario(medico1.getDni());
			assertTrue(periodos.size() == 0);
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	/** Pruebas de la tabla de sustituciones */
	public void testSustituciones() {
		ArrayList<Sustitucion> sustituciones;
			
		try {
			// Añadimos varios médicos a la base de datos
			FPCentroSalud.insertar(centro1);
			FPUsuario.insertar(medico1);
			FPUsuario.insertar(medico2);
			// Insertamos una sustitución correcta
			FPSustitucion.insertar(sustitucion1);
			FPSustitucion.insertar(sustitucion2);
		} catch(Exception e) {
			fail(e.toString());
		}

		try {
			// Recuperamos las sustituciones almacenadas
			sustituciones = FPSustitucion.consultarMedico(medico1.getDni());
			assertTrue((sustituciones.get(0).equals(sustitucion1) && sustituciones.get(1).equals(sustitucion2)
			           || (sustituciones.get(0).equals(sustitucion2) && sustituciones.get(1).equals(sustitucion1))));
			sustituciones = FPSustitucion.consultarSustituto(medico2.getDni());
			assertTrue((sustituciones.get(0).equals(sustitucion1) && sustituciones.get(1).equals(sustitucion2)
			           || (sustituciones.get(0).equals(sustitucion2) && sustituciones.get(1).equals(sustitucion1))));
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
}
