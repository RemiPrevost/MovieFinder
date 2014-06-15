package mf.exception.files;


@SuppressWarnings("serial")
public class CannotDeleteMovie extends Exception {
	
	public CannotDeleteMovie(String message){
		System.out.println(message);
	}
}