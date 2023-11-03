package dao.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dao.IClienteCsv;
import db.DB;
import model.entity.Cliente;
import model.entity.Endereco;

public class ClienteDaoCSV implements IClienteCsv {

	private final String NOME = "cliente";
	private DB banco = new DB();

	@Override
	public Cliente insert(Cliente conteudo) throws IOException {

		conteudo.getEndereco().setId(banco.getSequencia("enderecoSequenci"));
		banco.escrever(conteudo.toStringCSV(), NOME);
		return conteudo;
	}

	@Override
	public void delete(String id) throws IOException {
		Cliente cliente = findById(id);
		cliente.setAtivo(false);
		update(cliente);

	}

	@Override
	public Cliente update(Cliente conteudo) throws IOException {

		List<Cliente> list = findAll();

		int tamanho = list.size();
		for (int i = 0; i < tamanho; i++) {

			if (conteudo.getId().equals(list.get(i).getId())) {
				conteudo.getEndereco().setId(list.get(i).getEndereco().getId());
				list.set(i, conteudo);
			}

		}
		String[] dados = new String[list.size()];
		for (int i = 0; i < tamanho; i++) {
			dados[i] = list.get(i).toStringCSV();
		}
		banco.sobrescrever(dados, NOME);
		return conteudo;
	}

	@Override
	public List<Cliente> findAll() throws IOException {
		List<Cliente> list = new ArrayList<Cliente>();

		for (Cliente c : findAllInterno()) {
			if (c.isAtivo()) {
				list.add(c);
			}
		}
		return list;
	}

	protected List<Cliente> findAllInterno() throws IOException {
		List<Cliente> list = new ArrayList<>();

		for (String s : banco.ler(NOME)) {
			String[] dados = s.split(";");

			list.add(new Cliente(dados[0], dados[1],
					new Endereco(Long.parseLong(dados[2]), dados[3], dados[4], dados[5], dados[6]), dados[7], dados[8],
					Boolean.parseBoolean(dados[9])));
		}
		return list;
	}

	@Override
	public Cliente findById(String id) throws IOException {
		List<Cliente> list = findAllInterno();

		for (Cliente c : list) {
			if (c.isAtivo()) {
				return (c);
			}
		}
		return null;
	}

	protected Cliente findByIdAll(String id) throws IOException {
		List<Cliente> list = findAllInterno();
		for (Cliente c : list) {
			return (c);
		}
		throw new IOException("Cliente da venda não localizado");
	}

	@Override
	public List<Cliente> findByName(String name) throws IOException {
		List<Cliente> list = new ArrayList<>();
		List<Cliente> clientes = findAll();

		for (Cliente c : clientes) {
			if (c.getNome().toLowerCase().contains(name)) {
				list.add(c);
			}
		}
		return list;
	}

}
