package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

public class ClienteFormController implements Initializable {

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
	private TextField txtcomplemento ;
	   	
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
	
	@FXML
	public void onBtSalvarAction() {
		
	}
	
	@FXML
	public void onBtCancelarAction() {
		
	}
	
	@FXML
	public void onRbtPessoaAction() {
		rbtEmpresa.setSelected(false);
		lbCpfCnpj.setText("CPF");
		txtEmail.setDisable(true);
		lbContato.setText("Celular");
		lbNome.setText("Nome");
		Constraints.setTextFieldMaxLength(txtCpfCnpj, 11);
	}
	
	@FXML
	public void onRbtEmpresaAction() {
		rbtPessoaFisica.setSelected(false);
		lbCpfCnpj.setText("CNPJ");
		txtEmail.setDisable(false);
		lbContato.setText("Telefone");
		lbNome.setText("Empresa");
		Constraints.setTextFieldMaxLength(txtCpfCnpj, 14);
	}

	@Override
	public void initialize(URL url , ResourceBundle rb) {
		initializeNode();
		
		
	}
	
	private void initializeNode() {
		Constraints.setTextFieldInteger(txtCpfCnpj);
		Constraints.setTextFieldMaxLength(txtNome, 60);
		Constraints.setTextFieldString(txtNome);
		onRbtPessoaAction();
	}

}
