public class RemotoServidorFrontend extends UnicastRemoteObject implements IServidorFrontend {
	private IServidorFrontend servidor;
	public Cita pedirCita(long idSesion, Beneficiario beneficiario, String idMedico, Date fechaYHora, long duracion) throws ... {
		return servidor.pedirCita(idSesion, beneficiario, idMedico, fechaYHora, duracion);
	}
}
public class ServidorFrontend implements IServidorFrontend {
	public Cita pedirCita(long idSesion, Beneficiario beneficiario, String idMedico, Date fechaYHora, long duracion) throws ... {
		try {
			// Pedimos una cita para un beneficiario y un médico dados
			cita = GestorCitas.pedirCita(idSesion, beneficiario, idMedico, fechaYHora, duracion);
			login = GestorSesiones.getSesion(idSesion).getUsuario().getLogin();
			GestorConexionesLog.ponerMensaje(login, ITiposMensajeLog.TIPO_CREATE, "Tramitada una cita para el beneficiario con NIF " + beneficiario.getNif() + ".");
			GestorSesiones.actualizarClientes(idSesion, ICodigosOperacionesCliente.INSERTAR, cita);
		} catch(...) {
			// (manejadores de excepciones)
		}
		return cita;
	}
}