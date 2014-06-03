package org.callstack;


import static org.junit.Assert.assertEquals;

import java.io.Serializable;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.junit.Test;

public class UtilTest {
	@Test
	public void getNoArgsMethod() throws NoSuchMethodException {
		test("void foo()", getClass().getMethod("foo"));
	}

	@Test(expected = NoSuchMethodException.class)
	public void getNoNotExistingMethod() throws NoSuchMethodException {
		test("void doesNotExist()", null);
	}
	
	@Test
	public void getOneByteArgMethod() throws NoSuchMethodException {
		test(Method.class, "void foo(byte)", "foo", byte.class);
	}
	
	@Test
	public void getOneCharArgMethod() throws NoSuchMethodException {
		test(Method.class, "void foo(char)", "foo", char.class);
	}

	@Test
	public void getOneIntArgMethod() throws NoSuchMethodException {
		test(Method.class, "void foo(int)", "foo", int.class);
	}

	@Test
	public void getOneLongArgMethod() throws NoSuchMethodException {
		test(Method.class, "void foo(long)", "foo", long.class);
	}

	@Test
	public void getOneFloatArgMethod() throws NoSuchMethodException {
		test(Method.class, "void foo(float)", "foo", float.class);
	}

	@Test
	public void getOneDoubleArgMethod() throws NoSuchMethodException {
		test(Method.class, "void foo(double)", "foo", double.class);
	}

	@Test
	public void getOneStringArrayArgMethod() throws NoSuchMethodException {
		test(Method.class, "void foo(java.lang.String[])", "foo", String[].class);
	}
	
	@Test
	public void getIntStringArgsMethod() throws NoSuchMethodException {
		test(Method.class, "void foo(int, java.lang.String)", "foo", int.class, String.class);
	}
	
	@Test
	public void getOneDoubleArrayArgMethod() throws NoSuchMethodException {
		test(Method.class, "void foo(double[])", "foo", double[].class);
	}

	@Test
	public void getOneSerializableArgMethod() throws NoSuchMethodException {
		test(Method.class, "void foo(java.io.Serializable)", "foo", Serializable.class);
	}

	@Test
	public void getPublicSuperclassMethod() throws NoSuchMethodException {
		test(Method.class, "public java.lang.String toString()", "toString");
	}

	@Test
	public void getProtectedSuperclassMethod() throws NoSuchMethodException {
		test(Method.class, "protected java.lang.Object clone()", "clone");
	}

	@Test
	public void getPrivateSuperclassMethod() throws NoSuchMethodException {
		test(Method.class, "private static native void registerNatives()", "registerNatives");
	}
	
	@Test
	public void getPublicNoArgConstructor() throws NoSuchMethodException {
		test(Constructor.class, String.class, "public java.lang.String()", "String");
	}

	@Test
	public void getPublicCopyConstructor() throws NoSuchMethodException {
		test(Constructor.class, String.class, "public java.lang.String(java.lang.String)", "String", String.class);
	}

	@Test
	public void getPublicConstructorCharArrArg() throws NoSuchMethodException {
		test(Constructor.class, String.class, "public java.lang.String(char[])", "String", char[].class);
	}

	@Test
	public void getPublicConstructorCharArrArgIntInt() throws NoSuchMethodException {
		test(Constructor.class, String.class, "public java.lang.String(char[], int, int)", "String", char[].class, int.class, int.class);
	}

	@Test
	public void getPackagePrivateConstructorCharArrBoolean() throws NoSuchMethodException {
		test(Constructor.class, String.class, "public java.lang.String(char[], boolean)", "String", char[].class, boolean.class);
	}

	@Test
	public void getConstructorOfAnonymousInnerClass() throws NoSuchMethodException {
		test(Constructor.class, RUNNABLE.getClass(), getClass().getName() + ".1()", "1");
	}

	
	@Test
	public void getPublicMethodIntArr3D() throws NoSuchMethodException {
		test(Method.class, "public void foo(int[][][])", "foo", int[][][].class);
	}

	private <C> void test(Class<C> functionType, String desc, String methodName, Class<?> ... parameterTypes) throws NoSuchMethodException {
		test(functionType, getClass(), desc, methodName, parameterTypes);
	}
	
	private <C> void test(Class<C> functionType, Class<?> clazz, String desc, String methodName, Class<?> ... parameterTypes) throws NoSuchMethodException {
		AccessibleObject expected = Util.fetchMethod(clazz, methodName, parameterTypes);
		AccessibleObject actual = Util.getMethod(clazz, desc);
		assertEquals(expected, actual);
		assertEquals(functionType, actual.getClass());
	}
	

	private void test(String desc, Method expected) throws NoSuchMethodException {
		assertEquals(expected, Util.getMethod(getClass(), desc));
	}
	
	public void foo() {
		// dummy method for tests
	}

	public void foo(int i) {
		// dummy method for tests
	}

	public void foo(long i) {
		// dummy method for tests
	}

	public void foo(byte b) {
		// dummy method for tests
	}
	
	public void foo(char c) {
		// dummy method for tests
	}

	public void foo(short s) {
		// dummy method for tests
	}

	public void foo(float f) {
		// dummy method for tests
	}
	
	public void foo(double d) {
		// dummy method for tests
	}
	
	public void foo(double[] d) {
		// dummy method for tests
	}
	
	public void foo(Serializable s) {
		// dummy method for tests
	}
	
	public void foo(int i, String s) {
		// dummy method for tests
	}
	
	public void foo(String[] arr) {
		// dummy method for tests
	}
	
	public void foo(int[][][] arr3) {
		// dummy method for tests
	}
	
	private final static Runnable RUNNABLE = new Runnable() {
		@Override
		public void run() {
			// dummy method for tests
		}
	};
}
