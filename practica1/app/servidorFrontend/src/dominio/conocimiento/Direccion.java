package dominio.conocimiento;

import java.io.Serializable;

public class Direccion implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8460925743520811251L;
	
	private String domicilio, numero, piso, puerta, ciudad, provincia;
	private int id, cp;
	
	public Direccion () {}

	public Direccion(String domicilio, String numero, String piso, String puerta, String ciudad,
			String provincia, int cp) {
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

	public int getCp() {
		return cp;
	}

	public void setCp(int cp) {
		this.cp = cp;
	}
	
	public String toString () {
		String direccion = domicilio;
		if (numero.equals("s/n"))
			direccion += " s/n";
		else
			direccion += ", nº " + numero;
		if (!piso.equals(""))
			direccion += ", " + piso + "º";
		if (!puerta.equals(""))
			direccion += " " + puerta;
		direccion += ". " + ciudad + ", " + provincia + ". " + cp;
		return direccion;
	}
	
	public boolean equals (Object o) {
		Direccion d = null;
		boolean dev = false;
		if (o!=null && o instanceof Direccion) {
			d = (Direccion)o;
			dev = d.getDomicilio().equals(domicilio) && d.getNumero().equals(numero) && d.getPiso().equals(piso) 
					&& d.getPuerta().equals(puerta) && d.getCiudad().equals(ciudad) && d.getProvincia().equals(provincia) && d.getCp() == cp; 
		}
		return dev;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	

	
}
