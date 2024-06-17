package model.dao;

import db.DB;
import model.dao.impl.DepartmentDaoJDBC;
import model.dao.impl.SellerDaoJDBC;

public class DaoFactory {

	/* método utilitário responsável por instanciar a implementação do
	 * SellerDao e retorna-la, isso é um macete para não precisar expor
	 * a implementação(SellerDaoJDBC) no seu consumidor da classe SellerDaoJDBC
	 * e de deixar somente a interface, o consumidor conhecerá somente a interface,
	 * e é também uma forma de fazer uma injeção de dependência sem explicitar
	 * a implementação.
	 *  */
	public static SellerDao createSellerDao() {
		return new SellerDaoJDBC(DB.getConnection());
	}
	
	public static DepartmentDao createDepartmentDao() {
		return new DepartmentDaoJDBC(DB.getConnection());
	}
}
