package model.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import dao.impl.ProdutoDaoCSV;
import model.entity.Produto;

public class ProdutoService {
	ProdutoDaoCSV dao = new ProdutoDaoCSV();
	private static Comparator<Produto> compNome = (n1,n2) -> n1.getNome().compareTo(n2.getNome());

	public List<Produto> findAll() throws IOException {				
		return dao.findAll();
	}
	
	public void saveOrUpdate(Produto cproduto) throws IOException {
		if(dao.findById(cproduto.getId()+"") != null) {
			dao.update(cproduto);
			return;
		}
		dao.insert(cproduto);
	}
	
	public void remove(Produto cproduto) throws IOException {
		dao.delete(cproduto.getId()+"");
	}
	
	public Produto findById(String id) throws IOException {
		return dao.findById(id);
	}
	
	public List<Produto> findByName(String name) throws IOException {
		return dao.findByName(name);
	}
	
	public List<Produto> pesquisaPorNome(String nome) throws IOException{
		List<Produto> cprodutos = findAll();
		List<Produto> resultado = new ArrayList<Produto>();
		
		for(Produto c : cprodutos) {
			if(c.getNome().toLowerCase().contains(nome.toLowerCase())) {
				resultado.add(c);
			}
		}
		resultado.sort(compNome);
		return resultado;
	}
}
