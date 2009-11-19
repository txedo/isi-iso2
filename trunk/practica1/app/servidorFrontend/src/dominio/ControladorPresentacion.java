package dominio;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;

import persistencia.GestorConexiones;
import persistencia.IConexion;
import presentacion.JFServidorFrontend;

import comunicaciones.ProxyAgenteRemoto;
import comunicaciones.ServidorFrontend;

public class ControladorPresentacion {
	
	private ServidorFrontend servidor;
	private ObservadorPresentacion observador;
	
	public ControladorPresentacion() {
		observador = new ObservadorPresentacion();
		try {
			servidor = new ServidorFrontend(this);
		} catch (RemoteException e) {
			observador.actualizarVentanas(e.getMessage());
			System.out.println(e);
		}
	}
	
	public void iniciar() {
		try {
			
			this.iniciarConexiones();
			this.iniciarGUI();
		} catch (SQLException e) {
			observador.actualizarVentanas(e.getMessage());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void iniciarGUI () {
		JFServidorFrontend inst = new JFServidorFrontend();
		inst.setControladorPresentacion(this);
		inst.setVisible(true);
		observador.add(inst);
	}
	
	private void iniciarConexiones () throws SQLException, MalformedURLException, RemoteException, NotBoundException{
		IConexion conexion = (IConexion)persistencia.AgenteFrontend.getAgente();
		ProxyAgenteRemoto proxy = new ProxyAgenteRemoto();
		proxy.conectar("127.0.0.1");
		GestorConexiones.ponerConexion(conexion);
		GestorConexiones.ponerConexion(proxy);
		
	}

	public ObservadorPresentacion getObservador() {
		return observador;
	}

	public ServidorFrontend getServidor() {
		return servidor;
	}

}
