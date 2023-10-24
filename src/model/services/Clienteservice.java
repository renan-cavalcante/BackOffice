package model.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import dao.impl.ClienteDaoCSV;
import model.entity.Cliente;

public class ClienteService {
	ClienteDaoCSV dao = new ClienteDaoCSV();
	private static Comparator<Cliente> compNome = (n1,n2) -> n1.getNome().compareTo(n2.getNome());

	public List<Cliente> findAll() throws IOException {				
		return dao.findAll();
	}
	
	public void saveOrUpdate(Cliente cliente) throws IOException {
		if(dao.findById(cliente.getId()) != null) {
			dao.update(cliente);
			return;
		}
		dao.insert(cliente);
	}
	
	public void remove(Cliente cliente) throws IOException {
		dao.delete(cliente.getId());
	}
	
	public Cliente findById(String id) throws IOException {
		return dao.findById(id);
	}
	
	public List<Cliente> findByName(String name) throws IOException {
		return dao.findByName(name);
	}
	
	public List<Cliente> pesquisaPorNome(String nome) throws IOException{
		List<Cliente> clientes = findAll();
		List<Cliente> resultado = new ArrayList<Cliente>();
		
		for(Cliente c : clientes) {
			if(c.getNome().contains(nome)) {
				resultado.add(c);
	
				System.out.println(c.toString());
			}
		}
		resultado.sort(compNome);
		return resultado;
	}
}
