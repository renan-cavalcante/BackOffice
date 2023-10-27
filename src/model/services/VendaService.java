package model.services;

import java.io.IOException;
import java.util.List;

import dao.impl.VendaDaoCsv;
import model.entity.Venda;

public class VendaService {
	VendaDaoCsv dao = new VendaDaoCsv();

	public List<Venda> findAll() throws IOException {				
		return dao.findAll();
	}
	
	public void saveOrUpdate(Venda venda) throws Exception {
		if(dao.findById(venda.getId()+"") != null) {
			dao.update(venda);
			return;
		}
		dao.insert(venda);
	}
	
	public void remove(Venda cvenda) throws IOException {
		dao.delete(cvenda.getId()+"");
	}
	
	public Venda findById(String id) throws IOException {
		return dao.findById(id);
	}
	
	public List<Venda> findByName(String name) throws IOException {
		return dao.findByName(name);
	}
	

}
