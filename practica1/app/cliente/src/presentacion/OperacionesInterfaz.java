package presentacion;

/**
 * Enumeración con todas las operaciones disponibles en la interfaz
 * gráfica de los clientes.
 */
public enum OperacionesInterfaz {
	// Panel de beneficiarios
	RegistrarBeneficiario,
	ConsultarBeneficiario,
	ConsultarModificarBeneficiario,
	// Panel de usuarios
	RegistrarUsuario,
	ConsultarUsuario,
	ConsultarModificarUsuario,
	// Panel de citas
	TramitarCita,
	TramitarCitaVolante,
	ConsultarAnularCitaBeneficiario,
	ConsultarCitaMedico,
	// Panel de sustituciones
	EstablecerSustituto,
	// Panel de volantes
	EmitirVolante,
	// Otras operaciones
	OperacionInvalida
}
