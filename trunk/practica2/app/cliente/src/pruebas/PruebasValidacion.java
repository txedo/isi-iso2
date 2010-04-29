package pruebas;

import java.util.Date;

import dominio.conocimiento.IConstantes;
import presentacion.auxiliar.Validacion;
import excepciones.ApellidoIncorrectoException;
import excepciones.CodigoPostalIncorrectoException;
import excepciones.Contrase�aIncorrectaException;
import excepciones.CorreoElectronicoIncorrectoException;
import excepciones.DomicilioIncorrectoException;
import excepciones.FechaCitaIncorrectaException;
import excepciones.FechaNacimientoIncorrectaException;
import excepciones.FechaSustitucionIncorrectaException;
import excepciones.HoraSustitucionIncorrectaException;
import excepciones.IPInvalidaException;
import excepciones.IdVolanteIncorrectoException;
import excepciones.LocalidadIncorrectaException;
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
import junit.framework.TestCase;

/**
 * Pruebas de la clase encargada de comprobar la validez de los
 * campos introducidos por el usuario.
 */
public class PruebasValidacion extends TestCase {
	
	private String cadenaMaxima;
	private String numeroMaximo;
	
	public void setUp() {
		char[] c;
		int i;
		
		try {
			c = new char[Validacion.MAX_LONGITUD_CAMPOS + 1];
			for(i = 0; i < c.length; i++) {
				c[i] = 'A';
			}
			cadenaMaxima = new String(c);
			c = new char[Validacion.MAX_LONGITUD_CAMPOS_NUMERICOS + 1];
			for(i = 0; i < c.length; i++) {
				c[i] = '1';
			}
			numeroMaximo = new String(c);
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	public void tearDown() {
		// No es necesario ning�n c�digo de finalizaci�n
	}
	
	/** Pruebas de NIFs */
	public void testNIFs() {
		String[] invalidos, validos;
		
		try {
			// Probamos NIFs incorrectos
			invalidos = new String[] { "", "  ", "abc", "1234", "1234d", "12345678", "1234567890x", "12345678?", "12345678�", "12345678�", "12345678�", "  12345678A", "12345678A  ", cadenaMaxima };
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
			invalidos = new String[] { "", "  ", "abc", "1234", "123456789012x", "abcdefghijkl", "  123456789012", "123456789012  ", cadenaMaxima };
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
			invalidos = new String[] { "", "  ", "  Ana", "Ana  ", "1234", "Pablo123", "Luis�?", "pepe---luis", "pepe--", cadenaMaxima };
			for(String nombre : invalidos) {
				try {
					Validacion.comprobarNombre(nombre);
					fail("El nombre '" + nombre + "' deber�a ser inv�lido.");
				} catch(NombreIncorrectoException e) {
				}
			}
			// Probamos nombres correctos
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
			invalidos = new String[] { "", "  ", "1234", "Mu�oz 12345", "�L�pez?", "  Ortiz", "Ortiz  ", "Lopez-", "Dominguez--Garcia", cadenaMaxima };
			for(String apellidos : invalidos) {
				try {
					Validacion.comprobarApellidos(apellidos);
					fail("El apellido '" + apellidos + "' deber�a ser inv�lido.");
				} catch(ApellidoIncorrectoException e) {
				}
			}
			// Probamos apellidos correctos
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
			invalidos = new String[] { "", "  ", "1234", "��??", "A$*^!�?=", "  Calle", "  Calle", "Calle /\\-.", "C/Mata ", cadenaMaxima };
			for(String domicilio : invalidos) {
				try {
					Validacion.comprobarDomicilio(domicilio);
					fail("El domicilio '" + domicilio + "' deber�a ser inv�lido.");
				} catch(DomicilioIncorrectoException e) {
				}
			}
			// Probamos domicilios correctos
			validos = new String[] { "C/Mata", "C/Don Quijote", "Calle Santa Mar�a", "����� ����� ����" };
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
			invalidos = new String[] { "", "  ", "abcd", "-12", "8 A", "  2", "2  ", "a", "8AB", "0,59", "1.890", numeroMaximo };
			for(String numero : invalidos) {
				try {
					Validacion.comprobarNumero(numero);
					fail("El n�mero de domicilio '" + numero + "' deber�a ser inv�lido.");
				} catch(NumeroDomicilioIncorrectoException e) {
				}
			}
			// Probamos n�meros de domicilio correctos
			validos = new String[] { "0", "18", "590", "4B", numeroMaximo.substring(0, numeroMaximo.length() - 1) + "A" };
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
			invalidos = new String[] { "", "  ", "abcd", "-12", "8A", "0,59", "1.890", "G", "18  ", "  18", numeroMaximo };
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
			invalidos = new String[] { "", " ", "abcd", "1234", "1", "AB", "pq", "�", "�", "�", "�", cadenaMaxima };
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
	
	/** Pruebas de localidades */
	public void testLocalidades() {
		String[] invalidos, validos;
		
		try {
			// Probamos localidades incorrectas
			invalidos = new String[] { "", "  ", "1234", "  Toledo", "Toledo  ", "Ciudad 12345", "**Madrid**", cadenaMaxima };
			for(String localidad : invalidos) {
				try {
					Validacion.comprobarLocalidad(localidad);
					fail("La localidad '" + localidad + "' deber�a ser inv�lida.");
				} catch(LocalidadIncorrectaException e) {
				}
			}
			// Probamos localidades correctas
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
			invalidos = new String[] { "", "  ", "1234", "abc", "FGHIJ", "13000W", "**123", "18020  ", "  18020", numeroMaximo };
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
			invalidos = new String[] { "", "  ", "1234", "Badajoz  ", "  Badajoz", "Provincia 12345", "**Badajoz**", cadenaMaxima };
			for(String provincia : invalidos) {
				try {
					Validacion.comprobarProvincia(provincia);
					fail("La provincia '" + provincia + "' deber�a ser inv�lida.");
				} catch(ProvinciaIncorrectaException e) {
				}
			}
			// Probamos provincias correctas
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
			invalidos = new String[] { "", "  ", "1234", "abcd", "pedro@novale", "pedro.garcia@novale", "pedro.com", "a@.c", "maria@yahoo.es  ", "  maria@yahoo.es", cadenaMaxima };
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
			invalidos = new String[] { "", "  ", "1234", "abcd", "555666777", "91 1201 888", "900112233x", "9001122??", "926111222  ", "  926111222", numeroMaximo };
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
			invalidos = new String[] { "", "  ", "1234", "abcd", "555666777", "612 127 914", "600112233x", "6001122??", "626111222  ", "  626111222", numeroMaximo };
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
	
	/** Pruebas de nombres de usuario */
	public void testUsuarios() {
		String[] invalidos, validos;
		
		try {
			// Probamos nombres de usuario incorrectos
			invalidos = new String[] { "", "  ", "123abc", "abc$$$", "����������", "��������", "abc123456  ", "  abc123456", cadenaMaxima };
			for(String usuario : invalidos) {
				try {
					Validacion.comprobarUsuario(usuario);
					fail("El nombre de usuario '" + usuario + "' deber�a ser inv�lido.");
				} catch(LoginIncorrectoException e) {
				}
			}
			// Probamos nombres de usuario correctos
			validos = new String[] { "abc123", "a11111111", "abcdefgh", "UsEr123rEsU" };
			for(String usuario : validos) {
				try {
					Validacion.comprobarUsuario(usuario);
				} catch(LoginIncorrectoException e) {
					fail("El nombre de usuario '" + usuario + "' deber�a ser v�lido.");
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
			invalidos = new String[] { "", "  ", "abc123", "abc$$$123", "����������", "��������", "abc123456  ", "  abc123456", cadenaMaxima };
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
	
	/** Pruebas de fechas de citas */
	@SuppressWarnings("deprecation")
	public void testFechasCitas() {
		Date[] invalidos, validos;
		
		try {
			// Probamos fechas de citas incorrectas
			invalidos = new Date[] { new Date(1920 - 1900, 4, 4) };
			for(Date fecha : invalidos) {
				try {
					Validacion.comprobarFechaCita(fecha);
					fail("La fecha de cita '" + fecha + "' deber�a ser inv�lida.");
				} catch(FechaCitaIncorrectaException e) {
				}
			}
			// Probamos fechas de citas correctas
			validos = new Date[] { new Date(2015 - 1900, 4, 4) };
			for(Date fecha : validos) {
				try {
					Validacion.comprobarFechaCita(fecha);
				} catch(FechaCitaIncorrectaException e) {
					fail("La fecha de cita '" + fecha + "' deber�a ser v�lida.");
				}
			}
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	/** Pruebas de fechas de sustituciones */
	@SuppressWarnings("deprecation")
	public void testFechasSustituciones() {
		Date[] invalidos, validos;
		
		try {
			// Probamos fechas de citas incorrectas
			invalidos = new Date[] { new Date(1920 - 1900, 4, 4) };
			for(Date fecha : invalidos) {
				try {
					Validacion.comprobarFechaSustitucion(fecha);
					fail("La fecha de cita '" + fecha + "' deber�a ser inv�lida.");
				} catch(FechaSustitucionIncorrectaException e) {
				}
			}
			// Probamos fechas de citas correctas
			validos = new Date[] { new Date(2015 - 1900, 4, 4) };
			for(Date fecha : validos) {
				try {
					Validacion.comprobarFechaSustitucion(fecha);
				} catch(FechaSustitucionIncorrectaException e) {
					fail("La fecha de cita '" + fecha + "' deber�a ser v�lida.");
				}
			}
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	/** Pruebas de horas de sustituciones */
	public void testHorasSustituciones() {
		int[][] invalidos, validos;
		
		try {
			// Probamos horas de sustituciones incorrectas
			invalidos = new int[][] { new int[] { 0, 0 }, new int[] { 0, 23 }, new int[] { 14, 23 }, new int[] { 15, 10 }, new int[] { 17, 17 } };
			for(int[] horas : invalidos) {
				try {
					Validacion.comprobarHorasSustitucion(horas[0], horas[1]);
					fail("Las horas de sustituci�n " + horas[0] + " - " + horas[1] + " deber�a ser inv�lida.");
				} catch(HoraSustitucionIncorrectaException e) {
				}
			}
			// Probamos horas de sustituciones correctas
			validos = new int[][] { new int[] { 10, 15 }, new int[] { IConstantes.HORA_INICIO_JORNADA, IConstantes.HORA_FIN_JORNADA } };
			for(int[] horas : validos) {
				try {
					Validacion.comprobarHorasSustitucion(horas[0], horas[1]);
				} catch(HoraSustitucionIncorrectaException e) {
					fail("Las horas de sustituci�n " + horas[0] + " - " + horas[1] + " deber�a ser inv�lida.");
				}
			}
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	/** Pruebas de identificadores de volantes */
	public void testIdVolantes() {
		String[] invalidos, validos;
		
		try {
			// Probamos ids de volantes incorrectos
			invalidos = new String[] { "", "  ", "abc", "123a", "a123", "-150", "3.14", "4,105", "  1234", "1234  ", numeroMaximo };
			for(String id : invalidos) {
				try {
					Validacion.comprobarVolante(id);
					fail("El id de volante '" + id + "' deber�a ser inv�lida.");
				} catch(IdVolanteIncorrectoException e) {
				}
			}
			// Probamos ids de volantes correctos
			validos = new String[] { "1", "100", "500500" };
			for(String id : validos) {
				try {
					Validacion.comprobarVolante(id);
				} catch(IdVolanteIncorrectoException e) {
					fail("El id de volante '" + id + "' deber�a ser v�lida.");
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
