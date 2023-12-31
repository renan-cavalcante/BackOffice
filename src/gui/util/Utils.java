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
	
	public static Double tryParseDouble(String str) {
		try {
			return Double.parseDouble(str);
		}
		catch(NumberFormatException e){
			return null;
		}
	}

	public static Stage currentStage(MouseEvent event) {
		return (Stage) ((Node) event.getSource()).getScene().getWindow();
	}
	
	public static String classeChamadora () {
        Throwable thr = new Throwable();
        thr.fillInStackTrace ();
        StackTraceElement[] ste = thr.getStackTrace();
        return ste [2].getClassName();
    }

}
