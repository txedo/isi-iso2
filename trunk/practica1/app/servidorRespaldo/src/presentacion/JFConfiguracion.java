package presentacion;
import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.EventObject;
import java.util.regex.Pattern;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.EventListenerList;
import dominio.conocimiento.ConfiguracionRespaldo;

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
public class JFConfiguracion extends javax.swing.JFrame {

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

	public JFConfiguracion() {
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
				AnchorLayout pnlPanelLayout = new AnchorLayout();
				getContentPane().add(pnlPanel, BorderLayout.CENTER);
				pnlPanel.setLayout(pnlPanelLayout);
				pnlPanel.setPreferredSize(new java.awt.Dimension(229, 193));
				{
					btnAceptar = new JButton();
					pnlPanel.add(btnAceptar, new AnchorConstraint(151, 139, 803, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
					btnAceptar.setText("Aceptar");
					btnAceptar.setName("btnAceptar");
					btnAceptar.setDefaultCapable(true);
					btnAceptar.setPreferredSize(new java.awt.Dimension(80, 30));
					getRootPane().setDefaultButton(btnAceptar);
					btnAceptar.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							btnAceptarActionPerformed(evt);
						}
					});
				}
				{
					btnCancelar = new JButton();
					pnlPanel.add(btnCancelar, new AnchorConstraint(152, 15, 807, 134, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
					btnCancelar.setText("Cancelar");
					btnCancelar.setName("btnCancelar");
					btnCancelar.setPreferredSize(new java.awt.Dimension(80, 30));
					btnCancelar.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							btnCancelarActionPerformed(evt);
						}
					});
				}
				{
					pnlBDRespaldo = new JPanel();
					AnchorLayout pnlBDRespaldoLayout = new AnchorLayout();
					pnlPanel.add(pnlBDRespaldo, new AnchorConstraint(12, 10, 431, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
					pnlBDRespaldo.setBorder(BorderFactory.createTitledBorder(null, "Base de datos de respaldo", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma",0,11)));
					pnlBDRespaldo.setLayout(pnlBDRespaldoLayout);
					pnlBDRespaldo.setPreferredSize(new java.awt.Dimension(209, 73));
					{
						txtPuertoBDRespaldo = new JTextField();
						pnlBDRespaldo.add(txtPuertoBDRespaldo, new AnchorConstraint(595, 935, 869, 413, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
						txtPuertoBDRespaldo.setName("txtPuertoBDRespaldo");
						txtPuertoBDRespaldo.setPreferredSize(new java.awt.Dimension(109, 20));
					}
					{
						txtIPBDRespaldo = new JTextField();
						pnlBDRespaldo.add(txtIPBDRespaldo, new AnchorConstraint(253, 935, 527, 413, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
						txtIPBDRespaldo.setName("txtIPBDRespaldo");
						txtIPBDRespaldo.setPreferredSize(new java.awt.Dimension(109, 20));
					}
					{
						lblPuertoBDRespaldo = new JLabel();
						pnlBDRespaldo.add(lblPuertoBDRespaldo, new AnchorConstraint(609, 366, 801, 55, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
						lblPuertoBDRespaldo.setText("Puerto");
						lblPuertoBDRespaldo.setPreferredSize(new java.awt.Dimension(65, 14));
					}
					{
						lblIPBDRespaldo = new JLabel();
						pnlBDRespaldo.add(lblIPBDRespaldo, new AnchorConstraint(294, 366, 486, 55, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
						lblIPBDRespaldo.setText("Dirección IP");
						lblIPBDRespaldo.setPreferredSize(new java.awt.Dimension(65, 14));
					}
				}
				{
					pnlRespaldo = new JPanel();
					pnlPanel.add(pnlRespaldo, new AnchorConstraint(91, 10, 750, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
					pnlRespaldo.setBorder(BorderFactory.createTitledBorder(null, "Servidor de respaldo", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma",0,11)));
					pnlRespaldo.setLayout(null);
					pnlRespaldo.setFont(new java.awt.Font("Tahoma",1,11));
					pnlRespaldo.setPreferredSize(new java.awt.Dimension(209, 49));
					{
						txtPuertoRespaldo = new JTextField();
						pnlRespaldo.add(txtPuertoRespaldo);
						txtPuertoRespaldo.setBounds(114, 18, 79, 20);
						txtPuertoRespaldo.setName("txtPuertoRespaldo");
					}
					{
						lblPuertoRespaldo = new JLabel();
						pnlRespaldo.add(lblPuertoRespaldo);
						lblPuertoRespaldo.setText("Puerto de escucha");
						lblPuertoRespaldo.setBounds(11, 21, 97, 14);
					}
				}
			}
			pack();
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
		Pattern patronIP;
		int puerto;
		boolean valido;
		
		// Creamos un patrón que define las IPs válidas
		patronIP = Pattern.compile("\\b(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." + 
				"(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\b");
		
		// Comprobamos los datos de la BD de respaldo
		valido = true;
		if(!patronIP.matcher(txtIPBDRespaldo.getText()).matches()) {
			Dialogos.mostrarDialogoError(this, "Error", "La IP de la base de datos de respaldo tiene un formato incorrecto.");
			txtIPBDRespaldo.selectAll();
			txtIPBDRespaldo.grabFocus();
			valido = false;
		}
		if(valido) {
			try {
				puerto = Integer.parseInt(txtPuertoBDRespaldo.getText());
			} catch(NumberFormatException e) {
				puerto = -1;
			}
			if(puerto < 0 || puerto > 65535) {
				Dialogos.mostrarDialogoError(this, "Error", "El puerto de la base de datos de respaldo debe ser un número entre 0 y 65535.");
				txtPuertoBDRespaldo.selectAll();
				txtPuertoBDRespaldo.grabFocus();
				valido = false;				
			}
		}
		
		// Comprobamos los datos del servidor de respaldo
		if(valido) {
			try {
				puerto = Integer.parseInt(txtPuertoRespaldo.getText());
			} catch(NumberFormatException e) {
				puerto = -1;
			}
			if(puerto < 0 || puerto > 65535) {
				Dialogos.mostrarDialogoError(this, "Error", "El puerto de escucha del servidor de respaldo debe ser un número entre 0 y 65535.");
				txtPuertoRespaldo.selectAll();
				txtPuertoRespaldo.grabFocus();
				valido = false;				
			}
		}
		
		// Si los datos son válidos, cambiamos la configuración
		// de la instancia y cerramos la ventana
		if(valido) {
			try {
				configuracion.setIPBDRespaldo(txtIPBDRespaldo.getText());
				configuracion.setPuertoBDRespaldo(Integer.parseInt(txtPuertoBDRespaldo.getText()));
				configuracion.setPuertoRespaldo(Integer.parseInt(txtPuertoRespaldo.getText()));
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
