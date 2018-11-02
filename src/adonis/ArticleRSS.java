/*
 * Created on 09-avr.-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package adonis;
import javax.swing.*;
/**
 * @author christophe 
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ArticleRSS {
	private String titre;
	private String date;
	private String description;
	private String lien;
	private ImageIcon logo;
	public ArticleRSS(String titre, String date, String desc, String lien, ImageIcon logo){
		this.titre=titre;
		this.date=date;
		this.description=desc;
		this.lien=lien;
		this.logo=logo;
	}
	/**
	 * @return
	 */
	public String getLien() {
		return lien;
	}
	
	/**
	 * @return Returns the date.
	 */
	public String getDate() {
		return date;
	}
	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @return Returns the logo.
	 */
	public ImageIcon getLogo() {
		return logo;
	}
	/**
	 * @return Returns the titre.
	 */
	public String getTitre() {
		return titre;
	}
}
