package mf.tests;

import mf.exception.files.FileSystemCorrupted;
import mf.exception.files.FileSystemUnavailable;
import mf.files.Fichier;



public class test_fichier {

	public static void main(String[] args) {
		try {
			System.out.println(Fichier.OpenFileSystem(Fichier.WRITE));
			
			for (String s : Fichier.getTabFileName())
				System.out.println(s);
			
			System.out.println(Fichier.AddFileName("test"));
			System.out.println(Fichier.AddFileName("test3"));
			System.out.println();
			
			Fichier.CloseFileSystem();
			System.out.println(Fichier.OpenFileSystem(Fichier.READ));
			
			for (String s : Fichier.getTabFileName())
				System.out.println(s);
			
		} catch (FileSystemUnavailable | FileSystemCorrupted e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			Fichier.CloseFileSystem();
		}
	}

}
