package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import model.entity.CategoriaProduto;
import model.services.CategoriaProdutoService;

public class CategoriaProdutoListController implements Initializable {

	private CategoriaProdutoService categoriaProdutoService;

	@FXML
	private ListView<String> listViewCategoriaProduto;

	private ObservableList<String> obsList;

	public void setCategoriaProdutoService(CategoriaProdutoService service) {
		categoriaProdutoService = service;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	/**
	 * Metodo de inicialização da tela, pega referencia de tamanho da tela principal
	 * e seta para a listView
	 */
	private void initializeNodes() {
		Stage stage = (Stage) Main.getMainScene().getWindow();
		listViewCategoriaProduto.prefHeightProperty().bind(stage.heightProperty());
	}

	/**
	 * Carrega os dados das categoria cadastradas na listView
	 */
	public void updateListView() {
		List<CategoriaProduto> list;
		try {
			list = categoriaProdutoService.findAll();
			List<String> items = new ArrayList<String>();

			for (CategoriaProduto cp : list) {
				StringBuffer sb = new StringBuffer();
				sb.append(cp.getNome());
				sb.append("\n" + cp.getProdutosToString());
				items.add(sb.toString());
			}
			obsList = FXCollections.observableArrayList(items);
			listViewCategoriaProduto.setItems(obsList);

		} catch (IOException e) {
			Alerts.showAlert("Erro", "Erro ao conectar ao banco de dados", e.getMessage(), AlertType.ERROR);
			e.printStackTrace();
		}
	}

}
