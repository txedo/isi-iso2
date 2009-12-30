package pruebas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import persistencia.AgenteFrontend;
import persistencia.FPCentroSalud;
import persistencia.FPUsuario;
import comunicaciones.ConexionBDFrontend;
import comunicaciones.GestorConexionesBD;
import dominio.conocimiento.Administrador;
import dominio.conocimiento.Cabecera;
import dominio.conocimiento.CentroSalud;
import dominio.conocimiento.Citador;
import dominio.conocimiento.DiaSemana;
import dominio.conocimiento.Especialista;
import dominio.conocimiento.ISesion;
import dominio.conocimiento.Medico;
import dominio.conocimiento.Pediatra;
import dominio.conocimiento.PeriodoTrabajo;
import dominio.conocimiento.Sesion;
import dominio.control.GestorSesiones;
import dominio.control.GestorUsuarios;
import excepciones.MedicoInexistenteException;
import excepciones.MedicoYaExistenteException;
import excepciones.OperacionIncorrectaException;
import excepciones.SesionInvalidaException;
import junit.framework.TestCase;

public class PruebasMedicos extends TestCase {

	private CentroSalud centro1;
	private Medico medico1, medico2;
	private Citador citador1;
	private Administrador admin1;
	private PeriodoTrabajo periodo1, periodo2, periodo3;
	private ConexionBDFrontend conexionF;
	private ISesion sesionCitador;
	private ISesion sesionAdmin;
	private Pediatra pediatra;
	private Especialista especialista;
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
			sentencia = bd.prepareStatement("DELETE FROM volantes");
			sentencia.executeUpdate();
			// Ponemos la conexión local con la base de datos
			conexionF = new ConexionBDFrontend();
			GestorConexionesBD.ponerConexion(conexionF);
			//Inicializamos los tipos de medicos
			pediatra = new Pediatra();
			especialista = new Especialista();
			cabecera = new Cabecera();
			// Creamos objetos de prueba
			centro1 = new CentroSalud("Centro A", "Calle Toledo, 44");
			medico1 = new Medico("12345678", "medPrueba", "abcdef", "Eduardo", "P. C.", centro1, pediatra);
			medico2 = new Medico("87654321", "medico2", "xxx", "Carmen", "G. G.", centro1, cabecera);
			citador1 = new Citador("11223344", "citador", "cit123", "Fernando", "G. P.", centro1);
			admin1 = new Administrador("55667788", "admin", "nimda", "María", "L. F.", centro1);
			periodo1 = new PeriodoTrabajo(10, 14, DiaSemana.Miercoles);
			periodo2 = new PeriodoTrabajo(17, 19, DiaSemana.Viernes);
			periodo3 = new PeriodoTrabajo(16, 17, DiaSemana.Lunes);
			medico1.getCalendario().add(periodo1);
			medico1.getCalendario().add(periodo2);
			medico2.getCalendario().add(periodo3);
			FPCentroSalud.insertar(centro1);
			FPUsuario.insertar(medico1);
			FPUsuario.insertar(medico2);
			FPUsuario.insertar(citador1);
			FPUsuario.insertar(admin1);
			// Iniciamos dos sesiones con roles de citador y administrador
			sesionCitador = GestorSesiones.identificar(citador1.getLogin(), citador1.getPassword());
			sesionAdmin = GestorSesiones.identificar(admin1.getLogin(), admin1.getPassword());
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	protected void tearDown() {
		// Cerramos la sesión y quitamos la conexión local con la base de datos
		try {
			GestorSesiones.liberar(((Sesion)sesionCitador).getId());
			GestorSesiones.liberar(((Sesion)sesionAdmin).getId());
			GestorConexionesBD.quitarConexiones();
		} catch(SQLException e) {
			fail(e.toString());
		}
	}
	
	/** Pruebas de la operación que obtiene los datos de un médico */
	public void testObtenerMedico() {
		Medico medico;
		
		try {
			// Obtenemos los datos de un médico existente
			medico = GestorUsuarios.getMedico(sesionCitador.getId(), medico1.getDni());
			assertEquals(medico, medico1);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Intentamos acceder al servidor con un id de sesión erróneo
			medico = GestorUsuarios.getMedico(sesionCitador.getId() + 1, medico1.getDni());
			fail("Se esperaba una excepcion SesionInvalidaException");
		} catch(SesionInvalidaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepcion SesionInvalidaException");
		}
		
		try {
			// Intentamos obtener los datos de un usuario que no es médico
			medico = GestorUsuarios.getMedico(sesionCitador.getId(), citador1.getDni());
			fail("Se esperaba una excepcion MedicoInexistenteException");
		} catch(MedicoInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepcion MedicoInexistenteException");
		}
		
		try {
			// Intentamos obtener los datos de un usuario que no existe
			medico = GestorUsuarios.getMedico(sesionCitador.getId(), "94821491");
			fail("Se esperaba una excepcion MedicoInexistenteException");
		} catch(MedicoInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepcion MedicoInexistenteException");
		}
	}

