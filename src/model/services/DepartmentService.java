package model.services;

import java.util.ArrayList;
import java.util.List;

import model.entities.Department;

public class DepartmentService {
   
	public List<Department> findAll(){
		/*
		 * Numa primeira fase faremos um MOCK, que é retornar
		 * os dados de mentirinha, não serão os dados verdadeiros
		 * do banco dados.
		 * */
		List<Department> list = new ArrayList<>();
		list.add(new Department(1, "Books"));
		list.add(new Department(2, "Computers"));
		list.add(new Department(3, "Electronics"));
		
		return list;
	}
}
