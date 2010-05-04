package comunicaciones;

/**
 * Clase con la configuración de la conexión del servidor de respaldo.
 */
public class ConfiguracionRespaldo {

	private String ipBDRespaldo;
	private int puertoBDRespaldo;
	private int puertoRespaldo;
	
	public ConfiguracionRespaldo() {
		ipBDRespaldo = "127.0.0.1";
		puertoBDRespaldo = 3306;
		puertoRespaldo = 1098;
	}
	
	public ConfiguracionRespaldo(String ipBDRespaldo, int puertoBDRespaldo, int puertoRespaldo) {
		this.ipBDRespaldo = ipBDRespaldo;
		this.puertoBDRespaldo = puertoBDRespaldo;
		this.puertoRespaldo = puertoRespaldo;
	}

	public String getIPBDRespaldo() {
		return ipBDRespaldo;
	}
	
	public void setIPBDRespaldo(String ipBDRespaldo) {
		this.ipBDRespaldo = ipBDRespaldo;
	}
	
	public int getPuertoBDRespaldo() {
		return puertoBDRespaldo;
	}
	
	public void setPuertoBDRespaldo(int puertoBDRespaldo) {
		this.puertoBDRespaldo = puertoBDRespaldo;
	}
	
	public int getPuertoRespaldo() {
		return puertoRespaldo;
	}
	
	public void setPuertoRespaldo(int puertoRespaldo) {
		this.puertoRespaldo = puertoRespaldo;
	}
	
	public boolean equals(Object o) {
		ConfiguracionRespaldo c;
		boolean dev;
		
		dev = false;
		if(o != null && o instanceof ConfiguracionRespaldo) {
			c = (ConfiguracionRespaldo)o;
			dev = ipBDRespaldo.equals(c.getIPBDRespaldo()) && puertoBDRespaldo == c.getPuertoBDRespaldo() && puertoRespaldo == c.getPuertoRespaldo();
		}
		return dev;		
	}
	
}
