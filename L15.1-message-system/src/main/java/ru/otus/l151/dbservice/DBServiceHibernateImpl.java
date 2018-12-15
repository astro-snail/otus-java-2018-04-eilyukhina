package ru.otus.l151.dbservice;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;

import ru.otus.l151.app.MessageSystemContext;
import ru.otus.l151.cache.*;
import ru.otus.l151.dao.*;
import ru.otus.l151.dataset.*;
import ru.otus.l151.messagesystem.Address;
import ru.otus.l151.messagesystem.MessageSystem;

public class DBServiceHibernateImpl implements DBService {
	
	private final MessageSystemContext context;
	private final SessionFactory sessionFactory;
	private final Cache cache;

    public DBServiceHibernateImpl(MessageSystemContext context, Cache cache) {
        this.sessionFactory = new Configuration().configure().buildSessionFactory();
       	this.cache = cache;
       	this.context = context;
    }
    
   	@Override
	public void save(UserDataSet user) throws SQLException {
		try (Session session = sessionFactory.openSession()) {
			Transaction transaction = session.beginTransaction();
            UserDAO userDao = new UserDAOHibernateImpl(session);
            userDao.save(user);            
            transaction.commit();
        }
	}

	@Override
	public void delete(UserDataSet user) throws SQLException {
		try (Session session = sessionFactory.openSession()) {
			Transaction transaction = session.beginTransaction();	
            UserDAO dao = new UserDAOHibernateImpl(session);
            dao.delete(user);
            transaction.commit();
        }
	}
	
	@Override
	public UserDataSet load(Long id) throws SQLException {
		UserDataSet user = fromCache(UserDataSet.class, id);
		if (user == null) {
			try (Session session = sessionFactory.openSession()) {
				Transaction transaction = session.beginTransaction();
				UserDAO dao = new UserDAOHibernateImpl(session);
				user = dao.load(id);
				transaction.commit();
			}
			toCache(user);
		}
		return user;
	}
	
	@Override
	public UserDataSet loadByName(String name) throws SQLException {
		try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            UserDAO dao = new UserDAOHibernateImpl(session);
            UserDataSet user = dao.loadByName(name);
            transaction.commit();
            toCache(user);
            return user;
        }
	}
	
	@Override
	public List<UserDataSet> loadAll() throws SQLException {
		try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            UserDAO dao = new UserDAOHibernateImpl(session);
            List<UserDataSet> users = dao.loadAll();
            transaction.commit();
            toCache(users);
            return users;
        }
	}
	
	@Override
	public PhoneDataSet loadPhoneById(Long id) throws SQLException {
		PhoneDataSet phone = fromCache(PhoneDataSet.class, id);
		if (phone == null) {
			try (Session session = sessionFactory.openSession()) {
				Transaction transaction = session.beginTransaction();
				PhoneDAO dao = new PhoneDAOHibernateImpl(session);
				phone = dao.load(id);
				transaction.commit();
				toCache(phone);
			}
		}	
		return phone;
	}
	
	@Override
	public AddressDataSet loadAddressById(Long id) throws SQLException {
		AddressDataSet address = fromCache(AddressDataSet.class, id);
		if (address == null) {
			try (Session session = sessionFactory.openSession()) {
				Transaction transaction = session.beginTransaction();
				AddressDAO dao = new AddressDAOHibernateImpl(session);
				address = dao.load(id);
				transaction.commit();
				toCache(address);
			}
		}	
		return address;
	}
	
	@Override
	public AddressDataSet loadAddressByUserId(Long userId) throws SQLException {
		try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            AddressDAO dao = new AddressDAOHibernateImpl(session);
            AddressDataSet address = dao.loadByUserId(userId);
            transaction.commit();
            toCache(address);
            return address;
        }
	}
	
	@Override
	public List<PhoneDataSet> loadPhonesByUserId(Long userId) throws SQLException {
		try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            PhoneDAO dao = new PhoneDAOHibernateImpl(session);
            List<PhoneDataSet> phones = dao.loadByUserId(userId);
            transaction.commit();
            toCache(phones);
            return phones;
        }
	}
	
	@Override
	public void shutdown() {
		sessionFactory.close();
		cache.dispose();
	}
	
	@SuppressWarnings("unchecked")
	private <T extends DataSet> Class<T> getClassForHibernateObject(T dataSet) {
		if (dataSet instanceof HibernateProxy) {
		    LazyInitializer lazyInitializer = ((HibernateProxy)dataSet).getHibernateLazyInitializer();
		    return (Class<T>)lazyInitializer.getPersistentClass();
		} else {
		    return (Class<T>)dataSet.getClass();
		}
	}
	
	@SuppressWarnings("unchecked")
	private <T extends DataSet> T fromCache(Class<T> type, Long id) {
		DataSetKey cacheKey = new DataSetKey(type, id);
		Element element = cache.get(cacheKey);
		return element != null ? (T)element.getValue() : null;
	}
	
	private <T extends DataSet> void toCache(T dataSet) {
		if (dataSet == null) {
			return;
		}
		DataSetKey cacheKey = new DataSetKey(getClassForHibernateObject(dataSet), dataSet.getId());
		Element element = new Element(cacheKey, dataSet);
		cache.put(element);
	}

	private <T extends DataSet> void toCache(List<T> dataSets) {
		for (T dataSet : dataSets) {
			toCache(dataSet);
		}
	}
	
	@Override
	public void init() {
		context.registerDBService(this);		
	}

	@Override
	public Address getAddress() {
		return context.getDBServiceAddress();
	}
	
	@Override
	public MessageSystem getMessageSystem() {
		return context.getMessageSystem();
	}

	@Override
	public Map<String, String> getCacheParameters() {
		Map<String, String> cacheParameters = new HashMap<>();
		
		Properties cacheProperties = cache.getProperties();
		for (Object key : cacheProperties.keySet()) {
			cacheParameters.put(key.toString(), cacheProperties.get(key).toString());
		}
		cacheParameters.put("size", String.valueOf(cache.getSize()));
		cacheParameters.put("hitCount", String.valueOf(cache.getHitCount()));
		cacheParameters.put("missCount", String.valueOf(cache.getMissCount()));
		
		return cacheParameters;
	}
}
