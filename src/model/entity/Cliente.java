package model.entity;

public class Cliente extends Usuario{

	private Endereco endereco;
	private String contato;
	private String email;
	
	public Cliente() {
	}

	public Cliente(Long id, String nome, Endereco endereco, String contato) {
		super(id, nome);
		this.endereco = endereco;
		this.contato = contato;
	}
	
	public Cliente(Long id, String nome, Endereco endereco, String contato, String email) {
		super(id, nome);
		this.endereco = endereco;
		this.contato = contato;
		this.email = email;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public String getContato() {
		return contato;
	}

	public void setContato(String contato) {
		this.contato = contato;
	}
	

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String toStringCSV() {
		return getId()+";"+getNome()+";"+endereco.toStringCSV()+";"+contato+";"+email;
	}
	
	

}
