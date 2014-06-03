package org.callstack;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class CallStackElement<F extends AccessibleObject>  {
	private final CallStackElement<?> caller;
	private final Thread thread;
    private final Class<?> declaringType;
    private final F function;
    private final Object[] args;
    private final String fileName;
    private final int lineNumber;
    private final StackTraceElement stackTraceElement;
    
    
	public CallStackElement(CallStackElement<?> caller, Thread thread, Class<?> declaringType, String fileName, int lineNumber, F function, Object[] args) {
		super();
		this.caller = caller;
		this.thread = thread;
		this.declaringType = declaringType;
		this.fileName = fileName;
		this.lineNumber = lineNumber;
		this.function = function;
		this.args = args;

		final String functionName;
		if (function != null) {
			if (function instanceof Method) {
				functionName = ((Method)function).getName();
			} else if (function instanceof Constructor<?>) {
				functionName = "<init>";
			} else {
				throw new IllegalArgumentException(function.getClass().getName());
			}
		} else {
			// this may happen if declaringType is a dynamic proxy
			functionName = "n/a";
		}
		
		
		this.stackTraceElement = new StackTraceElement(declaringType.getName(), functionName, fileName, lineNumber);
	}
	
	public Class<?> getDeclaringType() {
		return declaringType;
	}
	
	public F getFunction() {
		return function;
	}
	
	public Object[] getArgs() {
		return args;
	}
	
	public StackTraceElement getStackTraceElement() {
		return stackTraceElement;
	}
    
	
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + "@" + declaringType + "." + function; 
	}

	public CallStackElement<?> getCaller() {
		return caller;
	}

	public Thread getThread() {
		return thread;
	}

	public String getFileName() {
		return fileName;
	}

	public int getLineNumber() {
		return lineNumber;
	}
}
