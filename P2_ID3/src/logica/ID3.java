package logica;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dao.LectorFicheros;

public class ID3 {

	private LectorFicheros dao;
	private ArrayList<String> ListaAtributos;
	private ArrayList<ArrayList<String>> ListaEjemplos;
	private ArrayList<String> ListaMeritos;
	private Map<String, Float> MapaDecisiones;
	private Nodo resultado;
	
	public ID3() {
		dao = new LectorFicheros();
		ListaAtributos = new ArrayList<String>();
		ListaEjemplos = new ArrayList<ArrayList<String>>();
		ListaMeritos = new ArrayList<String>();
		resultado = null;
		MapaDecisiones = new HashMap<String, Float>();
	}
	
	public void cargarTipos(TransferArchivos transfer) throws Exception {
		List<String> list = dao.leerTipos(transfer.getRuta() + transfer.getNombre());
		ListaAtributos.addAll(list);
	}
	
	public void cargarDatos(TransferArchivos transfer) throws Exception {
		List<String> list = dao.leerDatos(transfer.getRuta() + transfer.getNombre(), ListaAtributos);
		ArrayList<String> temp = new ArrayList<String>();
		int n = 0;
		for (int i = 0; i < list.size(); i++) {
			if (n >= ListaAtributos.size()) {
				n = 0;
				ListaEjemplos.add(temp);
				temp = new ArrayList<String>();
			}
			temp.add(list.get(i));
			n++;
		}
		ListaEjemplos.add(temp);
	}
	
	public Nodo getNodoResultado() {
		return this.resultado;
	}
	
	public void procesar() {
		Map<String, Float> meritos = new HashMap<String, Float>(); 
		int vuelta = 0;
		
		for (ArrayList<String> fila: ListaEjemplos) {
			if (!MapaDecisiones.containsKey(fila.get(fila.size()-1)))
				MapaDecisiones.put(fila.get(fila.size()-1), (float) 0);
		}
		
		while (ListaMeritos.size() != (ListaAtributos.size() - 1)) {
			meritos.clear();
			for (int i = 0; i < this.ListaAtributos.size() - 1; i++) {
				if(!ListaMeritos.contains(ListaAtributos.get(i)))
					meritos.put(ListaAtributos.get(i), calcularMerito(i));
			}
			
			siguienteRama(getMinKey(meritos), vuelta);
			ListaMeritos.add(getMinKey(meritos));
			vuelta++;
		}
	} 
	
	private float calcularMerito(int col) {
		ArrayList<String> tipo = new ArrayList<String>();
		ArrayList<Float> resultadosInfor = new ArrayList<Float>();
		int totalMeritos = ListaEjemplos.size();
		Map<String, Float> mapaMeritos = new HashMap<String, Float>();
		float resultado = 0;
		
		for (int i = 0; i < totalMeritos; i++) {
			if (!tipo.contains(ListaEjemplos.get(i).get(col))) {
				tipo.add(ListaEjemplos.get(i).get(col));
				mapaMeritos.put(ListaEjemplos.get(i).get(col), (float) 1);
			} else {
				float temp = mapaMeritos.get(ListaEjemplos.get(i).get(col));
				mapaMeritos.put(ListaEjemplos.get(i).get(col), temp + 1);
			}
		}
		
		for (int n = 0; n < mapaMeritos.size(); n++) {
			
			for (int i = 0; i < totalMeritos; i++) {
				if (tipo.get(n).equals(ListaEjemplos.get(i).get(col))) {
					float aux = MapaDecisiones.get(ListaEjemplos.get(i).get((ListaEjemplos.get(i).size())-1));
					MapaDecisiones.put(ListaEjemplos.get(i).get(ListaEjemplos.get(i).size()-1), aux + 1);
				}
			}
			Float merito = (mapaMeritos.get(tipo.get(n))/totalMeritos);
			float operacion = 0;
			for (String key: MapaDecisiones.keySet()) {
				float pi = (MapaDecisiones.get(key)/mapaMeritos.get(tipo.get(n)));
				float log = (float) (Math.log(pi)/(Math.log(2)));
				operacion += (-pi * log);
			}
			merito = merito * operacion;
			if (merito.equals(Float.NaN))
				merito = (float) 0;
			resultadosInfor.add(merito);
			for (String key: MapaDecisiones.keySet()) {
				MapaDecisiones.put(key, (float) 0);
			}
		}
		
		for (int i = 0; i < resultadosInfor.size(); i++) {
			resultado += resultadosInfor.get(i);
		}
		
		return resultado;
	}
	
	private String getMinKey(Map<String, Float> map) {
	    String minKey = null;
	    float minValue = Float.MAX_VALUE;
	    for(int i = 0; i < ListaAtributos.size() - 1; i++) {
	    	if (ListaMeritos.contains(ListaAtributos.get(i)))
	    		continue;
	    	String key = ListaAtributos.get(i);
	        float value = map.get(key);
	        if(value < minValue) {
	            minValue = value;
	            minKey = key;
	        }
	    }
	    return minKey;
	}
	
