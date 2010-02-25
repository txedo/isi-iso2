package persistencia;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Vector;

import comunicaciones.GestorConexionesBD;
import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.Direccion;
import dominio.conocimiento.Medico;
import dominio.conocimiento.RolesUsuario;
import dominio.conocimiento.Usuario;
import excepciones.BeneficiarioInexistenteException;
import excepciones.CentroSaludInexistenteException;
import excepciones.DireccionInexistenteException;
import excepciones.UsuarioIncorrectoException;

/**
 * Clase que permtie consultar, insertar, modificar y eliminar
 * beneficiarios de la base de datos.
 */
public class FPBeneficiario {

	private static final String TABLA_BENEFICIARIOS = "beneficiarios";
	
	private static final String COL_NIF = "nif";
	private static final String COL_NSS = "nss";
	private static final String COL_NOMBRE = "nombre";
	private static final String COL_APELLIDOS = "apellidos";
	private static final String COL_CORREO = "correo";
	private static final String COL_FECHA_NACIMIENTO = "fechaNacimiento";
	private static final String COL_TELEFONO = "telefono";
	private static final String COL_MOVIL = "movil";
	private static final String COL_NIF_MEDICO = "nifMedico";
	private static final String COL_BENEFICIARIOS_NIF = "nif";
	private static final String COL_ID_CENTRO = "idCentro";

	public static Beneficiario consultarPorNIF(String nif) throws SQLException, BeneficiarioInexistenteException, UsuarioIncorrectoException, CentroSaludInexistenteException, DireccionInexistenteException {
		ComandoSQL comando;
		ResultSet datos;
		Beneficiario beneficiario;
		Usuario medico;
		Direccion direccion;

		// Consultamos la base de datos
		comando = new ComandoSQLSentencia("SELECT * FROM " + TABLA_BENEFICIARIOS
				+ " WHERE " + COL_NIF + " = ?", nif);
		datos = GestorConexionesBD.consultar(comando);
		datos.next();

		// Si no se obtienen datos, es porque el beneficiario no existe
		if(datos.getRow() == 0) {
			datos.close();
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
			direccion = FPDireccion.consultar(beneficiario.getNif());
			beneficiario.setDireccion(direccion);
			if(datos.getString(COL_NIF_MEDICO) == null) {
				beneficiario.setMedicoAsignado(null);
			} else {
				medico = FPUsuario.consultar(datos.getString(COL_NIF_MEDICO));
				if(medico.getRol() != RolesUsuario.Medico) {
					datos.close();
					throw new UsuarioIncorrectoException("El beneficiario con NIF " + nif + " no tiene asignado un usuario con rol de médico.");
				}
				beneficiario.setMedicoAsignado((Medico)medico);
			}
			beneficiario.setCentroSalud(FPCentroSalud.consultar(datos.getInt(COL_ID_CENTRO)));
			datos.close();
		}

		return beneficiario;
	}
	
	public static Beneficiario consultarPorNSS(String nss) throws SQLException, BeneficiarioInexistenteException, UsuarioIncorrectoException, CentroSaludInexistenteException, DireccionInexistenteException {
		ComandoSQL comando;
		ResultSet datos;
		Beneficiario beneficiario;
		Usuario medico;
		Direccion direccion;

		// Consultamos la base de datos
		comando = new ComandoSQLSentencia("SELECT * FROM " + TABLA_BENEFICIARIOS
				+ " WHERE " + COL_NSS + " = ?", nss);
		datos = GestorConexionesBD.consultar(comando);
		datos.next();

		// Si no se obtienen datos, es porque el beneficiario no existe
		if(datos.getRow() == 0) {
			datos.close();
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
			direccion = FPDireccion.consultar(beneficiario.getNif());
			beneficiario.setDireccion(direccion);
			if(datos.getString(COL_NIF_MEDICO) == null) {
				beneficiario.setMedicoAsignado(null);
			} else {
				medico = FPUsuario.consultar(datos.getString(COL_NIF_MEDICO));
				if(medico.getRol() != RolesUsuario.Medico) {
					datos.close();
					throw new UsuarioIncorrectoException("El beneficiario con NSS " + nss + " no tiene asignado un usuario con rol de médico.");
				}
				beneficiario.setMedicoAsignado((Medico)medico);
			}
			beneficiario.setCentroSalud(FPCentroSalud.consultar(datos.getInt(COL_ID_CENTRO)));
			datos.close();
		}

		return beneficiario;
	}
	
