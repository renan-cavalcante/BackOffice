package dao;

import java.io.IOException;
import java.util.List;

public interface IArquivoCSV<T> {
	
	void insert(T conteudo) throws IOException;
	void delete(Long id) throws IOException;
	void update(T conteudo) throws IOException;
	List<T> findAll() throws IOException;
	T findById(Long id) throws IOException;
	T findByName(String name) throws IOException;
}
