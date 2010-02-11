package pruebas;

import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;
import persistencia.FPCentroSalud;
import persistencia.FPUsuario;
import dominio.conocimiento.Administrador;
import dominio.conocimiento.Cabecera;
import dominio.conocimiento.CentroSalud;
import dominio.conocimiento.Citador;
import dominio.conocimiento.DiaSemana;
import dominio.conocimiento.Encriptacion;
import dominio.conocimiento.Especialista;
import dominio.conocimiento.ISesion;
import dominio.conocimiento.Medico;
import dominio.conocimiento.Pediatra;
import dominio.conocimiento.PeriodoTrabajo;
import dominio.conocimiento.Sesion;
import dominio.control.GestorMedicos;
import dominio.control.GestorSesiones;
import excepciones.MedicoInexistenteException;
import excepciones.MedicoYaExistenteException;
import excepciones.OperacionIncorrectaException;
import excepciones.SesionInvalidaException;
import excepciones.SustitucionInvalidaException;

public class PruebasMedicos extends PruebasBase {

	private CentroSalud centro1;
	private Medico medico1, medico2, medico3, medico4;
	private Citador citador1;
	private Administrador admin1;
	private PeriodoTrabajo periodo11, periodo12;
	private PeriodoTrabajo periodo21;
	private PeriodoTrabajo periodo31, periodo32;
	private ISesion sesionCitador;
	private ISesion sesionAdmin;
	private Pediatra pediatra;
	private Especialista especialista;
	private Cabecera cabecera;
	
