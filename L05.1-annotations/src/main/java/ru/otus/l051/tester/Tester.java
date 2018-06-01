package ru.otus.l051.tester;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ru.otus.l051.annotations.*;

public class Tester {
	
	private Class<?> testClass;
	private List<Method> methodsBefore = new ArrayList<>();
	private List<Method> methodsAfter = new ArrayList<>();
	private List<Method> methodsTest = new ArrayList<>();
	
	private Tester(Class<?> testClass) {
		this.testClass = testClass;
		findMethodsToRun();
	}
	
	private void invokeInstanceMethod(Object obj, Method method) throws IllegalAccessException, InvocationTargetException {
		Class<?>[] paramTypes = method.getParameterTypes();
		Object[] params = new Object[paramTypes.length];
		
		method.setAccessible(true);
		method.invoke(obj, params);
	}
	
	private void findMethodsToRun() {
		
		Method[] methods = testClass.getDeclaredMethods();
				
		for (Method method : methods) {
			
			Annotation[] annotations = method.getAnnotations();
			
			for (Annotation annotation : annotations) {
				if (annotation.annotationType() == Before.class) { 
					methodsBefore.add(method);
				} 
				if (annotation.annotationType() == Test.class) { 
					methodsTest.add(method);
				} 
				if (annotation.annotationType() == After.class) { 
					methodsAfter.add(method);
				} 
			}
		}
	}
	
	private void runTests() {
		
		Object obj = null;
		
		try {
			obj = testClass.newInstance();
				
			for (Method method : methodsTest) {
				for (Method methodBefore : methodsBefore) {
					invokeInstanceMethod(obj, methodBefore);
				}
				invokeInstanceMethod(obj, method);
				for (Method methodAfter : methodsAfter) {
					invokeInstanceMethod(obj, methodAfter);
				}
			}	
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	public static void runTestsForClass(String className) throws ClassNotFoundException {
		Tester tester = new Tester(Class.forName(className));
		tester.runTests();		
	}
	
	public static void runTestsForPackage(String packageName) throws IOException, ClassNotFoundException {
		for (Class<?> classUnderTest : getClassesInPackage(packageName)) {
			runTestsForClass(classUnderTest.getName());
		}
	}
	
	private static List<Class<?>> getClassesInPackage(String packageName) throws ClassNotFoundException, IOException {
		/**
		 * Credit: https://dzone.com/articles/get-all-classes-within-package
		 */
		String path = packageName.replace('.', '/');
		
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		URL resource = classLoader.getResource(path);
		File directory = new File(resource.getFile());
		
		return findClassesInDirectory(directory, packageName);
	}
	
	private static List<Class<?>> findClassesInDirectory(File directory, String packageName) throws ClassNotFoundException {
		/**
		 * Credit: https://dzone.com/articles/get-all-classes-within-package
		 */
		List<Class<?>> classes = new ArrayList<>();
		
		if (!directory.exists()) {
			return classes;
		}
		
		File[] files = directory.listFiles();
		
		for (File file : files) {
			if (file.isDirectory()) {
				classes.addAll(findClassesInDirectory(file, packageName + '.' + file.getName()));
			} else if (file.getName().endsWith(".class")){
				classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().lastIndexOf('.'))));
			}
		}
		
		return classes;
	}
}
