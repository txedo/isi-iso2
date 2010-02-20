package presentacion;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.EventObject;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

import presentacion.auxiliar.Dialogos;
import presentacion.auxiliar.Validacion;
import presentacion.auxiliar.VentanaCerradaListener;
import dominio.conocimiento.ConfiguracionFrontend;
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
 * Ventana de configuración de las conexiones del servidor front-end.
 */
public class JFConfigFrontend extends javax.swing.JFrame {

	{
		//Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private static final long serialVersionUID = -7821595354878872893L;
	
	private EventListenerList listenerList;
	private ConfiguracionFrontend configuracion;

	private JLabel lblIPRespaldo;
	private JLabel lblPuertoRespaldo;
	private JTextField txtIPRespaldo;
	private JTextField txtPuertoRespaldo;
	private JPanel pnlRespaldo;
	private JPanel pnlPanel;
	private JButton btnAceptar;
	private JButton btnCancelar;
	private JPanel pnlFrontend;
	private JPanel pnlBDPrincipal;
	private JTextField txtPuertoFrontend;
	private JTextField txtPuertoBDPrincipal;
	private JLabel lblRespaldo;
	private JCheckBox chkRespaldo;
	private JTextField txtIPBDPrincipal;
	private JLabel lblPuertoFrontend;
	private JLabel lblPuertoBDPrincipal;
	private JLabel lblIPBDPrincipal;

