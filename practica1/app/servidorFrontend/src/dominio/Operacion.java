package dominio;

/**
 * Enumeración con todas las operaciones que puede realizar el servidor del
 * sistema.
 */
public enum Operacion {
	ConsultarMedico,
	ConsultarUsuario,
	CrearUsuario,
	CrearMedico,
	ModificarMedico,
	EliminarMedico,
	ModificarUsuario,
	EliminarUsuario,
	TramitarCita,
	EliminarCita,
	RegistrarBeneficiario,
	ConsultarBeneficiario,
	ModificarBeneficiario,
	ModificarCalendario,
	EstablecerSustituto,
	ObtenerCitas
}
