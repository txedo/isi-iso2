package dominio.control;

import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Vector;
import presentacion.JFLogin;
import presentacion.JFPrincipal;
import comunicaciones.ConfiguracionCliente;
import comunicaciones.RemotoCliente;
import comunicaciones.ICliente;
import comunicaciones.IServidorFrontend;
import comunicaciones.ProxyServidorFrontend;
import comunicaciones.UtilidadesComunicaciones;
import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.CentroSalud;
import dominio.conocimiento.Cita;
import dominio.conocimiento.DiaSemana;
import dominio.conocimiento.ICodigosMensajeAuxiliar;
import dominio.conocimiento.ICodigosOperacionesCliente;
import dominio.conocimiento.IMedico;
import dominio.conocimiento.ISesion;
import dominio.conocimiento.Medico;
import dominio.conocimiento.Operaciones;
import dominio.conocimiento.Sustitucion;
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
 * Controlador principal de la funcionalidad de los clientes. 
 */
public class ControladorCliente {

	private ProxyServidorFrontend servidor;
	private RemotoCliente cliente;
	private ISesion sesion;
	private JFLogin ventanaLogin;
	private JFPrincipal ventanaPrincipal;
	private String usuarioAutenticado;
	private String ipCliente;
	private boolean registrado;
	
	public ControladorCliente() {
		servidor = null;
		ipCliente = null;
	}
	
	public JFPrincipal getVentanaPrincipal() {
		return ventanaPrincipal;
	}
	
	public void identificarse() {
		// Ocultamos y cerramos las ventanas si todavía estaban abiertas
		if(ventanaLogin != null) {
			ventanaLogin.setVisible(false);
			ventanaLogin.dispose();
			ventanaLogin = null;
		}
		if(ventanaPrincipal != null) {
			ventanaPrincipal.setVisible(false);
			ventanaPrincipal.dispose();
			ventanaPrincipal = null;
		}
		// Creamos la ventana de login y la mostramos
		ventanaLogin = new JFLogin();
		ventanaLogin.setControlador(this);
		ventanaLogin.setLocationRelativeTo(null);
		ventanaLogin.setVisible(true);
	}

	public void iniciarSesion(ConfiguracionCliente configuracion, String login, String password) throws SQLException, UsuarioIncorrectoException, MalformedURLException, RemoteException, NotBoundException, UnknownHostException, Exception {
		// Por defecto, cuando se inicia sesión se registra el cliente remoto
		iniciarSesion(configuracion, login, password, true);
	}
	
