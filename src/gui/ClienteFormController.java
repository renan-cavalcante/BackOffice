package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

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
import model.services.ClienteService;

public class ClienteFormController implements Initializable {
	
	private Cliente cliente;
	
	private ClienteService service;
	
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
 	private TextField txtCpfCnpj ;

	@FXML
	private TextField txtNome ;
	   	
	@FXML
	private TextField txtRua ;
	
	@FXML
	private TextField txtNumero ;
	   	
	@FXML
	private TextField txtComplemento ;
	   	
	@FXML
	private TextField txtCep ;
	   	
	@FXML
  	private TextField txtContato ;
	
	@FXML
	private TextField txtEmail;
	
	@FXML
	private Label lbErrorCpfCnpj ;
	
	@FXML
	private Label lbErrorNome ;
	
	@FXML
	private Label lbErrorRua ;
	
	@FXML
	private Label lbErrorCep ;
	
	@FXML
	private Label lbErrorContato ;
	
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
			cliente = getFormData();
			service.saveOrUpdate(cliente);
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
			
		} catch (IOException e) {
			Alerts.showAlert("Erro ao Salvar", "Erro ao salvar o cliente", e.getMessage(), AlertType.ERROR);
			e.printStackTrace();
		}
	}
	

	private void notifyDataChangeListeners() {
		for(DataChargeListener listener: listeners) {
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
	public void initialize(URL url , ResourceBundle rb) {
		initializeNode();		
	}
	
	private void initializeNode() {
		Constraints.setTextFieldMaxLength(txtNome, 60);
		Constraints.setTextFieldString(txtNome);
		Constraints.mascaraCEP(txtCep);
		Constraints.mascaraCelular(txtContato);
		Constraints.mascaraCPF(txtCpfCnpj);
		Constraints.tamanhoCpf = 14;
		Constraints.setTextFieldMaxLengthCPF(txtCpfCnpj);
	}
	
	public void updateDataForm() {
		String cpfCnpj = cliente.getId() == null ? "": cliente.getId().toString();
		String nome = cliente.getNome() == null ? "" : cliente.getNome();
		String rua = cliente.getEndereco() == null ? "" : cliente.getEndereco().getLougradouro();
		String numero = cliente.getEndereco() == null ? "" : cliente.getEndereco().getNumero() ;
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
		
		cliente.setId(Utils.tryParseLong(txtCpfCnpj.getText()));
		cliente.setNome(txtNome.getText());
		endereco.setLougradouro(txtRua.getText());
		endereco.setNumero(txtNumero.getText());
		endereco.setComplemento(txtComplemento.getText());
		endereco.setCep(txtCep.getText());
		cliente.setEndereco(endereco);
		cliente.setContato(txtContato.getText());
		cliente.setEmail(txtEmail.getText());
		return cliente;
	}
	


}
