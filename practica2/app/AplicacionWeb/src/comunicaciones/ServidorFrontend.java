package comunicaciones;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Vector;

import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.CentroSalud;
import dominio.conocimiento.Cita;
import dominio.conocimiento.DiaSemana;
import dominio.conocimiento.ICodigosMensajeAuxiliar;
import dominio.conocimiento.IMedico;
import dominio.conocimiento.ISesion;
import dominio.conocimiento.Medico;
import dominio.conocimiento.Operaciones;
import dominio.conocimiento.TipoMedico;
import dominio.conocimiento.Usuario;
import dominio.conocimiento.Volante;
import excepciones.BeneficiarioInexistenteException;
import excepciones.BeneficiarioYaExistenteException;
import excepciones.CitaNoValidaException;
import excepciones.FechaNoValidaException;
import excepciones.MedicoInexistenteException;
import excepciones.MedicoYaExistenteException;
import excepciones.UsuarioIncorrectoException;
import excepciones.VolanteNoValidoException;

/**
 * Clase singleton utilizada por la aplicación web para acceder a la
 * funcionalidad del servidor front-end.
 */
public class ServidorFrontend {

	private ProxyServidorFrontend servidor;
	private boolean conectado;
	
	private static ServidorFrontend instancia;
	
	public static ServidorFrontend getServidor() throws MalformedURLException, RemoteException, NotBoundException {
		if(instancia == null) {
			instancia = new ServidorFrontend();
		}
		if(!instancia.isConectado()) {
			try {
				instancia.conectar(IConexion.IP, IConexion.PUERTO);
				instancia.setConectado(true);
			} catch(MalformedURLException ex) {
				throw ex;
			} catch(RemoteException ex) {
				throw ex;
			} catch(NotBoundException ex) {
				throw ex;
			}
		}
		return instancia;
	}
	
	private ServidorFrontend() {
		servidor = new ProxyServidorFrontend();
		conectado = false;
	}
	
	private boolean isConectado() {
		return conectado;
	}
	
	private void setConectado(boolean conectado) {
		this.conectado = conectado;
	}
	
	private void conectar(String ip, int puerto) throws MalformedURLException, RemoteException, NotBoundException {
		try {
			servidor.conectar(ip, puerto);
		} catch(RemoteException ex) {
			throw new RemoteException("No se puede conectar con el servidor front-end.");
		} catch(NotBoundException ex) {
			throw new NotBoundException("El servidor front-end está desactivado.");
		}
	}
	
	// Métodos de gestión de sesiones
	
	public ISesion identificarUsuario(String login, String password) throws RemoteException, SQLException, UsuarioIncorrectoException, Exception {
		return servidor.identificarUsuario(login, password);
	}
	
	public ISesion identificarBeneficiario(String nss) throws RemoteException, Exception {
		return (ISesion)servidor.mensajeAuxiliar(-1, ICodigosMensajeAuxiliar.IDENTIFICAR_BENEFICIARIO, nss);
	}
	
	public void cerrarSesion(long idSesion) throws RemoteException, Exception {
		servidor.liberar(idSesion);
	}
	
	@SuppressWarnings("unchecked")
	public Vector<Operaciones> operacionesDisponibles(long idSesion) throws RemoteException, Exception {
		return (Vector<Operaciones>)servidor.mensajeAuxiliar(idSesion, ICodigosMensajeAuxiliar.OPERACIONES_DISPONIBLES, null);
	}
	
	// Métodos de gestión de beneficiarios
	
	public Beneficiario consultarBeneficiarioPorNIF(long idSesion, String nif) throws RemoteException, SQLException, BeneficiarioInexistenteException, Exception {
		return servidor.getBeneficiario(idSesion, nif);
	}

	public Beneficiario consultarBeneficiarioPorNSS(long idSesion, String nss) throws RemoteException, SQLException, BeneficiarioInexistenteException, Exception {
		return servidor.getBeneficiarioPorNSS(idSesion, nss);
	}

	public void crearBeneficiario(long idSesion, Beneficiario beneficiario) throws RemoteException, SQLException, BeneficiarioYaExistenteException, Exception {
		servidor.crear(idSesion, beneficiario);
	}
			
	public void modificarBeneficiario(long idSesion, Beneficiario beneficiario) throws RemoteException, SQLException, BeneficiarioInexistenteException, Exception {
		servidor.modificar(idSesion, beneficiario);
	}
	
	public void eliminarBeneficiario(long idSesion, Beneficiario beneficiario) throws RemoteException, Exception {
		servidor.mensajeAuxiliar(idSesion, ICodigosMensajeAuxiliar.ELIMINAR_BENEFICIARIO, beneficiario);
	}

	// Métodos de gestión de usuarios

	public Usuario consultarUsuario(long idSesion, String nif) throws RemoteException, Exception {
		return (Usuario)servidor.mensajeAuxiliar(idSesion, ICodigosMensajeAuxiliar.CONSULTAR_USUARIO, nif);
	}
	
