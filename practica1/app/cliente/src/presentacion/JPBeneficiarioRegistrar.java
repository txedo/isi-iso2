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
import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;
import com.toedter.calendar.JDateChooser;
import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.Medico;
import dominio.conocimiento.Validacion;
import dominio.control.ControladorCliente;
import excepciones.ApellidoIncorrectoException;
import excepciones.BeneficiarioYaExistenteException;
import excepciones.CorreoElectronicoIncorrectoException;
import excepciones.DomicilioIncorrectoException;
import excepciones.FechaNacimientoIncorrectaException;
import excepciones.FormatoFechaIncorrectoException;
import excepciones.NIFIncorrectoException;
import excepciones.NSSIncorrectoException;
import excepciones.NombreIncorrectoException;
import excepciones.NumeroDomicilioIncorrectoException;
import excepciones.PisoDomicilioIncorrectoException;
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
			this.setSize(430, 390);
			this.setPreferredSize(new java.awt.Dimension(430, 390));
			{
				btnRestablecer = new JButton();
				this.add(btnRestablecer, new AnchorConstraint(302, 160, 961, 549, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
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
				btnCrear = new JButton();
				this.add(btnCrear, new AnchorConstraint(302, 17, 961, 765, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
				btnCrear.setDefaultCapable(true);
				//jPanelRegistrar.getRootPane().setDefaultButton(btnCrear);
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
				this.add(txtTelefonoMovil, new AnchorConstraint(256, 16, 805, 185, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtTelefonoMovil.setPreferredSize(new java.awt.Dimension(229, 22));
				txtTelefonoMovil.setName("txtTelefonoMovil");
			}
			{
				txtTelefonoFijo = new JTextField();
				this.add(txtTelefonoFijo, new AnchorConstraint(227, 16, 710, 185, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtTelefonoFijo.setPreferredSize(new java.awt.Dimension(229, 22));
				txtTelefonoFijo.setName("txtTelefonoFijo");
			}
			{
				txtCorreoElectronico = new JTextField();
				this.add(txtCorreoElectronico, new AnchorConstraint(200, 16, 612, 185, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtCorreoElectronico.setPreferredSize(new java.awt.Dimension(229, 22));
				txtCorreoElectronico.setName("txtCorreoElectronico");
			}
			{
				txtPuerta = new JTextField();
				this.add(txtPuerta, new AnchorConstraint(173, 961, 534, 388, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtPuerta.setPreferredSize(new java.awt.Dimension(25, 22));
				txtPuerta.setName("txtPuerta");
			}
			{
				txtPiso = new JTextField();
				this.add(txtPiso, new AnchorConstraint(173, 775, 534, 308, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtPiso.setPreferredSize(new java.awt.Dimension(25, 22));
				txtPiso.setName("txtPiso");
			}
			{
				txtNumero = new JTextField();
				this.add(txtNumero, new AnchorConstraint(173, 598, 534, 232, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtNumero.setPreferredSize(new java.awt.Dimension(25, 22));
				txtNumero.setName("txtNumero");
			}
			{
				txtDomicilio = new JTextField();
				this.add(txtDomicilio, new AnchorConstraint(146, 17, 514, 184, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtDomicilio.setPreferredSize(new java.awt.Dimension(229, 22));
				txtDomicilio.setName("txtDomicilio");
			}
			{
				dtcFechaNacimiento = new JDateChooser();
				this.add(dtcFechaNacimiento, new AnchorConstraint(119, 17, 408, 184, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				dtcFechaNacimiento.setPreferredSize(new java.awt.Dimension(229, 22));
				dtcFechaNacimiento.setSize(229, 22);
				dtcFechaNacimiento.setDateFormatString("dd/MM/yyyy");
				dtcFechaNacimiento.setToolTipText("Formato dd/MM/yyyy. Para más ayuda haga clic en el icono de la derecha.");
				dtcFechaNacimiento.setMaxSelectableDate(new Date());
				dtcFechaNacimiento.setName("dtcFechaNacimiento");
			}
			{
				txtApellidos = new JTextField();
				this.add(txtApellidos, new AnchorConstraint(92, 17, 416, 184, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtApellidos.setPreferredSize(new java.awt.Dimension(229, 22));
				txtApellidos.setName("txtApellidos");
			}
			{
				txtNombre = new JTextField();
				this.add(txtNombre, new AnchorConstraint(65, 17, 318, 184, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtNombre.setPreferredSize(new java.awt.Dimension(229, 22));
				txtNombre.setName("txtNombre");
			}
			{
				txtNSS = new JTextField();
				this.add(txtNSS, new AnchorConstraint(39, 17, 223, 184, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtNSS.setPreferredSize(new java.awt.Dimension(229, 22));
				txtNSS.setName("txtNSS");
			}
			{
				txtNIF = new JTextField();
				this.add(txtNIF, new AnchorConstraint(12, 17, 125, 184, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtNIF.setPreferredSize(new java.awt.Dimension(229, 22));
				txtNIF.setName("txtNIF");
			}
			{
				lblTelefonoMovil = new JLabel();
				this.add(lblTelefonoMovil, new AnchorConstraint(258, 245, 790, 13, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblTelefonoMovil.setText("Teléfono móvil");
				lblTelefonoMovil.setPreferredSize(new java.awt.Dimension(172, 14));
			}
			{
				lblTelefonoFijo = new JLabel();
				this.add(lblTelefonoFijo, new AnchorConstraint(231, 245, 692, 13, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblTelefonoFijo.setText("Teléfono fijo");
				lblTelefonoFijo.setPreferredSize(new java.awt.Dimension(172, 14));
			}
			{
				lblCorreoElectronico = new JLabel();
				this.add(lblCorreoElectronico, new AnchorConstraint(204, 245, 594, 13, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblCorreoElectronico.setText("Correo electrónico");
				lblCorreoElectronico.setPreferredSize(new java.awt.Dimension(172, 14));
			}
			{
				lblPuerta = new JLabel();
				this.add(lblPuerta, new AnchorConstraint(177, 905, 519, 339, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblPuerta.setText("Puerta");
				lblPuerta.setPreferredSize(new java.awt.Dimension(50, 14));
			}
			{
				lblPiso = new JLabel();
				this.add(lblPiso, new AnchorConstraint(177, 717, 525, 270, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblPiso.setText("Piso");
				lblPiso.setPreferredSize(new java.awt.Dimension(38, 14));
			}
			{
				lblNumero = new JLabel();
				this.add(lblNumero, new AnchorConstraint(177, 540, 515, 184, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblNumero.setText("Numero");
				lblNumero.setPreferredSize(new java.awt.Dimension(48, 14));
			}
			{
				lblDomicilio = new JLabel();
				this.add(lblDomicilio, new AnchorConstraint(150, 246, 496, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblDomicilio.setText("Domicilio");
				lblDomicilio.setPreferredSize(new java.awt.Dimension(172, 14));
			}
			{
				lblFechaNacimiento = new JLabel();
				this.add(lblFechaNacimiento, new AnchorConstraint(123, 246, 397, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblFechaNacimiento.setText("Fecha de nacimiento");
				lblFechaNacimiento.setPreferredSize(new java.awt.Dimension(172, 14));
			}
			{
				lblApellidos = new JLabel();
				this.add(lblApellidos, new AnchorConstraint(96, 246, 398, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblApellidos.setText("Apellidos");
				lblApellidos.setPreferredSize(new java.awt.Dimension(172, 14));
			}
			{
				lblNombre = new JLabel();
				this.add(lblNombre, new AnchorConstraint(69, 246, 303, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblNombre.setText("Nombre");
				lblNombre.setPreferredSize(new java.awt.Dimension(172, 14));
			}
			{
				lblNSS = new JLabel();
				this.add(lblNSS, new AnchorConstraint(42, 246, 209, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblNSS.setText("Número de Seguridad Social");
				lblNSS.setPreferredSize(new java.awt.Dimension(172, 14));
			}
			{
				lblNIF = new JLabel();
				this.add(lblNIF, new AnchorConstraint(15, 246, 110, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				lblNIF.setText("NIF");
				lblNIF.setPreferredSize(new java.awt.Dimension(172, 14));
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//$hide>>$
	
	private void btnCrearActionPerformed(ActionEvent evt) {
		Beneficiario beneficiario;
		Medico medico;
		
		try {
			
			// Comprobamos todos los campos
			Validacion.comprobarNIF(txtNIF.getText().trim());
			Validacion.comprobarNSS(txtNSS.getText().trim());
			Validacion.comprobarNombre(txtNombre.getText().trim());
			Validacion.comprobarApellidos(txtApellidos.getText().trim());
			Validacion.comprobarFechaNacimiento(dtcFechaNacimiento.getDate());
			Validacion.comprobarDomicilio(txtDomicilio.getText().trim());
			Validacion.comprobarNumero(txtNumero.getText().trim());
			if(!txtPiso.getText().equals("")) {
				Validacion.comprobarPiso(txtPiso.getText().trim());
			}
			if(!txtPuerta.getText().equals("")) {
				Validacion.comprobarPuerta(txtPuerta.getText().trim());
			}
			Validacion.comprobarCorreoElectronico(txtCorreoElectronico.getText().trim());
			Validacion.comprobarTelefonoFijo(txtTelefonoFijo.getText().trim());
			Validacion.comprobarTelefonoMovil(txtTelefonoMovil.getText().trim());

			// Creamos un nuevo beneficiario con los datos introducidos
			beneficiario = new Beneficiario();
			beneficiario.setNif(txtNIF.getText().trim().toUpperCase());
			beneficiario.setNss(txtNSS.getText().trim());
			beneficiario.setNombre(txtNombre.getText().trim());
			beneficiario.setApellidos(txtApellidos.getText().trim());
			beneficiario.setFechaNacimiento(dtcFechaNacimiento.getDate());
			beneficiario.setDomicilio(txtDomicilio.getText().trim() + ", nº " + txtNumero.getText().trim());
			if(!txtPiso.getText().equals("")) {
				beneficiario.setDomicilio(beneficiario.getDomicilio() + ", " + txtPiso.getText().trim() + "º");
			}
			if(!txtPuerta.getText().equals("")) {
				beneficiario.setDomicilio(beneficiario.getDomicilio() + " " + txtPuerta.getText().trim().toUpperCase());
			}
			beneficiario.setCorreo(txtCorreoElectronico.getText().trim());
			beneficiario.setTelefono(Integer.parseInt(txtTelefonoFijo.getText().trim()));
			beneficiario.setMovil(Integer.parseInt(txtTelefonoMovil.getText().trim()));

			// Solicitamos al servidor que se cree el beneficiario
			getControlador().crearBeneficiario(beneficiario);
			
			// Obtenemos el médico que se ha asignado al beneficiario
			medico = getControlador().getBeneficiario(beneficiario.getNif()).getMedicoAsignado();
			
			// Mostramos un mensaje indicando que el beneficiario se ha
			// creado correctamente y cuál es el médico asignado
			Dialogos.mostrarDialogoInformacion(getFrame(), "Operación correcta", "El beneficiario ha sido dado de alta en el sistema y se le ha asignado el siguiente médico:\n" + medico.getNombre() + " " + medico.getApellidos() + " (centro " + medico.getCentroSalud().getNombre() + ")");
			limpiarCamposRegistro();
			
		} catch(BeneficiarioYaExistenteException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getMessage());
			
		} catch(NIFIncorrectoException e) {
			txtNIF.selectAll();
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getMessage());
			txtNIF.grabFocus();
		} catch(NSSIncorrectoException e) {
			txtNSS.selectAll();
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getMessage());
			txtNSS.grabFocus();
		} catch(NombreIncorrectoException e) {
			txtNombre.selectAll();
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getMessage());
			txtNombre.grabFocus();
		} catch(ApellidoIncorrectoException e) {
			txtApellidos.selectAll();
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getMessage());
			txtApellidos.grabFocus();
		} catch(FormatoFechaIncorrectoException e) {
			((JTextField)dtcFechaNacimiento.getDateEditor()).selectAll();
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getMessage());
			dtcFechaNacimiento.grabFocus();
		} catch(FechaNacimientoIncorrectaException e) {
			((JTextField)dtcFechaNacimiento.getDateEditor()).selectAll();
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getMessage());
			dtcFechaNacimiento.grabFocus();
		} catch(DomicilioIncorrectoException e) {
			txtDomicilio.selectAll();
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getMessage());
			txtDomicilio.grabFocus();
		} catch(NumeroDomicilioIncorrectoException e) {
			txtNumero.selectAll();
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getMessage());
			txtNumero.grabFocus();
		} catch(PisoDomicilioIncorrectoException e) {
			txtPiso.selectAll();
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getMessage());
			txtPiso.grabFocus();
		} catch(PuertaDomicilioIncorrectoException e) {
			txtPuerta.selectAll();
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getMessage());
			txtPuerta.grabFocus();
		} catch(CorreoElectronicoIncorrectoException e) {
			txtCorreoElectronico.selectAll();
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getMessage());
			txtCorreoElectronico.grabFocus();
		} catch(TelefonoFijoIncorrectoException e) {
			txtTelefonoFijo.selectAll();
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getMessage());
			txtTelefonoFijo.grabFocus();
		} catch(TelefonoMovilIncorrectoException e) {
			txtTelefonoMovil.selectAll();
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getMessage());
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

	private void limpiarCamposRegistro() {
		txtNIF.setText("");
		txtNSS.setText("");
		txtNombre.setText("");
		txtApellidos.setText("");
		txtDomicilio.setText("");
		txtNumero.setText("");
		txtPiso.setText("");
		txtPuerta.setText("");
		dtcFechaNacimiento.setDate(null);
		txtCorreoElectronico.setText("");
		txtTelefonoFijo.setText("");
		txtTelefonoMovil.setText("");
	}
	
	//$hide<<$
	
}
