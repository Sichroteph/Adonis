
package adonis;

import javax.swing.*;

import extraction.EcritArticle;
import extraction.Extraction;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.File;
import java.net.*;

import listeners.CheckBoxStatusListener;

public class Controleur extends javax.swing.JFrame implements MouseListener,
		ItemListener, ActionListener {

	private Vector ensembleFlux = new Vector();

	private int nbArticles;

	// Booleen indiquant si tous les articles sont selectionnés
	private boolean isAllSelected = false;

	//	Compteur de lignes
	private int cptLignes = 0;

	private boolean xml;

	private boolean txt;

	private Vector adressesNonValides;

	//Path pour extraire les articles
	private Parametres parametres;

	public Controleur() throws Exception {

		parametres = new Parametres();
		initComponents();

	}

	private void initComponents() throws Exception {
		adressesNonValides = new Vector();
		jPanneauPrincipal = new javax.swing.JPanel(new BorderLayout());
		JPanel jPanneauDroit = new JPanel(new BorderLayout());
		JPanel jPanneauResume = new JPanel();
		JPanel jPanneauBouttonsPrincipaux = new JPanel(new BorderLayout());
		jPanneauGauchePrincipal = new javax.swing.JPanel();
		jPanneauGauche = new javax.swing.JPanel(new BorderLayout());
		jPanneauGauche1 = new javax.swing.JPanel(new BorderLayout());
		jPanneauGauche2 = new javax.swing.JPanel();
		jPanneauGauche3 = new javax.swing.JPanel();
		jPanneauGauche4 = new javax.swing.JPanel(new BorderLayout());
		jPanneauGauche5 = new javax.swing.JPanel();
		jPanneauGauche6 = new javax.swing.JPanel();
		jLabel1 = new javax.swing.JLabel();
		jMenuBar1 = new javax.swing.JMenuBar();
		jMenu1 = new javax.swing.JMenu();
		jMenu1.setText("Proprietés");
		jMenu2 = new javax.swing.JMenu();

		jMenu2.setText("A propos de");
		itemAdonis = new JMenuItem("Qui sommes nous");
		itemAdonis.addActionListener(this);
		jMenu2.add(itemAdonis);

		jMenuBar1.add(jMenu1);
		jMenuBar1.add(jMenu2);

		itemPreferences = new JMenuItem("Preferences generales");
		itemPreferences.addActionListener(this);
		jMenu1.add(itemPreferences);

		itemOuvrirConfig = new JMenuItem("Ouvrir les adresses des flux RSS");
		itemOuvrirConfig.addActionListener(this);
		jMenu1.add(itemOuvrirConfig);

		itemQuitter = new JMenuItem("Quitter");
		itemQuitter.addActionListener(this);
		jMenu1.add(itemQuitter);

		setJMenuBar(jMenuBar1);
		setTitle("Rss News Extractor - Extracteur de textes d'articles par flux RSS");
		jPanneauPrincipal.setPreferredSize(new java.awt.Dimension(1024, 700));

		jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		jLabel1.setText("***  En attente de reception ***");
		jLabel1.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
		jLabel1.setBorder(new javax.swing.border.TitledBorder("Informations"));
		jPanneauPrincipal.add(jLabel1, BorderLayout.NORTH);

		jPanneauGauche.setBackground(new java.awt.Color(200, 200, 200));
		jPanneauGauche.setBorder(new javax.swing.border.TitledBorder("Flux"));

		jPanneauGauche1.setPreferredSize(new java.awt.Dimension(150, 700));
		jPanneauGauche6.setPreferredSize(new java.awt.Dimension(70, 700));
		jPanneauGauche5.setPreferredSize(new java.awt.Dimension(40, 700));
		jPanneauGauche3.setPreferredSize(new java.awt.Dimension(40, 700));

		jPanneauGauche2.setLayout(new BoxLayout(jPanneauGauche2,
				BoxLayout.PAGE_AXIS));
		jPanneauGauche3.setLayout(new BoxLayout(jPanneauGauche3,
				BoxLayout.PAGE_AXIS));
		jPanneauGauche5.setLayout(new BoxLayout(jPanneauGauche5,
				BoxLayout.PAGE_AXIS));
		jPanneauGauche6.setLayout(new BoxLayout(jPanneauGauche6,
				BoxLayout.PAGE_AXIS));

		jPanneauGauche1.setBackground(new java.awt.Color(200, 200, 200));
		jPanneauGauche2.setBackground(new java.awt.Color(200, 200, 200));
		jPanneauGauche3.setBackground(new java.awt.Color(200, 200, 200));
		jPanneauGauche4.setBackground(new java.awt.Color(200, 200, 200));
		jPanneauGauche5.setBackground(new java.awt.Color(200, 200, 200));
		jPanneauGauche6.setBackground(new java.awt.Color(200, 200, 200));

		jPanneauGauche1.add(jPanneauGauche3, BorderLayout.WEST);
		jPanneauGauche1.add(jPanneauGauche4, BorderLayout.EAST);

		jPanneauGauche4.add(jPanneauGauche5, BorderLayout.WEST);
		jPanneauGauche4.add(jPanneauGauche6, BorderLayout.EAST);

		jPanneauGauche.add(jPanneauGauche1, BorderLayout.WEST);
		jPanneauGauche.add(jPanneauGauche2, BorderLayout.EAST);

		JScrollPane scroller = new JScrollPane(jPanneauGauche);
		scroller.setPreferredSize(new Dimension(700, 600));

		jPanneauGauchePrincipal.add(scroller);

		jPanneauPrincipal.add(jPanneauGauchePrincipal, BorderLayout.WEST);

		jPanneauResume.setPreferredSize(new java.awt.Dimension(250, 350));
		zoneResume = new JTextPane();
		zoneResume.setEditable(false);
		zoneResume.setPreferredSize(new java.awt.Dimension(250, 250));
		zoneResume.setBorder(new javax.swing.border.TitledBorder("Resumé"));
		zoneResume.setBackground(new java.awt.Color(200, 200, 200));
		jPanneauResume.add(zoneResume);

		jLabelLogo = new JLabel();
		jLabelLogo.setVisible(true);
		jPanneauResume.add(jLabelLogo);
		jPanneauDroit.add(jPanneauResume, java.awt.BorderLayout.NORTH);

		JButton bSelection = new JButton("Selectionner / Deselectionner tout");
		bSelection.setName("selection");
		bSelection.addMouseListener(this);

		bExtraction = new JButton("Extraire");
		bExtraction.setName("extraction");
		bExtraction
				.setToolTipText("Extraction de l'ensemble des articles selectionnés");

		bExtraction.addMouseListener(this);

		JPanel jPanneauDeCale = new JPanel(new BorderLayout());
		jPanneauDeCale.setPreferredSize(new java.awt.Dimension(260, 200));
		JPanel jPanneauDeCale2 = new JPanel(new GridLayout(4, 1));
		jPanneauDeCale2.setPreferredSize(new java.awt.Dimension(200, 200));
		jPanneauDeCale2.add(bSelection);
		jPanneauDeCale2.add(bExtraction);

		JLabel logoAdonis = new JLabel(logo);
		jPanneauDeCale2.add(logoAdonis);

		jPanneauDeCale.add(jPanneauDeCale2, java.awt.BorderLayout.EAST);

		jPanneauBouttonsPrincipaux.add(jPanneauDeCale,
				java.awt.BorderLayout.WEST);

		jPanneauBouttonsPrincipaux.setPreferredSize(new java.awt.Dimension(200,
				200));
		jPanneauDroit.add(jPanneauBouttonsPrincipaux,
				java.awt.BorderLayout.SOUTH);

		jPanneauDroit.setPreferredSize(new java.awt.Dimension(250, 500));
		jPanneauPrincipal.add(jPanneauDroit, java.awt.BorderLayout.CENTER);

		getContentPane().add(jPanneauPrincipal, java.awt.BorderLayout.CENTER);
		pack();
	}

	public void extraitArticlesRSS() throws Exception {
		nbArticles = 0;
		jLabelLogo.setIcon(logoPhoto);
		ChargeConfig config = new ChargeConfig();
		int cptAdressesValides = 0;
		String url = "";
		for (int i = 0; i < config.listeAdressesRss.size(); i++) {
			try {
				url = (String) config.listeAdressesRss.get(i);
				if (url != "") {
					Flux flux = new Flux(url, cptAdressesValides);
					if (flux.listeArticlesRSS.size() != 0) {
						cptAdressesValides++;
						ajouteArticlesRSS(flux);

					}
				}
			} catch (Exception e) {
				adressesNonValides.add(url);
			}
		}

		if (parametres.isAutoExtracted) {
			JCheckBox jcb;
			for (int i = 0; i < ensembleFlux.size(); i++) {
				jcb = ((JCheckBox) (((Flux) ensembleFlux.get(i)).listeJCheckBox
						.get(0)));
				jcb.setSelected(true);
			}
			Extraction ex = new Extraction(parametres, ensembleFlux);
			ex.addCheckBoxStatusListener(new Listener());
			afficheErreurAdresses();
		} else {
			afficheErreurAdresses();
		}

	}

	public void afficheErreurAdresses() {
		if (!adressesNonValides.isEmpty()) {
			Iterator iter = adressesNonValides.iterator();
			String adresses = "";
			while (iter.hasNext()) {
				adresses += (String) iter.next() + "\n";
			}
			JOptionPane
					.showMessageDialog(
							this,
							"Attention les adresses des flux RSS\n"
									+ adresses
									+ " ne sont pas valides.\nIl est conseillé de les retirer de la liste\n car pouvant affecter la stabilité du programme.",
							"Avertissement", JOptionPane.WARNING_MESSAGE);
			adressesNonValides = new Vector();
		}
	}

	public void ajouteArticlesRSS(Flux fluxCourant) throws Exception {
		if (fluxCourant.listeArticlesRSS.size() != 0) {
			nbArticles += fluxCourant.nbArticlesNouveaux(parametres);
			jLabel1.setText("---  " + nbArticles
					+ " nouveaux articles détectés  ---");
			ensembleFlux.add(fluxCourant);

			//Affichage du nom du flux
			JCheckBox jcb = (JCheckBox) (fluxCourant.listeJCheckBox.get(0));
			jcb.setBackground(new java.awt.Color(100, 100, 100));
			jcb.setForeground(new java.awt.Color(250, 250, 250));
			jcb.setBorder(new javax.swing.border.LineBorder(Color.black));
			jcb.setPreferredSize(new java.awt.Dimension(500, 40));
			jcb.setHorizontalAlignment(SwingConstants.LEFT);
			jcb.addItemListener(this);
			jPanneauGauche2.add(jcb);
			JButton jb = (JButton) fluxCourant.listeBouttons.get(0);
			jb.setBackground(new java.awt.Color(200, 200, 200));
			jb.setBorder(new javax.swing.border.LineBorder(Color.black));
			jb.setBackground(new java.awt.Color(100, 100, 100));
			jb.setForeground(new java.awt.Color(250, 250, 250));
			jb.setPreferredSize(new java.awt.Dimension(70, 40));
			cptLignes++;
			jb.addMouseListener(this);
			jPanneauGauche6.add(jb);

			JLabel labXml = (JLabel) (fluxCourant.listeImagesXml.get(0));
			labXml.setPreferredSize(new java.awt.Dimension(40, 40));
			jPanneauGauche5.add(labXml);

			JLabel labTxt = (JLabel) (fluxCourant.listeImagesTxt.get(0));
			labTxt.setPreferredSize(new java.awt.Dimension(40, 40));
			jPanneauGauche3.add(labTxt);

			for (int i = 1; i < fluxCourant.listeArticlesRSS.size(); i++) {

				jcb = (JCheckBox) (fluxCourant.listeJCheckBox.get(i));

				jcb.setBackground(new java.awt.Color(200, 200, 200));

				jcb.setBorder(new javax.swing.border.LineBorder(Color.black));
				jcb.setPreferredSize(new java.awt.Dimension(500, 40));
				jcb.addItemListener(this);

				jPanneauGauche2.add(jcb);

				jb = (JButton) fluxCourant.listeBouttons.get(i);
				jb.setBackground(new java.awt.Color(180, 180, 250));
				jb.setBorder(new javax.swing.border.LineBorder(Color.black));

				labXml = (JLabel) (fluxCourant.listeImagesXml.get(i));
				labXml.setPreferredSize(new java.awt.Dimension(40, 40));
				labXml.addMouseListener(this);
				jPanneauGauche5.add(labXml);

				labTxt = (JLabel) (fluxCourant.listeImagesTxt.get(i));
				labTxt.setPreferredSize(new java.awt.Dimension(40, 40));
				labTxt.addMouseListener(this);
				jPanneauGauche3.add(labTxt);

				cptLignes++;
				jb.addMouseListener(this);
				jb.setPreferredSize(new java.awt.Dimension(70, 40));
				jPanneauGauche6.add(jb);

			}
			jPanneauGauche.setPreferredSize(new java.awt.Dimension(700,
					cptLignes * 18 + 50));

			pack();
		}
	}

	public ImageIcon createImageIcon(String path) throws MalformedURLException {
		java.net.URL imgURL = Controleur.class.getResource(path);
		//URL imgURL = new URL(path);

		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

	public void mouseClicked(MouseEvent e) {

		if (!(e.getSource()).toString().contains("JLabel")) {
			JCheckBox jcb;
			if (((JButton) e.getSource()).getName() == "selection") {
				isAllSelected = !isAllSelected;
				for (int i = 0; i < ensembleFlux.size(); i++) {
					jcb = ((JCheckBox) (((Flux) ensembleFlux.get(i)).listeJCheckBox
							.get(0)));
					jcb.setSelected(isAllSelected);
				}
			} else if (((JButton) e.getSource()).getName() == "extraction") {
				try {
					Extraction ex = new Extraction(parametres, ensembleFlux);
					ex.addCheckBoxStatusListener(new Listener());

				} catch (Exception e1) {
					e1.printStackTrace();
				}
			} else {
				int noFlux = Integer.valueOf(
						((JButton) (e.getSource())).getName()).intValue();
				Flux fluxCourant = (Flux) ensembleFlux.get(noFlux);
				int noArticle = fluxCourant.listeBouttons.indexOf((JButton) e
						.getSource());
				ArticleRSS artCourant = (ArticleRSS) fluxCourant.listeArticlesRSS
						.get(noArticle);
				if (!(System.getProperty("os.name").matches(".*(w|W)indows.*"))) {
					String cmd = "";
					try {
						cmd = "mozilla " + artCourant.getLien();
						Runtime r = Runtime.getRuntime();
						Process p = r.exec(cmd);
					} catch (Exception e3) {
						System.out.println("erreur d'execution " + cmd
								+ e.toString());
					}
				} else{
					System.out.println("coucou");
					BrowserControl.displayURL(artCourant.getLien());
				}
			}
		} else {
			int noFlux = Integer.valueOf(((JLabel) (e.getSource())).getName())
					.intValue();
			Flux fluxCourant = (Flux) ensembleFlux.get(noFlux);
			int noArticle = 0;
			if (((JLabel) (e.getSource())).getToolTipText() == "Cliquez pour ouvrir le xml") {
				noArticle = fluxCourant.listeImagesXml.indexOf((JLabel) (e
						.getSource()));
			} else {
				noArticle = fluxCourant.listeImagesTxt.indexOf((JLabel) (e
						.getSource()));
			}
			ArticleRSS artCourant = (ArticleRSS) fluxCourant.listeArticlesRSS
					.get(0);
			String lien2 = artCourant.getLien().substring(7);
			String lienCoupe[] = lien2.split("/");
			lien2 = lienCoupe[0];

			String pathCourant = parametres.pathExtraction + "/"
					+ Extraction.nettoyageAdresse(lien2) + "/"
					+ Extraction.nettoyageAdresse(artCourant.getTitre());
			artCourant = (ArticleRSS) fluxCourant.listeArticlesRSS
					.get(noArticle);
			String tmp = pathCourant + "/"
					+ EcritArticle.nomDossierMois(artCourant.getDate()) + "/"
					+ EcritArticle.noJour(artCourant.getDate()) + "-"
					+ EcritArticle.nettoyageNom(artCourant.getTitre());

			File f = new File(tmp + ".txt");
			File f2 = new File(tmp + ".xml");

			if (((JLabel) (e.getSource())).getToolTipText() == "Cliquez pour ouvrir le xml") {
				if (f2.exists()){
					if (!(System.getProperty("os.name").matches(".*(w|W)indows.*"))) {
						String cmd = "";
						try {
							cmd = "emacs " + tmp + ".xml";
							Runtime r = Runtime.getRuntime();
							Process p = r.exec(cmd);
						} catch (Exception e3) {
							System.out.println("erreur d'execution " + cmd
									+ e.toString());
						}
					} else
						BrowserControl.displayURL(tmp + ".xml");
				}
			}

			else {
				if (f.exists()){
					if (!(System.getProperty("os.name").matches(".*(w|W)indows.*"))) {
						String cmd = "";
						try {
							cmd = "emacs " + tmp + ".txt";
							Runtime r = Runtime.getRuntime();
							Process p = r.exec(cmd);
						} catch (Exception e3) {
							System.out.println("erreur d'execution " + cmd
									+ e.toString());
						}
					} else
					BrowserControl.displayURL(tmp + ".txt");
				}
			}

		}
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
		if (!(e.getSource()).toString().contains("JLabel")) {
			if ((((JButton) e.getSource()).getName() != "extraction")
					&& (((JButton) e.getSource()).getName() != "selection")) {
				int noFlux = Integer.valueOf(
						((JButton) (e.getSource())).getName()).intValue();
				Flux fluxCourant = (Flux) ensembleFlux.get(noFlux);
				int noArticle = fluxCourant.listeBouttons.indexOf((JButton) e
						.getSource());
				ArticleRSS artCourant = (ArticleRSS) fluxCourant.listeArticlesRSS
						.get(noArticle);
				zoneResume.setText(artCourant.getDescription() + '\n' + '\n'
						+ artCourant.getDate());
				jLabelLogo.setIcon(artCourant.getLogo());

			}
		} else {
			int noFlux = Integer.valueOf(((JLabel) (e.getSource())).getName())
					.intValue();
			Flux fluxCourant = (Flux) ensembleFlux.get(noFlux);
			int noArticle = 0;
			if (((JLabel) (e.getSource())).getToolTipText() == "Cliquez pour ouvrir le xml") {
				noArticle = fluxCourant.listeImagesXml.indexOf((JLabel) (e
						.getSource()));
			} else {
				noArticle = fluxCourant.listeImagesTxt.indexOf((JLabel) (e
						.getSource()));
			}
			ArticleRSS artCourant = (ArticleRSS) fluxCourant.listeArticlesRSS
					.get(0);
			String lien2 = artCourant.getLien().substring(7);
			String lienCoupe[] = lien2.split("/");
			lien2 = lienCoupe[0];

			String pathCourant = parametres.pathExtraction + "/"
					+ Extraction.nettoyageAdresse(lien2) + "/"
					+ Extraction.nettoyageAdresse(artCourant.getTitre());
			artCourant = (ArticleRSS) fluxCourant.listeArticlesRSS
					.get(noArticle);
			String tmp = pathCourant + "/"
					+ EcritArticle.nomDossierMois(artCourant.getDate()) + "/"
					+ EcritArticle.noJour(artCourant.getDate()) + "-"
					+ EcritArticle.nettoyageNom(artCourant.getTitre());
			jLabelLogo.setIcon(artCourant.getLogo());

			File f = new File(tmp + ".txt");
			File f2 = new File(tmp + ".xml");

			if (((JLabel) (e.getSource())).getToolTipText() == "Cliquez pour ouvrir le xml") {

				if (f2.exists())
					zoneResume.setText("Fichier téléchargé sous : \n\n" + tmp
							+ ".xml\n\nCliquez pour ouvrir.");

			}

			else {
				if (f.exists())
					zoneResume.setText("Fichier téléchargé sous : \n\n" + tmp
							+ ".txt\n\nCliquez pour ouvrir.");

			}

		}

	}

	public void mouseExited(MouseEvent e) {
		zoneResume.setText(" ");

		jLabelLogo.setIcon(logoPhoto);
	}

	public void itemStateChanged(ItemEvent e) {
		JCheckBox jcbCourant = (JCheckBox) e.getSource();
		JCheckBox jcb;
		int noFlux = Integer.valueOf((jcbCourant).getName()).intValue();
		Flux fluxCourant = (Flux) ensembleFlux.get(noFlux);
		int noJcb = fluxCourant.listeJCheckBox.indexOf((JCheckBox) e
				.getSource());
		if (noJcb == 0) {
			boolean isSelected = jcbCourant.isSelected();
			for (int i = 0; i < fluxCourant.listeJCheckBox.size(); i++) {
				jcb = (JCheckBox) fluxCourant.listeJCheckBox.get(i);
				if (jcb.isEnabled())
					jcb.setSelected(isSelected);
			}
		}
	}

	public void actionPerformed(ActionEvent e) {
		JMenuItem source = (JMenuItem) (e.getSource());

		if (source.getText() == "Qui sommes nous")
			JOptionPane
					.showMessageDialog(
							this,
							"RNE v1.1- Rss News Extractor\nExtracteur de textes d'articles par flux RSS\n\nFreeware realisé sous la direction\nde l'université PAUL SABATIER, Toulouse.\n-Dans le cadre d'un TER (Travaux Etudes Recherches)\ndont l'objectif premier et d'aider les chercheurs en\nreconnaissance de la parole à se constituer un corpus\nde textes collant à l'actualité.\n\nEquipe ADONIS :\nBLANC Jonathan\nJEANNETTE Christophe\nRIVALS Samuel\nSCATOLLIN Serge",
							"A propos de", JOptionPane.INFORMATION_MESSAGE,
							logo);

		if (source.getText() == "Quitter")
			this.dispose();
		if (source.getText() == "Ouvrir les adresses des flux RSS"){
			if (!(System.getProperty("os.name").matches(".*(w|W)indows.*"))) {
				String cmd = "";
				try {
					cmd = "emacs " + "./Adresses_Flux_RSS.txt";
					Runtime r = Runtime.getRuntime();
					Process p = r.exec(cmd);
				} catch (Exception e3) {
					System.out.println("erreur d'execution " + cmd
							+ e.toString());
				}
			} else
				BrowserControl.displayURL("./Adresses_Flux_RSS.txt");
		}
		if (source.getText() == "Preferences generales") {
			Parametres param = new Parametres();
			param.initComponents();
			param.addCheckBoxStatusListener(new Listener());
			param.setVisible(true);
			parametres = param;
		}

	}

	private class Listener implements CheckBoxStatusListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see CheckBoxDisabledListener#checkBoxdisable(int, int)
		 */
		public synchronized void checkBoxdisable(int x, int y) {

			Flux flux = (Flux) ensembleFlux.get(x);
			setNbArticles(getNbArticles()
					- flux.reactualiseUneLigne(parametres, y));
			jLabel1.setText("***  " + nbArticles + " nouveaux articles  ***");
			validate();
		}

		private void setNbArticles(int i) {
			nbArticles = i;
		}

		private int getNbArticles() {
			return nbArticles;
		}

		public synchronized void checkBoxEncours(int x, int y) {

			JCheckBox jcb = (JCheckBox) (((Flux) ensembleFlux.get(x)).listeJCheckBox
					.elementAt(y));
			jcb.setForeground(new java.awt.Color(200, 50, 50));

			validate();

		}

		public void modificationParametres() {
			//Parametres modifies il faut reactualiser les JCheckBoxs
			nbArticles = 0;
			for (int i = 0; i < ensembleFlux.size(); i++) {
				nbArticles += ((Flux) ensembleFlux.get(i))
						.nbArticlesNouveaux(parametres);
				jLabel1.setText("---  " + nbArticles
						+ " nouveaux articles détectés  ---");

			}
			validate();

		}

	}

	/*
	 * Toutes les variables locales graphiques restent ici c plus clair car
	 * elles n'apportent rien a la lecture du code
	 */

	private javax.swing.JLabel jLabel1;

	private javax.swing.JLabel jLabelLogo;

	private javax.swing.JMenu jMenu1;

	private javax.swing.JMenu jMenu2;

	private javax.swing.JMenuBar jMenuBar1;

	private JMenuItem itemPreferences;

	private JMenuItem itemAdonis;

	private JMenuItem itemOuvrirConfig;

	private JMenuItem itemQuitter;

	private javax.swing.JPanel jPanneauPrincipal;

	private javax.swing.JPanel jPanneauGauchePrincipal;

	private javax.swing.JPanel jPanneauGauche;

	private javax.swing.JPanel jPanneauGauche1;

	private javax.swing.JPanel jPanneauGauche2;

	private javax.swing.JPanel jPanneauGauche3;

	private javax.swing.JPanel jPanneauGauche4;

	private javax.swing.JPanel jPanneauGauche5;

	private javax.swing.JPanel jPanneauGauche6;

	private JTextPane zoneResume;

	private JButton bExtraction;

	private ImageIcon logo = new ImageIcon("adonis.png");

	private ImageIcon logoPhoto = new ImageIcon("CAMERA.gif");
}