package dominio;

/**
 * Enumeración con todas las operaciones que puede realizar el servidor del
 * sistema.
 */
public enum Operacion {
	ConsultarUsuario,
	CrearUsuario,
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
