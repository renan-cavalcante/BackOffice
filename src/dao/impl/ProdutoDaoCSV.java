package dao.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dao.IArquivoCSV;
import db.DB;
import gui.util.Utils;
import model.entity.Produto;

public class ProdutoDaoCSV implements IArquivoCSV<Produto> {

	private CategoriaProdutoDaoCSV daoProduto;
	private DB banco = new DB();

	private final String NOME = "produto";

	@Override
	public void insert(Produto conteudo) throws IOException {
		conteudo.setId(banco.getSequencia("produtoSequenci"));
		banco.escrever(conteudo.toStringCsv(), NOME);
	}

	@Override
	public void delete(String id) throws IOException {
		Produto p = findById(id);
		p.setAtivo(false);
		update(p);

	}

	@Override
	public void update(Produto conteudo) throws IOException {
		List<Produto> list = findAllInterno();

		int tamanho = list.size();
		for (int i = 0; i < tamanho; i++) {
			if (conteudo.getId().equals(list.get(i).getId())) {
				conteudo.setId(list.get(i).getId());
				list.set(i, conteudo);
			}

		}
		String[] dados = new String[list.size()];
		for (int i = 0; i < tamanho; i++) {
			dados[i] = list.get(i).toStringCsv();
		}
		banco.sobrescrever(dados, NOME);
	}

	public List<Produto> findAllInterno() throws IOException {
		List<Produto> list = new ArrayList<>();
		List<String> produtos = banco.ler(NOME);

		for (String linha : produtos) {
			String[] dados = linha.split(";");
			if (daoProduto == null) {
				setCategoriaProdutoDaoCSV();
			}
			list.add(new Produto(Utils.tryParseLong(dados[0]), dados[1], Utils.tryParseDouble(dados[2]), dados[3],
					Utils.tryParseInt(dados[4]), daoProduto.findByIdForProduto(dados[5]), Boolean.parseBoolean(dados[6])));

		}

		return list;
	}

	@Override
	public List<Produto> findAll() throws IOException {
		List<Produto> list = findAllInterno();
		List<Produto> produtos = new ArrayList<>();

		for (Produto p : list) {
			if (p.isAtivo()) {
				produtos.add(p);
			}
		}
		return produtos;
	}

	@Override
	public Produto findById(String id) throws IOException {
		List<Produto> produtos = findAllInterno();

		for (Produto p : produtos) {
			if (id.trim().equals(p.getId().toString())) {
				if (daoProduto == null) {
					setCategoriaProdutoDaoCSV();
				}
				return (new Produto(p));
			}
		}
		return null;
	}

	@Override
	public List<Produto> findByName(String name) throws IOException {
		List<Produto> list = new ArrayList<>();
		List<Produto> produtos = findAllInterno();

		for (Produto p : produtos) {
			if (p.getNome().toLowerCase().contains(name)) {
				list.add(p);
			}
		}
		return list;
	}

	private void setCategoriaProdutoDaoCSV() {
		daoProduto = new CategoriaProdutoDaoCSV();
	}

}
