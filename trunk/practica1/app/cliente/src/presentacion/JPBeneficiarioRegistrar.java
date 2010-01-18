package presentacion;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;
import com.toedter.calendar.JDateChooser;
import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.Utilidades;
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

	public JPBeneficiarioRegistrar() {
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
				btnRestablecer = new JButton();
				this.add(btnRestablecer, new AnchorConstraint(302, 160, 961, 549, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
				btnRestablecer.setText("Restablecer");
				btnRestablecer.setPreferredSize(new java.awt.Dimension(120, 26));
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
			}
			{
				txtTelefonoFijo = new JTextField();
				this.add(txtTelefonoFijo, new AnchorConstraint(227, 16, 710, 185, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtTelefonoFijo.setPreferredSize(new java.awt.Dimension(229, 22));
			}
			{
				txtCorreoElectronico = new JTextField();
				this.add(txtCorreoElectronico, new AnchorConstraint(200, 16, 612, 185, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtCorreoElectronico.setPreferredSize(new java.awt.Dimension(229, 22));
			}
			{
				txtPuerta = new JTextField();
				this.add(txtPuerta, new AnchorConstraint(173, 961, 534, 388, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtPuerta.setPreferredSize(new java.awt.Dimension(25, 22));
			}
			{
				txtPiso = new JTextField();
				this.add(txtPiso, new AnchorConstraint(173, 775, 534, 308, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtPiso.setPreferredSize(new java.awt.Dimension(25, 22));
			}
			{
				txtNumero = new JTextField();
				this.add(txtNumero, new AnchorConstraint(173, 598, 534, 232, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtNumero.setPreferredSize(new java.awt.Dimension(25, 22));
			}
			{
				txtDomicilio = new JTextField();
				this.add(txtDomicilio, new AnchorConstraint(146, 17, 514, 184, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtDomicilio.setPreferredSize(new java.awt.Dimension(229, 22));
			}
			{
				dtcFechaNacimiento = new JDateChooser();
				this.add(dtcFechaNacimiento, new AnchorConstraint(119, 17, 408, 184, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				dtcFechaNacimiento.setPreferredSize(new java.awt.Dimension(229, 22));
				dtcFechaNacimiento.setSize(229, 22);
				dtcFechaNacimiento.setDateFormatString("dd/MM/yyyy");
				dtcFechaNacimiento.setToolTipText("Formato dd/MM/yyyy. Para más ayuda haga clic en el icono de la derecha.");
				dtcFechaNacimiento.setMaxSelectableDate(new Date());
			}
			{
				txtApellidos = new JTextField();
				this.add(txtApellidos, new AnchorConstraint(92, 17, 416, 184, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtApellidos.setPreferredSize(new java.awt.Dimension(229, 22));
			}
			{
				txtNombre = new JTextField();
				this.add(txtNombre, new AnchorConstraint(65, 17, 318, 184, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtNombre.setPreferredSize(new java.awt.Dimension(229, 22));
			}
			{
				txtNSS = new JTextField();
				this.add(txtNSS, new AnchorConstraint(39, 17, 223, 184, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtNSS.setPreferredSize(new java.awt.Dimension(229, 22));
			}
			{
				txtNIF = new JTextField();
				this.add(txtNIF, new AnchorConstraint(12, 17, 125, 184, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
				txtNIF.setPreferredSize(new java.awt.Dimension(229, 22));
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
		Beneficiario beneficiario = null;
		
		try {
			
			// Comprobamos todos los campos
			Utilidades.comprobarNIF(txtNIF.getText());
			Utilidades.comprobarNSS(txtNSS.getText());
			Utilidades.comprobarNombre(txtNombre.getText());
			Utilidades.comprobarApellidos(txtApellidos.getText());
			Utilidades.comprobarFecha(dtcFechaNacimiento.getDate());
			Utilidades.comprobarDomicilio(txtDomicilio.getText());
			Utilidades.comprobarNumero(txtNumero.getText());
			if(!txtPiso.getText().equals("")) Utilidades.comprobarPiso(txtPiso.getText());
			if(!txtPuerta.getText().equals("")) Utilidades.comprobarPuerta(txtPuerta.getText());
			Utilidades.comprobarCorreoElectronico(txtCorreoElectronico.getText());
			Utilidades.comprobarTelefonoFijo(txtTelefonoFijo.getText());
			Utilidades.comprobarTelefonoMovil(txtTelefonoMovil.getText());

			// Creamos un nuevo beneficiario con los datos introducidos
			beneficiario = new Beneficiario();
			beneficiario.setNif(txtNIF.getText().toUpperCase());
			beneficiario.setNss(txtNSS.getText());
			beneficiario.setNombre(txtNombre.getText());
			beneficiario.setApellidos(txtApellidos.getText());
			beneficiario.setFechaNacimiento(dtcFechaNacimiento.getDate());
			beneficiario.setDomicilio(txtDomicilio.getText() + ", nº " + txtNumero.getText());
			if(!txtPiso.getText().equals(""))
				beneficiario.setDomicilio(beneficiario.getDomicilio() + ", " + txtPiso.getText() + "º");
			if(!txtPuerta.getText().equals(""))
				beneficiario.setDomicilio(beneficiario.getDomicilio() + " " + txtPuerta.getText().toUpperCase());
			beneficiario.setCorreo(txtCorreoElectronico.getText());
			beneficiario.setTelefono(Integer.parseInt(txtTelefonoFijo.getText()));
			beneficiario.setMovil(Integer.parseInt(txtTelefonoMovil.getText()));

			// Solicitamos al servidor que se cree el beneficiario
			getControlador().crearBeneficiario(beneficiario);
			
			// El beneficiario se ha creado correctamente
			Dialogos.mostrarDialogoInformacion(getFrame(), "Operación correcta", "El beneficiario ha sido dado de alta en el sistema.");
			limpiarCamposRegistro();
			
		} catch(BeneficiarioYaExistenteException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.getMessage());			
		} catch(SQLException e) {
			System.out.println("Entra");
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.toString());
		} catch(RemoteException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.toString());
		
			
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
		} catch(FormatoFechaIncorrectoException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", "La fecha de nacimiento no está escrita en un formato válido (dd/MM/yyyy).");
		} catch(FechaNacimientoIncorrectaException e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", "La fecha de nacimiento no es válida.");
		} catch(DomicilioIncorrectoException e) {
			txtDomicilio.selectAll();
			Dialogos.mostrarDialogoError(getFrame(), "Error", "El domicilio sólo puede contener caracteres alfanuméricos.");
			txtDomicilio.grabFocus();
		} catch(NumeroDomicilioIncorrectoException e) {
			txtNumero.selectAll();
			Dialogos.mostrarDialogoError(getFrame(), "Error", "El número del domicilio debe ser un número entero.");
			txtNumero.grabFocus();
		} catch(PisoDomicilioIncorrectoException e) {
			txtPiso.selectAll();
			Dialogos.mostrarDialogoError(getFrame(), "Error", "El piso del domicilio debe ser un número entero.");
			txtPiso.grabFocus();
		} catch(PuertaDomicilioIncorrectoException e) {
			txtPuerta.selectAll();
			Dialogos.mostrarDialogoError(getFrame(), "Error", "La letra del domicilio debe ser un carácter alfabético.");
			txtPuerta.grabFocus();
		} catch(CorreoElectronicoIncorrectoException e) {
			txtCorreoElectronico.selectAll();
			Dialogos.mostrarDialogoError(getFrame(), "Error", "El formato del correo electrónico es incorrecto.");
			txtCorreoElectronico.grabFocus();
		} catch(TelefonoFijoIncorrectoException e) {
			txtTelefonoFijo.selectAll();
			Dialogos.mostrarDialogoError(getFrame(), "Error", "El teléfono fijo deben ser 9 digitos sin separadores y debe comenzar por 9.");
			txtTelefonoFijo.grabFocus();
		} catch(TelefonoMovilIncorrectoException e) {
			txtTelefonoMovil.selectAll();
			Dialogos.mostrarDialogoError(getFrame(), "Error", "El teléfono móvil deben ser 9 dígitos sin separadores y debe comenzar por 6.");
			txtTelefonoMovil.grabFocus();
			
		} catch(Exception e) {
			Dialogos.mostrarDialogoError(getFrame(), "Error", e.toString());
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
