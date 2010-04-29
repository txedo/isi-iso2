package presentacion;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.EventObject;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import presentacion.auxiliar.Dialogos;
import presentacion.auxiliar.Validacion;
import presentacion.auxiliar.VentanaCerradaListener;
import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;
import dominio.conocimiento.Administrador;
import dominio.conocimiento.Cabecera;
import dominio.conocimiento.CategoriasMedico;
import dominio.conocimiento.CentroSalud;
import dominio.conocimiento.Citador;
import dominio.conocimiento.Especialidades;
import dominio.conocimiento.Especialista;
import dominio.conocimiento.Medico;
import dominio.conocimiento.Pediatra;
import dominio.conocimiento.PeriodoTrabajo;
import dominio.conocimiento.Roles;
import dominio.conocimiento.TipoMedico;
import dominio.conocimiento.Usuario;
import dominio.conocimiento.IConstantes;
import dominio.control.ControladorCliente;
import excepciones.ApellidoIncorrectoException;
import excepciones.ContraseñaIncorrectaException;
import excepciones.CorreoElectronicoIncorrectoException;
import excepciones.LoginIncorrectoException;
import excepciones.NIFIncorrectoException;
import excepciones.NombreIncorrectoException;
import excepciones.TelefonoFijoIncorrectoException;
import excepciones.TelefonoMovilIncorrectoException;
import excepciones.UsuarioNoSeleccionadoException;

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
 * Panel que permite registrar nuevos usuarios en el sistema.
 */
public class JPUsuarioRegistrar extends JPBase implements IConstantes {

	private static final long serialVersionUID = 4857739286462180783L;
	
	private Vector<PeriodoTrabajo> periodos;

	private DefaultListModel lstTipoUsuarioModel;
	private DefaultListModel lstTipoMedicoModel;
	private JList lstTipoUsuario;
	private JButton btnRestablecer;
	private JButton btnCrearUsuario;
	private JLabel lblApellidos;
	private JPasswordField txtPassword;
	private JList lstTipoMedico;
	private JLabel lblTipoUsuario;
	private JLabel lblCamposOblig;
	private JLabel lblEspecialidad;
	private JComboBox cmbEspecialidad;
	private JLabel lblNombre;
	private JLabel lblPasswordConf;
	private JLabel lblTelefonoMovil;
	private JTextField txtTelefonoMovil;
	private JTextField txtTelefonoFijo;
	private JTextField txtCorreoElectronico;
	private JLabel lblTelefonoFijo;
	private JLabel lblCorreoElectronico;
	private JLabel lblHorasSemanales;
	private JLabel lblPassword;
	private JLabel lblLogin;
	private JLabel lblNIF;
	private JTextField txtApellidos;
	private JTextField txtNombre;
	private JPasswordField txtPasswordConf;
	private JTextField txtLogin;
	private JTextField txtNIF;
	private JButton btnCalendario;
	private JLabel lblCalendario;
	
	private JFCalendarioLaboral frmCalendario;
	
	public JPUsuarioRegistrar() {
		this(null, null);
		// Este constructor evita que aparezca un error al editar
		// los formularios o paneles que utilizan JPUsuarioRegistrar
	}
	
	public JPUsuarioRegistrar(JFPrincipal frame, ControladorCliente controlador) {
		super(frame, controlador);
		periodos = new Vector<PeriodoTrabajo>();
		initGUI();
		rellenarTiposUsuario();
		cambiarEstadoEspecialidad(false);
		cambiarEstadoConfiguracionCalendario(false);
	}
	
