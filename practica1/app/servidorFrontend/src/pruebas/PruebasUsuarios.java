package pruebas;

import java.sql.SQLException;
import java.util.Date;
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
	private PeriodoTrabajo periodo21;
	private PeriodoTrabajo periodo31, periodo32;
	private Beneficiario beneficiario1;
	private Direccion direccion1;
	private ISesion sesionCitador;
	private ISesion sesionAdmin;
	private Pediatra pediatra;
	private Especialista especialista;
	private Cabecera cabecera;
	
	protected void setUp() {
		try {
			// Preparamos la base de datos
			super.setUp();
			// Obtenemos el servidor frontend, que se utilizará para llamar
			// a los métodos del gestor y así probar las dos clases a la vez
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
			admin1 = new Administrador("55667788", "admin", Encriptacion.encriptarPasswordSHA1("nimda"), "María", "L. F.");
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
			// Iniciamos dos sesiones con roles de citador y administrador
			sesionCitador = GestorSesiones.identificar(citador1.getLogin(), "cit123");
			sesionAdmin = GestorSesiones.identificar(admin1.getLogin(), "nimda");
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
	
	/** Pruebas de la operación que consulta los datos de un usuario */
	public void testConsultarUsuario() {
		Usuario usuario;
		
		try {
			// Intentamos consultar un usuario con DNI nulo
			servidor.mensajeAuxiliar(sesionCitador.getId(), ICodigosMensajeAuxiliar.CONSULTAR_USUARIO, null);
			fail("Se esperaba una excepción NullPointerException");
		} catch(NullPointerException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción NullPointerException");
		}
		
		try {
			// Intentamos consultar los datos de un usuario sin permisos
			servidor.mensajeAuxiliar(sesionCitador.getId(), ICodigosMensajeAuxiliar.CONSULTAR_USUARIO, medico1.getDni());
			fail("Se esperaba una excepción OperacionIncorrectaException");
		} catch(OperacionIncorrectaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción OperacionIncorrectaException");
		}
		
		try {
			// Intentamos acceder al servidor con un id de sesión erróneo
			servidor.mensajeAuxiliar(-12345, ICodigosMensajeAuxiliar.CONSULTAR_USUARIO, medico1.getDni());
			fail("Se esperaba una excepción SesionInvalidaException");
		} catch(SesionInvalidaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción SesionInvalidaException");
		}
		
		try {
			// Intentamos obtener los datos de un usuario que no existe
			servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CONSULTAR_USUARIO, "99001290W");
			fail("Se esperaba una excepción UsuarioInexistenteException");
		} catch(UsuarioInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción UsuarioInexistenteException");
		}
		
		try {
			// Obtenemos los datos de varios usuarios existentes
			usuario = (Usuario)servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CONSULTAR_USUARIO, medico1.getDni());
			assertEquals(medico1, usuario);
			usuario = (Usuario)servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CONSULTAR_USUARIO, admin1.getDni());
			assertEquals(admin1, usuario);
		} catch(Exception e) {
			fail(e.toString());
		}
	}

	/** Pruebas de la operación que crea nuevos usuarios */
	public void testCrearUsuario() {
		Usuario usuario, usuarioGet;
		
		try {
			// Intentamos crear un usuario nulo
			servidor.mensajeAuxiliar(sesionCitador.getId(), ICodigosMensajeAuxiliar.CREAR_USUARIO, null);
			fail("Se esperaba una excepción NullPointerException");
		} catch(NullPointerException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción NullPointerException");
		}
		
		try {
			// Intentamos crear un usuario con la sesión del citador
			usuario = new Medico("6666666", "medNuevo", "medNuevo", "Juan", "P. C.", especialista);
			servidor.mensajeAuxiliar(sesionCitador.getId(), ICodigosMensajeAuxiliar.CREAR_USUARIO, usuario);
			fail("Se esperaba una excepción OperacionIncorrectaException");
		} catch(OperacionIncorrectaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción OperacionIncorrectaException");
		}
		
		try {
			// Intentamos acceder al servidor con un id de sesión erróneo
			usuario = new Medico("6666666", "medNuevo", "medNuevo", "Juan", "P. C.", especialista);
			servidor.mensajeAuxiliar(-12345, ICodigosMensajeAuxiliar.CREAR_USUARIO, usuario);
			fail("Se esperaba una excepción SesionInvalidaException");
		} catch(SesionInvalidaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción SesionInvalidaException");
		}
		
		try {
			// Intentamos añadir un usuario con un DNI que ya existe en la BD
			usuario = new Administrador(citador1.getDni(), "error", "error", "", "");
			servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CREAR_USUARIO, usuario);
			fail("Se esperaba una excepción UsuarioYaExistenteException");
		} catch(UsuarioYaExistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción UsuarioYaExistenteException");
		}

		try {
			// Intentamos añadir un usuario con el NIF de un beneficiario
			usuario = new Medico(beneficiario1.getNif(), "error", "error", "", "", cabecera);
			servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CREAR_USUARIO, usuario);
			fail("Se esperaba una excepción UsuarioYaExistenteException");
		} catch(UsuarioYaExistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción UsuarioYaExistenteException");
		}

		try {
			// Intentamos añadir un usuario con un login existente
			usuario = new Citador("11884654L", medico3.getLogin(), "error", "", "");
			servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CREAR_USUARIO, usuario);
			fail("Se esperaba una excepción UsuarioYaExistenteException");
		} catch(UsuarioYaExistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción UsuarioYaExistenteException");
		}
		
		try {
			// Creamos un nuevo usuario con la sesión del administrador
			usuario = new Administrador("77777777", "adminnuevo", "nuevoadmin", "Pepe", "A. B.");
			servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CREAR_USUARIO, usuario);
			// Comprobamos que el usuario se ha creado correctamente
			// (al crear el usuario su contraseña se habrá encriptado
			// y se le habrá asignado el único centro que hay)
			usuarioGet = (Usuario)servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CONSULTAR_USUARIO, usuario.getDni());
			usuario.setPassword(Encriptacion.encriptarPasswordSHA1("nuevoadmin"));
			usuario.setCentroSalud(centro1);
			assertEquals(usuario, usuarioGet);
			// Creamos un nuevo médico con la sesión del administrador
			usuario = new Medico("6666666", "medNuevo", "medNuevo", "Juan", "P. C.", especialista);
			servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CREAR_USUARIO, usuario);
			// Comprobamos que el usuario se ha creado correctamente
			usuarioGet = (Usuario)servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CONSULTAR_USUARIO, usuario.getDni());
			usuario.setPassword(Encriptacion.encriptarPasswordSHA1("medNuevo"));
			usuario.setCentroSalud(centro1);
			assertEquals(usuario, usuarioGet);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Intentamos crear un usuario sin haber ningún centro
			AgenteFrontend.getAgente().getConexion().prepareStatement("DELETE FROM centros").executeUpdate();
			usuario = new Medico("34712394", "otromas", "error", "Juan", "P. C.", especialista);
			servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CREAR_USUARIO, usuario);
			fail("Se esperaba una excepción SQLException");
		} catch(SQLException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción SQLException");
		}
	}

	/** Pruebas de la operación que modifica usuarios existentes */
	public void testModificarUsuario() {
		Usuario usuario, usuarioGet;
		
		try {
			// Intentamos modificar un usuario nulo
			servidor.mensajeAuxiliar(sesionCitador.getId(), ICodigosMensajeAuxiliar.MODIFICAR_USUARIO, null);
			fail("Se esperaba una excepción NullPointerException");
		} catch(NullPointerException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción NullPointerException");
		}
		
		try {
			// Intentamos modificar un usuario con la sesión del citador
			servidor.mensajeAuxiliar(sesionCitador.getId(), ICodigosMensajeAuxiliar.MODIFICAR_USUARIO, medico2);
			fail("Se esperaba una excepción OperacionIncorrectaException");
		} catch(OperacionIncorrectaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción OperacionIncorrectaException");
		}
		
		try {
			// Intentamos acceder al servidor con un id de sesión erróneo
			servidor.mensajeAuxiliar(-12345, ICodigosMensajeAuxiliar.MODIFICAR_USUARIO, medico2);
			fail("Se esperaba una excepción SesionInvalidaException");
		} catch(SesionInvalidaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción SesionInvalidaException");
		}
		
		try {
			// Intentamos modificar un usuario inexistente
			usuario = new Citador("91295019", "otro2", "otro", "Anaasa", "R. M.");
			servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.MODIFICAR_USUARIO, usuario);
			fail("Se esperaba una excepción UsuarioInexistenteException");
		} catch(UsuarioInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción UsuarioInexistenteException");
		}
		
		try {
			// Modificamos los datos de un médico existente sin tocar la contraseña
			medico1.setLogin("medCambiado");
			medico1.setApellidos("P. D.");
			medico1.getCalendario().remove(1);
			medico1.setPassword("");
			servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.MODIFICAR_USUARIO, medico1);
			// Comprobamos que el médico se haya actualizado correctamente
			// (la contraseña devuelta debe ser la original, "abcdef", encriptada)
			usuarioGet = (Usuario)servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CONSULTAR_USUARIO, medico1.getDni());
			medico1.setPassword(Encriptacion.encriptarPasswordSHA1("abcdef"));
			assertEquals(medico1, usuarioGet);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Modificamos los datos de un usuario existente cambiando la contraseña
			admin1.setPassword("zzz123");
			servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.MODIFICAR_USUARIO, admin1);
			// Comprobamos que el usuario se haya actualizado correctamente
			// (la contraseña devuelta debe ser la nueva, "zzz123", encriptada)
			usuarioGet = (Usuario)servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CONSULTAR_USUARIO, admin1.getDni());
			admin1.setPassword(Encriptacion.encriptarPasswordSHA1("zzz123"));
			assertEquals(admin1, usuarioGet);
		} catch(Exception e) {
			fail(e.toString());
		}
	}

	/** Pruebas de la operación que elimina usuarios existentes */
	public void testEliminarUsuario() {
		Beneficiario beneficiario;
		Usuario usuario;
		
		try {
			// Intentamos eliminar un usuario nulo
			servidor.mensajeAuxiliar(sesionCitador.getId(), ICodigosMensajeAuxiliar.ELIMINAR_USUARIO, null);
			fail("Se esperaba una excepción NullPointerException");
		} catch(NullPointerException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción NullPointerException");
		}
		
		try {
			// Intentamos eliminar un usuario con la sesión del citador
			servidor.mensajeAuxiliar(sesionCitador.getId(), ICodigosMensajeAuxiliar.ELIMINAR_USUARIO, citador1);
			fail("Se esperaba una excepción OperacionIncorrectaException");
		} catch(OperacionIncorrectaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción OperacionIncorrectaException");
		}
		
		try {
			// Intentamos acceder al servidor con un id de sesión erróneo
			servidor.mensajeAuxiliar(-12345, ICodigosMensajeAuxiliar.ELIMINAR_USUARIO, citador1);
			fail("Se esperaba una excepción SesionInvalidaException");
		} catch(SesionInvalidaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción SesionInvalidaException");
		}
		
		try {
			// Intentamos eliminar un médico que aún no se ha creado
			usuario = new Medico("78256514", "error", "error", "", "", pediatra);
			servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.ELIMINAR_USUARIO, usuario);
			fail("Se esperaba una excepción UsuarioInexistenteException");
		} catch(UsuarioInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción UsuarioInexistenteException");
		}
		
		try {
			// Eliminamos un usuario y un médico existentes como administrador
			servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.ELIMINAR_USUARIO, medico2);
			servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.ELIMINAR_USUARIO, citador1);
		} catch(Exception e) {
			fail(e.toString());
		}

		try {
			// Comprobamos que el usuario borrado ya no exista en el sistema
			servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CONSULTAR_USUARIO, citador1.getDni());
			fail("Se esperaba una excepción UsuarioInexistenteException");
		} catch(UsuarioInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción UsuarioInexistenteException");
		}
		
		try {
			// Comprobamos que el médico borrado ya no exista en el sistema
			servidor.mensajeAuxiliar(sesionAdmin.getId(), ICodigosMensajeAuxiliar.CONSULTAR_USUARIO, medico2.getDni());
			fail("Se esperaba una excepción UsuarioInexistenteException");
		} catch(UsuarioInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción UsuarioInexistenteException");
		}
		
		try {
			// Comprobamos que al borrar el médico el beneficiario que
			// tenía asignado ha cambiado de médico de cabecera
			beneficiario = servidor.getBeneficiario(sesionAdmin.getId(), beneficiario1.getNif());
			assertTrue(!beneficiario.getMedicoAsignado().equals(medico2));
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	/** Pruebas de la operación que devuelve los centros de salud */
	@SuppressWarnings("unchecked")
	public void testConsultarCentros() {
		Vector<CentroSalud> centros;
		
		try {
			// Intentamos acceder al servidor con un id de sesión erróneo
			servidor.mensajeAuxiliar(-12345, ICodigosMensajeAuxiliar.CONSULTAR_CENTROS, null);
			fail("Se esperaba una excepción SesionInvalidaException");
		} catch(SesionInvalidaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción SesionInvalidaException");
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
	
}
