package gui;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

import csvConnection.Pilha;
import gui.listeners.DataChargeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;
import model.entity.Produto;
import model.entity.Venda;
import model.services.VendaService;

@SuppressWarnings("rawtypes")
public class VendaViewController implements Initializable {
	
	private Venda venda;
	private VendaService vendaService;

	private List<DataChargeListener> listeners = new ArrayList<>();
	
	@FXML
	private Label labelId;
	
	@FXML
	private Label labelNome;
	
	@FXML
	private Label labelValor;
	
	@FXML
	private Label labelData;
	
	@FXML
	private TableView<Map<String, String>>  tableView;
	
	@FXML
	private TableColumn<Map, String> tableColumnNome;

	@FXML
	private TableColumn<Map, String> tableColumnValor;

	@FXML
	private TableColumn<Map, String> tableColumnQuantidade;

	@FXML
	private TableColumn<Map, String> tableColumnTotal;

	@FXML
	private Button btnEditar;

	private ObservableList<Map<String, String>> obsList;
	

	@FXML
	public void onBtnExcluir(ActionEvent event) {
		Optional<ButtonType> escolha = Alerts.showConfirmation("Confirmar", "Deseja mesmo deletar o venda");
		if (escolha.get() == ButtonType.OK) {

			try {
				vendaService.remove(venda);
				notifyDataChangeListeners();
				Utils.currentStage(event).close();
			} catch (IOException e) {
				Alerts.showAlert("Erro", "Erro ao deletar o venda", e.getMessage(), AlertType.ERROR);
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
		initializeNodes();
	}

	private void initializeNodes() {


		tableColumnNome.setCellValueFactory(new MapValueFactory<>("nome"));
		tableColumnValor.setCellValueFactory(new MapValueFactory<>("valor"));
		tableColumnQuantidade.setCellValueFactory(new MapValueFactory<>("quantidade"));
		tableColumnTotal.setCellValueFactory(new MapValueFactory<>("total"));


	}
	
	public void updateDataForm() throws Exception {
		List<Produto> list = new ArrayList<>();
		String id = venda.getId().toString();
		String nome = venda.getCliente().getNome();
		String valor = venda.getValor().toString();
		String data = venda.getDataFormat();
	
		labelId.setText(id);
		labelNome.setText(nome);
		labelValor.setText(valor);
		labelData.setText(data);
		
		Pilha<Produto> aux = venda.getProdutos().clonar();
		while(!aux.isEmpty()) {
			list.add(aux.pop());
		}
		
		obsList = FXCollections.<Map<String, String>>observableArrayList();
		try {
		
			for (Produto p : list) {
				Map<String, String> produto = new HashMap<>();
				produto.put("nome", p.getNome());

				produto.put("valor", p.getValor().toString());
			
				produto.put("quantidade", p.getQuantidade().toString());
				produto.put("total",p.getValor() * p.getQuantidade() +"");

				obsList.add(produto);
			}

			tableView.setItems(obsList);

		} catch (Exception e) {
			Alerts.showAlert("Erro", "Erro ao conectar ao banco de dados", e.getMessage(), AlertType.ERROR);
			e.printStackTrace();
		}


	}
	
	public void setVendaService(VendaService service) {
		vendaService = service;
	}
	
	public void setVenda(Venda venda) {
		this.venda = venda;
	}

}
