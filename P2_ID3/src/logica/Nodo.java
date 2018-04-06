package logica;

import java.util.ArrayList;

public class Nodo {
	
	private int id;
	private String nombre;
	private String arista;
	private Nodo nodoAnterior;
	private ArrayList<Nodo> listaNodosSig;
	
	public Nodo(int id, String nombre) {
		this.id = id;
		this.nombre = (nombre);
		this.listaNodosSig = new ArrayList<Nodo>();
	}
	
	public Nodo(int id, String nombre, String accion, Nodo nodoAnterior) {
		this.id = id;
		this.nombre = nombre;
		this.setArista(accion);
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
	public String getArista() {
		return arista;
	}

	/**
	 * @param accion the accion to set
	 */
	public void setArista(String accion) {
		this.arista = accion;
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
	
	public void eliminarNodoSiguiente(int index) {
		this.listaNodosSig.remove(index);
	}

}
