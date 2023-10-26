package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entity.CategoriaProduto;
import model.services.CategoriaProdutoService;

public class CategoriaProdutoTablController implements Initializable, DataChargeListener {

	private CategoriaProdutoService categoriaProdutoService;

	@FXML
	private Button buttonCadastrar;

	@FXML
	private Button buttonPesquisar;

	@FXML
	private Button buttonListCategoria;

	@FXML
	private TextField txfPesquisar;

	@FXML
	private TableView<CategoriaProduto> tableViewCategoriaProduto;

	@FXML
	private TableColumn<CategoriaProduto, Long> tableColumnId;

	@FXML
	private TableColumn<CategoriaProduto, String> tableColumnNome;

	@FXML
	private TableColumn<CategoriaProduto, String> tableColumnDescricao;

	private ObservableList<CategoriaProduto> obsList;

	@FXML
	public void onBtCadastrarAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		CategoriaProduto categoriaProduto = new CategoriaProduto();
		createDialogForm(categoriaProduto, "/gui/CategoriaProdutoForm.fxml", parentStage, X -> {
		});
	}
	
	@FXML
	public void onBtListAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		CategoriaProduto categoriaProduto = new CategoriaProduto();
		createDialogList(categoriaProduto, "/gui/CategoriaProdutoList.fxml", parentStage, X -> {
		});
	}


	@FXML
	public void onBtPesquisarAction() {
		List<CategoriaProduto> categoriaProdutos = new ArrayList<CategoriaProduto>();
		String textoPesquisa = txfPesquisar.getText();
		if (textoPesquisa != null) {
			try {
				if (textoPesquisa.matches("[0-9]*")) {
					categoriaProdutos.add(categoriaProdutoService.findById(textoPesquisa));

				} else {
					categoriaProdutos = (categoriaProdutoService.pesquisaPorNome(textoPesquisa));
				}
			} catch (IOException e) {
				Alerts.showAlert("Erro", "Erro ao pesquisar", e.getMessage(), AlertType.ERROR);
				e.printStackTrace();
			}
		}
		updateTableView(categoriaProdutos);

	}

	@FXML
	public void onBtTableLineAction(MouseEvent event) {

		TableViewSelectionModel<CategoriaProduto> tbv = tableViewCategoriaProduto.getSelectionModel();

		int indice = tbv.getSelectedIndex();
		if (indice >= 0) {
			CategoriaProduto categoria = (CategoriaProduto) tableViewCategoriaProduto.getItems().get(indice);
			createDialogView(categoria, "/gui/categoriaProdutoView.fxml", Utils.currentStage(event),
					(CategoriaProdutoViewController controller) -> {
					});
		}

		tbv.clearSelection();
	}

	public void setCategoriaProdutoService(CategoriaProdutoService service) {
		categoriaProdutoService = service;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {

		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		tableColumnDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewCategoriaProduto.prefHeightProperty().bind(stage.heightProperty());

	}

	public void updateTableView() {
		List<CategoriaProduto> list;
		try {
			list = categoriaProdutoService.findAll();
			obsList = FXCollections.observableArrayList(list);
			tableViewCategoriaProduto.setItems(obsList);

		} catch (IOException e) {
			Alerts.showAlert("Erro", "Erro ao conectar ao banco de dados", e.getMessage(), AlertType.ERROR);
			e.printStackTrace();
		}
	}

	public void updateTableView(List<CategoriaProduto> list) {

		obsList = FXCollections.observableArrayList(list);
		tableViewCategoriaProduto.setItems(obsList);

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
	
	@SuppressWarnings({ "unchecked" })
	private <T> void createDialogList(CategoriaProduto obj, String absoluteName, Stage parentStage,
			Consumer<T> initializingAction) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			CategoriaProdutoListController controller = loader.getController();

		
			controller.setCategoriaProdutoService(new CategoriaProdutoService());
			controller.updateListView();


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

	@SuppressWarnings({ "unchecked" })
	private <T> void createDialogView(CategoriaProduto obj, String absoluteName, Stage parentStage,
			Consumer<T> initializingAction) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			CategoriaProdutoViewController controller = loader.getController();

			controller.setCategoriaProduto(obj);
			controller.setCategoriaProdutoService(new CategoriaProdutoService());
			controller.updateDataForm();
			controller.subscribeDataListener(this);

			initializingAction.accept((T) controller);

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Categoria Produtos");
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
		updateTableView();
	}

}
