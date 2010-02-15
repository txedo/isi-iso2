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
	// Operaciones del Gestor de Usuarios
	ConsultarUsuario,
	RegistrarUsuario,
	ModificarUsuario,
	EliminarUsuario,
	// Operaciones del Gestor de Médicos
	ConsultarMedico,
	RegistrarMedico,
	ModificarMedico,
	EliminarMedico,
	ConsultarMedicosTipo,
	ConsultarSustitutosPosibles,
	EstablecerSustituto,
	// Operaciones del Gestor de Citas
	ConsultarCitas,
	TramitarCita,
	TramitarCitaVolante,
	AnularCita,
	// Operaciones del Gestor de Volantes
	ConsultarVolante,
	EmitirVolante,
}
