package presentacion;

import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;
import dominio.conocimiento.ConfiguracionRespaldo;
import dominio.control.ControladorRespaldo;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.EventObject;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;

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
public class JFServidorRespaldo extends javax.swing.JFrame implements IVentanaEstado {
	
	{
		//Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private static final long serialVersionUID = -3739906711082199809L;
	
	private int numeroClientes;
	
	private ControladorRespaldo controlador;
	private ConfiguracionRespaldo configuracion;
	private JFConfiguracion frmConfiguracion;
	
	private JPanel jPanel;
	private JScrollPane scpPanelLog;
	private JLabel lblBarraEstado;
	private JButton btnDesconectar;
	private JMenuBar mnbMenus;
	private JButton btnConectar;
	private JTextArea textLog;
	private JMenuItem mniAcercaDe;
	private JMenuItem mniConfigurar;
	private JMenuItem mniSalir;
	private JMenu mnuArchivo;
	private JMenu mnuAyuda;
	private JMenu mnuOpciones;
	private JLabel lblClientesConectados;
	
	public JFServidorRespaldo(ControladorRespaldo controlador) {
		super();
		initGUI();
		this.controlador = controlador;
		configuracion = frmConfiguracion.getConfiguracion();
	}
	
	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
			this.setTitle("Servidor Respaldo");
			this.setPreferredSize(new java.awt.Dimension(550, 400));
			this.setMinimumSize(new java.awt.Dimension(500, 300));
			setLocationRelativeTo(null);
			this.addWindowListener(new WindowAdapter() { 
				public void windowClosing(WindowEvent evt) {    
					thisWindowClosing(evt);
				}
			});
			{
				mnbMenus = new JMenuBar();
				setJMenuBar(mnbMenus);
				{
					mnuArchivo = new JMenu();
					mnbMenus.add(mnuArchivo);
					mnuArchivo.setText("Archivo");
					{
						mniSalir = new JMenuItem();
						mnuArchivo.add(mniSalir);
						mniSalir.setText("Salir");
					}
				}
				{
					mnuOpciones = new JMenu();
					mnbMenus.add(mnuOpciones);
					mnuOpciones.setText("Opciones");
					{
						mniConfigurar = new JMenuItem();
						mnuOpciones.add(mniConfigurar);
						mniConfigurar.setText("Configurar...");
						mniConfigurar.setName("mniConfigurar");
						mniConfigurar.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								mniConfigurarActionPerformed(evt);
							}
						});
					}
				}
				{
					mnuAyuda = new JMenu();
					mnbMenus.add(mnuAyuda);
					mnuAyuda.setText("Ayuda");
					{
						mniAcercaDe = new JMenuItem();
						mnuAyuda.add(mniAcercaDe);
						mniAcercaDe.setText("Acerca de...");
					}
				}
			}
			{
				jPanel = new JPanel();
				AnchorLayout jPanel1Layout = new AnchorLayout();
				getContentPane().add(jPanel, BorderLayout.CENTER);
				jPanel.setLayout(jPanel1Layout);
				jPanel.setPreferredSize(new java.awt.Dimension(542, 327));
				{
					lblClientesConectados = new JLabel();
					jPanel.add(lblClientesConectados, new AnchorConstraint(855, 10, 30, 10, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
					lblClientesConectados.setText("0 clientes conectados.");
					lblClientesConectados.setPreferredSize(new java.awt.Dimension(522, 16));
				}
				{
					scpPanelLog = new JScrollPane();
					jPanel.add(scpPanelLog, new AnchorConstraint(55, 10, 54, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
					scpPanelLog.setPreferredSize(new java.awt.Dimension(522, 212));
					scpPanelLog.setMinimumSize(new java.awt.Dimension(346, 155));
					{
						textLog = new JTextArea();
						scpPanelLog.setViewportView(textLog);
						textLog.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
						textLog.setEditable(false);
						textLog.setFont(new java.awt.Font("Tahoma",0,12));
					}
				}
				{
					lblBarraEstado = new JLabel();
					jPanel.add(lblBarraEstado, new AnchorConstraint(937, 10, 11, 10, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
					lblBarraEstado.setText("Servidor desconectado.");
					lblBarraEstado.setPreferredSize(new java.awt.Dimension(522, 14));
					lblBarraEstado.setName("lblBarraEstado");
				}
				{
					btnDesconectar = new JButton();
					jPanel.add(btnDesconectar, new AnchorConstraint(13, 643, 124, 133, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
					btnDesconectar.setText("Desconectar");
					btnDesconectar.setPreferredSize(new java.awt.Dimension(116, 30));
					btnDesconectar.setEnabled(false);
					btnDesconectar.setName("btnDesconectar");
					btnDesconectar.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							btnDesconectarActionPerformed(evt);
						}
					});
				}
				{
					btnConectar = new JButton();
					jPanel.add(btnConectar, new AnchorConstraint(13, 322, 165, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
					btnConectar.setText("Conectar");
					btnConectar.setPreferredSize(new java.awt.Dimension(110, 30));
					btnConectar.setName("btnConectar");
					btnConectar.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							btnConectarActionPerformed(evt);
						}
					});
				}
			}
			frmConfiguracion = new JFConfiguracion();
			frmConfiguracion.addVentanaCerradaListener(new VentanaCerradaListener() {
				public void ventanaCerrada(EventObject evt) {    
					frmConfiguracionVentanaCerrada(evt);
				}
			});
			pack();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//$hide>>$
	
	private void mniConfigurarActionPerformed(ActionEvent evt) {
		// Desactivamos la ventana hasta aceptar o cancelar la configuraci�n
		setEnabled(false);
		frmConfiguracion.setConfiguracion(configuracion);
		frmConfiguracion.setLocationRelativeTo(this);
		frmConfiguracion.setVisible(true);
	}

	private void frmConfiguracionVentanaCerrada(EventObject evt) {
		// Reactivamos la ventana 
		setEnabled(true);
		frmConfiguracion.setVisible(false);
		configuracion = frmConfiguracion.getConfiguracion();
	}

	private void btnConectarActionPerformed(ActionEvent evt) {
		activarServidor();
	}
	
	private void btnDesconectarActionPerformed(ActionEvent evt) {
		desactivarServidor();
	}

	private void thisWindowClosing(WindowEvent evt) {
		boolean salir;
		
		// Si el servidor est� activo, preguntamos antes de salir
		salir = false;
		if(controlador.getServidorActivo()) {
			if(Dialogos.mostrarDialogoPregunta(this, "Aviso", "Si cierras el servidor de respaldo, se desconectar� autom�ticamente. �Realmente quieres salir?")) {
				if(desactivarServidor()) {
					salir = true;
				}
			}
		} else {
			salir = true;
		}
		
		if(salir) {
			setVisible(false);
			dispose();
			System.exit(0);
		}
	}
	
	private boolean activarServidor() {
		boolean ok;
		
		ok = false;
		try {
			// Iniciamos el servidor frontend y la conexi�n con el de respaldo
			controlador.iniciarServidorRespaldo(configuracion);
			// Cambiamos el estado de la ventana
			btnConectar.setEnabled(false);
			btnDesconectar.setEnabled(true);
			lblBarraEstado.setText("Servidor preparado.");
			ok = true;
		} catch(SQLException e) {
			ponerMensaje("Error: " + e.getLocalizedMessage());
		} catch(MalformedURLException e) {
			ponerMensaje("Error: " + e.getLocalizedMessage());
		} catch(UnknownHostException e) {
			ponerMensaje("Error: " + e.getLocalizedMessage());
		} catch (NotBoundException e) {
			ponerMensaje("Error: " + e.getLocalizedMessage());
		} catch(RemoteException e) {
			ponerMensaje("Error: " + e.getLocalizedMessage());
			// Si se produce un fallo de RMI al conectar el
			// servidor, lo desconectamos para hacer el "unexport"
			// y que se pueda conectar de nuevo m�s tarde
			desactivarServidor();
		}
		
		return ok;
	}
	
	private boolean desactivarServidor() {
		boolean ok;
		
		ok = false;
		try {
			// Detenemos el servidor de respaldo
			controlador.detenerServidorRespaldo(configuracion);
			// Cambiamos el estado de la ventana
			btnDesconectar.setEnabled(false);
			btnConectar.setEnabled(true);
			lblBarraEstado.setText("Servidor desconectado.");
			ok = true;
		} catch(SQLException e) {
			ponerMensaje("Error: " + e.getLocalizedMessage());
		} catch(RemoteException e) {
			ponerMensaje("Error: " + e.getLocalizedMessage());
		} catch(MalformedURLException e) {
			ponerMensaje("Error: " + e.getLocalizedMessage());
		} catch(UnknownHostException e) {
			ponerMensaje("Error: " + e.getLocalizedMessage());
		}
		
		return ok;
	}
	
	public void ponerMensaje(String mensaje) {
		textLog.setText(textLog.getText() + mensaje + "\n");	
	}
	
	public String getMensajes() {
		return textLog.getText();
	}

	public void actualizarClientesEscuchando(int numeroClientes) {
		this.numeroClientes = numeroClientes;
		if(numeroClientes == 1) {
			lblClientesConectados.setText(numeroClientes + " cliente conectado.");
		} else {
			lblClientesConectados.setText(numeroClientes + " clientes conectados.");
		}
	}
	
	public int getClientesEscuchando() {
		return numeroClientes;
	}
	
	//$hide<<$
	
}
