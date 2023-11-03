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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entity.CategoriaProduto;
import model.entity.Produto;
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

	@FXML
	private ListView<Produto> listViewProdutos;

	@FXML
	private Button btnEditar;

	@FXML
	private Button btnExcluir;

	private ObservableList<Produto> obsList;

	/**
	 * Abre form com dados da CategoriaProduto para editar
	 * 
	 * @param event
	 */
	@FXML
	public void onBtnEditar(ActionEvent event) {
		createDialogForm("Editar", "/gui/CategoriaProdutoForm.fxml", Utils.currentStage(event), (CategoriaProdutoFormController controller) -> {
			controller.setCategoriaProduto(categoriaProduto);
			controller.setCategoriaProdutoService(new CategoriaProdutoService());
			controller.updateDataForm();
		});
	}

	/**
	 * Exclui a CategoriaProduto
	 * 
	 * @param event
	 * @throws IOException
	 * 
	 */
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
			catch (IllegalArgumentException e) {
				Alerts.showAlert("Erro", null, e.getMessage(), AlertType.ERROR);
				e.printStackTrace();
			}
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
	 * metodo para class se inscrever no listener
	 * 
	 * @param listener: class a se inscrista
	 */
	public void subscribeDataListener(DataChargeListener listener) {
		listeners.add(listener);
	}

	/**
	 * atualiza a listView dos produtos da categoria
	 */
	public void updateListView() {
		List<String> list = new ArrayList<String>();
		list.add(categoriaProduto.getProdutosToString());
		obsList = FXCollections.observableArrayList(categoriaProduto.getProdutos());
		listViewProdutos.setItems(obsList);
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
	}

	/**
	 * Atualiza os campos do form com os dados da CategoriaProduto selecionada
	 */
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

	/**
	 * Cria um form de acordo com absoluteName definido
	 * @param <T>
	 * @param titulo: titulo do form
	 * @param absoluteName: caminho do arquivo FXML do form 
	 * @param parentStage: Stage da tela principal
	 * @param initializingAction: consumer com funçoes do controller para serem iniciadas no form
	 * 
	 * @throws IOException
	 */
	private <T> void createDialogForm(String titulo, String absoluteName, Stage parentStage,
			Consumer<T> initializingAction) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();


			T controller = loader.getController();

			initializingAction.accept(controller);

			Stage dialogStage = new Stage();
			dialogStage.setTitle(titulo);
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
