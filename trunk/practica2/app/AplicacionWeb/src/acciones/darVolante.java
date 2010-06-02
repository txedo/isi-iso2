package acciones;

import java.rmi.RemoteException;
import java.util.Map;
import java.util.Vector;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import comunicaciones.ServidorFrontend;

import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.Especialidades;
import dominio.conocimiento.ISesion;
import dominio.conocimiento.Medico;

/**
 * Acción ejecutada cuando un médico va a emitir un volante para un
 * beneficiario.
 */
public class darVolante extends ActionSupport {

	private static final long serialVersionUID = 8606059954495046094L;
	
	private Vector<String> especialidades;
	private Vector<Beneficiario> beneficiarios;
	
	public String execute() throws RemoteException, Exception {
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
