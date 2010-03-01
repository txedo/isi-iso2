package pruebas;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Vector;
import dominio.UtilidadesDominio;
import dominio.conocimiento.Administrador;
import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.Cabecera;
import dominio.conocimiento.CategoriasMedico;
import dominio.conocimiento.CentroSalud;
import dominio.conocimiento.Cita;
import dominio.conocimiento.Citador;
import dominio.conocimiento.DiaSemana;
import dominio.conocimiento.Direccion;
import dominio.conocimiento.EntradaLog;
import dominio.conocimiento.Especialista;
import dominio.conocimiento.ITiposMensajeLog;
import dominio.conocimiento.Medico;
import dominio.conocimiento.Pediatra;
import dominio.conocimiento.PeriodoTrabajo;
import dominio.conocimiento.Sustitucion;
import dominio.conocimiento.TipoMedico;
import dominio.conocimiento.Usuario;
import dominio.conocimiento.Volante;
import excepciones.BeneficiarioInexistenteException;
import excepciones.CentroSaludInexistenteException;
import excepciones.CitaNoValidaException;
import excepciones.DireccionInexistenteException;
import excepciones.UsuarioIncorrectoException;
import excepciones.VolanteNoValidoException;
import persistencia.FPBeneficiario;
import persistencia.FPCentroSalud;
import persistencia.FPCita;
import persistencia.FPDireccion;
import persistencia.FPEntradaLog;
import persistencia.FPPeriodoTrabajo;
import persistencia.FPSustitucion;
import persistencia.FPTipoMedico;
import persistencia.FPUsuario;
import persistencia.FPVolante;
import persistencia.UtilidadesPersistencia;

public class PruebasPersistencia extends PruebasBase {

	private CentroSalud centro1, centro2, centro3;
	private EntradaLog entrada1, entrada2, entrada3;
	private EntradaLog entrada4, entrada5, entrada6;
	private Medico medico1, medico2, medico3, medico4;
	private Direccion direccion1, direccion2, direccion3;
	private Citador citador1, citador2;
	private Administrador administrador1;
	private Beneficiario beneficiario1, beneficiario2, beneficiario3;
	private Cita cita1, cita2, cita3;
	private PeriodoTrabajo periodo1, periodo2;
	private Sustitucion sustitucion1, sustitucion2;
	private Volante volante1, volante2;
	
