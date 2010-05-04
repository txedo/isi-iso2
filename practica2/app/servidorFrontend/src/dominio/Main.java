package dominio;

import java.sql.SQLException;
import java.util.List;

import persistencia.ConsultaHibernate;
import persistencia.FPCentroSalud;

import comunicaciones.ConfiguracionFrontend;
import comunicaciones.GestorConexionesBD;

import dominio.conocimiento.CentroSalud;
import dominio.conocimiento.EntradaLog;
import dominio.conocimiento.Usuario;
import dominio.control.ControladorFrontend;

/**
 * Clase principal del servidor front-end.
 */
public class Main {
	
	public static void main(String[] args) {
		ControladorFrontend cont;
		
		// Mostramos la única ventana del servidor
		cont = new ControladorFrontend();
		cont.mostrarVentana();
		/*try {
			cont.iniciarServidor(new ConfiguracionFrontend());
//			CentroSalud c = FPCentroSalud.consultar(11);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
/*
//		SessionFactory f = new Configuration().configure().buildSessionFactory();
//		
//		Session s = f.getCurrentSession();
		Session s = HibernateSessionFactory.getSession();
		//Session s = f.openSession();
		s.beginTransaction();
		CentroSalud c;
		c = new CentroSalud();
		c.setNombre("ABC");
		c.setDireccion("DEF");
		s.save(c);
		s.getTransaction().commit();
		*/
		/*ConsultaHibernate c;
		c = new ConsultaHibernate("FROM EntradaLog");
		try {
			List<?> l = GestorConexionesBD.consultarHibernate(c);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	
}
