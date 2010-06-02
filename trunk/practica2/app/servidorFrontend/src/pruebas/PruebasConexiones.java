package pruebas;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.uispec4j.UISpecTestCase;

import persistencia.ConsultaHibernate;
import presentacion.JFServidorFrontend;

import comunicaciones.ConexionBDFrontend;
import comunicaciones.ConexionLogBD;
import comunicaciones.ConexionLogVentana;
import comunicaciones.GestorConexionesBD;
import comunicaciones.GestorConexionesLog;

import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.CentroSalud;
import dominio.conocimiento.Direccion;
import dominio.conocimiento.EntradaLog;
import dominio.conocimiento.ITiposMensajeLog;

/**
 * Pruebas de los Gestores de conexiones de base de datos y de estado del
 * servidor.
 */
public class PruebasConexiones extends UISpecTestCase {

	private ConexionBDFrontend conexionBD;
	private ConexionLogBD conexionLogBD;
	private ConexionLogVentana conexionLogVentana;
	private JFServidorFrontend ventana;
	//private ProxyServidorRespaldo conexionRespaldo;
	
	protected void setUp() {
		try {
			super.setUp();
			// Inicializamos las conexiones con las bases de datos
			// y las ventanas de estado de los servidores
			conexionBD = new ConexionBDFrontend();
			conexionLogBD = new ConexionLogBD();
			conexionLogVentana = new ConexionLogVentana();
			ventana = new JFServidorFrontend(null);
			conexionLogVentana.ponerVentana(ventana);
			/*conexionRespaldo = new ProxyServidorRespaldo();
			conexionRespaldo.conectar(IDatosPruebas.IP_SERVIDOR_RESPALDO, IDatosPruebas.PUERTO_SERVIDOR_RESPALDO);*/
		} catch(Exception e) {
			fail(e.toString() + "\nPara ejecutar esta prueba se necesita tener activado el servidor de respaldo.");
		}
	}
	
	protected void tearDown() {
		// No es necesario ningún código de finalización
	}
	
