package org.callstack;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
	private final static Pattern pMethod = Pattern.compile("(\\S+)\\s*\\((.*)\\)");
	private final static Pattern pArray = Pattern.compile("\\[\\]");
	private final static Pattern pDigits = Pattern.compile("^\\d+$");
	private final static Pattern pAnonymousInnerClass = Pattern.compile("\\.(\\d+)");
	private final static Pattern pInnerClass = Pattern.compile("\\.([a-zA-Z_$][a-zA-Z\\d_$]*)$");
	
	private final static Map<String, Class<?>> primitives = new HashMap<>();
	static {
		primitives.put("byte", byte.class);
		primitives.put("char", char.class);
		primitives.put("short", short.class);
		primitives.put("int", int.class);
		primitives.put("long", long.class);
		primitives.put("boolean", boolean.class);
		primitives.put("float", float.class);
		primitives.put("double", double.class);
	}
	
	
	
	
	
	public static AccessibleObject getMethod(Class<?> clazz, String methodDesc) throws NoSuchMethodException, SecurityException {
		Matcher m = pMethod.matcher(methodDesc);
		if (!m.find()) {
			throw new IllegalArgumentException(methodDesc);
		}
		String[] methodNameFragments = m.group(1).split("\\.");
		String methodName = methodNameFragments[methodNameFragments.length - 1];
		
		String allParams = m.group(2);
		String[] methodParamNames = new String[0];
		if (!allParams.isEmpty()) {
			methodParamNames = m.group(2).split("\\s*,\\s*");
		}
		
		Class<?>[] methodParams = new Class[methodParamNames.length];
		for (int i = 0; i < methodParamNames.length; i++) {
			methodParams[i] = forName(methodParamNames[i]);
		}
		AccessibleObject method = fetchMethod(clazz, methodName, methodParams);
		
		return method;
	}
	
	//TODO: anonymous inner classes : org.junit.runner.manipulation.Sorter.1() - this is constructor call...
	static AccessibleObject fetchMethod(Class<?> clazz, String methodName, Class<?> ... parameterTypes) throws NoSuchMethodException {
		if (clazz == null) {
			throw new NullPointerException();
		}
		NoSuchMethodException exception = null;
		
		if (clazz.getSimpleName().equals(methodName) || pDigits.matcher(methodName).matches()) {
			return clazz.getDeclaredConstructor(parameterTypes);
		}
		
		try {
			return clazz.getMethod(methodName, parameterTypes);
		} catch (NoSuchMethodException e) {
			exception = e;
		}
		
		for (Class<?> c = clazz; c != null; c = c.getSuperclass()) {
			try {
				return c.getDeclaredMethod(methodName, parameterTypes);
			} catch (NoSuchMethodException e) {
				continue;
			}
		}
		throw exception;
	}
	
	
	
	private static Class<?> forName(String spec) {
		try {
			String name = pArray.matcher(spec).replaceAll("");
			int dim = (spec.length() - name.length()) / 2;
			
			Class<?> c = primitives.get(name);
			
			name = pAnonymousInnerClass.matcher(name).replaceAll("\\$$1");
			if (c == null) {
				try {
					c = Class.forName(name);
				} catch (ClassNotFoundException e) {
					// The class name passed here uses dot even to separate name of inner class from its outer class. 
					// For example name of class Inner defined into com.company.Outer is com.company.Outer.Inner and not com.company.Outer$Inner as it is required by Class.forName()
					// So, there is not way to understand where the full name of outer class ends and where the name of inner class starts. Indeed nesting depth of inner classes is unlimited.
					// This is the reason that we perform here several attempts to replace the last dot by $ and call forName() again.
					// If nothing helps the original exception is re-thrown. 
					for (String tryName = pInnerClass.matcher(name).replaceFirst("\\$$1"); tryName.contains("."); tryName = pInnerClass.matcher(tryName).replaceFirst("\\$$1")) {
						try {
							c = Class.forName(tryName);
						} catch (ClassNotFoundException e2) {
							continue;
						}
					}
					if (c == null) {
						throw e;
					}
				}
			}
			 
			if (dim > 0) {
				int[] dimensions = new int[dim];
				c = Array.newInstance(c, dimensions).getClass();
			}
			
			return c;
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException(spec, e);
		}
	}
}
