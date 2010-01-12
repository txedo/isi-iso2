package presentacion;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import excepciones.*;

public class Utilidades {
	
	private final static int NIF_LENGTH = 9;
	private final static int NSS_LENGTH = 12;
	private final static int TELEFONO_LENGTH = 9;
	
	public static void mostrarDialogoInformacion (JFrame ventanaPadre, String titulo, String mensaje) {
		JOptionPane.showMessageDialog(ventanaPadre, mensaje, titulo, JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static void mostrarDialogoError (JFrame ventanaPadre, String titulo, String mensaje) {
		JOptionPane.showMessageDialog(ventanaPadre, mensaje, titulo, JOptionPane.ERROR_MESSAGE);
	}
	
	public static void mostrarDialogoAdvertencia (JFrame ventanaPadre, String titulo, String mensaje) {
		JOptionPane.showMessageDialog(ventanaPadre, mensaje, titulo, JOptionPane.WARNING_MESSAGE);
	}
	
	public static void comprobarNIF (String nif) throws NIFIncorrectoException {
		// EL NIF deben ser 8 numeros y 1 letra
		boolean bCorrecto = false;
		boolean bAux = true;
		
		// Comprobamos la longitud del NIF 8numeros+1letra
		if (nif.length() == NIF_LENGTH) {
			// Comprobamos que el ultimo caracter es una letra
			if (Character.isLetter(nif.charAt(nif.length()-1))) {
				// Comprobamos que los 8 primeros caracters son digitos
				for (int i = 0; i < NIF_LENGTH-1 && bAux; i++) {
					bAux = Character.isDigit(nif.charAt(i));
				}
				bCorrecto = bAux;
			}
		}
		if (!bCorrecto)
			throw new NIFIncorrectoException();
	}
	
	public static void comprobarNSS (String nss) throws NSSIncorrectoException {
		// EL NSS debe contener 12 digitos
		boolean bCorrecto = false;
		boolean bAux = true;
		
		// Comprobamos la longitud del NSS
		if (nss.length() == NSS_LENGTH) {
			// Comprobamos que los 12 caracters son digitos
			for (int i = 0; i < NSS_LENGTH && bAux; i++) {
				bAux = Character.isDigit(nss.charAt(i));
			}
			bCorrecto = bAux;
		}
		if (!bCorrecto)
			throw new NSSIncorrectoException();
	}
	
	private static void comprobarCadena (String cadena) throws CadenaIncorrectaException, CadenaVaciaException {
		// Todos los caracteres de la cadena deben ser alfabeticos
		boolean bCorrecto = false;
		boolean bAux = true;
		
		// El primer caracter debe ser una letra
		if (cadena.length() > 0) {
			if (Character.isLetter(cadena.charAt(0))) {
				// El resto de caracteres pueden ser letra o espacio
				for (int i = 1; i < cadena.length() && bAux; i++) {
					bAux = Character.isLetter(cadena.charAt(i)) || Character.isWhitespace(cadena.charAt(i));
				}
				bCorrecto = bAux;
			}
			if (!bCorrecto)
				throw new CadenaIncorrectaException();
		}
		else {
			throw new CadenaVaciaException();
		}
	}
	
	public static void comprobarNombre (String cadena) throws NombreIncorrectoException {
		try {
			comprobarCadena (cadena);
		} catch (CadenaIncorrectaException e) {
			throw new NombreIncorrectoException();
		} catch (CadenaVaciaException e) {
			throw new NombreIncorrectoException();
		}
	}
	
	public static void comprobarApellidos (String cadena) throws ApellidoIncorrectoException {
		try {
			comprobarCadena (cadena);
		} catch (CadenaIncorrectaException e) {
			throw new ApellidoIncorrectoException();
		} catch (CadenaVaciaException e) {
			throw new ApellidoIncorrectoException();
		}
	}
	
	public static void comprobarDomicilio (String cadena) throws DomicilioIncorrectoException {
		// Todos los caracteres de la cadena deben ser alfabeticos
		boolean bCorrecto = false;
		
		// El primer caracter debe ser una letra
		if (cadena.length() > 0) {
			if (Character.isLetter(cadena.charAt(0))) {
				bCorrecto = true;
			}
		}
		if (!bCorrecto)
			throw new DomicilioIncorrectoException();
	}
	
	private static void comprobarLetra (String cadena) throws LetraIncorrectaException {
		boolean bCorrecto = false;
		boolean bAux = true;
		
		// Debe ser un solo caracter y debe ser una letra
		if (cadena.length() == 1)
			if (Character.isLetter(cadena.charAt(0)))
				bCorrecto = bAux;

		if (!bCorrecto)
			throw new LetraIncorrectaException();
	}

	private static void comprobarNumeroEntero (String numero) throws NumeroIncorrectoException {
		// Todos los caracteres del numero deben ser digitos
		boolean bCorrecto = false;
		boolean bAux = true;
		
		if (numero.length() > 0) {
			for (int i = 0; i < numero.length()-1 && bAux; i++) {
				bAux = Character.isDigit(numero.charAt(i));
			}
			bCorrecto = bAux;
		}

		if (!bCorrecto)
			throw new NumeroIncorrectoException();
	}
	
	public static void comprobarCorreoElectronico (String correo) throws CorreoElectronicoIncorrectoException {
		boolean bCorrecto = false;
		boolean bAux = true;
		
		if (correo.length() > 0) {
			// Obtenemos la posicion del caracter @
			int indexArroba = correo.indexOf("@");
			// Obtenemos la posicion del "." separador de dominio. Debe ir despues de la @ y no consecutivo
			int indexPunto = correo.indexOf(".", indexArroba+2);
			
			// El primer y ultimo caracter debe ser una letra
			if (Character.isLetter(correo.charAt(0)) && Character.isLetter(correo.charAt(correo.length()-1))) {
				// Si el correo tiene arroba y punto (despues de la arroba)
				if (indexArroba != -1 && indexPunto != -1) {
					// El resto de caracteres NO pueden ser espacios
					for (int i = 1; i < correo.length()-1 && bAux; i++) {
						bAux = !Character.isWhitespace(correo.charAt(i));
					}
					bCorrecto = bAux;
				}
			}
		}
		else {
			// Admitimos que el correo sea nulo ya que es opcional
			bCorrecto = true;
		}
		if (!bCorrecto)
			throw new CorreoElectronicoIncorrectoException();
	}
	
	private static boolean comprobarTelefono (String telefono, char primerDigito) {
		// El telefono deben ser 9 digitos
		boolean bCorrecto = false;
		boolean bAux = true;
		
		if (telefono.length() == TELEFONO_LENGTH){
			if (telefono.charAt(0) == primerDigito) {
				for (int i = 0; i < telefono.length()-1 && bAux; i++) {
					bAux = Character.isDigit(telefono.charAt(i));
				}
				bCorrecto = bAux;
			}
		}

		return bCorrecto;
	}
	
	public static void comprobarTelefonoFijo (String telefono) throws TelefonoFijoIncorrectoException {
		boolean bCorrecto = comprobarTelefono (telefono, '9');
		if (!bCorrecto)
			throw new TelefonoFijoIncorrectoException();
	}
	
	public static void comprobarTelefonoMovil (String telefono) throws TelefonoMovilIncorrectoException {
		boolean bCorrecto = comprobarTelefono (telefono, '6');
		if (!bCorrecto)
			throw new TelefonoMovilIncorrectoException();
	}

	public static void comprobarNumero(String text) throws NumeroDomicilioIncorrectoException {
		try {
			comprobarNumeroEntero(text);
		} catch (NumeroIncorrectoException e) {
			throw new NumeroDomicilioIncorrectoException ();
		}		
	}

	public static void comprobarPiso(String text) throws PisoDomicilioIncorrectoException {
		try {
			comprobarNumeroEntero(text);
		} catch (NumeroIncorrectoException e) {
			throw new PisoDomicilioIncorrectoException ();
		}	
	}

	public static void comprobarPuerta(String text) throws PuertaDomicilioIncorrectoException {
		try {
			comprobarLetra(text);
		} catch (LetraIncorrectaException e) {
			throw new PuertaDomicilioIncorrectoException();
		}		
	}
	
	public static void comprobarFecha(Date date) throws FechaNacimientoIncorrectaException, FormatoFechaIncorrectoException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try {
			sdf.format(date);
		} catch (Exception e) {
			throw new FormatoFechaIncorrectoException();
		}
		Date now = new Date();
		if (date.after(now)) throw new FechaNacimientoIncorrectaException();

	}
	
	public static String fechaToString(Date date) {
		String fecha = "";
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		fecha = sdf.format(date);
		return fecha;
	}
}
