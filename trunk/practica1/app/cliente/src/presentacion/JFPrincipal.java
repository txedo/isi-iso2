package presentacion;
import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;

import dominio.Beneficiario;
import dominio.ControladorLogin;
import dominio.Medico;
import dominio.Operacion;
import dominio.Usuario;
import excepciones.SesionInvalidaException;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import javax.swing.WindowConstants;
import javax.swing.SwingUtilities;


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
public class JFPrincipal extends javax.swing.JFrame {
	private ControladorLogin controlador;
	
	private JPanel jPanelOperaciones;
	private JPanel jPanelRegistrarBeneficiario;
	private JPanel jPanelTramitarCita;
	private JPanel jPanelModificarBeneficiario;
	private JLabel jLabel9;
	private JButton btnRestablecerCrearBeneficiario;
	private JButton btnCrearBeneficiario;
	private JTextField txtTelefonoMovil;
	private JTextField txtTelefonoFijo;
	private JPanel jPanelModificarUsuario;
	private JPanel jPanelEstablecerSustituto;
	private JPanel jPanelModificarCalendario;
	private JPanel jPanelConsultarMedico;
	private JPanel jPanelEliminarUsuario;
	private JPanel jPanelCrearUsuario;
	private JPanel jPanelConsultarBeneficiario;
	private JPanel jPanelEliminarCita;
	private JTextField txtCorreo;
	private JTextField txtDomicilio;
	private JTextField txtApellidos;
	private JTextField txtNombre;
	private JTextField txtNSS;
	private JLabel jLabel5;
	private JTextField txtNIF;
	private JLabel jLabel8;
	private JLabel jLabel7;
	private JLabel jLabel6;
	private JLabel jLabel4;
	private JLabel jLabel3;
	private JLabel jLabel2;
	private JLabel jLabel1;
	private JPanel jPanelBienvenida;
	private JTabbedPane jTabbedPaneOperaciones;

