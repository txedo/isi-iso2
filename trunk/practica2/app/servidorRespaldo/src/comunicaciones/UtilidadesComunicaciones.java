package comunicaciones;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Vector;
import java.util.regex.Pattern;

/**
 * Clase con métodos estáticos auxiliares utilizados relacionados con la
 * comunicación entre sistemas.
 */
public class UtilidadesComunicaciones {

	private final static String IP_LOCALHOST = "127.0.0.1";
	
	// Devuelve una IP pública, una IP privada o la IP local de este host,
	// buscándola en ese orden si no se encuentra alguna de ellas
	public static String obtenerIPHost() {
		Enumeration<NetworkInterface> interfaces;
		Enumeration<InetAddress> direccionesIP;
		Vector<String> ips;
		NetworkInterface interfaz;
		InetAddress direccionIP;
		Pattern patronIPPrivada;
		String ip;
		boolean encontrado;
		int i;
		
		// Creamos un patrón que define las IPs válidas
		patronIPPrivada = Pattern.compile("\\b" +
				// 10.0.0.0 - 10.255.255.255
				"((10)\\." +
				"(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?))|" +
                // 172.16.0.0 - 172.31.255.255
				"((172)\\." +
				"(1[6-9]|2[0-9]|3[0-1])\\." +
                "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?))|" +
                // 192.168.0.0 - 192.168.255.255
				"((192)\\." +
				"(168)\\." +
                "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?))" +
                "\\b");
		
		ip = "";
		try {
			// Recorremos todos los interfaces de red
			ips = new Vector<String>();
			interfaces = NetworkInterface.getNetworkInterfaces();
			while(interfaces.hasMoreElements()) {
				interfaz = interfaces.nextElement();
				// Recorremos todas las direcciones IP asignadas al interfaz
				direccionesIP = interfaz.getInetAddresses();
				while(direccionesIP.hasMoreElements()) {
					direccionIP = direccionesIP.nextElement();
					if(direccionIP instanceof Inet4Address) {
						ips.add(direccionIP.getHostAddress());
					}
				}
			}
			// Buscamos la primera IP pública
			encontrado = false;
			for(i = 0; !encontrado && i < ips.size(); i++) {
				if(!ips.get(i).equals(IP_LOCALHOST) && !patronIPPrivada.matcher(ips.get(i)).matches()) {
					encontrado = true;
					ip = ips.get(i);
				}
			}
			// Si no hemos encontrado una IP pública, buscamos la primera IP privada
			if(!encontrado) {
				for(i = 0; !encontrado && i < ips.size(); i++) {
					if(!ips.get(i).equals(IP_LOCALHOST)) {
						encontrado = true;
						ip = ips.get(i);
					}
				}
			}
			// Si no hemos encontrado una IP privada, usamos la IP localhost
			if(!encontrado) {
				ip = IP_LOCALHOST;
			}
		} catch(SocketException e) {
			ip = IP_LOCALHOST;
		}
		
		return ip;
	}
	
}
