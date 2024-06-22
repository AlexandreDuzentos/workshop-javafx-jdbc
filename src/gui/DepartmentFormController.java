package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

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
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentFormController implements Initializable {
	
    /* Criando uma dependência desse controller com o Department */
	private Department entity;
	
	/* Criando uma dependência desse controller com o DepartmentService */
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
	private Label labelErrorName;
	
	@FXML
	private Button btSave;
	
	@FXML 
	private Button btCancel;
	
	@FXML
	private void onBtSaveAction(ActionEvent event) {
		/* Se a condição for verdadeira, isso significa a dependência
		 * a um DepartmentService não foi injetada, isso é para evitar
		 * a exceção NullPointerException de ser lançada, quando o
		 * acesso a um membro do objeto depService for efetuado.
		 * */
		if(depService == null) {
			throw new IllegalStateException("Service was null");
		}
		
		try {
			entity = getFormData();
			depService.saveOrUpdate(entity); 
			notifyDataChangeListeners();
			/* Fechando a janela DepartmentForm após o salvamento
			 * de um Department ocorrer com sucesso.
			 * */
			Utils.currentStage(event).close();
		} catch(DBException e) {
			Alerts.showAlert("Error saving objet", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	private void notifyDataChangeListeners() {
		for(DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
		
	}

	private Department getFormData() {
		Department obj = new Department();
		
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		obj.setName(txtName.getText());
		
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
	 * de um Department.
	 * */
	public void setDepartment(Department entity) {
		this.entity = entity;
	}
	
	public void setDepartmentService(DepartmentService depService) {
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
	
	/* Método responsável por atualizar os valores dos campos
	 * do formulário com os dados do objeto Department.
	 * */
	public void updateFormData() {
		/* Se a condição for verdadeira, isso significa a dependência
		 * a um Department não foi injetada, isso é para evitar
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
	

}
