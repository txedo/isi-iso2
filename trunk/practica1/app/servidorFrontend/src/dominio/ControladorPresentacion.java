package dominio;

import java.rmi.RemoteException;
import java.sql.SQLException;

import persistencia.GestorConexiones;
import persistencia.IConexion;
import presentacion.JFServidorFrontend;

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
		}
	}
	
	public void iniciar() {
		try {
			this.iniciarConexiones();
			this.iniciarGUI();
		} catch (SQLException e) {
			observador.actualizarVentanas(e.getMessage());
		}
	}
	
	private void iniciarGUI () {
		JFServidorFrontend inst = new JFServidorFrontend();
		inst.setControladorPresentacion(this);
		inst.setVisible(true);
		observador.add(inst);
	}
	
	private void iniciarConexiones () throws SQLException{
		IConexion conexion = (IConexion)persistencia.AgenteLocal.getAgente();
		GestorConexiones.ponerConexion(conexion);
	}

	public ObservadorPresentacion getObservador() {
		return observador;
	}

	public ServidorFrontend getServidor() {
		return servidor;
	}

}
