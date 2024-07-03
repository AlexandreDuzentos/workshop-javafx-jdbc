package gui;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DBException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Department;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.DepartmentService;
import model.services.SellerService;

public class SellerFormController implements Initializable {
	
    /* Criando uma dependência desse controller com o Seller */
	private Seller entity;
	
	/* Criando uma dependência desse controller com o SellerService */
	private SellerService sellerService;
	
	/* Criando uma dependência com o DepartmentService */
	private DepartmentService depService;
	
	
	/* Essa coleção armazerá os objetos interessados em serem notificados
	 * quando o evento onDataChanged ocorrer.
	 * */
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtName;
	
	@FXML
	private TextField txtEmail;
	
	@FXML
	private TextField txtBaseSalary;
	
	@FXML
	private DatePicker dpBirthDate;
	
	@FXML
	private ComboBox<Department> cbDepartment;
	
	@FXML
	private Label labelErrorName;
	
	@FXML
	private Label labelErrorEmail;
	
	@FXML
	private Label labelErrorBaseSalary;
	
	@FXML
	private Label labelErrorBirthDate;
	
	@FXML
	private Button btSave;
	
	@FXML 
	private Button btCancel;
	
	private ObservableList<Department> obsList;
	
	@FXML
	private void onBtSaveAction(ActionEvent event) {
		/* Se a condição for verdadeira, isso significa a dependência
		 * a um SellerService não foi injetada, isso é para evitar
		 * a exceção NullPointerException de ser lançada, quando o
		 * acesso a um membro do objeto depService for efetuado.
		 * */
		if(sellerService == null) {
			throw new IllegalStateException("Service was null");
		}
		
		try {
			entity = getFormData();
			sellerService.saveOrUpdate(entity); 
			notifyDataChangeListeners();
			/* Fechando a janela SellerForm após o salvamento
			 * de um Seller ocorrer com sucesso.
			 * */
			Utils.currentStage(event).close();
		} catch(DBException e) {
			Alerts.showAlert("Error saving objet", null, e.getMessage(), AlertType.ERROR);
		} catch(ValidationException e) {
			setErrorMessages(e.getErrors());
		}
	}
	