	public void iniciarSesion(ConfiguracionCliente configuracion, String login, String password, boolean registrar) throws SQLException, UsuarioIncorrectoException, MalformedURLException, RemoteException, NotBoundException, UnknownHostException, Exception {
		// Obtenemos la IP de la máquina local
		ipCliente = UtilidadesComunicaciones.obtenerIPHost();
		
		// Indicamos a RMI que debe utilizar la IP obtenida como IP de este host
		// en las comunicaciones remotas; esta instrucción es necesaria porque
		// si el ordenador pertenece a más de una red, puede que RMI tome una IP
		// privada como IP del host y las comunicaciones entrantes no funcionen
		System.setProperty("java.rmi.server.hostname", ipCliente);

		try {
			// Establecemos conexión con el servidor front-end
			servidor = new ProxyServidorFrontend();
			servidor.conectar(configuracion.getIPFrontend(), configuracion.getPuertoFrontend());
		} catch(NotBoundException e) {
			throw new NotBoundException("No se puede conectar con el servidor front-end porque está desactivado (IP " + configuracion.getIPFrontend() + ", puerto " + String.valueOf(configuracion.getPuertoFrontend()) + ").");
		} catch(RemoteException e) {
			throw new RemoteException("No se puede conectar con el servidor front-end (IP " + configuracion.getIPFrontend() + ", puerto " + String.valueOf(configuracion.getPuertoFrontend()) + ").");
		}
		
		try {
			// Nos identificamos en el servidor
			sesion = (ISesion)servidor.identificar(login, password);
			usuarioAutenticado = login;
		} catch(RemoteException e) {
			throw new RemoteException("No se puede identificar el usuario en el servidor front-end (IP " + configuracion.getIPFrontend() + ", puerto " + String.valueOf(configuracion.getPuertoFrontend()) + ").");
		}

		// Comprobamos si hay que registrar el cliente en el servidor
		registrado = registrar;
		if(registrar) {
			try {
				// Creamos y activamos el cliente remoto
				cliente = RemotoCliente.getCliente();
				cliente.activar(ipCliente);
				((Cliente)cliente.getClienteExportado()).setControlador(this);
				// Registramos el cliente en el servidor
				servidor.registrar((ICliente)cliente, sesion.getId());
			} catch(RemoteException e) {
				throw new RemoteException("No se puede registrar el cliente en el servidor front-end (IP " + configuracion.getIPFrontend() + ", puerto " + String.valueOf(configuracion.getPuertoFrontend()) + ").");
			}
		}
		
		// Ocultamos y cerramos las ventanas si todavía estaban abiertas
		if(ventanaLogin != null) {
			ventanaLogin.setVisible(false);
			ventanaLogin.dispose();
			ventanaLogin = null;
		}
		if(ventanaPrincipal != null) {
			ventanaPrincipal.dispose();
			ventanaPrincipal = null;
		}
		// Creamos la ventana principal y la mostramos
		ventanaPrincipal = new JFPrincipal(this);
		ventanaPrincipal.iniciar();
		ventanaPrincipal.setLocationRelativeTo(null);
		ventanaPrincipal.setVisible(true);
	}

	public void cerrarControlador() throws RemoteException, MalformedURLException, NotBoundException {
		if(cliente != null) {
			cliente.desactivar(ipCliente);
		}
		if(ventanaLogin != null) {
			ventanaLogin.setVisible(false);
			ventanaLogin.dispose();
		}
		if(ventanaPrincipal != null) {
			ventanaPrincipal.setVisible(false);
			ventanaPrincipal.dispose();
		}
	}
	
	public ISesion getSesion() {
		return sesion;
	}	
	
	public String getUsuarioAutenticado() {
		return usuarioAutenticado;
	}
	
	public boolean isRegistrado() {
		return registrado;
	}

	public IServidorFrontend getServidor() {
		return servidor;
	}
		
	public int getPuertoEscucha() {
		return cliente.getPuertoEscucha();
	}
	
	public String getIPCliente() {
		return ipCliente;
	}
	
	
	// ---------------------------
	// Métodos servidor -> cliente
	// ---------------------------
	
	public void forzarCierreSesionDuplicada() {
		ventanaPrincipal.forzarCierreSesionDuplicada();
	}
	
	public void forzarCierreSesionEliminacion() {
		ventanaPrincipal.forzarCierreSesionEliminacion();
	}
	
	public void forzarCierreServidorDesconectado() {
		ventanaPrincipal.forzarCierreServidorDesconectado();
	}
	
	public void actualizarVentanaPrincipal(int operacion, Object dato) {
		switch(operacion) {
		case ICodigosOperacionesCliente.INSERTAR:
			if(dato instanceof Usuario) {
				ventanaPrincipal.usuarioRegistrado((Usuario)dato);
			} else if(dato instanceof Cita) {
				ventanaPrincipal.citaRegistrada((Cita)dato);
			} else if(dato instanceof Sustitucion) {
				ventanaPrincipal.sustitucionRegistrada((Sustitucion)dato);
			}
			break;
		case ICodigosOperacionesCliente.MODIFICAR:
			if(dato instanceof Beneficiario) {
				ventanaPrincipal.beneficiarioActualizado((Beneficiario)dato);
			} else if(dato instanceof Usuario) {
				ventanaPrincipal.usuarioActualizado((Usuario)dato);
			}
			break;
		case ICodigosOperacionesCliente.ELIMINAR:
			if(dato instanceof Beneficiario) {
				ventanaPrincipal.beneficiarioEliminado((Beneficiario)dato);
			} else if(dato instanceof Usuario) {
				ventanaPrincipal.usuarioEliminado((Usuario)dato);
			} else if(dato instanceof Cita) {
				ventanaPrincipal.citaAnulada((Cita)dato);
			}
			break;
		}
	}
	
	
	// ---------------------------
	// Métodos cliente -> servidor
	// ---------------------------
	
