package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import gui.listeners.DataChargeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.entity.Cliente;
import model.services.ClienteService;

public class ClienteViewController implements Initializable, DataChargeListener {

	private Cliente cliente;
	private ClienteService clienteService;
	private List<DataChargeListener> listeners = new ArrayList<>();

	@FXML
	private Label labelId;

	@FXML
	private Label labelNome;

	@FXML
	private Label labelEndereco;

	@FXML
	private Label labelContato;

	@FXML
	private Label labelEmail;

	@FXML
	private Button btnEditar;

	@FXML
	private Button btnExcluir;

	@FXML
	public void onBtnEditar(ActionEvent event) {
		createDialogForm(cliente, "/gui/ClienteForm.fxml", Utils.currentStage(event),
				(ClienteFormController controller) -> {
					controller.bloquearRadioButton();
					controller.bloquearCpfCnpj();
					controller.subscribeDataListener(this);
					controller.setNovo(false);
				});
	}

	@FXML
	public void onBtnExcluir(ActionEvent event) {
		Optional<ButtonType> escolha = Alerts.showConfirmation("Confirmar", "Deseja mesmo deletar o cliente");
		if (escolha.get() == ButtonType.OK) {

			try {
				clienteService.remove(cliente);
				notifyDataChangeListeners();
				Utils.currentStage(event).close();
			} catch (IOException e) {
				Alerts.showAlert("Erro", "Erro ao deletar o cliente", e.getMessage(), AlertType.ERROR);
				e.printStackTrace();
			}
		}
	}

	private void notifyDataChangeListeners() {
		for (DataChargeListener listener : listeners) {
			listener.onDataChanged();
		}
	}

	public void subscribeDataListener(DataChargeListener listener) {
		listeners.add(listener);
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO Auto-generated method stub

	}

	public void updateDataForm() {
		String id = cliente.getId().toString();
		String nome = cliente.getNome();
		String endereco = cliente.getEndereco().toString();
		String contato = cliente.getContato();
		String email = cliente.getEmail();

		labelId.setText(id);
		labelNome.setText(nome);
		labelEndereco.setText(endereco);
		labelContato.setText(contato);
		labelEmail.setText(email);

	}

	public void setClienteService(ClienteService service) {
		clienteService = service;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	@SuppressWarnings({ "unchecked" })
	private <T> void createDialogForm(Cliente obj, String absoluteName, Stage parentStage,
			Consumer<T> initializingAction) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			ClienteFormController controller = loader.getController();

			controller.setCliente(obj);
			controller.setClienteService(new ClienteService());
			controller.updateDataForm();

			initializingAction.accept((T) controller);

			Stage dialogStage = new Stage();
			dialogStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

				@Override
				public void handle(WindowEvent t) {
					t.consume();
					updateDataForm();
					dialogStage.close();

				}
			});
			dialogStage.setTitle("Editar Categoria");
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
		try {
			cliente = clienteService.findById(cliente.getId());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		updateDataForm();
		
	}
}
