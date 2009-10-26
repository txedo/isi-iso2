package persistencia;

public class ComandoSQL {

	private String sentencia;
	private Object[] parametros;
	private TipoComandoSQL tipo;
	
	public ComandoSQL(String sentencia, TipoComandoSQL tipo, Object... parametros) {
		this.sentencia = sentencia;
		this.parametros = parametros;
		this.tipo = tipo;
	}

	public String getSentencia() {
		return sentencia;
	}

	public Object[] getParametros() {
		return parametros;
	}
	
	public TipoComandoSQL getTipo() {
		return tipo;
	}

}
