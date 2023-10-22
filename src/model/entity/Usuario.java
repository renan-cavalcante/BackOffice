package model.entity;

public abstract class Usuario {

	public Usuario() {
	}
	
	private Long id;
	private Long nome;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getNome() {
		return nome;
	}
	public void setNome(Long nome) {
		this.nome = nome;
	}
	
	

}
