package gui.util;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

/* Classe utilitária da camada de view */
public class Alerts {
   
	/* Método responsável por exibir uma Janela de diálogo genérica */
	public static void showAlert(String title, String header, String content, AlertType type) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.show();
	}
	
	/* Método responsável por exibir um janela de dialogo, com botões 
	 * para confirmar ou cancelar a ação sendo performada.
	 * */
	public static Optional<ButtonType> showConfirmation(String title, String content) { 
		 Alert alert = new Alert(AlertType.CONFIRMATION); 
		alert.setTitle(title); 
		alert.setHeaderText(null); 
		alert.setContentText(content); 
		return alert.showAndWait(); 
		} 
}
