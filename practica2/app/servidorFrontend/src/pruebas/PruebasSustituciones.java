package pruebas;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Vector;
import persistencia.FPCentroSalud;
import persistencia.FPSustitucion;
import persistencia.FPUsuario;
import dominio.UtilidadesDominio;
import dominio.conocimiento.Administrador;
import dominio.conocimiento.Cabecera;
import dominio.conocimiento.CentroSalud;
import dominio.conocimiento.Citador;
import dominio.conocimiento.DiaSemana;
import dominio.conocimiento.Especialista;
import dominio.conocimiento.ICodigosMensajeAuxiliar;
import dominio.conocimiento.ICodigosOperacionesCliente;
import dominio.conocimiento.ISesion;
import dominio.conocimiento.Medico;
import dominio.conocimiento.Pediatra;
import dominio.conocimiento.PeriodoTrabajo;
import dominio.conocimiento.Sesion;
import dominio.conocimiento.Sustitucion;
import dominio.control.GestorSesiones;
import dominio.control.ServidorFrontend;
import excepciones.FechaNoValidaException;
import excepciones.MedicoInexistenteException;
import excepciones.OperacionIncorrectaException;
import excepciones.SesionInvalidaException;
import excepciones.SustitucionInvalidaException;

/**
 * Pruebas del Gestor de Sustituciones.
 */
public class PruebasSustituciones extends PruebasBase {

	private ServidorFrontend servidor;
	private CentroSalud centro1, centro2;
	private Medico medico1, medico2;
	private Citador citador1;
	private Administrador admin1;
	private ISesion sesionCitador;
	private ISesion sesionAdmin;
	private Pediatra pediatra;
	private Cabecera cabecera;
	private ClientePrueba clienteCitador, clienteAdmin;
	
