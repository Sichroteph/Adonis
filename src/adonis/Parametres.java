package adonis;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.io.*;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import listeners.CheckBoxStatusListener;



public class Parametres extends javax.swing.JFrame implements ActionListener{
	public String pathExtraction;
	public int nbThreadsExtraction;
	String nomFichierParametres="Parametres.txt";
	public boolean isExtracted2Txt;
	public boolean isExtracted2Xml;
	public boolean isBalisesPhrases;
	boolean isAutoExtracted;
	private Vector checkBoxStatusListeners = new Vector();
	
	public Parametres(){
		extraitParametresFichier();	
	}
	
	private void extraitParametresFichier() {
		try {
			
			File inputFile = new File(nomFichierParametres);
			
			BufferedReader in = new BufferedReader(new InputStreamReader(
					new FileInputStream(inputFile)));
			
			this.pathExtraction = in.readLine();
			this.nbThreadsExtraction = Integer.parseInt(in.readLine());
			
			int i = Integer.parseInt(in.readLine());
			if (i==1){
				isExtracted2Txt=true;
			}
			else{
				isExtracted2Txt=false;
			}
			i = Integer.parseInt(in.readLine());
			if (i==1){
				isExtracted2Xml=true;
			}
			else{
				isExtracted2Xml=false;
			}
			i = Integer.parseInt(in.readLine());
			if (i==1){
				isBalisesPhrases=true;
			}
			else{
				isBalisesPhrases=false;
			}
			i = Integer.parseInt(in.readLine());
			if (i==1){
				isAutoExtracted=true;
			}
			else{
				isAutoExtracted=false;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public void initComponents() {
		panneauPrincipal = new javax.swing.JPanel(new BorderLayout());	
		panneauOptionsGenerales = new javax.swing.JPanel(new GridLayout(8,2));
		panneauOptionsAvancees = new javax.swing.JPanel(new GridLayout(1,2));
		panneauPrincipal.setPreferredSize(new java.awt.Dimension(450,150));
		
		
		JTabbedPane parametres = new JTabbedPane();		
		boutonEnregistrer = new JButton("Enregistrer la configuration actuelle");
		boutonEnregistrer.addActionListener(this);
		
		// ONGLET 1 : Options generales
		messagePath = new JLabel("Regler le repertoire d'extraction");
		boutonModifierPath = new JButton("Modifier");
		JPanel panneauBout = new JPanel(new BorderLayout());
		panneauBout.add(boutonModifierPath,java.awt.BorderLayout.NORTH);
		
		boutonModifierPath.addActionListener(this);
		
		messageFormat = new JLabel("Choisir le ou les format de fichier");
		jcbTxt=new JCheckBox(".txt");
		jcbTxt.setSelected(isExtracted2Txt);
		jcbXml=new JCheckBox(".xml");
		jcbXml.setSelected(isExtracted2Xml);
		
		messageFormatTxt = new JLabel("Balises de phrases ?");
		jcbWindows=new JCheckBox();
		jcbWindows.setSelected(isBalisesPhrases);
		
		messageAutoExtracted = new JLabel("Auto extraction au demarrage ?");
		jcbAutoExtracted=new JCheckBox();
		jcbAutoExtracted.setSelected(isAutoExtracted);
		
		panneauOptionsGenerales.add(messagePath);
		panneauOptionsGenerales.add(panneauBout);
		panneauOptionsGenerales.add(new JPanel());
		panneauOptionsGenerales.add(new JPanel());
		panneauOptionsGenerales.add(messageFormat);
		panneauOptionsGenerales.add(jcbTxt);
		panneauOptionsGenerales.add(new JPanel());
		panneauOptionsGenerales.add(jcbXml);
		
		panneauOptionsGenerales.add(messageFormatTxt);
		panneauOptionsGenerales.add(jcbWindows);
		panneauOptionsGenerales.add(messageAutoExtracted);
		panneauOptionsGenerales.add(jcbAutoExtracted);

		
		parametres.addTab("General", null, panneauOptionsGenerales,null);
		
		// ONGLET 2 : Options avancees
		messageNbThreads = new JLabel("Nombre threads : "+nbThreadsExtraction);
		boutonModifierNbThreads = new JButton("Nombre ...");
		
		
		boutonModifierNbThreads.addActionListener(this);
		
		JPanel panneauNbThreads = new JPanel(new BorderLayout());
		JPanel panneauMessNbThreads = new JPanel(new BorderLayout());
		
		panneauMessNbThreads.add(messageNbThreads, java.awt.BorderLayout.NORTH);
		panneauNbThreads.add(boutonModifierNbThreads, java.awt.BorderLayout.NORTH);
		
		panneauOptionsAvancees.add(panneauMessNbThreads);
		panneauOptionsAvancees.add(panneauNbThreads);
		pack();
		
		parametres.addTab("Avancé", null, panneauOptionsAvancees,null);
		
		
		
		panneauPrincipal.add(parametres, java.awt.BorderLayout.NORTH);
		panneauPrincipal.add(boutonEnregistrer, java.awt.BorderLayout.SOUTH);
		
		setTitle("Preferences");
		panneauPrincipal.setPreferredSize(new java.awt.Dimension(450, 300));
		getContentPane().add(panneauPrincipal, java.awt.BorderLayout.CENTER);
		pack();
		
		
	}
	
	
	
	public void actionPerformed(ActionEvent event)
	{

		if(event.getSource()==boutonEnregistrer)
			enregistreFichierParametres();
		
		if(event.getSource()==boutonModifierPath){
			
			fc = new JFileChooser(pathExtraction);
			fc.setBorder(new TitledBorder("Choix d'un repertoire dedestination"));
			fc.setApproveButtonText("Selectionner");
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int returnVal = fc.showOpenDialog(this);
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				pathExtraction = fc.getSelectedFile().getAbsolutePath();
			}
		}
		if(event.getSource()==boutonModifierNbThreads){
			String threads ;
			
			threads = (String)JOptionPane.showInputDialog(
					
					this,
					"Entrez le nombre de Threads lancés simultanement\nlors de l'extraction",
					"Threads",
					JOptionPane.INFORMATION_MESSAGE,
					null,
					null,
					String.valueOf(nbThreadsExtraction));
			
			if(threads!=null&&threads.length()!=0){
				nbThreadsExtraction=Integer.parseInt(threads);
				messageNbThreads.setText("Nombre threads : \n"+nbThreadsExtraction);
			}
		}
		
		
	}
	
	private void enregistreFichierParametres() {
		try {
			
			FileWriter writer = new FileWriter(nomFichierParametres);
			writer.write(pathExtraction);
			writer.write('\n');
			writer.write(String.valueOf(nbThreadsExtraction));
			writer.write('\n');
			if (jcbTxt.isSelected()){
				isExtracted2Txt=true;
				writer.write(String.valueOf(1));
			}
			else{
				isExtracted2Txt=false;
				writer.write(String.valueOf(0));
			}
			writer.write('\n');
			
			if (jcbXml.isSelected()){
				isExtracted2Xml=true;
				writer.write(String.valueOf(1));
			}
			else{
				isExtracted2Xml=false;
				writer.write(String.valueOf(0));
			}
			writer.write('\n');
			if (jcbWindows.isSelected()){
				isBalisesPhrases=true;
				writer.write(String.valueOf(1));
			}
			else{
				isBalisesPhrases=false;
				writer.write(String.valueOf(0));
			}
			writer.write('\n');
			if (jcbAutoExtracted.isSelected()){
				isAutoExtracted=true;
				writer.write(String.valueOf(1));
			}
			else{
				isAutoExtracted=false;
				writer.write(String.valueOf(0));
			}
			writer.write('\n');
			writer.close();
			
			// pour Reactualiser l'affichage des Jcb
			notifyCheckBoxStatusListener();
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	private JPanel panneauPrincipal;
	private JTabbedPane parametres;
	private JButton boutonEnregistrer	;
	private JButton boutonModifierPath	;
	private JButton boutonModifierNbThreads	;
	
	
	private JPanel panneauOptionsGenerales;
	private JPanel panneauOptionsAvancees;
	private JFileChooser fc;
	private JLabel messagePath;
	private JLabel messageFormat;
	private JLabel messageFormatTxt;
	private JLabel messageNbThreads;
	private JLabel messageAutoExtracted;
	private JCheckBox jcbTxt;
	private JCheckBox jcbXml;
	private JCheckBox jcbWindows;
	private JCheckBox jcbAutoExtracted;
	/**
	 * @return Returns the nbThreadsExtraction.
	 */
	public int getNbThreadsExtraction() {
		return nbThreadsExtraction;
	}

	public void itemStateChanged(ItemEvent e) {
		
		}
	
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

	public void notifyCheckBoxStatusListener() {
		
		Iterator iter = checkBoxStatusListeners.iterator();
		while (iter.hasNext()) {
			CheckBoxStatusListener listener = (CheckBoxStatusListener) iter
					.next();

			
				listener.modificationParametres();
			

		}
	}
}

