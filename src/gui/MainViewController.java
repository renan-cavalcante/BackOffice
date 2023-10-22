package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
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
		loadView("/gui/ClienteView.fxml");
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
	
	private synchronized void loadView(String absoluteName) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVbox = loader.load();
			
			Scene mainScene = Main.getMainScene();
			VBox mainVbox =   (VBox)((ScrollPane) mainScene.getRoot()).getContent();
			
			Node mainMenu = mainVbox.getChildren().get(0);
			mainVbox.getChildren().clear();
			mainVbox.getChildren().add(mainMenu);
			mainVbox.getChildren().addAll(newVbox.getChildren());
			
			ClienteViewController controller = loader.getController();
			controller.setClienteService(new Clienteservice());
			controller.updateTableView();
		}
		catch(IOException e) {
			
		}
	}

}
