package logica.tiposdatos;

public enum Viento {
	VERDAD("verdad"), FALSO("falso");
	
	private final String value;
	
    private Viento(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
    
    public String getEstadoValue() {
    	Viento estado = Viento.FALSO; 
        return estado.getValue();
    }
}
