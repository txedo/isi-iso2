package pruebas;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Vector;
import persistencia.FPBeneficiario;
import persistencia.FPCentroSalud;
import persistencia.FPCita;
import persistencia.FPSustitucion;
import persistencia.FPUsuario;
import persistencia.FPVolante;
import dominio.UtilidadesDominio;
import dominio.conocimiento.Administrador;
import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.Cabecera;
import dominio.conocimiento.CentroSalud;
import dominio.conocimiento.Cita;
import dominio.conocimiento.Citador;
import dominio.conocimiento.DiaSemana;
import dominio.conocimiento.Direccion;
import dominio.conocimiento.Encriptacion;
import dominio.conocimiento.Especialista;
import dominio.conocimiento.ICodigosMensajeAuxiliar;
import dominio.conocimiento.ISesion;
import dominio.conocimiento.Medico;
import dominio.conocimiento.Pediatra;
import dominio.conocimiento.PeriodoTrabajo;
import dominio.conocimiento.Sesion;
import dominio.conocimiento.Sustitucion;
import dominio.conocimiento.Usuario;
import dominio.conocimiento.Volante;
import dominio.control.GestorSesiones;
import dominio.control.ServidorFrontend;
import excepciones.BeneficiarioInexistenteException;
import excepciones.CitaNoValidaException;
import excepciones.FechaNoValidaException;
import excepciones.MedicoInexistenteException;
import excepciones.OperacionIncorrectaException;
import excepciones.SesionInvalidaException;
import excepciones.VolanteNoValidoException;

/**
 * Pruebas del Gestor de Citas.
 */
public class PruebasCitas extends PruebasBase {

	private ServidorFrontend servidor;
	private CentroSalud centro1, centro2;
	private Medico medico1, medico2, medico3, medico4;
	private Citador citador1;
	private Administrador administrador1;
	private Beneficiario beneficiario1, beneficiario2, beneficiario3;
	private Usuario usuario1;
	private Direccion direccion1, direccion2;
	private PeriodoTrabajo periodo1, periodo2, periodo3;
	private ISesion sesionCitador;
	private ISesion sesionAdmin;
	private ISesion sesionMedico;
	private Pediatra pediatra;
	private Especialista especialista;
	private Cabecera cabecera;
	private Date fecha1, fecha2;
	private Date fechaCita1, fechaCita2, fechaCita3, fechaCita4;
	private Volante volante1, volante2;
	private GregorianCalendar calendar;
	private final long DURACION = 15;
	
