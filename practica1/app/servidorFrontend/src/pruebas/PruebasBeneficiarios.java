package pruebas;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Vector;
import persistencia.FPBeneficiario;
import persistencia.FPCentroSalud;
import persistencia.FPTipoMedico;
import persistencia.FPUsuario;
import dominio.UtilidadesDominio;
import dominio.conocimiento.Administrador;
import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.Cabecera;
import dominio.conocimiento.CentroSalud;
import dominio.conocimiento.Citador;
import dominio.conocimiento.DiaSemana;
import dominio.conocimiento.Direccion;
import dominio.conocimiento.ICodigosMensajeAuxiliar;
import dominio.conocimiento.ICodigosOperacionesCliente;
import dominio.conocimiento.ISesion;
import dominio.conocimiento.Medico;
import dominio.conocimiento.Pediatra;
import dominio.conocimiento.PeriodoTrabajo;
import dominio.conocimiento.Sesion;
import dominio.control.GestorSesiones;
import dominio.control.ServidorFrontend;
import excepciones.BeneficiarioInexistenteException;
import excepciones.BeneficiarioYaExistenteException;
import excepciones.OperacionIncorrectaException;
import excepciones.SesionInvalidaException;

/**
 * Pruebas del Gestor de Beneficiarios.
 */
public class PruebasBeneficiarios extends PruebasBase {

	private ServidorFrontend servidor;
	private CentroSalud centro1, centro2;
	private Medico medico1, medico2;
	private PeriodoTrabajo periodo1, periodo2, periodo3;
	private Beneficiario beneficiario1, beneficiario2;
	private Citador citador1;
	private Direccion direccion1, direccion2;
	private Administrador admin1;
	private ISesion sesionCitador, sesionAdmin, sesionMedico;
	private Pediatra pediatra;
	private Cabecera cabecera;
	private Date fecha1, fecha2;
	private ClientePrueba clienteAdmin, clienteMedico;
	
