package logica;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import dao.LectorFicheros;

public class ID3 {

	private LectorFicheros dao;
	private ArrayList<String> ListaAtributos;
	private ArrayList<ArrayList<String>> ListaEjemplos;
	private ArrayList<Nodo> resultado;
	
	public ID3() {
		dao = new LectorFicheros();
		ListaAtributos = new ArrayList<String>();
		ListaEjemplos = new ArrayList<ArrayList<String>>();
		resultado = new ArrayList<Nodo>();
	}
	
	public void cargarTipos(TransferArchivos transfer) throws Exception {
		List<String> list = dao.leerTipos(transfer.getRuta() + transfer.getNombre());
		ListaAtributos.addAll(list);
		System.out.println(ListaAtributos);
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
		System.out.println(ListaEjemplos);
	}
	
	public void procesar() {
		Map<String, Float> meritos = new HashMap<String, Float>(); 
		int vuelta = 0;
		
		while (!ListaAtributos.isEmpty()) {
			for (int i = 0; i < this.ListaAtributos.size() - 1; i++) {
				meritos.put(ListaAtributos.get(i), calcularMerito(i));
			}
			
			siguienteRama(getMinKey(meritos), vuelta);
			eliminarColumna(getMinKey(meritos));
			vuelta++;
		}
		
		/*resultado.put(getMinKey(meritos), Collections.min(meritos.values()));
		Float temp = Collections.min(meritos.values());*/
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
			Map<String, Float> temp = new HashMap<String, Float>();
			temp.put("si", (float) 0);
			temp.put("no", (float) 0);
			for (int i = 0; i < totalMeritos; i++) {
				if (tipo.get(n).equals(ListaEjemplos.get(i).get(col))) {
					float aux = temp.get(ListaEjemplos.get(i).get((ListaEjemplos.get(i).size())-1));
					temp.put(ListaEjemplos.get(i).get(ListaEjemplos.get(i).size()-1), aux + 1);
				}
			}
			float pi = (temp.get("si")/mapaMeritos.get(tipo.get(n)));
			float ni = (temp.get("no")/mapaMeritos.get(tipo.get(n)));
			float log1 = (float) (Math.log(pi)/(Math.log(2)));
			float log2 = (float) (Math.log(ni)/(Math.log(2)));
			Float merito = (mapaMeritos.get(tipo.get(n))/totalMeritos) * ((-pi * log1) + (-ni * log2)); 
			if (merito.equals(Float.NaN))
				merito = (float) 0;
			resultadosInfor.add(merito);
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
		Nodo nodo = new Nodo(vuelta, ramaActual);
		nodo.setNodoAnterior(vuelta - 1);
		ArrayList<String> tipo = new ArrayList<String>();
		Map<String, Map<String, Integer>> map = new HashMap<String, Map<String, Integer>>();
		int total = ListaEjemplos.size();
		int col = ListaAtributos.indexOf(ramaActual);
		for (int i = 0; i < total; i++) {
			if (!tipo.contains(ListaEjemplos.get(i).get(col))) {
				tipo.add(ListaEjemplos.get(i).get(col));
				map.put(ListaEjemplos.get(i).get(col), new HashMap<String, Integer>());
			}
		}
		for (int i = 0; i < tipo.size(); i++) {
			for (int n = 0; n < ListaEjemplos.size(); n++) {
				if (ListaEjemplos.get(n).get(col).equals(tipo.get(i))) {
					String temp = ListaEjemplos.get(n).get(ListaAtributos.size()-1);
					if (!map.get(tipo.get(i)).containsKey(temp)) {
						map.get(tipo.get(i)).put(temp, 1);
					} else {
						Map <String, Integer> aux = map.get(tipo.get(i));
						aux.put(temp, aux.get(temp) +1);
					}
				}
			}
		}
		for (String nombre: tipo) {
			Map<String, Integer> temp = map.get(nombre);
			if (temp.size() == 1) {
				for (String key: temp.keySet())
					nodo.setCondicion(nombre, key);
			} else
				nodo.setCondicion(nombre, "null");
		}
		resultado.add(nodo);
	}
	
	private void eliminarColumna(String ramaActual) {
		int col = ListaAtributos.indexOf(ramaActual);
		
		ListaAtributos.remove(col);
		
		if (ListaAtributos.size() == 1) {
			ListaAtributos.clear();
			ListaEjemplos.clear();
		}
	
		else {
			for (ArrayList<String> temp: ListaEjemplos) {
				temp.remove(col);
			}
		}
		
	}
	
}
