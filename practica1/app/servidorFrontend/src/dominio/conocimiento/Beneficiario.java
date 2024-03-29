package dominio.conocimiento;

import java.io.Serializable;
import java.util.Date;

/**
 * Clase que representa un beneficiario del sistema de salud.
 */
public class Beneficiario implements Serializable, Cloneable {
	
	private static final long serialVersionUID = 1938859991040689592L;
	
	private String nif;
	private String nss;
	private String nombre;
	private String apellidos;
	private Direccion direccion;
	private String correo;
	private String telefono;
	private String movil;
	private Date fechaNacimiento;
	private Medico medicoAsignado;	
	private CentroSalud centroSalud;

	public Beneficiario() {
		nif = "";
		nss = "";
		nombre = "";
		apellidos = "";
		direccion = new Direccion();
		correo = "";
		telefono = "";
		movil = "";
		fechaNacimiento = new Date();
		medicoAsignado = null;
		centroSalud = null;
	}
	
	public Beneficiario(String nif, String nss, String nombre, String apellidos, Date fecha, Direccion direccion, String correo, String telefono, String movil) {
		this.nif = nif;
		this.nss = nss;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.direccion = direccion;
		this.correo = correo;
		this.telefono = telefono;
		this.movil = movil;
		this.fechaNacimiento = fecha;
		medicoAsignado = null;
		centroSalud = null;
	}

	public String getNif() {
		return nif;
	}

	public void setNif(String nif) {
		this.nif = nif;
	}

	public String getNss() {
		return nss;
	}

	public void setNss(String nss) {
		this.nss = nss;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getMovil() {
		return movil;
	}

	public void setMovil(String movil) {
		this.movil = movil;
	}

	public Medico getMedicoAsignado() {
		return medicoAsignado;
	}

	public void setMedicoAsignado(Medico medicoAsignado) {
		this.medicoAsignado = medicoAsignado;
	}
	
	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}
	
	public Direccion getDireccion() {
		return direccion;
	}

	public void setDireccion(Direccion direccion) {
		this.direccion = direccion;
	}
	
	public CentroSalud getCentroSalud() {
		return centroSalud;
	}

	public void setCentroSalud(CentroSalud centro) {
		this.centroSalud = centro;
	}
	
	public int getEdad() {
		long msInicial, msFinal, diferencia;
		double dias;
		int edad;
		
		// Calculamos la diferencia entre las fechas en milisegundos
		msInicial = fechaNacimiento.getTime();
		msFinal = new Date().getTime();
		diferencia = msFinal - msInicial;

		// Calculamos el n�mero de d�as dividiendo entre los milisegundos de un d�a
		dias = Math.floor(diferencia / (1000 * 60 * 60 * 24));
		
		// Para obtener la edad, dividimos los d�as entre los d�as que tiene un a�o
		edad = (int)(dias / 365);
	
		return edad;
	}
	
	public Object clone() {
		Beneficiario b;
		
		b = new Beneficiario(nif, nss, nombre, apellidos, (Date)fechaNacimiento.clone(), (Direccion)direccion.clone(), correo, telefono, movil);
		b.setCentroSalud((centroSalud == null) ? null : (CentroSalud)centroSalud.clone());
		b.setMedicoAsignado((medicoAsignado == null) ? null : (Medico)medicoAsignado.clone());
		return b;
	}
	
	public boolean equals(Object o) {
		Beneficiario b;
		boolean dev;
		
		dev = false;
		if(o != null && o instanceof Beneficiario) {
			b = (Beneficiario)o;
			dev = nif.equals(b.getNif()) && nss.equals(b.getNss()) && nombre.equals(b.getNombre()) && apellidos.equals(b.getApellidos()) && direccion.equals(b.getDireccion()) && correo.equals(b.getCorreo()) && telefono.equals(b.getTelefono()) && movil.equals(b.getMovil());
			if(centroSalud == null) {
				dev = dev && (b.getCentroSalud() == null);
			} else {
				dev = dev && centroSalud.equals(b.getCentroSalud());
			}
			if(medicoAsignado == null) {
				dev = dev && (b.getMedicoAsignado() == null); 
			} else {
				dev = dev && medicoAsignado.equals(b.getMedicoAsignado());
			}
		}
		return dev;
	}
	
	public String toString() {
		return nif + ", " + nss + ", " + nombre + ", " + apellidos + ", " + direccion.toString() + ", " + correo + ", " + telefono + ", " + movil + ", " + fechaNacimiento.toString() + ", C: " + ((centroSalud == null) ? "(ninguno)" : centroSalud.getNombre()) + ", M:" + ((medicoAsignado == null) ? "(ninguno)" : medicoAsignado.getNif()); 
	}

}