	public void crearUsuario(long idSesion, Usuario usu) throws RemoteException, Exception {
		servidor.mensajeAuxiliar(idSesion, ICodigosMensajeAuxiliar.CREAR_USUARIO, usu);
	}

	public void modificarUsuario(long idSesion, Usuario usu) throws RemoteException, Exception {
		servidor.mensajeAuxiliar(idSesion, ICodigosMensajeAuxiliar.MODIFICAR_USUARIO, usu);
	}
	
	public void eliminarUsuario(long idSesion, Usuario usu) throws RemoteException, Exception {
		servidor.mensajeAuxiliar(idSesion, ICodigosMensajeAuxiliar.ELIMINAR_USUARIO, usu);
	}

	public boolean correspondeNIFUsuario(long idSesion, String nif) throws RemoteException, Exception {
		return (Boolean)servidor.mensajeAuxiliar(idSesion, ICodigosMensajeAuxiliar.CORRESPONDE_NIF_USUARIO, nif);
	}
	
	public Usuario consultarPropioUsuario(long idSesion) throws RemoteException, Exception {
		return (Usuario)servidor.mensajeAuxiliar(idSesion, ICodigosMensajeAuxiliar.CONSULTAR_PROPIO_USUARIO, null);
	}

	@SuppressWarnings("unchecked")
	public Vector<CentroSalud> consultarCentros(long idSesion) throws RemoteException, Exception {
		return (Vector<CentroSalud>)servidor.mensajeAuxiliar(idSesion, ICodigosMensajeAuxiliar.CONSULTAR_CENTROS, null);
	}
	
	// Métodos de gestión de médicos
	
	public Medico consultarMedico(long idSesion, String nif) throws RemoteException, MedicoInexistenteException, Exception {
		return servidor.getMedico(idSesion, nif);
	}
	
	public Medico consultarMedicoPorLogin(long idSesion, String login) throws RemoteException, Exception {
		return (Medico)servidor.mensajeAuxiliar(idSesion, ICodigosMensajeAuxiliar.CONSULTAR_MEDICO_POR_LOGIN, login);
	}

	public void crearMedico(long idSesion, Medico medico) throws RemoteException, MedicoYaExistenteException, Exception {
		servidor.crear(idSesion, medico);
	}
	
	public void modificarMedico(long idSesion, Medico medico) throws RemoteException, MedicoInexistenteException, SQLException, Exception {
		servidor.modificar(idSesion, medico);
	}

	public void eliminarMedico(long idSesion, Medico medico) throws RemoteException, MedicoInexistenteException, Exception {
		servidor.eliminar(idSesion, medico);
	}
	
	public void asignarSustituto(long idSesion, Medico medico, Vector<Date> dias, int horaDesde, int horaHasta, Medico sustituto) throws RemoteException, MedicoInexistenteException, SQLException, Exception {
		servidor.modificarCalendario(idSesion, medico, dias, new GregorianCalendar(1980, 0, 1, horaDesde, 0).getTime(), new GregorianCalendar(1980, 0, 1, horaHasta, 0).getTime(), (IMedico)sustituto);
	}

	@SuppressWarnings("unchecked")
	public Vector<Medico> obtenerMedicosTipo(long idSesion, TipoMedico tipo) throws RemoteException, Exception {
		return (Vector<Medico>)servidor.mensajeAuxiliar(idSesion, ICodigosMensajeAuxiliar.OBTENER_MEDICOS_TIPO, tipo);
	}

	@SuppressWarnings("unchecked")
	public Vector<Beneficiario> obtenerBeneficiariosMedico(long idSesion, String nifMedico) throws RemoteException, Exception {
		return (Vector<Beneficiario>)servidor.mensajeAuxiliar(idSesion, ICodigosMensajeAuxiliar.CONSULTAR_BENEFICIARIOS_MEDICO, nifMedico);
	}
	
	@SuppressWarnings("unchecked")
	public Hashtable<DiaSemana, Vector<String>> consultarHorarioMedico(long idSesion, String nifMedico) throws RemoteException, Exception {
		return (Hashtable<DiaSemana, Vector<String>>)servidor.mensajeAuxiliar(idSesion, ICodigosMensajeAuxiliar.CONSULTAR_HORARIO_MEDICO, nifMedico);
	}

	@SuppressWarnings("unchecked")
	public Hashtable<Date, Vector<String>> consultarHorasCitasMedico(long idSesion, String nifMedico) throws RemoteException, Exception {
		return (Hashtable<Date, Vector<String>>)servidor.mensajeAuxiliar(idSesion, ICodigosMensajeAuxiliar.CONSULTAR_HORAS_CITAS_MEDICO, nifMedico);
	}
	
	@SuppressWarnings("unchecked")
	public Vector<Cita> consultarCitasMedico(long idSesion, String nifMedico) throws RemoteException, Exception {
		return (Vector<Cita>)servidor.mensajeAuxiliar(idSesion, ICodigosMensajeAuxiliar.CONSULTAR_CITAS_MEDICO, nifMedico);
	}
	
