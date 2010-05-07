package dominio.conocimiento;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import dominio.UtilidadesDominio;

/**
 * Clase que representa un beneficiario del sistema de salud.
 */
public class Beneficiario implements Serializable, Cloneable {
	
	private static final long serialVersionUID = 1938859991040689592L;
	
	private String nif;
	private String nss;
	private String nombre;
	private String apellidos;
	private Set<Direccion> direcciones;
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
		direcciones = new HashSet<Direccion>();
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
		direcciones = new HashSet<Direccion>();
		direcciones.add(direccion);
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

	public Set<Direccion> getDirecciones() {
		return direcciones;
	}

	public void setDirecciones(Set<Direccion> direcciones) {
		this.direcciones = direcciones;
	}
	
	public Direccion getDireccion() {
		return (Direccion)direcciones.toArray()[0];
	}

	public void setDireccion(Direccion direccion) {
		direcciones = new HashSet<Direccion>();
		direcciones.add(direccion);
	}
	
	public CentroSalud getCentroSalud() {
		return centroSalud;
	}

	public void setCentroSalud(CentroSalud centro) {
		this.centroSalud = centro;
	}
	
	public Roles getRol() {
		return Roles.Beneficiario;
	}
	
	public int getEdad() {
		long msInicial, msFinal, diferencia;
		double dias;
		int edad;
		
		// Calculamos la diferencia entre las fechas en milisegundos
		msInicial = getFechaNacimiento().getTime();
		msFinal = new Date().getTime();
		diferencia = msFinal - msInicial;

		// Calculamos el número de días dividiendo entre los milisegundos de un día
		dias = Math.floor(diferencia / (1000 * 60 * 60 * 24));
		
		// Para obtener la edad, dividimos los días entre los días que tiene un año
		edad = (int)(dias / 365);
	
		return edad;
	}
	
	public Object clone() {
		Beneficiario b;
		
		b = new Beneficiario(getNif(), getNss(), getNombre(), getApellidos(), (Date)getFechaNacimiento().clone(), (Direccion)getDireccion().clone(), getCorreo(), getTelefono(), getMovil());
		b.setCentroSalud((getCentroSalud() == null) ? null : (CentroSalud)getCentroSalud().clone());
		b.setMedicoAsignado((getMedicoAsignado() == null) ? null : (Medico)getMedicoAsignado().clone());
		return b;
	}
	
	public boolean equals(Object o) {
		Beneficiario b;
		boolean dev;
		
		dev = false;
		if(o != null && o instanceof Beneficiario) {
			b = (Beneficiario)o;
			dev = getNif().equals(b.getNif()) && getNss().equals(b.getNss())
			    && getNombre().equals(b.getNombre()) && getApellidos().equals(b.getApellidos())
			    && getDireccion().equals(b.getDireccion()) && getCorreo().equals(b.getCorreo())
			    && getTelefono().equals(b.getTelefono()) && getMovil().equals(b.getMovil())
			    && UtilidadesDominio.fechaIgual(getFechaNacimiento(), b.getFechaNacimiento(), false);
			if(getCentroSalud() == null) {
				dev = dev && (b.getCentroSalud() == null);
			} else {
				dev = dev && getCentroSalud().equals(b.getCentroSalud());
			}
			if(getMedicoAsignado() == null) {
				dev = dev && (b.getMedicoAsignado() == null); 
			} else {
				dev = dev && getMedicoAsignado().equals(b.getMedicoAsignado());
			}
		}
		return dev;
	}
	
	public String toString() {
		return getNif() + ", " + getNss() + ", " + getNombre() + ", " + getApellidos()
		       + ", " + getDireccion().toString() + ", " + getCorreo() + ", " + getTelefono()
		       + ", " + getMovil() + ", " + getFechaNacimiento().toString()
		       + ", C: " + ((getCentroSalud() == null) ? "(ninguno)" : getCentroSalud().getNombre())
		       + ", M:" + ((getMedicoAsignado() == null) ? "(ninguno)" : getMedicoAsignado().getNif()); 
	}

}
