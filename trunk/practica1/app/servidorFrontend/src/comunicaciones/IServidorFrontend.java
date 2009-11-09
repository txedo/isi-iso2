package comunicaciones;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Vector;
import dominio.ISesion;
import excepciones.UsuarioIncorrectoException;

public interface IServidorFrontend extends Remote {

	public ISesion identificar(String login, String password) throws RemoteException, SQLException, UsuarioIncorrectoException, Exception;
	
/*	public Beneficiario getBeneficiario(long idSesion, String dni) throws RemoteException;
	
	public Beneficiario getBeneficiarioPorNSS(long idSesion, String nss) throws RemoteException;
	
	public Medico getMedico(long idSesion, String dni) throws RemoteException;
	
	public Cita pedirCita(long idSesion, Beneficiario beneficiario, String idMedico, Date fechaYHora, long duracion) throws RemoteException;
	
	public Cita pedirCita(long idSesion, Beneficiario beneficiario, long idVolante, Date fechaYHora, long duracion) throws RemoteException;
	
	public Vector<Cita> getCitas(long idSesion, String dni) throws RemoteException;

	public void anularCita(long idSesion, Cita cita) throws RemoteException;
	
	public void crear(long idSesion, Beneficiario beneficiario) throws RemoteException;
	
	public void modificar(long idSesion, Beneficiario beneficiario) throws RemoteException;
	
	public void crear(long idSesion, Medico medico) throws RemoteException;

	public void modificar(long idSesion, Medico medico) throws RemoteException;
	
	public void eliminar(long idSesion, Medico medico) throws RemoteException;
	
	public void modificarCalendario(long idSesion, Medico medico, Vector<Date> dias, Date horaDesde, Date horaHasta, IMedico sustituto) throws RemoteException;
	
	public Object mensajeAuxiliar(long idSesion, long codigoMensaje, Object informacion) throws RemoteException;
	
	public long emitirVolante(long idSesion, Beneficiario beneficiario, Medico emisor, Medico destino) throws RemoteException; */
	
}