	@SuppressWarnings("unchecked")
	public Vector<Date> consultarDiasCompletos(long idSesion, String nifMedico) throws RemoteException, Exception {
		return (Vector<Date>)servidor.mensajeAuxiliar(idSesion, ICodigosMensajeAuxiliar.CONSULTAR_DIAS_COMPLETOS, nifMedico);
	}
	
	@SuppressWarnings("unchecked")
	public Vector<Medico> obtenerPosiblesSustitutos(long idSesion, String nifMedico, Date dia, int horaDesde, int horaHasta) throws RemoteException, Exception {
		return (Vector<Medico>)servidor.mensajeAuxiliar(idSesion, ICodigosMensajeAuxiliar.OBTENER_POSIBLES_SUSTITUTOS, new Object[] { nifMedico, dia, horaDesde, horaHasta });
	}
	
	public Medico consultarMedicoCita(long idSesion, String nifMedico, Date dia) throws RemoteException, Exception {
		return (Medico)servidor.mensajeAuxiliar(idSesion, ICodigosMensajeAuxiliar.CONSULTAR_MEDICO_CITA, new Object[] { nifMedico, dia });
	}
	
	// Métodos de gestión de citas
	
	public Vector<Cita> consultarHistoricoCitas(long idSesion, String nif) throws RemoteException, BeneficiarioInexistenteException, SQLException, Exception {
		return servidor.getCitas(idSesion, nif);
	}
	
	@SuppressWarnings("unchecked")
	public Vector<Cita> consultarCitasPendientesBeneficiario(long idSesion, String nif) throws RemoteException, Exception {
		return (Vector<Cita>)servidor.mensajeAuxiliar(idSesion, ICodigosMensajeAuxiliar.CONSULTAR_CITAS_PENDIENTES, nif);
	}
	
	@SuppressWarnings("unchecked")
	public Vector<Cita> consultarCitasPendientesMedico(long idSesion, String nif) throws RemoteException, Exception {
		return (Vector<Cita>)servidor.mensajeAuxiliar(idSesion, ICodigosMensajeAuxiliar.CONSULTAR_CITAS_PENDIENTES_MEDICO, nif);
	}

	@SuppressWarnings("unchecked")
	public Vector<Cita> consultarCitasPropiasMedico(long idSesion) throws RemoteException, Exception {
		return (Vector<Cita>)servidor.mensajeAuxiliar(idSesion, ICodigosMensajeAuxiliar.CONSULTAR_CITAS_PROPIAS_MEDICO, null);
	}

	@SuppressWarnings("unchecked")
	public Vector<Cita> consultarCitasPendientesPropiasMedico(long idSesion) throws RemoteException, Exception {
		return (Vector<Cita>)servidor.mensajeAuxiliar(idSesion, ICodigosMensajeAuxiliar.CONSULTAR_CITAS_PENDIENTES_PROPIAS_MEDICO, null);
	}

	@SuppressWarnings("unchecked")
	public Vector<Cita> consultarCitasFechaMedico(long idSesion, String nif, Date dia, int horaDesde, int horaHasta) throws RemoteException, Exception {
		return (Vector<Cita>)servidor.mensajeAuxiliar(idSesion, ICodigosMensajeAuxiliar.CONSULTAR_CITAS_FECHA_MEDICO, new Object[] { nif, dia, horaDesde, horaHasta });
	}

	public Cita consultarCitaBeneficiario(long idSesion, long idCita) throws RemoteException, Exception {
		return (Cita)servidor.mensajeAuxiliar(idSesion, ICodigosMensajeAuxiliar.CONSULTAR_CITA_BENEFICIARIO, idCita);
	}

	public Cita pedirCita(long idSesion, Beneficiario beneficiario, String idMedico, Date fechaYHora, long duracion) throws RemoteException, BeneficiarioInexistenteException, MedicoInexistenteException, FechaNoValidaException, SQLException, Exception { 
		return servidor.pedirCita(idSesion, beneficiario, idMedico, fechaYHora, duracion);
	}
	
	public Cita pedirCita(long idSesion, Beneficiario beneficiario, long idVolante, Date fechaYHora, long duracion) throws RemoteException, BeneficiarioInexistenteException, MedicoInexistenteException, FechaNoValidaException, VolanteNoValidoException, SQLException, Exception {
		return servidor.pedirCita(idSesion, beneficiario, idVolante, fechaYHora, duracion);
	}
	 
	public void anularCita(long idSesion, Cita cita) throws RemoteException, CitaNoValidaException, SQLException, Exception {
		servidor.anularCita(idSesion, cita);
	}
		
	// Métodos de gestión de volantes
	
	public Volante consultarVolante(long idSesion, long idVolante) throws RemoteException, Exception {
		return (Volante)servidor.mensajeAuxiliar(idSesion, ICodigosMensajeAuxiliar.CONSULTAR_VOLANTE, idVolante);
	}

	public long emitirVolante(long idSesion, Beneficiario bene, Medico emisor, Medico receptor) throws RemoteException, BeneficiarioInexistenteException, MedicoInexistenteException, SQLException, Exception { 
		return servidor.emitirVolante(idSesion, bene, emisor, receptor);
	}

}