	// Métodos de gestión de sesiones
	
	public void cerrarSesion() throws RemoteException, Exception {
		servidor.liberar(sesion.getId());
	}
	
	@SuppressWarnings("unchecked")
	public Vector<Operaciones> operacionesDisponibles() throws RemoteException, Exception {
		return (Vector<Operaciones>)servidor.mensajeAuxiliar(sesion.getId(), ICodigosMensajeAuxiliar.OPERACIONES_DISPONIBLES, null);
	}
	
	// Métodos de gestión de beneficiarios
	
	public Beneficiario consultarBeneficiarioPorNIF(String nif) throws RemoteException, SQLException, BeneficiarioInexistenteException, Exception {
		return servidor.getBeneficiario(sesion.getId(), nif);
	}

	public Beneficiario consultarBeneficiarioPorNSS(String nss) throws RemoteException, SQLException, BeneficiarioInexistenteException, Exception {
		return servidor.getBeneficiarioPorNSS(sesion.getId(), nss);
	}

	public void crearBeneficiario(Beneficiario beneficiario) throws RemoteException, SQLException, BeneficiarioYaExistenteException, Exception {
		servidor.crear(sesion.getId(), beneficiario);
	}
			
	public void modificarBeneficiario(Beneficiario beneficiario) throws RemoteException, SQLException, BeneficiarioInexistenteException, Exception {
		servidor.modificar(sesion.getId(), beneficiario);
	}
	
	public void eliminarBeneficiario(Beneficiario beneficiario) throws RemoteException, Exception {
		servidor.mensajeAuxiliar(sesion.getId(), ICodigosMensajeAuxiliar.ELIMINAR_BENEFICIARIO, beneficiario);
	}

	// Métodos de gestión de usuarios

	public Usuario consultarUsuario(String nif) throws RemoteException, Exception {
		return (Usuario)servidor.mensajeAuxiliar(sesion.getId(), ICodigosMensajeAuxiliar.CONSULTAR_USUARIO, nif);
	}
	
	public void crearUsuario(Usuario usu) throws RemoteException, Exception {
		servidor.mensajeAuxiliar(sesion.getId(), ICodigosMensajeAuxiliar.CREAR_USUARIO, usu);
	}

	public void modificarUsuario(Usuario usu) throws RemoteException, Exception {
		servidor.mensajeAuxiliar(sesion.getId(), ICodigosMensajeAuxiliar.MODIFICAR_USUARIO, usu);
	}
	
	public void eliminarUsuario(Usuario usu) throws RemoteException, Exception {
		servidor.mensajeAuxiliar(sesion.getId(), ICodigosMensajeAuxiliar.ELIMINAR_USUARIO, usu);
	}

	public Usuario consultarPropioUsuario() throws RemoteException, Exception {
		return (Usuario)servidor.mensajeAuxiliar(sesion.getId(), ICodigosMensajeAuxiliar.CONSULTAR_PROPIO_USUARIO, null);
	}

	@SuppressWarnings("unchecked")
	public Vector<CentroSalud> consultarCentros() throws RemoteException, Exception {
		return (Vector<CentroSalud>)servidor.mensajeAuxiliar(sesion.getId(), ICodigosMensajeAuxiliar.CONSULTAR_CENTROS, null);
	}
	
	// Métodos de gestión de médicos
	
