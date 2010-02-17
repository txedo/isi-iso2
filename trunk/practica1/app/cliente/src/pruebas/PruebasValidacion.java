package pruebas;

import java.util.Date;

import presentacion.Validacion;

import excepciones.ApellidoIncorrectoException;
import excepciones.CodigoPostalIncorrectoException;
import excepciones.ContraseñaIncorrectaException;
import excepciones.CorreoElectronicoIncorrectoException;
import excepciones.DomicilioIncorrectoException;
import excepciones.FechaNacimientoIncorrectaException;
import excepciones.IPInvalidaException;
import excepciones.LocalidadIncorrectaException;
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
import junit.framework.TestCase;

/**
 * Pruebas de la clase encargada de comprobar la validez de los
 * campos introducidos por el usuario.
 */
public class PruebasValidacion extends TestCase {
	
	public void setUp() {
		// No es necesario ningún código de inicialización
	}
	
	public void tearDown() {
		// No es necesario ningún código de finalización
	}
	
	/** Pruebas de NIFs */
	public void testNIFs() {
		String[] invalidos, validos;
		
		try {
			// Probamos NIFs incorrectos
			invalidos = new String[] { "", "  ", "abc", "1234", "1234d", "12345678", "1234567890x", "12345678?", "12345678ñ", "12345678á", "12345678Ç", "  12345678A", "12345678A  " };
			for(String nif : invalidos) {
				try {
					Validacion.comprobarNIF(nif);
					fail("El NIF '" + nif + "' debería ser inválido.");
				} catch(NIFIncorrectoException e) {
				}
			}
			// Probamos NIFs correctos
			validos = new String[] { "12345678D", "87654321d", "00000000W" };
			for(String nif : validos) {
				try {
					Validacion.comprobarNIF(nif);
				} catch(NIFIncorrectoException e) {
					fail("El NIF '" + nif + "' debería ser válido.");
				}
			}
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	/** Pruebas de NSSs */
	public void testNSSs() {
		String[] invalidos, validos;
		
		try {
			// Probamos NSSs incorrectos
			invalidos = new String[] { "", "  ", "abc", "1234", "123456789012x", "abcdefghijkl", "  123456789012", "123456789012  " };
			for(String nss : invalidos) {
				try {
					Validacion.comprobarNSS(nss);
					fail("El NSS '" + nss + "' debería ser inválido.");
				} catch(NSSIncorrectoException e) {
				}
			}
			// Probamos NSSs correctos
			validos = new String[] { "123456789012" };
			for(String nss : validos) {
				try {
					Validacion.comprobarNSS(nss);
				} catch(NSSIncorrectoException e) {
					fail("El NSS '" + nss + "' debería ser válido.");
				}
			}
		} catch(Exception e) {
			fail(e.toString());
		}
	}

	/** Pruebas de nombres */
	public void testNombres() {
		String[] invalidos, validos;
		
		try {
			// Probamos nombres incorrectos
			// TODO: "Ana  " no debe ser válido, no debe acabar en espacio
			invalidos = new String[] { "", "  ", "1234", "Pablo123", "Luis¿?", "Ana  ", "  Ana" };
			for(String nombre : invalidos) {
				try {
					Validacion.comprobarNombre(nombre);
					fail("El nombre '" + nombre + "' debería ser inválido.");
				} catch(NombreIncorrectoException e) {
				}
			}
			// Probamos nombres correctos
			// TODO: ¿Se deben soportar nombres con guiones, como "Juan-Luis"?
			validos = new String[] { "Juan", "José Luis", "ÁÉÍÓÚ áéíóú ÑñÇç" };
			for(String nombre : validos) {
				try {
					Validacion.comprobarNombre(nombre);
				} catch(NombreIncorrectoException e) {
					fail("El nombre '" + nombre + "' debería ser válido.");
				}
			}
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	/** Pruebas de apellidos */
	public void testApellidos() {
		String[] invalidos, validos;
		
		try {
			// Probamos apellidos incorrectos
			// TODO: "Ortiz  " no debe ser válido, no debe acabar en espacio
			invalidos = new String[] { "", "  ", "1234", "Muñoz 12345", "¿López?", "Ortiz  ", "  Ortiz" };
			for(String apellidos : invalidos) {
				try {
					Validacion.comprobarApellidos(apellidos);
					fail("El apellido '" + apellidos + "' debería ser inválido.");
				} catch(ApellidoIncorrectoException e) {
				}
			}
			// Probamos apellidos correctos
			// TODO: ¡Hay que permitir guiones!
			validos = new String[] { "Sancho Ramírez", "Jiménez Domínguez-García", "ÁÉÍÓÚ áéíóú ÑñÇç" };
			for(String apellidos : validos) {
				try {
					Validacion.comprobarApellidos(apellidos);
				} catch(ApellidoIncorrectoException e) {
					fail("El apellido '" + apellidos + "' debería ser válido.");
				}
			}
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	/** Pruebas de fechas */
	@SuppressWarnings("deprecation")
	public void testFechasNacimiento() {
		Date[] invalidos, validos;
		
		try {
			// Probamos fechas de nacimiento incorrectas
			invalidos = new Date[] { new Date(2020 - 1900, 4, 4) };
			for(Date fecha : invalidos) {
				try {
					Validacion.comprobarFechaNacimiento(fecha);
					fail("La fecha de nacimiento '" + fecha + "' debería ser inválida.");
				} catch(FechaNacimientoIncorrectaException e) {
				}
			}
			// Probamos fechas de nacimiento correctas
			validos = new Date[] { new Date(1920 - 1900, 4, 4) };
			for(Date fecha : validos) {
				try {
					Validacion.comprobarFechaNacimiento(fecha);
				} catch(FechaNacimientoIncorrectaException e) {
					fail("La fecha de nacimiento '" + fecha + "' debería ser válida.");
				}
			}
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	/** Pruebas de domicilios */
	public void testDomicilios() {
		String[] invalidos, validos;
		
		try {
			// Probamos domicilios incorrectos
			// TODO: No debe acabar en espacios, y se debería
			// comprobar que sólo hubiera letras y otros caracteres (/, -...).
			invalidos = new String[] { "", "  ", "1234", "¿¿??", "A$*^!·?=", "C/Mata  ", "  C/Mata" };
			for(String domicilio : invalidos) {
				try {
					Validacion.comprobarDomicilio(domicilio);
					fail("El domicilio '" + domicilio + "' debería ser inválido.");
				} catch(DomicilioIncorrectoException e) {
				}
			}
			// Probamos domicilios correctos
			validos = new String[] { "C/Mata", "C/Don Quijote", "Calle Santa María", "ÁÉÍÓÚ áéíóú ÑñÇç /\\-." };
			for(String domicilio : validos) {
				try {
					Validacion.comprobarDomicilio(domicilio);
				} catch(DomicilioIncorrectoException e) {
					fail("El domicilio '" + domicilio + "' debería ser válido.");
				}
			}
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	/** Pruebas de números de domicilios */
	public void testNumerosDomicilio() {
		String[] invalidos, validos;
		
		try {
			// Probamos números de domicilio incorrectos
			invalidos = new String[] { "", "  ", "abcd", "-12", "8A", "0,59", "1.890", "18  ", "  18" };
			for(String numero : invalidos) {
				try {
					Validacion.comprobarNumero(numero);
					fail("El número de domicilio '" + numero + "' debería ser inválido.");
				} catch(NumeroDomicilioIncorrectoException e) {
				}
			}
			// Probamos números de domicilio correctos
			validos = new String[] { "0", "18", "590" };
			for(String numero : validos) {
				try {
					Validacion.comprobarNumero(numero);
				} catch(NumeroDomicilioIncorrectoException e) {
					fail("El número de domicilio '" + numero + "' debería ser válido.");
				}
			}
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	/** Pruebas de pisos de domicilios */
	public void testPisosDomicilio() {
		String[] invalidos, validos;
		
		try {
			// Probamos pisos de domicilio incorrectos
			invalidos = new String[] { "", "  ", "abcd", "-12", "8A", "0,59", "1.890", "18  ", "  18" };
			for(String piso : invalidos) {
				try {
					Validacion.comprobarPiso(piso);
					fail("El piso de domicilio '" + piso + "' debería ser inválido.");
				} catch(PisoDomicilioIncorrectoException e) {
				}
			}
			// Probamos números de domicilio correctos
			validos = new String[] { "0", "18", "590" };
			for(String piso : validos) {
				try {
					Validacion.comprobarPiso(piso);
				} catch(PisoDomicilioIncorrectoException e) {
					fail("El piso de domicilio '" + piso + "' debería ser válido.");
				}
			}
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	/** Pruebas de puertas de domicilios */
	public void testPuertasDomicilio() {
		String[] invalidos, validos;
		
		try {
			// Probamos puertas de domicilio incorrectos
			invalidos = new String[] { "", " ", "abcd", "1234", "AB", "pq", "Ñ", "Ç", "Á", "é" };
			for(String puerta : invalidos) {
				try {
					Validacion.comprobarPuerta(puerta);
					fail("La puerta de domicilio '" + puerta + "' debería ser inválida.");
				} catch(PuertaDomicilioIncorrectoException e) {
				}
			}
			// Probamos puertas de domicilio correctos
			validos = new String[] { "A", "Z", "p", "w" };
			for(String puerta : validos) {
				try {
					Validacion.comprobarPuerta(puerta);
				} catch(PuertaDomicilioIncorrectoException e) {
					fail("La puerta de domicilio '" + puerta + "' debería ser válida.");
				}
			}
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	/** Pruebas de domicilios completos */
	public void testDomiciliosCompletos() {
		try {
			// Probamos domicilios completos válidos
			Validacion.comprobarDomicilioCompleto("Avda. Reyes Católicos", "14", "5", "A");
			Validacion.comprobarDomicilioCompleto("Avda. Reyes Católicos", "14", "", "");
			Validacion.comprobarDomicilioCompleto("Avda. Reyes Católicos", "", "", "");
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Probamos un domicilio con puerta pero sin número
			Validacion.comprobarDomicilioCompleto("Avda. Reyes Católicos", "", "", "A");
			fail("Se esperaba una excepción NumeroDomicilioIncorrectoException");
		} catch(NumeroDomicilioIncorrectoException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción NumeroDomicilioIncorrectoException");
		}

		try {
			// Probamos un domicilio con piso pero sin número
			Validacion.comprobarDomicilioCompleto("Avda. Reyes Católicos", "", "2", "");
			fail("Se esperaba una excepción NumeroDomicilioIncorrectoException");
		} catch(NumeroDomicilioIncorrectoException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción NumeroDomicilioIncorrectoException");
		}

		try {
			// Probamos un domicilio con puerta pero sin piso
			Validacion.comprobarDomicilioCompleto("Avda. Reyes Católicos", "14", "", "B");
			fail("Se esperaba una excepción PisoDomicilioIncorrectoException");
		} catch(PisoDomicilioIncorrectoException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción PisoDomicilioIncorrectoException");
		}

		try {
			// Probamos un domicilio con piso pero sin puerta
			Validacion.comprobarDomicilioCompleto("Avda. Reyes Católicos", "14", "2", "");
			fail("Se esperaba una excepción PuertaDomicilioIncorrectoException");
		} catch(PuertaDomicilioIncorrectoException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepción PuertaDomicilioIncorrectoException");
		}
	}
	
	/** Pruebas de localidades */
	public void testLocalidades() {
		String[] invalidos, validos;
		
		try {
			// Probamos localidades incorrectas
			invalidos = new String[] { "", "  ", "1234", "Ciudad 12345", "**Madrid**", "Valencia  ", "  Valencia" };
			for(String localidad : invalidos) {
				try {
					Validacion.comprobarLocalidad(localidad);
					fail("La localidad '" + localidad + "' debería ser inválida.");
				} catch(LocalidadIncorrectaException e) {
				}
			}
			// Probamos localidades correctas
			// TODO: ¿Deben soportarse ciudades con guiones, tipo "Aaaaa-bbbb"?
			validos = new String[] { "Mérida", "Ciudad Real", "ÁÉÍÓÚ áéíóú ÑñÇç" };
			for(String localidad : validos) {
				try {
					Validacion.comprobarLocalidad(localidad);
				} catch(LocalidadIncorrectaException e) {
					fail("La localidad '" + localidad + "' debería ser válida.");
				}
			}
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	/** Pruebas de códigos postales */
	public void testCodigosPostales() {
		String[] invalidos, validos;
		
		try {
			// Probamos códigos postales incorrectos
			invalidos = new String[] { "", "  ", "1234", "abc", "FGHIJ", "13000W", "**123", "18020  ", "  18020" };
			for(String codigo : invalidos) {
				try {
					Validacion.comprobarCodigoPostal(codigo);
					fail("El código postal '" + codigo + "' debería ser inválido.");
				} catch(CodigoPostalIncorrectoException e) {
				}
			}
			// Probamos códigos postales correctos
			validos = new String[] { "13002", "98444", "00004" };
			for(String codigo : validos) {
				try {
					Validacion.comprobarCodigoPostal(codigo);
				} catch(CodigoPostalIncorrectoException e) {
					fail("El código postal '" + codigo + "' debería ser válido.");
				}
			}
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	/** Pruebas de provincias */
	public void testProvincias() {
		String[] invalidos, validos;
		
		try {
			// Probamos provincias incorrectas
			invalidos = new String[] { "", "  ", "1234", "Provincia 12345", "**Badajoz**", "Sevilla  ", "  Sevilla" };
			for(String provincia : invalidos) {
				try {
					Validacion.comprobarProvincia(provincia);
					fail("La provincia '" + provincia + "' debería ser inválida.");
				} catch(ProvinciaIncorrectaException e) {
				}
			}
			// Probamos provincias correctas
			// TODO: ¿Deben soportarse provincias con guiones, tipo "Aaaaa-bbbb"?
			validos = new String[] { "Barcelona", "Ciudad Real", "ÁÉÍÓÚ áéíóú ÑñÇç" };
			for(String provincia : validos) {
				try {
					Validacion.comprobarProvincia(provincia);
				} catch(ProvinciaIncorrectaException e) {
					fail("La provincia '" + provincia + "' debería ser válida.");
				}
			}
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	/** Pruebas de correos electrónicos */
	public void testCorreosElectronicos() {
		String[] invalidos, validos;
		
		try {
			// Probamos correos electrónicos incorrectos
			// TODO: No debe constar sólo de espacios
			invalidos = new String[] { "", "  ", "1234", "abcd", "pedro@novale", "pedro.garcia@novale", "pedro.com", "a@.c", "maria@yahoo.es  ", "  maria@yahoo.es" };
			for(String correo : invalidos) {
				try {
					Validacion.comprobarCorreoElectronico(correo);
					fail("El correo electrónico '" + correo + "' debería ser inválido.");
				} catch(CorreoElectronicoIncorrectoException e) {
				}
			}
			// Probamos correos electrónicos correctos
			validos = new String[] { "maria@yahoo.es", "a12345@gmail.com", "abc-def@mail-example.com", "abc.def@mail.example.com" };
			for(String correo : validos) {
				try {
					Validacion.comprobarCorreoElectronico(correo);
				} catch(CorreoElectronicoIncorrectoException e) {
					fail("El correo electrónico '" + correo + "' debería ser válido.");
				}
			}
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	/** Pruebas de teléfonos fijos */
	public void testTelefonosFijos() {
		String[] invalidos, validos;
		
		try {
			// Probamos teléfonos fijos incorrectos
			invalidos = new String[] { "", "  ", "1234", "abcd", "555666777", "91 1201 888", "900112233x", "9001122??", "926111222  ", "  926111222" };
			for(String telefono : invalidos) {
				try {
					Validacion.comprobarTelefonoFijo(telefono);
					fail("El teléfono fijo '" + telefono + "' debería ser inválido.");
				} catch(TelefonoFijoIncorrectoException e) {
				}
			}
			// Probamos teléfonos fijos correctos
			validos = new String[] { "926111222" };
			for(String telefono : validos) {
				try {
					Validacion.comprobarTelefonoFijo(telefono);
				} catch(TelefonoFijoIncorrectoException e) {
					fail("El teléfono fijo '" + telefono + "' debería ser válido.");
				}
			}
		} catch(Exception e) {
			fail(e.toString());
		}	
	}
	
	/** Pruebas de teléfonos móviles */
	public void testTelefonosMoviles() {
		String[] invalidos, validos;
		
		try {
			// Probamos teléfonos móviles incorrectos
			invalidos = new String[] { "", "  ", "1234", "abcd", "555666777", "612 127 914", "600112233x", "6001122??", "626111222  ", "  626111222" };
			for(String telefono : invalidos) {
				try {
					Validacion.comprobarTelefonoMovil(telefono);
					fail("El teléfono móvil '" + telefono + "' debería ser inválido.");
				} catch(TelefonoMovilIncorrectoException e) {
				}
			}
			// Probamos teléfonos móviles correctos
			validos = new String[] { "626111222" };
			for(String telefono : validos) {
				try {
					Validacion.comprobarTelefonoMovil(telefono);
				} catch(TelefonoMovilIncorrectoException e) {
					fail("El teléfono móvil '" + telefono + "' debería ser válido.");
				}
			}
		} catch(Exception e) {
			fail(e.toString());
		}	
	}
	
	/** Pruebas de contraseñas */
	public void testContraseñas() {
		String[] invalidos, validos;
		
		try {
			// Probamos contraseñas incorrectas
			invalidos = new String[] { "", "  ", "abc123", "abc$$$123", "ÁÉÍÓÚáéíóú", "ÑÑÑÑññññ", "abc123456  ", "  abc123456" };
			for(String clave : invalidos) {
				try {
					Validacion.comprobarContraseña(clave);
					fail("La contraseña '" + clave + "' debería ser inválida.");
				} catch(ContraseñaIncorrectaException e) {
				}
			}
			// Probamos contraseñas correctas
			validos = new String[] { "abc123456", "12345678", "abcdefgh", "PASSword087" };
			for(String clave : validos) {
				try {
					Validacion.comprobarContraseña(clave);
				} catch(ContraseñaIncorrectaException e) {
					fail("La contraseña '" + clave + "' debería ser válida.");
				}
			}
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	/** Pruebas de direcciones IP */
	public void testDireccionesIP() {
		String[] invalidos, validos;
		
		try {
			// Probamos IPs incorrectas
			invalidos = new String[] { "", "  ", "abc", "1234", "300.0.0.300", "128.0.0.256", "128.0.0.-1", "127.0.0.1  ", "  127.0.0.1" };
			for(String ip : invalidos) {
				try {
					Validacion.comprobarDireccionIP(ip);
					fail("La dirección IP '" + ip + "' debería ser inválida.");
				} catch(IPInvalidaException e) {
				}
			}
			// Probamos IPs correctas
			validos = new String[] { "127.0.0.1", "34.98.240.10", "0.0.0.0", "255.255.255.255" };
			for(String ip : validos) {
				try {
					Validacion.comprobarDireccionIP(ip);
				} catch(IPInvalidaException e) {
					fail("La dirección IP '" + ip + "' debería ser válida.");
				}
			}
		} catch(Exception e) {
			fail(e.toString());
		}
	}

	/** Pruebas de puertos */
	public void testPuertos() {
		String[] invalidos, validos;
		
		try {
			// Probamos IPs incorrectas
			invalidos = new String[] { "", "  ", "-1", "0", "abcd", "65536", "400x", "3,45", "1.000", "100000" };
			for(String puerto : invalidos) {
				try {
					Validacion.comprobarPuerto(puerto);
					fail("El puerto '" + puerto + "' debería ser inválido.");
				} catch(PuertoInvalidoException e) {
				}
			}
			// Probamos IPs correctas
			validos = new String[] { "1", "65535", "100", "1000", "00001000" };
			for(String puerto : validos) {
				try {
					Validacion.comprobarPuerto(puerto);
				} catch(PuertoInvalidoException e) {
					fail("El puerto '" + puerto + "' debería ser válido.");
				}
			}
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
}
