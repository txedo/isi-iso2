package presentacion;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.DebugGraphics;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;
import dominio.conocimiento.Administrador;
import dominio.conocimiento.Cabecera;
import dominio.conocimiento.Citador;
import dominio.conocimiento.Especialista;
import dominio.conocimiento.Medico;
import dominio.conocimiento.Pediatra;
import dominio.conocimiento.PeriodoTrabajo;
import dominio.conocimiento.RolesUsuarios;
import dominio.conocimiento.TipoMedico;
import dominio.conocimiento.Usuario;
import dominio.conocimiento.Validacion;
import dominio.conocimiento.IConstantes;
import dominio.control.ControladorCliente;
import excepciones.ApellidoIncorrectoException;
import excepciones.CentroSaludInexistenteException;
import excepciones.ContraseñaIncorrectaException;
import excepciones.NIFIncorrectoException;
import excepciones.NombreIncorrectoException;
import excepciones.UsuarioNoSeleccionadoException;
import excepciones.UsuarioYaExistenteException;

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
	
	private final String USU_ADMINISTRADOR = "Administrador";
	private final String USU_MEDICO = "Medico";
	private final String USU_CITADOR = "Citador";
	private final String MED_CABECERA = "Cabecera";
	private final String MED_PEDIATRA = "Pediatra";
	private final String MED_ESPECIALISTA = "Especialista";
	
	private DefaultListModel lstTipoUsuarioModel;
	private DefaultListModel lstTipoMedicoModel;
	private JList lstTipoUsuario;
	private JButton btnRestablecer;
	private JButton btnCrearUsuario;
	private JLabel lblApellidos;
	private JPasswordField txtPassword;
	private JList lstTipoMedico;
	private JLabel lblTipoUsuario;
	private JLabel lblEspecialidad;
	private JTextField txtEspecialidad;
	private JLabel lblNombre;
	private JLabel lblPassword2;
	private JLabel lblPassword;
	private JLabel lblLogin;
	private JLabel lblNIF;
	private JTextField txtApellidos;
	private JTextField txtNombre;
	private JPasswordField txtPassword2;
	private JTextField txtLogin;
	private JTextField txtNIF;
	private JButton btnCalendario;
	private JLabel lblCalendario;
	private Vector<PeriodoTrabajo> periodos;
	
	public JPUsuarioRegistrar(JFrame frame, ControladorCliente controlador) {
		super(frame, controlador);
		periodos = new Vector<PeriodoTrabajo>();
		initGUI();
		crearModelos();
	}
	
	private void initGUI() {
		try {
			AnchorLayout thisLayout = new AnchorLayout();
			this.setLayout(thisLayout);
			this.setSize(430, 390);
			this.setPreferredSize(new java.awt.Dimension(430, 390));
			{
				btnCalendario = new JButton();
				this.add(btnCalendario, new AnchorConstraint(232, 905, 650, 184, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				btnCalendario.setText("Configurar...");
				btnCalendario.setPreferredSize(new java.awt.Dimension(110, 21));
				btnCalendario.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnCalendarioActionPerformed(evt);
					}
				});
			}
			{
				lblCalendario = new JLabel();
				this.add(lblCalendario, new AnchorConstraint(235, 332, 675, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblCalendario.setText("Calendario laboral");
				lblCalendario.setPreferredSize(new java.awt.Dimension(86, 14));
			}
			{
				lblEspecialidad = new JLabel();
				this.add(lblEspecialidad, new AnchorConstraint(262, 347, 680, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblEspecialidad.setText("Especialidad");
				lblEspecialidad.setPreferredSize(new java.awt.Dimension(71, 16));
			}
			{
				txtEspecialidad = new JTextField();
				this.add(txtEspecialidad, new AnchorConstraint(259, 18, 691, 184, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtEspecialidad.setPreferredSize(new java.awt.Dimension(229, 22));
			}
			{
				btnRestablecer = new JButton();
				this.add(btnRestablecer, new AnchorConstraint(309, 182, 957, 219, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
				btnRestablecer.setText("Restablecer");
				btnRestablecer.setPreferredSize(new java.awt.Dimension(154, 26));
				btnRestablecer.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnRestablecerActionPerformed(evt);
					}
				});
			}
			{
				btnCrearUsuario = new JButton();
				this.add(btnCrearUsuario, new AnchorConstraint(309, 17, 957, 603, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
				btnCrearUsuario.setText("Crear usuario");
				btnCrearUsuario.setPreferredSize(new java.awt.Dimension(154, 26));
				btnCrearUsuario.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnCrearUsuarioActionPerformed(evt);
					}
				});
			}
			{
				lstTipoMedico = new JList();
				this.add(lstTipoMedico, new AnchorConstraint(173, 961, 601, 303, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lstTipoMedico.setPreferredSize(new java.awt.Dimension(110, 54));
				lstTipoMedico.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
				lstTipoMedico.setVisible(false);
				lstTipoMedico.addListSelectionListener(new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent evt) {
						lstTipoMedicoValueChanged(evt);
					}
				});
			}
			{
				lstTipoUsuario = new JList();
				this.add(lstTipoUsuario, new AnchorConstraint(173, 684, 773, 184, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lstTipoUsuario.setPreferredSize(new java.awt.Dimension(110, 54));
				lstTipoUsuario.setDebugGraphicsOptions(DebugGraphics.BUFFERED_OPTION);
				lstTipoUsuario.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				lstTipoUsuario.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
				lstTipoUsuario.addListSelectionListener(new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent evt) {
						lstTipoUsuarioValueChanged(evt);
					}
				});
			}
			{
				txtPassword2 = new JPasswordField();
				this.add(txtPassword2, new AnchorConstraint(146, 17, 534, 184, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtPassword2.setPreferredSize(new java.awt.Dimension(229, 22));
			}
			{
				txtPassword = new JPasswordField();
				this.add(txtPassword, new AnchorConstraint(119, 17, 449, 184, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtPassword.setPreferredSize(new java.awt.Dimension(229, 22));
			}
			{
				txtLogin = new JTextField();
				this.add(txtLogin, new AnchorConstraint(92, 17, 363, 184, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtLogin.setPreferredSize(new java.awt.Dimension(229, 22));
			}
			{
				txtApellidos = new JTextField();
				this.add(txtApellidos, new AnchorConstraint(65, 17, 277, 184, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtApellidos.setPreferredSize(new java.awt.Dimension(229, 22));
			}
			{
				txtNombre = new JTextField();
				this.add(txtNombre, new AnchorConstraint(39, 17, 195, 184, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtNombre.setPreferredSize(new java.awt.Dimension(229, 22));
			}
			{
				txtNIF = new JTextField();
				this.add(txtNIF, new AnchorConstraint(12, 17, 109, 184, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtNIF.setPreferredSize(new java.awt.Dimension(229, 22));
			}
			{
				lblTipoUsuario = new JLabel();
				this.add(lblTipoUsuario, new AnchorConstraint(176, 246, 604, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblTipoUsuario.setText("Tipo de usuario");
				lblTipoUsuario.setPreferredSize(new java.awt.Dimension(172, 14));
			}
			{
				lblPassword2 = new JLabel();
				this.add(lblPassword2, new AnchorConstraint(149, 246, 519, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblPassword2.setText("Confirmar contraseña");
				lblPassword2.setPreferredSize(new java.awt.Dimension(172, 14));
			}
			{
				lblPassword = new JLabel();
				this.add(lblPassword, new AnchorConstraint(122, 246, 433, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblPassword.setText("Contraseña");
				lblPassword.setPreferredSize(new java.awt.Dimension(172, 14));
			}
			{
				lblLogin = new JLabel();
				this.add(lblLogin, new AnchorConstraint(95, 246, 347, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblLogin.setText("Usuario");
				lblLogin.setPreferredSize(new java.awt.Dimension(172, 14));
			}
			{
				lblApellidos = new JLabel();
				this.add(lblApellidos, new AnchorConstraint(68, 246, 261, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblApellidos.setText("Apellidos");
				lblApellidos.setPreferredSize(new java.awt.Dimension(172, 14));
			}
			{
				lblNombre = new JLabel();
				this.add(lblNombre, new AnchorConstraint(42, 246, 182, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblNombre.setText("Nombre");
				lblNombre.setPreferredSize(new java.awt.Dimension(172, 15));
			}
			{
				lblNIF = new JLabel();
				this.add(lblNIF, new AnchorConstraint(15, 246, 93, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblNIF.setText("NIF");
				lblNIF.setPreferredSize(new java.awt.Dimension(172, 14));
			}
			cambiarEstadoEspecialidad(false);
			cambiarEstadoConfiguracionCalendario(false);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void btnRestablecerActionPerformed(ActionEvent evt) {
		limpiarCamposRegistro();
	}
	
	private void btnCrearUsuarioActionPerformed(ActionEvent evt) {
		Usuario usu = null;
		TipoMedico tipo = null;
		boolean esMedico = false;		
		
		try {
			
			// Comprobamos todos los campos
			Validacion.comprobarNIF(txtNIF.getText());
			Validacion.comprobarNombre(txtNombre.getText());
			Validacion.comprobarApellidos(txtApellidos.getText());
			Validacion.comprobarContraseña(txtPassword.getText());	
			if (!txtPassword.getText().equals(txtPassword2.getText()))
					throw new ContraseñaIncorrectaException("Las contraseñas no coinciden");
			if (lstTipoUsuario.getSelectedIndex()==-1)
				throw new UsuarioNoSeleccionadoException();
			else if (lstTipoMedico.getSelectedIndex()==-1 && lstTipoUsuario.getSelectedValue().equals(USU_MEDICO))
				throw new UsuarioNoSeleccionadoException();
			if (lstTipoUsuario.getSelectedValue().equals(USU_MEDICO) && lstTipoMedico.getSelectedValue().equals(MED_ESPECIALISTA))
				Validacion.comprobarCadena(txtEspecialidad.getText());				

			// Creamos un nuevo usuario con los datos introducidos
			switch (RolesUsuarios.valueOf(lstTipoUsuario.getSelectedValue().toString())) {
				case Administrador:
					usu = new Administrador();
					break;
				case Citador:
					usu = new Citador();
					break;
				case Medico:
					esMedico = true;
					usu = new Medico();
					break;
			}
			usu.setDni(txtNIF.getText());
			usu.setNombre(txtNombre.getText());
			usu.setApellidos(txtApellidos.getText());
			usu.setLogin(txtLogin.getText());
			usu.setPassword(txtPassword.getText());
			
			if (esMedico) {
				if (lstTipoMedico.getSelectedValue().equals(MED_CABECERA))
					tipo = new Cabecera();
				else if (lstTipoMedico.getSelectedValue().equals(MED_PEDIATRA))
					tipo = new Pediatra();
				else if (lstTipoMedico.getSelectedValue().equals(MED_ESPECIALISTA))
					tipo = new Especialista(txtEspecialidad.getText());
				((Medico)usu).setTipoMedico(tipo);
				((Medico)usu).setCalendario(periodos);
				getControlador().crearMedico((Medico)usu);
			}
			else {
				getControlador().crearUsuario(usu);
			}
			// El usuario se ha creado correctamente
			Dialogos.mostrarDialogoInformacion(getFrame(), "Operación correcta", "El usuario ha sido dado de alta en el sistema.");
			limpiarCamposRegistro();
			
		} catch(UsuarioYaExistenteException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getMessage());	
		} catch(CentroSaludInexistenteException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getMessage());		
		} catch(SQLException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.toString());
		} catch(RemoteException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.toString());
		} catch(UsuarioNoSeleccionadoException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", "Debe seleccionar un tipo de usuario y, si es el caso, un tipo de médico");
		} catch(NIFIncorrectoException e) {
			txtNIF.selectAll();
			Dialogos.mostrarDialogoError(getFrame(), "Error", "El NIF debe ser el número de DNI (incluyendo el 0) y la letra sin guión.");
			txtNIF.grabFocus();
		} catch(NombreIncorrectoException e) {
			txtNombre.selectAll();
			Dialogos.mostrarDialogoError(getFrame(), "Error", "El nombre del usuario sólo puede contener letras y espacios.");
			txtNombre.grabFocus();
		} catch(ApellidoIncorrectoException e) {
			txtApellidos.selectAll();
			Dialogos.mostrarDialogoError(getFrame(), "Error", "Los apellidos del usuario sólo pueden contener letras y espacios.");
			txtApellidos.grabFocus();
		} catch(ContraseñaIncorrectaException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getMessage());
		} catch(Exception e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.toString());
		}
	}
	
	private void btnCalendarioActionPerformed(ActionEvent evt) {
		JDialog calendario = new JDCalendarioLaboral(this, periodos);
		((JDCalendarioLaboral)calendario).setModificable(true);
		calendario.setVisible(true);
	}


	private void crearModelos() {
		lstTipoUsuarioModel = new DefaultListModel();
		lstTipoUsuarioModel.addElement(USU_ADMINISTRADOR);
		lstTipoUsuarioModel.addElement(USU_MEDICO);
		lstTipoUsuarioModel.addElement(USU_CITADOR);
		lstTipoUsuario.setModel(lstTipoUsuarioModel);
		lstTipoMedicoModel = new DefaultListModel();
		lstTipoMedicoModel.addElement(MED_CABECERA);
		lstTipoMedicoModel.addElement(MED_PEDIATRA);
		lstTipoMedicoModel.addElement(MED_ESPECIALISTA);
		lstTipoMedico.setModel(lstTipoMedicoModel);
	}

	private void cambiarEstadoEspecialidad(boolean estado) {
		lblEspecialidad.setVisible(estado);
		txtEspecialidad.setVisible(estado);
		if (!estado)
			txtEspecialidad.setText("");
	}
	
	private void lstTipoUsuarioValueChanged(ListSelectionEvent evt) {
		cambiarEstadoEspecialidad(false);
		cambiarEstadoConfiguracionCalendario(false);
		if (lstTipoUsuario.getSelectedIndex()!=-1)
			if (lstTipoUsuario.getSelectedValue().equals(USU_MEDICO)) {
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
	}

	private void lstTipoMedicoValueChanged(ListSelectionEvent evt) {
		if (lstTipoUsuario.getSelectedIndex()!=-1)
			if (lstTipoMedico.getSelectedValue().equals(MED_ESPECIALISTA)) {
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
		txtPassword2.setText("");
		lstTipoUsuario.clearSelection();
		lstTipoMedico.clearSelection();
		lstTipoMedico.setVisible(false);
	}
	
	public void setPeriodos (Vector<PeriodoTrabajo> p) {
		periodos = new Vector<PeriodoTrabajo>();
		periodos.addAll(p);
	}
	
	// $hide<<$

}
