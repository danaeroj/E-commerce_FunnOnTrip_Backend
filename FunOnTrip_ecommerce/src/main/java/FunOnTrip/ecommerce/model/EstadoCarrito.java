package FunOnTrip.ecommerce.model;

public enum EstadoCarrito {
    ACTIVO("activo"),
    ABANDONADO("abandonado"),
    CONVERTIDO("convertido");
    
    private final String valor;
    
    EstadoCarrito(String valor) {
        this.valor = valor;
    }
    
    public String getValor() {
        return valor;
    }
    
    // MÉTODO PARA CONVERTIR DE STRING A ENUM
    public static EstadoCarrito fromString(String text) {
        for (EstadoCarrito estado : EstadoCarrito.values()) {
            if (estado.valor.equalsIgnoreCase(text)) {
                return estado;
            }
        }
        throw new IllegalArgumentException("Estado no válido: " + text + 
            ". Valores válidos: activo, abandonado, convertido");
    }
    
    // MÉTODO PARA OBTENER EL VALOR ENUM DESDE SU NOMBRE
    public static EstadoCarrito fromName(String name) {
        try {
            return EstadoCarrito.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Estado no válido: " + name);
        }
    }
}