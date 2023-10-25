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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entity.CategoriaProduto;
import model.services.CategoriaProdutoService;

public class CategoriaProdutoViewController implements Initializable {
	
	private CategoriaProduto categoriaProduto;
	private CategoriaProdutoService categoriaProdutoService;
	private List<DataChargeListener> listeners = new ArrayList<>();
	
	@FXML
	private Label labelId;
	
	@FXML
	private Label labelNome;
	
	@FXML
	private TextArea txtAreaDescricao;
	
//	@FXML
//	private ListView<Produtos> listViewProdutos;
	
	@FXML
	private Button btnEditar;
	
	@FXML
	private Button btnExcluir;
	
	@FXML
	public void onBtnEditar(ActionEvent event) {
		createDialogForm(categoriaProduto, "/gui/CategoriaProdutoForm.fxml", Utils.currentStage(event),
				(x) -> {});
	}
	
	@FXML
	public void onBtnExcluir(ActionEvent event) {
		Optional<ButtonType> escolha = Alerts.showConfirmation("Confirmar", "Deseja mesmo deletar o cliente");
		if (escolha.get() == ButtonType.OK) {

			try {
				categoriaProdutoService.remove(categoriaProduto);
				notifyDataChangeListeners();
				Utils.currentStage(event).close();
			} catch (IOException e) {
				Alerts.showAlert("Erro", "Erro ao deletar o cliente", e.getMessage(), AlertType.ERROR);
				e.printStackTrace();
			}
		}
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
		String id = categoriaProduto.getId().toString();
		String nome = categoriaProduto.getNome();
		String descricao = categoriaProduto.getDescricao();

		labelId.setText(id);
		labelNome.setText(nome);
		txtAreaDescricao.setText(descricao);

	}
	
	public void setCategoriaProdutoService(CategoriaProdutoService service) {
		categoriaProdutoService = service;
	}
	
	public void setCategoriaProduto(CategoriaProduto categoriaProduto) {
		this.categoriaProduto = categoriaProduto;
	}
	
	@SuppressWarnings({ "unchecked" })
	private <T> void createDialogForm(CategoriaProduto obj, String absoluteName, Stage parentStage,
			Consumer<T> initializingAction) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			CategoriaProdutoFormController controller = loader.getController();

			controller.setCategoriaProduto(obj);
			controller.setCategoriaProdutoService(new CategoriaProdutoService());
			controller.updateDataForm();

			initializingAction.accept((T) controller);

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Editar Categoria");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (IOException e) {
			Alerts.showAlert("IO Excpetion", "Erro carregando view", e.getMessage(), AlertType.ERROR);
		}
	}
}
