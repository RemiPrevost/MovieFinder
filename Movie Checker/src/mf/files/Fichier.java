package mf.files;
import java.io.File;
import java.util.ArrayList;
import java.awt.Desktop;

import mf.exception.files.*;


@SuppressWarnings("serial")
public class Fichier extends File{
	// enregistre le nom du r�pertoire de travail ou le chemin complet s'il s'agit d'un fichier
	private static String WORD_DIRECTORY = "";
	public static final String AFFICHE_DIRECTORY = "affiches\\";
	
	//********* CONSTRUCTEUR *********//
	public Fichier(String dir) throws UnfoundFile{
		super(dir);
		if (!dir.isEmpty())
			WORD_DIRECTORY = dir + "\\";
		this.checkPresence(); //v�rifie l'existence du fichier ou du dossier
	}
	
	//******************************************//
	//*************** PUBLIC *******************//
	//******************************************//
	
	//retourne un ArrayList de String contenant le nom et le chemin relatif de tous les fichiers trouv�s
	// THROWS UnfoundFile si le fichier n'existe pas ou IsNotDirectory si c'est un fichier.
	public ArrayList<String> getFilms() throws UnfoundFile, IsNotDirectory{
		this.checkPresence(); //v�rifie l'existence du fichier ou du dossier
		
		return findFiles(this); // appel � la fonction r�curssive
	}
	
	//renomme un fichier � partir de son chemin relatif. Retourne TRUE si pas d'erreur
	//THROWS UnfoundedFile si le fichier old_name n'existe pas ou IsnotFile s'il s'agit d'un dossier
	public boolean renameFile(String old_name, String new_name) throws UnfoundFile, IsNotFile{
		Fichier f = new Fichier(WORD_DIRECTORY+old_name);
		
		f.checkIsFile(); //V�rifie qu'il s'agisse bien d'un fichier
		
		return f.renameTo(new File(WORD_DIRECTORY+new_name));
	}
	
	//lance la lecture d'une video avec le lecteur par d�faut.
	//THROWS CannotPlayMovie si echec de la lecture
	public static void playMovie(String name) throws CannotPlayMovie{
		Desktop desktop = Desktop.getDesktop();
		if (desktop.isSupported(Desktop.Action.OPEN)) { // test la compatiilit�
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
	
	//fonction r�curssive de recherche de tous les fichier d'un dossier d
	private ArrayList<String> findFiles(File d) throws IsNotDirectory {
		ArrayList <String> tabF = new ArrayList<String>();
		
		checkIsDirectory();
		
		for (File f : d.listFiles()) { 
			if (f.isDirectory()) // si on trouve encore un dossier alors il faut l'ouvrir pour rechercher d'�ventuel fichiers
				tabF.addAll(findFiles(f));
			else //sinon, on ajoute � ArrayList le nom du fichier trouv� avec son chemin relatif
				tabF.add(f.getPath().substring(WORD_DIRECTORY.length()+1));
		}
		return tabF;
	}
	
	//V�rifie que le fichier ou dossier existe et l�ve l'exception UnfoundFile sinon
	private void checkPresence()  throws UnfoundFile{
		if (!(new File(WORD_DIRECTORY).exists()))
			throw new UnfoundFile("Cannot find directory " + WORD_DIRECTORY);
	}
	
	//V�rifie que le fichier est un dossier et l�ve l'exception IsNotDirectory sinon
	private void checkIsDirectory()  throws IsNotDirectory{
		if (!new File(WORD_DIRECTORY).isDirectory())
			throw new IsNotDirectory(WORD_DIRECTORY + " is a file, not a directory");
	}
	
	//V�rifie que le fichier n'est pas un dossier et l�ve l'exception IsNotFile sinon
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
