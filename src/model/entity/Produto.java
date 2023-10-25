package model.entity;

public class Produto {
	private Long id;
	private String nome;
	private Double valor;
	private String descricao;
	private Integer quantidade;
    private CategoriaProduto categoria;
    
	public Produto() {
		super();
	}

	public Produto(Long id, String nome, Double valor, String descricao, Integer quantidade, CategoriaProduto categoria) {
		super();
		this.id = id;
		this.nome = nome;
		this.valor = valor;
		this.descricao = descricao;
		this.quantidade = quantidade;
		this.categoria = categoria;
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

	public CategoriaProduto getCategoria() {
		return categoria;
	}

	public void setCategoria(CategoriaProduto categoria) {
		this.categoria = categoria;
	}

	
	public String toStringCsv() {
		return  id + ";" + nome + ";" + valor + ";" + descricao
				+ ";" + quantidade + ";" + categoria.getId();
	}
    
	
    
}
