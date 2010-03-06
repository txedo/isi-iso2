package pruebas;

/**
 * Pruebas de la clase que se comunica con los clientes registrados
 * en el servidor front-end.
 */
public class PruebasProxyCliente extends PruebasBase {

	private ClientePrueba cliente;
	
	protected void setUp() {
		try {
			// Preparamos la base de datos
			super.setUp();
			// Creamos un cliente de prueba
			cliente = new ClientePrueba();
			cliente.activar(cliente.getDireccionIP());
		} catch(Exception e) {
			fail(e.toString());
		}
	}

	protected void tearDown() {
		try {
			// Desactivamos el cliente
			cliente.desactivar(cliente.getDireccionIP());
			// Cerramos la base de datos
			super.tearDown();
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	public void testA() {
		
	}
	
}