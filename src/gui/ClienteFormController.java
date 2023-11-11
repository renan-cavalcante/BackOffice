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
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import model.entity.Cliente;
import model.entity.Endereco;
import model.exception.ValidationException;
import model.services.ClienteService;

public class ClienteFormController implements Initializable {

	private Cliente cliente;
	private ClienteService service;
	private boolean novo;
	private List<DataChargeListener> listeners = new ArrayList<>();

	@FXML
	private RadioButton rbtPessoaFisica;

	@FXML
	private RadioButton rbtEmpresa;

	@FXML
	private Label lbCpfCnpj;

	@FXML
	private Label lbContato;

	@FXML
	private Label lbNome;

	@FXML
	private TextField txtCpfCnpj;

	@FXML
	private TextField txtNome;

	@FXML
	private TextField txtRua;

	@FXML
	private TextField txtNumero;

	@FXML
	private TextField txtComplemento;

	@FXML
	private TextField txtCep;

	@FXML
	private TextField txtContato;

	@FXML
	private TextField txtEmail;

	@FXML
	private Label lbErrorCpfCnpj;

	@FXML
	private Label lbErrorNome;

	@FXML
	private Label lbErrorRua;

	@FXML
	private Label lbErrorCep;

	@FXML
	private Label lbErrorContato;
	
	@FXML
	private Label lbErrorEmail;

	@FXML
	private Button btSalvar;

	@FXML
	private Button btcancelar;

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public void setClienteService(ClienteService service) {
		this.service = service;
	}

	public void subscribeDataListener(DataChargeListener listener) {
		listeners.add(listener);
	}

	@FXML
	public void onBtSalvarAction(ActionEvent event) {
		try {
			clearErro();
			cliente = getFormData();
			if(novo) {
				cliente = service.save(cliente);
			}else {
				cliente = service.update(cliente);
			}
			
			
			notifyDataChangeListeners();
			Utils.currentStage(event).close();

		} catch (IOException e) {
			Alerts.showAlert("Erro ao Salvar", "Erro ao salvar o cliente", e.getMessage(), AlertType.ERROR);
			e.printStackTrace();
		} catch (ValidationException e) {
			setMessagemErro(e.getErros());
		}
	}

	private void notifyDataChangeListeners() {
		for (DataChargeListener listener : listeners) {
			if (listener instanceof CarrinhoPageController) {
				CarrinhoPageController.setCliente(cliente);	
			}
			listener.onDataChanged();

		}
	}

	@FXML
	public void onBtCancelarAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	@FXML
	public void onRbtPessoaAction() {
		rbtEmpresa.setSelected(false);
		lbCpfCnpj.setText("CPF");
		txtEmail.setDisable(true);
		lbContato.setText("Celular");
		lbNome.setText("Nome");
		Constraints.mascaraCPF(txtCpfCnpj);
		Constraints.mascaraCelular(txtContato);
		Constraints.tamanhoCpf = 14;
	}

	@FXML
	public void onRbtEmpresaAction() {
		rbtPessoaFisica.setSelected(false);
		lbCpfCnpj.setText("CNPJ");
		txtEmail.setDisable(false);
		lbContato.setText("Telefone");
		lbNome.setText("Empresa");
		Constraints.mascaraCNPJ(txtCpfCnpj);
		Constraints.mascaraTelefone(txtContato);
		Constraints.tamanhoCpf = 18;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNode();
	}

	private void initializeNode() {
		Constraints.setTextFieldMaxLength(txtNome, 60);
		Constraints.setTextFieldMaxLength(txtNumero, 8);
		Constraints.setTextFieldMaxLength(txtComplemento, 20);
		Constraints.setTextFieldString(txtNome);
		Constraints.mascaraCEP(txtCep);
		Constraints.mascaraCelular(txtContato);
		Constraints.mascaraCPF(txtCpfCnpj);
		Constraints.tamanhoCpf = 14;
		Constraints.setTextFieldMaxLengthCPF(txtCpfCnpj);
	}

