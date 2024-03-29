package pruebas;

import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;
import persistencia.AgenteFrontend;
import persistencia.FPBeneficiario;
import persistencia.FPCentroSalud;
import persistencia.FPCita;
import persistencia.FPUsuario;
import dominio.UtilidadesDominio;
import dominio.conocimiento.Administrador;
import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.Cabecera;
import dominio.conocimiento.CentroSalud;
import dominio.conocimiento.Cita;
import dominio.conocimiento.Citador;
import dominio.conocimiento.DiaSemana;
import dominio.conocimiento.Direccion;
import dominio.conocimiento.Especialista;
import dominio.conocimiento.ICodigosMensajeAuxiliar;
import dominio.conocimiento.ICodigosOperacionesCliente;
import dominio.conocimiento.ISesion;
import dominio.conocimiento.Medico;
import dominio.conocimiento.Pediatra;
import dominio.conocimiento.PeriodoTrabajo;
import dominio.conocimiento.Sesion;
import dominio.conocimiento.Usuario;
import dominio.control.GestorSesiones;
import dominio.control.ServidorFrontend;
import excepciones.OperacionIncorrectaException;
import excepciones.SesionInvalidaException;
import excepciones.UsuarioInexistenteException;
import excepciones.UsuarioYaExistenteException;

/**
 * Pruebas del Gestor de Usuarios.
 */
public class PruebasUsuarios extends PruebasBase {

	private ServidorFrontend servidor;
	private CentroSalud centro1;
	private Medico medico1, medico2, medico3, medico4;
	private Citador citador1;
	private Administrador admin1;
	private PeriodoTrabajo periodo11, periodo12;
	private PeriodoTrabajo periodo21, periodo22;
	private PeriodoTrabajo periodo31, periodo32;
	private Beneficiario beneficiario1;
	private Direccion direccion1;
	private ISesion sesionCitador, sesionAdmin, sesionMedico;
	private Pediatra pediatra;
	private Especialista especialista;
	private Cabecera cabecera;
	private ClientePrueba clienteAdmin, clienteCitador, clienteMedico;
	private boolean sesionCitadorCerrada;
	
