package persistencia;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import comunicaciones.GestorConexionesBD;

import dominio.Beneficiario;
import dominio.Cita;
import dominio.Medico;
import dominio.Volante;
import excepciones.BeneficiarioInexistenteException;
import excepciones.CentroSaludIncorrectoException;
import excepciones.UsuarioIncorrectoException;
import excepciones.VolanteNoValidoException;

public class FPVolante {

	private static final String TABLA_VOLANTES = "volantes";
	private static final String ID_VOLANTE = "id";
	private static final String COL_DNI_BENEFICIARIO = "dniBeneficiario";
	private static final String COL_DNI_MEDICO_EMISOR = "dniMedicoEmisor";
	private static final String COL_DNI_MEDICO_RECEPTOR = "dniMedicoReceptor";
	
	public static Volante consultarPorID(long id) throws SQLException, VolanteNoValidoException, BeneficiarioInexistenteException, UsuarioIncorrectoException, CentroSaludIncorrectoException {
		ComandoSQL comando;
		ResultSet datos;
		Volante vol = new Volante();
		Medico medEmisor = null;
		Medico medReceptor = null;
		Beneficiario bene = null;
		
		// Consultamos la base de datos
		comando = new ComandoSQLSentencia("SELECT * FROM "
				+ TABLA_VOLANTES + " WHERE " + ID_VOLANTE + " = ?", id);
		datos = GestorConexionesBD.consultar(comando);
		datos.next();
		
		// Si no se obtienen datos, no existe el volante 
		if (datos.getRow() == 0) 
			throw new VolanteNoValidoException("No existe el volante con el id introducido");
		
		// Se recorren las filas
		while (datos.next()){
			bene = FPBeneficiario.consultarPorNIF(COL_DNI_BENEFICIARIO);
			medEmisor = (Medico) FPUsuario.consultar(COL_DNI_MEDICO_EMISOR);
			medReceptor = (Medico) FPUsuario.consultar(COL_DNI_MEDICO_RECEPTOR);
			vol.setId(id);
			vol.setBeneficiario(bene);
			vol.setEmisor(medEmisor);
			vol.setReceptor(medReceptor);
		}
		return vol;
	}
		
	public void insertar(Volante vol) throws SQLException {
		ComandoSQL comando;
		ResultSet datos;

		comando = new ComandoSQLSentencia("INSERT INTO " + TABLA_VOLANTES+ " (" + COL_DNI_BENEFICIARIO + ", " + COL_DNI_MEDICO_EMISOR + ", " + COL_DNI_MEDICO_RECEPTOR + ") VALUES (?, ?, ?)",
		                                  vol.getBeneficiario().getNif(), vol.getEmisor().getDni(), vol.getReceptor().getDni());
		GestorConexionesBD.ejecutar(comando);
		
		// Cambiamos el id del volante
		comando = new ComandoSQLSentencia("SELECT LAST_INSERT_ID()");			
		datos = GestorConexionesBD.consultar(comando);
		datos.next();
		vol.setId(datos.getInt("LAST_INSERT_ID()"));
	}
}
