package dominio;

import java.rmi.RemoteException;
import java.sql.SQLException;

import comunicaciones.ConexionBDRespaldo;

import persistencia.AgenteRespaldo;
import presentacion.JFServidorRespaldo;

public class Main {
	
	public static void main(String[] args) {
		JFServidorRespaldo r = new JFServidorRespaldo();
		ConexionBDRespaldo c ;
		
		try {
			c = new ConexionBDRespaldo();
			r.setConexion(c);
			r.setVisible(true);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

}
