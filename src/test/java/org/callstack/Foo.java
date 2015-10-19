package org.callstack;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.AccessibleObject;


public class Foo {
	private static final Class<?>[] NO_PARAMS = new Class[0];
	private static final Object[] NO_ARGS = new Object[0];
	private static final int UNKNOWN_HASH_CODE = -1;
	
	private Object testCase;
	
	public Foo() {
	}
	
	public Foo(Object testCase) {
		this.testCase = testCase;
	}

	public Foo(boolean dummy) {
		assertCallStackElement(0, getClass(), hashCode(), "<init>", new Class<?>[] {boolean.class}, new Object[] {true});
		assertCallStackElement(1, CallStackTest.class, UNKNOWN_HASH_CODE, "constructorBooleanArg", NO_PARAMS, NO_ARGS);
	}
	
	
	public void foo() throws NoSuchMethodException {
		assertCallStack();
		assertCallStackElement(0, getClass(), hashCode(), "foo", NO_PARAMS, NO_ARGS);
		assertCallStackElement(1, CallStackTest.class, testCase.hashCode(), "voidNoArgs", NO_PARAMS, NO_ARGS);
	}
	
	public void foo(int[][] arr3) {
		assertCallStack();
		assertCallStackElement(0, getClass(), hashCode(), "foo", new Class[] {int[][].class}, new Object[] {new int[][]{{1,2,3}, {4,5,6}}});
		assertCallStackElement(1, CallStackTest.class, testCase.hashCode(), "void2dArr", NO_PARAMS, NO_ARGS);
	}

	public static void staticFoo() throws NoSuchMethodException {
		assertCallStack();
		assertCallStackElement(0, Foo.class, null, "staticFoo", NO_PARAMS, NO_ARGS);
		assertCallStackElement(1, CallStackTest.class, UNKNOWN_HASH_CODE, "staticVoidNoArgs", NO_PARAMS, NO_ARGS);
	}
	
	private static AccessibleObject getMethod(Class<?> type, String methodName, Class<?>... parameterTypes) {
		try {
			if (type.getSimpleName().equals(methodName) || "<init>".equals(methodName)) {
				return type.getConstructor(parameterTypes);
			}
			return type.getMethod(methodName, parameterTypes);
		} catch (NoSuchMethodException e) {
			throw new IllegalArgumentException(e);
		}
	}

	private static void assertCallStack() {
		CallStackElement<?>[] callstack = CallStack.getCallStack();
		assertNotNull(callstack);
		assertTrue(callstack.length > 3); //3: 1 - this method, 2 - method being tested, 3 - unit test
	}
	
	private static void assertCallStackElement(
			int n, 
			Class<?> clazz, 
			Integer expectedThisHashCode,
			String functionName, 
			Class<?>[] expectedParamTypes, 
			Object[] expeectedArguments) {
		CallStackElement<?>[] callstack = CallStack.getCallStack();
		assertCallStackElement(callstack, n + 1, clazz, expectedThisHashCode, functionName, expectedParamTypes, expeectedArguments); // +1 to take into consideration this method.
	}

	private static <F extends AccessibleObject> void assertCallStackElement(
			CallStackElement<?>[]callstack, 
			int n, 
			Class<?> clazz, 
			Integer expectedThisHashCode,
			String functionName, 
			Class<?>[] expectedParamTypes, 
			Object[] expeectedArguments) {

		@SuppressWarnings("unchecked")
		CallStackElement<F> callstackElement = (CallStackElement<F>)callstack[n];
		@SuppressWarnings("unchecked")
		F function = (F)getMethod(clazz, functionName);
		@SuppressWarnings("unchecked")
		Class<F> functionType = (Class<F>)function.getClass();
		
		assertCallStackElement(
				callstackElement, 
				clazz,
				expectedThisHashCode, 
				functionName, 
				functionType, 
				expectedParamTypes,
				expeectedArguments);		
	}

	private static <F extends AccessibleObject> void assertCallStackElement(
			CallStackElement<F> callstackElement, 
			Class<?> expectedDeclaredType,
			Integer expectedThisHashCode,
			String expectedFunctionName, 
			Class<F> expectedFunctionType, 
			Class<?>[] expectedParamTypes, 
			Object[] expeectedArguments) {
		
		if (expectedThisHashCode == null) {
			assertNull(callstackElement.getThis());
		} else if(expectedThisHashCode == UNKNOWN_HASH_CODE) {
			assertNotNull(callstackElement.getThis());
		} else {
			assertEquals(expectedThisHashCode.intValue(), callstackElement.getThis().hashCode());
		}
		assertEquals(expectedDeclaredType, callstackElement.getDeclaringType());
		assertEquals(getMethod(expectedDeclaredType, expectedFunctionName, expectedParamTypes), callstackElement.getFunction());
		assertArrayEquals(expeectedArguments, callstackElement.getArgs());
		
		StackTraceElement stackTraceElement = callstackElement.getStackTraceElement();
		assertEquals(expectedDeclaredType.getName(), stackTraceElement.getClassName());
		assertEquals(expectedFunctionName, stackTraceElement.getMethodName());
		
	}
}
