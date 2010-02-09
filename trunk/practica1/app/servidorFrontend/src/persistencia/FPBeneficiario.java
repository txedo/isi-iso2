package persistencia;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import comunicaciones.GestorConexionesBD;
import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.Medico;
import excepciones.BeneficiarioInexistenteException;
import excepciones.CentroSaludIncorrectoException;
import excepciones.UsuarioIncorrectoException;

/**
 * Clase dedicada a consultar y modificar beneficiarios en la base de datos.
 */
public class FPBeneficiario {

	private static final String TABLA_BENEFICIARIOS = "beneficiarios";
	
	private static final String COL_NIF = "nif";
	private static final String COL_NSS = "nss";
	private static final String COL_NOMBRE = "nombre";
	private static final String COL_APELLIDOS = "apellidos";
	private static final String COL_DOMICILIO = "domicilio";
	private static final String COL_NUMERO_DOMICILIO = "numeroDomicilio";
	private static final String COL_PISO_DOMICILIO = "pisoDomicilio";
	private static final String COL_PUERTA_DOMICILIO = "puertaDomicilio";
	private static final String COL_CIUDAD = "ciudad";
	private static final String COL_PROVINCIA = "provincia";
	private static final String COL_CP = "cp";
	private static final String COL_CORREO = "correo";
	private static final String COL_FECHA_NACIMIENTO = "fechaNacimiento";
	private static final String COL_TELEFONO = "telefono";
	private static final String COL_MOVIL = "movil";
	private static final String COL_DNI_MEDICO = "dniMedico";

	public static Beneficiario consultarPorNIF(String nif) throws SQLException, BeneficiarioInexistenteException, UsuarioIncorrectoException, CentroSaludIncorrectoException {
		ComandoSQL comando;
		ResultSet datos;
		Beneficiario bene;
		Medico medico;

		// Consultamos la base de datos
		comando = new ComandoSQLSentencia("SELECT * FROM " + TABLA_BENEFICIARIOS + " WHERE " + COL_NIF + " = ?", nif);
		datos = GestorConexionesBD.consultar(comando);
		datos.next();

		// Si no se obtienen datos, es porque el beneficiario no existe
		if(datos.getRow() == 0) {
			throw new BeneficiarioInexistenteException("El beneficiario con NIF " + nif + " no se encuentra dado de alta en el sistema.");
		} else {
			// Establecemos los datos del beneficiario
			bene = new Beneficiario();
			bene.setNif(datos.getString(COL_NIF));
			bene.setNss(datos.getString(COL_NSS));
			bene.setNombre(datos.getString(COL_NOMBRE));
			bene.setApellidos(datos.getString(COL_APELLIDOS));
			bene.setDomicilio("Calle: " + datos.getString(COL_DOMICILIO));
			if (!datos.getString(COL_NUMERO_DOMICILIO).equals(""))
				bene.setDomicilio(bene.getDomicilio() + " Nº: " + datos.getString(COL_NUMERO_DOMICILIO));
			if (!datos.getString(COL_PISO_DOMICILIO).equals(""))
				bene.setDomicilio(bene.getDomicilio() + " Piso: " + datos.getString(COL_PISO_DOMICILIO) + "º");
			if (!datos.getString(COL_PUERTA_DOMICILIO).equals(""))
				bene.setDomicilio(bene.getDomicilio() + " Letra: " + datos.getString(COL_PUERTA_DOMICILIO));
			bene.setDomicilio(bene.getDomicilio() + " Ciudad: " + datos.getString(COL_CIUDAD));
			bene.setDomicilio(bene.getDomicilio() + " Provincia: " + datos.getString(COL_PROVINCIA));
			bene.setDomicilio(bene.getDomicilio() + " CP: " + datos.getString(COL_CP));			
			bene.setCorreo(datos.getString(COL_CORREO));
			bene.setFechaNacimiento(new Date(datos.getTimestamp(COL_FECHA_NACIMIENTO).getTime()));
			bene.setTelefono(datos.getInt(COL_TELEFONO));
			bene.setMovil(datos.getInt(COL_MOVIL));
			medico = (Medico)FPUsuario.consultar(datos.getString(COL_DNI_MEDICO));
			bene.setMedicoAsignado(medico);
		}

		return bene;
	}
	
