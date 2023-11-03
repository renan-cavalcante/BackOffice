package db;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class DB {
	
	public  File getArquivo(String nome) throws IOException {
		File arquivo = new File( getDir() + "\\"+nome +".csv");
		if(!arquivo.exists() || !arquivo.isFile()) {
			arquivo.createNewFile();
		}
		return arquivo;	
	}
	
	public  File getArquivoTxt(String nome) throws IOException {
		File arquivo = new File( getDir() + "\\"+nome +".txt");
		if(!arquivo.exists() || !arquivo.isFile()) {
			arquivo.createNewFile();
			if(arquivo.getPath().contains("Sequenci")) {
				FileWriter fileWriter = new FileWriter(arquivo, true);
				PrintWriter printWriter = new PrintWriter(fileWriter);
				printWriter.write("0");
				printWriter.close();
				fileWriter.close();
			}
		}
		return arquivo;	
	}
	
	public  String getDir() {
		String execucao = System.getProperty("user.dir");
		File dir = new File(execucao+"\\dados");
		if(!dir.exists() || !dir.isDirectory()) {
			dir.mkdir();
		}
		return dir.getPath();	
	}
	
	public  Long getSequencia(String arqSeq) throws IOException {
		File arquivo = getArquivoTxt(arqSeq);
		FileInputStream fluxo = new FileInputStream(arquivo);
		InputStreamReader leitor = new InputStreamReader(fluxo);
		BufferedReader buffer = new BufferedReader(leitor);
		String linha = buffer.readLine();
		Long sequencia = Long.parseLong(linha);
		addSequencia(arqSeq, (sequencia+1)+"");
		fluxo.close();
		leitor.close();
		buffer.close();
		return sequencia;
		
	}
	
	private  void addSequencia(String arqSeq, String numero) throws IOException {
		File arquivo = getArquivoTxt(arqSeq);
		FileWriter fileWriter = new FileWriter(arquivo);
		PrintWriter printWriter = new PrintWriter(fileWriter);
		printWriter.write(numero);
		printWriter.close();
		fileWriter.close();
	}
	
	public  void escrever(String dados, String arquivo) throws IOException {
		File caminho = getArquivo(arquivo);
		FileWriter fileWriter = new FileWriter(caminho, true);
		PrintWriter printWriter = new PrintWriter(fileWriter);
		printWriter.write(dados+ "\r\n");
		printWriter.close();
		fileWriter.close();
	}
	
	public  void sobrescrever(String[] dados, String arquivo) throws IOException {
		File caminho = getArquivo(arquivo);
		FileWriter fileWriter = new FileWriter(caminho, false);
		PrintWriter printWriter = new PrintWriter(fileWriter);
		for(String s : dados) {
			printWriter.write(s+ "\r\n");
		}
		printWriter.close();
		fileWriter.close();
	}
	
	public  List<String> ler(String arquivo) throws IOException {
		List<String> dados = new ArrayList<>();
		File caminho = getArquivo(arquivo);
		FileInputStream fluxo = new FileInputStream(caminho);
		InputStreamReader leitor = new InputStreamReader(fluxo);
		BufferedReader buffer = new BufferedReader(leitor);
		String linha = buffer.readLine();
		
		while (linha != null) {
			dados.add(linha);
			linha = buffer.readLine();
		}
		fluxo.close();
		leitor.close();
		buffer.close();
		return dados;
		
	}

}
