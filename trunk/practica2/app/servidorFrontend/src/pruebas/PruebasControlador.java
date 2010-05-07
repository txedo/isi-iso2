package pruebas;

import java.rmi.RemoteException;

import org.uispec4j.Trigger;
import org.uispec4j.Window;
import org.uispec4j.interception.WindowInterceptor;

import comunicaciones.ConexionBDFrontend;
import comunicaciones.ConfiguracionFrontend;
import comunicaciones.GestorConexionesBD;

import dominio.Main;
import dominio.UtilidadesDominio;
import dominio.conocimiento.Administrador;
import dominio.conocimiento.CentroSalud;
import dominio.conocimiento.ISesion;
import dominio.control.ControladorFrontend;
import dominio.control.ServidorFrontend;

/**
 * Pruebas del controlador principal del servidor front-end.
 */
public class PruebasControlador extends PruebasBase {

	private ControladorFrontend controlador;
	
	protected void setUp() {
		CentroSalud centro;
		Administrador admin;
		
		try {
			// Preparamos la base de datos
			super.setUp();
			// Creamos un administrador de prueba
			GestorConexionesBD.quitarConexiones();
			GestorConexionesBD.ponerConexion(new ConexionBDFrontend());
			centro = new CentroSalud("ABC", "ABC");
			admin = new Administrador("31112223A", "admin", UtilidadesDominio.encriptarPasswordSHA1("admin"), "a", "b", "", "", "");
			admin.setCentroSalud(centro);
			GestorConexionesBD.iniciarTransaccion();
			GestorConexionesBD.insertar(centro);
			GestorConexionesBD.insertar(admin);
			GestorConexionesBD.terminarTransaccion();
			// Inicializamos el controlador y la ventana de estado
			controlador = new ControladorFrontend();
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
	
	/** Pruebas del controlador */
	public void testControlador() {
		ConfiguracionFrontend configuracion = null;
		ClientePrueba cliente = null;
		ISesion sesion;
		
		try {
			// Mostramos y ocultamos la ventana principal
			WindowInterceptor.run(new Trigger() {
				public void run() {
					controlador.mostrarVentana();
				}
			});
			assertTrue(controlador.getVentana().isVisible());
			controlador.ocultarVentana();
			assertFalse(controlador.getVentana().isVisible());
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Activamos el servidor varias veces para ver si no hay fallos
			configuracion = new ConfiguracionFrontend(IDatosPruebas.IP_BASEDATOS_PRINCIPAL, IDatosPruebas.PUERTO_BASEDATOS_PRINCIPAL, IDatosPruebas.PUERTO_ESCUCHA);
			controlador.iniciarServidor(configuracion);
			controlador.iniciarServidor(configuracion);
			assertTrue(controlador.isServidorActivo());
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Iniciamos sesión con un cliente
			cliente = new ClientePrueba();
			cliente.activar(IDatosPruebas.IP_ESCUCHA_CLIENTES);
			sesion = ServidorFrontend.getServidor().identificarUsuario("admin", "admin");
			ServidorFrontend.getServidor().registrar(cliente, sesion.getId());
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Intentamos activar el servidor en un puerto diferente
			configuracion = new ConfiguracionFrontend(IDatosPruebas.IP_BASEDATOS_PRINCIPAL, IDatosPruebas.PUERTO_BASEDATOS_PRINCIPAL, IDatosPruebas.PUERTO_ESCUCHA + 500);
			controlador.iniciarServidor(configuracion);
			fail("Se esperaba una RemoteException");
		} catch(RemoteException e) {
		} catch(Exception e) {
			fail("Se esperaba una RemoteException");
		}
		
		try {
			// Desactivamos el servidor dos veces para ver si no hay fallos
			configuracion = new ConfiguracionFrontend(IDatosPruebas.IP_BASEDATOS_PRINCIPAL, IDatosPruebas.PUERTO_BASEDATOS_PRINCIPAL, IDatosPruebas.PUERTO_ESCUCHA);
			controlador.detenerServidor(configuracion);
			controlador.detenerServidor(configuracion);
			assertFalse(controlador.isServidorActivo());
			// Comprobamos que se ha intentado cerrar el único cliente conectado
			Thread.sleep(100);
			assertTrue(cliente.isLlamadoServidorInaccesible());
		} catch(Exception e) {
			fail(e.toString());
		}
		
		/*try {
			// Activamos el servidor con el servidor de respaldo activado
			configuracion = new ConfiguracionFrontend(IDatosPruebas.IP_BASEDATOS_PRINCIPAL, IDatosPruebas.PUERTO_BASEDATOS_PRINCIPAL, IDatosPruebas.IP_SERVIDOR_RESPALDO, IDatosPruebas.PUERTO_SERVIDOR_RESPALDO, IDatosPruebas.PUERTO_ESCUCHA);
			controlador.iniciarServidor(configuracion);
			controlador.iniciarServidor(configuracion);
			assertTrue(controlador.isServidorActivo());
			// Desactivamos el servidor
			controlador.detenerServidor(configuracion);
			controlador.detenerServidor(configuracion);
			assertFalse(controlador.isServidorActivo());
		} catch(Exception e) {
			e.printStackTrace();
			fail(e.toString() + "\nPara ejecutar esta prueba se necesita tener activado el servidor de respaldo.");
		}*/
	}
	
	/** Pruebas de la clase Main */
	public void testMain() {
		Window ventana;
		
		try {
			// Comprobamos que el método Main muestra la ventana principal del servidor
			ventana = WindowInterceptor.run(new Trigger() {
				public void run() {
					Main.main(new String[] {});
				}
			});
			assertEquals(ventana.getTitle(), "Servidor Front-End");
			ventana.dispose();
		} catch(Exception e) {
			fail(e.toString());
		}
	}

}
