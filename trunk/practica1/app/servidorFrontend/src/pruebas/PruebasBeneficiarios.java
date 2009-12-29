package pruebas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import persistencia.AgenteFrontend;
import persistencia.FPBeneficiario;
import persistencia.FPCentroSalud;
import persistencia.FPTipoMedico;
import persistencia.FPUsuario;
import comunicaciones.ConexionBDFrontend;
import comunicaciones.GestorConexionesBD;
import dominio.Administrador;
import dominio.Beneficiario;
import dominio.CentroSalud;
import dominio.Citador;
import dominio.Especialista;
import dominio.GestorBeneficiarios;
import dominio.GestorSesiones;
import dominio.ISesion;
import dominio.Medico;
import dominio.Cabecera;
import dominio.Pediatra;
import dominio.Sesion;
import dominio.TiposMedicos;
import excepciones.BeneficiarioInexistenteException;
import excepciones.BeneficiarioYaExistenteException;
import excepciones.OperacionIncorrectaException;
import excepciones.SesionInvalidaException;
import junit.framework.TestCase;

public class PruebasBeneficiarios extends TestCase {

	private CentroSalud centro1;
	private Medico medico1, medico2;
	private Beneficiario bene1, bene2;
	private Citador citador1;
	private Administrador admin1;
	private ConexionBDFrontend conexionF;
	private ISesion sesionCitador;
	private ISesion sesionAdmin;
	private ISesion sesionMedico;
	private Pediatra pediatra;
	private Especialista especialista;
	private Cabecera cabecera;
	
	protected void setUp() {
		Connection bd;
		PreparedStatement sentencia;
		
		try {
			// Borramos la base de datos
			bd = AgenteFrontend.getAgente().getConexion();
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
			sentencia = bd.prepareStatement("DELETE FROM volantes");
			sentencia.executeUpdate();
			// Ponemos la conexión local con la base de datos
			conexionF = new ConexionBDFrontend();
			GestorConexionesBD.ponerConexion(conexionF);
			//Inicializamos los tipos de medicos
			pediatra = new Pediatra();
			especialista = new Especialista();
			cabecera = new Cabecera();
			// Creamos objetos de prueba
			centro1 = new CentroSalud("Centro A", "Calle Toledo, 44");
			medico1 = new Medico("12345678", "medPrueba", "abcdef", "Eduardo", "P. C.", centro1, pediatra);
			medico2 = new Medico("87654321", "medico2", "xxx", "Carmen", "G. G.", centro1, cabecera);
			citador1 = new Citador("11223344", "citador", "cit123", "Fernando", "G. P.", centro1);
			admin1 = new Administrador("55667788", "admin", "nimda", "María", "L. F.", centro1);
			bene1 = new Beneficiario("12345678","123456-ab","bene1","asdfg","alguno","uno@gmail.com",13,123456789,987654321);
			bene1.setMedicoAsignado(medico2);
			bene2 = new Beneficiario("46208746","164028-de","bene2","asadasdfg","algun otro","dos@gmail.com",21,923456789,687654322);
			bene2.setMedicoAsignado(medico1);
			FPCentroSalud.insertar(centro1);
			FPUsuario.insertar(medico1);
			FPUsuario.insertar(medico2);
			FPUsuario.insertar(citador1);
			FPUsuario.insertar(admin1);
			FPBeneficiario.insertar(bene1);
			FPBeneficiario.insertar(bene2);
			// Iniciamos tres sesiones con roles de citador, administrador y medico
			sesionCitador = GestorSesiones.identificar(citador1.getLogin(), citador1.getPassword());
			sesionAdmin = GestorSesiones.identificar(admin1.getLogin(), admin1.getPassword());
			sesionMedico = GestorSesiones.identificar(medico1.getLogin(), medico1.getPassword());
		} catch(Exception e) {
			fail(e.toString());
			
		}
	}
	
	protected void tearDown() {
		// Cerramos la sesión y quitamos la conexión local con la base de datos
		try {
			GestorSesiones.liberar(((Sesion)sesionCitador).getId());
			GestorSesiones.liberar(((Sesion)sesionAdmin).getId());
			GestorSesiones.liberar(((Sesion)sesionMedico).getId());
			GestorConexionesBD.quitarConexiones();
		} catch(SQLException e) {
			fail(e.toString());
		}
	}
	
