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
import dominio.conocimiento.IMedico;
import dominio.conocimiento.ISesion;
import dominio.conocimiento.Medico;
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
 * Proxy utilizado para conectarse con el servidor front-end.
 */
public class ProxyServidorFrontend implements IServidorFrontend {
	
	private IServidorFrontend servidor;
	
	private String ip;
	private int puerto;
	
	public void conectar(String ip, int puerto) throws MalformedURLException, RemoteException, NotBoundException {
		String url;
		
		this.ip = ip;
		this.puerto = puerto;
		url = "rmi://" + ip + ":" + String.valueOf(puerto) + "/" + NOMBRE_SERVIDOR;
		servidor = (IServidorFrontend)Naming.lookup(url);
	}
	
	private void reconectar() throws RemoteException {
		String url;
		
		url = "rmi://" + ip + ":" + String.valueOf(puerto) + "/" + NOMBRE_SERVIDOR;
        try {
			servidor = (IServidorFrontend)Naming.lookup(url);
		} catch(NotBoundException e) {
			throw new RemoteException(e.getMessage());
		} catch(MalformedURLException e) {
			throw new RemoteException(e.getMessage());
		}
	}

	// Métodos de gestión de sesiones
	
	public ISesion identificarUsuario(String login, String password) throws RemoteException, SQLException, UsuarioIncorrectoException, Exception {
		ISesion sesion;
		
		try {
			sesion = servidor.identificarUsuario(login, password);
		} catch(RemoteException ex) {
			reconectar();
			sesion = servidor.identificarUsuario(login, password);
		}
		return sesion;
	}

	public void registrar(ICliente cliente, long idSesion) throws RemoteException, SesionNoIniciadaException, Exception {
		try {
			servidor.registrar(cliente, idSesion);
		} catch(RemoteException ex) {
			reconectar();
			servidor.registrar(cliente, idSesion);
		}
	}
	
	public void liberar(long idSesion) throws RemoteException, Exception {
		try {
			servidor.liberar(idSesion);
		} catch(RemoteException ex) {
			reconectar();
			servidor.liberar(idSesion);
		}
	}
	
	// Métodos de gestión de beneficiarios
	
	public Beneficiario getBeneficiario(long idSesion, String nif) throws RemoteException, SQLException, BeneficiarioInexistenteException, Exception {
		Beneficiario beneficiario;
		
		try {
			beneficiario = servidor.getBeneficiario(idSesion, nif);
		} catch(RemoteException ex) {
			reconectar();
			beneficiario = servidor.getBeneficiario(idSesion, nif);
		}
		return beneficiario;
	}

	public Beneficiario getBeneficiarioPorNSS(long idSesion, String nss) throws RemoteException, SQLException, BeneficiarioInexistenteException, Exception {
		Beneficiario beneficiario;
		
		try {
			beneficiario = servidor.getBeneficiarioPorNSS(idSesion, nss);
		} catch(RemoteException ex) {
			reconectar();
			beneficiario = servidor.getBeneficiarioPorNSS(idSesion, nss);
		}
		return beneficiario;
	}

	public void crear(long idSesion, Beneficiario bene) throws RemoteException, SQLException, BeneficiarioYaExistenteException, Exception {
		try {
			servidor.crear(idSesion, bene);
		} catch(RemoteException ex) {
			reconectar();
			servidor.crear(idSesion, bene);
		}
	}

	public void modificar(long idSesion, Beneficiario bene) throws RemoteException, SQLException, BeneficiarioInexistenteException, Exception {
		try {
			servidor.modificar(idSesion, bene);
		} catch(RemoteException ex) {
			reconectar();
			servidor.modificar(idSesion, bene);
		}
	}
	
	// Métodos de gestión de médicos
		
	public Medico getMedico(long idSesion, String dni) throws RemoteException, MedicoInexistenteException, Exception {
		Medico medico;
		
		try {
			medico = servidor.getMedico(idSesion, dni);
		} catch(RemoteException ex) {
			reconectar();
			medico = servidor.getMedico(idSesion, dni);
		}
		return medico;
	}

