package id3;

import ui.Controlador;
import ui.WindowController;

public class Main {

	public static void main(String[] args) {
		
		Controlador controlador = new Controlador();
		WindowController ui = new WindowController(controlador);
		
		ui.run();

	}

}
