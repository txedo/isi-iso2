public class JPCitaTramitar extends JPBase {
	private void pnlBeneficiarioBeneficiarioBuscado(EventObject evt) {
		// Borramos los datos de la �ltima tramitaci�n de cita
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
			// un m�dico asignado
			if(beneficiario.getMedicoAsignado() == null) {
				// (...)
			} else {
				// Consultamos al servidor toda la informaci�n
				// necesaria para el panel de tramitaci�n
				diasOcupados = getControlador().consultarDiasCompletos(beneficiario.getMedicoAsignado().getNif());
				citasOcupadas = getControlador().consultarHorasCitasMedico(beneficiario.getMedicoAsignado().getNif());
				horasCitas = getControlador().consultarHorarioMedico(beneficiario.getMedicoAsignado().getNif());
				// Deshabilitamos los d�as de la semana que no son
				// laborables para el m�dico del beneficiario
				// (...)
				// Deshabilitamos los d�as que el m�dico no puede
				// pasar consulta en el calendario del panel
				// (...)
				// Buscamos el primer d�a y hora disponible para una cita
				// (...)
				// Activamos el registro de citas
				cambiarEstado(true);
			}
		} catch(...) {
			// (manejadores de excepciones)
		}
	}
}