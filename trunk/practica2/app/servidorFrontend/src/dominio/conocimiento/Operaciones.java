package dominio.conocimiento;

/**
 * Enumeración con todas las operaciones que puede realizar el servidor del
 * sistema.
 */
public enum Operaciones {
	// Operaciones del Gestor de Beneficiarios
	ConsultarBeneficiario,
	RegistrarBeneficiario,
	ModificarBeneficiario,
	EliminarBeneficiario,
	ConsultarBeneficiariosMedico,
	// Operaciones del Gestor de Usuarios
	ConsultarUsuario,
	RegistrarUsuario,
	ModificarUsuario,
	EliminarUsuario,
	CorrespondeNIFUsuario,
	ConsultarPropioUsuario,
	ConsultarCentros,
	// Operaciones del Gestor de Médicos
	ConsultarMedico,
	RegistrarMedico,
	ModificarMedico,
	EliminarMedico,
	ConsultarMedicosTipo,
	ConsultarSustitutosPosibles,
	EstablecerSustituto,
	ConsultarMedicoCita,
	ConsultarPropioMedico,
	// Operaciones del Gestor de Citas
	ConsultarCitasBeneficiario,
	ConsultarCitasMedico,
	TramitarCita,
	TramitarCitaVolante,
	AnularCita,
	ConsultarCitasPropiasMedico,
	// Operaciones del Gestor de Volantes
	ConsultarVolante,
	EmitirVolante
}
