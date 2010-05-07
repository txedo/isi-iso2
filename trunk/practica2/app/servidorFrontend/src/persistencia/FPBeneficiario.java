package persistencia;

import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

import comunicaciones.GestorConexionesBD;

import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.Direccion;
import excepciones.BeneficiarioInexistenteException;

/**
 * Clase que permtie consultar, insertar, modificar y eliminar
 * beneficiarios de la base de datos utilizando Hibernate.
 */
public class FPBeneficiario {

	private static final String CLASE_BENEFICIARIO = "Beneficiario";
	
	private static final String ATRIB_NIF = "nif";
	private static final String ATRIB_NSS = "nss";

	public static Beneficiario consultarPorNIF(String nif) throws SQLException, BeneficiarioInexistenteException {
		ConsultaHibernate consulta;
		List<?> resultados;
		Beneficiario beneficiario;
		
		// Consultamos la base de datos
		consulta = new ConsultaHibernate("FROM " + CLASE_BENEFICIARIO + " WHERE "
				 + ATRIB_NIF + " = ?", nif);
		resultados = GestorConexionesBD.consultar(consulta);
		
		// Si no se obtienen datos, es porque el beneficiario no existe
		if(resultados.size() == 0) {
			throw new BeneficiarioInexistenteException("El beneficiario con NIF " + nif + " no se encuentra dado de alta en el sistema.");
		} else {
			// Recuperamos el beneficiario leído
			beneficiario = (Beneficiario)((Beneficiario)resultados.get(0)).clone();
			// Borramos los objetos leídos de la caché
			for(Object objeto : resultados) {
				GestorConexionesBD.borrarCache(objeto);
			}
		}
		
		return beneficiario;
	}
	
	public static Beneficiario consultarPorNSS(String nss) throws SQLException, BeneficiarioInexistenteException {
		ConsultaHibernate consulta;
		List<?> resultados;
		Beneficiario beneficiario;
		
		// Consultamos la base de datos
		consulta = new ConsultaHibernate("FROM " + CLASE_BENEFICIARIO + " WHERE "
				 + ATRIB_NSS + " = ?", nss);
		resultados = GestorConexionesBD.consultar(consulta);
		
		// Si no se obtienen datos, es porque el beneficiario no existe
		if(resultados.size() == 0) {
			throw new BeneficiarioInexistenteException("El beneficiario con NSS " + nss + " no se encuentra dado de alta en el sistema.");
		} else {
			// Recuperamos el beneficiario leído
			beneficiario = (Beneficiario)((Beneficiario)resultados.get(0)).clone();
			// Borramos los objetos leídos de la caché
			for(Object objeto : resultados) {
				GestorConexionesBD.borrarCache(objeto);
			}
		}
		
		return beneficiario;
	}
	
	public static Vector<String> consultarBeneficiariosMedico(String nifMedico) throws SQLException {
		ConsultaHibernate consulta;
		List<?> resultados;
		Vector<String> nifs;
		
		// Consultamos la base de datos
		consulta = new ConsultaHibernate("FROM " + CLASE_BENEFICIARIO + " WHERE "
				 + "medicoAsignado.nif = ?", nifMedico);
		resultados = GestorConexionesBD.consultar(consulta);
		
		// Borramos los objetos leídos de la caché
		for(Object objeto : resultados) {
			GestorConexionesBD.borrarCache(objeto);
		}
		
		// Devolvemos la lista de NIFs de los beneficiarios
		nifs = new Vector<String>();
		for(Object beneficiario : resultados) {
			nifs.add(((Beneficiario)beneficiario).getNif());
		}
		
		return nifs;
	}

	public static void insertar(Beneficiario beneficiario) throws SQLException {
		// Modificamos la base de datos (automáticamente
		// se inserta la dirección del beneficiario)
		try {
			GestorConexionesBD.iniciarTransaccion();
			GestorConexionesBD.insertar(beneficiario.clone());
		} finally {
			GestorConexionesBD.terminarTransaccion();
		}
	}

	public static void modificar(Beneficiario beneficiario) throws SQLException, BeneficiarioInexistenteException {
		ConsultaHibernate consulta;
		List<?> resultados;
		Beneficiario beneficiarioActual;
		
		// Consultamos la base de datos
		consulta = new ConsultaHibernate("FROM " + CLASE_BENEFICIARIO + " WHERE "
				 + ATRIB_NIF + " = ?", beneficiario.getNif());
		resultados = GestorConexionesBD.consultar(consulta);
		
		// Si no se obtienen datos, es porque el beneficiario no existe
		if(resultados.size() == 0) {
			throw new BeneficiarioInexistenteException("El beneficiario con NIF " + beneficiario.getNif() + " no se encuentra dado de alta en el sistema.");
		} else {
			// Recuperamos el beneficiario que hay actualmente en la base de datos
			beneficiarioActual = (Beneficiario)resultados.get(0);
		}
		
		try {
			// Eliminamos a mano los campos que hacen referencia a otras
			// tablas, porque al actualizarlos Hibernate no borra los
			// campos antiguos automáticamente, sino que los mantiene
			GestorConexionesBD.iniciarTransaccion();
			GestorConexionesBD.eliminar(beneficiarioActual.getDireccion());
			// Actualizamos los datos del beneficiario
			beneficiarioActual.setApellidos(beneficiario.getApellidos());
			beneficiarioActual.setCentroSalud(beneficiario.getCentroSalud());
			beneficiarioActual.setCorreo(beneficiario.getCorreo());
			beneficiarioActual.setDireccion((Direccion)beneficiario.getDireccion().clone());
			beneficiarioActual.getDireccion().setId(-1);
			beneficiarioActual.setFechaNacimiento(beneficiario.getFechaNacimiento());
			beneficiarioActual.setMedicoAsignado(beneficiario.getMedicoAsignado());
			beneficiarioActual.setMovil(beneficiario.getMovil());
			beneficiarioActual.setNombre(beneficiario.getNombre());
			beneficiarioActual.setTelefono(beneficiario.getTelefono());
			// Modificamos la base de datos
			GestorConexionesBD.actualizar(beneficiarioActual);
		} finally {
			GestorConexionesBD.terminarTransaccion();
		}
		
		// Borramos los objetos leídos de la caché
		for(Object objeto : resultados) {
			GestorConexionesBD.borrarCache(objeto);
		}
	}

	public static void eliminar(Beneficiario beneficiario) throws SQLException, BeneficiarioInexistenteException {
		// Modificamos la base de datos (automáticamente
		// se elimina la dirección del beneficiario)
		try {
			GestorConexionesBD.iniciarTransaccion();
			GestorConexionesBD.eliminar(consultarPorNIF(beneficiario.getNif()));
		} finally {
			GestorConexionesBD.terminarTransaccion();
		}
	}
	
}
