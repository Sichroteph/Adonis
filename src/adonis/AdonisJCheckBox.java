
package adonis;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JCheckBox;

/*
 * Created on 15 avr. 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author Jonathan
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class AdonisJCheckBox extends JCheckBox {
	private boolean isSelected;

	private Vector checkboxes;

	public AdonisJCheckBox(String text) {
		super(text);
		isSelected = true;
	}

	public AdonisJCheckBox(String text, boolean selected, boolean premier) {
		super(text, selected);
		isSelected = selected;
		if (premier) {
			checkboxes = new Vector();
		} else
			checkboxes = null;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void select() {
		isSelected = true;
		if (checkboxes != null) {
			Iterator it = checkboxes.iterator();
			while (it.hasNext()) {
				AdonisJCheckBox acb = (AdonisJCheckBox) it.next();
				acb.select();
				acb.setSelected(true);
			}
		}
	}

	public void deselect() {
		isSelected = false;
		if (checkboxes != null) {
			Iterator it = checkboxes.iterator();
			while (it.hasNext()) {
				AdonisJCheckBox acb = (AdonisJCheckBox) it.next();
				acb.deselect();
				acb.setSelected(false);
			}
		}
	}

	public void ajouteCheckBox(AdonisJCheckBox c) {
		checkboxes.add(c);
	}

	/**
	 * @return Returns the checkboxes.
	 */
	public Vector getCheckboxes() {
		return checkboxes;
	}
}