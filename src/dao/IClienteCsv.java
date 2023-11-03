package dao;

import java.io.IOException;
import java.util.List;

import model.entity.Cliente;

public interface IClienteCsv {
	
	Cliente insert(Cliente conteudo) throws IOException, Throwable;
	void delete(String id) throws IOException;
	Cliente update(Cliente conteudo) throws IOException;
	List<Cliente> findAll() throws IOException;
	Cliente findById(String id) throws IOException;
	List<Cliente> findByName(String name) throws IOException;
}