package presentacion;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.EventObject;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;

import dominio.conocimiento.Administrador;
import dominio.conocimiento.CentroSalud;
import dominio.conocimiento.Citador;
import dominio.conocimiento.Medico;
import dominio.conocimiento.PeriodoTrabajo;
import dominio.conocimiento.Usuario;
import dominio.conocimiento.RolesUsuarios;
import dominio.conocimiento.Validacion;
import dominio.control.ControladorCliente;
import excepciones.ApellidoIncorrectoException;
import excepciones.CadenaVaciaException;
import excepciones.NIFIncorrectoException;
import excepciones.NombreIncorrectoException;
import excepciones.UsuarioInexistenteException;

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
public class JPUsuarioConsultar extends JPBase {

	private JLabel lblNIFUsuario;
	private JLabel lblLogin;
	private JLabel lblPass;
	private JTextField txtNombre;
	private JTextField txtLogin;
	private JTextField txtApellidos;
	private JTextField txtDNI;
	private JButton btnBuscar;
	private JTextField txtPass;
	private JLabel lblApellidos;
	private JLabel lblNombre;
	private JButton btnAplicar;
	private JLabel lblRol;
	private JButton btnCalendario;
	private JLabel lblCalendario;
	private JComboBox cbRoles;
	private JButton btnEliminar;
	private JCheckBox chkEditar;
	private JLabel lblCentro;
	private JTextField txtCentro;
	
	private JFCalendarioLaboral calendario;

	private CentroSalud centro;
	private Vector<PeriodoTrabajo> periodos;
	private Usuario usuario;
	
	private final String TIPO_ADMINISTRADOR = "Administrador";
	private final String TIPO_MEDICO = "Médico";
	private final String TIPO_CITADOR = "Citador";

	public JPUsuarioConsultar(JFrame frame, ControladorCliente controlador) {
		super(frame, controlador);
		initGUI();
	}
	
