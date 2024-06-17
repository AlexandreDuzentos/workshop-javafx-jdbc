package db;

public class DBIntegrityException extends RuntimeException {

	private static final long serialVersionUID = 1L;
   /* 
    * Exceção que será lançada quando um erro de integridade referencial 
    * ocorrer.
    * 
    * se eu tiver uma chave
	* estrangeira relacionada um registro
	* e a chave primária correspondente a àquela chave estrangeira
	* não existir, eu tenho um erro
	* de integridade referencial, pois, o registro associado com
	* a chave estrangeira fica com a referência errada.
	* Este problema pode ocorrer quando o registro associado a chave primária
	* for deletado. 
	* 
	* Por conta disso, o próprio sgbd não permite que chaves primárias de
	* registros que estão relacionadas a outras tabelas por meio da chave
	* estrangeiras sejam deletados, quando ocorre uma tentativa de deleção,
	* um erro de integriade referencial é lançado. 
    * */
	
	public DBIntegrityException(String msg) {
		super(msg);
	}
}