	/**
	* Auto-generated main method to display this JFrame
	*/
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFPrincipal inst = new JFPrincipal();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}
	
	public JFPrincipal() {
		super();
		initGUI();
		Object operaciones = null;
		System.out.println("hola");
		try {
			operaciones = controlador.operacionesDisponibles();
			System.out.println("adios");
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SesionInvalidaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (((ArrayList<Operacion>)operaciones).contains(Operacion.RegistrarBeneficiario))
			jPanelRegistrarBeneficiario.setVisible(true);
		else jPanelRegistrarBeneficiario.setVisible(false);
		if (((ArrayList<Operacion>)operaciones).contains(Operacion.ModificarBeneficiario))
			jPanelModificarBeneficiario.setVisible(true);
		else jPanelModificarBeneficiario.setVisible(false);
		if (((ArrayList<Operacion>)operaciones).contains(Operacion.ConsultarBeneficiario))
			jPanelConsultarBeneficiario.setVisible(true);
		else jPanelConsultarBeneficiario.setVisible(false);
		if (((ArrayList<Operacion>)operaciones).contains(Operacion.TramitarCita))
			jPanelTramitarCita.setVisible(true);
		else jPanelTramitarCita.setVisible(false);
		if (((ArrayList<Operacion>)operaciones).contains(Operacion.EliminarCita))
			jPanelEliminarCita.setVisible(true);
		else jPanelEliminarCita.setVisible(false);
		if (((ArrayList<Operacion>)operaciones).contains(Operacion.CrearUsuario))
			jPanelCrearUsuario.setVisible(true);
		else jPanelCrearUsuario.setVisible(false);
		if (((ArrayList<Operacion>)operaciones).contains(Operacion.ModificarUsuario))
			jPanelModificarUsuario.setVisible(true);
		else jPanelModificarUsuario.setVisible(false);
		if (((ArrayList<Operacion>)operaciones).contains(Operacion.EliminarUsuario))
			jPanelEliminarUsuario.setVisible(true);
		else jPanelEliminarUsuario.setVisible(false);
		if (((ArrayList<Operacion>)operaciones).contains(Operacion.ConsultarMedico))
			jPanelConsultarMedico.setVisible(true);
		else jPanelConsultarMedico.setVisible(false);
		if (((ArrayList<Operacion>)operaciones).contains(Operacion.ModificarCalendario))
			jPanelModificarCalendario.setVisible(true);
		else jPanelModificarCalendario.setVisible(false);
		if (((ArrayList<Operacion>)operaciones).contains(Operacion.EstablecerSustituto))
			jPanelEstablecerSustituto.setVisible(true);
		else jPanelEstablecerSustituto.setVisible(false);
	}
	
	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			this.setPreferredSize(new java.awt.Dimension(600, 380));
			{
				jPanelOperaciones = new JPanel();
				AnchorLayout jPanelOperacionesLayout = new AnchorLayout();
				getContentPane().add(jPanelOperaciones, BorderLayout.CENTER);
				jPanelOperaciones.setLayout(jPanelOperacionesLayout);
				jPanelOperaciones.setPreferredSize(new java.awt.Dimension(592, 323));
				{
					jTabbedPaneOperaciones = new JTabbedPane();
					jPanelOperaciones.add(jTabbedPaneOperaciones, new AnchorConstraint(1, 979, 962, 20, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
					jTabbedPaneOperaciones.setPreferredSize(new java.awt.Dimension(578, 292));
					{
						jPanelBienvenida = new JPanel();
						AnchorLayout jPanelBienvenidaLayout = new AnchorLayout();
						jTabbedPaneOperaciones.addTab("Inicio", null, jPanelBienvenida, null);
						jPanelBienvenida.setLayout(jPanelBienvenidaLayout);
						{
							jLabel9 = new JLabel();
							jPanelBienvenida.add(jLabel9, new AnchorConstraint(378, 581, 427, 386, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
							jLabel9.setText("Mensaje de bienvenida");
							jLabel9.setPreferredSize(new java.awt.Dimension(110, 14));
						}
					}
					{
						jPanelRegistrarBeneficiario = new JPanel();
						AnchorLayout jPanelRegistrarBeneficiarioLayout = new AnchorLayout();
						jTabbedPaneOperaciones.addTab("Registrar Beneficiario", null, jPanelRegistrarBeneficiario, null);
						jPanelRegistrarBeneficiario.setLayout(jPanelRegistrarBeneficiarioLayout);
						{
							btnRestablecerCrearBeneficiario = new JButton();
							jPanelRegistrarBeneficiario.add(btnRestablecerCrearBeneficiario, new AnchorConstraint(243, 191, 961, 549, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
							btnRestablecerCrearBeneficiario.setText("Restablecer");
							btnRestablecerCrearBeneficiario.setPreferredSize(new java.awt.Dimension(145, 25));
							btnRestablecerCrearBeneficiario.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									btnRestablecerCrearBeneficiarioActionPerformed(evt);
								}
							});
						}
						{
							btnCrearBeneficiario = new JButton();
							jPanelRegistrarBeneficiario.add(btnCrearBeneficiario, new AnchorConstraint(243, 17, 961, 765, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE));
							btnCrearBeneficiario.setText("Crear beneficiario");
							btnCrearBeneficiario.setPreferredSize(new java.awt.Dimension(155, 25));
							btnCrearBeneficiario.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									btnCrearBeneficiarioActionPerformed(evt);
								}
							});
						}
						{
							txtTelefonoMovil = new JTextField();
							jPanelRegistrarBeneficiario.add(txtTelefonoMovil, new AnchorConstraint(199, 17, 805, 184, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
							txtTelefonoMovil.setPreferredSize(new java.awt.Dimension(355, 22));
						}
						{
							txtTelefonoFijo = new JTextField();
							jPanelRegistrarBeneficiario.add(txtTelefonoFijo, new AnchorConstraint(173, 17, 710, 184, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
							txtTelefonoFijo.setPreferredSize(new java.awt.Dimension(355, 22));
						}
						{
							txtCorreo = new JTextField();
							jPanelRegistrarBeneficiario.add(txtCorreo, new AnchorConstraint(146, 17, 612, 184, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
							txtCorreo.setPreferredSize(new java.awt.Dimension(355, 22));
						}
						{
							txtDomicilio = new JTextField();
							jPanelRegistrarBeneficiario.add(txtDomicilio, new AnchorConstraint(119, 17, 514, 184, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
							txtDomicilio.setPreferredSize(new java.awt.Dimension(355, 22));
						}
						{
							txtApellidos = new JTextField();
							jPanelRegistrarBeneficiario.add(txtApellidos, new AnchorConstraint(92, 17, 416, 184, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
							txtApellidos.setPreferredSize(new java.awt.Dimension(355, 22));
						}
						{
							txtNombre = new JTextField();
							jPanelRegistrarBeneficiario.add(txtNombre, new AnchorConstraint(65, 17, 318, 184, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
							txtNombre.setPreferredSize(new java.awt.Dimension(355, 22));
						}
						{
							txtNSS = new JTextField();
							jPanelRegistrarBeneficiario.add(txtNSS, new AnchorConstraint(39, 17, 223, 184, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
							txtNSS.setPreferredSize(new java.awt.Dimension(355, 22));
						}
						{
							txtNIF = new JTextField();
							jPanelRegistrarBeneficiario.add(txtNIF, new AnchorConstraint(12, 17, 125, 184, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
							txtNIF.setPreferredSize(new java.awt.Dimension(355, 22));
						}
						{
							jLabel8 = new JLabel();
							jPanelRegistrarBeneficiario.add(jLabel8, new AnchorConstraint(203, 331, 790, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
							jLabel8.setText("Teléfono móvil");
							jLabel8.setPreferredSize(new java.awt.Dimension(172, 14));
						}
						{
							jLabel7 = new JLabel();
							jPanelRegistrarBeneficiario.add(jLabel7, new AnchorConstraint(176, 331, 692, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
							jLabel7.setText("Teléfono fijo");
							jLabel7.setPreferredSize(new java.awt.Dimension(172, 14));
						}
						{
							jLabel6 = new JLabel();
							jPanelRegistrarBeneficiario.add(jLabel6, new AnchorConstraint(149, 331, 594, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
							jLabel6.setText("Correo electrónico");
							jLabel6.setPreferredSize(new java.awt.Dimension(172, 14));
						}
						{
							jLabel5 = new JLabel();
							jPanelRegistrarBeneficiario.add(jLabel5, new AnchorConstraint(122, 331, 496, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
							jLabel5.setText("Domicilio");
							jLabel5.setPreferredSize(new java.awt.Dimension(172, 14));
						}
						{
							jLabel4 = new JLabel();
							jPanelRegistrarBeneficiario.add(jLabel4, new AnchorConstraint(95, 331, 398, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
							jLabel4.setText("Apellidos");
							jLabel4.setPreferredSize(new java.awt.Dimension(172, 14));
						}
						{
							jLabel3 = new JLabel();
							jPanelRegistrarBeneficiario.add(jLabel3, new AnchorConstraint(68, 331, 303, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
							jLabel3.setText("Nombre");
							jLabel3.setPreferredSize(new java.awt.Dimension(172, 14));
						}
						{
							jLabel2 = new JLabel();
							jPanelRegistrarBeneficiario.add(jLabel2, new AnchorConstraint(42, 299, 209, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
							jLabel2.setText("Número de Seguridad Social");
							jLabel2.setPreferredSize(new java.awt.Dimension(172, 15));
						}
						{
							jLabel1 = new JLabel();
							jPanelRegistrarBeneficiario.add(jLabel1, new AnchorConstraint(15, 331, 110, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
							jLabel1.setText("NIF");
							jLabel1.setPreferredSize(new java.awt.Dimension(172, 14));
						}
					}
					{
						jPanelModificarBeneficiario = new JPanel();
						AnchorLayout jPanelModificarBeneficiarioLayout = new AnchorLayout();
						jTabbedPaneOperaciones.addTab("Modificar Beneficiario", null, jPanelModificarBeneficiario, null);
						jPanelModificarBeneficiario.setLayout(jPanelModificarBeneficiarioLayout);
					}
					{
						jPanelConsultarBeneficiario = new JPanel();
						jTabbedPaneOperaciones.addTab("Consultar Beneficiario", null, jPanelConsultarBeneficiario, null);
						AnchorLayout jPanelConsultarBeneficiarioLayout = new AnchorLayout();
						jPanelConsultarBeneficiario.setLayout(jPanelConsultarBeneficiarioLayout);
					}
					{
						jPanelTramitarCita = new JPanel();
						AnchorLayout jPanelTramitarCitaLayout = new AnchorLayout();
						jTabbedPaneOperaciones.addTab("Tramitar Cita", null, jPanelTramitarCita, null);
						jPanelTramitarCita.setLayout(jPanelTramitarCitaLayout);
					}
					{
						jPanelEliminarCita = new JPanel();
						AnchorLayout jPanelEliminarCitaLayout = new AnchorLayout();
						jTabbedPaneOperaciones.addTab("Anular Cita", null, jPanelEliminarCita, null);
						jPanelEliminarCita.setLayout(jPanelEliminarCitaLayout);
					}
					{
						jPanelCrearUsuario = new JPanel();
						AnchorLayout jPanelCrearUsuarioLayout = new AnchorLayout();
						jTabbedPaneOperaciones.addTab("Crear Usuario", null, jPanelCrearUsuario, null);
						jPanelCrearUsuario.setLayout(jPanelCrearUsuarioLayout);
					}
					{
						jPanelModificarUsuario = new JPanel();
						AnchorLayout jPanelModificarUsuarioLayout = new AnchorLayout();
						jTabbedPaneOperaciones.addTab("Modificar Usuario", null, jPanelModificarUsuario, null);
						jPanelModificarUsuario.setLayout(jPanelModificarUsuarioLayout);
					}
					{
						jPanelEliminarUsuario = new JPanel();
						AnchorLayout jPanelEliminarUsuarioLayout = new AnchorLayout();
						jTabbedPaneOperaciones.addTab("Eliminar Usuario", null, jPanelEliminarUsuario, null);
						jPanelEliminarUsuario.setLayout(jPanelEliminarUsuarioLayout);
					}
					{
						jPanelConsultarMedico = new JPanel();
						AnchorLayout jPanelConsultarMedicoLayout = new AnchorLayout();
						jTabbedPaneOperaciones.addTab("Consultar Medico", null, jPanelConsultarMedico, null);
						jPanelConsultarMedico.setLayout(jPanelConsultarMedicoLayout);
					}
					{
						jPanelModificarCalendario = new JPanel();
						AnchorLayout jPanelModificarCalendarioLayout = new AnchorLayout();
						jTabbedPaneOperaciones.addTab("Modificar Calendario", null, jPanelModificarCalendario, null);
						jPanelModificarCalendario.setLayout(jPanelModificarCalendarioLayout);
					}
					{
						jPanelEstablecerSustituto = new JPanel();
						AnchorLayout jPanelEstablecerSustitutoLayout = new AnchorLayout();
						jTabbedPaneOperaciones.addTab("Establecer Sustituto", null, jPanelEstablecerSustituto, null);
						jPanelEstablecerSustituto.setLayout(jPanelEstablecerSustitutoLayout);
					}
				}
			}
			pack();
			this.setSize(600, 380);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void btnRestablecerCrearBeneficiarioActionPerformed(ActionEvent evt) {
		txtNIF.setText("");
		txtNSS.setText("");
		txtNombre.setText("");
		txtApellidos.setText("");
		txtDomicilio.setText("");
		txtCorreo.setText("");
		txtTelefonoFijo.setText("");
		txtTelefonoMovil.setText("");
	}
	
	private void btnCrearBeneficiarioActionPerformed(ActionEvent evt) {
		Beneficiario beneficiario = null;
		Usuario medico = null;
		boolean datosCorrectos = true;
		
		if (!Utilidades.comprobarNIF(txtNIF.getText())) {
			datosCorrectos = false;
		}
		if (!Utilidades.comprobarNSS(txtNSS.getText())){
			datosCorrectos = false;
		}
		if (!Utilidades.comprobarCadena(txtNombre.getText())) {
			datosCorrectos = false;
		}
		if (!Utilidades.comprobarCadena(txtApellidos.getText())) {
			datosCorrectos = false;
		}
		if (!Utilidades.comprobarCadena(txtDomicilio.getText())) {
			datosCorrectos = false;
		}
		if (!Utilidades.comprobarCorreoElectronico(txtCorreo.getText())) {
			datosCorrectos = false;
		}
		if (!Utilidades.comprobarCorreoElectronico(txtCorreo.getText())) {
			datosCorrectos = false;
		}
		if (!Utilidades.comprobarTelefono(txtTelefonoFijo.getText())) {
			datosCorrectos = false;
		}
		if (!Utilidades.comprobarTelefono(txtTelefonoMovil.getText())) {
			datosCorrectos = false;
		}

		if (datosCorrectos) {
			beneficiario = new Beneficiario();
			medico = new Medico();
			
			// TEST
			medico.setDni("12345678");
			// FIN DE TEST
			
			beneficiario.setNif(txtNIF.getText());
			beneficiario.setNss(txtNSS.getText());
			beneficiario.setNombre(txtNombre.getText());
			beneficiario.setApellidos(txtApellidos.getText());
			beneficiario.setDomicilio(txtDomicilio.getText());
			beneficiario.setCorreo(txtCorreo.getText());
			beneficiario.setTelefono(Integer.parseInt(txtTelefonoFijo.getText()));
			beneficiario.setMovil(Integer.parseInt(txtTelefonoMovil.getText()));
			beneficiario.setMedicoAsignado((Medico)medico);
			
			try {
				beneficiario.insertar();
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(this, "Error en la creacion del beneficiario:\n" + e.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
			}
		}
	}

	public void setControlador(ControladorLogin controlador) {
		this.controlador = controlador;
	}

}
