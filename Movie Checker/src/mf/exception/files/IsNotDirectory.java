package mf.exception.files;

@SuppressWarnings("serial")
public class IsNotDirectory extends Exception {
	public IsNotDirectory(){
		super();
	}
	public IsNotDirectory(String message){
		super(message);
	}
}