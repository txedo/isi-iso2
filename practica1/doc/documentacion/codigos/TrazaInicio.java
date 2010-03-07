public class Main {
	public static void main(String args[]) {
		// Mostramos la ventana de identificación
		cont = new ControladorCliente();
		cont.identificarse();
	}
}

public class ControladorCliente {
	private JFLogin ventanaLogin;
	public void identificarse() {
		// Ocultamos y cerramos las ventanas si todavía estaban abiertas
		// (...)
		// Creamos la ventana de login y la mostramos
		ventanaLogin = new JFLogin();
		ventanaLogin.setControlador(this);
		ventanaLogin.setLocationRelativeTo(null);
		ventanaLogin.setVisible(true);
	}
}