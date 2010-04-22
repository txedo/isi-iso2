package pruebas;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Vector;
import persistencia.AgenteFrontend;
import persistencia.FPBeneficiario;
import persistencia.FPCentroSalud;
import persistencia.FPSustitucion;
import persistencia.FPUsuario;
import dominio.UtilidadesDominio;
import dominio.conocimiento.Administrador;
import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.Cabecera;
import dominio.conocimiento.CentroSalud;
import dominio.conocimiento.Citador;
import dominio.conocimiento.DiaSemana;
import dominio.conocimiento.Direccion;
import dominio.conocimiento.Especialista;
import dominio.conocimiento.ICodigosMensajeAuxiliar;
import dominio.conocimiento.ICodigosOperacionesCliente;
import dominio.conocimiento.IConstantes;
import dominio.conocimiento.ISesion;
import dominio.conocimiento.Medico;
import dominio.conocimiento.Pediatra;
import dominio.conocimiento.PeriodoTrabajo;
import dominio.conocimiento.Sesion;
import dominio.conocimiento.Sustitucion;
import dominio.control.GestorMedicos;
import dominio.control.GestorSesiones;
import dominio.control.ServidorFrontend;
import excepciones.FechaNoValidaException;
import excepciones.MedicoInexistenteException;
import excepciones.MedicoYaExistenteException;
import excepciones.OperacionIncorrectaException;
import excepciones.SesionInvalidaException;

/**
 * Pruebas del Gestor de M�dicos.
 */
public class PruebasMedicos extends PruebasBase {

