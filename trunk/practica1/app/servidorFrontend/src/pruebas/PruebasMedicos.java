package pruebas;

import java.sql.SQLException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;
import persistencia.AgenteFrontend;
import persistencia.FPBeneficiario;
import persistencia.FPCentroSalud;
import persistencia.FPUsuario;
import dominio.conocimiento.Administrador;
import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.Cabecera;
import dominio.conocimiento.CentroSalud;
import dominio.conocimiento.Citador;
import dominio.conocimiento.DiaSemana;
import dominio.conocimiento.Direccion;
import dominio.conocimiento.Encriptacion;
import dominio.conocimiento.Especialista;
import dominio.conocimiento.ICodigosMensajeAuxiliar;
import dominio.conocimiento.IConstantes;
import dominio.conocimiento.ISesion;
import dominio.conocimiento.Medico;
import dominio.conocimiento.Pediatra;
import dominio.conocimiento.PeriodoTrabajo;
import dominio.conocimiento.Sesion;
import dominio.control.GestorMedicos;
import dominio.control.GestorSesiones;
import dominio.control.ServidorFrontend;
import excepciones.MedicoInexistenteException;
import excepciones.MedicoYaExistenteException;
import excepciones.OperacionIncorrectaException;
import excepciones.SesionInvalidaException;

/**
 * Pruebas del Gestor de M�dicos.
 */
public class PruebasMedicos extends PruebasBase {

	private ServidorFrontend servidor;
	private CentroSalud centro1;
	private Medico medico1, medico2, medico3, medico4;
	private Citador citador1;
	private Administrador admin1;
	private PeriodoTrabajo periodo11, periodo12;
	private PeriodoTrabajo periodo21;
	private PeriodoTrabajo periodo31, periodo32;
	private Beneficiario beneficiario1;
	private Direccion direccion1;
	private ISesion sesionCitador;
	private ISesion sesionMedico;
	private ISesion sesionAdmin;
	private Pediatra pediatra;
	private Especialista especialista;
	private Cabecera cabecera;
	
