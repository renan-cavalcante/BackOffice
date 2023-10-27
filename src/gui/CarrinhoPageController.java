package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.naming.directory.InvalidAttributesException;

import csvConnection.Pilha;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import model.entity.Carrinho;
import model.entity.CategoriaProduto;
import model.entity.Cliente;
import model.entity.Produto;
import model.services.CarrinhoService;
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
	private TableView<Produto> listViewProduto;


	@FXML
	private TableColumn<Produto, String> tableColumnNome;

	@FXML
	private TableColumn<Produto, Integer> tableColumnQuantidade;

	@FXML
	private TableColumn<Produto, Double> tableColumnValor;

	@FXML
	private Label labelNome;

	@FXML
	private Label labelEndereco;

	@FXML
	private Label labelContato;

	@FXML
	private Label precoTotal;

	@FXML
	private TextField txtQuantidade;

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
		if (comoBoxCliente.getSelectionModel().getSelectedItem() != null) {
			try {
				String[] dadosCliente = comoBoxCliente.getEditor().getText().split(",");
				Cliente c = clienteService.findById(dadosCliente[1]);

				carrinho.setCliente(c);
				labelNome.setText(carrinho.getCliente().getNome());
				labelEndereco.setText(carrinho.getCliente().getEndereco().toString());
				labelContato.setText(carrinho.getCliente().getContato());

			} catch (InvalidAttributesException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Alerts.showAlert("Erro", "Erro ao inserir cliente", e.getMessage(), AlertType.ERROR);
			}
		}

	}

	@FXML
	public void btnOnInserirProduto() {
		try {
			if (comoBoxProduto.getSelectionModel().getSelectedItem() != null) {

				String[] dadosProduto = comoBoxProduto.getEditor().getText().split(",");
				Produto p = new Produto(produtoService.findById(dadosProduto[1]));
				p.addQuantidade(Utils.tryParseInt(txtQuantidade.getText()));
				carrinho.addProdutos(p);

			}
			updateListViewProduto();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Alerts.showAlert("Erro", "Erro ao inserir cliente", e.getMessage(), AlertType.ERROR);
		}

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
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		tableColumnQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
		tableColumnValor.setCellValueFactory(new PropertyValueFactory<>("valor"));

	}

	public void updateComboBoxClientePesquisa() {
		List<Cliente> list;
		try {
			list = clienteService.findAll();
			obsListCliente = FXCollections.observableArrayList(list);
			comoBoxCliente.setItems(obsListCliente);
			Constraints.pesquisa(comoBoxCliente, obsListCliente);
		} catch (IOException e) {
			Alerts.showAlert("Erro", "Erro ao conectar ao banco de dados", e.getMessage(), AlertType.ERROR);
			e.printStackTrace();
		}
	}

	public void updateComboBoxProdutoPesquisa() {
		List<Produto> list;
		try {
			list = produtoService.findAll();
			list.forEach(System.out::println);
			obsListProdutoInserir = FXCollections.observableArrayList(list);
			comoBoxProduto.setItems(obsListProdutoInserir);
			Constraints.pesquisaProduto(comoBoxProduto, obsListProdutoInserir);
			comoBoxCliente.show();
		} catch (IOException e) {
			Alerts.showAlert("Erro", "Erro ao conectar ao banco de dados", e.getMessage(), AlertType.ERROR);
			e.printStackTrace();
		}
	}

	public void updateListViewProduto() throws Exception {
		List<Produto> list = new ArrayList<>();
		Pilha<Produto> aux = carrinho.getProdutos().clonar();

		while (!aux.isEmpty()) {
			list.add(aux.pop());
		}

		obsListProdutoInserido = FXCollections.observableList(list);
		listViewProduto.setItems(obsListProdutoInserido);
		listViewProduto.refresh();
		precoTotal.setText(carrinho.calcularTotal().toString());
	}
	


}
