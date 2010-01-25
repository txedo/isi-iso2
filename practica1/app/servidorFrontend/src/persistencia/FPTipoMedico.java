package persistencia;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import comunicaciones.GestorConexionesBD;
import dominio.conocimiento.Administrador;
import dominio.conocimiento.Cabecera;
import dominio.conocimiento.CentroSalud;
import dominio.conocimiento.Citador;
import dominio.conocimiento.Especialista;
import dominio.conocimiento.Medico;
import dominio.conocimiento.Pediatra;
import dominio.conocimiento.PeriodoTrabajo;
import dominio.conocimiento.Roles;
import dominio.conocimiento.TipoMedico;
import dominio.conocimiento.Usuario;
import excepciones.CentroSaludIncorrectoException;
import excepciones.MedicoInexistenteException;
import excepciones.UsuarioIncorrectoException;

public class FPTipoMedico {
	
	private static final String TABLA_TIPO_MEDICO = "tiposmedico";
	private static final String TABLA_USUARIOS = "usuarios";
	
	private static final String COL_DNI_TIPO_MEDICO = "dniMedico";
	private static final String COL_TIPO_MEDICO = "tipo";
	private static final String COL_DNI = "dni";
	private static final String COL_ROL = "rol";
	private static final String COL_ESPECIALIDAD = "especialidad";
	
	public static TipoMedico consultar (String dniMedico) throws SQLException, UsuarioIncorrectoException {
		ComandoSQL comando;
		ResultSet datos;
		TipoMedico tipo = null;
		
		// Consultamos la base de datos
		comando = new ComandoSQLSentencia("SELECT * FROM " + TABLA_TIPO_MEDICO + " WHERE " + COL_DNI_TIPO_MEDICO + " = ?", dniMedico);
		datos = GestorConexionesBD.consultar(comando);
		datos.next();
		
		// Si no se obtienen datos, es porque el usuario es
		// incorrecto (o no existe, pero se trata como incorrecto)
		if(datos.getRow() == 0) {
			throw new UsuarioIncorrectoException("No existe el médico con DNI '" + dniMedico +"'");  
		} else {		
			// Establecemos el tipo del medico
			if (datos.getString(COL_TIPO_MEDICO).equals("Especialista"))
				tipo = new Especialista(datos.getString(COL_ESPECIALIDAD));
			else if (datos.getString(COL_TIPO_MEDICO).equals("Cabecera"))
				tipo = new Cabecera();
			else
				tipo = new Pediatra();
		}
		return tipo;
	}
	
	public static Medico consultarTipoMedicoAleatorio(TipoMedico tipoMedico) throws SQLException, CentroSaludIncorrectoException, UsuarioIncorrectoException {
		ComandoSQL comando;
		ResultSet datos;
		Medico medico = null;
		
		// Consultamos la base de datos
		comando = new ComandoSQLSentencia("SELECT * FROM " + TABLA_USUARIOS + "," + TABLA_TIPO_MEDICO + " WHERE " + COL_DNI + " = " + COL_DNI_TIPO_MEDICO + " AND " + COL_TIPO_MEDICO + " = ?", tipoMedico.getClass().getSimpleName());
		datos = GestorConexionesBD.consultar(comando);
		datos.next();
		
		// Si no se obtienen datos, es porque el usuario es
		// incorrecto (o no existe, pero se trata como incorrecto)
		if(datos.getRow() == 0) {
			throw new UsuarioIncorrectoException("No hay ningun medico de tipo '" + tipoMedico.getClass().getSimpleName() +"' en la base de datos");
		} // Si existe, el rol del usuario recuperado debe ser un medico 
		else if (datos.getInt(COL_ROL)!= Roles.Medico.ordinal()){
			throw new UsuarioIncorrectoException("A un beneficiario solo se puede asignar un medico");
		}else{			
			//Consultamos el medico
			medico = (Medico)FPUsuario.consultar(datos.getString(COL_DNI));		
			// Establecemos el tipo del medico
			medico.setTipoMedico(tipoMedico);
		}
		
		return medico;
	}
	
	public static String consultarTipo(Medico medico) throws UsuarioIncorrectoException, SQLException{
		ComandoSQL comando;
		ResultSet datos;
		String tipo="";
		
		// Consultamos la base de datos
		comando = new ComandoSQLSentencia("SELECT * FROM " + TABLA_TIPO_MEDICO + " WHERE " + COL_DNI_TIPO_MEDICO + " = ? ", medico.getDni());
		datos = GestorConexionesBD.consultar(comando);
		datos.next();
		
		// Si no se obtienen datos, es porque el usuario es
		// incorrecto (o no existe, pero se trata como incorrecto)
		if(datos.getRow() == 0) {
			throw new UsuarioIncorrectoException("No hay ningun medico con dni " + medico.getDni() + " en la base de datos");
		}else{
			tipo=datos.getString(COL_TIPO_MEDICO);
		}
		
		return tipo;		
		
	}
	
	public static ArrayList<Medico> consultarTodo (String tipoMedico) throws SQLException, UsuarioIncorrectoException, CentroSaludIncorrectoException, MedicoInexistenteException {
		ComandoSQL comando;
		ResultSet datos;
		ArrayList<Medico> medicos;
		Medico medico = null;
		TipoMedico tipoM = null;
	
		// Consultamos la base de datos
		comando = new ComandoSQLSentencia("SELECT * FROM " + TABLA_TIPO_MEDICO + " WHERE " + COL_TIPO_MEDICO + " = ?", tipoMedico);
		datos = GestorConexionesBD.consultar(comando);
		datos.next();
		
		// Recorremos la lista de medicos con el tipo indicado
		medicos = new ArrayList<Medico>();
		if(datos.getRow() == 0) 
			throw new MedicoInexistenteException("No existe ningún médico de tipo " + tipoMedico + " registrado en el sistema");
		do {
			// Creamos el tipo de medico adecuado
			if (tipoMedico.equals("Especialista")) 
				tipoM = new Especialista(datos.getString(COL_ESPECIALIDAD));
			else if (tipoMedico.equals("Cabecera")) 
				tipoM = new Cabecera();
			else  
				tipoM = new Pediatra();
		
			medico = (Medico) FPUsuario.consultar(datos.getString(COL_DNI_TIPO_MEDICO));		
			medico.setTipoMedico(tipoM);
			// Añadimos el medico a la lista que se va a devolver
			medicos.add(medico);
		} while (datos.next());
	
		return medicos;

	}
	
	public static void insertar(Usuario usuario) throws SQLException{
		ComandoSQL comando;
		if (((Medico)usuario).getTipoMedico() instanceof Especialista)
			comando = new ComandoSQLSentencia("INSERT INTO " + TABLA_TIPO_MEDICO + " VALUES (?,?,?)", usuario.getDni(), ((Medico)usuario).getTipoMedico().getClass().getSimpleName(),((Especialista)(((Medico)usuario).getTipoMedico())).getEspecialidad());
		else
			comando = new ComandoSQLSentencia("INSERT INTO " + TABLA_TIPO_MEDICO + " (dniMedico, tipo) VALUES (?,?)", usuario.getDni(), ((Medico)usuario).getTipoMedico().getClass().getSimpleName());
		
		GestorConexionesBD.ejecutar(comando);
	}
	
	public static void eliminar(Usuario usuario) throws SQLException{
		ComandoSQL comando = new ComandoSQLSentencia("DELETE FROM " + TABLA_TIPO_MEDICO + " WHERE " + COL_DNI_TIPO_MEDICO + " = ?", usuario.getDni());
		GestorConexionesBD.ejecutar(comando);
	}

}
