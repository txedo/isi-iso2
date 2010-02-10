package persistencia;

import java.sql.ResultSet;
import java.sql.SQLException;
import comunicaciones.GestorConexionesBD;
import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.Cita;
import dominio.conocimiento.Medico;
import dominio.conocimiento.Volante;
import excepciones.BeneficiarioInexistenteException;
import excepciones.CentroSaludIncorrectoException;
import excepciones.CitaNoValidaException;
import excepciones.DireccionIncorrectaException;
import excepciones.UsuarioIncorrectoException;
import excepciones.VolanteNoValidoException;

/**
 * Clase dedicada a consultar y modificar volantes en la base de datos.
 */
public class FPVolante {

	private static final String TABLA_VOLANTES = "volantes";
	
	private static final String COL_ID_VOLANTE = "id";
	private static final String COL_NIF_BENEFICIARIO = "nifBeneficiario";
	private static final String COL_DNI_MEDICO_EMISOR = "dniMedicoEmisor";
	private static final String COL_DNI_MEDICO_RECEPTOR = "dniMedicoReceptor";
	private static final String COL_ID_CITA = "idCita";
	
	public static Volante consultar(long id) throws SQLException, VolanteNoValidoException, CitaNoValidaException, BeneficiarioInexistenteException, UsuarioIncorrectoException, CentroSaludIncorrectoException, DireccionIncorrectaException {
		ComandoSQL comando;
		ResultSet datos;
		Volante volante;
		Medico medEmisor;
		Medico medReceptor;
		Beneficiario bene;
		Cita cita;
		
		// Consultamos la base de datos
		comando = new ComandoSQLSentencia("SELECT * FROM " + TABLA_VOLANTES + " WHERE " + COL_ID_VOLANTE + " = ?", id);
		datos = GestorConexionesBD.consultar(comando);
		datos.next();
		
		// Si no se obtienen datos, es porque no existe el volante 
		if(datos.getRow() == 0) {
			throw new VolanteNoValidoException("No existe ningún volante con el id " + String.valueOf(id) + ".");
		} else {
			// Establecemos los datos del volante
			volante = new Volante();
			bene = FPBeneficiario.consultarPorNIF(datos.getString(COL_NIF_BENEFICIARIO));
			medEmisor = (Medico)FPUsuario.consultar(datos.getString(COL_DNI_MEDICO_EMISOR));
			medReceptor = (Medico)FPUsuario.consultar(datos.getString(COL_DNI_MEDICO_RECEPTOR));
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
		}
		return volante;
	}
		
	public static void insertar(Volante volante) throws SQLException {
		ComandoSQL comando;
		ResultSet datos;

		// Modificamos la base de datos
		comando = new ComandoSQLSentencia("INSERT INTO " + TABLA_VOLANTES + " (" + COL_NIF_BENEFICIARIO + ", " + COL_DNI_MEDICO_EMISOR + ", " + COL_DNI_MEDICO_RECEPTOR + ", " + COL_ID_CITA + ") VALUES (?, ?, ?, ?)",
		                                  volante.getBeneficiario().getNif(), volante.getEmisor().getDni(), volante.getReceptor().getDni(), (volante.getCita() == null ? null : volante.getCita().getId()));
		GestorConexionesBD.ejecutar(comando);
		
		// Cambiamos el id del nuevo volante
		comando = new ComandoSQLSentencia("SELECT LAST_INSERT_ID()");			
		datos = GestorConexionesBD.consultar(comando);
		datos.next();
		volante.setId(datos.getInt("LAST_INSERT_ID()"));
	}

	public static void modificar(Volante volante) throws SQLException {
		ComandoSQL comando;

		// Modificamos la base de datos
		comando = new ComandoSQLSentencia("UPDATE " + TABLA_VOLANTES + " SET " + COL_NIF_BENEFICIARIO + " = ?, " + COL_DNI_MEDICO_EMISOR + " = ?, " + COL_DNI_MEDICO_RECEPTOR + " = ?, " + COL_ID_CITA + "= ?",
		                                  volante.getBeneficiario().getNif(), volante.getEmisor().getDni(), volante.getReceptor().getDni(), (volante.getCita() == null ? null : volante.getCita().getId()));
		GestorConexionesBD.ejecutar(comando);
	}
	
}
