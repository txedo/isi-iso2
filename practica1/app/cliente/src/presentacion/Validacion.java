package presentacion;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import excepciones.ApellidoIncorrectoException;
import excepciones.CodigoPostalIncorrectoException;
import excepciones.CadenaIncorrectaException;
import excepciones.CadenaVaciaException;
import excepciones.LocalidadIncorrectaException;
import excepciones.ContraseñaIncorrectaException;
import excepciones.CorreoElectronicoIncorrectoException;
import excepciones.DomicilioIncorrectoException;
import excepciones.EnteroIncorrectoException;
import excepciones.FechaNacimientoIncorrectaException;
import excepciones.FormatoFechaIncorrectoException;
import excepciones.IPInvalidaException;
import excepciones.LetraIncorrectaException;
import excepciones.LoginIncorrectoException;
import excepciones.NIFIncorrectoException;
import excepciones.NSSIncorrectoException;
import excepciones.NombreIncorrectoException;
import excepciones.NumeroDomicilioIncorrectoException;
import excepciones.PisoDomicilioIncorrectoException;
import excepciones.ProvinciaIncorrectaException;
import excepciones.PuertaDomicilioIncorrectoException;
import excepciones.PuertoInvalidoException;
import excepciones.TelefonoFijoIncorrectoException;
import excepciones.TelefonoMovilIncorrectoException;

/**
 * Clase estática que contiene métodos para comprobar la validez
 * de los campos de las ventanas.
 */
public class Validacion {

	public static final int NIF_LONGITUD = 9;
	public static final int NSS_LONGITUD = 12;
	public static final int CP_LONGITUD = 5;
	public static final int TELEFONO_LONGITUD = 9;
	public static final int PUERTO_MINIMO = 1;
	public static final int PUERTO_MAXIMO = 65535;
	
	// Un NIF debe estar compuesto por 8 dígitos y 1 letra
	public static void comprobarNIF(String nif) throws NIFIncorrectoException {
		boolean bCorrecto = false;
		boolean bAux = true;
		
		// Comprobamos la longitud del NIF 8numeros+1letra
		if(nif.length() == NIF_LONGITUD) {
			// Comprobamos que el ultimo caracter es una letra
			if((nif.charAt(nif.length() - 1) >= 'A' && nif.charAt(nif.length() - 1) <= 'Z')
			 || (nif.charAt(nif.length() - 1) >= 'a' && nif.charAt(nif.length() - 1) <= 'z')) {
				// Comprobamos que los 8 primeros caracteres son dígitos
				for(int i = 0; i < NIF_LONGITUD-1 && bAux; i++) {
					bAux = Character.isDigit(nif.charAt(i));
				}
				bCorrecto = bAux;
			}
		}
		
		if(!bCorrecto) {
			throw new NIFIncorrectoException();
		}
	}
	
	// Un NSS debe estar compuesto por 12 dígitos
	public static void comprobarNSS(String nss) throws NSSIncorrectoException {
		boolean bCorrecto = false;
		boolean bAux = true;
		
		// Comprobamos la longitud del NSS
		if(nss.length() == NSS_LONGITUD) {
			// Comprobamos que los 12 caracters son digitos
			for(int i = 0; i < NSS_LONGITUD && bAux; i++) {
				bAux = Character.isDigit(nss.charAt(i));
			}
			bCorrecto = bAux;
		}
		
		if(!bCorrecto) {
			throw new NSSIncorrectoException();
		}
	}
	
	// Una cadena es válida si todos sus caracteres son alfabéticos o espacios
	public static void comprobarCadena(String cadena) throws CadenaIncorrectaException, CadenaVaciaException {
		boolean bCorrecto = false;
		boolean bAux = true;
		
		// El primer caracter debe ser una letra
		if(cadena.length() > 0) {
			if(Character.isLetter(cadena.charAt(0))) {
				// El resto de caracteres pueden ser letra o espacio
				for(int i = 1; i < cadena.length() && bAux; i++) {
					bAux = Character.isLetter(cadena.charAt(i)) || Character.isWhitespace(cadena.charAt(i));
				}
				bCorrecto = bAux;
			}
			if(!bCorrecto) {
				throw new CadenaIncorrectaException();
			}
		} else {
			throw new CadenaVaciaException();
		}
	}
	
	public static void comprobarNombre(String cadena) throws NombreIncorrectoException {
		try {
			comprobarCadena(cadena);
		} catch(CadenaIncorrectaException e) {
			throw new NombreIncorrectoException();
		} catch(CadenaVaciaException e) {
			throw new NombreIncorrectoException();
		}
	}
	
