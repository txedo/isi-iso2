package dominio;

import java.sql.SQLException;
import java.util.ArrayList;

import excepciones.BeneficiarioIncorrectoException;
import excepciones.CentroSaludIncorrectoException;
import excepciones.UsuarioIncorrectoException;

import persistencia.FPBeneficiario;

public class Beneficiario {
	private String nif;
	private String nss;
	private String nombre;
	private String apellidos;
	private String domicilio;
	private String correo;
	private int telefono;
	private int movil;
	ArrayList<Cita> citas;
	ArrayList<Volante> volantes;
	Medico medicoAsignado;

	public Beneficiario() {
	}
	
	public Beneficiario(String nif, String nss, String nombre,
			String apellidos, String domicilio, String correo, int telefono,
			int movil) {
		this.nif = nif;
		this.nss = nss;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.domicilio = domicilio;
		this.correo = correo;
		this.telefono = telefono;
		this.movil = movil;
		this.medicoAsignado = null;
		ArrayList<Cita> citas = new ArrayList<Cita>();
		ArrayList<Volante> volantes = new ArrayList<Volante>();
	}

	public static Beneficiario consultarPorNIF(String nif) throws SQLException, BeneficiarioIncorrectoException, UsuarioIncorrectoException, CentroSaludIncorrectoException {
		return FPBeneficiario.consultarPorNIF(nif);
	}

	public static Beneficiario consultarPorNSS(String nss) throws SQLException, BeneficiarioIncorrectoException, UsuarioIncorrectoException, CentroSaludIncorrectoException {
		return FPBeneficiario.consultarPorNSS(nss);
	}

	public void insertar() throws SQLException {
		FPBeneficiario.insertar(this);
	}

	public void modificar() throws SQLException {
		FPBeneficiario.modificar(this);
	}

	public String getNif() {
		return nif;
	}

	public void setNif(String nif) {
		this.nif = nif;
	}

	public String getNss() {
		return nss;
	}

	public void setNss(String nss) {
		this.nss = nss;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public int getTelefono() {
		return telefono;
	}

	public void setTelefono(int telefono) {
		this.telefono = telefono;
	}

	public int getMovil() {
		return movil;
	}

	public void setMovil(int movil) {
		this.movil = movil;
	}

	public ArrayList<Cita> getCitas() {
		return citas;
	}

	public void setCitas(ArrayList<Cita> citas) {
		this.citas = citas;
	}

	public ArrayList<Volante> getVolantes() {
		return volantes;
	}

	public void setVolantes(ArrayList<Volante> volantes) {
		this.volantes = volantes;
	}

	public Medico getMedicoAsignado() {
		return medicoAsignado;
	}

	public void setMedicoAsignado(Medico medicoAsignado) {
		this.medicoAsignado = medicoAsignado;
	}
	
	
}
