package comunicaciones;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;

import dominio.ISesion;

import excepciones.UsuarioIncorrectoException;

public class Proxy {

		public static void main (String args[]) throws SQLException, UsuarioIncorrectoException, MalformedURLException, RemoteException, NotBoundException, Exception {
			IServidorFrontend servidorFE = (IServidorFrontend)Naming.lookup("rmi://127.0.0.1:2995/servidorfrontend");
			ISesion prueba = (ISesion)servidorFE.identificar("txedo", "asdf");
			System.out.println(prueba.getId());
			System.out.println(prueba.getRol());
		}
}
