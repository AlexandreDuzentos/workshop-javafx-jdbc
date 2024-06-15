package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable {
	
	/* Criando uma dependência para o DepartmentService.
	 * */
	private DepartmentService service;
    
	/* O TableView é um tipo genérico, ou seja, ele é parametrizado
	 * por tipo, essa é uma referência para o controle TableView
	 * que está na minha tela.
	 * */
	@FXML
	private TableView<Department> tableViewDepartment;
	
	/* O tableColumn também é parametrizado por tipo, sendo 
	 * o primeiro tipo o tipo da entidade e o segundo tipo
	 * o tipo dos valores que vão na coluna da tabela.
	 * */
	@FXML
	private TableColumn<Department, Integer> tableColumnId;
	
	@FXML
	private TableColumn<Department, String> tableColumnName;
	
	@FXML
	private Button btNew;
	
	private ObservableList<Department> obsList;
	
	@FXML
	public void onBtNewAction() {
		System.out.println("onBtNewAction");
	}
	
	/* Método responsável por injetar uma dependência */
	public void setDepartmentService(DepartmentService service) {
		this.service = service;
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
		
		/* Setando o height do tableViewDepartment como a altura
		 * do stage da window principal. 
		 * */
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());
		
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
		if(service == null) {
			throw new IllegalArgumentException("Service was null");
		}
		
		List<Department> list = service.findAll();
		
		/* Isso aqui já instância o meu observableList e insere
		 * nele os dados da list comum.
		 * */
		obsList = FXCollections.observableArrayList(list);
		
		/* Inserindo os dados da obsList na tableView */
		tableViewDepartment.setItems(obsList);
	}

}
