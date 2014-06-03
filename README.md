# CallStack
## Motivation
Java provides `StackTrace` and starting from java 1.4 we can enjoy `StackTraceElement`:

```java
StackTraceElement[] elements = new Throwable().getStackTrace();
for(StackTraceElement element : elements) {
	element.getFileName()
	element.getLineNumber();
	element.getClassName();
	element.getMethodName();
} 
```

So, we can retrieve some information about caller of specific method. 
1. file name and line number are almost useless when source code is unavailable
2. `className` is string, so to get information about the `Class` we have to invoke performance expensive `Class.forName()`
3. `methodName` does not allow to distinguish between overloaded versions of method. 
4. we cannot retrieve the real arguments used to invoke specific method. 

`CallStack` solves all these problems.

## About
`CallStack` is a small `AspectJ` dependent pure java library that allows to retrieve information about current sequence of
method calls:
1. `Method` and its prototype
2. `Class` where the method is declared
3. Arguments used to call the method. 

All this information is available from be beginning of call sequence until current method. 

## Usage
It is very easy to use `CallStack`:

```java
CallStackElement<?>[] elements = CallStack.getCallStack();
for(CallStackElement element : elements) {
	element.getFileName()
	element.getDeclaredType()
	element.getLineNumber();
	element.getArgs(); // object array of arguments used to call current method or constructor
	element.getFunction(); // returns either Method or Constructor
} 
```

Library uses AspectJ, so to enable the feature we have to perform dynamic weaving by adding the following command line
parameter when running our application: `-javaagent:ASPECTJ_PATH/aspectjweaver-1.8.0.jar`.
For details please refer to this [document](http://www.eclipse.org/aspectj/doc/released/devguide/ltw.html).
 
