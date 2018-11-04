package ru.otus.l081.writer;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonNumber;
import javax.json.JsonValue;

public class JsonObjectWriter {
	
	public String writeToJson(Object object) {
		try {
			JsonObject jsonObject = objectToJson(object);
			return jsonObject.toString();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private JsonValue elementToJson(Object element, Class<?> type) throws Exception {
		if (type.isPrimitive()) {
			return primitiveToJson(element);
		} else if (type.isArray()) {
			return arrayToJson(element);
		} else if (Collection.class.isAssignableFrom(type)) {
			return collectionToJson(element);
		} else if (Map.class.isAssignableFrom(type)) {
			return mapToJson(element);
		} else {
			return simpleObjectToJson(element);
		}
	}
	
	private JsonObject objectToJson(Object obj) throws Exception {
		JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
		
		Field[] fields = obj.getClass().getDeclaredFields();
		
		for (Field field : fields) {
			field.setAccessible(true);
		
			int mod = field.getModifiers();
			if (Modifier.isStatic(mod) || Modifier.isTransient(mod)) {
				continue;
			}
			objectBuilder.add(field.getName(), elementToJson(field.get(obj), field.getType()));
		}
		return objectBuilder.build();
	}
		
	private JsonArray arrayToJson(Object obj) throws Exception {
		JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
		
		Class<?> type = obj.getClass().getComponentType();
		
		int length = Array.getLength(obj);

		for (int i = 0; i < length; i++) {
			arrayBuilder.add(elementToJson(Array.get(obj, i), type));
		}
		return arrayBuilder.build();
	}

	private JsonArray collectionToJson(Object obj) throws Exception {
		JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
		
		Collection<?> collection = (Collection<?>)obj;
		
		for (Object element : collection) {
			arrayBuilder.add(elementToJson(element, element.getClass()));
		}
		return arrayBuilder.build();
	}
	
	private JsonObject mapToJson(Object obj) throws Exception {
		JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
				
		Map<?, ?> map = (Map<?, ?>)obj;
		
		for (Object key : map.keySet()) {
			Object value = map.get(key);
			objectBuilder.add(key.toString(), elementToJson(value, value.getClass()));
		}
		return objectBuilder.build();
	}
	
	private JsonValue primitiveToJson(Object obj) {
		return simpleObjectToJson(obj);
	}
	
	private JsonValue simpleObjectToJson(Object obj) {
		if (obj instanceof Number) {
			return numberToJson((Number)obj);			
		} else if (obj instanceof Boolean) {
			return booleanToJson((Boolean)obj);
		} else {
			return Json.createValue(obj.toString());
		}	
	}
	
	private JsonNumber numberToJson(Number obj) {
		if (obj instanceof Double || obj instanceof Float) {
			return Json.createValue(obj.doubleValue());
		} else if (obj instanceof Long) {
			return Json.createValue(obj.longValue());
		} else { 
			return Json.createValue(obj.intValue());
		} 
	}
	
	private JsonValue booleanToJson(Boolean obj) {
		return obj.equals(true) ? JsonValue.TRUE : JsonValue.FALSE;
	}
	
}

