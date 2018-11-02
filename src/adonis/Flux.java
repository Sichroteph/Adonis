/*
 * Created on 09-avr.-2005
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */

package adonis;

import java.io.File;
import java.net.URL;
import java.net.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.*;

import org.jdom.Element;
import org.jdom.input.SAXBuilder;


import extraction.EcritArticle;
import extraction.Extraction;

public class Flux {
	public Vector listeArticlesRSS;
	
	public Vector listeJCheckBox;
	
	Vector listeBouttons;
	
	Vector listeImagesXml;
	
	Vector listeImagesTxt;
	
	int nbArticles;
	
	int noFlux;
	
	public Flux(String url, int cpt) throws Exception {
		this.noFlux = cpt;
		listeArticlesRSS = new Vector();
		listeJCheckBox = new Vector();
		listeBouttons = new Vector();
		listeImagesXml = new Vector();
		listeImagesTxt = new Vector();
		SAXBuilder sxb = new SAXBuilder();
		
		//On crée un nouveau document JDOM avec en argument le fichier XML
		//Le parsing est terminé
		
		URL adresse = new URL(url);
		document = sxb.build(adresse);
		
		if (document != null) {
			//On initialise un nouvel élément racine avec l'élément racine du
			// document.
			racine = document.getRootElement();
			//Méthode définie dans la partie 3.2. de cet article
			enregistreInfosFlux();
		} else {
			System.out.println("Attention le lien " + url
					+ " ne semble pas être valide");
		}
	}
	
	public void enregistreInfosFlux() throws MalformedURLException {
		nbArticles = 0;
		racine = racine.getChild("channel");
		
		/*
		 * 
		 * Recuperation du nom et du theme du journal
		 *  
		 */
		String titre = new String(racine.getChild("title").getText());
		String description = "Pas de description disponible";
		if (racine.getChild("description") != null)
			description = new String(racine.getChild("description").getText());
		
		String lien = new String(racine.getChild("link").getText());
		
		String date = "Pas de date dispo";
		if (racine.getChild("pubDate") != null)
			date = new String(racine.getChild("pubDate").getText());
		if (racine.getChild("lastBuildDate") != null)
			date = new String(racine.getChild("lastBuildDate").getText());
		
		String urlLogo = "";
		ImageIcon logo = new ImageIcon();
		//Extraction du logo
		listItems = racine.getChildren("image");
		Iterator i = listItems.iterator();
		while (i.hasNext()) {
			Element courant = (Element) i.next();
			//On affiche le nom de l'element courant
			if (courant.getChild("url") != null)
				urlLogo = courant.getChild("url").getText();
			try {
				logo = createImageIcon(urlLogo);
				
			} catch (MalformedURLException e) {
				System.out.println("l'adresse de l'image logo est invalide.");
			}
		}
		
		ArticleRSS artRSS = new ArticleRSS(titre, date, description, lien, logo);
		ajoute(artRSS);
		JCheckBox jcb = new JCheckBox(titre);
		jcb.setName(String.valueOf(noFlux).toString());
		ajoute(jcb);
		
		JButton jb = new JButton("  Information  ");
		jb.setName(String.valueOf(noFlux).toString());
		ajoute(jb);
		
		JLabel logoTemp = new JLabel(logoVide);
		logoTemp.setName(String.valueOf(noFlux).toString());
		JLabel logoTemp2 = new JLabel(logoVide);
		logoTemp2.setName(String.valueOf(noFlux).toString());
		listeImagesXml.add(logoTemp);
		listeImagesTxt.add(logoTemp2);
		/*
		 * 
		 * Extracion des informations des articles
		 *  
		 */
		listItems = racine.getChildren("item");
		//On crée un Iterator sur notre liste
		i = listItems.iterator();
		while (i.hasNext()) {
			nbArticles++;
			titre = "Pas de titre disponible";
			description = "Pas de resumé disponible";
			date = "Pas de date disponible";
			lien = null;
			
			//On recrée l'Element courant à chaque tour de boucle afin de
			//pouvoir utiliser les méthodes propres aux Element comme :
			//selectionner un noeud fils, modifier du texte, etc...
			Element courant = (Element) i.next();
			//On affiche le nom de l'element courant
			if (courant.getChild("title") != null)
				titre = courant.getChild("title").getText();
			if (!titre.matches("[ \t\n\f\r]*(ADV:|adv:).*")) {
				if (courant.getChild("description") != null)
					description = nettoyageNom(courant.getChild("description")
							.getText());
				lien = courant.getChild("link").getText();
				if (courant.getChild("pubDate") != null)
					date = courant.getChild("pubDate").getText();
				artRSS = new ArticleRSS(titre, date, description, lien, logo);
				ajoute(artRSS);
				jcb = new JCheckBox(titre);
				jcb.setName(String.valueOf(noFlux).toString());
				ajoute(jcb);
				
				jb = new JButton("     Resumé     ");
				jb.setName(String.valueOf(noFlux).toString());
				ajoute(jb);
				
				logoTemp = new JLabel(logoVide);
				logoTemp.setToolTipText("Cliquez pour ouvrir le xml");
				logoTemp.setName(String.valueOf(noFlux).toString());
				logoTemp2 = new JLabel(logoVide);
				logoTemp2.setName(String.valueOf(noFlux).toString());
				logoTemp2.setToolTipText("Cliquez pour ouvrir le texte");
				
				listeImagesXml.add(logoTemp);
				listeImagesTxt.add(logoTemp2);
			}
		}
	}
	
