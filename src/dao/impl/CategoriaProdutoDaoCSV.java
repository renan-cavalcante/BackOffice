package dao.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dao.IArquivoCSV;
import db.DB;
import gui.util.Utils;
import model.entity.CategoriaProduto;
import model.entity.Produto;

public class CategoriaProdutoDaoCSV implements IArquivoCSV<CategoriaProduto> {

	private ProdutoDaoCSV daoProduto;
	private final String NOME = "categoria_produto";
	private DB banco = new DB();

	@Override
	public void insert(CategoriaProduto conteudo) throws IOException {

		conteudo.setId(banco.getSequencia("categoriaProdutosSequenci"));
		banco.escrever(conteudo.toStringCsv(), NOME);
	}

	@Override
	public void delete(String id) throws IOException, IllegalArgumentException {
		CategoriaProduto categoria = findById(id);
		boolean excluir = true;

		for (Produto p : categoria.getProdutos()) {

			if (p.isAtivo()) {
				excluir = false;
				System.out.println("ativo");
				throw new IllegalArgumentException(
						"A categoria não pode ser excluida, pois possui produtos relacionadas a ela");
			} else {
				excluir = true;
			}

		}

		if (excluir) {
			categoria.setAtivo(false);
			update(categoria);
		}

	}

	@Override
	public void update(CategoriaProduto conteudo) throws IOException {

		List<CategoriaProduto> list = findAll();

		int tamanho = list.size();
		for (int i = 0; i < tamanho; i++) {

			if (conteudo.getId() == (list.get(i).getId())) {
				list.set(i, conteudo);
			}

		}
		String[] dados = new String[list.size()];
		for (int i = 0; i < tamanho; i++) {
			dados[i] = list.get(i).toStringCsv();
		}
		banco.sobrescrever(dados, NOME);
	}

//	Utils.classeChamadora() != ProdutoDaoCSV.class.getName()
	protected List<CategoriaProduto> findAllInterno() throws IOException {
		List<CategoriaProduto> list = new ArrayList<>();
		List<String> categorias = banco.ler(NOME);

		for (String linha : categorias) {
			String[] dados = linha.split(";");

			list.add(new CategoriaProduto(Long.parseLong(dados[0]), dados[1], dados[2],
					relacionamentoCategoriaProduto(dados[0]), Boolean.parseBoolean(dados[3])));

		}

		return list;
	}

	// Utils.classeChamadora() == ProdutoDaoCSV.class.getName()
	protected List<CategoriaProduto> findAllInternoRelacionamento() throws IOException {
		List<CategoriaProduto> list = new ArrayList<>();
		List<String> categorias = banco.ler(NOME);

		for (String linha : categorias) {
			String[] dados = linha.split(";");

			list.add(
					new CategoriaProduto(Long.parseLong(dados[0]), dados[1], dados[2], Boolean.parseBoolean(dados[3])));

		}

		return list;
	}

	@Override
	public List<CategoriaProduto> findAll() throws IOException {
		List<CategoriaProduto> categoria = new ArrayList<>();
		List<CategoriaProduto> list = Utils.classeChamadora() != ProdutoDaoCSV.class.getName() ? findAllInterno()
				: findAllInternoRelacionamento();

		for (CategoriaProduto c : list) {
			if (c.isAtivo()) {
				categoria.add(c);
			}
		}
		return categoria;
	}

	private List<Produto> relacionamentoCategoriaProduto(String id) throws IOException {
		List<Produto> produtos = new ArrayList<>();

		setDaoService();
		for (Produto p : daoProduto.findAll()) {
			if (p.getCategoria().getId().toString().equals(id)) {
				produtos.add(p);
			}
		}
		return produtos;
	}

	@Override
	public CategoriaProduto findById(String id) throws IOException {
		List<CategoriaProduto> list = findAll();

		for (CategoriaProduto c : list) {
			if (id.trim().equals(c.getId().toString())) {
				return (c);
			}
		}
		return null;

	}

	@Override
	public List<CategoriaProduto> findByName(String name) throws IOException {
		List<CategoriaProduto> categoria = new ArrayList<>();
		List<CategoriaProduto> list = findAll();

		for (CategoriaProduto c : list) {
			if (c.getNome().toLowerCase().contains(name)) {

				categoria.add(c);

			}
		}
		return categoria;

	}

	public void setDaoService() {
		daoProduto = new ProdutoDaoCSV();
	}

	public CategoriaProduto findByIdForProduto(String id) throws IOException {
		List<CategoriaProduto> list = findAllInternoRelacionamento();

		for (CategoriaProduto c : list) {
			if (id.trim().equals(c.getId().toString()) && c.isAtivo()) {
				return (c);
			}
		}
		return null;
	}

}
