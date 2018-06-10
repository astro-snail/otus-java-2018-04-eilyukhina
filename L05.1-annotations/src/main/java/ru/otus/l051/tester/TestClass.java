package ru.otus.l051.tester;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestClass {
	
	private final Class<?> testClass;
	
	private final Map<Class<? extends Annotation>, List<Method>> annotatedMethods; 
	
	public TestClass(Class<?> testClass) {
		this.testClass = testClass;
		
		if (testClass != null && testClass.getConstructors().length > 1) {
			throw new IllegalArgumentException(testClass.getName() + ": test class can only have one constructor");
		}
		
		Map<Class<? extends Annotation>, List<Method>> map = new HashMap<>();
		findAnnotatedMethods(map);
		
		annotatedMethods = Collections.unmodifiableMap(map);
	}
	

	private void findAnnotatedMethods(Map<Class<? extends Annotation>, List<Method>> annotatedMethods) {
        for (Method method : testClass.getDeclaredMethods()) {
        	
        	for (Annotation annotation : method.getAnnotations()) {
               
        		Class<? extends Annotation> type = annotation.annotationType();
                if (!annotatedMethods.containsKey(type)) {
                	annotatedMethods.put(type, new ArrayList<Method>());
                }
                
        		List<Method> methods = annotatedMethods.get(type);
        		methods.add(method);
        	}	
        }
    }
	
	public Class<?> getTestClass() {
		return testClass;
	}

	public Constructor<?> getConstructor() throws NoSuchMethodException {
		return testClass.getConstructor();
	}
	
	public List<Method> getAnnotatedMethods(Class<? extends Annotation> type) {
		if (!annotatedMethods.containsKey(type)) {
			return new ArrayList<Method>();
		}
		return annotatedMethods.get(type);
	}
}
