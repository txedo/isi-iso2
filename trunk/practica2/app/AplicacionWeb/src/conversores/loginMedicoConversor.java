package conversores;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;

import comunicaciones.IConexion;
import comunicaciones.ServidorFrontend;

import dominio.conocimiento.ISesion;
import dominio.conocimiento.Medico;
import excepciones.UsuarioIncorrectoException;

public class loginMedicoConversor extends StrutsTypeConverter {

	public Object convertFromString(Map map, String[] values, Class toClass) {
		Medico medico = null;
		if (values!=null && values.length>0) {
				String login = values[0];
				String password = values[1];
				ServidorFrontend p = null;
				try {
					p = ServidorFrontend.getServidor();
					p.conectar(IConexion.IP, IConexion.PUERTO);
					ISesion s = p.identificarUsuario(login, password);
					//medico = (Medico) p.getMedicoPorLogin(s.getId(), login);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NotBoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UsuarioIncorrectoException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
		}
		return medico;
	}

	public String convertToString(Map arg0, Object arg1) {
		return null;
	}

}
