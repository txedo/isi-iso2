package persistencia;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;

import comunicaciones.GestorConexionesBD;

import dominio.conocimiento.Medico;
import dominio.conocimiento.PeriodoTrabajo;
import dominio.conocimiento.TipoMedico;
import dominio.conocimiento.Usuario;
import excepciones.UsuarioIncorrectoException;

/**
 * Clase que permite consultar, insertar, modificar y eliminar usuarios
 * de la base de datos utilizando Hibernate.
 */
public class FPUsuario {
	
	private static final String CLASE_USUARIO = "Usuario";
	
	private static final String ATRIB_NIF = "nif";
	private static final String ATRIB_LOGIN = "login";
	private static final String ATRIB_PASSWORD = "password";

	public static Usuario consultar(String nif) throws SQLException, UsuarioIncorrectoException {
		ConsultaHibernate consulta;
		List<?> resultados;
		Usuario usuario;
		
		// Consultamos la base de datos
		consulta = new ConsultaHibernate("FROM " + CLASE_USUARIO + " WHERE "
				 + ATRIB_NIF + " = ?", nif);
		resultados = GestorConexionesBD.consultar(consulta);
		
		// Si no se obtienen datos, es porque el usuario no existe
		if(resultados.size() == 0) {
			throw new UsuarioIncorrectoException("El usuario con NIF " + nif + " no se encuentra dado de alta en el sistema.");
		} else {
			// Recuperamos el usuario leído
			usuario = (Usuario)((Usuario)resultados.get(0)).clone();
			// Borramos los objetos leídos de la caché
			for(Object objeto : resultados) {
				GestorConexionesBD.borrarCache(objeto);
			}
		}
		
		return usuario;
	}
	
	public static Usuario consultar(String login, String password) throws SQLException, UsuarioIncorrectoException {
		ConsultaHibernate consulta;
		List<?> resultados;
		Usuario usuario;
		
		// Consultamos la base de datos
		consulta = new ConsultaHibernate("FROM " + CLASE_USUARIO + " WHERE "
				 + ATRIB_LOGIN + " = ? AND " + ATRIB_PASSWORD + " = ?",
				 login, password);
		resultados = GestorConexionesBD.consultar(consulta);
		
		// Si no se obtienen datos, es porque el usuario no existe
		if(resultados.size() == 0) {
			throw new UsuarioIncorrectoException("El nombre de usuario o contraseña introducidos no son válidos.");
		} else {
			// Recuperamos el usuario leído
			usuario = (Usuario)((Usuario)resultados.get(0)).clone();
			// Borramos los objetos leídos de la caché
			for(Object objeto : resultados) {
				GestorConexionesBD.borrarCache(objeto);
			}
		}
		
		return usuario;
	}
	
	public static Usuario consultarPorLogin(String login) throws SQLException, UsuarioIncorrectoException {
		ConsultaHibernate consulta;
		List<?> resultados;
		Usuario usuario;
		
		// Consultamos la base de datos
		consulta = new ConsultaHibernate("FROM " + CLASE_USUARIO + " WHERE "
				 + ATRIB_LOGIN + " = ?", login);
		resultados = GestorConexionesBD.consultar(consulta);
		
		// Si no se obtienen datos, es porque el usuario no existe
		if(resultados.size() == 0) {
			throw new UsuarioIncorrectoException("El nombre de usuario introducido no es válido.");
		} else {
			// Recuperamos el usuario leído
			usuario = (Usuario)((Usuario)resultados.get(0)).clone();
			// Borramos los objetos leídos de la caché
			for(Object objeto : resultados) {
				GestorConexionesBD.borrarCache(objeto);
			}
		}
		
		return usuario;
	}
	
	public static boolean correspondeNIFUsuario(String nif) throws SQLException {
		ConsultaHibernate consulta;
		List<?> resultados;
		boolean existeUsuario;
		
		// Consultamos la base de datos
		consulta = new ConsultaHibernate("FROM " + CLASE_USUARIO + " WHERE "
				 + ATRIB_NIF + " = ?", nif);
		resultados = GestorConexionesBD.consultar(consulta);
		
		// Si no se obtienen datos, es porque el usuario no existe
		if(resultados.size() == 0) {
			existeUsuario = false;
		} else {
			existeUsuario = true;
		}
		
		// Borramos los objetos leídos de la caché
		for(Object objeto : resultados) {
			GestorConexionesBD.borrarCache(objeto);
		}
		
		return existeUsuario;
	}
	
	public static Vector<String> consultarMedicos(TipoMedico tipo) throws SQLException {
		ConsultaHibernate consulta;
		List<?> resultados;
		Vector<String> nifs;
		
		// Consultamos la base de datos
		consulta = new ConsultaHibernate("FROM " + CLASE_USUARIO);
		resultados = GestorConexionesBD.consultar(consulta);
		
		// Devolvemos la lista de usuarios que son médicos del tipo pedido
		nifs = new Vector<String>();
		for(Object usuario : resultados) {
			if(usuario instanceof Medico && ((Medico)usuario).getTipoMedico().equals(tipo)) {
				nifs.add(((Usuario)usuario).getNif());
			}
		}
		
		// Borramos los objetos leídos de la caché
		for(Object objeto : resultados) {
			GestorConexionesBD.borrarCache(objeto);
		}
		
		return nifs;
	}
	
