package model.entity;

public class Cliente extends Usuario{

	private Endereco endereco;
	private String celular;
	
	public Cliente() {
	}

	public Cliente(Endereco endereco, String celular) {
		super();
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