	private void initGUI() {
		try {
			AnchorLayout thisLayout = new AnchorLayout();
			this.setLayout(thisLayout);
			this.setPreferredSize(new java.awt.Dimension(430, 428));
			{
				lblHorasSemanales = new JLabel();
				this.add(lblHorasSemanales, new AnchorConstraint(316, 973, 644, 268, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblHorasSemanales.setText("0 horas semanales");
				lblHorasSemanales.setPreferredSize(new java.awt.Dimension(150, 16));
				lblHorasSemanales.setName("lblHorasSemanales");
			}
			{
				btnCalendario = new JButton();
				this.add(btnCalendario, new AnchorConstraint(312, 905, 650, 150, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				btnCalendario.setText("Configurar...");
				btnCalendario.setPreferredSize(new java.awt.Dimension(110, 23));
				btnCalendario.setName("btnCalendario");
				btnCalendario.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnCalendarioActionPerformed(evt);
					}
				});
			}
			{
				lblCalendario = new JLabel();
				this.add(lblCalendario, new AnchorConstraint(316, 292, 675, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblCalendario.setText("Calendario laboral");
				lblCalendario.setPreferredSize(new java.awt.Dimension(126, 14));
			}
			{
				lblEspecialidad = new JLabel();
				this.add(lblEspecialidad, new AnchorConstraint(343, 292, 680, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblEspecialidad.setText("Especialidad *");
				lblEspecialidad.setPreferredSize(new java.awt.Dimension(126, 16));
			}
			{
				cmbEspecialidad = new JComboBox();
				this.add(cmbEspecialidad, new AnchorConstraint(340, 12, 691, 150, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				cmbEspecialidad.setPreferredSize(new java.awt.Dimension(268, 22));
				cmbEspecialidad.setName("cmbEspecialidad");
			}
			{
				btnRestablecer = new JButton();
				this.add(btnRestablecer, new AnchorConstraint(390, 308, 957, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				btnRestablecer.setText("Restablecer");
				btnRestablecer.setPreferredSize(new java.awt.Dimension(120, 26));
				btnRestablecer.setName("btnRestablecer");
				btnRestablecer.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnRestablecerActionPerformed(evt);
					}
				});
			}
			{
				btnCrearUsuario = new JButton();
				this.add(btnCrearUsuario, new AnchorConstraint(390, 12, 957, 603, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
				btnCrearUsuario.setText("Crear usuario");
				btnCrearUsuario.setPreferredSize(new java.awt.Dimension(120, 26));
				btnCrearUsuario.setName("btnCrearUsuario");
				btnCrearUsuario.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnCrearUsuarioActionPerformed(evt);
					}
				});
			}
			{
				lstTipoMedico = new JList();
				this.add(lstTipoMedico, new AnchorConstraint(253, 961, 601, 268, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lstTipoMedico.setPreferredSize(new java.awt.Dimension(110, 54));
				lstTipoMedico.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
				lstTipoMedico.setVisible(false);
				lstTipoMedico.setName("lstTipoMedico");
				lstTipoMedico.addListSelectionListener(new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent evt) {
						lstTipoMedicoValueChanged(evt);
					}
				});
			}
			{
				lstTipoUsuario = new JList();
				this.add(lstTipoUsuario, new AnchorConstraint(253, 684, 773, 150, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lstTipoUsuario.setPreferredSize(new java.awt.Dimension(110, 54));
				lstTipoUsuario.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				lstTipoUsuario.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
				lstTipoUsuario.setName("lstTipoUsuario");
				lstTipoUsuario.addListSelectionListener(new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent evt) {
						lstTipoUsuarioValueChanged(evt);
					}
				});
			}
			{
				txtPasswordConf = new JPasswordField();
				this.add(txtPasswordConf, new AnchorConstraint(146, 12, 534, 150, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtPasswordConf.setPreferredSize(new java.awt.Dimension(268, 22));
				txtPasswordConf.setName("txtPasswordConf");
			}
			{
				txtPassword = new JPasswordField();
				this.add(txtPassword, new AnchorConstraint(119, 12, 449, 150, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtPassword.setPreferredSize(new java.awt.Dimension(268, 22));
				txtPassword.setName("txtPassword");
			}
			{
				txtLogin = new JTextField();
				this.add(txtLogin, new AnchorConstraint(92, 12, 363, 150, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtLogin.setPreferredSize(new java.awt.Dimension(268, 22));
				txtLogin.setName("txtLogin");
			}
			{
				txtApellidos = new JTextField();
				this.add(txtApellidos, new AnchorConstraint(65, 12, 277, 150, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtApellidos.setPreferredSize(new java.awt.Dimension(268, 22));
				txtApellidos.setName("txtApellidos");
			}
			{
				txtNombre = new JTextField();
				this.add(txtNombre, new AnchorConstraint(39, 12, 195, 150, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtNombre.setPreferredSize(new java.awt.Dimension(268, 22));
				txtNombre.setName("txtNombre");
			}
			{
				txtNIF = new JTextField();
				this.add(txtNIF, new AnchorConstraint(12, 12, 109, 150, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtNIF.setPreferredSize(new java.awt.Dimension(268, 22));
				txtNIF.setName("txtNIF");
			}
			{
				lblTipoUsuario = new JLabel();
				this.add(lblTipoUsuario, new AnchorConstraint(257, 292, 604, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblTipoUsuario.setText("Tipo de usuario *");
				lblTipoUsuario.setPreferredSize(new java.awt.Dimension(126, 14));
			}
			{
				lblPasswordConf = new JLabel();
				this.add(lblPasswordConf, new AnchorConstraint(150, 288, 519, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblPasswordConf.setText("Confirmar contraseña *");
				lblPasswordConf.setPreferredSize(new java.awt.Dimension(130, 14));
			}
			{
				lblPassword = new JLabel();
				this.add(lblPassword, new AnchorConstraint(123, 292, 433, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblPassword.setText("Contraseña *");
				lblPassword.setPreferredSize(new java.awt.Dimension(126, 14));
			}
			{
				lblLogin = new JLabel();
				this.add(lblLogin, new AnchorConstraint(96, 292, 347, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblLogin.setText("Usuario *");
				lblLogin.setPreferredSize(new java.awt.Dimension(126, 14));
			}
			{
				lblApellidos = new JLabel();
				this.add(lblApellidos, new AnchorConstraint(69, 292, 261, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblApellidos.setText("Apellidos *");
				lblApellidos.setPreferredSize(new java.awt.Dimension(126, 14));
			}
			{
				lblNombre = new JLabel();
				this.add(lblNombre, new AnchorConstraint(43, 292, 182, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblNombre.setText("Nombre *");
				lblNombre.setPreferredSize(new java.awt.Dimension(126, 14));
			}
			{
				lblNIF = new JLabel();
				this.add(lblNIF, new AnchorConstraint(16, 292, 93, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblNIF.setText("NIF *");
				lblNIF.setPreferredSize(new java.awt.Dimension(126, 14));
			}
			{
				lblCamposOblig = new JLabel();
				this.add(lblCamposOblig, new AnchorConstraint(365, 13, 900, 696, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
				lblCamposOblig.setText("* Campos obligatorios");
				lblCamposOblig.setPreferredSize(new java.awt.Dimension(129, 17));
				lblCamposOblig.setHorizontalAlignment(SwingConstants.TRAILING);
				lblCamposOblig.setFont(lblCamposOblig.getFont().deriveFont(10.0f));
			}
			{
				lblTelefonoMovil = new JLabel();
				this.add(lblTelefonoMovil, new AnchorConstraint(231, 431, 790, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblTelefonoMovil.setText("Teléfono móvil");
				lblTelefonoMovil.setPreferredSize(new java.awt.Dimension(126, 14));
			}
			{
				lblTelefonoFijo = new JLabel();
				this.add(lblTelefonoFijo, new AnchorConstraint(204, 431, 692, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblTelefonoFijo.setText("Teléfono fijo");
				lblTelefonoFijo.setPreferredSize(new java.awt.Dimension(126, 14));
			}
			{
				lblCorreoElectronico = new JLabel();
				this.add(lblCorreoElectronico, new AnchorConstraint(177, 431, 594, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblCorreoElectronico.setText("Correo electrónico");
				lblCorreoElectronico.setPreferredSize(new java.awt.Dimension(126, 14));
			}
			{
				txtTelefonoMovil = new JTextField();
				this.add(txtTelefonoMovil, new AnchorConstraint(227, 12, 805, 150, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtTelefonoMovil.setPreferredSize(new java.awt.Dimension(268, 22));
				txtTelefonoMovil.setName("txtTelefonoMovil");
			}
			{
				txtTelefonoFijo = new JTextField();
				this.add(txtTelefonoFijo, new AnchorConstraint(200, 12, 710, 150, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtTelefonoFijo.setPreferredSize(new java.awt.Dimension(268, 22));
				txtTelefonoFijo.setName("txtTelefonoFijo");
			}
			{
				txtCorreoElectronico = new JTextField();
				this.add(txtCorreoElectronico, new AnchorConstraint(173, 12, 612, 150, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtCorreoElectronico.setPreferredSize(new java.awt.Dimension(268, 22));
				txtCorreoElectronico.setName("txtCorreoElectronico");
			}
		} catch(Exception e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getMessage());
		}
	}
	
	//$hide>>$
	
	private void btnRestablecerActionPerformed(ActionEvent evt) {
		limpiarCamposRegistro();
	}
	
	private void btnCrearUsuarioActionPerformed(ActionEvent evt) {
		Usuario usuario = null;
		TipoMedico tipo = null;
		CentroSalud centro;
		
		try {
			
			// Comprobamos todos los campos
			Validacion.comprobarNIF(txtNIF.getText().trim().toUpperCase());
			Validacion.comprobarNombre(txtNombre.getText().trim());
			Validacion.comprobarApellidos(txtApellidos.getText().trim());
			Validacion.comprobarUsuario(txtLogin.getText().trim());
			Validacion.comprobarContraseña(new String(txtPassword.getPassword()));
			if(!campoVacio(txtCorreoElectronico)) {
				Validacion.comprobarCorreoElectronico(txtCorreoElectronico.getText().trim());
			}
			if(!campoVacio(txtTelefonoFijo)) {
				Validacion.comprobarTelefonoFijo(txtTelefonoFijo.getText().trim());
			}
			if(!campoVacio(txtTelefonoMovil)) {
				Validacion.comprobarTelefonoMovil(txtTelefonoMovil.getText().trim());
			}
			if(!(new String(txtPassword.getPassword())).equals(new String(txtPasswordConf.getPassword()))) {
				throw new ContraseñaIncorrectaException("Las contraseñas no coinciden.");
			}
			if(lstTipoUsuario.getSelectedIndex() == -1) {
				throw new UsuarioNoSeleccionadoException("No se ha seleccionado el tipo de usuario.");
			} else if(lstTipoMedico.getSelectedIndex() == -1 && lstTipoUsuario.getSelectedValue().equals(Roles.Médico.name())) {
				throw new UsuarioNoSeleccionadoException("No se ha seleccionado el tipo de médico.");
			}

			// Creamos un nuevo usuario con los datos introducidos
			switch (Roles.valueOf(lstTipoUsuario.getSelectedValue().toString())) {
				case Administrador:
					usuario = new Administrador();
					break;
				case Citador:
					usuario = new Citador();
					break;
				case Médico:
					usuario = new Medico();
					break;
			}
			usuario.setNif(txtNIF.getText().trim().toUpperCase());
			usuario.setNombre(txtNombre.getText().trim());
			usuario.setApellidos(txtApellidos.getText().trim());
			usuario.setLogin(txtLogin.getText().trim());
			usuario.setPassword(new String(txtPassword.getPassword()));
			usuario.setCorreo(txtCorreoElectronico.getText().trim());
			usuario.setTelefono(txtTelefonoFijo.getText().trim());
			usuario.setMovil(txtTelefonoMovil.getText().trim());
			
			// Creamos el tipo de médico si es necesario
			if(usuario.getRol() == Roles.Médico) {
				switch(CategoriasMedico.valueOf(lstTipoMedico.getSelectedValue().toString())) {
				case Cabecera:
					tipo = new Cabecera();
					break;
				case Pediatra:
					tipo = new Pediatra();
					break;
				case Especialista:
					tipo = new Especialista(cmbEspecialidad.getSelectedItem().toString());
					break;
				}
				((Medico)usuario).setTipoMedico(tipo);
				((Medico)usuario).setCalendario(periodos);
			}
			
			// Solicitamos al servidor que se cree el usuario
			getControlador().crearUsuario(usuario);
			
			// Obtenemos el centro que se le ha asignado al usuario
			centro = getControlador().consultarUsuario(usuario.getNif()).getCentroSalud();

			// Mostramos el resultado de la operación y limpiamos el panel
			Dialogos.mostrarDialogoInformacion(getFrame(), "Operación correcta", "El usuario ha sido dado de alta en el sistema y se\nle ha asignado automáticamente el siguiente centro:\n" + centro.getNombre() + " (" + centro.getDireccion() + ")");
			restablecerPanel();
		
		} catch(NIFIncorrectoException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getMessage());
			txtNIF.selectAll();
			txtNIF.grabFocus();
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
		} catch(UsuarioNoSeleccionadoException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getMessage());

		} catch(SQLException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getLocalizedMessage());
		} catch(RemoteException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getLocalizedMessage());
		} catch(Exception e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getLocalizedMessage());
		}
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
		frmCalendario.setModificable(true);
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
	
	private boolean campoVacio(JTextField campo) {
		return campo.getText().trim().equals("");
	}

	private void rellenarTiposUsuario() {
		lstTipoUsuarioModel = new DefaultListModel();
		for(Roles rol : Roles.values()) {
			lstTipoUsuarioModel.addElement(rol.name());			
		}
		lstTipoUsuario.setModel(lstTipoUsuarioModel);
		lstTipoMedicoModel = new DefaultListModel();
		for(CategoriasMedico tipo : CategoriasMedico.values()) {
			lstTipoMedicoModel.addElement(tipo.name());			
		}
		lstTipoMedico.setModel(lstTipoMedicoModel);
	}

	private void cambiarEstadoEspecialidad(boolean estado) {
		ComboBoxModel cmbEspecialidadModel;
		String[] valores;
		int i;
		
		lblEspecialidad.setVisible(estado);
		cmbEspecialidad.setVisible(estado);
		valores = new String[Especialidades.values().length]; 
		if(!estado) {
			cmbEspecialidadModel = new DefaultComboBoxModel();
		} else {
			for(i = 0; i < Especialidades.values().length; i++) { 
				valores[i] = Especialidades.values()[i].name();
			}
			cmbEspecialidadModel = new DefaultComboBoxModel(valores);
		}
		cmbEspecialidad.setModel(cmbEspecialidadModel);	
	}
	
	private void lstTipoUsuarioValueChanged(ListSelectionEvent evt) {
		cambiarEstadoEspecialidad(false);
		cambiarEstadoConfiguracionCalendario(false);
		if (lstTipoUsuario.getSelectedIndex()!=-1)
			if (lstTipoUsuario.getSelectedValue().equals(Roles.Médico.name())) {
				lstTipoMedico.setSelectedIndex(0);
				lstTipoMedico.setVisible(true);
				cambiarEstadoConfiguracionCalendario(true);
			} else {
				lstTipoMedico.setSelectedIndex(-1);
				lstTipoMedico.setVisible(false);
			}
	}
	
	private void cambiarEstadoConfiguracionCalendario(boolean b) {
		lblCalendario.setVisible(b);
		btnCalendario.setVisible(b);
		lblHorasSemanales.setVisible(b);
	}

	private void lstTipoMedicoValueChanged(ListSelectionEvent evt) {
		if (lstTipoUsuario.getSelectedIndex()!=-1)
			if (lstTipoMedico.getSelectedIndex()!=-1 && lstTipoMedico.getSelectedValue().equals(CategoriasMedico.Especialista.name())) {
				cambiarEstadoEspecialidad(true);
			} else {
				cambiarEstadoEspecialidad(false);
			}
	}
	
	private void limpiarCamposRegistro() {
		txtNIF.setText("");
		txtNombre.setText("");
		txtApellidos.setText("");
		txtLogin.setText("");
		txtPassword.setText("");
		txtPasswordConf.setText("");
		txtCorreoElectronico.setText("");
		txtTelefonoFijo.setText("");
		txtTelefonoMovil.setText("");
		lblHorasSemanales.setText("0 horas semanales");
		lstTipoUsuario.clearSelection();
		lstTipoMedico.clearSelection();
		lstTipoMedico.setVisible(false);
		lblCalendario.setVisible(false);
		btnCalendario.setVisible(false);
		lblHorasSemanales.setVisible(false);
		lblEspecialidad.setVisible(false);
		cmbEspecialidad.setVisible(false);
		periodos = new Vector<PeriodoTrabajo>();
	}
	
	// Métodos públicos
	
	public void restablecerPanel() {
		limpiarCamposRegistro();
	}
	
	//$hide<<$

}
