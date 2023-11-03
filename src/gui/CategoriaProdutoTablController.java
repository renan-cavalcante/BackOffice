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

	/**
	 * Chama o form de cadastrado de categoria
	 * 
	 * @param event
	 */
	@FXML
	public void onBtCadastrarAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		CategoriaProduto categoriaProduto = new CategoriaProduto();
		createDialogForm("Cadastra categoria do produto", "/gui/CategoriaProdutoForm.fxml", parentStage, (CategoriaProdutoFormController controller) -> {
			controller.setCategoriaProduto(categoriaProduto);
			controller.setCategoriaProdutoService(new CategoriaProdutoService());
			controller.updateDataForm();
			controller.subscribeDataListener(this);

		});
	}

	/**
	 * Exibi a tela de listagem de Produto por CategoriaProduto
	 * 
	 * @param event
	 */
	@FXML
	public void onBtListAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		createDialogForm("Produtos por categoria", "/gui/CategoriaProdutoList.fxml", parentStage,
				(CategoriaProdutoListController controller) -> {
					controller.setCategoriaProdutoService(new CategoriaProdutoService());
					controller.updateListView();
				});
	}

	/**
	 * Pega os dados inseridos no textField de pesquisa e busca correlação no banco
	 * de dados por id (Retorna apenas 1 categoria) ou nome( Retornar todos que
	 * atendam a pesquisa) e atualiza a tabela de com os dados encontrados
	 * 
	 * @throws IOException
	 */
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
				if(textoPesquisa.isBlank() || textoPesquisa.isEmpty()){
					categoriaProdutos = categoriaProdutoService.findAll();
				}
			} catch (IOException e) {
				Alerts.showAlert("Erro", "Erro ao pesquisar", e.getMessage(), AlertType.ERROR);
				e.printStackTrace();
			}
		}
		updateTableView(categoriaProdutos);

	}

	/**
	 * Pega os dados da linha selecionada na tabela e chama o view da categoria
	 * selecionada
	 * 
	 * @param event
	 */
	@FXML
	public void onBtTableLineAction(MouseEvent event) {

		TableViewSelectionModel<CategoriaProduto> tbv = tableViewCategoriaProduto.getSelectionModel();

		int indice = tbv.getSelectedIndex();
		if (indice >= 0) {
			CategoriaProduto categoria = (CategoriaProduto) tableViewCategoriaProduto.getItems().get(indice);
			createDialogForm("Categoria", "/gui/categoriaProdutoView.fxml", Utils.currentStage(event),
					(CategoriaProdutoViewController controller) -> {
						controller.setCategoriaProduto(categoria);
						controller.setCategoriaProdutoService(new CategoriaProdutoService());
						controller.updateDataForm();
						controller.updateListView();
						controller.subscribeDataListener(this);
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

	/**
	 * Inicializa as colunas da tabela de CategoriaProduto, e seta o tamanho de
	 * acordo com a tela
	 */
	private void initializeNodes() {

		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		tableColumnDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewCategoriaProduto.prefHeightProperty().bind(stage.heightProperty());

	}

	/**
	 * Atualiza a tabela de CategoriaProduto com os dados salvos no bqanco de dados
	 * 
	 * @throws IOException
	 */
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

	/**
	 * Atualia a tabela de CategoriaProduto com a lista passada
	 * 
	 * @param list : lista com CategoriaProdutos a serem exibidas na tabela
	 */
	public void updateTableView(List<CategoriaProduto> list) {

		obsList = FXCollections.observableArrayList(list);
		tableViewCategoriaProduto.setItems(obsList);

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
			e.printStackTrace();
		}
	}

	/**
	 * listener para quando um form alterar dados de CategoriaProduto, atualizar a tabela de categorias
	 */
	@Override
	public void onDataChanged() {
		updateTableView();
	}

}
