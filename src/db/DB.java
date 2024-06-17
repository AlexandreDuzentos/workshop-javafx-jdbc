package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/* ---- classe responsável por conectar e desconectar com o banco de dados ----- */
public class DB {
	
	/* Objeto de conexão com o banco de dados do JDBC */
	private static Connection conn = null;
	
	/* 
	 * Método responsável por fazer uma conexão com o banco de dados e
	 * retornar o objeto de conexão com o banco de dados.
	 **/
	public static Connection getConnection() {
		if(conn == null) {
			try {
				Properties props = loadProperties();
				String url = props.getProperty("dburl");
				
			    // A conexão com o banco de dados é efetivada nessa linha
				conn = DriverManager.getConnection(url, props);
			} catch(SQLException e) {
				throw new DBException(e.getMessage());
			}
		}
		
		return conn;
	}
	
	/* Método responsável por fechar a conexão com o banco de dados */
	public static void closeConnection() {
		if(conn != null) {
			try {
			  conn.close();
			} catch(SQLException e) {
				throw new DBException(e.getMessage());
			}
		}
		
	}
   
	/* 
	 * Método responsável por ler o arquivo contendo 
	 * os dados de conexão com o banco de dados e armazenar
	 * esses dados no objeto props do tipo Properties.
	 * 
	 * retorna um objeto do tipo Properties.
	 * */
	private static Properties loadProperties() {
		try(FileInputStream fs = new FileInputStream("db.properties")){
			Properties props = new Properties();
			
			/* 
			 * O método load faz a leitura do arquivo db.properties apontado
			 * pelo objeto fs e guarda os dados dentro do objeto props.
			 * */
			props.load(fs);
			
			return props;
		} catch(IOException e) {
			throw new DBException(e.getMessage());
		}
	} 
	
	/* ----- Métodos auxiliares para fechar recursos externos que foram alocados ----- */
	
	// método responsável por fechar o objeto Statement
	public static void closeStatement(Statement st) {
		if(st != null) {
			
			try {
				st.close();
			} catch (SQLException e) {
				/* Lançamos a nossa exceção personalizada para garantir que uma
				 * RuntimeException será lançada, pois a DBException também é
				 * uma RuntimeException, que é um tipo de exceção que o compilador
				 * não obriga a tratar.
				 * 
				 * O compilador obrigar a tratar uma exceção significa que ela
				 * deve ser tratada em caso de possibilidade dela ser lançada pelo
				 * trecho de código que a lança.
				 * */
				throw new DBException(e.getMessage()); // lançando uma exceção que o compilador não obriga a tratar
			}
		}
	}
	
	
	/* Método responsável por fechar o objeto ResultSet */
	public static void closeResultSet(ResultSet rs)  {
		if(rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				throw new DBException(e.getMessage());
			}
		}
	}
}
