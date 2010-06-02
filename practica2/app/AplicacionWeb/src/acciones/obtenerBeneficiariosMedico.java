package acciones;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Map;
import java.util.Vector;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import comunicaciones.ServidorFrontend;

import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.Especialidades;
import dominio.conocimiento.ISesion;
import dominio.conocimiento.Medico;
import excepciones.SesionNoIniciadaException;

/**
 * Acción ejecutada cuando un médico va a emitir un volante para un
 * beneficiario.
 */
public class obtenerBeneficiariosMedico extends ActionSupport {

	private static final long serialVersionUID = 8606059954495046094L;
	
	private Vector<String> especialidades;
	private Vector<Beneficiario> beneficiarios;
	
	public String execute() throws RemoteException, SQLException, NullPointerException, Exception {
		Map<String, Object> parametros;
		ServidorFrontend servidor;
		ISesion sesion;
		Medico medico;
		
		// Creamos la lista de especialidades
		especialidades = new Vector<String>();
		for(Especialidades esp : Especialidades.values()) {
			especialidades.add(esp.name());
		}
		// Obtenemos la sesión del cliente y el médico de la sesión HTTP
		parametros = ActionContext.getContext().getSession();
        sesion = (ISesion)parametros.get("SesionFrontend");
		medico = (Medico)parametros.get("Medico");
		// Se lanza una excepcion si nio se ha iniciado sesion y se invoca a esta acción
		if (sesion ==null || medico == null)
			throw new SesionNoIniciadaException("No se puede acceder a una página interna si no se inicia sesión previamente");
		// Consultamos los beneficiarios del médico
		servidor = ServidorFrontend.getServidor();
		beneficiarios = servidor.obtenerBeneficiariosMedico(sesion.getId(), medico.getNif());
		
		return SUCCESS;
	}
	
	public Vector<Beneficiario> getBeneficiarios() {
		return beneficiarios;
	}

	public Vector<String> getEspecialidades() {
		return especialidades;
	}
	
}
