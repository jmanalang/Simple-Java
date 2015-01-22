package util.jrtl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CallableMethod {

	public static CallableMethod Static(MethodInfo m) {
		if (m == null || !m.isStatic())
			throw new IllegalArgumentException("Problem creating a static CallableMethod.");
		return new CallableMethod(m._method,null);
	}

	public static CallableMethod Instance(MethodInfo m, DynamicObject o) {
		if(m == null || o == null || m._method == null || o.getRawObject() == null)
			throw new IllegalArgumentException("Arguments cannot be null for CallableMethod.Instace()");
		
		if (!m._method.getDeclaringClass().isAssignableFrom(o.getRawObject().getClass()))
			throw new IllegalArgumentException("The method must exist in the object.");
		
		return new CallableMethod(m._method,o.getRawObject());
	}

	private Method _m;
	private Object _o;
	
	private CallableMethod(Method m, Object o){
		_m = m;
		_o = o;
	}
	
	
	public CallResult call(Object... args){
		try {
			
			Object ret = _m.invoke(_o, args);
			if(_m.getReturnType() == void.class)
				return CallResult.VoidCallResult();
			return CallResult.newReturnValueCallResult(ret);
			
		} catch (IllegalArgumentException e) {
			return CallResult.newErrorCallResult("IllegalArguemnt Exception in CallableMethod.call().", "Talk to your instructor.");
		} catch (IllegalAccessException e) {
			return CallResult.newErrorCallResult("The method "+_m.toString()+" was inaccessible.", "Make the method public.");
		} catch (InvocationTargetException e) {
			return CallResult.newErrorCallResult("The method "+_m.toString()+" threw an exception of type " + e.getCause().getClass().getCanonicalName(), "Correct the code in the method.");
		} catch (NullPointerException e) {
			// _obj is not null, and this only occurs when calling an instance method on a null reference
			return CallResult.newErrorCallResult("Should not happen, _obj is not null", "");
		} catch (ExceptionInInitializerError e){
			return CallResult.newErrorCallResult("Error during initialization caused by this method call. " + e.getException(), "");
		} catch (Throwable t){
			return CallResult.newErrorCallResult(t.getMessage(), "");
		}
	}
	
}
