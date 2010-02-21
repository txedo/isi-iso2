package presentacion;
import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.EventObject;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.event.EventListenerList;
import presentacion.auxiliar.VentanaCerradaListener;


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
public class JFAcercaDe extends javax.swing.JFrame {

	private static final long serialVersionUID = -4903915570854306815L;

	{
		//Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private EventListenerList listenerList;
	private JButton btnAceptar;
	private JPanel jPanel1;
	private JTextPane txtTitulo2;
	private JTextPane txtTitulo;
	private JTextPane txtAcercaDe;
	
	public JFAcercaDe() {
		super();
		initGUI();
		listenerList = new EventListenerList();
	}
	
	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			AnchorLayout thisLayout = new AnchorLayout();
			getContentPane().setLayout(thisLayout);
			this.setTitle("Acerca de Servidor de Respaldo (SSCA)");
			{
				jPanel1 = new JPanel();
				AnchorLayout jPanel1Layout = new AnchorLayout();
				getContentPane().add(jPanel1, new AnchorConstraint(1, 1001, 1001, 1, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
				jPanel1.setPreferredSize(new java.awt.Dimension(394, 275));
				jPanel1.setLayout(jPanel1Layout);
				{
					txtTitulo2 = new JTextPane();
					jPanel1.add(txtTitulo2, new AnchorConstraint(37, 89, 209, 101, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
					txtTitulo2.setText("Servidor de Respaldo");
					txtTitulo2.setPreferredSize(new java.awt.Dimension(204, 30));
					txtTitulo2.setFont(new java.awt.Font("Tahoma",1,20));
					txtTitulo2.setOpaque(false);
					txtTitulo2.setFocusable(false);
					txtTitulo2.setEditable(false);
				}
				{
					txtTitulo = new JTextPane();
					jPanel1.add(txtTitulo, new AnchorConstraint(12, 29, 246, 33, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
					txtTitulo.setText("Sistema de Salud de Comunidades Autónomas");
					txtTitulo.setPreferredSize(new java.awt.Dimension(352, 17));
					txtTitulo.setOpaque(false);
					txtTitulo.setEditable(false);
					txtTitulo.setFont(new java.awt.Font("Tahoma",1,14));
					txtTitulo.setFocusable(false);
				}
				{
					txtAcercaDe = new JTextPane();
					jPanel1.add(txtAcercaDe, new AnchorConstraint(266, 965, 827, 26, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
					txtAcercaDe.setPreferredSize(new java.awt.Dimension(370, 155));
					txtAcercaDe.setOpaque(false);
					txtAcercaDe.setContentType("text/html");
					txtAcercaDe.setText("Versión: 1.0<br>\nPágina web: <a href=\"http://www.inf-cr.uclm.es\">ESI@UCLM</a><br>\nDesarrolladores:<br>\n<ul>Juan Andrada Romero (<a href=\"mailto:juan.andrada@alu.uclm.es\">juan.andrada@alu.uclm.es</a>)<br>\nJuan Gallardo Casero (<a href=\"mailto:juan.gallardo@alu.uclm.es\">juan.gallardo@alu.uclm.es</a>)<br>\nJose Domingo López López (<a href=\"mailto:josed.lopez1@alu.uclm.es\">josed.lopez1@alu.uclm.es</a>)<br>");
					txtAcercaDe.setEditable(false);
					txtAcercaDe.setFocusable(false);
					txtAcercaDe.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
				}
				{
					btnAceptar = new JButton();
					jPanel1.add(btnAceptar, new AnchorConstraint(866, 14, 12, 753, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE));
					btnAceptar.setText("Aceptar");
					btnAceptar.setPreferredSize(new java.awt.Dimension(83, 25));
					btnAceptar.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							btnAceptarActionPerformed(evt);
						}
					});
				}
			}
			setResizable(false);
			this.setPreferredSize(new java.awt.Dimension(400, 300));
			this.setMaximumSize(new java.awt.Dimension(420, 300));
			this.setMinimumSize(new java.awt.Dimension(420, 300));
			this.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent evt) {
					thisWindowClosing(evt);
				}
			});
			pack();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void btnAceptarActionPerformed(ActionEvent evt) {
		cerrarVentana();
	}
	
	private void thisWindowClosing(WindowEvent evt) {
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
	
	public void addVentanaCerradaListener(VentanaCerradaListener listener) {
		listenerList.add(VentanaCerradaListener.class, listener);
	}

	public void removeVentanaCerradaListener(VentanaCerradaListener listener) {
		listenerList.remove(VentanaCerradaListener.class, listener);
	}
}
