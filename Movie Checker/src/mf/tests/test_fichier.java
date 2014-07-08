package mf.tests;

import java.io.File;

import javax.swing.filechooser.FileSystemView;



public class test_fichier {

	public static void main(String[] args) {
		FileSystemView FSV = FileSystemView.getFileSystemView();
		for (int i = 65; i < 91; i++) {
			File f = new File((char)i+new String(":\\"));
			if (f.exists())
				System.out.println(FSV.getSystemDisplayName(f));
		}
	}

}
