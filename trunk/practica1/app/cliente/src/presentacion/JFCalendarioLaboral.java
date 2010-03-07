package presentacion;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.EventObject;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.WindowConstants;
import javax.swing.event.EventListenerList;

import presentacion.auxiliar.VentanaCerradaListener;

import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;
import dominio.conocimiento.PeriodoTrabajo;

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
 * Ventana de consulta y edición del calendario laboral de los médicos.
 */
public class JFCalendarioLaboral extends javax.swing.JFrame {

	private static final long serialVersionUID = -2734721898893202142L;
	
	private EventListenerList listenerList;

	private JPCalendarioConsultar jPanelConsultarCalendario;
	private JButton btnRestablecerTodo;
	private JButton btnRestablecer;
	private JButton btnAceptar;

	public JFCalendarioLaboral() {
		super();
		initGUI();
		listenerList = new EventListenerList();
	}
	
	private void initGUI() {
		try {
			AnchorLayout thisLayout = new AnchorLayout();
			this.setLayout(thisLayout);
			this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
			this.setTitle("Configuración del calendario laboral");
			this.setResizable(false);
			this.setPreferredSize(new java.awt.Dimension(438, 293));
			this.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent evt) {
					thisWindowClosing(evt);
				}
			});
			{
				jPanelConsultarCalendario = new JPCalendarioConsultar();
				getContentPane().add(jPanelConsultarCalendario, new AnchorConstraint(2, 977, 836, 1, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
				jPanelConsultarCalendario.setVisible(true);
				jPanelConsultarCalendario.setPreferredSize(new java.awt.Dimension(412, 207));
				jPanelConsultarCalendario.setName("pnlConsultarCalendario");
			}
			{
				btnRestablecerTodo = new JButton();
				getContentPane().add(btnRestablecerTodo, new AnchorConstraint(916, 299, 11, 12, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
				btnRestablecerTodo.setText("Restablecer todo");
				btnRestablecerTodo.setPreferredSize(new java.awt.Dimension(117, 24));
				btnRestablecerTodo.setEnabled(false);
				btnRestablecerTodo.setName("btnRestablecerTodo");
				btnRestablecerTodo.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnRestablecerTodoActionPerformed(evt);
					}
				});
			}
			{
				btnRestablecer = new JButton();
				getContentPane().add(btnRestablecer, new AnchorConstraint(916, 136, 11, 540, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE));
				btnRestablecer.setText("Restablecer");
				btnRestablecer.setPreferredSize(new java.awt.Dimension(117, 24));
				btnRestablecer.setEnabled(false);
				btnRestablecer.setName("btnRestablecer");
				btnRestablecer.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnRestablecerActionPerformed(evt);
					}
				});
			}
			{
				btnAceptar = new JButton();
				getContentPane().add(btnAceptar, new AnchorConstraint(911, 10, 11, 779, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE));
				btnAceptar.setText("Aceptar");
				btnAceptar.setPreferredSize(new java.awt.Dimension(117, 24));
				btnAceptar.setName("btnAceptar");
				btnAceptar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnAceptarActionPerformed(evt);
					}
				});
			}
			pack();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//$hide>>$
	
	private void btnRestablecerActionPerformed(ActionEvent evt) {
		jPanelConsultarCalendario.restablecerDiaSeleccionado();
	}

	private void btnRestablecerTodoActionPerformed(ActionEvent evt) {
		jPanelConsultarCalendario.restablecerTodo();
	}

	private void thisWindowClosing(WindowEvent evt) {
		cerrarVentana();
	}
	
	private void btnAceptarActionPerformed(ActionEvent evt) {
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

	// Métodos públicos
	
	public Vector<PeriodoTrabajo> getPeriodosTrabajo() {
		return jPanelConsultarCalendario.getPeriodosTrabajo();
	}
	
	public void setPeriodosTrabajo(Vector<PeriodoTrabajo> periodos) {
		jPanelConsultarCalendario.setPeriodosTrabajo(periodos);
	}
	
	public int getHorasSemanales() {
		return jPanelConsultarCalendario.getHorasSemanales();
	}
	
	public void setModificable(boolean modificable) {
		btnRestablecer.setEnabled(modificable);
		btnRestablecerTodo.setEnabled(modificable);
		jPanelConsultarCalendario.setModificable(modificable);
	}
		
	public void addVentanaCerradaListener(VentanaCerradaListener listener) {
		listenerList.add(VentanaCerradaListener.class, listener);
	}

	public void removeVentanaCerradaListener(VentanaCerradaListener listener) {
		listenerList.remove(VentanaCerradaListener.class, listener);
	}
	
	//$hide<<$
	
}
