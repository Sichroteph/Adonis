/*
 * Created on 3 mai 2005
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

package extraction;
import java.util.Vector;

import org.htmlparser.Attribute;
import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.Text;
import org.htmlparser.nodes.RemarkNode;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.ScriptTag;
import org.htmlparser.tags.Span;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.util.ParserException;
import org.htmlparser.visitors.NodeVisitor;

class ParseHtml extends NodeVisitor {
	String texteArticle;

	String texteArticleSecondaire;

	String auteur;

	boolean titre;

	boolean texte;

	boolean lienOuOption;

	boolean texteSecondaire;

	boolean script;

	boolean estAuteur;

	int nbTagDebutTexte;

	String tagDebutTexte;

	int nbBalisesApresTexte;

	private Parser parser;

	public ParseHtml() {
		super(true); 
		texteArticle = "";
		texteArticleSecondaire = "";
		auteur = "";
		titre = false;
		texte = false;
		texteSecondaire = true;
		script = false;
		tagDebutTexte = "";
		nbTagDebutTexte = 0;
		nbBalisesApresTexte = 7;
		lienOuOption = false;
		estAuteur = false;
	}

	public void visitTag(Tag tag) {
		// process tags here
		/*
		 * if (tag instanceof LinkTag) { LinkTag linkTag = (LinkTag) tag;
		 * System.out.print("\"" + linkTag.getLinkText() + "\" => ");
		 * System.out.println(linkTag.getLink()); } if (tag instanceof FrameTag) {
		 * FrameTag frameTag = (FrameTag) tag;
		 * System.out.println(frameTag.getFrameLocation()); }
		 */

		if (!texte) {
			nbBalisesApresTexte++;
		}
		if(tag.getTagName().compareToIgnoreCase("h2") == 0){
			lienOuOption = true;
			Vector vAttributs = tag.getAttributesEx();
			for (int i = 0; i < vAttributs.size(); i++) {
				Attribute att = (Attribute) vAttributs.elementAt(i);
				if (att.getValue() != null) {
					if (att.getValue().compareToIgnoreCase("ChapoDoc") == 0){
						lienOuOption = false;
					}
				}
			}
		}
		else if (tag.getTagName().compareToIgnoreCase("a") == 0
				|| tag.getTagName().compareToIgnoreCase("option") == 0) {
			lienOuOption = true;
		}
		else if (texte && ((tag.getTagName().compareToIgnoreCase("p") == 0)||tag.getTagName().compareToIgnoreCase("br") == 0)) {
			//texteArticle += "\n";
		} else if (texte
				&& tag.getTagName().compareToIgnoreCase(tagDebutTexte) == 0) {
			nbTagDebutTexte++;
		} else if (tag instanceof ImageTag) {
			if (texte) {
				String lettre = recupereLettre(((ImageTag) tag).getImageURL());
				if (lettre.length() == 1)
					texteArticle += lettre;
			}
		} else if (tag instanceof ScriptTag) {
			script = true;
		} else if (tag instanceof Span || tag instanceof Div
				|| tag instanceof TableColumn
				|| tag.getTagName().compareToIgnoreCase("p") == 0) {
			Vector vAttributs = tag.getAttributesEx();
			for (int i = 0; i < vAttributs.size(); i++) {
				Attribute att = (Attribute) vAttributs.elementAt(i);
				if ((att.getName() != null)
						&& att.getName().compareToIgnoreCase("onclick") == 0) {
					lienOuOption = true;
				}
				if (att.getValue() != null) {
					if (att
							.getValue()
							.matches(
									"[^<>-e]*(ar-txt|art-txt|art-chapo|zikchapo|text|Text|TEXT|corpsarticle|EditCorps|MsoNormal|lien-news|articletexte)[^e<>]*")
							&& (att.getName().compareToIgnoreCase("class") == 0 || att
									.getName().compareToIgnoreCase("id") == 0)) {
						texte = true;
						texteSecondaire = false;
						nbBalisesApresTexte = 0;
						tagDebutTexte = tag.getTagName();
					}
					if (att.getValue().matches("[^<>]*art-aut[^<>]*")) {
						estAuteur = true;
					}
				}
			}
		}
	}

	public void visitStringNode(Text text) {
		if (estAuteur && !text.getText().matches("[ \t\n\f\r]*")) {
			auteur = supprimeCaracteresSpeciaux(text.getText());
		} else if (!lienOuOption) {
			if (text.getText().matches(
					"(.*[ ]par[ ].*)|(.*[ ]avec[ ].*)|(.*[ ]et[ ].*)")) {
				String[] nomPrenom = text.getText().split("[ ]+");
				if (nomPrenom.length > 1 && nomPrenom.length <= 6) {
					auteur = supprimeCaracteresSpeciaux(text.getText());
				}
			} else {
				String[] nomPrenom = text.getText().split("[ -]+");
				if (nomPrenom.length > 1 && nomPrenom.length <= 4) {
					estAuteur = true;
					for (int i = 0; i < nomPrenom.length; i++) {
						if (!nomPrenom[i]
								.matches("[ \t\n\f\r]*([A-Z][^A-Z]*|.*de.*)")) {
							estAuteur = false;
						}
					}
					if (estAuteur && !texteSecondaire
							&& (nbBalisesApresTexte < 7)) {
						auteur = supprimeCaracteresSpeciaux(text.getText());
					} else if (estAuteur && texteSecondaire) {
						auteur = supprimeCaracteresSpeciaux(text.getText());
					}
				}
			}
		}
		if (texteSecondaire && !script && !lienOuOption) {
			if (text.getText().matches("[ \t\n\f\r]*([«A-Z]|[a-z0-9]|\").*([.,)»]|\")[ \t\n\f\r]*")) {
				texteArticleSecondaire += text.getText();
			}
		} else if (texte && !script) {
			if (!text.getText().matches("Pub.*|pub.*|PUB.*|[ \t\f\r\n]*"))
				texteArticle += text.getText();
		}
		estAuteur = false;
	}

	public void visitEndTag(Tag endTag) {
		if (endTag.getTagName().compareToIgnoreCase("script") == 0) {
			script = false;
		} else if ((endTag.getTagName().compareToIgnoreCase("a") == 0
				|| endTag.getTagName().compareToIgnoreCase("div") == 0 || endTag
				.getTagName().compareToIgnoreCase("option") == 0 || endTag
				.getTagName().compareToIgnoreCase("h2") == 0)
				&& lienOuOption) {
			lienOuOption = false;
		} else if (endTag.getTagName().compareToIgnoreCase(tagDebutTexte) == 0) {
			if (texte) {
				if (nbTagDebutTexte == 0) {
					texteArticle += "\n";
					texte = false;
					nbBalisesApresTexte = 0;
				} else {
					nbTagDebutTexte--;
				}
			}
		}
	}

	public void visitRemarkNode(RemarkNode remarkNode) {
	}

	public String recupereLettre(String adresseImage) {
		String[] tab = adresseImage.split("[/.]");
		return tab[tab.length - 2].toUpperCase();
	}

	public String supprimeCaracteresSpeciaux(String source) {
		String res;
		res = source.replaceAll("&quot;", "");
		res = res.replaceAll("&amp;", "&");
		res = res.replaceAll("&lt;", "<");
		res = res.replaceAll("&gt;", ">");
		res = res.replaceAll("&nbsp;", " ");
		res = res.replaceAll("&brvbar;", "|");
		res = res.replaceAll("&sect;", "§");
		res = res.replaceAll("&curren;", "¤");
		res = res.replaceAll("&uml;", "¨");
		res = res.replaceAll("&deg;", "°");
		res = res.replaceAll("&ccedil;", "ç");
		res = res.replaceAll("&agrace;", "à");
		res = res.replaceAll("&pilde;", "ã");
		res = res.replaceAll("&auml;", "ä");
		res = res.replaceAll("&eacute;", "é");
		res = res.replaceAll("&egrave;", "è");
		res = res.replaceAll("&ecirc;", "ê");
		res = res.replaceAll("&euml;", "ë");
		res = res.replaceAll("&icirc;", "î");
		res = res.replaceAll("&iuml;", "ï");
		res = res.replaceAll("&ocirc;", "ô");
		res = res.replaceAll("&ugrave;", "ù");
		res = res.replaceAll("&ucirc;", "û");
		res = res.replaceAll("&uuml;", "ü");
		res = res.replaceAll("&agrave;", "à");
		res = res.replaceAll("[ \t\f\r]+", " ");
		res = res.replaceAll("[\n]+", "\n");
		res = res.replaceAll("&bull;[ ]*", ".");
		res = res.replaceAll("\\[-\\] fermer", "");
		res = res.replaceAll("&#233;", "é");
		res = res.replaceAll("&#160;"," ");
		return res;
	}

	public String getTexteArticle() {
		if (texteSecondaire || texteArticle.matches("[ \t\n\f\r]*"))
			return supprimeCaracteresSpeciaux(texteArticleSecondaire);
		else
			return supprimeCaracteresSpeciaux(texteArticle);
	}

	public String getAuteur() {
		auteur = auteur.replaceAll("\n", "");
		return auteur;
	}

	public void parse(String url) throws ParserException {
		url = url.replace("url.asp?/","");
		parser = new Parser(url);
		parser.visitAllNodesWith(this);
	}
}