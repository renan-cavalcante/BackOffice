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
	private ObservableList<CategoriaProduto> obsList;

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

	@FXML
	private TextArea txtAreaDescricao;

	@FXML
	private Label lbErroNome;
	@FXML
	private Label lbErroDescricao;

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

	/**
	 * metodo para class se inscrever no listener
	 * 
	 * @param listener: class a se inscrista
	 */
	public void subscribeDataListener(DataChargeListener listener) {
		listeners.add(listener);
	}

	/**
	 * Salva ou atualiza o produto do form
	 * 
	 * @param event
	 * @throws IOException
	 * @throws ValidationException
	 */
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

	/**
	 * Avisa as class inscrita no listener que ouve atualização
	 */
	private void notifyDataChangeListeners() {
		for (DataChargeListener listener : listeners) {
			listener.onDataChanged();
		}
	}

	/**
	 * Fecha o form sem salvar os dados
	 * 
	 * @param event
	 */
	@FXML
	public void onBtCancelarAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	/**
	 * Carrega o comboBox com as CategoriasProduto salvos
	 */
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
	}


	/**
	 * Atualiza os campos do form com os dados do Produto selecionado
	 */
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

	/**
	 * Inicia um produto com os dados do form
	 * 
	 * @return Produto
	 */
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

	/**
	 * Valida os dados inseridos no forms de acordo com a regra de negocio
	 * 
	 * @param ve: map de exceptions com os erros de validações
	 */
	private void validacao(ValidationException ve) {

		validarVazio(txtNome, ve, "nome");
		if (txtAreaDescricao.getText() == null || txtAreaDescricao.getText().trim().equals("")) {
			ve.addErro("descricao", "Descrição não pode ser vazia");
		}

	}

	/**
	 * Verifica se o textField esta vazio, e adiciona ao map de exceptions
	 * 
	 * @param txt:  TextFild a ser validado
	 * @param ve:   map de exceptions com os erros de validações
	 * @param nome: nome do campo do textfield
	 */
	private void validarVazio(TextField txt, ValidationException ve, String nome) {
		if (txt.getText() == null || txt.getText().trim().equals("")) {
			ve.addErro(nome, nome + " não pode ser vazio");
		}
	}

	/**
	 * Exibe as mensagens de erro para os campos que não atende a validação
	 * 
	 * @param Map dos erros encontrados na validação
	 */
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
