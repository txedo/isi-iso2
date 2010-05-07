package pruebas;

import java.util.List;

import junit.framework.TestCase;
import persistencia.ConsultaHibernate;
import persistencia.HibernateSessionFactory;

import comunicaciones.ConexionBDFrontend;
import comunicaciones.GestorConexionesBD;

/**
 * Clase base para las pruebas, que prepara y abre la conexión con la
 * base de datos al iniciar las pruebas y la cierra al finalizar.
 */
public class PruebasBase extends TestCase {
	
	protected void setUp() {
		ConsultaHibernate consulta;
		List<?> datos;
		ConexionBDFrontend conexion;
		boolean iniciado;
		
		try {
			// Ponemos la conexión local con la base de datos
			conexion = new ConexionBDFrontend();
			GestorConexionesBD.ponerConexion(conexion);
			// Cerramos la sesión de Hibernate para que no haya
			// problemas al reutilizar los objetos de las pruebas
			HibernateSessionFactory.closeSession();
			// Borramos la base de datos
			iniciado = false;
			for(String clase : new String[] { "Sustitucion", "Volante", "Cita", "Beneficiario", "Usuario", "CentroSalud", "EntradaLog" }) {
				consulta = new ConsultaHibernate("FROM " + clase);
				datos = GestorConexionesBD.consultar(consulta);
				for(Object objeto : datos) {
					if(!iniciado) {
						GestorConexionesBD.iniciarTransaccion();
						iniciado = true;
					}
					GestorConexionesBD.borrarCache(objeto);
					GestorConexionesBD.eliminar(objeto);
				}
			}
			if(iniciado) {
				GestorConexionesBD.terminarTransaccion();
			}
		} catch(Exception e) {
			e.printStackTrace();
			fail(e.toString());
		}
	}
	
	protected void tearDown() {
		try {
			// Vaciamos la lista de conexiones
			GestorConexionesBD.quitarConexiones();
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
}
