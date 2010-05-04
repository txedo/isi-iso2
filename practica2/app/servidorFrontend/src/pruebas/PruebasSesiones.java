package pruebas;

import java.util.Vector;
import persistencia.FPCentroSalud;
import persistencia.FPUsuario;
import dominio.UtilidadesDominio;
import dominio.conocimiento.Administrador;
import dominio.conocimiento.CentroSalud;
import dominio.conocimiento.ICodigosMensajeAuxiliar;
import dominio.conocimiento.ISesion;
import dominio.conocimiento.Medico;
import dominio.conocimiento.Operaciones;
import dominio.conocimiento.Pediatra;
import dominio.conocimiento.Roles;
import dominio.conocimiento.SesionUsuario;
import dominio.control.GestorSesiones;
import dominio.control.ServidorFrontend;
import excepciones.SesionInvalidaException;
import excepciones.SesionNoIniciadaException;
import excepciones.UsuarioIncorrectoException;

/**
 * Pruebas del Gestor de Sesiones.
 */
public class PruebasSesiones extends PruebasBase {
	
	private ServidorFrontend servidor;
	private CentroSalud centro1;
	private Medico medico1, medico2;
	private Administrador administrador1;
	private Pediatra pediatra;
	private ClientePrueba cliente1;
	
	protected void setUp() {
		try {
			// Preparamos la base de datos
			super.setUp();
			// Obtenemos el servidor frontend, que se utilizará para llamar
			// a los métodos del gestor y así probar las dos clases a la vez
			servidor = ServidorFrontend.getServidor();
			// Creamos objetos de prueba
			pediatra = new Pediatra();
			centro1 = new CentroSalud("Centro A", "Calle 3, nº3");
			medico1 = new Medico("12345678A", "medPrueba", UtilidadesDominio.encriptarPasswordSHA1("abcdef"), "Eduardo", "P. C.", "", "", "", pediatra);
			medico2 = new Medico("99887766A", "otroMedico", UtilidadesDominio.encriptarPasswordSHA1("xxxxx"), "Ramón", "S. J.", "", "", "", pediatra);
			administrador1 = new Administrador("12121212A", "admin", UtilidadesDominio.encriptarPasswordSHA1("admin"), "Administrador", "Apellidos", "", "999888777", "667788888");
			medico1.setCentroSalud(centro1);
			medico2.setCentroSalud(centro1);
			administrador1.setCentroSalud(centro1);
			FPCentroSalud.insertar(centro1);
			FPUsuario.insertar(medico1);
			FPUsuario.insertar(medico2);
			FPUsuario.insertar(administrador1);
			// Creamos dos clientes
			cliente1 = new ClientePrueba();
			cliente1.activar(IDatosPruebas.IP_ESCUCHA_CLIENTES);
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	protected void tearDown() {
		try {
			// Cerramos la base de datos
			super.tearDown();
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	/** Pruebas de la operación que inicia una nueva sesión */
	public void testIdentificar() {
		ISesion sesion = null, sesion2;
		
		try {
			// Intentamos identificarnos en el sistema con un usuario inexistente
			servidor.identificarUsuario("administrador", "admin");
			fail("Se esperaba una excepción UsusarioIncorrectoException");
		} catch(UsuarioIncorrectoException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción UsusarioIncorrectoException");
		}

		try {
			// Intentamos identificarnos en el sistema con una contraseña errónea
			servidor.identificarUsuario("admin", "nniimmddaa");
			fail("Se esperaba una excepción UsusarioIncorrectoException");
		} catch(UsuarioIncorrectoException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción UsusarioIncorrectoException");
		}

		try {
			// Nos identificamos en el sistema con un usuario correcto
			// y comprobamos que la sesión tiene el rol correcto
			sesion = servidor.identificarUsuario("admin", "admin");
			assertNotNull(sesion);
			assertEquals(Roles.Administrador.ordinal(), sesion.getRol());
			// Realizamos una operación con la sesión obtenida
			servidor.modificar(sesion.getId(), medico2);
		} catch(Exception e) {
			fail("No se esperaba ninguna excepcion " + e.getMessage());
		}
		
		try {
			// Nos identificamos en el sistema con un usuario correcto
			sesion = servidor.identificarUsuario("medPrueba", "abcdef");
			assertNotNull(sesion);
			assertEquals(Roles.Médico.ordinal(), sesion.getRol());
			// Esta espera evita que la prueba falle de vez en cuando,
			// probablemente porque no da tiempo a almacenarse la sesión
			Thread.sleep(50);
			// Iniciamos una nueva sesión con el mismo usuario
			sesion2 = servidor.identificarUsuario("medPrueba", "abcdef");
			assertNotNull(sesion);
			assertTrue(sesion.getId() != sesion2.getId());
			// Realizamos una operación con la última sesión
			servidor.mensajeAuxiliar(sesion2.getId(), ICodigosMensajeAuxiliar.CONSULTAR_CENTROS, null);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Intentamos realizar una operación con la sesión inicial
			servidor.mensajeAuxiliar(sesion.getId(), ICodigosMensajeAuxiliar.CONSULTAR_CENTROS, null);
			fail("Se esperaba una excepción SesionInvalidaException");
		} catch(SesionInvalidaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción SesionInvalidaException");
		}
	}
	
	/** Pruebas de las operaciones que registran y liberar clientes en el sistema */
	public void testRegistrarLiberar() {
		ISesion sesion = null;
	
		try {
			// Intentamos registrar un cliente con una sesión no válida
			servidor.registrar(cliente1, -12345);
			fail("Se esperaba una excepción SesionNoIniciadaException");
		} catch(SesionNoIniciadaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción SesionNoIniciadaException");
		}

		try {
			// Iniciamos una sesión como administrador y registramos un cliente
			sesion = servidor.identificarUsuario("admin", "admin");
			servidor.registrar(cliente1, sesion.getId());
			assertNotNull(GestorSesiones.getClientes().get(sesion.getId()));
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Volvemos a iniciar sesión como adminisrador para
			// ver que se cierra la sesión anterior
			sesion = servidor.identificarUsuario("admin", "admin");
			Thread.sleep(100);
			assertTrue(cliente1.isLlamadoCerrarSesion());
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Intentamos liberar un cliente con una sesión no válida
			servidor.liberar(-12345);
			fail("Se esperaba una excepción SesionNoIniciadaException");
		} catch(SesionNoIniciadaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción SesionNoIniciadaException");
		}

		try {
			// Liberamos el cliente registrado del sistema
			servidor.liberar(sesion.getId());
			assertNull(GestorSesiones.getClientes().get(sesion.getId()));
		} catch(Exception e) {
			fail(e.toString());
		}
	}

	/** Pruebas de la operación que consulta las operaciones disponibles */
	@SuppressWarnings("unchecked")
	public void testOperacionesDisponibles() {
		Vector<Operaciones> operaciones;
		ISesion sesion;
		
		try {
			// Intentamos obtener las operaciones disponibles de una sesión no válida
			servidor.mensajeAuxiliar(-12345, ICodigosMensajeAuxiliar.OPERACIONES_DISPONIBLES, null);
			fail("Se esperaba una excepción SesionInvalidaException");
		} catch(SesionInvalidaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción SesionInvalidaException");
		}
		
		try {
			// Iniciamos una sesión como administrador
			sesion = servidor.identificarUsuario("admin", "admin");
			// Obtenemos las operaciones disponibles
			operaciones = (Vector<Operaciones>)servidor.mensajeAuxiliar(sesion.getId(), ICodigosMensajeAuxiliar.OPERACIONES_DISPONIBLES, null);
			assertNotNull(operaciones);
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	/** Pruebas de las operaciones de la clase Sesion */
	@SuppressWarnings("deprecation")
	public void testClaseSesion() {
		SesionUsuario sesion, sesionB;
		
		try {
			// Comprobamos que el método equals funciona bien
			sesion = new SesionUsuario(100, administrador1);
			sesionB = new SesionUsuario(100, administrador1);
			assertTrue(sesion.equals(sesionB));
			sesionB.setUsuario(medico1);
			assertFalse(sesion.equals(sesionB));
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	/** Pruebas con datos nulos */
	public void testDatosNulos() {
		try {
			// Intentamos identificarnos con un nombre de usuario nulo
			servidor.identificarUsuario(null, "");
			fail("Se esperaba una excepción NullPointerException");
		} catch(NullPointerException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción NullPointerException");
		}
		
		try {
			// Intentamos identificarnos con una contraseña nula
			servidor.identificarUsuario("", null);
			fail("Se esperaba una excepción NullPointerException");
		} catch(NullPointerException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción NullPointerException");
		}

		try {
			// Intentamos registrar un cliente nulo
			servidor.registrar(null, 0);
			fail("Se esperaba una excepción NullPointerException");
		} catch(NullPointerException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción NullPointerException");
		}
	}
	
}
