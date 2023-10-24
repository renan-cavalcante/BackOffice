package model.entity;

public class CategoriaProduto {

	private Long idCategoria;
	private String nome;
	private String descricao;

	public CategoriaProduto(Long idCategoria, String nome, String descricao) {
		this.idCategoria = idCategoria;
		this.nome = nome;
		this.descricao = descricao;
	}

	public CategoriaProduto() {
		super();
	}
	
	public Long getId() {
		return idCategoria;
	}

	public void setId(Long idCategoria) {
		this.idCategoria = idCategoria;
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
		return  idCategoria + ";" + nome + ";" + descricao;
	}

	@Override
	public String toString() {
		return nome+"\n\t a\n\t a";
	}
	
	public String toString2() {
		return nome+"";
	}
	

}
