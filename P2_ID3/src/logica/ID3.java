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
	private Map<String, Float> MapaDecisiones;
	private Nodo resultado;
	
	public ID3() {
		dao = new LectorFicheros();
		ListaAtributos = new ArrayList<String>();
		ListaEjemplos = new ArrayList<ArrayList<String>>();
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
		
		ArrayList<String> aristas = new ArrayList<String>();
		Map<String, Integer> ListaMeritos = new HashMap<String, Integer>();
		for (int i = 0; i < this.ListaAtributos.size() - 1; i++) {
				meritos.put(ListaAtributos.get(i), calcularMerito(i, aristas, ListaMeritos));
		}
		String minKey = getMinKey(meritos, ListaMeritos);
		
		ListaMeritos.put(minKey, ListaAtributos.indexOf(minKey));
		resultado = new Nodo(vuelta, minKey);
		resultado = siguienteRama(resultado, minKey, vuelta, aristas, ListaMeritos);
		vuelta++;
	} 
	
	private float calcularMerito(int col, ArrayList<String> aristas, Map<String, Integer> ListaMeritos) {
		ArrayList<Float> resultadosInfor = new ArrayList<Float>();
		int totalMeritos = ListaEjemplos.size();
		Map<String, Float> mapaMeritos = new HashMap<String, Float>();
		float resultado = 0;
		
		for (int i = 0; i < totalMeritos; i++) {
			Boolean anadir = true;
			if (!ListaMeritos.isEmpty()) {
				for (String clave: ListaMeritos.keySet()) {
					if (!aristas.contains(ListaEjemplos.get(i).get(ListaMeritos.get(clave))))
						anadir = false;
				}
			}
			if (anadir) {
				if (!mapaMeritos.containsKey(ListaEjemplos.get(i).get(col))) {
					mapaMeritos.put(ListaEjemplos.get(i).get(col), (float) 1);
				} else {
					float temp = mapaMeritos.get(ListaEjemplos.get(i).get(col));
					mapaMeritos.put(ListaEjemplos.get(i).get(col), temp + 1);
				}
			}
		}
		
		for (String tipo: mapaMeritos.keySet()) {
			for (int i = 0; i < totalMeritos; i++) {
				if (tipo.equals(ListaEjemplos.get(i).get(col))) {
					Boolean anadir = true;
					if (ListaMeritos.isEmpty()) {
						float aux = MapaDecisiones.get(ListaEjemplos.get(i).get((ListaEjemplos.get(i).size())-1));
						MapaDecisiones.put(ListaEjemplos.get(i).get(ListaEjemplos.get(i).size()-1), aux + 1);
					}
					else {
						for (String clave: ListaMeritos.keySet()) {
							if (!aristas.contains(ListaEjemplos.get(i).get(ListaMeritos.get(clave))))
								anadir = false;
						}
						if (anadir) {
							float aux = MapaDecisiones.get(ListaEjemplos.get(i).get((ListaEjemplos.get(i).size())-1));
							MapaDecisiones.put(ListaEjemplos.get(i).get(ListaEjemplos.get(i).size()-1), aux + 1);
						}
					}
				}
			}
			Float merito = (mapaMeritos.get(tipo)/totalMeritos);
			float operacion = 0;
			for (String key: MapaDecisiones.keySet()) {
				float pi = (MapaDecisiones.get(key)/mapaMeritos.get(tipo));
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
	
	private String getMinKey(Map<String, Float> map, Map<String, Integer> ListaMeritos) {
	    String minKey = null;
	    float minValue = Float.MAX_VALUE;
	    for(int i = 0; i < ListaAtributos.size() - 1; i++) {
	    	if (ListaMeritos.containsKey(ListaAtributos.get(i)))
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
	
	private Nodo siguienteRama(Nodo nodo, String ramaActual, int vuelta, ArrayList<String> e,
			Map<String, Integer> ListaMeritos) {
		Map<String, Map<String, Integer>> map = new HashMap<String, Map<String, Integer>>();
		int total = ListaEjemplos.size();
		int col = ListaAtributos.indexOf(ramaActual);
		for (int i = 0; i < total; i++) {
			if(!map.containsKey(ListaEjemplos.get(i).get(col)))
				map.put(ListaEjemplos.get(i).get(col), new HashMap<String, Integer>());
		}
		for (String nombre: map.keySet()) {
			for (int n = 0; n < ListaEjemplos.size(); n++) {
				Boolean anadir = true;
				if (!ListaMeritos.isEmpty() && !e.isEmpty()) {
					for (String clave: ListaMeritos.keySet()) {
						if (!clave.equals(ramaActual) && 
								!e.contains(ListaEjemplos.get(n).get(ListaMeritos.get(clave))))
							anadir = false;
					}
				}
				if (anadir && ListaEjemplos.get(n).get(col).equals(nombre)) {
					String temp = ListaEjemplos.get(n).get(ListaAtributos.size()-1);
					if (!map.get(nombre).containsKey(temp)) {
						map.get(nombre).put(temp, 1);
					} else {
						Map <String, Integer> aux = map.get(nombre);
						aux.put(temp, aux.get(temp) +1);
					}
				}
			}
			agregarNodosSucesores(map, nombre, vuelta, nodo, ramaActual);
		}
		if (nodo.getListaNodosSig() != null && !nodo.getListaNodosSig().isEmpty()) {
			for (Nodo nodoSig: nodo.getListaNodosSig()) {
				ArrayList<String> aristas = new ArrayList<String>();
				aristas.addAll(e);
				aristas.add(nodoSig.getArista());
				Map<String, Float> meritos = new HashMap<String, Float>();
				for (int i = 0; i < this.ListaAtributos.size() - 1; i++) {
					if(!ListaMeritos.containsKey(ListaAtributos.get(i)) && !nodo.getNombre().equals(ListaAtributos.get(i)))
						meritos.put(ListaAtributos.get(i), calcularMerito(i,aristas, ListaMeritos));
				}
				String minKey = getMinKey(meritos, ListaMeritos);
				if (minKey != null) {
					Map<String, Integer> lista = new HashMap<String, Integer>();
					lista.putAll(ListaMeritos);
					lista.put(getMinKey(meritos, ListaMeritos), ListaAtributos.indexOf(minKey));
					nodoSig = siguienteRama(nodoSig, minKey, vuelta + 1, aristas, lista);
				}	
			}
		}
		return nodo;
	}
	
	private void agregarNodosSucesores(Map<String, Map<String, Integer>> map, String nombre,  
			int vuelta, Nodo nodo, String nombreNodo) {
		if (nodo.getListaNodosSig() != null) { 
			nodo.setNombre(nombreNodo);
			Map<String, Integer> temp = map.get(nombre);
			if (temp.size() == 1) {
				for (String key: temp.keySet()) {
					Nodo nodoSiguiente = new Nodo(vuelta, key);
					nodoSiguiente.setArista(nombre);
					nodoSiguiente.setNodoSiguiente(null);
					nodoSiguiente.setNodoAnterior(nodo);
					nodo.setNodoSiguiente(nodoSiguiente);
				}
			} else if (vuelta != ListaAtributos.size() - 2){
				Nodo nodoSiguiente = new Nodo(vuelta, null);
				nodoSiguiente.setArista(nombre);
				nodoSiguiente.setNodoAnterior(nodo);
				nodo.setNodoSiguiente(nodoSiguiente);
			} else {
				Nodo nodoSiguiente = new Nodo(vuelta, null);
				nodoSiguiente.setArista(nombre);
				nodoSiguiente.setNodoAnterior(nodo);
				nodo.setNombre("?");
			}
		}
	}
}
