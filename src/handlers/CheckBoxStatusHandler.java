package handlers;
import listeners.CheckBoxStatusListener;

/*
 * Created on 16 juin 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author Jonathan
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface CheckBoxStatusHandler {
	public void addCheckBoxStatusListener(CheckBoxStatusListener l);
	public void removeCheckBoxStatusListener(CheckBoxStatusListener l);
	public void notifyCheckBoxStatusListener(int x,int y, boolean isEnded);
}
