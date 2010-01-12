package presentacion;

import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.border.BevelBorder;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;
import dominio.conocimiento.Operaciones;

/**
* Panel que muestra una serie de operaciones y permite seleccionar una.
*/
public class JPOperaciones extends JPanel {

	private static final long serialVersionUID = -6305737945313540184L;
	
	private EventListenerList listenerList;
	
	private ArrayList<Operaciones> operaciones;
	
	private DefaultListModel lstOperacionesModel;
	private JLabel lblOperaciones;
	private JList lstOperaciones;

	public JPOperaciones() {
		super();
		initGUI();
		operaciones = new ArrayList<Operaciones>();
		listenerList = new EventListenerList();
	}
	
	private void initGUI() {
		try {
			AnchorLayout thisLayout = new AnchorLayout();
			this.setLayout(thisLayout);
			this.setSize(130, 310);
			this.setPreferredSize(new java.awt.Dimension(130, 310));
			{
				lblOperaciones = new JLabel();
				this.add(lblOperaciones, new AnchorConstraint(0, 1004, 89, 7, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblOperaciones.setText("Operaciones:");
				lblOperaciones.setPreferredSize(new java.awt.Dimension(107, 15));
			}
			{
				lstOperaciones = new JList();
				this.add(lstOperaciones, new AnchorConstraint(21, 1004, 11, 7, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
				lstOperaciones.setPreferredSize(new java.awt.Dimension(107, 264));
				lstOperaciones.setLayout(null);
				lstOperaciones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				lstOperaciones.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
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
	
	public void ponerOperacion(Operaciones operacion) {
		if(!operaciones.contains(operacion)) {
			operaciones.add(operacion);
			actualizarListaOperaciones();
		}
	}
	
	public void quitarOperacion(Operaciones operacion) {
		if(operaciones.contains(operacion)) {
			operaciones.remove(operacion);
			actualizarListaOperaciones();
		}		
	}
	
	public void setOperacion(Operaciones operacion) {
		if(operaciones.contains(operacion)) {
			lstOperaciones.setSelectedValue(operaciones.indexOf(operacion), true);
		}
	}

	public Operaciones getOperacion() {
		Operaciones operacionSel;
		
		if(lstOperaciones.getSelectedIndex() != -1) {
			operacionSel = operaciones.get(lstOperaciones.getSelectedIndex());
		} else {
			operacionSel = Operaciones.ObtenerCitas; //TODO:Crear operación inválida?
		}
		return operacionSel;
	}
	
	private void actualizarListaOperaciones() {
		lstOperacionesModel = new DefaultListModel();
		for(Operaciones operacion : operaciones) {
			lstOperacionesModel.addElement(nombreOperacion(operacion));
		}
		lstOperaciones.setModel(lstOperacionesModel);
		if(operaciones.size() != 0) {
			lstOperaciones.setSelectedIndex(lstOperaciones.getFirstVisibleIndex());
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

	private String nombreOperacion(Operaciones operacion) {
		String nombre;
		
		switch(operacion) {
		case ConsultarBeneficiario:
			nombre = "Consultar beneficiario";
			break;
		case ConsultarMedico:
			nombre = "Consultar médico";
			break;
		case ConsultarUsuario:
			nombre = "Consultar usuario";
			break;
		case CrearUsuario:
			nombre = "Registrar usuario";
			break;
		case AnularCita:
			nombre = "Anular cita";
			break;
		case EliminarMedico:
			nombre = "Eliminar médico";
			break;
		case EliminarUsuario:
			nombre = "Eliminar usuario";
			break;
		case EmitirVolante:
			nombre = "Emitir volante";
			break;
		case EstablecerSustituto:
			nombre = "Establecer sustituto";
			break;
		case ModificarBeneficiario:
			nombre = "Modificar beneficiario";
			break;
		case ModificarCalendario:
			nombre = "Modificar calendario";
			break;
		case ModificarMedico:
			nombre = "Modificar médico";
			break;
		case ModificarUsuario:
			nombre = "Modificar usuario";
			break;
		case ObtenerCitas:
			nombre = "Recuperar citas";
			break;
		case RegistrarBeneficiario:
			nombre = "Registrar beneficiario";
			break;
		case RegistrarMedico:
			nombre = "Registrar médico";
			break;
		case TramitarCita:
			nombre = "Tramitar cita";
			break;
		default:
			nombre = "Operación desconocida";
			break;
		}
		
		return nombre;
	}
		
	//$hide<<$
	
}
