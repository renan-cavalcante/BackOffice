package csvConnection;

public class Pilha<T> {

	No<T> topo;

	public Pilha() {
		topo = null;
	}

	public Pilha(No<T> topo) {
		this.topo = topo;
	}

	public void push(T valor) {
		No<T> no = new No<>(valor, topo);
		topo = no;
	}

	public boolean isEmpty() {
		return topo == null;
	}

	public T pop() throws Exception {
		if (!isEmpty()) {
			T dado = topo.dado;
			topo = topo.no;
			return dado;
		}

		throw new Exception("Pilha vazia");
	}

	public int size() {
		int tamanho = 0;

		if (!isEmpty()) {
			No<T> aux = topo;
			while (aux != null) {
				aux = aux.no;
				tamanho++;
			}
		}

		return tamanho;
	}

	public T top() throws Exception {
		if (!isEmpty()) {
			T dado = topo.dado;
			return dado;
		}

		throw new Exception("Pilha vazia");
	}

	public Pilha<T> clonar() {
		return new Pilha<>(topo);
	}

}
