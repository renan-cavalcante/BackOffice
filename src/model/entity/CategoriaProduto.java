package model.entity;

import java.util.ArrayList;
import java.util.List;

public class CategoriaProduto {

	private Long id;
	private String nome;
	private String descricao;
	private List<Produto> produtos = new ArrayList<>();

	public CategoriaProduto(Long id, String nome, String descricao) {
		this.id = id;
		this.nome = nome;
		this.descricao = descricao;
	}
	

	public CategoriaProduto(Long id, String nome, String descricao, List<Produto> produtos) {
		super();
		this.id = id;
		this.nome = nome;
		this.descricao = descricao;
		this.produtos = produtos;
	}



	public CategoriaProduto() {
		super();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long idCategoria) {
		this.id = idCategoria;
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
		return  id + ";" + nome + ";" + descricao;
	}

	@Override
	public String toString() {
		return nome;
	}
	
	public String toString2() {
		return nome+"";
	}

	public List<Produto> getProdutos() {
		return produtos;
	}
	
	public String getProdutosToString() {
		StringBuffer sb = new StringBuffer();
		for(Produto p: getProdutos()) {
			sb.append("   "+p.getNome());
			sb.append("\n");
		}
		return sb.toString();
	}

	public void setProdutos(List<Produto> produtos) {
		this.produtos = produtos;
	}
	
	public void addProdutos(Produto produto) {
		produtos.add(produto);
	}
	
	public void removeProdutos(Produto produto) {
		produtos.remove(produto);
	}
}
