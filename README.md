# CallStack
## Motivation
Java provides `StackTrace` and starting from java 1.4 we can use `StackTraceElement`:

```java
StackTraceElement[] elements = new Throwable().getStackTrace();
for(StackTraceElement element : elements) {
	element.getFileName()
	element.getLineNumber();
	element.getClassName();
	element.getMethodName();
} 
```

We can retrieve some information about caller of any method. 
However: 
* file name and line number are almost useless when source code is unavailable
* `className` is a string, so to get information about the `Class` we have to invoke performance expensive `Class.forName()`
* `methodName` does not allow to distinguish between overloaded versions of method. 
* we cannot retrieve the real arguments used to invoke specific method. 

`CallStack` solves all these problems.

## About
`CallStack` is a small `AspectJ` dependent pure java library that allows to retrieve information about current sequence of
method calls:
* `Method` and its prototype
* `Class` where the method is declared
* `This` - object that called current method. 
* Arguments used to call the method. 

All this information is available for all methods in current call sequence, i.e. typically starts from `main()` method
for the main thread or from `run()` method for all other threads.  

## Usage
It is very easy to use `CallStack`:

```java
CallStackElement<?>[] elements = CallStack.getCallStack();
for(CallStackElement element : elements) {
	element.getFileName()
	element.getDeclaredType()
	element.getLineNumber();
	element.getThis(); // object that called current method.  
	element.getArgs(); // object array of arguments used to call current method or constructor
	element.getFunction(); // returns either Method or Constructor
	element.getThread(); // thread where the call was done. Useful in multithreaded application if call stack is passed to other thread for processing.
	element.getStackTraceElement(); // the good old stack trace is available here too.
} 
```

Library uses AspectJ dynamic weaving. Just add the following command line
parameter when running your application: `-javaagent:ASPECTJ_PATH/aspectjweaver-1.8.0.jar`.
For details please refer to this [document](http://www.eclipse.org/aspectj/doc/released/devguide/ltw.html).
 
