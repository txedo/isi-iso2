public class JPCitaTramitar extends JPBase {
	private ControladorCliente controlador;
	private void btnRegistrarActionPerformed(ActionEvent evt) {
		try {
			// Comprobamos que la hora seleccionada sea válida
			Validacion.comprobarFechaCita(dtcDiaCita.getDate());
			if(!horaSeleccionadaValida()) {
				// (hora de la cita incorrecta)
			} else {
				// Obtenemos la hora definitiva de la cita
				hora = Cita.horaCadenaCita(cmbHorasCitas.getSelectedItem().toString());
				fecha = dtcDiaCita.getDate();
				// Solicitamos la cita
				getControlador().pedirCita(beneficiario, beneficiario.getMedicoAsignado().getNif(), new Date(fecha.getYear(), fecha.getMonth(), fecha.getDate(), hora.getHours(), hora.getMinutes()), IConstantes.DURACION_CITA);
				// Mostramos el resultado de la operación y limpiamos el panel
				// (...)
			}
		} catch(FechaCitaIncorrectaException e) {
			// (fecha de la cita incorrecta)
		} catch(...) {
			// (otros manejadores de excepciones)
		}
	}
}
public class ControladorCliente {
	private ProxyServidorFrontend servidor;
	public Cita pedirCita(Beneficiario beneficiario, String idMedico, Date fechaYHora, long duracion) throws ... {
		return servidor.pedirCita(sesion.getId(), beneficiario, idMedico, fechaYHora, duracion);
	}
}
public class ProxyServidorFrontend implements IServidorFrontend {
	private IServidorFrontend servidor;
	public Cita pedirCita(long idSesion, Beneficiario bene, String nifMedico, Date fechaYhora, long duracion) throws ... {
		return servidor.pedirCita(idSesion, bene, nifMedico, fechaYhora, duracion);
	}
}