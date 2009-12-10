package presentacion;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Utilidades {
	final static int NIF_LENGTH = 9;
	final static int NSS_LENGTH = 12;
	final static int TELEFONO_LENGTH = 9;
	
	public static void mostrarDialogoInformacion (JFrame ventanaPadre, String titulo, String mensaje) {
		JOptionPane.showMessageDialog(ventanaPadre, mensaje, titulo, JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static void mostrarDialogoError (JFrame ventanaPadre, String titulo, String mensaje) {
		JOptionPane.showMessageDialog(ventanaPadre, mensaje, titulo, JOptionPane.ERROR_MESSAGE);
	}
	
	public static void mostrarDialogoAdvertencia (JFrame ventanaPadre, String titulo, String mensaje) {
		JOptionPane.showMessageDialog(ventanaPadre, mensaje, titulo, JOptionPane.WARNING_MESSAGE);
	}
	
	public static boolean comprobarNIF (String nif) {
		// EL NIF deben ser 8 numeros y 1 letra
		boolean bCorrecto = false;
		boolean bAux = true;
		
		// Comprobamos la longitud del NIF 8numeros+1letra
		if (nif.length() == NIF_LENGTH) {
			// Comprobamos que el ultimo caracter es una letra
			if (!Character.isDigit(nif.charAt(nif.length()-1))) {
				// Comprobamos que los 8 primeros caracters son digitos
				for (int i = 0; i < NIF_LENGTH-2 && bAux; i++) {
					bAux = Character.isDigit(nif.charAt(i));
				}
				bCorrecto = bAux;
			}
		}
		return bCorrecto;
	}
	
	public static boolean comprobarNSS (String nss) {
		// EL NSS debe contener 12 digitos
		boolean bCorrecto = false;
		boolean bAux = true;
		
		// Comprobamos la longitud del NSS
		if (nss.length() == NSS_LENGTH) {
			// Comprobamos que los 12 caracters son digitos
			for (int i = 0; i < NIF_LENGTH-2 && bAux; i++) {
				bAux = Character.isDigit(nss.charAt(i));
			}
			bCorrecto = bAux;
		}
		return bCorrecto;
	}
	
	public static boolean comprobarCadena (String cadena) {
		// Todos los caracteres de la cadena deben ser alfabeticos
		boolean bCorrecto = false;
		boolean bAux = true;
		
		// El primer caracter debe ser una letra
		if (cadena.length() > 0) {
			if (Character.isLetter(cadena.charAt(0))) {
				// El resto de caracteres pueden ser letra o espacio
				for (int i = 1; i < cadena.length()-1 && bAux; i++) {
					bAux = Character.isLetter(cadena.charAt(i)) || Character.isWhitespace(cadena.charAt(i));
				}
				bCorrecto = bAux;
			}
		}

		return bCorrecto;
	}
	
	public static boolean comprobarDomicilio (String cadena) {
		// Todos los caracteres de la cadena deben ser alfabeticos
		boolean bCorrecto = false;
		
		// El primer caracter debe ser una letra
		if (cadena.length() > 0) {
			if (Character.isLetter(cadena.charAt(0))) {
				bCorrecto = true;
			}
		}

		return bCorrecto;
	}
	
	public static boolean comprobarLetra (String cadena) {
		boolean bCorrecto = false;
		boolean bAux = true;
		
		// Debe ser un solo caracter y debe ser una letra
		if (cadena.length() == 1)
			if (Character.isLetter(cadena.charAt(0)))
				bCorrecto = bAux;

		return bCorrecto;
	}

	public static boolean comprobarNumeroEntero (String numero) {
		// Todos los caracteres del numero deben ser digitos
		boolean bCorrecto = false;
		boolean bAux = true;
		
		if (numero.length() > 0) {
			for (int i = 0; i < numero.length()-1 && bAux; i++) {
				bAux = Character.isDigit(numero.charAt(i));
			}
			bCorrecto = bAux;
		}

		return bCorrecto;
	}
	
	public static boolean comprobarCorreoElectronico (String correo) {
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


		return bCorrecto;
	}
	
	public static boolean comprobarTelefono (String telefono) {
		// El telefono deben ser 9 digitos
		boolean bCorrecto = false;
		boolean bAux = true;
		
		if (telefono.length() == TELEFONO_LENGTH){
			for (int i = 0; i < telefono.length()-1 && bAux; i++) {
				bAux = Character.isDigit(telefono.charAt(i));
			}
			bCorrecto = bAux;
		}

		return bCorrecto;
	}
}
