package org.callstack;

import org.junit.Ignore;
import org.junit.Test;

public class CallStackTest {
	@Test
	public void voidNoArgs() throws NoSuchMethodException {
		new Foo().foo();
	}

	@Test
	public void void2dArr() throws NoSuchMethodException {
		new Foo().foo(new int[][]{{1,2,3}, {4,5,6}});
	}

	@Test
	public void staticVoidNoArgs() throws NoSuchMethodException {
		Foo.staticFoo();
	}

	@Test
	@Ignore("Constructors are not supported now. FIXME")
	public void constructorBooleanArg() throws NoSuchMethodException {
		new Foo(true);
	}
	
	public void foo() {
		new Throwable().printStackTrace();
	}
}
