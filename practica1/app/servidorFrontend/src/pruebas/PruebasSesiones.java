package pruebas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import comunicaciones.ConexionBDFrontend;
import comunicaciones.GestorConexionesBD;
import junit.framework.TestCase;
import persistencia.AgenteFrontend;
import persistencia.FPCentroSalud;
import persistencia.FPUsuario;
import dominio.conocimiento.Administrador;
import dominio.conocimiento.CentroSalud;
import dominio.conocimiento.ISesion;
import dominio.conocimiento.Medico;
import dominio.conocimiento.Pediatra;
import dominio.conocimiento.RolesUsuarios;
import dominio.control.GestorSesiones;
import excepciones.UsuarioIncorrectoException;

public class PruebasSesiones extends TestCase {
	
	private CentroSalud centro1;
	private Medico medico1;
	private Administrador administrador1;
	private ConexionBDFrontend conexionF = null;
	private Pediatra pediatra;
	
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
			// Ponemos la conexión local con la base de datos
			conexionF = new ConexionBDFrontend();
			GestorConexionesBD.ponerConexion(conexionF);
			//Inicializamos los tipos de medicos
			pediatra = new Pediatra();
			// Creamos objetos de prueba
			centro1 = new CentroSalud("Centro A", "Calle Toledo, 44");
			medico1 = new Medico("12345678", "medPrueba", "abcdef", "Eduardo", "P. C.", pediatra);
			administrador1 = new Administrador("12121212", "admin", "nimda", "Administrador", "Apellidos");
			medico1.setCentroSalud(centro1);
			administrador1.setCentroSalud(centro1);
			FPCentroSalud.insertar(centro1);
			FPUsuario.insertar(medico1);
			FPUsuario.insertar(administrador1);
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	protected void tearDown() {
		// Quitamos la conexión local con la base de datos
		GestorConexionesBD.quitarConexiones();
	}
	
	/** Prueba del escenario normal: se crea la sesion **/
	public void testCrearSesion() {	
		try {
			ISesion s = GestorSesiones.identificar("admin", "nimda");
			// Comprobamos que la sesion no es nula y que el rol es el que corresponde
			assertNotNull(s);
			assertEquals(RolesUsuarios.Administrador.ordinal(), s.getRol());
		} catch(Exception e) {
			fail("No se esperaba ninguna excepcion " + e.getMessage());
		}
	}
	
	/** Prueba de un escenario alternativo: existia una sesion creada **/
	public void testCerraryAbrirSesion(){	
		try {
			ISesion s = GestorSesiones.identificar("medPrueba", "abcdef");
			assertNotNull(s);
			assertEquals(RolesUsuarios.Medico.ordinal(), s.getRol());
			ISesion ns = GestorSesiones.identificar("medPrueba", "abcdef");
			assertNotNull(ns);
			assertNotSame(s,ns);
			//assertEquals(s,ns);
			assertEquals(RolesUsuarios.Medico.ordinal(), ns.getRol());
		} catch(Exception e) {
			fail("No se esperaba ninguna excepcion " + e.getMessage());
		}
	}
	
	/** Prueba de un escenario alternativo: no existe el usuario **/
	public void testUsuarioIncorrecto() {	
		try {
			GestorSesiones.identificar("administrador", "nimda");
			fail("Se esperaba UsusarioIncorrectoException");
		} catch(UsuarioIncorrectoException e) {
		} catch(Exception e) {
			fail("No se esperaba la excepcion " + e.getMessage());
		}
	}
}
