package presentacion;

import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;
import dominio.conocimiento.Operaciones;


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
* Panel que muestra una serie de operaciones y permite seleccionar una.
*/
public class JPOperaciones extends JPanel {

	private static final long serialVersionUID = -6305737945313540184L;
	
	private EventListenerList listenerList;
	
	private ArrayList<OperacionesInterfaz> operaciones;
	
	private DefaultListModel lstOperacionesModel;
	private DefaultListCellRenderer lstOperacionesRenderer;
	private JLabel lblOperaciones;
	private JList lstOperaciones;

	public JPOperaciones() {
		super();
		initGUI();
		operaciones = new ArrayList<OperacionesInterfaz>();
		listenerList = new EventListenerList();
		lstOperacionesRenderer = new DefaultListCellRenderer();
		lstOperacionesRenderer.setHorizontalAlignment(JLabel.CENTER);
		lstOperaciones.setCellRenderer(lstOperacionesRenderer);
	}
	
	private void initGUI() {
		try {
			AnchorLayout thisLayout = new AnchorLayout();
			this.setLayout(thisLayout);
			this.setSize(130, 310);
			this.setPreferredSize(new java.awt.Dimension(130, 310));
			{
				lblOperaciones = new JLabel();
				this.add(lblOperaciones, new AnchorConstraint(2, 1004, 89, 7, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblOperaciones.setText("Operaciones");
				lblOperaciones.setPreferredSize(new java.awt.Dimension(123, 15));
				lblOperaciones.setHorizontalAlignment(SwingConstants.CENTER);
			}
			{
				lstOperaciones = new JList();
				this.add(lstOperaciones, new AnchorConstraint(21, 965, 11, 7, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
				lstOperaciones.setPreferredSize(new java.awt.Dimension(118, 278));
				lstOperaciones.setLayout(null);
				lstOperaciones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				lstOperaciones.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
				lstOperaciones.setFixedCellHeight(35);
				lstOperaciones.addListSelectionListener(new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent evt) {
						lstOperacionesValueChanged(evt);
					}
				});
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//$hide>>$
	
	public void ponerOperacion(OperacionesInterfaz operacion) {
		if(!operaciones.contains(operacion)) {
			operaciones.add(operacion);
			actualizarListaOperaciones();
		}
	}
	
	public void quitarOperacion(OperacionesInterfaz operacion) {
		if(operaciones.contains(operacion)) {
			operaciones.remove(operacion);
			actualizarListaOperaciones();
		}		
	}
	
	public void setOperacion(OperacionesInterfaz operacion) {
		if(operaciones.contains(operacion)) {
			lstOperaciones.setSelectedValue(operaciones.indexOf(operacion), true);
		}
	}

	public OperacionesInterfaz getOperacion() {
		OperacionesInterfaz operacionSel;
		
		if(lstOperaciones.getSelectedIndex() != -1) {
			operacionSel = operaciones.get(lstOperaciones.getSelectedIndex());
		} else {
			operacionSel = OperacionesInterfaz.OperacionInvalida;
		}
		return operacionSel;
	}
	
	private void actualizarListaOperaciones() {
		lstOperacionesModel = new DefaultListModel();
		for(OperacionesInterfaz operacion : operaciones) {
			lstOperacionesModel.addElement("<html><center>" + nombreOperacion(operacion) + "</center></html>");
		}
		lstOperaciones.setModel(lstOperacionesModel);
		if(operaciones.size() != 0) {
			lstOperaciones.setSelectedIndex(0);
		}
		else {
			lstOperaciones.setSelectedIndex(-1);
		}
		lstOperaciones.repaint();
	}
	
	private void lstOperacionesValueChanged(ListSelectionEvent evt) {
		Object[] listeners;
		int i;

		// Notificamos que ha cambiado la operación seleccionada
		listeners = listenerList.getListenerList();
		for(i = 0; i < listeners.length; i += 2) {
			if(listeners[i] == OperacionCambiadaListener.class) {
				((OperacionCambiadaListener)listeners[i + 1]).operacionCambiada(evt);
			}
		}
	}
	
	public void addOperacionCambiadaListener(OperacionCambiadaListener listener) {
		listenerList.add(OperacionCambiadaListener.class, listener);
	}

	public void removeOperacionCambiadaListener(OperacionCambiadaListener listener) {
		listenerList.remove(OperacionCambiadaListener.class, listener);
	}

	private String nombreOperacion(OperacionesInterfaz operacion) {
		String nombre;
		
		switch(operacion) {
		case RegistrarBeneficiario:
			nombre = "Registrar beneficiario";
			break;
		case ConsultarModificarBeneficiario:
			nombre = "Consultar o<br>modificar beneficiario";
			break;
		case RegistrarUsuario:
			nombre = "Registrar usuario";
			break;
		case ConsultarModificarUsuario:
			nombre = "Consultar o<br>modificar usuario";
			break;
		case TramitarCita:
			nombre = "Tramitar cita";
			break;
		case ConsultarAnularCita:
			nombre = "Consultar o<br>anular cita";
			break;
		case EstablecerSustituto:
			nombre = "Establecer sustituto";
			break;
		default:
			nombre = "Operación desconocida";
			break;
		}
		
		return nombre;
	}
		
	//$hide<<$
	
}
