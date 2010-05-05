package acciones;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Map;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import comunicaciones.IConexion;
import comunicaciones.ProxyServidorFrontend;

import dominio.conocimiento.ISesion;
import dominio.conocimiento.Volante;

public class citaEspecialista extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4741351353645398459L;
	
	private long nVolante;
	private Volante volante;
	
	public String execute() {
		ProxyServidorFrontend p;
		// Tomamos el idSesion del contexto de la accion
		Map<String, Object> parametros = ActionContext.getContext().getSession();
		ISesion s = (ISesion) parametros.get("SesionFrontend");
		if (s!=null) {
			try {
				p = ProxyServidorFrontend.getProxy();
				p.conectar(IConexion.IP, IConexion.PUERTO);
				volante = p.getVolante(s.getId(), nVolante);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NotBoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}//TODO: el caso del else, que es un error
		return SUCCESS;	
	}

	public long getNVolante() {
		return nVolante;
	}

	public void setNVolante(long volante) {
		nVolante = volante;
	}

	public Volante getVolante() {
		return volante;
	}

}
