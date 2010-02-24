package presentacion;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.EventObject;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.EventListenerList;

import presentacion.auxiliar.Dialogos;
import presentacion.auxiliar.UsuarioBuscadoListener;
import presentacion.auxiliar.Validacion;
import presentacion.auxiliar.VentanaCerradaListener;

import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;
import dominio.conocimiento.Administrador;
import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.CategoriasMedico;
import dominio.conocimiento.Cita;
import dominio.conocimiento.Citador;
import dominio.conocimiento.Especialista;
import dominio.conocimiento.Medico;
import dominio.conocimiento.PeriodoTrabajo;
import dominio.conocimiento.Usuario;
import dominio.conocimiento.RolesUsuario;
import dominio.control.ControladorCliente;
import excepciones.ApellidoIncorrectoException;
import excepciones.CadenaVaciaException;
import excepciones.Contrase�aIncorrectaException;
import excepciones.CorreoElectronicoIncorrectoException;
import excepciones.LoginIncorrectoException;
import excepciones.NIFIncorrectoException;
import excepciones.NombreIncorrectoException;
import excepciones.TelefonoFijoIncorrectoException;
import excepciones.TelefonoMovilIncorrectoException;
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
/**
 * Panel que permite consultar, modificar y eliminar usuarios existentes. 
 */
public class JPUsuarioConsultar extends JPBase {

	private static final long serialVersionUID = 2736737327573021315L;
	
	private EventListenerList listenerList;
	private Vector<PeriodoTrabajo> periodos;
	private Usuario usuario;
	private boolean passwordCambiada;
	
	private JLabel lblNIFBuscado;
	private JLabel lblTelefonoMovil;
	private JLabel lblTelefonoFijo;
	private JLabel lblCorreoElectronico;
	private JTextField txtTelefonoMovil;
	private JTextField txtTelefonoFijo;
	private JTextField txtCorreoElectronico;
	private JPasswordField txtPassword;
	private JLabel lblPassword;
	private JLabel lblEspecialidad;
	private JTextField txtEspecialidad;
	private JLabel lblHorasSemanales;
	private JLabel lblNIF;
	private JLabel lblLogin;
	private JLabel lblPasswordConf;
	private JTextField txtNombre;
	private JTextField txtLogin;
	private JTextField txtApellidos;
	private JTextField txtNIFBuscado;
	private JButton btnBuscar;
	private JPasswordField txtPasswordConf;
	private JLabel lblApellidos;
	private JLabel lblNombre;
	private JButton btnGuardar;
	private JLabel lblRol;
	private JTextField txtNIF;
	private JLabel lblCamposOblig;
	private JLabel lblBuscar;
	private JButton btnCalendario;
	private JLabel lblCalendario;
	private JComboBox cmbRol;
	private JButton btnEliminar;
	private JCheckBox chkEditar;
	private JLabel lblCentro;
	private JTextField txtCentro;

	private JFCalendarioLaboral frmCalendario;

	public JPUsuarioConsultar() {
		this(null, null);
		// Este constructor evita que aparezca un error al editar
		// los formularios o paneles que utilizan JPUsuarioConsultar
	}
	
	public JPUsuarioConsultar(JFrame frame, ControladorCliente controlador) {
		super(frame, controlador);
		initGUI();
		listenerList = new EventListenerList();
		usuario = null;
		cambiarEdicion(false);
	}
	
