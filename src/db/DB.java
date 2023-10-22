package db;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class DB {
	
	public static File getArquivo(String nome) throws IOException {
		File arquivo = new File( getDir() + "\\"+nome +".csv");
		if(!arquivo.exists() || !arquivo.isFile()) {
			arquivo.createNewFile();
		}
		return arquivo;	
	}
	
	public static File getArquivoTxt(String nome) throws IOException {
		File arquivo = new File( getDir() + "\\"+nome +".txt");
		if(!arquivo.exists() || !arquivo.isFile()) {
			arquivo.createNewFile();
			if(arquivo.getPath().contains("Sequenci")) {
				addSequencia(arquivo.getPath(),1+"");
			}
		}
		return arquivo;	
	}
	
	public static String getDir() {
		String execucao = System.getProperty("user.dir");
		File dir = new File(execucao+"\\dados");
		if(!dir.exists() || !dir.isDirectory()) {
			dir.mkdir();
		}
		return dir.getPath();	
	}
	
	public static Long getSequencia(String arqSeq) throws IOException {
		File arquivo = DB.getArquivoTxt(arqSeq);
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
	
	private static void addSequencia(String arqSeq, String numero) throws IOException {
		File arquivo = DB.getArquivoTxt(arqSeq);
		FileWriter fileWriter = new FileWriter(arquivo);
		PrintWriter printWriter = new PrintWriter(fileWriter);
		printWriter.write(numero);
		printWriter.close();
		fileWriter.close();
	}

}
