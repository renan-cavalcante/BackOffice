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

import dao.IArquivoCSV;
import db.DB;
import gui.util.Utils;
import model.entity.Produto;
import model.services.CategoriaProdutoService;

public class ProdutoDaoCSV implements IArquivoCSV<Produto>{
	
	private CategoriaProdutoService serviceProduto = new CategoriaProdutoService();
	
	private final String NOME = "produto";

	@Override
	public void insert(Produto conteudo) throws IOException {
		File arquivo = DB.getArquivo(NOME);
		FileWriter fileWriter = new FileWriter(arquivo, true);
		PrintWriter printWriter = new PrintWriter(fileWriter);
		conteudo.setId(DB. getSequencia("produtoSequenci"));
		printWriter.write(conteudo.toStringCsv()+"\r\n");
		printWriter.close();
		fileWriter.close();
	}

	@Override
	public void delete(String id) throws IOException {
		List<Produto> list = findAll();
		File arquivo = DB.getArquivo(NOME);
		FileWriter fileWriter = new FileWriter(arquivo, false);
		PrintWriter printWriter = new PrintWriter(fileWriter);
		for(Produto c : list) {
			if(Utils.tryParseLong(id) == c.getId()) {
				printWriter.write(c.toStringCsv()+"\r\n");
			}
			
		}
		printWriter.close();
		fileWriter.close();
		
	}

	@Override
	public void update(Produto conteudo) throws IOException {
		
		List<Produto> list = findAll();
		
		int tamanho = list.size();
		for(int i = 0; i < tamanho; i++){
			
			
			if(conteudo.getId().equals(list.get(i).getId())) {
				conteudo.setId(list.get(i).getId());
				list.set(i, conteudo);
			}
	
		}
		File arquivo = DB.getArquivo(NOME);
		FileWriter fileWriter = new FileWriter(arquivo, false);
		PrintWriter printWriter = new PrintWriter(fileWriter);
		for(Produto c : list) {
			printWriter.write(c.toStringCsv()+"\r\n");
		}
		printWriter.close();
		fileWriter.close();
	}

	@Override
	public List<Produto> findAll() throws IOException {
		List<Produto> list = new ArrayList<>();
		File arquivo = DB.getArquivo(NOME);
		FileInputStream fluxo = new FileInputStream(arquivo);
		InputStreamReader leitor = new InputStreamReader(fluxo);
		BufferedReader buffer = new BufferedReader(leitor);
		String linha = buffer.readLine();
		
		while(linha!=null){
			String[] dados = linha.split(";");
			list.add(new Produto(Utils.tryParseLong(dados[0]), dados[1], Utils.tryParseDouble(dados[2]),dados[3], Utils.tryParseInt(dados[4]),serviceProduto.findById(dados[5])));
			linha = buffer.readLine();
		}
		fluxo.close();
		leitor.close();
		buffer.close();
		return list;
	}

	@Override
	public Produto findById(String id) throws IOException {
		File arquivo = DB.getArquivo(NOME);
		FileInputStream fluxo = new FileInputStream(arquivo);
		InputStreamReader leitor = new InputStreamReader(fluxo);
		BufferedReader buffer = new BufferedReader(leitor);
		String linha = buffer.readLine();
		
		while(linha!=null){
			String[] dados = linha.split(";");
			if(id.trim().equals(dados[0])) {
				fluxo.close();
				leitor.close();
				buffer.close();
				return (new Produto(Utils.tryParseLong(dados[0]), dados[1], Utils.tryParseDouble(dados[2]),dados[3], Utils.tryParseInt(dados[4]),serviceProduto.findById(dados[5])));
			}
			linha = buffer.readLine();
		}
		fluxo.close();
		leitor.close();
		buffer.close();
		return null;
	}

	@Override
	public List<Produto> findByName(String name) throws IOException {
		List<Produto> list = new ArrayList<>();
		File arquivo = DB.getArquivo(NOME);
		FileInputStream fluxo = new FileInputStream(arquivo);
		InputStreamReader leitor = new InputStreamReader(fluxo);
		BufferedReader buffer = new BufferedReader(leitor);
		String linha = buffer.readLine();
		
		while(linha!=null){
			String[] dados = linha.split(";");
			if(name.trim().toLowerCase().contains(dados[1])) {
				fluxo.close();
				leitor.close();
				buffer.close();
				list.add(new Produto(Utils.tryParseLong(dados[0]), dados[1], Utils.tryParseDouble(dados[2]),dados[3], Utils.tryParseInt(dados[4]),serviceProduto.findById(dados[5])));
			}
			linha = buffer.readLine();
		}
		System.out.println("nada");
		fluxo.close();
		leitor.close();
		buffer.close();
		return list;
	}

	
}
