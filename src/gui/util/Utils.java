package gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Utils {
	
	public static Stage currentStage(ActionEvent event) {
		return (Stage) ((Node) event.getSource()).getScene().getWindow();
	}
	
	public static Integer tryParseInt(String str) {
		try {
			return Integer.parseInt(str);
		}
		catch(NumberFormatException e){
			return null;
		}
	}
	
	public static Long tryParseLong(String str) {
		try {
			return Long.parseLong(str);
		}
		catch(NumberFormatException e){
			return null;
		}
	}

	public static Stage currentStage(MouseEvent event) {
		return (Stage) ((Node) event.getSource()).getScene().getWindow();
	}

}
