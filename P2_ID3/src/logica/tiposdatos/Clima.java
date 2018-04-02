package logica.tiposdatos;

public enum Clima {
	VACIO (""), CALUROSO ("caluroso"), TEMPLADO ("templado"), FRIO ("frio");
	
	private final String value;
	
    private Clima(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
    
    public String getEstadoValue() {
    	Clima estado = Clima.VACIO; 
        return estado.getValue();
    }
}
