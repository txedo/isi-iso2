package pruebas;

import java.util.Vector;
import javax.swing.JList;
import org.uispec4j.Button;
import org.uispec4j.ListBox;
import org.uispec4j.Panel;
import org.uispec4j.Trigger;
import org.uispec4j.Window;
import org.uispec4j.interception.WindowInterceptor;
import presentacion.JFCalendarioLaboral;
import dominio.conocimiento.DiaSemana;
import dominio.conocimiento.PeriodoTrabajo;

/**
 * Pruebas de la ventana de consulta y edición del calendario laboral de los médicos.
 */
public class PruebasJFCalendarioLaboral  extends org.uispec4j.UISpecTestCase implements IDatosConexionPruebas {

	private Panel pnlCalendario;
	private Panel pnlCalendarioConsultar;
	private Button btnAceptar;
	private Button btnRestablecer;
	private Button btnRestablecerTodo;
	private Button btnPropagar;
	private ListBox lstDiasSemana;

	private JFCalendarioLaboral frmCalendario;
	private JList jlstDiasSemana;
	
	private PeriodoTrabajo periodo1;
	private Vector<PeriodoTrabajo> periodos;
	
	protected void setUp() {
		periodos = new Vector<PeriodoTrabajo>();
		
		frmCalendario = new JFCalendarioLaboral();
		pnlCalendario = new Panel(frmCalendario);
		pnlCalendarioConsultar = pnlCalendario.getPanel("pnlConsultarCalendario");
		lstDiasSemana = pnlCalendarioConsultar.getListBox("lstDiasSemana");
		btnAceptar = pnlCalendario.getButton("btnAceptar");
		btnRestablecer = pnlCalendario.getButton("btnRestablecer");
		btnRestablecerTodo = pnlCalendario.getButton("btnRestablecerTodo");
		btnPropagar = pnlCalendarioConsultar.getButton("btnPropagar");
		
		jlstDiasSemana = (JList) lstDiasSemana.getAwtComponent();
		
	}
	
	protected void tearDown() {
		frmCalendario.dispose();
		periodos = new Vector<PeriodoTrabajo>();
	}
	
