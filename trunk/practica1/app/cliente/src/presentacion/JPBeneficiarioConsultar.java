package presentacion;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;
import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.Medico;
import dominio.conocimiento.Operaciones;
import dominio.conocimiento.Utilidades;
import excepciones.ApellidoIncorrectoException;
import excepciones.BeneficiarioInexistenteException;
import excepciones.CadenaVaciaException;
import excepciones.CorreoElectronicoIncorrectoException;
import excepciones.DomicilioIncorrectoException;
import excepciones.MedicoInexistenteException;
import excepciones.NIFIncorrectoException;
import excepciones.NSSIncorrectoException;
import excepciones.NombreIncorrectoException;
import excepciones.TelefonoFijoIncorrectoException;
import excepciones.TelefonoMovilIncorrectoException;


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
 * Panel que permite consultar beneficiarios existentes.
 */
public class JPBeneficiarioConsultar extends JPBase {

	private static final long serialVersionUID = 2053055425639245150L;
	
	private final String ID_NIF = "NIF";
	private final String ID_NSS = "NSS";
	
	private ComboBoxModel cmbIdentificacionModel;
	private JLabel lblMedicoAsignado;
	private JTextField txtMedicoAsignado;
	private JLabel lblTelefonoMovil;
	private JTextField txtFechaNacimiento;
	private JLabel lblFechaNacimiento;
	private JLabel lblTelefonoFijo;
	private JLabel lblCorreoElectronico;
	private JLabel lblDomicilio;
	private JLabel lblApellidos;
	private JLabel lblNombre;
	private JLabel lblNSS;
	private JLabel lblNIF;
	private JTextField txtTelefonoMovil;
	private JTextField txtTelefonoFijo;
	private JButton btnAplicar;
	private JCheckBox chkEditar;
	private JTextField txtCorreoElectronico;
	private JTextField txtDomicilio;
	private JTextField txtApellidos;
	private JTextField txtNombre;
	private JTextField txtNSS;
	private JTextField txtNIF;
	private JTextField txtIdentificacion;
	private JButton btnBuscar;
	private JLabel lblBuscar;
	private JComboBox cmbIdentificacion;

