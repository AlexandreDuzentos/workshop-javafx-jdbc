package model.dao;

import java.util.List;

import model.entities.Department;

/* o model compreende não só as entidades(entities), mas também as classes
 * que fazem transformações nessas entidades.
 * */
public interface DepartmentDao {
	/* operação que insere no banco de dados o objeto passado como parâmetro de entrada */
   void insert(Department obj);
   
   /* operação faz uma atualização no banco de dados */
   void update(Department obj);
   
   /* operação responsável por remover Department pelo id */
   void deleteById(Integer id);
   
   /* operação responsável por recuperar Departament pelo id */
   Department findById(Integer id);
   
   /* operação responsável por recuperar todos os Departments do banco de dados */
   List<Department> findAll();
 }
