package model.entity;

import java.util.Stack;

import javax.naming.directory.InvalidAttributesException;

import lib.Pilha;

public class Carrinho {
	
	private Long id;
	private Cliente cliente;
	private Pilha<Produto> produtos;
	
	public Carrinho(Long id, Cliente cliente) {
		super();
		this.id = id;
		this.cliente = cliente;
		produtos = new Pilha<>();
	}
	
	public Carrinho() {
		produtos = new Pilha<>();
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) throws InvalidAttributesException {
		if(this.cliente != null) {
			throw new InvalidAttributesException("O carrinho já possui um cliente");
		}
		this.cliente = cliente;
	}

	public Pilha<Produto> getProdutos() {
		return produtos;
	}

	public void addProdutos(Produto produto) throws Exception {
		Pilha<Produto> aux = new Pilha<>();

		while(!produtos.isEmpty() ){
			aux.push(produtos.top());
			if(produtos.pop().equals(produto)) {
				produto.addQuantidade(aux.pop().getQuantidade());
			}
			
		}
		while(!aux.isEmpty()) {
			produtos.push(aux.pop());
		}
		produtos.push(produto);
	}
	
	public void removeProdutos(Produto produto) throws Exception {
		Stack<Produto> aux = new Stack<>();
		while(!produtos.isEmpty() && produtos.top()!=produto) {
			aux.push(produtos.pop());
		}
		if(!produtos.isEmpty()) {
			produtos.pop();
		}
		while(!aux.isEmpty()) {
			produtos.push(aux.pop());
		}
	}
	
	public Double calcularTotal()  {
		Pilha<Produto> aux = produtos.clonar();
		Double total = 0D;
		while(!aux.isEmpty()) {
			try {
				total += aux.top().getValor() * aux.pop().getQuantidade();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return total;
	}
	

}
