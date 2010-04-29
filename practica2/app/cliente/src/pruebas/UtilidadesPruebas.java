package pruebas;

import java.util.Random;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import org.uispec4j.Button;
import org.uispec4j.ListBox;
import org.uispec4j.Trigger;
import org.uispec4j.Window;
import org.uispec4j.interception.WindowHandler;
import org.uispec4j.interception.WindowInterceptor;
import comunicaciones.ConfiguracionCliente;
import comunicaciones.RemotoCliente;
import comunicaciones.UtilidadesComunicaciones;
import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.Medico;
import dominio.conocimiento.Usuario;
import dominio.control.ControladorCliente;
import excepciones.BeneficiarioYaExistenteException;
import excepciones.UsuarioYaExistenteException;
import presentacion.auxiliar.Validacion;

/**
 * Clase estática con métodos auxiliares utilizados en las pruebas.
 */
public class UtilidadesPruebas {

	private static Random rnd = new Random(System.currentTimeMillis());

	private static Window ventanaMostrada;
	
	private static ControladorCliente controladorCabecera;
	private static ControladorCliente controlador = null;
	private static Window winControlador = null;
	private static String loginStatic, passwordStatic;

	// Crea un nuevo beneficiario en el sistema, asegurándose de que no exista ya
	public static Beneficiario crearBeneficiario(ControladorCliente controlador, Beneficiario beneficiario) throws Exception {
		boolean valido;
		
		// Mientras el beneficiario exista, se le asigna un nuevo NIF y NSS
		beneficiario.setNif(generarNIF());					
		beneficiario.setNss(generarNSS());
		do {
			valido = false;
			try {					
				controlador.crearBeneficiario(beneficiario);
				valido = true;
			} catch(BeneficiarioYaExistenteException e) {
				beneficiario.setNif(generarNIF());					
				beneficiario.setNss(generarNSS());
			}
		} while(!valido);
		
		// Consultamos el beneficiario por si el médico se asignó aleatoriamente
		beneficiario = controlador.consultarBeneficiarioPorNIF(beneficiario.getNif());
		
		return beneficiario;
	}

	// Crea un nuevo usuario en el sistema, asegurándose de que no exista ya
	public static Usuario crearUsuario(ControladorCliente controlador, Usuario usuario) throws Exception {
		boolean valido;

		// Mientras el usuario exista, se le asigna un nuevo NIF y login
		usuario.setNif(generarNIF());					
		usuario.setLogin(generarLogin());
		usuario.setPassword(usuario.getLogin());
		do {
			valido = false;
			try {					
				controlador.crearUsuario(usuario);
				valido = true;
			} catch(UsuarioYaExistenteException e) {
				usuario.setNif(generarNIF());
				usuario.setLogin(generarLogin());
				usuario.setPassword(usuario.getLogin());
			}
		} while(!valido);
		
		// Consultamos el usuario para obtener el centro de salud asignado aleatoriamente
		usuario = controlador.consultarUsuario(usuario.getNif());
		
		return usuario;
	}
	
	// Emite un volante para un beneficiario utilizando una nueva sesión como médico
	public static long emitirVolante(Beneficiario beneficiario, Medico emisor, Medico receptor, String login, String password) throws Exception {
		Window winCabecera;
		long idVolante;

		// Iniciamos sesión como médico con un nuevo controlador
		loginStatic = login;
		passwordStatic = password;
		controladorCabecera = new ControladorCliente();
		winCabecera = WindowInterceptor.run(new Trigger() {
			public void run() throws Exception {
				controladorCabecera.iniciarSesion(new ConfiguracionCliente(IDatosConexionPruebas.IP_SERVIDOR_FRONTEND, IDatosConexionPruebas.PUERTO_SERVIDOR_FRONTEND), loginStatic, passwordStatic, false);
			}
		});
		
		// Emitimos un nuevo volante con los datos indicados
		idVolante = controladorCabecera.emitirVolante(beneficiario, emisor, receptor);
		
		// Cerramos la sesión y liberamos los recursos usados
		controladorCabecera.cerrarSesion();
		controladorCabecera.cerrarControlador();
		winCabecera.dispose();

		return idVolante;
	}

	// Inicia sesión con un nuevo usuario en un controlador auxiliar
	public static ControladorCliente crearControladorAuxiliar(String login, String password) throws Exception {
		// Iniciamos sesión con un nuevo controlador
		loginStatic = login;
		passwordStatic = password;
		controlador = new ControladorCliente();
		winControlador = WindowInterceptor.run(new Trigger() {
			public void run() throws Exception {
				controlador.iniciarSesion(new ConfiguracionCliente(IDatosConexionPruebas.IP_SERVIDOR_FRONTEND, IDatosConexionPruebas.PUERTO_SERVIDOR_FRONTEND), loginStatic, passwordStatic, false);
			}
		});

		return controlador;
	}
	
