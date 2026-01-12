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
}