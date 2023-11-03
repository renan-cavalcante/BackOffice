package model.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import dao.impl.CategoriaProdutoDaoCSV;
import model.entity.CategoriaProduto;

public class CategoriaProdutoService {
	CategoriaProdutoDaoCSV dao = new CategoriaProdutoDaoCSV();
	private static Comparator<CategoriaProduto> compNome = (n1,n2) -> n1.getNome().compareTo(n2.getNome());

	public List<CategoriaProduto> findAll() throws IOException {				
		return dao.findAll();
	}
	
	public void saveOrUpdate(CategoriaProduto categoriaProduto) throws IOException {
		if(dao.findById(categoriaProduto.getId()+"") != null) {
			dao.update(categoriaProduto);
			return;
		}
		dao.insert(categoriaProduto);
	}
	
	public void remove(CategoriaProduto categoriaProduto) throws IOException, IllegalArgumentException {
		dao.delete(categoriaProduto.getId()+"");
	}
	
	public CategoriaProduto findById(String id) throws IOException {
		return dao.findById(id);
	}
	
	public List<CategoriaProduto> findByName(String name) throws IOException {
		return dao.findByName(name);
	}
	
	public List<CategoriaProduto> pesquisaPorNome(String nome) throws IOException{
		List<CategoriaProduto> categoriaProdutos = findAll();
		List<CategoriaProduto> resultado = new ArrayList<CategoriaProduto>();
		
		for(CategoriaProduto c : categoriaProdutos) {
			if(c.getNome().toLowerCase().contains(nome.toLowerCase())) {
				resultado.add(c);
			}
		}
		resultado.sort(compNome);
		return resultado;
	}
}