	/** Pruebas de la operación que crea nuevos médicos */
	public void testCrearMedico() {
		Medico medico, medicoGet;
		
		try {
			// Creamos un nuevo médico con la sesión del administrador
			medico = new Medico("6666666", "medNuevo", "medNuevo", "Juan", "P. C.", centro1, especialista);
			GestorUsuarios.crearMedico(sesionAdmin.getId(), medico);
			// Comprobamos que el médico se ha creado correctamente
			medicoGet = GestorUsuarios.getMedico(sesionAdmin.getId(), medico.getDni());
			assertEquals(medico, medicoGet);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Intentamos crear un nuevo médico con el rol de citador
			medico = new Medico("77777777", "error", "error", "", "", centro1, especialista);
			GestorUsuarios.crearMedico(sesionCitador.getId(), medico);
			fail("Se esperaba una excepcion OperacionIncorrectaException");
		} catch(OperacionIncorrectaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepcion OperacionIncorrectaException");
		}
		
		try {
			// Intentamos añadir un médico con un DNI que ya existe en la BD
			medico = new Medico(citador1.getDni(), "error", "error", "", "", centro1, cabecera);
			GestorUsuarios.crearMedico(sesionAdmin.getId(), medico);
			fail("Se esperaba una excepcion MedicoYaExistenteException");
		} catch(MedicoYaExistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepcion MedicoYaExistenteException");
		}
	}

	/** Pruebas de la operación que modifica médicos existentes */
	public void testModificarMedico() {
		Medico medico, medicoGet;
		
		try {
			// Modificamos los datos de un médico existente como administrador
			medico1.setLogin("medCambiado");
			medico1.setApellidos("P. D.");
			medico1.getCalendario().remove(1);
			GestorUsuarios.modificarMedico(sesionAdmin.getId(), medico1);
			// Comprobamos que el médico se haya actualizado correctamente
			medicoGet = GestorUsuarios.getMedico(sesionAdmin.getId(), medico1.getDni());
			assertEquals(medico1, medicoGet);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Intentamos modificar un médico con el rol de citador
			GestorUsuarios.modificarMedico(sesionCitador.getId(), medico1);
			fail("Se esperaba una excepcion OperacionIncorrectaException");
		} catch(OperacionIncorrectaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepcion OperacionIncorrectaException");
		}
		
		try {
			// Intentamos modificar un médico que aún no se ha creado
			medico = new Medico("21412395", "error", "error", "", "", centro1, pediatra);
			GestorUsuarios.modificarMedico(sesionAdmin.getId(), medico);
			fail("Se esperaba una excepcion MedicoInexistenteException");
		} catch(MedicoInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepcion MedicoInexistenteException");
		}
	}

	/** Pruebas de la operación que elimina médicos existentes */
	public void testEliminarMedico() {
		Medico medico;
		
		try {
			// Eliminamos un médico existente como administrador
			GestorUsuarios.eliminarMedico(sesionAdmin.getId(), medico2);
		} catch(Exception e) {
			e.printStackTrace();
			fail(e.toString());
		}

		try {
			// Comprobamos que el médico borrado ya no exista en el sistema
			GestorUsuarios.getMedico(sesionAdmin.getId(), medico2.getDni());
			fail("Se esperaba una excepcion MedicoInexistenteException");
		} catch(MedicoInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepcion MedicoInexistenteException");
		}
		
		try {
			// Intentamos borrar un médico con el rol de citador
			GestorUsuarios.eliminarMedico(sesionCitador.getId(), medico1);
			fail("Se esperaba una excepcion OperacionIncorrectaException");
		} catch(OperacionIncorrectaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepcion OperacionIncorrectaException");
		}
		
		try {
			// Intentamos eliminar un médico que aún no se ha creado
			medico = new Medico("78256514", "error", "error", "", "", centro1, pediatra);
			GestorUsuarios.eliminarMedico(sesionAdmin.getId(), medico);
			fail("Se esperaba una excepcion MedicoInexistenteException");
		} catch(MedicoInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepcion MedicoInexistenteException");
		}
	}
	
	/** Pruebas relacionadas con los calendarios de los médicos */
	public void testCalendariosMedico() {
		Date fecha;
		
		try {
			// Comprobamos varias fechas válidas
			// (el médico 1 tiene como horario de trabajo los miércoles
			// de 10:00 a 14:00 y los viernes de 17:00 a 19:00)
			fecha = new Date(2009 - 1900, 11, 2, 10, 0, 0); // Martes 10:00-10:10
			assertTrue(medico1.fechaEnCalendario(fecha, 10));
			fecha = new Date(2009 - 1900, 11, 2, 12, 30, 0); // Martes 12:30-12:40
			assertTrue(medico1.fechaEnCalendario(fecha, 10));
			fecha = new Date(2009 - 1900, 11, 2, 13, 50, 0); // Martes 13:50-14:00
			assertTrue(medico1.fechaEnCalendario(fecha, 10));
			fecha = new Date(2009 - 1900, 11, 4, 18, 20, 0); // Viernes 18:20-18:30
			assertTrue(medico1.fechaEnCalendario(fecha, 10));
			// Comprobamos varias fechas no válidas
			fecha = new Date(2009 - 1900, 11, 2, 9, 50, 0); // Martes 9:50-10:00
			assertFalse(medico1.fechaEnCalendario(fecha, 10));
			fecha = new Date(2009 - 1900, 11, 2, 14, 00, 0); // Martes 14:00-14:10
			assertFalse(medico1.fechaEnCalendario(fecha, 10));
			fecha = new Date(2009 - 1900, 11, 2, 17, 30, 0); // Martes 17:30-17:40
			assertFalse(medico1.fechaEnCalendario(fecha, 10));
			fecha = new Date(2009 - 1900, 11, 4, 10, 00, 0); // Viernes 10:00-10:10
			assertFalse(medico1.fechaEnCalendario(fecha, 10));
			fecha = new Date(2009 - 1900, 11, 6, 12, 00, 0); // Domingo 12:00-12:10
			assertFalse(medico1.fechaEnCalendario(fecha, 10));
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
}