	public void updateDataForm() {
		String cpfCnpj = cliente.getId() == null ? "" : cliente.getId().toString();
		String nome = cliente.getNome() == null ? "" : cliente.getNome();
		String rua = cliente.getEndereco() == null ? "" : cliente.getEndereco().getLougradouro();
		String numero = cliente.getEndereco() == null ? "" : cliente.getEndereco().getNumero();
		String complemento = cliente.getEndereco() == null ? "" : cliente.getEndereco().getComplemento();
		String cep = cliente.getEndereco() == null ? "" : cliente.getEndereco().getCep();
		String contato = cliente.getContato() == null ? "" : cliente.getContato();
		String email = cliente.getEmail() == null ? "" : cliente.getEmail();

		txtCpfCnpj.setText(cpfCnpj);
		txtNome.setText(nome);
		txtRua.setText(rua);
		txtNumero.setText(numero);
		txtComplemento.setText(complemento);
		txtCep.setText(cep);
		txtContato.setText(contato);
		txtEmail.setText(email);

	}

	private Cliente getFormData() {
		Cliente cliente = new Cliente();
		Endereco endereco = new Endereco();

		ValidationException exception = new ValidationException("Validação erros");
		validacao(exception);

		cliente.setId(txtCpfCnpj.getText());
		cliente.setNome(txtNome.getText());
		endereco.setLougradouro(txtRua.getText());
		endereco.setNumero(txtNumero.getText());
		endereco.setComplemento(txtComplemento.getText());
		endereco.setCep(txtCep.getText());
		cliente.setEndereco(endereco);
		cliente.setContato(txtContato.getText());
		cliente.setEmail(txtEmail.getText());

		if (exception.getErros().size() > 0) {
			throw exception;
			
		}
		return cliente;
	}

	private void validacao(ValidationException ve) {
		if (rbtPessoaFisica.isSelected()) {
			validarTamanho(txtCpfCnpj, ve, "cpf", 14);
			validarTamanho(txtContato, ve, "celular", 14);
		}

		
		
		if (rbtEmpresa.isSelected()) {
			validarVazio(txtEmail, ve, "email");
			validarTamanho(txtContato, ve, "telefone", 13);
			validarTamanho(txtCpfCnpj, ve, "cnpj", 18);
		}
		

		validarVazio(txtNome, ve, "nome");
		validarVazio(txtRua, ve, "rua");
		validarTamanho(txtCep, ve, "cep", 9);		
		
	}

	private void validarVazio(TextField txt, ValidationException ve, String nome) {
		if (txt.getText() == null || txt.getText().trim().equals("")) {
			ve.addErro(nome, nome + " não pode ser vazio");
		}
	}
	
	private void validarTamanho(TextField txt, ValidationException ve, String nome, int tamanho) {
		if (txt.getText().length() < tamanho) {
			ve.addErro(nome, nome + " invalido");
		}
	}

	private void setMessagemErro(Map<String, String> erros) {
		Set<String> fields = erros.keySet();

		if (fields.contains("cpf")) {
			lbErrorCpfCnpj.setText(erros.get("cpf"));
		}
		if (fields.contains("cnpj")) {
			lbErrorCpfCnpj.setText(erros.get("cnpj"));
		}
		if (fields.contains("celular")) {
			lbErrorContato.setText(erros.get("celular"));
		}
		if (fields.contains("telefone")) {
			lbErrorContato.setText(erros.get("telefone"));
		}
		if (fields.contains("nome")) {
			lbErrorNome.setText(erros.get("nome"));
		}
		if (fields.contains("rua")) {
			lbErrorRua.setText(erros.get("rua"));
		}
		if (fields.contains("cep")) {
			lbErrorCep.setText(erros.get("cep"));
		}
		if (fields.contains("email")) {
			lbErrorEmail.setText(erros.get("email"));
		}

	}
	
	public void bloquearRadioButton() {
		rbtEmpresa.setDisable(true);
		rbtPessoaFisica.setDisable(true);
	}
	
	public void bloquearCpfCnpj() {
		txtCpfCnpj.setDisable(true);
		txtCpfCnpj.setEditable(false);
	}
	
	private void clearErro() {
		lbErrorContato.setText("");
		lbErrorCpfCnpj.setText("");
		lbErrorEmail.setText("");
		lbErrorNome.setText("");
		lbErrorRua.setText("");
		lbErrorCep.setText("");
	}

	public boolean isNovo() {
		return novo;
	}

	public void setNovo(boolean novo) {
		this.novo = novo;
	}

}
