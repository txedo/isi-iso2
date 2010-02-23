package pruebas;

import persistencia.FPCentroSalud;
import persistencia.FPUsuario;
import dominio.conocimiento.Administrador;
import dominio.conocimiento.CentroSalud;
import dominio.conocimiento.Encriptacion;
import dominio.conocimiento.ISesion;
import dominio.conocimiento.Medico;
import dominio.conocimiento.Pediatra;
import dominio.conocimiento.RolesUsuario;
import dominio.control.GestorSesiones;
import excepciones.UsuarioIncorrectoException;

public class PruebasSesiones extends PruebasBase {
	
	private CentroSalud centro1;
	private Medico medico1;
	private Administrador administrador1;
	private Pediatra pediatra;
	
	protected void setUp() {
		try {
			// Preparamos la base de datos
			super.setUp();
			//Inicializamos los tipos de medicos
			pediatra = new Pediatra();
			// Creamos objetos de prueba
			centro1 = new CentroSalud("Centro A", "Calle 3, nº3");
			medico1 = new Medico("12345678A", "medPrueba", Encriptacion.encriptarPasswordSHA1("abcdef"), "Eduardo", "P. C.", "", "", "", pediatra);
			administrador1 = new Administrador("12121212A", "admin", Encriptacion.encriptarPasswordSHA1("nimda"), "Administrador", "Apellidos", "", "999888777", "667788888");
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
		try {
			// Cerramos la base de datos
			super.tearDown();
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	/** Prueba del escenario normal: se crea la sesion **/
	public void testCrearSesion() {	
		try {
			ISesion s = GestorSesiones.identificar("admin", "nimda");
			// Comprobamos que la sesion no es nula y que el rol es el que corresponde
			assertNotNull(s);
			assertEquals(RolesUsuario.Administrador.ordinal(), s.getRol());
		} catch(Exception e) {
			fail("No se esperaba ninguna excepcion " + e.getMessage());
		}
	}
	
	/** Prueba de un escenario alternativo: existia una sesion creada **/
	public void testCerraryAbrirSesion(){	
		try {
			ISesion s = GestorSesiones.identificar("medPrueba", "abcdef");
			assertNotNull(s);
			assertEquals(RolesUsuario.Medico.ordinal(), s.getRol());
			ISesion ns = GestorSesiones.identificar("medPrueba", "abcdef");
			assertNotNull(ns);
			assertNotSame(s,ns);
			//assertEquals(s,ns);
			assertEquals(RolesUsuario.Medico.ordinal(), ns.getRol());
		} catch(Exception e) {
			fail(e.toString());
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
