package dominio;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import comunicaciones.IServidorFrontend;
import dominio.Controlador;
import excepciones.UsuarioIncorrectoException;

public class Main {

	public static void main (String args[]) throws SQLException, UsuarioIncorrectoException, MalformedURLException, RemoteException, NotBoundException, Exception {
//		IServidorFrontend servidorFE;
	
//		servidorFE = null;
		
		Controlador c;
		c = new Controlador();
//		c.setServidor(servidorFE);
		ISesion s = c.identificarse();
		
		JOptionPane.showMessageDialog(null, "Sesión iniciada correctamente:\nId: " + String.valueOf(s.getId()) + "\nRol: " + String.valueOf(s.getRol()));
	}
	
}
