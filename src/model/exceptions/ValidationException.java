package model.exceptions;

import java.util.HashMap;
import java.util.Map;

public class ValidationException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	/* Como essa exceção é para validar um formulário, eu vou fazer
	 * essa exceção carregar as mensagens de erros(com seus respectivos
	 * campos) caso existam.
	 * */
	
	/* Coleção usada para armazenar o campo e a mensagem de erro correspodente */
	private Map<String, String> errors = new HashMap<>();
	
	public ValidationException(String msg) {
		super(msg);
	}
	
	/* Método responsável por obter os erros da coleção Map */
	public Map<String, String> getErrors(){
		return errors;
	}
	
	/* Método responsável por adicionar erros a coleção Map */
	public void addError(String fieldName, String errorMessage) {
		errors.put(fieldName, errorMessage);
	}

}