	// No funciona la conexión con la BD secundaria mediante Hibernate
	/** Pruebas de las conexiones con las bases de datos */
	@SuppressWarnings("deprecation")
	public void testConexionBD() {
		Beneficiario beneficiario, beneficiarioGet;
		//Usuario usuario, usuarioGet;
		//CentroSalud centro;
		
		try {
			// Intentamos ejecutar un comando sin ninguna base de datos configurada
			GestorConexionesBD.quitarConexiones();
			GestorConexionesBD.consultar(new ConsultaHibernate("FROM Usuario"));
			fail("Se esperaba una excepción SQLException");
		} catch(SQLException e) {
			assertEquals("La lista de conexiones está vacía.", e.getMessage());
		} catch(Exception e) {
			fail("Se esperaba una excepción SQLException");
		}

		try {
			// Intentamos insertar un objeto sin ninguna base de datos configurada
			GestorConexionesBD.iniciarTransaccion();
			GestorConexionesBD.insertar(new CentroSalud("ABC", "DEF"));
			GestorConexionesBD.terminarTransaccion();
			fail("Se esperaba una excepción SQLException");
		} catch(SQLException e) {
			assertEquals("La lista de conexiones está vacía.", e.getMessage());
		} catch(Exception e) {
			fail("Se esperaba una excepción SQLException");
		}

		try {
			// Configuramos y borramos la base de datos principal
			GestorConexionesBD.quitarConexiones();
			GestorConexionesBD.ponerConexion(conexionBD);
			borrarBaseDatos();
			// Ejecutamos varias operaciones sobre la base de datos
			beneficiario = new Beneficiario("11223344O", "112233009988", "A", "B", new Date(1980 - 1900, 3, 6), new Direccion("A", "3", "", "", "B", "C", 10000), "", "", "");
			GestorConexionesBD.iniciarTransaccion();
			GestorConexionesBD.insertar(beneficiario);
			GestorConexionesBD.terminarTransaccion();
			beneficiario.setApellidos("Nuevo");
			GestorConexionesBD.iniciarTransaccion();
			GestorConexionesBD.actualizar(beneficiario);
			GestorConexionesBD.terminarTransaccion();
			beneficiarioGet = (Beneficiario)GestorConexionesBD.consultar(new ConsultaHibernate("FROM Beneficiario WHERE nss='112233009988'")).get(0);
			assertEquals(beneficiarioGet.getApellidos(), "Nuevo");
			GestorConexionesBD.borrarCache(beneficiario);
			GestorConexionesBD.borrarCache(beneficiarioGet);
		} catch(Exception e) {
			fail(e.toString());
		}

		/*try {
			// Configuramos y borramos la base de datos secundaria
			GestorConexionesBD.quitarConexiones();
			GestorConexionesBD.ponerConexion(conexionRespaldo);
			borrarBaseDatos();
			// Ejecutamos varias sentencias SQL para comprobar que
			// se tienen permisos para modificar la BD secundaria
			beneficiario = new Beneficiario("11223344O", "112233009988", "A", "B", new Date(1980 - 1900, 3, 6), new Direccion("A", "3", "", "", "B", "C", 10000), "", "", "");
			GestorConexionesBD.iniciarTransaccion();
			GestorConexionesBD.insertar(beneficiario);
			GestorConexionesBD.terminarTransaccion();
			beneficiario.setApellidos("Nuevo");
			GestorConexionesBD.iniciarTransaccion();
			GestorConexionesBD.actualizar(beneficiario);
			GestorConexionesBD.terminarTransaccion();
			beneficiarioGet = (Beneficiario)GestorConexionesBD.consultar(new ConsultaHibernate("FROM Beneficiario WHERE nss='112233009988'")).get(0);
			assertEquals(beneficiarioGet.getApellidos(), "Nuevo");
			GestorConexionesBD.borrarCache(beneficiario);
			GestorConexionesBD.borrarCache(beneficiarioGet);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Configuramos y borramos las dos bases de datos
			GestorConexionesBD.quitarConexiones();
			GestorConexionesBD.ponerConexion(conexionBD);
			GestorConexionesBD.ponerConexion(conexionRespaldo);
			borrarBaseDatos();
			// Ejecutamos varias sentencias SQL sobre las dos bases de datos
			centro = new CentroSalud("AAA", "BBB");
			usuario = new Administrador("88776655O", "login", "pass", "J", "K", "", "", "");
			usuario.setCentroSalud(centro);
			GestorConexionesBD.iniciarTransaccion();
			GestorConexionesBD.insertar(usuario);
			GestorConexionesBD.terminarTransaccion();
			usuario.setPassword("nuevapassword");
			GestorConexionesBD.iniciarTransaccion();
			GestorConexionesBD.actualizar(usuario);
			GestorConexionesBD.terminarTransaccion();
			GestorConexionesBD.borrarCache(usuario);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Vemos si los cambios se han aplicado en la BD principal
			GestorConexionesBD.quitarConexiones();
			GestorConexionesBD.ponerConexion(conexionBD);
			usuarioGet = (Usuario)GestorConexionesBD.consultar(new ConsultaHibernate("FROM Usuario WHERE nif='88776655O'")).get(0);
			assertTrue(usuarioGet.getPassword().equals("nuevapassword"));
			assertTrue(usuarioGet.getRol() == Roles.Administrador);
			assertTrue(usuarioGet.getApellidos().equals("K"));
			GestorConexionesBD.borrarCache(usuarioGet);
		} catch(Exception e) {
			fail(e.toString());
		}

		try {
			// Vemos si los cambios se han aplicado en la BD secundaria
			// (intentamos repetir una clave primaria)
			GestorConexionesBD.quitarConexiones();
			GestorConexionesBD.ponerConexion(conexionRespaldo);
			usuario = new Administrador("88776655O", "aaa", "bbb", "J", "K", "", "", "");
			GestorConexionesBD.iniciarTransaccion();
			GestorConexionesBD.insertar(usuario);
			GestorConexionesBD.terminarTransaccion();
			fail("Se esperaba una excepción SQLException");
		} catch(SQLException e) {
			assertEquals("Error en el acceso a la base de datos secundaria.", e.getMessage());
		} catch(Exception e) {
			fail("Se esperaba una excepción SQLException");
		}
		
		try {
			// Desincronizamos las dos bases de datos
			GestorConexionesBD.quitarConexiones();
			GestorConexionesBD.ponerConexion(conexionRespaldo);
			comando = new ComandoSQLSentencia("INSERT INTO usuarios (nif, login, password, rol, nombre, apellidos, idCentro) VALUES (\"44004400Z\", \"otrologin\", \"otrapass\", 0, \"C\", \"D\", null)");
			GestorConexionesBD.ejecutar(comando);
			comando = new ComandoSQLSentencia("INSERT INTO beneficiarios (nif, nss, nombre, apellidos, fechaNacimiento, nifMedico, idCentro) VALUES (\"77665544L\", \"998877776655\", \"A\", \"B\", \"1980-01-01\", null, null)");
			GestorConexionesBD.ejecutar(comando);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Intentamos ejecutar una sentencia que funciona bien sobre
			// la primera base de datos (principal) pero falla en la segunda
			// (secundaria), porque el login del usuario está repetido
			GestorConexionesBD.quitarConexiones();
			GestorConexionesBD.ponerConexion(conexionBD);
			GestorConexionesBD.ponerConexion(conexionRespaldo);
			comando = new ComandoSQLSentencia("INSERT INTO usuarios (nif, login, password, rol, nombre, apellidos, idCentro) VALUES (\"33333333P\", \"otrologin\", \"otrapass\", 0, \"C\", \"D\", null)");
			GestorConexionesBD.ejecutar(comando);
			fail("Se esperaba una excepción SQLException");
		} catch(SQLException e) {
			assertEquals("Error en el acceso a la base de datos secundaria.", e.getMessage());
		} catch(Exception e) {
			fail("Se esperaba una excepción SQLException");
		}
		
		try {
			// Comprobamos que la sentencia no se ha llegado a ejecutar
			// en la primera base de datos (principal)
			GestorConexionesBD.quitarConexiones();
			GestorConexionesBD.ponerConexion(conexionBD);
			comando = new ComandoSQLSentencia("SELECT * FROM usuarios WHERE nif = \"33333333P\"");
			resultados = GestorConexionesBD.consultar(comando);
			assertFalse(resultados.next());
		} catch(Exception e) {
			fail(e.toString());
		}

		try {
			// Intentamos ejecutar una sentencia que funciona bien sobre
			// la primera base de datos (secundaria) pero falla en la segunda
			// (principal), porque no existe el beneficiario 77665544L
			GestorConexionesBD.quitarConexiones();
			GestorConexionesBD.ponerConexion(conexionRespaldo);
			GestorConexionesBD.ponerConexion(conexionBD);
			comando = new ComandoSQLSentencia("INSERT INTO direcciones (nifBeneficiario, domicilio, numero, piso, puerta, ciudad, provincia, cp) VALUES (\"77665544L\", \"A\", \"5\", \"\", \"\", \"Ciudad\", \"Provincia\", 13000)");
			GestorConexionesBD.ejecutar(comando);
			fail("Se esperaba una excepción SQLException");
		} catch(SQLException e) {
			assertEquals("Error en el acceso a la base de datos principal.", e.getMessage());
		} catch(Exception e) {
			fail("Se esperaba una excepción SQLException");
		}
		
		try {
			// Comprobamos que la sentencia no se ha llegado a ejecutar
			// en la primera base de datos (secundaria); como no se pueden
			// hacer consultas probamos a insertar la dirección, si el comando
			// anterior se hubiera ejecutado esto no se podría hacer
			GestorConexionesBD.quitarConexiones();
			GestorConexionesBD.ponerConexion(conexionRespaldo);
			comando = new ComandoSQLSentencia("INSERT INTO direcciones (nifBeneficiario, domicilio, numero, piso, puerta, ciudad, provincia, cp) VALUES (\"77665544L\", \"A\", \"5\", \"\", \"\", \"Ciudad\", \"Provincia\", 13000)");
			GestorConexionesBD.ejecutar(comando);
		} catch(Exception e) {
			fail(e.toString());
		}*/
	}
	