	private void notifyDataChangeListeners() {
		for(DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
		
	}
    
	/* Método responsável por obter os dados do formulário para salvamento
	 * ou atualização, a exata operação dependerá do id do objeto estar
	 * setado ou não.
	 * */
	private Seller getFormData() {
		Seller obj = new Seller();
		
		ValidationException exception = new ValidationException("Validation error");
		
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		
		if(txtName.getText() == null || txtName.getText().trim().equals("")) {
			exception.addError("name", "Field can`t be empty");
		}
		obj.setName(txtName.getText());
		
		if(txtEmail.getText() == null || txtEmail.getText().trim().equals("")) {
			exception.addError("email", "Field can`t be empty");
		}
		obj.setEmail(txtEmail.getText());
		
		/* 
		 * O método from obtém uma instância um Instant de um objeto
		 * temporal.
		 * 
		 * O método atStartOfDate converte a data que foi escolhida no DatePicker
		 * e que está no fuso-horário do computador do usuário para o Instant, que é
		 * uma data indepedente de localidade.
		 * 
		 * É assim que a gente pega o valor do DatePicker, é numa variável 
		 * do tipo Instant.
		 * */
		
		if(dpBirthDate.getValue() == null) {
			exception.addError("birthDate", "Field can't be empty");
		} else {
			Instant instant = Instant.from(dpBirthDate.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setBirthDate(Date.from(instant));
		}
		
		if(txtBaseSalary.getText() == null || txtBaseSalary.getText().trim().equals("")) {
			exception.addError("baseSalary", "Field can`t be empty");
		}
		obj.setBaseSalary(Utils.tryParseToDouble(txtBaseSalary.getText()));
		
		obj.setDepartment(cbDepartment.getValue());
		
		
		
		/* Testando a existência de alguma exceção no objeto exception,
		 * caso exista, eu já lanço a exceção.
		 *  */
		if(exception.getErrors().size() > 0) {
			throw exception;
		}
		
		return obj;
	}

	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
		initializeComboBoxDepartment();
	}
	
	/* Método responsável por inicializar o comportamento dos nodes
	 * da minha aplicação gráfica.
	 * */
	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 70);
		Constraints.setTextFieldDouble(txtBaseSalary);
		Constraints.setTextFieldMaxLength(txtEmail, 60);
		Utils.formatDatePicker(dpBirthDate, "dd/MM/yyyy");
	}
	
	/* Método responsável por fazer a injeção da dependência
	 * de um Seller.
	 * */
	public void setSeller(Seller entity) {
		this.entity = entity;
	}
	
	/* Método responsável por injetar dependências */
	public void setServices(SellerService sellerService, DepartmentService depService) {
		this.sellerService = sellerService;
		this.depService = depService;
	}
	
	/* Método responsável por inscrever um objeto interessado
	 * em ser notificado quando o evento onDataChanged ocorrer,
	 * isso desde que esse objeto implemente a interface
	 * DataChangeListener.
	 * 
	 * */
	public void subscribeDataChangeListener(DataChangeListener listener) {
		this.dataChangeListeners.add(listener);
	}
	
	/* Método responsável por popular os valores dos campos
	 * do formulário com os dados do objeto Seller quando a
	 * view de formulário estiver para ser aberta.
	 * */
	public void updateFormData() {
		/* Se a condição for verdadeira, isso significa a dependência
		 * a um Seller não foi injetada, isso é para evitar
		 * a exceção NullPointerException de ser lançada quando
		 * o acesso a um membro do objeto entity for efetuado.
		 * */
		if(entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		/* O método valueOf da classe String converte um
		 * objeto passado como argumento numa String, isso
		 * é necessário aqui pois o método setText espera um
		 * dado do tipo String e o Id é do tipo Integer.
		 * */
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
		txtEmail.setText(entity.getEmail());
		
		/* Setando o separador de casas decimais que
		 * será usado no campo abaixo para o .(ponto).
		 * */
		Locale.setDefault(Locale.US);
		txtBaseSalary.setText(String.format("%.2f",entity.getBaseSalary()));
		
		if(entity.getBirthDate() != null) {
			/* Convertendo a data armazenada no meu banco no formato
			 * global para o formato local, pois é no formato local que
			 * a data deve ser apresentado no computador do usuário
			 * final.
			 * 
			 * o método setValue recebe uma instância de um objeto do tipo LocalDate.
			 * 
			 * o método ofInstant instância um objeto do tipo LocalDate a partir
			 * de um instante e de um timezone especificado.
			 * 
			 * o método toInstant converte um objeto do tipo Date para um
			 * objeto do tipo Instant.
			 * */
		  dpBirthDate.setValue(LocalDate.ofInstant(entity.getBirthDate().toInstant(), ZoneId.systemDefault()));
		  
		  /* Esse teste é necessário para previnir que seja setado um Department valendo null,
		   * pois, num cadastro de um Seller, o Department pode não estar
		   * setado, ou seja, o Seller pode não ter um Department ainda.
		   * */
		  if(entity.getDepartment() == null) {
			  /* 
			   * Setando o Department para ser apresentado dentro do 
			   * ComboBox como sendo o primeiro.
			   * */
			  cbDepartment.getSelectionModel().selectFirst();
		  } else {
			  /* 
			   * Setando o Department para ser apresentado dentro do 
			   * ComboBox como sendo o Deparment associado ao seller.
			   * */
			  cbDepartment.setValue(entity.getDepartment());
		  }
		  
		}
		
	}
	
	/* 
	 * Método responsável por carregar os objetos associados(Departments) a
	 * a um seller.
	 * */
	public void loadAssociatedObjects() {
		if(depService == null) {
			throw new IllegalStateException("DepartmentService was null");
		}
		
		List<Department> list = depService.findAll();
		obsList = FXCollections.observableArrayList(list);
		cbDepartment.setItems(obsList);
	}
	
	/* Método responsável por exibir mensagens de erros nos labels
	 * dos campos do formulário.
	 * */
	public void setErrorMessages(Map<String, String> errors) {
		/* A método keySet retorna as chaves da coleção Map */
		Set<String> fields = errors.keySet();
		
		/* Verificando a existência da chave, caso ela exista, isso que
		 * dizer que o valor também existe e logo já setamos o valor
		 * mensagem de erro no controle responsável por exibi-la no campo
		 * do formulário.
		 * */
		if(fields.contains("name")) {
			labelErrorName.setText(errors.get("name"));
		} else {
			labelErrorName.setText("");
		}
		
		if(fields.contains("email")) {
			labelErrorEmail.setText(errors.get("email"));
		} else {
			labelErrorEmail.setText("");
		}
		
		if(fields.contains("baseSalary")) {
			labelErrorBaseSalary.setText(errors.get("baseSalary"));
		} else {
			labelErrorBaseSalary.setText("");
		}
		
		if(fields.contains("birthDate")) {	
		    labelErrorBirthDate.setText(errors.get("birthDate"));
		} else {
			labelErrorBaseSalary.setText("");
		}
	}
	
	/* 
	 * Método responsável por formatar a forma como o Department é 
	 * apresentado dentro do comboBox.
	 * */
	private void initializeComboBoxDepartment() {
		Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department>() {
			@Override
			protected void updateItem(Department item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getName());
			}
		};
		cbDepartment.setCellFactory(factory);
		cbDepartment.setButtonCell(factory.call(null));
	}

}