	protected void setUp() {
		try {
			// Preparamos la base de datos
			super.setUp();
			// Obtenemos el servidor frontend, que se utilizar� para llamar
			// a los m�todos del gestor y as� probar las dos clases a la vez
			servidor = ServidorFrontend.getServidor();
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
			direccion1 = new Direccion("calle 1", "", "", "", "aadsfaada", "afafssafad", 13500);
			beneficiario1 = new Beneficiario("88484848L", "123456-ab", "bene1", "asdfg", new Date(), direccion1, "add@sf.com", "123456789", "987654321");
			beneficiario1.setCentroSalud(medico2.getCentroSalud());
			beneficiario1.setMedicoAsignado(medico2);
			FPCentroSalud.insertar(centro1);
			FPUsuario.insertar(medico1);
			FPUsuario.insertar(medico2);
			FPUsuario.insertar(medico3);
			FPUsuario.insertar(medico4);
			FPUsuario.insertar(citador1);
			FPUsuario.insertar(admin1);
			FPBeneficiario.insertar(beneficiario1);
			// Iniciamos tres sesiones con roles de citador y administrador
			sesionCitador = GestorSesiones.identificar(citador1.getLogin(), "cit123");
			sesionMedico = GestorSesiones.identificar(medico1.getLogin(), "abcdef");
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
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	/** Pruebas de la operaci�n que consulta un m�dico existente */
	public void testConsultarMedico() {
		Medico medicoGet;
		
		try {
			// Intentamos consultar un m�dico con DNI nulo
			servidor.getMedico(sesionCitador.getId(), null);
			fail("Se esperaba una excepci�n NullPointerException");
		} catch(NullPointerException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n NullPointerException");
		}
		
		try {
			// Intentamos consultar los datos de un m�dico sin permisos
			servidor.getMedico(sesionCitador.getId(), medico1.getDni());
			fail("Se esperaba una excepci�n OperacionIncorrectaException");
		} catch(OperacionIncorrectaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n OperacionIncorrectaException");
		}
		
		try {
			// Intentamos acceder al servidor con un id de sesi�n err�neo
			servidor.getMedico(-12345, medico1.getDni());
			fail("Se esperaba una excepci�n SesionInvalidaException");
		} catch(SesionInvalidaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n SesionInvalidaException");
		}
		
		try {
			// Intentamos obtener los datos de un m�dico que no existe
			servidor.getMedico(sesionAdmin.getId(), "99001290W");
			fail("Se esperaba una excepci�n MedicoInexistenteException");
		} catch(MedicoInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n MedicoInexistenteException");
		}
		
		try {
			// Obtenemos los datos de un m�dico existente
			medicoGet = (Medico)servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CONSULTAR_USUARIO, medico1.getDni());
			assertEquals(medico1, medicoGet);
		} catch(Exception e) {
			fail(e.toString());
		}
	}

	/** Pruebas de la operaci�n que crea nuevos m�dicos */
	public void testCrearMedico() {
		Medico medico, medicoGet;
		
		try {
			// Intentamos crear un m�dico nulo
			servidor.crear(sesionCitador.getId(), (Medico)null);
			fail("Se esperaba una excepci�n NullPointerException");
		} catch(NullPointerException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n NullPointerException");
		}
		
		try {
			// Intentamos crear un m�dico con la sesi�n del citador
			medico = new Medico("6666666", "medNuevo", "medNuevo", "Juan", "P. C.", especialista);
			servidor.crear(sesionCitador.getId(), medico);
			fail("Se esperaba una excepci�n OperacionIncorrectaException");
		} catch(OperacionIncorrectaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n OperacionIncorrectaException");
		}
		
		try {
			// Intentamos acceder al servidor con un id de sesi�n err�neo
			medico = new Medico("6666666", "medNuevo", "medNuevo", "Juan", "P. C.", especialista);
			servidor.crear(-12345, medico);
			fail("Se esperaba una excepci�n SesionInvalidaException");
		} catch(SesionInvalidaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n SesionInvalidaException");
		}
		
		try {
			// Intentamos a�adir un m�dico con un DNI que ya existe en la BD
			medico = new Medico(citador1.getDni(), "error", "error", "", "", especialista);
			servidor.crear(sesionAdmin.getId(), medico);
			fail("Se esperaba una excepci�n MedicoYaExistenteException");
		} catch(MedicoYaExistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n MedicoYaExistenteException");
		}

		try {
			// Creamos un nuevo m�dico con la sesi�n del administrador
			medico = new Medico("6666666", "medNuevo", "medNuevo", "Juan", "P. C.", especialista);
			servidor.crear(sesionAdmin.getId(), medico);
			// Comprobamos que el usuario se ha creado correctamente
			medicoGet = (Medico)servidor.getMedico(sesionAdmin.getId(), medico.getDni());
			medico.setPassword(Encriptacion.encriptarPasswordSHA1("medNuevo"));
			medico.setCentroSalud(centro1);
			assertEquals(medico, medicoGet);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Intentamos crear un m�dico sin haber ning�n centro
			AgenteFrontend.getAgente().getConexion().prepareStatement("DELETE FROM centros").executeUpdate();
			medico = new Medico("34712394", "otromas", "error", "Juan", "P. C.", especialista);
			servidor.crear(sesionAdmin.getId(), medico);
			fail("Se esperaba una excepci�n SQLException");
		} catch(SQLException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n SQLException");
		}
	}
	
	/** Pruebas de la operaci�n que modifica m�dicos existentes */
	public void testModificarMedico() {
		Medico medico, medicoGet;
		
		try {
			// Intentamos modificar un m�dico nulo
			servidor.modificar(sesionCitador.getId(), (Medico)null);
			fail("Se esperaba una excepci�n NullPointerException");
		} catch(NullPointerException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n NullPointerException");
		}
		
		try {
			// Intentamos modificar un m�dico con la sesi�n del citador
			servidor.modificar(sesionCitador.getId(), medico2);
			fail("Se esperaba una excepci�n OperacionIncorrectaException");
		} catch(OperacionIncorrectaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n OperacionIncorrectaException");
		}
		
		try {
			// Intentamos acceder al servidor con un id de sesi�n err�neo
			servidor.modificar(-12345, medico2);
			fail("Se esperaba una excepci�n SesionInvalidaException");
		} catch(SesionInvalidaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n SesionInvalidaException");
		}
		
		try {
			// Intentamos modificar un m�dico inexistente
			medico = new Medico("91295019", "mas", "mas", "Luis", "R. M.", pediatra);
			servidor.modificar(sesionAdmin.getId(), medico);
			fail("Se esperaba una excepci�n MedicoInexistenteException");
		} catch(MedicoInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n MedicoInexistenteException");
		}
		
		try {
			// Modificamos los datos de un m�dico existente sin tocar la contrase�a
			medico1.setTipoMedico(cabecera);
			medico1.getCalendario().clear();
			medico1.setPassword("");
			servidor.modificar(sesionAdmin.getId(), medico1);
			// Comprobamos que el m�dico se haya actualizado correctamente
			medicoGet = servidor.getMedico(sesionAdmin.getId(), medico1.getDni());
			medico1.setPassword(Encriptacion.encriptarPasswordSHA1("abcdef"));
			assertEquals(medico1, medicoGet);
		} catch(Exception e) {
			fail(e.toString());
		}
	}

	/** Pruebas de la operaci�n que elimina m�dicos existentes */
	public void testEliminarMedico() {
		Medico medico;
		
		try {
			// Intentamos eliminar un m�dico nulo
			servidor.eliminar(sesionCitador.getId(), null);
			fail("Se esperaba una excepci�n NullPointerException");
		} catch(NullPointerException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n NullPointerException");
		}
		
		try {
			// Intentamos eliminar un m�dico con la sesi�n del citador
			servidor.eliminar(sesionCitador.getId(), medico2);
			fail("Se esperaba una excepci�n OperacionIncorrectaException");
		} catch(OperacionIncorrectaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n OperacionIncorrectaException");
		}
		
		try {
			// Intentamos acceder al servidor con un id de sesi�n err�neo
			servidor.eliminar(-12345, medico2);
			fail("Se esperaba una excepci�n SesionInvalidaException");
		} catch(SesionInvalidaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n SesionInvalidaException");
		}
		
		try {
			// Intentamos eliminar un m�dico que a�n no se ha creado
			medico = new Medico("78256514", "error", "error", "", "", pediatra);
			servidor.eliminar(sesionAdmin.getId(), medico);
			fail("Se esperaba una excepci�n MedicoInexistenteException");
		} catch(MedicoInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n MedicoInexistenteException");
		}
		
		try {
			// Eliminamos un m�dico existente como administrador
			servidor.eliminar(sesionAdmin.getId(), medico2);
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	/** Pruebas de la operaci�n que devuelve el horario de un m�dico */
	@SuppressWarnings("deprecation")
	public void testConsultarHorarioMedico() {
		Date fecha;
		Hashtable<DiaSemana, Vector<String>> horario;
		Medico medico;
		PeriodoTrabajo p;
		
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
			
			
			try {
				// Intentamos obtener el horario de un m�dico nulo
				GestorMedicos.consultarHorarioMedico(sesionAdmin.getId(), null);
				fail("Se esperaba una excepci�n NullPointerException");
			} catch(NullPointerException e) {
			} catch(Exception e) {
				fail("Se esperaba una excepci�n NullPointerException");
			}
			
			try {
				// Intentamos consultar el horario de un m�dico con el rol de m�dico
				GestorMedicos.consultarHorarioMedico(sesionMedico.getId(), medico1.getDni());
				fail("Se esperaba una excepci�n OperacionIncorrectaException");
			} catch(OperacionIncorrectaException e) {
			} catch(Exception e) {
				fail("Se esperaba una excepci�n OperacionIncorrectaException");
			}
			
			try {
				// Intentamos obtener el horario de un medico inexistente
				medico = new Medico("91295019", "otro2", Encriptacion.encriptarPasswordSHA1("otro"), "Anaasa", "R. M.", cabecera);
				GestorMedicos.consultarHorarioMedico(sesionAdmin.getId(), medico.getDni());
				fail("Se esperaba una excepci�n MedicoInexistenteException");
			} catch(MedicoInexistenteException e) {
			} catch(Exception e) {
				fail("Se esperaba una excepci�n MedicoInexistenteException");
			}
			
			try {
				// Intentamos obtener el horario de un usuario que no es m�dico
				GestorMedicos.consultarHorarioMedico(sesionAdmin.getId(), admin1.getDni());
				fail("Se esperaba una excepci�n MedicoInexistenteException");
			} catch(MedicoInexistenteException e) {
			} catch(Exception e) {
				fail("Se esperaba una excepci�n MedicoInexistenteException");
			}
			
			try {
				// Intentamos obtener el horario de un medico correcto				
				horario = GestorMedicos.consultarHorarioMedico(sesionAdmin.getId(), medico2.getDni());
				// No se debe obtener una tabla vac�a
				assertTrue(horario.keys().hasMoreElements());
				// Segun el calendario del m�dico2, se deben obtener horas s�lo para los Lunes
				assertTrue(horario.get(DiaSemana.Lunes).size()!=0);
				assertTrue(horario.get(DiaSemana.Martes).size()==0);
				assertTrue(horario.get(DiaSemana.Miercoles).size()==0);
				assertTrue(horario.get(DiaSemana.Jueves).size()==0);
				assertTrue(horario.get(DiaSemana.Viernes).size()==0);
				// El n�mero de citas para ese d�a debe ser (horaFinal - horaInicio ) / Duracion (en minutos)
				p = medico2.getCalendario().get(0);
				assertTrue(horario.get(DiaSemana.Lunes).size()==(((p.getHoraFinal() - p.getHoraInicio()) * 60) / IConstantes.DURACION_CITA));
			} catch(Exception e) {
				fail("Se esperaba una excepci�n al obtener el horario de un m�dico");
			}
			
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	/** Pruebas relacionadas con las sustituciones de m�dicos */
/*	@SuppressWarnings("deprecation")
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
	}	*/

	/*
	
	public void testNuevasSustituciones() {
		Medico m1;
		Medico m2;
		Medico m3;
		Medico m4;
		Medico m5;
		m1 = new Medico("1234", "1234", "1234", "abc", "abc", new Especialista("ABC"));
		m1.setCentroSalud(centro1);
		m1.getCalendario().add(new PeriodoTrabajo(10, 14, DiaSemana.Lunes));
		m1.getCalendario().add(new PeriodoTrabajo(1, 23, DiaSemana.Miercoles));
		m2 = new Medico("1111", "1111", "1234", "abc", "abc", new Especialista("ABC"));
		m2.setCentroSalud(centro1);
		m2.getCalendario().add(new PeriodoTrabajo(10, 11, DiaSemana.Lunes));
		m2.getCalendario().add(new PeriodoTrabajo(13, 18, DiaSemana.Lunes));
		m2.getCalendario().add(new PeriodoTrabajo(2, 3, DiaSemana.Martes));
		m3 = new Medico("2222", "2222", "1234", "abc", "abc", new Especialista("ABC"));
		m3.setCentroSalud(centro1);
		m3.getCalendario().add(new PeriodoTrabajo(8, 20, DiaSemana.Lunes));
		m3.getCalendario().add(new PeriodoTrabajo(14, 20, DiaSemana.Jueves));
		m4 = new Medico("3333", "3333", "1234", "abc", "abc", new Pediatra());
		m4.getCalendario().add(new PeriodoTrabajo(10, 13, DiaSemana.Lunes));
		m4.setCentroSalud(centro1);
		m5 = new Medico("4444", "4444", "1234", "abc", "abc", new Especialista("ABC"));
		CentroSalud centro2 = new CentroSalud("ABC", "HIJ");
		m5.setCentroSalud(centro1);
		Sustitucion s;
		s = new Sustitucion(new Date(2010 - 1900, 1, 15), 9, 21, m2, m5);
		try {
			FPCentroSalud.insertar(centro2);
			FPUsuario.insertar(m1);
			FPUsuario.insertar(m2);
			FPUsuario.insertar(m3);
			FPUsuario.insertar(m4);
			FPUsuario.insertar(m5);
			FPSustitucion.insertar(s);
			Vector<Medico> m = GestorMedicos.obtenerPosiblesSustitutos(sesionAdmin.getId(), "1234", new Date(2010 - 1900, 1, 15), 11, 13);
			System.out.println(m.size());
			for(Medico ms:m)
			System.out.println(ms.getDni());
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	*/	
	
}