	public static void insertar(Usuario usuario) throws SQLException {
		Usuario usuarioNuevo;
		Medico medico, medicoNuevo;
		
		// Modificamos la base de datos y copiamos los ids asignados
		try {
			GestorConexionesBD.iniciarTransaccion();
			usuarioNuevo = (Usuario)GestorConexionesBD.insertar(usuario.clone());
			if(usuario instanceof Medico) {
				medicoNuevo = (Medico)usuarioNuevo;
				medico = (Medico)usuario;
				medico.getTipoMedico().setId(medicoNuevo.getTipoMedico().getId());
				medico.getCalendario().clear();
				medico.getCalendario().addAll(medicoNuevo.getCalendario());
			}
		} finally {
			GestorConexionesBD.terminarTransaccion();
		}
	}
	
	public static void modificar(Usuario usuario) throws SQLException, UsuarioIncorrectoException {
		ConsultaHibernate consulta;
		List<?> resultados;
		Usuario usuarioActual;
		Medico medicoActual, medico;

		// Consultamos la base de datos
		consulta = new ConsultaHibernate("FROM " + CLASE_USUARIO + " WHERE "
				 + ATRIB_NIF + " = ?", usuario.getNif());
		resultados = GestorConexionesBD.consultar(consulta);
		
		// Si no se obtienen datos, es porque el usuario no existe
		if(resultados.size() == 0) {
			throw new UsuarioIncorrectoException("El usuario con NIF " + usuario.getNif() + " no se encuentra dado de alta en el sistema.");
		} else {
			// Recuperamos el usuario que hay actualmente en la base de datos
			usuarioActual = (Usuario)resultados.get(0);
		}

		try {
			if(usuarioActual.getRol() != usuario.getRol()) {
				// Borramos los objetos leídos de la caché
				for(Object objeto : resultados) {
					GestorConexionesBD.borrarCache(objeto);
				}
				resultados.clear();
				// Si se va a cambiar el rol del usuario, se tiene que
				// eliminar el usuario antiguo e insertar el nuevo
				GestorConexionesBD.iniciarTransaccion();
				GestorConexionesBD.eliminar(usuarioActual.clone());
				GestorConexionesBD.insertar(usuario);
			} else {
				// Eliminamos a mano los campos que hacen referencia a otras
				// tablas, porque al actualizarlos Hibernate no borra los
				// campos antiguos automáticamente, sino que los mantiene
				GestorConexionesBD.iniciarTransaccion();
				if(usuarioActual instanceof Medico) {
					medicoActual = (Medico)usuarioActual;
					GestorConexionesBD.eliminar(medicoActual.getTipoMedico());
					for(PeriodoTrabajo periodo : medicoActual.getCalendario()) {
						GestorConexionesBD.eliminar(periodo);
					}
				}
				// Actualizamos los datos del usuario
				usuarioActual.setApellidos(usuario.getApellidos());
				usuarioActual.setCentroSalud(usuario.getCentroSalud());
				usuarioActual.setCorreo(usuario.getCorreo());
				usuarioActual.setLogin(usuario.getLogin());
				usuarioActual.setMovil(usuario.getMovil());
				usuarioActual.setNombre(usuario.getNombre());
				usuarioActual.setPassword(usuario.getPassword());
				usuarioActual.setTelefono(usuario.getTelefono());
				if(usuarioActual instanceof Medico) {
					medicoActual = (Medico)usuarioActual;
					medico = (Medico)usuario;
					medicoActual.setTipoMedico((TipoMedico)medico.getTipoMedico().clone());
					for(TipoMedico tipo : medicoActual.getTiposMedico()) {
						tipo.setId(-1);
					}
					medicoActual.setCalendario(new HashSet<PeriodoTrabajo>());
					for(PeriodoTrabajo periodo : medico.getCalendario()) {
						medicoActual.getCalendario().add((PeriodoTrabajo)periodo.clone());
					}
					for(PeriodoTrabajo periodo : medicoActual.getCalendario()) {
						periodo.setId(-1);
					}
				}
				// Modificamos la base de datos
				GestorConexionesBD.actualizar(usuarioActual);
			}
		} finally {
			GestorConexionesBD.terminarTransaccion();
		}

		// Borramos los objetos leídos de la caché
		for(Object objeto : resultados) {
			GestorConexionesBD.borrarCache(objeto);
		}
	}
	
	public static void eliminar(Usuario usuario) throws SQLException, UsuarioIncorrectoException {
		// Modificamos la base de datos (automáticamente se
		// borran los datos adicionales si el usuario es médico)
		try {
			GestorConexionesBD.iniciarTransaccion();
			GestorConexionesBD.eliminar(consultar(usuario.getNif()));
		} finally {
			GestorConexionesBD.terminarTransaccion();
		}
	}
	
}
