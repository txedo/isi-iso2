package presentacion;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.sql.SQLException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;

import dominio.conocimiento.Administrador;
import dominio.conocimiento.CentroSalud;
import dominio.conocimiento.Citador;
import dominio.conocimiento.Medico;
import dominio.conocimiento.Usuario;
import dominio.conocimiento.Roles;
import dominio.conocimiento.Utilidades;
import excepciones.ApellidoIncorrectoException;
import excepciones.CadenaVaciaException;
import excepciones.CorreoElectronicoIncorrectoException;
import excepciones.DomicilioIncorrectoException;
import excepciones.NIFIncorrectoException;
import excepciones.NSSIncorrectoException;
import excepciones.NombreIncorrectoException;
import excepciones.TelefonoFijoIncorrectoException;
import excepciones.TelefonoMovilIncorrectoException;
import excepciones.UsuarioInexistenteException;
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
public class JPUsuarioConsultar extends JPBase {

	private JLabel lblNIFUsuario;
	private JLabel lblLogin;
	private JLabel lblPass;
	private JTextField txtNombre;
	private JRadioButton radiobtnModificar;
	private ButtonGroup buttonGroup;
	private JTextField txtLogin;
	private JTextField txtApellidos;
	private JTextField txtDNI;
	private JButton btnBuscar;
	private JTextField txtPass;
	private JLabel lblApellidos;
	private JLabel lblNombre;
	private JButton btnAplicar;
	private JLabel lblRol;
	private JLabel lblCentro;
	private JTextField txtRol;
	private JTextField txtCentro;
	private JRadioButton radiobtnEliminar;
	
	private CentroSalud centro;

	public JPUsuarioConsultar() {
		super();
		initGUI();
	}
	
