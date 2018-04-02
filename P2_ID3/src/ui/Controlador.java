package ui;

import logica.ID3;
import logica.TransferArchivos;

public class Controlador {
	
	private WindowController ui;
	private Estados state;
	private ID3 algoritmo;

	public Estados getState() {
		return state;
	}

	public void setState(Estados state) {
		this.state = state;
	}

	public void setAlgoritmo(ID3 algoritmo) {
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
				setState(Estados.PROCESAR);
				this.accion(null); //MODIFICAR ESTO!
				break;
			case PROCESAR:
				algoritmo.procesar();
				setState(Estados.FIN);
				break;
			default: break;
		}
		
		ui.update();
	}

}
