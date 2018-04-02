package logica.tiposdatos;

public enum EstadoCielo {
	VACIO (""), SOLEADO ("soleado"), NUBLADO ("nublado"), LLUVIOSO ("lluvioso");
	
	private final String value;
	
    private EstadoCielo(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
    
    public String getEstadoValue() {
    	EstadoCielo estado = EstadoCielo.VACIO; 
        return estado.getValue();
    }
}