	public static void comprobarApellidos(String cadena) throws ApellidoIncorrectoException {
		try {
			comprobarCadena(cadena);
		} catch(CadenaIncorrectaException e) {
			throw new ApellidoIncorrectoException();
		} catch(CadenaVaciaException e) {
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
	
	// Una letra sólo puede estar en el rango A - Z o a - z (sin contar la ñ)
	private static void comprobarLetra(String cadena) throws LetraIncorrectaException {
		boolean bCorrecto = false;
		
		if(cadena.length() == 1) {
			if((cadena.toUpperCase().charAt(0) >= 'A' && cadena.toUpperCase().charAt(0) <= 'Z')) {
				bCorrecto = true;
			}
		}

		if(!bCorrecto) {
			throw new LetraIncorrectaException();
		}
	}
	
	public static void comprobarDomicilioCompleto(String domicilio, String numero, String piso, String puerta) throws DomicilioIncorrectoException, NumeroDomicilioIncorrectoException, PisoDomicilioIncorrectoException, PuertaDomicilioIncorrectoException {
		comprobarDomicilio(domicilio);
		if(numero.equals("")) {
			// Si no se indica número tampoco se puede indicar piso y puerta
			if(!piso.equals("")) {
				throw new NumeroDomicilioIncorrectoException("El número del domicilio es obligatorio si se introduce la piso.");
			} else if(!puerta.equals("")) {
				throw new NumeroDomicilioIncorrectoException("El número del domicilio es obligatorio si se introduce la puerta.");
			}
		} else {
			comprobarNumero(numero);
			if(piso.equals("")) {
				// Si no se indica piso tampoco se puede indicar puerta
				if(!puerta.equals("")) {
					throw new PisoDomicilioIncorrectoException("El piso del domicilio es obligatorio si se introduce la puerta.");
				}
			} else {
				comprobarPiso(piso);
				// Si se indica piso es obligatoria la puerta
				if(puerta.equals("")) {
					throw new PuertaDomicilioIncorrectoException("La puerta del domicilio es obligatoria si se introduce el piso.");
				} else {
					comprobarPuerta(puerta);
				}
			}
		}
	}
	
	public static void comprobarLocalidad(String cadena) throws LocalidadIncorrectaException {
		try {
			comprobarCadena(cadena);
		} catch(CadenaIncorrectaException e) {
			throw new LocalidadIncorrectaException();
		} catch(CadenaVaciaException e) {
			throw new LocalidadIncorrectaException();
		}
	}
	
	public static void comprobarProvincia(String cadena) throws ProvinciaIncorrectaException {
		try {
			comprobarCadena(cadena);
		} catch(CadenaIncorrectaException e) {
			throw new ProvinciaIncorrectaException();
		} catch(CadenaVaciaException e) {
			throw new ProvinciaIncorrectaException();
		}
	}
	
	public static void comprobarCodigoPostal(String cp) throws CodigoPostalIncorrectoException {
		boolean bCorrecto = false;
		boolean bAux = true;
		
		// Comprobamos la longitud del CP
		if(cp.length() == CP_LONGITUD) {
			// Comprobamos que los 5 caracteres son dígitos
			for(int i = 0; i < CP_LONGITUD && bAux; i++) {
				bAux = Character.isDigit(cp.charAt(i));
			}
			bCorrecto = bAux;
		}
		
		if(!bCorrecto) {
			throw new CodigoPostalIncorrectoException();
		}
	}

	public static void comprobarEntero (String numero) throws EnteroIncorrectoException {
		// Todos los caracteres del numero deben ser digitos
		boolean bCorrecto = false;
		boolean bAux = true;
		
		if (numero.length() > 0) {
			for (int i = 0; i < numero.length() && bAux; i++) {
				bAux = Character.isDigit(numero.charAt(i));
			}
			bCorrecto = bAux;
		}

		if (!bCorrecto)
			throw new EnteroIncorrectoException();
	}
	
	public static void comprobarCorreoElectronico (String correo) throws CorreoElectronicoIncorrectoException {
		boolean bCorrecto = false;
		boolean bAux = true;
		
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

		if(!bCorrecto) {
			throw new CorreoElectronicoIncorrectoException();
		}
	}
	
	private static boolean comprobarTelefono (String telefono, char primerDigito) {
		// El telefono deben ser 9 digitos
		boolean bCorrecto = false;
		boolean bAux = true;
		
		if (telefono.length() == TELEFONO_LONGITUD){
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
			comprobarEntero(text);
		} catch (EnteroIncorrectoException e) {
			throw new NumeroDomicilioIncorrectoException ();
		}		
	}

	public static void comprobarPiso(String text) throws PisoDomicilioIncorrectoException {
		try {
			comprobarEntero(text);
		} catch (EnteroIncorrectoException e) {
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
	
	public static void comprobarFechaNacimiento(Date date) throws FechaNacimientoIncorrectaException, FormatoFechaIncorrectoException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try {
			sdf.format(date);
		} catch (Exception e) {
			throw new FormatoFechaIncorrectoException();
		}
		Date now = new Date();
		if (date.after(now)) throw new FechaNacimientoIncorrectaException();

	}
	
	public static void comprobarUsuario(String usuario) throws LoginIncorrectoException {
		//TODO:Por hacer
		if(usuario.length() == 0) {
			throw new LoginIncorrectoException();
		}
	}
	
	public static void comprobarContraseña(String pass) throws ContraseñaIncorrectaException {
		boolean valido = false;
		// Contraseña alfanumérica de mínimo 8 caracteres
		Pattern p = Pattern.compile("[a-zA-Z0-9]+");
	    Matcher m = p.matcher(pass);
	    if (m.matches())
	    	if (pass.length() > 7)
	    		valido = true;
	    if (!valido)
	    	throw new ContraseñaIncorrectaException();
	}
	
	public static void comprobarDireccionIP(String ip) throws IPInvalidaException {
		Pattern patronIP;
		
		// Creamos un patrón que define las IPs válidas
		patronIP = Pattern.compile("\\b(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." + 
				"(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\b");
		
		if(ip.equals("")) {
			throw new IPInvalidaException("La dirección IP no puede ser nula.");
		} else if(!patronIP.matcher(ip).matches()) {
			throw new IPInvalidaException();
		}
	}
	
	public static void comprobarPuerto(String puerto) throws PuertoInvalidoException {
		int numPuerto;
	
		if(puerto.equals("")) {
			throw new PuertoInvalidoException("El puerto no puede ser nulo.");
		} else {
			try {
				numPuerto = Integer.parseInt(puerto);
				if(numPuerto < PUERTO_MINIMO || numPuerto > PUERTO_MAXIMO) {
					throw new PuertoInvalidoException();
				}
			} catch(NumberFormatException ex) {
				throw new PuertoInvalidoException();
			}
		}
	}
	
}
