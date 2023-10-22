package model.entity;

public class Endereco {

	private Long id;
	private String lougradouro;
	private String numero;
	private String complemento;
	private String cep;
	
	public Endereco() {
	}

	public Endereco(Long id, String lougradouro, String numero, String complemento, String cep) {
		super();
		this.id = id;
		this.lougradouro = lougradouro;
		this.numero = numero;
		this.complemento = complemento;
		this.cep = cep;
	}



	public Endereco(String lougradouro, String numero, String complemento, String cep) {
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
		if(complemento != null) {
			return complemento;
		}
		return "";
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
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return lougradouro + ", Nº" + numero + " " + getComplemento()+" - "+ cep;
	}
	
	public String toStringCSV() {
		return id+";"+lougradouro + ";" + numero + ";" +getComplemento()+";"+ cep;
	}
}
