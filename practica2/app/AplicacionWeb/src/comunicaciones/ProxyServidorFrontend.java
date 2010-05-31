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
import dominio.conocimiento.ICodigosMensajeAuxiliar;
import dominio.conocimiento.IMedico;
import dominio.conocimiento.ISesion;
import dominio.conocimiento.Medico;
import dominio.conocimiento.Volante;
import excepciones.BeneficiarioInexistenteException;
import excepciones.BeneficiarioYaExistenteException;
import excepciones.CitaNoValidaException;
import excepciones.FechaNoValidaException;
import excepciones.MedicoInexistenteException;
import excepciones.MedicoYaExistenteException;
import excepciones.SesionNoIniciadaException;
import excepciones.UsuarioIncorrectoException;
import excepciones.VolanteNoValidoException;

/**
 * Proxy singleton utilizado para conectarse con el servidor front-end.
 */
public class ProxyServidorFrontend implements IServidorFrontend {
	
	private IServidorFrontend servidor;
	private static ProxyServidorFrontend instancia;
	
	public static ProxyServidorFrontend getProxy() throws RemoteException {
		if(instancia == null) {
			instancia = new ProxyServidorFrontend();
		}
		return instancia;
	}
	
	public void conectar(String ip, int puerto) throws MalformedURLException, RemoteException, NotBoundException {
		String url;
		if(servidor == null) {
			try {
				url = "rmi://" + ip + ":" + String.valueOf(puerto) + "/" + NOMBRE_SERVIDOR;
				servidor = (IServidorFrontend)Naming.lookup(url);
			} catch(RemoteException ex) {
				throw new RemoteException("No se puede conectar con el servidor front-end.");
			} catch(NotBoundException ex) {
				throw new NotBoundException("El servidor front-end está desactivado.");
			}
		}
	}
	
	// Métodos de gestión de sesiones
	
	public ISesion identificarUsuario(String login, String password) throws RemoteException, SQLException, UsuarioIncorrectoException, Exception {
		return servidor.identificarUsuario(login, password);
	}
	
	// En este caso, el idSesion no se utiliza, pues se va a identificar el beneficiario, por lo que aún no tiene idSesion asignado
	public ISesion identificarBeneficiario(String nss) throws RemoteException, Exception {
		return (ISesion) servidor.mensajeAuxiliar(-1, ICodigosMensajeAuxiliar.IDENTIFICAR_BENEFICIARIO, nss);
	}

	public void registrar(ICliente cliente, long idSesion) throws RemoteException, SesionNoIniciadaException, Exception {
		servidor.registrar(cliente, idSesion);
	}
	
	public void liberar(long idSesion) throws RemoteException, Exception {
		servidor.liberar(idSesion);
	}
	
	// Métodos de gestión de beneficiarios
	
	public Beneficiario getBeneficiario(long idSesion, String nif) throws RemoteException, SQLException, BeneficiarioInexistenteException, Exception {
		return servidor.getBeneficiario(idSesion, nif);
	}

	public Beneficiario getBeneficiarioPorNSS(long idSesion, String nss) throws RemoteException, SQLException, BeneficiarioInexistenteException, Exception {
		return servidor.getBeneficiarioPorNSS(idSesion, nss);
	}

	public void crear(long idSesion, Beneficiario bene) throws RemoteException, SQLException, BeneficiarioYaExistenteException, Exception {
		servidor.crear(idSesion, bene);
	}

	public void modificar(long idSesion, Beneficiario bene) throws RemoteException, SQLException, BeneficiarioInexistenteException, Exception {
		servidor.modificar(idSesion, bene);
	}
	
	// Métodos de gestión de médicos
	
	public Medico getMedicoPorLogin(long idSesion, String login) throws RemoteException, Exception {
		return (Medico) servidor.mensajeAuxiliar(idSesion, ICodigosMensajeAuxiliar.CONSULTAR_MEDICO_POR_LOGIN,login);
	}
	
	public Medico getMedico(long idSesion, String dni) throws RemoteException, MedicoInexistenteException, Exception {
		return servidor.getMedico(idSesion, dni);
	}

	public void crear(long idSesion, Medico medico) throws RemoteException, MedicoYaExistenteException, SQLException, Exception {
		servidor.crear(idSesion, medico);
	}

	public void modificar(long idSesion, Medico medico) throws RemoteException, MedicoInexistenteException, SQLException, Exception {
		servidor.modificar(idSesion, medico);
	}
	
	public void eliminar(long idSesion, Medico medico) throws RemoteException, MedicoInexistenteException, SQLException, Exception {
		servidor.eliminar(idSesion, medico);
	}

	public void modificarCalendario(long idSesion, Medico medico, Vector<Date> dias, Date horaDesde, Date horaHasta, IMedico sustituto) throws RemoteException, MedicoInexistenteException, SQLException, Exception {
		servidor.modificarCalendario(idSesion, medico, dias, horaDesde, horaHasta, sustituto);
	}
	
	// Métodos de gestión de citas

	public Cita pedirCita(long idSesion, Beneficiario bene, String nifMedico, Date fechaYhora, long duracion) throws RemoteException, BeneficiarioInexistenteException, MedicoInexistenteException, FechaNoValidaException, SQLException, Exception {
		return servidor.pedirCita(idSesion, bene, nifMedico, fechaYhora, duracion);
	}

	public Cita pedirCita(long dniBeneficiario, Beneficiario bene, long idVolante, Date fechaYhora, long duracion) throws RemoteException, BeneficiarioInexistenteException, MedicoInexistenteException, FechaNoValidaException, VolanteNoValidoException, SQLException, Exception {
		return servidor.pedirCita(dniBeneficiario, bene, idVolante, fechaYhora, duracion);
	}

	public Vector<Cita> getCitas(long idSesion, String dniBeneficiario) throws RemoteException, BeneficiarioInexistenteException, SQLException, Exception {
		return servidor.getCitas(idSesion, dniBeneficiario);
	}

	public void anularCita(long idSesion, Cita cita) throws RemoteException, CitaNoValidaException, SQLException, Exception {
		servidor.anularCita(idSesion, cita);	
	}
	
	public long emitirVolante(long idSesion, Beneficiario bene, Medico emisor, Medico receptor) throws RemoteException, BeneficiarioInexistenteException, MedicoInexistenteException, SQLException, Exception {
		return servidor.emitirVolante(idSesion, bene, emisor, receptor);
	}
	
	public Volante getVolante(long idSesion, long idVolante) throws RemoteException, Exception {
		return (Volante) servidor.mensajeAuxiliar(idSesion, ICodigosMensajeAuxiliar.CONSULTAR_VOLANTE, idVolante);
	}
	
	// Método auxiliar

	public Object mensajeAuxiliar(long idSesion, long codigoMensaje, Object informacion) throws RemoteException, Exception {
		return servidor.mensajeAuxiliar(idSesion, codigoMensaje, informacion);
	}
		
}
