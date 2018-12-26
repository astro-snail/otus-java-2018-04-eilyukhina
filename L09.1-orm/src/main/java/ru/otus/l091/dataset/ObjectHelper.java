package ru.otus.l091.dataset;

import java.lang.reflect.Field;
import java.sql.SQLException;

public class ObjectHelper {

	public static <T> Field getFieldByName(T object, String name) throws SQLException {
		Field field = null;
		try {
			try {
				field = object.getClass().getDeclaredField(name);
			} catch (Exception e) {
				field = object.getClass().getSuperclass().getDeclaredField(name);
			}
		} catch (Exception e) {
			throw new SQLException(e);
		}
		return field;
	}

}
