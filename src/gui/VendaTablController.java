package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import csvConnection.Pilha;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entity.Carrinho;
import model.entity.Produto;
import model.entity.Venda;
import model.services.ProdutoService;
import model.services.VendaService;

@SuppressWarnings("rawtypes")
public class VendaTablController implements Initializable, DataChargeListener {

	private VendaService vendaService;
	private List<Map<String, String>> vendas = new ArrayList<>();

	@FXML
	private Button buttonCadastrar;

	@FXML
	private Button buttonPesquisar;

	@FXML
	private TextField txfPesquisar;

	@FXML
	private TableView<Map<String, String>> tableViewVenda;

	@FXML
	private TableColumn<Map, String> tableColumnId;

	@FXML
	private TableColumn<Map, String> tableColumnNome;

	@FXML
	private TableColumn<Map, String> tableColumnValor;

	@FXML
	private TableColumn<Map, String> tableColumnQuantidade;

	@FXML
	private TableColumn<Map, String> tableColumnDescricao;
	
	@FXML
	private TableColumn<Map, String> tableColumnData;

	private ObservableList<Map<String, String>> obsList;

	@FXML
	public void onBtCadastrarAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		
		try {
			createDialogView("Carrinho","/gui/CarrinhoPage.fxml",parentStage, (CarrinhoPageController controller) -> {
				if(CarrinhoPageController.getCarrinho() != null) {
					Optional<ButtonType> escolha = Alerts.showConfirmation("Nova venda","Existe uma venda em curso, deseja continuala?" );
					if(escolha.get() == ButtonType.OK) {
						controller.onDataChanged();
					}else {
						CarrinhoPageController.setCarrinho(new Carrinho());
					}
				}else {
					CarrinhoPageController.setCarrinho(new Carrinho());
				}
				controller.setProdutoService(new ProdutoService());
				controller.updateComboBoxClientePesquisa();
				controller.updateComboBoxProdutoPesquisa();
			}
			);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@FXML
	public void onBtPesquisarAction() {
		List<Venda> vendas = new ArrayList<Venda>();
		String textoPesquisa = txfPesquisar.getText();
		if (!textoPesquisa.isBlank() && !textoPesquisa.isEmpty()) {
			try {
				if (textoPesquisa.matches("[0-9]*")) {
					vendas.add(vendaService.findById(textoPesquisa));

				} else {
					vendas = (vendaService.pesquisaPorNome(textoPesquisa));
				}
			} catch (IOException e) {
				Alerts.showAlert("Erro", "Erro ao pesquisar", e.getMessage(), AlertType.ERROR);
				e.printStackTrace();
			}
			updateTableView(vendas);
		} else {
			updateTableView();
		}

	}

	@FXML
	public void onBtTableLineAction(MouseEvent event) throws Exception {

		TableViewSelectionModel<Map<String, String>> tbv = tableViewVenda.getSelectionModel();

		int indice = tbv.getSelectedIndex();
		if (indice >= 0) {
			String id = tableViewVenda.getItems().get(indice).get("id");
			Venda venda = vendaService.findById(id);
			createDialogView("Venda", "/gui/VendaView.fxml", Utils.currentStage(event),
					(VendaViewController controller) -> {
						
						try {
							controller.setVenda(venda);
							controller.setVendaService(new VendaService());
							controller.updateDataForm();
							controller.subscribeDataListener(this);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					});
		}

		tbv.clearSelection();
	}

	public void setVendaService(VendaService service) {
		vendaService = service;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {

		tableColumnId.setCellValueFactory(new MapValueFactory<>("id"));
		tableColumnNome.setCellValueFactory(new MapValueFactory<>("nome"));
		tableColumnValor.setCellValueFactory(new MapValueFactory<>("valor"));
		tableColumnQuantidade.setCellValueFactory(new MapValueFactory<>("quantidade"));
		tableColumnData.setCellValueFactory(new MapValueFactory<>("data"));

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewVenda.prefHeightProperty().bind(stage.heightProperty());

	}

	public void updateTableView() {
		List<Venda> list;
		obsList = FXCollections.<Map<String, String>>observableArrayList();
		try {
			list = vendaService.findAll();
			for (Venda v : list) {
				Map<String, String> venda = new HashMap<>();
				venda.put("id", v.getId().toString());
				venda.put("nome", v.getCliente().getNome());

				venda.put("valor", v.getValor().toString());
				Pilha<Produto> aux = v.getProdutos().clonar();
				Integer itens = 0;
				while (!aux.isEmpty()) {
					itens += aux.pop().getQuantidade();
				}
				venda.put("quantidade", itens.toString());
				venda.put("data", v.getDataFormat());
				vendas.add(venda);

				obsList.add(venda);
			}

			tableViewVenda.setItems(obsList);

		} catch (Exception e) {
			Alerts.showAlert("Erro", "Erro ao conectar ao banco de dados", e.getMessage(), AlertType.ERROR);
			e.printStackTrace();
		}
	}

	public void updateTableView(List<Venda> list) {
		obsList = FXCollections.<Map<String, String>>observableArrayList();
		try {
			for (Venda v : list) {
				if(v != null) {
					Map<String, String> venda = new HashMap<>();
					venda.put("id", v.getId().toString());
					venda.put("nome", v.getCliente().getNome());
					venda.put("valor", v.getValor().toString());
					Pilha<Produto> aux = v.getProdutos().clonar();
					Integer itens = 0;
					while (!aux.isEmpty()) {
						itens += aux.pop().getQuantidade();
					}
					venda.put("quantidade", itens.toString());
					vendas.add(venda);

					obsList.add(venda);
				}
				
			}

			tableViewVenda.setItems(obsList);
			tableViewVenda.refresh();

		} catch (Exception e) {
			Alerts.showAlert("Erro", "Erro ao conectar ao banco de dados", e.getMessage(), AlertType.ERROR);
			e.printStackTrace();
		}

	}


	private <T> void createDialogView(String titulo, String absoluteName, Stage parentStage, Consumer<T> initializingAction)
			throws Exception {
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

	@Override
	public void onDataChanged() {
		updateTableView();
	}
}
