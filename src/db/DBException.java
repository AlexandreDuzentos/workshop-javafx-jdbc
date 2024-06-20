package db;

/* 
 * Exceção que será lançada quando o banco de dados
 * lançar alguma exceção.
 * */
public class DBException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public DBException(String msg) {
		super(msg);
	}

}
