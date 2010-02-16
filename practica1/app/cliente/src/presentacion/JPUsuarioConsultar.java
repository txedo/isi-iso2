package presentacion;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.EventObject;
import java.util.Vector;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.EventListenerList;
import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;
import dominio.conocimiento.Administrador;
import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.CentroSalud;
import dominio.conocimiento.Cita;
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
/**
 * Panel que permite consultar, modificar y eliminar usuarios existentes. 
 */
public class JPUsuarioConsultar extends JPBase {

	private static final long serialVersionUID = 2736737327573021315L;
	
	private final String TIPO_ADMINISTRADOR = "Administrador";
	private final String TIPO_MEDICO = "Médico";
	private final String TIPO_CITADOR = "Citador";

	private EventListenerList listenerList;
	
	private JFCalendarioLaboral calendario;

	private CentroSalud centro;
	private Vector<PeriodoTrabajo> periodos;
	private Usuario usuario;

	private JLabel lblNIFBuscado;
	private JLabel lblNIF;
	private JLabel lblLogin;
	private JLabel lblPassword;
	private JTextField txtNombre;
	private JTextField txtLogin;
	private JTextField txtApellidos;
	private JTextField txtNIFBuscado;
	private JButton btnBuscar;
	private JTextField txtPassword;
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

	public JPUsuarioConsultar() {
		this(null, null);
		// Este constructor evita que aparezca un error al editar con
		// el Jigloo los formularios que utilizan JPUsuarioConsultar
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
			this.setPreferredSize(new java.awt.Dimension(430, 390));
			{
				btnCalendario = new JButton();
				this.add(btnCalendario, new AnchorConstraint(270, 508, 601, 116, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				btnCalendario.setText("Ver...");
				btnCalendario.setPreferredSize(new java.awt.Dimension(110, 23));
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
				this.add(lblCalendario, new AnchorConstraint(274, 309, 591, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblCalendario.setText("Calendario laboral");
				lblCalendario.setPreferredSize(new java.awt.Dimension(111, 14));
			}
			{
				cmbRol = new JComboBox();
				this.add(cmbRol, new AnchorConstraint(213, 12, 462, 116, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				cmbRol.setPreferredSize(new java.awt.Dimension(302, 23));
				cmbRol.setEnabled(false);
				cmbRol.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent evt) {
						cbRolesItemStateChanged(evt);
					}
				});
				rellenarModelo(new String [] {""});
			}
			{
				btnEliminar = new JButton();
				this.add(btnEliminar, new AnchorConstraint(329, 161, 673, 9, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				btnEliminar.setText("Eliminar usuario");
				btnEliminar.setPreferredSize(new java.awt.Dimension(120, 26));
				btnEliminar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnEliminarActionPerformed(evt);
					}
				});
			}
			{
				lblCentro = new JLabel();
				this.add(lblCentro, new AnchorConstraint(244, 321, 526, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblCentro.setText("Centro asignado");
				lblCentro.setPreferredSize(new java.awt.Dimension(99, 16));
			}
			{
				lblRol = new JLabel();
				this.add(lblRol, new AnchorConstraint(216, 321, 452, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblRol.setText("Rol asignado");
				lblRol.setPreferredSize(new java.awt.Dimension(99, 16));
			}
			{
				txtCentro = new JTextField();
				this.add(txtCentro, new AnchorConstraint(241, 12, 462, 116, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtCentro.setPreferredSize(new java.awt.Dimension(302, 23));
				txtCentro.setEditable(false);
			}
			{
				txtLogin = new JTextField();
				this.add(txtLogin, new AnchorConstraint(157, 12, 314, 116, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtLogin.setPreferredSize(new java.awt.Dimension(302, 23));
				txtLogin.setEditable(false);
			}
			{
				txtApellidos = new JTextField();
				this.add(txtApellidos, new AnchorConstraint(129, 12, 226, 116, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtApellidos.setPreferredSize(new java.awt.Dimension(302, 23));				
				txtApellidos.setEditable(false);
			}
			{
				txtNombre = new JTextField();
				this.add(txtNombre, new AnchorConstraint(101, 12, 162, 116, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtNombre.setPreferredSize(new java.awt.Dimension(302, 23));
				txtNombre.setEditable(false);
			}
			{
				txtNIFBuscado = new JTextField();
				this.add(txtNIFBuscado, new AnchorConstraint(36, 83, 91, 116, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtNIFBuscado.setPreferredSize(new java.awt.Dimension(231, 23));
			}
			{
				btnBuscar = new JButton();
				this.add(btnBuscar, new AnchorConstraint(36, 11, 91, 829, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
				btnBuscar.setText("Buscar");
				btnBuscar.setPreferredSize(new java.awt.Dimension(66, 23));
				btnBuscar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnBuscarActionPerformed(evt);
					}
				});
			}
			{
				txtPassword = new JTextField();
				this.add(txtPassword, new AnchorConstraint(185, 12, 388, 116, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtPassword.setPreferredSize(new java.awt.Dimension(302, 23));
				txtPassword.setEditable(false);
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
				lblPassword = new JLabel();
				this.add(lblPassword, new AnchorConstraint(188, 156, 360, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblPassword.setText("Contraseña *");
				lblPassword.setPreferredSize(new java.awt.Dimension(99, 16));
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
				this.add(btnGuardar, new AnchorConstraint(327, 12, 534, 670, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
				btnGuardar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnGuardarActionPerformed(evt);
					}
				});
			}
			{
				chkEditar = new JCheckBox();
				this.add(chkEditar, new AnchorConstraint(330, 142, 665, 419, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
				chkEditar.setText("Habilitar edición");
				chkEditar.setEnabled(false);
				chkEditar.setPreferredSize(new java.awt.Dimension(106, 19));
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
				this.add(lblCamposOblig, new AnchorConstraint(303, 13, 875, 673, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
				lblCamposOblig.setText("* Campos obligatorios");
				lblCamposOblig.setHorizontalAlignment(SwingConstants.TRAILING);
				lblCamposOblig.setPreferredSize(new java.awt.Dimension(129, 17));
				lblCamposOblig.setFont(lblCamposOblig.getFont().deriveFont(10.0f));
			}
			{
				txtNIF = new JTextField();
				this.add(txtNIF, new AnchorConstraint(73, 12, 219, 116, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtNIF.setEditable(false);
				txtNIF.setPreferredSize(new java.awt.Dimension(302, 23));
			}
			{
				lblNIF = new JLabel();
				this.add(lblNIF, new AnchorConstraint(76, 254, 237, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblNIF.setText("NIF *");
				lblNIF.setPreferredSize(new java.awt.Dimension(99, 16));
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}	
	//$hide>>$
	
	public Usuario getUsuario() {
		return usuario;
	}
	
	private void rellenarModelo(String [] informacion) {
		ComboBoxModel cbRolesModel = new DefaultComboBoxModel(informacion);
		cmbRol.setModel(cbRolesModel);	
	}
	
	private void btnBuscarActionPerformed(ActionEvent evt) {
		Object[] listeners;
		int i;

		// Borramos la información del antiguo usuario consultado
		limpiarCamposConsulta();
		cambiarEdicion(false);
					
		try {
			
			// Buscamos el usuario solicitado
			Validacion.comprobarNIF(txtNIFBuscado.getText());
			usuario = (Usuario) getControlador().consultarUsuario(txtNIFBuscado.getText());

			// Mostramos los datos del usuario encontrado
			Dialogos.mostrarDialogoInformacion(getFrame(), "Resultados de la búsqueda", "Usuario encontrado.");
			rellenarModelo(new String [] {TIPO_ADMINISTRADOR, TIPO_CITADOR, TIPO_MEDICO});
			txtNIFBuscado.setText("");
			txtNIF.setText(usuario.getDni());
			txtLogin.setText(usuario.getLogin());
			txtPassword.setText(usuario.getPassword());
			txtNombre.setText(usuario.getNombre());
			txtApellidos.setText(usuario.getApellidos());
			cmbRol.setSelectedIndex(usuario.getRol().ordinal());
			centro = usuario.getCentroSalud();
			txtCentro.setText(centro.getNombre());
			if (usuario.getRol().equals(RolesUsuarios.Medico))
				periodos = ((Medico)usuario).getCalendario();
			chkEditar.setEnabled(true);
			
		} catch(UsuarioInexistenteException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getMessage());
			limpiarCamposConsulta();
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
	
	private void btnGuardarActionPerformed(ActionEvent evt) {
		Usuario usuarioMod = null;
		
		try {			
			// Comprobamos los campos que pueden dar fallo
			Validacion.comprobarNombre(txtNombre.getText());
			Validacion.comprobarApellidos(txtApellidos.getText());
			
			switch(RolesUsuarios.values()[cmbRol.getSelectedIndex()]) {
				case Administrador:
					usuarioMod = new Administrador();
					break;
				case Citador:
					usuarioMod = new Citador();
					break;
				case Medico:
					usuarioMod = new Medico();
					break;
			}
			usuarioMod.setDni(txtNIF.getText());
			usuarioMod.setLogin(txtLogin.getText());
			usuarioMod.setPassword(txtPassword.getText());
			usuarioMod.setNombre(txtNombre.getText());
			usuarioMod.setApellidos(txtApellidos.getText());
			// Dejamos el mismo centro de salud
			usuarioMod.setCentroSalud(centro);
			
			// Modificamos el usuario
			if (usuarioMod.getRol().equals(RolesUsuarios.Medico)) {
				// Cambiamos el calendario
				((Medico)usuarioMod).setCalendario(periodos);
				// Ponemos su tipo de médico
				((Medico)usuarioMod).setTipoMedico(((Medico)usuario).getTipoMedico());
				getControlador().modificarMedico((Medico)usuarioMod);				
			}
			else
				getControlador().modificarUsuario(usuarioMod);
			
			Dialogos.mostrarDialogoInformacion(getFrame(), "Operación correcta", "El usuario ha sido modificado correctamente.");
			limpiarCamposConsulta();
			cambiarEdicion(false);
			
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
		Vector<Beneficiario> beneficiarios;
		Vector<Cita> citas;
		String mensaje = "";
		boolean respuesta = Dialogos.mostrarDialogoPregunta(getFrame(), "Pregunta", "¿Seguro que desea eliminar este usuario del sistema?");
		
		if (respuesta) {
			try {
				if (usuario.getRol().equals(RolesUsuarios.Medico)) {
					beneficiarios = getControlador().obtenerBeneficiariosMedico(usuario.getDni());
					citas = getControlador().consultarCitasMedico(usuario.getDni());
					// TODO: Falta hacer lo mismo para las sustituciones
					if (beneficiarios.size()!=0)
						mensaje = "El médico que quiere borrar tiene beneficiarios asignados. \n";
	
					if (citas.size()!=0)
						mensaje += "El médico que quiere borrar tiene citas pendientes. \n";
					
					mensaje += "¿Seguro que quiere continuar con la eliminación?";
					respuesta = Dialogos.mostrarDialogoPregunta(getFrame(), "Pregunta", mensaje);
					if (respuesta) {
						// Eliminamos las citas de ese médico
						for (Cita c: citas)
						 	getControlador().anularCita(c);
						// TODO: cancelar las sustituciones
						Dialogos.mostrarDialogoInformacion(getFrame(), "Informacion", "Se va a asignar un nuevo médico a los beneficiarios afectados");
						// Asignamos un nuevo médico a los beneficiarios
						for (Beneficiario b: beneficiarios)
							getControlador().asignarMedicoBeneficiario(b);
						Dialogos.mostrarDialogoInformacion(getFrame(), "Informacion", "Beneficiarios actualizados");
						// Eliminamos el médico al final, para evitar problemas con claves ajenas nulas
						getControlador().eliminarMedico((Medico)usuario);
						Dialogos.mostrarDialogoInformacion(getFrame(), "Operación correcta", "El usuario ha sido eliminado correctamente.");
						limpiarCamposConsulta();
						cambiarEdicion(false);
					}
				}
				else {
					getControlador().eliminarUsuario(usuario);
					Dialogos.mostrarDialogoInformacion(getFrame(), "Operación correcta", "El usuario ha sido eliminado correctamente.");
					limpiarCamposConsulta();
					cambiarEdicion(false);
				}
			} catch(SQLException e) {
				Dialogos.mostrarDialogoError(getFrame(), "Error", e.toString());
			} catch(RemoteException e) {
				Dialogos.mostrarDialogoError(getFrame(), "Error", e.toString());		
			} catch(Exception e) {
				Dialogos.mostrarDialogoError(getFrame(), "Error", e.toString());
			}
		}
	}
	
	private void chkEditarActionPerformed(ActionEvent evt) {
		cambiarEdicion(chkEditar.isSelected());
	}
	
	public void addUsuarioBuscadoListener(UsuarioBuscadoListener listener) {
		listenerList.add(UsuarioBuscadoListener.class, listener);
	}

	public void removeUsuarioBuscadoListener(UsuarioBuscadoListener listener) {
		listenerList.remove(UsuarioBuscadoListener.class, listener);
	}
	
	private void btnCalendarioActionPerformed(ActionEvent evt) {
		// Creamos la ventana de consulta/edición de calendarios
		calendario = new JFCalendarioLaboral();
		calendario.addVentanaCerradaListener(new VentanaCerradaListener() {
			public void ventanaCerrada(EventObject evt) {    
				calendarioVentanaCerrada(evt);
			}
		});
		// Desactivamos la ventana hasta cerrar la ventana de calendarios
		getFrame().setEnabled(false);
		calendario.setPeriodosTrabajo(periodos);
		calendario.setModificable(chkEditar.isSelected());
		calendario.setLocationRelativeTo(this);		
		calendario.setVisible(true);
	}
	
	private void calendarioVentanaCerrada(EventObject evt) {
		// Reactivamos la ventana 
		getFrame().setEnabled(true);
		calendario.setVisible(false);
		periodos = new Vector<PeriodoTrabajo>();
		periodos.addAll(calendario.getPeriodosTrabajo());
		// Eliminamos la ventana de configuración
		calendario.dispose();
	}
	
	private void cbRolesItemStateChanged(ItemEvent evt) {
		if (evt.getStateChange() == ItemEvent.SELECTED) {
			if (evt.getItem().equals(TIPO_MEDICO)) {
				btnCalendario.setEnabled(true);
			}
			else {
				btnCalendario.setEnabled(false);
				periodos = new Vector<PeriodoTrabajo>();
			}
		}
	}

	private void cambiarEdicion(boolean activar) {
		txtNombre.setEditable(activar);
		txtApellidos.setEditable(activar);
		txtLogin.setEditable(activar);
		txtPassword.setEditable(activar);
		cmbRol.setEnabled(activar);
		btnGuardar.setEnabled(activar);
		btnEliminar.setEnabled(activar);
		lblCamposOblig.setVisible(activar);
		if(activar) {
			lblNIF.setText("NIF *");
			lblNombre.setText("Nombre *");
			lblApellidos.setText("Apellidos *");
			lblLogin.setText("Usuario *");
			lblPassword.setText("Contraseña *");
			lblRol.setText("Rol asignado *");
			lblCalendario.setText("Calendario laboral *");
			btnCalendario.setText("Configurar...");
		} else {
			lblNIF.setText("NIF");
			lblNombre.setText("Nombre");
			lblApellidos.setText("Apellidos");
			lblLogin.setText("Usuario");
			lblPassword.setText("Contraseña");
			lblRol.setText("Rol asignado");
			lblCalendario.setText("Calendario laboral");
			btnCalendario.setText("Ver...");
		}
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

	public void limpiarCamposConsulta() {
		usuario = null;
		txtNIF.setText("");
		txtLogin.setText("");
		txtPassword.setText("");
		txtNombre.setText("");
		txtApellidos.setText("");
		txtCentro.setText("");
		chkEditar.setSelected(false);
		chkEditar.setEnabled(false);
		rellenarModelo(new String [] {""});
		cambiarEdicion(false);
	}

	//$hide<<$
	
}
