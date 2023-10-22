package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.Clienteservice;

public class MainViewController implements Initializable {

	@FXML
	private MenuItem menuCliente;
	
	@FXML
	private MenuItem menuItemProdutos;
	
	@FXML
	private MenuItem menuItemCategorias;
	
	@FXML
	private MenuItem menuLogin;
	
	@FXML
	public void onMenuClienteAction() {
		loadView("/gui/ClienteView.fxml", (ClienteViewController controller) -> {
			controller.setClienteService(new Clienteservice());
			controller.updateTableView();
		});
	}
	
	@FXML
	public void onMenuItemProdutosAction() {
		
	}
	
	@FXML
	public void onMenuItemCategoriaAction() {
		
	}
	
	@FXML
	public void onMenuItemLoginAction() {
		
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO Auto-generated method stub
		
	}
	
	private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVbox = loader.load();
			
			Scene mainScene = Main.getMainScene();
			VBox mainVbox =   (VBox)((ScrollPane) mainScene.getRoot()).getContent();
			
			Node mainMenu = mainVbox.getChildren().get(0);
			mainVbox.getChildren().clear();
			mainVbox.getChildren().add(mainMenu);
			mainVbox.getChildren().addAll(newVbox.getChildren());
			
			T controller = loader.getController();
			initializingAction.accept(controller);
		}
		catch(IOException e) {
			Alerts.showAlert("IO Exception", "Erro ao carregar View", e.getMessage(), AlertType.ERROR);
		}
	}

}
