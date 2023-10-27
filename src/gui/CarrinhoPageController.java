package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javax.naming.directory.InvalidAttributesException;

import gui.util.Alerts;
import gui.util.Constraints;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import model.entity.Carrinho;
import model.entity.Cliente;
import model.entity.Produto;
import model.services.ClienteService;
import model.services.ProdutoService;

public class CarrinhoPageController implements Initializable {

	private ClienteService clienteService = new ClienteService();
	private ProdutoService produtoService;
	private Carrinho carrinho = new Carrinho();

	@FXML
	private ComboBox<Cliente> comoBoxCliente;

	@FXML
	private ComboBox<Produto> comoBoxProduto;

	@FXML
	private ListView<Produto> listViewProduto;
	@FXML
	private Label labelNome;

	@FXML
	private Label labelEndereco;

	@FXML
	private Label labelContato;

	@FXML
	private Label precoTotal;

	@FXML
	private Button btnCadastraCliente;
	
	@FXML
	private Button btnInserirCliente;

	@FXML
	private Button btnInseirProduto;

	@FXML
	private Button btnFinalizr;

	@FXML
	private Button btnSalavar;

	private ObservableList<Cliente> obsListCliente;

	private ObservableList<Produto> obsListProdutoInserir;

	private ObservableList<Produto> obsListProdutoInserido;

	@FXML
	public void btnOnCadastraCliente() {

	}
	
	@FXML
	public synchronized void btnOnInserirCliente() {
		if(comoBoxCliente.getSelectionModel().getSelectedItem() != null) {
			try {
				Cliente cliente1 = clienteService.findByName(comoBoxCliente.getEditor().getText().trim()).get(0);
				System.out.println(cliente1);
				carrinho.setCliente(cliente1);
				labelNome.setText(carrinho.getCliente().getNome());
				labelEndereco.setText(carrinho.getCliente().getEndereco().toString());
				labelContato.setText(carrinho.getCliente().getContato());
				
			} catch (InvalidAttributesException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Alerts.showAlert("Erro", "Erro ao inserir cliente",e.getMessage(), AlertType.ERROR);
			}
			
			
		}else {
			System.out.println("não passou");
		}

	}

	@FXML
	public void btnOnInserirProduto() {

	}

	@FXML
	public void btnOnFinalizar() {

	}

	@FXML
	public void btnOnSalvar() {

	}

	public void setClienteService(ClienteService service) {
		clienteService = service;
	}

	public void setProdutoService(ProdutoService service) {
		produtoService = service;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {

	}

	public void updateComboBoxClientePesquisa() {
		List<Cliente> list;
		try {
			list = clienteService.findAll();
			obsListCliente = FXCollections.observableArrayList(list);
			comoBoxCliente.setItems(obsListCliente);
			Constraints.pesquisa(comoBoxCliente, obsListCliente);
			comoBoxCliente.show();
		} catch (IOException e) {
			Alerts.showAlert("Erro", "Erro ao conectar ao banco de dados", e.getMessage(), AlertType.ERROR);
			e.printStackTrace();
		}
	}
	
	public void updateComboBoxProdutoPesquisa() {
		List<Produto> list;
		try {
			list = produtoService.findAll();
			obsListProdutoInserir = FXCollections.observableArrayList(list);
			comoBoxProduto.setItems(obsListProdutoInserir);
			Constraints.pesquisa(comoBoxCliente, obsListCliente);
			comoBoxCliente.show();
		} catch (IOException e) {
			Alerts.showAlert("Erro", "Erro ao conectar ao banco de dados", e.getMessage(), AlertType.ERROR);
			e.printStackTrace();
		}
	}
	
	public void updateListViewProduto() {
		
			obsListProdutoInserido = FXCollections.observableList(carrinho.getProdutos());
			listViewProduto.setItems(obsListProdutoInserido);
		
	}

}
