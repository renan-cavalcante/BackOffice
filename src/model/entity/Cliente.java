package model.entity;

public class Cliente extends Usuario{

	private Endereco endereco;
	private String celular;
	
	public Cliente() {
	}

	public Cliente(Long id, String nome, Endereco endereco, String celular) {
		super(id, nome);
		this.endereco = endereco;
		this.celular = celular;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}
	
	
	
	

}