	/** Pruebas de la operación que obtiene los datos de un beneficiario */
	public void testObtenerBeneficiario() {
		Beneficiario bene;
		
		try {
			// Obtenemos los datos de un beneficiario por nif
			bene = GestorBeneficiarios.getBeneficiario(sesionCitador.getId(), bene1.getNif());
			assertEquals(bene, bene1);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Obtenemos los datos de un beneficiario por nss
			bene = GestorBeneficiarios.getBeneficiarioPorNSS(sesionCitador.getId(), bene2.getNss());
			assertEquals(bene, bene2);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Intentamos acceder al servidor con un id de sesión erróneo
			bene = GestorBeneficiarios.getBeneficiarioPorNSS(sesionCitador.getId() + 1, bene2.getNss());
			fail("Se esperaba una excepcion SesionInvalidaException");
		} catch(SesionInvalidaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepcion SesionInvalidaException");
		}
		
		try {
			// Intentamos obtener los datos de un usuario que no es beneficiario
			bene = GestorBeneficiarios.getBeneficiario(sesionCitador.getId(), citador1.getDni());
			fail("Se esperaba una excepcion BeneficiarioInexistenteException");
		} catch(BeneficiarioInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepcion BeneficiarioInexistenteException");
		}
		
		try {
			// Intentamos obtener los datos de un beneficiario que no existe
			bene = GestorBeneficiarios.getBeneficiarioPorNSS(sesionCitador.getId(), "1");
			fail("Se esperaba una excepcion BeneficiarioInexistenteException");
		} catch(BeneficiarioInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepcion BeneficiarioInexistenteException");
		}
	}

	/** Pruebas de la operación que crea nuevos beneficiarios */
	public void testCrearBeneficiario() {
		Beneficiario bene, beneGet;
		try {
			// Creamos un nuevo beneficiario con la sesión del administrador
			bene = new Beneficiario("6666666", "14124as-cd", "beNuevo", "nuevos", "calle de la luna", "luna@hotmail.com", 13, 34698124, 67912312);
			GestorBeneficiarios.crear(sesionAdmin.getId(), bene);
			// Comprobamos que el beneficiario se ha creado correctamente
			beneGet = GestorBeneficiarios.getBeneficiario(sesionAdmin.getId(), bene.getNif());
			assertEquals(bene, beneGet);
			
			// Se le ha tenido que asignar un pediatra
			assertNotNull(beneGet.getMedicoAsignado());
			assertEquals(FPTipoMedico.consultarTipo(beneGet.getMedicoAsignado()), (new Pediatra()).getClass().getSimpleName());
		} catch(Exception e) {
			e.printStackTrace();
			fail(e.toString());
		}
		
		try {
			// Intentamos crear un nuevo beneficiario con el rol de medico
			bene = new Beneficiario("77777777", "131716-co", "error", "error", "", "", 15, 123456789, 987654321);
			GestorBeneficiarios.crear(sesionMedico.getId(), bene);
			fail("Se esperaba una excepcion OperacionIncorrectaException");
		} catch(OperacionIncorrectaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepcion OperacionIncorrectaException");
		}
		
		try {
			// Intentamos añadir un beneficiario con un DNI que ya existe en la BD
			bene = new Beneficiario(bene1.getNif(), bene1.getNss(), "error", "error", "", "", 21, 123456789, 987654321);
			GestorBeneficiarios.crear(sesionAdmin.getId(), bene);
			fail("Se esperaba una excepcion BeneficiarioYaExistenteException");
		} catch(BeneficiarioYaExistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepcion BeneficiarioYaExistenteException");
		}
	}

	/** Pruebas de la operación que modifica beneficiarios existentes **/
	public void testModificarBeneficiario() {
		Beneficiario bene, beneGet;
		
		try {
			// Modificamos los datos de un benficiario existente como administrador
			bene1.setNombre("beneCambiado");
			GestorBeneficiarios.modificar(sesionAdmin.getId(), bene1);
			// Comprobamos que el beneficiario se haya actualizado correctamente
			beneGet = GestorBeneficiarios.getBeneficiario(sesionAdmin.getId(), bene1.getNif());
			assertEquals(bene1, beneGet);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Intentamos modificar un beneficiario con el rol de medico
			GestorBeneficiarios.modificar(sesionMedico.getId(), bene1);
			fail("Se esperaba una excepcion OperacionIncorrectaException");
		} catch(OperacionIncorrectaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepcion OperacionIncorrectaException");
		}
		
		try {
			// Intentamos modificar un beneficiario que aún no se ha creado
			bene = new Beneficiario("21412395", "131314-as", "error", "error", "", "", 21, 123456789, 987654321);
			GestorBeneficiarios.modificar(sesionAdmin.getId(), bene);
			fail("Se esperaba una excepcion BeneficiarioInexistenteException");
		} catch(BeneficiarioInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepcion BeneficiarioInexistenteException");
		}
	}
	
}