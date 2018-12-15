package ru.otus.l151.dao;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;

import ru.otus.l151.dataset.PhoneDataSet;

public class PhoneDAOHibernateImpl implements PhoneDAO {
	
	private final Session session;
	
	public PhoneDAOHibernateImpl(final Session session) {
		this.session = session;
	}
	
	@Override
	public void save(PhoneDataSet dataSet) {
		session.saveOrUpdate(dataSet);
	}

	@Override
	public PhoneDataSet load(Long id) {
		return session.load(PhoneDataSet.class, id);
	}

	@Override
	public PhoneDataSet loadByNumber(String number) {
		CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<PhoneDataSet> criteria = builder.createQuery(PhoneDataSet.class);
        Root<PhoneDataSet> from = criteria.from(PhoneDataSet.class);
        criteria.where(builder.equal(from.get("number"), number));
        Query<PhoneDataSet> query = session.createQuery(criteria);
        return query.uniqueResult();
	}

	@Override
	public List<PhoneDataSet> loadByUserId(Long userId) {
		CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<PhoneDataSet> criteria = builder.createQuery(PhoneDataSet.class);
        Root<PhoneDataSet> from = criteria.from(PhoneDataSet.class);
        criteria.where(builder.equal(from.get("user"), userId));
        Query<PhoneDataSet> query = session.createQuery(criteria);
        return query.list();
	}

	@Override
	public List<PhoneDataSet> loadAll() {
		CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<PhoneDataSet> criteria = builder.createQuery(PhoneDataSet.class);
        criteria.from(PhoneDataSet.class);
        return session.createQuery(criteria).list();
	}

	@Override
	public void delete(PhoneDataSet dataSet) {
		session.delete(dataSet);
	}

}
