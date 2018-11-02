
package extraction;
import handlers.CheckBoxStatusHandler;

import java.util.Iterator;
import java.util.Vector;

import javax.swing.JCheckBox;

import adonis.ArticleRSS;
import adonis.Flux;
import adonis.Parametres;

import listeners.CheckBoxStatusListener;
import listeners.ThreadListener;

/*
 * Created on 23-avr.-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author christophe
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class Extraction implements ThreadListener, CheckBoxStatusHandler {

	private String path;

	private Vector ensembleFlux;

	private boolean xml;

	private boolean txt;

	boolean isBalisesPhrases;
	private int nbThreads;

	private int i;

	private int j;

	private int tailleEnsembleFlux;

	private Vector checkBoxStatusListeners = new Vector();

	public Extraction(Parametres parametres, Vector ensembleFlux)
			throws Exception {
		this.path = parametres.pathExtraction;
		this.ensembleFlux = ensembleFlux;
		this.xml = parametres.isExtracted2Xml;
		this.txt = parametres.isExtracted2Txt;
		this.isBalisesPhrases=parametres.isBalisesPhrases;
		this.nbThreads = parametres.nbThreadsExtraction;

		
		tailleEnsembleFlux = ensembleFlux.size();
		int nbThreadLancees = 0;
		i = 0;
		j = 1;
		while ((nbThreadLancees < nbThreads) && i < tailleEnsembleFlux) {
			//System.out.println("entree: i=" + i);
			ArticleRSS articleCourant = (ArticleRSS) (((Flux) ensembleFlux
					.get(i)).listeArticlesRSS).get(0);

			// On coupe le http:// du lien
			String lien2 = articleCourant.getLien().substring(7);
			String lienCoupe[] = lien2.split("/");
			lien2 = lienCoupe[0];
			String pathExtraction;
			pathExtraction = path + "/" + nettoyageAdresse(lien2) + "/"
					+ nettoyageAdresse(articleCourant.getTitre());
			// On nettoie le path des caracteres interdits
			Flux tmp;
			//System.out.println("avant boucle: i=" + i);
			while ((nbThreadLancees < nbThreads)
					&& (j < ((tmp = (Flux) ensembleFlux.get(i))).listeArticlesRSS
							.size())) {
				if (((JCheckBox) ((Vector) tmp.listeJCheckBox).elementAt(j))
						.isSelected()) {
					articleCourant = (ArticleRSS) (((Flux) ensembleFlux.get(i)).listeArticlesRSS)
							.get(j);
					ExtraitArticle ea = new ExtraitArticle(
							articleCourant,
							pathExtraction, xml, txt, isBalisesPhrases,i, j);
					ea.addThreadListener(this);
					ea.start();
					nbThreadLancees++;
				}
				j++;
				//System.out.println("sortie boucle j = " + j);
			}
			if (j >= ((Flux) ensembleFlux.get(i)).listeArticlesRSS.size()) {
				i++;
				j = 1;
			}
		}
		//System.out.println("sortie boucle def i =" + i + ", j=" + j);
	}

	public static String nettoyageAdresse(String adresse){
		return adresse.replaceAll("(\"|[<>/*:?|]|\\|\\]|\\]|[ ])+", "_");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see EndThreadListener#endThread()
	 */

	public synchronized void beginThread(int i, int j) {
		notifyCheckBoxStatusListener(i, j, false);

	}

	public synchronized void endThread(int x, int y) {
		notifyCheckBoxStatusListener(x, y, true);
		relanceThread();
	}

	public synchronized void relanceThread() {
		if (i < tailleEnsembleFlux) {
			ArticleRSS articleCourant = (ArticleRSS) (((Flux) ensembleFlux
					.get(i)).listeArticlesRSS).get(0);
			// On coupe le http:// du lien
			String lien2 = articleCourant.getLien().substring(7);
			String lienCoupe[] = lien2.split("/");
			lien2 = lienCoupe[0];
			//On nettoie le path des caracteres interdits
			String pathExtraction = path + "/" + nettoyageAdresse(lien2) + "/"
					+ nettoyageAdresse(articleCourant.getTitre());
			Flux tmp;

			if (j < ((tmp = (Flux) ensembleFlux.get(i)).listeArticlesRSS.size())) {
				if (!(((JCheckBox) ((Vector) tmp.listeJCheckBox).elementAt(j))
						.isSelected())) {
					j++;
					relanceThread();
				} else {
					articleCourant = (ArticleRSS) (((Flux) ensembleFlux.get(i)).listeArticlesRSS)
							.get(j);
					ExtraitArticle ea = new ExtraitArticle(
							(ArticleRSS) (tmp.listeArticlesRSS).get(j),
							pathExtraction, xml, txt, isBalisesPhrases, i, j);
					ea.addThreadListener(this);
					ea.start();

					j++;
				}
			} else {
				if (i + 1 < tailleEnsembleFlux) {
					i++;
					j = 1;
					relanceThread();
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see CheckBoxDisabledHandler#addCheckBoxDisabledListener(CheckBoxDisabledListener)
	 */
	public void addCheckBoxStatusListener(CheckBoxStatusListener l) {
		checkBoxStatusListeners.add(l);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see CheckBoxDisabledHandler#removeCheckBoxDisabledListener(CheckBoxDisabledListener)
	 */
	public void removeCheckBoxStatusListener(CheckBoxStatusListener l) {
		checkBoxStatusListeners.remove(l);
	}

	public void notifyCheckBoxStatusListener(int x, int y, boolean isEnded) {
		Iterator iter = checkBoxStatusListeners.iterator();
		while (iter.hasNext()) {
			CheckBoxStatusListener listener = (CheckBoxStatusListener) iter
					.next();

			// lance listener
			if (isEnded == true)
				listener.checkBoxdisable(x, y);
			else
				listener.checkBoxEncours(x, y);

		}
	}

}