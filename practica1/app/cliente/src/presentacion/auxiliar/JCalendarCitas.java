package presentacion.auxiliar;

import java.awt.BorderLayout;
import com.toedter.calendar.JCalendar;

/**
 * Implementación del JCalendar utilizada por el control JDateChooserCitas.
 */
public class JCalendarCitas extends JCalendar {
	
	private static final long serialVersionUID = -5988604736883612453L;
	
	public JCalendarCitas() {
		super();
		// Eliminamos la configuración del selector antiguo
		remove(dayChooser);
		// Creamos y configuramos el selector nuevo
		dayChooser = new JDayChooserCitas(weekOfYearVisible);
		dayChooser.addPropertyChangeListener(this);
		monthChooser.setDayChooser(dayChooser);
		monthChooser.addPropertyChangeListener(this);
		yearChooser.setDayChooser(dayChooser);
		yearChooser.addPropertyChangeListener(this);
		add(dayChooser, BorderLayout.CENTER);
	}
	
}

