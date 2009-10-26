package comunicaciones;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

public class ServidorFrontendFachada extends UnicastRemoteObject implements IServidorFrontend {

	public ServidorFrontendFachada() throws RemoteException {
		super();
	}
	
	public ISesion identificar(String login, String password) throws RemoteException {
		return GestorSesiones.identificar(login, password);
	}
	
	public Beneficiario getBeneficiario(long idSesion, String dni) throws RemoteException {
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
	}
	
}
