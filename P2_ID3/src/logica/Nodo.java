package logica;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Nodo {
	
	private int id;
	private String nombre;
	private String accion;
	private Nodo nodoAnterior;
	private ArrayList<Nodo> listaNodosSig;
	
	public Nodo(int id, String nombre) {
		this.id = (id);
		this.nombre = (nombre);
		this.listaNodosSig = new ArrayList<Nodo>();
	}
	
	public Nodo(int id, String nombre, String accion, Nodo nodoAnterior) {
		this.id = (id);
		this.nombre = nombre;
		this.setAccion(accion);
		this.setNodoAnterior(nodoAnterior);
	}

	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @return the accion
	 */
	public String getAccion() {
		return accion;
	}

	/**
	 * @param accion the accion to set
	 */
	public void setAccion(String accion) {
		this.accion = accion;
	}

	/**
	 * @return the nodoAnterior
	 */
	public Nodo getNodoAnterior() {
		return nodoAnterior;
	}

	/**
	 * @param nodoAnterior the nodoAnterior to set
	 */
	public void setNodoAnterior(Nodo nodoAnterior) {
		if (nodoAnterior == null) 
			this.nodoAnterior = null;
		else
			this.nodoAnterior = nodoAnterior;
	}

	public ArrayList<Nodo> getListaNodosSig() {
		return listaNodosSig;
	}

	public void setNodoSiguiente(Nodo siguienteNodo) {
		if (siguienteNodo != null)
			this.listaNodosSig.add(siguienteNodo);
		else
			this.listaNodosSig = null;
	}

}
