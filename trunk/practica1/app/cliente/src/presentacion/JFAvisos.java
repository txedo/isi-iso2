package presentacion;

import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;
import dominio.conocimiento.Beneficiario;
import dominio.conocimiento.Cita;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;

import presentacion.auxiliar.Dialogos;
import presentacion.auxiliar.TableCellRendererCitas;
import presentacion.auxiliar.UtilidadesTablas;

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
 * Ventana en la que se muestran avisos para los administradores y citadores.
 */
public class JFAvisos extends javax.swing.JFrame {

	private static final long serialVersionUID = 1172495276588177828L;
	
	private JPanel pnlPanel;
	private JLabel lblTitulo;
	private JScrollPane scpTablaAvisos;
	private JButton btnCerrar;
	private JTable tblTablaAvisos;
	
	public JFAvisos() {
		super();
		initGUI();
	}
	
	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			this.setTitle("Aviso");
			this.setMinimumSize(new java.awt.Dimension(400, 250));
			{
				pnlPanel = new JPanel();
				AnchorLayout pnlPanelLayout = new AnchorLayout();
				pnlPanel.setLayout(pnlPanelLayout);
				getContentPane().add(pnlPanel, BorderLayout.CENTER);
				pnlPanel.setPreferredSize(new java.awt.Dimension(584, 373));
				{
					btnCerrar = new JButton();
					pnlPanel.add(btnCerrar, new AnchorConstraint(904, 11, 11, 0, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE));
					btnCerrar.setText("Cerrar");
					btnCerrar.setPreferredSize(new java.awt.Dimension(101, 25));
					btnCerrar.setName("btnCerrar");
					btnCerrar.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							btnCerrarActionPerformed(evt);
						}
					});
				}
				{
					lblTitulo = new JLabel();
					pnlPanel.add(lblTitulo, new AnchorConstraint(12, 12, 108, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS));
					lblTitulo.setText("Mensaje");
					lblTitulo.setPreferredSize(new java.awt.Dimension(360, 16));
				}
				{
					scpTablaAvisos = new JScrollPane();
					pnlPanel.add(scpTablaAvisos, new AnchorConstraint(40, 12, 49, 12, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
					scpTablaAvisos.setPreferredSize(new java.awt.Dimension(560, 284));
					{
						tblTablaAvisos = new JTable();
						scpTablaAvisos.setViewportView(tblTablaAvisos);				
						tblTablaAvisos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
						tblTablaAvisos.setCellEditor(null);
						tblTablaAvisos.setDefaultRenderer(Object.class, new TableCellRendererCitas());
						tblTablaAvisos.setName("tblTablaAvisos");
					}
				}
			}
			pack();
			this.setSize(600, 350);
		} catch(Exception e) {
			Dialogos.mostrarDialogoError(this, "Error", e.getLocalizedMessage());
		}
	}
	
	//$hide>>$
	
	private void btnCerrarActionPerformed(ActionEvent evt) {
		setVisible(false);
		dispose();
	}

	// Métodos públicos
	
	public void mostrarBeneficiarios(String titulo, Vector<Beneficiario> beneficiarios) {
		// Mostramos la tabla de beneficiarios
		lblTitulo.setText(titulo);
		UtilidadesTablas.crearTablaBeneficiarios(tblTablaAvisos, beneficiarios.size());
		UtilidadesTablas.rellenarTablaBeneficiarios(tblTablaAvisos, beneficiarios);
		
		// Mostramos la ventana
		setVisible(true);
	}
	
	public void mostrarCitas(String titulo, Vector<Cita> citas) {
		// Mostramos la tabla de citas
		lblTitulo.setText(titulo);
		UtilidadesTablas.crearTablaCitasMedico(tblTablaAvisos, citas.size());
		UtilidadesTablas.rellenarTablaCitasMedico(tblTablaAvisos, citas);		
		
		// Mostramos la ventana
		setVisible(true);
	}

	//$hide<<$

}
