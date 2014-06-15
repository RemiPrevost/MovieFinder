package mf.net;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;

import javax.imageio.ImageIO;

import mf.database.pojo.Acteur;
import mf.database.pojo.Nationalite;
import mf.database.pojo.Realisateur;
import mf.database.pojo.Type;
import mf.exception.net.ConnectionFail;
import mf.files.Fichier;
import mf.model.FicheFilm;

public class Stream {
	
	private static ArrayList<FicheFilm> liste_fiches_films = new ArrayList<FicheFilm>();
	
	private static int delta = 0;
	
	//******************************************//
	//*************** PUBLIC *******************//
	//******************************************//

	public static void launchSearch(String titre) throws ConnectionFail {
		String adresse_request = "http://www.allocine.fr/recherche/?q=";
		String adresse_film = "http://www.allocine.fr";
		String html = new String();
		ArrayList<String> liste_url = new ArrayList<String>();
		
		liste_fiches_films = new ArrayList<FicheFilm>(); // r�initialisation de la liste de fiche � 0
		try {
			titre = URLEncoder.encode(titre,"UTF-8"); //encodage du query pour convetion URL
		
			html = getHTML(adresse_request+titre); // obtention du code html de la page de recherche du film
			liste_url = findHTML(html); // obtention de la liste de l'url des film retourn�s par la requ�te
		
			for (String url : liste_url) { // pour toutes les url de la liste
				html = getHTML(adresse_film+url); // obtenir le code html de cet url
				fillFicheFilm(html); // remplir l'ArrayList avec une nouvelle FicheFilm
			}
			
			//for (FicheFilm ff : liste_fiches_films)
				//System.out.println(ff.toString());
			
		} catch (UnsupportedEncodingException e) { // en cas d'�chec du codage de la requ�te, retourner une liste vide
			System.out.println("Echec de l'encodage du query");
		}
	}
	
	// Retourne la fiche film suivante si il y a une, affiche un  message d'erreur si la liste est vide
	public static FicheFilm getNext() {
		if (hasNext()) {
			FicheFilm fiche = liste_fiches_films.get(0);
			liste_fiches_films.remove(0);
			return fiche;
		}
		System.out.println("No more FicheFilm to return");
		return new FicheFilm();
	}
	
	// retourne vrai si la liste de fiche film n'est pas vide, faux sinon
	public static boolean hasNext() {
		if (liste_fiches_films.isEmpty())
			return false;
		else
			return true;
	}
	
	// t�l�charge l'image depuis l'adresse pr�sic�e et la stocke dans une Image
	public static Image downloadImageToImage(String adresse) throws IOException {
		Image affiche = null;
		URL url = new URL(adresse);
		affiche = ImageIO.read(url);
		
		return affiche;
	}
	
	// t�l�charge l'image depuis l'adresse pr�sic�e et la stocke dans le dossier d�di� aux affiches
	public static String downloadImageToFolder(String adresse) throws IOException {
		/* t�l�chargement de l'image */
		Image affiche = null;
		URL url = new URL(adresse);
		affiche = ImageIO.read(url);
		
		/* transformation de l'affiche depuis Image to BufferedImage */
		BufferedImage affiche_buff = new BufferedImage(affiche.getWidth(null),affiche.getHeight(null),BufferedImage.TYPE_INT_RGB);
		Graphics2D graphic = affiche_buff.createGraphics();
		graphic.drawImage(affiche, 0, 0, affiche.getWidth(null), affiche.getHeight(null), null);
		graphic.dispose();
		
		/* enregistrement de l'image au format png, le nom de l'image est celui des millisecondes �coul�es le 1 janvier 1970
		 * plus un delta pour �viter les doublons lors d'une m�me ex�cution */
		String name = new String(System.currentTimeMillis()+delta+".aff");
		File output_file = new File(Fichier.AFFICHE_DIRECTORY+name);
		ImageIO.write(affiche_buff, "jpg", output_file);
		
		affiche = null;
		affiche_buff = null;
		output_file = null;
		System.gc();
		
		return name;
	}

	//******************************************//
	//************** PRIVATE *******************//
	//******************************************//

