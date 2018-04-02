package logica.tiposdatos;

public enum Humedad {
	VACIO(""), ALTA ("alta"), NORMAL("normal"), BAJA("baja");
	
	private final String value;
	
    private Humedad(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
    
    public String getEstadoValue() {
    	Humedad estado = Humedad.VACIO; 
        return estado.getValue();
    }
}
