package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import gui.listeners.DataChargeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.entity.CategoriaProduto;
import model.exception.ValidationException;
import model.services.CategoriaProdutoService;

public class CategoriaProdutoFormController implements Initializable {

	private CategoriaProduto categoriaProduto;

	private CategoriaProdutoService service;

	private List<DataChargeListener> listeners = new ArrayList<>();


	@FXML
	private TextField txtId;

	@FXML
	private TextField txtNome;

	@FXML
	private TextArea txtAreaDescricao;
	
	@FXML private Label lbErroNome;
	@FXML private Label lbErroDescricao;

	@FXML
	private Button btSalvar;

	@FXML
	private Button btcancelar;

	public void setCategoriaProduto(CategoriaProduto categoriaProduto) {
		this.categoriaProduto = categoriaProduto;
	}

	public void setCategoriaProdutoService(CategoriaProdutoService service) {
		this.service = service;
	}

	public void subscribeDataListener(DataChargeListener listener) {
		listeners.add(listener);
	}

	@FXML
	public void onBtSalvarAction(ActionEvent event) {
		try {
			categoriaProduto = getFormData();
			service.saveOrUpdate(categoriaProduto);
			notifyDataChangeListeners();
			Utils.currentStage(event).close();

		} catch (IOException e) {
			Alerts.showAlert("Erro ao Salvar", "Erro ao salvar a categoriaProduto", e.getMessage(), AlertType.ERROR);
			e.printStackTrace();
		} catch (ValidationException e) {
			setMessagemErro(e.getErros());
		}
	}

	private void notifyDataChangeListeners() {
		for (DataChargeListener listener : listeners) {
			listener.onDataChanged();
		}
	}

	@FXML
	public void onBtCancelarAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNode();
	}

	private void initializeNode() {
//		Constraints.setTextFieldMaxLength(txtNome, 60);
//		Constraints.setTextFieldString(txtNome);
//		Constraints.mascaraCEP(txtCep);
//		Constraints.mascaraCelular(txtContato);
//		Constraints.mascaraCPF(txtCpfCnpj);
//		Constraints.tamanhoCpf = 14;
//		Constraints.setTextFieldMaxLengthCPF(txtCpfCnpj);
	}

	public void updateDataForm() {
		String id = categoriaProduto.getId() == null ? "" : categoriaProduto.getId().toString();
		String nome = categoriaProduto.getNome() == null ? "" : categoriaProduto.getNome();
		String descricao = categoriaProduto.getDescricao() == null ? "" : categoriaProduto.getDescricao();

		txtId.setText(id);
		txtNome.setText(nome);
		txtAreaDescricao.setText(descricao);


	}

	private CategoriaProduto getFormData() {
		CategoriaProduto categoriaProduto = new CategoriaProduto();

		ValidationException exception = new ValidationException("Validação erros");
		validacao(exception);

		categoriaProduto.setId(Utils.tryParseLong(txtId.getText()));
		categoriaProduto.setNome(txtNome.getText());
		categoriaProduto.setDescricao(txtAreaDescricao.getText());
	

		if (exception.getErros().size() > 0) {
			throw exception;
		}
		return categoriaProduto;
	}

	private void validacao(ValidationException ve) {

		validarVazio(txtNome, ve, "nome");
		if (txtAreaDescricao.getText() == null || txtAreaDescricao.getText().trim().equals("")) {
			ve.addErro("descricao", "Descrição não pode ser vazia");
		}
		
	}

	private void validarVazio(TextField txt, ValidationException ve, String nome) {
		if (txt.getText() == null || txt.getText().trim().equals("")) {
			ve.addErro(nome, nome + " não pode ser vazio");
		}
	}
	

	private void setMessagemErro(Map<String, String> erros) {
		Set<String> fields = erros.keySet();

		if (fields.contains("nome")) {
			lbErroNome.setText(erros.get("nome"));
		}
		if (fields.contains("descricao")) {
			lbErroDescricao.setText(erros.get("descricao"));
		}


	}
}
