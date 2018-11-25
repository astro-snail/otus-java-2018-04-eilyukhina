package ru.otus.l131.dao;

import java.sql.SQLException;
import java.util.List;

import ru.otus.l131.dao.base.BasicDAO;
import ru.otus.l131.dataset.PhoneDataSet;

public interface PhoneDAO extends BasicDAO<PhoneDataSet> {
	
	void save(PhoneDataSet dataSet) throws SQLException;

	PhoneDataSet load(Long id) throws SQLException;
	
	PhoneDataSet loadByNumber(String number) throws SQLException;

	List<PhoneDataSet> loadByUserId(Long userId) throws SQLException;
	
	List<PhoneDataSet> loadAll() throws SQLException;
	
	void delete(PhoneDataSet dataSet) throws SQLException;

}
