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
import model.entity.Cliente;
import model.entity.Endereco;

public class ClienteDaoCSV implements IArquivoCSV<Cliente>{
	
	private final String NOME = "cliente";

	@Override
	public void insert(Cliente conteudo) throws IOException {
		File arquivo = DB.getArquivo(NOME);
		FileWriter fileWriter = new FileWriter(arquivo, true);
		PrintWriter printWriter = new PrintWriter(fileWriter);
		printWriter.write(conteudo.toStringCSV());
		printWriter.close();
		fileWriter.close();
	}

	@Override
	public void delete(Long id) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Cliente conteudo) throws IOException {
		// TODO Auto-generated method stub
	}

	@Override
	public List<Cliente> findAll() throws IOException {
		List<Cliente> list = new ArrayList<>();
		File arquivo = DB.getArquivo(NOME);
		FileInputStream fluxo = new FileInputStream(arquivo);
		InputStreamReader leitor = new InputStreamReader(fluxo);
		BufferedReader buffer = new BufferedReader(leitor);
		String linha = buffer.readLine();
		
		while(linha!=null){
			String[] dados = linha.split(";");
			list.add(new Cliente(Long.parseLong(dados[0]), dados[1], new Endereco(Long.parseLong(dados[2]), dados[3], dados[4], dados[5], dados[6]), dados[7]));
			linha = buffer.readLine();
		}
		fluxo.close();
		leitor.close();
		buffer.close();
		return list;
	}

	@Override
	public Cliente findById(Long id) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cliente findByName(String name) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	
}