	public void crear(long idSesion, Medico medico) throws RemoteException, MedicoYaExistenteException, SQLException, Exception {
		try {
			servidor.crear(idSesion, medico);
		} catch(RemoteException ex) {
			reconectar();
			servidor.crear(idSesion, medico);
		}
	}

	public void modificar(long idSesion, Medico medico) throws RemoteException, MedicoInexistenteException, SQLException, Exception {
		try {
			servidor.modificar(idSesion, medico);
		} catch(RemoteException ex) {
			reconectar();
			servidor.modificar(idSesion, medico);
		}
	}
	
	public void eliminar(long idSesion, Medico medico) throws RemoteException, MedicoInexistenteException, SQLException, Exception {
		try {
			servidor.eliminar(idSesion, medico);
		} catch(RemoteException ex) {
			reconectar();
			servidor.eliminar(idSesion, medico);
		}
	}

	public void modificarCalendario(long idSesion, Medico medico, Vector<Date> dias, Date horaDesde, Date horaHasta, IMedico sustituto) throws RemoteException, MedicoInexistenteException, SQLException, Exception {
		try {
			servidor.modificarCalendario(idSesion, medico, dias, horaDesde, horaHasta, sustituto);
		} catch(RemoteException ex) {
			reconectar();
			servidor.modificarCalendario(idSesion, medico, dias, horaDesde, horaHasta, sustituto);
		}
	}
	
	// Métodos de gestión de citas

	public Cita pedirCita(long idSesion, Beneficiario bene, String nifMedico, Date fechaYhora, long duracion) throws RemoteException, BeneficiarioInexistenteException, MedicoInexistenteException, FechaNoValidaException, SQLException, Exception {
		Cita cita;
		
		try {
			cita = servidor.pedirCita(idSesion, bene, nifMedico, fechaYhora, duracion);
		} catch(RemoteException ex) {
			reconectar();
			cita = servidor.pedirCita(idSesion, bene, nifMedico, fechaYhora, duracion);
		}
		return cita;
	}

	public Cita pedirCita(long dniBeneficiario, Beneficiario bene, long idVolante, Date fechaYhora, long duracion) throws RemoteException, BeneficiarioInexistenteException, MedicoInexistenteException, FechaNoValidaException, VolanteNoValidoException, SQLException, Exception {
		Cita cita;
		
		try {
			cita = servidor.pedirCita(dniBeneficiario, bene, idVolante, fechaYhora, duracion);
		} catch(RemoteException ex) {
			reconectar();
			cita = servidor.pedirCita(dniBeneficiario, bene, idVolante, fechaYhora, duracion);
		}
		return cita;
	}

	public Vector<Cita> getCitas(long idSesion, String dniBeneficiario) throws RemoteException, BeneficiarioInexistenteException, SQLException, Exception {
		Vector<Cita> citas;
		
		try {
			citas = servidor.getCitas(idSesion, dniBeneficiario);
		} catch(RemoteException ex) {
			reconectar();
			citas = servidor.getCitas(idSesion, dniBeneficiario);
		}
		return citas;
	}

	public void anularCita(long idSesion, Cita cita) throws RemoteException, CitaNoValidaException, SQLException, Exception {
		try {
			servidor.anularCita(idSesion, cita);
		} catch(RemoteException ex) {
			reconectar();
			servidor.anularCita(idSesion, cita);
		}
	}
	
	public long emitirVolante(long idSesion, Beneficiario bene, Medico emisor, Medico receptor) throws RemoteException, BeneficiarioInexistenteException, MedicoInexistenteException, SQLException, Exception {
		long idVolante;
		
		try {
			idVolante = servidor.emitirVolante(idSesion, bene, emisor, receptor);
		} catch(RemoteException ex) {
			reconectar();
			idVolante = servidor.emitirVolante(idSesion, bene, emisor, receptor);
		}
		return idVolante;
	}
		
	// Método auxiliar

	public Object mensajeAuxiliar(long idSesion, long codigoMensaje, Object informacion) throws RemoteException, Exception {
		Object resultado;
		
		try {
			resultado = servidor.mensajeAuxiliar(idSesion, codigoMensaje, informacion);
		} catch(RemoteException ex) {
			reconectar();
			resultado = servidor.mensajeAuxiliar(idSesion, codigoMensaje, informacion);
		}
		return resultado;
	}
		
}
