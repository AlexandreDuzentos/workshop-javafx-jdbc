package model.services;

import java.util.ArrayList;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentService {
	
	private DepartmentDao depDao = DaoFactory.createDepartmentDao();
   
	public List<Department> findAll(){
		/*
		 * Numa primeira fase faremos um MOCK, que é retornar
		 * os dados de mentirinha, não serão os dados verdadeiros
		 * do banco dados.
		 * */
		
		/*
		List<Department> list = new ArrayList<>();
		list.add(new Department(1, "Books"));
		list.add(new Department(2, "Computers"));
		list.add(new Department(3, "Electronics"));
		*/
		
		return depDao.findAll();
	}
	
	public void saveOrUpdate(Department dep) {
		/* Se essa condição for verdadeira, quer dizer
		 * que é uma inserção de um Deparment */
		if(dep.getId() == null) {
			depDao.insert(dep);
		} else {
			depDao.update(dep);
		}
		
	}
	
	public void remove(Department obj) {
		depDao.deleteById(obj.getId());
	}
}
