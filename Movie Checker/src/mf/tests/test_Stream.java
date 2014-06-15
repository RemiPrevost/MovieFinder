package mf.tests;

import java.io.IOException;

import mf.exception.net.ConnectionFail;
import mf.net.Stream;


public class test_Stream {
	public static void main(String[] args){
		test_updateStream();
		test_downloadImageToFolder();
	}
	
	private static void test_downloadImageToFolder() {
		try {
			Stream.downloadImageToFolder("http://fr.web.img4.acsta.net/r_640_600/b_1_d6d6d6/pictures/14/06/10/10/12/041503.jpg");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void test_updateStream(){
		try {
			Stream.launchSearch("sin city");
			while (Stream.hasNext())
				System.out.println(Stream.getNext().toString());
		} catch (ConnectionFail e) {
			e.printStackTrace();
		}
	}
}