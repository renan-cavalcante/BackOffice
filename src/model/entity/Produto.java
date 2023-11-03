package model.entity;

import java.util.Objects;

public class Produto {
	private Long id;
	private String nome;
	private Double valor;
	private String descricao;
	private Integer quantidade;
    private CategoriaProduto categoria;
    private boolean ativo;
    
	public Produto() {
		super();
		ativo = true;
	}
	
	public Produto(Produto p) {
		id = p.getId();
		nome = p.getNome();
		valor = p.getValor();
		descricao = p.getDescricao();
		quantidade = 0;
		categoria = p.getCategoria();
		ativo = true;
	}

	public Produto(Long id, String nome, Double valor, String descricao, Integer quantidade, CategoriaProduto categoria, Boolean ativo) {
		super();
		this.id = id;
		this.nome = nome;
		this.valor = valor;
		this.descricao = descricao;
		this.quantidade = quantidade;
		this.categoria = categoria;
		this.ativo = ativo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}
	
	public void addQuantidade(Integer quantidade) {
		this.quantidade += quantidade;
	}
	
	public void subQuantidade(Integer quantidade) {
		this.quantidade -= quantidade;
	}

	public CategoriaProduto getCategoria() {
		return categoria;
	}

	public void setCategoria(CategoriaProduto categoria) {
		this.categoria = categoria;
	}

	
	public String toStringCsv() {
		return  id + ";" + nome + ";" + valor + ";" + descricao
				+ ";" + quantidade + ";" + categoria.getId()+";"+ativo;
	}

	@Override
	public String toString() {
		return nome +", " +id ;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Produto other = (Produto) obj;
		return Objects.equals(id, other.id);
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	} 
	
}