	@SuppressWarnings("deprecation")
	protected void setUp() {
		try {
			// Preparamos la base de datos
			super.setUp();
			// Creamos objetos de prueba
			centro1 = new CentroSalud("Centro A", "C/Pequeña, nº4");
			centro2 = new CentroSalud("Centro B", "C/Media, nº10");
			centro3 = new CentroSalud("Centro C", "C/Grande, nº5");
			entrada1 = new EntradaLog("juan", new Timestamp(109, 11, 1, 10, 10, 10, 0), ITiposMensajeLog.TIPO_CREATE, "Entrada CREATE.");
			entrada2 = new EntradaLog("luis", new Timestamp(109, 5, 25, 7, 30, 0, 0), ITiposMensajeLog.TIPO_READ, "Entrada READ.");
			entrada3 = new EntradaLog("pepe", new Timestamp(109, 9, 4, 8, 0, 0, 0), ITiposMensajeLog.TIPO_UPDATE, "Entrada UPDATE.");
			entrada4 = new EntradaLog("carmen", new Timestamp(109, 8, 10, 8, 0, 0, 0), ITiposMensajeLog.TIPO_DELETE, "Entrada DELETE.");
			entrada5 = new EntradaLog("ana", new Timestamp(109, 1, 10, 8, 0, 0, 0), ITiposMensajeLog.TIPO_INFO, "Entrada INFO.");
			entrada6 = new EntradaLog("mal", new Timestamp(109, 9, 7, 20, 0, 0, 0), "mal", "Entrada con errores.");
			medico1 = new Medico("12345678A", "medPrueba", UtilidadesDominio.encriptarPasswordSHA1("abcdef"), "Eduardo", "P. C.", "", "", "", new Especialista("ninguna"));
			medico2 = new Medico("87654321A", "medico2", UtilidadesDominio.encriptarPasswordSHA1("xxx"), "Carmen", "G. G.", "carmen@gmail.com", "", "666123123", new Cabecera());
			medico3 = new Medico("29478569A", "otroMedico", UtilidadesDominio.encriptarPasswordSHA1("xxx"), "José", "R. B.", "josejose@uclm.es", "900112233", "600112233", new Cabecera());
			medico4 = new Medico("89123479A", "especial", UtilidadesDominio.encriptarPasswordSHA1("especial"), "Isabel", "P. G.", "", "", "", new Pediatra());
			citador1 = new Citador("1112223A", "citador", UtilidadesDominio.encriptarPasswordSHA1("abcdef"), "Luis", "E. G.", "luiseg55@yahoo.com", "", "612121212");
			citador2 = new Citador("9998887A", "citador", UtilidadesDominio.encriptarPasswordSHA1("abcdef"), "Ana", "B. E.", "", "989989989", "");
			administrador1 = new Administrador("12121212A", "admin", UtilidadesDominio.encriptarPasswordSHA1("admin"), "Administrador", "", "adminssca@ssca.com", "900000000", "600000000");
			medico1.setCentroSalud(centro1);
			medico2.setCentroSalud(centro1);
			medico3.setCentroSalud(centro2);
			medico4.setCentroSalud(centro2);
			citador1.setCentroSalud(centro1);
			citador2.setCentroSalud(centro2);
			administrador1.setCentroSalud(centro1);
			periodo1 = new PeriodoTrabajo(10, 12, DiaSemana.Lunes);
			periodo2 = new PeriodoTrabajo(16, 20, DiaSemana.Jueves);
			direccion1 = new Direccion("Calle Toledo", "3", "", "", "Ciudad", "Provincia", 15500);
			direccion2 = new Direccion("Plaza de España", "12", "", "", "Ciudad", "Provincia", 16500);
			direccion3 = new Direccion("Avenida Principal", "4", "3", "A", "Ciudad", "Provincia", 17500);
			beneficiario1 = new Beneficiario("11223344W", "121212454545", "Ángel", "L. A.", new Date(1985 - 1900, 4, 1), direccion1, "angel129@gmail.com", "900111222", "600111222");
			beneficiario2 = new Beneficiario("88776655R", "444444444444", "José", "R. S.", new Date(1990 - 1900, 8, 20), direccion2, "pepepepe@otro.com", "900123123", "600123123");
			beneficiario3 = new Beneficiario("91839184P", "888111111888", "Alicia", "S. L.", new Date(1945 - 1900, 1, 17), direccion3, "ali45@yahoo.es", "900455455", "600455455");
			beneficiario1.setCentroSalud(medico1.getCentroSalud());
			beneficiario2.setCentroSalud(medico2.getCentroSalud());
			beneficiario3.setCentroSalud(medico3.getCentroSalud());
			beneficiario1.setMedicoAsignado(medico1);
			beneficiario2.setMedicoAsignado(medico2);
			beneficiario3.setMedicoAsignado(medico3);
			cita1 = new Cita(new Date(2010 - 1900, 1, 10), 15, beneficiario1, medico1);
			cita2 = new Cita(new Date(2010 - 1900, 2, 5), 15, beneficiario2, medico2);
			cita3 = new Cita(new Date(2010 - 1900, 2, 24), 15, beneficiario2, medico1);
			sustitucion1 = new Sustitucion(new Date(2009 - 1900, 11, 1), 10, 14, medico1, medico2);
			sustitucion2 = new Sustitucion(new Date(2009 - 1900, 11, 2), 9, 12, medico1, medico2);
			volante1 = new Volante(medico1, medico2, beneficiario1, null, new Date());
			volante2 = new Volante(medico2, medico3, beneficiario2, cita1, new Date());
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
	
	/** Pruebas de la tabla de beneficiarios */
	@SuppressWarnings("deprecation")
	public void testBeneficiarios() {
		Vector<String> nifsBeneficiarios;
		Beneficiario beneficiario;
		
		try {
			// Intentamos buscar un beneficiario inexistente
			FPBeneficiario.consultarPorNIF("12345678M");
			fail("Se esperaba una excepción BeneficiarioInexistenteException");
		} catch(BeneficiarioInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción BeneficiarioInexistenteException");
		}
		
		try {
			// Intentamos buscar un beneficiario inexistente
			FPBeneficiario.consultarPorNSS("000000000000");
			fail("Se esperaba una excepción BeneficiarioInexistenteException");
		} catch(BeneficiarioInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción BeneficiarioInexistenteException");
		}
		
		try {
			// Añadimos los centros de salud asociados a los médicos
			FPCentroSalud.insertar(centro1);
			FPCentroSalud.insertar(centro2);
			FPCentroSalud.insertar(centro3);
			// Insertamos los médicos de los beneficiarios
			FPUsuario.insertar(medico1);
			FPUsuario.insertar(medico2);
			FPUsuario.insertar(medico3);
			// Insertamos varios beneficiarios
			FPBeneficiario.insertar(beneficiario1);
			FPBeneficiario.insertar(beneficiario2);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Intentamos insertar un beneficiario con un NIF existente
			beneficiario3.setNif(beneficiario1.getNif());
			FPBeneficiario.insertar(beneficiario3);
			fail("Se esperaba una excepción SQLException");
		} catch(SQLException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción SQLException");
		}

		try {
			// Intentamos insertar un beneficiario con un NSS existente
			beneficiario3.setNif("12019340L");
			beneficiario3.setNss(beneficiario1.getNss());
			FPBeneficiario.insertar(beneficiario3);
			fail("Se esperaba una excepción SQLException");
		} catch(SQLException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción SQLException");
		}
		
		try {
			// Insertamos otro beneficiario más sin médico asignado
			beneficiario3.setNss("998877778899");
			beneficiario3.setMedicoAsignado(null);
			FPBeneficiario.insertar(beneficiario3);
			// Recuperamos varios beneficiarios de las dos formas posibles
			beneficiario = FPBeneficiario.consultarPorNIF(beneficiario1.getNif());
			assertEquals(beneficiario1, beneficiario);
			beneficiario = FPBeneficiario.consultarPorNSS(beneficiario1.getNss());
			assertEquals(beneficiario1, beneficiario);
			beneficiario = FPBeneficiario.consultarPorNIF(beneficiario2.getNif());
			assertEquals(beneficiario2, beneficiario);
			beneficiario = FPBeneficiario.consultarPorNSS(beneficiario2.getNss());
			assertEquals(beneficiario2, beneficiario);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Modificamos un beneficiario
			beneficiario1.setApellidos("V. L.");
			beneficiario1.setCentroSalud(centro2);
			beneficiario1.setCorreo("nuevocorreo@terra.com");
			beneficiario1.getDireccion().setCiudad("Nueva cuidad");
			beneficiario1.getDireccion().setCP(15000);
			beneficiario1.setFechaNacimiento(new Date(1985 - 1900, 4, 2));
			beneficiario1.setMovil("612312333");
			beneficiario1.setNombre("Pedro");
			beneficiario1.setTelefono("912312333");
			beneficiario1.setMedicoAsignado(medico2);
			FPBeneficiario.modificar(beneficiario1);
			// Comprobamos si los cambios han tenido efecto
			beneficiario = FPBeneficiario.consultarPorNIF(beneficiario1.getNif());
			assertEquals(beneficiario1, beneficiario);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Obtenemos la lista de beneficiarios de un médico dado
			nifsBeneficiarios = FPBeneficiario.consultarBeneficiariosMedico(medico2.getNif());
			assertTrue(nifsBeneficiarios.size() == 2);
			assertTrue((nifsBeneficiarios.get(0).equals(beneficiario1.getNif()) && nifsBeneficiarios.get(1).equals(beneficiario2.getNif()))
			           || (nifsBeneficiarios.get(0).equals(beneficiario2.getNif()) && nifsBeneficiarios.get(1).equals(beneficiario1.getNif()))); 
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Eliminamos un beneficiario
			FPBeneficiario.eliminar(beneficiario2);
			// Comprobamos si los cambios han tenido efecto
			FPBeneficiario.consultarPorNIF(beneficiario2.getNif());
			fail("Se esperaba una excepción BeneficiarioInexistenteException");
		} catch(BeneficiarioInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción BeneficiarioInexistenteException");
		}
		
		try {
			// Comprobamos que al borrar un beneficiario se
			// elimina también su dirección asociada
			FPDireccion.consultar(beneficiario2.getNif());
			fail("Se esperaba una excepción DireccionIncorrectaException");
		} catch(DireccionInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción DireccionIncorrectaException");
		}
		
		try {
			// Comprobamos que los beneficiarios no borrados siguen existiendo
			beneficiario = FPBeneficiario.consultarPorNIF(beneficiario1.getNif());
			assertEquals(beneficiario1, beneficiario);
			beneficiario = FPBeneficiario.consultarPorNIF(beneficiario3.getNif());
			assertEquals(beneficiario3, beneficiario);
			beneficiario = FPBeneficiario.consultarPorNSS(beneficiario3.getNss());
			assertEquals(beneficiario3, beneficiario);
		} catch(Exception e) {
			fail(e.toString());
		}
	}

	/** Pruebas de la tabla de centros de salud */
	public void testCentrosSalud() {
		Vector<CentroSalud> centros;
		CentroSalud centro;
		
		try {
			// Intentamos buscar un centro aleatorio sin haber uno
			centro = FPCentroSalud.consultarAleatorio();
			fail("Se esperaba una excepción CentroSaludInexistenteException");
		} catch(CentroSaludInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción CentroSaludInexistenteException");
		}
		
		try {
			// Insertamos varios centros correctos
			FPCentroSalud.insertar(centro1);
			FPCentroSalud.insertar(centro3);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Comprobamos que los centros se han añadido correctamente
			centro = FPCentroSalud.consultarAleatorio();
			assertTrue(centro1.equals(centro) || centro3.equals(centro));
		} catch(Exception e) {
			fail(e.toString());
		}
				
		try {
			// Intentamos insertar el mismo centro para ver si falla
			// (no puede haber dos centros con el mismo nombre)
			FPCentroSalud.insertar(centro1);
			fail("Se esperaba una excepción SQLException");
		} catch(SQLException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción SQLException");
		}

		try {
			// Intentamos buscar un centro inexistente
			centro = FPCentroSalud.consultar(1000);
			fail("Se esperaba una excepción CentroSaludInexistenteException");
		} catch(CentroSaludInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción CentroSaludInexistenteException");
		}
		
		try {
			// Obtenemos la lista completa de centros de salud
			centros = FPCentroSalud.consultarTodo();
			assertTrue(centros.size() == 2);
			assertTrue((centros.get(0).equals(centro1) && centros.get(1).equals(centro3))
			           || (centros.get(0).equals(centro3) && centros.get(1).equals(centro1)));
		} catch(Exception e) {
			fail(e.toString());
		}
	}

	/** Pruebas de la tabla de citas */
	public void testCitas() {
		Vector<Cita> citas;
		Cita cita;
		
		try {
			// Intentamos buscar una cita inexistente
			FPCita.consultar(1000);
			fail("Se esperaba una excepción CitaNoValidaException");
		} catch(CitaNoValidaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción CitaNoValidaException");
		}
		
		try {
			// Intentamos buscar una cita inexistente
			FPCita.consultar(new Date(), 15, "11223344W", "11223344W");
			fail("Se esperaba una excepción CitaNoValidaException");
		} catch(CitaNoValidaException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción CitaNoValidaException");
		}
		
		try {
			// Insertamos los médicos y los beneficiarios
			FPCentroSalud.insertar(centro1);
			FPUsuario.insertar(medico1);
			FPUsuario.insertar(medico2);
			FPBeneficiario.insertar(beneficiario1);
			FPBeneficiario.insertar(beneficiario2);
			// Recuperamos las citas de un beneficiario y un médico
			// que no tienen citas para ver que no falla
			citas = FPCita.consultarPorBeneficiario(beneficiario1.getNif());
			assertTrue(citas.size() == 0);
			citas = FPCita.consultarPorMedico(medico1.getNif());
			assertTrue(citas.size() == 0);
		} catch(Exception e) {
			fail(e.toString());
		}

		try {
			// Intentamos recuperar las citas de un beneficiario que no existe
			FPCita.consultarPorBeneficiario("20008882P");
			fail("Se esperaba una excepción BeneficiarioInexistenteException");
		} catch(BeneficiarioInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción BeneficiarioInexistenteException");
		}

		try {
			// Intentamos recuperar las citas de un médico que no existe
			FPCita.consultarPorMedico("20008882P");
			fail("Se esperaba una excepción UsuarioIncorrectoException");
		} catch(UsuarioIncorrectoException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción UsuarioIncorrectoException");
		}
		
		try {
			// Intentamos recuperar las citas de un usuario que
			// existe en el sistema pero no es médico
			FPUsuario.insertar(administrador1);
			FPCita.consultarPorMedico(administrador1.getNif());
			fail("Se esperaba una excepción UsuarioIncorrectoException");
		} catch(UsuarioIncorrectoException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción UsuarioIncorrectoException");
		}
		
		try {
			// Insertamos varias citas correctas
			// (y repetidas, que se permite)
			FPCita.insertar(cita1);
			FPCita.insertar(cita2);
			FPCita.insertar(cita3);
			FPCita.insertar(cita2);
			// Recuperamos varias citas de las dos formas posibles
			cita = FPCita.consultar(cita1.getId());
			assertEquals(cita1, cita);
			cita = FPCita.consultar(cita1.getFechaYHora(), cita1.getDuracion(), cita1.getMedico().getNif(), cita1.getBeneficiario().getNif());
			assertEquals(cita1, cita);
			cita = FPCita.consultar(cita2.getId());
			assertEquals(cita2, cita);
			cita = FPCita.consultar(cita2.getFechaYHora(), cita2.getDuracion(), cita2.getMedico().getNif(), cita2.getBeneficiario().getNif());
			assertEquals(cita2, cita);
			// Comprobamos que cada médico y beneficiario tiene sus citas
			citas = FPCita.consultarPorBeneficiario(beneficiario1.getNif());
			assertTrue(citas.size() == 1);
			assertTrue(citas.get(0).equals(cita1));
			citas = FPCita.consultarPorMedico(medico1.getNif());
			assertTrue(citas.size() == 2);
			assertTrue((citas.get(0).equals(cita1) && citas.get(1).equals(cita3)
					   || (citas.get(0).equals(cita3) && citas.get(1).equals(cita1))));
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Eliminamos una cita
			FPCita.eliminar(cita1);
			// Comprobamos que los cambios han tenido efecto
			citas = FPCita.consultarPorBeneficiario(beneficiario1.getNif());
			assertTrue(citas.size() == 0);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Comprobamos que las citas no borradas siguen existiendo
			// Recuperamos varias citas de las dos formas posibles
			cita = FPCita.consultar(cita2.getId());
			assertEquals(cita2, cita);
			cita = FPCita.consultar(cita3.getId());
			assertEquals(cita3, cita);
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	/** Pruebas de la tabla de direcciones */
	public void testDirecciones() {
		Direccion direccion;
		
		try {
			// Intentamos buscar la dirección de un beneficiario inexistente
			FPDireccion.consultar("88118811N");
			fail("Se esperaba una excepción DireccionInexistenteException");
		} catch(DireccionInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción DireccionInexistenteException");
		}
		
		try {
			// Añadimos los centros de salud asociados a los médicos
			FPCentroSalud.insertar(centro1);
			FPCentroSalud.insertar(centro2);
			FPCentroSalud.insertar(centro3);
			// Insertamos los médicos de los beneficiarios
			FPUsuario.insertar(medico1);
			FPUsuario.insertar(medico2);
			FPUsuario.insertar(medico3);
			// Insertamos varios beneficiarios
			// (se llama automáticamente a FPDireccion)
			FPBeneficiario.insertar(beneficiario1);
			FPBeneficiario.insertar(beneficiario2);
			FPBeneficiario.insertar(beneficiario3);
			// Recuperamos la dirección de los beneficiarios creados
			direccion = FPDireccion.consultar(beneficiario1.getNif());
			assertEquals(direccion1, direccion);
			direccion = FPDireccion.consultar(beneficiario2.getNif());
			assertEquals(direccion2, direccion);
			direccion = FPDireccion.consultar(beneficiario3.getNif());
			assertEquals(direccion3, direccion);
		} catch(Exception e) {
			fail(e.toString());
		}

		try {
			// Modificamos una dirección existente
			direccion2.setCiudad("Más lejos");
			direccion2.setCP(9001);
			direccion2.setDomicilio("Nuevo domicilio");
			direccion2.setNumero("8A");
			direccion2.setPiso("5");
			direccion2.setProvincia("Nueva provincia");
			direccion2.setPuerta("L");
			FPDireccion.modificar(beneficiario2.getNif(), direccion2);
			// Comprobamos que los cambios han tenido efecto
			direccion = FPDireccion.consultar(beneficiario2.getNif());
			assertEquals(direccion2, direccion);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Eliminamos una dirección existente
			FPDireccion.eliminar(beneficiario3.getNif());
			// Comprobamos que los cambios han tenido efecto
			FPDireccion.consultar(beneficiario3.getNif());
			fail("Se esperaba una excepción DireccionInexistenteException");
		} catch(DireccionInexistenteException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción DireccionInexistenteException");
		}
		
		try {
			// Comprobamos que las direcciones no borradas siguen existiendo
			// Recuperamos la dirección de los beneficiarios creados
			direccion = FPDireccion.consultar(beneficiario1.getNif());
			assertEquals(direccion1, direccion);
			direccion = FPDireccion.consultar(beneficiario2.getNif());
			assertEquals(direccion2, direccion);
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	/** Pruebas de la tabla de entradas del log */
	public void testEntradasLog() {
		Vector<EntradaLog> log;
		
		try {
			// Consultamos las entradas sin haber ninguna en
			// el log para ver si se devuelve una lista vacía
			log = FPEntradaLog.consultarLog();
			assertTrue(log != null && log.size() == 0);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Insertamos nuevas entradas válidas
			// (y repetidas, que se permite)
			FPEntradaLog.insertar(entrada1);
			FPEntradaLog.insertar(entrada2);
			FPEntradaLog.insertar(entrada1);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Intentamos insertar una entrada con un tipo
			// de mensaje o acción no permitido
			FPEntradaLog.insertar(entrada6);
			fail("Se esperaba una excepción SQLException");
		} catch(SQLException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción SQLException");
		}

		try {
			// Comprobamos que las entradas se hayan añadido bien
			log = FPEntradaLog.consultarLog();
			assertTrue(log.size() == 3);
			assertTrue((log.get(0).equals(entrada1) && log.get(1).equals(entrada2)
			           || (log.get(0).equals(entrada2) && log.get(1).equals(entrada1))));
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Insertamos entradas con el resto de tipos de mensaje
			FPEntradaLog.insertar(entrada3);
			FPEntradaLog.insertar(entrada4);
			FPEntradaLog.insertar(entrada5);
		} catch(Exception e) {
			fail(e.toString());
		}
	}

	/** Pruebas de la tabla de períodos de trabajo */
	public void testPeriodosTrabajo() {
		Vector<PeriodoTrabajo> periodos;
		
		try {
			// Intentamos consultar los períodos de trabajo antes de
			// añadirlos para ver que se devuelve una lista vacía
			periodos = FPPeriodoTrabajo.consultarHorario("11223344W");
			assertTrue(periodos.size() == 0);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Añadimos un médico a la base de datos
			FPCentroSalud.insertar(centro1);
			FPUsuario.insertar(medico1);
			// Insertamos varios períodos de trabajo correctos
			FPPeriodoTrabajo.insertar(medico1.getNif(), periodo1);
			FPPeriodoTrabajo.insertar(medico1.getNif(), periodo2);
		} catch(Exception e) {
			fail(e.toString());
		}

		try {
			// Intentamos insertar un período de trabajo
			// asociado a un NIF que no es de ningún usuario
			FPPeriodoTrabajo.insertar("00110011X", periodo2);
			fail("Se esperaba una SQLException");
		} catch(SQLException e) {
		} catch(Exception e) {
			fail("Se esperaba una SQLException");
		}
		
		try {
			// Modificamos un período de trabajo existente
			periodo1.setHoraInicio(9);
			periodo1.setHoraFinal(11);
			periodo1.setDia(DiaSemana.Viernes);
			FPPeriodoTrabajo.modificar(periodo1);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Recuperamos los períodos de trabajo almacenados
			// y comprobamos si la modificación tuvo efecto
			periodos = FPPeriodoTrabajo.consultarHorario(medico1.getNif());
			assertTrue((periodos.get(0).equals(periodo1) && periodos.get(1).equals(periodo2)
			           || (periodos.get(0).equals(periodo2) && periodos.get(1).equals(periodo1))));
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Eliminamos un período de trabajo
			FPPeriodoTrabajo.eliminar(periodo1);
			// Comprobamos si los cambios han tenido efecto
			periodos = FPPeriodoTrabajo.consultarHorario(medico1.getNif());
			assertTrue(periodos.size() == 1);
			assertTrue(periodos.get(0).equals(periodo2));
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	/** Pruebas de la tabla de sustituciones */
	public void testSustituciones() {
		Vector<Sustitucion> sustituciones;
		
		try {
			// Añadimos varios médicos a la base de datos
			FPCentroSalud.insertar(centro1);
			FPUsuario.insertar(medico1);
			FPUsuario.insertar(medico2);
			// Intentamos consultar las sustituciones antes de añadirlas
			// para comprobar que se devuelve una lista vacía
			sustituciones = FPSustitucion.consultarPorSustituido(medico1.getNif());
			assertTrue(sustituciones.size() == 0);
			sustituciones = FPSustitucion.consultarPorSustituto(medico2.getNif());
			assertTrue(sustituciones.size() == 0);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Intentamos leer las sustituciones de un médico que no existe
			FPSustitucion.consultarPorSustituido("01233210N");
			fail("Se esperaba una excepción UsuarioIncorrectoException");
		} catch(UsuarioIncorrectoException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción UsuarioIncorrectoException");
		}
		
		try {
			// Intentamos leer las sustituciones hechas por un médico que no existe
			FPSustitucion.consultarPorSustituto("01233210N");
			fail("Se esperaba una excepción UsuarioIncorrectoException");
		} catch(UsuarioIncorrectoException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción UsuarioIncorrectoException");
		}
		
		try {
			// Insertamos varias sustituciones correctas
			FPSustitucion.insertar(sustitucion1);
			FPSustitucion.insertar(sustitucion2);
			// Recuperamos las sustituciones almacenadas
			sustituciones = FPSustitucion.consultarPorSustituido(medico1.getNif());
			assertTrue((sustituciones.get(0).equals(sustitucion1) && sustituciones.get(1).equals(sustitucion2)
			           || (sustituciones.get(0).equals(sustitucion2) && sustituciones.get(1).equals(sustitucion1))));
			sustituciones = FPSustitucion.consultarPorSustituto(medico2.getNif());
			assertTrue((sustituciones.get(0).equals(sustitucion1) && sustituciones.get(1).equals(sustitucion2)
			           || (sustituciones.get(0).equals(sustitucion2) && sustituciones.get(1).equals(sustitucion1))));
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Intentamos consultar las sustituciones de un
			// usuario que existe pero no es médico
			FPUsuario.insertar(administrador1);
			FPSustitucion.consultarPorSustituido(administrador1.getNif());
			fail("Se esperaba una excepción UsuarioIncorrectoException");
		} catch(UsuarioIncorrectoException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción UsuarioIncorrectoException");
		}
		
		try {
			// Intentamos consultar las sustituciones hechas
			// por un usuario que existe pero no es médico
			FPUsuario.insertar(citador1);
			FPSustitucion.consultarPorSustituto(citador1.getNif());
			fail("Se esperaba una excepción UsuarioIncorrectoException");
		} catch(UsuarioIncorrectoException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción UsuarioIncorrectoException");
		}
	}
	
	/** Pruebas de la tabla de tipos de médicos */
	public void testTiposMedico() {
		Vector<String> medicos;
		TipoMedico tipo;
		
		try {
			// Intentamos obtener el tipo de un médico inexistente
			FPTipoMedico.consultar("98777789A");
			fail("Se esperaba una excepción UsuarioIncorrectoException");
		} catch(UsuarioIncorrectoException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción UsuarioIncorrectoException");
		}
		
		try {
			// Intentamos obtener una lista con los médicos especialistas
			// antes de añadirlos para ver que se devuelve una lista vacía
			medicos = FPTipoMedico.consultarMedicos(new Especialista(""));
			assertTrue(medicos.size() == 0);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Insertamos varios centros y médicos
			// (se llama automáticamente a FPTipoMedico)
			FPCentroSalud.insertar(centro1);
			FPCentroSalud.insertar(centro2);
			FPUsuario.insertar(medico1);
			FPUsuario.insertar(medico2);
			FPUsuario.insertar(medico3);
			FPUsuario.insertar(medico4);
			// Recuperamos varios de los tipos de médicos insertados
			tipo = FPTipoMedico.consultar(medico1.getNif());
			assertEquals(tipo, medico1.getTipoMedico());
			tipo = FPTipoMedico.consultar(medico2.getNif());
			assertEquals(tipo, medico2.getTipoMedico());
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Obtenemos una lista con los médicos de cabecera
			medicos = FPTipoMedico.consultarMedicos(new Cabecera());
			assertTrue(medicos.size() == 2);
			assertTrue((medicos.get(0).equals(medico2.getNif()) && medicos.get(1).equals(medico3.getNif()))
			           || (medicos.get(0).equals(medico3.getNif()) && medicos.get(1).equals(medico2.getNif())));
			// Obtenemos una lista con los médidos pediatras
			medicos = FPTipoMedico.consultarMedicos(new Pediatra());
			assertTrue(medicos.size() == 1);
			assertTrue(medicos.get(0).equals(medico4.getNif()));
		} catch(Exception e) {
			fail(e.toString());
		}

		try {
			// Eliminamos uno de los tipos de médicos
			FPTipoMedico.eliminar(medico2.getNif());
			// Comprobamos que los cambios han tenido efecto
			medicos = FPTipoMedico.consultarMedicos(new Cabecera());
			assertTrue(medicos.size() == 1);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Comprobamos que los tipos de médicos no borrados siguen existiendo
			tipo = FPTipoMedico.consultar(medico1.getNif());
			assertEquals(tipo, medico1.getTipoMedico());
			tipo = FPTipoMedico.consultar(medico3.getNif());
			assertEquals(tipo, medico3.getTipoMedico());
			tipo = FPTipoMedico.consultar(medico4.getNif());
			assertEquals(tipo, medico4.getTipoMedico());
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	/** Pruebas de la tabla de usuarios */
	public void testUsuarios() {
		Vector<PeriodoTrabajo> horario;
		Usuario usuario;
		
		try {
			// Intentamos buscar un usuario sin haber creado ninguno
			usuario = FPUsuario.consultar("1234567");
			fail("Se esperaba una excepción UsuarioIncorrectoException");
		} catch(UsuarioIncorrectoException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción UsuarioIncorrectoException");
		}
		
		try {
			// Intentamos buscar un usuario sin haber creado ninguno
			usuario = FPUsuario.consultarPorLogin("login");
			fail("Se esperaba una excepción UsuarioIncorrectoException");
		} catch(UsuarioIncorrectoException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción UsuarioIncorrectoException");
		}
		
		try {
			// Intentamos buscar un usuario sin haber creado ninguno
			usuario = FPUsuario.consultar("login", "pass");
			fail("Se esperaba una excepción UsuarioIncorrectoException");
		} catch(UsuarioIncorrectoException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción UsuarioIncorrectoException");
		}
		
		try {
			// Añadimos los centros de salud asociados a los usuarios
			FPCentroSalud.insertar(centro1);
			FPCentroSalud.insertar(centro2);
			FPCentroSalud.insertar(centro3);
			// Insertamos varios usuarios correctos de todos los tipos
			medico1.getCalendario().add(periodo1);
			medico2.getCalendario().add(periodo2);
			FPUsuario.insertar(medico1);
			FPUsuario.insertar(medico2);
			FPUsuario.insertar(citador1);
			FPUsuario.insertar(administrador1);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Intentamos insertar un usuario con un NIF existente
			FPUsuario.insertar(medico1);
			fail("Se esperaba una excepción SQLException");
		} catch(SQLException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción SQLException");
		}

		try {
			// Intentamos insertar un usuario con un login existente
			FPUsuario.insertar(citador2);
			fail("Se esperaba una excepción SQLException");
		} catch(SQLException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción SQLException");
		}
		
		try {
			// Recuperamos varios usuarios insertados de las tres formas posibles
			usuario = FPUsuario.consultar(medico1.getNif());
			assertEquals(medico1, usuario);
			usuario = FPUsuario.consultar(citador1.getNif());
			assertEquals(citador1, usuario);
			usuario = FPUsuario.consultar(medico1.getLogin(), medico1.getPassword());
			assertEquals(medico1, usuario);
			usuario = FPUsuario.consultar(citador1.getLogin(), citador1.getPassword());
			assertEquals(citador1, usuario);
			usuario = FPUsuario.consultarPorLogin(medico1.getLogin());
			assertEquals(medico1, usuario);
			usuario = FPUsuario.consultarPorLogin(citador1.getLogin());
			assertEquals(citador1, usuario);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Modificamos un usuario que no sea médico
			citador1.setApellidos("U. L.");
			citador1.setCorreo("abc@abc.abc");
			citador1.setLogin("ramoncitador");
			citador1.setMovil("666777888");
			citador1.setNombre("Ramón");
			citador1.setPassword(UtilidadesDominio.encriptarPasswordSHA1("nuevapass"));
			citador1.setTelefono("999888777");
			FPUsuario.modificar(citador1);
			// Comprobamos si los cambios han tenido efecto
			usuario = FPUsuario.consultar(citador1.getNif());
			assertEquals(citador1, usuario);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Modificamos un médico
			medico1.setLogin("nuevologin");
			medico1.getCalendario().add(periodo2);
			medico1.setTipoMedico(new Cabecera());
			FPUsuario.modificar(medico1);
			// Comprobamos si los cambios han tenido efecto
			usuario = FPUsuario.consultar(medico1.getNif());
			assertEquals(medico1, usuario);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Modificamos un usuario de forma incorrecta (login repetido)
			medico1.setLogin("admin");
			FPUsuario.modificar(medico1);
			fail("Se esperaba una excepción SQLException");
		} catch(SQLException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción SQLException");
		}
		
		try {
			// Eliminamos un usuario
			FPUsuario.eliminar(administrador1);
			// Comprobamos si los cambios han tenido efecto
			usuario = FPUsuario.consultar(administrador1.getNif());
			fail("Se esperaba una excepción UsuarioIncorrectoException");
		} catch(UsuarioIncorrectoException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción UsuarioIncorrectoException");
		}
		
		try {
			// Eliminamos un médico
			FPUsuario.eliminar(medico1);
			// Comprobamos si los cambios han tenido efecto
			usuario = FPUsuario.consultar(medico1.getNif());
			fail("Se esperaba una excepción UsuarioIncorrectoException");
		} catch(UsuarioIncorrectoException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción UsuarioIncorrectoException");
		}
		
		try {
			// Comprobamos que al borrar el médico también se
			// ha eliminado la información sobre su tipo
			FPTipoMedico.consultar(medico1.getNif());
			fail("Se esperaba una excepción UsuarioIncorrectoException");
		} catch(UsuarioIncorrectoException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción UsuarioIncorrectoException");
		}
		
		try {
			// Comprobamos que al borrar el médico también se
			// ha eliminado su calendario completo
			horario = FPPeriodoTrabajo.consultarHorario(medico1.getNif());
			assertTrue(horario.size() == 0);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Comprobamos que los usuarios no borrados siguen existiendo
			usuario = FPUsuario.consultar(citador1.getNif());
			assertEquals(citador1, usuario);
			usuario = FPUsuario.consultar(citador1.getLogin(), citador1.getPassword());
			assertEquals(citador1, usuario);
			usuario = FPUsuario.consultar(medico2.getNif());
			assertEquals(medico2, usuario);
			usuario = FPUsuario.consultar(medico2.getLogin(), medico2.getPassword());
			assertEquals(medico2, usuario);
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	/** Pruebas de la tabla de volantes */
	@SuppressWarnings("deprecation")
	public void testVolantes() {
		Volante volante;
		
		try {
			// Intentamos buscar un volante inexistente
			FPVolante.consultar(-900);
			fail("Se esperaba una excepción VolanteNoValidoException");
		} catch(VolanteNoValidoException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción VolanteNoValidoException");
		}
		
		try {
			// Insertamos todos los objetos referidos por los volantes
			FPCentroSalud.insertar(centro1);
			FPCentroSalud.insertar(centro2);
			FPUsuario.insertar(medico1);
			FPUsuario.insertar(medico2);
			FPUsuario.insertar(medico3);
			FPBeneficiario.insertar(beneficiario1);
			FPBeneficiario.insertar(beneficiario2);
			FPCita.insertar(cita1);
			FPCita.insertar(cita2);
			// Insertamos varios volantes correctos
			// (y repetidos, que se permite)
			FPVolante.insertar(volante1);
			FPVolante.insertar(volante2);
			FPVolante.insertar(volante1);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Comprobamos que los volantes se han añadido bien
			volante = FPVolante.consultar(volante1.getId());
			assertEquals(volante1, volante);
			volante = FPVolante.consultar(volante2.getId());
			assertEquals(volante2, volante);
		} catch(Exception e) {
			fail(e.toString());
		}

		try {
			// Modificamos un volante existente
			volante1.setCita(cita2);
			volante1.setEmisor(medico2);
			volante1.setReceptor(medico1);
			volante1.setBeneficiario(beneficiario2);
			volante1.setFechaCaducidad(new Date(2010 - 1900, 4, 4));
			FPVolante.modificar(volante1);
			// Comprobamos que los cambios han tenido efecto
			volante = FPVolante.consultar(volante1.getId());
			assertEquals(volante1, volante);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Comprobamos que los volantes siguen teniendo los datos correctos
			volante = FPVolante.consultar(volante1.getId());
			assertEquals(volante1, volante);
			volante = FPVolante.consultar(volante2.getId());
			assertEquals(volante2, volante);
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	/** Pruebas de las utilidades de persistencia */
	public void testUtilidades() {
		String nif;
		
		try {
			// Añadimos varios beneficiarios y usuarios
			FPCentroSalud.insertar(centro1);
			FPCentroSalud.insertar(centro2);
			FPUsuario.insertar(medico1);
			FPUsuario.insertar(medico2);
			FPUsuario.insertar(medico3);
			FPUsuario.insertar(administrador1);
			FPBeneficiario.insertar(beneficiario1);
			FPBeneficiario.insertar(beneficiario2);
			FPBeneficiario.insertar(beneficiario3);
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Comprobamos que existe el NIF de un beneficiario
			assertTrue(UtilidadesPersistencia.existeNIF(beneficiario3.getNif()));
			// Comprobamos que existe el NIF de un usuario
			assertTrue(UtilidadesPersistencia.existeNIF(administrador1.getNif()));
			// Comprobamos que no existe un NIF aleatorio
			assertFalse(UtilidadesPersistencia.existeNIF("11881188P"));
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Buscamos un médico de cabecera al azar del centro1
			nif = UtilidadesPersistencia.obtenerMedicoAleatorioTipoCentro(CategoriasMedico.Cabecera, centro1);
			assertEquals(medico2.getNif(), nif);
			// Buscamos un médico de cabecera al azar del centro2
			nif = UtilidadesPersistencia.obtenerMedicoAleatorioTipoCentro(CategoriasMedico.Cabecera, centro2);
			assertEquals(medico3.getNif(), nif);
			// Buscamos un médico al azar que no existe
			nif = UtilidadesPersistencia.obtenerMedicoAleatorioTipoCentro(CategoriasMedico.Especialista, centro2);
			assertEquals("", nif);
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
}
