package persistencia;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import comunicaciones.GestorConexionesBD;

import dominio.CentroSalud;
import dominio.Medico;
import dominio.PeriodoTrabajo;
import dominio.Roles;
import dominio.TipoMedico;
import dominio.Usuario;
import excepciones.CentroSaludIncorrectoException;
import excepciones.UsuarioIncorrectoException;

public class FPTipoMedico {
	
	private static final String TABLA_TIPO_MEDICO = "tiposMedico";
	private static final String TABLA_USUARIOS = "usuarios";
	
	private static final String COL_DNI_TIPO_MEDICO = "dniMedico";
	private static final String COL_TIPO_MEDICO = "tipo";
	private static final String COL_DNI = "dni";
	private static final String COL_ROL = "rol";
	
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
	
	public static void insertar(Usuario usuario) throws SQLException{
		ComandoSQL comando = new ComandoSQLSentencia("INSERT INTO " + TABLA_TIPO_MEDICO + " VALUES (?,?)", usuario.getDni(), ((Medico)usuario).getTipoMedico().getClass().getSimpleName());
		GestorConexionesBD.ejecutar(comando);
	}
	
	public static void eliminar(Usuario usuario) throws SQLException{
		ComandoSQL comando = new ComandoSQLSentencia("DELETE FROM " + TABLA_TIPO_MEDICO + " WHERE " + COL_DNI_TIPO_MEDICO + " = ?", usuario.getDni());
		GestorConexionesBD.ejecutar(comando);
	}

}
