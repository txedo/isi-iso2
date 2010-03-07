public class JPCitaTramitar extends JPBase {
	private void cmbHorasCitasItemStateChanged(ItemEvent evt) {
		if(evt.getStateChange() == ItemEvent.SELECTED) {
			mostrarMedicoCita();
		}
	}
	private void mostrarMedicoCita() {
		try {
			if(cmbHorasCitas.getSelectedItem() != null && horaSeleccionadaValida()) {
				// Obtenemos la hora de la cita
				hora = Cita.horaCadenaCita(cmbHorasCitas.getSelectedItem().toString());
				fecha = dtcDiaCita.getDate();
				diaCita = new Date(fecha.getYear(), fecha.getMonth(), fecha.getDate(), hora.getHours(), hora.getMinutes());
				// Consultamos qué médico daría realmente la cita
				medico = getControlador().consultarMedicoCita(beneficiario.getMedicoAsignado().getNif(), diaCita);
				if(medico.getNif().equals(beneficiario.getMedicoAsignado().getNif())) {
					txtMedico.setText(medico.getApellidos() + ", " + medico.getNombre() + " (" + medico.getNif() + ")");
				} else {
					txtMedico.setText(medico.getApellidos() + ", " + medico.getNombre() + " (" + medico.getNif() + "), sustituye a " + beneficiario.getMedicoAsignado().getApellidos() + ", " + beneficiario.getMedicoAsignado().getNombre() + " (" + beneficiario.getMedicoAsignado().getNif() + ")");
				}
			} else {
				txtMedico.setText("(fecha no válida)");
			}
		} catch(...) {
			// (manejadores de excepciones)
		}
	}
}