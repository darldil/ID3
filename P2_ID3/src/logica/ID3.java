package logica;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dao.LectorFicheros;

public class ID3 {

	private LectorFicheros dao;
	private ArrayList<String> ListaAtributos;
	private ArrayList<ArrayList<String>> ListaEjemplos;
	private Map<String, ArrayList<Map>> resultado;
	
	public ID3() {
		dao = new LectorFicheros();
		ListaAtributos = new ArrayList<String>();
		ListaEjemplos = new ArrayList<ArrayList<String>>();
		resultado = new HashMap<String, ArrayList<Map>>();
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
		for (int i = 0; i < this.ListaAtributos.size(); i++) {
			//Map<String, Float> temp = new HashMap<String, Float>();
			meritos.put(ListaAtributos.get(i), calcularMerito(i));
		}
		
		siguienteRama(getMinKey(meritos));
		
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
	    for(String key: ListaAtributos) {
	        float value = map.get(key);
	        if(value < minValue) {
	            minValue = value;
	            minKey = key;
	        }
	    }
	    return minKey;
	}
	
	private void siguienteRama(String ramaActual) {
		
	}
	
}
