
package extraction;
import handlers.ThreadHandler;

import java.io.BufferedReader;
//import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import listeners.ThreadListener;

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
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class ExtraitArticle extends Thread implements ThreadHandler {
	private ArticleRSS article;

	private String path;
	
	private boolean xml;
	
	private boolean txt;
	
	private boolean isBalisesPhrases;
	
	private int i;
	
	private int j;
	
	static org.jdom.Document document;

	static Element racine;

	static List listItems;
	
	Vector endThreadListeners = new Vector();

	public ExtraitArticle(ArticleRSS article,String path,boolean xml,boolean txt,boolean isBalisesPhrases, int i,int j) {
		this.article = article;
		this.path = path;
		this.xml = xml;
		this.txt = txt;
		this.isBalisesPhrases=isBalisesPhrases;
		this.i = i;
		this.j=j;
		//System.out.println("extraitArticle i="+i+", j="+j);
	}

	public BufferedReader openUrl(URL url) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
		return br;
	}

	public void run() {
		try {
		String texteArticle = "";
		String auteur = "";
		BufferedReader br;
		String str = "";
		String tmp;
		String lien = article.getLien();
		ParseHtml parseHtml = new ParseHtml();
			Iterator iter = endThreadListeners.iterator();
			while (iter.hasNext()) {
				ThreadListener listener = (ThreadListener) iter.next();
				listener.beginThread(i,j);
			}
			
			
			
			
			parseHtml.parse(lien);
			texteArticle = parseHtml.getTexteArticle();
			auteur = parseHtml.getAuteur();
			
			EcritArticle ea = new EcritArticle(article,path,texteArticle,auteur,xml,txt,isBalisesPhrases,i,j);
			ea.setThreadListeners(endThreadListeners);
			ea.start();
			
		} catch (Exception e) {
			e.printStackTrace();
			notifyRelanceThread();
		}
	}

		public String entreBalises(String str, String regex){
			String tmp = "";
			String[] tmpTab = str.split(regex);
			for(int i=0;i<tmpTab.length/2;i++){
				tmp = tmp + tmpTab[(i*2)+1];
			}
			return tmp;
		}

		/* (non-Javadoc)
		 * @see EndThreadHandler#EndThreadMovedListener(EndThreadListener)
		 */
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
				listener.endThread(i,j);
			}
		}

		/* (non-Javadoc)
		 * @see EndThreadHandler#notifyRelanceThread()
		 */
		public void notifyRelanceThread() {
			Iterator iter = endThreadListeners.iterator();
			while (iter.hasNext()) {
				ThreadListener listener = (ThreadListener) iter.next();
				listener.relanceThread();
			}
		}
		
}