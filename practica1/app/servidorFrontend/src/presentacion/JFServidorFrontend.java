package presentacion;

import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;
import comunicaciones.ConfiguracionFrontend;

import dominio.control.ControladorFrontend;
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
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;

import presentacion.auxiliar.Dialogos;
import presentacion.auxiliar.IVentanaEstado;
import presentacion.auxiliar.VentanaCerradaListener;

import java.util.EventObject;

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
 * Ventana principal del servidor front-end.
 */
public class JFServidorFrontend extends javax.swing.JFrame implements IVentanaEstado {
	
	{
		//Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private static final long serialVersionUID = -113838536647924014L;
	
	private ControladorFrontend controlador;
	private ConfiguracionFrontend configuracion;
	private JFConfigFrontend frmConfiguracion;
	private JFAcercaDeFrontend frmAcercaDe;
	private int numeroClientes;

	private JLabel lblConfigRespaldo;
	private JLabel lblConfigBD;
	private JMenuItem mniDesconectar;
	private JMenuItem mniConectar;
	private JPanel pnlPanel;
	private JScrollPane scpPanelLog;
	private JLabel lblBarraEstado;
	private JButton btnDesconectar;
	private JLabel lblClientesConectados;
	private JButton btnConectar;
	private JButton btnSalir;
	private JMenuBar mnbMenus;
	private JTextArea txtLog;
	private JMenuItem mniAcercaDe;
	private JMenuItem mniConfigurar;
	private JMenuItem mniSalir;
	private JSeparator sepSeparador;
	private JMenu mnuArchivo;
	private JMenu mnuAyuda;
	private JMenu mnuOpciones;
	
