package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DBException;
import model.dao.DepartmentDao;
import model.entities.Department;
import model.entities.Seller;

public class DepartmentDaoJDBC  implements DepartmentDao {
	
	private Connection conn;
	
	public DepartmentDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	/* operação responsável por inserir um novo Department no banco de dados */
	@Override
	public void insert(Department obj) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conn.prepareStatement("insert into department "
					+ "(name) "
					+"values "
					+"(?)",
					Statement.RETURN_GENERATED_KEYS);
			
			ps.setString(1, obj.getName());
			
			int rowsAffected = ps.executeUpdate();
			
			if(rowsAffected > 0) {
				rs = ps.getGeneratedKeys();
				/* É um if porque um único Department foi inserido, logo,
				 * um único id será retornado caso a transação ocorra com
				 * sucesso e o if não testa a condição mais de uma vez por execução.
				 * */
				if(rs.next()) {
					int id = rs.getInt(1); // obtendo o id do último Department inserido.
					/* 
					 * populando o objeto Department com o seu novo id correspondente
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

	/* operação responsável por atualizar um Department do banco de dados */
	@Override
	public void update(Department obj) {
			PreparedStatement ps = null;
			
			try {
				ps = conn.prepareStatement("update department "
						+ "set Id = ?,Name = ?" 
						+ "where Id = ?"
					     );
				
				ps.setInt(1, obj.getId());
				ps.setString(2, obj.getName());
				ps.setInt(3, obj.getId());
				
				ps.executeUpdate();
				
			} catch(SQLException e) {	
				throw new DBException(e.getMessage());
			} finally {
				DB.closeStatement(ps);
			}
		
	}

	/* Operação responsável por remover um Department pelo id */
	@Override
	public void deleteById(Integer id) {
			PreparedStatement ps = null;
			
			try {
				ps = conn.prepareStatement("delete from department where id = ?");
				
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

	/* Operação responsável por recuperar um Deparment pelo id */
	@Override
	public Department findById(Integer id) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conn.prepareStatement("SELECT * from department "
					  + "where Id = ?"
			     );
			
			ps.setInt(1, id);
			
			rs = ps.executeQuery();
			
			if(rs.next()) {
				Department dept = instantiateDepartment(rs);
				
				return dept;
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

	/* operação responsável por recuperar todos os Departments do banco de dados */
	@Override
	public List<Department> findAll() {
		 PreparedStatement ps = null;
		 ResultSet rs = null;
		   
		   try {
			     ps = conn.prepareStatement(
					   "SELECT * FROM department "
				 ); 
			   
			   rs = ps.executeQuery();
			   
			   List<Department> list = new ArrayList<>();
			   
			   while(rs.next()) {
				   Department dept = instantiateDepartment(rs);
				   list.add(dept);
			   }
			 
			   return list;
		   } catch(SQLException e) {
			   throw new DBException(e.getMessage());
		   } finally {
			   DB.closeStatement(ps);
			   DB.closeResultSet(rs);
		   }
	}
	
	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department dept = new Department();
		dept.setId(rs.getInt("Id"));
		dept.setName(rs.getString("Name"));
		
		return dept;
	}

}
