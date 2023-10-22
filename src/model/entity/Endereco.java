package model.entity;

public class Endereco {

	private String lougradouro;
	private String numero;
	private String complemento;
	private String cep;
	
	public Endereco() {
	}
	
	

	public Endereco(String lougradouro, String numero, String complemento, String cep) {
		super();
		this.lougradouro = lougradouro;
		this.numero = numero;
		this.complemento = complemento;
		this.cep = cep;
	}



	public String getLougradouro() {
		return lougradouro;
	}

	public void setLougradouro(String lougradouro) {
		this.lougradouro = lougradouro;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}
	
	

}
