public class JPCitaTramitar extends JPBase {
	private void pnlBeneficiarioBeneficiarioBuscado(EventObject evt) {
		// Borramos los datos de la última tramitación de cita
		limpiarCamposTramitacion();
		// Obtenemos el beneficiario que se ha buscado en el panel de consulta
		beneficiario = pnlBeneficiario.getBeneficiario();
		if(beneficiario != null) {
			// Mostramos las fechas y horas disponibles para pedir una cita
			mostrarHorasCitasMedico();
		}
	}
	private void mostrarHorasCitasMedico() {
		try {
			// Para poder pedir cita, el beneficiario debe tener
			// un médico asignado
			if(beneficiario.getMedicoAsignado() == null) {
				// (...)
			} else {
				// Consultamos al servidor toda la información
				// necesaria para el panel de tramitación
				diasOcupados = getControlador().consultarDiasCompletos(beneficiario.getMedicoAsignado().getNif());
				citasOcupadas = getControlador().consultarHorasCitasMedico(beneficiario.getMedicoAsignado().getNif());
				horasCitas = getControlador().consultarHorarioMedico(beneficiario.getMedicoAsignado().getNif());
				// Deshabilitamos los días de la semana que no son
				// laborables para el médico del beneficiario
				// (...)
				// Deshabilitamos los días que el médico no puede
				// pasar consulta en el calendario del panel
				// (...)
				// Buscamos el primer día y hora disponible para una cita
				// (...)
				// Activamos el registro de citas
				cambiarEstado(true);
			}
		} catch(...) {
			// (manejadores de excepciones)
		}
	}
}