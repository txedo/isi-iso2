package presentacion;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;
import com.toedter.calendar.JDateChooser;
import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.Direccion;
import dominio.conocimiento.Medico;
import dominio.control.ControladorCliente;
import excepciones.ApellidoIncorrectoException;
import excepciones.BeneficiarioYaExistenteException;
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
 * Panel que permite registrar nuevos beneficiarios en el sistema.
 */
public class JPBeneficiarioRegistrar extends JPBase {

	private static final long serialVersionUID = 6966877345630053451L;
	
	private JButton btnRestablecer;
	private JButton btnCrear;
	private JTextField txtTelefonoMovil;
	private JTextField txtTelefonoFijo;
	private JTextField txtPiso;
	private JDateChooser dtcFechaNacimiento;
	private JLabel lblFechaNacimiento;
	private JLabel lblPuerta;
	private JLabel lblPiso;
	private JTextField txtPuerta;
	private JLabel lblNumero;
	private JTextField txtNumero;
	private JTextField txtCorreoElectronico;
	private JTextField txtDomicilio;
	private JTextField txtApellidos;
	private JTextField txtNombre;
	private JTextField txtNSS;
	private JLabel lblDomicilio;
	private JTextField txtNIF;
	private JLabel lblTelefonoMovil;
	private JLabel lblCamposOblig;
	private JTextField txtCP;
	private JLabel lblCP;
	private JLabel lblProvincia;
	private JLabel lblLocalidad;
	private JTextField txtLocalidad;
	private JTextField txtProvincia;
	private JLabel lblTelefonoFijo;
	private JLabel lblCorreoElectronico;
	private JLabel lblApellidos;
	private JLabel lblNombre;
	private JLabel lblNSS;
	private JLabel lblNIF;

	public JPBeneficiarioRegistrar(JFrame frame, ControladorCliente controlador) {
		super(frame, controlador);
		initGUI();
	}
	
