package pruebas;

import java.sql.SQLException;

import persistencia.FPCentroSalud;
import persistencia.FPUsuario;
import dominio.conocimiento.Administrador;
import dominio.conocimiento.CentroSalud;
import dominio.conocimiento.Citador;
import dominio.conocimiento.Encriptacion;
import dominio.conocimiento.ISesion;
import dominio.conocimiento.Sesion;
import dominio.conocimiento.Usuario;
import dominio.control.GestorSesiones;
import dominio.control.GestorUsuarios;
import excepciones.CentroSaludIncorrectoException;
import excepciones.OperacionIncorrectaException;
import excepciones.UsuarioYaExistenteException;

/** Pruebas de usuarios para cubrir aquellos casos que no cubren el resto de pruebas **/

public class PruebasUsuarios extends PruebasBase{

	private CentroSalud centro1;
	private Citador citador1;
	private Administrador admin1;
	private ISesion sesionCitador;
	private ISesion sesionAdmin;
	
	protected void setUp() {
		try {
			// Preparamos la base de datos
			super.setUp();
			// Creamos objetos de prueba
			centro1 = new CentroSalud("Centro Provincial", "Calle Ninguna, s/n");
			citador1 = new Citador("11223344", "citador", Encriptacion.encriptarPasswordSHA1("cit123"), "Fernando", "G. P.");
			admin1 = new Administrador("55667788", "admin", Encriptacion.encriptarPasswordSHA1("nimda"), "María", "L. F.");
			citador1.setCentroSalud(centro1);
			admin1.setCentroSalud(centro1);
			FPCentroSalud.insertar(centro1);
			FPUsuario.insertar(citador1);
			FPUsuario.insertar(admin1);
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
	
	public void testCasosUsuarios () {
		Usuario usu;
		
		try {
			// Intentamos consultar un administrador nulo
			usu = GestorUsuarios.consultarUsuario(sesionAdmin.getId(), null);
			fail("Se esperaba una excepcion NullPointerException");
		} catch(NullPointerException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepcion NullPointerException");
		}
		
		try {
			// Intentamos crear un administrador nulo
			GestorUsuarios.crearUsuario(sesionAdmin.getId(), null);
			fail("Se esperaba una excepcion NullPointerException");
		} catch(NullPointerException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepcion NullPointerException");
		}
				
		try {
			// Intentamos modificar administrador nulo
			GestorUsuarios.modificarUsuario(sesionAdmin.getId(), null);
			fail("Se esperaba una excepcion NullPointerException");
		} catch(NullPointerException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepcion NullPointerException");
		}
		
		try {
			// Intentamos eliminar administrador nulo
			GestorUsuarios.eliminarUsuario(sesionAdmin.getId(), null);
			fail("Se esperaba una excepcion NullPointerException");
		} catch(NullPointerException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepcion NullPointerException");
		}
		
		try {
			// Intentamos crear un administrador con el mismo login que otro
			usu =  new Administrador("55667781", "admin", Encriptacion.encriptarPasswordSHA1("nimda"), "María", "L. F.");
			GestorUsuarios.crearUsuario(sesionAdmin.getId(), usu);
			fail("Se esperaba una excepcion UsuarioYaExistenteException");
		} catch(UsuarioYaExistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepcion UsuarioYaExistenteException");
		}
		
	}
	
}
