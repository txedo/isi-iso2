package comunicaciones;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;

import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.Cita;
import dominio.conocimiento.ISesion;
import dominio.conocimiento.Medico;
import excepciones.BeneficiarioInexistenteException;
import excepciones.BeneficiarioYaExistenteException;
import excepciones.CitaNoValidaException;
import excepciones.FechaNoValidaException;
import excepciones.MedicoInexistenteException;
import excepciones.MedicoYaExistenteException;
import excepciones.SesionInvalidaException;
import excepciones.SesionNoIniciadaException;
import excepciones.UsuarioIncorrectoException;
import excepciones.VolanteNoValidoException;

/**
 * Proxy utilizado para conectarse con el servidor frontend.
 */
public class ProxyServidorFrontend implements IServidorFrontend {
	
	private IServidorFrontend servidor;
	
	public void conectar(String ip) throws MalformedURLException, RemoteException, NotBoundException {
		String url;
		
		url = "rmi://" + ip + ":" + String.valueOf(PUERTO_SERVIDOR) + "/" + NOMBRE_SERVIDOR;
        servidor = (IServidorFrontend)Naming.lookup(url);
	}
	
	public ISesion identificar(String login, String password) throws RemoteException, SQLException, UsuarioIncorrectoException, Exception {
		return servidor.identificar(login, password);
	}

	public Medico getMedico(long idSesion, String dni) throws RemoteException, MedicoInexistenteException, Exception {
		return servidor.getMedico(idSesion, dni);
	}

	public void crear(long idSesion, Medico medico) throws RemoteException, MedicoYaExistenteException, SQLException, Exception {
		servidor.crear(idSesion, medico);
	}

	public void eliminar(long idSesion, Medico medico) throws RemoteException, MedicoInexistenteException, SQLException, Exception {
		servidor.eliminar(idSesion, medico);
	}

	public void modificar(long idSesion, Medico medico) throws RemoteException, MedicoInexistenteException, SQLException, Exception {
		servidor.modificar(idSesion, medico);
	}

	public Beneficiario getBeneficiario(long idSesion, String nif) throws RemoteException, SQLException, BeneficiarioInexistenteException, Exception {
		return servidor.getBeneficiario(idSesion, nif);
	}

	public Beneficiario getBeneficiarioPorNSS(long idSesion, String nss) throws RemoteException, SQLException, BeneficiarioInexistenteException, Exception {
		return servidor.getBeneficiarioPorNSS(idSesion, nss);
	}
	
	public void liberar(long idSesion) throws RemoteException, Exception {
		servidor.liberar(idSesion);
	}

	public void registrar(ICliente cliente, long idSesion) throws RemoteException, SesionNoIniciadaException, Exception {
		servidor.registrar(cliente, idSesion);
	}

	public void crear(long idSesion, Beneficiario bene) throws RemoteException, SQLException, BeneficiarioYaExistenteException, Exception {
		servidor.crear(idSesion, bene);
		
	}

	public void modificar(long idSesion, Beneficiario bene) throws RemoteException, SQLException, BeneficiarioInexistenteException, Exception {
		servidor.modificar(idSesion, bene);
	}

	public Object mensajeAuxiliar(long idSesion, long codigoMensaje, Object informacion) throws RemoteException, SesionInvalidaException {
		return servidor.mensajeAuxiliar(idSesion, codigoMensaje, informacion);
	}

	@Override
	public void anularCita(long arg0, Cita arg1) throws RemoteException,
			CitaNoValidaException, SQLException, Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Vector<Cita> getCitas(long arg0, String arg1)
			throws RemoteException, BeneficiarioInexistenteException,
			SQLException, Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cita pedirCita(long arg0, Beneficiario arg1, String arg2, Date arg3,
			long arg4) throws RemoteException,
			BeneficiarioInexistenteException, MedicoInexistenteException,
			FechaNoValidaException, SQLException, Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cita pedirCita(long arg0, Beneficiario arg1, long arg2, Date arg3,
			long arg4) throws RemoteException,
			BeneficiarioInexistenteException, MedicoInexistenteException,
			FechaNoValidaException, VolanteNoValidoException, SQLException,
			Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long emitirVolante(long arg0, Beneficiario arg1, Medico arg2,
			Medico arg3) throws RemoteException,
			BeneficiarioInexistenteException, MedicoInexistenteException,
			SQLException, Exception {
		// TODO Auto-generated method stub
		return 0;
	}
		
}
