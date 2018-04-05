package logica;

import java.util.HashMap;
import java.util.Map;

public class Nodo {
	
	private int id;
	private String nombre;
	private String accion;
	private int nodoAnterior;
	private Map<String, String> condicion;
	
	public Nodo(int id, String nombre) {
		this.id = (id);
		this.nombre = (nombre);
		this.condicion = new HashMap<String, String>();
	}
	
	public Nodo(int id, String nombre, String accion, int nodoAnterior, Map<String, String> condicion) {
		this.id = (id);
		this.nombre = nombre;
		this.setAccion(accion);
		this.setNodoAnterior(nodoAnterior);
		this.condicion = (condicion);
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
	public int getNodoAnterior() {
		return nodoAnterior;
	}

	/**
	 * @param nodoAnterior the nodoAnterior to set
	 */
	public void setNodoAnterior(int nodoAnterior) {
		this.nodoAnterior = nodoAnterior;
	}

	/**
	 * @return the condicion
	 */
	public Map<String, String> getCondicion() {
		return condicion;
	}

	/**
	 * @param condicion the condicion to set
	 */
	public void setCondicion(String nombre, String condicion) {
		this.condicion.put(nombre, condicion);
	}

}
