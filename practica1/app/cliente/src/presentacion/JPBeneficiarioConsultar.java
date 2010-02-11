package presentacion;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Date;
import java.util.EventObject;
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
import com.toedter.calendar.JDateChooser;
import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.Direccion;
import dominio.conocimiento.Validacion;
import dominio.control.ControladorCliente;
import excepciones.ApellidoIncorrectoException;
import excepciones.BeneficiarioInexistenteException;
import excepciones.CadenaVaciaException;
import excepciones.CodigoPostalIncorrectoException;
import excepciones.CorreoElectronicoIncorrectoException;
import excepciones.DomicilioIncorrectoException;
import excepciones.FechaNacimientoIncorrectaException;
import excepciones.FormatoFechaIncorrectoException;
import excepciones.LocalidadIncorrectaException;
import excepciones.NIFIncorrectoException;
import excepciones.NSSIncorrectoException;
import excepciones.NombreIncorrectoException;
import excepciones.NumeroDomicilioIncorrectoException;
import excepciones.PisoDomicilioIncorrectoException;
import excepciones.ProvinciaIncorrectaException;
import excepciones.PuertaDomicilioIncorrectoException;
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
	
	private EventListenerList listenerList;
	
	private Beneficiario beneficiario;

	private ComboBoxModel cmbIdentificacionModel;
	private JLabel lblMedicoAsignado;
	private JTextField txtMedicoAsignado;
	private JLabel lblTelefonoMovil;
	private JButton btnEliminar;
	private JLabel lblCamposOblig;
	private JTextField txtCP;
	private JLabel lblCP;
	private JLabel lblProvincia;
	private JLabel lblLocalidad;
	private JTextField txtLocalidad;
	private JTextField txtProvincia;
	private JTextField txtPuerta;
	private JTextField txtPiso;
	private JTextField txtNumero;
	private JLabel lblPuerta;
	private JLabel lblPiso;
	private JLabel lblNumero;
	private JDateChooser dtcFechaNacimiento;
	private JLabel lblCentro;
	private JTextField txtCentro;
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
	private JButton btnGuardar;
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
		this(null, null);
		// Este constructor evita que aparezca un error al editar con
		// el Jigloo los formularios que utilizan JPBeneficiarioConsultar
	}
	
	public JPBeneficiarioConsultar(JFrame frame, ControladorCliente controlador) {
		super(frame, controlador);
		initGUI();
		deshabilitarEdicion();
		listenerList = new EventListenerList();
		beneficiario = null;
	}
	
	private void initGUI() {
		try {
			AnchorLayout thisLayout = new AnchorLayout();
			this.setLayout(thisLayout);
			this.setPreferredSize(new java.awt.Dimension(430, 527));
			{
				btnEliminar = new JButton();
				this.add(btnEliminar, new AnchorConstraint(489, 161, 974, 9, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				btnEliminar.setText("Eliminar beneficiario");
				btnEliminar.setPreferredSize(new java.awt.Dimension(129, 26));
				btnEliminar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnEliminarActionPerformed(evt);
					}
				});
			}
			{
				lblFechaNacimiento = new JLabel();
				this.add(lblFechaNacimiento, new AnchorConstraint(189, 294, 493, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblFechaNacimiento.setText("Fecha de nacimiento *");
				lblFechaNacimiento.setPreferredSize(new java.awt.Dimension(120, 16));
			}
			{
				btnGuardar = new JButton();
				this.add(btnGuardar, new AnchorConstraint(489, 12, 963, 794, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
				btnGuardar.setText("Guardar cambios");
				btnGuardar.setPreferredSize(new java.awt.Dimension(120, 26));
				btnGuardar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnGuardarActionPerformed(evt);
					}
				});
				btnGuardar.setEnabled(false);
				btnGuardar.setSize(120, 26);
			}
			{
				chkEditar = new JCheckBox();
				this.add(chkEditar, new AnchorConstraint(494, 137, 854, 788, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
				chkEditar.setText("Habilitar edición");
				chkEditar.setPreferredSize(new java.awt.Dimension(114, 14));
				chkEditar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						chkEditarActionPerformed(evt);
					}
				});
				chkEditar.setEnabled(false);
			}
			{
				lblMedicoAsignado = new JLabel();
				this.add(lblMedicoAsignado, new AnchorConstraint(411, 277, 950, 9, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblMedicoAsignado.setText("Médico asignado");
				lblMedicoAsignado.setPreferredSize(new java.awt.Dimension(110, 18));
			}
			{
				lblTelefonoMovil = new JLabel();
				this.add(lblTelefonoMovil, new AnchorConstraint(385, 277, 861, 9, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblTelefonoMovil.setText("Teléfono móvil");
				lblTelefonoMovil.setPreferredSize(new java.awt.Dimension(110, 15));
			}
			{
				lblTelefonoFijo = new JLabel();
				this.add(lblTelefonoFijo, new AnchorConstraint(358, 277, 776, 9, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblTelefonoFijo.setText("Teléfono fijo");
				lblTelefonoFijo.setPreferredSize(new java.awt.Dimension(110, 15));
			}
			{
				lblCorreoElectronico = new JLabel();
				this.add(lblCorreoElectronico, new AnchorConstraint(330, 273, 690, 9, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblCorreoElectronico.setText("Correo electrónico");
				lblCorreoElectronico.setPreferredSize(new java.awt.Dimension(108, 15));
			}
			{
				lblDomicilio = new JLabel();
				this.add(lblDomicilio, new AnchorConstraint(217, 277, 604, 9, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblDomicilio.setText("Domicilio *");
				lblDomicilio.setPreferredSize(new java.awt.Dimension(110, 15));
			}
			{
				lblApellidos = new JLabel();
				this.add(lblApellidos, new AnchorConstraint(161, 280, 522, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblApellidos.setText("Apellidos *");
				lblApellidos.setPreferredSize(new java.awt.Dimension(110, 15));
			}
			{
				lblNombre = new JLabel();
				this.add(lblNombre, new AnchorConstraint(133, 280, 433, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblNombre.setText("Nombre *");
				lblNombre.setPreferredSize(new java.awt.Dimension(110, 15));
			}
			{
				lblNSS = new JLabel();
				this.add(lblNSS, new AnchorConstraint(105, 280, 344, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblNSS.setText("NSS *");
				lblNSS.setPreferredSize(new java.awt.Dimension(110, 15));
			}
			{
				lblNIF = new JLabel();
				this.add(lblNIF, new AnchorConstraint(77, 280, 261, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblNIF.setText("NIF *");
				lblNIF.setPreferredSize(new java.awt.Dimension(110, 15));
			}
			{
				txtMedicoAsignado = new JTextField();
				this.add(txtMedicoAsignado, new AnchorConstraint(409, 12, 953, 138, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtMedicoAsignado.setEditable(false);
				txtMedicoAsignado.setPreferredSize(new java.awt.Dimension(280, 22));
			}
			{
				txtTelefonoMovil = new JTextField();
				this.add(txtTelefonoMovil, new AnchorConstraint(381, 12, 874, 138, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtTelefonoMovil.setPreferredSize(new java.awt.Dimension(280, 23));
				txtTelefonoMovil.setEditable(false);
			}
			{
				txtTelefonoFijo = new JTextField();
				this.add(txtTelefonoFijo, new AnchorConstraint(354, 12, 788, 138, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtTelefonoFijo.setPreferredSize(new java.awt.Dimension(280, 23));
				txtTelefonoFijo.setEditable(false);
			}
			{
				txtCorreoElectronico = new JTextField();
				this.add(txtCorreoElectronico, new AnchorConstraint(326, 12, 700, 138, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtCorreoElectronico.setPreferredSize(new java.awt.Dimension(280, 23));
				txtCorreoElectronico.setEditable(false);
			}
			{
				txtDomicilio = new JTextField();
				this.add(txtDomicilio, new AnchorConstraint(213, 12, 617, 138, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtDomicilio.setPreferredSize(new java.awt.Dimension(280, 23));
				txtDomicilio.setEditable(false);
			}
			{
				txtApellidos = new JTextField();
				this.add(txtApellidos, new AnchorConstraint(157, 12, 534, 138, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtApellidos.setPreferredSize(new java.awt.Dimension(280, 23));
				txtApellidos.setEditable(false);
			}
			{
				txtNombre = new JTextField();
				this.add(txtNombre, new AnchorConstraint(129, 12, 442, 138, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtNombre.setPreferredSize(new java.awt.Dimension(280, 23));
				txtNombre.setEditable(false);
			}
			{
				txtNSS = new JTextField();
				this.add(txtNSS, new AnchorConstraint(101, 12, 357, 138, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtNSS.setPreferredSize(new java.awt.Dimension(280, 23));
				txtNSS.setEditable(false);
			}
			{
				txtNIF = new JTextField();
				this.add(txtNIF, new AnchorConstraint(73, 12, 271, 138, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtNIF.setPreferredSize(new java.awt.Dimension(280, 23));
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
				this.add(lblBuscar, new AnchorConstraint(14, 387, 90, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblBuscar.setText("Buscar beneficiario por:");
				lblBuscar.setPreferredSize(new java.awt.Dimension(156, 14));
			}
			{
				cmbIdentificacion = new JComboBox();
				this.add(cmbIdentificacion, new AnchorConstraint(37, 236, 188, 9, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				cmbIdentificacion.setPreferredSize(new java.awt.Dimension(100, 22));
				cmbIdentificacionModel = new DefaultComboBoxModel(new String[] { ID_NIF, ID_NSS });
				cmbIdentificacion.setModel(cmbIdentificacionModel);
			}
			{
				txtCentro = new JTextField();
				this.add(txtCentro, new AnchorConstraint(436, 12, 893, 138, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtCentro.setEditable(false);
				txtCentro.setPreferredSize(new java.awt.Dimension(280, 22));
			}
			{
				lblCentro = new JLabel();
				this.add(lblCentro, new AnchorConstraint(438, 277, 891, 9, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblCentro.setText("Centro asignado");
				lblCentro.setPreferredSize(new java.awt.Dimension(110, 18));
			}
			{
				dtcFechaNacimiento = new JDateChooser();
				this.add(dtcFechaNacimiento, new AnchorConstraint(186, 12, 477, 138, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				dtcFechaNacimiento.setDateFormatString("dd/MM/yyyy");
				dtcFechaNacimiento.setMaxSelectableDate(new Date());
				dtcFechaNacimiento.setPreferredSize(new java.awt.Dimension(280, 22));
				dtcFechaNacimiento.setToolTipText("Formato dd/MM/yyyy. Haga clic en el icono de la derecha para desplegar un calendario.");
				dtcFechaNacimiento.setName("dtcFechaNacimiento");
				dtcFechaNacimiento.setEnabled(false);
			}
			{
				lblPuerta = new JLabel();
				this.add(lblPuerta, new AnchorConstraint(245, 905, 519, 331, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblPuerta.setText("Puerta");
				lblPuerta.setPreferredSize(new java.awt.Dimension(43, 14));
			}
			{
				lblPiso = new JLabel();
				this.add(lblPiso, new AnchorConstraint(245, 717, 525, 248, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblPiso.setText("Piso");
				lblPiso.setPreferredSize(new java.awt.Dimension(31, 14));
			}
			{
				lblNumero = new JLabel();
				this.add(lblNumero, new AnchorConstraint(245, 540, 515, 140, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblNumero.setText("Número");
				lblNumero.setPreferredSize(new java.awt.Dimension(48, 14));
			}
			{
				txtPuerta = new JTextField();
				this.add(txtPuerta, new AnchorConstraint(241, 961, 534, 376, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtPuerta.setPreferredSize(new java.awt.Dimension(29, 22));
				txtPuerta.setName("txtPuerta");
				txtPuerta.setEditable(false);
			}
			{
				txtPiso = new JTextField();
				this.add(txtPiso, new AnchorConstraint(241, 775, 534, 279, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtPiso.setPreferredSize(new java.awt.Dimension(29, 22));
				txtPiso.setName("txtPiso");
				txtPiso.setEditable(false);
			}
			{
				txtNumero = new JTextField();
				this.add(txtNumero, new AnchorConstraint(241, 598, 534, 194, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtNumero.setPreferredSize(new java.awt.Dimension(29, 22));
				txtNumero.setName("txtNumero");
				txtNumero.setEditable(false);
			}
			{
				txtCP = new JTextField();
				this.add(txtCP, new AnchorConstraint(268, 12, 580, 856, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
				txtCP.setPreferredSize(new java.awt.Dimension(55, 23));
				txtCP.setName("txtCP");
				txtCP.setEditable(false);
			}
			{
				lblCP = new JLabel();
				this.add(lblCP, new AnchorConstraint(271, 72, 570, 801, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
				lblCP.setText("CP *");
				lblCP.setPreferredSize(new java.awt.Dimension(25, 16));
			}
			{
				lblProvincia = new JLabel();
				this.add(lblProvincia, new AnchorConstraint(300, 143, 644, 9, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblProvincia.setText("Provincia *");
				lblProvincia.setPreferredSize(new java.awt.Dimension(119, 16));
			}
			{
				lblLocalidad = new JLabel();
				this.add(lblLocalidad, new AnchorConstraint(271, 329, 570, 9, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblLocalidad.setText("Localidad *");
				lblLocalidad.setPreferredSize(new java.awt.Dimension(121, 16));
			}
			{
				txtLocalidad = new JTextField();
				this.add(txtLocalidad, new AnchorConstraint(268, 115, 580, 138, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtLocalidad.setPreferredSize(new java.awt.Dimension(177, 23));
				txtLocalidad.setName("txtLocalidad");
				txtLocalidad.setEditable(false);
			}
			{
				txtProvincia = new JTextField();
				this.add(txtProvincia, new AnchorConstraint(297, 12, 655, 138, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtProvincia.setPreferredSize(new java.awt.Dimension(280, 23));
				txtProvincia.setName("txtProvincia");
				txtProvincia.setEditable(false);
			}
			{
				lblCamposOblig = new JLabel();
				this.add(lblCamposOblig, new AnchorConstraint(462, 12, 875, 673, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
				lblCamposOblig.setText("* Campos obligatorios");
				lblCamposOblig.setHorizontalAlignment(SwingConstants.TRAILING);
				lblCamposOblig.setPreferredSize(new java.awt.Dimension(129, 17));
				lblCamposOblig.setFont(lblCamposOblig.getFont().deriveFont(10.0f));
			}
			btnEliminar.setEnabled(false);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void btnEliminarActionPerformed(ActionEvent evt) {
		boolean respuesta = Dialogos.mostrarDialogoPregunta(getFrame(), "Pregunta", "¿Realmente desea eliminar este beneficiario?");
		if (respuesta) {
			try {
				getControlador().eliminarBeneficiario(beneficiario);
				Dialogos.mostrarDialogoInformacion(getFrame(), "Informacion", "Beneficiario eliminado correctamente");
				limpiarCamposConsulta();
			} catch(BeneficiarioInexistenteException e) {
				Dialogos.mostrarDialogoError(getFrame(), "Error", e.getMessage());
				
			} catch(SQLException e) {
				Dialogos.mostrarDialogoError(getFrame(), "Error", e.getLocalizedMessage());
			} catch(RemoteException e) {
				Dialogos.mostrarDialogoError(getFrame(), "Error", e.getLocalizedMessage());
			} catch(Exception e) {
				Dialogos.mostrarDialogoError(getFrame(), "Error", e.getLocalizedMessage());
			}
			
		}
	}

	//$hide>>$
	
	public Beneficiario getBeneficiario() {
		return beneficiario;
	}
	
	private void btnBuscarActionPerformed(ActionEvent evt) {
		Object[] listeners;
		String identificacion, tipo;
		int i;
		
		// Borramos la información del antiguo beneficiario consultado
		limpiarCamposConsulta();
		deshabilitarEdicion();

		try {

			// Obtenemos el identificador para buscar el beneficiario
			identificacion = txtIdentificacion.getText().toUpperCase();
			tipo = (String)cmbIdentificacion.getSelectedItem();
			if(identificacion.equals("")) {
				throw new CadenaVaciaException();
			}

			// Buscamos el beneficiario solicitado
			if(tipo.equals(ID_NIF)) {
				Validacion.comprobarNIF(identificacion);
				beneficiario = getControlador().consultarBeneficiario(identificacion);
			} else if(tipo.equals(ID_NSS)) {
				Validacion.comprobarNSS(identificacion);
				beneficiario = getControlador().consultarBeneficiarioPorNSS(identificacion);
			}

			// Mostramos los datos del beneficiario encontrado
			Dialogos.mostrarDialogoInformacion(getFrame(), "Búsqueda correcta", "Beneficiario encontrado.");			
			txtIdentificacion.setText("");
			txtNIF.setText(beneficiario.getNif());
			txtNSS.setText(beneficiario.getNss());
			txtNombre.setText(beneficiario.getNombre());
			txtApellidos.setText(beneficiario.getApellidos());
			dtcFechaNacimiento.setDate(beneficiario.getFechaNacimiento());
			txtLocalidad.setText(beneficiario.getDireccion().getCiudad());
			txtProvincia.setText(beneficiario.getDireccion().getProvincia());
			txtCP.setText(Integer.toString(beneficiario.getDireccion().getCP()));
			txtDomicilio.setText(beneficiario.getDireccion().getDomicilio());
			txtNumero.setText(beneficiario.getDireccion().getNumero());
			txtPiso.setText(beneficiario.getDireccion().getPiso());
			txtPuerta.setText(beneficiario.getDireccion().getPuerta());
			txtCorreoElectronico.setText(beneficiario.getCorreo());
			txtTelefonoFijo.setText(beneficiario.getTelefono());
			txtTelefonoMovil.setText(beneficiario.getMovil());
			txtMedicoAsignado.setText(beneficiario.getMedicoAsignado().getApellidos() + ", " + beneficiario.getMedicoAsignado().getNombre() + " (" + beneficiario.getMedicoAsignado().getDni() + ")");
			txtCentro.setText(beneficiario.getMedicoAsignado().getCentroSalud().getNombre() + "; " + beneficiario.getMedicoAsignado().getCentroSalud().getDireccion().toString());
			chkEditar.setEnabled(true);
			btnEliminar.setEnabled(true);
			
		} catch(BeneficiarioInexistenteException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getMessage());
			txtIdentificacion.selectAll();
			txtIdentificacion.grabFocus();			

		} catch(CadenaVaciaException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", "Debe introducir un NIF o un NSS.");
			txtIdentificacion.grabFocus();
		} catch(NIFIncorrectoException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getMessage());
			txtIdentificacion.selectAll();
			txtIdentificacion.grabFocus();
		} catch(NSSIncorrectoException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getMessage());
			txtIdentificacion.selectAll();
			txtIdentificacion.grabFocus();

		} catch(SQLException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getLocalizedMessage());
		} catch(RemoteException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getLocalizedMessage());
		} catch(Exception e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getLocalizedMessage());
		}
		
		// Notificamos que ha cambiado el beneficiario seleccionado
		listeners = listenerList.getListenerList();
		for(i = 0; i < listeners.length; i += 2) {
			if(listeners[i] == BeneficiarioBuscadoListener.class) {
				((BeneficiarioBuscadoListener)listeners[i + 1]).beneficiarioBuscado(new EventObject(this));
			}
		}
	}
	
	private void btnGuardarActionPerformed(ActionEvent evt) {
		Beneficiario benefCambiado = null;
		Direccion dir;
		
		try {
			
			// Comprobamos todos los campos
			Validacion.comprobarNIF(txtNIF.getText().trim());
			Validacion.comprobarNSS(txtNSS.getText().trim());
			Validacion.comprobarNombre(txtNombre.getText().trim());
			Validacion.comprobarApellidos(txtApellidos.getText().trim());
			Validacion.comprobarFechaNacimiento(dtcFechaNacimiento.getDate());
			Validacion.comprobarDomicilio(txtDomicilio.getText().trim());
			if(!campoVacio(txtNumero)) {
				Validacion.comprobarNumero(txtNumero.getText().trim());
			}
			if(!campoVacio(txtPiso)) {
				Validacion.comprobarPiso(txtPiso.getText().trim());
			}
			if(!campoVacio(txtPuerta)) {
				Validacion.comprobarPuerta(txtPuerta.getText().trim());
			}
			Validacion.comprobarDomicilioCompleto(txtDomicilio.getText().trim(), txtNumero.getText().trim(), txtPiso.getText().trim(), txtPuerta.getText().trim());
			Validacion.comprobarLocalidad(txtLocalidad.getText().trim());
			Validacion.comprobarCodigoPostal(txtCP.getText().trim());
			Validacion.comprobarProvincia(txtProvincia.getText().trim());
			if(!campoVacio(txtCorreoElectronico)) {
				Validacion.comprobarCorreoElectronico(txtCorreoElectronico.getText().trim());
			}
			if(!campoVacio(txtTelefonoFijo)) {
				Validacion.comprobarTelefonoFijo(txtTelefonoFijo.getText().trim());
			}
			if(!campoVacio(txtTelefonoMovil)) {
				Validacion.comprobarTelefonoMovil(txtTelefonoMovil.getText().trim());
			}
			
			// Creamos un nuevo beneficiario con los datos introducidos
			benefCambiado = new Beneficiario();
			benefCambiado.setNif(txtNIF.getText().trim().toUpperCase());
			benefCambiado.setNss(txtNSS.getText().trim());
			benefCambiado.setNombre(txtNombre.getText().trim());
			benefCambiado.setApellidos(txtApellidos.getText().trim());
			benefCambiado.setFechaNacimiento(dtcFechaNacimiento.getDate());
			dir = new Direccion();
			dir.setDomicilio(txtDomicilio.getText());
			if(campoVacio(txtNumero))
				dir.setNumero("s/n");
			else
				dir.setNumero(txtNumero.getText());
			dir.setPiso(txtPiso.getText());
			dir.setPuerta(txtPuerta.getText());
			dir.setCiudad(txtLocalidad.getText());
			dir.setProvincia(txtProvincia.getText());
			dir.setCP(Integer.parseInt(txtCP.getText()));
			benefCambiado.setDireccion(dir);	
			benefCambiado.setCorreo(txtCorreoElectronico.getText().trim());
			benefCambiado.setTelefono(txtTelefonoFijo.getText().trim());
			benefCambiado.setMovil(txtTelefonoMovil.getText().trim());
			
			// Se asigna el mismo médico
			benefCambiado.setMedicoAsignado(beneficiario.getMedicoAsignado());
			
			// Solicitamos al servidor que se modifique el beneficiario
			getControlador().modificarBeneficiario(benefCambiado);
			
			// Mostramos un mensaje indicando que el beneficiario
			// se ha modificado correctamente
			Dialogos.mostrarDialogoInformacion(getFrame(), "Operación correcta", "El beneficiario ha sido modificado correctamente.");
			limpiarCamposConsulta();
			deshabilitarEdicion();
			
		} catch(BeneficiarioInexistenteException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getMessage());
			
		} catch(NIFIncorrectoException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getMessage());
			txtNIF.selectAll();
			txtNIF.grabFocus();
		} catch(NSSIncorrectoException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getMessage());
			txtNSS.selectAll();
			txtNSS.grabFocus();
		} catch(NombreIncorrectoException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getMessage());
			txtNombre.selectAll();
			txtNombre.grabFocus();
		} catch(ApellidoIncorrectoException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getMessage());
			txtApellidos.selectAll();
			txtApellidos.grabFocus();
		} catch(FormatoFechaIncorrectoException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getMessage());
			((JTextField)dtcFechaNacimiento.getDateEditor()).selectAll();
			dtcFechaNacimiento.grabFocus();
		} catch(FechaNacimientoIncorrectaException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getMessage());
			((JTextField)dtcFechaNacimiento.getDateEditor()).selectAll();
			dtcFechaNacimiento.grabFocus();
		} catch(DomicilioIncorrectoException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getMessage());
			txtDomicilio.selectAll();
			txtDomicilio.grabFocus();
		} catch(NumeroDomicilioIncorrectoException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getMessage());
			txtNumero.selectAll();
			txtNumero.grabFocus();
		} catch(PisoDomicilioIncorrectoException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getMessage());
			txtPiso.selectAll();
			txtPiso.grabFocus();
		} catch(PuertaDomicilioIncorrectoException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getMessage());
			txtPuerta.selectAll();
			txtPuerta.grabFocus();
		} catch(LocalidadIncorrectaException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getMessage());
			txtLocalidad.selectAll();
			txtLocalidad.grabFocus();
		} catch(CodigoPostalIncorrectoException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getMessage());
			txtCP.selectAll();
			txtCP.grabFocus();
		} catch(ProvinciaIncorrectaException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getMessage());
			txtProvincia.selectAll();
			txtProvincia.grabFocus();
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

	private void chkEditarActionPerformed(ActionEvent evt) {
		cambiarEdicion(chkEditar.isSelected());
	}
	
	public void addBeneficiarioBuscadoListener(BeneficiarioBuscadoListener listener) {
		listenerList.add(BeneficiarioBuscadoListener.class, listener);
	}

	public void removeBeneficiarioBuscadoListener(BeneficiarioBuscadoListener listener) {
		listenerList.remove(BeneficiarioBuscadoListener.class, listener);
	}
	
	private void deshabilitarEdicion() {
		cambiarEdicion(false);
	}
	
	private void cambiarEdicion(boolean activar) {
		txtNombre.setEditable(activar);
		txtApellidos.setEditable(activar);
		txtDomicilio.setEditable(activar);
		txtNumero.setEditable(activar);
		txtPiso.setEditable(activar);
		txtPuerta.setEditable(activar);
		txtLocalidad.setEditable(activar);
		txtCP.setEditable(activar);
		txtProvincia.setEditable(activar);
		dtcFechaNacimiento.setEnabled(activar);
		txtCorreoElectronico.setEditable(activar);
		txtTelefonoFijo.setEditable(activar);
		txtTelefonoMovil.setEditable(activar);
		btnGuardar.setEnabled(activar);
		lblCamposOblig.setVisible(activar);
		if(activar) {
			lblNIF.setText("NIF *");
			lblNSS.setText("NSS *");
			lblNombre.setText("Nombre *");
			lblApellidos.setText("Apellidos *");
			lblFechaNacimiento.setText("Fecha de nacimiento *");
			lblDomicilio.setText("Domicilio *");
			lblLocalidad.setText("Localidad *");
			lblCP.setText("CP *");
			lblProvincia.setText("Provincia *");
		} else {
			lblNIF.setText("NIF");
			lblNSS.setText("NSS");
			lblNombre.setText("Nombre");
			lblApellidos.setText("Apellidos");
			lblFechaNacimiento.setText("Fecha de nacimiento");
			lblDomicilio.setText("Domicilio");
			lblLocalidad.setText("Localidad");
			lblCP.setText("CP");
			lblProvincia.setText("Provincia");
		}
	}

	private boolean campoVacio(JTextField campo) {
		return campo.getText().trim().equals("");
	}
	
	public void reducirPanel() {
		// Este método oculta algunos controles de la interfaz para
		// que se pueda reutilizar el panel desde otros paneles
		// (como JPEmitirVolante) mostrando sólo la información
		// básica de los beneficiarios
		lblFechaNacimiento.setVisible(false);
		dtcFechaNacimiento.setVisible(false);
		lblDomicilio.setVisible(false);
		txtDomicilio.setVisible(false);
		lblCorreoElectronico.setVisible(false);
		txtCorreoElectronico.setVisible(false);
		lblTelefonoFijo.setVisible(false);
		txtTelefonoFijo.setVisible(false);
		lblTelefonoMovil.setVisible(false);
		txtTelefonoMovil.setVisible(false);
		btnGuardar.setVisible(false);
		chkEditar.setVisible(false);
		this.remove(lblMedicoAsignado);
		this.add(lblMedicoAsignado, ((AnchorLayout)this.getLayout()).getLayoutComponentConstraint(lblFechaNacimiento));
		this.remove(txtMedicoAsignado);
		this.add(txtMedicoAsignado, ((AnchorLayout)this.getLayout()).getLayoutComponentConstraint(dtcFechaNacimiento));
		this.remove(lblCentro);
		this.add(lblCentro, ((AnchorLayout)this.getLayout()).getLayoutComponentConstraint(lblDomicilio));
		this.remove(txtCentro);
		this.add(txtCentro, ((AnchorLayout)this.getLayout()).getLayoutComponentConstraint(txtDomicilio));
	}
	
	public void desactivarModificacion() {
		// Este método se utiliza cuando un usuario puede
		// consultar un beneficiario, pero no modificarlo
		chkEditar.setVisible(false);
		btnGuardar.setVisible(false);
	}
	
	public void limpiarCamposConsulta() {
		beneficiario = null;
		txtNIF.setText("");
		txtNSS.setText("");
		txtNombre.setText("");
		txtApellidos.setText("");
		dtcFechaNacimiento.setDate(null);
		txtDomicilio.setText("");
		txtCorreoElectronico.setText("");
		txtTelefonoFijo.setText("");
		txtTelefonoMovil.setText("");
		txtMedicoAsignado.setText("");
		txtLocalidad.setText("");
		txtProvincia.setText("");
		txtDomicilio.setText("");
		txtCP.setText("");
		txtNumero.setText("");
		txtPiso.setText("");
		txtPuerta.setText("");
		txtCentro.setText("");
		chkEditar.setEnabled(false);
		chkEditar.setSelected(false);
		btnEliminar.setEnabled(false);
	}
	
	//$hide<<$
	
}
