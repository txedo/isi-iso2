package persistencia;

import java.sql.SQLException;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import comunicaciones.GestorConexionesBD;

import dominio.conocimiento.CategoriasMedico;
import dominio.conocimiento.CentroSalud;
import dominio.conocimiento.Medico;
import dominio.conocimiento.Usuario;

/**
 * Clase con métodos estáticos auxiliares para acceder a la base de datos que
 * relacionan varias tablas.
 */
public class UtilidadesPersistencia {

	private static final String CLASE_USUARIO = "Usuario";
	private static final String CLASE_BENEFICIARIO = "Beneficiario";

	private static final String ATRIB_USUARIO_NIF = "nif";
	private static final String ATRIB_BENEFICIARIO_NIF = "nif";
	private static final String ATRIB_CENTRO_USUARIO = "centroSalud.id";
	
	public static boolean existeNIF(String nif) throws SQLException {
		ConsultaHibernate consulta;
		List<?> resultados;
		boolean existe;
		
		// Comprobamos si hay algún beneficiario con el NIF indicado
		consulta = new ConsultaHibernate("FROM " + CLASE_BENEFICIARIO + " WHERE "
				 + ATRIB_BENEFICIARIO_NIF + " = ?", nif);
		resultados = GestorConexionesBD.consultar(consulta);
		if(resultados.size() > 0) {
			existe = true;
		} else {
			// Comprobamos si hay algún usuario con el NIF indicado
			consulta = new ConsultaHibernate("FROM " + CLASE_USUARIO + " WHERE "
					 + ATRIB_USUARIO_NIF + " = ?", nif);
			resultados = GestorConexionesBD.consultar(consulta);
			if(resultados.size() > 0) {
				existe = true;
			} else {
				existe = false;
			}
		}
		
		return existe;
	}
	
	public static String obtenerMedicoAleatorioTipoCentro(CategoriasMedico tipoMedico, CentroSalud centro) throws SQLException {
		Vector<String> nifsMedicos;
		ConsultaHibernate consulta;
		List<?> resultados;
		Random rnd;
		String nif;
		
		// Obtenemos los médicos del centro indicado
		consulta = new ConsultaHibernate("FROM " + CLASE_USUARIO + " WHERE "
				 + ATRIB_CENTRO_USUARIO + " = ?", centro.getId());
		resultados = GestorConexionesBD.consultar(consulta);
		
		// Creamos una lista con los médicos leídos del tipo pasado como parámetro
		nifsMedicos = new Vector<String>();
		for(Object medico : resultados) {
			if(medico instanceof Medico && ((Medico)medico).getTipoMedico().getCategoria() == tipoMedico) {
				nifsMedicos.add(((Usuario)medico).getNif());
			}
		}
		
		// Devolvemos un NIF aleatorio, o una cadena vacía si no hay ninguno
		if(nifsMedicos.size() == 0) {
			nif = "";
		} else {
			rnd = new Random(System.currentTimeMillis());
			nif = nifsMedicos.get(rnd.nextInt(nifsMedicos.size()));
		}
		
		return nif;
	}
	
}
