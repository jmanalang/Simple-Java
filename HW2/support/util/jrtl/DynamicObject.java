package util.jrtl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

public class DynamicObject {
	
//	private static final DynamicObject NULL_OBJECT = new DynamicObject(null);
	
	private Object _this;
	private DynamicType _type;
	private String _cause;
	private String _fix;
	
	public Object getRawObject() {
		return _this;
	}
	
	public DynamicType getType() {
		return _type;
	}
	
	/*package*/ DynamicObject(Object o){
		this(o, "", "");
	}
	
	private DynamicObject(Object o, String cause, String fix) {
		_this = o;
		_cause = cause;
		_fix = fix;
		if(_this!=null)
			_type = DynamicType.getInstance(_this.getClass());
	}
	
	public String getProblemCause() {
		return _cause;
	}
	
	public String getProblemFix() {
		return _fix;
	}
	
	public static DynamicObject createFromClass(DynamicType c, Object... args){

		Class<?>[] types = getParameterTypeArray(args);
		
		String constructor = getMethodDescription(c.getClass().getCanonicalName(), types);
		
		try{
			
			Constructor<?>[] cons = c.getType().getConstructors();
			
			for(Constructor<?> con : cons){
				Class<?>[] params = con.getParameterTypes();
				if(types.length == params.length){
					boolean ok = true;
					for(int i = 0; i < types.length; i++){
						if(!params[i].isAssignableFrom(types[i])){
							ok = false;
							break;
						}
					}
					if (ok){
						return new DynamicObject(con.newInstance(args));
					}
				}
			}
			
			// No Matching constructor found
			
			
			
			return new DynamicObject(null, "The constructor '"+constructor+"' was not found", "define a constructor matching '"+constructor+"'");
			
		} catch (IllegalArgumentException e) {
			return new DynamicObject(null, "a testing error occured (DynamicObject/createFromClass/IllegalArgumentException)", "tell your instructor pronto!");
		} catch (InstantiationException e) {
			return new DynamicObject(null, "the class "+c.getName()+" is abstract, and cannot be instantiated.", "make the class concrete.");
		} catch (IllegalAccessException e) {
			return new DynamicObject(null, "a testing error occured (DynamicObject/createFromClass/IllegalAccessException)", "tell your instructor pronto!");
		} catch (InvocationTargetException e) {
			return new DynamicObject(null, "running the constructor "+constructor+" threw an exception: "+ e.getCause().getMessage()+" // "+e.getCause().getClass().getName(), "debug your constructor code.");
		} catch (ExceptionInInitializerError e) {
			return new DynamicObject(null, "a testing error occured (DynamicObject/createFromClass/ExceptionInInitializerError)", "tell your instructor pronto!");
		}
	}
	
	private static String getMethodDescription(String name, Class<?>[] types){
		StringBuilder method = new StringBuilder();
		method.append(name);
		method.append('(');
		if(types.length > 0){
			method.append(types[0].getCanonicalName());
			for(int i = 1; i < types.length; i++){
				method.append(',');
				method.append(types[i].getCanonicalName());
			}
		}
		method.append(')');
		return method.toString();
	}
	
	public boolean isNull() {
		return _this == null;
	}
	
	private Method getMethod(String name, Object... args){
		if(isNull()) return null;
		
		Class<?>[] types = getParameterTypeArray(args);
		
		Method[] methods = _type.getType().getMethods();
		
		for(Method m : methods){
			Class<?>[] params = m.getParameterTypes();
			if(m.getName().equals(name) && params.length == types.length){
				boolean ok = true;
				for(int i = 0; i < params.length; i++){
					if(!params[i].isAssignableFrom(types[i]))
						ok = false;
				}
				if (ok) return m;
			}
		}
		return null;
	}
	
	public CallResult call(String name, Object... args){
		if(isNull()) return CallResult.newErrorCallResult("The DynamicObject is null.", "Initialize the object.");
		
		String description = getMethodDescription(name, getParameterTypeArray(args));
		
		Method m = getMethod(name,args);
		
		if(m == null) return CallResult.newErrorCallResult("The method "+description+" was not found.", "Define the method.");
		
		if(Modifier.isStatic(m.getModifiers()))
			return CallResult.newErrorCallResult("Invalid method call,", "");
		
		// method with the proper signature found
		
		try {
			Object ret = m.invoke(_this, args);
			
			if(m.getReturnType() == void.class)
				return CallResult.VoidCallResult();
			else
				return CallResult.newReturnValueCallResult(ret);
			
		} catch (IllegalArgumentException e) {
			return CallResult.newErrorCallResult("IllegalArguemnt Exception in DynamicObject.call().", "Talk to your instructor.");
		} catch (IllegalAccessException e) {
			return CallResult.newErrorCallResult("The method "+description+" was inaccessible.", "Make the method public.");
		} catch (InvocationTargetException e) {
			return CallResult.newErrorCallResult("The method "+description+" threw an exception of type " + e.getCause().getClass().getCanonicalName(), "Correct the code in the method.");
		} catch (NullPointerException e) {
			// _obj is not null, and this only occurs when calling an instance method on a null reference
			return CallResult.newErrorCallResult("Should not happen, _obj is not null", "");
		} catch (ExceptionInInitializerError e){
			return CallResult.newErrorCallResult("Error during initialization caused by this method call. " + e.getException(), "");
		}
		
	}
	
	
	
	public void callVoidMethod(String name, Object... args){
		call(name,args);
	}
	
	public DynamicObject callNonVoidMethod(String name, Object... args){
		
		CallResult res = call(name,args);
		
		if(!res.isError())
			return res.getResult();
		
		return null;
		
	}
	
	private static Class<?>[] getParameterTypeArray(Object... args){
		ArrayList<Class<?>> parameterTypes = new ArrayList<Class<?>>();
		
		for(Object arg : args){
			parameterTypes.add(arg.getClass());
		}
		
		return parameterTypes.toArray(new Class<?>[0]);
		
	}
	
	public <T> Collection<T> getFieldsByType (Class<T> c){
		if(isNull()) return Collections.emptyList();
		Collection<T> col = new LinkedList<T>();
		for(Field f : _type.getType().getDeclaredFields()){
			if(c.isAssignableFrom(f.getType())){
				try {
					f.setAccessible(true);
					col.add(c.cast(f.get(_this)));
				} catch (IllegalArgumentException e) {
				} catch (IllegalAccessException e) {}
			}
		}
		return col;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getFieldByName (String name) {
		
		
		try {
			Field f = _type.getType().getDeclaredField(name);
			f.setAccessible(true);
			
			return (T)(f.get(_this));
		} catch (SecurityException e) {
		} catch (NoSuchFieldException e) {
		} catch (IllegalAccessException e){}
		
		return null;
	}
	
	public DynamicObject castAsParent() {
		if(isNull()) return this;
		DynamicObject obj = new DynamicObject(_this);
		obj._type = DynamicType.getInstance(obj._type.getType().getSuperclass());
		return obj;
	}
	

}
