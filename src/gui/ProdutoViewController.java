package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import gui.listeners.DataChargeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.entity.Produto;
import model.services.ProdutoService;

public class ProdutoViewController implements Initializable, DataChargeListener {

	private Produto produto;
	private ProdutoService produtoService;
	private List<DataChargeListener> listeners = new ArrayList<>();


	@FXML
	private Label labelId;

	@FXML
	private Label labelNome;

	@FXML
	private Label labelValor;

	@FXML
	private Label labelQuantidade;

	@FXML
	private Label labelCategoria;

	@FXML
	private TextArea txtAreaDescricao;

	@FXML
	private Button btnEditar;

	@FXML
	private Button btnExcluir;

	@FXML
	private Button btnEstoque;

	@FXML
	public void onBtnEditar(ActionEvent event) {
		createDialogForm("Editar Categoria", "/gui/ProdutoForm.fxml", Utils.currentStage(event),
				(ProdutoFormController controller) -> {
					controller.setProduto(produto);
					controller.setProdutoService(new ProdutoService());
					controller.updateDataForm();
				});
	}

	@FXML
	public void onBtnExcluir(ActionEvent event) {
		Optional<ButtonType> escolha = Alerts.showConfirmation("Confirmar", "Deseja mesmo deletar o produto");
		if (escolha.get() == ButtonType.OK) {

			try {
				produtoService.remove(produto);
				notifyDataChangeListeners();
				Utils.currentStage(event).close();
			} catch (IOException e) {
				Alerts.showAlert("Erro", "Erro ao deletar o produto", e.getMessage(), AlertType.ERROR);
				e.printStackTrace();
			}
		}
	}

	@FXML
	public void onBtnEstoque(ActionEvent event) {
		createDialogForm("Estoque", "/gui/EditQuantidadeEstoque.fxml", Utils.currentStage(event),
				(EstoqueController controller) -> {
					controller.setProduto(produto);
					controller.valorInicial(produto.getQuantidade().toString());
					controller.subscribeDataListener(this);
				});
	}

	private void notifyDataChangeListeners() {
		for (DataChargeListener listener : listeners) {
			listener.onDataChanged();
		}
	}

	public void subscribeDataListener(DataChargeListener listener) {
		listeners.add(listener);
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO Auto-generated method stub

	}

	public void updateDataForm() {
		String id = produto.getId().toString();
		String nome = produto.getNome();
		String valor = produto.getValor().toString();
		String quantidade = produto.getQuantidade().toString();
		String descricao = produto.getDescricao();
		String categoria = produto.getCategoria().getNome();

		labelId.setText(id);
		labelNome.setText(nome);
		labelValor.setText(valor);
		labelQuantidade.setText(quantidade);
		labelCategoria.setText(categoria);
		txtAreaDescricao.setText(descricao);

	}

	public void setProdutoService(ProdutoService service) {
		produtoService = service;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	private <T> void createDialogForm(String titulo, String absoluteName, Stage parentStage,
			Consumer<T> initializingAction) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			T controller = loader.getController();

			initializingAction.accept(controller);

			Stage dialogStage = new Stage();
			dialogStage.setTitle(titulo);
			dialogStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

				@Override
				public void handle(WindowEvent t) {
					t.consume();
					updateDataForm();

					dialogStage.close();

				}
			});
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (IOException e) {
			Alerts.showAlert("IO Excpetion", "Erro carregando view", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChanged() {
		updateDataForm();
		
	}

}