	protected void setUp() {
		try {
			// Preparamos la base de datos
			super.setUp();
			//Inicializamos los tipos de medicos
			pediatra = new Pediatra();
			especialista = new Especialista("Ginecologia");
			cabecera = new Cabecera();
			// Creamos objetos de prueba
			centro1 = new CentroSalud("Centro Provincial", "Calle Ninguna, s/n");
			medico1 = new Medico("12345678", "medPrueba", Encriptacion.encriptarPasswordSHA1("abcdef"), "Eduardo", "P. C.", pediatra);
			medico2 = new Medico("87654321", "medico2", Encriptacion.encriptarPasswordSHA1("xxx"), "Carmen", "G. G.", cabecera);
			medico3 = new Medico("58782350", "jjj", Encriptacion.encriptarPasswordSHA1("jjj"), "Juan", "P. F.", especialista);
			medico4 = new Medico("91295016", "otro", Encriptacion.encriptarPasswordSHA1("otro"), "Ana", "R. M.", cabecera);
			citador1 = new Citador("11223344", "citador", Encriptacion.encriptarPasswordSHA1("cit123"), "Fernando", "G. P.");
			admin1 = new Administrador("55667788", "admin", Encriptacion.encriptarPasswordSHA1("nimda"), "Mar�a", "L. F.");
			medico1.setCentroSalud(centro1);
			medico2.setCentroSalud(centro1);
			medico3.setCentroSalud(centro1);
			medico4.setCentroSalud(centro1);
			citador1.setCentroSalud(centro1);
			admin1.setCentroSalud(centro1);
			periodo11 = new PeriodoTrabajo(10, 14, DiaSemana.Miercoles);
			periodo12 = new PeriodoTrabajo(17, 19, DiaSemana.Viernes);
			periodo21 = new PeriodoTrabajo(16, 17, DiaSemana.Lunes);
			periodo31 = new PeriodoTrabajo(9, 13, DiaSemana.Martes);
			periodo32 = new PeriodoTrabajo(16, 18, DiaSemana.Viernes);
			medico1.getCalendario().add(periodo11);
			medico1.getCalendario().add(periodo12);
			medico2.getCalendario().add(periodo21);
			medico3.getCalendario().add(periodo31);
			medico3.getCalendario().add(periodo32);
			FPCentroSalud.insertar(centro1);
			FPUsuario.insertar(medico1);
			FPUsuario.insertar(medico2);
			FPUsuario.insertar(medico3);
			FPUsuario.insertar(medico4);
			FPUsuario.insertar(citador1);
			FPUsuario.insertar(admin1);
			// Iniciamos dos sesiones con roles de citador y administrador
			sesionCitador = GestorSesiones.identificar(citador1.getLogin(), "cit123");
			sesionAdmin = GestorSesiones.identificar(admin1.getLogin(), "nimda");
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	protected void tearDown() {
		try {
			// Cerramos la sesi�n
			GestorSesiones.liberar(((Sesion)sesionCitador).getId());
			GestorSesiones.liberar(((Sesion)sesionAdmin).getId());
			// Cerramos la base de datos
			super.tearDown();
		} catch(SQLException e) {
			fail(e.toString());
		}
	}
	
	/** Pruebas de la operaci�n que obtiene los datos de un m�dico */
	public void testObtenerMedico() {
		Medico medico;
		
		try {
			// Obtenemos los datos de un m�dico existente
			medico = GestorMedicos.consultarMedico(sesionAdmin.getId(), medico1.getDni());
			assertEquals(medico, medico1);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Intentamos acceder al servidor con un id de sesi�n err�neo
			medico = GestorMedicos.consultarMedico(sesionCitador.getId() + 1, medico1.getDni());
			fail("Se esperaba una excepcion SesionInvalidaException");
		} catch(SesionInvalidaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepcion SesionInvalidaException");
		}
		
		try {
			// Intentamos obtener los datos de un usuario que no es m�dico
			medico = GestorMedicos.consultarMedico(sesionAdmin.getId(), citador1.getDni());
			fail("Se esperaba una excepcion MedicoInexistenteException");
		} catch(MedicoInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepcion MedicoInexistenteException");
		}
		
		try {
			// Intentamos obtener los datos de un usuario que no existe
			medico = GestorMedicos.consultarMedico(sesionAdmin.getId(), "94821491");
			fail("Se esperaba una excepcion MedicoInexistenteException");
		} catch(MedicoInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepcion MedicoInexistenteException");
		}
	}

	/** Pruebas de la operaci�n que crea nuevos m�dicos */
	public void testCrearMedico() {
		Medico medico, medicoGet;
		
		try {
			// Creamos un nuevo m�dico con la sesi�n del administrador
			medico = new Medico("6666666", "medNuevo", "medNuevo", "Juan", "P. C.", especialista);
			medico.setCentroSalud(centro1);
			GestorMedicos.crearMedico(sesionAdmin.getId(), medico);
			// Al crear el m�dico la contrase�a se habr� encriptado
			medico.setPassword(Encriptacion.encriptarPasswordSHA1("medNuevo"));
			// Comprobamos que el m�dico se ha creado correctamente
			medicoGet = GestorMedicos.consultarMedico(sesionAdmin.getId(), medico.getDni());
			assertEquals(medico, medicoGet);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Intentamos crear un nuevo m�dico con el rol de citador
			medico = new Medico("77777777", "error", Encriptacion.encriptarPasswordSHA1("error"), "", "", especialista);
			medico.setCentroSalud(centro1);
			GestorMedicos.crearMedico(sesionCitador.getId(), medico);
			fail("Se esperaba una excepcion OperacionIncorrectaException");
		} catch(OperacionIncorrectaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepcion OperacionIncorrectaException");
		}
		
		try {
			// Intentamos a�adir un m�dico con un DNI que ya existe en la BD
			medico = new Medico(citador1.getDni(), Encriptacion.encriptarPasswordSHA1("error"), "error", "", "", cabecera);
			medico.setCentroSalud(centro1);
			GestorMedicos.crearMedico(sesionAdmin.getId(), medico);
			fail("Se esperaba una excepcion MedicoYaExistenteException");
		} catch(MedicoYaExistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepcion MedicoYaExistenteException");
		}
	}

	/** Pruebas de la operaci�n que modifica m�dicos existentes */
	public void testModificarMedico() {
		Medico medico, medicoGet;
		
		//TODO: Aqu� hay que comprobar si funciona que, al cambiar
		// un m�dico pasando la contrase�a "" se mantiene la antigua,
		// y si se pasa otra cosa se encripta la nueva contrase�a
		
		try {
			// Modificamos los datos de un m�dico existente como administrador
			medico1.setLogin("medCambiado");
			medico1.setApellidos("P. D.");
			medico1.getCalendario().remove(1);
			GestorMedicos.modificarMedico(sesionAdmin.getId(), medico1);
			// Comprobamos que el m�dico se haya actualizado correctamente
			medicoGet = GestorMedicos.consultarMedico(sesionAdmin.getId(), medico1.getDni());
			System.out.println(medico1);
			System.out.println(medicoGet);
			assertEquals(medico1, medicoGet);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Intentamos modificar un m�dico con el rol de citador
			GestorMedicos.modificarMedico(sesionCitador.getId(), medico1);
			fail("Se esperaba una excepcion OperacionIncorrectaException");
		} catch(OperacionIncorrectaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepcion OperacionIncorrectaException");
		}
		
		try {
			// Intentamos modificar un m�dico que a�n no se ha creado
			medico = new Medico("21412395", "error", Encriptacion.encriptarPasswordSHA1("error"), "", "", pediatra);
			medico.setCentroSalud(centro1);
			GestorMedicos.modificarMedico(sesionAdmin.getId(), medico);
			fail("Se esperaba una excepcion MedicoInexistenteException");
		} catch(MedicoInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepcion MedicoInexistenteException");
		}
	}

	/** Pruebas de la operaci�n que elimina m�dicos existentes */
	public void testEliminarMedico() {
		Medico medico;
		
		try {
			// Eliminamos un m�dico existente como administrador
			GestorMedicos.eliminarMedico(sesionAdmin.getId(), medico2);
		} catch(Exception e) {
			e.printStackTrace();
			fail(e.toString());
		}

		try {
			// Comprobamos que el m�dico borrado ya no exista en el sistema
			GestorMedicos.consultarMedico(sesionAdmin.getId(), medico2.getDni());
			fail("Se esperaba una excepcion MedicoInexistenteException");
		} catch(MedicoInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepcion MedicoInexistenteException");
		}
		
		try {
			// Intentamos borrar un m�dico con el rol de citador
			GestorMedicos.eliminarMedico(sesionCitador.getId(), medico1);
			fail("Se esperaba una excepcion OperacionIncorrectaException");
		} catch(OperacionIncorrectaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepcion OperacionIncorrectaException");
		}
		
		try {
			// Intentamos eliminar un m�dico que a�n no se ha creado
			medico = new Medico("78256514", "error", Encriptacion.encriptarPasswordSHA1("error"), "", "", pediatra);
			medico.setCentroSalud(centro1);
			GestorMedicos.eliminarMedico(sesionAdmin.getId(), medico);
			fail("Se esperaba una excepcion MedicoInexistenteException");
		} catch(MedicoInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepcion MedicoInexistenteException");
		}
	}
	
	/** Pruebas relacionadas con los calendarios de los m�dicos */
	@SuppressWarnings("deprecation")
	public void testCalendariosMedico() {
		Date fecha;
		
		try {
			// Comprobamos varias fechas v�lidas
			// (el m�dico 1 tiene como horario de trabajo los mi�rcoles
			// de 10:00 a 14:00 y los viernes de 17:00 a 19:00)
			fecha = new Date(2009 - 1900, 11, 2, 10, 0, 0); // Mi�rcoles 10:00-10:10
			assertTrue(medico1.fechaEnCalendario(fecha, 10));
			fecha = new Date(2009 - 1900, 11, 2, 12, 30, 0); // Mi�rcoles 12:30-12:40
			assertTrue(medico1.fechaEnCalendario(fecha, 10));
			fecha = new Date(2009 - 1900, 11, 2, 13, 50, 0); // Mi�rcoles 13:50-14:00
			assertTrue(medico1.fechaEnCalendario(fecha, 10));
			fecha = new Date(2009 - 1900, 11, 4, 18, 20, 0); // Viernes 18:20-18:30
			assertTrue(medico1.fechaEnCalendario(fecha, 10));
			// Comprobamos varias fechas no v�lidas
			fecha = new Date(2009 - 1900, 11, 2, 9, 50, 0); // Mi�rcoles 9:50-10:00
			assertFalse(medico1.fechaEnCalendario(fecha, 10));
			fecha = new Date(2009 - 1900, 11, 2, 14, 00, 0); // Mi�rcoles 14:00-14:10
			assertFalse(medico1.fechaEnCalendario(fecha, 10));
			fecha = new Date(2009 - 1900, 11, 2, 17, 30, 0); // Mi�rcoles 17:30-17:40
			assertFalse(medico1.fechaEnCalendario(fecha, 10));
			fecha = new Date(2009 - 1900, 11, 4, 10, 00, 0); // Viernes 10:00-10:10
			assertFalse(medico1.fechaEnCalendario(fecha, 10));
			fecha = new Date(2009 - 1900, 11, 6, 12, 00, 0); // Domingo 12:00-12:10
			assertFalse(medico1.fechaEnCalendario(fecha, 10));
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	/** Pruebas relacionadas con las sustituciones de m�dicos */
	@SuppressWarnings("deprecation")
	public void testSustituciones() {
		Vector<Date> dias;
		
		try {
			// Intentamos a�adir una sustituci�n v�lida (el m�dico 2
			// sustituye al m�dico 1 el 2/12/2009 y el 4/12/2009)
			dias = new Vector<Date>();
			dias.add(new Date(2009 - 1900, 11, 2)); // Mi�rcoles 2/12/2009
			dias.add(new Date(2009 - 1900, 11, 4)); // Viernes 4/12/2009
			GestorMedicos.modificarCalendario(sesionAdmin.getId(), medico1, dias, medico2);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Intentamos a�adir una sustituci�n inv�lida porque el m�dico
			// que se va a sustituir ya tiene una sustituci�n (el m�dico 1
			// ya va a ser sustituido el d�a 4/12/2009) 
			dias = new Vector<Date>();
			dias.add(new Date(2009 - 1900, 11, 3)); // Jueves 3/12/2009
			dias.add(new Date(2009 - 1900, 11, 4)); // Viernes 4/12/2009
			GestorMedicos.modificarCalendario(sesionAdmin.getId(), medico1, dias, medico4);
			fail("Se esperaba una excepci�n SustitucionInvalidaException");
		} catch(SustitucionInvalidaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n SustitucionInvalidaException");
		}
		
		try {
			// Intentamos a�adir una sustituci�n inv�lida porque el m�dico
			// que va a hacer la sustituci�n ya tiene otra sustituci�n
			// (el m�dico 2 va a hacer una sustituci�n el d�a 4/12/2009)
			dias = new Vector<Date>();
			dias.add(new Date(2009 - 1900, 11, 3)); // Jueves 3/12/2009
			dias.add(new Date(2009 - 1900, 11, 4)); // Viernes 4/12/2009
			GestorMedicos.modificarCalendario(sesionAdmin.getId(), medico3, dias, medico2);
			fail("Se esperaba una excepci�n SustitucionInvalidaException");
		} catch(SustitucionInvalidaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n SustitucionInvalidaException");
		}
		
		try {
			// Intentamos a�adir una sustituci�n entre dos m�dicos cuyos
			// calendarios se solapan el d�a que se va a sustituir
			// (los viernes el m�dico 1 y 3 trabajan de 17:00 a 18:00)
			dias = new Vector<Date>();
			dias.add(new Date(2009 - 1900, 11, 11)); // Viernes 11/12/2009
			GestorMedicos.modificarCalendario(sesionAdmin.getId(), medico1, dias, medico3);
			fail("Se esperaba una excepci�n SustitucionInvalidaException");
		} catch(SustitucionInvalidaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n SustitucionInvalidaException");
		}
	}	
	
}