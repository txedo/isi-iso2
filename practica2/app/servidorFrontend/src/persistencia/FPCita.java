package persistencia;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import comunicaciones.GestorConexionesBD;

import dominio.conocimiento.Cita;
import dominio.conocimiento.Roles;
import dominio.conocimiento.Usuario;
import excepciones.BeneficiarioInexistenteException;
import excepciones.CitaNoValidaException;
import excepciones.UsuarioIncorrectoException;

/**
 * Clase que permite consultar, insertar y eliminar citas de la base de datos
 * utilizando Hibernate.
 */
public class FPCita {

	private static final String CLASE_CITA = "Cita";
	
	private static final String ATRIB_ID = "id";
	private static final String ATRIB_FECHA = "fecha";
	private static final String ATRIB_DURACION = "duracion";
	private static final String ATRIB_NIF_BENEFICIARIO = "beneficiario.nif";
	private static final String ATRIB_NIF_MEDICO = "medico.nif";
	
	public static Cita consultar(long id) throws SQLException, CitaNoValidaException {
		ConsultaHibernate consulta;
		List<?> resultados;
		Cita cita;
		
		// Consultamos la base de datos
		consulta = new ConsultaHibernate("FROM " + CLASE_CITA + " WHERE "
				 + ATRIB_ID + " = ?", id);
		resultados = GestorConexionesBD.consultar(consulta);
		
		// Si no se obtienen datos, es porque la cita no existe
		if(resultados.size() == 0) {
			throw new CitaNoValidaException("No existe ninguna cita con el id " + String.valueOf(id) + ".");
		} else {
			// Recuperamos la cita leída
			cita = (Cita)((Cita)resultados.get(0)).clone();
			// Borramos los objetos leídos de la caché
			for(Object objeto : resultados) {
				GestorConexionesBD.borrarCache(objeto);
			}
		}
		
		return cita;
	}
	
	public static Cita consultar(Date fechaYHora, long duracion, String nifMedico, String nifBeneficiario) throws SQLException, CitaNoValidaException {
		ConsultaHibernate consulta;
		List<?> resultados;
		Cita cita;
		
		// Consultamos la base de datos
		consulta = new ConsultaHibernate("FROM " + CLASE_CITA + " WHERE "
				 + ATRIB_FECHA + " = ? AND " + ATRIB_DURACION + " = ? AND "
				 + ATRIB_NIF_MEDICO + " = ? AND " + ATRIB_NIF_BENEFICIARIO + " = ?",
				 fechaYHora, duracion, nifMedico, nifBeneficiario);
		resultados = GestorConexionesBD.consultar(consulta);
		
		// Si no se obtienen datos, es porque la cita no existe
		if(resultados.size() == 0) {
			throw new CitaNoValidaException("No existe ninguna cita con los datos indicados.");
		} else {
			// Recuperamos el centro de salud leído
			cita = (Cita)((Cita)resultados.get(0)).clone();
			// Borramos los objetos leídos de la caché
			for(Object objeto : resultados) {
				GestorConexionesBD.borrarCache(objeto);
			}
		}
		
		return cita;
	}
	
	public static Vector<Cita> consultarPorBeneficiario(String nifBeneficiario) throws SQLException, BeneficiarioInexistenteException {
		ConsultaHibernate consulta;
		List<?> resultados;
		Vector<Cita> citas;
		
		// Comprobamos que el beneficiario exista
		FPBeneficiario.consultarPorNIF(nifBeneficiario);

		// Consultamos la base de datos
		consulta = new ConsultaHibernate("FROM " + CLASE_CITA + " WHERE "
				 + ATRIB_NIF_BENEFICIARIO + " = ? ORDER BY " + ATRIB_FECHA + " ASC",
				 nifBeneficiario);
		resultados = GestorConexionesBD.consultar(consulta);
		
		// Devolvemos la lista de citas del beneficiario
		citas = new Vector<Cita>();
		for(Object cita : resultados) {
			citas.add((Cita)((Cita)cita).clone());
		}
		
		// Borramos los objetos leídos de la caché
		for(Object objeto : resultados) {
			GestorConexionesBD.borrarCache(objeto);
		}

		return citas;
	}

	public static Vector<Cita> consultarPorMedico(String nifMedico) throws SQLException, UsuarioIncorrectoException {
		ConsultaHibernate consulta;
		List<?> resultados;
		Vector<Cita> citas;
		Usuario usuario;
		
		// Comprobamos que el médico exista
		usuario = FPUsuario.consultar(nifMedico);
		if(usuario.getRol() != Roles.Médico) {
			throw new UsuarioIncorrectoException("No se pueden consultar las citas del usuario con NIF " + String.valueOf(nifMedico) + " porque no es un médico.");
		}

		// Consultamos la base de datos
		consulta = new ConsultaHibernate("FROM " + CLASE_CITA + " WHERE "
				 + ATRIB_NIF_MEDICO + " = ? ORDER BY " + ATRIB_FECHA + " ASC",
				 nifMedico);
		resultados = GestorConexionesBD.consultar(consulta);
				
		// Devolvemos la lista de citas del médico
		citas = new Vector<Cita>();
		for(Object cita : resultados) {
			citas.add((Cita)((Cita)cita).clone());
		}
		
		// Borramos los objetos leídos de la caché
		for(Object objeto : resultados) {
			GestorConexionesBD.borrarCache(objeto);
		}

		return citas;
	}

	public static void insertar(Cita cita) throws SQLException {
		Cita citaNueva;
		
		// Modificamos la base de datos y copiamos el id asignado
		try {
			GestorConexionesBD.iniciarTransaccion();
			citaNueva = (Cita)GestorConexionesBD.insertar(cita.clone());
			cita.setId(citaNueva.getId());
		} finally {
			GestorConexionesBD.terminarTransaccion();
		}
	}

	public static void eliminar(Cita cita) throws SQLException, CitaNoValidaException {
		// Modificamos la base de datos
		try {
			GestorConexionesBD.iniciarTransaccion();
			GestorConexionesBD.eliminar(consultar(cita.getFechaYHora(), cita.getDuracion(), cita.getMedico().getNif(), cita.getBeneficiario().getNif()));
		} finally {
			GestorConexionesBD.terminarTransaccion();
		}
	}

}
