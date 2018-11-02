package extraction;


import handlers.ThreadHandler;

import java.io.*;
import java.util.Iterator;
import java.util.Vector;

import listeners.ThreadListener;

import adonis.ArticleRSS;

/*
 * Created on 15 juin 2005
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
public class EcritArticle extends Thread implements ThreadHandler {
	
	private ArticleRSS article;
	
	private String path;
	
	private boolean xml;
	
	private boolean txt;
	
	private boolean isBalisesPhrases;
	
	private String texteArticle;
	
	private String auteur;
	
	private int i;
	
	private int j;
	
	private Vector endThreadListeners;
	
	public EcritArticle(ArticleRSS article, String path, String texteArticle,
			String auteur, boolean xml, boolean txt, boolean isBalisesPhrases, int i, int j) {
		this.article = article;
		this.path = path;
		this.xml = xml;
		this.txt = txt;
		this.isBalisesPhrases=isBalisesPhrases;
		this.texteArticle = texteArticle;
		this.auteur = auteur;
		this.i = i;
		this.j = j;
		
		endThreadListeners = new Vector();
	}
	
	public void run() {
		try {
			PrintWriter writer;
			path = path + "/" + nomDossierMois(article.getDate());
			File fich = new File(path);
			fich.mkdirs();
			
			if (txt) {
				writer = new PrintWriter(transfoISO8859(path + "/" + noJour(article.getDate())
						+ "-" + nettoyageNom(article.getTitre()) + ".txt"));
				
				
				writer.write(traitementTxt(texteArticle));
				writer.close();
			}
			if (xml) {
				writer = new PrintWriter(transfoISO8859(path + "/" + noJour(article.getDate())
						+ "-" + nettoyageNom(article.getTitre()) + ".xml"));
				
				writer.write(traitementXml(texteArticle));
				
				writer.close();
			}
			
			// fin thread
			notifyEndThreadListener();
			
		} catch (Exception e) {
			e.printStackTrace();
			notifyRelanceThread();
		}
	}
	
	private String traitementXml(String texte) {
		String stringXml="<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>";
		
		stringXml+="<article>\n";
		stringXml+="<date>";
		stringXml+=article.getDate();
		stringXml+="</date>\n";
		stringXml+="<auteur>";
		stringXml+=auteur;
		stringXml+="</auteur>\n";
		stringXml+="<titre>";
		stringXml+=article.getTitre();
		stringXml+="</titre>\n";
		stringXml+="<resume>";
		stringXml+=article.getDescription();
		stringXml+="</resume>\n";
		stringXml+="<texte>\n";
		if(isBalisesPhrases){
			stringXml+="<phrase>";
			stringXml +=(texte.replace("\"","")).replace(". ", ".</phrase>\n<phrase>");
			stringXml+="</phrase>\n";
			stringXml +=stringXml.replace("<phrase></phrase>", "");
			stringXml = stringXml.substring(0,(stringXml.length()/2) - 1);
		}
		else
			stringXml += texte.replace(". ", ".\n\n");
		
		stringXml+="</texte>\n";
		stringXml+="</article>\n";
		
		return transfoISO8859(stringXml);
	}
	
	
	// Fait des retours à la ligne en fin de phrase pour
	// plus de lisibilité
	private String traitementTxt(String texte) {
		texte = texte.replace(". ", ".\n\n");
		return (texte);
	}
	
	
	public void addThreadListener(ThreadListener l) {
		endThreadListeners.add(l);
	}
	
	public void removeThreadListener(ThreadListener l) {
		endThreadListeners.remove(l);
	}
	
	public void notifyEndThreadListener() {
		Iterator iter = endThreadListeners.iterator();
		while (iter.hasNext()) {
			ThreadListener listener = (ThreadListener) iter.next();
			
			listener.endThread(i, j);
		}
	}
	
	/**
	 * @param endThreadListeners
	 *            The endThreadListeners to set.
	 */
	public void setThreadListeners(Vector endThreadListeners) {
		this.endThreadListeners = endThreadListeners;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see EndThreadHandler#notifyRelanceThread()
	 */
	public void notifyRelanceThread() {
		Iterator iter = endThreadListeners.iterator();
		while (iter.hasNext()) {
			ThreadListener listener = (ThreadListener) iter.next();
			listener.relanceThread();
		}
	}
	private String transfoISO8859 (String texte){
		String stringISO = "";
		try {
			stringISO = new String(texte.getBytes(), "ISO-8859-1");
			
			
		} catch (java.io.UnsupportedEncodingException e) {
			// Le codage n'est pas reconnu.
			e.printStackTrace();
		}
		return stringISO;
	}
	
	
	
	//	Enleve les caracteres " : du path
	public static String nettoyageNom(String nom) {
		String[] nomDecompose = nom.split(" ");
		String nomFinal = "";
		if (nomDecompose.length >= 5) {
			for (int j = 0; j < 4; j++) {
				nomFinal += nomDecompose[j] + "_";
			}
			nomFinal += nomDecompose[4];
		}
		else{
			for (int j = 0; j < nomDecompose.length - 1; j++) {
				nomFinal += nomDecompose[j] + "_";
			}
			nomFinal += nomDecompose[nomDecompose.length - 1];
		}
		return nomFinal.replaceAll("(\"|[<>/*:?|]|\\|\\]|\\]|[ ])+", "_");
	}
	
	public static String noJour(String date) {
		if (!date.matches("Pas.*")) {
			String[] elementsDate = date.split(" ");
			return elementsDate[1];
		} else {
			return "";
		}
	}
	
	public static String nomDossierMois(String date) {
		String[] elementsDate = date.split(" ");
		if (elementsDate[2].compareToIgnoreCase("jan") == 0) {
			return "Janvier_" + elementsDate[3];
		} else if (elementsDate[2].compareToIgnoreCase("feb") == 0) {
			return "Fevrier_" + elementsDate[3];
		} else if (elementsDate[2].compareToIgnoreCase("mar") == 0) {
			return "Mars_" + elementsDate[3];
		} else if (elementsDate[2].compareToIgnoreCase("apr") == 0) {
			return "Avril_" + elementsDate[3];
		} else if (elementsDate[2].compareToIgnoreCase("may") == 0) {
			return "Mai_" + elementsDate[3];
		} else if (elementsDate[2].compareToIgnoreCase("jun") == 0) {
			return "Juin_" + elementsDate[3];
		} else if (elementsDate[2].compareToIgnoreCase("jul") == 0) {
			return "Juillet_" + elementsDate[3];
		} else if (elementsDate[2].compareToIgnoreCase("aug") == 0) {
			return "Aout_" + elementsDate[3];
		} else if (elementsDate[2].compareToIgnoreCase("sep") == 0) {
			return "Septembre_" + elementsDate[3];
		} else if (elementsDate[2].compareToIgnoreCase("oct") == 0) {
			return "Octobre_" + elementsDate[3];
		} else if (elementsDate[2].compareToIgnoreCase("nov") == 0) {
			return "Novembre_" + elementsDate[3];
		} else if (elementsDate[2].compareToIgnoreCase("dec") == 0) {
			return "Decembre_" + elementsDate[3];
		} else {
			return "Sans_Date";
		}
	}
	
}