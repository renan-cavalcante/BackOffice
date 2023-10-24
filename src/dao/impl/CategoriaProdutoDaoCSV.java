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
import model.entity.CategoriaProduto;

public class CategoriaProdutoDaoCSV implements IArquivoCSV<CategoriaProduto>{
	
	private final String NOME = "categoria_produto";

	@Override
	public void insert(CategoriaProduto conteudo) throws IOException {
		File arquivo = DB.getArquivo(NOME);
		FileWriter fileWriter = new FileWriter(arquivo, true);
		PrintWriter printWriter = new PrintWriter(fileWriter);
		conteudo.setId(DB. getSequencia("categoriaProdutosSequenci"));
		printWriter.write(conteudo.toStringCsv()+"\r\n");
		printWriter.close();
		fileWriter.close();
	}

	@Override
	public void delete(String id) throws IOException {
		List<CategoriaProduto> list = findAll();
		File arquivo = DB.getArquivo(NOME);
		FileWriter fileWriter = new FileWriter(arquivo, false);
		PrintWriter printWriter = new PrintWriter(fileWriter);
		for(CategoriaProduto c : list) {
			if(Utils.tryParseLong(id) != (c.getId())) {
				printWriter.write(c.toStringCsv()+"\r\n");
			}
			
		}
		printWriter.close();
		fileWriter.close();
		
	}

	@Override
	public void update(CategoriaProduto conteudo) throws IOException {
		
		List<CategoriaProduto> list = findAll();
		
		int tamanho = list.size();
		for(int i = 0; i < tamanho; i++){
			
			
			if(conteudo.getId() == (list.get(i).getId())) {
				list.set(i, conteudo);
			}
	
		}
		File arquivo = DB.getArquivo(NOME);
		FileWriter fileWriter = new FileWriter(arquivo, false);
		PrintWriter printWriter = new PrintWriter(fileWriter);
		for(CategoriaProduto c : list) {
			printWriter.write(c.toStringCsv()+"\r\n");
		}
		printWriter.close();
		fileWriter.close();
	}

	@Override
	public List<CategoriaProduto> findAll() throws IOException {
		List<CategoriaProduto> list = new ArrayList<>();
		File arquivo = DB.getArquivo(NOME);
		FileInputStream fluxo = new FileInputStream(arquivo);
		InputStreamReader leitor = new InputStreamReader(fluxo);
		BufferedReader buffer = new BufferedReader(leitor);
		String linha = buffer.readLine();
		
		while(linha!=null){
			String[] dados = linha.split(";");
			list.add(new CategoriaProduto(Long.parseLong(dados[0]),dados[1],dados[2]));
			linha = buffer.readLine();
		}
		fluxo.close();
		leitor.close();
		buffer.close();
		return list;
	}

	@Override
	public CategoriaProduto findById(String id) throws IOException {
		
		File arquivo = DB.getArquivo(NOME);
		FileInputStream fluxo = new FileInputStream(arquivo);
		InputStreamReader leitor = new InputStreamReader(fluxo);
		BufferedReader buffer = new BufferedReader(leitor);
		String linha = buffer.readLine();
		
		while(linha!=null){
			String[] dados = linha.split(";");
			if(id.equals(dados[0])) {
				fluxo.close();
				leitor.close();
				buffer.close();
				return (new CategoriaProduto(Utils.tryParseLong(dados[0]),dados[1],dados[2]));
			}
			linha = buffer.readLine();
		}
		fluxo.close();
		leitor.close();
		buffer.close();
		return null;
	}

	@Override
	public List<CategoriaProduto> findByName(String name) throws IOException {
		List<CategoriaProduto> list = new ArrayList<>();
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
				list.add(new CategoriaProduto(Long.parseLong(dados[0]),dados[1],dados[2]));
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
