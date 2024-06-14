package application;
	
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;


public class Main extends Application {
	
	/* Criando uma referência ao objeto do tipo Scene para que
	 * ele possa ser utilizado em outras partes do código que precisam
	 * exibir alguma view na scene */
	private static Scene mainScene;
	
	@Override
	public void start(Stage primaryStage) { 
		try {
			 /*
			   Obtendo o FXML correspondente a view principal, que é
			   a MainView usando o caminho absoluto.
			 */ 
			 FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainView.fxml"));
			 
			 /* Atribuindo o FXML obtido para o componente(nódo) raíz da MainView */
			 ScrollPane scrollPane = loader.load(); 
			 
			 /* 
			  * As duas declarações abaixo deixarão o componente
			  * scrollPane ajustado a janela, ou seja, ele ficará
			  * responsivo em relação a janela.
			  * */
			 scrollPane.setFitToHeight(true);
			 scrollPane.setFitToWidth(true);
			 
			 /* 
			  * Setando o componente raíz da MainView na Scene.
			  * */
			 mainScene = new Scene(scrollPane); 
			 
			 /* setando a Scene no Stage */
			 primaryStage.setScene(mainScene); 
			 
			 /* Setando um title para o stage */
			 primaryStage.setTitle("Sample JavaFX application"); 
			 
			 /* Exibindo o stage */
			 primaryStage.show(); 
		 } catch (IOException e) { 
		 e.printStackTrace(); 
		 } 
	} 
	
	public static Scene getMainScene() {
		return Main.mainScene;
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
