package model.services;

import java.io.IOException;
import java.util.List;

import dao.impl.ClienteDaoCSV;
import model.entity.Cliente;

public class Clienteservice {
	ClienteDaoCSV dao = new ClienteDaoCSV();

	public List<Cliente> findAll() throws IOException {				
		return dao.findAll();
	}
}
