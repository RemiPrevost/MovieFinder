package mf.exception.files;

@SuppressWarnings("serial")
public class FileSystemUnavailable extends Exception {

	public FileSystemUnavailable(String message) {
		System.out.println(message);
	}


}
