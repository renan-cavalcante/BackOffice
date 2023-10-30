package csvConnection;

public class No<T> {
	T dado;
	No<T> no;
	
	public No(T valor, No<T> no) {
		this.no = no;
		dado = valor;
	}
}
