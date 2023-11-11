package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import gui.listeners.DataChargeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.entity.Produto;
import model.exception.ValidationException;
import model.services.ProdutoService;

public class EstoqueController implements Initializable {

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

	public void setProduto(Produto p) {
		produto = p;
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

		if (produto.getQuantidade() > 0) {
			produto.subQuantidade(1);
			txtQuantidade.setText(produto.getQuantidade().toString());
		} else {
			Alerts.showAlert("Erro", null, "A quantidade não pode ser negativa", AlertType.ERROR);
		}

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

		produto.addQuantidade(1);
		txtQuantidade.setText(produto.getQuantidade().toString());

	}


	/**
	 * Salva ou atualiza o estoque do produto do form
	 * 
	 * @param event
	 * @throws IOException
	 * @throws ValidationException
	 */
	@FXML
	public void onBtSalvarAction(ActionEvent event) {

		try {
			int quantidade = produto.getQuantidade();
			if (quantidade >= 0) {
				produto.setQuantidade(Utils.tryParseInt(txtQuantidade.getText()));
				produtoService.update(produto);
				notifyDataChangeListeners();
			}else {
				Alerts.showAlert("Erro", null, "A quantidade não pode ser negativa", AlertType.ERROR);
			}

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
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		
		initizalizeNode();
		
	}

	private void initizalizeNode() {
		Constraints.setTextFieldInteger(txtQuantidade);
		
		

	}
	
	public void valorInicial(String valor) {
		txtQuantidade.setText(valor);

	}
	
	private void notifyDataChangeListeners() {
		for (DataChargeListener listener : listeners) {
			listener.onDataChanged();
		}
	}

	public void subscribeDataListener(DataChargeListener listener) {
		listeners.add(listener);
	}
	

}
