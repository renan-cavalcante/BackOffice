package dao;

import java.io.IOException;
import java.util.List;

public interface IArquivoCSV<T> {
	
	void insert(T conteudo) throws IOException;
	void delete(String id) throws IOException;
	void update(T conteudo) throws IOException;
	List<T> findAll() throws IOException;
	T findById(String id) throws IOException;
	List<T> findByName(String name) throws IOException;
}
