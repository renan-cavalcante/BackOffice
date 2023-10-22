package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entity.Cliente;
import model.entity.Endereco;
import model.services.Clienteservice;

public class ClienteViewController implements Initializable {
	
	private Clienteservice clienteService;

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
	private TableColumn<Cliente, String> tableColumnCelular ;
	
	private ObservableList<Cliente> obsList;
	
	@FXML
	public void onBtCadastrarAction() {
	}
	
	@FXML
	public void onBtPesquisarAction() {
	}

	public void setClienteService(Clienteservice service) {
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
		tableColumnCelular.setCellValueFactory(new PropertyValueFactory<>("celular"));
		
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
}                        
