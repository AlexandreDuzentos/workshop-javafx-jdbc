package gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils {
    
	/* Método responsável por obter o stage em que um evento 
	 * de clique ocorreu.
	 * 
	 * O objeto ActionEvent se refere a um evento do clique
	 * em algum controle visual que ocorreu em um stage.
	 * */
	public static Stage currentStage(ActionEvent event) {
		return (Stage)((Node)event.getSource()).getScene().getWindow();
	}
}