	private void siguienteRama(String ramaActual, int vuelta) {
		if (resultado == null)
			resultado = new Nodo(vuelta, ramaActual);
		Map<String, Map<String, Integer>> map = new HashMap<String, Map<String, Integer>>();
		int total = ListaEjemplos.size();
		int col = ListaAtributos.indexOf(ramaActual);
		for (int i = 0; i < total; i++) {
			if(!map.containsKey(ListaEjemplos.get(i).get(col)))
				map.put(ListaEjemplos.get(i).get(col), new HashMap<String, Integer>());
		}
		for (String nombre: map.keySet()) {
			for (int n = 0; n < ListaEjemplos.size(); n++) {
				if (ListaEjemplos.get(n).get(col).equals(nombre)) {
					String temp = ListaEjemplos.get(n).get(ListaAtributos.size()-1);
					if (!map.get(nombre).containsKey(temp)) {
						map.get(nombre).put(temp, 1);
					} else {
						Map <String, Integer> aux = map.get(nombre);
						aux.put(temp, aux.get(temp) +1);
					}
				}
			}
		}
		agregarNodosSucesores(map, vuelta, resultado, ramaActual);
			
	}
	
	private void agregarNodosSucesores(Map<String, Map<String, Integer>> map, 
			int vuelta, Nodo nodo, String nombreNodo) {
		if (nodo.getListaNodosSig() != null && nodo.getListaNodosSig().isEmpty()) {
			nodo.setNombre(nombreNodo);
			for (String nombre: map.keySet()) {
				Map<String, Integer> temp = map.get(nombre);
				if (temp.size() == 1) {
					for (String key: temp.keySet()) {
						Nodo nodoSiguiente = new Nodo(vuelta + 1 , key);
						nodoSiguiente.setAccion(nombre);
						nodoSiguiente.setNodoSiguiente(null);
						nodoSiguiente.setNodoAnterior(nodo);
						nodo.setNodoSiguiente(nodoSiguiente);
					}
				} else if (vuelta != ListaAtributos.size() - 2){
					Nodo nodoSiguiente = new Nodo(vuelta + 1 , null);
					nodoSiguiente.setAccion(nombre);
					nodoSiguiente.setNodoAnterior(nodo);
					nodo.setNodoSiguiente(nodoSiguiente);
				} else {
					Nodo nodoSiguiente = new Nodo(vuelta + 1 , null);
					nodoSiguiente.setAccion(nombre);
					nodoSiguiente.setNodoAnterior(nodo);
					cerrarArbol(nodoSiguiente);
					if (!nodoSiguiente.getNombre().equals("?"))
						nodo.setNodoSiguiente(nodoSiguiente);
				}
			}
		} else if (nodo.getListaNodosSig() != null){
			for (int i = 0; i < nodo.getListaNodosSig().size(); i++) {
				Nodo nodoHijo = nodo.getListaNodosSig().get(i);
				agregarNodosSucesores(map, vuelta, nodoHijo, nombreNodo);
				if (nodoHijo.getListaNodosSig() != null && nodoHijo.getListaNodosSig().isEmpty() && 
						!MapaDecisiones.containsKey(nodoHijo.getNombre())) {
					nodoHijo.getNodoAnterior().eliminarNodoSiguiente(nodo.getListaNodosSig().indexOf(nodoHijo));
					i = -1;
				}
			}
		}
	}
	
	private void cerrarArbol(Nodo nodo) {
		nodo.setNodoSiguiente(null);
		
		ArrayList<String> map = new ArrayList<String>();
		Boolean continua = true;
		ArrayList<ArrayList<String>> filas = new ArrayList<ArrayList<String>>();
		Nodo nodoAnterior = nodo.getNodoAnterior();
		map.add(nodo.getAccion());
		while (nodoAnterior != null && nodoAnterior.getAccion() != null){
			map.add(nodoAnterior.getAccion());
			nodoAnterior = nodoAnterior.getNodoAnterior();
		}
		for (ArrayList<String> fila: ListaEjemplos) {
			for (int c = 0; c < fila.size() - 1 && continua; c++) {
				String dato = fila.get(c);
				if (!map.contains(dato))
					continua = false;
			} if (continua)
				filas.add(ListaEjemplos.get(ListaEjemplos.indexOf(fila)));
			continua = true;
		}
		Map<String, Integer> respuestas = new HashMap<String, Integer>();
		for (ArrayList<String> fila: filas) { 
			if(!respuestas.containsKey(fila.get(ListaAtributos.size() - 1)))
				respuestas.put(fila.get(ListaAtributos.size() - 1), 1);
			else {
				int temp = respuestas.get(fila.get(ListaAtributos.size() - 1));
				respuestas.put(fila.get(ListaAtributos.size() - 1), temp + 1);
			}
		} 
		if (respuestas.size() == 1) {
			for (String key: respuestas.keySet()) {
				nodo.setNombre(key);
			}
		}
		else
			nodo.setNombre("?");
	}
	
}
