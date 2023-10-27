package model.entity;

import java.util.Stack;

import javax.naming.directory.InvalidAttributesException;

public class Carrinho {
	
	private Long id;
	private Cliente cliente;
	private Stack<Produto> produtos = new Stack<>();
	
	public Carrinho(Long id, Cliente cliente) {
		super();
		this.id = id;
		this.cliente = cliente;
	}
	
	public Carrinho() {

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

	public Stack<Produto> getProdutos() {
		return produtos;
	}

	public void addProdutos(Produto produto) {
		this.produtos.push(produto);
	}
	
	public void removeProdutos(Produto produto) {
		Stack<Produto> aux = new Stack<>();
		while(!produtos.isEmpty() && produtos.peek()!=produto) {
			aux.push(produtos.pop());
		}
		if(!produtos.isEmpty()) {
			produtos.pop();
		}
		while(!aux.isEmpty()) {
			produtos.push(aux.pop());
		}
	}
	
	

}
