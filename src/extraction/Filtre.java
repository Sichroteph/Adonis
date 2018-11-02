package extraction;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import org.jdom.Element;

import adonis.ArticleRSS;

/*
 * Created on 15 avr. 2005
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
public class Filtre extends Thread{
	private ArticleRSS article;
	private String texteArticle;
	private String auteur;
	
	static org.jdom.Document document;
	static Element racine;
	static List listItems; 
	
	public Filtre(ArticleRSS article){
		this.article = article;
	}
	
	public BufferedReader openUrl(String strUrl) throws Exception{
	    URL url;
	    BufferedReader br = null;
		url = new URL(strUrl);
		br = new BufferedReader(
					new InputStreamReader(
					url.openStream()));
	    return br;
	  }
	
	public void run() {
		texteArticle = "";
		auteur = "";
		//System.out.println("lien: " + article.getLien());
		BufferedReader br;
		String str = "";
	    String tmp;
	    String[] tmpStr = article.getLien().split("http://|http://www[.]|www[.]");
	    System.out.println(tmpStr[1]);
	    /*File f = new File("toto");
	    if (f.mkdir())
	    System.out.println("ok");
	    else
	    System.out.println("pas ok");*/
	/*	try {
			br = openUrl(article.lien);
			//crée une String avec le contenu du fichier
			while ((tmp = br.readLine()) != null)
			  str = str + tmp;
		} catch (Exception e) {
			e.printStackTrace();
		}
		String[] tokens = str.split("</?[^<>]*>");
		for(int i=0;i<tokens.length;i++){
			if(tokens[i].matches("[ \t\n\f\ra-zA-Z_0-9]*[.][ \t\n\f\ra-zA-Z_0-9]*"))
				texteArticle = tokens[i];
		}
		System.out.println(texteArticle);*/
	}
	
}
