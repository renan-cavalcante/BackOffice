package model.entity;

public abstract class Usuario {

	private String id;
	private String nome;
	
	public Usuario() {
	}
		
	public Usuario(String id, String nome) {
		super();
		this.id = id;
		this.nome = nome;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	

}
