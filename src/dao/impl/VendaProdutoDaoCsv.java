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

import db.DB;

public class VendaProdutoDaoCsv {
	private final String NOME = "venda_produto";
	

	protected void insert(String[] conteudo) throws IOException {
		File arquivo = DB.getArquivo(NOME);
		FileWriter fileWriter = new FileWriter(arquivo, true);
		PrintWriter printWriter = new PrintWriter(fileWriter);
		printWriter.write(conteudo + "\r\n");
		printWriter.close();
		fileWriter.close();

	}

	protected void delete(String id) throws IOException {
		List<String[]> list = findAll();
		File arquivo = DB.getArquivo(NOME);
		FileWriter fileWriter = new FileWriter(arquivo, false);
		PrintWriter printWriter = new PrintWriter(fileWriter);
		for (String[] s : list) {
			if (!id.equals(s[0])||!id.equals(s[1])) {
				printWriter.write(s[0]+";"+s[1]+";"+s[2] + "\r\n");
			}

		}
		printWriter.close();
		fileWriter.close();

	}


	protected List<String[]> findAll() throws IOException {
		List<String[]> list = new ArrayList<>();
		File arquivo = DB.getArquivo(NOME);
		FileInputStream fluxo = new FileInputStream(arquivo);
		InputStreamReader leitor = new InputStreamReader(fluxo);
		BufferedReader buffer = new BufferedReader(leitor);
		String linha = buffer.readLine();
		
		while(linha!=null){
			String[] dados = linha.split(";");
			list.add(dados);
			linha = buffer.readLine();
		}
		fluxo.close();
		leitor.close();
		buffer.close(); 
		return list;
	}


	public String[] findByIdProduto(String idProduto, String idVenda) throws IOException {
		File arquivo = DB.getArquivo(NOME);
		FileInputStream fluxo = new FileInputStream(arquivo);
		InputStreamReader leitor = new InputStreamReader(fluxo);
		BufferedReader buffer = new BufferedReader(leitor);
		String linha = buffer.readLine();
		
		while(linha!=null){
			String[] dados = linha.split(";");
			if(idProduto.trim().equals(dados[1]) && idVenda.trim().equals(dados[0])) {
				fluxo.close();
				leitor.close();
				buffer.close();
				return (dados);
			}
			linha = buffer.readLine();
		}
		fluxo.close();
		leitor.close();
		buffer.close();
		return null;
	}


	protected List<String[]> findById(String id) throws IOException {
		List<String[]> list = new ArrayList<>();
		File arquivo = DB.getArquivo(NOME);
		FileInputStream fluxo = new FileInputStream(arquivo);
		InputStreamReader leitor = new InputStreamReader(fluxo);
		BufferedReader buffer = new BufferedReader(leitor);
		String linha = buffer.readLine();
		
		while(linha!=null){
			String[] dados = linha.split(";");
			if(dados[0].equals(id)) {
				list.add(dados);
			}
			linha = buffer.readLine();
		}
		fluxo.close();
		leitor.close();
		buffer.close();
		return list;
	}
}
