package mf.exception.net;

@SuppressWarnings("serial")
public class ConnectionFail extends Exception {
	
	public ConnectionFail(String message){
		System.out.println(message);
	}
}