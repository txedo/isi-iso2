package persistencia;

import java.sql.ResultSet;
import java.sql.SQLException;
import comunicaciones.GestorConexionesBD;
import dominio.Beneficiario;
import dominio.Medico;
import dominio.Volante;
import excepciones.BeneficiarioInexistenteException;
import excepciones.CentroSaludIncorrectoException;
import excepciones.UsuarioIncorrectoException;
import excepciones.VolanteNoValidoException;

/**
 * Clase dedicada a consultar y modificar volantes en la base de datos.
 */
public class FPVolante {

	private static final String TABLA_VOLANTES = "volantes";
	
	private static final String COL_ID_VOLANTE = "id";
	private static final String COL_DNI_BENEFICIARIO = "dniBeneficiario";
	private static final String COL_DNI_MEDICO_EMISOR = "dniMedicoEmisor";
	private static final String COL_DNI_MEDICO_RECEPTOR = "dniMedicoReceptor";
	
	public static Volante consultar(long id) throws SQLException, VolanteNoValidoException, BeneficiarioInexistenteException, UsuarioIncorrectoException, CentroSaludIncorrectoException {
		ComandoSQL comando;
		ResultSet datos;
		Volante volante;
		Medico medEmisor;
		Medico medReceptor;
		Beneficiario bene;
		
		// Consultamos la base de datos
		comando = new ComandoSQLSentencia("SELECT * FROM " + TABLA_VOLANTES + " WHERE " + COL_ID_VOLANTE + " = ?", id);
		datos = GestorConexionesBD.consultar(comando);
		datos.next();
		
		// Si no se obtienen datos, es porque no existe el volante 
		if(datos.getRow() == 0) {
			throw new VolanteNoValidoException("No existe ningún volante con el id " + String.valueOf(id));
		} else {
			// Establecemos los datos del volante
			volante = new Volante();
			bene = FPBeneficiario.consultarPorNIF(COL_DNI_BENEFICIARIO);
			medEmisor = (Medico)FPUsuario.consultar(COL_DNI_MEDICO_EMISOR);
			medReceptor = (Medico)FPUsuario.consultar(COL_DNI_MEDICO_RECEPTOR);
			volante.setId(id);
			volante.setBeneficiario(bene);
			volante.setEmisor(medEmisor);
			volante.setReceptor(medReceptor);
		}
		return volante;
	}
		
	public void insertar(Volante volante) throws SQLException {
		ComandoSQL comando;
		ResultSet datos;

		comando = new ComandoSQLSentencia("INSERT INTO " + TABLA_VOLANTES + " (" + COL_DNI_BENEFICIARIO + ", " + COL_DNI_MEDICO_EMISOR + ", " + COL_DNI_MEDICO_RECEPTOR + ") VALUES (?, ?, ?)",
		                                  volante.getBeneficiario().getNif(), volante.getEmisor().getDni(), volante.getReceptor().getDni());
		GestorConexionesBD.ejecutar(comando);
		
		// Cambiamos el id del nuevo volante
		comando = new ComandoSQLSentencia("SELECT LAST_INSERT_ID()");			
		datos = GestorConexionesBD.consultar(comando);
		datos.next();
		volante.setId(datos.getInt("LAST_INSERT_ID()"));
	}
}