	public static Beneficiario consultarPorNSS(String nss) throws SQLException, BeneficiarioInexistenteException, UsuarioIncorrectoException, CentroSaludIncorrectoException {
		ComandoSQL comando;
		ResultSet datos;
		Beneficiario bene;
		Medico medico;

		// Consultamos la base de datos
		comando = new ComandoSQLSentencia("SELECT * FROM " + TABLA_BENEFICIARIOS + " WHERE " + COL_NSS + " = ?", nss);
		datos = GestorConexionesBD.consultar(comando);
		datos.next();

		// Si no se obtienen datos, es porque el beneficiario no existe
		if (datos.getRow() == 0) {
			throw new BeneficiarioInexistenteException("El beneficiario con NSS " + nss + " no se encuentra dado de alta en el sistema.");
		} else {
			// Establecemos los datos del beneficiario
			bene = new Beneficiario();
			bene.setNif(datos.getString(COL_NIF));
			bene.setNss(datos.getString(COL_NSS));
			bene.setNombre(datos.getString(COL_NOMBRE));
			bene.setApellidos(datos.getString(COL_APELLIDOS));
			bene.setDomicilio("Calle: " + datos.getString(COL_DOMICILIO));
			if (!datos.getString(COL_NUMERO_DOMICILIO).equals(""))
				bene.setDomicilio(bene.getDomicilio() + " Nº: " + datos.getString(COL_NUMERO_DOMICILIO));
			if (!datos.getString(COL_PISO_DOMICILIO).equals(""))
				bene.setDomicilio(bene.getDomicilio() + " Piso: " + datos.getString(COL_PISO_DOMICILIO) + "º");
			if (!datos.getString(COL_PUERTA_DOMICILIO).equals(""))
				bene.setDomicilio(bene.getDomicilio() + " Letra: " + datos.getString(COL_PUERTA_DOMICILIO));
			bene.setDomicilio(bene.getDomicilio() + " Ciudad: " + datos.getString(COL_CIUDAD));
			bene.setDomicilio(bene.getDomicilio() + " Provincia: " + datos.getString(COL_PROVINCIA));
			bene.setDomicilio(bene.getDomicilio() + " CP: " + datos.getString(COL_CP));			
			bene.setCorreo(datos.getString(COL_CORREO));
			bene.setFechaNacimiento(new Date(datos.getTimestamp(COL_FECHA_NACIMIENTO).getTime()));
			bene.setTelefono(datos.getInt(COL_TELEFONO));
			bene.setMovil(datos.getInt(COL_MOVIL));
			medico = (Medico)FPUsuario.consultar(datos.getString(COL_DNI_MEDICO));
			bene.setMedicoAsignado(medico);
		}

		return bene;
	}

	public static void insertar(Beneficiario bene) throws SQLException {
		// Separamos la direccion del beneficiario en campos
		String domicilio, letra, ciudad, provincia, numero, piso;
		int cp;
		numero = "";
		piso = "";
		letra = "";
		
		domicilio = bene.getDomicilio().substring(bene.getDomicilio().indexOf("Calle: ")+7, bene.getDomicilio().indexOf("Nº: ")-1).trim();
		if (bene.getDomicilio().contains("Nº: "))
			numero = bene.getDomicilio().substring(bene.getDomicilio().indexOf("Nº: ")+4, bene.getDomicilio().indexOf("Piso: ")).trim();
		if (bene.getDomicilio().contains("Piso: "))
			piso = bene.getDomicilio().substring(bene.getDomicilio().indexOf("Piso: ")+6, bene.getDomicilio().indexOf("Letra: ")-2).trim();
		if (bene.getDomicilio().contains("Letra: "))
			letra = bene.getDomicilio().substring(bene.getDomicilio().indexOf("Letra: ")+7, bene.getDomicilio().indexOf("Ciudad: ")).trim();
		ciudad = bene.getDomicilio().substring(bene.getDomicilio().indexOf("Ciudad: ")+8, bene.getDomicilio().indexOf("Provincia: ")-1).trim();
		provincia = bene.getDomicilio().substring(bene.getDomicilio().indexOf("Provincia: ")+11, bene.getDomicilio().indexOf("CP: ")-1).trim();
		cp = Integer.parseInt(bene.getDomicilio().substring(bene.getDomicilio().indexOf("CP: ")+4, bene.getDomicilio().length()).trim());
		
		
		ComandoSQL comando;

		// Modificamos la base de datos
		comando = new ComandoSQLSentencia("INSERT INTO " + TABLA_BENEFICIARIOS
				+ " (" + COL_NIF + ", " + COL_NSS + ", " + COL_NOMBRE
				+ ", " + COL_APELLIDOS + ", " + COL_DOMICILIO + ", " + COL_NUMERO_DOMICILIO + ", " + COL_PISO_DOMICILIO + ", " + 
				COL_PUERTA_DOMICILIO + ", " + COL_CIUDAD + ", " + COL_PROVINCIA + ", " + COL_CP + ", " + COL_CORREO
				+ ", " + COL_FECHA_NACIMIENTO + ", " + COL_TELEFONO + ", " + COL_MOVIL + ", " + COL_DNI_MEDICO + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
				bene.getNif(), bene.getNss(), bene.getNombre(),
				bene.getApellidos(), domicilio, numero, piso, letra, ciudad, provincia, cp, bene.getCorreo(), new Timestamp(bene.getFechaNacimiento().getTime()), bene.getTelefono(), bene.getMovil(), bene.getMedicoAsignado().getDni());
		GestorConexionesBD.ejecutar(comando);
	}

