package pruebas;

import java.text.SimpleDateFormat;
import java.util.Date;
import persistencia.FPBeneficiario;
import persistencia.FPCentroSalud;
import persistencia.FPUsuario;
import persistencia.FPVolante;
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
import dominio.conocimiento.ISesion;
import dominio.conocimiento.Medico;
import dominio.conocimiento.Pediatra;
import dominio.conocimiento.PeriodoTrabajo;
import dominio.conocimiento.Sesion;
import dominio.conocimiento.Usuario;
import dominio.conocimiento.Volante;
import dominio.control.GestorSesiones;
import dominio.control.GestorVolantes;
import dominio.control.ServidorFrontend;
import excepciones.BeneficiarioInexistenteException;
import excepciones.MedicoInexistenteException;
import excepciones.OperacionIncorrectaException;
import excepciones.SesionInvalidaException;
import excepciones.VolanteNoValidoException;

/**
 * Pruebas del Gestor de Volantes.
 */
public class PruebasVolantes extends PruebasBase {

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
	private Volante volante1, volante2;
	
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
			// Inicializamos los tipos de medicos
			pediatra = new Pediatra();
			especialista = new Especialista("Traumatologia");
			cabecera = new Cabecera();
			// Creamos objetos de prueba
			centro1 = new CentroSalud("Centro A", "Calle 2, nº2");
			centro2 = new CentroSalud("Centro B", "Calle 4, nº4");
			medico1 = new Medico("12345678", "medico1", Encriptacion.encriptarPasswordSHA1("abcdef"), "Eduardo", "P. C.", "", "", "", pediatra);
			medico2 = new Medico("87654321", "medico2", Encriptacion.encriptarPasswordSHA1("xxx"), "Carmen", "G. G.", "carmen@gmail.com", "", "666123123", cabecera);
			medico3 = new Medico("34581732", "medico3", Encriptacion.encriptarPasswordSHA1("pass"), "nombre", "apellido", "nombre@correos.es", "", "", especialista);
			medico4 = new Medico("09761231", "medNoRegistrado", Encriptacion.encriptarPasswordSHA1("asas"), "E", "P", "", "999888777", "666555444", cabecera);
			citador1 = new Citador("11223344", "citador", Encriptacion.encriptarPasswordSHA1("cit123"), "Fernando", "G. P.", "", "" ,"");
			administrador1 = new Administrador("55667788", "admin", Encriptacion.encriptarPasswordSHA1("admin"), "María", "L. F.", "admin@yahoo.com", "999999999", "666666666");
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
			volante1 = new Volante(medico1,medico3,beneficiario1,null, new Date());
			volante2 = new Volante(medico1,medico3,beneficiario1,null, new Date());
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
	
