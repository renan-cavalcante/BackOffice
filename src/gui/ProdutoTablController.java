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
import model.entity.Produto;
import model.services.CategoriaProdutoService;
import model.services.ProdutoService;

public class ProdutoTablController implements Initializable, DataChargeListener {

	private ProdutoService produtoService;
	private ObservableList<Produto> obsList;

	@FXML
	private Button buttonCadastrar;

	@FXML
	private Button buttonPesquisar;

	@FXML
	private TextField txfPesquisar;

	@FXML
	private TableView<Produto> tableViewProduto;

	@FXML
	private TableColumn<Produto, Long> tableColumnId;

	@FXML
	private TableColumn<Produto, String> tableColumnNome;

	@FXML
	private TableColumn<Produto, Double> tableColumnValor;

	@FXML
	private TableColumn<Produto, Integer> tableColumnQuantidade;

	@FXML
	private TableColumn<Produto, String> tableColumnDescricao;

	@FXML
	private TableColumn<Produto, CategoriaProduto> tableColumnCategoria;

	/**
	 * Cria um form para cadastra novo Produto
	 * 
	 * @param event
	 */
	@FXML
	public void onBtCadastrarAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Produto produto = new Produto();
		createDialogForm("Cadastra produto", "/gui/ProdutoForm.fxml", parentStage, (ProdutoFormController controller) -> {
			controller.setProduto(produto);
			controller.setProdutoService(new ProdutoService());
			controller.updateDataForm();
			controller.subscribeDataListener(this);
			controller.setCategoriaProdutoService(new CategoriaProdutoService());
			controller.updateComboBoxView();
		});
	}

	/**
	 * Pega os dados inseridos no textField de pesquisa e busca correlação no banco
	 * de dados por id (Retorna apenas 1 Produto) ou nome( Retornar todos que
	 * atendam a pesquisa) e atualiza a tabela de com os dados encontrados
	 * 
	 * @throws IOException
	 */
	@FXML
	public void onBtPesquisarAction() {
		List<Produto> produtos = new ArrayList<Produto>();
		String textoPesquisa = txfPesquisar.getText();
		if (textoPesquisa != null) {
			try {
				if (textoPesquisa.matches("[0-9]*")) {
					produtos.add(produtoService.findById(textoPesquisa));

				} else {
					produtos = (produtoService.pesquisaPorNome(textoPesquisa));
				}
				if(textoPesquisa.isBlank() || textoPesquisa.isEmpty()){
					produtos = produtoService.findAll();
				}
			} catch (IOException e) {
				Alerts.showAlert("Erro", "Erro ao pesquisar", e.getMessage(), AlertType.ERROR);
				e.printStackTrace();
			}
		}
		updateTableView(produtos);
	}

	/**
	 * Abre form de view do produto selecionado na tabela
	 * @param event
	 */
	@FXML
	public void onBtTableLineAction(MouseEvent event) {

		TableViewSelectionModel<Produto> tbv = tableViewProduto.getSelectionModel();

		int indice = tbv.getSelectedIndex();
		if (indice >= 0) {
			Produto categoria = (Produto) tableViewProduto.getItems().get(indice);
			createDialogForm("Categoria Produtos", "/gui/produtoView.fxml", Utils.currentStage(event),
					(ProdutoViewController controller) -> {
						controller.setProduto(categoria);
						controller.setProdutoService(new ProdutoService());
						controller.updateDataForm();
						controller.subscribeDataListener(this);
					});
					
		}

		tbv.clearSelection();
	}

	public void setProdutoService(ProdutoService service) {
		produtoService = service;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	/**
	 * Inicializa as colunas da tabela
	 */
	private void initializeNodes() {

		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		tableColumnDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
		tableColumnValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
		tableColumnQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
		tableColumnCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewProduto.prefHeightProperty().bind(stage.heightProperty());

	}

	/**
	 * atualiza a tabela com os dados dos produtos salvos
	 * 
	 * @throws IOException
	 */
	public void updateTableView() {
		List<Produto> list;
		try {
			list = produtoService.findAll();
			obsList = FXCollections.observableArrayList(list);
			tableViewProduto.setItems(obsList);

		} catch (IOException e) {
			Alerts.showAlert("Erro", "Erro ao conectar ao banco de dados", e.getMessage(), AlertType.ERROR);
			e.printStackTrace();
		}
	}

	/**
	 * Atualiza os dados da tabela com a lista de Produtos passada
	 * 
	 * @param list : lista de Produtos
	 */
	public void updateTableView(List<Produto> list) {

		obsList = FXCollections.observableArrayList(list);
		tableViewProduto.setItems(obsList);

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

			

			initializingAction.accept( controller);

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

	/**
	 * listener para quando um form alterar dados de CategoriaProduto, atualizar a tabela de produtos
	 */
	@Override
	public void onDataChanged() {
		updateTableView();
	}
}
