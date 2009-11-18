package comunicaciones;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;

import dominio.ControladorPresentacion;
import dominio.GestorSesiones;
import dominio.ISesion;
import excepciones.UsuarioIncorrectoException;



public class ServidorFrontend extends UnicastRemoteObject implements IServidorFrontend {
	private ControladorPresentacion controlador;
	
	public ServidorFrontend(ControladorPresentacion c) throws RemoteException {
		super();
		LocateRegistry.createRegistry(2995);
		controlador = c;
	}
	
    public void conectar() throws MalformedURLException, RemoteException {
    	controlador.getObservador().actualizarVentanas("Servidor iniciado.");
        try {            
            Naming.bind("rmi://127.0.0.1:2995/servidorfrontend", this);
        }
        catch (AlreadyBoundException ex) {
            Naming.rebind("rmi://127.0.0.1:2995/servidorfrontend", this);
        }
    }
    
    public void desconectar() throws RemoteException, MalformedURLException, NotBoundException {
    	controlador.getObservador().actualizarVentanas("Servidor detenido.");
        Naming.unbind("rmi://127.0.0.1:2995/servidorfrontend");
    }
	
    //TODO quitar la excepcion generica Exception
	public ISesion identificar(String login, String password) throws RemoteException, SQLException, UsuarioIncorrectoException, Exception {
		try {
			ISesion s = GestorSesiones.identificar(login, password);
			controlador.getObservador().actualizarVentanas("Usuario '" + login + "' autenticado.");
			return s;
		}
		catch (RemoteException re) {
			controlador.getObservador().actualizarVentanas(re.getMessage());
			throw re;
		}
		catch (SQLException se) {
			controlador.getObservador().actualizarVentanas(se.getMessage());
			throw se;
		}
		catch (UsuarioIncorrectoException uie) {
			controlador.getObservador().actualizarVentanas(uie.getMessage());
			throw uie;
		}
		catch (Exception e) {
			controlador.getObservador().actualizarVentanas(e.getMessage());
			throw e;
		}
	}
	
	/*	public Beneficiario getBeneficiario(long idSesion, String dni) throws RemoteException {
	return GestorBeneficiarios.getBeneficiario(idSesion, dni);
	}
	
	public Beneficiario getBeneficiarioPorNSS(long idSesion, String nss) throws RemoteException {
		return GestorBeneficiarios.getBeneficiarioPorNSS(idSesion, nss);
	}
	
	public Medico getMedico(long idSesion, String dni) throws RemoteException {
		return GestorMedicos.getMedico(idSesion, dni);
	}
	
	public Cita pedirCita(long idSesion, Beneficiario beneficiario, String idMedico, Date fechaYHora, long duracion) throws RemoteException {
		return GestorCitas.pedirCita(idSesion, beneficiario, idMedico, fechaYHora, duracion);
	}
	
	public Cita pedirCita(long idSesion, Beneficiario beneficiario, long idVolante, Date fechaYHora, long duracion) throws RemoteException {
		return GestorCitas.pedirCita(idSesion, beneficiario, idVolante, fechaYHora, duracion);
	}
	
	public Vector<Cita> getCitas(long idSesion, String dni) throws RemoteException {
		return GestorCitas.getCitas(idSesion, dni);
	}
	
	public void anularCita(long idSesion, Cita cita) throws RemoteException {
		GestorCitas.anularCita(idSesion, cita);
	}
	
	public void crear(long idSesion, Beneficiario beneficiario) throws RemoteException {
		GestorBeneficiarios.crear(idSesion, beneficiario);
	}
	
	public void modificar(long idSesion, Beneficiario beneficiario) throws RemoteException {
		GestorBeneficiarios.modificar(idSesion, beneficiario);
	}
	
	public void crear(long idSesion, Medico medico) throws RemoteException {
		GestorMedicos.crear(idSesion, medico);
	}
	
	public void modificar(long idSesion, Medico medico) throws RemoteException {
		GestorMedicos.modificar(idSesion, medico);
	}
	
	public void eliminar(long idSesion, Medico medico) throws RemoteException {
		GestorMedicos.eliminar(idSesion, medico);
	}
	
	public void modificarCalendario(long idSesion, Medico medico, Vector<Date> dias, Date horaDesde, Date horaHasta, IMedico sustituto) throws RemoteException {
		GestorMedicos.modificarCalendario(idSesion, medico, dias, horaDesde, horaHasta, sustituto);
	}
	
	public Object mensajeAuxiliar(long idSesion, long codigoMensaje, Object informacion) throws RemoteException {
		return GestorMensajes.mensajeAuxiliar(idSesion, codigoMensaje, informacion);
	}
	
	public long emitirVolante(long idSesion, Beneficiario beneficiario, Medico emisor, Medico destino) throws RemoteException {
		return GestorMedicos.emitirVolante(idSesion, beneficiario, emisor, destino);
	} */
}
