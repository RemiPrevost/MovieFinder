package mf;
import mf.controller.Controller;
import mf.model.Model;

public class Main {

	public static void main(String[] args) {
		
		Model model = new Model();
		new Controller(model);

	}

}