	private void initGUI() {
		try {
			AnchorLayout thisLayout = new AnchorLayout();
			this.setLayout(thisLayout);
			this.setPreferredSize(new java.awt.Dimension(430, 399));
			{
				lblCamposOblig = new JLabel();
				this.add(lblCamposOblig, new AnchorConstraint(335, 13, 900, 696, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
				lblCamposOblig.setText("* Campos obligatorios");
				lblCamposOblig.setPreferredSize(new java.awt.Dimension(129, 17));
				lblCamposOblig.setHorizontalAlignment(SwingConstants.TRAILING);
				lblCamposOblig.setFont(lblCamposOblig.getFont().deriveFont(10.0f));
			}
			{
				txtCP = new JTextField();
				this.add(txtCP, new AnchorConstraint(199, 12, 580, 856, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
				txtCP.setPreferredSize(new java.awt.Dimension(55, 23));
				txtCP.setName("txtCP");
			}
			{
				lblCP = new JLabel();
				this.add(lblCP, new AnchorConstraint(202, 72, 570, 801, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
				lblCP.setText("CP *");
				lblCP.setPreferredSize(new java.awt.Dimension(25, 16));
			}
			{
				lblProvincia = new JLabel();
				this.add(lblProvincia, new AnchorConstraint(231, 143, 644, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblProvincia.setText("Provincia *");
				lblProvincia.setPreferredSize(new java.awt.Dimension(119, 16));
			}
			{
				lblLocalidad = new JLabel();
				this.add(lblLocalidad, new AnchorConstraint(202, 329, 570, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblLocalidad.setText("Localidad *");
				lblLocalidad.setPreferredSize(new java.awt.Dimension(121, 16));
			}
			{
				txtLocalidad = new JTextField();
				this.add(txtLocalidad, new AnchorConstraint(199, 110, 580, 145, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtLocalidad.setPreferredSize(new java.awt.Dimension(175, 23));
				txtLocalidad.setName("txtLocalidad");
			}
			{
				txtProvincia = new JTextField();
				this.add(txtProvincia, new AnchorConstraint(228, 12, 655, 145, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtProvincia.setPreferredSize(new java.awt.Dimension(273, 23));
				txtProvincia.setName("txtProvincia");
			}
			{
				btnRestablecer = new JButton();
				this.add(btnRestablecer, new AnchorConstraint(361, 142, 961, 549, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
				btnRestablecer.setText("Restablecer");
				btnRestablecer.setPreferredSize(new java.awt.Dimension(120, 25));
				btnRestablecer.setName("btnRestablecer");
				btnRestablecer.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnRestablecerActionPerformed(evt);
					}
				});
			}
			{
				btnCrear = new JButton();
				this.add(btnCrear, new AnchorConstraint(361, 13, 961, 765, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
				btnCrear.setDefaultCapable(true);
				btnCrear.setText("Crear beneficiario");
				btnCrear.setPreferredSize(new java.awt.Dimension(120, 26));
				btnCrear.setName("btnCrear");
				btnCrear.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnCrearActionPerformed(evt);
					}
				});
			}
			{
				txtTelefonoMovil = new JTextField();
				this.add(txtTelefonoMovil, new AnchorConstraint(310, 12, 805, 145, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtTelefonoMovil.setPreferredSize(new java.awt.Dimension(273, 22));
				txtTelefonoMovil.setName("txtTelefonoMovil");
			}
			{
				txtTelefonoFijo = new JTextField();
				this.add(txtTelefonoFijo, new AnchorConstraint(283, 12, 710, 145, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtTelefonoFijo.setPreferredSize(new java.awt.Dimension(273, 22));
				txtTelefonoFijo.setName("txtTelefonoFijo");
			}
			{
				txtCorreoElectronico = new JTextField();
				this.add(txtCorreoElectronico, new AnchorConstraint(256, 12, 612, 145, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtCorreoElectronico.setPreferredSize(new java.awt.Dimension(273, 22));
				txtCorreoElectronico.setName("txtCorreoElectronico");
			}
			{
				txtPuerta = new JTextField();
				this.add(txtPuerta, new AnchorConstraint(172, 961, 534, 383, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtPuerta.setPreferredSize(new java.awt.Dimension(29, 22));
				txtPuerta.setName("txtPuerta");
			}
			{
				txtPiso = new JTextField();
				this.add(txtPiso, new AnchorConstraint(172, 775, 534, 286, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtPiso.setPreferredSize(new java.awt.Dimension(29, 22));
				txtPiso.setName("txtPiso");
			}
			{
				txtNumero = new JTextField();
				this.add(txtNumero, new AnchorConstraint(172, 598, 534, 201, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtNumero.setPreferredSize(new java.awt.Dimension(29, 22));
				txtNumero.setName("txtNumero");
			}
			{
				txtDomicilio = new JTextField();
				this.add(txtDomicilio, new AnchorConstraint(146, 12, 514, 145, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtDomicilio.setPreferredSize(new java.awt.Dimension(273, 22));
				txtDomicilio.setName("txtDomicilio");
			}
			{
				dtcFechaNacimiento = new JDateChooser();
				this.add(dtcFechaNacimiento, new AnchorConstraint(119, 12, 408, 145, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				dtcFechaNacimiento.setPreferredSize(new java.awt.Dimension(273, 22));
				dtcFechaNacimiento.setDateFormatString("dd/MM/yyyy");
				dtcFechaNacimiento.setToolTipText("Formato dd/MM/yyyy. Haga clic en el icono de la derecha para desplegar un calendario.");
				dtcFechaNacimiento.setMaxSelectableDate(new Date());
				dtcFechaNacimiento.setName("dtcFechaNacimiento");
			}
			{
				txtApellidos = new JTextField();
				this.add(txtApellidos, new AnchorConstraint(92, 12, 416, 145, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtApellidos.setPreferredSize(new java.awt.Dimension(273, 22));
				txtApellidos.setName("txtApellidos");
			}
			{
				txtNombre = new JTextField();
				this.add(txtNombre, new AnchorConstraint(65, 12, 318, 145, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtNombre.setPreferredSize(new java.awt.Dimension(273, 22));
				txtNombre.setName("txtNombre");
			}
			{
				txtNSS = new JTextField();
				this.add(txtNSS, new AnchorConstraint(39, 12, 223, 145, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtNSS.setPreferredSize(new java.awt.Dimension(273, 22));
				txtNSS.setName("txtNSS");
			}
			{
				txtNIF = new JTextField();
				this.add(txtNIF, new AnchorConstraint(12, 12, 125, 145, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtNIF.setPreferredSize(new java.awt.Dimension(273, 22));
				txtNIF.setName("txtNIF");
			}
			{
				lblTelefonoMovil = new JLabel();
				this.add(lblTelefonoMovil, new AnchorConstraint(312, 431, 790, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblTelefonoMovil.setText("Teléfono móvil");
				lblTelefonoMovil.setPreferredSize(new java.awt.Dimension(119, 14));
			}
			{
				lblTelefonoFijo = new JLabel();
				this.add(lblTelefonoFijo, new AnchorConstraint(287, 431, 692, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblTelefonoFijo.setText("Teléfono fijo");
				lblTelefonoFijo.setPreferredSize(new java.awt.Dimension(119, 14));
			}
			{
				lblCorreoElectronico = new JLabel();
				this.add(lblCorreoElectronico, new AnchorConstraint(260, 431, 594, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblCorreoElectronico.setText("Correo electrónico");
				lblCorreoElectronico.setPreferredSize(new java.awt.Dimension(119, 14));
			}
			{
				lblPuerta = new JLabel();
				this.add(lblPuerta, new AnchorConstraint(176, 905, 519, 338, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblPuerta.setText("Puerta");
				lblPuerta.setPreferredSize(new java.awt.Dimension(43, 14));
			}
			{
				lblPiso = new JLabel();
				this.add(lblPiso, new AnchorConstraint(176, 717, 525, 255, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblPiso.setText("Piso");
				lblPiso.setPreferredSize(new java.awt.Dimension(31, 14));
			}
			{
				lblNumero = new JLabel();
				this.add(lblNumero, new AnchorConstraint(176, 540, 515, 147, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblNumero.setText("Número");
				lblNumero.setPreferredSize(new java.awt.Dimension(48, 14));
			}
			{
				lblDomicilio = new JLabel();
				this.add(lblDomicilio, new AnchorConstraint(150, 429, 496, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblDomicilio.setText("Domicilio *");
				lblDomicilio.setPreferredSize(new java.awt.Dimension(121, 14));
			}
			{
				lblFechaNacimiento = new JLabel();
				this.add(lblFechaNacimiento, new AnchorConstraint(123, 429, 397, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblFechaNacimiento.setText("Fecha de nacimiento *");
				lblFechaNacimiento.setPreferredSize(new java.awt.Dimension(121, 14));
			}
			{
				lblApellidos = new JLabel();
				this.add(lblApellidos, new AnchorConstraint(96, 429, 398, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblApellidos.setText("Apellidos *");
				lblApellidos.setPreferredSize(new java.awt.Dimension(121, 14));
			}
			{
				lblNombre = new JLabel();
				this.add(lblNombre, new AnchorConstraint(69, 429, 303, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblNombre.setText("Nombre *");
				lblNombre.setPreferredSize(new java.awt.Dimension(121, 14));
			}
			{
				lblNSS = new JLabel();
				this.add(lblNSS, new AnchorConstraint(43, 429, 209, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblNSS.setText("NSS *");
				lblNSS.setPreferredSize(new java.awt.Dimension(121, 14));
			}
			{
				lblNIF = new JLabel();
				this.add(lblNIF, new AnchorConstraint(16, 429, 110, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblNIF.setText("NIF *");
				lblNIF.setPreferredSize(new java.awt.Dimension(121, 14));
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//$hide>>$
	
	private void btnCrearActionPerformed(ActionEvent evt) {
		Beneficiario beneficiario;
		Medico medico;
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
			beneficiario = new Beneficiario();
			beneficiario.setNif(txtNIF.getText().trim().toUpperCase());
			beneficiario.setNss(txtNSS.getText().trim());
			beneficiario.setNombre(txtNombre.getText().trim());
			beneficiario.setApellidos(txtApellidos.getText().trim());
			beneficiario.setFechaNacimiento(dtcFechaNacimiento.getDate());
			dir = new Direccion();
			dir.setDomicilio(txtDomicilio.getText());
			if (!txtNumero.getText().equals(""))
				dir.setNumero(txtNumero.getText());
			else
				dir.setNumero("s/n");
			dir.setPiso(txtPiso.getText());
			dir.setPuerta(txtPuerta.getText());
			dir.setCiudad(txtLocalidad.getText());
			dir.setProvincia(txtProvincia.getText());
			dir.setCP(Integer.parseInt(txtCP.getText()));
			beneficiario.setDireccion(dir);
			beneficiario.setCorreo(txtCorreoElectronico.getText().trim());
			beneficiario.setTelefono(txtTelefonoFijo.getText().trim());
			beneficiario.setMovil(txtTelefonoMovil.getText().trim());

			// Solicitamos al servidor que se cree el beneficiario
			getControlador().crearBeneficiario(beneficiario);
			
			// Obtenemos el médico que se ha asignado al beneficiario
			medico = getControlador().consultarBeneficiarioPorNIF(beneficiario.getNif()).getMedicoAsignado();
			
			// Mostramos un mensaje indicando que el beneficiario se ha
			// creado correctamente y cuál es el médico asignado
			Dialogos.mostrarDialogoInformacion(getFrame(), "Operación correcta", "El beneficiario ha sido dado de alta en el sistema y se le\nha asignado el siguiente médico y centro de salud:\n " + medico.getNombre() + " " + medico.getApellidos() + "\n " + medico.getCentroSalud().getNombre());
			limpiarCamposRegistro();
			
		} catch(BeneficiarioYaExistenteException e) {
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

	private void btnRestablecerActionPerformed(ActionEvent evt) {
		limpiarCamposRegistro();
	}

	private boolean campoVacio(JTextField campo) {
		return campo.getText().trim().equals("");
	}
	
	public void limpiarCamposRegistro() {
		txtNIF.setText("");
		txtNSS.setText("");
		txtNombre.setText("");
		txtApellidos.setText("");
		dtcFechaNacimiento.setDate(null);
		txtDomicilio.setText("");
		txtNumero.setText("");
		txtPiso.setText("");
		txtPuerta.setText("");
		txtLocalidad.setText("");
		txtCP.setText("");
		txtProvincia.setText("");
		txtCorreoElectronico.setText("");
		txtTelefonoFijo.setText("");
		txtTelefonoMovil.setText("");
		txtLocalidad.setText("");
		txtCP.setText("");
		txtProvincia.setText("");
	}
	
	//$hide<<$
	
}