	void ajoute(ArticleRSS artRSS) {
		listeArticlesRSS.add(artRSS);
	}
	
	void ajoute(JCheckBox jcb) {
		listeJCheckBox.add(jcb);
	}
	
	void ajoute(JButton jb) {
		listeBouttons.add(jb);
	}
	
	public int nbArticlesNouveaux(Parametres parametres) {
		String pathExtraction = parametres.pathExtraction;
		
		int nbArticlesNouveaux = 0;
		JCheckBox jcb;
		JLabel jLabel;
		ArticleRSS articleCourant = (ArticleRSS) listeArticlesRSS.get(0);
		
		// On coupe le http:// du lien
		String lien2 = articleCourant.getLien().substring(7);
		String lienCoupe[] = lien2.split("/");
		lien2 = lienCoupe[0];
		
		String pathCourant = pathExtraction + "/"
		+ Extraction.nettoyageAdresse(lien2) + "/"
		+ Extraction.nettoyageAdresse(articleCourant.getTitre());
		for (int i = 1; i < listeArticlesRSS.size(); i++) {
			articleCourant = (ArticleRSS) listeArticlesRSS.get(i);
			String tmp = pathCourant + "/"
			+ EcritArticle.nomDossierMois(articleCourant.getDate()) + "/"
			+ EcritArticle.noJour(articleCourant.getDate()) + "-"
			+ EcritArticle.nettoyageNom(articleCourant.getTitre());
			File f = new File(tmp + ".txt");
			File f1 = new File(tmp + ".xml");
			
			jcb = (JCheckBox) (listeJCheckBox.get(i));
			jcb.setEnabled(false);
			jcb.setForeground(new java.awt.Color(0, 0, 0));
			boolean xml = parametres.isExtracted2Xml;
			boolean txt = parametres.isExtracted2Txt;
			
			if (f.exists()) {
				jLabel = (JLabel) (listeImagesTxt.get(i));
				jLabel.setIcon(logoTxt);
			}
			if (f1.exists()) {
				jLabel = (JLabel) (listeImagesXml.get(i));
				jLabel.setIcon(logoXml);
			}
			
			if (((!(f1.exists())) && parametres.isExtracted2Xml)
					|| ((!(f.exists())) && parametres.isExtracted2Txt)) {
				nbArticlesNouveaux++;
				jcb = (JCheckBox) (listeJCheckBox.get(i));
				jcb.setEnabled(true);
			}
			
		}
		return nbArticlesNouveaux;
	}
	
	public static String nettoyageNom(String nom) {
		//nom=nom.replaceAll("&#39;","'");
		Pattern motif = Pattern.compile("&#39;");
		Matcher matcher = motif.matcher(nom);
		nom = matcher.replaceAll("'");
		
		motif = Pattern.compile("&#34;");
		matcher = motif.matcher(nom);
		nom = matcher.replaceAll("\"");
		
		return nom;
	}
	
	public ImageIcon createImageIcon(String path) throws MalformedURLException {
		URL imgURL = new URL(path);
		
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}
	
	public int reactualiseUneLigne(Parametres parametres, int i) {
		String pathExtraction = parametres.pathExtraction;
		JCheckBox jcb;
		JLabel jLabel;
		ArticleRSS articleCourant = (ArticleRSS) listeArticlesRSS.get(0);
		
		// On coupe le http:// du lien
		String lien2 = articleCourant.getLien().substring(7);
		String lienCoupe[] = lien2.split("/");
		lien2 = lienCoupe[0];
		
		String pathCourant = pathExtraction + "/"
		+ Extraction.nettoyageAdresse(lien2) + "/"
		+ Extraction.nettoyageAdresse(articleCourant.getTitre());
		
		articleCourant = (ArticleRSS) listeArticlesRSS.get(i);
		String tmp = pathCourant + "/"
		+ EcritArticle.nomDossierMois(articleCourant.getDate()) + "/"
		+ EcritArticle.noJour(articleCourant.getDate()) + "-"
		+ EcritArticle.nettoyageNom(articleCourant.getTitre());
		File f = new File(tmp + ".txt");
		File f1 = new File(tmp + ".xml");
		
		jcb = (JCheckBox) (listeJCheckBox.get(i));
		jcb.setEnabled(false);
		int estTelecharge = 1 ;
		jcb.setForeground(new java.awt.Color(0, 0, 0));
		boolean xml = parametres.isExtracted2Xml;
		boolean txt = parametres.isExtracted2Txt;
		
		if (f.exists()) {
			jLabel = (JLabel) (listeImagesTxt.get(i));
			jLabel.setIcon(logoTxt);
		}
		if (f1.exists()) {
			jLabel = (JLabel) (listeImagesXml.get(i));
			jLabel.setIcon(logoXml);
		}
		
		if (((!(f1.exists())) && parametres.isExtracted2Xml)
				|| ((!(f.exists())) && parametres.isExtracted2Txt)) {
			jcb = (JCheckBox) (listeJCheckBox.get(i));
			jcb.setEnabled(true);
			estTelecharge = 0 ;
		}
		return  estTelecharge;
	}
	
	
	static org.jdom.Document document;
	
	static Element racine;
	
	static List listItems;
	
	ImageIcon logoXml = new ImageIcon("xml.gif");
	
	ImageIcon logoTxt = new ImageIcon("txt.gif");
	
	ImageIcon logoVide = new ImageIcon("vide.gif");
	
}