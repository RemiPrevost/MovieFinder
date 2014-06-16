package mf.tests;

import java.util.ArrayList;

import mf.exception.files.IsNotDirectory;
import mf.exception.files.UnfoundFile;
import mf.files.Fichier;


public class test_fichier {

	public static void main(String[] args) {
		try {
			ArrayList<String> LF = Fichier.getFilms();
			for (String s : LF) {
				System.out.println(s);
				System.out.println(Fichier.ExtractNameMovie(s));
				System.out.println();
			}
			
			
		} catch (UnfoundFile | IsNotDirectory e) {
			e.printStackTrace();
		}
		
		
		
	}

}