	@SuppressWarnings("deprecation")
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
			medico1 = new Medico("12345678", "medPrueba", UtilidadesDominio.encriptarPasswordSHA1("abcdef"), "Eduardo", "P. C.", "", "", "", pediatra);
			medico2 = new Medico("87654321", "medico2", UtilidadesDominio.encriptarPasswordSHA1("xxx"), "Carmen", "G. G.", "carmen@gmail.com", "", "", cabecera);
			medico3 = new Medico("58782350", "jjj", UtilidadesDominio.encriptarPasswordSHA1("jjj"), "Juan", "P. F.", "", "987654321", "678901234", especialista);
			medico4 = new Medico("91295016", "otro", UtilidadesDominio.encriptarPasswordSHA1("otro"), "Ana", "R. M.", "anarm87@uclm.es", "", "666777012", cabecera);
			citador1 = new Citador("11223344", "citador", UtilidadesDominio.encriptarPasswordSHA1("cit123"), "Fernando", "G. P.", "", "", "");
			admin1 = new Administrador("55667788", "admin", UtilidadesDominio.encriptarPasswordSHA1("admin"), "Mar�a", "L. F.", "mari@terra.com", "", "666111222");
			medico1.setCentroSalud(centro1);
			medico2.setCentroSalud(centro1);
			medico3.setCentroSalud(centro1);
			medico4.setCentroSalud(centro1);
			citador1.setCentroSalud(centro1);
			admin1.setCentroSalud(centro1);
			periodo11 = new PeriodoTrabajo(10, 14, DiaSemana.Miercoles);
			periodo12 = new PeriodoTrabajo(17, 19, DiaSemana.Viernes);
			periodo21 = new PeriodoTrabajo(16, 20, DiaSemana.Lunes);
			periodo22 = new PeriodoTrabajo(9, 14, DiaSemana.Jueves);
			periodo31 = new PeriodoTrabajo(9, 13, DiaSemana.Martes);
			periodo32 = new PeriodoTrabajo(16, 18, DiaSemana.Viernes);
			medico1.getCalendario().add(periodo11);
			medico1.getCalendario().add(periodo12);
			medico2.getCalendario().add(periodo21);
			medico2.getCalendario().add(periodo22);
			medico3.getCalendario().add(periodo31);
			medico3.getCalendario().add(periodo32);
			direccion1 = new Direccion("calle 1", "", "", "", "aadsfaada", "afafssafad", 13500);
			beneficiario1 = new Beneficiario("88484848L", "123456-ab", "bene1", "asdfg", new Date(1950 - 1900, 7, 20), direccion1, "add@sf.com", "123456789", "987654321");
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
			// Iniciamos dos sesiones con roles de citador y administrador
			sesionCitador = GestorSesiones.identificar(citador1.getLogin(), "cit123");
			sesionAdmin = GestorSesiones.identificar(admin1.getLogin(), "admin");
			sesionMedico = GestorSesiones.identificar(medico1.getLogin(), "abcdef");
			sesionCitadorCerrada = false;
			// Registramos dos clientes
			clienteAdmin = new ClientePrueba();
			clienteCitador = new ClientePrueba();
			clienteMedico = new ClientePrueba();
			clienteAdmin.activar(IDatosPruebas.IP_ESCUCHA_CLIENTES);
			clienteCitador.activar(IDatosPruebas.IP_ESCUCHA_CLIENTES);
			clienteMedico.activar(IDatosPruebas.IP_ESCUCHA_CLIENTES);
			GestorSesiones.registrar(sesionAdmin.getId(), clienteAdmin);
			GestorSesiones.registrar(sesionCitador.getId(), clienteCitador);
			GestorSesiones.registrar(sesionMedico.getId(), clienteMedico);
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	protected void tearDown() {
		try {
			// Cerramos la sesi�n
			if(!sesionCitadorCerrada) {
				GestorSesiones.liberar(((Sesion)sesionCitador).getId());
			}
			GestorSesiones.liberar(((Sesion)sesionAdmin).getId());
			// Cerramos la base de datos
			super.tearDown();
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	/** Pruebas de la operaci�n que consulta los datos de un usuario */
	public void testConsultarUsuario() {
		Usuario usuario;
		
		try {
			// Intentamos consultar los datos de un usuario sin permisos
			servidor.mensajeAuxiliar(sesionCitador.getId(), ICodigosMensajeAuxiliar.CONSULTAR_USUARIO, medico1.getNif());
			fail("Se esperaba una excepci�n OperacionIncorrectaException");
		} catch(OperacionIncorrectaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n OperacionIncorrectaException");
		}
		
		try {
			// Intentamos acceder al servidor con un id de sesi�n err�neo
			servidor.mensajeAuxiliar(-12345, ICodigosMensajeAuxiliar.CONSULTAR_USUARIO, medico1.getNif());
			fail("Se esperaba una excepci�n SesionInvalidaException");
		} catch(SesionInvalidaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n SesionInvalidaException");
		}
		
		try {
			// Intentamos acceder al servidor con un id de sesi�n err�neo
			servidor.mensajeAuxiliar(-12345, ICodigosMensajeAuxiliar.CONSULTAR_PROPIO_USUARIO, null);
			fail("Se esperaba una excepci�n SesionInvalidaException");
		} catch(SesionInvalidaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n SesionInvalidaException");
		}
		
		try {
			// Intentamos obtener los datos de un usuario que no existe
			servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CONSULTAR_USUARIO, "99001290W");
			fail("Se esperaba una excepci�n UsuarioInexistenteException");
		} catch(UsuarioInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n UsuarioInexistenteException");
		}
		
		try {
			// Obtenemos los datos de varios usuarios existentes
			usuario = (Usuario)servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CONSULTAR_USUARIO, medico1.getNif());
			assertEquals(medico1, usuario);
			usuario = (Usuario)servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CONSULTAR_USUARIO, admin1.getNif());
			assertEquals(admin1, usuario);
			// Obtenemos los datos del propio usuario
			usuario = (Usuario)servidor.mensajeAuxiliar(sesionCitador.getId(), ICodigosMensajeAuxiliar.CONSULTAR_PROPIO_USUARIO, null);
			assertEquals(citador1, usuario);
			usuario = (Usuario)servidor.mensajeAuxiliar(sesionMedico.getId(), ICodigosMensajeAuxiliar.CONSULTAR_PROPIO_USUARIO, null);
			assertEquals(medico1, usuario);
		} catch(Exception e) {
			fail(e.toString());
		}
	}

	/** Pruebas de la operaci�n que crea nuevos usuarios */
	public void testCrearUsuario() {
		Usuario usuario, usuarioGet;
		
		try {
			// Intentamos crear un usuario con la sesi�n del citador
			usuario = new Medico("6666666", "medNuevo", "medNuevo", "Juan", "P. C.", "", "", "", especialista);
			servidor.mensajeAuxiliar(sesionCitador.getId(), ICodigosMensajeAuxiliar.CREAR_USUARIO, usuario);
			fail("Se esperaba una excepci�n OperacionIncorrectaException");
		} catch(OperacionIncorrectaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n OperacionIncorrectaException");
		}
		
		try {
			// Intentamos acceder al servidor con un id de sesi�n err�neo
			usuario = new Medico("6666666", "medNuevo", "medNuevo", "Juan", "P. C.", "", "", "", especialista);
			servidor.mensajeAuxiliar(-12345, ICodigosMensajeAuxiliar.CREAR_USUARIO, usuario);
			fail("Se esperaba una excepci�n SesionInvalidaException");
		} catch(SesionInvalidaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n SesionInvalidaException");
		}
		
		try {
			// Intentamos a�adir un usuario con un NIF que ya existe en la BD
			usuario = new Administrador(citador1.getNif(), "error", "error", "", "", "", "", "");
			servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CREAR_USUARIO, usuario);
			fail("Se esperaba una excepci�n UsuarioYaExistenteException");
		} catch(UsuarioYaExistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n UsuarioYaExistenteException");
		}

		try {
			// Intentamos a�adir un usuario con el NIF de un beneficiario
			usuario = new Medico(beneficiario1.getNif(), "error", "error", "", "", "", "", "", cabecera);
			servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CREAR_USUARIO, usuario);
			fail("Se esperaba una excepci�n UsuarioYaExistenteException");
		} catch(UsuarioYaExistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n UsuarioYaExistenteException");
		}

		try {
			// Intentamos a�adir un usuario con un login existente
			usuario = new Citador("11884654L", medico3.getLogin(), "error", "", "", "", "", "");
			servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CREAR_USUARIO, usuario);
			fail("Se esperaba una excepci�n UsuarioYaExistenteException");
		} catch(UsuarioYaExistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n UsuarioYaExistenteException");
		}
		
		try {
			// Creamos un nuevo usuario con la sesi�n del administrador
			usuario = new Administrador("77777777", "adminnuevo", "nuevoadmin", "Pepe", "A. B.", "pepeadmin@yahoo.es", "987111111", "678111111");
			servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CREAR_USUARIO, usuario);
			// Comprobamos que el usuario se ha creado correctamente
			// (al crear el usuario su contrase�a se habr� encriptado
			// y se le habr� asignado el �nico centro que hay)
			Thread.sleep(100);
			usuarioGet = (Usuario)servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CONSULTAR_USUARIO, usuario.getNif());
			usuario.setPassword(UtilidadesDominio.encriptarPasswordSHA1("nuevoadmin"));
			usuario.setCentroSalud(centro1);
			assertEquals(usuario, usuarioGet);
			// Comprobamos que se ha avisado a los clientes del registro del usuario
			Thread.sleep(100);
			assertTrue(clienteCitador.getUltimaOperacion() == ICodigosOperacionesCliente.INSERTAR);
			assertEquals(usuario, clienteCitador.getUltimoDato());
			assertNull(clienteAdmin.getUltimoDato());
			// Creamos un nuevo m�dico con la sesi�n del administrador
			usuario = new Medico("6666666", "medNuevo", "medNuevo", "Juan", "P. C.", "", "", "", especialista);
			servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CREAR_USUARIO, usuario);
			// Comprobamos que el usuario se ha creado correctamente
			usuarioGet = (Usuario)servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CONSULTAR_USUARIO, usuario.getNif());
			usuario.setPassword(UtilidadesDominio.encriptarPasswordSHA1("medNuevo"));
			usuario.setCentroSalud(centro1);
			assertEquals(usuario, usuarioGet);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Intentamos crear un usuario sin haber ning�n centro
			AgenteFrontend.getAgente().getConexion().prepareStatement("DELETE FROM centros").executeUpdate();
			usuario = new Medico("34712394", "otromas", "error", "Juan", "P. C.", "", "", "", especialista);
			servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CREAR_USUARIO, usuario);
			fail("Se esperaba una excepci�n SQLException");
		} catch(SQLException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n SQLException");
		}
	}

	/** Pruebas de la operaci�n que modifica usuarios existentes */
	@SuppressWarnings("deprecation")
	public void testModificarUsuario() {
		Vector<Cita> citas;
		Cita cita;
		Usuario usuario, usuarioGet;
		
		try {
			// Intentamos modificar un usuario con la sesi�n del citador
			servidor.mensajeAuxiliar(sesionCitador.getId(), ICodigosMensajeAuxiliar.MODIFICAR_USUARIO, medico2);
			fail("Se esperaba una excepci�n OperacionIncorrectaException");
		} catch(OperacionIncorrectaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n OperacionIncorrectaException");
		}
		
		try {
			// Intentamos acceder al servidor con un id de sesi�n err�neo
			servidor.mensajeAuxiliar(-12345, ICodigosMensajeAuxiliar.MODIFICAR_USUARIO, medico2);
			fail("Se esperaba una excepci�n SesionInvalidaException");
		} catch(SesionInvalidaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n SesionInvalidaException");
		}
		
		try {
			// Intentamos modificar un usuario inexistente
			usuario = new Citador("91295019", "otro2", "otro", "Anaasa", "R. M.", "", "", "");
			servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.MODIFICAR_USUARIO, usuario);
			fail("Se esperaba una excepci�n UsuarioInexistenteException");
		} catch(UsuarioInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n UsuarioInexistenteException");
		}
		
		try {
			// Insertamos nuevas citas para el m�dico que se va a borrar
			cita = new Cita(new Date(2009 - 1900, 1, 9, 17, 30), 15, beneficiario1, medico2);
			FPCita.insertar(cita);
			cita = new Cita(new Date(2015 - 1900, 2, 9, 17, 30), 15, beneficiario1, medico2);
			FPCita.insertar(cita);
			cita = new Cita(new Date(2015 - 1900, 2, 12, 10, 30), 15, beneficiario1, medico2);
			FPCita.insertar(cita);
			citas = FPCita.consultarPorBeneficiario(beneficiario1.getNif());
			assertTrue(citas.size() == 3);
			// Modificamos los datos de un m�dico existente sin tocar la contrase�a
			medico2.setLogin("medCambiado");
			medico2.setApellidos("P. D.");
			medico2.getCalendario().remove(periodo22);
			medico2.setPassword("");
			servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.MODIFICAR_USUARIO, medico2);
			// Comprobamos que el m�dico se haya actualizado correctamente
			// (la contrase�a devuelta debe ser la original, "xxx", encriptada)
			usuarioGet = (Usuario)servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CONSULTAR_USUARIO, medico2.getNif());
			medico2.setPassword(UtilidadesDominio.encriptarPasswordSHA1("xxx"));
			assertEquals(medico2, usuarioGet);
			// Comprobamos que se ha avisado a los clientes del cambio del m�dico
			Thread.sleep(100);
			medico2.setPassword("");
			assertTrue(clienteCitador.getUltimaOperacion() == ICodigosOperacionesCliente.MODIFICAR);
			assertEquals(medico2, clienteCitador.getUltimoDato());
			assertNull(clienteAdmin.getUltimoDato());
			// Comprobamos que se ha eliminado la cita pendiente del
			// m�dico que queda fuera de su nuevo calendario
			citas = FPCita.consultarPorBeneficiario(beneficiario1.getNif());
			assertTrue(citas.size() == 2);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Modificamos los datos de un usuario existente cambiando la contrase�a
			admin1.setPassword("zzz123");
			servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.MODIFICAR_USUARIO, admin1);
			// Comprobamos que el usuario se haya actualizado correctamente
			// (la contrase�a devuelta debe ser la nueva, "zzz123", encriptada)
			usuarioGet = (Usuario)servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CONSULTAR_USUARIO, admin1.getNif());
			admin1.setPassword(UtilidadesDominio.encriptarPasswordSHA1("zzz123"));
			assertEquals(admin1, usuarioGet);
		} catch(Exception e) {
			fail(e.toString());
		}
	}

	/** Pruebas de la operaci�n que elimina usuarios existentes */
	public void testEliminarUsuario() {
		Beneficiario beneficiario;
		Usuario usuario;
		
		try {
			// Intentamos eliminar un usuario con la sesi�n del citador
			servidor.mensajeAuxiliar(sesionCitador.getId(), ICodigosMensajeAuxiliar.ELIMINAR_USUARIO, citador1);
			fail("Se esperaba una excepci�n OperacionIncorrectaException");
		} catch(OperacionIncorrectaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n OperacionIncorrectaException");
		}
		
		try {
			// Intentamos acceder al servidor con un id de sesi�n err�neo
			servidor.mensajeAuxiliar(-12345, ICodigosMensajeAuxiliar.ELIMINAR_USUARIO, citador1);
			fail("Se esperaba una excepci�n SesionInvalidaException");
		} catch(SesionInvalidaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n SesionInvalidaException");
		}
		
		try {
			// Intentamos eliminar un m�dico que a�n no se ha creado
			usuario = new Medico("78256514", "error", "error", "", "", "", "", "", pediatra);
			servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.ELIMINAR_USUARIO, usuario);
			fail("Se esperaba una excepci�n UsuarioInexistenteException");
		} catch(UsuarioInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n UsuarioInexistenteException");
		}
		
		try {
			// Eliminamos un usuario y un m�dico existentes como administrador
			servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.ELIMINAR_USUARIO, medico2);
			servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.ELIMINAR_USUARIO, citador1);
			// Comprobamos que al beneficiario que ten�a asignado el m�dico
			// borrado se le ha asignado otro m�dico disponible
			beneficiario = FPBeneficiario.consultarPorNIF(beneficiario1.getNif());
			assertEquals(medico4, beneficiario.getMedicoAsignado());
			// Comprobamos que se ha avisado a los clientes del borrado del m�dico
			Thread.sleep(100);
			assertTrue(clienteMedico.getUltimaOperacion() == ICodigosOperacionesCliente.ELIMINAR);
			assertEquals(citador1, clienteMedico.getUltimoDato());
			// Comprobamos que se ha intentado cerrar la sesi�n del usuario eliminado
			assertTrue(clienteCitador.isLlamadoCerrarSesionEliminacion());
			sesionCitadorCerrada = true;
		} catch(Exception e) {
			fail(e.toString());
		}

		try {
			// Comprobamos que el usuario borrado ya no exista en el sistema
			servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CONSULTAR_USUARIO, citador1.getNif());
			fail("Se esperaba una excepci�n UsuarioInexistenteException");
		} catch(UsuarioInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n UsuarioInexistenteException");
		}
		
		try {
			// Comprobamos que el m�dico borrado ya no exista en el sistema
			servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CONSULTAR_USUARIO, medico2.getNif());
			fail("Se esperaba una excepci�n UsuarioInexistenteException");
		} catch(UsuarioInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n UsuarioInexistenteException");
		}
		
		try {
			// Comprobamos que al borrar el m�dico el beneficiario que
			// ten�a asignado ha cambiado de m�dico de cabecera
			beneficiario = servidor.getBeneficiario(sesionAdmin.getId(), beneficiario1.getNif());
			assertTrue(!beneficiario.getMedicoAsignado().equals(medico2));
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	/** Pruebas de la operaci�n que devuelve los centros de salud */
	@SuppressWarnings("unchecked")
	public void testConsultarCentros() {
		Vector<CentroSalud> centros;
		
		try {
			// Intentamos acceder al servidor con un id de sesi�n err�neo
			servidor.mensajeAuxiliar(-12345, ICodigosMensajeAuxiliar.CONSULTAR_CENTROS, null);
			fail("Se esperaba una excepci�n SesionInvalidaException");
		} catch(SesionInvalidaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n SesionInvalidaException");
		}
		
		try {
			// Obtenemos la lista de centros de salud
			centros = (Vector<CentroSalud>)servidor.mensajeAuxiliar(sesionCitador.getId(), ICodigosMensajeAuxiliar.CONSULTAR_CENTROS, null);
			assertTrue(centros.size() == 1);
			assertEquals(centros.get(0), centro1);
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	/** Pruebas de las operaciones de la clase Usuario */
	public void testClaseUsuario() {
		Usuario usuario;
		
		try {
			// Comprobamos que los m�todos toString y equals no producen
			// excepci�n si el centro de salud es nulo
			usuario = new Administrador("11223344P", "login", "pass", "ABC", "DEF", "", "", "");
			assertNotNull(usuario.toString());
			assertTrue(usuario.equals(usuario));
		} catch(Exception e) {
			fail(e.toString());
		}
	}

	/** Pruebas con datos nulos */
	public void testDatosNulos() {
		try {
			// Intentamos consultar un usuario con NIF nulo
			servidor.mensajeAuxiliar(sesionCitador.getId(), ICodigosMensajeAuxiliar.CONSULTAR_USUARIO, null);
			fail("Se esperaba una excepci�n NullPointerException");
		} catch(NullPointerException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n NullPointerException");
		}

		try {
			// Intentamos crear un usuario nulo
			servidor.mensajeAuxiliar(sesionCitador.getId(), ICodigosMensajeAuxiliar.CREAR_USUARIO, null);
			fail("Se esperaba una excepci�n NullPointerException");
		} catch(NullPointerException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n NullPointerException");
		}

		try {
			// Intentamos modificar un usuario nulo
			servidor.mensajeAuxiliar(sesionCitador.getId(), ICodigosMensajeAuxiliar.MODIFICAR_USUARIO, null);
			fail("Se esperaba una excepci�n NullPointerException");
		} catch(NullPointerException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n NullPointerException");
		}
		
		try {
			// Intentamos eliminar un usuario nulo
			servidor.mensajeAuxiliar(sesionCitador.getId(), ICodigosMensajeAuxiliar.ELIMINAR_USUARIO, null);
			fail("Se esperaba una excepci�n NullPointerException");
		} catch(NullPointerException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n NullPointerException");
		}
	}
	
}
