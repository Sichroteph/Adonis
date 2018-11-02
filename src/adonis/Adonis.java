package adonis;

import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.UIManager;
/*
 * Created on 11-avr.-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author christophe
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Adonis {
	public static void main(String args[]) throws Exception
	{	
		
		/*
		UIManager.LookAndFeelInfo[]  info = UIManager.getInstalledLookAndFeels() ;
		for(int i=0;i<info.length;i++)
			System.out.println(info[i]);
		*/
		
		
		try {
			//UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {try {
			//UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
		} catch (Exception e2) {System.out.println("probleme l&f"); }
		}
		
		
		
		Controleur ad = new Controleur();
		ad.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Image icone = Toolkit.getDefaultToolkit().getImage("icone.png");
		ad.setIconImage(icone);
		ad.setVisible(true);
		ad.extraitArticlesRSS();
	}
	
}
