package presentacion.auxiliar;

/**
 * Enumeraci�n con todas las operaciones disponibles en la interfaz
 * gr�fica de los clientes.
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
	ConsultarAnularCitasBeneficiario,
	ConsultarCitasMedico,
	// Panel de sustituciones
	EstablecerSustituto,
	// Panel de volantes
	EmitirVolante,
	// Otras operaciones
	OperacionInvalida
}
