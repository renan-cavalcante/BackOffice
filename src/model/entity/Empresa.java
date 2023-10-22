package model.entity;

public class Empresa {

	private Endereco endereco;
	private String telefone;
	private String email;
	
	public Empresa() {
	}

	public Empresa(Endereco endereco, String telefone, String email) {
		super();
		this.endereco = endereco;
		this.telefone = telefone;
		this.email = email;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
