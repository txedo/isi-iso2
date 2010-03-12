package comunicaciones;

import java.sql.ResultSet;
import java.sql.SQLException;
import persistencia.AgenteRespaldo;
import persistencia.ComandoSQL;

/**
 * Clase intermedia para conectarse con el agente de la base de datos
 * secundaria.
 */
public class ConexionBDRespaldo implements IConexionBD {
	
	private AgenteRespaldo agente;
	
	public ConexionBDRespaldo() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		agente = AgenteRespaldo.getAgente();
	}
	
	public AgenteRespaldo getAgente() {
		return agente;
	}
	
	// Métodos del agente
	
	public void abrir() throws SQLException {
		agente.abrir();
	}
	
	public void cerrar() throws SQLException {
		agente.cerrar();
	}
	
	public ResultSet consultar(ComandoSQL comando) throws SQLException {
		return agente.consultar(comando);
	}

	public void ejecutar(ComandoSQL comando) throws SQLException {
		agente.ejecutar(comando);
	}

	public void commit() throws SQLException {
		agente.commit();
	}

	public void rollback() throws SQLException {
		agente.rollback();
	}

}