	private void initGUI() {
		try {
			AnchorLayout thisLayout = new AnchorLayout();
			this.setLayout(thisLayout);
			this.setSize(430, 459);
			this.setPreferredSize(new java.awt.Dimension(430, 499));
			{
				lblHorasSemanales = new JLabel();
				this.add(lblHorasSemanales, new AnchorConstraint(382, 770, 742, 259, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblHorasSemanales.setText("0 horas semanales");
				lblHorasSemanales.setPreferredSize(new java.awt.Dimension(97, 16));
				lblHorasSemanales.setName("lblHorasSemanales");
			}
			{
				btnCalendario = new JButton();
				this.add(btnCalendario, new AnchorConstraint(379, 508, 601, 141, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				btnCalendario.setText("Ver...");
				btnCalendario.setPreferredSize(new java.awt.Dimension(110, 23));
				btnCalendario.setToolTipText("S�lo tienen definido un calendario laboral los usuarios de tipo M�dico");
				btnCalendario.setEnabled(false);
				btnCalendario.setName("btnCalendario");
				btnCalendario.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnCalendarioActionPerformed(evt);
					}
				});
			}
			{
				lblCalendario = new JLabel();
				this.add(lblCalendario, new AnchorConstraint(383, 309, 591, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblCalendario.setText("Calendario laboral");
				lblCalendario.setPreferredSize(new java.awt.Dimension(111, 14));
			}
			{
				cmbRol = new JComboBox();
				this.add(cmbRol, new AnchorConstraint(322, 12, 462, 141, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				cmbRol.setPreferredSize(new java.awt.Dimension(277, 23));
				cmbRol.setEnabled(false);
				cmbRol.setName("cmbRol");
				cmbRol.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent evt) {
						cmbRolesItemStateChanged(evt);
					}
				});
			}
			{
				btnEliminar = new JButton();
				this.add(btnEliminar, new AnchorConstraint(461, 161, 673, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				btnEliminar.setText("Eliminar usuario");
				btnEliminar.setPreferredSize(new java.awt.Dimension(120, 26));
				btnEliminar.setName("btnEliminar");
				btnEliminar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnEliminarActionPerformed(evt);
					}
				});
			}
			{
				lblCentro = new JLabel();
				this.add(lblCentro, new AnchorConstraint(353, 321, 526, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblCentro.setText("Centro asignado");
				lblCentro.setPreferredSize(new java.awt.Dimension(99, 16));
			}
			{
				lblRol = new JLabel();
				this.add(lblRol, new AnchorConstraint(325, 301, 452, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblRol.setText("Tipo de usuario *");
				lblRol.setPreferredSize(new java.awt.Dimension(119, 16));
			}
			{
				txtCentro = new JTextField();
				this.add(txtCentro, new AnchorConstraint(350, 12, 462, 141, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtCentro.setPreferredSize(new java.awt.Dimension(277, 23));
				txtCentro.setEditable(false);
				txtCentro.setName("txtCentro");
			}
			{
				txtLogin = new JTextField();
				this.add(txtLogin, new AnchorConstraint(157, 12, 314, 141, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtLogin.setPreferredSize(new java.awt.Dimension(277, 23));
				txtLogin.setEditable(false);
				txtLogin.setName("txtLogin");
			}
			{
				txtApellidos = new JTextField();
				this.add(txtApellidos, new AnchorConstraint(129, 12, 226, 141, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtApellidos.setPreferredSize(new java.awt.Dimension(277, 23));				
				txtApellidos.setEditable(false);
				txtApellidos.setName("txtApellidos");
			}
			{
				txtNombre = new JTextField();
				this.add(txtNombre, new AnchorConstraint(101, 12, 162, 141, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtNombre.setPreferredSize(new java.awt.Dimension(277, 23));
				txtNombre.setEditable(false);
				txtNombre.setName("txtNombre");
			}
			{
				txtNIFBuscado = new JTextField();
				this.add(txtNIFBuscado, new AnchorConstraint(36, 83, 91, 141, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtNIFBuscado.setPreferredSize(new java.awt.Dimension(206, 23));
				txtNIFBuscado.setName("txtNifBuscado");
			}
			{
				btnBuscar = new JButton();
				this.add(btnBuscar, new AnchorConstraint(36, 11, 91, 829, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
				btnBuscar.setText("Buscar");
				btnBuscar.setPreferredSize(new java.awt.Dimension(66, 23));
				btnBuscar.setName("btnBuscar");
				btnBuscar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnBuscarActionPerformed(evt);
					}
				});
			}
			{
				txtPasswordConf = new JPasswordField();
				this.add(txtPasswordConf, new AnchorConstraint(213, 12, 388, 141, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtPasswordConf.setPreferredSize(new java.awt.Dimension(277, 23));
				txtPasswordConf.setEditable(false);
				txtPasswordConf.setName("txtPasswordConf");
			}
			{
				lblApellidos = new JLabel();
				this.add(lblApellidos, new AnchorConstraint(132, 198, 229, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblApellidos.setText("Apellidos *");
				lblApellidos.setPreferredSize(new java.awt.Dimension(99, 16));
			}
			{
				lblNombre = new JLabel();
				this.add(lblNombre, new AnchorConstraint(104, 156, 144, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblNombre.setText("Nombre *");
				lblNombre.setPreferredSize(new java.awt.Dimension(99, 16));
			}
			{
				lblPasswordConf = new JLabel();
				this.add(lblPasswordConf, new AnchorConstraint(216, 156, 360, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblPasswordConf.setText("Confirmar contrase�a *");
				lblPasswordConf.setPreferredSize(new java.awt.Dimension(125, 16));
			}
			{
				lblLogin = new JLabel();
				this.add(lblLogin, new AnchorConstraint(160, 156, 301, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblLogin.setText("Usuario *");
				lblLogin.setPreferredSize(new java.awt.Dimension(99, 16));
			}
			{
				lblNIFBuscado = new JLabel();
				this.add(lblNIFBuscado, new AnchorConstraint(39, 140, 73, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblNIFBuscado.setText("NIF");
				lblNIFBuscado.setPreferredSize(new java.awt.Dimension(99, 16));
			}
			{
				btnGuardar = new JButton();
				btnGuardar.setText("Guardar cambios");
				btnGuardar.setPreferredSize(new java.awt.Dimension(120, 26));
				this.add(btnGuardar, new AnchorConstraint(459, 11, 534, 670, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
				btnGuardar.setName("btnGuardar");
				btnGuardar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnGuardarActionPerformed(evt);
					}
				});
			}
			{
				chkEditar = new JCheckBox();
				this.add(chkEditar, new AnchorConstraint(462, 141, 665, 419, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
				chkEditar.setText("Habilitar edici�n");
				chkEditar.setEnabled(false);
				chkEditar.setPreferredSize(new java.awt.Dimension(106, 14));
				chkEditar.setName("chkEditar");
				chkEditar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						chkEditarActionPerformed(evt);
					}
				});
			}
			{
				lblBuscar = new JLabel();
				this.add(lblBuscar, new AnchorConstraint(14, 387, 73, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblBuscar.setText("Buscar usuario:");
				lblBuscar.setPreferredSize(new java.awt.Dimension(156, 14));
			}
			{
				lblCamposOblig = new JLabel();
				this.add(lblCamposOblig, new AnchorConstraint(435, 12, 875, 673, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
				lblCamposOblig.setText("* Campos obligatorios");
				lblCamposOblig.setHorizontalAlignment(SwingConstants.TRAILING);
				lblCamposOblig.setPreferredSize(new java.awt.Dimension(129, 17));
				lblCamposOblig.setFont(lblCamposOblig.getFont().deriveFont(10.0f));
			}
			{
				txtNIF = new JTextField();
				this.add(txtNIF, new AnchorConstraint(73, 12, 219, 141, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtNIF.setEditable(false);
				txtNIF.setPreferredSize(new java.awt.Dimension(277, 23));
				txtNIF.setName("txtNif");
			}
			{
				lblNIF = new JLabel();
				this.add(lblNIF, new AnchorConstraint(76, 254, 237, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblNIF.setText("NIF *");
				lblNIF.setPreferredSize(new java.awt.Dimension(99, 16));
			}
			{
				txtEspecialidad = new JTextField();
				this.add(txtEspecialidad, new AnchorConstraint(407, 12, 760, 139, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtEspecialidad.setEditable(false);
				txtEspecialidad.setPreferredSize(new java.awt.Dimension(279, 23));
				txtEspecialidad.setName("txtEspecialidad");
			}
			{
				lblEspecialidad = new JLabel();
				this.add(lblEspecialidad, new AnchorConstraint(412, 282, 811, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblEspecialidad.setText("Especialidad *");
				lblEspecialidad.setPreferredSize(new java.awt.Dimension(111, 13));
			}
			{
				txtPassword = new JPasswordField();
				this.add(txtPassword, new AnchorConstraint(185, 12, 454, 141, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtPassword.setEditable(false);
				txtPassword.setPreferredSize(new java.awt.Dimension(277, 23));
				txtPassword.setName("txtPassword");
				txtPassword.addFocusListener(new FocusAdapter() {
					public void focusGained(FocusEvent evt) {
						txtPasswordFocusGained(evt);
					}
				});
			}
			{
				lblPassword = new JLabel();
				this.add(lblPassword, new AnchorConstraint(188, 254, 445, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblPassword.setText("Contrase�a *");
				lblPassword.setPreferredSize(new java.awt.Dimension(99, 16));
			}
			{
				txtTelefonoMovil = new JTextField();
				this.add(txtTelefonoMovil, new AnchorConstraint(295, 12, 874, 141, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtTelefonoMovil.setPreferredSize(new java.awt.Dimension(277, 23));
				txtTelefonoMovil.setEditable(false);
				txtTelefonoMovil.setName("txtTelefonoMovil");
			}
			{
				txtTelefonoFijo = new JTextField();
				this.add(txtTelefonoFijo, new AnchorConstraint(268, 12, 788, 141, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtTelefonoFijo.setPreferredSize(new java.awt.Dimension(277, 23));
				txtTelefonoFijo.setEditable(false);
				txtTelefonoFijo.setName("txtTelefonoFijo");
			}
			{
				txtCorreoElectronico = new JTextField();
				this.add(txtCorreoElectronico, new AnchorConstraint(240, 12, 700, 141, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtCorreoElectronico.setPreferredSize(new java.awt.Dimension(277, 23));
				txtCorreoElectronico.setEditable(false);
				txtCorreoElectronico.setName("txtCorreoElectronico");
			}
			{
				lblTelefonoMovil = new JLabel();
				this.add(lblTelefonoMovil, new AnchorConstraint(299, 277, 861, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblTelefonoMovil.setText("Tel�fono m�vil");
				lblTelefonoMovil.setPreferredSize(new java.awt.Dimension(110, 15));
			}
			{
				lblTelefonoFijo = new JLabel();
				this.add(lblTelefonoFijo, new AnchorConstraint(272, 277, 776, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblTelefonoFijo.setText("Tel�fono fijo");
				lblTelefonoFijo.setPreferredSize(new java.awt.Dimension(110, 15));
			}
			{
				lblCorreoElectronico = new JLabel();
				this.add(lblCorreoElectronico, new AnchorConstraint(244, 273, 690, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblCorreoElectronico.setText("Correo electr�nico");
				lblCorreoElectronico.setPreferredSize(new java.awt.Dimension(108, 15));
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//$hide>>$
	
	private void btnBuscarActionPerformed(ActionEvent evt) {
		Object[] listeners;
		int i;

		// Borramos la informaci�n del antiguo usuario consultado
		limpiarCamposConsulta();
					
		try {
			
			// Buscamos el usuario solicitado
			Validacion.comprobarNIF(txtNIFBuscado.getText().trim().toUpperCase());
			usuario = getControlador().consultarUsuario(txtNIFBuscado.getText().trim().toUpperCase());

			// Mostramos los datos del usuario encontrado
			Dialogos.mostrarDialogoInformacion(getFrame(), "Resultados de la b�squeda", "Usuario encontrado.");
			
			mostrarDatosUsuario(usuario);
			
		} catch(UsuarioInexistenteException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getMessage());
			txtNIFBuscado.selectAll();
			txtNIFBuscado.grabFocus();			

		} catch(CadenaVaciaException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", "Debe introducir un DNI.");
			txtNIFBuscado.grabFocus();	
		} catch(NIFIncorrectoException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getMessage());
			txtNIFBuscado.selectAll();
			txtNIFBuscado.grabFocus();	
			
		} catch(SQLException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getLocalizedMessage());
		} catch(RemoteException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getLocalizedMessage());
		} catch(Exception e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getLocalizedMessage());
		}
		
		// Notificamos que ha cambiado el usuario seleccionado
		listeners = listenerList.getListenerList();
		for(i = 0; i < listeners.length; i += 2) {
			if(listeners[i] == UsuarioBuscadoListener.class) {
				((UsuarioBuscadoListener)listeners[i + 1]).usuarioBuscado(new EventObject(this));
			}
		}
	}
	
	private void mostrarDatosUsuario(Usuario usuario) {
		int horas;
		
		this.usuario = usuario;
		
		txtNIFBuscado.setText("");
		txtNIF.setText(usuario.getDni());
		txtLogin.setText(usuario.getLogin());
		txtPassword.setText("*****");
		txtPasswordConf.setText("");
		txtNombre.setText(usuario.getNombre());
		txtApellidos.setText(usuario.getApellidos());
		txtCorreoElectronico.setText(usuario.getCorreo());
		txtTelefonoFijo.setText(usuario.getTelefono());
		txtTelefonoMovil.setText(usuario.getMovil());
		txtCentro.setText(usuario.getCentroSalud().getNombre() + " (" + usuario.getCentroSalud().getDireccion() + ")");
		if(usuario.getRol().equals(RolesUsuario.Medico)) {
			rellenarTiposUsuario(new String[] { RolesUsuario.Medico.name() + " (" + ((Medico)usuario).getTipoMedico().getCategoria().name() + ")" });
			lblCalendario.setVisible(true);
			btnCalendario.setVisible(true);
			btnCalendario.setEnabled(true);
			lblHorasSemanales.setVisible(true);
			periodos = ((Medico)usuario).getCalendario();
			horas = 0;
			for(PeriodoTrabajo periodo : periodos) {
				horas += periodo.getNumeroHoras();
			}
			if(horas == 1) {
				lblHorasSemanales.setText("1 hora semanal");
			} else {
				lblHorasSemanales.setText(horas + " horas semanales");
			}
			if(((Medico)usuario).getTipoMedico().getCategoria() == CategoriasMedico.Especialista) {
				lblEspecialidad.setVisible(true);
				txtEspecialidad.setVisible(true);
				txtEspecialidad.setText(((Especialista)((Medico)usuario).getTipoMedico()).getEspecialidad());
			} else {
				lblEspecialidad.setVisible(false);
				txtEspecialidad.setVisible(false);
			}
		} else {
			rellenarTiposUsuarioNoMedico();
			lblCalendario.setVisible(false);
			btnCalendario.setVisible(false);
			lblHorasSemanales.setVisible(false);
			lblEspecialidad.setVisible(false);
			txtEspecialidad.setVisible(false);
		}
		cmbRol.setSelectedItem(usuario.getRol().name());
		chkEditar.setEnabled(true);
	}
	
	private void btnGuardarActionPerformed(ActionEvent evt) {
		Usuario usuarioModif = null;
		Vector<Cita> citas = null;
		Vector<Cita> citasAfectadas = new Vector<Cita>();
		boolean actualizar = false;
		JFAvisos frmAviso;
		
		try {
			
			// Comprobamos todos los campos
			Validacion.comprobarNombre(txtNombre.getText().trim());
			Validacion.comprobarApellidos(txtApellidos.getText().trim());
			Validacion.comprobarUsuario(txtLogin.getText().trim());
			if(passwordCambiada && (txtPassword.getPassword().length > 0 || txtPasswordConf.getPassword().length > 0)) { 
				Validacion.comprobarContrase�a(new String(txtPasswordConf.getPassword()));
				if(!(new String(txtPassword.getPassword())).equals(new String(txtPasswordConf.getPassword()))) {
					throw new Contrase�aIncorrectaException("Las contrase�as no coinciden.");
				}
			}
			if(!campoVacio(txtCorreoElectronico)) {
				Validacion.comprobarCorreoElectronico(txtCorreoElectronico.getText().trim());
			}
			if(!campoVacio(txtTelefonoFijo)) {
				Validacion.comprobarTelefonoFijo(txtTelefonoFijo.getText().trim());
			}
			if(!campoVacio(txtTelefonoMovil)) {
				Validacion.comprobarTelefonoMovil(txtTelefonoMovil.getText().trim());
			}
			
			// Creamos un nuevo usuario con los datos introducidos
			// (si el usuario era m�dico no puede cambiar de rol)
			if(usuario.getRol() == RolesUsuario.Medico) {
				usuarioModif = new Medico();
			} else {
				if(cmbRol.getSelectedItem().toString().equals(RolesUsuario.Administrador.name())) {
					usuarioModif = new Administrador();
				} else if(cmbRol.getSelectedItem().toString().equals(RolesUsuario.Citador.name())) {
					usuarioModif = new Citador();
				}
			}
			usuarioModif.setDni(txtNIF.getText().trim().toUpperCase());
			usuarioModif.setLogin(txtLogin.getText().trim());
			if(passwordCambiada && txtPassword.getPassword().length > 0) { 
				usuarioModif.setPassword(new String(txtPassword.getPassword()));
			} else {
				usuarioModif.setPassword("");
			}
			usuarioModif.setNombre(txtNombre.getText().trim());
			usuarioModif.setApellidos(txtApellidos.getText().trim());
			usuarioModif.setCorreo(txtCorreoElectronico.getText().trim());
			usuarioModif.setTelefono(txtTelefonoFijo.getText().trim());
			usuarioModif.setMovil(txtTelefonoMovil.getText().trim());
			
			// Dejamos el mismo centro de salud
			usuarioModif.setCentroSalud(usuario.getCentroSalud());
			
			// Si el usuario es un m�dico y ha cambiado su horario de trabajo, 
			// se comprueba que �sto no afecte a las citas que ya ten�a asignadas.
			// Si afecta, se pide confirmaci�n para anular las citas afectadas
			if(usuarioModif.getRol().equals(RolesUsuario.Medico)) {
				((Medico)usuarioModif).setCalendario(periodos);
				((Medico)usuarioModif).setTipoMedico(((Medico)usuario).getTipoMedico());
				citas = getControlador().consultarCitasMedico(usuario.getDni());
				if (citas.size()>0) {
					// Si se ha limpiado todo el calendario, todas las citas est�n afectadas
					if (((Medico)usuarioModif).getCalendario().size()==0) {
						citasAfectadas.addAll(citas);
					}
					else {
						for (int i=0; i<citas.size(); i++) {
							for (PeriodoTrabajo p: ((Medico)usuarioModif).getCalendario()) {
								// Si la cita que ya tenia asignado, no est� en ningun periodo, se inserta en la lista de citas afectadas
								if (!citas.get(i).citaEnHoras(p.getHoraInicio(), p.getHoraFinal()))
									citasAfectadas.add(citas.get(i));
							}
						}
						if (citasAfectadas.size()>0)
							actualizar = Dialogos.mostrarDialogoPregunta(getFrame(), "Aviso", "Si se modifican los per�odos de trabajo del m�dico, algunas citas se ver�n afectadas y ser�n eliminadas.\n�Desea continuar?");
						else
							actualizar = true;
					}
				}
				else
					actualizar = true;
			}
			
			if (actualizar || !(usuarioModif instanceof Medico)) {
				// Solicitamos al servidor que se modifique el usuario
				getControlador().modificarUsuario(usuarioModif);
				// Mostramos el resultado de la operaci�n y limpiamos el panel
				Dialogos.mostrarDialogoInformacion(getFrame(), "Operaci�n correcta", "El usuario ha sido modificado correctamente.");
				restablecerPanel();
				// Anulamos las citas
				if (citasAfectadas.size()>0 && actualizar) {
					// Se eliminan las citas afectadas
					for (Cita c : citasAfectadas) {
						getControlador().anularCita(c);
					}
					// Se avisa de las citas eliminadas
					if (citasAfectadas.size()>0) {
						frmAviso = new JFAvisos();
						frmAviso.mostrarCitas("Las siguientes citas han sido eliminadas:", citasAfectadas);
					}
				}
				
			}
			
		} catch(UsuarioInexistenteException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getMessage());	

		} catch(NombreIncorrectoException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getMessage());
			txtNombre.selectAll();
			txtNombre.grabFocus();
		} catch(ApellidoIncorrectoException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getMessage());
			txtApellidos.selectAll();
			txtApellidos.grabFocus();
		} catch(LoginIncorrectoException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getMessage());
			txtLogin.selectAll();
			txtLogin.grabFocus();
		} catch(Contrase�aIncorrectaException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getMessage());
			txtPassword.selectAll();
			txtPassword.grabFocus();
		} catch(CorreoElectronicoIncorrectoException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getMessage());
			txtCorreoElectronico.selectAll();
			txtCorreoElectronico.grabFocus();
		} catch(TelefonoFijoIncorrectoException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getMessage());
			txtTelefonoFijo.selectAll();
			txtTelefonoFijo.grabFocus();
		} catch(TelefonoMovilIncorrectoException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getMessage());
			txtTelefonoMovil.selectAll();
			txtTelefonoMovil.grabFocus();
		
		} catch(SQLException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getLocalizedMessage());
		} catch(RemoteException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getLocalizedMessage());		
		} catch(Exception e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getLocalizedMessage());
		}
	}
	
	private void btnEliminarActionPerformed(ActionEvent evt) {
		JFAvisos frmAviso;
		Vector<Beneficiario> beneficiarios, beneficiariosCamb;
		Vector<Cita> citas;
		String mensaje;
		boolean respuesta;
		
		// Solicitamos confirmaci�n para eliminar el usuario
		respuesta = Dialogos.mostrarDialogoPregunta(getFrame(), "Pregunta", "�Seguro que desea eliminar este usuario?");
		
		if(respuesta) {
			try {
				
				if(usuario.getRol().equals(RolesUsuario.Medico)) {
					
					// Si el usuario a borrar es un m�dico, vemos si tiene
					// beneficiarios asignados o citas pendientes
					beneficiarios = getControlador().obtenerBeneficiariosMedico(usuario.getDni());
					citas = getControlador().consultarCitasMedico(usuario.getDni());
					mensaje = "";
					if(beneficiarios.size() > 0 && citas.size() > 0) {
						mensaje = "El m�dico que va a borrar tiene beneficiarios asignados y citas pendientes.\n";
					} else if(beneficiarios.size() > 0) {
						mensaje = "El m�dico que va a borrar tiene beneficiarios asignados.\n";
					} else if(citas.size() > 0) {
						mensaje = "El m�dico que va a borrar tiene citas pendientes.\n";
					}
					if(beneficiarios.size() > 0 || citas.size() > 0) {
						mensaje += "�Quiere continuar con la eliminaci�n?";
						respuesta = Dialogos.mostrarDialogoPregunta(getFrame(), "Pregunta", mensaje);
					} else {
						respuesta = true;
					}
					
					if(respuesta) {
						
						// Solicitamos al servidor que se elimine el m�dico
						getControlador().eliminarUsuario((Medico)usuario);
						
						// Mostramos el resultado de la operaci�n y limpiamos el panel
						Dialogos.mostrarDialogoInformacion(getFrame(), "Operaci�n correcta", "El usuario ha sido eliminado correctamente.");
						restablecerPanel();
						
						// Volvemos a leer los beneficiarios para obtener sus
						// nuevos m�dicos y se los mostramos al administrador
						// para que les pueda avisar del cambio
						if(beneficiarios.size() > 0) {
							frmAviso = new JFAvisos();
							beneficiariosCamb = new Vector<Beneficiario>();
							for(Beneficiario beneficiario : beneficiarios) {
								beneficiariosCamb.add(getControlador().consultarBeneficiarioPorNIF(beneficiario.getNif()));
							}
							frmAviso.mostrarBeneficiarios("Los siguientes beneficiarios han cambiado de m�dico:", beneficiariosCamb);
						}
						
						// Mostramos las citas que se han anulado al eliminar el m�dico
						if(citas.size() > 0) {
							frmAviso = new JFAvisos();
							frmAviso.mostrarCitas("Las siguientes citas se han anulado:", citas);
						}
						
					}
					
				} else {
					
					// Solicitamos al servidor que se elimine el usuario
					getControlador().eliminarUsuario(usuario);
					
					// Mostramos el resultado de la operaci�n y limpiamos el panel
					Dialogos.mostrarDialogoInformacion(getFrame(), "Operaci�n correcta", "El usuario ha sido eliminado correctamente.");
					restablecerPanel();
					
				}
				
			} catch(SQLException e) {
				e.printStackTrace();
				Dialogos.mostrarDialogoError(getFrame(), "Error", e.getLocalizedMessage());
			} catch(RemoteException e) {
				Dialogos.mostrarDialogoError(getFrame(), "Error", e.getLocalizedMessage());		
			} catch(Exception e) {
				e.printStackTrace();
				Dialogos.mostrarDialogoError(getFrame(), "Error", e.getLocalizedMessage());
			}
		}
	}
	
	private void chkEditarActionPerformed(ActionEvent evt) {
		cambiarEdicion(chkEditar.isSelected());
	}
	
	private void btnCalendarioActionPerformed(ActionEvent evt) {
		// Creamos la ventana de consulta/edici�n de calendarios
		frmCalendario = new JFCalendarioLaboral();
		frmCalendario.addVentanaCerradaListener(new VentanaCerradaListener() {
			public void ventanaCerrada(EventObject evt) {    
				calendarioVentanaCerrada(evt);
			}
		});
		// Desactivamos la ventana hasta cerrar la ventana de calendarios
		getFrame().setEnabled(false);
		frmCalendario.setPeriodosTrabajo(periodos);
		frmCalendario.setModificable(chkEditar.isSelected());
		frmCalendario.setLocationRelativeTo(this);		
		frmCalendario.setVisible(true);
	}
	
	private void calendarioVentanaCerrada(EventObject evt) {
		// Reactivamos la ventana 
		getFrame().setEnabled(true);
		frmCalendario.setVisible(false);
		periodos = new Vector<PeriodoTrabajo>();
		periodos.addAll(frmCalendario.getPeriodosTrabajo());
		if(frmCalendario.getHorasSemanales() == 1) {
			lblHorasSemanales.setText("1 hora semanal");
		} else {
			lblHorasSemanales.setText(frmCalendario.getHorasSemanales() + " horas semanales");
		}
		// Eliminamos la ventana de configuraci�n
		frmCalendario.dispose();
	}
	
	private void cmbRolesItemStateChanged(ItemEvent evt) {
		if(evt.getStateChange() == ItemEvent.SELECTED) {
			if(evt.getItem().equals(RolesUsuario.Medico.name())) {
				btnCalendario.setEnabled(true);
			} else {
				btnCalendario.setEnabled(false);
				periodos = new Vector<PeriodoTrabajo>();
			}
		}
	}

	private void txtPasswordFocusGained(FocusEvent evt) {
		if(txtPassword.isEditable() && !passwordCambiada) {
			txtPassword.setText("");
			passwordCambiada = true;
		}
	}

	private boolean campoVacio(JTextField campo) {
		return campo.getText().trim().equals("");
	}
	
	private void cambiarEdicion(boolean activar) {
		txtNombre.setEditable(activar);
		txtApellidos.setEditable(activar);
		txtLogin.setEditable(activar);
		txtPassword.setEditable(activar);
		txtPasswordConf.setEditable(activar);
		txtCorreoElectronico.setEditable(activar);
		txtTelefonoFijo.setEditable(activar);
		txtTelefonoMovil.setEditable(activar);
		cmbRol.setEnabled(activar);
		btnGuardar.setEnabled(activar);
		btnEliminar.setEnabled(activar);
		lblCamposOblig.setVisible(activar);
		if(activar) {
			lblNIF.setText("NIF *");
			lblNombre.setText("Nombre *");
			lblApellidos.setText("Apellidos *");
			lblLogin.setText("Usuario *");
			lblPassword.setText("Contrase�a *");
			lblPasswordConf.setText("Confirmar contrase�a *");
			lblRol.setText("Tipo de usuario *");
			lblCalendario.setText("Calendario laboral *");
			lblCentro.setText("Centro asignado *");
			btnCalendario.setText("Configurar...");
		} else {
			lblNIF.setText("NIF");
			lblNombre.setText("Nombre");
			lblApellidos.setText("Apellidos");
			lblLogin.setText("Usuario");
			lblPassword.setText("Contrase�a");
			lblPasswordConf.setText("Confirmar contrase�a");
			lblRol.setText("Tipo de usuario");
			lblCalendario.setText("Calendario laboral");
			lblCentro.setText("Centro asignado");
			btnCalendario.setText("Ver...");
		}
		passwordCambiada = false;
	}

	private void limpiarCamposConsulta() {
		usuario = null;
		txtNIF.setText("");
		txtLogin.setText("");
		txtPassword.setText("");
		txtPasswordConf.setText("");
		txtNombre.setText("");
		txtApellidos.setText("");
		txtCentro.setText("");
		txtCorreoElectronico.setText("");
		txtTelefonoFijo.setText("");
		txtTelefonoMovil.setText("");
		chkEditar.setEnabled(false);
		chkEditar.setSelected(false);
		cmbRol.setSelectedIndex(-1);
		lblCalendario.setVisible(false);
		btnCalendario.setVisible(false);
		lblHorasSemanales.setVisible(false);
		lblEspecialidad.setVisible(false);
		txtEspecialidad.setVisible(false);
		cambiarEdicion(false);
	}

	private void rellenarTiposUsuario(String[] elementos) {
		DefaultComboBoxModel cmbRolesModel;
		
		cmbRolesModel = new DefaultComboBoxModel(elementos);
		cmbRol.setModel(cmbRolesModel);	
	}

	private void rellenarTiposUsuarioNoMedico() {
		DefaultComboBoxModel cmbRolesModel;
		
		cmbRolesModel = new DefaultComboBoxModel();
		for(RolesUsuario rol : RolesUsuario.values()) {
			if(rol != RolesUsuario.Medico) {
				cmbRolesModel.addElement(rol.name());
			}
		}
		cmbRol.setModel(cmbRolesModel);	
	}

	// M�todos p�blicos

	public Usuario getUsuario() {
		return usuario;
	}

	public void addUsuarioBuscadoListener(UsuarioBuscadoListener listener) {
		listenerList.add(UsuarioBuscadoListener.class, listener);
	}

	public void removeUsuarioBuscadoListener(UsuarioBuscadoListener listener) {
		listenerList.remove(UsuarioBuscadoListener.class, listener);
	}
	
	public void reducirPanel() {
		// Este m�todo oculta algunos controles de la interfaz para
		// que se pueda reutilizar el panel desde otros paneles
		// (como JPEstablecerSustituto) mostrando s�lo la informaci�n
		// b�sica de los usuarios
		lblLogin.setVisible(false);
		txtLogin.setVisible(false);
		lblPassword.setVisible(false);
		txtPassword.setVisible(false);
		lblPasswordConf.setVisible(false);
		txtPasswordConf.setVisible(false);
		lblCorreoElectronico.setVisible(false);
		txtCorreoElectronico.setVisible(false);
		lblTelefonoFijo.setVisible(false);
		txtTelefonoFijo.setVisible(false);
		lblTelefonoMovil.setVisible(false);
		txtTelefonoMovil.setVisible(false);
		lblRol.setVisible(false);
		cmbRol.setVisible(false);
		lblCalendario.setVisible(false);
		btnCalendario.setVisible(false);
		btnGuardar.setVisible(false);
		chkEditar.setVisible(false);
		btnEliminar.setVisible(false);
		this.remove(lblCentro);
		this.add(lblCentro, ((AnchorLayout)this.getLayout()).getLayoutComponentConstraint(lblLogin));
		this.remove(txtCentro);
		this.add(txtCentro, ((AnchorLayout)this.getLayout()).getLayoutComponentConstraint(txtLogin));
	}
	
	public void desactivarModificacion() {
		// Este m�todo se utiliza cuando un usuario puede
		// consultar un beneficiario, pero no modificarlo
		chkEditar.setVisible(false);
		btnGuardar.setVisible(false);
		btnEliminar.setVisible(false);
	}
	
	// <m�todos del observador>
	
	public void usuarioActualizado(Usuario usuario) {
		if(this.usuario != null && this.usuario.getDni().equals(usuario.getDni())) {
			// Otro cliente ha actualizado el beneficiario mostrado
			Dialogos.mostrarDialogoAdvertencia(getFrame(), "Aviso", "El usuario mostrado ha sido modificado por otro cliente.");
			mostrarDatosUsuario(usuario);
		}
	}
	
	public void usuarioEliminado(Usuario usuario) {
		if(this.usuario != null && this.usuario.getDni().equals(usuario.getDni())) {
			// Otro cliente ha eliminado el beneficiario mostrado
			Dialogos.mostrarDialogoAdvertencia(getFrame(), "Aviso", "El usuario mostrado ha sido eliminado por otro cliente.");
			restablecerPanel();
		}
	}
	
	public void restablecerPanel() {
		txtNIFBuscado.setText("");
		limpiarCamposConsulta();
	}
	
	//$hide<<$
	
}