	// Cierra la sesión abierta con el método crearControladorAuxiliar
	public static void cerrarControladorAuxiliar() throws Exception {
		if(controlador != null) {
			controlador.cerrarSesion();
			controlador.cerrarControlador();
			RemotoCliente.getCliente().activar(UtilidadesComunicaciones.obtenerIPHost());
			controlador = null;
		}
		if(winControlador != null) {
			winControlador.dispose();
			winControlador = null;
		}
	}
	
	// Comprueba que en una lista aparezcan los usuarios indicados
	public static boolean comprobarListaUsuarios(ListBox lista, Usuario[] usuarios) {
		JList jlista;
		String nombreUsuario;
		boolean encontrado, dev;
		int i;
		
		dev = true;
		jlista = lista.getAwtComponent();
		if(jlista.getModel().getSize() != usuarios.length) {
			dev = false;
		} else {
			for(Usuario usuario : usuarios) {
				// Buscamos el usuario en la lista
				encontrado = false;
				nombreUsuario = usuario.getApellidos() + ", " + usuario.getNombre() + " (" + usuario.getNif() + ")";
				for(i = 0; i < usuarios.length; i++) {
					if(nombreUsuario.equals(jlista.getModel().getElementAt(i))) {
						encontrado = true;
					}
				}
				if(!encontrado) {
					dev = false;
				}
			}
		}
		
		return dev;
	}
	
	// Devuelve el texto del cuadro de diálogo mostrado al pulsar el botón indicado
	public static String obtenerTextoDialogo(Button btn) {
		String mensaje = "";
		JOptionPane optionPane = null;
		JDialog dialogo = null;
		WindowInterceptor.init(btn.triggerClick()).process(
				new WindowHandler() {
					public Trigger process(Window window) {
						ventanaMostrada = window;
						return Trigger.DO_NOTHING;
					}
				}).run();
		// El JOptionPane está encapsulado dentro del dialogo (JDialog) que devuelve el WindowInterceptor
		dialogo = ((JDialog)ventanaMostrada.getAwtComponent()); 
		optionPane = (JOptionPane) dialogo.getContentPane().getComponents()[0];
		mensaje = optionPane.getMessage().toString();
		return mensaje;
	}
	
	// Devuelve el texto del cuadro de diálogo mostrado al pulsar el botón indicado,
	// y cierra el cuadro de diálogo pulsando el botón con el texto especificado
	public static String obtenerTextoDialogo(Button btn, final String txtBotonCierre) {
		String mensaje = "";
		JOptionPane optionPane = null;
		JDialog dialogo = null;
		WindowInterceptor.init(btn.triggerClick()).process(
				new WindowHandler() {
					public Trigger process(Window window) {
						ventanaMostrada = window;
						return window.getButton(txtBotonCierre).triggerClick();
					}
				}).run();
		// El JOptionPane está encapsulado dentro del dialogo (JDialog) que devuelve el WindowInterceptor
		dialogo = ((JDialog)ventanaMostrada.getAwtComponent()); 
		optionPane = (JOptionPane) dialogo.getContentPane().getComponents()[0];
		mensaje = optionPane.getMessage().toString();
		return mensaje;
	}
	
	// Devuelve el texto del segundo cuadro de diálogo mostrado al pulsar el botón
	// indicado, y cierra ambos diálogos pulsando los botones con los textos especificados
	public static String obtenerTextoSegundoDialogo(Button btn, final String txtBotonCierre1, final String txtBotonCierre2) {
		String mensaje = "";
		JOptionPane optionPane = null;
		JDialog dialogo = null;
		WindowInterceptor.init(btn.triggerClick()).process(
				new WindowHandler() {
					public Trigger process(Window window) {
						return window.getButton(txtBotonCierre1).triggerClick();
					}
				}).process(new WindowHandler() {
					public Trigger process(Window window) {
						ventanaMostrada = window;
						return window.getButton(txtBotonCierre2).triggerClick();
					}
				}).run();
		// El JOptionPane está encapsulado dentro del dialogo (JDialog) que devuelve el WindowInterceptor
		dialogo = ((JDialog)ventanaMostrada.getAwtComponent()); 
		optionPane = (JOptionPane) dialogo.getContentPane().getComponents()[0];
		mensaje = optionPane.getMessage().toString();
		return mensaje;
	}
	
	public static String generarLogin() {
		return ("usuario" + rnd.nextInt(999999999));
	}			
	
	public static String generarNIF() {
		String nif = "";
		String letra = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		
		// Generamos un NIF aleatorio
		for(int i = 0; i < Validacion.NIF_LONGITUD - 1; i++) {
			nif = nif + String.valueOf(rnd.nextInt(10));
		}
		nif = nif + letra.charAt(rnd.nextInt(letra.length()-1));
		
		return nif;
	}
	
	public static String generarNSS() {
		String nss = "";
		
		// Generamos un NSS aleatorio
		for(int i = 0; i < Validacion.NSS_LONGITUD; i++) {
			nss = nss + String.valueOf(rnd.nextInt(10));
		}
		
		return nss;
	}
	
}
