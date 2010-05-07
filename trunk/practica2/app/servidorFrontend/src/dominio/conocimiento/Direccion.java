package dominio.conocimiento;

import java.io.Serializable;

/**
 * Clase que representa la dirección en la que vive un beneficiario.
 */
public class Direccion implements Serializable, Cloneable {

	private static final long serialVersionUID = -8460925743520811251L;
	
	private int id;
	private String domicilio;
	private String numero;
	private String piso;
	private String puerta;
	private String ciudad;
	private String provincia;
	private int cp;
	
	public Direccion() {
		id = -1;
		domicilio = "";
		numero = "";
		piso = "";
		puerta = "";
		ciudad = "";
		provincia = "";
		cp = 0;
	}

	public Direccion(String domicilio, String numero, String piso, String puerta, String ciudad, String provincia, int cp) {
		id = -1;
		this.domicilio = domicilio;
		this.puerta = puerta;
		this.ciudad = ciudad;
		this.provincia = provincia;
		this.numero = numero;
		this.piso = piso;
		this.cp = cp;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}

	public String getPuerta() {
		return puerta;
	}

	public void setPuerta(String puerta) {
		this.puerta = puerta;
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getPiso() {
		return piso;
	}

	public void setPiso(String piso) {
		this.piso = piso;
	}

	public int getCP() {
		return cp;
	}

	public void setCP(int cp) {
		this.cp = cp;
	}
	
	public Object clone() {
		Direccion d;
		
		d = new Direccion(getDomicilio(), getNumero(), getPiso(), getPuerta(), getCiudad(), getProvincia(), getCP());
		return d;
	}
	
	public boolean equals(Object o) {
		Direccion d;
		boolean dev;
		
		dev = false;
		if(o != null && o instanceof Direccion) {
			d = (Direccion)o;
			dev = getDomicilio().equals(d.getDomicilio()) && getNumero().equals(d.getNumero())
			    && getPiso().equals(d.getPiso()) && getPuerta().equals(d.getPuerta())
			    && getCiudad().equals(d.getCiudad()) && getProvincia().equals(d.getProvincia())
			    && getCP() == d.getCP(); 
		}
		return dev;
	}

	public String toString() {
		String direccion;
		
		direccion = getDomicilio();
		if(getNumero().equals("")) {
			direccion += " s/n";
		} else {
			direccion += " " + getNumero();
		}
		if(!getPiso().equals("")) {
			direccion += ", " + getPiso() + "º";
		}
		if(!getPuerta().equals("")) {
			direccion += " " + getPuerta();
		}
		direccion += ". " + getCiudad() + " (" + cp + "), " + getProvincia();
		return direccion;
	}
	
}
