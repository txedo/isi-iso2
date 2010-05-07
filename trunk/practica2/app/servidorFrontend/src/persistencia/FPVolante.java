package persistencia;

import java.sql.SQLException;
import java.util.List;

import comunicaciones.GestorConexionesBD;

import dominio.conocimiento.Volante;
import excepciones.VolanteNoValidoException;

/**
 * Clase que permite consultar, insertar y modificar volantes de la
 * base de datos utilizando Hibernate.
 */
public class FPVolante {

	private static final String CLASE_VOLANTE = "Volante";
	
	private static final String ATRIB_ID = "id";
	
	public static Volante consultar(long id) throws SQLException, VolanteNoValidoException {
		ConsultaHibernate consulta;
		List<?> resultados;
		Volante volante;
		
		// Consultamos la base de datos
		consulta = new ConsultaHibernate("FROM " + CLASE_VOLANTE + " WHERE "
				 + ATRIB_ID + " = ?", id);
		resultados = GestorConexionesBD.consultar(consulta);
		
		// Si no se obtienen datos, es porque el volante no existe
		if(resultados.size() == 0) {
			throw new VolanteNoValidoException("No existe ningún volante con el id " + String.valueOf(id) + ".");
		} else {
			// Recuperamos el volante leído
			volante = (Volante)((Volante)resultados.get(0)).clone();
			// Borramos los objetos leídos de la caché
			for(Object objeto : resultados) {
				GestorConexionesBD.borrarCache(objeto);
			}
		}
		
		return volante;
	}
		
	public static void insertar(Volante volante) throws SQLException {
		Volante volanteNuevo;
		
		// Modificamos la base de datos y copiamos el id asignado
		try {
			GestorConexionesBD.iniciarTransaccion();
			volanteNuevo = (Volante)GestorConexionesBD.insertar(volante.clone());
			volante.setId(volanteNuevo.getId());
		} finally {
			GestorConexionesBD.terminarTransaccion();
		}
	}

	public static void modificar(Volante volante) throws SQLException, VolanteNoValidoException {
		ConsultaHibernate consulta;
		List<?> resultados;
		Volante volanteActual;
		
		// Consultamos la base de datos
		consulta = new ConsultaHibernate("FROM " + CLASE_VOLANTE + " WHERE "
				 + ATRIB_ID + " = ?", volante.getId());
		resultados = GestorConexionesBD.consultar(consulta);
		
		// Si no se obtienen datos, es porque el volante no existe
		if(resultados.size() == 0) {
			throw new VolanteNoValidoException("No existe ningún volante con el id " + String.valueOf(volante.getId()) + ".");
		} else {
			// Recuperamos el volante que hay actualmente en la base de datos
			volanteActual = (Volante)resultados.get(0);
		}
		
		try {
			GestorConexionesBD.iniciarTransaccion();
			// Actualizamos los datos del volante
			volanteActual.setBeneficiario(volante.getBeneficiario());
			volanteActual.setCita(volante.getCita());
			volanteActual.setEmisor(volante.getEmisor());
			volanteActual.setReceptor(volante.getReceptor());
			volanteActual.setFechaCaducidad(volante.getFechaCaducidad());
			volanteActual.setId(volante.getId());
			// Modificamos la base de datos
			GestorConexionesBD.actualizar(volanteActual);
		} finally {
			GestorConexionesBD.terminarTransaccion();
		}
		
		// Borramos los objetos leídos de la caché
		for(Object objeto : resultados) {
			GestorConexionesBD.borrarCache(objeto);
		}
	}
	
}
