package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DBException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {
	
	/* 
	 * A implementaçao do sellerDao terá essa dependência com a conexão, isso
	 * é feito para prover reuso do objeto Connection nos outros métodos. 
	 * */
	private Connection conn;
	
	public SellerDaoJDBC(Connection conn) {
		/* injetando a dependência via construtor */
		this.conn = conn;
	}

	/* operação que insere no banco de dados o objeto passado como parâmetro de entrada */
	@Override
	public void insert(Seller obj) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conn.prepareStatement("insert into seller "
					+ "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
					+"values "
					+"(?,?,?,?,?)",
					Statement.RETURN_GENERATED_KEYS);
			
			ps.setString(1, obj.getName());
			ps.setString(2, obj.getName());
			ps.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			ps.setDouble(4, obj.getBaseSalary());
			ps.setInt(5, obj.getDepartment().getId());
			
			int rowsAffected = ps.executeUpdate();
			
			if(rowsAffected > 0) {
				rs = ps.getGeneratedKeys();
				/* É um if porque um único Seller foi inserido, logo,
				 * um único id será retornado caso a transação ocorra com
				 * sucesso e o if não testa a condição mais de uma vez por execução.
				 * */
				if(rs.next()) {
					int id = rs.getInt(1); // obtendo o id do último Seller inserido.
					/* 
					 * populando o objeto Seller com o seu novo id correspondente
					 * ao novo registro inserido no banco de dados.
					 * */
					obj.setId(id);
				} else {
					throw new DBException("Unexpected error! No rows affected!");
				}
			}
			
		} catch(SQLException e) {	
			throw new DBException(e.getMessage());
		} finally {
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}
		
		
		
	}

	/* operação faz uma atualização no banco de dados */
	@Override
	public void update(Seller obj) {
		PreparedStatement ps = null;
		
		try {
			ps = conn.prepareStatement("update seller "
					+ "set Name = ?,Email = ?,BirthDate = ?,BaseSalary = ?,"
					+ "DepartmentId = ? "
					+ "where Id = ?"
				     );
			
			ps.setString(1, obj.getName());
			ps.setString(2, obj.getName());
			ps.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			ps.setDouble(4, obj.getBaseSalary());
			ps.setInt(5, obj.getDepartment().getId());
			ps.setInt(6, obj.getId());
			
			ps.executeUpdate();
			
		} catch(SQLException e) {	
			throw new DBException(e.getMessage());
		} finally {
			DB.closeStatement(ps);
		}
		
	}

	 /* operação responsável por remover Seller pelo id */
	@Override
	public void deleteById(Integer id) {
		PreparedStatement ps = null;
		
		try {
			ps = conn.prepareStatement("delete from seller where id = ?");
			
			ps.setInt(1, id);
			int rows = ps.executeUpdate();
			
			if(rows == 0) {
				throw new DBException("The informed id does not exist!");
			}
		} catch(SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeStatement(ps);
		}
		
	}

	/* operação responsável por recuperar Seller pelo id */
	@Override
	public Seller findById(Integer id) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conn.prepareStatement("SELECT seller.*, department.name as deptName "
					  + "from seller inner join department "
					  + "on seller.departmentId = department.Id "
					  + "where seller.Id = ?"
			     );
			
			ps.setInt(1, id);
			
			rs = ps.executeQuery();
			
			if(rs.next()) {
				Department dept = instantiateDepartment(rs);
				
				Seller sl = instantiateSeller(rs, dept);
				
				return sl;
			}
			
			return null;
			
		} catch(SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
			/*  A conexão não foi fechada aqui porque o
			 *  nosso SellerDaoJDBC ainda usará o objeto de conexão
			 *  noutros métodos.
			 *  */
		}
	}

	/* 
	 *  Método responsável por instanciar um Seller passando os dados recuperados
	 *  da base de dados e associa-lo ao department e retorna um objeto Seller
	 *  que tem um associação a um department.
	 *  
	 *  Aqui nesse método a exceção será propagada ao invés de tratada porque
	 *  o método que usa esse método já trata a exceção, ao propaga-la, estou
	 *  dizendo que o método instantiateDepartment pode lançar essa
	 *  exceção(SQLException).
	 *  */
	private Seller instantiateSeller(ResultSet rs, Department dept) throws SQLException {
		Seller seller = new Seller();
		seller.setId(rs.getInt("Id"));
		seller.setName(rs.getString("Name"));
		seller.setEmail(rs.getString("Email"));
		seller.setBirthDate(rs.getDate("BirthDate"));
		seller.setBaseSalary(rs.getDouble("BaseSalary"));
		seller.setDepartment(dept);
		
		return seller;
	}

	/* 
	 * Método responsável por instanciar um Department passando os dados
	 * recuperados do banco de dados, retorna um objeto Department.
	 * 
	 * Aqui nesse método a exceção será propagada ao invés de tratada porque
	 * o método que usa esse método já trata a exceção, ao propaga-la, estou
	 * dizendo que o método instantiateDepartment pode lançar essa
	 * exceção(SQLException).
	 * */
	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department dept = new Department();
		dept.setId(rs.getInt("DepartmentId"));
		dept.setName(rs.getString("DeptName"));
		
		return dept;
	}

	/* operação responsável por recuperar todos os Sellers do banco de dados */
	@Override
	public List<Seller> findAll() {
	   PreparedStatement ps = null;
	   ResultSet rs = null;
	   
	   try {
		         ps = conn.prepareStatement(
				   "SELECT seller.*, department.Name as deptName "
				 + "FROM seller INNER JOIN department "
				 + "on seller.DepartmentId = department.id "
				 + "order by Name"
				 );
		   
		   rs = ps.executeQuery();
		   
		   List<Seller> list = new ArrayList<>();
		   Map<Integer, Department> map = new HashMap<>();
		   
		   while(rs.next()) {
			   /* usamos esse controle para evitar repetição de instâncias
			    * de Department por que um Department pode estar relacionado
			    * a mais de um Seller na consulta que está sendo feita, então 
			    * isso é feita para evitar instâncias de objetos Department desnessárias,
			    * uma vez que pode se tratar do mesmo Department relacionado a um Seller,
			    * quanto menor o número de instâncias, menor é o consumo de
			    * memória, faz-se, na verdade um reaproveitamento de objetos.
			    * */
			   Department dept = map.get(rs.getInt("departmentId"));
				
				if(dept == null) {
					/*
					 * instanciando um Department apenas se ele ainda não
					 * estiver dentro da estrutura Map.
					 * */
					dept = instantiateDepartment(rs);
					
					map.put(rs.getInt("departmentId"), dept);
				}
				
				Seller sl = instantiateSeller(rs, dept);
				
				list.add(sl);
		   }
		   
		   return list;
	   } catch(SQLException e) {
		   throw new DBException(e.getMessage());
	   } finally {
		   DB.closeStatement(ps);
		   DB.closeResultSet(rs);
	   }
	}
	
    /* operação responsável por recuperar todos os sellers por Department(um único Department) */
	@Override
	public List<Seller> findByDepartment(Department department) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conn.prepareStatement("SELECT seller.*, department.Name as deptName "
					+ "FROM seller INNER JOIN department "
					+ "on seller.DepartmentId = department.id "
					+ "WHERE departmentId = ? "
					+ "order by Name"
					);
			
			ps.setInt(1, department.getId());
			
			rs = ps.executeQuery();
			
			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();
			
			while(rs.next()) {
				/* 
				 * Fazendo um controle para controlar a não repetição de
				 * instância de Department por meio do uso da estrutura Map.
				 * 
				 * Obtendo o id de um Department do ResultSet e passando
				 * como argumento para a função get da estrutura Map, afim
				 * de obtermos o Department correspondente a àquele id dentro
				 * da estrutura Map caso ele exista.
				 * 
				 * Com toda essa lógica feita abaixo, nós teremos o mesmo Department
				 * apontando para Sellers diferentes, garantindo assim a unicidade de
				 * Department associado aos Sellers.
				 * 
				 * usamos esse controle para evitar repetição de instâncias
			     * de Department por que um Department pode estar relacionado
			     * a mais de um Seller na consulta que está sendo feita, então 
			     * isso é feita para evitar instâncias de objetos Department desnessárias,
			     * uma vez que pode se tratar do mesmo Department relacionado a um Seller, quanto
			     * menor o número de instâncias, menor é o consumo de memória
			     * do programa, faz-se na verdade um reaproveitamento de
			     * objetos.
			    * */
				
				Department dept = map.get(rs.getInt("departmentId"));
				
				if(dept == null) {
					/*
					 * instanciando um Department apenas se ele ainda não
					 * estiver dentro da estrutura Map.
					 * */
					dept = instantiateDepartment(rs);
					
					map.put(rs.getInt("departmentId"), dept);
				}
				
				Seller sl = instantiateSeller(rs, dept);
				
				list.add(sl);
			}
			
			return list;
			
		} catch(SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(ps);
		}
	}

}
