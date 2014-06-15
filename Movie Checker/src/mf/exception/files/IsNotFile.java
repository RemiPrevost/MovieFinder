package mf.exception.files;

@SuppressWarnings("serial")
public class IsNotFile extends Exception {
	public IsNotFile(){
		super();
	}
	public IsNotFile(String message){
		super(message);
	}
}