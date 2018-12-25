package ru.otus.l151.dao;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;

import ru.otus.l151.dataset.AddressDataSet;

public class AddressDAOHibernateImpl implements AddressDAO {

	private final Session session;

	public AddressDAOHibernateImpl(final Session session) {
		this.session = session;
	}

	@Override
	public void save(AddressDataSet dataSet) {
		session.saveOrUpdate(dataSet);
	}

	@Override
	public AddressDataSet load(Long id) {
		return session.load(AddressDataSet.class, id);
	}

	@Override
	public AddressDataSet loadByStreet(String street) {
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<AddressDataSet> criteria = builder.createQuery(AddressDataSet.class);
		Root<AddressDataSet> from = criteria.from(AddressDataSet.class);
		criteria.where(builder.equal(from.get("street"), street));
		Query<AddressDataSet> query = session.createQuery(criteria);
		return query.uniqueResult();
	}

	@Override
	public AddressDataSet loadByUserId(Long userId) {
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<AddressDataSet> criteria = builder.createQuery(AddressDataSet.class);
		Root<AddressDataSet> from = criteria.from(AddressDataSet.class);
		criteria.where(builder.equal(from.get("user"), userId));
		Query<AddressDataSet> query = session.createQuery(criteria);
		return query.uniqueResult();
	}

	@Override
	public List<AddressDataSet> loadAll() {
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<AddressDataSet> criteria = builder.createQuery(AddressDataSet.class);
		criteria.from(AddressDataSet.class);
		return session.createQuery(criteria).list();
	}

	@Override
	public void delete(AddressDataSet dataSet) {
		session.delete(dataSet);
	}

}
