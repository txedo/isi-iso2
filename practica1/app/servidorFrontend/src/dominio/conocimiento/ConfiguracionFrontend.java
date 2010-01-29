package dominio.conocimiento;

/**
 * Clase con la configuración del servidor frontend.
 */
public class ConfiguracionFrontend {

	private String ipBDPrincipal;
	private int puertoBDPrincipal;
	private String ipRespaldo;
	private int puertoRespaldo;
	private boolean respaldoActivado;
	private int puertoFrontend;
	
	public ConfiguracionFrontend() {
		ipBDPrincipal = "127.0.0.1";
		puertoBDPrincipal = 3306;
		ipRespaldo = "127.0.0.1";
		puertoRespaldo = 1098;
		respaldoActivado = false;
		puertoFrontend = 2995;
	}
	
	public ConfiguracionFrontend(String ipBDPrincipal, int puertoBDPrincipal, String ipRespaldo, int puertoRespaldo, int puertoFrontend) {
		this.ipBDPrincipal = ipBDPrincipal;
		this.puertoBDPrincipal = puertoBDPrincipal;
		this.ipRespaldo = ipRespaldo;
		this.puertoRespaldo = puertoRespaldo;
		this.puertoFrontend = puertoFrontend;
		respaldoActivado = true;
	}
	
	public ConfiguracionFrontend(String ipBDPrincipal, int puertoBDPrincipal, int puertoFrontend) {
		this.ipBDPrincipal = ipBDPrincipal;
		this.puertoBDPrincipal = puertoBDPrincipal;
		this.ipRespaldo = "127.0.0.1";
		this.puertoRespaldo = 1098;
		this.puertoFrontend = puertoFrontend;
		respaldoActivado = false;
	}

	public String getIPBDPrincipal() {
		return ipBDPrincipal;
	}
	
	public void setIPBDPrincipal(String ipBDPrincipal) {
		this.ipBDPrincipal = ipBDPrincipal;
	}
	
	public int getPuertoBDPrincipal() {
		return puertoBDPrincipal;
	}
	
	public void setPuertoBDPrincipal(int puertoBDPrincipal) {
		this.puertoBDPrincipal = puertoBDPrincipal;
	}

	public String getIPRespaldo() {
		return ipRespaldo;
	}
	
	public void setIPRespaldo(String ipRespaldo) {
		this.ipRespaldo = ipRespaldo;
	}
	
	public int getPuertoRespaldo() {
		return puertoRespaldo;
	}
	
	public void setPuertoRespaldo(int puertoRespaldo) {
		this.puertoRespaldo = puertoRespaldo;
	}
	
	public boolean isRespaldoActivado() {
		return respaldoActivado;
	}

	public void setRespaldoActivado(boolean respaldoActivado) {
		this.respaldoActivado = respaldoActivado;
	}
	
	public int getPuertoFrontend() {
		return puertoFrontend;
	}
	
	public void setPuertoFrontend(int puertoFrontend) {
		this.puertoFrontend = puertoFrontend;
	}
	
	public boolean equals(Object o) {
		ConfiguracionFrontend c;
		boolean dev;
		
		dev = false;
		if(o != null && o instanceof ConfiguracionFrontend) {
			c = (ConfiguracionFrontend)o;
			if(respaldoActivado == c.isRespaldoActivado()) {
				if(respaldoActivado) {
					dev = ipBDPrincipal.equals(c.getIPBDPrincipal()) && puertoBDPrincipal == c.getPuertoBDPrincipal() && puertoFrontend == c.getPuertoFrontend();
				} else {
					dev = ipBDPrincipal.equals(c.getIPBDPrincipal()) && puertoBDPrincipal == c.getPuertoBDPrincipal() && ipRespaldo.equals(c.getIPRespaldo()) && puertoRespaldo == c.getPuertoRespaldo() && puertoFrontend == c.getPuertoFrontend();
				}
			}
		}
		return dev;		
	}
	
}
