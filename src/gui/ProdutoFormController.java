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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.entity.CategoriaProduto;
import model.entity.Produto;
import model.exception.ValidationException;
import model.services.CategoriaProdutoService;
import model.services.ProdutoService;

public class ProdutoFormController implements Initializable {

	private Produto produto;

	private ProdutoService service;
	private CategoriaProdutoService categoriaService;

	private List<DataChargeListener> listeners = new ArrayList<>();


	@FXML
	private TextField txtId;

	@FXML
	private TextField txtNome;
	
	@FXML
	private TextField txtValor;

	@FXML
	private TextField txtQuantidade;

	@FXML
	private ComboBox<CategoriaProduto> cbxCategoria;
	
	private ObservableList<CategoriaProduto> obsList;
	

	@FXML
	private TextArea txtAreaDescricao;
	
	@FXML private Label lbErroNome;
	@FXML private Label lbErroDescricao;

	@FXML
	private Button btSalvar;

	@FXML
	private Button btcancelar;

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public void setProdutoService(ProdutoService service) {
		this.service = service;
	}
	
	public void setCategoriaProdutoService(CategoriaProdutoService service) {
		this.categoriaService = service;
	}

	public void subscribeDataListener(DataChargeListener listener) {
		listeners.add(listener);
	}

	@FXML
	public void onBtSalvarAction(ActionEvent event) {
		try {
			produto = getFormData();
			service.saveOrUpdate(produto);
			notifyDataChangeListeners();
			Utils.currentStage(event).close();

		} catch (IOException e) {
			Alerts.showAlert("Erro ao Salvar", "Erro ao salvar a produto", e.getMessage(), AlertType.ERROR);
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
	
	public void updateComboBoxView() {
		List<CategoriaProduto> list;
		try {
			list = categoriaService.findAll();
			obsList = FXCollections.observableArrayList(list);
			cbxCategoria.setItems(obsList);

		} catch (IOException e) {
			Alerts.showAlert("Erro", "Erro ao conectar ao banco de dados", e.getMessage(), AlertType.ERROR);
			e.printStackTrace();
		}
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
		String id = produto.getId() == null ? "" : produto.getId().toString();
		String nome = produto.getNome() == null ? "" : produto.getNome();
		String valor = produto.getValor() == null ? "" : produto.getValor().toString();
		String quantidade = produto.getQuantidade() == null ? "" : produto.getQuantidade().toString();
		String descricao = produto.getDescricao() == null ? "" : produto.getDescricao();
		CategoriaProduto categoria = produto.getCategoria();

		txtId.setText(id);
		txtNome.setText(nome);
		txtValor.setText(valor);
		txtQuantidade.setText(quantidade);
		cbxCategoria.setValue(categoria);
		txtAreaDescricao.setText(descricao);


	}

	private Produto getFormData() {
		Produto produto = new Produto();
		CategoriaProduto categoriaProduto = cbxCategoria.getValue();

		ValidationException exception = new ValidationException("Validação erros");
		validacao(exception);

		produto.setId(Utils.tryParseLong(txtId.getText()));
		produto.setNome(txtNome.getText());
		produto.setValor(Utils.tryParseDouble(txtValor.getText()));
		produto.setQuantidade(Utils.tryParseInt(txtQuantidade.getText()));
		produto.setDescricao(txtAreaDescricao.getText());
		produto.setCategoria(categoriaProduto);
	

		if (exception.getErros().size() > 0) {
			throw exception;
		}
		return produto;
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
