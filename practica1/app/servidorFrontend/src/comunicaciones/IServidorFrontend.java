package comunicaciones;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

import dominio.Beneficiario;
import dominio.ISesion;
import excepciones.BeneficiarioInexistenteException;
import excepciones.BeneficiarioYaExistenteException;
import excepciones.SesionInvalidaException;
import excepciones.SesionNoIniciadaException;
import dominio.Medico;
import excepciones.MedicoInexistenteException;
import excepciones.MedicoYaExistenteException;
import excepciones.UsuarioIncorrectoException;

public interface IServidorFrontend extends Remote {

	public final String NOMBRE_SERVIDOR = "servidorfrontend";
	public final int PUERTO_SERVIDOR = 2995;
	
	public ISesion identificar(String login, String password) throws RemoteException, SQLException, UsuarioIncorrectoException, Exception;
	
    public void registrar (ICliente cliente, long idSesion) throws RemoteException, SesionNoIniciadaException, Exception;	
	
	public void liberar (long idSesion) throws RemoteException, Exception;
	
	public Beneficiario getBeneficiario(long idSesion, String dni) throws RemoteException, SQLException, BeneficiarioInexistenteException, Exception;

	public Beneficiario getBeneficiarioPorNSS(long idSesion, String nss) throws RemoteException, SQLException, BeneficiarioInexistenteException, Exception;
	
	public void crear(long idSesion, Beneficiario beneficiario) throws RemoteException, SQLException, BeneficiarioYaExistenteException, Exception;
	
	public void modificar(long idSesion, Beneficiario beneficiario) throws RemoteException, SQLException, BeneficiarioInexistenteException, Exception;
	
    public Medico getMedico(long idSesion, String dni) throws RemoteException, MedicoInexistenteException, Exception;
	
	/*public Cita pedirCita(long idSesion, Beneficiario beneficiario, String idMedico, Date fechaYHora, long duracion) throws RemoteException;
	
	public Cita pedirCita(long idSesion, Beneficiario beneficiario, long idVolante, Date fechaYHora, long duracion) throws RemoteException;
	
	public Vector<Cita> getCitas(long idSesion, String dni) throws RemoteException;

	public void anularCita(long idSesion, Cita cita) throws RemoteException;
	
	*/
	public void crear(long idSesion, Medico medico) throws RemoteException, MedicoYaExistenteException, SQLException, Exception;

	public void modificar(long idSesion, Medico medico) throws RemoteException, MedicoInexistenteException, SQLException, Exception;
	
	public void eliminar(long idSesion, Medico medico) throws RemoteException, MedicoInexistenteException, SQLException, Exception;

/*	public void modificarCalendario(long idSesion, Medico medico, Vector<Date> dias, Date horaDesde, Date horaHasta, IMedico sustituto) throws RemoteException;
*/
	public Object mensajeAuxiliar(long idSesion, long codigoMensaje, Object informacion) throws RemoteException, SesionInvalidaException;
/*	
	public long emitirVolante(long idSesion, Beneficiario beneficiario, Medico emisor, Medico destino) throws RemoteException; */
	
}