	private ServidorFrontend servidor;
	private CentroSalud centro1, centro2;
	private Medico medico1, medico2, medico3, medico4, medico5;
	private Citador citador1;
	private Administrador admin1;
	private PeriodoTrabajo periodo11, periodo12;
	private PeriodoTrabajo periodo21;
	private PeriodoTrabajo periodo31, periodo32;
	private Beneficiario beneficiario1;
	private Direccion direccion1;
	private ISesion sesionCitador, sesionMedico, sesionAdmin;
	private Pediatra pediatra;
	private Especialista especialista;
	private Cabecera cabecera;
	private ClientePrueba clienteAdmin, clienteMedico;
	
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
			centro2 = new CentroSalud("Centro Nuevo", "Calle Ninguna m�s, s/n");
			medico1 = new Medico("12345678", "medPrueba", UtilidadesDominio.encriptarPasswordSHA1("abcdef"), "Eduardo", "P. C.", "", "", "", pediatra);
			medico2 = new Medico("87654321", "medico2", UtilidadesDominio.encriptarPasswordSHA1("xxx"), "Carmen", "G. G.", "carmen@gmail.com", "", "", cabecera);
			medico3 = new Medico("58782350", "jjj", UtilidadesDominio.encriptarPasswordSHA1("jjj"), "Juan", "P. F.", "", "", "", especialista);
			medico4 = new Medico("91295016", "otro", UtilidadesDominio.encriptarPasswordSHA1("otro"), "Ana", "R. M.", "", "", "", cabecera);
			medico5 = new Medico("23589712", "aaa", UtilidadesDominio.encriptarPasswordSHA1("aaa"), "Noelia", "S. S.", "", "", "", new Especialista("Traumatologia"));
			citador1 = new Citador("11223344", "citador", UtilidadesDominio.encriptarPasswordSHA1("cit123"), "Fernando", "G. P.", "fernan@gmail.com", "", "681871340");
			admin1 = new Administrador("55667788", "admin", UtilidadesDominio.encriptarPasswordSHA1("admin"), "Mar�a", "L. F.", "", "911222333", "");
			medico1.setCentroSalud(centro1);
			medico2.setCentroSalud(centro1);
			medico3.setCentroSalud(centro1);
			medico4.setCentroSalud(centro1);
			medico5.setCentroSalud(centro1);
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
			FPCentroSalud.insertar(centro2);
			FPUsuario.insertar(medico1);
			FPUsuario.insertar(medico2);
			FPUsuario.insertar(medico3);
			FPUsuario.insertar(medico4);
			FPUsuario.insertar(medico5);
			FPUsuario.insertar(citador1);
			FPUsuario.insertar(admin1);
			FPBeneficiario.insertar(beneficiario1);
			// Iniciamos tres sesiones con roles de citador y administrador
			sesionCitador = GestorSesiones.identificar(citador1.getLogin(), "cit123");
			sesionMedico = GestorSesiones.identificar(medico1.getLogin(), "abcdef");
			sesionAdmin = GestorSesiones.identificar(admin1.getLogin(), "admin");
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
			// Intentamos consultar los datos de un m�dico sin permisos
			servidor.getMedico(sesionMedico.getId(), medico1.getNif());
			fail("Se esperaba una excepci�n OperacionIncorrectaException");
		} catch(OperacionIncorrectaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n OperacionIncorrectaException");
		}
		
		try {
			// Intentamos acceder al servidor con un id de sesi�n err�neo
			servidor.getMedico(-12345, medico1.getNif());
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
			// Intentamos obtener los datos de un usuario que no es m�dico
			servidor.getMedico(sesionAdmin.getId(), citador1.getNif());
			fail("Se esperaba una excepci�n MedicoInexistenteException");
		} catch(MedicoInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n MedicoInexistenteException");
		}
		
		try {
			// Obtenemos los datos de un m�dico existente
			medicoGet = (Medico)servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CONSULTAR_USUARIO, medico1.getNif());
			assertEquals(medico1, medicoGet);
		} catch(Exception e) {
			fail(e.toString());
		}
	}

	/** Pruebas de la operaci�n que crea nuevos m�dicos */
	public void testCrearMedico() {
		Medico medico, medicoGet;
		
		try {
			// Intentamos crear un m�dico con la sesi�n del citador
			medico = new Medico("6666666", "medNuevo", "medNuevo", "Juan", "P. C.", "", "", "", especialista);
			servidor.crear(sesionCitador.getId(), medico);
			fail("Se esperaba una excepci�n OperacionIncorrectaException");
		} catch(OperacionIncorrectaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n OperacionIncorrectaException");
		}
		
		try {
			// Intentamos acceder al servidor con un id de sesi�n err�neo
			medico = new Medico("6666666", "medNuevo", "medNuevo", "Juan", "P. C.", "", "", "", especialista);
			servidor.crear(-12345, medico);
			fail("Se esperaba una excepci�n SesionInvalidaException");
		} catch(SesionInvalidaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n SesionInvalidaException");
		}
		
		try {
			// Intentamos a�adir un m�dico con un NIF que ya existe en la BD
			medico = new Medico(citador1.getNif(), "error", "error", "", "", "", "", "", especialista);
			servidor.crear(sesionAdmin.getId(), medico);
			fail("Se esperaba una excepci�n MedicoYaExistenteException");
		} catch(MedicoYaExistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n MedicoYaExistenteException");
		}

		try {
			// Creamos un nuevo m�dico con la sesi�n del administrador
			medico = new Medico("6666666", "medNuevo", "medNuevo", "Juan", "P. C.", "juan67@otro.com", "", "", especialista);
			servidor.crear(sesionAdmin.getId(), medico);
			// Comprobamos que el usuario se ha creado correctamente
			Thread.sleep(100);
			medicoGet = (Medico)servidor.getMedico(sesionAdmin.getId(), medico.getNif());
			medico.setPassword(UtilidadesDominio.encriptarPasswordSHA1("medNuevo"));
			medico.setCentroSalud(medicoGet.getCentroSalud().equals(centro1) ? centro1 : centro2);
			assertEquals(medico, medicoGet);
			// Comprobamos que se ha avisado a los clientes del registro del m�dico
			Thread.sleep(100);
			assertTrue(clienteMedico.getUltimaOperacion() == ICodigosOperacionesCliente.INSERTAR);
			assertEquals(medico, clienteMedico.getUltimoDato());
			assertNull(clienteAdmin.getUltimoDato());
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Intentamos crear un m�dico sin haber ning�n centro
			AgenteFrontend.getAgente().getConexion().prepareStatement("DELETE FROM centros").executeUpdate();
			medico = new Medico("34712394", "otromas", "error", "Juan", "P. C.", "", "", "", especialista);
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
			medico = new Medico("91295019", "mas", "mas", "Luis", "R. M.", "", "999111333", "", pediatra);
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
			medicoGet = servidor.getMedico(sesionAdmin.getId(), medico1.getNif());
			medico1.setPassword(UtilidadesDominio.encriptarPasswordSHA1("abcdef"));
			assertEquals(medico1, medicoGet);
			// Comprobamos que se ha avisado a los clientes del cambio del m�dico
			Thread.sleep(100);
			medico1.setPassword("");
			assertTrue(clienteMedico.getUltimaOperacion() == ICodigosOperacionesCliente.MODIFICAR);
			assertEquals(medico1, clienteMedico.getUltimoDato());
			assertNull(clienteAdmin.getUltimoDato());
		} catch(Exception e) {
			fail(e.toString());
		}
	}

	/** Pruebas de la operaci�n que elimina m�dicos existentes */
	public void testEliminarMedico() {
		Medico medico;
		
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
			medico = new Medico("78256514", "error", "error", "", "", "", "", "", pediatra);
			servidor.eliminar(sesionAdmin.getId(), medico);
			fail("Se esperaba una excepci�n MedicoInexistenteException");
		} catch(MedicoInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n MedicoInexistenteException");
		}
		
		try {
			// Eliminamos un m�dico existente como administrador
			servidor.eliminar(sesionAdmin.getId(), medico2);
			// Comprobamos que se ha avisado a los clientes del cambio del m�dico
			Thread.sleep(100);
			assertTrue(clienteMedico.getUltimaOperacion() == ICodigosOperacionesCliente.ELIMINAR);
			assertEquals(medico2, clienteMedico.getUltimoDato());
			assertNull(clienteAdmin.getUltimoDato());
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	/** Pruebas de la operaci�n que devuelve el horario de un m�dico */
	public void testConsultarHorarioMedico() {
		Hashtable<DiaSemana, Vector<String>> horario;
		Medico medico;
		PeriodoTrabajo periodo;
			
		try {
			// Intentamos consultar el horario de un m�dico con una sesi�n sin permisos
			servidor.mensajeAuxiliar(sesionMedico.getId(), ICodigosMensajeAuxiliar.CONSULTAR_HORARIO_MEDICO, medico1.getNif());
			fail("Se esperaba una excepci�n OperacionIncorrectaException");
		} catch(OperacionIncorrectaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n OperacionIncorrectaException");
		}
		
		try {
			// Intentamos acceder al servidor con un id de sesi�n err�neo
			servidor.mensajeAuxiliar(-12345, ICodigosMensajeAuxiliar.CONSULTAR_HORARIO_MEDICO, medico1.getNif());
			fail("Se esperaba una excepci�n SesionInvalidaException");
		} catch(SesionInvalidaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n SesionInvalidaException");
		}
		
		try {
			// Intentamos obtener el horario de un m�dico inexistente
			medico = new Medico("91295019", "otro2", UtilidadesDominio.encriptarPasswordSHA1("otro"), "Anaasa", "R. M.", "anaasa@uclm.es", "", "", cabecera);
			servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CONSULTAR_HORARIO_MEDICO, medico.getNif());
			fail("Se esperaba una excepci�n MedicoInexistenteException");
		} catch(MedicoInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n MedicoInexistenteException");
		}
		
		try {
			// Intentamos obtener el horario de un usuario que no es m�dico
			servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CONSULTAR_HORARIO_MEDICO, admin1.getNif());
			fail("Se esperaba una excepci�n MedicoInexistenteException");
		} catch(MedicoInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n MedicoInexistenteException");
		}
		
		try {
			// Consultamos el horario de un m�dico correcto				
			horario = GestorMedicos.consultarHorarioMedico(sesionAdmin.getId(), medico1.getNif());
			// Seg�n el calendario de medico1, s�lo se deben
			// devolver horas los mi�rcoles y los viernes
			assertTrue(horario.size() != 0);
			assertTrue(horario.get(DiaSemana.Lunes).size() == 0);
			assertTrue(horario.get(DiaSemana.Martes).size() == 0);
			assertTrue(horario.get(DiaSemana.Miercoles).size() != 0);
			assertTrue(horario.get(DiaSemana.Jueves).size() == 0);
			assertTrue(horario.get(DiaSemana.Viernes).size() != 0);
			// Comprobamos que el n�mero de citas devueltas para cada
			// d�a es el esperado
			periodo = medico1.getCalendario().get(0);
			assertTrue(horario.get(DiaSemana.Miercoles).size() == ((periodo.getNumeroHoras() * 60) / IConstantes.DURACION_CITA));
			periodo = medico1.getCalendario().get(1);
			assertTrue(horario.get(DiaSemana.Viernes).size() == ((periodo.getNumeroHoras() * 60) / IConstantes.DURACION_CITA));
		} catch(Exception e) {
			fail("Se esperaba una excepci�n al obtener el horario de un m�dico");
		}
	}
	
	/** Pruebas de la operaci�n que obtiene los m�dicos de un cierto tipo */
	@SuppressWarnings("unchecked")
	public void testConsultarMedicosTipo() {
		Vector<Medico> medicos;
		
		try {
			// Intentamos consultar los m�dicos de un tipo con una sesi�n sin permisos
			servidor.mensajeAuxiliar(sesionCitador.getId(), ICodigosMensajeAuxiliar.OBTENER_MEDICOS_TIPO, cabecera);
			fail("Se esperaba una excepci�n OperacionIncorrectaException");
		} catch(OperacionIncorrectaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n OperacionIncorrectaException");
		}
		
		try {
			// Intentamos acceder al servidor con un id de sesi�n err�neo
			servidor.mensajeAuxiliar(-12345, ICodigosMensajeAuxiliar.OBTENER_MEDICOS_TIPO, cabecera);
			fail("Se esperaba una excepci�n SesionInvalidaException");
		} catch(SesionInvalidaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n SesionInvalidaException");
		}
		
		try {
			// Obtenemos los m�dicos de cabecera
			medicos = (Vector<Medico>)servidor.mensajeAuxiliar(sesionMedico.getId(), ICodigosMensajeAuxiliar.OBTENER_MEDICOS_TIPO, new Cabecera());
			assertTrue(medicos.size() == 2);
			assertTrue((medicos.get(0).equals(medico2) && medicos.get(1).equals(medico4))
					   || (medicos.get(0).equals(medico4) && medicos.get(1).equals(medico2)));
			medicos = (Vector<Medico>)servidor.mensajeAuxiliar(sesionMedico.getId(), ICodigosMensajeAuxiliar.OBTENER_MEDICOS_TIPO, new Pediatra());
			assertTrue(medicos.size() == 1);
			assertTrue(medicos.get(0).equals(medico1));
			medicos = (Vector<Medico>)servidor.mensajeAuxiliar(sesionMedico.getId(), ICodigosMensajeAuxiliar.OBTENER_MEDICOS_TIPO, new Especialista("Ginecologia"));
			assertTrue(medicos.size() == 1);
			assertTrue(medicos.get(0).equals(medico3));
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	/** Pruebas de la operaci�n que consulta el m�dico que dar� realmente una cita */
	public void testConsultarMedicoCita() {
		Calendar calend;
		Sustitucion sustitucion;
		Medico medico;
		
		try {
			// Intentamos obtener qui�n dar�a una cita con una sesi�n sin permisos
			servidor.mensajeAuxiliar(sesionMedico.getId(), ICodigosMensajeAuxiliar.CONSULTAR_MEDICO_CITA, new Object[] { medico1.getNif(), new Date() });
			fail("Se esperaba una excepci�n OperacionIncorrectaException");
		} catch(OperacionIncorrectaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n OperacionIncorrectaException");
		}
		
		try {
			// Intentamos acceder al servidor con un id de sesi�n err�neo
			servidor.mensajeAuxiliar(-12345, ICodigosMensajeAuxiliar.CONSULTAR_MEDICO_CITA, new Object[] { medico1.getNif(), new Date() });
			fail("Se esperaba una excepci�n SesionInvalidaException");
		} catch(SesionInvalidaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n SesionInvalidaException");
		}
		
		try {
			// Intentamos obtener qui�n dar�a la cita de un m�dico inexistente
			medico = new Medico("91295019", "otro2", UtilidadesDominio.encriptarPasswordSHA1("otro"), "Anaasa", "R. M.", "anaasa@uclm.es", "", "", cabecera);
			servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CONSULTAR_MEDICO_CITA, new Object[] { medico.getNif(), new Date() });
			fail("Se esperaba una excepci�n MedicoInexistenteException");
		} catch(MedicoInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n MedicoInexistenteException");
		}
		
		try {
			// Intentamos obtener qui�n dar�a la cita de un usuario que no es m�dico
			servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CONSULTAR_MEDICO_CITA, new Object[] { admin1.getNif(), new Date() });
			fail("Se esperaba una excepci�n MedicoInexistenteException");
		} catch(MedicoInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n MedicoInexistenteException");
		}
		
		try {
			// Intentamos obtener qui�n dar�a la cita de un m�dico en una
			// fecha en la que el m�dico no tiene trabajo
			calend = new GregorianCalendar(2010, 1, 24, 17, 0); // Mi�rcoles 24/2/2010
			servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CONSULTAR_MEDICO_CITA, new Object[] { medico1.getNif(), calend.getTime() });
			fail("Se esperaba una excepci�n FechaNoValidaException");
		} catch(FechaNoValidaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n FechaNoValidaException");
		}
		
		try {
			// Obtenemos qui�n dar�a la cita de un m�dico sin sustituciones 
			calend = new GregorianCalendar(2010, 1, 24, 12, 0); // Mi�rcoles 24/2/2010
			medico = (Medico)servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CONSULTAR_MEDICO_CITA, new Object[] { medico1.getNif(), calend.getTime() });
			assertEquals(medico, medico1);
			// A�adimos una sustituci�n y comprobamos que el m�dico sustituido
			// ya no es el que dar�a la cita que no puede dar pero s� otra diferente
			calend = new GregorianCalendar(2010, 1, 26); // Viernes 26/2/2010
			sustitucion = new Sustitucion(calend.getTime(), 13, 17, medico3, medico2);
			FPSustitucion.insertar(sustitucion);
			calend = new GregorianCalendar(2010, 1, 26, 16, 30); // Viernes 26/2/2010
			medico = (Medico)servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CONSULTAR_MEDICO_CITA, new Object[] { medico3.getNif(), calend.getTime() });
			assertEquals(medico, medico2);
			calend = new GregorianCalendar(2010, 1, 26, 17, 30); // Viernes 26/2/2010
			medico = (Medico)servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CONSULTAR_MEDICO_CITA, new Object[] { medico3.getNif(), calend.getTime() });
			assertEquals(medico, medico3);
			// Comprobamos que el m�todo funciona con sustituciones recursivas
			calend = new GregorianCalendar(2010, 1, 26); // Viernes 26/2/2010
			sustitucion = new Sustitucion(calend.getTime(), 13, 17, medico2, medico1);
			FPSustitucion.insertar(sustitucion);
			calend = new GregorianCalendar(2010, 1, 26, 16, 30); // Viernes 26/2/2010
			medico = (Medico)servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CONSULTAR_MEDICO_CITA, new Object[] { medico3.getNif(), calend.getTime() });
			assertEquals(medico, medico1);
		} catch(Exception e) {
			fail(e.toString());
		}
	}

	/** Pruebas de las operaciones de la clase Medico */
	@SuppressWarnings("deprecation")
	public void testClaseMedico() {
		Medico medicoClone;
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
		
		try {
			// Comprobamos que los m�todos clone y equals funcionan bien
			medicoClone = (Medico)medico1.clone();
			assertTrue(medico1.equals(medicoClone));
			medicoClone.setTipoMedico(new Cabecera());
			assertFalse(medico1.equals(medicoClone));
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	/** Pruebas con datos nulos */
	public void testDatosNulos() {
		try {
			// Intentamos consultar un m�dico con NIF nulo
			servidor.getMedico(sesionCitador.getId(), null);
			fail("Se esperaba una excepci�n NullPointerException");
		} catch(NullPointerException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n NullPointerException");
		}
		
		try {
			// Intentamos crear un m�dico nulo
			servidor.crear(sesionCitador.getId(), (Medico)null);
			fail("Se esperaba una excepci�n NullPointerException");
		} catch(NullPointerException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n NullPointerException");
		}
		
		try {
			// Intentamos modificar un m�dico nulo
			servidor.modificar(sesionCitador.getId(), (Medico)null);
			fail("Se esperaba una excepci�n NullPointerException");
		} catch(NullPointerException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n NullPointerException");
		}
		
		try {
			// Intentamos eliminar un m�dico nulo
			servidor.eliminar(sesionCitador.getId(), null);
			fail("Se esperaba una excepci�n NullPointerException");
		} catch(NullPointerException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n NullPointerException");
		}
		
		try {
			// Intentamos obtener el horario de un m�dico nulo
			servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CONSULTAR_HORARIO_MEDICO, null);
			fail("Se esperaba una excepci�n NullPointerException");
		} catch(NullPointerException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n NullPointerException");
		}
		
		try {
			// Intentamos obtener los m�dicos de un tipo nulo
			servidor.mensajeAuxiliar(sesionMedico.getId(), ICodigosMensajeAuxiliar.OBTENER_MEDICOS_TIPO, null);
			fail("Se esperaba una excepci�n NullPointerException");
		} catch(NullPointerException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n NullPointerException");
		}
		
		try {
			// Intentamos obtener qui�n dar�a la cita de un m�dico nulo
			servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CONSULTAR_MEDICO_CITA, new Object[] { null, new Date() });
			fail("Se esperaba una excepci�n NullPointerException");
		} catch(NullPointerException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n NullPointerException");
		}		

		try {
			// Intentamos obtener qui�n dar�a una cita en un d�a nulo
			servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CONSULTAR_MEDICO_CITA, new Object[] { medico1.getNif(), null });
			fail("Se esperaba una excepci�n NullPointerException");
		} catch(NullPointerException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n NullPointerException");
		}
	}
	
}