	public static Vector<String> consultarBeneficiariosMedico(String nifMedico) throws SQLException {
		ComandoSQL comando;
		ResultSet datos;
		Vector<String> nifsBeneficiarios;
     
		// Consultamos la base de datos
		comando = new ComandoSQLSentencia("SELECT " + COL_BENEFICIARIOS_NIF + " FROM "
				+ TABLA_BENEFICIARIOS + " WHERE " + COL_NIF_MEDICO + " = ? ", nifMedico);
		datos = GestorConexionesBD.consultar(comando);
 
		// Devolvemos la lista de beneficiarios
		nifsBeneficiarios = new Vector<String>();
		while(datos.next()) {
			nifsBeneficiarios.add(datos.getString(COL_BENEFICIARIOS_NIF));
		}
		datos.close();
		
		return nifsBeneficiarios;
	}

	public static void insertar(Beneficiario beneficiario) throws SQLException {
		ComandoSQL comando;
		
		// Modificamos la base de datos
		comando = new ComandoSQLSentencia("INSERT INTO " + TABLA_BENEFICIARIOS
				+ " (" + COL_NIF + ", " + COL_NSS + ", " + COL_NOMBRE
				+ ", " + COL_APELLIDOS  + ", " + COL_CORREO + ", " + COL_FECHA_NACIMIENTO
				+ ", " + COL_TELEFONO + ", " + COL_MOVIL + ", " + COL_NIF_MEDICO + ", " + COL_ID_CENTRO
				+ ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
				beneficiario.getNif(), beneficiario.getNss(), beneficiario.getNombre(),
				beneficiario.getApellidos(), beneficiario.getCorreo(),
				new Timestamp(beneficiario.getFechaNacimiento().getTime()),
				beneficiario.getTelefono(), beneficiario.getMovil(),
				(beneficiario.getMedicoAsignado() == null ? null : beneficiario.getMedicoAsignado().getNif()),
				beneficiario.getCentroSalud().getId());
		GestorConexionesBD.ejecutar(comando);
		
		// Insertamos la dirección del beneficiario
		FPDireccion.insertar(beneficiario.getNif(), beneficiario.getDireccion());
	}

	public static void modificar(Beneficiario beneficiario) throws SQLException {
		ComandoSQL comando;
		
		// Modificamos la base de datos
		// (el NIF y el NSS no se pueden cambiar)
		comando = new ComandoSQLSentencia("UPDATE " + TABLA_BENEFICIARIOS + " SET "
				+ COL_NOMBRE + " = ?, " + COL_APELLIDOS + " = ?, " + COL_CORREO + " = ?, "
				+ COL_FECHA_NACIMIENTO + " = ?, " + COL_TELEFONO + " = ?, "
				+ COL_MOVIL + " = ?, " + COL_NIF_MEDICO + " = ?, " + COL_ID_CENTRO
				+ " = ? WHERE " + COL_NIF + " = ? ", 
				beneficiario.getNombre(), beneficiario.getApellidos(),
				beneficiario.getCorreo(), new Timestamp(beneficiario.getFechaNacimiento().getTime()),
				beneficiario.getTelefono(), beneficiario.getMovil(),
				(beneficiario.getMedicoAsignado() == null ? null : beneficiario.getMedicoAsignado().getNif()),
				beneficiario.getCentroSalud().getId(), beneficiario.getNif());
		GestorConexionesBD.ejecutar(comando);
		
		// Modificamos la dirección del beneficiario
		FPDireccion.modificar(beneficiario.getNif(), beneficiario.getDireccion());
	}

	public static void eliminar(Beneficiario beneficiario) throws SQLException {
		ComandoSQL comando;

		// Modificamos la base de datos
		comando = new ComandoSQLSentencia("DELETE FROM " + TABLA_BENEFICIARIOS
				+ " WHERE " + COL_NIF + " = ?",
				beneficiario.getNif()); 
		GestorConexionesBD.ejecutar(comando);
	}
	
}
