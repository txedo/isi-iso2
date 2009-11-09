package excepciones;

public class UsuarioIncorrectoException extends Exception {

	private String mensajeError;

	public UsuarioIncorrectoException(String mensajeError) {
		this.mensajeError = mensajeError;
	}

	public String getMensajeError() {
		return mensajeError;
	}
	
}
