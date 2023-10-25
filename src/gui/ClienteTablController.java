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
import javafx.beans.property.ReadOnlyObjectWrapper;
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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entity.Cliente;
import model.entity.Endereco;
import model.services.ClienteService;

public class ClienteTablController implements Initializable, DataChargeListener {

	private ClienteService clienteService;
	
	

	@FXML
	private Button buttonCadastrar;

	@FXML
	private Button buttonPesquisar;

	@FXML
	private TextField txfPesquisar;

	@FXML
	private TableView<Cliente> tableViewCliente;

	@FXML
	private TableColumn<Cliente, Long> tableColumnCpf;

	@FXML
	private TableColumn<Cliente, String> tableColumnNome;

	@FXML
	private TableColumn<Cliente, Endereco> tableColumnEndereco;

	@FXML
	private TableColumn<Cliente, String> tableColumnContato;

	@FXML
	private TableColumn<Cliente, String> tableColumnEmail;

	@FXML
	private TableColumn<Cliente, Cliente> tableColumnREMOVER;

	private ObservableList<Cliente> obsList;

	@FXML
	public void onBtCadastrarAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Cliente cliente = new Cliente();
		createDialogForm(cliente, "/gui/ClienteForm.fxml", parentStage, X -> {
		});
	}

	@FXML
	public void onBtTableLineAction(MouseEvent event) {

		TableViewSelectionModel<Cliente> tbv = tableViewCliente.getSelectionModel();

		int indice = tbv.getSelectedIndex();
		if (indice >= 0) {
			Cliente cliente = (Cliente) tableViewCliente.getItems().get(indice);
			createDialogView(cliente, "/gui/ClienteView.fxml", Utils.currentStage(event),
					(ClienteViewController controller) -> {
					});
		}

		tbv.clearSelection();
	}

	@FXML
	public void onBtPesquisarAction() {
		List<Cliente> clientes = new ArrayList<Cliente>();
		String textoPesquisa = txfPesquisar.getText();
		if (textoPesquisa != null) {
			try {
				if (!textoPesquisa.matches("^(([a-zA-Z ]|[é])*)$")
						&& (textoPesquisa.length() == 14 || textoPesquisa.length() == 18)) {
					clientes.add(clienteService.findById(textoPesquisa));

				} else {
					clientes = (clienteService.pesquisaPorNome(textoPesquisa));
				}
			} catch (IOException e) {
				Alerts.showAlert("Erro", "Erro ao pesquisar", e.getMessage(), AlertType.ERROR);
				e.printStackTrace();
			}
		}
		updateTableView(clientes);

	}

	public void setClienteService(ClienteService service) {
		clienteService = service;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		tableColumnCpf.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		tableColumnEndereco.setCellValueFactory(new PropertyValueFactory<>("endereco"));
		tableColumnContato.setCellValueFactory(new PropertyValueFactory<>("contato"));
		tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewCliente.prefHeightProperty().bind(stage.heightProperty());
	}

	public void updateTableView() {
		List<Cliente> list;
		try {
			list = clienteService.findAll();
			obsList = FXCollections.observableArrayList(list);
			tableViewCliente.setItems(obsList);
			initRemoveButtons();
		} catch (IOException e) {
			Alerts.showAlert("Erro", "Erro ao conectar ao banco de dados", e.getMessage(), AlertType.ERROR);
			e.printStackTrace();
		}
	}

	public void updateTableView(List<Cliente> list) {

		obsList = FXCollections.observableArrayList(list);
		tableViewCliente.setItems(obsList);
		initRemoveButtons();

	}

	@SuppressWarnings({ "unchecked", "hiding" })
	private <T> void createDialogForm(Cliente obj, String absoluteName, Stage parentStage,
			Consumer<T> initializingAction) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			ClienteFormController controller = loader.getController();
			if (obj.getId() != null) {
				if (obj.getId().length() > 14) {
					controller.onRbtEmpresaAction();
				}

			}

			controller.setCliente(obj);
			controller.setClienteService(new ClienteService());
			controller.updateDataForm();
			controller.subscribeDataListener(this);

			initializingAction.accept((T) controller);

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Cadastra cliente");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (IOException e) {
			Alerts.showAlert("IO Excpetion", "Erro carregando view", e.getMessage(), AlertType.ERROR);
		}
	}
	
	@SuppressWarnings("unchecked")
	private <T> void createDialogView(Cliente obj, String absoluteName, Stage parentStage,
			Consumer<T> initializingAction) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			ClienteViewController controller = loader.getController();
			controller.setCliente(obj);
			controller.setClienteService(new ClienteService());
			controller.updateDataForm();
			controller.subscribeDataListener(this);
			

			initializingAction.accept((T) controller);

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Cadastra cliente");
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

	private void initRemoveButtons() {
		tableColumnREMOVER.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVER.setCellFactory(param -> new TableCell<Cliente, Cliente>() {
			private final Button button = new Button("Deletar");

			protected void updateItem(Cliente cliente, boolean empty) {
				super.updateItem(cliente, empty);

				if (cliente == null) {
					setGraphic(null);
					return;
				}

				setGraphic(button);
				button.setOnAction(event -> removeEntity(cliente));
			}
		});
	}

	private void removeEntity(Cliente cliente) {
		Optional<ButtonType> escolha = Alerts.showConfirmation("Confirmar", "Deseja mesmo deletar o cliente");
		if (escolha.get() == ButtonType.OK) {

			try {
				clienteService.remove(cliente);
				updateTableView();
			} catch (IOException e) {
				Alerts.showAlert("Erro", "Erro ao deletar o cliente", e.getMessage(), AlertType.ERROR);
				e.printStackTrace();
			}
		}
	}
}