	protected void setUp() {
		try {
			// Preparamos la base de datos
			super.setUp();
			// Obtenemos el servidor frontend, que se utilizará para llamar
			// a los métodos del gestor y así probar las dos clases a la vez
			servidor = ServidorFrontend.getServidor();
			//Inicializamos los tipos de medicos
			pediatra = new Pediatra();
			cabecera = new Cabecera();
			// Creamos objetos de prueba
			centro1 = new CentroSalud("Centro Provincial", "Calle Ninguna, s/n");
			centro2 = new CentroSalud("Centro Nuevo", "Calle Ninguna más, s/n");
			medico1 = new Medico("12345678", "medPrueba", UtilidadesDominio.encriptarPasswordSHA1("abcdef"), "Eduardo", "P. C.", "", "", "", pediatra);
			medico2 = new Medico("87654321", "medico2", UtilidadesDominio.encriptarPasswordSHA1("xxx"), "Carmen", "G. G.", "carmen@gmail.com", "", "", cabecera);
			citador1 = new Citador("11223344", "citador", UtilidadesDominio.encriptarPasswordSHA1("cit123"), "Fernando", "G. P.", "fernan@gmail.com", "", "681871340");
			admin1 = new Administrador("55667788", "admin", UtilidadesDominio.encriptarPasswordSHA1("admin"), "María", "L. F.", "", "911222333", "");
			medico1.setCentroSalud(centro1);
			medico2.setCentroSalud(centro1);
			citador1.setCentroSalud(centro1);
			admin1.setCentroSalud(centro1);
			FPCentroSalud.insertar(centro1);
			FPCentroSalud.insertar(centro2);
			FPUsuario.insertar(medico1);
			FPUsuario.insertar(medico2);
			FPUsuario.insertar(citador1);
			FPUsuario.insertar(admin1);
			// Iniciamos dos sesiones con roles de citador y administrador
			sesionCitador = GestorSesiones.identificarUsuario(citador1.getLogin(), "cit123");
			sesionAdmin = GestorSesiones.identificarUsuario(admin1.getLogin(), "admin");
			// Registramos dos clientes
			clienteCitador = new ClientePrueba();
			clienteAdmin = new ClientePrueba();
			clienteCitador.activar(IDatosPruebas.IP_ESCUCHA_CLIENTES);
			clienteAdmin.activar(IDatosPruebas.IP_ESCUCHA_CLIENTES);
			GestorSesiones.registrar(sesionCitador.getId(), clienteCitador);
			GestorSesiones.registrar(sesionAdmin.getId(), clienteAdmin);
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	protected void tearDown() {
		try {
			// Cerramos la sesión
			GestorSesiones.liberar(((Sesion)sesionCitador).getId());
			GestorSesiones.liberar(((Sesion)sesionAdmin).getId());
			// Cerramos la base de datos
			super.tearDown();
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	/** Pruebas de la operación que obtiene una lista de posibles sustitutos */
	@SuppressWarnings("unchecked")
	public void testObtenerPosiblesSustitutos() {
		Vector<Medico> medicos;
		Medico m1 = null, m2 = null, m3 = null, m4 = null, m5 = null, m6 = null, medico;
		Sustitucion sustitucion;
		Calendar calend;
		
		try {
			// Intentamos obtener los sustitutos de un médico con una sesión sin permisos
			servidor.mensajeAuxiliar(sesionCitador.getId(), ICodigosMensajeAuxiliar.OBTENER_POSIBLES_SUSTITUTOS, new Object[] { medico1.getNif(), new Date(), 0, 0 });
			fail("Se esperaba una excepción OperacionIncorrectaException");
		} catch(OperacionIncorrectaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción OperacionIncorrectaException");
		}
		
		try {
			// Intentamos acceder al servidor con un id de sesión erróneo
			servidor.mensajeAuxiliar(-12345, ICodigosMensajeAuxiliar.OBTENER_POSIBLES_SUSTITUTOS, new Object[] { medico1.getNif(), new Date(), 0, 0 });
			fail("Se esperaba una excepción SesionInvalidaException");
		} catch(SesionInvalidaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción SesionInvalidaException");
		}
		
		try {
			// Intentamos obtener los sustitutos de un médico inexistente
			medico = new Medico("91295019", "otro2", UtilidadesDominio.encriptarPasswordSHA1("otro"), "Anaasa", "R. M.", "anaasa@uclm.es", "", "", cabecera);
			servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.OBTENER_POSIBLES_SUSTITUTOS, new Object[] { medico.getNif(), new Date(), 0, 0 });
			fail("Se esperaba una excepción MedicoInexistenteException");
		} catch(MedicoInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción MedicoInexistenteException");
		}
		
		try {
			// Intentamos obtener los sustitutos de un usuario que no es médico
			servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.OBTENER_POSIBLES_SUSTITUTOS, new Object[] { admin1.getNif(), new Date(), 0, 0 });
			fail("Se esperaba una excepción MedicoInexistenteException");
		} catch(MedicoInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción MedicoInexistenteException");
		}
		
		try {
			// Intentamos obtener los sustitutos de un médico en un día anterior a hoy
			calend = Calendar.getInstance();
			calend.add(Calendar.DAY_OF_MONTH, -3);
			servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.OBTENER_POSIBLES_SUSTITUTOS, new Object[] { medico1.getNif(), calend.getTime(), 9, 19 });
			fail("Se esperaba una excepción FechaNoValidaException");
		} catch(FechaNoValidaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción FechaNoValidaException");
		}
		
		try {
			// Intentamos obtener los sustitutos de un médico en un rango de horas no válido
			servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.OBTENER_POSIBLES_SUSTITUTOS, new Object[] { medico1.getNif(), new Date(), 5, 19 });
			fail("Se esperaba una excepción FechaNoValidaException");
		} catch(FechaNoValidaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción FechaNoValidaException");
		}

		try {
			// Intentamos obtener los sustitutos de un médico en un rango de horas no válido
			servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.OBTENER_POSIBLES_SUSTITUTOS, new Object[] { medico1.getNif(), new Date(), 9, 23 });
			fail("Se esperaba una excepción FechaNoValidaException");
		} catch(FechaNoValidaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción FechaNoValidaException");
		}
		
