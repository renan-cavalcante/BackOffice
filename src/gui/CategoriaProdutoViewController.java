package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
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
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entity.CategoriaProduto;
import model.services.CategoriaProdutoService;

public class CategoriaProdutoViewController implements Initializable, DataChargeListener {

	private CategoriaProdutoService categoriaProdutoService;

	@FXML
	private Button buttonCadastrar;

	@FXML
	private Button buttonPesquisar;
	

	@FXML
	private TextField txfPesquisar;
	
	@FXML
	private ListView<CategoriaProduto> listViewCategoriaProduto;

	private ObservableList<CategoriaProduto> obsList;

	@FXML
	public void onBtCadastrarAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		CategoriaProduto categoriaProduto = new CategoriaProduto();
		createDialogForm(categoriaProduto, "/gui/CategoriaProdutoForm.fxml", parentStage, X -> {
		});
	}


	@FXML
	public void onBtPesquisarAction() {
		List<CategoriaProduto> categoriaProdutos = new ArrayList<CategoriaProduto>();
		String textoPesquisa = txfPesquisar.getText();
		if (textoPesquisa != null) {
			try {
				if (!textoPesquisa.matches("^(([a-zA-Z ]|[é])*)$")
						&& (textoPesquisa.length() == 14 || textoPesquisa.length() == 18)) {
					categoriaProdutos.add(categoriaProdutoService.findById(textoPesquisa));

				} else {
					categoriaProdutos = (categoriaProdutoService.pesquisaPorNome(textoPesquisa));
				}
			} catch (IOException e) {
				Alerts.showAlert("Erro", "Erro ao pesquisar", e.getMessage(), AlertType.ERROR);
				e.printStackTrace();
			}
		}
		updateListView(categoriaProdutos);

	}

	public void setCategoriaProdutoService(CategoriaProdutoService service) {
		categoriaProdutoService = service;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {

		Stage stage = (Stage) Main.getMainScene().getWindow();
		if(listViewCategoriaProduto != null) {
			listViewCategoriaProduto.prefHeightProperty().bind(stage.heightProperty());
		}
		
	}

	public void updateListView() {
		List<CategoriaProduto> list;
		try {
			list = categoriaProdutoService.findAll();
			
			obsList = FXCollections.observableArrayList(list);
			listViewCategoriaProduto.setItems(obsList);
			
			
		} catch (IOException  e) {
			Alerts.showAlert("Erro", "Erro ao conectar ao banco de dados", e.getMessage(), AlertType.ERROR);
			e.printStackTrace();
		}
	}

	public void updateListView(List<CategoriaProduto> list) {

		obsList = FXCollections.observableArrayList(list);
		listViewCategoriaProduto.setItems(obsList);

	}

	@SuppressWarnings({ "unchecked", "hiding" })
	private <T> void createDialogForm(CategoriaProduto obj, String absoluteName, Stage parentStage,
			Consumer<T> initializingAction) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			CategoriaProdutoFormController controller = loader.getController();
	
			controller.setCategoriaProduto(obj);
			controller.setCategoriaProdutoService(new CategoriaProdutoService());
			controller.updateDataForm();
			controller.subscribeDataListener(this);

			initializingAction.accept((T) controller);

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Cadastra categoriaProduto");
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
		updateListView();
	}

//	private void initRemoveButtons() {
//		tableColumnREMOVER.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
//		tableColumnREMOVER.setCellFactory(param -> new TableCell<CategoriaProduto, CategoriaProduto>() {
//			private final Button button = new Button("Deletar");
//
//			protected void updateItem(CategoriaProduto categoriaProduto, boolean empty) {
//				super.updateItem(categoriaProduto, empty);
//
//				if (categoriaProduto == null) {
//					setGraphic(null);
//					return;
//				}
//
//				setGraphic(button);
//				button.setOnAction(event -> removeEntity(categoriaProduto));
//			}
//		});
//	}

	@SuppressWarnings("unused")
	private void removeEntity(CategoriaProduto categoriaProduto) {
		Optional<ButtonType> escolha = Alerts.showConfirmation("Confirmar", "Deseja mesmo deletar A categoria");
		if (escolha.get() == ButtonType.OK) {

			try {
				categoriaProdutoService.remove(categoriaProduto);
				updateListView();
			} catch (IOException e) {
				Alerts.showAlert("Erro", "Erro ao deletar o categoriaProduto", e.getMessage(), AlertType.ERROR);
				e.printStackTrace();
			}
		}
	}
}
