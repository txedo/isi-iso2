package pruebas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Vector;

import junit.framework.TestCase;

import persistencia.AgenteFrontend;
import persistencia.FPBeneficiario;
import persistencia.FPCentroSalud;
import persistencia.FPCita;
import persistencia.FPUsuario;
import persistencia.FPVolante;

import comunicaciones.ConexionBDFrontend;
import comunicaciones.GestorConexionesBD;

import dominio.conocimiento.Administrador;
import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.Cabecera;
import dominio.conocimiento.CentroSalud;
import dominio.conocimiento.Cita;
import dominio.conocimiento.Citador;
import dominio.conocimiento.DiaSemana;
import dominio.conocimiento.Direccion;
import dominio.conocimiento.Especialista;
import dominio.conocimiento.ISesion;
import dominio.conocimiento.Medico;
import dominio.conocimiento.Pediatra;
import dominio.conocimiento.PeriodoTrabajo;
import dominio.conocimiento.Sesion;
import dominio.conocimiento.Usuario;
import dominio.conocimiento.Volante;
import dominio.control.GestorCitas;
import dominio.control.GestorSesiones;
import excepciones.BeneficiarioInexistenteException;
import excepciones.FechaNoValidaException;
import excepciones.MedicoInexistenteException;
import excepciones.OperacionIncorrectaException;
import excepciones.SesionInvalidaException;
import excepciones.VolanteNoValidoException;

public class PruebasCitas extends TestCase{

	private CentroSalud centro1;
	private Medico medico1, medico2, medico3, medico4;
	private Citador citador1;
	private Administrador admin1;
	private Beneficiario bene1, bene2;
	private Usuario usu1;
	private Direccion dir1;
	private PeriodoTrabajo periodo1, periodo2, periodo3;
	private ConexionBDFrontend conexionF;
	private ISesion sesionCitador;
	private ISesion sesionAdmin;
	private ISesion sesionMedico;
	private Pediatra pediatra;
	private Especialista especialista;
	private Cabecera cabecera;
	private Date fecha1;
	private Date fecha2;
	private Date fechaCita1;
	private Date fechaCita2;
	private GregorianCalendar calendar;
	private final long DURACION = 15;
	
