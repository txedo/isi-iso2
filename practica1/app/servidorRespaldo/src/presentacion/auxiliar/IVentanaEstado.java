package presentacion.auxiliar;

/**
 * Interfaz para las ventanas que muestran el estado del servidor
 * (los mensajes generados y el número de clientes conectados).
 */
public interface IVentanaEstado {
	
	public void ponerMensaje(String mensaje);
	
	public void actualizarClientesEscuchando(int numeroClientes);
	
}
