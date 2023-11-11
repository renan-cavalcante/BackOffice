package gui;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import csvConnection.Pilha;
import exercicio3.filalib.Fila;
import gui.listeners.DataChargeListener;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import model.entity.Carrinho;
import model.entity.Produto;
import model.entity.Venda;
import model.services.ProdutoService;
import model.services.VendaService;

public class CheckoutController implements Initializable {

	private Fila<Produto> produtos = new Fila<>();
	private VendaService vendaService = new VendaService();
	private Carrinho carrinho;
	private ProdutoService produtoService = new ProdutoService();
	private List<DataChargeListener> listeners = new ArrayList<>();

	@FXML
	private Label labelProduto;

	@FXML
	private Button btNext;

	@FXML
	private Button btcancelar;

	public void setVendaService(VendaService vendaService) {
		this.vendaService = vendaService;

		setFila(carrinho.getProdutos().clonar());
		initializeNode();
	}

	public void setCarrinho(Carrinho carrinho) {
		this.carrinho = carrinho;
		
	}

	/**
	 * Converte uma pilha para list e seta no atributo produtos
	 * 
	 * @param p : Pilha<Produto>
	 */
	private void setFila(Pilha<Produto> p) {
		Pilha<Produto> aux = p.clonar();
		while (!aux.isEmpty()) {
			try {
				produtos.insert(aux.pop());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Passa para o proximo produto da List
	 * 
	 * @param event
	 */
	@FXML
	public void onBtNextAction(ActionEvent event) {

		try {
			if (!produtos.isEmpty()) {
				Produto produto = produtos.Remove();
				StringBuilder sb = new StringBuilder();
				sb.append(produto.getNome());
				sb.append("\t");
				sb.append("Valor: " + produto.getValor());
				sb.append("\t");
				sb.append("quantidade: " + produto.getQuantidade());
				sb.append("\t");
				sb.append("Total: " + produto.getValor() * produto.getQuantidade());

				labelProduto.setText(sb.toString());
				if (produtos.isEmpty()) {
					btNext.setText("COMPRAR");
				}

			} else {
				Produto[] produtos = produtoService.tabelaHash();
				Pilha<Produto> pilhaProduto = carrinho.getProdutos().clonar();
				while(!pilhaProduto.isEmpty()) {
					Produto produtoCarrinho =  pilhaProduto.pop();
					Produto produtoEstoque = produtos[produtoCarrinho.hashCode()];
					produtoEstoque.subQuantidade(produtoCarrinho.getQuantidade());
					produtoService.update(produtoEstoque);
					
				}
				vendaService.saveOrUpdate(new Venda(carrinho, LocalDateTime.now()));
				carrinho = null;
				notifyDataChangeListeners();
				Utils.currentStage(event).close();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@FXML
	public void onBtCancelarAction(ActionEvent event) {

	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		
	}
	
	private void initializeNode() {
		
			try {
				if (!produtos.isEmpty()) {
					Produto produto;
				produto = produtos.Remove();
				StringBuilder sb = new StringBuilder();
				sb.append(produto.getNome());
				sb.append("\t");
				sb.append("Valor: " + produto.getValor());
				sb.append("\t");
				sb.append("quantidade: " + produto.getQuantidade());
				sb.append("\t");
				sb.append("Total: " + produto.getValor() * produto.getQuantidade());

				labelProduto.setText(sb.toString());
				if (produtos.isEmpty()) {
					btNext.setText("COMPRAR");
				}

			}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	}
	
	/**
	 * metodo para class se inscrever no listener
	 * 
	 * @param listener: class a se inscrista
	 */
	public void subscribeDataListener(DataChargeListener listener) {
		listeners.add(listener);
	}
	
	/**
	 * Avisa as class inscrita no listener que ouve atualização
	 */
	private void notifyDataChangeListeners() {
		for (DataChargeListener listener : listeners) {
			listener.onDataChanged();
		}
	}


}
