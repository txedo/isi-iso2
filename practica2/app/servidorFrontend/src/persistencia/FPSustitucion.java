package persistencia;

import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

import comunicaciones.GestorConexionesBD;

import dominio.conocimiento.Roles;
import dominio.conocimiento.Sustitucion;
import dominio.conocimiento.Usuario;
import excepciones.CentroSaludInexistenteException;
import excepciones.DireccionInexistenteException;
import excepciones.UsuarioIncorrectoException;

/**
 * Clase que permite consultar e insertar sustituciones de m�dicos en la
 * base de datos utilizando Hibernate.
 */
public class FPSustitucion {

	private static final String CLASE_SUSTITUCION = "Sustitucion";
	
	private static final String ATRIB_NIF_MEDICO = "medico.nif";
	private static final String ATRIB_NIF_SUSTITUTO = "sustituto.nif";
	
	public static Vector<Sustitucion> consultarPorSustituido(String nifMedico) throws SQLException, UsuarioIncorrectoException, CentroSaludInexistenteException, DireccionInexistenteException {
		ConsultaHibernate consulta;
		List<?> resultados;
		Vector<Sustitucion> sustituciones;
		Usuario usuario;
		
		// Comprobamos que el m�dico exista
		usuario = FPUsuario.consultar(nifMedico);
		if(usuario.getRol() != Roles.M�dico) {
			throw new UsuarioIncorrectoException("No se pueden consultar las sustituciones del usuario con NIF " + String.valueOf(nifMedico) + " porque no es un m�dico.");
		}

		// Consultamos la base de datos
		consulta = new ConsultaHibernate("FROM " + CLASE_SUSTITUCION + " WHERE "
				 + ATRIB_NIF_MEDICO + " = ?", nifMedico);
		resultados = GestorConexionesBD.consultar(consulta);
				
		// Devolvemos la lista de sustituciones del m�dico
		sustituciones = new Vector<Sustitucion>();
		for(Object sustitucion : resultados) {
			sustituciones.add((Sustitucion)((Sustitucion)sustitucion).clone());
		}
		
		// Borramos los objetos le�dos de la cach�
		for(Object objeto : resultados) {
			GestorConexionesBD.borrarCache(objeto);
		}

		return sustituciones;
	}
	
	public static Vector<Sustitucion> consultarPorSustituto(String nifMedico) throws SQLException, UsuarioIncorrectoException, CentroSaludInexistenteException, DireccionInexistenteException {
		ConsultaHibernate consulta;
		List<?> resultados;
		Vector<Sustitucion> sustituciones;
		Usuario usuario;
		
		// Comprobamos que el m�dico exista
		usuario = FPUsuario.consultar(nifMedico);
		if(usuario.getRol() != Roles.M�dico) {
			throw new UsuarioIncorrectoException("No se pueden consultar las sustituciones hechas por el usuario con NIF " + String.valueOf(nifMedico) + " porque no es un m�dico.");
		}

		// Consultamos la base de datos
		consulta = new ConsultaHibernate("FROM " + CLASE_SUSTITUCION + " WHERE "
				 + ATRIB_NIF_SUSTITUTO + " = ?", nifMedico);
		resultados = GestorConexionesBD.consultar(consulta);
				
		// Devolvemos la lista de sustituciones del m�dico
		sustituciones = new Vector<Sustitucion>();
		for(Object sustitucion : resultados) {
			sustituciones.add((Sustitucion)((Sustitucion)sustitucion).clone());
		}
		
		// Borramos los objetos le�dos de la cach�
		for(Object objeto : resultados) {
			GestorConexionesBD.borrarCache(objeto);
		}

		return sustituciones;
	}
	
	public static void insertar(Sustitucion sustitucion) throws SQLException {
		Sustitucion sustitucionNueva;
		
		// Modificamos la base de datos y copiamos el id asignado
		try {
			GestorConexionesBD.iniciarTransaccion();
			sustitucionNueva = (Sustitucion)GestorConexionesBD.insertar(sustitucion.clone());
			sustitucion.setId(sustitucionNueva.getId());
		} finally {
			GestorConexionesBD.terminarTransaccion();
		}
	}
	
}
