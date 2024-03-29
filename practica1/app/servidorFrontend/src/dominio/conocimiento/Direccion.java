package dominio.conocimiento;

import java.io.Serializable;

/**
 * Clase que representa la direcci�n en la que vive un beneficiario.
 */
public class Direccion implements Serializable, Cloneable {

	private static final long serialVersionUID = -8460925743520811251L;
	
	private String domicilio;
	private String numero;
	private String piso;
	private String puerta;
	private String ciudad;
	private String provincia;
	private int cp;
	
	public Direccion() {
		domicilio = "";
		numero = "";
		piso = "";
		puerta = "";
		ciudad = "";
		provincia = "";
		cp = 0;
	}

	public Direccion(String domicilio, String numero, String piso, String puerta, String ciudad, String provincia, int cp) {
		this.domicilio = domicilio;
		this.puerta = puerta;
		this.ciudad = ciudad;
		this.provincia = provincia;
		this.numero = numero;
		this.piso = piso;
		this.cp = cp;
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
		
		d = new Direccion(domicilio, numero, piso, puerta, ciudad, provincia, cp);
		return d;
	}
	
	public boolean equals(Object o) {
		Direccion d;
		boolean dev;
		
		dev = false;
		if(o != null && o instanceof Direccion) {
			d = (Direccion)o;
			dev = d.getDomicilio().equals(domicilio) && d.getNumero().equals(numero) && d.getPiso().equals(piso) && d.getPuerta().equals(puerta) && d.getCiudad().equals(ciudad) && d.getProvincia().equals(provincia) && d.getCP() == cp; 
		}
		return dev;
	}

	public String toString() {
		String direccion;
		
		direccion = domicilio;
		if(numero.equals("")) {
			direccion += " s/n";
		} else {
			direccion += " " + numero;
		}
		if(!piso.equals("")) {
			direccion += ", " + piso + "�";
		}
		if(!puerta.equals("")) {
			direccion += " " + puerta;
		}
		direccion += ". " + ciudad + " (" + cp + "), " + provincia;
		return direccion;
	}
	
}
