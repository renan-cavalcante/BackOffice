package db;

import java.io.File;
import java.io.IOException;

public class DB {
	
	public static File getArquivo(String nome) throws IOException {
		File arquivo = new File( getDir() + "\\"+nome +".csv");
		if(!arquivo.exists() || !arquivo.isFile()) {
			arquivo.createNewFile();
		}
		return arquivo;
		
	}
	
	private static String getDir() {
		String execucao = System.getProperty("user.dir");
		File dir = new File(execucao+"\\dados");
		if(!dir.exists() || !dir.isDirectory()) {
			dir.mkdir();
		}
		return dir.getPath();	
	}

}
