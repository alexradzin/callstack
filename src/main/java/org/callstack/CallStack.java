package org.callstack;

import java.lang.reflect.AccessibleObject;
import java.util.ArrayList;
import java.util.List;

public class CallStack {
	private static ThreadLocal<List<CallStackElement<?>>> callStackElements = new ThreadLocal<>();
	private final static CallStackElement<?>[] typeMarkingArray = new CallStackElement[0];
	
	
	public static CallStackElement<?>[] getCallStack() {
		return callStackElements.get().toArray(typeMarkingArray);
	}
	
	static <F extends AccessibleObject> void pushCallStackElement(Class<?> declaringType, String fileName, int lineNumber, F function, Object[] args) {
		List<CallStackElement<?>> elements = callStackElements.get();
		CallStackElement<?> caller = null;
		if (elements == null) {
			elements =  new ArrayList<>();
			callStackElements.set(elements);
		} else if(elements.size() > 0) {
			caller = elements.get(0);
		}

		elements.add(0, new CallStackElement<F>(caller, Thread.currentThread(), declaringType, fileName, lineNumber, function, args));
	}
	
	static void removeCallStackElement() {
		List<CallStackElement<?>> elements = callStackElements.get();
		if(elements != null && !elements.isEmpty()) {
			elements.remove(0);
		}
	}
}
