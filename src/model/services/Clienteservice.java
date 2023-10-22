package model.services;

import java.io.IOException;
import java.util.List;

import dao.impl.ClienteDaoCSV;
import model.entity.Cliente;

public class ClienteService {
	ClienteDaoCSV dao = new ClienteDaoCSV();

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
}
