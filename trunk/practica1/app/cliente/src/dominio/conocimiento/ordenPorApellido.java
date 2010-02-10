package dominio.conocimiento;

public class ordenPorApellido implements java.util.Comparator {
	
	 public int compare(Object medico1, Object medico2) {
		return ((Medico)medico1).getApellidos().compareTo(((Medico)medico2).getApellidos());
	 }

}
