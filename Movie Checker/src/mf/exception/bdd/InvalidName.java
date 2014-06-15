package mf.exception.bdd;

@SuppressWarnings("serial")
public class InvalidName extends Exception {

		public InvalidName(){
			super();
		}
		public InvalidName(String message){
			super(message);
		}
	}
