package mf.files;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mf.exception.files.CannotDeleteMovie;
import mf.exception.files.CannotPlayMovie;
import mf.exception.files.FileSystemCorrupted;
import mf.exception.files.FileSystemUnavailable;
import mf.exception.files.IsNotDirectory;
import mf.exception.files.IsNotFile;
import mf.exception.files.UnfoundFile;


public class Fichier{
	// enregistre le nom du répertoire de travail ou le chemin complet s'il s'agit d'un fichier
	private static final String WORK_DIRECTORY = "I:\\Films\\";
	public static final String AFFICHE_DIRECTORY = "affiches\\";
	
	public static final int READ = 0;
	public static final int WRITE = 1;
	
	private static BufferedReader read_buff_system = null;
	private static FileWriter writer_buff_system = null;
	
	//******************************************//
	//*************** PUBLIC *******************//
	//******************************************//
	
	/* Ouvre le fichier système (le fichier est crée s'il n'existe pas) en cas d'échec, le fichier n'est pas présent.
	 * Parameters : mode - Indique s'il s'agit d'une ouverture en lecture ou en écriture (Valeurs : Fichier.READ, Fichier.WRITE)
	 * Return : true si l'ouverture/création s'est bien déroulée
	 * 			false en cas d'échec (le fichier n'est alors pas présent) */
	public static boolean OpenFileSystem(int mode) {
		if (mode == READ) {
			try {
				read_buff_system = new BufferedReader(new FileReader("bin\\system"));
				read_buff_system.mark(0);
				return true;
			} catch (IOException e) {
				return false;
			}
		}
		else if(mode == WRITE) {
			try {
				read_buff_system = new BufferedReader(new FileReader("bin\\system"));
				writer_buff_system = new FileWriter("bin\\system", true);
				return true;
			} catch (IOException e) {
				return false;
			}
		}
		else
			return false;
	}
	
	/* Ferme le fichier système précédement ouvert */
	public static void CloseFileSystem() {
		if (read_buff_system != null)
			try {
				read_buff_system.close();
				read_buff_system = null;
			} catch (IOException e) {
				read_buff_system = null;
			}
		if (writer_buff_system != null)
			try {
				writer_buff_system.close();
				writer_buff_system = null;
			} catch (IOException e) {
				writer_buff_system = null;
			}
	}
	
	/* Retourne un tableau contenant tous les noms de fichiers dans la liste d'exculsion
	 * THROWS : FileSystemUnavailable - tentative d'accès au fichier system sans ouverture
	 * 			FileSystemCorrupted - Erreur détectée lors de la lecture du fichier */
	public static String[] getTabFileName() throws FileSystemUnavailable, FileSystemCorrupted {
		if (read_buff_system == null)
			throw new FileSystemUnavailable("Le fichier système n'est pas encore ouvert en lecture");
		
		try {
			String[] tab_file_name = new String[Integer.parseInt(read_buff_system.readLine())];
			read_buff_system.mark(0);
			
			for (int i = 0; i < tab_file_name.length; i++) {
				tab_file_name[i] = read_buff_system.readLine();
			}
			read_buff_system.reset();
			
			return tab_file_name;
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
			throw new FileSystemCorrupted("Fichier system corrompu");
		}
	}
	
	public static boolean AddFileName(String name) throws FileSystemUnavailable {
		if (writer_buff_system == null)
			throw new FileSystemUnavailable("Le fichier système n'est pas encore ouvert en écriture");
		if (read_buff_system == null)
			throw new FileSystemUnavailable("Le fichier système n'est pas encore ouvert en lecture");
		
		try {
			/* on vérifie que le fichier n'est pas déjà présent dans la liste d'exclusion */
			String line;
			while ((line = read_buff_system.readLine()) != null) {
				if (line.equals(name))
					return true;
			}
			read_buff_system.reset();
			
			writer_buff_system.write(name);
			
			return true;
			
		} catch (IOException e) {
			return false;
		}
	}

	//retourne un ArrayList de String contenant le nom et le chemin relatif de tous les fichiers trouvés
	// THROWS UnfoundFile si le fichier n'existe pas ou IsNotDirectory si c'est un fichier.
	public static ArrayList<String> getFilms() throws UnfoundFile, IsNotDirectory{
		checkPresence(new File(WORK_DIRECTORY)); //vérifie l'existence du fichier ou du dossier
		
		return findFiles(new File(WORK_DIRECTORY)); // appel à la fonction récurssive
	}
	
	//renomme un fichier à partir de son chemin relatif. Retourne TRUE si pas d'erreur
	//THROWS UnfoundedFile si le fichier old_name n'existe pas ou IsnotFile s'il s'agit d'un dossier
	public static boolean renameFile(String old_name, String new_name) throws UnfoundFile, IsNotFile{
		File f = new File(WORK_DIRECTORY+old_name);
		
		checkIsFile(f); //Vérifie qu'il s'agisse bien d'un fichier
		
		return f.renameTo(new File(WORK_DIRECTORY+new_name));
	}
	
	//lance la lecture d'une video avec le lecteur par défaut.
	//THROWS CannotPlayMovie si echec de la lecture
	public static void playMovie(String name) throws CannotPlayMovie{
		Desktop desktop = Desktop.getDesktop();
		if (desktop.isSupported(Desktop.Action.OPEN)) { // test la compatiilité
			try {
				desktop.open(new File(WORK_DIRECTORY+name));
			}catch (Exception e) {
				throw new CannotPlayMovie("Cannot play movie " + name);
			}
		}
		else 
			throw new CannotPlayMovie("Cannot play movie " + name);
	}
	
