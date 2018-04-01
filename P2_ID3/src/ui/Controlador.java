package ui;

import logica.Algoritmo;
import logica.TransferArchivos;

public class Controlador {
	
	private WindowController ui;
	private Estados state;
	private Algoritmo algoritmo;

	public Estados getState() {
		return state;
	}

	public void setState(Estados state) {
		this.state = state;
	}

	public void setAlgoritmo(Algoritmo algoritmo) {
		this.algoritmo = algoritmo;
	}

	public void setUi(WindowController ui) {
		this.ui = ui;
	}
	
	public void accion(Object datos) throws Exception {
		
		switch(state) {
			case LEER_ATRIB: 
				algoritmo.cargarTipos((TransferArchivos)datos); 
				setState(Estados.LEER_JUEGO);
				break;
			case LEER_JUEGO: 
				algoritmo.cargarDatos((TransferArchivos)datos); 
				setState(Estados.LEER_JUEGO);
				break;
			default: break;
		}
		
		ui.update();
	}

}