	protected void setUp() {
		try {
			// Preparamos la base de datos
			super.setUp();
			// Obtenemos el servidor frontend, que se utilizará para llamar
			// a los métodos del gestor y así probar las dos clases a la vez
			servidor = ServidorFrontend.getServidor();
			// Inicializamos fechas de nacimiento para los beneficiarios
			// y las fechas de las citas
			SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd/MM/yyyy");
			fecha1 = formatoDelTexto.parse("1/8/1951");
			fecha2 = formatoDelTexto.parse("23/2/2002");
			// Se usa el gregorianCalendar para no restar 1900 al año de la fecha de tipo Date
			// Al mes hay que restar 1, es decir, Enero seria el mes 0
			// El formato del gregorianCalendar es: año,mes,dia,hora,minutos
			// Esta fecha de cita es valida para el medico1 e invalida para el medico2
			// La fecha seria el miercoles 13 de Enero de 2016, a las 10:30
			calendar = new GregorianCalendar(2016,1-1,13,10,30);
			fechaCita1 = calendar.getTime();
			// Fecha de cita valida para el medico2 e invalida para el medico1
			// La cita seria el lunes 11 de Enero de 2016, a las 16:15
			calendar = new GregorianCalendar(2016,1-1,11,16,15);
			fechaCita2 = calendar.getTime();
			// Fecha no valida
			calendar = new GregorianCalendar(2016,1-1,11,16,19);
			fechaCita3 = calendar.getTime();
			// Fecha anterior a la actual
			calendar = new GregorianCalendar(2009,1-1,11,16,19);
			fechaCita4 = calendar.getTime();
			// Inicializamos los tipos de medicos
			pediatra = new Pediatra();
			especialista = new Especialista("Traumatologia");
			cabecera = new Cabecera();
			// Creamos objetos de prueba
			centro1 = new CentroSalud("Centro A", "Calle 2, nº2");
			centro2 = new CentroSalud("Centro B", "Calle 4, nº4");
			medico1 = new Medico("12345678", "medico1", Encriptacion.encriptarPasswordSHA1("abcdef"), "Eduardo", "P. C.", "", "", "", pediatra);
			medico2 = new Medico("87654321", "medico2", Encriptacion.encriptarPasswordSHA1("xxx"), "Carmen", "G. G.", "carmen@gmail.com", "", "666123123", cabecera);
			medico3 = new Medico("34581732", "medico3", Encriptacion.encriptarPasswordSHA1("pass"), "nombre", "apellido", "", "999000000", "666111111", especialista);
			medico4 = new Medico("09761231", "medNoRegistrado", Encriptacion.encriptarPasswordSHA1("asas"), "E", "P", "", "", "", cabecera);
			citador1 = new Citador("11223344", "citador", Encriptacion.encriptarPasswordSHA1("cit123"), "Fernando", "G. P.", "", "", "");
			administrador1 = new Administrador("55667788", "admin", Encriptacion.encriptarPasswordSHA1("admin"), "María", "L. F.", "admin@terra.com", "911444111", "698098875");
			medico1.setCentroSalud(centro1);
			medico2.setCentroSalud(centro1);
			medico3.setCentroSalud(centro1);
			medico4.setCentroSalud(centro1);
			citador1.setCentroSalud(centro1);
			administrador1.setCentroSalud(centro1);
			periodo1 = new PeriodoTrabajo(10, 14, DiaSemana.Miercoles);
			periodo2 = new PeriodoTrabajo(17, 19, DiaSemana.Viernes);
			periodo3 = new PeriodoTrabajo(16, 17, DiaSemana.Lunes);
			medico1.getCalendario().add(periodo1);
			medico1.getCalendario().add(periodo2);
			medico2.getCalendario().add(periodo3);
			medico3.getCalendario().add(periodo3);
			usuario1 = new Administrador("04328172", "usuario", Encriptacion.encriptarPasswordSHA1("f"), "O", "C", "", "", "");
			usuario1.setCentroSalud(centro1);
			direccion1 = new Direccion("Avda. Mayor", "10", "4", "C", "Ciudad", "Provincia", 10234);
			direccion2 = new Direccion("Calle Principal", "5", "", "", "Ciudad", "Provincia", 10234);
			beneficiario1 = new Beneficiario("12345679S", "123456-ab", "bene1", "asdfg", fecha1, direccion1, "uno@gmail.com", "123456789", "987654321");
			beneficiario1.setCentroSalud(medico2.getCentroSalud());
			beneficiario1.setMedicoAsignado(medico2);
			beneficiario2 = new Beneficiario("46208746S", "164028-de", "bene2", "asadasdfg", fecha2, direccion2, "dos@gmail.com", "923456789", "687654322");
			beneficiario2.setCentroSalud(medico1.getCentroSalud());
			beneficiario2.setMedicoAsignado(medico1);
			beneficiario3 = new Beneficiario("63139010S", "134171-de", "bene3", "sdffsdsd", fecha2, direccion2, "tres@gmail.com", "923456789", "687654322");
			beneficiario3.setCentroSalud(centro2);
			volante1 = new Volante(medico1,medico3,beneficiario1,null,new Date());
			volante2 = new Volante(medico1,medico3,beneficiario1,null,new Date());
			FPCentroSalud.insertar(centro1);
			FPCentroSalud.insertar(centro2);
			FPUsuario.insertar(medico1);
			FPUsuario.insertar(medico2);
			FPUsuario.insertar(medico3);
			FPUsuario.insertar(citador1);
			FPUsuario.insertar(administrador1);
			FPUsuario.insertar(usuario1);
			FPBeneficiario.insertar(beneficiario1);
			FPBeneficiario.insertar(beneficiario3);
			FPVolante.insertar(volante1);
			FPVolante.insertar(volante2);
			// Iniciamos tres sesiones con roles de citador, administrador y medico
			sesionCitador = GestorSesiones.identificar(citador1.getLogin(), "cit123");
			sesionAdmin = GestorSesiones.identificar(administrador1.getLogin(), "admin");
			sesionMedico = GestorSesiones.identificar(medico1.getLogin(), "abcdef");
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	protected void tearDown() {
		try {
			// Cerramos las sesiones
			GestorSesiones.liberar(((Sesion)sesionCitador).getId());
			GestorSesiones.liberar(((Sesion)sesionAdmin).getId());
			GestorSesiones.liberar(((Sesion)sesionMedico).getId());
			// Cerramos la base de datos
			super.tearDown();
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	/** Pruebas de las operaciones que consultan las citas de un beneficiario */
	@SuppressWarnings("unchecked")
	public void testConsultarCitasBeneficiario() {
		Vector<Cita> citas;
		Medico medicoAsignado;
		Date fechaCitaPendiente;
		Cita citaPasada = null, citaPendiente = null;
		
		try {
			// Intentamos consultar todas las citas de un beneficiario con NIF nulo
			servidor.getCitas(sesionAdmin.getId(), null);
			fail("Se esperaba una excepción NullPointerException");
		} catch(NullPointerException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción NullPointerException");
		}
		
		try {
			// Intentamos consultar las citas pendientes de un beneficiario con NIF nulo
			servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CONSULTAR_CITAS_PENDIENTES, null);
			fail("Se esperaba una excepción NullPointerException");
		} catch(NullPointerException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción NullPointerException");
		}
		
		try {
			// Intentamos consultar las citas con una sesión sin permisos
			servidor.getCitas(sesionMedico.getId(), beneficiario1.getNif());
			fail("Se esperaba una excepción OperacionIncorrectaException");
		} catch(OperacionIncorrectaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción OperacionIncorrectaException");
		}
		
		try {
			// Intentamos consultar las citas pendientes con una sesión sin permisos
			servidor.mensajeAuxiliar(sesionMedico.getId(), ICodigosMensajeAuxiliar.CONSULTAR_CITAS_PENDIENTES, beneficiario1.getNif());
			fail("Se esperaba una excepción OperacionIncorrectaException");
		} catch(OperacionIncorrectaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción OperacionIncorrectaException");
		}
		
		try {
			// Intentamos acceder al servidor con un id de sesión erróneo
			servidor.getCitas(-12345, beneficiario1.getNif());
			fail("Se esperaba una excepción SesionInvalidaException");
		} catch(SesionInvalidaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción SesionInvalidaException");
		}
		
		try {
			// Intentamos acceder al servidor con un id de sesión erróneo
			servidor.mensajeAuxiliar(-12345, ICodigosMensajeAuxiliar.CONSULTAR_CITAS_PENDIENTES, beneficiario1.getNif());
			fail("Se esperaba una excepción SesionInvalidaException");
		} catch(SesionInvalidaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción SesionInvalidaException");
		}
		
		try {
			// Insertamos varias citas válidas, tanto pendientes como pasadas
			medicoAsignado = beneficiario1.getMedicoAsignado();
			if(medicoAsignado.getNif().equals(medico1.getNif())) {
				calendar = new GregorianCalendar(2015, 6 - 1, 12, 16, 15);
				fechaCitaPendiente = calendar.getTime();
			} else {
				calendar = new GregorianCalendar(2015, 6 - 1, 8, 16, 15);
				fechaCitaPendiente = calendar.getTime();
			}
			citaPendiente = servidor.pedirCita(sesionCitador.getId(), beneficiario1, medicoAsignado.getNif(), fechaCitaPendiente, DURACION);
			citaPasada = new Cita(fechaCita4, DURACION, beneficiario1, medicoAsignado);
			FPCita.insertar(citaPasada);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Consultamos todas las citas del beneficiario
			citas = servidor.getCitas(sesionCitador.getId(), beneficiario1.getNif());
			assertTrue(citas.size() == 2);
			assertTrue((citas.get(0).equals(citaPendiente) && citas.get(1).equals(citaPasada))
					   || (citas.get(0).equals(citaPasada) && citas.get(1).equals(citaPendiente)));
			// Consultamos sólo las citas pendientes del beneficiario
			citas = (Vector<Cita>)servidor.mensajeAuxiliar(sesionCitador.getId(), ICodigosMensajeAuxiliar.CONSULTAR_CITAS_PENDIENTES, beneficiario1.getNif());
			assertTrue(citas.size() == 1);
			assertTrue(citas.get(0).equals(citaPendiente));
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	/** Pruebas de las operaciones que consultan las citas de un médico */
	@SuppressWarnings("unchecked")
	public void testConsultarCitasMedico() {
		Vector<Cita> citas;
		Medico medico, medicoAsignado;
		Date fechaCitaPendiente;
		Cita citaPasada = null, citaPendiente = null, citaSustituta = null;
		Sustitucion sustitucion;
		
		try {
			// Intentamos consultar todas las citas de un médico con NIF nulo
			servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CONSULTAR_CITAS_MEDICO, null);
			fail("Se esperaba una excepción NullPointerException");
		} catch(NullPointerException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción NullPointerException");
		}
		
		try {
			// Intentamos consultar las citas pendientes de un médico con NIF nulo
			servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CONSULTAR_CITAS_PENDIENTES_MEDICO, null);
			fail("Se esperaba una excepción NullPointerException");
		} catch(NullPointerException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción NullPointerException");
		}
		
		try {
			// Intentamos consultar las citas en un rango de un médico con NIF nulo
			servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CONSULTAR_CITAS_FECHA_MEDICO, new Object[] { null, null, 0, 0 });
			fail("Se esperaba una excepción NullPointerException");
		} catch(NullPointerException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción NullPointerException");
		}
		
		try {
			// Intentamos consultar las citas de un médico en un rango nulo
			servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CONSULTAR_CITAS_FECHA_MEDICO, new Object[] { medico1.getNif(), null, 0, 0 });
			fail("Se esperaba una excepción NullPointerException");
		} catch(NullPointerException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción NullPointerException");
		}
		
		try {
			// Intentamos consultar las citas con una sesión sin permisos
			servidor.mensajeAuxiliar(sesionMedico.getId(), ICodigosMensajeAuxiliar.CONSULTAR_CITAS_MEDICO, medico1.getNif());
			fail("Se esperaba una excepción OperacionIncorrectaException");
		} catch(OperacionIncorrectaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción OperacionIncorrectaException");
		}
		
		try {
			// Intentamos consultar las citas pendientes con una sesión sin permisos
			servidor.mensajeAuxiliar(sesionMedico.getId(), ICodigosMensajeAuxiliar.CONSULTAR_CITAS_PENDIENTES_MEDICO, medico1.getNif());
			fail("Se esperaba una excepción OperacionIncorrectaException");
		} catch(OperacionIncorrectaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción OperacionIncorrectaException");
		}
		
		try {
			// Intentamos consultar las citas en un rango con una sesión sin permisos
			servidor.mensajeAuxiliar(sesionMedico.getId(), ICodigosMensajeAuxiliar.CONSULTAR_CITAS_FECHA_MEDICO, new Object[] { medico1.getNif(), new Date(), 10, 20 });
			fail("Se esperaba una excepción OperacionIncorrectaException");
		} catch(OperacionIncorrectaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción OperacionIncorrectaException");
		}
		
		try {
			// Intentamos acceder al servidor con un id de sesión erróneo
			servidor.mensajeAuxiliar(-12345, ICodigosMensajeAuxiliar.CONSULTAR_CITAS_MEDICO, medico1.getNif());
			fail("Se esperaba una excepción SesionInvalidaException");
		} catch(SesionInvalidaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción SesionInvalidaException");
		}
		
		try {
			// Intentamos acceder al servidor con un id de sesión erróneo
			servidor.mensajeAuxiliar(-12345, ICodigosMensajeAuxiliar.CONSULTAR_CITAS_PENDIENTES_MEDICO, medico1.getNif());
			fail("Se esperaba una excepción SesionInvalidaException");
		} catch(SesionInvalidaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción SesionInvalidaException");
		}
		
		try {
			// Intentamos acceder al servidor con un id de sesión erróneo
			servidor.mensajeAuxiliar(-12345, ICodigosMensajeAuxiliar.CONSULTAR_CITAS_FECHA_MEDICO, new Object[] { medico1.getNif(), new Date(), 10, 20 });
			fail("Se esperaba una excepción SesionInvalidaException");
		} catch(SesionInvalidaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción SesionInvalidaException");
		}
		
		try {
			// Intentamos consultar las citas de un médico que no existe
			medico = new Medico("11229933W", "error", "error", "", "", "", "", "", cabecera);
			servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CONSULTAR_CITAS_MEDICO, medico.getNif());
			fail("Se esperaba una excepción MedicoInexistenteException");
		} catch(MedicoInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción MedicoInexistenteException");
		}

		try {
			// Intentamos consultar las citas pendientes de un médico que no existe
			medico = new Medico("11229933W", "error", "error", "", "", "", "", "", cabecera);
			servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CONSULTAR_CITAS_PENDIENTES_MEDICO, medico.getNif());
			fail("Se esperaba una excepción MedicoInexistenteException");
		} catch(MedicoInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción MedicoInexistenteException");
		}

