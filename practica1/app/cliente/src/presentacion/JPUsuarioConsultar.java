package presentacion;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Date;
import java.util.EventObject;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
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
import dominio.UtilidadesDominio;
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
import excepciones.ContraseñaIncorrectaException;
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
	
	private final String PASSWORD_OCULTA = "........";
	
	private EventListenerList listenerList;
	private Vector<PeriodoTrabajo> periodos;
	private Usuario usuario;
	private boolean passwordCambiada;
	private boolean soloMedicos;
	
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
	
	public JPUsuarioConsultar(JFPrincipal frame, ControladorCliente controlador) {
		super(frame, controlador);
		initGUI();
		listenerList = new EventListenerList();
		usuario = null;
		soloMedicos = false;
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
				btnCalendario.setToolTipText("Sólo tienen definido un calendario laboral los usuarios de tipo Médico");
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
				txtPasswordConf.addFocusListener(new FocusAdapter() {
					public void focusGained(FocusEvent evt) {
						txtPasswordConfFocusGained(evt);
					}
				});
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
				lblPasswordConf.setText("Confirmar contraseña *");
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
				chkEditar.setText("Habilitar edición");
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
				lblNIF.setText("NIF");
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
				lblEspecialidad.setText("Especialidad");
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
				lblPassword.setText("Contraseña *");
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
				lblTelefonoMovil.setText("Teléfono móvil");
				lblTelefonoMovil.setPreferredSize(new java.awt.Dimension(110, 15));
			}
			{
				lblTelefonoFijo = new JLabel();
				this.add(lblTelefonoFijo, new AnchorConstraint(272, 277, 776, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblTelefonoFijo.setText("Teléfono fijo");
				lblTelefonoFijo.setPreferredSize(new java.awt.Dimension(110, 15));
			}
			{
				lblCorreoElectronico = new JLabel();
				this.add(lblCorreoElectronico, new AnchorConstraint(244, 273, 690, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblCorreoElectronico.setText("Correo electrónico");
				lblCorreoElectronico.setPreferredSize(new java.awt.Dimension(108, 15));
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//$hide>>$
	
	private void btnBuscarActionPerformed(ActionEvent evt) {
		// Borramos la información del antiguo usuario consultado
		limpiarCamposConsulta();
					
		try {
			
			// Buscamos el usuario solicitado
			Validacion.comprobarNIF(txtNIFBuscado.getText().trim().toUpperCase());
			if(soloMedicos) {
				usuario = getControlador().consultarMedico(txtNIFBuscado.getText().trim().toUpperCase());
			} else {
				usuario = getControlador().consultarUsuario(txtNIFBuscado.getText().trim().toUpperCase());
			}

			// Mostramos los datos del usuario encontrado
			if(soloMedicos) {
				Dialogos.mostrarDialogoInformacion(getFrame(), "Resultados de la búsqueda", "Médico encontrado.");
			} else {
				Dialogos.mostrarDialogoInformacion(getFrame(), "Resultados de la búsqueda", "Usuario encontrado.");
			}
			
			mostrarDatosUsuario(usuario);
			
		} catch(UsuarioInexistenteException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getMessage());
			txtNIFBuscado.selectAll();
			txtNIFBuscado.grabFocus();			

		} catch(CadenaVaciaException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", "Debe introducir un NIF.");
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
	}
	
	private void mostrarDatosUsuario(Usuario usuario) {
		Object[] listeners;
		int horas;
		int i;
		
		// Actualizamos el usuario
		this.usuario = usuario;
		
		txtNIFBuscado.setText("");
		txtNIF.setText(usuario.getNif());
		txtLogin.setText(usuario.getLogin());
		txtPassword.setText(PASSWORD_OCULTA);
		txtPasswordConf.setText(PASSWORD_OCULTA);
		txtNombre.setText(usuario.getNombre());
		txtApellidos.setText(usuario.getApellidos());
		txtCorreoElectronico.setText(usuario.getCorreo());
		txtTelefonoFijo.setText(usuario.getTelefono());
		txtTelefonoMovil.setText(usuario.getMovil());
		txtCentro.setText(usuario.getCentroSalud().getNombre() + " (" + usuario.getCentroSalud().getDireccion() + ")");
		if(usuario.getRol().equals(RolesUsuario.Médico)) {
			rellenarTiposUsuario(new String[] { RolesUsuario.Médico.name() + " (" + ((Medico)usuario).getTipoMedico().getCategoria().name() + ")" });
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
		
		// Notificamos que ha cambiado el usuario seleccionado
		listeners = listenerList.getListenerList();
		for(i = 0; i < listeners.length; i += 2) {
			if(listeners[i] == UsuarioBuscadoListener.class) {
				((UsuarioBuscadoListener)listeners[i + 1]).usuarioBuscado(new EventObject(this));
			}
		}
	}
	
	private void btnGuardarActionPerformed(ActionEvent evt) {
		JFAvisos frmAviso;
		Usuario usuarioModif = null;
		Vector<Cita> citas, citasAfectadas = null;
		boolean cambioPassword, actualizar = false, afectada;
		
		try {
			
			// Comprobamos todos los campos
			Validacion.comprobarNombre(txtNombre.getText().trim());
			Validacion.comprobarApellidos(txtApellidos.getText().trim());
			Validacion.comprobarUsuario(txtLogin.getText().trim());
			if((passwordCambiada || !(new String(txtPassword.getPassword()).equals(PASSWORD_OCULTA)))
			   && (txtPassword.getPassword().length > 0 || txtPasswordConf.getPassword().length > 0)) { 
				Validacion.comprobarContraseña(new String(txtPassword.getPassword()));
				if(!(new String(txtPassword.getPassword())).equals(new String(txtPasswordConf.getPassword()))) {
					throw new ContraseñaIncorrectaException("Las contraseñas no coinciden.");
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
			// (si el usuario era médico no puede cambiar de rol)
			if(usuario.getRol() == RolesUsuario.Médico) {
				usuarioModif = new Medico();
			} else {
				if(cmbRol.getSelectedItem().toString().equals(RolesUsuario.Administrador.name())) {
					usuarioModif = new Administrador();
				} else if(cmbRol.getSelectedItem().toString().equals(RolesUsuario.Citador.name())) {
					usuarioModif = new Citador();
				}
			}
			usuarioModif.setNif(txtNIF.getText().trim().toUpperCase());
			usuarioModif.setLogin(txtLogin.getText().trim());
			if(passwordCambiada && txtPassword.getPassword().length > 0) {
				cambioPassword = true;
				usuarioModif.setPassword(new String(txtPassword.getPassword()));
			} else {
				cambioPassword = false;
				usuarioModif.setPassword(usuario.getPassword());
			}
			usuarioModif.setNombre(txtNombre.getText().trim());
			usuarioModif.setApellidos(txtApellidos.getText().trim());
			usuarioModif.setCorreo(txtCorreoElectronico.getText().trim());
			usuarioModif.setTelefono(txtTelefonoFijo.getText().trim());
			usuarioModif.setMovil(txtTelefonoMovil.getText().trim());
			
			// Dejamos el mismo centro de salud
			usuarioModif.setCentroSalud(usuario.getCentroSalud());

			// Cambiamos los atributos propios de los médicos
			if(usuarioModif.getRol() == RolesUsuario.Médico) {
				((Medico)usuarioModif).setCalendario(periodos);
				((Medico)usuarioModif).setTipoMedico(((Medico)usuario).getTipoMedico());
			}

			// Si el usuario es un médico y ha cambiado su horario de trabajo, 
			// se comprueba que ésto no afecte a las citas que ya tenía asignadas;
			// si afecta a alguna, se pide confirmación para anular las citas afectadas
			actualizar = true;
			if(usuarioModif.getRol() == RolesUsuario.Médico) {
				citas = getControlador().consultarCitasMedico(usuario.getNif());
				citasAfectadas = new Vector<Cita>();
				for(Cita cita : citas) {
					if(cita.getFechaYHora().after(new Date())) {
						afectada = true;
						for(PeriodoTrabajo periodo : ((Medico)usuarioModif).getCalendario()) {
							if(UtilidadesDominio.diaFecha(cita.getFechaYHora()) == periodo.getDia()
							 && cita.citaEnHoras(periodo.getHoraInicio(), periodo.getHoraFinal())) {
								// La cita queda dentro del nuevo horario
								afectada = false;
							}
						}
						if(afectada) {
							citasAfectadas.add(cita);
						}
					}
				}
				if(citasAfectadas.size() > 0) {
					actualizar = Dialogos.mostrarDialogoPregunta(getFrame(), "Pregunta", "Si se modifica el calendario de trabajo del médico, algunas\ncitas pendientes se verán afectadas y serán anuladas.\n¿Desea continuar?");
				}
			}
			
			if(actualizar) {
				// Si se le ha dado a botón de "Guardar" sin haber hecho ningún cambio, se muestra un aviso y no se realiza la operación
				
				if (usuario.equals(usuarioModif))
					Dialogos.mostrarDialogoAdvertencia(getFrame(), "Aviso", "No se ha hecho ningún cambio sobre el usuario.");
				else {
					// Si se va a mantener la contraseña, se deja en blanco
					// el campo del usuario para que el servidor sepa que
					// debe dejar la misma contraseña
					if(!cambioPassword) {
						usuarioModif.setPassword("");
					}
					// Solicitamos al servidor que se modifique el usuario
					getControlador().modificarUsuario(usuarioModif);
					// Mostramos el resultado de la operación y limpiamos el panel
					Dialogos.mostrarDialogoInformacion(getFrame(), "Operación correcta", "El usuario ha sido modificado correctamente.");
					restablecerPanel();
					// Mostramos las citas afectadas si se ha modificado un médico
					if(citasAfectadas != null && citasAfectadas.size() > 0) {
						// Se avisa de las citas eliminadas
						frmAviso = new JFAvisos();
						frmAviso.setLocationRelativeTo(this);
						frmAviso.mostrarCitas("Las siguientes citas han sido anuladas:", citasAfectadas);
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
		} catch(ContraseñaIncorrectaException e) {
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
		
		// Solicitamos confirmación para eliminar el usuario
		respuesta = Dialogos.mostrarDialogoPregunta(getFrame(), "Pregunta", "¿Seguro que desea eliminar este usuario?");
		
		if(respuesta) {
			try {
				
				if(usuario.getRol().equals(RolesUsuario.Médico)) {
					
					// Si el usuario a borrar es un médico, vemos si tiene
					// beneficiarios asignados o citas pendientes
					beneficiarios = getControlador().obtenerBeneficiariosMedico(usuario.getNif());
					citas = getControlador().consultarCitasMedico(usuario.getNif());
					mensaje = "";
					if(beneficiarios.size() > 0 && citas.size() > 0) {
						mensaje = "El médico que va a borrar tiene beneficiarios asignados y citas pendientes.\n";
					} else if(beneficiarios.size() > 0) {
						mensaje = "El médico que va a borrar tiene beneficiarios asignados.\n";
					} else if(citas.size() > 0) {
						mensaje = "El médico que va a borrar tiene citas pendientes.\n";
					}
					if(beneficiarios.size() > 0 || citas.size() > 0) {
						mensaje += "¿Quiere continuar con la eliminación?";
						respuesta = Dialogos.mostrarDialogoPregunta(getFrame(), "Pregunta", mensaje);
					} else {
						respuesta = true;
					}
					
					if(respuesta) {
						
						// Solicitamos al servidor que se elimine el médico
						getControlador().eliminarUsuario((Medico)usuario);
						
						// Mostramos el resultado de la operación y limpiamos el panel
						Dialogos.mostrarDialogoInformacion(getFrame(), "Operación correcta", "El usuario ha sido eliminado correctamente.");
						restablecerPanel();
						
						// Volvemos a leer los beneficiarios para obtener sus
						// nuevos médicos y se los mostramos al administrador
						// para que les pueda avisar del cambio
						if(beneficiarios.size() > 0) {
							frmAviso = new JFAvisos();
							frmAviso.setLocationRelativeTo(this);
							beneficiariosCamb = new Vector<Beneficiario>();
							for(Beneficiario beneficiario : beneficiarios) {
								beneficiariosCamb.add(getControlador().consultarBeneficiarioPorNIF(beneficiario.getNif()));
							}
							frmAviso.mostrarBeneficiarios("Los siguientes beneficiarios han cambiado de médico:", beneficiariosCamb);
						}
						
						// Mostramos las citas que se han anulado al eliminar el médico
						if(citas.size() > 0) {
							frmAviso = new JFAvisos();
							frmAviso.setLocationRelativeTo(this);
							frmAviso.mostrarCitas("Las siguientes citas se han anulado:", citas);
						}
						
					}
					
				} else {
					
					// Solicitamos al servidor que se elimine el usuario
					getControlador().eliminarUsuario(usuario);
					
					// Mostramos el resultado de la operación y limpiamos el panel
					Dialogos.mostrarDialogoInformacion(getFrame(), "Operación correcta", "El usuario ha sido eliminado correctamente.");
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
		// Creamos la ventana de consulta/edición de calendarios
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
		// Eliminamos la ventana de configuración
		frmCalendario.dispose();
	}
	
	private void cmbRolesItemStateChanged(ItemEvent evt) {
		if(evt.getStateChange() == ItemEvent.SELECTED) {
			if(evt.getItem().equals(RolesUsuario.Médico.name())) {
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
			txtPasswordConf.setText("");
			passwordCambiada = true;
		}
	}

	private void txtPasswordConfFocusGained(FocusEvent evt) {
		if(txtPasswordConf.isEditable() && !passwordCambiada) {
			txtPassword.setText("");
			txtPasswordConf.setText("");
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
			lblNombre.setText("Nombre *");
			lblApellidos.setText("Apellidos *");
			lblLogin.setText("Usuario *");
			lblPassword.setText("Contraseña *");
			lblPasswordConf.setText("Confirmar contraseña *");
			if (!usuario.getRol().equals(RolesUsuario.Médico))
				lblRol.setText("Tipo de usuario *");
			btnCalendario.setText("Configurar...");
		} else {
			lblNIF.setText("NIF");
			lblNombre.setText("Nombre");
			lblApellidos.setText("Apellidos");
			lblLogin.setText("Usuario");
			lblPassword.setText("Contraseña");
			lblPasswordConf.setText("Confirmar contraseña");
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
			if(rol != RolesUsuario.Médico) {
				cmbRolesModel.addElement(rol.name());
			}
		}
		cmbRol.setModel(cmbRolesModel);	
	}

	// Métodos públicos

	public Usuario getUsuario() {
		return usuario;
	}
	
	public void setSoloMedicos(boolean soloMedicos) {
		this.soloMedicos = soloMedicos;
		if(soloMedicos) {
			lblBuscar.setText("Buscar médico:");
		} else {
			lblBuscar.setText("Buscar usuario:");
		}
	}

	public void addUsuarioBuscadoListener(UsuarioBuscadoListener listener) {
		listenerList.add(UsuarioBuscadoListener.class, listener);
	}

	public void removeUsuarioBuscadoListener(UsuarioBuscadoListener listener) {
		listenerList.remove(UsuarioBuscadoListener.class, listener);
	}
	
	public void reducirPanel() {
		// Este método oculta algunos controles de la interfaz para
		// que se pueda reutilizar el panel desde otros paneles
		// (como JPEstablecerSustituto) mostrando sólo la información
		// básica de los usuarios
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
		// Este método se utiliza cuando un usuario puede
		// consultar un beneficiario, pero no modificarlo
		chkEditar.setVisible(false);
		btnGuardar.setVisible(false);
		btnEliminar.setVisible(false);
	}
	
	// <métodos del observador>
	
	public void usuarioActualizado(Usuario usuario) {
		if(this.usuario != null && this.usuario.getNif().equals(usuario.getNif())) {
			// Otro cliente ha actualizado el beneficiario mostrado
			Dialogos.mostrarDialogoAdvertencia(getFrame(), "Aviso", "El usuario mostrado ha sido modificado por otro cliente.");
			mostrarDatosUsuario(usuario);
		}
	}
	
	public void usuarioEliminado(Usuario usuario) {
		if(this.usuario != null && this.usuario.getNif().equals(usuario.getNif())) {
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
