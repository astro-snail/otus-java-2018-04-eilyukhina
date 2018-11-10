package ru.otus.l091.executor;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import ru.otus.l091.dataset.DataSet;
import ru.otus.l091.dataset.ObjectHelper;
import ru.otus.l091.handler.Handler;
import ru.otus.l091.handler.ResultSetHandler;

public class QueryExecutor {
	private static final String SELECT_USER = "SELECT * FROM users WHERE id = ?";
	private static final String UPDATE_USER = "UPDATE users SET name = ?, age = ? WHERE id = ?";
	private static final String INSERT_USER = "INSERT INTO users (name, age) VALUES (?, ?)";

	private final Connection connection;

    public QueryExecutor(Connection connection) {
        this.connection = connection;
    }

    private <T extends DataSet> T query(String sql, ResultSetHandler<T> handler, Object... params) throws SQLException {
		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			prepare(stmt, params);
			ResultSet rs = stmt.executeQuery();
			return handler.handle(rs);
		}
    }

    private <T extends DataSet> int update(String sql, T user, String... names) throws SQLException {
    	try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			prepare(stmt, getParams(user, names));
			int updates = stmt.executeUpdate();
			if (updates == 0) {
				throw new SQLException("User not updated");
			}
			return updates;
		}
    }

    private <T extends DataSet> int insert(String sql, T user, String... names) throws SQLException {
    	try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			prepare(stmt, getParams(user, names));
			int inserts = stmt.executeUpdate();
			if (inserts == 0) {
				throw new SQLException("User not created");
			}
			try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					user.setId(generatedKeys.getLong(1));
				} else {
					throw new SQLException("User ID not generated");
				}
			}
			return inserts;
		}
    }

    private void prepare(PreparedStatement stmt, Object... params) throws SQLException {
    	ParameterMetaData metadata = stmt.getParameterMetaData();
    	int count = metadata.getParameterCount();
    	if (count != params.length) {
    		throw new SQLException("Wrong number of parameters");
    	}
    	for (int i = 0; i < count; i++) {
    		stmt.setObject(i + 1, params[i]);
    	}
    }
    
    private Object[] getParams(Object obj, String[] names) throws SQLException {
    	Object[] params = new Object[names.length];
    	try {
    		for (int i = 0; i < names.length; i++) {
    			Field field = ObjectHelper.getFieldByName(obj, names[i]);
    			field.setAccessible(true);
    			params[i] = field.get(obj);
    		}
    	} catch (Exception e) {
    		throw new SQLException(e);
    	}
    	return params;
    }

	public <T extends DataSet> void save(T user) throws SQLException {
		if (!exists(user)) {
			insert(INSERT_USER, user, "name", "age");
		} else {
			update(UPDATE_USER, user, "name", "age", "id");
		}
	}

	public <T extends DataSet> T load(long id, Class<T> type) throws SQLException {
		ResultSetHandler<T> handler = new Handler<T>(type);
		return query(SELECT_USER, handler, id);
	}

	private <T extends DataSet> boolean exists(T user) throws SQLException {
		return load(user.getId(), user.getClass()) != null;
	}

}

