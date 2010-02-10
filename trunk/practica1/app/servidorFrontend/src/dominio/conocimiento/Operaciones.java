package dominio.conocimiento;

/**
 * Enumeración con todas las operaciones que puede realizar el servidor del
 * sistema.
 */
public enum Operaciones {
	CrearUsuario,
	ConsultarUsuario,
	ModificarUsuario,
	EliminarUsuario,
	TramitarCita,
	ConsultarCitas,
	AnularCita,
	EmitirVolante,
	RegistrarBeneficiario,
	ConsultarBeneficiario,
	ModificarBeneficiario,
	EliminarBeneficiario,
	RegistrarMedico,
	ConsultarMedico,
	EliminarMedico,
	ModificarMedico,
	ModificarCalendario,
	EstablecerSustituto,
	ConsultarMedicosTipo,
	CalcularDiasCompletosMedico,
	ConsultarVolante
}