	protected void setUp() {
		try {
			// Preparamos la base de datos
			super.setUp();
			// Obtenemos el servidor frontend, que se utilizar� para llamar
			// a los m�todos del gestor y as� probar las dos clases a la vez
			servidor = ServidorFrontend.getServidor();
			// Creamos objetos de prueba
			SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd/MM/yyyy");
			fecha1 = formatoDelTexto.parse("1/8/1951");
			fecha2 = formatoDelTexto.parse("23/2/2002");
			pediatra = new Pediatra();
			cabecera = new Cabecera();
			direccion1 = new Direccion("calle 1", "", "", "", "aadsfaada", "afafssafad", 13500);
			direccion2 = new Direccion("calle 2", "10", "2", "A", "aadsfaada", "afafssafad", 13500);
			centro1 = new CentroSalud("Centro A", "Calle 1, n�1");
			centro2 = new CentroSalud("Centro B", "Calle 2, n�2");
			medico1 = new Medico("12345678A", "medPrueba", UtilidadesDominio.encriptarPasswordSHA1("abcdef"), "Eduardo", "P. C.", "", "", "", pediatra);
			medico2 = new Medico("87654321A", "medico2", UtilidadesDominio.encriptarPasswordSHA1("xxx"), "Carmen", "G. G.", "carmen@gmail.com", "", "666123123", cabecera);
			medico1.setCentroSalud(centro1);
			medico2.setCentroSalud(centro1);
			periodo1 = new PeriodoTrabajo(10, 14, DiaSemana.Miercoles);
			periodo2 = new PeriodoTrabajo(17, 19, DiaSemana.Viernes);
			periodo3 = new PeriodoTrabajo(16, 17, DiaSemana.Lunes);
			medico1.getCalendario().add(periodo1);
			medico1.getCalendario().add(periodo2);
			medico2.getCalendario().add(periodo3);
			citador1 = new Citador("11223344A", "citador", UtilidadesDominio.encriptarPasswordSHA1("cit123"), "Fernando", "G. P.", "", "926111222", "");
			admin1 = new Administrador("55667788A", "admin", UtilidadesDominio.encriptarPasswordSHA1("admin"), "Mar�a", "L. F.", "marilf@yahoo.es", "", "666000000");
			citador1.setCentroSalud(centro1);
			admin1.setCentroSalud(centro1);
			beneficiario1 = new Beneficiario("12345678A", "123456-ab", "bene1", "asdfg", fecha1, direccion1, "add@sf.com", "123456789", "987654321");
			beneficiario1.setCentroSalud(medico2.getCentroSalud());
			beneficiario1.setMedicoAsignado(medico2);
			beneficiario2 = new Beneficiario("46208746A", "164028-de", "bene2", "asadasdfg", fecha2, direccion2, "dos@gmail.com", "923456789", "687654322");
			beneficiario2.setCentroSalud(centro2);
			beneficiario2.setMedicoAsignado(null);
			// Insertamos varios usuarios y beneficiarios
			FPCentroSalud.insertar(centro1);
			FPCentroSalud.insertar(centro2);
			FPUsuario.insertar(medico1);
			FPUsuario.insertar(medico2);
			FPUsuario.insertar(citador1);
			FPUsuario.insertar(admin1);
			FPBeneficiario.insertar(beneficiario1);
			FPBeneficiario.insertar(beneficiario2);
			// Iniciamos tres sesiones con roles de citador, administrador y medico
			sesionCitador = GestorSesiones.identificar(citador1.getLogin(), "cit123");
			sesionAdmin = GestorSesiones.identificar(admin1.getLogin(), "admin");
			sesionMedico = GestorSesiones.identificar(medico1.getLogin(), "abcdef");
			// Registramos dos clientes
			clienteAdmin = new ClientePrueba();
			clienteMedico = new ClientePrueba();
			clienteAdmin.activar(IDatosPruebas.IP_ESCUCHA_CLIENTES);
			clienteMedico.activar(IDatosPruebas.IP_ESCUCHA_CLIENTES);
			GestorSesiones.registrar(sesionAdmin.getId(), clienteAdmin);
			GestorSesiones.registrar(sesionMedico.getId(), clienteMedico);
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	protected void tearDown() {
		try {
			// Cerramos la sesi�n
			GestorSesiones.liberar(((Sesion)sesionCitador).getId());
			GestorSesiones.liberar(((Sesion)sesionAdmin).getId());
			GestorSesiones.liberar(((Sesion)sesionMedico).getId());
			// Cerramos la base de datos
			super.tearDown();
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	/** Pruebas de la operaci�n que obtiene los datos de un beneficiario */
	public void testConsultarBeneficiario() {
		Beneficiario beneficiario;
		
		try {
			// Intentamos acceder al servidor con un id de sesi�n err�neo
			servidor.getBeneficiario(-12345, beneficiario2.getNss());
			fail("Se esperaba una excepci�n SesionInvalidaException");
		} catch(SesionInvalidaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n SesionInvalidaException");
		}
		
		try {
			// Intentamos acceder al servidor con un id de sesi�n err�neo
			servidor.getBeneficiarioPorNSS(-12345, beneficiario2.getNss());
			fail("Se esperaba una excepci�n SesionInvalidaException");
		} catch(SesionInvalidaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n SesionInvalidaException");
		}
		
		try {
			// Intentamos obtener los datos de un usuario que no es beneficiario
			servidor.getBeneficiario(sesionCitador.getId(), citador1.getNif());
			fail("Se esperaba una excepci�n BeneficiarioInexistenteException");
		} catch(BeneficiarioInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n BeneficiarioInexistenteException");
		}
		
		try {
			// Intentamos obtener los datos de un usuario que no es beneficiario
			servidor.getBeneficiarioPorNSS(sesionCitador.getId(), citador1.getNif());
			fail("Se esperaba una excepci�n BeneficiarioInexistenteException");
		} catch(BeneficiarioInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n BeneficiarioInexistenteException");
		}
		
		try {
			// Obtenemos los datos de un beneficiario por su NIF
			beneficiario = servidor.getBeneficiario(sesionCitador.getId(), beneficiario1.getNif());
			assertEquals(beneficiario, beneficiario1);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Obtenemos los datos de un beneficiario sin m�dico por su NIF
			beneficiario = servidor.getBeneficiario(sesionCitador.getId(), beneficiario2.getNif());
			assertEquals(beneficiario, beneficiario2);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Obtenemos los datos de un beneficiario por su NSS
			beneficiario = servidor.getBeneficiarioPorNSS(sesionCitador.getId(), beneficiario1.getNss());
			assertEquals(beneficiario, beneficiario1);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Obtenemos los datos de un beneficiario sin m�dico por su NSS
			beneficiario = servidor.getBeneficiarioPorNSS(sesionCitador.getId(), beneficiario2.getNss());
			assertEquals(beneficiario, beneficiario2);
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	/** Pruebas de la operaci�n que crea nuevos beneficiarios */
	public void testCrearBeneficiario() {
		Beneficiario beneficiario, beneficiarioGet;
		
		try {
			// Intentamos crear un nuevo beneficiario con la sesi�n de m�dico
			beneficiario = new Beneficiario("77777777W", "131716-co", "error", "error", fecha1, direccion1, "", "123456789", "987654321");
			beneficiario.setCentroSalud(medico2.getCentroSalud());
			beneficiario.setMedicoAsignado(medico2);
			servidor.crear(sesionMedico.getId(), beneficiario);
			fail("Se esperaba una excepci�n OperacionIncorrectaException");
		} catch(OperacionIncorrectaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n OperacionIncorrectaException");
		}
		
		try {
			// Intentamos acceder al servidor con un id de sesi�n err�neo
			beneficiario = new Beneficiario("77777777W", "131716-co", "error", "error", fecha1, direccion1, "", "123456789", "987654321");
			beneficiario.setCentroSalud(medico2.getCentroSalud());
			beneficiario.setMedicoAsignado(medico2);
			servidor.crear(-12345, beneficiario);
			fail("Se esperaba una excepci�n SesionInvalidaException");
		} catch(SesionInvalidaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n SesionInvalidaException");
		}
		
		try {
			// Intentamos a�adir un beneficiario con un NIF que ya existe en la BD
			beneficiario = new Beneficiario(beneficiario1.getNif(), "009988009988", "error", "error", fecha1, direccion2, "", "123456789", "987654321");
			beneficiario.setCentroSalud(centro1);
			servidor.crear(sesionAdmin.getId(), beneficiario);
			fail("Se esperaba una excepci�n BeneficiarioYaExistenteException");
		} catch(BeneficiarioYaExistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n BeneficiarioYaExistenteException");
		}
		
		try {
			// Intentamos a�adir un beneficiario con el NIF de un usuario
			beneficiario = new Beneficiario(medico1.getNif(), "009988009988", "error", "error", fecha1, direccion2, "", "123456789", "987654321");
			beneficiario.setCentroSalud(centro1);
			servidor.crear(sesionAdmin.getId(), beneficiario);
			fail("Se esperaba una excepci�n BeneficiarioYaExistenteException");
		} catch(BeneficiarioYaExistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n BeneficiarioYaExistenteException");
		}
		
		try {
			// Intentamos a�adir un beneficiario con un NSS que ya existe en la BD
			beneficiario = new Beneficiario("10239184L", beneficiario1.getNss(), "error", "error", fecha1, direccion1, "", "123456789", "987654321");
			beneficiario.setCentroSalud(centro1);
			servidor.crear(sesionAdmin.getId(), beneficiario);
			fail("Se esperaba una excepci�n BeneficiarioYaExistenteException");
		} catch(BeneficiarioYaExistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n BeneficiarioYaExistenteException");
		}
		
		try {
			// Creamos un nuevo beneficiario con la sesi�n del administrador
			beneficiario = new Beneficiario("66666666A", "141414141414", "beNuevo", "nuevos", fecha2, direccion2, "luna@hotmail.com", "34698124", "67912312");
			beneficiario.setCentroSalud(centro1);
			beneficiario.setMedicoAsignado(null);
			servidor.crear(sesionAdmin.getId(), beneficiario);
			// Comprobamos que el beneficiario se ha creado correctamente
			beneficiarioGet = servidor.getBeneficiario(sesionAdmin.getId(), beneficiario.getNif());
			beneficiario.setMedicoAsignado(beneficiarioGet.getMedicoAsignado());
			assertEquals(beneficiario, beneficiarioGet);			
			// Vemos si se le ha asignado un pediatra como m�dico de cabecera
			assertNotNull(beneficiarioGet.getMedicoAsignado());
			assertEquals(FPTipoMedico.consultar(beneficiarioGet.getMedicoAsignado().getNif()), new Pediatra());
			// Comprobamos que se ha avisado a los clientes del registro del beneficiario
			Thread.sleep(100);
			beneficiario.setMedicoAsignado(null);
			assertTrue(clienteMedico.getUltimaOperacion() == ICodigosOperacionesCliente.INSERTAR);
			assertEquals(beneficiario, clienteMedico.getUltimoDato());
			assertNull(clienteAdmin.getUltimoDato());
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Creamos un nuevo beneficiario con la sesi�n del administrador
			beneficiario = new Beneficiario("88888888B", "262626262626", "beNuevo", "nuevos", fecha1, direccion2, "luna@hotmail.com", "34698124", "67912312");
			beneficiario.setCentroSalud(centro1);
			beneficiario.setMedicoAsignado(null);
			servidor.crear(sesionAdmin.getId(), beneficiario);
			// Comprobamos que el beneficiario se ha creado correctamente
			beneficiarioGet = servidor.getBeneficiario(sesionAdmin.getId(), beneficiario.getNif());
			beneficiario.setMedicoAsignado(beneficiarioGet.getMedicoAsignado());
			assertEquals(beneficiario, beneficiarioGet);			
			// Vemos si se le ha asignado un m�dico de cabecera que no es pediatra
			assertNotNull(beneficiarioGet.getMedicoAsignado());
			assertEquals(FPTipoMedico.consultar(beneficiarioGet.getMedicoAsignado().getNif()), new Cabecera());
		} catch(Exception e) {
			fail(e.toString());
		}
	}

	/** Pruebas de la operaci�n que modifica beneficiarios existentes **/
	public void testModificarBeneficiario() {
		Beneficiario beneficiario, beneficiarioGet;
		
		try {
			// Intentamos modificar un beneficiario con la sesi�n del m�dico
			beneficiario1.setCentroSalud(centro1);
			beneficiario1.setCorreo("ninguno@ninguno.com");
			servidor.modificar(sesionMedico.getId(), beneficiario1);
			fail("Se esperaba una excepci�n OperacionIncorrectaException");
		} catch(OperacionIncorrectaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n OperacionIncorrectaException");
		}
		
		try {
			// Intentamos acceder al servidor con un id de sesi�n err�neo
			beneficiario1.setApellidos("nuevos apellidos");
			servidor.modificar(-12345, beneficiario1);
			fail("Se esperaba una excepci�n SesionInvalidaException");
		} catch(SesionInvalidaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n SesionInvalidaException");
		}
		
		try {
			// Intentamos modificar un beneficiario que a�n no se ha creado
			beneficiario = new Beneficiario("21412395", "131314-as", "error", "error", fecha2, direccion1, "", "123456789", "987654321");
			beneficiario.setCentroSalud(centro1);
			servidor.modificar(sesionAdmin.getId(), beneficiario);
			fail("Se esperaba una excepci�n BeneficiarioInexistenteException");
		} catch(BeneficiarioInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n BeneficiarioInexistenteException");
		}
		
		try {
			// Modificamos los datos de un benficiario existente como administrador
			beneficiario1.setCentroSalud(centro1);
			beneficiario1.setNombre("beneCambiado");
			servidor.modificar(sesionAdmin.getId(), beneficiario1);
			// Comprobamos que el beneficiario se haya actualizado correctamente
			beneficiarioGet = servidor.getBeneficiario(sesionAdmin.getId(), beneficiario1.getNif());
			assertEquals(beneficiario1, beneficiarioGet);
			// Comprobamos que se ha avisado a los clientes del cambio del beneficiario
			Thread.sleep(100);
			assertTrue(clienteMedico.getUltimaOperacion() == ICodigosOperacionesCliente.MODIFICAR);
			assertEquals(beneficiario1, clienteMedico.getUltimoDato());
			assertNull(clienteAdmin.getUltimoDato());
		} catch(Exception e) {
			fail(e.toString());
		}

		try {
			// Modificamos la fecha de nacimiento de un beneficiario para
			// que se asigne autom�ticamente un nuevo m�dico
			beneficiario1.setFechaNacimiento(fecha2);
			servidor.modificar(sesionCitador.getId(), beneficiario1);
			// Comprobamos que el beneficiario se haya actualizado correctamente
			beneficiarioGet = servidor.getBeneficiario(sesionCitador.getId(), beneficiario1.getNif());
			assertFalse(beneficiarioGet.getMedicoAsignado().equals(beneficiario1.getMedicoAsignado()));
			beneficiario1.setMedicoAsignado(beneficiarioGet.getMedicoAsignado());
			assertEquals(beneficiario1, beneficiarioGet);
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	/** Pruebas de la operaci�n que elimina beneficiarios existentes  */
	public void testEliminarBeneficiario() {
		Beneficiario beneficiario;
		
		try {
			// Intentamos eliminar un beneficiario con la sesi�n del m�dico
			servidor.mensajeAuxiliar(sesionMedico.getId(), ICodigosMensajeAuxiliar.ELIMINAR_BENEFICIARIO, beneficiario1);
			fail("Se esperaba una excepci�n OperacionIncorrectaException");
		} catch(OperacionIncorrectaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n OperacionIncorrectaException");
		}
		
		try {
			// Intentamos acceder al servidor con un id de sesi�n err�neo
			servidor.mensajeAuxiliar(-12345, ICodigosMensajeAuxiliar.ELIMINAR_BENEFICIARIO, beneficiario1);
			fail("Se esperaba una excepci�n SesionInvalidaException");
		} catch(SesionInvalidaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n SesionInvalidaException");
		}

		try {
			// Intentamos eliminar un beneficiario que a�n no se ha creado
			beneficiario = new Beneficiario("21412395", "131314-as", "error", "error", fecha2, direccion1, "", "123456789", "987654321");
			servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.ELIMINAR_BENEFICIARIO, beneficiario);
			fail("Se esperaba una excepci�n BeneficiarioInexistenteException");
		} catch(BeneficiarioInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n BeneficiarioInexistenteException");
		}
		
		try {
			// Eliminamos un beneficiario existente
			servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.ELIMINAR_BENEFICIARIO, beneficiario1);
			// Comprobamos que se ha avisado a los clientes del borrado del beneficiario
			Thread.sleep(100);
			assertTrue(clienteMedico.getUltimaOperacion() == ICodigosOperacionesCliente.ELIMINAR);
			assertEquals(beneficiario1, clienteMedico.getUltimoDato());
			assertNull(clienteAdmin.getUltimoDato());
			// Comprobamos que el beneficiario ya no existe
			beneficiario = servidor.getBeneficiario(sesionAdmin.getId(), beneficiario1.getNif());
			fail("Se esperaba una excepci�n BeneficiarioInexistenteException");
		} catch(BeneficiarioInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n BeneficiarioInexistenteException");
		}
	}
	
	/** Pruebas de la operaci�n que obtiene los beneficiarios de un m�dico */
	@SuppressWarnings("unchecked")
	public void testConsultarBeneficiariosMedico() {
		Vector<Beneficiario> beneficiarios;
				
		try {
			// Intentamos consultar los beneficiarios de un m�dico con la sesi�n del citador
			servidor.mensajeAuxiliar(sesionCitador.getId(), ICodigosMensajeAuxiliar.CONSULTAR_BENEFICIARIOS_MEDICO, medico1.getNif());
			fail("Se esperaba una excepci�n OperacionIncorrectaException");
		} catch(OperacionIncorrectaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n OperacionIncorrectaException");
		}
		
		try {
			// Intentamos acceder al servidor con un id de sesi�n err�neo
			servidor.mensajeAuxiliar(-12345, ICodigosMensajeAuxiliar.CONSULTAR_BENEFICIARIOS_MEDICO, medico1.getNif());
			fail("Se esperaba una excepci�n SesionInvalidaException");
		} catch(SesionInvalidaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n SesionInvalidaException");
		}
		
		try {
			// Obtenemos los beneficiarios de los m�dicos del sistema
			beneficiarios = (Vector<Beneficiario>)servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CONSULTAR_BENEFICIARIOS_MEDICO, medico2.getNif());
			assertTrue(beneficiarios.size() == 1);
			assertTrue(beneficiarios.get(0).equals(beneficiario1));
			beneficiarios = (Vector<Beneficiario>)servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CONSULTAR_BENEFICIARIOS_MEDICO, medico1.getNif());
			assertTrue(beneficiarios.size() == 0);
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	/** Pruebas de las operaciones de la clase Beneficiario */
	public void testClaseBeneficiario() {
		GregorianCalendar calend;
		Beneficiario beneficiario, beneficiarioClone;
		
		try {
			// Creamos un beneficiario nacido ayer
			calend = new GregorianCalendar();
			calend.roll(Calendar.DAY_OF_MONTH, false);
			beneficiario = new Beneficiario("11223344P", "001199881100", "ABC", "DEF", calend.getTime(), direccion1, "", "", "");
			assertTrue(beneficiario.getEdad() == 0);
			// Creamos un beneficiario de 1 a�o
			calend = new GregorianCalendar();
			calend.roll(Calendar.YEAR, false);
			beneficiario.setFechaNacimiento(calend.getTime());
			assertTrue(beneficiario.getEdad() == 1);
			// Creamos un beneficiario de 50 a�os
			calend = new GregorianCalendar();
			calend.add(Calendar.YEAR, -50);
			calend.add(Calendar.DAY_OF_MONTH, 3);
			beneficiario.setFechaNacimiento(calend.getTime());
			assertTrue(beneficiario.getEdad() == 50);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Comprobamos que los m�todos toString y equals no producen
			// excepci�n si el centro de salud o el m�dico son nulos
			beneficiario = new Beneficiario("11223344P", "001199881100", "ABC", "DEF", new Date(), direccion1, "", "", "");
			assertNotNull(beneficiario.toString());
			assertTrue(beneficiario.equals(beneficiario));
			// Comprobamos que los m�todos clone y equals funcionan bien
			beneficiarioClone = (Beneficiario)beneficiario.clone();
			assertTrue(beneficiario.equals(beneficiarioClone));
			beneficiarioClone.setCentroSalud(centro1);
			assertFalse(beneficiario.equals(beneficiarioClone));
		} catch(Exception e) {
			fail(e.toString());
		}
	}

	/** Pruebas con datos nulos */
	public void testDatosNulos() {
		Beneficiario beneficiario;
		
		try {
			// Intentamos obtener un beneficiario con NIF nulo
			servidor.getBeneficiario(sesionCitador.getId(), null);
			fail("Se esperaba una excepci�n NullPointerException");
		} catch (NullPointerException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n NullPointerException");
		}
		
		try {
			// Intentamos obtener un beneficiario con NSS nulo
			servidor.getBeneficiarioPorNSS(sesionCitador.getId(), null);
			fail("Se esperaba una excepci�n NullPointerException");
		} catch (NullPointerException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n NullPointerException");
		}
		
		try {
			// Intentamos crear un beneficiario nulo
			servidor.crear(sesionAdmin.getId(), (Beneficiario)null);
			fail("Se esperaba una excepci�n NullPointerException");
		} catch(NullPointerException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n NullPointerException");
		}
		
		try {
			// Intentamos crear un beneficiario con un centro de salud nulo
			beneficiario = new Beneficiario("66666666A", "14124as-cd", "beNuevo", "nuevos", fecha2, direccion2, "luna@hotmail.com", "34698124", "67912312");
			beneficiario.setCentroSalud(null);
			servidor.crear(sesionAdmin.getId(), beneficiario);
			fail("Se esperaba una excepci�n NullPointerException");
		} catch(NullPointerException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n NullPointerException");
		}
		try {
			// Intentamos modificar un beneficiario nulo
			servidor.modificar(sesionAdmin.getId(), (Beneficiario)null);
			fail("Se esperaba NullPointerException");
		} catch (NullPointerException e) {
		} catch(Exception e) {
			fail("Se esperaba NullPointerException");
		}
		
		try {
			// Intentamos modificar un beneficiario con un centro de salud nulo
			beneficiario1.setCentroSalud(null);
			servidor.modificar(sesionAdmin.getId(), beneficiario1);
			fail("Se esperaba NullPointerException");
		} catch (NullPointerException e) {
		} catch(Exception e) {
			fail("Se esperaba NullPointerException");
		}
		
		try {
			// Intentamos eliminar un beneficiario nulo
			servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.ELIMINAR_BENEFICIARIO, null);
			fail("Se esperaba NullPointerException");
		} catch (NullPointerException e) {
		} catch(Exception e) {
			fail("Se esperaba NullPointerException");
		}

		try {
			// Intentamos consultar los beneficiarios de un m�dico con NIF nulo
			servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CONSULTAR_BENEFICIARIOS_MEDICO, null);
			fail("Se esperaba NullPointerException");
		} catch(NullPointerException e) {
		} catch(Exception e) {
			fail("Se esperaba NullPointerException");
		}
	}
	
}