package ui;

import java.awt.EventQueue;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import logica.ID3;
import logica.TransferArchivos;

public class WindowController {
	
	private Controlador contr;
	private Estados state;
	
	public WindowController(Controlador c) {
		contr = c;
		state = Estados.LEER_ATRIB;
		contr.setState(state);
		contr.setAlgoritmo(new ID3());
		contr.setUi(this);
	}
	
	public void run() {
		switch (state) {
			case LEER_ATRIB: leerArchivos(); break;
			case LEER_JUEGO: leerArchivos(); break;
			default: break;
		}
	}
	
	public void update() {
		state = contr.getState();
		run();
	}
	
	private void leerArchivos() {
		try {
			EventQueue.invokeLater(new Runnable() {
				@Override
			    public void run() {
					JFileChooser c = new JFileChooser();
				      // Demonstrate "Open" dialog:
				      int rVal = c.showOpenDialog(null);
				      if (rVal == JFileChooser.APPROVE_OPTION) {
							TransferArchivos transfer = new TransferArchivos();
							transfer.setNombre(c.getSelectedFile().getName());
							transfer.setRuta(c.getCurrentDirectory().toString() + '/');
							try {
								c.removeAll();
								contr.accion(transfer);
							} catch (Exception e) {
								e.printStackTrace(); 
								System.exit(0);
							}
				      }
				      else if (rVal == JFileChooser.CANCEL_OPTION) {
				    	  JOptionPane.showMessageDialog(null, "Debe seleccionar los archivos. Ejecute de nuevo la aplicación");
				    	  System.exit(0);
				      }
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error catastrofico. Ejecute de nuevo la aplicación");
	    	  System.exit(0);
		}
	}

}
