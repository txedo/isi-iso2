package pruebas;

import java.util.Date;
import java.util.Vector;
import persistencia.FPCentroSalud;
import persistencia.FPUsuario;
import comunicaciones.RemotoServidorFrontend;
import dominio.UtilidadesDominio;
import dominio.conocimiento.Administrador;
import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.CentroSalud;
import dominio.conocimiento.Cita;
import dominio.conocimiento.DiaSemana;
import dominio.conocimiento.Direccion;
import dominio.conocimiento.Especialidades;
import dominio.conocimiento.Especialista;
import dominio.conocimiento.ICodigosMensajeAuxiliar;
import dominio.conocimiento.IConstantes;
import dominio.conocimiento.IMedico;
import dominio.conocimiento.ISesion;
import dominio.conocimiento.Medico;
import dominio.conocimiento.Pediatra;
import dominio.conocimiento.PeriodoTrabajo;

/**
 * Pruebas del objeto remoto exportado por el servidor front-end para
 * conectarse con la base de datos y la ventana de estado.
 */
public class PruebasRemotoServidor extends PruebasBase {
	
	private RemotoServidorFrontend conexion;
	private Administrador admin;
	private Medico medico;
	private CentroSalud centro;
	
	public void setUp() {
		try {
			// Preparamos la base de datos
			super.setUp();
			// Creamos el objeto remoto exportado por el servidor front-end
			conexion = RemotoServidorFrontend.getServidor();
			// Creamos un administrador en la base de datos
			centro = new CentroSalud("Centro de pruebas", "");
			admin = new Administrador("11886622X", "adminprueba", UtilidadesDominio.encriptarPasswordSHA1("adminprueba"), "abc", "abc", "", "", "");
			admin.setCentroSalud(centro);
			medico = new Medico("66330044X", "medicoprueba", UtilidadesDominio.encriptarPasswordSHA1("medicoprueba"), "abc", "abc", "", "", "", new Pediatra());
			medico.getCalendario().add(new PeriodoTrabajo(10, 12, DiaSemana.Lunes));
			medico.setCentroSalud(centro);
			FPCentroSalud.insertar(centro);
			FPUsuario.insertar(admin);
			FPUsuario.insertar(medico);
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	public void tearDown() {
		try {
			// Cerramos la base de datos
			super.tearDown();
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	/** Pruebas de conexi�n y desconexi�n */
	public void testConectarDesconectar() {
		try {
			// Activamos la conexi�n varias veces para ver si no hay fallos
			conexion.activar(IDatosPruebas.IP_ESCUCHA, IDatosPruebas.PUERTO_ESCUCHA);
			conexion.activar(IDatosPruebas.IP_ESCUCHA, IDatosPruebas.PUERTO_ESCUCHA);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Desactivamos la conexi�n varias veces para ver si no hay fallos
			conexion.desactivar(IDatosPruebas.IP_ESCUCHA, IDatosPruebas.PUERTO_ESCUCHA);
			conexion.desactivar(IDatosPruebas.IP_ESCUCHA, IDatosPruebas.PUERTO_ESCUCHA);
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	/** Pruebas de las operaciones */
	@SuppressWarnings("deprecation")
	public void testOperaciones() {
		Vector<Date> dias;
		Vector<Cita> citas;
		ClientePrueba cliente;
		Beneficiario beneficiario = null;
		Medico medico1 = null, medico2 = null;
		ISesion sesionAdmin = null, sesionMedico = null;
		long idVolante = 0;
		
		// Todas las operaciones de la clase RemotoServidorFrontend se redirigen
		// a la clase ServidorFrontend, la cual se prueba con los casos de prueba
		// de los gestores; por eso aqu� no se contempla m�s que un escenario
		// para ver que la operaci�n se ejecuta correctamente
		
		try {
			// Probamos las operaciones de gesti�n de sesiones
			sesionAdmin = conexion.identificar(admin.getLogin(), admin.getLogin());
			sesionMedico = conexion.identificar(medico.getLogin(), medico.getLogin());
			cliente = new ClientePrueba();
			cliente.activar(cliente.getDireccionIP());
			conexion.registrar(cliente, sesionAdmin.getId());
			conexion.liberar(sesionAdmin.getId());
			sesionAdmin = conexion.identificar(admin.getLogin(), admin.getLogin());
		} catch(Exception e) {
			fail(e.toString());
		}

		try {
			// Probamos las operaciones de gesti�n de beneficiarios
			beneficiario = new Beneficiario("00002222A", "767676767676", "P", "Q", new Date(), new Direccion("A", "2", "", "", "B", "C", 10000), "", "", "");
			beneficiario.setCentroSalud(centro);
			beneficiario.setMedicoAsignado(medico);
			conexion.crear(sesionAdmin.getId(), beneficiario);
			beneficiario.setApellidos("X");
			conexion.modificar(sesionAdmin.getId(), beneficiario);
			assertEquals(beneficiario, conexion.getBeneficiario(sesionAdmin.getId(), beneficiario.getNif()));
			assertEquals(beneficiario, conexion.getBeneficiarioPorNSS(sesionAdmin.getId(), beneficiario.getNss()));
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Probamos las operaciones de gesti�n de m�dicos
			medico1 = new Medico("55113300X", "medico1", "medico1", "abc", "abc", "", "", "", new Especialista(Especialidades.Traumatologia.name()));
			medico1.setCentroSalud(centro);
			medico1.getCalendario().add(new PeriodoTrabajo(10, 12, DiaSemana.Lunes));
			medico2 = new Medico("66880011X", "medico2", "medico2", "abc", "abc", "", "", "", new Especialista(Especialidades.Traumatologia.name()));
			medico2.setCentroSalud(centro);
			conexion.crear(sesionAdmin.getId(), medico1);
			conexion.crear(sesionAdmin.getId(), medico2);
			medico2.setApellidos("apellidos");
			conexion.modificar(sesionAdmin.getId(), medico2);
			medico2.setPassword(UtilidadesDominio.encriptarPasswordSHA1(medico2.getPassword()));
			assertEquals(medico2, conexion.getMedico(sesionAdmin.getId(), medico2.getNif()));
			dias = new Vector<Date>();
			dias.add(new Date(2015 - 1900, 8, 14)); // Lunes 14-Septiembre-2015
			conexion.modificarCalendario(sesionAdmin.getId(), medico1, dias, new Date(2010 - 1900, 1, 1, 9, 0), new Date(2010 - 1900, 1, 1, 13, 0), (IMedico)medico2);
			conexion.eliminar(sesionAdmin.getId(), medico2);
		} catch(Exception e) {
			fail(e.toString());
		}

		try {
			// Probamos las operaciones de gesti�n de volantes
			idVolante = conexion.emitirVolante(sesionMedico.getId(), beneficiario, medico1, medico1);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Probamos las operaciones de gesti�n de citas
			conexion.pedirCita(sesionAdmin.getId(), beneficiario, medico.getNif(), new Date(2015 - 1900, 8, 14, 10, 15), IConstantes.DURACION_CITA);
			conexion.pedirCita(sesionAdmin.getId(), beneficiario, idVolante, new Date(2015 - 1900, 8, 14, 10, 15), IConstantes.DURACION_CITA);
			citas = conexion.getCitas(sesionAdmin.getId(), beneficiario.getNif());
			assertTrue(citas.size() == 2);
			conexion.anularCita(sesionAdmin.getId(), citas.get(0));
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Probamos alguna operaci�n del mensaje auxiliar
			medico1 = (Medico)conexion.mensajeAuxiliar(sesionMedico.getId(), ICodigosMensajeAuxiliar.CONSULTAR_PROPIO_USUARIO, null);
			assertEquals(medico1, medico);
		} catch(Exception e) {
			fail(e.toString());
		}
	}

}
