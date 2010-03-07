public class ControladorCliente {
	private ProxyServidorFrontend servidor;
	private ISesion sesion;
	public void iniciarSesion(ConfiguracionCliente configuracion, String login, String password, boolean registrar) throws ... {
		// Obtenemos la IP de la máquina local
		// (...)
		try {
			// Establecemos conexión con el servidor front-end
			servidor = new ProxyServidorFrontend();
			servidor.conectar(configuracion.getIPFrontend(), configuracion.getPuertoFrontend());
		} catch(...) {
			// (manejadores de excepciones)
		}
		try {
			// Nos identificamos en el servidor
			sesion = (ISesion)servidor.identificar(login, password);
			usuarioAutenticado = login;
		} catch(...) {
			// (manejadores de excepciones)
		}
		// Comprobamos si hay que registrar el cliente en el servidor
		registrado = registrar;
		if(registrar) {
			try {
				// Creamos y activamos el cliente remoto
				cliente = RemotoCliente.getCliente();
				cliente.activar(ipCliente);
				((Cliente)cliente.getClienteExportado()).setControlador(this);
				// Registramos el cliente en el servidor
				servidor.registrar((ICliente)cliente, sesion.getId());
			} catch(...) {
				// (manejadores de excepciones)
			}
		}
		// Ocultamos y cerramos las ventanas si todavía estaban abiertas
		if(ventanaLogin != null) {
			ventanaLogin.setVisible(false);
			ventanaLogin.dispose();
			ventanaLogin = null;
		}
		// (...)
		// Creamos la ventana principal y la mostramos
		ventanaPrincipal = new JFPrincipal(this);
		ventanaPrincipal.iniciar();
		ventanaPrincipal.setLocationRelativeTo(null);
		ventanaPrincipal.setVisible(true);
	}
}