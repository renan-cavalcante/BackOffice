package gui;

import java.net.URL;
import java.util.ResourceBundle;

import csvConnection.Pilha;
import exercicio3.filalib.Fila;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import model.entity.Carrinho;
import model.entity.Produto;
import model.entity.Venda;
import model.services.VendaService;

public class CheckoutController implements Initializable {

	private Fila<Produto> produtos = new Fila<>();
	private VendaService vendaService = new VendaService();
	private Carrinho carrinho = new Carrinho();

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
		while (!p.isEmpty()) {
			try {
				produtos.insert(p.pop());
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

				vendaService.saveOrUpdate(new Venda(carrinho));
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

}