	/** Pruebas de las conexiones para actualizar el estado del servidor */
	public void testConexionLog() {
		List<?> datos;
		String[] lineas;
		
		try {
			// Configuramos el almacenamiento de los mensajes en la base de datos
			GestorConexionesLog.quitarConexiones();
			GestorConexionesLog.ponerConexion(conexionLogBD);
			GestorConexionesBD.quitarConexiones();
			GestorConexionesBD.ponerConexion(conexionBD);
			borrarBaseDatos();
			// Comprobamos que ahora no hay ningún mensaje en la BD
			assertTrue(GestorConexionesBD.consultar(new ConsultaHibernate("FROM EntradaLog")).size() == 0);
			// Generamos nuevos mensajes y cambiamos los clientes a la escucha
			GestorConexionesLog.ponerMensaje(null, ITiposMensajeLog.TIPO_INFO, "Mensaje de prueba");
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_UPDATE, "Otro mensaje de prueba");
			GestorConexionesLog.actualizarClientesEscuchando(3);
			// Comprobamos que ahora hay dos mensajes en la BD
			datos = GestorConexionesBD.consultar(new ConsultaHibernate("FROM EntradaLog"));
			assertTrue(datos.size() == 2);
			assertTrue(((EntradaLog)datos.get(0)).getMensaje().equals("Mensaje de prueba") || ((EntradaLog)datos.get(0)).getMensaje().equals("Otro mensaje de prueba"));
			assertTrue(((EntradaLog)datos.get(1)).getMensaje().equals("Mensaje de prueba") || ((EntradaLog)datos.get(1)).getMensaje().equals("Otro mensaje de prueba"));
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Configuramos la visualización de los mensajes en el servidor front-end
			GestorConexionesLog.quitarConexiones();
			GestorConexionesLog.ponerConexion(conexionLogVentana);
			// Obtenemos los mensajes actuales de la ventana
			assertEquals(ventana.getMensajes(), "");
			assertTrue(ventana.getClientesEscuchando() == 0);
			// Generamos nuevos mensajes y cambiamos los clientes a la escucha
			GestorConexionesLog.ponerMensaje("prueba", ITiposMensajeLog.TIPO_INFO, "Mensaje de prueba");
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_UPDATE, "Otro mensaje de prueba");
			GestorConexionesLog.actualizarClientesEscuchando(3);
			// Comprobamos que la ventana se ha actualizado
			lineas = ventana.getMensajes().split("\n");
			assertTrue(lineas[0].endsWith("Mensaje de prueba"));
			assertTrue(lineas[1].endsWith("Otro mensaje de prueba"));
			assertTrue(ventana.getClientesEscuchando() == 3);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		/*try {
			// Configuramos la visualización de los mensajes en el servidor de respaldo
			GestorConexionesLog.quitarConexiones();
			GestorConexionesLog.ponerConexion(conexionRespaldo);
			// Generamos nuevos mensajes y cambiamos los clientes a la escucha
			GestorConexionesLog.ponerMensaje("prueba", ITiposMensajeLog.TIPO_INFO, "Mensaje de prueba");
			GestorConexionesLog.ponerMensaje(ITiposMensajeLog.TIPO_UPDATE, "Otro mensaje de prueba");
			GestorConexionesLog.actualizarClientesEscuchando(3);
			// No hay ninguna forma de recuperar los mensajes de la ventana del
			// servidor de respaldo, suponemos que la conexión funciona bien
			// porque en las PruebasRemotoServidor del servidor de respaldo
			// se prueba la clase remota ServidorRespaldo
		} catch(Exception e) {
			fail(e.toString());
		}*/
	}
	
	private void borrarBaseDatos() {
		ConsultaHibernate consulta;
		List<?> datos;
		boolean iniciado;
		
		try {
			iniciado = false;
			for(String clase : new String[] { "Sustitucion", "Volante", "Cita", "Beneficiario", "Usuario", "CentroSalud", "EntradaLog" }) {
				consulta = new ConsultaHibernate("FROM " + clase);
				datos = GestorConexionesBD.consultar(consulta);
				for(Object objeto : datos) {
					if(!iniciado) {
						GestorConexionesBD.iniciarTransaccion();
						iniciado = true;
					}
					GestorConexionesBD.borrarCache(objeto);
					GestorConexionesBD.eliminar(objeto);
				}
			}
			if(iniciado) {
				GestorConexionesBD.terminarTransaccion();
			}
		} catch(Exception e) {
			fail(e.toString());
		}
	}

}