		try {
			// Intentamos consultar las citas en un rango de un médico que no existe
			medico = new Medico("11229933W", "error", "error", "", "", "", "", "", cabecera);
			servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CONSULTAR_CITAS_FECHA_MEDICO, new Object[] { medico.getNif(), new Date(), 10, 20 });
			fail("Se esperaba una excepción MedicoInexistenteException");
		} catch(MedicoInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción MedicoInexistenteException");
		}
		
		try {
			// Intentamos consultar las citas de un usuario que no es médico
			servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CONSULTAR_CITAS_MEDICO, administrador1.getNif());
			fail("Se esperaba una excepción MedicoInexistenteException");
		} catch(MedicoInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción MedicoInexistenteException");
		}

		try {
			// Intentamos consultar las citas pendientes de un usuario que no es médico
			servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CONSULTAR_CITAS_PENDIENTES_MEDICO, administrador1.getNif());
			fail("Se esperaba una excepción MedicoInexistenteException");
		} catch(MedicoInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción MedicoInexistenteException");
		}

		try {
			// Intentamos consultar las citas en un rango de un usuario que no es médico
			servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CONSULTAR_CITAS_FECHA_MEDICO, new Object[] { administrador1.getNif(), new Date(), 10, 20 });
			fail("Se esperaba una excepción MedicoInexistenteException");
		} catch(MedicoInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción MedicoInexistenteException");
		}
		
		try {
			// Insertamos varias citas válidas, tanto pendientes como pasadas
			medicoAsignado = beneficiario1.getMedicoAsignado();
			if(medicoAsignado.getNif().equals(medico1.getNif())) {
				calendar = new GregorianCalendar(2015, 5, 12, 16, 15);
				fechaCitaPendiente = calendar.getTime();
			} else {
				calendar = new GregorianCalendar(2015, 5, 8, 16, 15);
				fechaCitaPendiente = calendar.getTime();
			}
			citaPendiente = servidor.pedirCita(sesionCitador.getId(), beneficiario1, medicoAsignado.getNif(), fechaCitaPendiente, DURACION);
			citaPasada = new Cita(fechaCita4, DURACION, beneficiario1, medicoAsignado);
			FPCita.insertar(citaPasada);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Consultamos todas las citas del médico
			citas = (Vector<Cita>)servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CONSULTAR_CITAS_MEDICO, beneficiario1.getMedicoAsignado().getNif());
			assertTrue(citas.size() == 2);
			assertTrue((citas.get(0).equals(citaPendiente) && citas.get(1).equals(citaPasada))
					   || (citas.get(0).equals(citaPasada) && citas.get(1).equals(citaPendiente)));
			// Consultamos sólo las citas pendientes del beneficiario
			citas = (Vector<Cita>)servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CONSULTAR_CITAS_PENDIENTES_MEDICO, beneficiario1.getMedicoAsignado().getNif());
			assertTrue(citas.size() == 1);
			assertTrue(citas.get(0).equals(citaPendiente));
			// Consultamos las citas que queden dentro de un rango
			citas = (Vector<Cita>)servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CONSULTAR_CITAS_FECHA_MEDICO, new Object[] { beneficiario1.getMedicoAsignado().getNif(), new GregorianCalendar(2015, 5, 14).getTime(), 10, 20 });
			assertTrue(citas.size() == 0);
			citas = (Vector<Cita>)servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CONSULTAR_CITAS_FECHA_MEDICO, new Object[] { beneficiario1.getMedicoAsignado().getNif(), new GregorianCalendar(2015, 5, 8).getTime(), 10, 20 });
			assertTrue(citas.size() == 1);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Eliminamos las citas anteriores
			citas = (Vector<Cita>)servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CONSULTAR_CITAS_MEDICO, beneficiario1.getMedicoAsignado().getNif());
			for(Cita cita : citas) {
				FPCita.eliminar(cita);
			}
			// Insertamos varias citas pendientes nuevas
			citaPendiente = new Cita(new GregorianCalendar(2015, 2, 24, 10, 0).getTime(), DURACION, beneficiario1, medico1);
			FPCita.insertar(citaPendiente);
			citaPendiente = new Cita(new GregorianCalendar(2015, 2, 24, 20, 0).getTime(), DURACION, beneficiario1, medico2);
			FPCita.insertar(citaPendiente);
			citaSustituta = new Cita(new GregorianCalendar(2015, 2, 24, 15, 0).getTime(), DURACION, beneficiario1, medico1);
			FPCita.insertar(citaSustituta);
			// Añadimos una sustitución para que medico2 tenga que sustituir
			// a medico1 en la cita de las 15:00
			sustitucion = new Sustitucion(new GregorianCalendar(2015, 2, 24).getTime(), 12, 18, medico1, medico2);
			FPSustitucion.insertar(sustitucion);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Comprobamos que la cita que va a sustituir medico2 aparece en la
			// lista de citas de medico2 y no en la de medico1
			citas = (Vector<Cita>)servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CONSULTAR_CITAS_MEDICO, medico1.getNif());
			assertFalse(citas.contains(citaSustituta));
			citas = (Vector<Cita>)servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CONSULTAR_CITAS_MEDICO, medico2.getNif());
			assertTrue(citas.contains(citaSustituta));
			citas = (Vector<Cita>)servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CONSULTAR_CITAS_PENDIENTES_MEDICO, medico1.getNif());
			assertFalse(citas.contains(citaSustituta));
			citas = (Vector<Cita>)servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CONSULTAR_CITAS_PENDIENTES_MEDICO, medico2.getNif());
			assertTrue(citas.contains(citaSustituta));
		} catch(Exception e) {
			fail(e.toString());
		}
	}

	/** Pruebas de la operación que solicita una cita para el médico de cabecera */
	public void testPedirCitaSinVolante() {	
		Vector<Cita> citas;
		Medico medicoAsignado;
		Date fechaCita;
		
		try {
			// Intentamos pedir cita con un beneficiario nulo
			servidor.pedirCita(sesionAdmin.getId(), null, beneficiario1.getMedicoAsignado().getNif(), fecha1, DURACION);
			fail("Se esperaba una excepción NullPointerException");
		} catch(NullPointerException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción NullPointerException");
		}
		
		try {
			// Intentamos pedir cita con un médico nulo
			servidor.pedirCita(sesionAdmin.getId(), beneficiario1, null, fecha1, DURACION);
			fail("Se esperaba una excepción NullPointerException");
		} catch(NullPointerException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción NullPointerException");
		}
		
		try {
			// Intentamos pedir cita con una fecha nula
			servidor.pedirCita(sesionAdmin.getId(), beneficiario1, beneficiario1.getMedicoAsignado().getNif(), null, DURACION);
			fail("Se esperaba una excepción NullPointerException");
		} catch(NullPointerException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción NullPointerException");
		}
		
		try {
			// Intentamos pedir cita con una sesión sin permisos
			servidor.pedirCita(sesionMedico.getId(), beneficiario1, beneficiario1.getMedicoAsignado().getNif(), fecha1, DURACION);
			fail("Se esperaba una excepción OperacionIncorrectaException");
		} catch(OperacionIncorrectaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción OperacionIncorrectaException");
		}
		
		try {
			// Intentamos acceder al servidor con un id de sesión erróneo
			servidor.pedirCita(-12345, beneficiario1, beneficiario1.getMedicoAsignado().getNif(), fecha1, DURACION);
			fail("Se esperaba una excepción SesionInvalidaException");
		} catch(SesionInvalidaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción SesionInvalidaException");
		}
		
		try {
			// Intentamos dar una cita a un beneficiario no registrado 
			servidor.pedirCita(sesionCitador.getId(), beneficiario2, medico1.getNif(), fecha1, DURACION);
			fail("Se esperaba una excepción BeneficiarioInexistenteException");
		} catch(BeneficiarioInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción BeneficiarioInexistenteException");
		}
		
		try {
			// Intentamos dar una cita a un beneficiario sin médico asignado 
			servidor.pedirCita(sesionCitador.getId(), beneficiario3, medico1.getNif(), fecha1, DURACION);
			fail("Se esperaba una excepción MedicoInexistenteException");
		} catch(MedicoInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción MedicoInexistenteException");
		}
		
		try {
			// Intentamos dar una cita con un médico no registrado
			servidor.pedirCita(sesionCitador.getId(), beneficiario1, medico4.getNif(), fecha1, DURACION);
			fail("Se esperaba una excepción MedicoInexistenteException");
		} catch(MedicoInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción MedicoInexistenteException");
		}
		
		try {
			// Intentamos dar una cita con un usuario que no es médico
			servidor.pedirCita(sesionCitador.getId(), beneficiario1, administrador1.getNif(), fecha1, DURACION);
			fail("Se esperaba una excepción MedicoInexistenteException");
		} catch(MedicoInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción MedicoInexistenteException");
		}
		
		try {
			// Intentamos dar una cita a un beneficiario con un médico
			// que no es el que tiene asignado actualmente
			medicoAsignado = beneficiario1.getMedicoAsignado();
			medicoAsignado = (medicoAsignado.equals(medico1)) ? medico2 : medico1;
			servidor.pedirCita(sesionCitador.getId(), beneficiario1, medicoAsignado.getNif(), fecha1, DURACION);
			fail("Se esperaba una excepción CitaNoValidaException");
		} catch(CitaNoValidaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción CitaNoValidaException");
		}
		
		try {
			// Intentamos dar una cita para una fecha anterior a hoy
			servidor.pedirCita(sesionCitador.getId(), beneficiario1, beneficiario1.getMedicoAsignado().getNif(), fechaCita4, DURACION);
			fail("Se esperaba una excepción FechaNoValidaException");
		} catch(FechaNoValidaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción FechaNoValidaException");
		}
		
		try {
			// Intentamos dar cita a una hora que no es múltiplo
			// de la duración de las citas
			servidor.pedirCita(sesionAdmin.getId(), beneficiario1, beneficiario1.getMedicoAsignado().getNif(), fechaCita3, DURACION);
			fail("Se esperaba una excepción FechaNoValidaException");
		} catch(FechaNoValidaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción FechaNoValidaException");
		}

		try {
			// Intentamos dar una cita en una fecha no válida para
			// el médico asignado al beneficiario
			medicoAsignado = beneficiario1.getMedicoAsignado();
			fechaCita = (medicoAsignado.equals(medico1)) ? fechaCita2 : fechaCita1;
			servidor.pedirCita(sesionCitador.getId(), beneficiario1, medicoAsignado.getNif(), fechaCita, DURACION);
			fail("Se esperaba una excepción FechaNoValidaException");
		} catch(FechaNoValidaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción FechaNoValidaException");
		}
		
		try {
			// Solicitamos una cita correcta para un beneficiario
			medicoAsignado = beneficiario1.getMedicoAsignado();
			fechaCita = (medicoAsignado.equals(medico1)) ? fechaCita1 : fechaCita2;
			servidor.pedirCita(sesionCitador.getId(), beneficiario1, medicoAsignado.getNif(), fechaCita, DURACION);
			// Comprobamos que la cita se ha añadido correctamente
			citas = servidor.getCitas(sesionCitador.getId(), beneficiario1.getNif());
			assertTrue(citas.size() == 1);
			assertEquals(new Cita(fechaCita, DURACION, beneficiario1, medicoAsignado), citas.get(0));
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Intentamos solicitar la misma cita de antes			
			medicoAsignado = beneficiario1.getMedicoAsignado();
			fechaCita = (medicoAsignado.equals(medico1)) ? fechaCita1 : fechaCita2;
			servidor.pedirCita(sesionCitador.getId(), beneficiario1, medicoAsignado.getNif(), fechaCita, DURACION);
			fail("Se esperaba una excepción CitaNoValidaException");
		} catch(CitaNoValidaException e){ 
		} catch(Exception e) {
			fail("Se esperaba una excepción CitaNoValidaException");
		}
	}

	/** Pruebas de la operación que solicita una cita a partir de un volante */
	public void testPedirCitaConVolante() {
		Vector<Cita> citas;
		Cita cita = null;
		
		try {
			// Intentamos pedir cita con un beneficiario nulo
			servidor.pedirCita(sesionAdmin.getId(), null, 0, fecha1, DURACION);
			fail("Se esperaba una excepción NullPointerException");
		} catch(NullPointerException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción NullPointerException");
		}
		
		try {
			// Intentamos pedir cita con una fecha nula
			servidor.pedirCita(sesionAdmin.getId(), beneficiario1, 0, null, DURACION);
			fail("Se esperaba una excepción NullPointerException");
		} catch(NullPointerException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción NullPointerException");
		}
		
		try {
			// Intentamos pedir cita con una sesión sin permisos
			servidor.pedirCita(sesionMedico.getId(), beneficiario1, 0, fecha1, DURACION);
			fail("Se esperaba una excepción OperacionIncorrectaException");
		} catch(OperacionIncorrectaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción OperacionIncorrectaException");
		}
		
		try {
			// Intentamos acceder al servidor con un id de sesión erróneo
			servidor.pedirCita(-12345, beneficiario1, 0, fecha1, DURACION);
			fail("Se esperaba una excepción SesionInvalidaException");
		} catch(SesionInvalidaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción SesionInvalidaException");
		}
		
		try {
			// Intentamos dar una cita a un beneficiario no registrado 
			servidor.pedirCita(sesionCitador.getId(), beneficiario2, 0, fecha1, DURACION);
			fail("Se esperaba una excepción BeneficiarioInexistenteException");
		} catch(BeneficiarioInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción BeneficiarioInexistenteException");
		}
		
		try {
			// Intentamos dar una cita a un beneficiario sin médico asignado 
			servidor.pedirCita(sesionCitador.getId(), beneficiario3, 0, fecha1, DURACION);
			fail("Se esperaba una excepción MedicoInexistenteException");
		} catch(MedicoInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción MedicoInexistenteException");
		}
		
		try {
			// Intentamos dar una cita con un volante que no existe	
			servidor.pedirCita(sesionCitador.getId(), beneficiario1, 13121312, fechaCita2, DURACION);
			fail("Se esperaba una excepción VolanteNoValidoException");
		} catch(VolanteNoValidoException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción VolanteNoValidoException");
		}
		
		try {
			// Intentamos dar una cita con un volante a un beneficiario
			// que no es el que tiene asociado al volante
			FPBeneficiario.insertar(beneficiario2);
			servidor.pedirCita(sesionCitador.getId(), beneficiario2, volante1.getId(), fechaCita1, DURACION);
			fail("Se esperaba una excepción VolanteNoValidoException");
		} catch(VolanteNoValidoException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción VolanteNoValidoException");
		}
		
		try {
			// Intentamos dar una cita para una fecha anterior a hoy
			servidor.pedirCita(sesionCitador.getId(), beneficiario1, volante1.getId(), fechaCita4, DURACION);
			fail("Se esperaba una excepción FechaNoValidaException");
		} catch(FechaNoValidaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción FechaNoValidaException");
		}
		
		try {
			// Intentamos dar cita a una hora que no es múltiplo
			// de la duración de las citas
			servidor.pedirCita(sesionCitador.getId(), beneficiario1, volante1.getId(), fechaCita3, DURACION);
			fail("Se esperaba una excepción FechaNoValidaException");
		} catch(FechaNoValidaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción FechaNoValidaException");
		}
	
		try {
			// Intentamos dar una cita a una fecha no válida para el médico receptor
			servidor.pedirCita(sesionCitador.getId(), beneficiario1, volante1.getId(), fechaCita1, DURACION);
			fail("Se esperaba una excepción FechaNoValidaException");
		} catch(FechaNoValidaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción FechaNoValidaException");
		}
		
		try {
			// Solicitamos una cita válida a partir de un volante
			cita = servidor.pedirCita(sesionCitador.getId(), beneficiario1, volante1.getId(), fechaCita2, DURACION);
			// Comprobamos que la cita se ha añadido correctamente
			citas = servidor.getCitas(sesionCitador.getId(), beneficiario1.getNif());
			assertEquals(new Cita(fechaCita2, DURACION, beneficiario1, volante1.getReceptor()), citas.get(0));
		} catch(Exception e) {			
			fail(e.toString());
		}
		
		try {
			// Intentamos solicitar una cita a partir de otro volante a la misma hora
			servidor.pedirCita(sesionCitador.getId(), beneficiario1, volante2.getId(), fechaCita2, DURACION);
			fail("Se esperaba una excepción CitaNoValidaException");
		} catch(CitaNoValidaException e){ 
		} catch(Exception e) {
			fail("Se esperaba una excepción CitaNoValidaException");
		}
		
		try {
			// Intentamos solicitar una cita con un volante que ya se ha utilizado		
			servidor.pedirCita(sesionCitador.getId(), beneficiario1, volante1.getId(), fechaCita2, DURACION);
			fail("Se esperaba una excepción VolanteNoValidoException");
		} catch(VolanteNoValidoException e){ 
		} catch(Exception e) {
			fail("Se esperaba una excepción VolanteNoValidoException");
		}
		
		try {
			// Anulamos la cita del volante y pedimos otra cita para el mismo volante
			servidor.anularCita(sesionCitador.getId(), cita);
			cita = servidor.pedirCita(sesionCitador.getId(), beneficiario1, volante1.getId(), fechaCita2, DURACION);
		} catch(Exception e) {
			fail(e.toString());
		}
	}

	/** Pruebas de la operación que anula citas existentes **/
	public void testAnularCita() {	
		Medico medicoAsignado;
		Date fechaCita;
		Cita cita = null;
		Vector<Cita> citas;
		
		try {
			// Intentamos anular una cita nula
			servidor.anularCita(sesionAdmin.getId(), null);
			fail("Se esperaba una excepción NullPointerException");
		} catch(NullPointerException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción NullPointerException");
		}
		
		try {
			// Creamos una cita correcta
			medicoAsignado = beneficiario1.getMedicoAsignado();
			if(medicoAsignado.getNif().equals(medico1.getNif())) {
				fechaCita = fechaCita1;
			} else {
				fechaCita = fechaCita2;
			}
			cita = servidor.pedirCita(sesionCitador.getId(), beneficiario1, medicoAsignado.getNif(), fechaCita, DURACION);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Intentamos anular una cita con una sesión sin permisos
			servidor.anularCita(sesionMedico.getId(), cita);
			fail("Se esperaba una excepción OperacionIncorrectaException");
		} catch(OperacionIncorrectaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción OperacionIncorrectaException");
		}
		
		try {
			// Intentamos acceder al servidor con un id de sesión erróneo
			servidor.anularCita(-12345, cita);
			fail("Se esperaba una excepción SesionInvalidaException");
		} catch(SesionInvalidaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción SesionInvalidaException");
		}
		
		try {
			// Anulamos la cita recién creada
			servidor.anularCita(sesionCitador.getId(), cita);
			// Comprobamos que la cita ya no existe
			citas = servidor.getCitas(sesionCitador.getId(), beneficiario1.getNif());
			assertTrue(citas.size() == 0);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Intentamos anular la misma cita otra vez 
			servidor.anularCita(sesionCitador.getId(), cita);
			fail("Se esperaba una excepción CitaNoValidaException");
		} catch(CitaNoValidaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción CitaNoValidaException");
		}

	}

	/** Pruebas de la operación que obtiene las horas que un médico pasa cita */
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void testConsultarHorasCitasMedico() {
		Hashtable<Date, Vector<String>> citasMedico;
		Medico medicoAsignado;
		Date fecha, fechaCita;
		Cita citaPendiente, citaPasada;
		Calendar cal;
		
		try {
			// Intentamos consultar las horas de las citas de un médico nulo
			servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CONSULTAR_HORAS_CITAS_MEDICO, null);
			fail("Se esperaba una excepción NullPointerException");
		} catch(NullPointerException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción NullPointerException");
		}
		
		try {
			// Intentamos consultar las horas de las citas con una sesión sin permisos
			servidor.mensajeAuxiliar(sesionMedico.getId(), ICodigosMensajeAuxiliar.CONSULTAR_HORAS_CITAS_MEDICO, medico1.getNif());
			fail("Se esperaba una excepción OperacionIncorrectaException");
		} catch(OperacionIncorrectaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción OperacionIncorrectaException");
		}
		
		try {
			// Intentamos acceder al servidor con un id de sesión erróneo
			servidor.mensajeAuxiliar(-12345, ICodigosMensajeAuxiliar.CONSULTAR_HORAS_CITAS_MEDICO, medico1.getNif());
			fail("Se esperaba una excepción SesionInvalidaException");
		} catch(SesionInvalidaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción SesionInvalidaException");
		}

		try {
			// Intentamos consultar las horas de las citas de un médico que no existe
			servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CONSULTAR_HORAS_CITAS_MEDICO, "00110011N");
			fail("Se esperaba una excepción MedicoInexistenteException");
		} catch(MedicoInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción MedicoInexistenteException");
		}
		
		try {
			// Intentamos consultar las horas de las citas de un usuario que no es médico
			servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CONSULTAR_HORAS_CITAS_MEDICO, administrador1.getNif());
			fail("Se esperaba una excepción MedicoInexistenteException");
		} catch(MedicoInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción MedicoInexistenteException");
		}
		
		try {
			// Insertamos varias citas
			FPBeneficiario.insertar(beneficiario2);
			medicoAsignado = beneficiario2.getMedicoAsignado();
			fechaCita = (medicoAsignado.equals(medico1)) ? fechaCita1 : fechaCita2;
			citaPendiente = servidor.pedirCita(sesionCitador.getId(), beneficiario2, medicoAsignado.getNif(), fechaCita, DURACION);
			citaPasada = new Cita(fechaCita4, DURACION, beneficiario2, medicoAsignado);
			FPCita.insertar(citaPasada);
			// Consultamos las horas de las citas del médico de las citas creadas
			citasMedico = (Hashtable<Date, Vector<String>>)servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CONSULTAR_HORAS_CITAS_MEDICO, medicoAsignado.getNif());
			// Comprobamos que la cita devuelta es la esperada
			cal = Calendar.getInstance();
			cal.setTime(citaPendiente.getFechaYHora());
			fecha = new Date(cal.get(Calendar.YEAR) - 1900, cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
			assertEquals(citasMedico.get(fecha).get(0), Cita.cadenaHoraCita(citaPendiente.getFechaYHora()));
			cal.setTime(citaPasada.getFechaYHora());
			fecha = new Date(cal.get(Calendar.YEAR) - 1900, cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
			assertNull(citasMedico.get(fecha));
		} catch(Exception e) {
			e.printStackTrace();
			fail(e.toString());
		}
	}
	
	/** Pruebas de la operación que devuelve los días completos de un médico */
	@SuppressWarnings("unchecked")
	public void testConsultarDiasCompletos() {
		Vector<Date> dias;
		Medico medicoAsignado; 
		Date fecha1,fecha2, fecha3, fecha4;
		
		try {
			// Intentamos consultar los días completos de un médico nulo
			servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CONSULTAR_DIAS_COMPLETOS, null);
			fail("Se esperaba una excepción NullPointerException");
		} catch(NullPointerException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción NullPointerException");
		}
		
		try {
			// Intentamos consultar los días completos con una sesión sin permisos
			servidor.mensajeAuxiliar(sesionMedico.getId(), ICodigosMensajeAuxiliar.CONSULTAR_DIAS_COMPLETOS, medico1.getNif());
			fail("Se esperaba una excepción OperacionIncorrectaException");
		} catch(OperacionIncorrectaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción OperacionIncorrectaException");
		}
		
		try {
			// Intentamos acceder al servidor con un id de sesión erróneo
			servidor.mensajeAuxiliar(-12345, ICodigosMensajeAuxiliar.CONSULTAR_DIAS_COMPLETOS, medico1.getNif());
			fail("Se esperaba una excepción SesionInvalidaException");
		} catch(SesionInvalidaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción SesionInvalidaException");
		}

		try {
			// Intentamos consultar los días completos de un médico que no existe
			servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CONSULTAR_DIAS_COMPLETOS, "00110011N");
			fail("Se esperaba una excepción MedicoInexistenteException");
		} catch(MedicoInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción MedicoInexistenteException");
		}
		

		try {
			// Intentamos consultar las horas de las citas de un usuario que no es médico
			servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CONSULTAR_DIAS_COMPLETOS, administrador1.getNif());
			fail("Se esperaba una excepción MedicoInexistenteException");
		} catch(MedicoInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción MedicoInexistenteException");
		}
		
		try {
			// Consultamos los días completos de un médico que no tiene citas
			dias = (Vector<Date>)servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CONSULTAR_DIAS_COMPLETOS, medico2.getNif());
			assertTrue(dias.size() == 0);
			// Ocupamos un día entero con citas
			medicoAsignado = beneficiario1.getMedicoAsignado();
			calendar = new GregorianCalendar(2011, 1 - 1 ,10, 16, 0);
			fecha1 = calendar.getTime();
			calendar = new GregorianCalendar(2011, 1 - 1, 10, 16, 30);
			fecha2 = calendar.getTime();
			calendar = new GregorianCalendar(2011, 1 - 1, 10, 16, 15);
			fecha3 = calendar.getTime();
			calendar = new GregorianCalendar(2011, 1 - 1, 10, 16, 45);
			fecha4 = calendar.getTime();
			servidor.pedirCita(sesionCitador.getId(), beneficiario1, medicoAsignado.getNif(), fecha1, DURACION);
			servidor.pedirCita(sesionCitador.getId(), beneficiario1, medicoAsignado.getNif(), fecha2, DURACION);
			servidor.pedirCita(sesionCitador.getId(), beneficiario1, medicoAsignado.getNif(), fecha3, DURACION);
			servidor.pedirCita(sesionCitador.getId(), beneficiario1, medicoAsignado.getNif(), fecha4, DURACION);
			servidor.pedirCita(sesionCitador.getId(), beneficiario1, medicoAsignado.getNif(), fechaCita2, DURACION);
			// Consultamos ahora los días completos del médico
			dias = (Vector<Date>)servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CONSULTAR_DIAS_COMPLETOS, medico2.getNif());
			// Comprobamos que se ha devuelto el día completo
			assertTrue(dias.size() == 1);
			assertTrue(UtilidadesDominio.fechaIgual(dias.get(0), new GregorianCalendar(2011, 1 - 1, 10).getTime(), false));
		} catch(Exception e) {
			fail(e.toString());
		}
	}

	/** Pruebas de las operaciones de la clase Cita */
	@SuppressWarnings("deprecation")
	public void testClaseCita() {
		Calendar calend;
		Date hora;
		String cadena;
		Cita cita;
		
		try {
			// Comprobamos que las horas de las citas tienen el formato esperado
			calend = Calendar.getInstance();
			hora = Cita.horaCadenaCita("22:45");
			calend.setTime(hora);
			assertTrue(calend.get(Calendar.HOUR_OF_DAY) == 22);
			assertTrue(calend.get(Calendar.MINUTE) == 45);
			assertTrue(calend.get(Calendar.SECOND) == 0);
			calend = Calendar.getInstance();
			hora = Cita.horaCadenaCita("09:00");
			calend.setTime(hora);
			assertTrue(calend.get(Calendar.HOUR_OF_DAY) == 9);
			assertTrue(calend.get(Calendar.MINUTE) == 0);
			assertTrue(calend.get(Calendar.SECOND) == 0);
			cadena = Cita.cadenaHoraCita(new Date(2010 - 1900, 0, 1, 16, 30, 0));
			assertEquals("16:30", cadena);
			cadena = Cita.cadenaHoraCita(new Date(2010 - 1900, 0, 1, 1, 50, 0));
			assertEquals("01:50", cadena);
			// Comprobamos que se detecta bien si la hora de la cita está
			// dentro de un determinado rango de horas
			cita = new Cita(new Date(2010 - 1900, 4, 5, 18, 30, 0), 15, beneficiario1, medico1);
			assertTrue(cita.citaEnHoras(16, 21));
			assertFalse(cita.citaEnHoras(12, 14));
			assertFalse(cita.citaEnHoras(19, 23));
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
}