	public void testMostrarCalendarioVacio() {
		try {			
			Window win;
			win = WindowInterceptor.run(new Trigger() {
				public void run() {
					try {
						// Al iniciar la ventana, el número de horas debe ser 0
						frmCalendario.setVisible(true);
						assertEquals(frmCalendario.getHorasSemanales(), 0);
						// No puede haber periodos de trabajo en el calendario (vector vacio)
						assertEquals(frmCalendario.getPeriodosTrabajo(), periodos);
						// Botones activados por defecto
						assertTrue(btnAceptar.isEnabled());
						assertFalse(btnRestablecer.isEnabled());
						assertFalse(btnRestablecerTodo.isEnabled());
						assertTrue(btnPropagar.isEnabled());
						// Se selecciona por defecto el primer dia
						assertTrue(jlstDiasSemana.getSelectedIndex()==0);
						btnAceptar.click();
					} catch(Exception e) {
						fail(e.toString());
					}
				}
			});
			win.dispose();
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	public void testRestablecerTodo() {
		// Creamos un periodo de trabajo
		periodo1 = new PeriodoTrabajo(9, 10, DiaSemana.Lunes);
		periodos.add(periodo1);
		try {			
			Window win;
			win = WindowInterceptor.run(new Trigger() {
				public void run() {
					try {
						frmCalendario.setVisible(true);
						// Establecemos el periodo de trabajo
						frmCalendario.setPeriodosTrabajo(periodos);
						assertEquals(frmCalendario.getHorasSemanales(), 1);
						assertEquals(frmCalendario.getPeriodosTrabajo(), periodos);
						// Botones activados por defecto
						assertTrue(btnAceptar.isEnabled());
						assertFalse(btnRestablecer.isEnabled());
						assertFalse(btnRestablecerTodo.isEnabled());
						assertTrue(btnPropagar.isEnabled());
						// Se selecciona por defecto el primer dia
						assertTrue(jlstDiasSemana.getSelectedIndex()==0);
						// Ponemos el calendario modificable
						frmCalendario.setModificable(true);
						// Se deden haber habilitado los botones
						assertTrue(btnRestablecer.isEnabled());
						assertTrue(btnRestablecerTodo.isEnabled());
						// Al pulsar el botón, se restablece todo el calendario
						btnRestablecerTodo.click();
						assertEquals(frmCalendario.getHorasSemanales(), 0);
						periodos = new Vector<PeriodoTrabajo>();
						assertEquals(frmCalendario.getPeriodosTrabajo(), periodos);
						btnAceptar.click();
					} catch(Exception e) {
						fail(e.toString());
					}
				}
			});
			win.dispose();
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	public void testRestablecer1() {
		// Creamos un periodo de trabajo
		periodo1 = new PeriodoTrabajo(9, 10, DiaSemana.Lunes);
		periodos.add(periodo1);
		try {			
			Window win;
			win = WindowInterceptor.run(new Trigger() {
				public void run() {
					try {
						frmCalendario.setVisible(true);
						// Establecemos el periodo de trabajo
						frmCalendario.setPeriodosTrabajo(periodos);
						assertEquals(frmCalendario.getHorasSemanales(), 1);
						assertEquals(frmCalendario.getPeriodosTrabajo(), periodos);
						// Botones activados por defecto
						assertTrue(btnAceptar.isEnabled());
						assertFalse(btnRestablecer.isEnabled());
						assertFalse(btnRestablecerTodo.isEnabled());
						assertTrue(btnPropagar.isEnabled());
						// Se selecciona por defecto el primer dia
						assertTrue(jlstDiasSemana.getSelectedIndex()==0);
						// Ponemos el calendario modificable
						frmCalendario.setModificable(true);
						// Se deden haber habilitado los botones
						assertTrue(btnRestablecer.isEnabled());
						assertTrue(btnRestablecerTodo.isEnabled());
						// Al pulsar el botón, se restablece el dia seleccionado por defecto, que coincide
						// con el periodo de trabajo introducido
						btnRestablecer.click();
						assertEquals(frmCalendario.getHorasSemanales(), 0);
						periodos = new Vector<PeriodoTrabajo>();
						assertEquals(frmCalendario.getPeriodosTrabajo(), periodos);
						btnAceptar.click();
					} catch(Exception e) {
						fail(e.toString());
					}
				}
			});
			win.dispose();
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	public void testRestablecer2() {
		// Creamos un periodo de trabajo en otro día 
		periodo1 = new PeriodoTrabajo(10, 16, DiaSemana.Martes);
		periodos.add(periodo1);
		try {			
			Window win;
			win = WindowInterceptor.run(new Trigger() {
				public void run() {
					try {
						frmCalendario.setVisible(true);
						// Establecemos el periodo de trabajo
						frmCalendario.setPeriodosTrabajo(periodos);
						assertEquals(frmCalendario.getHorasSemanales(), 6);
						assertEquals(frmCalendario.getPeriodosTrabajo(), periodos);
						// Botones activados por defecto
						assertTrue(btnAceptar.isEnabled());
						assertFalse(btnRestablecer.isEnabled());
						assertFalse(btnRestablecerTodo.isEnabled());
						assertTrue(btnPropagar.isEnabled());
						// Se selecciona por defecto el primer dia
						assertTrue(jlstDiasSemana.getSelectedIndex()==0);
						// Ponemos el calendario modificable
						frmCalendario.setModificable(true);
						// Se deden haber habilitado los botones
						assertTrue(btnRestablecer.isEnabled());
						assertTrue(btnRestablecerTodo.isEnabled());
						// Al pulsar el botón, se restablece el dia seleccionado por defecto, que no coincide
						// con el periodo de trabajo introducido
						btnRestablecer.click();
						assertEquals(frmCalendario.getHorasSemanales(), 6);
						// Seleccionamos el día del periodo de trabajo introducido
						jlstDiasSemana.setSelectedIndex(1);
						btnRestablecer.click();
						periodos = new Vector<PeriodoTrabajo>();
						assertEquals(frmCalendario.getPeriodosTrabajo(), periodos);
						btnAceptar.click();
					} catch(Exception e) {
						fail(e.toString());
					}
				}
			});
			win.dispose();
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	public void testPropagar() {
		// Creamos periodos de trabajo
		periodos = new Vector<PeriodoTrabajo>();
		periodo1 = new PeriodoTrabajo(10, 16, DiaSemana.Martes);
		periodos.add(periodo1);
		try {			
			Window win;
			win = WindowInterceptor.run(new Trigger() {
				public void run() {
					try {
						frmCalendario.setVisible(true);
						// Establecemos los periodos de trabajo
						frmCalendario.setPeriodosTrabajo(periodos);
						assertEquals(frmCalendario.getHorasSemanales(), 6);
						assertEquals(frmCalendario.getPeriodosTrabajo(), periodos);
						// Botones activados por defecto
						assertTrue(btnAceptar.isEnabled());
						assertFalse(btnRestablecer.isEnabled());
						assertFalse(btnRestablecerTodo.isEnabled());
						assertTrue(btnPropagar.isEnabled());
						// Se selecciona por defecto el primer dia
						assertTrue(jlstDiasSemana.getSelectedIndex()==0);
						// Propagamos las horas del día seleccionado por defecto al resto de paneles
						btnPropagar.click();
						// Como el dia por defecto es Lunes y no hay ninguna hora, el resultado de la
						// propagación debe ser 0 horas
						assertEquals(frmCalendario.getHorasSemanales(), 0);
						// Seleccionamos el Martes, que tiene horas asignadas
						jlstDiasSemana.setSelectedIndex(1);
						frmCalendario.setPeriodosTrabajo(periodos);
						btnPropagar.click();
						// Ahora deben haberse propagado las horas del Martes
						assertEquals(frmCalendario.getHorasSemanales(), 30);
						assertTrue(frmCalendario.getPeriodosTrabajo().size()==5);
						btnAceptar.click();
					} catch(Exception e) {
						fail(e.toString());
					}
				}
			});
			win.dispose();
		} catch(Exception e) {
			fail(e.toString());
		}
	}

}
