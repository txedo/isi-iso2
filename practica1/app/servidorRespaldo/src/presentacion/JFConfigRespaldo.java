package presentacion;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.EventObject;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.EventListenerList;

import presentacion.auxiliar.Dialogos;
import presentacion.auxiliar.Validacion;
import presentacion.auxiliar.VentanaCerradaListener;
import dominio.conocimiento.ConfiguracionRespaldo;
import excepciones.IPInvalidaException;
import excepciones.PuertoInvalidoException;

/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
/**
 * Ventana de configuración del servidor de respaldo.
 */
public class JFConfigRespaldo extends javax.swing.JFrame {

	{
		//Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private static final long serialVersionUID = -8427011762744694132L;

	private EventListenerList listenerList;
	private ConfiguracionRespaldo configuracion;
		
	private JPanel pnlPanel;
	private JButton btnAceptar;
	private JButton btnCancelar;
	private JPanel pnlRespaldo;
	private JPanel pnlBDRespaldo;
	private JTextField txtPuertoRespaldo;
	private JTextField txtPuertoBDRespaldo;
	private JTextField txtIPBDRespaldo;
	private JLabel lblPuertoRespaldo;
	private JLabel lblPuertoBDRespaldo;
	private JLabel lblIPBDRespaldo;

	public JFConfigRespaldo() {
		super();
		initGUI();
		setConfiguracion(new ConfiguracionRespaldo());
		listenerList = new EventListenerList();
	}
	
	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
			this.setTitle("Configuración");
			this.setResizable(false);
			this.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent evt) {
					thisWindowClosing(evt);
				}
			});
			{
				pnlPanel = new JPanel();
				getContentPane().add(pnlPanel, BorderLayout.CENTER);
				pnlPanel.setLayout(null);
				{
					btnAceptar = new JButton();
					pnlPanel.add(btnAceptar);
					btnAceptar.setText("Aceptar");
					btnAceptar.setName("btnAceptar");
					btnAceptar.setDefaultCapable(true);
					btnAceptar.setBounds(10, 164, 82, 30);
					getRootPane().setDefaultButton(btnAceptar);
					btnAceptar.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							btnAceptarActionPerformed(evt);
						}
					});
				}
				{
					btnCancelar = new JButton();
					pnlPanel.add(btnCancelar);
					btnCancelar.setText("Cancelar");
					btnCancelar.setName("btnCancelar");
					btnCancelar.setBounds(137, 164, 82, 30);
					btnCancelar.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							btnCancelarActionPerformed(evt);
						}
					});
				}
				{
					pnlBDRespaldo = new JPanel();
					pnlPanel.add(pnlBDRespaldo);
					pnlBDRespaldo.setBorder(BorderFactory.createTitledBorder(null, "Base de datos de respaldo", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma",0,11)));
					pnlBDRespaldo.setLayout(null);
					pnlBDRespaldo.setBounds(10, 10, 209, 84);
					{
						txtPuertoBDRespaldo = new JTextField();
						pnlBDRespaldo.add(txtPuertoBDRespaldo);
						txtPuertoBDRespaldo.setName("txtPuertoBDRespaldo");
						txtPuertoBDRespaldo.setBounds(90, 51, 103, 20);
					}
					{
						txtIPBDRespaldo = new JTextField();
						pnlBDRespaldo.add(txtIPBDRespaldo);
						txtIPBDRespaldo.setName("txtIPBDRespaldo");
						txtIPBDRespaldo.setBounds(90, 22, 103, 20);
					}
					{
						lblPuertoBDRespaldo = new JLabel();
						pnlBDRespaldo.add(lblPuertoBDRespaldo);
						lblPuertoBDRespaldo.setText("Puerto");
						lblPuertoBDRespaldo.setBounds(12, 54, 68, 14);
					}
					{
						lblIPBDRespaldo = new JLabel();
						pnlBDRespaldo.add(lblIPBDRespaldo);
						lblIPBDRespaldo.setText("Dirección IP");
						lblIPBDRespaldo.setBounds(12, 25, 68, 14);
					}
				}
				{
					pnlRespaldo = new JPanel();
					pnlPanel.add(pnlRespaldo);
					pnlRespaldo.setBorder(BorderFactory.createTitledBorder(null, "Servidor de respaldo", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma",0,11)));
					pnlRespaldo.setLayout(null);
					pnlRespaldo.setFont(new java.awt.Font("Tahoma",1,11));
					pnlRespaldo.setBounds(10, 100, 209, 55);
					{
						txtPuertoRespaldo = new JTextField();
						pnlRespaldo.add(txtPuertoRespaldo);
						txtPuertoRespaldo.setBounds(114, 20, 79, 20);
						txtPuertoRespaldo.setName("txtPuertoRespaldo");
					}
					{
						lblPuertoRespaldo = new JLabel();
						pnlRespaldo.add(lblPuertoRespaldo);
						lblPuertoRespaldo.setText("Puerto de escucha");
						lblPuertoRespaldo.setBounds(11, 23, 97, 14);
					}
				}
			}
			pack();
			this.setSize(237, 237);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//$hide>>$
	
	public ConfiguracionRespaldo getConfiguracion() {
		// Devolvemos la última configuración aceptada
		// (no la mostrada actualmente en la ventana)
		return configuracion;
	}
	
	public void setConfiguracion(ConfiguracionRespaldo configuracion) {
		// Cambia la configuración aceptada y mostrada en la ventana
		this.configuracion = configuracion;
		txtIPBDRespaldo.setText(configuracion.getIPBDRespaldo());
		txtPuertoBDRespaldo.setText(String.valueOf(configuracion.getPuertoBDRespaldo()));
		txtPuertoRespaldo.setText(String.valueOf(configuracion.getPuertoRespaldo()));
	}
	
	public void addVentanaCerradaListener(VentanaCerradaListener listener) {
		listenerList.add(VentanaCerradaListener.class, listener);
	}

	public void removeVentanaCerradaListener(VentanaCerradaListener listener) {
		listenerList.remove(VentanaCerradaListener.class, listener);
	}
	
	private void thisWindowClosing(WindowEvent evt) {
		cerrarVentana();
	}

	private void btnAceptarActionPerformed(ActionEvent evt) {
		boolean valido;
		
		// Comprobamos los datos de la BD de respaldo
		valido = true;
		try {
			Validacion.comprobarDireccionIP(txtIPBDRespaldo.getText().trim());
			Validacion.comprobarPuerto(txtPuertoBDRespaldo.getText().trim());
		} catch(IPInvalidaException ex) {
			Dialogos.mostrarDialogoError(this, "Error", "La dirección IP de la base de datos de respaldo tiene un formato incorrecto.");
			txtIPBDRespaldo.selectAll();
			txtIPBDRespaldo.grabFocus();
			valido = false;
		} catch(PuertoInvalidoException ex) {
			Dialogos.mostrarDialogoError(this, "Error", "El puerto de la base de datos de respaldo debe ser un número entre " + String.valueOf(Validacion.PUERTO_MINIMO) + " y " + String.valueOf(Validacion.PUERTO_MAXIMO) + ".");
			txtPuertoBDRespaldo.selectAll();
			txtPuertoBDRespaldo.grabFocus();
			valido = false;
		}

		// Comprobamos los datos del servidor de respaldo
		if(valido) {
			try {
				Validacion.comprobarPuerto(txtPuertoRespaldo.getText().trim());
			} catch(PuertoInvalidoException ex) {
				Dialogos.mostrarDialogoError(this, "Error", "El puerto de escucha del servidor de respaldo debe ser un número entre " + String.valueOf(Validacion.PUERTO_MINIMO) + " y " + String.valueOf(Validacion.PUERTO_MAXIMO) + ".");
				txtPuertoRespaldo.selectAll();
				txtPuertoRespaldo.grabFocus();
				valido = false;
			}
		}
		
		// Si los datos son válidos, cambiamos la configuración
		// de la instancia y cerramos la ventana
		if(valido) {
			try {
				configuracion.setIPBDRespaldo(txtIPBDRespaldo.getText().trim());
				configuracion.setPuertoBDRespaldo(Integer.parseInt(txtPuertoBDRespaldo.getText().trim()));
				configuracion.setPuertoRespaldo(Integer.parseInt(txtPuertoRespaldo.getText().trim()));
			} catch(NumberFormatException e) {
				// No puede haber errores porque ya se ha
				// comprobado que los puertos son válidos
			}
			cerrarVentana();
		}
	}
	
	private void btnCancelarActionPerformed(ActionEvent evt) {
		cerrarVentana();
	}
	
	private void cerrarVentana() {
		Object[] listeners;
		int i;
		
		// Notificamos que la ventana se ha cerrado
		listeners = listenerList.getListenerList();
		for(i = 0; i < listeners.length; i += 2) {
			if(listeners[i] == VentanaCerradaListener.class) {
				((VentanaCerradaListener)listeners[i + 1]).ventanaCerrada(new EventObject(this));
			}
		}
	}

	//$hide<<$

}