		try {
			// Intentamos obtener los sustitutos de un médico en un rango de horas no válido
			servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.OBTENER_POSIBLES_SUSTITUTOS, new Object[] { medico1.getNif(), new Date(), 19, 9 });
			fail("Se esperaba una excepción FechaNoValidaException");
		} catch(FechaNoValidaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción FechaNoValidaException");
		}
		
		try {
			// Intentamos obtener los sustitutos de un médico en una fecha que no trabaja
			calend = new GregorianCalendar(2015, 1, 11); // Miércoles 11/2/2015
			servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.OBTENER_POSIBLES_SUSTITUTOS, new Object[] { medico1.getNif(), calend.getTime(), 16, 19 });
			fail("Se esperaba una excepción FechaNoValidaException");
		} catch(FechaNoValidaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción FechaNoValidaException");
		}
		
		try {
			// Creamos varios médicos de prueba
			m1 = new Medico("11111111A", "aaaa", "1234", "abc", "abc", "", "", "", new Especialista("ABC"));
			m1.setCentroSalud(centro1);
			m1.getCalendario().add(new PeriodoTrabajo(10, 14, DiaSemana.Lunes));
			m1.getCalendario().add(new PeriodoTrabajo(9, 20, DiaSemana.Miercoles));
			m2 = new Medico("22222222A", "bbbb", "1234", "abc", "abc", "", "", "", new Especialista("ABC"));
			m2.setCentroSalud(centro1);
			m2.getCalendario().add(new PeriodoTrabajo(10, 11, DiaSemana.Lunes));
			m2.getCalendario().add(new PeriodoTrabajo(13, 18, DiaSemana.Lunes));
			m2.getCalendario().add(new PeriodoTrabajo(15, 20, DiaSemana.Martes));
			m3 = new Medico("33333333A", "cccc", "1234", "abc", "abc", "", "", "", new Especialista("ABC"));
			m3.setCentroSalud(centro1);
			m3.getCalendario().add(new PeriodoTrabajo(9, 20, DiaSemana.Lunes));
			m3.getCalendario().add(new PeriodoTrabajo(14, 20, DiaSemana.Jueves));
			m4 = new Medico("44444444A", "dddd", "1234", "abc", "abc", "", "", "", new Pediatra());
			m4.getCalendario().add(new PeriodoTrabajo(10, 13, DiaSemana.Lunes));
			m4.getCalendario().add(new PeriodoTrabajo(17, 18, DiaSemana.Miercoles));
			m4.setCentroSalud(centro1);
			m5 = new Medico("55555555A", "eeee", "1234", "abc", "abc", "", "", "", new Especialista("XYZ"));
			m5.getCalendario().add(new PeriodoTrabajo(9, 20, DiaSemana.Martes));
			m5.setCentroSalud(centro1); 
			m6 = new Medico("66666666A", "ffff", "1234", "abc", "abc", "", "", "", new Especialista("ABC"));
			m6.setCentroSalud(centro2);
			FPUsuario.insertar(m1);
			FPUsuario.insertar(m2);
			FPUsuario.insertar(m3);
			FPUsuario.insertar(m4);
			FPUsuario.insertar(m5);
			FPUsuario.insertar(m6);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Intentamos obtener los sustitutos de un médico que ya está siendo
			// sustituido en parte de las horas indicadas
			calend = new GregorianCalendar(2015, 1, 12); // Jueves 12/2/2015
			sustitucion = new Sustitucion(calend.getTime(), 15, 18, m3, m6);
			FPSustitucion.insertar(sustitucion);
			medicos = (Vector<Medico>)servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.OBTENER_POSIBLES_SUSTITUTOS, new Object[] { m3.getNif(), calend.getTime(), 16, 18 });
			fail("Se esperaba una excepción SustitucionInvalidaException");
		} catch(SustitucionInvalidaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción SustitucionInvalidaException");
		}
		
		try {
			// Intentamos obtener los sustitutos de un médico que ya está
			// sustituyendo a otro en parte de las horas indicadas
			calend = new GregorianCalendar(2015, 1, 11); // Miércoles 11/2/2015
			sustitucion = new Sustitucion(calend.getTime(), 15, 18, m1, m4);
			FPSustitucion.insertar(sustitucion);
			medicos = (Vector<Medico>)servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.OBTENER_POSIBLES_SUSTITUTOS, new Object[] { m4.getNif(), calend.getTime(), 17, 20 });
			fail("Se esperaba una excepción SustitucionInvalidaException");
		} catch(SustitucionInvalidaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción SustitucionInvalidaException");
		}
		
		try {
			// Buscamos los sustitutos posibles para el médico m1 un lunes de 10 a 11
			calend = new GregorianCalendar(2015, 1, 9); // Lunes 9/2/2015
			medicos = (Vector<Medico>)servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.OBTENER_POSIBLES_SUSTITUTOS, new Object[] { m1.getNif(), calend.getTime(), 10, 11 });
			assertTrue(medicos.size() == 0);
			// Buscamos los sustitutos posibles para el médico m1 un lunes de 13 a 15
			calend = new GregorianCalendar(2015, 1, 9); // Lunes 9/2/2015
			medicos = (Vector<Medico>)servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.OBTENER_POSIBLES_SUSTITUTOS, new Object[] { m1.getNif(), calend.getTime(), 13, 15 });
			assertTrue(medicos.size() == 0);
			// Buscamos los sustitutos posibles para el médico m1 un lunes de 11 a 13
			calend = new GregorianCalendar(2015, 1, 9); // Lunes 9/2/2015
			medicos = (Vector<Medico>)servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.OBTENER_POSIBLES_SUSTITUTOS, new Object[] { m1.getNif(), calend.getTime(), 11, 13 });
			assertTrue(medicos.size() == 1);
			assertTrue(medicos.get(0).equals(m2));
			// Buscamos los sustitutos posibles para el médico m2 un martes de 14 a 16
			calend = new GregorianCalendar(2015, 1, 10); // Martes 10/2/2015
			medicos = (Vector<Medico>)servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.OBTENER_POSIBLES_SUSTITUTOS, new Object[] { m2.getNif(), calend.getTime(), 14, 16 });
			assertTrue(medicos.size() == 2);
			assertTrue((medicos.get(0).equals(m1) && medicos.get(1).equals(m3))
			            || (medicos.get(0).equals(m3) && medicos.get(1).equals(m1)));
			// Añadimos una sustitución para el médico m3 el martes de 14 a 15
			// y volvemos a recuperar los sustitutos posibles, que deberán ser
			// los mismos porque el médico m4 no trabaja de 14 a 15
			calend = new GregorianCalendar(2015, 1, 10); // Martes 10/2/2015
			sustitucion = new Sustitucion(calend.getTime(), 14, 15, m4, m3);
			FPSustitucion.insertar(sustitucion);
			medicos = (Vector<Medico>)servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.OBTENER_POSIBLES_SUSTITUTOS, new Object[] { m2.getNif(), calend.getTime(), 14, 16 });
			assertTrue(medicos.size() == 2);
			assertTrue((medicos.get(0).equals(m1) && medicos.get(1).equals(m3))
			            || (medicos.get(0).equals(m3) && medicos.get(1).equals(m1)));
			// Añadimos una sustitución para el médico m3 el martes de 14 a 15
			// y volvemos a recuperar los sustitutos posibles, que ahora ya no
			// tendrá a m3 porque debe pasar citas de m5 de 14 a 15
			calend = new GregorianCalendar(2015, 1, 10); // Martes 10/2/2015
			sustitucion = new Sustitucion(calend.getTime(), 14, 15, m5, m3);
			FPSustitucion.insertar(sustitucion);
			medicos = (Vector<Medico>)servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.OBTENER_POSIBLES_SUSTITUTOS, new Object[] { m2.getNif(), calend.getTime(), 14, 16 });
			assertTrue(medicos.size() == 1);
			assertTrue(medicos.get(0).equals(m1));
			// Añadimos una sustitución del médico 1 el martes de 14 a 15
			// y volvemos a recuperar los sustitutos posibles, que ahora tampoco
			// tendrá a m1 porque ese médico estará siendo sustituido de 14 a 15
			calend = new GregorianCalendar(2015, 1, 10); // Martes 10/2/2015
			sustitucion = new Sustitucion(calend.getTime(), 14, 15, m1, m6);
			FPSustitucion.insertar(sustitucion);
			medicos = (Vector<Medico>)servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.OBTENER_POSIBLES_SUSTITUTOS, new Object[] { m2.getNif(), calend.getTime(), 14, 16 });
			assertTrue(medicos.size() == 0);
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	/** Pruebas de la operación que establece un nuevo sustituto para un médico */
	@SuppressWarnings("deprecation")
	public void testEstablecerSustituto() {
		Vector<Date> dias;
		Vector<Sustitucion> sustituciones;
		Sustitucion sustitucion;
		Calendar calend;
		Medico m1 = null, m2 = null, m3 = null, m4 = null, m5 = null, m6 = null, medico;
		
		try {
			// Intentamos asignar un sustituto con una sesión sin permisos
			servidor.modificarCalendario(sesionCitador.getId(), medico1, new Vector<Date>(), new Date(), new Date(), medico2);
			fail("Se esperaba una excepción OperacionIncorrectaException");
		} catch(OperacionIncorrectaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción OperacionIncorrectaException");
		}
		
		try {
			// Intentamos acceder al servidor con un id de sesión erróneo
			servidor.modificarCalendario(-12345, medico1, new Vector<Date>(), new Date(), new Date(), medico2);
			fail("Se esperaba una excepción SesionInvalidaException");
		} catch(SesionInvalidaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción SesionInvalidaException");
		}
		
		try {
			// Intentamos asignar un sustituto a un médico inexistente
			medico = new Medico("91295019", "otro2", UtilidadesDominio.encriptarPasswordSHA1("otro"), "Anaasa", "R. M.", "anaasa@uclm.es", "", "", cabecera);
			servidor.modificarCalendario(sesionAdmin.getId(), medico, new Vector<Date>(), new Date(), new Date(), medico2);
			fail("Se esperaba una excepción MedicoInexistenteException");
		} catch(MedicoInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción MedicoInexistenteException");
		}
		
		try {
			// Intentamos asignar un sustituto inexsitente
			medico = new Medico("91295019", "otro2", UtilidadesDominio.encriptarPasswordSHA1("otro"), "Anaasa", "R. M.", "anaasa@uclm.es", "", "", cabecera);
			servidor.modificarCalendario(sesionAdmin.getId(), medico1, new Vector<Date>(), new Date(), new Date(), medico);
			fail("Se esperaba una excepción MedicoInexistenteException");
		} catch(MedicoInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción MedicoInexistenteException");
		}
		
		try {
			// Intentamos asignar un sustituto en un día anterior a hoy
			calend = Calendar.getInstance();
			calend.add(Calendar.DAY_OF_MONTH, -3);
			dias = new Vector<Date>();
			dias.add(calend.getTime());
			servidor.modificarCalendario(sesionAdmin.getId(), medico1, dias, new Date(), new Date(), medico2);
			fail("Se esperaba una excepción FechaNoValidaException");
		} catch(FechaNoValidaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción FechaNoValidaException");
		}
		
		try {
			// Creamos varios médicos de prueba
			m1 = new Medico("11111111A", "aaaa", "1234", "abc", "abc", "", "", "", new Especialista("ABC"));
			m1.setCentroSalud(centro1);
			m1.getCalendario().add(new PeriodoTrabajo(10, 14, DiaSemana.Lunes));
			m1.getCalendario().add(new PeriodoTrabajo(9, 20, DiaSemana.Miercoles));
			m2 = new Medico("22222222A", "bbbb", "1234", "abc", "abc", "", "", "", new Especialista("ABC"));
			m2.setCentroSalud(centro1);
			m2.getCalendario().add(new PeriodoTrabajo(10, 11, DiaSemana.Lunes));
			m2.getCalendario().add(new PeriodoTrabajo(13, 18, DiaSemana.Lunes));
			m2.getCalendario().add(new PeriodoTrabajo(15, 20, DiaSemana.Martes));
			m3 = new Medico("33333333A", "cccc", "1234", "abc", "abc", "", "", "", new Especialista("ABC"));
			m3.setCentroSalud(centro1);
			m3.getCalendario().add(new PeriodoTrabajo(9, 20, DiaSemana.Lunes));
			m3.getCalendario().add(new PeriodoTrabajo(14, 20, DiaSemana.Jueves));
			m4 = new Medico("44444444A", "dddd", "1234", "abc", "abc", "", "", "", new Pediatra());
			m4.getCalendario().add(new PeriodoTrabajo(10, 13, DiaSemana.Lunes));
			m4.setCentroSalud(centro1);
			m5 = new Medico("55555555A", "eeee", "1234", "abc", "abc", "", "", "", new Especialista("XYZ"));
			m5.getCalendario().add(new PeriodoTrabajo(9, 20, DiaSemana.Martes));
			m5.setCentroSalud(centro1); 
			m6 = new Medico("66666666A", "ffff", "1234", "abc", "abc", "", "", "", new Especialista("ABC"));
			m6.setCentroSalud(centro2);
			FPUsuario.insertar(m1);
			FPUsuario.insertar(m2);
			FPUsuario.insertar(m3);
			FPUsuario.insertar(m4);
			FPUsuario.insertar(m5);
			FPUsuario.insertar(m6);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Intentamos asignar a un médico un sustituto que no es elegible
			calend = new GregorianCalendar(2015, 1, 10); // Martes 10/2/2015
			dias = new Vector<Date>();
			dias.add(calend.getTime());
			servidor.modificarCalendario(sesionAdmin.getId(), m2, dias, new Date(2010 - 1900, 0, 1, 14, 0), new Date(2010 - 1900, 0, 1, 16, 0), m5);
			fail("Se esperaba una excepción SustitucionInvalidaException");
		} catch(SustitucionInvalidaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción SustitucionInvalidaException");
		}
		
		try {
			// Asignamos una sustitución válida
			calend = new GregorianCalendar(2015, 1, 9); // Lunes 9/2/2015
			dias = new Vector<Date>();
			dias.add(calend.getTime());
			servidor.modificarCalendario(sesionAdmin.getId(), m1, dias, new Date(2010 - 1900, 0, 1, 11, 0), new Date(2010 - 1900, 0, 1, 13, 0), m2);
			// Comprobamos que la sustitución se ha añadido
			sustituciones = FPSustitucion.consultarPorSustituido(m1.getNif());
			sustitucion = new Sustitucion(calend.getTime(), 11, 13, m1, m2);
			assertTrue(sustituciones.contains(sustitucion));
			// Comprobamos que se ha avisado a los clientes de la nueva sustitución
			Thread.sleep(100);
			assertTrue(clienteCitador.getUltimaOperacion() == ICodigosOperacionesCliente.INSERTAR);
			assertEquals(sustitucion, clienteCitador.getUltimoDato());
			assertNull(clienteAdmin.getUltimoDato());
		} catch(Exception e) {
			fail(e.toString());
		}
	}

	/** Pruebas de las operaciones de la clase Sustitucion */
	@SuppressWarnings("deprecation")
	public void testClaseSustitucion() {
		Sustitucion sustitucion;
		
		try {
			// Comprobamos que se detecta bien si las horas de la
			// sustitución están dentro de un determinado rango de horas
			sustitucion = new Sustitucion(new Date(2010 - 1900, 5, 5), 10, 12, medico1, medico2);
			assertTrue(sustitucion.horaEnSustitucion(10, 11));
			assertTrue(sustitucion.horaEnSustitucion(11, 12));
			assertTrue(sustitucion.horaEnSustitucion(9, 13));
			sustitucion = new Sustitucion(new Date(2010 - 1900, 5, 5), 16, 20, medico1, medico2);
			assertFalse(sustitucion.horaEnSustitucion(14, 16));
			assertFalse(sustitucion.horaEnSustitucion(20, 22));
			sustitucion = new Sustitucion(new Date(2010 - 1900, 5, 5), 9, 14, medico1, medico2);
			assertTrue(sustitucion.horaEnSustitucion(new Date(2010 - 1900, 5, 5, 9, 30)));
			assertTrue(sustitucion.horaEnSustitucion(new Date(2010 - 1900, 5, 5, 13, 25)));
			assertFalse(sustitucion.horaEnSustitucion(new Date(2010 - 1900, 5, 5, 14, 0)));
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	/** Pruebas con datos nulos */
	public void testDatosNulos() {
		Vector<Date> dias;
		
		try {
			// Intentamos obtener los sustitutos de un médico nulo
			servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.OBTENER_POSIBLES_SUSTITUTOS, new Object[] { null, null, 0, 0 });
			fail("Se esperaba una excepción NullPointerException");
		} catch(NullPointerException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción NullPointerException");
		}		

		try {
			// Intentamos obtener los sustitutos de un médico en un día nulo
			servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.OBTENER_POSIBLES_SUSTITUTOS, new Object[] { medico1.getNif(), null, 0, 0 });
			fail("Se esperaba una excepción NullPointerException");
		} catch(NullPointerException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción NullPointerException");
		}
	
		try {
			// Intentamos asignar un sustituto a un médico nulo
			servidor.modificarCalendario(sesionAdmin.getId(), null, new Vector<Date>(), new Date(), new Date(), medico2);
			fail("Se esperaba una excepción NullPointerException");
		} catch(NullPointerException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción NullPointerException");
		}

		try {
			// Intentamos asignar un sustituto en días nulos
			servidor.modificarCalendario(sesionAdmin.getId(), medico1, null, new Date(), new Date(), medico2);
			fail("Se esperaba una excepción NullPointerException");
		} catch(NullPointerException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción NullPointerException");
		}

		try {
			// Intentamos asignar un sustituto en un día nulo
			dias = new Vector<Date>();
			dias.add(null);
			servidor.modificarCalendario(sesionAdmin.getId(), medico1, dias, new Date(), new Date(), medico2);
			fail("Se esperaba una excepción NullPointerException");
		} catch(NullPointerException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción NullPointerException");
		}

		try {
			// Intentamos asignar un sustituto en un rango nulo
			servidor.modificarCalendario(sesionAdmin.getId(), medico1, new Vector<Date>(), null, new Date(), medico2);
			fail("Se esperaba una excepción NullPointerException");
		} catch(NullPointerException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción NullPointerException");
		}

		try {
			// Intentamos asignar un sustituto en un rango nulo
			servidor.modificarCalendario(sesionAdmin.getId(), medico1, new Vector<Date>(), new Date(), null, medico2);
			fail("Se esperaba una excepción NullPointerException");
		} catch(NullPointerException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción NullPointerException");
		}
		
		try {
			// Intentamos asignar un sustituto nulo
			dias = new Vector<Date>();
			servidor.modificarCalendario(sesionAdmin.getId(), medico1, new Vector<Date>(), new Date(), new Date(), null);
			fail("Se esperaba una excepción NullPointerException");
		} catch(NullPointerException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción NullPointerException");
		}
	}
	
}
