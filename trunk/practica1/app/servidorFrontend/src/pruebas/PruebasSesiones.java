package pruebas;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

import comunicaciones.ICliente;
import persistencia.FPCentroSalud;
import persistencia.FPUsuario;
import dominio.conocimiento.Administrador;
import dominio.conocimiento.CentroSalud;
import dominio.conocimiento.Encriptacion;
import dominio.conocimiento.ICodigosMensajeAuxiliar;
import dominio.conocimiento.ISesion;
import dominio.conocimiento.Medico;
import dominio.conocimiento.Operaciones;
import dominio.conocimiento.Pediatra;
import dominio.conocimiento.RolesUsuario;
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
			medico1 = new Medico("12345678A", "medPrueba", Encriptacion.encriptarPasswordSHA1("abcdef"), "Eduardo", "P. C.", "", "", "", pediatra);
			medico2 = new Medico("99887766A", "otroMedico", Encriptacion.encriptarPasswordSHA1("xxxxx"), "Ramón", "S. J.", "", "", "", pediatra);
			administrador1 = new Administrador("12121212A", "admin", Encriptacion.encriptarPasswordSHA1("admin"), "Administrador", "Apellidos", "", "999888777", "667788888");
			medico1.setCentroSalud(centro1);
			medico2.setCentroSalud(centro1);
			administrador1.setCentroSalud(centro1);
			FPCentroSalud.insertar(centro1);
			FPUsuario.insertar(medico1);
			FPUsuario.insertar(medico2);
			FPUsuario.insertar(administrador1);
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
	
	/** Pruebas de la operación que inicia una nueva sesión **/
	public void testIdentificar() {
		ISesion sesion = null, sesion2;
		
		try {
			// Intentamos identificarnos con un nombre de usuario nulo
			servidor.identificar(null, "");
			fail("Se esperaba una excepción NullPointerException");
		} catch(NullPointerException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción NullPointerException");
		}
		
		try {
			// Intentamos identificarnos con una contraseña nula
			servidor.identificar("", null);
			fail("Se esperaba una excepción NullPointerException");
		} catch(NullPointerException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción NullPointerException");
		}
		
		try {
			// Intentamos identificarnos en el sistema con un usuario inexistente
			servidor.identificar("administrador", "admin");
			fail("Se esperaba una excepción UsusarioIncorrectoException");
		} catch(UsuarioIncorrectoException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción UsusarioIncorrectoException");
		}

		try {
			// Intentamos identificarnos en el sistema con una contraseña errónea
			servidor.identificar("admin", "nniimmddaa");
			fail("Se esperaba una excepción UsusarioIncorrectoException");
		} catch(UsuarioIncorrectoException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción UsusarioIncorrectoException");
		}

		try {
			// Nos identificamos en el sistema con un usuario correcto
			// y comprobamos que la sesión tiene el rol correcto
			sesion = servidor.identificar("admin", "admin");
			assertNotNull(sesion);
			assertEquals(RolesUsuario.Administrador.ordinal(), sesion.getRol());
			// Realizamos una operación con la sesión obtenida
			servidor.modificar(sesion.getId(), medico2);
		} catch(Exception e) {
			fail("No se esperaba ninguna excepcion " + e.getMessage());
		}
		
		try {
			// Nos identificamos en el sistema con un usuario correcto
			sesion = servidor.identificar("medPrueba", "abcdef");
			assertNotNull(sesion);
			assertEquals(RolesUsuario.Medico.ordinal(), sesion.getRol());
			// Esta espera evita que la prueba falle de vez en cuando,
			// probablemente porque no da tiempo a almacenarse la sesión
			Thread.sleep(50);
			// Iniciamos una nueva sesión con el mismo usuario
			sesion2 = servidor.identificar("medPrueba", "abcdef");
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
	
	/** Pruebas de las operaciones que registran y liberar clientes en el sistema **/
	public void testRegistrarLiberar() {
		ClienteDummy cliente = null;
		ISesion sesion = null;
		
		try {
			// Inicializamos un cliente
			cliente = new ClienteDummy();
		} catch(Exception e) {
			fail(e.toString());
		}
	
		try {
			// Intentamos registrar un cliente nulo
			servidor.registrar(null, 0);
			fail("Se esperaba una excepción NullPointerException");
		} catch(NullPointerException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción NullPointerException");
		}

		try {
			// Intentamos registrar un cliente con una sesión no válida
			servidor.registrar(cliente, -12345);
			fail("Se esperaba una excepción SesionNoIniciadaException");
		} catch(SesionNoIniciadaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción SesionNoIniciadaException");
		}

		try {
			// Iniciamos una sesión como administrador
			sesion = servidor.identificar("admin", "admin");
			// Registramos un nuevo cliente en el sistema
			cliente.activar("127.0.0.1");
			servidor.registrar(cliente, sesion.getId());
			assertNotNull(GestorSesiones.getClientes().get(sesion.getId()));
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
			cliente.desactivar("127.0.0.1");
			assertNull(GestorSesiones.getClientes().get(sesion.getId()));
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	/*			// Comprobamos que al realizar una operación el cliente es notificado
	assertFalse(cliente.llamado());
	servidor.modificar(sesion.getId(), medico1);
	assertTrue(cliente.llamado());*/
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
			sesion = servidor.identificar("admin", "admin");
			// Obtenemos las operaciones disponibles
			operaciones = (Vector<Operaciones>)servidor.mensajeAuxiliar(sesion.getId(), ICodigosMensajeAuxiliar.OPERACIONES_DISPONIBLES, null);
			assertNotNull(operaciones);
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	// -----------------------------------------------------------------------
	
	private class ClienteDummy extends UnicastRemoteObject implements ICliente {

		private static final long serialVersionUID = -6461417903923553869L;

		private final int PUERTO_INICIAL_CLIENTE = 3995;

		private boolean registro;
		private int puerto;
		private boolean llamado;
		
		public ClienteDummy() throws RemoteException {
			super();
			registro = false;
		}
		
	    public void activar(String ip) throws RemoteException, MalformedURLException {
			boolean puertoUsado;

			// Si el objeto ya estaba exportado, controlamos las
			// excepciones y no las lanzamos hacia arriba
	    	try {
	    		if(!registro) {
	    			// Buscamos un puerto que no esté ya en uso en el equipo
	    			puertoUsado = true;
	    			puerto = PUERTO_INICIAL_CLIENTE;
	    			do {
	    				try {
	    					LocateRegistry.createRegistry(puerto);
	    					puertoUsado = false;
	    				} catch(ExportException e) {
	    					puerto++;
	    				}
	    			} while(puertoUsado);
	    			registro = true;
	    		}
	    		exportObject(this, puerto);
	        } catch(ExportException ex) {
	        	if(!ex.getMessage().toLowerCase().equals("object already exported")) {
	        		throw ex;
	        	}
	        }
	        try {
	            Naming.bind("rmi://" + ip + ":" + String.valueOf(puerto) + "/" + NOMBRE_CLIENTE, this);
	        } catch(AlreadyBoundException ex) {
	            Naming.rebind("rmi://" + ip + ":" + String.valueOf(puerto) + "/" + NOMBRE_CLIENTE, this);
	        }
	    }
	    
	    public void desactivar(String ip) throws RemoteException, MalformedURLException, NotBoundException {
			// Si el objeto no estaba exportado, controlamos las
			// excepciones y no las lanzamos hacia arriba
	    	try {
	    		unexportObject(this, false);
	    	} catch(NoSuchObjectException ex) {
	    	}
	    	try {
	    		Naming.unbind("rmi://" + ip + ":" + String.valueOf(puerto) + "/" + NOMBRE_CLIENTE);
	    	} catch(NotBoundException ex) {
	    	}
	    }

	    public String getDireccionIP() throws RemoteException {
			return "127.0.0.1";
		}

		public int getPuerto() throws RemoteException {
			return puerto;
		}

		public void actualizarVentanas(int operacion, Object dato) throws RemoteException {
			llamado = true;
		}
		
		public void servidorInaccesible() throws RemoteException {
		}
		
		public void cerrarSesion() throws RemoteException {
		}
		
		public boolean llamado() {
			return llamado;
		}

		public void cerrarSesionEliminacion() throws RemoteException {
			
		}
	    
	}
	
}