	private void initGUI() {
		try {
			AnchorLayout thisLayout = new AnchorLayout();
			this.setLayout(thisLayout);
			this.setSize(430, 390);
			this.setPreferredSize(new java.awt.Dimension(430, 390));
			{
				lblCentro = new JLabel();
				this.add(lblCentro, new AnchorConstraint(189, 309, 526, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblCentro.setText("Centro salud");
				lblCentro.setPreferredSize(new java.awt.Dimension(109, 16));
			}
			{
				lblRol = new JLabel();
				this.add(lblRol, new AnchorConstraint(160, 383, 452, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblRol.setText("Rol");
				lblRol.setPreferredSize(new java.awt.Dimension(35, 16));
			}
			{
				txtRol = new JTextField();
				this.add(txtRol, new AnchorConstraint(157, 83, 537, 100, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtRol.setPreferredSize(new java.awt.Dimension(247, 23));
			}
			{
				txtCentro = new JTextField();
				this.add(txtCentro, new AnchorConstraint(186, 83, 462, 100, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtCentro.setPreferredSize(new java.awt.Dimension(247, 23));
			}
			{
				radiobtnEliminar = new JRadioButton();
				this.add(radiobtnEliminar, new AnchorConstraint(260, 88, 534, 629, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
				radiobtnEliminar.setText("Eliminar");
				radiobtnEliminar.setPreferredSize(new java.awt.Dimension(72, 20));
			}
			{
				radiobtnModificar = new JRadioButton();
				this.add(radiobtnModificar, new AnchorConstraint(233, 88, 534, 629, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
				radiobtnModificar.setText("Modificar");
				radiobtnModificar.setPreferredSize(new java.awt.Dimension(72, 20));
			}
			{
				txtLogin = new JTextField();
				this.add(txtLogin, new AnchorConstraint(40, 83, 162, 100, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtLogin.setPreferredSize(new java.awt.Dimension(247, 23));
			}
			{
				txtApellidos = new JTextField();
				this.add(txtApellidos, new AnchorConstraint(128, 83, 388, 100, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtApellidos.setPreferredSize(new java.awt.Dimension(247, 23));
			}
			{
				txtNombre = new JTextField();
				this.add(txtNombre, new AnchorConstraint(99, 83, 314, 100, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtNombre.setPreferredSize(new java.awt.Dimension(247, 23));
			}
			{
				txtDNI = new JTextField();
				this.add(txtDNI, new AnchorConstraint(12, 83, 91, 100, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtDNI.setPreferredSize(new java.awt.Dimension(247, 23));
			}
			{
				btnBuscar = new JButton();
				this.add(btnBuscar, new AnchorConstraint(12, 11, 91, 829, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
				btnBuscar.setText("Buscar");
				btnBuscar.setPreferredSize(new java.awt.Dimension(66, 23));
				btnBuscar.setSize(66, 23);
				btnBuscar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnBuscarActionPerformed(evt);
					}
				});
			}
			{
				txtPass = new JTextField();
				this.add(txtPass, new AnchorConstraint(68, 83, 226, 100, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtPass.setPreferredSize(new java.awt.Dimension(247, 23));				
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
				this.add(lblPass, new AnchorConstraint(71, 361, 229, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblPass.setText("Password");
				lblPass.setPreferredSize(new java.awt.Dimension(57, 16));
			}
			{
				lblLogin = new JLabel();
				this.add(lblLogin, new AnchorConstraint(43, 367, 144, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblLogin.setText("Login");
				lblLogin.setPreferredSize(new java.awt.Dimension(51, 16));
			}
			{
				lblNIFUsuario = new JLabel();
				this.add(lblNIFUsuario, new AnchorConstraint(15, 368, 73, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblNIFUsuario.setText("NIF");
				lblNIFUsuario.setPreferredSize(new java.awt.Dimension(50, 16));
			}
			{
				buttonGroup = new ButtonGroup();
			}
			{
				btnAplicar = new JButton();
				btnAplicar.setText("Aplicar");
				btnAplicar.setPreferredSize(new java.awt.Dimension(66, 23));
				this.add(btnAplicar, new AnchorConstraint(250, 11, 534, 670, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
				btnAplicar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnAplicarActionPerformed(evt);
					}
				});
			}			
			
			buttonGroup.add(radiobtnEliminar);
			radiobtnEliminar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					radiobtnEliminarActionPerformed(evt);
				}
			});
			buttonGroup.add(radiobtnModificar);
			radiobtnModificar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					radiobtnModificarActionPerformed(evt);
				}
			});
			
			configurarFormularioConsultar(false);
			activarModificacion(false);
			txtCentro.setEditable(false);
			txtCentro.setFocusable(false);
			txtRol.setEditable(false);
			txtRol.setFocusable(false);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void btnBuscarActionPerformed(ActionEvent evt) {
		Usuario usuario = null;
					
		try {
			// Buscamos el usuario solicitado
			Utilidades.comprobarNIF(txtDNI.getText());
			usuario = (Usuario) getControlador().consultarUsuario(txtDNI.getText());

			// Mostramos los datos del usuario encontrado
			Dialogos.mostrarDialogoInformacion(getFrame(), "Resultados de la búsqueda", "Usuario encontrado.");
			txtLogin.setText(usuario.getLogin());
			txtPass.setText(usuario.getPassword());
			txtNombre.setText(usuario.getNombre());
			txtApellidos.setText(usuario.getApellidos());
			txtRol.setText(usuario.getRol().toString());
			centro = usuario.getCentroSalud();
			txtCentro.setText(centro.getNombre());
			
			activarModificacion(true);
			txtDNI.setEditable(false);
			txtDNI.setFocusable(false);
			
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
		txtRol.setText("");
		configurarFormularioConsultar(false);
		buttonGroup.clearSelection();
		activarModificacion(false);
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
		
	}
	
	private void activarModificacion(Boolean b) {
		radiobtnEliminar.setEnabled(b);
		radiobtnModificar.setEnabled(b);
		btnAplicar.setEnabled(b);
	}
	
	private void radiobtnModificarActionPerformed(ActionEvent evt) {
		configurarFormularioConsultar(true);
		txtLogin.setEditable(false);
		txtLogin.setFocusable(false);
		btnAplicar.setEnabled(true);
	}
	
	private void radiobtnEliminarActionPerformed(ActionEvent evt) {
		configurarFormularioConsultar(false);	
		btnAplicar.setEnabled(true);
	}
	
	private void btnAplicarActionPerformed(ActionEvent evt) {
		try {
			// Creamos el usuario con los datos (si son correctos), para modificarlo/eliminarlo
			Usuario usu = null;
			
			// Comprobamos los campos que pueden dar fallo
			Utilidades.comprobarNombre(txtNombre.getText());
			Utilidades.comprobarApellidos(txtApellidos.getText());
			
			switch(Roles.valueOf(txtRol.getText())) {
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
			usu.setDni(txtDNI.getText());
			usu.setLogin(txtLogin.getText());
			usu.setPassword(txtPass.getText());
			usu.setNombre(txtNombre.getText());
			usu.setApellidos(txtApellidos.getText());
			usu.setCentroSalud(centro);
			
			if (radiobtnModificar.isSelected()){
				
				getControlador().modificarUsuario(usu);
				Dialogos.mostrarDialogoInformacion(getFrame(), "Operación correcta", "El usuario ha sido modificado correctamente.");
				limpiarDatos();
				configurarFormularioConsultar(false);
				txtDNI.setEditable(true);
				txtDNI.setFocusable(true);
			}
			else if (radiobtnEliminar.isSelected()){
				int response = JOptionPane.showConfirmDialog(this, "¿Seguro que desea eliminar este usuario del sistema?", "Confirmar", JOptionPane.YES_NO_OPTION);
				if (response == JOptionPane.YES_OPTION) {
					getControlador().eliminarUsuario(usu);
					Dialogos.mostrarDialogoInformacion(getFrame(), "Operación correcta", "El usuario ha sido eliminado correctamente.");
					limpiarDatos();
					configurarFormularioConsultar(false);
					txtDNI.setEditable(true);
					txtDNI.setFocusable(true);
				}
			}
		} catch(NombreIncorrectoException e) {
			txtNombre.selectAll();
			Dialogos.mostrarDialogoError(getFrame(), "Error", "El nombre del usuario sólo puede contener letras y espacios.");
			txtNombre.grabFocus();
		} catch(ApellidoIncorrectoException e) {
			txtApellidos.selectAll();
			Dialogos.mostrarDialogoError(getFrame(), "Error", "Los apellidos del usuario sólo pueden contener letras y espacios.");
			txtApellidos.grabFocus();
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.toString());
		} catch(RemoteException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.toString());
			
		} catch(Exception e) {
			e.printStackTrace();
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.toString());
		}
	}

}
