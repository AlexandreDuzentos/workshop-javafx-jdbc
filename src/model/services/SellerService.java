package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Seller;

public class SellerService {
	private SellerDao sellerDao = DaoFactory.createSellerDao();
	   
	public List<Seller> findAll(){
		/*
		 * Numa primeira fase faremos um MOCK, que é retornar
		 * os dados de mentirinha, não serão os dados verdadeiros
		 * do banco dados.
		 * */
		
		/*
		List<Seller> list = new ArrayList<>();
		list.add(new Seller(1, "Books"));
		list.add(new Seller(2, "Computers"));
		list.add(new Seller(3, "Electronics"));
		*/
		
		return sellerDao.findAll();
	}
	
	public void saveOrUpdate(Seller seller) {
		/* Se essa condição for verdadeira, quer dizer
		 * que é uma inserção de um Deparment */
		if(seller.getId() == null) {
			sellerDao.insert(seller);
		} else {
			sellerDao.update(seller);
		}
		
	}
	
	public void remove(Seller obj) {
		sellerDao.deleteById(obj.getId());
	}
	
}