	/* supprime le fichier portant le nom name du support */
	public static boolean deleteMovie(String name) throws CannotDeleteMovie, UnfoundFile, IsNotFile {
		File f = new File(WORK_DIRECTORY+name);
		
		checkIsFile(f);
		
		return f.delete();
	}
	
	/* retourne le nom du fichier le plus proche possible du titre du film pour être recherché */
	public static String ExtractNameMovie(String str_in) {
		String str_out = new String(str_in);
		

		/*  on sauvegarde le(s) dossiers parents avant le traitement */
		int i;
		while ((i = str_out.indexOf("\\")) != -1) {
			str_out = str_out.substring(i+1, str_out.length());
		}
		
		/* on retire l'extension */
		i = str_out.length() -1;
		while (str_out.charAt(i) != '.')
			i--;
		str_out = str_out.substring(0,i);
		
		str_out = str_out.replace('.', ' ');
		
		Pattern p = Pattern.compile("\\[.+\\]");
		Matcher m = p.matcher(str_out);
		str_out = m.replaceAll("");
		
		if ((i = str_out.toLowerCase().indexOf("truefrench")) != -1)
			str_out = str_out.substring(0, i);
		
		if ((i = str_out.toLowerCase().indexOf("french")) != -1)
			str_out = str_out.substring(0, i);
		
		if ((i = str_out.toLowerCase().indexOf("vf")) != -1)
			str_out = str_out.substring(0, i) + str_out.substring(i+2, str_out.length());
		
		if ((i = str_out.toLowerCase().indexOf("vo ")) != -1)
			str_out = str_out.substring(0, i) + str_out.substring(i+2, str_out.length());
		
		if ((i = str_out.toLowerCase().indexOf("vostf")) != -1)
			str_out = str_out.substring(0, i);
		
		if ((i = str_out.toLowerCase().indexOf("vostfr")) != -1)
			str_out = str_out.substring(0, i);
		
		if ((i = str_out.toLowerCase().indexOf("fr ")) != -1)
			str_out = str_out.substring(0, i) + str_out.substring(i+2, str_out.length());
		
		if ((i = str_out.toLowerCase().indexOf("vostgb")) != -1)
			str_out = str_out.substring(0, i);
		
		if ((i = str_out.toLowerCase().indexOf("720p")) != -1)
			str_out = str_out.substring(0, i);
		
		if ((i = str_out.toLowerCase().indexOf("1080p")) != -1)
			str_out = str_out.substring(0, i);
		
		if ((i = str_out.toLowerCase().indexOf("dvdrip")) != -1)
			str_out = str_out.substring(0, i);
		
		if ((i = str_out.toLowerCase().indexOf("dvd")) != -1)
			str_out = str_out.substring(0, i);
		
		
		if ((i = str_out.toLowerCase().indexOf("divx")) != -1)
			str_out = str_out.substring(0, i);
		
		if ((i = str_out.toLowerCase().indexOf("by")) != -1)
			str_out = str_out.substring(0, i);
		
		if ((i = str_out.toLowerCase().indexOf("multi ")) != -1)
			str_out = str_out.substring(0, i);
				
		return str_out;
	}
	
	//******************************************//
	//************** PRIVATE *******************//
	//******************************************//
	
	//fonction récurssive de recherche de tous les fichier d'un dossier d
	private static ArrayList<String> findFiles(File d) throws IsNotDirectory {
		ArrayList <String> tabF = new ArrayList<String>();
		
		checkIsDirectory(d);
		
		if (d.listFiles() != null)
			for (File f : d.listFiles()) { 
				if (f.isDirectory()) // si on trouve encore un dossier alors il faut l'ouvrir pour rechercher d'éventuels fichiers
					tabF.addAll(findFiles(f));
				else {//sinon, on ajoute à ArrayList le nom du fichier trouvé avec son chemin relatif
					String str_temp = f.getPath().substring(WORK_DIRECTORY.length());
					if (isMovie(str_temp))
						tabF.add(str_temp);
				}
			}
		return tabF;
	}
	
	//Vérifie que le fichier ou dossier existe et lève l'exception UnfoundFile sinon
	private static void checkPresence(File f)  throws UnfoundFile{
		if (!(new File(f.getAbsolutePath()).exists()))
			throw new UnfoundFile("Cannot find directory " + f.getName());
	}
	
	//Vérifie que le fichier est un dossier et lève l'exception IsNotDirectory sinon
	private static void checkIsDirectory(File f)  throws IsNotDirectory{
		if (!new File(f.getAbsolutePath()).isDirectory())
			throw new IsNotDirectory(f.getName() + " is a file, not a directory");
	}
	
	//Vérifie que le fichier n'est pas un dossier et lève l'exception IsNotFile sinon
	private static void checkIsFile(File f)  throws IsNotFile{
		if (new File(f.getAbsolutePath()).isDirectory())
			throw new IsNotFile(f.getName() + " is a directory, not a file");
	}
	
	/* retourne vrai si l'extension du fichier correspond à celle d'un film */
	private static boolean isMovie(String str_in) {
		return (str_in.contains(".avi") || str_in.contains(".AVI") || str_in.contains(".flv") || str_in.contains(".mkv") || str_in.contains(".avi")
				|| str_in.contains(".mp4") || str_in.contains(".ogm") || str_in.contains(".m4v") || str_in.contains(".MKV") 
				|| str_in.contains(".Avi"));
	}
}
