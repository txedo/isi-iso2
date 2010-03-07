public class JPBeneficiarioConsultar extends JPBase {
	private Beneficiario beneficiario;
	private void btnBuscarActionPerformed(ActionEvent evt) {
		// Borramos la información del antiguo beneficiario consultado
		limpiarCamposConsulta();
		try {
			// Obtenemos el identificador para buscar el beneficiario
			identificacion = txtIdentificacion.getText().toUpperCase().trim();
			tipo = (String)cmbIdentificacion.getSelectedItem();
			if(identificacion.equals("")) {
				throw new CadenaVaciaException();
			}
			// Buscamos el beneficiario solicitado
			if(tipo.equals(ID_NIF)) {
				Validacion.comprobarNIF(identificacion);
				beneficiarioBuscado = getControlador().consultarBeneficiarioPorNIF(identificacion);
			} else if(tipo.equals(ID_NSS)) {
				Validacion.comprobarNSS(identificacion);
				beneficiarioBuscado = getControlador().consultarBeneficiarioPorNSS(identificacion);
			}
			// Mostramos los resultados de la búsqueda
			// (...)
		} catch(BeneficiarioInexistenteException e) {
			// (beneficiario inexistente)
		} catch(CadenaVaciaException e) {
			// (NIF o NSS vacío)
		} catch(NIFIncorrectoException e) {
			// (NIF incorrecto)
		} catch(NSSIncorrectoException e) {
			// (NSS incorrecto)
		} catch(...) {
			// (otros manejadores de excepciones)
		}
		// Mostramos los datos del beneficiario encontrado
		mostrarDatosBeneficiario(beneficiarioBuscado);
	}
	private void mostrarDatosBeneficiario(Beneficiario beneficiario) {
		// Actualizamos el beneficiario
		this.beneficiario = beneficiario;
		// Mostramos los datos del beneficiario encontrado
		if(beneficiario != null) {
			txtIdentificacion.setText("");
			txtNIF.setText(beneficiario.getNif());
			txtNSS.setText(beneficiario.getNss());
			txtNombre.setText(beneficiario.getNombre());
			txtApellidos.setText(beneficiario.getApellidos());
			dtcFechaNacimiento.setDate(beneficiario.getFechaNacimiento());
			// (inicialización de otros campos)
			if(beneficiario.getMedicoAsignado() == null) {
				txtMedicoAsignado.setText("(ninguno)");
			} else {
				txtMedicoAsignado.setText(beneficiario.getMedicoAsignado().getApellidos() + ", " + beneficiario.getMedicoAsignado().getNombre() + " (" + beneficiario.getMedicoAsignado().getNif() + ")");
			}
			cmbCentros.setSelectedIndex(centros.indexOf(beneficiario.getCentroSalud()));
		}
		// Notificamos que ha cambiado el beneficiario seleccionado
		listeners = listenerList.getListenerList();
		for(i = 0; i < listeners.length; i += 2) {
			if(listeners[i] == BeneficiarioBuscadoListener.class) {
				((BeneficiarioBuscadoListener)listeners[i + 1]).beneficiarioBuscado(new EventObject(this));
			}
		}
	}
}