package persistencia;

import java.sql.ResultSet;
import java.sql.SQLException;
import comunicaciones.GestorConexionesBD;
import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.Cita;
import dominio.conocimiento.Medico;
import dominio.conocimiento.Volante;
import excepciones.BeneficiarioInexistenteException;
import excepciones.CentroSaludInexistenteException;
import excepciones.CitaNoValidaException;
import excepciones.DireccionInexistenteException;
import excepciones.UsuarioIncorrectoException;
import excepciones.VolanteNoValidoException;

/**
 * Clase que permite consultar, insertar y modificar volantes de la
 * base de datos.
 */
public class FPVolante {

	private static final String TABLA_VOLANTES = "volantes";
	
	private static final String COL_ID_VOLANTE = "id";
	private static final String COL_NIF_BENEFICIARIO = "nifBeneficiario";
	private static final String COL_NIF_MEDICO_EMISOR = "nifMedicoEmisor";
	private static final String COL_NIF_MEDICO_RECEPTOR = "nifMedicoReceptor";
	private static final String COL_ID_CITA = "idCita";
	private static final String COL_FECHA_CADUCIDAD = "fechaCaducidad";
	
	public static Volante consultar(long id) throws SQLException, VolanteNoValidoException, CitaNoValidaException, BeneficiarioInexistenteException, UsuarioIncorrectoException, CentroSaludInexistenteException, DireccionInexistenteException {
		ComandoSQL comando;
		ResultSet datos;
		Volante volante;
		Medico medEmisor;
		Medico medReceptor;
		Beneficiario bene;
		Cita cita;
		
		// Consultamos la base de datos
		comando = new ComandoSQLSentencia("SELECT * FROM " + TABLA_VOLANTES
				+ " WHERE " + COL_ID_VOLANTE + " = ?", id);
		datos = GestorConexionesBD.consultar(comando);
		datos.next();
		
		// Si no se obtienen datos, es porque no existe el volante 
		if(datos.getRow() == 0) {
			datos.close();
			throw new VolanteNoValidoException("No existe ning�n volante con el id " + String.valueOf(id) + ".");
		} else {
			// Establecemos los datos del volante
			volante = new Volante();
			bene = FPBeneficiario.consultarPorNIF(datos.getString(COL_NIF_BENEFICIARIO));
			medEmisor = (Medico)FPUsuario.consultar(datos.getString(COL_NIF_MEDICO_EMISOR));
			medReceptor = (Medico)FPUsuario.consultar(datos.getString(COL_NIF_MEDICO_RECEPTOR));
			if(datos.getString(COL_ID_CITA) == null) {
				cita = null;
			} else {
				cita = FPCita.consultar(datos.getInt(COL_ID_CITA));
			}
			volante.setId(id);
			volante.setBeneficiario(bene);
			volante.setEmisor(medEmisor);
			volante.setReceptor(medReceptor);
			volante.setCita(cita);
			volante.setFechaCaducidad(datos.getDate(COL_FECHA_CADUCIDAD));
		}
		
		return volante;
	}
		
	public static void insertar(Volante volante) throws SQLException {
		ComandoSQL comando;
		ResultSet datos;

		// Modificamos la base de datos
		comando = new ComandoSQLSentencia("INSERT INTO " + TABLA_VOLANTES
				+ " (" + COL_NIF_BENEFICIARIO + ", " + COL_NIF_MEDICO_EMISOR
				+ ", " + COL_NIF_MEDICO_RECEPTOR + ", " + COL_FECHA_CADUCIDAD
				+ ", " + COL_ID_CITA + ") VALUES (?, ?, ?, ?, ?)",
				volante.getBeneficiario().getNif(), volante.getEmisor().getNif(),
				volante.getReceptor().getNif(), volante.getFechaCaducidad(),
				(volante.getCita() == null ? null : volante.getCita().getId()));
		GestorConexionesBD.ejecutar(comando);
		
		// Recuperamos el id autonum�rico asignado al nuevo volante
		comando = new ComandoSQLSentencia("SELECT LAST_INSERT_ID()");			
		datos = GestorConexionesBD.consultar(comando);
		datos.next();
		volante.setId(datos.getInt("LAST_INSERT_ID()"));
		datos.close();
	}

	public static void modificar(Volante volante) throws SQLException {
		ComandoSQL comando;

		// Modificamos la base de datos
		comando = new ComandoSQLSentencia("UPDATE " + TABLA_VOLANTES + " SET "
				+ COL_NIF_BENEFICIARIO + " = ?, " + COL_NIF_MEDICO_EMISOR + " = ?, "
				+ COL_NIF_MEDICO_RECEPTOR + " = ?, " + COL_FECHA_CADUCIDAD + " = ?, "
				+ COL_ID_CITA + "= ? WHERE " + COL_ID_VOLANTE + " = ?",
				volante.getBeneficiario().getNif(), volante.getEmisor().getNif(),
				volante.getReceptor().getNif(), volante.getFechaCaducidad(),
				(volante.getCita() == null ? null : volante.getCita().getId()),
				volante.getId());
		GestorConexionesBD.ejecutar(comando);
	}
	
}
