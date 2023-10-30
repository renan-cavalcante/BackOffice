package dao.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
	private ProdutoDaoCSV daoProduto = new ProdutoDaoCSV();

	@Override
	public void insert(Venda conteudo) throws Exception  {
		File arquivo = DB.getArquivo(NOME);
		FileWriter fileWriter = new FileWriter(arquivo, true);
		PrintWriter printWriter = new PrintWriter(fileWriter);
		conteudo.setId(DB.getSequencia("vendaSequenci"));
		System.out.println("insertVenda");
		relacionamentoVendaProdutoInsert(conteudo);
		printWriter.write(conteudo.toStringCsv() + "\r\n");
		printWriter.close();
		fileWriter.close();

	}

	private void relacionamentoVendaProdutoInsert(Venda venda) throws Exception {
		
		Pilha<Produto> produtos = venda.getProdutos();
		while(!produtos.isEmpty()) {
			String[] conteudo = new String[3];
			conteudo[0] = venda.getId().toString();
			conteudo[1] = produtos.top().getId().toString();
			conteudo[2] = produtos.pop().getQuantidade().toString();
			dao.insert(conteudo);
		}
		
	}

	@Override
	public void delete(String id) throws IOException {
		List<Venda> list = findAll();
		File arquivo = DB.getArquivo(NOME);
		FileWriter fileWriter = new FileWriter(arquivo, false);
		PrintWriter printWriter = new PrintWriter(fileWriter);
		for (Venda v : list) {
			if (!id.equals(v.getId().toString())) {
				printWriter.write(v.toStringCsv() + "\r\n");
			}else {
				dao.delete(id);
			}

		}
		printWriter.close();
		fileWriter.close();

	}

	@Override
	public void update(Venda conteudo) throws IOException {
		List<Venda> list = findAll();

		int tamanho = list.size();
		for (int i = 0; i < tamanho; i++) {

			if (conteudo.getId().equals(list.get(i).getId())) {
				conteudo.setId(list.get(i).getId());
				list.set(i, conteudo);
			}

		}
		File arquivo = DB.getArquivo(NOME);
		FileWriter fileWriter = new FileWriter(arquivo, false);
		PrintWriter printWriter = new PrintWriter(fileWriter);
		for (Venda v : list) {
			printWriter.write(v.toStringCsv() + "\r\n");
		}
		printWriter.close();
		fileWriter.close();

	}

	@Override
	public List<Venda> findAll() throws IOException {
		List<Venda> list = new ArrayList<>();
		File arquivo = DB.getArquivo(NOME);
		FileInputStream fluxo = new FileInputStream(arquivo);
		InputStreamReader leitor = new InputStreamReader(fluxo);
		BufferedReader buffer = new BufferedReader(leitor);
		String linha = buffer.readLine();

		while (linha != null) {
			String[] dados = linha.split(";");
			Pilha<Produto> produtos = new Pilha<>();
			
			List<String[]> produtosCsv = dao.findById(dados[0]);
			for(String[] s : produtosCsv) {
				Produto produto = daoProduto.findById(s[1]);
				produto.setQuantidade(Utils.tryParseInt(s[2]));
				produtos.push(produto);
			}
			list.add(new Venda(Utils.tryParseLong(dados[0]),daoCliente.findById(dados[1]),produtos,Utils.tryParseDouble(dados[2])));
			linha = buffer.readLine();
		}
		fluxo.close();
		leitor.close();
		buffer.close();
		return list;
	}

	@Override
	public Venda findById(String id) throws IOException {
		File arquivo = DB.getArquivo(NOME);
		FileInputStream fluxo = new FileInputStream(arquivo);
		InputStreamReader leitor = new InputStreamReader(fluxo);
		BufferedReader buffer = new BufferedReader(leitor);
		String linha = buffer.readLine();

		while (linha != null) {
			String[] dados = linha.split(";");
			if (id.trim().equals(dados[0])) {
				fluxo.close();
				leitor.close();
				buffer.close();
				Pilha<Produto> produtos = new Pilha<>();
				
				List<String[]> produtosCsv = dao.findById(dados[0]);
				for(String[] s : produtosCsv) {
					Produto produto = daoProduto.findById(s[1]);
					produto.setQuantidade(Utils.tryParseInt(s[2]));
					produtos.push(produto);
				}
				return(new Venda(Utils.tryParseLong(dados[0]),daoCliente.findById(dados[1]),produtos,Utils.tryParseDouble(dados[2])));
		
			}
			linha = buffer.readLine();
		}
		fluxo.close();
		leitor.close();
		buffer.close();
		return null;
	}

	@Override
	public List<Venda> findByName(String name) throws IOException {
		List<Venda> list = new ArrayList<>();
		File arquivo = DB.getArquivo(NOME);
		FileInputStream fluxo = new FileInputStream(arquivo);
		InputStreamReader leitor = new InputStreamReader(fluxo);
		BufferedReader buffer = new BufferedReader(leitor);
		String linha = buffer.readLine();

		while (linha != null) {
			String[] dados = linha.split(";");
			if (dados[1].trim().toLowerCase().contains(name)) {
				Pilha<Produto> produtos = new Pilha<>();
				
				List<String[]> produtosCsv = dao.findById(dados[0]);
				for(String[] s : produtosCsv) {
					Produto produto = daoProduto.findById(s[1]);
					produto.setQuantidade(Utils.tryParseInt(s[2]));
					produtos.push(produto);
				}
				list.add(new Venda(Utils.tryParseLong(dados[0]),daoCliente.findById(dados[1]),produtos,Utils.tryParseDouble(dados[2])));
			}
			linha = buffer.readLine();
		}
		fluxo.close();
		leitor.close();
		buffer.close();
		return list;
	}

}
