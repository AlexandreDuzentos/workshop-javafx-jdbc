package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class MainViewController implements Initializable {
	
	@FXML
	private MenuItem menuItemSeller;
	
	@FXML
	private MenuItem menuItemDepartment;
	
	@FXML
	private MenuItem about;
	
	/* 
	 * Método responsável por tratar o evento do click no controle
	 * correspondente ao menuItemSeller.
	 * */
	@FXML
	public void onMenuItemSellerAction() {
		System.out.println("onMenuItemSellerAction");
	}
	
	/* 
	 * Método responsável por tratar o evento do click no controle
	 * correspondente ao menuItemDepartment.
	 * */
	@FXML
    public void onMenuItemDepartmentAction() {
		loadView("/gui/DepartmentList.fxml");
	}
    
	/* 
	 * Método responsável por tratar o evento do click no controle
	 * correspondente ao menuItemAbout.
	 * */
    @FXML
    public void onMenuItemAboutAction() {
		loadView("/gui/AboutView.fxml");
	}
	
	

    /* Método executado quando a controller é instânciado */
	@Override
	public void initialize(URL uri, ResourceBundle rb) {
			
	}
	
	/*
	 * Método responsável por carregar uma view dentro da MainView.
	 * 
	 * Se você quiser garantir que todo o processamento do método
	 * loadView ocorra sem ser interrompido, como assim interrompido?
	 * é porque a aplicação gráfica é multi-threading, tem várias 
	 * threads executando inclusive a thread da tela, se você quiser
	 * que ela não seja interrompida para depois executar e dar algum comportamento
	 * inesperado, você pode usar a palavra reservada synchronized na 
	 * assinatura do método, e aí você garante que essa processamento
	 * todo não será interrompido durante o multi-thread.
	 * 
	 * métodos synchronized habilitam uma estratégia simples para
	 * previnir interferência de threads e erros consistentes de
	 * memória.
	 * 
	 * thread: é uma linha de execução individual no computador.
	 * */
	public synchronized void loadView(String absoluteName) {
		try {
			/* 
			* Obtendo o FXML correspondente a AboutView ou a qualquer outra
			* view a partir do seu caminho absoluto. 
			**/
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			
			/* Atribuindo o FXML obtido para um objeto do tipo do
			 * componente raíz do FXML da AboutView ou qualquer
			 * outra view, por padrão, todas as telas abertas a
			 * partir da view principal terão como elemento
			 * raíz um VBox.
			 **/
			VBox newVBox = loader.load();
			
			/* Obtendo uma referêcia para cena principal para que 
			 * possamos manipula-la afim de carregar outras views
			 * dentro dela.
			 * */
			Scene mainScene = Main.getMainScene();
			
			/* O método getRoot obtém o componente raíz da mainScene, que no
			 * caso será um scrollPane, mas um objeto do tipo Parent é retornado.
			 * 
			 * Como um objeto do tipo Parent é retornado e o tipo que
			 * eu desejo é um ScrollPane, então eu fiz um downcasting
			 * de um objeto do tipo Parent para um objeto do tipo
			 * ScrollPane e isso também serviu para que eu possa
			 * acessar o método getContent, que o objeto do tipo
			 * Parent não possui, mas o objeto ScrollPane possui.
			 * 
			 * O downcasting é necessário pois o tipo do objeto retornado pelo
			 * método getRoot é scrollPane, mas o compilador não sabe disso
			 * de forma natural, então é necessário falar isso para
			 * ele de forma explícita.
			 * 
			 * O método getContent de um objeto do tipo ScrollPane é responsável por obter oque está dentro do content
			 * do ScrollPane que é um VBox e ele retorna um objeto o tipo Node que é um supertipo
			 * de VBox e eu desejo ter um componente do tipo VBox, por isso é que
			 * foi feito o downcasting para VBox.
			 * 
			 * Abaixo nós temos uma referência para o VBox que está na minha
			 * janela principal.
			 *  */
			VBox mainVBox = (VBox)((ScrollPane)mainScene.getRoot()).getContent();
			
			/* 
			 * O método getChildren obtém os elementos chidren do
			 * vBox e o get(indice) pega um filho num índice
			 * específico, o índice específico nesse caso corresponderá
			 * a um objeto do tipo MenuBar.
			 * 
			 * o mainMenu corresponde ao menu da minha view
			 * principal, aqui eu estou guardando uma referência a ele.
			 * 
			 * Upcasting de um componente do tipo MenuBar para um componente
			 * do tipo Node, Node é um tipo mais genérico que o MenuBar.
			 * */
			Node mainMenu = mainVBox.getChildren().get(0);
			
			/* Limpando todos os elementos filhos do mainVBox */
			mainVBox.getChildren().clear();
			
			/* Adicionando o mainMenu dentro dos filhos do mainVBox */
			mainVBox.getChildren().add(mainMenu);
			
			/* 
			 * Adicionando os filhos da VBox da janela que eu
			 * estiver abrindo nos filhos do MainVBox.
			 * */
			mainVBox.getChildren().addAll(newVBox.getChildren());
					
		} catch (IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

}
