package mf.exception.files;


@SuppressWarnings("serial")
public class CannotPlayMovie extends Exception {
	
	public CannotPlayMovie(String message){
		System.out.println(message);
	}
}