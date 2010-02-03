package dominio.conocimiento;

import java.io.Serializable;
import java.util.Date;

/**
 * Clase que representa un beneficiario del sistema de salud.
 */
public class Beneficiario implements Serializable {
	
	private static final long serialVersionUID = 1938859991040689592L;
	
	private String nif;
	private String nss;
	private String nombre;
	private String apellidos;
	private String domicilio;
	private String correo;
	private int telefono;
	private int movil;
	private Date fechaNacimiento;
	private Medico medicoAsignado;	

	public Beneficiario() {
	}
	
	public Beneficiario(String nif, String nss, String nombre,
			String apellidos, Date fecha, String domicilio, String correo,
			int telefono, int movil) {
		this.nif = nif;
		this.nss = nss;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.domicilio = domicilio;
		this.correo = correo;
		this.telefono = telefono;
		this.movil = movil;
		this.fechaNacimiento=fecha;
		this.medicoAsignado = null;
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

	public String getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public int getTelefono() {
		return telefono;
	}

	public void setTelefono(int telefono) {
		this.telefono = telefono;
	}

	public int getMovil() {
		return movil;
	}

	public void setMovil(int movil) {
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
	
	public int getEdad() {
		int edad = -1;
		
		// Se obtienen milisegundos de las fechas
		long fechaInicialMs = fechaNacimiento.getTime();
		long fechaFinalMs = new Date().getTime();
		long diferencia = fechaFinalMs - fechaInicialMs;
		// Se divide por el numero de milisegundos de un dia
		double dias = Math.floor(diferencia / (1000 * 60 * 60 * 24));
		// Para la edad, los dias se dividen por los dias que tiene un año
		edad = (int)(dias / 365);
	
		return edad;
	}
	
	public boolean equals(Object o) {
		Beneficiario b;
		boolean dev;
		
		dev = false;
		if(o != null && o instanceof Beneficiario) {
			b = (Beneficiario)o;
			dev = nif.equals(b.getNif()) && nss.equals(b.getNss()) && nombre.equals(b.getNombre()) && apellidos.equals(b.getApellidos()) && domicilio.equals(b.getDomicilio()) && correo.equals(b.getCorreo()) && telefono == b.getTelefono() && movil == b.getMovil() && medicoAsignado.equals(b.getMedicoAsignado());
		}
		return dev;
	}
	
}
