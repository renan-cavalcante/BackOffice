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
import model.entity.Produto;

public class CategoriaProdutoDaoCSV implements IArquivoCSV<CategoriaProduto>{
	
	private ProdutoDaoCSV daoProduto;
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
			 if( Utils.classeChamadora() == ProdutoDaoCSV.class.getName()) {
				 list.add(new CategoriaProduto(Long.parseLong(dados[0]),dados[1],dados[2]));
			 }else {
				 list.add(new CategoriaProduto(Long.parseLong(dados[0]),dados[1],dados[2], relacionamentoCategoriaProduto(dados[0])));
			 }
			
			linha = buffer.readLine();
		}
		fluxo.close();
		leitor.close();
		buffer.close();
		return list;
	}
	
	private List<Produto> relacionamentoCategoriaProduto(String id) throws IOException {
		List<Produto> produtos = new ArrayList<>();
		
		
		setDaoService();
		for(Produto p : daoProduto.findAll()) {
			if(p.getCategoria().getId().toString().equals(id)) {
				produtos.add(p);
			}
		}
		return produtos;
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
				CategoriaProduto cp;
				fluxo.close();
				leitor.close();
				buffer.close();
				 if( Utils.classeChamadora() == ProdutoDaoCSV.class.getName()) {
					 cp = (new CategoriaProduto(Long.parseLong(dados[0]),dados[1],dados[2]));
				 }else {
					 cp = (new CategoriaProduto(Long.parseLong(dados[0]),dados[1],dados[2], relacionamentoCategoriaProduto(dados[0])));
				 }
				return (cp);
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
				if( Utils.classeChamadora() == ProdutoDaoCSV.class.getName()) {
					 list.add(new CategoriaProduto(Long.parseLong(dados[0]),dados[1],dados[2]));
				 }else {
					 list.add(new CategoriaProduto(Long.parseLong(dados[0]),dados[1],dados[2], relacionamentoCategoriaProduto(dados[0])));
				 }
			}
			linha = buffer.readLine();
		}
		System.out.println("nada");
		fluxo.close();
		leitor.close();
		buffer.close();
		return list;
	}
	
	public void setDaoService() {
		daoProduto = new ProdutoDaoCSV();
	}

	
}
