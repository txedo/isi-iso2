package comunicaciones;

/**
 * Clase con la configuración de la conexión del cliente.
 */
public class ConfiguracionCliente {

	private String ipFrontend;
	private int puertoFrontend;
	
	public ConfiguracionCliente() {
		ipFrontend = "127.0.0.1";
		puertoFrontend = 2995;
	}
		
	public ConfiguracionCliente(String ipFrontend, int puertoFrontend) {
		this.ipFrontend = ipFrontend;
		this.puertoFrontend = puertoFrontend;
	}

	public String getIPFrontend() {
		return ipFrontend;
	}
	
	public void setIPFrontend(String ipFrontend) {
		this.ipFrontend = ipFrontend;
	}
	
	public int getPuertoFrontend() {
		return puertoFrontend;
	}
	
	public void setPuertoFrontend(int puertoFrontend) {
		this.puertoFrontend = puertoFrontend;
	}

	public boolean equals(Object o) {
		ConfiguracionCliente c;
		boolean dev;
		
		dev = false;
		if(o != null && o instanceof ConfiguracionCliente) {
			c = (ConfiguracionCliente)o;
			dev = ipFrontend.equals(c.getIPFrontend()) && puertoFrontend == c.getPuertoFrontend();
		}
		return dev;		
	}
	
}