	/** Pruebas de la operación que consulta volantes existentes */
	public void testConsultarVolante() {
		Volante volanteGet;
		
		try {
			// Intentamos acceder al servidor con un id de sesión erróneo
			servidor.mensajeAuxiliar(-12345, ICodigosMensajeAuxiliar.CONSULTAR_VOLANTE, volante1.getId());
			fail("Se esperaba una excepción SesionInvalidaException");
		} catch(SesionInvalidaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción SesionInvalidaException");
		}
		
		try {
			// Intentamos consultar un volante inexistente
			servidor.mensajeAuxiliar(sesionCitador.getId(), ICodigosMensajeAuxiliar.CONSULTAR_VOLANTE, -12345L);
			fail("Se esperaba una excepción VolanteNoValidoException");
		} catch(VolanteNoValidoException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción VolanteNoValidoException");
		}
		
		try {
			// Consultamos un volante existente
			volanteGet = (Volante)servidor.mensajeAuxiliar(sesionCitador.getId(), ICodigosMensajeAuxiliar.CONSULTAR_VOLANTE, volante1.getId());
			assertEquals(volante1, volanteGet);
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	/** Pruebas de la operación que emite volantes **/
	public void testEmitirVolante() {	
		Volante volanteGet = null;
		long idVolante = -1;
		
		try {
			// Intentamos emitir un volante para un beneficiario nulo
			servidor.emitirVolante(sesionMedico.getId(), null, medico1, medico3);
			fail("Se esperaba una excepción NullPointerException");
		} catch(NullPointerException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción NullPointerException");
		}
		
		try {
			// Intentamos emitir un volante para un médico emisor nulo
			servidor.emitirVolante(sesionMedico.getId(), beneficiario1, null, medico3);			
			fail("Se esperaba una excepción NullPointerException");
		} catch(NullPointerException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción NullPointerException");
		}
		
		try {
			// Intentamos emitir un volante para un médico receptor nulo
			servidor.emitirVolante(sesionMedico.getId(), beneficiario1, medico1, null);
			fail("Se esperaba una excepción NullPointerException");
		} catch(NullPointerException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción NullPointerException");
		}
		
		try {
			// Intentamos emitir un volante como administrador
			servidor.emitirVolante(sesionAdmin.getId(), beneficiario1, medico1, medico3);
			fail("Se esperaba una excepción OperacionIncorrectaException");
		} catch(OperacionIncorrectaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción OperacionIncorrectaException");
		}
		
		try {
			// Intentamos acceder al servidor con un id de sesión erróneo
			servidor.emitirVolante(-12345, beneficiario1, medico1, medico3);
			fail("Se esperaba una excepción SesionInvalidaException");
		} catch(SesionInvalidaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción SesionInvalidaException");
		}
		
		try {
			// Intentamos emitir un volante para un beneficiario no registrado 
			servidor.emitirVolante(sesionMedico.getId(), beneficiario2, medico1, medico3);
			fail("Se esperaba una excepción BeneficiarioInexistenteException");
		} catch(BeneficiarioInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción BeneficiarioInexistenteException");
		}
		
		try {
			// Intentamos emitir un volante para un beneficiario que no tiene médico 
			servidor.emitirVolante(sesionMedico.getId(), beneficiario3, medico1, medico3);
			fail("Se esperaba una excepción MedicoInexistenteException");
		} catch(MedicoInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción MedicoInexistenteException");
		}
		
		try {
			// Intentamos emitir un volante de un médico no registrado
			servidor.emitirVolante(sesionMedico.getId(), beneficiario1, medico4, medico1);
			fail("Se esperaba una excepción MedicoInexistenteException");
		} catch(MedicoInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción MedicoInexistenteException");
		}
		
		try {
			// Intentamos emitir un volante para un médico no registrado
			servidor.emitirVolante(sesionMedico.getId(), beneficiario1, medico1, medico4);
			fail("Se esperaba una excepción MedicoInexistenteException");
		} catch(MedicoInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción MedicoInexistenteException");
		}
		
		try {
			// Intentamos emitir un volante para un médico receptor que no es especialista
			servidor.emitirVolante(sesionMedico.getId(), beneficiario1, medico1, medico2);
			fail("Se esperaba una excepción VolanteNoValidoException");
		} catch(VolanteNoValidoException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción VolanteNoValidoException");
		}
		
		try {
			// Emitimos un volante correcto
			idVolante = servidor.emitirVolante(sesionMedico.getId(), beneficiario1, medico1, medico3);
			assertTrue(idVolante != -1);
			volanteGet = GestorVolantes.consultarVolante(sesionAdmin.getId(), idVolante);
			assertEquals(idVolante, volanteGet.getId());
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Emitimos un volante de un especialista para sí mismo
			idVolante = servidor.emitirVolante(sesionMedico.getId(), beneficiario1, medico3, medico3);
			assertTrue(idVolante != -1);
			volanteGet = GestorVolantes.consultarVolante(sesionAdmin.getId(), idVolante);
			assertEquals(idVolante, volanteGet.getId());
		} catch(Exception e) {
			fail(e.toString());
		}
	}

}
