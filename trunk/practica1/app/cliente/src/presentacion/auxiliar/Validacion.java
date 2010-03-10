package presentacion.auxiliar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dominio.conocimiento.IConstantes;
import excepciones.ApellidoIncorrectoException;
import excepciones.CadenaLongitudMaximaException;
import excepciones.CodigoPostalIncorrectoException;
import excepciones.CadenaIncorrectaException;
import excepciones.CadenaVaciaException;
import excepciones.EnteroLongitudMaximaException;
import excepciones.FechaCitaIncorrectaException;
import excepciones.FechaSustitucionIncorrectaException;
import excepciones.HoraSustitucionIncorrectaException;
import excepciones.IdVolanteIncorrectoException;
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

	public static final int PASSWORD_LONGITUD_MINIMA = 8;

	public static final int MAX_LONGITUD_CAMPOS_NUMERICOS = 15;
	public static final int MAX_LONGITUD_CAMPOS = 255;

	public static final int PUERTO_MINIMO = 1;
	public static final int PUERTO_MAXIMO = 65535;
	
	// Un NIF debe estar compuesto por 8 dígitos y 1 letra
	public static void comprobarNIF(String nif) throws NIFIncorrectoException {
		boolean bCorrecto = false;
		boolean bAux = true;
		
		// Comprobamos la longitud del NIF (8 números + 1 letra)
		if(nif.length() == NIF_LONGITUD) {
			// Comprobamos que el ultimo caracter es una letra
			if(esLetraAZ(nif.charAt(nif.length() - 1))) {
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
	
	// Un nombre sólo puede tener letras, espacios o guiones
	public static void comprobarNombre(String cadena) throws NombreIncorrectoException {
		try {
			comprobarCadena(cadena);
		} catch(CadenaIncorrectaException e) {
			throw new NombreIncorrectoException();
		} catch(CadenaVaciaException e) {
			throw new NombreIncorrectoException();
		} catch(CadenaLongitudMaximaException e) {
			throw new NombreIncorrectoException("El nombre supera la longitud máxima permitida de " + MAX_LONGITUD_CAMPOS + " caracteres.");
		}
	}

	// Los apellidos sólo pueden tener letras, espacios o guiones
	public static void comprobarApellidos(String cadena) throws ApellidoIncorrectoException {
		try {
			comprobarCadena(cadena);
		} catch(CadenaIncorrectaException e) {
			throw new ApellidoIncorrectoException();
		} catch(CadenaVaciaException e) {
			throw new ApellidoIncorrectoException();
		} catch(CadenaLongitudMaximaException e) {
			throw new ApellidoIncorrectoException("Los apellidos superan la longitud máxima permitida de " + MAX_LONGITUD_CAMPOS + " caracteres.");
		}
	}
	
	// La fecha de nacimiento debe ser válida y no anterior a la fecha actual
	public static void comprobarFechaNacimiento(Date date) throws FechaNacimientoIncorrectaException, FormatoFechaIncorrectoException {
		SimpleDateFormat sdf;
		
		sdf = new SimpleDateFormat("dd/MM/yyyy");
		try {
			sdf.format(date);
		} catch(Exception e) {
			throw new FormatoFechaIncorrectoException();
		}
		
		if(date.after(new Date())) {
			throw new FechaNacimientoIncorrectaException();
		}
	}
	
	public static void comprobarDomicilio(String cadena) throws DomicilioIncorrectoException {
		boolean bCorrecto = false;
		boolean bAux = true;
		
		if(cadena.length() > MAX_LONGITUD_CAMPOS) {
			throw new DomicilioIncorrectoException("El domicilio supera la longitud máxima permitida de " + MAX_LONGITUD_CAMPOS + " caracteres.");
		}
		
		// El primer caracter debe ser una letra
		if(cadena.length() > 0) {
			if(Character.isLetter(cadena.charAt(0))) {
				bAux = !cadena.contains("--");
				bAux = bAux && !cadena.contains("//");
				// El resto de caracteres pueden ser letra, espacio, guion o barra
				for(int i = 1; i < cadena.length() && bAux; i++) {
					bAux = cadena.charAt(i) == '/' || Character.isLetter(cadena.charAt(i)) || Character.isWhitespace(cadena.charAt(i)) || cadena.charAt(i) == '-';
				}
				bCorrecto = bAux;
				
				// No se puede terminar la cadena con un guión ni con espacios
				if ((bCorrecto && cadena.charAt(cadena.length()-1) == '-') || bCorrecto && Character.isWhitespace(cadena.charAt(cadena.length()-1)))
					bCorrecto = false;
				
			}
			if(!bCorrecto) {
				throw new DomicilioIncorrectoException();
			}
		} else {
			throw new DomicilioIncorrectoException();
		}
	}
	
	// Una cadena es válida si todos sus caracteres son alfabéticos, espacios o guiones
	public static void comprobarCadena(String cadena) throws CadenaIncorrectaException, CadenaVaciaException, CadenaLongitudMaximaException {
		boolean bCorrecto = false;
		boolean bAux = true;

		if(cadena.length() > MAX_LONGITUD_CAMPOS) {
			throw new CadenaLongitudMaximaException();
		}
		
		// El primer caracter debe ser una letra
		if(cadena.length() > 0) {
			if(Character.isLetter(cadena.charAt(0))) {
				bAux = !cadena.contains("--");
				// El resto de caracteres pueden ser letra, espacio o guion (no se permiten guiones juntos, como --)
				for(int i = 1; i < cadena.length() && bAux; i++) {
					bAux = Character.isLetter(cadena.charAt(i)) || Character.isWhitespace(cadena.charAt(i)) || cadena.charAt(i) == '-';
				}
				bCorrecto = bAux;
				
				// No se puede terminar la cadena con un guión ni con espacios
				if ((bCorrecto && cadena.charAt(cadena.length()-1) == '-') || bCorrecto && Character.isWhitespace(cadena.charAt(cadena.length()-1)))
					bCorrecto = false;
				
			}
			if(!bCorrecto) {
				throw new CadenaIncorrectaException();
			}
		} else {
			throw new CadenaVaciaException();
		}
	}
	
	// El número del domicilio debe ser un número acabado opcionalmente en una letra
	public static void comprobarNumero(String text) throws NumeroDomicilioIncorrectoException {
		boolean bAux = true;
		boolean bCorrecto = false;
		
		if(text.length() > MAX_LONGITUD_CAMPOS_NUMERICOS + 1
		 || (text.length() > MAX_LONGITUD_CAMPOS_NUMERICOS && !esLetraAZ(text.charAt(MAX_LONGITUD_CAMPOS_NUMERICOS)))) {
			throw new NumeroDomicilioIncorrectoException("El número de domicilio supera la longitud máxima permitida de " + MAX_LONGITUD_CAMPOS_NUMERICOS + " caracteres (sin incluir la letra).");
		}
		
		if (text.length() > 0) {
			for (int i = 0; i < text.length() && bAux; i++) {
				// Solo puede aparecer una letra en la última posición
				// de la cadena y no puede ser sólo una letra
				if (Character.isLetter(text.charAt(i))) {
					if (i == text.length()-1 && text.length() >1 )
						bAux = true;
					else
						bAux = false;
				} else
					bAux = Character.isDigit(text.charAt(i));
			}
			bCorrecto = bAux;
			
		}
		if (!bCorrecto)
			throw new NumeroDomicilioIncorrectoException();
		
	}

	// El piso del domicilio debe ser un número
	public static void comprobarPiso(String text) throws PisoDomicilioIncorrectoException {
		try {
			comprobarEntero(text);
		} catch(EnteroIncorrectoException e) {
			throw new PisoDomicilioIncorrectoException();
		} catch(EnteroLongitudMaximaException e) {
			throw new PisoDomicilioIncorrectoException("El piso del domicilio supera la longitud máxima permitida de " + MAX_LONGITUD_CAMPOS_NUMERICOS + " caracteres.");
		}	
	}

	// La puerta del domicilio debe ser una única letra
	public static void comprobarPuerta(String text) throws PuertaDomicilioIncorrectoException {
		try {
			comprobarLetra(text);
		} catch(LetraIncorrectaException e) {
			throw new PuertaDomicilioIncorrectoException();
		}
	}
	
	public static void comprobarLocalidad(String cadena) throws LocalidadIncorrectaException {
		try {
			comprobarCadena(cadena);
		} catch(CadenaIncorrectaException e) {
			throw new LocalidadIncorrectaException();
		} catch(CadenaVaciaException e) {
			throw new LocalidadIncorrectaException();
		} catch(CadenaLongitudMaximaException e) {
			throw new LocalidadIncorrectaException("La localidad supera la longitud máxima permitida de " + MAX_LONGITUD_CAMPOS + " caracteres.");
		}
	}
	
	public static void comprobarProvincia(String cadena) throws ProvinciaIncorrectaException {
		try {
			comprobarCadena(cadena);
		} catch(CadenaIncorrectaException e) {
			throw new ProvinciaIncorrectaException();
		} catch(CadenaVaciaException e) {
			throw new ProvinciaIncorrectaException();
		} catch(CadenaLongitudMaximaException e) {
			throw new ProvinciaIncorrectaException("La provincia supera la longitud máxima permitida de " + MAX_LONGITUD_CAMPOS + " caracteres.");
		}
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

	public static void comprobarEntero(String numero) throws EnteroIncorrectoException, EnteroLongitudMaximaException {
		// Todos los caracteres del numero deben ser digitos
		boolean bCorrecto = false;
		boolean bAux = true;

		if(numero.length() > MAX_LONGITUD_CAMPOS_NUMERICOS) {
			throw new EnteroLongitudMaximaException();
		}
		
		if (numero.length() > 0) {
			for (int i = 0; i < numero.length() && bAux; i++) {
				bAux = Character.isDigit(numero.charAt(i));
			}
			bCorrecto = bAux;
		}

		if (!bCorrecto)
			throw new EnteroIncorrectoException();
	}
	
	public static void comprobarCorreoElectronico(String correo) throws CorreoElectronicoIncorrectoException {
		Pattern patronCorreo;
		Matcher matcher;
		boolean bCorrecto = false;
		
		if(correo.length() > MAX_LONGITUD_CAMPOS) {
			throw new CorreoElectronicoIncorrectoException("El correo electrónico supera la longitud máxima permitida de " + MAX_LONGITUD_CAMPOS + " caracteres.");
		}
		
		// Creamos un patrón para definir el formato de un e-mail
		// Formato de e-mail siguiendo la norma RFC 2822
		// Referencia: http://www.regular-expressions.info/email.html
		patronCorreo = Pattern.compile("[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?");
		matcher = patronCorreo.matcher(correo);
	    if(matcher.matches()) {
	    	bCorrecto = true;
	    }

		if(!bCorrecto) {
			throw new CorreoElectronicoIncorrectoException();
		}
	}
	
	private static boolean comprobarTelefono(String telefono, char primerDigito) {
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
	
	public static void comprobarTelefonoFijo(String telefono) throws TelefonoFijoIncorrectoException {
		boolean bCorrecto = comprobarTelefono(telefono, '9');
		if(!bCorrecto)
			throw new TelefonoFijoIncorrectoException();
	}
	
	public static void comprobarTelefonoMovil(String telefono) throws TelefonoMovilIncorrectoException {
		boolean bCorrecto = comprobarTelefono(telefono, '6');
		if(!bCorrecto)
			throw new TelefonoMovilIncorrectoException();
	}
	
	public static void comprobarUsuario(String usuario) throws LoginIncorrectoException {
		Pattern patronUsuario;
	    Matcher matcher;
		boolean bCorrecto = false;
		
		if(usuario.length() > MAX_LONGITUD_CAMPOS) {
			throw new LoginIncorrectoException("El nombre de usuario supera la longitud máxima permitida de " + MAX_LONGITUD_CAMPOS + " caracteres.");
		}
		
		if(usuario.length() > 0) {
			// Nombre de usuario alfanumérico que comience por una letra
			patronUsuario = Pattern.compile("[a-zA-Z][a-zA-Z0-9]+");
		    matcher = patronUsuario.matcher(usuario);
		    if(matcher.matches())
	    		bCorrecto = true;
		}
		if (!bCorrecto)
			throw new LoginIncorrectoException();
	}
	
	public static void comprobarContraseña(String password) throws ContraseñaIncorrectaException {
		Pattern patronPassword;
	    Matcher matcher;
		boolean bCorrecto = false;
		
		if(password.length() > MAX_LONGITUD_CAMPOS) {
			throw new ContraseñaIncorrectaException("La contraseña supera la longitud máxima permitida de " + MAX_LONGITUD_CAMPOS + " caracteres.");
		}

		if(password.length() >= PASSWORD_LONGITUD_MINIMA) {
			// Contraseña alfanumérica
			patronPassword = Pattern.compile("[a-zA-Z0-9]+");
		    matcher = patronPassword.matcher(password);
		    if(matcher.matches())
	    		bCorrecto = true;
		}
		
	    if (!bCorrecto)
	    	throw new ContraseñaIncorrectaException();
	}
	
	// El id. del volante debe ser un número
	public static void comprobarVolante(String idVolante) throws IdVolanteIncorrectoException {
		try {
			comprobarEntero(idVolante);
		} catch(EnteroIncorrectoException e) {
			throw new IdVolanteIncorrectoException();
		} catch(EnteroLongitudMaximaException e) {
			throw new IdVolanteIncorrectoException("El identificador del volante supera la longitud máxima permitida de " + MAX_LONGITUD_CAMPOS_NUMERICOS + " caracteres.");
		}
	}

	// La fecha de la cita debe ser válida y posterior a la fecha actual
	public static void comprobarFechaCita(Date date) throws FormatoFechaIncorrectoException, FechaCitaIncorrectaException {
		SimpleDateFormat sdf;
		
		sdf = new SimpleDateFormat("dd/MM/yyyy");
		try {
			sdf.format(date);
		} catch(Exception e) {
			throw new FormatoFechaIncorrectoException();
		}
		
		if(date.before(new Date())) {
			throw new FechaCitaIncorrectaException();
		}
	}
	
	// La fecha de la sustitución debe ser válida y posterior a la fecha actual
	public static void comprobarFechaSustitucion(Date date) throws FormatoFechaIncorrectoException, FechaSustitucionIncorrectaException {
		SimpleDateFormat sdf;
		
		if(date == null) {
			throw new FechaSustitucionIncorrectaException();
		}
		
		sdf = new SimpleDateFormat("dd/MM/yyyy");
		try {
			sdf.format(date);
		} catch(Exception e) {
			throw new FormatoFechaIncorrectoException();
		}
		
		if(date.before(new Date())) {
			throw new FechaSustitucionIncorrectaException();
		}
	}
	
	// Las horas de la sustitución deben estar dentro de la jornada laboral y la inicial
	// debe ser menor que la final
	public static void comprobarHorasSustitucion(int horaInicial, int horaFinal) throws HoraSustitucionIncorrectaException {
		if(horaInicial < IConstantes.HORA_INICIO_JORNADA) {
			throw new HoraSustitucionIncorrectaException("La hora inicial de la sustitución queda fuera del horario laboral de los médicos.");
		} else if(horaFinal > IConstantes.HORA_FIN_JORNADA) {
			throw new HoraSustitucionIncorrectaException("La hora final de la sustitución queda fuera del horario laboral de los médicos.");
		} else if(horaInicial >= horaFinal) {
			throw new HoraSustitucionIncorrectaException("La hora final de la sustitución debe ser mayor que la inicial.");
		}
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
					throw new PuertoInvalidoException("El puerto debe ser un número comprendido entre " + PUERTO_MINIMO + " y " + PUERTO_MAXIMO + ".");
				}
			} catch(NumberFormatException ex) {
				throw new PuertoInvalidoException();
			}
		}
	}
	
	private static boolean esLetraAZ(char c) {
		return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
	}
	
}