	// Remplit la fiche d'un film � partir de son code html
	private static void fillFicheFilm(String html) {
		FicheFilm fiche = new FicheFilm();
		int index_debut = html.indexOf("<title>"); // le nom de r�sultat ce trouve juste avant cette cha�ne de caract�re dont on r�cup�re l'index
		int index_fin = index_debut;
		int index_fin_bloc;
		
		Realisateur realisateur = new Realisateur();
		Acteur acteur = new Acteur();
		
		////////////////////// << TITRE >> //////////////////////
		index_fin = html.indexOf(" - film ", index_fin);
		if (index_fin == -1)
			index_fin = html.indexOf(" - Court ", index_fin);
		fiche.setTitre_f(convertCS(html.substring(index_debut+8, index_fin))); // recup�ration du titre du film
		
		index_fin = html.indexOf("</title>",index_fin);
		try { ////////////////////// << DATE >> //////////////////////
			fiche.setDate(Integer.parseInt(html.substring(index_fin-17, index_fin-13),10)); //tentative de r�cup�ration de l'ann�e de sortie
		} catch (Exception e) { // possible echec de parseInt si le string en param�tre n'est pas compos� de chiffre uniquement
			fiche.setDate(-1); //date ind�termin�e
		}
		
		//////////////////////<< AFFICHE FILM >> //////////////////////
		index_debut = html.indexOf("<meta property=\"og:image\"",index_fin);
		if (index_debut == -1)
			fiche.setAffiche_url("?"); // impossible de trouver l'affiche du film
		else {
			index_fin = html.indexOf("\" /><meta property=\"og:type\"",index_debut);
			fiche.setAffiche_url(html.substring(index_debut+35, index_fin)); // r�cup�ration de l'adresse de l'affiche du film
		}
		
		//////////////////////<< REALISATEURS >> //////////////////////
		index_debut = html.indexOf("itemprop=\"director\"",index_fin); // aller au champs director
		if (index_debut == -1)
			fiche.addRealisateurs(new Realisateur()); // en cas d'�chec retourner un realisateur vide
		else {
			index_fin_bloc = html.indexOf("</td>",index_debut); // rep�rage de la fin du bloc director
			index_debut = html.indexOf("title=\"",index_fin); // aller au prochain producteur
			while (index_debut < index_fin_bloc) { // tant que notre index_debut n'est pas sorti du bloc director
				index_fin = html.indexOf(" ",index_debut); // encadrer son pr�nom
				realisateur.setPrenom(convertCS(html.substring(index_debut+7, index_fin))); // l'ajouter � notre objet temporaire realisateur
				index_debut = index_fin + 1; // encadrer son nom
				index_fin = html.indexOf("\"",index_debut);
				realisateur.setNom(convertCS(html.substring(index_debut, index_fin)));// l'ajouter � notre objet temporaire realisateur
				fiche.addRealisateurs(realisateur); // ajouter le realisateur � la liste
				realisateur = new Realisateur(); // remettre l'objet temporaire r�alisateur � 0
				index_debut = html.indexOf("title=\"",index_fin); // aller au prochain producteur
			}
			index_debut = index_fin_bloc;
		}
		
		//////////////////////<< ACTEURS >> //////////////////////
		index_debut = html.indexOf("itemprop=\"actors\"",index_fin); // aller au champs actors
		if (index_debut == -1)
			fiche.addRealisateurs(new Realisateur()); // en cas d'�chec retourner un acteur vide
		else {
			index_fin_bloc = html.indexOf("</td>",index_debut); // rep�rage de la fin du bloc actors
			index_debut = html.indexOf("title=\"",index_fin); // aller au prochain acteur
			while (index_debut < index_fin_bloc) { // tant que notre index_debut n'est pas sorti du bloc actors
				index_fin = html.indexOf(" ",index_debut); // encadrer son pr�nom
				acteur.setPrenom(convertCS(html.substring(index_debut+7, index_fin))); // l'ajouter � notre objet temporaire acteur
				index_debut = index_fin + 1; // encadrer son nom
				index_fin = html.indexOf("\"",index_debut);
				acteur.setNom(convertCS(html.substring(index_debut, index_fin)));// l'ajouter � notre objet temporaire acteur
				fiche.addActeur(acteur); // ajouter l'acteur � la liste
				acteur = new Acteur(); // remettre l'objet temporaire acteur � 0
				index_debut = html.indexOf("title=\"",index_fin); // aller au prochain acteur
			}
			index_debut = index_fin_bloc;
		}
		

		//////////////////////<< GENRES >> //////////////////////
		index_debut = html.indexOf("Genre\n</span>",index_fin); // aller au champs genre
		if (index_debut == -1)
			fiche.addGenre(new Type()); // en cas d'�chec retourner un genre vide
		else {
			index_debut = html.indexOf("itemprop=\"genre\">",index_debut); // aller au premier genre
			while (index_debut != -1) { // tant que notre index_debut n'est pas sorti du bloc genre
				index_fin = html.indexOf("</span>", index_debut);// encadrer le genre
				if (html.substring(index_debut+17, index_fin) != "Divers") // Si le genre est diff�rent de "Divers"
					fiche.addGenre(new Type(convertCS(html.substring(index_debut+17, index_fin)))); // l'ajouter � la liste de Genre
				
				index_debut = html.indexOf("itemprop=\"genre\">",index_debut +1); // rechercher le Genre suivant
			}
			index_debut = index_fin;
		}
		
		//////////////////////<< NATIONALITES >> //////////////////////
		index_debut = html.indexOf("Nationalité",index_fin); // aller au champs nationalite
		if (index_debut == -1)
			fiche.addNation(new Nationalite()); // en cas d'�chec retourner un une nationalite vide
		else {
			index_fin_bloc = html.indexOf("</td>",index_debut);
			index_debut = html.indexOf("\">",index_debut); // postionnement de l'index d�but
			
			while (index_debut < index_fin_bloc) { // tant que notre index_debut n'est pas sorti du bloc nationalite
				index_fin = html.indexOf("</span>",index_debut+5); // positionnement de l'index fin
				fiche.addNation(new Nationalite(convertCS(html.substring(index_debut+3,index_fin)))); // on ajoute la nationalite trouv�e � la liste
				index_debut = index_fin + 1;

				index_debut = html.indexOf("\">",index_debut); // aller � la prochaine nationalite
			}
			index_debut = index_fin_bloc;
		}
		
		//////////////////////<< NOTE >> //////////////////////
		index_debut = html.indexOf("Spectateurs",index_fin); // aller au champs genre
		if (index_debut == -1)
			fiche.setNote(-1); // en cas d'�chec on met la note � -1 <=> inconnue
		else {
			index_debut = html.indexOf("itemprop=\"ratingValue\" content=\"",index_debut);
			try {
				fiche.setNote(Float.parseFloat(html.substring(index_debut+32, index_debut+35))); // l'ajouter � la liste de Genre
			} catch (Exception e) { // possible echec de parseInt si le string en param�tre n'est pas compos� de chiffre uniquement
				index_debut = html.indexOf(">",index_debut);
				try {
					fiche.setNote(Float.parseFloat(html.substring(index_debut+1, index_debut+4).replaceFirst(",", "."))); // l'ajouter � la liste de Genre
				} catch (Exception e1) { // possible echec de parseInt si le string en param�tre n'est pas compos� de chiffre uniquement
					fiche.setDate(-1); //date ind�termin�e
				}
			}
		}
		index_debut = index_fin;
		
		if (fiche.getDate() <= Calendar.getInstance().get(Calendar.YEAR)) // on ne propose pas � l'utilisateur des films qui ne sont pas encore sortis
			liste_fiches_films.add(fiche);
	}
	