	public Medico consultarMedico(String nif) throws RemoteException, MedicoInexistenteException, Exception {
		return servidor.getMedico(sesion.getId(), nif);
	}
	
	public void crearMedico(Medico medico) throws RemoteException, MedicoYaExistenteException, Exception {
		servidor.crear(sesion.getId(), medico);
	}
	
	public void modificarMedico(Medico medico) throws RemoteException, MedicoInexistenteException, SQLException, Exception {
		servidor.modificar(sesion.getId(), medico);
	}

	public void eliminarMedico(Medico medico) throws RemoteException, MedicoInexistenteException, Exception {
		servidor.eliminar(sesion.getId(), medico);
	}
	
	public void asignarSustituto(Medico medico, Vector<Date> dias, int horaDesde, int horaHasta, Medico sustituto) throws RemoteException, MedicoInexistenteException, SQLException, Exception {
		servidor.modificarCalendario(sesion.getId(), medico, dias, new GregorianCalendar(1980, 0, 1, horaDesde, 0).getTime(), new GregorianCalendar(1980, 0, 1, horaHasta, 0).getTime(), (IMedico)sustituto);
	}

	@SuppressWarnings("unchecked")
	public Vector<Medico> obtenerMedicosTipo(TipoMedico tipo) throws RemoteException, Exception {
		return (Vector<Medico>)servidor.mensajeAuxiliar(sesion.getId(), ICodigosMensajeAuxiliar.OBTENER_MEDICOS_TIPO, tipo);
	}

	@SuppressWarnings("unchecked")
	public Vector<Beneficiario> obtenerBeneficiariosMedico(String nifMedico) throws RemoteException, Exception {
		return (Vector<Beneficiario>)servidor.mensajeAuxiliar(sesion.getId(), ICodigosMensajeAuxiliar.CONSULTAR_BENEFICIARIOS_MEDICO, nifMedico);
	}
	
	@SuppressWarnings("unchecked")
	public Hashtable<DiaSemana, Vector<String>> consultarHorarioMedico(String nifMedico) throws RemoteException, Exception {
		return (Hashtable<DiaSemana, Vector<String>>)servidor.mensajeAuxiliar(sesion.getId(), ICodigosMensajeAuxiliar.CONSULTAR_HORARIO_MEDICO, nifMedico);
	}

	@SuppressWarnings("unchecked")
	public Hashtable<Date, Vector<String>> consultarHorasCitasMedico(String nifMedico) throws RemoteException, Exception {
		return (Hashtable<Date, Vector<String>>)servidor.mensajeAuxiliar(sesion.getId(), ICodigosMensajeAuxiliar.CONSULTAR_HORAS_CITAS_MEDICO, nifMedico);
	}
	
	@SuppressWarnings("unchecked")
	public Vector<Cita> consultarCitasMedico(String nifMedico) throws RemoteException, Exception {
		return (Vector<Cita>)servidor.mensajeAuxiliar(sesion.getId(), ICodigosMensajeAuxiliar.CONSULTAR_CITAS_MEDICO, nifMedico);
	}
	
	@SuppressWarnings("unchecked")
	public Vector<Date> consultarDiasCompletos(String nifMedico) throws RemoteException, Exception {
		return (Vector<Date>)servidor.mensajeAuxiliar(sesion.getId(), ICodigosMensajeAuxiliar.CONSULTAR_DIAS_COMPLETOS, nifMedico);
	}
	
	@SuppressWarnings("unchecked")
	public Vector<Medico> obtenerPosiblesSustitutos(String nifMedico, Date dia, int horaDesde, int horaHasta) throws RemoteException, Exception {
		return (Vector<Medico>)servidor.mensajeAuxiliar(sesion.getId(), ICodigosMensajeAuxiliar.OBTENER_POSIBLES_SUSTITUTOS, new Object[] { nifMedico, dia, horaDesde, horaHasta });
	}
	
