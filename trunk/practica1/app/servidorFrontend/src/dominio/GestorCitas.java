package dominio;

import java.sql.Date;
import java.util.Vector;

public class GestorCitas {

	public static Cita pedirCita(long idSesion, Beneficiario beneficiario, String idMedico, Date fechaYHora, long duracion) {
		throw new UnsupportedOperationException();
	}

	public static Cita pedirCita(long idSesion, Beneficiario beneficiario, long idVolante, Date fechaYHora, long duracion) {
		throw new UnsupportedOperationException();
	}

	public static Vector<Cita> getCitas(long idSesion, String dni) {
		throw new UnsupportedOperationException();
	}

	public static void anularCita(long idSesion, Cita cita) {
		throw new UnsupportedOperationException();
	}

}
