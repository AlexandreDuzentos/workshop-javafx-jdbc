package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DBException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.SellerService;

public class SellerFormController implements Initializable {
	
    /* Criando uma dependência desse controller com o Seller */
	private Seller entity;
	
	/* Criando uma dependência desse controller com o SellerService */
	private SellerService sellerService;
	
	/* Essa coleção armazerá os objetos interessados em serem notificados
	 * quando o evento onDataChanged ocorrer.
	 * */
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtName;
	
	@FXML
	private Label labelErrorName;
	
	@FXML
	private Button btSave;
	
	@FXML 
	private Button btCancel;
	
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

	private Seller getFormData() {
		Seller obj = new Seller();
		
		ValidationException exception = new ValidationException("Validation error");
		
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		
		if(txtName.getText() == null || txtName.getText().trim().equals("")) {
			exception.addError("name", "Field can`t be empty");
		}
		obj.setName(txtName.getText());
		
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
	}
	
	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 30);
	}
	
	/* Método responsável por fazer a injeção da dependência
	 * de um Seller.
	 * */
	public void setSeller(Seller entity) {
		this.entity = entity;
	}
	
	public void setSellerService(SellerService sellerService) {
		this.sellerService = sellerService;
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
	
	/* Método responsável por atualizar os valores dos campos
	 * do formulário com os dados do objeto Seller.
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
	}
	
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
		}
	}
	

}
