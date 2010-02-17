package pruebas;

import java.util.Date;

import presentacion.Validacion;

import excepciones.ApellidoIncorrectoException;
import excepciones.CodigoPostalIncorrectoException;
import excepciones.Contrase�aIncorrectaException;
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
		// No es necesario ning�n c�digo de inicializaci�n
	}
	
	public void tearDown() {
		// No es necesario ning�n c�digo de finalizaci�n
	}
	
	/** Pruebas de NIFs */
	public void testNIFs() {
		String[] invalidos, validos;
		
		try {
			// Probamos NIFs incorrectos
			invalidos = new String[] { "", "  ", "abc", "1234", "1234d", "12345678", "1234567890x", "12345678?", "12345678�", "12345678�", "12345678�", "  12345678A", "12345678A  " };
			for(String nif : invalidos) {
				try {
					Validacion.comprobarNIF(nif);
					fail("El NIF '" + nif + "' deber�a ser inv�lido.");
				} catch(NIFIncorrectoException e) {
				}
			}
			// Probamos NIFs correctos
			validos = new String[] { "12345678D", "87654321d", "00000000W" };
			for(String nif : validos) {
				try {
					Validacion.comprobarNIF(nif);
				} catch(NIFIncorrectoException e) {
					fail("El NIF '" + nif + "' deber�a ser v�lido.");
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
					fail("El NSS '" + nss + "' deber�a ser inv�lido.");
				} catch(NSSIncorrectoException e) {
				}
			}
			// Probamos NSSs correctos
			validos = new String[] { "123456789012" };
			for(String nss : validos) {
				try {
					Validacion.comprobarNSS(nss);
				} catch(NSSIncorrectoException e) {
					fail("El NSS '" + nss + "' deber�a ser v�lido.");
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
			// TODO: "Ana  " no debe ser v�lido, no debe acabar en espacio
			invalidos = new String[] { "", "  ", "1234", "Pablo123", "Luis�?", "Ana  ", "  Ana" };
			for(String nombre : invalidos) {
				try {
					Validacion.comprobarNombre(nombre);
					fail("El nombre '" + nombre + "' deber�a ser inv�lido.");
				} catch(NombreIncorrectoException e) {
				}
			}
			// Probamos nombres correctos
			// TODO: �Se deben soportar nombres con guiones, como "Juan-Luis"?
			validos = new String[] { "Juan", "Jos� Luis", "����� ����� ����" };
			for(String nombre : validos) {
				try {
					Validacion.comprobarNombre(nombre);
				} catch(NombreIncorrectoException e) {
					fail("El nombre '" + nombre + "' deber�a ser v�lido.");
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
			// TODO: "Ortiz  " no debe ser v�lido, no debe acabar en espacio
			invalidos = new String[] { "", "  ", "1234", "Mu�oz 12345", "�L�pez?", "Ortiz  ", "  Ortiz" };
			for(String apellidos : invalidos) {
				try {
					Validacion.comprobarApellidos(apellidos);
					fail("El apellido '" + apellidos + "' deber�a ser inv�lido.");
				} catch(ApellidoIncorrectoException e) {
				}
			}
			// Probamos apellidos correctos
			// TODO: �Hay que permitir guiones!
			validos = new String[] { "Sancho Ram�rez", "Jim�nez Dom�nguez-Garc�a", "����� ����� ����" };
			for(String apellidos : validos) {
				try {
					Validacion.comprobarApellidos(apellidos);
				} catch(ApellidoIncorrectoException e) {
					fail("El apellido '" + apellidos + "' deber�a ser v�lido.");
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
					fail("La fecha de nacimiento '" + fecha + "' deber�a ser inv�lida.");
				} catch(FechaNacimientoIncorrectaException e) {
				}
			}
			// Probamos fechas de nacimiento correctas
			validos = new Date[] { new Date(1920 - 1900, 4, 4) };
			for(Date fecha : validos) {
				try {
					Validacion.comprobarFechaNacimiento(fecha);
				} catch(FechaNacimientoIncorrectaException e) {
					fail("La fecha de nacimiento '" + fecha + "' deber�a ser v�lida.");
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
			// TODO: No debe acabar en espacios, y se deber�a
			// comprobar que s�lo hubiera letras y otros caracteres (/, -...).
			invalidos = new String[] { "", "  ", "1234", "��??", "A$*^!�?=", "C/Mata  ", "  C/Mata" };
			for(String domicilio : invalidos) {
				try {
					Validacion.comprobarDomicilio(domicilio);
					fail("El domicilio '" + domicilio + "' deber�a ser inv�lido.");
				} catch(DomicilioIncorrectoException e) {
				}
			}
			// Probamos domicilios correctos
			validos = new String[] { "C/Mata", "C/Don Quijote", "Calle Santa Mar�a", "����� ����� ���� /\\-." };
			for(String domicilio : validos) {
				try {
					Validacion.comprobarDomicilio(domicilio);
				} catch(DomicilioIncorrectoException e) {
					fail("El domicilio '" + domicilio + "' deber�a ser v�lido.");
				}
			}
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	/** Pruebas de n�meros de domicilios */
	public void testNumerosDomicilio() {
		String[] invalidos, validos;
		
		try {
			// Probamos n�meros de domicilio incorrectos
			invalidos = new String[] { "", "  ", "abcd", "-12", "8A", "0,59", "1.890", "18  ", "  18" };
			for(String numero : invalidos) {
				try {
					Validacion.comprobarNumero(numero);
					fail("El n�mero de domicilio '" + numero + "' deber�a ser inv�lido.");
				} catch(NumeroDomicilioIncorrectoException e) {
				}
			}
			// Probamos n�meros de domicilio correctos
			validos = new String[] { "0", "18", "590" };
			for(String numero : validos) {
				try {
					Validacion.comprobarNumero(numero);
				} catch(NumeroDomicilioIncorrectoException e) {
					fail("El n�mero de domicilio '" + numero + "' deber�a ser v�lido.");
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
					fail("El piso de domicilio '" + piso + "' deber�a ser inv�lido.");
				} catch(PisoDomicilioIncorrectoException e) {
				}
			}
			// Probamos n�meros de domicilio correctos
			validos = new String[] { "0", "18", "590" };
			for(String piso : validos) {
				try {
					Validacion.comprobarPiso(piso);
				} catch(PisoDomicilioIncorrectoException e) {
					fail("El piso de domicilio '" + piso + "' deber�a ser v�lido.");
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
			invalidos = new String[] { "", " ", "abcd", "1234", "AB", "pq", "�", "�", "�", "�" };
			for(String puerta : invalidos) {
				try {
					Validacion.comprobarPuerta(puerta);
					fail("La puerta de domicilio '" + puerta + "' deber�a ser inv�lida.");
				} catch(PuertaDomicilioIncorrectoException e) {
				}
			}
			// Probamos puertas de domicilio correctos
			validos = new String[] { "A", "Z", "p", "w" };
			for(String puerta : validos) {
				try {
					Validacion.comprobarPuerta(puerta);
				} catch(PuertaDomicilioIncorrectoException e) {
					fail("La puerta de domicilio '" + puerta + "' deber�a ser v�lida.");
				}
			}
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	/** Pruebas de domicilios completos */
	public void testDomiciliosCompletos() {
		try {
			// Probamos domicilios completos v�lidos
			Validacion.comprobarDomicilioCompleto("Avda. Reyes Cat�licos", "14", "5", "A");
			Validacion.comprobarDomicilioCompleto("Avda. Reyes Cat�licos", "14", "", "");
			Validacion.comprobarDomicilioCompleto("Avda. Reyes Cat�licos", "", "", "");
		} catch(Exception e) {
			fail(e.toString());
		}
		
		try {
			// Probamos un domicilio con puerta pero sin n�mero
			Validacion.comprobarDomicilioCompleto("Avda. Reyes Cat�licos", "", "", "A");
			fail("Se esperaba una excepci�n NumeroDomicilioIncorrectoException");
		} catch(NumeroDomicilioIncorrectoException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n NumeroDomicilioIncorrectoException");
		}

		try {
			// Probamos un domicilio con piso pero sin n�mero
			Validacion.comprobarDomicilioCompleto("Avda. Reyes Cat�licos", "", "2", "");
			fail("Se esperaba una excepci�n NumeroDomicilioIncorrectoException");
		} catch(NumeroDomicilioIncorrectoException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n NumeroDomicilioIncorrectoException");
		}

		try {
			// Probamos un domicilio con puerta pero sin piso
			Validacion.comprobarDomicilioCompleto("Avda. Reyes Cat�licos", "14", "", "B");
			fail("Se esperaba una excepci�n PisoDomicilioIncorrectoException");
		} catch(PisoDomicilioIncorrectoException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n PisoDomicilioIncorrectoException");
		}

		try {
			// Probamos un domicilio con piso pero sin puerta
			Validacion.comprobarDomicilioCompleto("Avda. Reyes Cat�licos", "14", "2", "");
			fail("Se esperaba una excepci�n PuertaDomicilioIncorrectoException");
		} catch(PuertaDomicilioIncorrectoException e) {
		} catch(Exception e) {
			fail("Se esperaba una excepci�n PuertaDomicilioIncorrectoException");
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
					fail("La localidad '" + localidad + "' deber�a ser inv�lida.");
				} catch(LocalidadIncorrectaException e) {
				}
			}
			// Probamos localidades correctas
			// TODO: �Deben soportarse ciudades con guiones, tipo "Aaaaa-bbbb"?
			validos = new String[] { "M�rida", "Ciudad Real", "����� ����� ����" };
			for(String localidad : validos) {
				try {
					Validacion.comprobarLocalidad(localidad);
				} catch(LocalidadIncorrectaException e) {
					fail("La localidad '" + localidad + "' deber�a ser v�lida.");
				}
			}
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	/** Pruebas de c�digos postales */
	public void testCodigosPostales() {
		String[] invalidos, validos;
		
		try {
			// Probamos c�digos postales incorrectos
			invalidos = new String[] { "", "  ", "1234", "abc", "FGHIJ", "13000W", "**123", "18020  ", "  18020" };
			for(String codigo : invalidos) {
				try {
					Validacion.comprobarCodigoPostal(codigo);
					fail("El c�digo postal '" + codigo + "' deber�a ser inv�lido.");
				} catch(CodigoPostalIncorrectoException e) {
				}
			}
			// Probamos c�digos postales correctos
			validos = new String[] { "13002", "98444", "00004" };
			for(String codigo : validos) {
				try {
					Validacion.comprobarCodigoPostal(codigo);
				} catch(CodigoPostalIncorrectoException e) {
					fail("El c�digo postal '" + codigo + "' deber�a ser v�lido.");
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
					fail("La provincia '" + provincia + "' deber�a ser inv�lida.");
				} catch(ProvinciaIncorrectaException e) {
				}
			}
			// Probamos provincias correctas
			// TODO: �Deben soportarse provincias con guiones, tipo "Aaaaa-bbbb"?
			validos = new String[] { "Barcelona", "Ciudad Real", "����� ����� ����" };
			for(String provincia : validos) {
				try {
					Validacion.comprobarProvincia(provincia);
				} catch(ProvinciaIncorrectaException e) {
					fail("La provincia '" + provincia + "' deber�a ser v�lida.");
				}
			}
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	/** Pruebas de correos electr�nicos */
	public void testCorreosElectronicos() {
		String[] invalidos, validos;
		
		try {
			// Probamos correos electr�nicos incorrectos
			// TODO: No debe constar s�lo de espacios
			invalidos = new String[] { "", "  ", "1234", "abcd", "pedro@novale", "pedro.garcia@novale", "pedro.com", "a@.c", "maria@yahoo.es  ", "  maria@yahoo.es" };
			for(String correo : invalidos) {
				try {
					Validacion.comprobarCorreoElectronico(correo);
					fail("El correo electr�nico '" + correo + "' deber�a ser inv�lido.");
				} catch(CorreoElectronicoIncorrectoException e) {
				}
			}
			// Probamos correos electr�nicos correctos
			validos = new String[] { "maria@yahoo.es", "a12345@gmail.com", "abc-def@mail-example.com", "abc.def@mail.example.com" };
			for(String correo : validos) {
				try {
					Validacion.comprobarCorreoElectronico(correo);
				} catch(CorreoElectronicoIncorrectoException e) {
					fail("El correo electr�nico '" + correo + "' deber�a ser v�lido.");
				}
			}
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	/** Pruebas de tel�fonos fijos */
	public void testTelefonosFijos() {
		String[] invalidos, validos;
		
		try {
			// Probamos tel�fonos fijos incorrectos
			invalidos = new String[] { "", "  ", "1234", "abcd", "555666777", "91 1201 888", "900112233x", "9001122??", "926111222  ", "  926111222" };
			for(String telefono : invalidos) {
				try {
					Validacion.comprobarTelefonoFijo(telefono);
					fail("El tel�fono fijo '" + telefono + "' deber�a ser inv�lido.");
				} catch(TelefonoFijoIncorrectoException e) {
				}
			}
			// Probamos tel�fonos fijos correctos
			validos = new String[] { "926111222" };
			for(String telefono : validos) {
				try {
					Validacion.comprobarTelefonoFijo(telefono);
				} catch(TelefonoFijoIncorrectoException e) {
					fail("El tel�fono fijo '" + telefono + "' deber�a ser v�lido.");
				}
			}
		} catch(Exception e) {
			fail(e.toString());
		}	
	}
	
	/** Pruebas de tel�fonos m�viles */
	public void testTelefonosMoviles() {
		String[] invalidos, validos;
		
		try {
			// Probamos tel�fonos m�viles incorrectos
			invalidos = new String[] { "", "  ", "1234", "abcd", "555666777", "612 127 914", "600112233x", "6001122??", "626111222  ", "  626111222" };
			for(String telefono : invalidos) {
				try {
					Validacion.comprobarTelefonoMovil(telefono);
					fail("El tel�fono m�vil '" + telefono + "' deber�a ser inv�lido.");
				} catch(TelefonoMovilIncorrectoException e) {
				}
			}
			// Probamos tel�fonos m�viles correctos
			validos = new String[] { "626111222" };
			for(String telefono : validos) {
				try {
					Validacion.comprobarTelefonoMovil(telefono);
				} catch(TelefonoMovilIncorrectoException e) {
					fail("El tel�fono m�vil '" + telefono + "' deber�a ser v�lido.");
				}
			}
		} catch(Exception e) {
			fail(e.toString());
		}	
	}
	
	/** Pruebas de contrase�as */
	public void testContrase�as() {
		String[] invalidos, validos;
		
		try {
			// Probamos contrase�as incorrectas
			invalidos = new String[] { "", "  ", "abc123", "abc$$$123", "����������", "��������", "abc123456  ", "  abc123456" };
			for(String clave : invalidos) {
				try {
					Validacion.comprobarContrase�a(clave);
					fail("La contrase�a '" + clave + "' deber�a ser inv�lida.");
				} catch(Contrase�aIncorrectaException e) {
				}
			}
			// Probamos contrase�as correctas
			validos = new String[] { "abc123456", "12345678", "abcdefgh", "PASSword087" };
			for(String clave : validos) {
				try {
					Validacion.comprobarContrase�a(clave);
				} catch(Contrase�aIncorrectaException e) {
					fail("La contrase�a '" + clave + "' deber�a ser v�lida.");
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
					fail("La direcci�n IP '" + ip + "' deber�a ser inv�lida.");
				} catch(IPInvalidaException e) {
				}
			}
			// Probamos IPs correctas
			validos = new String[] { "127.0.0.1", "34.98.240.10", "0.0.0.0", "255.255.255.255" };
			for(String ip : validos) {
				try {
					Validacion.comprobarDireccionIP(ip);
				} catch(IPInvalidaException e) {
					fail("La direcci�n IP '" + ip + "' deber�a ser v�lida.");
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
					fail("El puerto '" + puerto + "' deber�a ser inv�lido.");
				} catch(PuertoInvalidoException e) {
				}
			}
			// Probamos IPs correctas
			validos = new String[] { "1", "65535", "100", "1000", "00001000" };
			for(String puerto : validos) {
				try {
					Validacion.comprobarPuerto(puerto);
				} catch(PuertoInvalidoException e) {
					fail("El puerto '" + puerto + "' deber�a ser v�lido.");
				}
			}
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
}