	public JFServidorFrontend(ControladorFrontend controlador) {
		super();
		initGUI();
		this.controlador = controlador;
		configuracion = new ConfiguracionFrontend();
		actualizarEstado();
	}
	
	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
			this.setTitle("Servidor Front-End");
			this.setPreferredSize(new java.awt.Dimension(577, 409));
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
						mniConectar = new JMenuItem();
						mnuArchivo.add(mniConectar);
						mniConectar.setText("Conectar");
						mniConectar.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								mniConectarActionPerformed(evt);
							}
						});
					}
					{
						mniDesconectar = new JMenuItem();
						mnuArchivo.add(mniDesconectar);
						mniDesconectar.setText("Desconectar");
						mniDesconectar.setEnabled(false);
						mniDesconectar.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								mniDesconectarActionPerformed(evt);
							}
						});
					}
					{
						sepSeparador = new JSeparator();
						mnuArchivo.add(sepSeparador);
					}
					{
						mniSalir = new JMenuItem();
						mnuArchivo.add(mniSalir);
						mniSalir.setText("Salir");
						mniSalir.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								mniSalirActionPerformed(evt);
							}
						});
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
						mniAcercaDe.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								mniAcercaDeActionPerformed(evt);
							}
						});
					}
				}
			}
			{
				pnlPanel = new JPanel();
				AnchorLayout jPanel1Layout = new AnchorLayout();
				getContentPane().add(pnlPanel, BorderLayout.CENTER);
				pnlPanel.setLayout(jPanel1Layout);
				pnlPanel.setPreferredSize(new java.awt.Dimension(542, 327));
				{
					lblClientesConectados = new JLabel();
					pnlPanel.add(lblClientesConectados, new AnchorConstraint(855, 285, 30, 10, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
					lblClientesConectados.setText("0 clientes conectados.");
					lblClientesConectados.setPreferredSize(new java.awt.Dimension(239, 16));
					lblClientesConectados.setName("lblClientesConectados");
				}
				{
					scpPanelLog = new JScrollPane();
					pnlPanel.add(scpPanelLog, new AnchorConstraint(55, 10, 54, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
					scpPanelLog.setPreferredSize(new java.awt.Dimension(522, 212));
					scpPanelLog.setMinimumSize(new java.awt.Dimension(346, 155));
					{
						txtLog = new JTextArea();
						scpPanelLog.setViewportView(txtLog);
						txtLog.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
						txtLog.setEditable(false);
						txtLog.setFont(new java.awt.Font("Tahoma",0,12));
					}
				}
				{
					lblBarraEstado = new JLabel();
					pnlPanel.add(lblBarraEstado, new AnchorConstraint(937, 285, 11, 10, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
					lblBarraEstado.setText("Servidor desconectado.");
					lblBarraEstado.setPreferredSize(new java.awt.Dimension(239, 14));
					lblBarraEstado.setName("lblBarraEstado");
				}
				{
					btnDesconectar = new JButton();
					pnlPanel.add(btnDesconectar, new AnchorConstraint(13, 643, 124, 133, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
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
					pnlPanel.add(btnConectar, new AnchorConstraint(13, 322, 165, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
					btnConectar.setText("Conectar");
					btnConectar.setPreferredSize(new java.awt.Dimension(110, 30));
					btnConectar.setName("btnConectar");
					btnConectar.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							btnConectarActionPerformed(evt);
						}
					});
				}
				{
					btnSalir = new JButton();
					pnlPanel.add(btnSalir, new AnchorConstraint(13, 10, 127, 851, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
					btnSalir.setText("Salir");
					btnSalir.setPreferredSize(new java.awt.Dimension(63, 30));
					btnSalir.setName("btnSalir");
					btnSalir.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							btnSalirActionPerformed(evt);
						}
					});
				}
				{
					lblConfigBD = new JLabel();
					pnlPanel.add(lblConfigBD, new AnchorConstraint(872, 10, 30, 463, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE));
					lblConfigBD.setText("BD Principal: IP XXX.XXX.XXX.XXX, puerto XXXXX");
					lblConfigBD.setPreferredSize(new java.awt.Dimension(277, 14));
					lblConfigBD.setHorizontalAlignment(SwingConstants.TRAILING);
					lblConfigBD.setName("lblConfigBD");
				}
				{
					lblConfigRespaldo = new JLabel();
					pnlPanel.add(lblConfigRespaldo, new AnchorConstraint(925, 10, 11, 463, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE));
					lblConfigRespaldo.setText("Servidor Respaldo: IP XXX.XXX.XXX.XXX, puerto XXXXX");
					lblConfigRespaldo.setPreferredSize(new java.awt.Dimension(277, 15));
					lblConfigRespaldo.setHorizontalAlignment(SwingConstants.TRAILING);
					lblConfigRespaldo.setName("lblConfigRespaldo");
				}
			}
			pack();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void mniAcercaDeActionPerformed(ActionEvent evt) {
		frmAcercaDe = new JFAcercaDeFrontend();
		frmAcercaDe.addVentanaCerradaListener(new VentanaCerradaListener() {
			public void ventanaCerrada(EventObject evt) {    
				frmAcercaDeVentanaCerrada(evt);
			}
		});
		frmAcercaDe.setLocationRelativeTo(this);
		this.setEnabled(false);
		frmAcercaDe.setVisible(true);
	}
	
	private void frmAcercaDeVentanaCerrada(EventObject evt) {
		// Reactivamos la ventana 
		setEnabled(true);
		frmAcercaDe.setVisible(false);
		frmAcercaDe.dispose();
		frmAcercaDe = null;
	}
	
	private void mniConfigurarActionPerformed(ActionEvent evt) {
		// Creamos la ventana de configuración
		frmConfiguracion = new JFConfigFrontend();
		frmConfiguracion.addVentanaCerradaListener(new VentanaCerradaListener() {
			public void ventanaCerrada(EventObject evt) {    
				frmConfiguracionVentanaCerrada(evt);
			}
		});
		// Desactivamos la ventana hasta aceptar o cancelar la configuración
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
		actualizarEstado();
		// Eliminamos la ventana de configuración
		frmConfiguracion.dispose();
	}
	
	private void btnConectarActionPerformed(ActionEvent evt) {
		activarServidor();
	}
	
	private void btnDesconectarActionPerformed(ActionEvent evt) {
		confirmarDesactivarServidor();
	}

	private void thisWindowClosing(WindowEvent evt) {
		cerrarServidor();
	}
	
	private void mniConectarActionPerformed(ActionEvent evt) {
		activarServidor();
	}
	
	private void mniDesconectarActionPerformed(ActionEvent evt) {
		desactivarServidor();
	}

	private void mniSalirActionPerformed(ActionEvent evt) {
		cerrarServidor();
	}
	
	private void btnSalirActionPerformed(ActionEvent evt) {
		cerrarServidor();
	}
	
	private void actualizarEstado() {
		if(controlador != null && controlador.isServidorActivo()) {
			lblBarraEstado.setText("Servidor preparado en " + controlador.getIPServidor() + " (puerto " + String.valueOf(configuracion.getPuertoFrontend()) + ").");
		} else {
			lblBarraEstado.setText("Servidor desconectado (puerto " + String.valueOf(configuracion.getPuertoFrontend()) + ").");
		}
		lblConfigBD.setText("BD Principal: IP " + configuracion.getIPBDPrincipal() + ", puerto " + String.valueOf(configuracion.getPuertoBDPrincipal()));
		if(configuracion.isRespaldoActivado()) {
			lblConfigRespaldo.setText("Servidor Respaldo: IP " + configuracion.getIPRespaldo() + ", puerto " + String.valueOf(configuracion.getPuertoRespaldo()));
		} else {
			lblConfigRespaldo.setText("Servidor Respaldo: (desactivado)");
		}
	}
	
	private boolean activarServidor() {
		boolean ok;
		
		ok = false;
		try {
			// Iniciamos el servidor frontend y la conexión con el de respaldo
			controlador.iniciarServidor(configuracion);
			// Cambiamos el estado de la ventana
			btnConectar.setEnabled(false);
			mniConectar.setEnabled(false);
			mniConfigurar.setEnabled(false);
			btnDesconectar.setEnabled(true);
			mniDesconectar.setEnabled(true);
			actualizarEstado();
			ok = true;
		} catch(SQLException e) {
			ponerMensaje("Error: " + e.getLocalizedMessage());
		} catch(MalformedURLException e) {
			ponerMensaje("Error: " + e.getLocalizedMessage());
		} catch(UnknownHostException e) {
			ponerMensaje("Error: " + e.getLocalizedMessage());
		} catch(NotBoundException e) {
			ponerMensaje("Error: " + e.getLocalizedMessage());
		} catch(RemoteException e) {
			ponerMensaje("Error: " + e.getLocalizedMessage());
			// Si se produce un fallo de RMI al conectar el
			// servidor, lo desconectamos para hacer el "unexport"
			// y que se pueda conectar de nuevo más tarde
			desactivarServidor();
		}
		
		return ok;
	}
	
	private boolean desactivarServidor() {
		boolean ok;
		
		ok = false;
		try {
			// Desconectamos a los clientes, detenemos el servidor frontend y la conexión con el de respaldo
			controlador.detenerServidor(configuracion);
			// Cambiamos el estado de la ventana
			btnDesconectar.setEnabled(false);
			mniDesconectar.setEnabled(false);
			btnConectar.setEnabled(true);
			mniConectar.setEnabled(true);
			mniConfigurar.setEnabled(true);
			actualizarClientesEscuchando(controlador.getNumeroClientesConectados());
			actualizarEstado();
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
	
	private void confirmarDesactivarServidor() {
		// Si el servidor está activo, preguntamos antes de desconectarlo
		if(controlador.isServidorActivo()) {
			if(controlador.getNumeroClientesConectados() > 0) {
				if(Dialogos.mostrarDialogoPregunta(this, "Aviso", "Si desconecta el servidor front-end se perderá la conexión con los clientes.\n¿Realmente quiere desconectarlo?")) {
					desactivarServidor();
				}
			} else {
				desactivarServidor();
			}
		}
	}
	
	private void cerrarServidor() {
		boolean salir;
		
		// Si el servidor está activo, preguntamos antes de salir
		salir = false;
		if(controlador.isServidorActivo()) {
			if(Dialogos.mostrarDialogoPregunta(this, "Aviso", "Si cierra el servidor front-end, éste se desconectará automáticamente.\n¿Realmente quiere salir?")) {
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
	
	// Métodos públicos
	
	public void ponerMensaje(String mensaje) {
		txtLog.setText(txtLog.getText() + mensaje + "\n");
		txtLog.setCaretPosition(txtLog.getDocument().getLength());
	}

	public String getMensajes() {
		return txtLog.getText();
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
