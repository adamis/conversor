package br.com.conversor;

public class Main {

	
	public static void main(String[] args) {
		Integer SIZE_PAGINA = 1000;
		for (int pagina = 0; pagina < 10; pagina++) {
			String sql = "";
			
			sql = sql + " LIMIT "+pagina*SIZE_PAGINA+", "+SIZE_PAGINA;
			
			System.err.println(""+sql);
		}
	}
	
}