	public JFConfigFrontend() {
		super();
		initGUI();
		setConfiguracion(new ConfiguracionFrontend());
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
					btnAceptar.setBounds(10, 282, 82, 30);
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
					btnCancelar.setBounds(137, 282, 82, 30);
					btnCancelar.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							btnCancelarActionPerformed(evt);
						}
					});
				}
				{
					pnlBDPrincipal = new JPanel();
					pnlPanel.add(pnlBDPrincipal);
					pnlBDPrincipal.setBorder(BorderFactory.createTitledBorder("Base de datos principal"));
					pnlBDPrincipal.setLayout(null);
					pnlBDPrincipal.setBounds(10, 10, 209, 84);
					{
						txtPuertoBDPrincipal = new JTextField();
						pnlBDPrincipal.add(txtPuertoBDPrincipal);
						txtPuertoBDPrincipal.setName("txtPuertoBDPrincipal");
						txtPuertoBDPrincipal.setBounds(90, 51, 103, 20);
					}
					{
						txtIPBDPrincipal = new JTextField();
						pnlBDPrincipal.add(txtIPBDPrincipal);
						txtIPBDPrincipal.setName("txtIPBDPrincipal");
						txtIPBDPrincipal.setBounds(90, 22, 103, 20);
					}
					{
						lblPuertoBDPrincipal = new JLabel();
						pnlBDPrincipal.add(lblPuertoBDPrincipal);
						lblPuertoBDPrincipal.setText("Puerto");
						lblPuertoBDPrincipal.setBounds(12, 54, 68, 14);
					}
					{
						lblIPBDPrincipal = new JLabel();
						pnlBDPrincipal.add(lblIPBDPrincipal);
						lblIPBDPrincipal.setText("Dirección IP");
						lblIPBDPrincipal.setBounds(12, 25, 68, 14);
					}
				}
				{
					pnlFrontend = new JPanel();
					pnlPanel.add(pnlFrontend);
					pnlFrontend.setBorder(BorderFactory.createTitledBorder("Servidor front-end"));
					pnlFrontend.setLayout(null);
					pnlFrontend.setFont(new java.awt.Font("Tahoma",1,11));
					pnlFrontend.setBounds(10, 218, 209, 55);
					{
						txtPuertoFrontend = new JTextField();
						pnlFrontend.add(txtPuertoFrontend);
						txtPuertoFrontend.setBounds(114, 20, 79, 20);
						txtPuertoFrontend.setName("txtPuertoFrontend");
					}
					{
						lblPuertoFrontend = new JLabel();
						pnlFrontend.add(lblPuertoFrontend);
						lblPuertoFrontend.setText("Puerto de escucha");
						lblPuertoFrontend.setBounds(11, 23, 97, 14);
					}
				}
				{
					pnlRespaldo = new JPanel();
					pnlPanel.add(pnlRespaldo);
					pnlRespaldo.setBorder(BorderFactory.createTitledBorder("Servidor de respaldo"));
					pnlRespaldo.setBounds(10, 100, 209, 112);
					pnlRespaldo.setLayout(null);
					{
						txtPuertoRespaldo = new JTextField();
						pnlRespaldo.add(txtPuertoRespaldo);
						txtPuertoRespaldo.setName("txtPuertoRespaldo");
						txtPuertoRespaldo.setBounds(90, 78, 103, 20);
					}
					{
						txtIPRespaldo = new JTextField();
						pnlRespaldo.add(txtIPRespaldo);
						txtIPRespaldo.setName("txtIPRespaldo");
						txtIPRespaldo.setBounds(90, 49, 103, 20);
					}
					{
						lblPuertoRespaldo = new JLabel();
						pnlRespaldo.add(lblPuertoRespaldo);
						lblPuertoRespaldo.setText("Puerto");
						lblPuertoRespaldo.setBounds(12, 81, 68, 14);
					}
					{
						lblIPRespaldo = new JLabel();
						pnlRespaldo.add(lblIPRespaldo);
						lblIPRespaldo.setText("Dirección IP");
						lblIPRespaldo.setBounds(12, 52, 68, 14);
					}
					{
						lblRespaldo = new JLabel();
						pnlRespaldo.add(lblRespaldo);
						lblRespaldo.setText("Activado");
						lblRespaldo.setBounds(12, 25, 42, 14);
					}
					{
						chkRespaldo = new JCheckBox();
						pnlRespaldo.add(chkRespaldo);
						chkRespaldo.setName("chkRespaldo");
						chkRespaldo.setBounds(87, 21, 26, 23);
						chkRespaldo.addChangeListener(new ChangeListener() {
							public void stateChanged(ChangeEvent evt) {
								chkRespaldoStateChanged(evt);
							}
						});
					}
				}
			}
			pack();
			this.setSize(237, 353);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//$hide>>$
	
	public ConfiguracionFrontend getConfiguracion() {
		// Devolvemos la última configuración aceptada
		// (no la mostrada actualmente en la ventana)
		return configuracion;
	}
	
	public void setConfiguracion(ConfiguracionFrontend configuracion) {
		// Cambia la configuración aceptada y mostrada en la ventana
		this.configuracion = configuracion;
		txtIPBDPrincipal.setText(configuracion.getIPBDPrincipal());
		txtPuertoBDPrincipal.setText(String.valueOf(configuracion.getPuertoBDPrincipal()));
		txtIPRespaldo.setText(configuracion.getIPRespaldo());
		txtPuertoRespaldo.setText(String.valueOf(configuracion.getPuertoRespaldo()));
		chkRespaldo.setSelected(configuracion.isRespaldoActivado());
		txtIPRespaldo.setEnabled(configuracion.isRespaldoActivado());
		txtPuertoRespaldo.setEnabled(configuracion.isRespaldoActivado());
		txtPuertoFrontend.setText(String.valueOf(configuracion.getPuertoFrontend()));
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

	private void chkRespaldoStateChanged(ChangeEvent evt) {
		txtIPRespaldo.setEnabled(chkRespaldo.isSelected());
		txtPuertoRespaldo.setEnabled(chkRespaldo.isSelected());
	}

	private void btnAceptarActionPerformed(ActionEvent evt) {
		boolean valido;
		
		// Comprobamos los datos de la BD principal
		valido = true;
		try {
			Validacion.comprobarDireccionIP(txtIPBDPrincipal.getText().trim());
			Validacion.comprobarPuerto(txtPuertoBDPrincipal.getText().trim());
		} catch(IPInvalidaException ex) {
			Dialogos.mostrarDialogoError(this, "Error", "La dirección IP de la base de datos principal tiene un formato incorrecto.");
			txtIPBDPrincipal.selectAll();
			txtIPBDPrincipal.grabFocus();
			valido = false;
		} catch(PuertoInvalidoException ex) {
			Dialogos.mostrarDialogoError(this, "Error", "El puerto de la base de datos principal debe ser un número entre " + String.valueOf(Validacion.PUERTO_MINIMO) + " y " + String.valueOf(Validacion.PUERTO_MAXIMO) + ".");
			txtPuertoBDPrincipal.selectAll();
			txtPuertoBDPrincipal.grabFocus();
			valido = false;
		} catch(NumberFormatException ex) {
			Dialogos.mostrarDialogoError(this, "Error", "El puerto de la base de datos principal debe ser un número entre " + String.valueOf(Validacion.PUERTO_MINIMO) + " y " + String.valueOf(Validacion.PUERTO_MAXIMO) + ".");
			txtPuertoBDPrincipal.selectAll();
			txtPuertoBDPrincipal.grabFocus();
			valido = false;
		}
		
		// Comprobamos los datos del servidor de respaldo (si está activo)
		if(valido && chkRespaldo.isSelected()) {
			try {
				Validacion.comprobarDireccionIP(txtIPRespaldo.getText().trim());
				Validacion.comprobarPuerto(txtPuertoRespaldo.getText().trim());
			} catch(IPInvalidaException ex) {
				Dialogos.mostrarDialogoError(this, "Error", "La dirección IP del servidor de respaldo tiene un formato incorrecto.");
				txtIPRespaldo.selectAll();
				txtIPRespaldo.grabFocus();
				valido = false;
			} catch(PuertoInvalidoException ex) {
				Dialogos.mostrarDialogoError(this, "Error", "El puerto del servidor de respaldo debe ser un número entre " + String.valueOf(Validacion.PUERTO_MINIMO) + " y " + String.valueOf(Validacion.PUERTO_MAXIMO) + ".");
				txtPuertoRespaldo.selectAll();
				txtPuertoRespaldo.grabFocus();
				valido = false;
			} catch(NumberFormatException ex) {
				Dialogos.mostrarDialogoError(this, "Error", "El puerto del servidor de respaldo debe ser un número entre " + String.valueOf(Validacion.PUERTO_MINIMO) + " y " + String.valueOf(Validacion.PUERTO_MAXIMO) + ".");
				txtPuertoRespaldo.selectAll();
				txtPuertoRespaldo.grabFocus();
				valido = false;
			}
		}
		
		// Comprobamos los datos del servidor frontend
		if(valido) {
			try {
				Validacion.comprobarPuerto(txtPuertoFrontend.getText().trim());
			} catch(PuertoInvalidoException ex) {
				Dialogos.mostrarDialogoError(this, "Error", "El puerto de escucha del servidor front-end debe ser un número entre " + String.valueOf(Validacion.PUERTO_MINIMO) + " y " + String.valueOf(Validacion.PUERTO_MAXIMO) + ".");
				txtPuertoFrontend.selectAll();
				txtPuertoFrontend.grabFocus();
				valido = false;
			} catch(NumberFormatException ex) {
				Dialogos.mostrarDialogoError(this, "Error", "El puerto de escucha del servidor front-end debe ser un número entre " + String.valueOf(Validacion.PUERTO_MINIMO) + " y " + String.valueOf(Validacion.PUERTO_MAXIMO) + ".");
				txtPuertoFrontend.selectAll();
				txtPuertoFrontend.grabFocus();
				valido = false;
			}
		}
		
		// Si los datos son válidos, cambiamos la configuración
		// de la instancia y cerramos la ventana
		if(valido) {
			try {
				configuracion.setIPBDPrincipal(txtIPBDPrincipal.getText().trim());
				configuracion.setPuertoBDPrincipal(Integer.parseInt(txtPuertoBDPrincipal.getText().trim()));
				configuracion.setRespaldoActivado(chkRespaldo.isSelected());
				if(chkRespaldo.isSelected()) {
					configuracion.setIPRespaldo(txtIPRespaldo.getText().trim());
					configuracion.setPuertoRespaldo(Integer.parseInt(txtPuertoRespaldo.getText().trim()));
				} else {
					configuracion.setIPRespaldo((new ConfiguracionFrontend()).getIPRespaldo());
					configuracion.setPuertoRespaldo((new ConfiguracionFrontend()).getPuertoRespaldo());
				}
				configuracion.setPuertoFrontend(Integer.parseInt(txtPuertoFrontend.getText().trim()));
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
