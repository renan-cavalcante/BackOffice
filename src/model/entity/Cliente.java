package model.entity;

import java.util.Objects;

public class Cliente {

	private String id;
	private String nome;
	private Endereco endereco;
	private String contato;
	private String email;
	private boolean ativo = true;

	public Cliente() {
		ativo = true;
	}

	public Cliente(String id, String nome, Endereco endereco, String contato) {
		this.id = id;
		this.nome = nome;
		this.endereco = endereco;
		this.contato = contato;
	}

	public Cliente(String id, String nome, Endereco endereco, String contato, String email) {
		this.id = id;
		this.nome = nome;
		this.endereco = endereco;
		this.contato = contato;
		this.email = email;
	}

	public Cliente(String id, String nome, Endereco endereco, String contato, String email, boolean ativo) {
		this.id = id;
		this.nome = nome;
		this.endereco = endereco;
		this.contato = contato;
		this.email = email;
		this.ativo = ativo;
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
		return email = email == null ? " " : email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cliente other = (Cliente) obj;
		return Objects.equals(id, other.id);
	}

	public String toStringCSV() {
		return getId() + ";" + getNome() + ";" + endereco.toStringCSV() + ";" + contato + ";" + getEmail() + ";"
				+ ativo;
	}

	@Override
	public String toString() {
		return getNome() + ", " + getId();
	}


}
