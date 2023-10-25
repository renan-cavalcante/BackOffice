package model.entity;

public class CategoriaProduto {

	private Long id;
	private String nome;
	private String descricao;

	public CategoriaProduto(Long id, String nome, String descricao) {
		this.id = id;
		this.nome = nome;
		this.descricao = descricao;
	}

	public CategoriaProduto() {
		super();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long idCategoria) {
		this.id = idCategoria;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public String toStringCsv() {
		return  id + ";" + nome + ";" + descricao;
	}

	@Override
	public String toString() {
		return nome;
	}
	
	public String toString2() {
		return nome+"";
	}
	

}
