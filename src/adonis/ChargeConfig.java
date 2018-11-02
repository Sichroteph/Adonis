/*
 * Created on 11-avr.-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package adonis;
import java.io.*;
import java.util.*;
/**
 * @author christophe 
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ChargeConfig {
	Vector listeAdressesRss;
	
	public ChargeConfig() throws Exception
	{ 
		listeAdressesRss = new Vector();
		
		File inputFile = new File("Adresses_Flux_RSS.txt");
		BufferedReader in = new BufferedReader(new InputStreamReader(
				new FileInputStream(inputFile)));
		
		String url = in.readLine();
		while (url!=null)
		{		
			// L'adresse doit comporter un // , mais pas en premiere
			// position (sinon c'est un commentaire)
			if ((url.indexOf("//")!=0)&&(url.indexOf("//")!=-1))
				listeAdressesRss.add(url);
			url = in.readLine();
		}		
	}
	
	
}

