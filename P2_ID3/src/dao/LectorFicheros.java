package dao;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LectorFicheros {
	
	public List<String> leerTipos(String archivo) throws FileNotFoundException, IOException {
	    List<String> resultado = new ArrayList<String>();
		String cadena;
	    FileReader f = new FileReader(archivo);
	    BufferedReader b = new BufferedReader(f);
	    while((cadena = b.readLine())!=null) {
	    	resultado = Arrays.asList(cadena.split(","));
	    }
	    b.close();
	      
	    return resultado;
	}
	
	public List<String> leerDatos(String archivo, ArrayList<String> tipos) throws FileNotFoundException, IOException {
	    List<String> resultado = new ArrayList<String>();
		String cadena;
	    FileReader f = new FileReader(archivo);
	    BufferedReader b = new BufferedReader(f);
	    while((cadena = b.readLine())!=null) {
	    	if (!cadena.equals(""))
	    		resultado.addAll(Arrays.asList(cadena.split(",")));
	    }
	    b.close();
	      
	    return resultado;
	}

}
