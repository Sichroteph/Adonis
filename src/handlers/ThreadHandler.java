package handlers;
import listeners.ThreadListener;


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
public interface ThreadHandler {
	public void addThreadListener(ThreadListener l);
	public void removeThreadListener(ThreadListener l);
	public void notifyEndThreadListener();

}