	public JPBeneficiarioConsultar() {
		super();
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
				txtFechaNacimiento = new JTextField();
				this.add(txtFechaNacimiento, new AnchorConstraint(174, 83, 521, 138, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtFechaNacimiento.setPreferredSize(new java.awt.Dimension(209, 23));
				txtFechaNacimiento.setEditable(false);
			}
			{
				lblFechaNacimiento = new JLabel();
				this.add(lblFechaNacimiento, new AnchorConstraint(177, 304, 493, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblFechaNacimiento.setText("Fecha de nacimiento");
				lblFechaNacimiento.setPreferredSize(new java.awt.Dimension(116, 16));
			}
			{
				btnAplicar = new JButton();
				this.add(btnAplicar, new AnchorConstraint(278, 11, 963, 794, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
				btnAplicar.setText("Aplicar");
				btnAplicar.setPreferredSize(new java.awt.Dimension(66, 23));
				btnAplicar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnAplicarActionPerformed(evt);
					}
				});
				btnAplicar.setEnabled(false);
			}
			{
				chkEditar = new JCheckBox();
				this.add(chkEditar, new AnchorConstraint(255, -18, 854, 788, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
				chkEditar.setText("¿Editar?");
				chkEditar.setPreferredSize(new java.awt.Dimension(95, 14));
				chkEditar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						chkEditarActionPerformed(evt);
					}
				});
				chkEditar.setEnabled(false);
			}
			{
				lblMedicoAsignado = new JLabel();
				this.add(lblMedicoAsignado, new AnchorConstraint(314, 311, 950, 9, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblMedicoAsignado.setText("Medico asignado");
				lblMedicoAsignado.setPreferredSize(new java.awt.Dimension(110, 18));
			}
			{
				lblTelefonoMovil = new JLabel();
				this.add(lblTelefonoMovil, new AnchorConstraint(289, 311, 861, 9, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblTelefonoMovil.setText("Teléfono móvil");
				lblTelefonoMovil.setPreferredSize(new java.awt.Dimension(110, 15));
			}
			{
				lblTelefonoFijo = new JLabel();
				this.add(lblTelefonoFijo, new AnchorConstraint(262, 311, 776, 9, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblTelefonoFijo.setText("Teléfono fijo");
				lblTelefonoFijo.setPreferredSize(new java.awt.Dimension(110, 15));
			}
			{
				lblCorreoElectronico = new JLabel();
				this.add(lblCorreoElectronico, new AnchorConstraint(235, 313, 690, 9, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblCorreoElectronico.setText("Correo electrónico");
				lblCorreoElectronico.setPreferredSize(new java.awt.Dimension(108, 15));
			}
			{
				lblDomicilio = new JLabel();
				this.add(lblDomicilio, new AnchorConstraint(208, 311, 604, 9, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblDomicilio.setText("Domicilio");
				lblDomicilio.setPreferredSize(new java.awt.Dimension(110, 15));
			}
			{
				lblApellidos = new JLabel();
				this.add(lblApellidos, new AnchorConstraint(149, 310, 522, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblApellidos.setText("Apellidos");
				lblApellidos.setPreferredSize(new java.awt.Dimension(110, 15));
			}
			{
				lblNombre = new JLabel();
				this.add(lblNombre, new AnchorConstraint(121, 310, 433, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblNombre.setText("Nombre");
				lblNombre.setPreferredSize(new java.awt.Dimension(110, 15));
			}
			{
				lblNSS = new JLabel();
				this.add(lblNSS, new AnchorConstraint(93, 310, 344, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblNSS.setText("NSS");
				lblNSS.setPreferredSize(new java.awt.Dimension(110, 15));
			}
			{
				lblNIF = new JLabel();
				this.add(lblNIF, new AnchorConstraint(67, 310, 261, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblNIF.setText("NIF");
				lblNIF.setPreferredSize(new java.awt.Dimension(110, 15));
			}
			{
				txtMedicoAsignado = new JTextField();
				this.add(txtMedicoAsignado, new AnchorConstraint(312, 83, 953, 137, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtMedicoAsignado.setEditable(false);
				txtMedicoAsignado.setFocusable(false);
				txtMedicoAsignado.setPreferredSize(new java.awt.Dimension(210, 22));
			}
			{
				txtTelefonoMovil = new JTextField();
				this.add(txtTelefonoMovil, new AnchorConstraint(286, 84, 874, 137, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtTelefonoMovil.setPreferredSize(new java.awt.Dimension(209, 23));
				txtTelefonoMovil.setFocusable(false);
				txtTelefonoMovil.setEditable(false);
			}
			{
				txtTelefonoFijo = new JTextField();
				this.add(txtTelefonoFijo, new AnchorConstraint(259, 84, 788, 137, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtTelefonoFijo.setPreferredSize(new java.awt.Dimension(209, 23));
				txtTelefonoFijo.setFocusable(false);
				txtTelefonoFijo.setEditable(false);
			}
			{
				txtCorreoElectronico = new JTextField();
				this.add(txtCorreoElectronico, new AnchorConstraint(231, 84, 700, 138, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtCorreoElectronico.setPreferredSize(new java.awt.Dimension(208, 23));
				txtCorreoElectronico.setFocusable(false);
				txtCorreoElectronico.setEditable(false);
			}
			{
				txtDomicilio = new JTextField();
				this.add(txtDomicilio, new AnchorConstraint(202, 84, 617, 138, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtDomicilio.setPreferredSize(new java.awt.Dimension(208, 23));
				txtDomicilio.setFocusable(false);
				txtDomicilio.setEditable(false);
			}
			{
				txtApellidos = new JTextField();
				this.add(txtApellidos, new AnchorConstraint(145, 83, 534, 138, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtApellidos.setPreferredSize(new java.awt.Dimension(209, 23));
				txtApellidos.setFocusable(false);
				txtApellidos.setEditable(false);
			}
			{
				txtNombre = new JTextField();
				this.add(txtNombre, new AnchorConstraint(117, 83, 442, 138, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtNombre.setPreferredSize(new java.awt.Dimension(209, 23));
				txtNombre.setFocusable(false);
				txtNombre.setEditable(false);
			}
			{
				txtNSS = new JTextField();
				this.add(txtNSS, new AnchorConstraint(89, 83, 357, 138, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtNSS.setPreferredSize(new java.awt.Dimension(209, 23));
				txtNSS.setFocusable(false);
				txtNSS.setEditable(false);
			}
			{
				txtNIF = new JTextField();
				this.add(txtNIF, new AnchorConstraint(63, 83, 271, 138, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtNIF.setPreferredSize(new java.awt.Dimension(209, 23));
				txtNIF.setFocusable(false);
				txtNIF.setEditable(false);
			}
			{
				txtIdentificacion = new JTextField();
				this.add(txtIdentificacion, new AnchorConstraint(36, 83, 188, 138, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtIdentificacion.setPreferredSize(new java.awt.Dimension(209, 23));
				txtIdentificacion.setDragEnabled(true);
			}
			{
				btnBuscar = new JButton();
				this.add(btnBuscar, new AnchorConstraint(36, 11, 152, 847, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
				btnBuscar.setDefaultCapable(true);
				//jPanelConsultar.getRootPane().setDefaultButton(btnBuscar);
				btnBuscar.setText("Buscar");
				btnBuscar.setPreferredSize(new java.awt.Dimension(66, 23));
				btnBuscar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnBuscarActionPerformed(evt);
					}
				});
			}
			{
				lblBuscar = new JLabel();
				this.add(lblBuscar, new AnchorConstraint(14, 264, 90, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblBuscar.setText("Buscar beneficiario por:");
				lblBuscar.setPreferredSize(new java.awt.Dimension(156, 14));
			}
			{
				cmbIdentificacion = new JComboBox();
				this.add(cmbIdentificacion, new AnchorConstraint(37, 236, 188, 9, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				cmbIdentificacion.setPreferredSize(new java.awt.Dimension(100, 22));
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//$hide>>$
	
	private void crearModelos() {
		cmbIdentificacionModel = new DefaultComboBoxModel(new String[] { ID_NIF, ID_NSS });
		cmbIdentificacion.setModel(cmbIdentificacionModel);
	}
	
	public void setDesactivarModificacion(){
		chkEditar.setVisible(false);
		btnAplicar.setVisible(false);
	}
	
	private void btnBuscarActionPerformed(ActionEvent evt) {
		Beneficiario beneficiario = null;
		String sIdentificacion;
		String sTipo;
					
		try {

			// Obtenemos el identificador para buscar el beneficiario
			sIdentificacion = txtIdentificacion.getText().toUpperCase();
			sTipo = (String)cmbIdentificacion.getSelectedItem();
			if(sIdentificacion.equals("")) {
				throw new CadenaVaciaException();
			}

			// Buscamos el beneficiario solicitado
			if(sTipo.equals(ID_NIF)) {
				Utilidades.comprobarNIF(sIdentificacion);
				beneficiario = getControlador().getBeneficiario(sIdentificacion);
			} else if(sTipo.equals(ID_NSS)) {
				Utilidades.comprobarNSS(sIdentificacion);
				beneficiario = getControlador().getBeneficiarioPorNSS(sIdentificacion);
			}

			// Mostramos los datos del beneficiario encontrado
			Dialogos.mostrarDialogoInformacion(getFrame(), "Resultados de la búsqueda", "Beneficiario encontrado.");
			txtIdentificacion.setText("");
			txtNIF.setText(beneficiario.getNif());
			txtNSS.setText(beneficiario.getNss());
			txtNombre.setText(beneficiario.getNombre());
			txtApellidos.setText(beneficiario.getApellidos());
			txtFechaNacimiento.setText(new SimpleDateFormat("dd/MM/yyyy").format(beneficiario.getFechaNacimiento()));
			txtDomicilio.setText(beneficiario.getDomicilio());
			txtCorreoElectronico.setText(beneficiario.getCorreo());
			txtTelefonoFijo.setText(Integer.toString(beneficiario.getTelefono()));
			txtTelefonoMovil.setText(Integer.toString(beneficiario.getMovil()));
			txtMedicoAsignado.setText(beneficiario.getMedicoAsignado().getDni());
			chkEditar.setEnabled(true);

		} catch(SQLException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.toString());
		} catch(RemoteException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.toString());
		} catch(BeneficiarioInexistenteException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", "El beneficiario no se encuentra dado de alta en el sistema.");
			txtIdentificacion.selectAll();
			txtIdentificacion.grabFocus();			
		} catch(CadenaVaciaException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", "Debe introducir un NIF o NSS.");
			txtIdentificacion.grabFocus();
		} catch(NIFIncorrectoException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", "Debe introducir NIF válido.");
			txtIdentificacion.selectAll();
			txtIdentificacion.grabFocus();
		} catch(NSSIncorrectoException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", "Debe introducir un NSS válido.");
			txtIdentificacion.selectAll();
			txtIdentificacion.grabFocus();
			
		} catch(Exception e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.toString());
		}
	}
	
	private void btnAplicarActionPerformed(ActionEvent evt) {
		Beneficiario beneficiario = null;
		Medico medico = null;
		
		try {
			
			// Comprobamos todos los campos
			Utilidades.comprobarNIF(txtNIF.getText());
			Utilidades.comprobarNSS(txtNSS.getText());
			Utilidades.comprobarNombre(txtNombre.getText());
			Utilidades.comprobarApellidos(txtApellidos.getText());
			Utilidades.comprobarDomicilio(txtDomicilio.getText());			
			Utilidades.comprobarCorreoElectronico(txtCorreoElectronico.getText());
			Utilidades.comprobarTelefonoFijo(txtTelefonoFijo.getText());
			Utilidades.comprobarTelefonoMovil(txtTelefonoMovil.getText());
			
			// Creamos un beneficiario con los datos introducidos
			beneficiario = new Beneficiario();
			beneficiario.setNif(txtNIF.getText().toUpperCase());
			beneficiario.setNss(txtNSS.getText());
			beneficiario.setNombre(txtNombre.getText());
			beneficiario.setApellidos(txtApellidos.getText());
			beneficiario.setFechaNacimiento(new Date(Integer.parseInt(txtFechaNacimiento.getText().substring(6, 9)), Integer.parseInt(txtFechaNacimiento.getText().substring(3, 4)), Integer.parseInt(txtFechaNacimiento.getText().substring(0, 1)))); 
			beneficiario.setDomicilio(txtDomicilio.getText());
			beneficiario.setCorreo(txtCorreoElectronico.getText());
			beneficiario.setTelefono(Integer.parseInt(txtTelefonoFijo.getText()));
			beneficiario.setMovil(Integer.parseInt(txtTelefonoMovil.getText()));
			medico = getControlador().consultarMedico(txtMedicoAsignado.getText());
			beneficiario.setMedicoAsignado(medico);
			
			// Solicitamos al servidor que se modifique el beneficiario
			getControlador().modificarBeneficiario(beneficiario);
			
			// El beneficiario se ha modificado correctamente
			Dialogos.mostrarDialogoInformacion(getFrame(), "Operación correcta", "El beneficiario ha sido modificado correctamente.");
			limpiarCamposConsultar();
			configurarFormularioConsultar(false);
			
		} catch(SQLException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.toString());
		} catch(RemoteException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.toString());
		} catch(BeneficiarioInexistenteException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", "El beneficiario no se encuentra dado de alta en el sistema.");
		} catch(MedicoInexistenteException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", "El médico del beneficiario no se encuentra dado de alta en el sistema.");

		} catch(NIFIncorrectoException e) {
			txtNIF.selectAll();
			Dialogos.mostrarDialogoError(getFrame(), "Error", "El NIF debe ser el número de DNI (incluyendo el 0) y la letra sin guión.");
			txtNIF.grabFocus();
		} catch(NSSIncorrectoException e) {
			txtNSS.selectAll();
			Dialogos.mostrarDialogoError(getFrame(), "Error", "El NSS debe contener 12 dígitos.");
			txtNSS.grabFocus();
		} catch(NombreIncorrectoException e) {
			txtNombre.selectAll();
			Dialogos.mostrarDialogoError(getFrame(), "Error", "El nombre del beneficiario sólo puede contener letras y espacios.");
			txtNombre.grabFocus();
		} catch(ApellidoIncorrectoException e) {
			txtApellidos.selectAll();
			Dialogos.mostrarDialogoError(getFrame(), "Error", "Los apellidos del beneficiario sólo pueden contener letras y espacios.");
			txtApellidos.grabFocus();
		} catch(DomicilioIncorrectoException e) {
			txtDomicilio.selectAll();
			Dialogos.mostrarDialogoError(getFrame(), "Error", "El domicilio sólo puede contener caracteres alfanuméricos.");
			txtDomicilio.grabFocus();
		} catch(CorreoElectronicoIncorrectoException e) {
			txtCorreoElectronico.selectAll();
			Dialogos.mostrarDialogoError(getFrame(), "Error", "El formato del correo electrónico es incorrecto.");
			txtCorreoElectronico.grabFocus();
		} catch(TelefonoFijoIncorrectoException e) {
			txtTelefonoFijo.selectAll();
			Dialogos.mostrarDialogoError(getFrame(), "Error", "El teléfono fijo deben ser 9 dígitos sin separadores y debe comenzar por 9.");
			txtTelefonoFijo.grabFocus();
		} catch(TelefonoMovilIncorrectoException e) {
			txtTelefonoMovil.selectAll();
			Dialogos.mostrarDialogoError(getFrame(), "Error", "El teléfono móvil deben ser 9 dígitos sin separadores y debe comenzar por 6.");
			txtTelefonoMovil.grabFocus();

		} catch(Exception e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.toString());
		}
	}
	
	private void limpiarCamposConsultar() {
		txtNIF.setText("");
		txtNSS.setText("");
		txtNombre.setText("");
		txtApellidos.setText("");
		txtFechaNacimiento.setText("");
		txtDomicilio.setText("");
		txtCorreoElectronico.setText("");
		txtTelefonoFijo.setText("");
		txtTelefonoMovil.setText("");
		txtMedicoAsignado.setText("");
		chkEditar.setSelected(false);
		chkEditar.setEnabled(false);
	}
	
	private void chkEditarActionPerformed(ActionEvent evt) {
		boolean bEstado;
		
		bEstado = chkEditar.isSelected();
		configurarFormularioConsultar(bEstado);
	}

	private void configurarFormularioConsultar(boolean estado) {
		txtNombre.setEditable(estado);
		txtNombre.setFocusable(estado);
		txtApellidos.setEditable(estado);
		txtApellidos.setFocusable(estado);
		txtDomicilio.setEditable(estado);
		txtDomicilio.setFocusable(estado);
		txtCorreoElectronico.setEditable(estado);
		txtCorreoElectronico.setFocusable(estado);
		txtTelefonoFijo.setEditable(estado);
		txtTelefonoFijo.setFocusable(estado);
		txtTelefonoMovil.setEditable(estado);
		txtTelefonoMovil.setFocusable(estado);
		btnAplicar.setEnabled(estado);
	}

	//$hide<<$
	
}