	public Medico consultarMedicoCita(String nifMedico, Date dia) throws RemoteException, Exception {
		return (Medico)servidor.mensajeAuxiliar(sesion.getId(), ICodigosMensajeAuxiliar.CONSULTAR_MEDICO_CITA, new Object[] { nifMedico, dia });
	}
	
	// Métodos de gestión de citas
	
	public Vector<Cita> consultarHistoricoCitas(String nif) throws RemoteException, BeneficiarioInexistenteException, SQLException, Exception {
		return servidor.getCitas(sesion.getId(), nif);
	}
	
	@SuppressWarnings("unchecked")
	public Vector<Cita> consultarCitasPendientesBeneficiario(String nif) throws RemoteException, Exception {
		return (Vector<Cita>)servidor.mensajeAuxiliar(sesion.getId(), ICodigosMensajeAuxiliar.CONSULTAR_CITAS_PENDIENTES, nif);
	}
	
	@SuppressWarnings("unchecked")
	public Vector<Cita> consultarCitasPendientesMedico(String nif) throws RemoteException, Exception {
		return (Vector<Cita>)servidor.mensajeAuxiliar(sesion.getId(), ICodigosMensajeAuxiliar.CONSULTAR_CITAS_PENDIENTES_MEDICO, nif);
	}

	@SuppressWarnings("unchecked")
	public Vector<Cita> consultarCitasPropiasMedico() throws RemoteException, Exception {
		return (Vector<Cita>)servidor.mensajeAuxiliar(sesion.getId(), ICodigosMensajeAuxiliar.CONSULTAR_CITAS_PROPIAS_MEDICO, null);
	}

	@SuppressWarnings("unchecked")
	public Vector<Cita> consultarCitasPendientesPropiasMedico() throws RemoteException, Exception {
		return (Vector<Cita>)servidor.mensajeAuxiliar(sesion.getId(), ICodigosMensajeAuxiliar.CONSULTAR_CITAS_PENDIENTES_PROPIAS_MEDICO, null);
	}

	@SuppressWarnings("unchecked")
	public Vector<Cita> consultarCitasFechaMedico(String nif, Date dia, int horaDesde, int horaHasta) throws RemoteException, Exception {
		return (Vector<Cita>)servidor.mensajeAuxiliar(sesion.getId(), ICodigosMensajeAuxiliar.CONSULTAR_CITAS_FECHA_MEDICO, new Object[] { nif, dia, horaDesde, horaHasta });
	}

	public Cita pedirCita(Beneficiario beneficiario, String idMedico, Date fechaYHora, long duracion) throws RemoteException, BeneficiarioInexistenteException, MedicoInexistenteException, FechaNoValidaException, SQLException, Exception { 
		return servidor.pedirCita(sesion.getId(), beneficiario, idMedico, fechaYHora, duracion);
	}
	
	public Cita pedirCita(Beneficiario beneficiario, long idVolante, Date fechaYHora, long duracion) throws RemoteException, BeneficiarioInexistenteException, MedicoInexistenteException, FechaNoValidaException, VolanteNoValidoException, SQLException, Exception {
		return servidor.pedirCita(sesion.getId(), beneficiario, idVolante, fechaYHora, duracion);
	}
	 
	public void anularCita(Cita cita) throws RemoteException, CitaNoValidaException, SQLException, Exception {
		servidor.anularCita(sesion.getId(), cita);
	}
		
	// Métodos de gestión de volantes
	
	public Volante consultarVolante(long idVolante) throws RemoteException, Exception {
		return (Volante)servidor.mensajeAuxiliar(sesion.getId(), ICodigosMensajeAuxiliar.CONSULTAR_VOLANTE, idVolante);
	}

	public long emitirVolante(Beneficiario bene, Medico emisor, Medico receptor) throws RemoteException, BeneficiarioInexistenteException, MedicoInexistenteException, SQLException, Exception { 
		return servidor.emitirVolante(sesion.getId(), bene, emisor, receptor);
	}
	
}
