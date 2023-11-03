package gui.util;

import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import model.entity.Cliente;
import model.entity.Produto;

public class Constraints {

	public static int tamanhoCpf;
	private static ObservableList<Cliente> obsList2;
	private static ObservableList<Produto> obsListProduto;
	
	public static void pesquisa(ComboBox<Cliente> cmb, ObservableList<Cliente> obsList) {
		
		cmb.getEditor().setOnKeyTyped((KeyEvent event) -> {
			cmb.hide();
			
			 if (!cmb.getEditor().getText().matches("\\d.*")) {
				 obsList2 = FXCollections.observableArrayList(obsList.stream().filter(
							cliente -> cliente.getNome().toLowerCase().contains(cmb.getEditor().getText().toLowerCase()))
							.collect(Collectors.toList()));
			 }else {
				 obsList2 = FXCollections.observableArrayList(obsList.stream().filter(
							cliente -> cliente.getId().contains(cmb.getEditor().getText()))
							.collect(Collectors.toList()));
			 }
			
			
			cmb.setItems(obsList2);
			cmb.show();
		});
	}


	public static void setTextFieldInteger(TextField txt) {
		txt.textProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue != null && !newValue.matches("\\d*")) {
				txt.setText(oldValue);
			}
		});
	}

	public static void setTextFieldMaxLength(TextField txt, int max) {
		txt.textProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue != null && newValue.length() > max) {
				txt.setText(oldValue);
			}
		});
	}

	public static void setTextFieldMaxLengthCPF(TextField txt) {
		txt.textProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue != null && newValue.length() > tamanhoCpf) {
				txt.setText(oldValue);
			}
		});
	}

	public static void setTextFieldDouble(TextField txt) {
		txt.textProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue != null && !newValue.matches("\\d*([\\,]\\d*)?")) {
				txt.setText(oldValue);
			}
		});
	}

	public static void setTextFieldString(TextField txt) {
		txt.textProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue != null && !newValue.matches("^(([a-zA-Z ]|[é])*)$")) {
				txt.setText(oldValue);
			}
		});
	}

	public static void mascaraCEP(TextField textField) {
		setTextFieldMaxLength(textField, 9);

		textField.setOnKeyTyped((KeyEvent event) -> {
			if ("0123456789".contains(event.getCharacter()) == false) {
				event.consume();
			}

			if (event.getCharacter().trim().length() == 0) { // apagando

				if (textField.getText().length() == 6) {
					textField.setText(textField.getText().substring(0, 5));
					textField.positionCaret(textField.getText().length());
				}

			} else { // escrevendo

				if (textField.getText().length() == 9)
					event.consume();

				if (textField.getText().length() == 5) {
					textField.setText(textField.getText() + "-");
					textField.positionCaret(textField.getText().length());
				}

			}
		});

		textField.setOnKeyReleased((KeyEvent evt) -> {

			if (!textField.getText().matches("\\d-*")) {
				textField.setText(textField.getText().replaceAll("[^\\d-]", ""));
				textField.positionCaret(textField.getText().length());
			}
		});

	}

	public static void mascaraCPF(TextField textField) {

		textField.setOnKeyTyped((KeyEvent event) -> {
			if ("0123456789".contains(event.getCharacter()) == false) {
				event.consume();
			}

			if (event.getCharacter().trim().length() == 0) { // apagando

				if (textField.getText().length() == 4) {
					textField.setText(textField.getText().substring(0, 3));
					textField.positionCaret(textField.getText().length());
				}
				if (textField.getText().length() == 8) {
					textField.setText(textField.getText().substring(0, 7));
					textField.positionCaret(textField.getText().length());
				}
				if (textField.getText().length() == 12) {
					textField.setText(textField.getText().substring(0, 11));
					textField.positionCaret(textField.getText().length());
				}

			} else { // escrevendo

				if (textField.getText().length() == 14)
					event.consume();

				if (textField.getText().length() == 3) {
					textField.setText(textField.getText() + ".");
					textField.positionCaret(textField.getText().length());
				}
				if (textField.getText().length() == 7) {
					textField.setText(textField.getText() + ".");
					textField.positionCaret(textField.getText().length());
				}
				if (textField.getText().length() == 11) {
					textField.setText(textField.getText() + "-");
					textField.positionCaret(textField.getText().length());
				}

			}
		});

		textField.setOnKeyReleased((KeyEvent evt) -> {

			if (!textField.getText().matches("\\d.-*")) {
				textField.setText(textField.getText().replaceAll("[^\\d.-]", ""));
				textField.positionCaret(textField.getText().length());
			}
		});

	}


	public static void mascaraCNPJ(TextField textField) {

		tamanhoCpf = 18;
		textField.setOnKeyTyped((KeyEvent event) -> {
			if ("0123456789".contains(event.getCharacter()) == false) {
				event.consume();
			}

			if (event.getCharacter().trim().length() == 0) { // apagando

				if (textField.getText().length() == 3) {
					textField.setText(textField.getText().substring(0, 2));
					textField.positionCaret(textField.getText().length());
				}
				if (textField.getText().length() == 7) {
					textField.setText(textField.getText().substring(0, 6));
					textField.positionCaret(textField.getText().length());
				}
				if (textField.getText().length() == 11) {
					textField.setText(textField.getText().substring(0, 10));
					textField.positionCaret(textField.getText().length());
				}
				if (textField.getText().length() == 16) {
					textField.setText(textField.getText().substring(0, 15));
					textField.positionCaret(textField.getText().length());
				}

			} else { // escrevendo

				if (textField.getText().length() == 18)
					event.consume();

				if (textField.getText().length() == 2) {
					textField.setText(textField.getText() + ".");
					textField.positionCaret(textField.getText().length());
				}
				if (textField.getText().length() == 6) {
					textField.setText(textField.getText() + ".");
					textField.positionCaret(textField.getText().length());
				}
				if (textField.getText().length() == 10) {
					textField.setText(textField.getText() + "/");
					textField.positionCaret(textField.getText().length());
				}
				if (textField.getText().length() == 15) {
					textField.setText(textField.getText() + "-");
					textField.positionCaret(textField.getText().length());
				}

			}
		});

		textField.setOnKeyReleased((KeyEvent evt) -> {

			if (!textField.getText().matches("\\d./-*")) {
				textField.setText(textField.getText().replaceAll("[^\\d./-]", ""));
				textField.positionCaret(textField.getText().length());
			}
		});

	}

	public static void mascaraTelefone(TextField textField) {
		setTextFieldMaxLength(textField, 13);
		textField.setOnKeyTyped((KeyEvent event) -> {
			if ("0123456789".contains(event.getCharacter()) == false) {
				event.consume();
			}

			if (event.getCharacter().trim().length() == 0) { // apagando

				if (textField.getText().length() == 10 && textField.getText().substring(9, 10).equals("-")) {
					textField.setText(textField.getText().substring(0, 9));
					textField.positionCaret(textField.getText().length());
				}
				if (textField.getText().length() == 9 && textField.getText().substring(8, 9).equals("-")) {
					textField.setText(textField.getText().substring(0, 8));
					textField.positionCaret(textField.getText().length());
				}
				if (textField.getText().length() == 4) {
					textField.setText(textField.getText().substring(0, 3));
					textField.positionCaret(textField.getText().length());
				}
				if (textField.getText().length() == 1) {
					textField.setText("");
				}

			} else { // escrevendo

				if (textField.getText().length() == 14)
					event.consume();

				if (textField.getText().length() <= 1) {
					textField.setText("(" + event.getCharacter());
					textField.positionCaret(textField.getText().length());
					event.consume();
				}
				if (textField.getText().length() == 3) {
					textField.setText(textField.getText() + ")");
					textField.positionCaret(textField.getText().length());
					event.consume();
				}
				if (textField.getText().length() == 8) {
					textField.setText(textField.getText() + "-");
					textField.positionCaret(textField.getText().length());
					event.consume();
				}
//	                if(textField.getText().length()==9&&textField.getText().substring(8,9)!="-"){
//	                    textField.setText(textField.getText()+"-"+event.getCharacter());
//	                    textField.positionCaret(textField.getText().length());
//	                    event.consume();
//	                }
				if (textField.getText().length() == 13 && textField.getText().substring(8, 9).equals("-")) {
					textField.setText(textField.getText().substring(0, 8) + textField.getText().substring(9, 10) + "-"
							+ textField.getText().substring(10, 13) + event.getCharacter());
					textField.positionCaret(textField.getText().length());
					event.consume();
				}

			}

		});

		textField.setOnKeyReleased((KeyEvent evt) -> {

			if (!textField.getText().matches("\\d()-*")) {
				textField.setText(textField.getText().replaceAll("[^\\d()-]", ""));
				textField.positionCaret(textField.getText().length());
			}
		});

	}

	public static void mascaraCelular(TextField textField) {
		setTextFieldMaxLength(textField, 14);
		textField.setOnKeyTyped((KeyEvent event) -> {
			if ("0123456789".contains(event.getCharacter()) == false) {
				event.consume();
			}

			if (event.getCharacter().trim().length() == 0) { // apagando

				if (textField.getText().length() == 10 && textField.getText().substring(9, 10).equals("-")) {
					textField.setText(textField.getText().substring(0, 9));
					textField.positionCaret(textField.getText().length());
				}
				if (textField.getText().length() == 9 && textField.getText().substring(8, 9).equals("-")) {
					textField.setText(textField.getText().substring(0, 8));
					textField.positionCaret(textField.getText().length());
				}
				if (textField.getText().length() == 4) {
					textField.setText(textField.getText().substring(0, 3));
					textField.positionCaret(textField.getText().length());
				}
				if (textField.getText().length() == 1) {
					textField.setText("");
				}

			} else { // escrevendo

				if (textField.getText().length() == 14)
					event.consume();

				if (textField.getText().length() <= 1) {
					textField.setText("(" + event.getCharacter());
					textField.positionCaret(textField.getText().length());
					event.consume();
				}
				if (textField.getText().length() == 3) {
					textField.setText(textField.getText() + ")");
					textField.positionCaret(textField.getText().length());
					event.consume();
				}
				if (textField.getText().length() == 8) {
					textField.setText(textField.getText() + "-");
					textField.positionCaret(textField.getText().length());
					event.consume();
				}
//	                if(textField.getText().length()==9&&textField.getText().substring(8,9)!="-"){
//	                    textField.setText(textField.getText()+"-"+event.getCharacter());
//	                    textField.positionCaret(textField.getText().length());
//	                    event.consume();
//	                }
				if (textField.getText().length() == 13 && textField.getText().substring(8, 9).equals("-")) {
					textField.setText(textField.getText().substring(0, 8) + textField.getText().substring(9, 10) + "-"
							+ textField.getText().substring(10, 13) + event.getCharacter());
					textField.positionCaret(textField.getText().length());
					event.consume();
				}

			}

		});

		textField.setOnKeyReleased((KeyEvent evt) -> {

			if (!textField.getText().matches("\\d()-*")) {
				textField.setText(textField.getText().replaceAll("[^\\d()-]", ""));
				textField.positionCaret(textField.getText().length());
			}
		});

	}


	public static void pesquisaProduto(ComboBox<Produto> comoBox, ObservableList<Produto> obsListCliente) {
		
		comoBox.getEditor().setOnKeyTyped((KeyEvent event) -> {
			comoBox.hide();
			
			 if (!comoBox.getEditor().getText().matches("\\d.*")) {
				 obsListProduto = FXCollections.observableArrayList(obsListCliente.stream().filter(
							produto -> produto.getNome().toLowerCase().contains(comoBox.getEditor().getText().toLowerCase()))
							.collect(Collectors.toList()));
			 }else {
				 obsListProduto = FXCollections.observableArrayList(obsListCliente.stream().filter(
							cliente -> cliente.getId().toString().contains(comoBox.getEditor().getText()))
							.collect(Collectors.toList()));
			 }
			
			
			comoBox.setItems(obsListProduto);
			comoBox.show();
		});
		
	}
}