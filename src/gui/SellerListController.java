package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DBIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Seller;
import model.services.SellerService;

public class SellerListController implements Initializable, DataChangeListener {
	
	/* Criando uma dependência para o SellerService.
	 * */
	private SellerService sellerService;
    
	/* O TableView é um tipo genérico, ou seja, ele é parametrizado
	 * por tipo, essa é uma referência para o controle TableView
	 * que está na minha tela.
	 * */
	@FXML
	private TableView<Seller> tableViewSeller;
	
	/* O tableColumn também é parametrizado por tipo, sendo 
	 * o primeiro tipo o tipo da entidade e o segundo tipo
	 * o tipo dos valores que vão na coluna da tabela.
	 * */
	@FXML
	private TableColumn<Seller, Integer> tableColumnId;
	
	@FXML
	private TableColumn<Seller, String> tableColumnName;
	
	@FXML
	private TableColumn<Seller, Seller> tableColumnEDIT;
	
	@FXML
	private TableColumn<Seller, Seller> tableColumnREMOVE;
	
	@FXML
	private Button btNew;
	
	private ObservableList<Seller> obsList;
	
	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event); 
		
		/* Instânciando um Seller vazio, ou seja, sem dados nele,
		 * isto porque essa é a uma operação de salvamento de um
		 * novo Seller, logo, os campos do formulário não precisarão
		 * ser carregados com dados.
		 * */
		Seller dep = new Seller();
		createDialogForm(dep ,"/gui/SellerForm.fxml", parentStage);
	}
	
	/* Método responsável por injetar uma dependência para o objeto
	 * SellerService */
	public void setSellerService(SellerService sellerService) {
		this.sellerService = sellerService;
	}

	/* Esse método é executado quando o construtor é
	 * instanciado, dentro dele colocamos código responsável por
	 * inicializar o estado do meu objeto.
	 * */
	@Override
	public void initialize(URL url, ResourceBundle rb) {	
		initializeNodes();
	}

	/* Método responsável por inicializar o comportamento
	 * das minhas colunas da TableView.
	 * */
	private void initializeNodes() {
		/* Esse é um padrão do javafx para iniciar o comportamento das colunas */
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		
		/* Macete para fazer a TableView acompanhar a altura da janela.
		 * o método getWindow do objeto Scene retorna um objeto do tipo Window.
		 * o tipo Window é um supertipo do tipo Stage, então para
		 * essa atribuição seja possível, é necessário fazer um downcasting
		 * de Window para Stage.
		 *  */
		
		/* Referência para o stage da window principal */
		Stage stage = (Stage)Main.getMainScene().getWindow();
		
		/* Setando o height do tableViewSeller como a altura
		 * do stage da window principal. 
		 * */
		tableViewSeller.prefHeightProperty().bind(stage.heightProperty());
		
	}
	
	/* Esse método será responsável por acessar o departmentService, carregar
	 * os Departaments e inserir esses Departaments na minha
	 * observableList e aí sim, esse ObservableList será associado
	 * com o meu tableView e aí os department serão exibidos na tela
	 * para mim.
	 *  */
	public void updateTableView() {
		/* Se o programador esquecer de injetar a dependência
		 * ao service, uma exceção será lançada.
		 * 
		 * Isso também está sendo feito porque a nossa injeção
		 * de dependência é totalmente manual, nós não estamos
		 * usando um container ou um framework que fará isso
		 * automáticamente para nós.
		 * 
		 * Essa é uma programação defensiva.
		 * */
		if(sellerService == null) {
			throw new IllegalArgumentException("Service was null");
		}
		
		List<Seller> list = sellerService.findAll();
		
		/* Isso aqui já instância o meu observableList e insere
		 * nele os dados da list comum.
		 * */
		obsList = FXCollections.observableArrayList(list);
		
		/* Inserindo os dados da obsList na tableView */
		tableViewSeller.setItems(obsList);
		
		initEditButtons();
		initRemoveButtons();
	}
	
	/* Método responsável por criar uma window de dialógo ou seja,
	 * um modal, quando criamos um modal precisamos informar
	 * para ele qual é a janela pai(parentStage) que criou esse
	 * Modal de dialogo, a janela pai seria  janela já existente
	 * na tela, uma vez que, o modal ficará por cima dela, então, em
	 * última instância, esse método cria um nova window */
	private void createDialogForm(Seller dep, String absoluteName, Stage parentStage) {
		try {
			/* 
			* Obtendo o FXML correspondente a alguma view
			* partir do seu caminho absoluto. 
			**/
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			
			Pane pane = loader.load();
			
			/* Injetando o Seller passado como argumento no
			 * SellerFormController, passar um objeto para o
			 * formulário é uma prática comum quando nós estamos
			 * trabalhando no padrão MVC.
			 *  */
			//SellerFormController controller = loader.getController();
			//controller.setSeller(dep);
			//controller.setSellerService(new SellerService());
			
			/* Registrando um listener que será notificado quando o evento ocorrer */
			//controller.subscribeDataChangeListener(this);
			//controller.updateFormData();
			
			/* Quando eu vou carregar uma janela modal na frente
			 * janela existente, eu tenho que instânciar um novo
			 * stage, então será um stage na frente do outro */
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter Seller data");
			dialogStage.setScene(new Scene(pane));
			/* Defindo a window(stage) como redimensionável */
			dialogStage.setResizable(false);
			
			/* Setando o Stage(window) pai dessa window modal que será aberta */
			dialogStage.initOwner(parentStage);
			
			/* 
			 * Método responsável por definir se o comportamento
			 * da window sendo aberta será modal ou se terá outro
			 * comportamento.
			 * 
			 * Uma janela com um comportamento modal não aceita que
			 * eu acesse outras windows sem que eu feche ela antes.
			 * 
			 * */
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (IOException e) {
			Alerts.showAlert("IO Exception","Error loading dialog window", e.getMessage(), AlertType.ERROR);
		} finally {
			
		}
	}

	@Override
	public void onDataChanged() {
		updateTableView();	
	}
	
	/* 
	 * Método responsável por criar um botão de edição para cada linha da
	 * minha tabela, o código desse método é muito específico do JAVAFX.
	 * */
	private void initEditButtons() { 
			tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue())); 
			tableColumnEDIT.setCellFactory(param -> new TableCell<Seller, Seller>() { 
			     private final Button button = new Button("edit");
			     
				 @Override
				 protected void updateItem(Seller obj, boolean empty) { 
					 super.updateItem(obj, empty); 
					 
					 if (obj == null) { 
						 setGraphic(null); 
						 return; 
				     } 
					 
					 setGraphic(button); 
						 button.setOnAction( 
						 event -> createDialogForm( 
						       obj, "/gui/SellerForm.fxml",Utils.currentStage(event))); 
						 } 
					 }); 
		} 
	
	public void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue())); 
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Seller, Seller>() { 
		     private final Button button = new Button("delete");
		     
			 @Override
			 protected void updateItem(Seller obj, boolean empty) { 
				 super.updateItem(obj, empty); 
				 
				 if (obj == null) { 
					 setGraphic(null); 
					 return; 
			     } 
				 
				 setGraphic(button); 
					 button.setOnAction( 
					 event -> removeEntity(obj));       
					 }
 
				 }); 
	}
	
	/* Método responsável por remover um Deparmento da minha TableView */
	private void removeEntity(Seller obj) {
		/* O tipo Optional do java armazena um objeto cujo tipo
		 * é correspondente ao tipo que ele foi parâmetrizado.
		 * */
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Are you sure to delete?");
		
		/* 
		 * Testando se o usuário clicou no botão de comfirmação
		 * na hora de deletar um registro do banco de dados.
		 * 
		 * O método get do objeto Optional retorna o objeto armazenado dentro
		 * do objeto optional.
		 * */
		if(result.get() == ButtonType.OK) {
			if(sellerService == null) {
				throw new IllegalArgumentException("service was null");
			}
			
			try {
				sellerService.remove(obj);
				updateTableView();
			} catch(DBIntegrityException e) {
				Alerts.showAlert("Error removing Seller", null, e.getMessage(), AlertType.ERROR);
			}
			
		}
	}

}