	private void initGUI() {
		try {
			AnchorLayout thisLayout = new AnchorLayout();
			this.setLayout(thisLayout);
			this.setPreferredSize(new java.awt.Dimension(430, 390));
			{
				btnCalendario = new JButton();
				this.add(btnCalendario, new AnchorConstraint(220, 508, 601, 118, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				btnCalendario.setText("Ver...");
				btnCalendario.setPreferredSize(new java.awt.Dimension(110, 21));
				btnCalendario.setEnabled(false);
				btnCalendario.setToolTipText("Sólo tienen definido un calendario laboral los usuarios de tipo Médico");
				btnCalendario.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnCalendarioActionPerformed(evt);
					}
				});
			}
			{
				lblCalendario = new JLabel();
				this.add(lblCalendario, new AnchorConstraint(223, 307, 591, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblCalendario.setText("Calendario laboral");
				lblCalendario.setPreferredSize(new java.awt.Dimension(111, 14));
			}
			{
				cbRoles = new JComboBox();
				this.add(cbRoles, new AnchorConstraint(156, 77, 462, 118, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				cbRoles.setPreferredSize(new java.awt.Dimension(251, 23));
				cbRoles.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent evt) {
						cbRolesItemStateChanged(evt);
					}
				});
				rellenarModelo(new String [] {""});
			}
			{
				btnEliminar = new JButton();
				this.add(btnEliminar, new AnchorConstraint(273, 161, 673, 9, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				btnEliminar.setText("Eliminar");
				btnEliminar.setPreferredSize(new java.awt.Dimension(120, 26));
				btnEliminar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnEliminarActionPerformed(evt);
					}
				});
			}
			{
				lblCentro = new JLabel();
				this.add(lblCentro, new AnchorConstraint(188, 330, 526, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblCentro.setText("Centro asignado");
				lblCentro.setPreferredSize(new java.awt.Dimension(88, 16));
			}
			{
				lblRol = new JLabel();
				this.add(lblRol, new AnchorConstraint(160, 330, 452, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblRol.setText("Rol asignado");
				lblRol.setPreferredSize(new java.awt.Dimension(88, 16));
			}
			{
				txtCentro = new JTextField();
				this.add(txtCentro, new AnchorConstraint(185, 77, 462, 118, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtCentro.setPreferredSize(new java.awt.Dimension(251, 23));
			}
			{
				txtLogin = new JTextField();
				this.add(txtLogin, new AnchorConstraint(39, 77, 162, 118, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtLogin.setPreferredSize(new java.awt.Dimension(251, 23));
			}
			{
				txtApellidos = new JTextField();
				this.add(txtApellidos, new AnchorConstraint(127, 77, 388, 118, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtApellidos.setPreferredSize(new java.awt.Dimension(251, 23));
			}
			{
				txtNombre = new JTextField();
				this.add(txtNombre, new AnchorConstraint(98, 77, 314, 118, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtNombre.setPreferredSize(new java.awt.Dimension(251, 23));
			}
			{
				txtDNI = new JTextField();
				this.add(txtDNI, new AnchorConstraint(11, 77, 91, 118, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtDNI.setPreferredSize(new java.awt.Dimension(251, 23));
			}
			{
				btnBuscar = new JButton();
				this.add(btnBuscar, new AnchorConstraint(11, 5, 91, 829, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
				btnBuscar.setText("Buscar");
				btnBuscar.setPreferredSize(new java.awt.Dimension(66, 23));
				btnBuscar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnBuscarActionPerformed(evt);
					}
				});
			}
			{
				txtPass = new JTextField();
				this.add(txtPass, new AnchorConstraint(67, 77, 226, 118, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtPass.setPreferredSize(new java.awt.Dimension(251, 23));				
			}
			{
				lblApellidos = new JLabel();
				this.add(lblApellidos, new AnchorConstraint(131, 361, 360, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblApellidos.setText("Apellidos");
				lblApellidos.setPreferredSize(new java.awt.Dimension(57, 16));
			}
			{
				lblNombre = new JLabel();
				this.add(lblNombre, new AnchorConstraint(102, 369, 301, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblNombre.setText("Nombre");
				lblNombre.setPreferredSize(new java.awt.Dimension(49, 16));
			}
			{
				lblPass = new JLabel();
				this.add(lblPass, new AnchorConstraint(71, 348, 229, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblPass.setText("Contraseña");
				lblPass.setPreferredSize(new java.awt.Dimension(70, 16));
			}
			{
				lblLogin = new JLabel();
				this.add(lblLogin, new AnchorConstraint(43, 367, 144, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblLogin.setText("Usuario");
				lblLogin.setPreferredSize(new java.awt.Dimension(51, 16));
			}
			{
				lblNIFUsuario = new JLabel();
				this.add(lblNIFUsuario, new AnchorConstraint(15, 368, 73, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblNIFUsuario.setText("NIF");
				lblNIFUsuario.setPreferredSize(new java.awt.Dimension(50, 16));
			}
			{
				btnAplicar = new JButton();
				btnAplicar.setText("Guardar Cambios");
				btnAplicar.setPreferredSize(new java.awt.Dimension(120, 26));
				this.add(btnAplicar, new AnchorConstraint(271, 5, 534, 670, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
				btnAplicar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnAplicarActionPerformed(evt);
					}
				});
			}
			{
				chkEditar = new JCheckBox();
				this.add(chkEditar, new AnchorConstraint(274, 136, 665, 419, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
				chkEditar.setText("Habilitar edición");
				chkEditar.setEnabled(false);
				chkEditar.setPreferredSize(new java.awt.Dimension(106, 19));
				chkEditar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						chkEditarActionPerformed(evt);
					}
				});
			}
			
			configurarFormularioConsultar(false);
			txtCentro.setEditable(false);
			txtCentro.setFocusable(false);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void rellenarModelo(String [] informacion) {
		ComboBoxModel cbRolesModel = new DefaultComboBoxModel(informacion);
		cbRoles.setModel(cbRolesModel);	
	}
	
	private void btnBuscarActionPerformed(ActionEvent evt) {
		usuario = null;
					
		try {
			// Buscamos el usuario solicitado
			Validacion.comprobarNIF(txtDNI.getText());
			usuario = (Usuario) getControlador().consultarUsuario(txtDNI.getText());

			// Mostramos los datos del usuario encontrado
			Dialogos.mostrarDialogoInformacion(getFrame(), "Resultados de la búsqueda", "Usuario encontrado.");
			rellenarModelo(new String [] {TIPO_ADMINISTRADOR, TIPO_CITADOR, TIPO_MEDICO});
			txtLogin.setText(usuario.getLogin());
			txtPass.setText(usuario.getPassword());
			txtNombre.setText(usuario.getNombre());
			txtApellidos.setText(usuario.getApellidos());
			cbRoles.setSelectedIndex(usuario.getRol().ordinal());
			centro = usuario.getCentroSalud();
			txtCentro.setText(centro.getNombre());
			if (usuario.getRol().equals(RolesUsuarios.Medico))
				periodos = ((Medico)usuario).getCalendario();
			chkEditar.setEnabled(true);
			
		} catch(SQLException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.toString());
		} catch(RemoteException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.toString());
		} catch(UsuarioInexistenteException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", "El usuario no se encuentra dado de alta en el sistema.");
			limpiarDatos();
			txtDNI.selectAll();
			txtDNI.grabFocus();			
		} catch(CadenaVaciaException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", "Debe introducir un DNI.");
			txtDNI.grabFocus();	
		} catch(NIFIncorrectoException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", "Debe introducir DNI válido.");
			txtDNI.selectAll();
			txtDNI.grabFocus();	
			
		} catch(Exception e) {
			e.printStackTrace();
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.toString());
		}
	}

	private void limpiarDatos() {
		txtDNI.setText("");
		txtLogin.setText("");
		txtPass.setText("");
		txtNombre.setText("");
		txtApellidos.setText("");
		txtCentro.setText("");
		chkEditar.setSelected(false);
		chkEditar.setEnabled(false);
		rellenarModelo(new String [] {""});
		configurarFormularioConsultar(false);
	}
	
	private void configurarFormularioConsultar(boolean estado) {
		txtLogin.setEditable(estado);
		txtLogin.setFocusable(estado);
		txtPass.setEditable(estado);
		txtPass.setFocusable(estado);
		txtNombre.setEditable(estado);
		txtNombre.setFocusable(estado);
		txtApellidos.setEditable(estado);
		txtApellidos.setFocusable(estado);
		btnAplicar.setEnabled(estado);
		btnEliminar.setEnabled(estado);
		cbRoles.setFocusable(estado);
		cbRoles.setEnabled(estado);	
		if (estado)
			btnCalendario.setText("Configurar...");
		else
			btnCalendario.setText("Ver...");
	}
	
	private void btnAplicarActionPerformed(ActionEvent evt) {
		try {			
			// Comprobamos los campos que pueden dar fallo
			Validacion.comprobarNombre(txtNombre.getText());
			Validacion.comprobarApellidos(txtApellidos.getText());
			
			switch(RolesUsuarios.values()[cbRoles.getSelectedIndex()]) {
				case Administrador:
					usuario = new Administrador();
					break;
				case Citador:
					usuario = new Citador();
					break;
				case Medico:
					usuario = new Medico();
					break;
			}
			usuario.setDni(txtDNI.getText());
			usuario.setLogin(txtLogin.getText());
			usuario.setPassword(txtPass.getText());
			usuario.setNombre(txtNombre.getText());
			usuario.setApellidos(txtApellidos.getText());
			// Dejamos el mismo centro de salud
			usuario.setCentroSalud(centro);
			
			// Modificamos el usuario
			getControlador().modificarUsuario(usuario);
			Dialogos.mostrarDialogoInformacion(getFrame(), "Operación correcta", "El usuario ha sido modificado correctamente.");
			limpiarDatos();
			configurarFormularioConsultar(false);
			
		} catch(NombreIncorrectoException e) {
			txtNombre.selectAll();
			Dialogos.mostrarDialogoError(getFrame(), "Error", "El nombre del usuario sólo puede contener letras y espacios.");
			txtNombre.grabFocus();
		} catch(ApellidoIncorrectoException e) {
			txtApellidos.selectAll();
			Dialogos.mostrarDialogoError(getFrame(), "Error", "Los apellidos del usuario sólo pueden contener letras y espacios.");
			txtApellidos.grabFocus();
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.toString());
		} catch(SQLException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.toString());
		} catch(RemoteException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.toString());		
		}catch(Exception e) {
			e.printStackTrace();
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.toString());
		}
	}
	
	private void btnEliminarActionPerformed(ActionEvent evt) {
		boolean respuesta = Dialogos.mostrarDialogoPregunta(getFrame(), "Pregunta", "¿Seguro que desea eliminar este usuario del sistema?");
		if (respuesta) {
			try {
				getControlador().eliminarUsuario(usuario);
				Dialogos.mostrarDialogoInformacion(getFrame(), "Operación correcta", "El usuario ha sido eliminado correctamente.");
				limpiarDatos();
				configurarFormularioConsultar(false);
			} catch(SQLException e) {
				Dialogos.mostrarDialogoError(getFrame(), "Error", e.toString());
			} catch(RemoteException e) {
				Dialogos.mostrarDialogoError(getFrame(), "Error", e.toString());		
			}catch(Exception e) {
				e.printStackTrace();
				Dialogos.mostrarDialogoError(getFrame(), "Error", e.toString());
			}
		}
	}
	
	private void chkEditarActionPerformed(ActionEvent evt) {
		if (chkEditar.isSelected())
			configurarFormularioConsultar(true);
		else
			configurarFormularioConsultar(false);
	}
	public void setPeriodos (Vector<PeriodoTrabajo> p) {
		periodos = new Vector<PeriodoTrabajo>();
		periodos.addAll(p);
	}
	
	private void btnCalendarioActionPerformed(ActionEvent evt) {
		calendario = new JFCalendarioLaboral(this, periodos);
		calendario.addVentanaCerradaListener(new VentanaCerradaListener() {
			public void ventanaCerrada(EventObject evt) {    
				calendarioVentanaCerrada(evt);
			}
		});
		
		setEnabled(false);
		calendario.setModificable(chkEditar.isSelected());
		calendario.setLocationRelativeTo(this);		
		calendario.setVisible(true);
	}
	
	private void calendarioVentanaCerrada(EventObject evt) {
		// Reactivamos la ventana 
		setEnabled(true);
	}
	
	private void cbRolesItemStateChanged(ItemEvent evt) {
		if (evt.getStateChange() == ItemEvent.SELECTED) {
			if (evt.getItem().equals(TIPO_MEDICO)) {
				btnCalendario.setEnabled(true);
			}
			else {
				btnCalendario.setEnabled(false);
			}
		}
	}
	
	private void chkEditarStateChanged(ChangeEvent evt) {
		System.out.println("chkEditar.stateChanged, event="+evt);
		//TODO add your code for chkEditar.stateChanged
	}

}
