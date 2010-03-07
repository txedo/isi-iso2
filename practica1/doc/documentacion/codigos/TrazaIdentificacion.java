public class JFLogin extends javax.swing.JFrame {
	private void btnConectarActionPerformed(ActionEvent evt) {
		try {
			// Comprobamos los campos de la ventana
			Validacion.comprobarUsuario(txtUsuario.getText().trim());
			Validacion.comprobarContraseña(new String(txtPassword.getPassword()));
			Validacion.comprobarDireccionIP(txtDireccionServidor.getText().trim());
			Validacion.comprobarPuerto(txtPuertoServidor.getText().trim());
			puerto = Integer.parseInt(txtPuertoServidor.getText().trim());
			controlador.iniciarSesion(new ConfiguracionCliente(txtDireccionServidor.getText(), puerto), txtUsuario.getText(), new String(txtPassword.getPassword()));
		} catch(UsuarioIncorrectoException e) {
			// (usuario inexistente)
		} catch(LoginIncorrectoException e) {
			// (nombre de usuario incorrecto)
		} catch(ContraseñaIncorrectaException e) {
			// (contraseña incorrecta)
		} catch(...) {
			// (otros manejadores de excepciones)
		}
	}
}