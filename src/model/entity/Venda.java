package model.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import csvConnection.Pilha;

public class Venda {

	private Long id;
	private Cliente cliente;
	private Pilha<Produto> produtos;
	private Double valor;
	private LocalDateTime data;
	private boolean ativo;

	public Venda() {
		super();
		this.ativo = true;
	}

	public Venda(Carrinho c, LocalDateTime data) {
		cliente = c.getCliente();
		produtos = c.getProdutos();
		valor = c.calcularTotal();
		this.ativo = true;
		this.data = data;
	}

	public Venda(Long id, Cliente cliente, Pilha<Produto> produtos, Double valor, boolean ativo, LocalDateTime data) {
		super();
		this.id = id;
		this.cliente = cliente;
		this.produtos = produtos;
		this.valor = valor;
		this.ativo = ativo;
		this.data = data;
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

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Pilha<Produto> getProdutos() {
		return produtos;
	}

	public void setProdutos(Pilha<Produto> produtos) {
		this.produtos = produtos;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public LocalDateTime getData() {
		return data;
	}
	
	public String getDataFormat() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm");
		return data.format(formatter);
	}

	public void setData(LocalDateTime data) {
		this.data = data;
	}

	public String toStringCsv() {
		return id + ";" + cliente.getId() + ";" + valor + ";" + ativo+";"+data;
	}

}