	public static void modificar(Beneficiario bene) throws SQLException {
		// Separamos la direccion del beneficiario en campos
		String domicilio, letra, ciudad, provincia, numero, piso;
		int cp;
		numero = "";
		piso = "";
		letra = "";
		
		domicilio = bene.getDomicilio().substring(bene.getDomicilio().indexOf("Calle: ")+7, bene.getDomicilio().indexOf("Nº: ")-1).trim();
		if (bene.getDomicilio().contains("Nº: "))
			numero = bene.getDomicilio().substring(bene.getDomicilio().indexOf("Nº: ")+4, bene.getDomicilio().indexOf("Piso: ")).trim();
		if (bene.getDomicilio().contains("Piso: "))
			piso = bene.getDomicilio().substring(bene.getDomicilio().indexOf("Piso: ")+6, bene.getDomicilio().indexOf("Letra: ")-2).trim();
		if (bene.getDomicilio().contains("Letra: "))
			letra = bene.getDomicilio().substring(bene.getDomicilio().indexOf("Letra: ")+7, bene.getDomicilio().indexOf("Ciudad: ")).trim();
		ciudad = bene.getDomicilio().substring(bene.getDomicilio().indexOf("Ciudad: ")+8, bene.getDomicilio().indexOf("Provincia: ")-1).trim();
		provincia = bene.getDomicilio().substring(bene.getDomicilio().indexOf("Provincia: ")+11, bene.getDomicilio().indexOf("CP: ")-1).trim();
		cp = Integer.parseInt(bene.getDomicilio().substring(bene.getDomicilio().indexOf("CP: ")+4, bene.getDomicilio().length()).trim());
		
		ComandoSQL comando;

		// Modificamos la base de datos
		// (el NIF y el NSS no se pueden cambiar)
		comando = new ComandoSQLSentencia("UPDATE " + TABLA_BENEFICIARIOS + " SET "
				+ COL_NOMBRE + "=?, " + COL_APELLIDOS + "=?, " + COL_DOMICILIO + "=?, " + COL_NUMERO_DOMICILIO + "=?, " + COL_PISO_DOMICILIO + "=?, " + 
				COL_PUERTA_DOMICILIO + "=?, " + COL_CIUDAD + "=?, " + COL_PROVINCIA + "=?, " + COL_CP + "=?, " + COL_CORREO
				+ "=?, " + COL_TELEFONO + "=?, " + COL_MOVIL + "=?, " + COL_DNI_MEDICO + "=? WHERE " + COL_NIF + "=? ", 
				bene.getNombre(), bene.getApellidos(), domicilio, numero, piso, letra, ciudad, provincia, cp, bene.getCorreo(), bene.getTelefono(), bene.getMovil(), bene.getMedicoAsignado().getDni(), bene.getNif());
		GestorConexionesBD.ejecutar(comando);
	}

}
