package gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import application.Main;
import gui.listeners.DataChargeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.entity.CategoriaProduto;
import model.exception.ValidationException;
import model.services.CategoriaProdutoService;

public class CategoriaProdutoListController implements Initializable {

	private CategoriaProdutoService categoriaProdutoService;

	@FXML
	private ListView<String> listViewCategoriaProduto;



	private ObservableList<String> obsList;

//	@FXML
//	public void onBtPesquisarAction() {
//		List<CategoriaProduto> categoriaProdutos = new ArrayList<CategoriaProduto>();
//		String textoPesquisa = txfPesquisar.getText();
//		if (textoPesquisa != null) {
//			try {
//				if (textoPesquisa.matches("[0-9]*")) {
//					categoriaProdutos.add(categoriaProdutoService.findById(textoPesquisa));
//
//				} else {
//					categoriaProdutos = (categoriaProdutoService.pesquisaPorNome(textoPesquisa));
//				}
//			} catch (IOException e) {
//				Alerts.showAlert("Erro", "Erro ao pesquisar", e.getMessage(), AlertType.ERROR);
//				e.printStackTrace();
//			}
//		}
//		updateTableView(categoriaProdutos);
//
//	}

//	@FXML
//	public void onBtTableLineAction(MouseEvent event) {
//
//		ListViewSelectionModel<CategoriaProduto> tbv = listViewCategoriaProduto.getSelectionModel();
//
//		int indice = tbv.getSelectedIndex();
//		if (indice >= 0) {
//			CategoriaProduto categoria = (CategoriaProduto) tableViewCategoriaProduto.getItems().get(indice);
//			createDialogView(categoria, "/gui/categoriaProdutoView.fxml", Utils.currentStage(event),
//					(CategoriaProdutoViewController controller) -> {
//					});
//		}
//
//		tbv.clearSelection();
//	}

	public void setCategoriaProdutoService(CategoriaProdutoService service) {
		categoriaProdutoService = service;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {

	
		Stage stage = (Stage) Main.getMainScene().getWindow();
		listViewCategoriaProduto.prefHeightProperty().bind(stage.heightProperty());

	}

	public void updateListView() {
		List<CategoriaProduto> list;
		try {
			list = categoriaProdutoService.findAll();
			List<String> items = new ArrayList<String>();
			
			for(CategoriaProduto cp : list) {
				StringBuffer sb = new StringBuffer();
				sb.append(cp.getNome());
				sb.append("\n"+cp.getProdutosToString());
				items.add(sb.toString());
			}
			obsList = FXCollections.observableArrayList(items);
			listViewCategoriaProduto.setItems(obsList);


		} catch (IOException e) {
			Alerts.showAlert("Erro", "Erro ao conectar ao banco de dados", e.getMessage(), AlertType.ERROR);
			e.printStackTrace();
		}
	}


	



//	@SuppressWarnings({ "unchecked" })
//	private <T> void createDialogView(CategoriaProduto obj, String absoluteName, Stage parentStage,
//			Consumer<T> initializingAction) {
//		try {
//			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
//			Pane pane = loader.load();
//
//			CategoriaProdutoViewController controller = loader.getController();
//
//			controller.setCategoriaProduto(obj);
//			controller.setCategoriaProdutoService(new CategoriaProdutoService());
//			controller.updateDataForm();
//			controller.subscribeDataListener(this);
//
//			initializingAction.accept((T) controller);
//
//			Stage dialogStage = new Stage();
//			dialogStage.setTitle("Categoria Produtos");
//			dialogStage.setScene(new Scene(pane));
//			dialogStage.setResizable(false);
//			dialogStage.initOwner(parentStage);
//			dialogStage.initModality(Modality.WINDOW_MODAL);
//			dialogStage.showAndWait();
//		} catch (IOException e) {
//			Alerts.showAlert("IO Excpetion", "Erro carregando view", e.getMessage(), AlertType.ERROR);
//		}
//	}


}
