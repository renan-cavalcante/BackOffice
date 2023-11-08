package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.naming.directory.InvalidAttributesException;

import gui.listeners.DataChargeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.entity.Carrinho;
import model.entity.Produto;
import model.exception.ValidationException;
import model.services.ProdutoService;

public class CarrinhoEditController implements Initializable {

	private Carrinho carrinho;
	private Produto produto;
	private ProdutoService produtoService = new ProdutoService();
	private int quantidadeOriginal;


	private List<DataChargeListener> listeners = new ArrayList<>();

	@FXML
	private TextField txtQuantidade;

	@FXML
	private Button btMais;

	@FXML
	private Button btMenos;

	@FXML
	private Button btSalvar;

	@FXML
	private Button btcancelar;

	@FXML
	private Button btExcluir;

	public void setProduto(Produto produto) {
		this.produto = produto;
		quantidadeOriginal = this.produto.getQuantidade();
		txtQuantidade.setText(produto.getQuantidade().toString());
	}
	
	public void setCarrinho(Carrinho carrinho) {
		this.carrinho = carrinho;
	}

	public void setCarrinhoService(ProdutoService service) {
		this.produtoService = service;
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
	 * Aumenta quantidade do produto
	 * 
	 * @param event
	 * @throws IOException
	 * @throws ValidationException
	 */
	@FXML
	public void onBtMais(ActionEvent event) {

		try {
			int quantidade = produto.getQuantidade();
	
			produto.addQuantidade(1);

			if (!verificaDisponibilidadeEstoque()) {
				produto.setQuantidade(quantidade);
				throw new InvalidAttributesException("Não a estoque suficiente para adiciona o produto");
			}
			
			txtQuantidade.setText(Utils.tryParseInt(txtQuantidade.getText()) + 1 + "");
		} catch (InvalidAttributesException e) {
			Alerts.showAlert("Invalido", null, e.getMessage(), AlertType.WARNING);
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Diminui quantidade do produto
	 * 
	 * @param event
	 * @throws IOException
	 * @throws ValidationException
	 */
	@FXML
	public void onBtMenos(ActionEvent event) {

		try {
			if (produto.getQuantidade() > 0) {
				produto.subQuantidade(1);
				txtQuantidade.setText(Utils.tryParseInt(txtQuantidade.getText()) - 1 + "");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Avisa as class inscrita no listener que ouve atualização
	 */
	private void notifyDataChangeListeners() {
		for (DataChargeListener listener : listeners) {
			listener.onDataChanged();
		}
	}
	
	/**
	 * Salva ou atualiza o produto do form
	 * 
	 * @param event
	 * @throws IOException
	 * @throws ValidationException
	 */
	@FXML
	public void onBtSalvarAction(ActionEvent event) {
	
		try {
			int quantidade = produto.getQuantidade();
			produto.setQuantidade(Utils.tryParseInt(txtQuantidade.getText()));
			if (!verificaDisponibilidadeEstoque()) {
				produto.setQuantidade(quantidade);
				throw new InvalidAttributesException("Não a estoque suficiente para adiciona o produto");
			}

			if (produto.getQuantidade() < 1) {
				carrinho.removeProdutos(produto);
			}
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
		} catch (InvalidAttributesException e) {
			Alerts.showAlert("Invalido", null, e.getMessage(), AlertType.WARNING);
			e.printStackTrace();
		} catch (Exception e) {
			Alerts.showAlert("Erro", null, e.getMessage(), AlertType.ERROR);
			e.printStackTrace();
		}
	}
	
	/**
	 * Fecha o form sem salvar os dados
	 * 
	 * @param event
	 */
	@FXML
	public void onBtExcluirAction(ActionEvent event) {
		try {
			carrinho.removeProdutos(produto);
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
		} catch (Exception e) {
			Alerts.showAlert("Erro", null, e.getMessage(), AlertType.ERROR);
			e.printStackTrace();
		}
	}

	/**
	 * Fecha o form sem salvar os dados
	 * 
	 * @param event
	 */
	@FXML
	public void onBtCancelarAction(ActionEvent event) {
		produto.setQuantidade(quantidadeOriginal);
	
		Utils.currentStage(event).close();
	}
	
	public void onBtClosed(Stage stage) {
	stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			
			@Override
			public void handle(WindowEvent t) {
				t.consume();
				produto.setQuantidade(quantidadeOriginal);
				stage.close();
				
			}
		});
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initizalizeNode();
		
	}

	private void initizalizeNode() {
		Constraints.setTextFieldInteger(txtQuantidade);
		

	}
	
	private boolean verificaDisponibilidadeEstoque( ) throws Exception{
		Produto produtoEstoque = produtoService.findById(produto.getId().toString());
	

		if(produto.getQuantidade() > produtoEstoque.getQuantidade()) {
			return false;
		}		
	
		return true;
	
	}

}
