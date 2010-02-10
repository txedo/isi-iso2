package persistencia;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import comunicaciones.GestorConexionesBD;
import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.Direccion;
import dominio.conocimiento.Medico;
import excepciones.BeneficiarioInexistenteException;
import excepciones.CentroSaludIncorrectoException;
import excepciones.DireccionIncorrectaException;
import excepciones.UsuarioIncorrectoException;

/**
 * Clase dedicada a consultar y modificar beneficiarios en la base de datos.
 */
public class FPBeneficiario {

	private static final String TABLA_BENEFICIARIOS = "beneficiarios";
	
	private static final String COL_NIF = "nif";
	private static final String COL_NSS = "nss";
	private static final String COL_NOMBRE = "nombre";
	private static final String COL_APELLIDOS = "apellidos";
	private static final String COL_DIRECCION = "direccion";
	private static final String COL_CORREO = "correo";
	private static final String COL_FECHA_NACIMIENTO = "fechaNacimiento";
	private static final String COL_TELEFONO = "telefono";
	private static final String COL_MOVIL = "movil";
	private static final String COL_DNI_MEDICO = "dniMedico";

	public static Beneficiario consultarPorNIF(String nif) throws SQLException, BeneficiarioInexistenteException, UsuarioIncorrectoException, CentroSaludIncorrectoException, DireccionIncorrectaException {
		ComandoSQL comando;
		ResultSet datos;
		Beneficiario beneficiario;
		Medico medico;
		Direccion direccion;

		// Consultamos la base de datos
		comando = new ComandoSQLSentencia("SELECT * FROM " + TABLA_BENEFICIARIOS + " WHERE " + COL_NIF + " = ?", nif);
		datos = GestorConexionesBD.consultar(comando);
		datos.next();

		// Si no se obtienen datos, es porque el beneficiario no existe
		if(datos.getRow() == 0) {
			throw new BeneficiarioInexistenteException("El beneficiario con NIF " + nif + " no se encuentra dado de alta en el sistema.");
		} else {
			// Establecemos los datos del beneficiario
			beneficiario = new Beneficiario();
			beneficiario.setNif(datos.getString(COL_NIF));
			beneficiario.setNss(datos.getString(COL_NSS));
			beneficiario.setNombre(datos.getString(COL_NOMBRE));
			beneficiario.setApellidos(datos.getString(COL_APELLIDOS));
			beneficiario.setCorreo(datos.getString(COL_CORREO));
			beneficiario.setFechaNacimiento(new Date(datos.getTimestamp(COL_FECHA_NACIMIENTO).getTime()));
			beneficiario.setTelefono(datos.getString(COL_TELEFONO));
			beneficiario.setMovil(datos.getString(COL_MOVIL));
			direccion = FPDireccion.consultar(datos.getInt(COL_DIRECCION));
			beneficiario.setDireccion(direccion);
			medico = (Medico)FPUsuario.consultar(datos.getString(COL_DNI_MEDICO));
			beneficiario.setMedicoAsignado(medico);
		}

		return beneficiario;
	}
	
	public static Beneficiario consultarPorNSS(String nss) throws SQLException, BeneficiarioInexistenteException, UsuarioIncorrectoException, CentroSaludIncorrectoException, DireccionIncorrectaException {
		ComandoSQL comando;
		ResultSet datos;
		Beneficiario beneficiario;
		Medico medico;
		Direccion direccion;

		// Consultamos la base de datos
		comando = new ComandoSQLSentencia("SELECT * FROM " + TABLA_BENEFICIARIOS + " WHERE " + COL_NSS + " = ?", nss);
		datos = GestorConexionesBD.consultar(comando);
		datos.next();

		// Si no se obtienen datos, es porque el beneficiario no existe
		if(datos.getRow() == 0) {
			throw new BeneficiarioInexistenteException("El beneficiario con NSS " + nss + " no se encuentra dado de alta en el sistema.");
		} else {
			// Establecemos los datos del beneficiario
			beneficiario = new Beneficiario();
			beneficiario.setNif(datos.getString(COL_NIF));
			beneficiario.setNss(datos.getString(COL_NSS));
			beneficiario.setNombre(datos.getString(COL_NOMBRE));
			beneficiario.setApellidos(datos.getString(COL_APELLIDOS));
			beneficiario.setCorreo(datos.getString(COL_CORREO));
			beneficiario.setFechaNacimiento(new Date(datos.getTimestamp(COL_FECHA_NACIMIENTO).getTime()));
			beneficiario.setTelefono(datos.getString(COL_TELEFONO));
			beneficiario.setMovil(datos.getString(COL_MOVIL));
			direccion = FPDireccion.consultar(datos.getInt(COL_DIRECCION));
			beneficiario.setDireccion(direccion);	
			medico = (Medico)FPUsuario.consultar(datos.getString(COL_DNI_MEDICO));
			beneficiario.setMedicoAsignado(medico);
		}

		return beneficiario;
	}

	public static void insertar(Beneficiario beneficiario) throws SQLException {
		ComandoSQL comando;

		// Insertamos primero la direcci�n del beneficiario
		FPDireccion.insertar(beneficiario.getDireccion());
		
		// Modificamos la base de datos
		comando = new ComandoSQLSentencia("INSERT INTO " + TABLA_BENEFICIARIOS
				+ " (" + COL_NIF + ", " + COL_NSS + ", " + COL_NOMBRE
				+ ", " + COL_APELLIDOS  + ", " + COL_DIRECCION + ", " + COL_CORREO
				+ ", " + COL_FECHA_NACIMIENTO + ", " + COL_TELEFONO + ", " + COL_MOVIL
				+ ", " + COL_DNI_MEDICO + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
				beneficiario.getNif(), beneficiario.getNss(), beneficiario.getNombre(),
				beneficiario.getApellidos(), beneficiario.getDireccion().getId(),
				beneficiario.getCorreo(), new Timestamp(beneficiario.getFechaNacimiento().getTime()),
				beneficiario.getTelefono(), beneficiario.getMovil(),
				beneficiario.getMedicoAsignado().getDni());
		GestorConexionesBD.ejecutar(comando);
	}

	public static void modificar(Beneficiario beneficiario) throws SQLException {
		ComandoSQL comando;

		// Modificamos primero la direcci�n del beneficiario
		FPDireccion.modificar(beneficiario.getDireccion());
		
		// Modificamos la base de datos
		// (el NIF y el NSS no se pueden cambiar)
		comando = new ComandoSQLSentencia("UPDATE " + TABLA_BENEFICIARIOS + " SET "
				+ COL_NOMBRE + " = ?, " + COL_APELLIDOS + " = ?, " + COL_CORREO + " = ?, "
				+ COL_TELEFONO + " = ?, " + COL_MOVIL + " = ?, " + COL_DNI_MEDICO + " = ? "
				+ "WHERE " + COL_NIF + " = ? ", 
				beneficiario.getNombre(), beneficiario.getApellidos(),
				beneficiario.getCorreo(), beneficiario.getTelefono(),
				beneficiario.getMovil(), beneficiario.getMedicoAsignado().getDni(),
				beneficiario.getNif());
		GestorConexionesBD.ejecutar(comando);
	}

}
