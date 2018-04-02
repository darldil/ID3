package logica.tiposdatos;

public enum Jugar {
	SI("si"), NO("no");
	
	private final String value;
	
    private Jugar(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
    
    public String getEstadoValue() {
    	Jugar estado = Jugar.NO; 
        return estado.getValue();
    }
}
