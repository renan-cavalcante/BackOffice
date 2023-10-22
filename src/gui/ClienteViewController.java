package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entity.Cliente;
import model.entity.Endereco;
import model.services.ClienteService;

public class ClienteViewController implements Initializable, DataChargeListener {
	
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
	private TableColumn<Cliente, String> tableColumnNome ;
	 
	@FXML
	private TableColumn<Cliente, Endereco> tableColumnEndereco ;
	 
	@FXML
	private TableColumn<Cliente, String> tableColumnContato ;
	
	private ObservableList<Cliente> obsList;
	
	@FXML
	public void onBtCadastrarAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Cliente cliente = new Cliente();
		createDialogForm(cliente, "/gui/ClienteForm.fxml", parentStage);
	}
	
	@FXML
	public void onBtPesquisarAction() {
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
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewCliente.prefHeightProperty().bind(stage.heightProperty());
	}
	
	public void updateTableView()  {
		List<Cliente> list;
		try {
			list = clienteService.findAll();
			obsList = FXCollections.observableArrayList(list);
			tableViewCliente.setItems(obsList);
		} catch (IOException e) {
			Alerts.showAlert("Erro", "Erro ao conectar ao banco de dados", e.getMessage(), AlertType.ERROR);
			e.printStackTrace();
		}
	}
	
	private void createDialogForm(Cliente obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader =new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
			ClienteFormController controller = loader.getController();
			controller.setCliente(obj);
			controller.setClienteService(new ClienteService());
			controller.updateDataForm();
			controller.subscribeDataListener(this);
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Cadastra cliente");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		}
		catch(IOException e) {
			Alerts.showAlert("IO Excpetion", "Erro carregando view", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChanged() {
		updateTableView();
	}
}                        
