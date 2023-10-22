package model.services;

import java.util.ArrayList;
import java.util.List;

import model.entity.Cliente;
import model.entity.Endereco;

public class Clienteservice {

	public List<Cliente> findAll() {
		List<Cliente> list = new ArrayList<>();
		Endereco endereco1 = new Endereco("Rua1", "10", "B", "01013-020");
		list.add(new Cliente(0157423L, "Robson", endereco1, "11-01574-15420"));
				
		return list;
	}
}
