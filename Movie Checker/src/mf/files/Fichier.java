package mf.files;
import java.io.File;
import java.util.ArrayList;
import java.awt.Desktop;

import mf.exception.files.*;


@SuppressWarnings("serial")
public class Fichier extends File{
	// enregistre le nom du répertoire de travail ou le chemin complet s'il s'agit d'un fichier
	private static String WORD_DIRECTORY = "";
	public static final String AFFICHE_DIRECTORY = "affiches\\";
	
	//********* CONSTRUCTEUR *********//
	public Fichier(String dir) throws UnfoundFile{
		super(dir);
		if (!dir.isEmpty())
			WORD_DIRECTORY = dir + "\\";
		this.checkPresence(); //vérifie l'existence du fichier ou du dossier
	}
	
	//******************************************//
	//*************** PUBLIC *******************//
	//******************************************//
	
	//retourne un ArrayList de String contenant le nom et le chemin relatif de tous les fichiers trouvés
	// THROWS UnfoundFile si le fichier n'existe pas ou IsNotDirectory si c'est un fichier.
	public ArrayList<String> getFilms() throws UnfoundFile, IsNotDirectory{
		this.checkPresence(); //vérifie l'existence du fichier ou du dossier
		
		return findFiles(this); // appel à la fonction récurssive
	}
	
	//renomme un fichier à partir de son chemin relatif. Retourne TRUE si pas d'erreur
	//THROWS UnfoundedFile si le fichier old_name n'existe pas ou IsnotFile s'il s'agit d'un dossier
	public boolean renameFile(String old_name, String new_name) throws UnfoundFile, IsNotFile{
		Fichier f = new Fichier(WORD_DIRECTORY+old_name);
		
		f.checkIsFile(); //Vérifie qu'il s'agisse bien d'un fichier
		
		return f.renameTo(new File(WORD_DIRECTORY+new_name));
	}
	
	//lance la lecture d'une video avec le lecteur par défaut.
	//THROWS CannotPlayMovie si echec de la lecture
	public static void playMovie(String name) throws CannotPlayMovie{
		Desktop desktop = Desktop.getDesktop();
		if (desktop.isSupported(Desktop.Action.OPEN)) { // test la compatiilité
			try {
				desktop.open(new File(WORD_DIRECTORY+name));
			}catch (Exception e) {
				throw new CannotPlayMovie("Cannot play movie " + name);
			}
		}
		else 
			throw new CannotPlayMovie("Cannot play movie " + name);
	}
	
	public static boolean deleteMovie(String name) throws CannotDeleteMovie, UnfoundFile, IsNotFile {
		Fichier f = new Fichier(WORD_DIRECTORY+name);
		
		f.checkIsFile();
		
		return f.delete();
	}
	
	//******************************************//
	//************** PRIVATE *******************//
	//******************************************//
	
	//fonction récurssive de recherche de tous les fichier d'un dossier d
	private ArrayList<String> findFiles(File d) throws IsNotDirectory {
		ArrayList <String> tabF = new ArrayList<String>();
		
		checkIsDirectory();
		
		for (File f : d.listFiles()) { 
			if (f.isDirectory()) // si on trouve encore un dossier alors il faut l'ouvrir pour rechercher d'éventuel fichiers
				tabF.addAll(findFiles(f));
			else //sinon, on ajoute à ArrayList le nom du fichier trouvé avec son chemin relatif
				tabF.add(f.getPath().substring(WORD_DIRECTORY.length()+1));
		}
		return tabF;
	}
	
	//Vérifie que le fichier ou dossier existe et lève l'exception UnfoundFile sinon
	private void checkPresence()  throws UnfoundFile{
		if (!(new File(WORD_DIRECTORY).exists()))
			throw new UnfoundFile("Cannot find directory " + WORD_DIRECTORY);
	}
	
	//Vérifie que le fichier est un dossier et lève l'exception IsNotDirectory sinon
	private void checkIsDirectory()  throws IsNotDirectory{
		if (!new File(WORD_DIRECTORY).isDirectory())
			throw new IsNotDirectory(WORD_DIRECTORY + " is a file, not a directory");
	}
	
	//Vérifie que le fichier n'est pas un dossier et lève l'exception IsNotFile sinon
	private void checkIsFile()  throws IsNotFile{
		if (new File(WORD_DIRECTORY).isDirectory())
			throw new IsNotFile(WORD_DIRECTORY + " is a directory, not a file");
	}
	
	/*private ArrayList<String> filterExtension(ArrayList<String> fichiers) {
		ArrayList<String> extensions = new ArrayList<String>();
		
		for (String f : fichiers) {
			if (!extensions.contains(f.substring(f.indexOf("."))))
				extensions.add(f.substring(f.indexOf(".")+1));
		}
		System.out.println(extensions.toString());
		return extensions;
	}*/
}
