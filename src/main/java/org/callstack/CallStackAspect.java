package org.callstack;

import java.lang.reflect.AccessibleObject;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.SourceLocation;


@Aspect
public class CallStackAspect {
	@Pointcut("(execution(* *.*(..)) || (execution(*.new(..)) && !within(CallStackAspect))) && !cflow(call(* CallStackAspect.push(..)))   && !cflow(call(* CallStackAspect.pop(..))) && !cflow(call(* CallStack.*(..))) ")
	public void anyCall() {
	}

	@Before("anyCall()")
	public void before(JoinPoint thisJoinPoint) throws NoSuchMethodException {
		push(thisJoinPoint);
	}

	@After("anyCall()")
	public void after(JoinPoint thisJoinPoint) {
		pop();
	}
	
	
	private void push(JoinPoint thisJoinPoint) throws NoSuchMethodException {
		Signature signature = thisJoinPoint.getSignature();
		Class<?> type = signature.getDeclaringType();
		
		AccessibleObject function = null;
		try {
			function = Util.getMethod(type, signature.toLongString());
		} catch (NoSuchMethodException e) {
			// N/A Typical for various dynamic proxies etc. 
			//TODO: add log here?
		}
		SourceLocation location = thisJoinPoint.getSourceLocation();
		CallStack.pushCallStackElement(type, location.getFileName(), location.getLine(), function, thisJoinPoint.getArgs());
	}

	private void pop() {
		CallStack.removeCallStackElement();
	}

}
