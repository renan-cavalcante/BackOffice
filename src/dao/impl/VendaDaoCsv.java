package dao.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import csvConnection.Pilha;
import dao.IArquivoCSV;
import db.DB;
import gui.util.Utils;
import model.entity.Produto;
import model.entity.Venda;

public class VendaDaoCsv implements IArquivoCSV<Venda> {
	private final String NOME = "venda";
	private VendaProdutoDaoCsv dao = new VendaProdutoDaoCsv();
	private ClienteDaoCSV daoCliente = new ClienteDaoCSV();
	private DB banco = new DB();

	@Override
	public void insert(Venda conteudo) throws Exception {

		conteudo.setId(banco.getSequencia("vendaSequenci"));
		relacionamentoVendaProdutoInsert(conteudo);
		banco.escrever(conteudo.toStringCsv(), NOME);
	}

	private void relacionamentoVendaProdutoInsert(Venda venda) throws Exception {
		Pilha<Produto> produtos = venda.getProdutos();
		while (!produtos.isEmpty()) {
			String[] conteudo = new String[3];
			conteudo[0] = venda.getId().toString();
			conteudo[1] = produtos.top().getId().toString();
			conteudo[2] = produtos.pop().getQuantidade().toString();
			dao.insert(conteudo);
		}

	}

	@Override
	public void delete(String id) throws IOException {
		Venda venda = findById(id);
		if (venda != null) {
			venda.setAtivo(false);
			update(venda);
		}
	}

	@Override
	public void update(Venda conteudo) throws IOException {
		List<Venda> list = findAllAtivo();

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

	public List<Venda> findAllAtivo() throws IOException {
		List<Venda> list = new ArrayList<>();
		List<String> vendas = banco.ler(NOME);

		for (String s : vendas) {
			String[] dados = s.split(";");
			Pilha<Produto> produtos = dao.findProdutosVenda(dados[0]);

			list.add(new Venda(Utils.tryParseLong(dados[0]), daoCliente.findByIdAll(dados[1]), produtos,
					Utils.tryParseDouble(dados[2]), Boolean.parseBoolean(dados[3])));

		}

		return list;
	}

	@Override
	public List<Venda> findAll() throws IOException {
		List<Venda> list = findAllAtivo();
		List<Venda> vendas = new ArrayList<>();

		for (Venda v : list) {

			if (v.isAtivo()) {
				vendas.add(v);
			}
		}
		return vendas;
	}

	@Override
	public Venda findById(String id) throws IOException {
		List<Venda> vendas = findAll();

		for (Venda v : vendas) {
			if (v.getId().toString().equals(id)) {
				return (v);
			}
		}
		return null;
	}

	@Override
	public List<Venda> findByName(String name) throws IOException {
		List<Venda> vendas = findAll();
		List<Venda> list = new ArrayList<>();

		for (Venda v : vendas) {
			if (v.getCliente().getNome().toLowerCase().contains(name)) {
				list.add(v);
			}
		}
		return list;
	}

}
