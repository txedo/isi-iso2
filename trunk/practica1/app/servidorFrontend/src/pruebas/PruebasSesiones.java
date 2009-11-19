package pruebas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import comunicaciones.ConexionBDFrontEnd;

import junit.framework.TestCase;

import persistencia.AgenteFrontend;
import persistencia.GestorConexiones;
import dominio.Administrador;
import dominio.CentroSalud;
import dominio.Citador;
import dominio.EntradaLog;
import dominio.GestorSesiones;
import dominio.ISesion;
import dominio.Medico;
import dominio.Roles;
import dominio.Sesion;
import dominio.Usuario;
import excepciones.CentroSaludIncorrectoException;
import excepciones.UsuarioIncorrectoException;

public class PruebasSesiones extends TestCase{
	
	CentroSalud centro1;
	Medico medico1;
	Administrador administrador1;
	ConexionBDFrontEnd conexionF = null;
	
	protected void setUp() {
		Connection bd;
		PreparedStatement sentencia;
		
		try {
			// Borramos la base de datos
			bd = AgenteFrontend.getAgente().getConexion();
			sentencia = bd.prepareStatement("DELETE FROM centros");
			sentencia.executeUpdate();
			sentencia = bd.prepareStatement("DELETE FROM usuarios");
			sentencia.executeUpdate();
			sentencia = bd.prepareStatement("DELETE FROM tipoMedico");
			sentencia.executeUpdate();
			sentencia = bd.prepareStatement("DELETE FROM entradasLog");
			sentencia.executeUpdate();
			// Ponemos la conexión local con la base de datos
			conexionF = new ConexionBDFrontEnd();
			GestorConexiones.ponerConexion(conexionF);
			// Creamos objetos de prueba
			centro1 = new CentroSalud("Centro A", "Calle Toledo, 44");
			medico1 = new Medico("12345678", "medPrueba", "abcdef", "Eduardo", "P. C.", centro1);
			administrador1 = new Administrador("12121212", "admin", "nimda", "Administrador", "Apellidos", centro1);
			centro1.insertar();
			medico1.insertar();
			administrador1.insertar();
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	protected void tearDown() {
		// Quitamos la conexión local con la base de datos
		GestorConexiones.quitarConexion(conexionF);
	}
	
	/** Prueba del escenario normal: se crea la sesion **/
	public void testCrearSesion() {	
		try {
			ISesion s = GestorSesiones.identificar("admin", "nimda");
			// Comprobamos que la sesion no es nula y que el rol es el que corresponde
			assertNotNull(s);
			assertEquals(Roles.Administrador.ordinal(), s.getRol());
		} catch(Exception e) {
			fail("No se esperaba ninguna excepcion " + e.getMessage());
		}
	}
	
	/** Prueba de un escenario alternativo: existia una sesion creada **/
	public void testCerraryAbrirSesion(){	
		try {
			ISesion s = GestorSesiones.identificar("medPrueba", "abcdef");
			assertNotNull(s);
			assertEquals(Roles.Medico.ordinal(), s.getRol());
			ISesion ns = GestorSesiones.identificar("medPrueba", "abcdef");
			assertNotNull(ns);
			assertNotSame(s,ns);
			//assertEquals(s,ns);
			assertEquals(Roles.Medico.ordinal(), ns.getRol());
		} catch(Exception e) {
			fail("No se esperaba ninguna excepcion " + e.getMessage());
		}
	}
	
	/** Prueba de un escenario alternativo: no existe el usuario **/
	public void testUsuarioIncorrecto() {	
		try {
			ISesion s = GestorSesiones.identificar("administrador", "nimda");
			fail("Se esperaba UsusarioIncorrectoException");
		} catch(UsuarioIncorrectoException e) {
		} catch(Exception e) {
			fail("No se esperaba la excepcion " + e.getMessage());
		}
	}
}
		