	// recup�re le code html de la requ�te de recherche du film portant le titre titre_f
	private static String getHTML(String adresse) throws ConnectionFail {
		
		URL url;
		String html = new String();
		BufferedReader in = null;
		String line = new String();

		try {
			
			url = new URL(adresse); 
			
		URLConnection url_conn = url.openConnection(); //Connection au serveur

		in = new BufferedReader(new InputStreamReader(url_conn.getInputStream())); //recup�ration du code html
		while ((line = in.readLine()) != null)
			html += '\n' + line;
		
		if (html.isEmpty()) throw new ConnectionFail("Cannot connect to server");

		return html;
		
		} catch (IOException e) {
			throw new ConnectionFail("Cannot connect to server");
		} finally {
			try {
				in.close();
			} catch (Exception e) {
				throw new ConnectionFail("Cannot close connection");
			}
		}
	}
	
	// recup�re l'adresse internet de la fiche de chaque films trouv� dans le code html de la requ�te faite sur AlloCin�
	private static ArrayList<String> findHTML(String html){
		ArrayList<String> liste_url = new ArrayList<String>();
		int nb_result = 0; // nombre de r�sultats retourn�s par la requ�te.
		int index_fin = html.indexOf("dans les titres de films."); // le nom de r�sultat ce trouve juste avant cette cha�ne de caract�re dont on r�cup�re l'index
		int index_debut = index_fin;
		
		try {
			while(!isDigit(html.charAt(index_fin))) // on place index_fin sur le dernier chiffre gr�ce � une d�cr�mentation tant que le caract�re � l'index n'est pas un chiffre
				index_fin--;
			
			while(html.charAt(index_debut) != '\n') // on place index_debut sur le caract�re de retour � la ligne pr�c�dent � index_fin car le premier chiffre se trouve juste apr�s ce dernier.
				index_debut--;

			nb_result = Integer.parseInt(html.substring(index_debut+1,index_fin+1), 10); // Extraction de la chaine entre l'indice index_debut et index_fin qui contient le nombre et cast en int
		
		} catch (StringIndexOutOfBoundsException e) { //si la requ�te n'a pas retourn� de resultat, la chaine de caract�re ci-dessus est absente. La d�cr�mentation de i fini
			nb_result = 0; //amen� une demande d'acc�s � un index n�gatif ce qui l�ve une exception. Dans ce cas, on met nb_r�sultat � 0
		}
		
		if (nb_result == 0)
			return liste_url; //si la recherche ne donne rien on retourne une liste vide d'url
		if (nb_result > 5)
			nb_result = 5;
		for (int i = 1; i <= nb_result; i++) { //on a nb_result adresses � trouver
			index_debut = html.indexOf("/film/fichefilm_gen_cfilm=",index_debut);
			index_fin = html.indexOf(">",index_debut);
			liste_url.add(html.substring(index_debut,index_fin-1));

			index_debut = html.indexOf("fichefilm_gen_cfilm=",index_fin); //permet d'�viter l'adresse suivante qui est la m�me que la pr�c�dente d�j� r�cup�r�e
			index_fin = index_debut;
		}
		
		return liste_url;
	}

	//indique si l'entier re�u est compris entre 0 et 9
	private static boolean isDigit(int i) {
		return ((char)i >= 48 && (char)i <= 57);
	}
	
	private static String convertCS(String s) {
		return (new String(s.getBytes(Charset.forName("ISO-8859-1")),Charset.forName("UTF-8")));
	}

}