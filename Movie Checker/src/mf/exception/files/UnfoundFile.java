package mf.exception.files;

@SuppressWarnings("serial")
public class UnfoundFile extends Exception {
	public UnfoundFile(){
		super();
	}
	public UnfoundFile(String message){
		super(message);
	}
}
