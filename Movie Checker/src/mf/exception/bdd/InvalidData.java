package mf.exception.bdd;

@SuppressWarnings("serial")
public class InvalidData extends Exception {

		public InvalidData(){
			super();
		}
		public InvalidData(String message){
			super(message);
		}
	}
