package dominio;

import java.rmi.RemoteException;
import java.sql.SQLException;

import persistencia.AgenteRemoto;
import presentacion.JFServidorRespaldo;

public class Main {
	
	public static void main(String[] args) {
		JFServidorRespaldo r = new JFServidorRespaldo();
		AgenteRemoto a;
		
		try {
			a = AgenteRemoto.getAgente();
			r.setAgente(a);
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
