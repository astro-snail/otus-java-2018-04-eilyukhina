package ru.otus.l151.dao.base;

import java.sql.SQLException;
import java.util.List;

import ru.otus.l151.dataset.DataSet;

public interface BasicDAO<T extends DataSet> {

	void save(T dataSet) throws SQLException;

	T load(Long id) throws SQLException;

	List<T> loadAll() throws SQLException;

	void delete(T dataSet) throws SQLException;

}
