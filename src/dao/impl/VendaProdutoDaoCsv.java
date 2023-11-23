package dao.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lib.Pilha;
import db.DB;
import gui.util.Utils;
import model.entity.Produto;

public class VendaProdutoDaoCsv {
	private final String NOME = "venda_produto";
	private ProdutoDaoCSV produtoDao = new ProdutoDaoCSV();
	private DB banco = new DB();

	protected void insert(String[] conteudo) throws IOException {
		banco.escrever(conteudo[0] + ";" + conteudo[1] + ";" + conteudo[2], NOME);
	}

	protected List<String[]> findAll() throws IOException {
		List<String[]> list = new ArrayList<>();

		for (String s : banco.ler(NOME)) {
			String[] dados = s.split(";");
			list.add(dados);
		}
		return list;
	}

	public Pilha<Produto> findProdutosVenda(String idVenda) throws IOException {
		List<String[]> vendaProduto = findById(idVenda);
		Pilha<Produto> produtos = new Pilha<>();

		for (Produto p : produtoDao.findAllInterno()) {
			for (String[] s : vendaProduto) {
				if (p.getId().toString().equals(s[1])) {
					Produto produtoVenda = new Produto(p);
					produtoVenda.setQuantidade(Utils.tryParseInt(s[2]));
					produtos.push(produtoVenda);
				}
			}

		}
		return produtos;
	}

	protected List<String[]> findById(String id) throws IOException {
		List<String[]> list = new ArrayList<>();

		for (String[] s : findAll()) {
			if (s[0].equals(id)) {
				list.add(s);
			}
		}
		return list;
	}
}
