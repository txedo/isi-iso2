package presentacion;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.EventObject;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DebugGraphics;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;
import dominio.conocimiento.Administrador;
import dominio.conocimiento.Cabecera;
import dominio.conocimiento.CategoriasMedico;
import dominio.conocimiento.Citador;
import dominio.conocimiento.Especialidades;
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
import excepciones.Contrase�aIncorrectaException;
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
	private JComboBox cbEspecialidad;
	private JLabel lblNombre;
	private JLabel lblPasswordConf;
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
			this.setPreferredSize(new java.awt.Dimension(430, 390));
			{
				lblHorasSemanales = new JLabel();
				this.add(lblHorasSemanales, new AnchorConstraint(235, 973, 644, 268, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblHorasSemanales.setText("0 horas semanales");
				lblHorasSemanales.setPreferredSize(new java.awt.Dimension(150, 16));
			}
			{
				btnCalendario = new JButton();
				this.add(btnCalendario, new AnchorConstraint(231, 905, 650, 150, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				btnCalendario.setText("Configurar...");
				btnCalendario.setPreferredSize(new java.awt.Dimension(110, 23));
				btnCalendario.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnCalendarioActionPerformed(evt);
					}
				});
			}
			{
				lblCalendario = new JLabel();
				this.add(lblCalendario, new AnchorConstraint(235, 292, 675, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblCalendario.setText("Calendario laboral *");
				lblCalendario.setPreferredSize(new java.awt.Dimension(126, 14));
			}
			{
				lblEspecialidad = new JLabel();
				this.add(lblEspecialidad, new AnchorConstraint(262, 292, 680, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblEspecialidad.setText("Especialidad *");
				lblEspecialidad.setPreferredSize(new java.awt.Dimension(126, 16));
			}
			{
				cbEspecialidad = new JComboBox();
				this.add(cbEspecialidad, new AnchorConstraint(259, 18, 691, 184, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				cbEspecialidad.setPreferredSize(new java.awt.Dimension(229, 22));
			}
			{
				btnRestablecer = new JButton();
				this.add(btnRestablecer, new AnchorConstraint(309, 141, 957, 219, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
				btnRestablecer.setText("Restablecer");
				btnRestablecer.setPreferredSize(new java.awt.Dimension(120, 26));
				btnRestablecer.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnRestablecerActionPerformed(evt);
					}
				});
			}
			{
				btnCrearUsuario = new JButton();
				this.add(btnCrearUsuario, new AnchorConstraint(309, 12, 957, 603, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
				btnCrearUsuario.setText("Crear usuario");
				btnCrearUsuario.setPreferredSize(new java.awt.Dimension(120, 26));
				btnCrearUsuario.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnCrearUsuarioActionPerformed(evt);
					}
				});
			}
			{
				lstTipoMedico = new JList();
				this.add(lstTipoMedico, new AnchorConstraint(172, 961, 601, 268, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
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
				this.add(lstTipoUsuario, new AnchorConstraint(172, 684, 773, 150, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lstTipoUsuario.setPreferredSize(new java.awt.Dimension(110, 54));
				lstTipoUsuario.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				lstTipoUsuario.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
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
			}
			{
				txtPassword = new JPasswordField();
				this.add(txtPassword, new AnchorConstraint(119, 12, 449, 150, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtPassword.setPreferredSize(new java.awt.Dimension(268, 22));
			}
			{
				txtLogin = new JTextField();
				this.add(txtLogin, new AnchorConstraint(92, 12, 363, 150, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtLogin.setPreferredSize(new java.awt.Dimension(268, 22));
			}
			{
				txtApellidos = new JTextField();
				this.add(txtApellidos, new AnchorConstraint(65, 12, 277, 150, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtApellidos.setPreferredSize(new java.awt.Dimension(268, 22));
			}
			{
				txtNombre = new JTextField();
				this.add(txtNombre, new AnchorConstraint(39, 12, 195, 150, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtNombre.setPreferredSize(new java.awt.Dimension(268, 22));
			}
			{
				txtNIF = new JTextField();
				this.add(txtNIF, new AnchorConstraint(12, 12, 109, 150, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtNIF.setPreferredSize(new java.awt.Dimension(268, 22));
			}
			{
				lblTipoUsuario = new JLabel();
				this.add(lblTipoUsuario, new AnchorConstraint(176, 292, 604, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblTipoUsuario.setText("Tipo de usuario *");
				lblTipoUsuario.setPreferredSize(new java.awt.Dimension(126, 14));
			}
			{
				lblPasswordConf = new JLabel();
				this.add(lblPasswordConf, new AnchorConstraint(150, 288, 519, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblPasswordConf.setText("Confirmar contrase�a *");
				lblPasswordConf.setPreferredSize(new java.awt.Dimension(130, 14));
			}
			{
				lblPassword = new JLabel();
				this.add(lblPassword, new AnchorConstraint(123, 292, 433, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblPassword.setText("Contrase�a *");
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
				this.add(lblCamposOblig, new AnchorConstraint(284, 13, 900, 696, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
				lblCamposOblig.setText("* Campos obligatorios");
				lblCamposOblig.setPreferredSize(new java.awt.Dimension(129, 17));
				lblCamposOblig.setHorizontalAlignment(SwingConstants.TRAILING);
				lblCamposOblig.setFont(lblCamposOblig.getFont().deriveFont(10.0f));
			}
			cambiarEstadoEspecialidad(false);
			cambiarEstadoConfiguracionCalendario(false);
			rellenarModelo(new String [] {""});
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//$hide>>$
	
	private void rellenarModelo(String [] informacion) {
		ComboBoxModel cbEspecialidadModel = new DefaultComboBoxModel(informacion);
		cbEspecialidad.setModel(cbEspecialidadModel);	
	}
	
	private void btnRestablecerActionPerformed(ActionEvent evt) {
		limpiarCamposRegistro();
	}
	
	private void btnCrearUsuarioActionPerformed(ActionEvent evt) {
		Usuario usu = null;
		TipoMedico tipo = null;	
		
		try {
			
			// Comprobamos todos los campos
			Validacion.comprobarNIF(txtNIF.getText());
			Validacion.comprobarNombre(txtNombre.getText());
			Validacion.comprobarApellidos(txtApellidos.getText());
			Validacion.comprobarContrase�a(txtPassword.getText());	
			if (!txtPassword.getText().equals(txtPasswordConf.getText()))
					throw new Contrase�aIncorrectaException("Las contrase�as no coinciden");
			if (lstTipoUsuario.getSelectedIndex()==-1)
				throw new UsuarioNoSeleccionadoException();
			else if (lstTipoMedico.getSelectedIndex()==-1 && lstTipoUsuario.getSelectedValue().equals(RolesUsuarios.Medico.name()))
				throw new UsuarioNoSeleccionadoException();								

			// Creamos un nuevo usuario con los datos introducidos
			switch (RolesUsuarios.valueOf(lstTipoUsuario.getSelectedValue().toString())) {
				case Administrador:
					usu = new Administrador();
					break;
				case Citador:
					usu = new Citador();
					break;
				case Medico:
					usu = new Medico();
					break;
			}
			usu.setDni(txtNIF.getText());
			usu.setNombre(txtNombre.getText());
			usu.setApellidos(txtApellidos.getText());
			usu.setLogin(txtLogin.getText());
			usu.setPassword(txtPassword.getText());
			
			if (usu.getRol() == RolesUsuarios.Medico) {
				switch (CategoriasMedico.valueOf(lstTipoMedico.getSelectedValue().toString())) {
				case Cabecera:
					tipo = new Cabecera();
					break;
				case Pediatra:
					tipo = new Pediatra();
					break;
				case Especialista:
					tipo = new Especialista(cbEspecialidad.getSelectedItem().toString());
					break;
				}
				((Medico)usu).setTipoMedico(tipo);
				((Medico)usu).setCalendario(periodos);
				getControlador().crearMedico((Medico)usu);
			}
			else {
				getControlador().crearUsuario(usu);
			}
			// El usuario se ha creado correctamente
			Dialogos.mostrarDialogoInformacion(getFrame(), "Operaci�n correcta", "El usuario ha sido dado de alta en el sistema.");
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
			Dialogos.mostrarDialogoError(getFrame(), "Error", "Debe seleccionar un tipo de usuario y, si es el caso, un tipo de m�dico");
		} catch(NIFIncorrectoException e) {
			txtNIF.selectAll();
			Dialogos.mostrarDialogoError(getFrame(), "Error", "El NIF debe ser el n�mero de DNI (incluyendo el 0) y la letra sin gui�n.");
			txtNIF.grabFocus();
		} catch(NombreIncorrectoException e) {
			txtNombre.selectAll();
			Dialogos.mostrarDialogoError(getFrame(), "Error", "El nombre del usuario s�lo puede contener letras y espacios.");
			txtNombre.grabFocus();
		} catch(ApellidoIncorrectoException e) {
			txtApellidos.selectAll();
			Dialogos.mostrarDialogoError(getFrame(), "Error", "Los apellidos del usuario s�lo pueden contener letras y espacios.");
			txtApellidos.grabFocus();
		} catch(Contrase�aIncorrectaException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getMessage());
		} catch(Exception e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.toString());
		}
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
		// Eliminamos la ventana de configuraci�n
		frmCalendario.dispose();
	}

	private void crearModelos() {
		lstTipoUsuarioModel = new DefaultListModel();
		for(RolesUsuarios rol : RolesUsuarios.values()) {
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
		lblEspecialidad.setVisible(estado);
		cbEspecialidad.setVisible(estado);
		String [] valores = new String [Especialidades.values().length]; 
		if (!estado)
			rellenarModelo(new String [] {""});
		else {
			for (int i=0; i<Especialidades.values().length; i++) 
				valores[i] = Especialidades.values()[i].toString();
			rellenarModelo(valores);
		}
	}
	
	private void lstTipoUsuarioValueChanged(ListSelectionEvent evt) {
		cambiarEstadoEspecialidad(false);
		cambiarEstadoConfiguracionCalendario(false);
		if (lstTipoUsuario.getSelectedIndex()!=-1)
			if (lstTipoUsuario.getSelectedValue().equals(RolesUsuarios.Medico.name())) {
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
			if (lstTipoMedico.getSelectedValue().equals(CategoriasMedico.Especialista.name())) {
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
		lblHorasSemanales.setText("0 horas semanales");
		lstTipoUsuario.clearSelection();
		lstTipoMedico.clearSelection();
		lstTipoMedico.setVisible(false);
		periodos = new Vector<PeriodoTrabajo>();
	}
	
	//$hide<<$

}
