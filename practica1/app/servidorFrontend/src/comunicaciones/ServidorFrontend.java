package comunicaciones;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import presentacion.JFServidorFrontend;

public class ServidorFrontend extends UnicastRemoteObject {
	protected ServidorFrontend() throws RemoteException {
		super();
		LocateRegistry.createRegistry(2995);
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws RemoteException {
		JFServidorFrontend inst = new JFServidorFrontend(new ServidorFrontend());
		inst.setVisible(true);
	}
	
    public void conectar() throws MalformedURLException, RemoteException {
        try {            
            Naming.bind("rmi://127.0.0.1:2995/servidorfrontend", this);
        }
        catch (AlreadyBoundException ex) {
            Naming.rebind("rmi://127.0.0.1:2995/servidorfrontend", this);
        }
    }
    
    public void desconectar() throws RemoteException, MalformedURLException, NotBoundException {
        Naming.unbind("rmi://127.0.0.1:2995/servidorfrontend");
    }
}