	protected void setUp() {
		Connection bd;
		PreparedStatement sentencia;
		AgenteFrontend agente;
		
		try {
			// Borramos la base de datos
			agente = AgenteFrontend.getAgente();
			agente.setIP("127.0.0.1");
			agente.setPuerto(3306);
			agente.abrir();
			bd = agente.getConexion();
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
			sentencia = bd.prepareStatement("DELETE FROM direcciones");
			sentencia.executeUpdate();
			// Ponemos la conexión local con la base de datos
			conexionF = new ConexionBDFrontend();
			GestorConexionesBD.ponerConexion(conexionF);
			// Inicializamos fechas de nacimiento para los beneficiarios y las fechas de las citas
			SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd/MM/yyyy");
			fecha1 = formatoDelTexto.parse("1/8/1951");
			fecha2 = formatoDelTexto.parse("23/2/2002");
			
			// Se usa el gregorianCalendar para no restar 1900 al año de la fecha de tipo Date
			// Al mes hay que restar 1, es decir, Enero seria el mes 0
			// El formato del gregorianCalendar es: año,mes,dia,hora,minutos
			
			// Esta fecha de cita es valida para el medico1 e invalida para el medico2
			// La fecha seria el miercoles 13 de Enero de 2010, a las 10:30
			calendar = new GregorianCalendar(2010,1-1,13,10,30);
			fechaCita1 = calendar.getTime();
			
			// Fecha de cita valida para el medico2 e invalida para el medico1
			// La cita seria el lunes 11 de Enero de 2010, a las 16:15
			calendar = new GregorianCalendar(2010,1-1,11,16,15);
			fechaCita2 = calendar.getTime();
			
			//Inicializamos los tipos de medicos
			pediatra = new Pediatra();
			especialista = new Especialista("Traumatologia");
			cabecera = new Cabecera();
			// Creamos objetos de prueba
			dir1 = new Direccion("calle 1", "1", "", "", "aadsf", "afafssaf", 12500);
			centro1 = new CentroSalud("Centro A", dir1);
			medico1 = new Medico("12345678", "medico1", "abcdef", "Eduardo", "P. C.", pediatra);
			medico2 = new Medico("87654321", "medico2", "xxx", "Carmen", "G. G.", cabecera);
			medico3 = new Medico("34581732", "medico3", "pass", "nombre", "apellido", especialista);
			medico4 = new Medico("09761231", "medNoRegistrado", "asas", "E", "P", cabecera);
			citador1 = new Citador("11223344", "citador", "cit123", "Fernando", "G. P.");
			admin1 = new Administrador("55667788", "admin", "nimda", "María", "L. F.");
			medico1.setCentroSalud(centro1);
			medico2.setCentroSalud(centro1);
			medico3.setCentroSalud(centro1);
			medico4.setCentroSalud(centro1);
			citador1.setCentroSalud(centro1);
			admin1.setCentroSalud(centro1);
			periodo1 = new PeriodoTrabajo(10, 14, DiaSemana.Miercoles);
			periodo2 = new PeriodoTrabajo(17, 19, DiaSemana.Viernes);
			periodo3 = new PeriodoTrabajo(16, 17, DiaSemana.Lunes);
			medico1.getCalendario().add(periodo1);
			medico1.getCalendario().add(periodo2);
			medico2.getCalendario().add(periodo3);
			medico3.getCalendario().add(periodo3);
			usu1 = new Administrador("04328172", "usuario", "f", "O", "C");
			usu1.setCentroSalud(centro1);
			bene1 = new Beneficiario("12345679", "123456-ab", "bene1", "asdfg", fecha1, dir1, "uno@gmail.com", "123456789", "987654321");
			bene1.setMedicoAsignado(medico2);
			bene2 = new Beneficiario("46208746", "164028-de", "bene2", "asadasdfg", fecha2, dir1, "dos@gmail.com", "923456789", "687654322");
			bene2.setMedicoAsignado(medico1);
			FPCentroSalud.insertar(centro1);
			FPUsuario.insertar(medico1);
			FPUsuario.insertar(medico2);
			FPUsuario.insertar(medico3);
			FPUsuario.insertar(citador1);
			FPUsuario.insertar(admin1);
			FPUsuario.insertar(usu1);
			FPBeneficiario.insertar(bene1);
			// Iniciamos tres sesiones con roles de citador, administrador y medico
			sesionCitador = GestorSesiones.identificar(citador1.getLogin(), citador1.getPassword());
			sesionAdmin = GestorSesiones.identificar(admin1.getLogin(), admin1.getPassword());
			sesionMedico = GestorSesiones.identificar(medico1.getLogin(), medico1.getPassword());
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	protected void tearDown() {
		// Cerramos las sesiones y quitamos la conexión local con la base de datos
		try {
			GestorSesiones.liberar(((Sesion)sesionCitador).getId());
			GestorSesiones.liberar(((Sesion)sesionAdmin).getId());
			GestorSesiones.liberar(((Sesion)sesionMedico).getId());
			GestorConexionesBD.quitarConexiones();
		} catch(SQLException e) {
			fail(e.toString());
		}
	}
	
	/** Pruebas para pedir una cita sin volante **/
	public void testPedirCitaSinVolante() {	
		Cita cita;
		Vector<Cita> citas;
		Medico medicoAsignado;
		Date fechaCita;
		
		try {
			// Intentamos acceder al servidor con un id de sesión erróneo
			cita = GestorCitas.pedirCita(sesionCitador.getId() + 1, bene1, bene1.getMedicoAsignado().getDni(), fecha1, DURACION);
			fail("Se esperaba una excepcion SesionInvalidaException");
		} catch(SesionInvalidaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepcion SesionInvalidaException");
		}
		
		try {
			// Intentamos dar cita con un rol que no sea citador ni administrador
			cita = GestorCitas.pedirCita(sesionMedico.getId(), bene1, bene1.getMedicoAsignado().getDni(), fecha1, DURACION);
			fail("Se esperaba una excepcion OperacionIncorrectaException");
		} catch(OperacionIncorrectaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepcion OperacionIncorrectaException");
		}
		
		try {
			// Intentamos dar una cita a un beneficiario no registrado 
			cita = GestorCitas.pedirCita(sesionCitador.getId(), bene2, medico1.getDni(), fecha1, DURACION);
			fail("Se esperaba una excepcion BeneficiarioInexistenteException");
		} catch(BeneficiarioInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepcion BeneficiarioInexistenteException");
		}
		
		try {
			// Intentamos dar una cita con un medico no registrado
			cita = GestorCitas.pedirCita(sesionCitador.getId(), bene1, medico4.getDni(), fecha1, DURACION);
			fail("Se esperaba una excepcion MedicoInexistenteException");
		} catch(MedicoInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepcion MedicoInexistenteException");
		}
		
		try {
			// Intentamos dar una cita con un usuario que no es medico
			cita = GestorCitas.pedirCita(sesionCitador.getId(), bene1, admin1.getDni(), fecha1, DURACION);
			fail("Se esperaba una excepcion MedicoInexistenteException");
		} catch(MedicoInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepcion MedicoInexistenteException");
		}
		
		try {
			// Intentamos dar una cita en una fecha no valida para el medico asignado al beneficiario1
			medicoAsignado = bene1.getMedicoAsignado();
			if (medicoAsignado.getDni().equals(medico1.getDni()))
				fechaCita = fechaCita2;
			else
				fechaCita = fechaCita1;
			
			cita = GestorCitas.pedirCita(sesionCitador.getId(), bene1, medicoAsignado.getDni(), fechaCita, DURACION);
			fail("Se esperaba una excepcion FechaNoValidaException");
		} catch(FechaNoValidaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepcion FechaNoValidaException");
		}
		
		try {
			// Intentamos dar una cita al beneficiario1 con un medico que no es su medico asignado
			medicoAsignado = bene1.getMedicoAsignado();
			if (medicoAsignado.getDni().equals(medico1.getDni()))
				medicoAsignado = medico2;
			else
				medicoAsignado = medico1;
			
			cita = GestorCitas.pedirCita(sesionCitador.getId(), bene1, medicoAsignado.getDni(), fecha1, DURACION);
			fail("Se esperaba una excepcion al no coincidir el medico asignado ");
		} catch(Exception e) { }
		
		try {
			// Se da la cita del beneficiario1 con su medico asignado
			
			// Se comprueba el medico asignado al beneficiario1 y se establece la fecha correcta para cada medico
			medicoAsignado = bene1.getMedicoAsignado();
			if (medicoAsignado.getDni().equals(medico1.getDni()))
				fechaCita = fechaCita1;
			else
				fechaCita = fechaCita2;
			
			// Se pide la cita		
			cita = GestorCitas.pedirCita(sesionCitador.getId(), bene1, medicoAsignado.getDni(), fechaCita, DURACION);
			// Se recuperan las citas del beneficiario1 para ver si realmente existe
			citas = GestorCitas.consultarCitas(sesionCitador.getId(), bene1.getNif());
			// Como se ha limpiado la base de datos antes de ejecutar este caso de prueba, solo se obtendrá una cita
			assertEquals(cita, citas.get(0));
		} catch(Exception e) {
			e.printStackTrace();
			fail("No se esperaba ninguna excepcion para registrar la cita");
		}
		
		try {
			// Intentamos dar una cita con el medico asignado al beneficiario1 en la misma fecha y hora			
			medicoAsignado = bene1.getMedicoAsignado();
			if (medicoAsignado.getDni().equals(medico1.getDni()))
				fechaCita = fechaCita1;
			else
				fechaCita = fechaCita2;
			
			// Se pide la cita		
			cita = GestorCitas.pedirCita(sesionCitador.getId(), bene1, medicoAsignado.getDni(), fechaCita, DURACION);
			fail("Se esperaba FechaNoValidaException");
		} catch(FechaNoValidaException e){ 
		} catch(Exception e) {
			fail("Se esperaba FechaNoValidaException");
		}
		
		/** try {
			// Intentamos dar una cita con el medico asignado al beneficiario1 sin que haya acabado la cita anterior		
			medicoAsignado = bene1.getMedicoAsignado();
			if (medicoAsignado.getDni().equals(medico1.getDni())) {
				calendar = new GregorianCalendar(2010,1-1,13,10,36);
				fechaCita = calendar.getTime();
			}else {
				calendar = new GregorianCalendar(2010,1-1,11,16,23);
				fechaCita = calendar.getTime();
			}
			
			// Se pide la cita		
			cita = GestorCitas.pedirCita(sesionCitador.getId(), bene1, medicoAsignado.getDni(), fechaCita, DURACION);
			fail("Se esperaba FechaNoValidaException");
		} catch(FechaNoValidaException e){ 
		} catch(Exception e) {
			fail("Se esperaba FechaNoValidaException");
		} **/
		
	}

	/** Pruebas para pedir una cita con volante **/
	public void testPedirCitaConVolante() {	
		Cita cita;
		Vector<Cita> citas;
		Volante volante = null;
		
		try {
			// 	Creamos un volante valido
			volante = new Volante(medico1,medico3,bene1,null);
			FPVolante.insertar(volante);
		} catch(Exception e){
			fail("No se esperaba ninguna excepcion al crear el volante");
		}
		
		try {
			// Intentamos acceder al servidor con un id de sesión erróneo
			cita = GestorCitas.pedirCita(sesionCitador.getId() + 1, bene1, medico1.getDni(), fecha1, DURACION);
			fail("Se esperaba una excepcion SesionInvalidaException");
		} catch(SesionInvalidaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepcion SesionInvalidaException");
		}
		
		try {
			// Intentamos dar cita con un rol que no sea citador ni administrador
			cita = GestorCitas.pedirCita(sesionMedico.getId(), bene1, medico1.getDni(), fecha1, DURACION);
			fail("Se esperaba una excepcion OperacionIncorrectaException");
		} catch(OperacionIncorrectaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepcion OperacionIncorrectaException");
		}
		
		try {
			// Intentamos dar una cita a un beneficiario no registrado 
			cita = GestorCitas.pedirCita(sesionCitador.getId(), bene2, medico1.getDni(), fecha1, DURACION);
			fail("Se esperaba una excepcion BeneficiarioInexistenteException");
		} catch(BeneficiarioInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepcion BeneficiarioInexistenteException");
		}
		
		try {
			// Intentamos dar una cita con un idVolante no valido	
			FPBeneficiario.insertar(bene2);
			cita = GestorCitas.pedirCita(sesionCitador.getId(), bene1, volante.getId() + 1, fechaCita1, DURACION);
			fail("Se esperaba una excepcion VolanteNoValidoException");
		} catch(VolanteNoValidoException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepcion VolanteNoValidoException");
		}
		
		try {
			// Intentamos dar una cita a un beneficiario que no es el asociado al volante
			cita = GestorCitas.pedirCita(sesionCitador.getId(), bene2, volante.getId() + 1, fechaCita1, DURACION);
			fail("Se esperaba una excepcion VolanteNoValidoException");
		} catch(VolanteNoValidoException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepcion VolanteNoValidoException");
		}
		
		try {
			// Intentamos dar una cita con una fecha no valida para el medico receptor del volante
			cita = GestorCitas.pedirCita(sesionCitador.getId(), bene1, volante.getId(), fechaCita1, DURACION);
			fail("Se esperaba una excepcion FechaNoValidaException");
		} catch(FechaNoValidaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepcion FechaNoValidaException");
		}
		
		try {
			// Intentamos dar una cita valida para el medico receptor del volante
			cita = GestorCitas.pedirCita(sesionCitador.getId(), bene1, volante.getId(), fechaCita2, DURACION);
			citas = GestorCitas.consultarCitas(sesionCitador.getId(), bene1.getNif());
			// Como se ha limpiado la base de datos antes de ejecutar este caso de prueba, solo se obtendrá una cita
			assertEquals(cita, citas.get(0));
		} catch(Exception e) {			
			fail("No se esperaba excepcion al registrar la cita con el volante");
		}
		
		try {
			// Intentamos dar una cita en la misma fecha y hora para el medico receptor del volante		
			cita = GestorCitas.pedirCita(sesionCitador.getId(), bene1, volante.getId(), fechaCita2, DURACION);
			fail("Se esperaba FechaNoValidaException");
		} catch(FechaNoValidaException e){ 
		} catch(Exception e) {
			fail("Se esperaba FechaNoValidaException");
		}
	}
	
	/** Pruebas para emitir un volante **/
	public void testEmitirVolante() {	
		Volante volanteRecuperado = null;
		long idVolante = -1;
		
		try {
			// Intentamos acceder al servidor con un id de sesión erróneo
			idVolante = GestorCitas.emitirVolante(sesionCitador.getId() + 1, bene1, medico1, medico3);
			fail("Se esperaba una excepcion SesionInvalidaException");
		} catch(SesionInvalidaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepcion SesionInvalidaException");
		}
		
		try {
			// Intentamos emitir un volante como un administrador
			idVolante = GestorCitas.emitirVolante(sesionAdmin.getId(), bene1, medico1, medico3);
			fail("Se esperaba una excepcion OperacionIncorrectaException");
		} catch(OperacionIncorrectaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepcion OperacionIncorrectaException");
		}
		
		try {
			// Intentamos emitir un volante para un beneficiario no registrado 
			idVolante = GestorCitas.emitirVolante(sesionMedico.getId(), bene2, medico1, medico3);
			fail("Se esperaba una excepcion BeneficiarioInexistenteException");
		} catch(BeneficiarioInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepcion BeneficiarioInexistenteException");
		}
		
		try {
			// Intentamos emitir un volante para un medico no registrado
			idVolante = GestorCitas.emitirVolante(sesionMedico.getId(), bene1, medico4, medico4);
			fail("Se esperaba una excepcion MedicoInexistenteException");
		} catch(MedicoInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepcion MedicoInexistenteException");
		}
			
		try {
			// Intentamos emitir un volante para un medico receptor que no es especialista
			idVolante = GestorCitas.emitirVolante(sesionMedico.getId(), bene1, medico1, medico2);
			fail("Se esperaba una excepcion VolanteNoValidoException");
		} catch(VolanteNoValidoException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepcion VolanteNoValidoException");
		}
		
		try {
			// Intentamos emitir un volante correcto
			idVolante = GestorCitas.emitirVolante(sesionMedico.getId(), bene1, medico1, medico3);
			assertTrue(idVolante != -1);
			volanteRecuperado = FPVolante.consultar(idVolante);
			assertEquals(volanteRecuperado.getId(), idVolante);
		} catch(Exception e) {
			fail("No se esperaba ninguna excepción al crear el volante");
		}
		
		try {
			// Intentamos emitir un volante correcto, en este caso, del especialista para si mismo
			idVolante = GestorCitas.emitirVolante(sesionMedico.getId(), bene1, medico3, medico3);
			assertTrue(idVolante != -1);
			volanteRecuperado = FPVolante.consultar(idVolante);
			assertEquals(volanteRecuperado.getId(), idVolante);
		} catch(Exception e) { 
			fail("No se esperaba ninguna excepción al crear el volante");
		}
	}
	
	/** Pruebas para anular una cita **/
	public void testAnularCita() {	
		Medico medicoAsignado;
		Date fechaCita;
		Cita cita;
		Vector<Cita> citas;
		
		try {
			medicoAsignado = bene1.getMedicoAsignado();
			if (medicoAsignado.getDni().equals(medico1.getDni()))
				fechaCita = fechaCita1;
			else
				fechaCita = fechaCita2;
			
			// Creamos una cita correcta
			cita = GestorCitas.pedirCita(sesionCitador.getId(), bene1, medicoAsignado.getDni(), fechaCita, DURACION);
			// Se recuperan las citas del beneficiario1 para ver si realmente existe
			citas = GestorCitas.consultarCitas(sesionCitador.getId(), bene1.getNif());
			// Como se ha limpiado la base de datos antes de ejecutar este caso de prueba, solo se obtendrá una cita
			assertEquals(cita, citas.get(0));
			// Anulamos esa cita 
			GestorCitas.anularCita(sesionCitador.getId(), cita);
			// Si se ha anulado correctamente, se puede volver a concertar la scita con los mismos datos
			cita = GestorCitas.pedirCita(sesionCitador.getId(), bene1, medicoAsignado.getDni(), fechaCita, DURACION);
			citas = GestorCitas.consultarCitas(sesionCitador.getId(), bene1.getNif());
			assertEquals(cita, citas.get(0));
		} catch(Exception e) {
			fail("No se esperaba ninguna excepcion al anular la cita");
		}

	}
	
}
