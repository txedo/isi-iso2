package pruebas;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import org.uispec4j.Trigger;
import org.uispec4j.UISpecTestCase;
import org.uispec4j.Window;
import org.uispec4j.interception.WindowInterceptor;
import persistencia.AgenteFrontend;
import comunicaciones.ConexionBDFrontend;
import comunicaciones.ConfiguracionFrontend;
import comunicaciones.GestorConexionesBD;
import dominio.Main;
import dominio.UtilidadesDominio;
import dominio.conocimiento.ISesion;
import dominio.control.ControladorFrontend;
import dominio.control.ServidorFrontend;

/**
 * Pruebas del controlador principal del servidor front-end.
 */
public class PruebasControlador extends UISpecTestCase {

	private ControladorFrontend controlador;
	
	public void setUp() {
		Connection bd;
		PreparedStatement sentencia;
		AgenteFrontend agente;
		
		try {
			// Creamos un administrador de prueba
			agente = AgenteFrontend.getAgente();
			agente.setIP(IDatosPruebas.IP_BASEDATOS_PRINCIPAL);
			agente.setPuerto(IDatosPruebas.PUERTO_BASEDATOS_PRINCIPAL);
			agente.abrir();
			bd = agente.getConexion();
			sentencia = bd.prepareStatement("DELETE FROM centros");
			sentencia.executeUpdate();
			sentencia = bd.prepareStatement("DELETE FROM usuarios");
			sentencia.executeUpdate();
			sentencia = bd.prepareStatement("INSERT INTO centros (id, nombre, direccion) VALUES (1, \"ABC\", \"ABC\")");
			sentencia.executeUpdate();
			sentencia = bd.prepareStatement("INSERT INTO usuarios (nif, login, password, rol, nombre, apellidos, idCentro) VALUES (\"88776655O\", \"admin\", \"" + UtilidadesDominio.encriptarPasswordSHA1("admin") + "\", 0, \"C\", \"D\", 1)");
			sentencia.executeUpdate();
			bd.commit();
			GestorConexionesBD.ponerConexion(new ConexionBDFrontend());
			// Inicializamos el controlador y la ventana de estado
			controlador = new ControladorFrontend();
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	public void tearDown() {
		try {
			// Cerramos las bases de datos
			GestorConexionesBD.cerrarConexiones();
			GestorConexionesBD.quitarConexiones();
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
			sesion = ServidorFrontend.getServidor().identificar("admin", "admin");
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
		
		try {
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
			fail(e.toString() + "\nPara ejecutar esta prueba se necesita tener activado el servidor de respaldo.");
		}
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
