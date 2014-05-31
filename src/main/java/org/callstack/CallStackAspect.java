package org.callstack;

import java.lang.reflect.AccessibleObject;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.annotation.SuppressAjWarnings;
import org.aspectj.lang.reflect.SourceLocation;


@Aspect
@SuppressAjWarnings("unmatchedSuperTypeInCall")
public class CallStackAspect {
	//@Pointcut("(execution(* *.*(..)) || execution(java.lang.Object.new(..)) ) && !cflow(call(* CallStackAspect.push(..)))   && !cflow(call(* CallStackAspect.pop(..))) && !cflow(call(* CallStack.*(..))) ")
	//@Pointcut("(execution(* *.*(..)) || call(new()) ) && !cflow(call(* CallStackAspect.push(..)))   && !cflow(call(* CallStackAspect.pop(..))) && !cflow(call(* CallStack.*(..))) ")
	@Pointcut("execution(* *.*(..))  && !cflow(call(* CallStackAspect.push(..)))   && !cflow(call(* CallStackAspect.pop(..))) && !cflow(call(* CallStack.*(..))) ")
	//@Pointcut("execution(* *.*(..)) && !cflow(call(* CallStackAspect.push(..)))   && !cflow(call(* CallStackAspect.pop(..))) && !cflow(call(* CallStack.*(..))) ")
	@SuppressAjWarnings("unmatchedSuperTypeInCall")
	public void anyCall() {
	}

	@Before("anyCall()")
	@SuppressAjWarnings("unmatchedSuperTypeInCall")
	public void before(JoinPoint thisJoinPoint) throws NoSuchMethodException {
		push(thisJoinPoint);
	}

	@After("anyCall()")
	@SuppressAjWarnings("unmatchedSuperTypeInCall")
	public void after(JoinPoint thisJoinPoint) {
		pop();
	}
	
	
	private void push(JoinPoint thisJoinPoint) throws NoSuchMethodException {
		//thisJoinPoint.getSourceLocation().get
		Signature signature = thisJoinPoint.getSignature();
		Class<?> type = signature.getDeclaringType();
		AccessibleObject function = Util.getMethod(type, signature.toLongString());
		SourceLocation location = thisJoinPoint.getSourceLocation();
		CallStack.pushCallStackElement(type, location.getFileName(), location.getLine(), function, thisJoinPoint.getArgs());
	}

	private void pop() {
		CallStack.removeCallStackElement();
	}

}
