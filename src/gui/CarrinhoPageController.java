package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.Consumer;

import javax.naming.directory.InvalidAttributesException;

import csvConnection.Pilha;
import gui.listeners.DataChargeListener;
import gui.util.Alerts;
import gui.util.Constraints;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entity.Carrinho;
import model.entity.Cliente;
import model.entity.Produto;
import model.exception.ValidationException;
import model.services.ClienteService;
import model.services.ProdutoService;
import model.services.VendaService;

public class CarrinhoPageController implements Initializable, DataChargeListener {

	private ClienteService clienteService = new ClienteService();
	private ProdutoService produtoService;
	private static Carrinho carrinho = new Carrinho();

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

	/**
	 * Exibe tela de cadastro de cliente
	 * @param event
	 */
	@FXML
	public void btnOnCadastraCliente(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Cliente cliente = new Cliente();
		createDialogForm(cliente, "/gui/ClienteForm.fxml", parentStage, (ClienteFormController controller) -> {
			controller.setCliente(cliente);
			controller.setClienteService(new ClienteService());
			controller.updateDataForm();

			controller.subscribeDataListener(this);
		});
	}

	/**
	 * Insere cliente cadastrado para o carrinho
	 * 
	 * @throws InvalidAttibutesException
	 * @throws IOException
	 */
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

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Alerts.showAlert("Erro", "Erro ao inserir cliente", e.getMessage(), AlertType.ERROR);
			} catch (InvalidAttributesException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/**
	 * Insere produto selecionado para o carrinho
	 */
	@FXML
	public void btnOnInserirProduto() {
		try {
			if (comoBoxProduto.getSelectionModel().getSelectedItem() != null) {

				String[] dadosProduto = comoBoxProduto.getEditor().getText().split(",");
				Produto p = new Produto(produtoService.findById(dadosProduto[1]));
				p.addQuantidade(Utils.tryParseInt(txtQuantidade.getText()));
				carrinho.addProdutos(p);

			}
			updateTableViewProduto();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Alerts.showAlert("Erro", "Erro ao inserir cliente", e.getMessage(), AlertType.ERROR);
		}

	}

	/**
	 * chama pagina de checkou para finaliza a venda
	 * 
	 * @param event
	 */
	@FXML
	public void btnOnFinalizar(ActionEvent event) {
		try {
			ValidationException exception = new ValidationException("Validação erros");
			validacao(exception);

			if (exception.getErros().size() > 0) {
				throw exception;
			}
			createCheckout("/gui/Checkout.fxml", Utils.currentStage(event));
		} catch (ValidationException e) {
			setMessagemErro(e.getErros());
		}
	}

	/**
	 * salva carrinho
	 */
	@FXML
	public void btnOnSalvar() {

	}

	public void setClienteService(ClienteService service) {
		clienteService = service;
	}

	public void setProdutoService(ProdutoService service) {
		produtoService = service;
	}

	public static void setCliente(Cliente c) {
		try {
			carrinho.setCliente(c);

		} catch (InvalidAttributesException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * inicializa campos da tabela de produtos inseridos
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		tableColumnQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
		tableColumnValor.setCellValueFactory(new PropertyValueFactory<>("valor"));

	}

	/**
	 * Carrega o comboBox de pesquisa de clientes
	 */
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

	/**
	 * Carrega o comboBox de pesquisa de produtos
	 */
	public void updateComboBoxProdutoPesquisa() {
		List<Produto> list;
		try {
			list = produtoService.findAll();
			obsListProdutoInserir = FXCollections.observableArrayList(list);
			comoBoxProduto.setItems(obsListProdutoInserir);
			Constraints.pesquisaProduto(comoBoxProduto, obsListProdutoInserir);
		} catch (IOException e) {
			Alerts.showAlert("Erro", "Erro ao conectar ao banco de dados", e.getMessage(), AlertType.ERROR);
			e.printStackTrace();
		}
	}

	/**
	 * Atualiza a tabela de produtos inseridos
	 * 
	 * @throws Exception
	 */
	public void updateTableViewProduto() throws Exception {
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

	/**
	 * Cria um form de acordo com absoluteName definido
	 * 
	 * @param absoluteName: caminho do arquivo FXML do form
	 * @param parentStage:  Stage da tela principal
	 * 
	 * @throws IOException
	 */
	public synchronized void createCheckout(String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			CheckoutController controller = loader.getController();
			controller.setCarrinho(carrinho);
			controller.setVendaService(new VendaService());

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Checkout");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (IOException e) {
			Alerts.showAlert("IO Excpetion", "Erro carregando view", e.getMessage(), AlertType.ERROR);
			e.printStackTrace();
		}
	}

	private <T> void createDialogForm(Cliente obj, String absoluteName, Stage parentStage,
			Consumer<T> initializingAction) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			T controller = loader.getController();

			initializingAction.accept(controller);

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

	/**
	 * Valida os dados inseridos no forms de acordo com a regra de negocio
	 * 
	 * @param ve: map de exceptions com os erros de validações
	 */
	private void validacao(ValidationException ve) {
		if (carrinho.getCliente() == null) {
			ve.addErro("Cliente", "Selecione um cliente para a venda");
		}
		if (carrinho.getProdutos().isEmpty()) {
			ve.addErro("Produtos", "Insira no minimo um produto para a venda");
		}

	}

	/**
	 * Exibe as mensagens de erro para os campos que não atende a validação
	 * 
	 * @param Map dos erros encontrados na validação
	 */
	private void setMessagemErro(Map<String, String> erros) {
		Set<String> fields = erros.keySet();

		if (fields.contains("Cliente")) {
			Alerts.showAlert("Erro", null, (erros.get("Cliente")), AlertType.ERROR);
		}
		if (fields.contains("Produtos")) {
			Alerts.showAlert("Erro", null, (erros.get("Produtos")), AlertType.ERROR);
		}

	}

	/**
	 * Atualiza dados do cliente inserido
	 */
	@Override
	public void onDataChanged() {
		labelNome.setText(carrinho.getCliente().getNome());
		labelEndereco.setText(carrinho.getCliente().getEndereco().toString());
		labelContato.setText(carrinho.getCliente().getContato());

	}

}
