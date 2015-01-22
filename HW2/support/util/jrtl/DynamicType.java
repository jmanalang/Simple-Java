package util.jrtl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class DynamicType {

	private Class<?> _class;
	
	private final static Map<Class<?>,DynamicType> _map;
	
	public final static DynamicType NULL_TYPE = new DynamicType(null);
	
	static {
		_map = new ConcurrentHashMap<Class<?>, DynamicType>();
	}
	
	private DynamicType(Class<?> c){
		_class = c;
	}
	
	public static DynamicType getInstance(Class<?> c){
		if( c == null) return NULL_TYPE;
		
		if(!_map.containsKey(c)){
			_map.put(c, new DynamicType(c));
		}
		return _map.get(c);
	}

	public static DynamicType getInstance(Object o){
		if(o == null) return NULL_TYPE;
		return getInstance(o.getClass());
	}

	public static DynamicType getInstance(String name){
		Class<?> c = null;
		
		try {
			c = Class.forName(name);
		} catch (ClassNotFoundException e) {}
		
		return getInstance(c);
	}
	
	public Class<?> getType(){
		return _class;
	}
	
	public boolean isNull() { return _class == null; }
	
	public boolean isInterface() {
		if(!isNull()){
			return _class.isInterface();
		}
		return false;
	}
	
	public boolean isAbstract() {
		if(!isNull()){
			return Modifier.isAbstract(_class.getModifiers());
		}
		return false;
	}
	
	public String getName() {
		return _class.getSimpleName();
	}
	
	public String getPackage() {
		return _class.getPackage().getName();
	}
	
	public int getFieldCount() {
		if(isNull()) return 0;
		return _class.getDeclaredFields().length;
	}
	
	private Method getMethod(String name, Class<?>... params){
		if(isNull()) return null;
		
		Method m = null;
		
		try {
			m = _class.getDeclaredMethod(name, params);
		} catch (SecurityException e) {}
		catch (NoSuchMethodException e) {}
		
		return m;
	}
	
	public boolean hasConstructor(Class<?>... params) {
		if(isNull()) return false;
		Constructor<?> con = null;
		
		try {
			con = _class.getConstructor(params);
		} catch (SecurityException e) {}
		catch (NoSuchMethodException e) {}
		
		return con != null;
	}
	
	//public String getMethodNameBySignature
	
	public boolean hasMethodOfType(Class<?> returnType, Class<?>... params) {

		if (isNull()) { return false; }
		
		//TODO should this be _class.getDeclaredMethods()?
		Method[] methods = _class.getMethods();
		
		for(Method m : methods) {
			if(matchSignature(m, returnType, params))
				return true;
		}
		return false;
	}
	
	public boolean hasMethod(String name, Class<?>... params){
		return getMethod(name,params) != null;
	}
	
	public boolean hasVoidMethod(String name, Class<?>... params) {
		Method m;
		if( (m = getMethod(name,params)) != null){
			return m.getReturnType() == void.class;
		} else {
			return false;
		}
	}
	
	public boolean hasNonVoidMethod(String name, Class<?>... params){
		Method m;
		if( (m = getMethod(name,params)) != null){
			return m.getReturnType() != void.class;
		} else {
			return false;
		}
	}
	
	public boolean extendsClass(Class<?> parent){
		if(isNull() || isInterface()) return false;
		
		Class<?> child = _class.getSuperclass();
		
		while(child != null){
			if(child.equals(parent)) return true;
			child = child.getSuperclass();
		}
		return false;
	}
	
	public boolean extendsClass(String className){
		Class<?> c = null;
		
		try {
			c = Class.forName(className);
		} catch (ClassNotFoundException e) {}
		
		return extendsClass(c);
	}
	
	public boolean hasField(String name, Class<?> type){
		try {
			Field f = _class.getDeclaredField(name);
			return f.getName().equals(name) && type.isAssignableFrom(f.getType());
		} catch (SecurityException e) {
		} catch (NoSuchFieldException e) {
		}
		
		return false;
		
	}
	
	public boolean hasFieldByName(String fieldName){
		
		try {
			return _class.getDeclaredField(fieldName) != null;
		} catch (SecurityException e) {} 
		catch (NoSuchFieldException e) {}
		
		return false;
	}
	
	public boolean hasFieldByType(Class<?> fieldClass){
		return getFieldCountByType(fieldClass) > 0;
	}
	
	public boolean hasFieldByType(String typeName){
		Class<?> c = null;
		
		try {
			c = Class.forName(typeName);
		} catch (ClassNotFoundException e) {}
		
		return hasFieldByType(c);
	}

	public boolean hasNFieldsByType(Class<?> fieldClass, int count){
		if(isNull() || count > getFieldCount()) return false;
		
		return getFieldCountByType(fieldClass) == count;
	}

	public boolean hasNFieldsByType(String typeName, int count){
		if(isNull() || count > getFieldCount()) return false;
		
		return getFieldCountByType(typeName) == count;
	}
	
	public int getFieldCountByType(Class<?> fieldClass){
		if(isNull()) return 0;
		
		int count = 0;
		
		Field[] fields = _class.getDeclaredFields();
		
		for(Field f : fields){
			if(f.getType().equals(fieldClass))
				count++;
		}
		
		return count;
	}
	
	public int getFieldCountByType(String typeName){
		Class<?> c = null;
		
		try {
			c = Class.forName(typeName);
		} catch (ClassNotFoundException e) {}
		
		return getFieldCountByType(c);
	}
	
	public DynamicObject createInstance(Object... args){
		return DynamicObject.createFromClass(this, args);
	}

	public DynamicType getParent() {
		return DynamicType.getInstance(_class.getSuperclass());
	}
	
	public boolean implementsInterface(Class<?> type){
		if(isNull()) return false;
		Class<?>[] interfaces = _class.getInterfaces();
		for(Class<?> c : interfaces){
			if(c.equals(type)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean implementsInterface(String type){
		if(isNull()) return false;
		Class<?> interfaceClass = null;
		try{
			interfaceClass = Class.forName(type);
		} catch (ClassNotFoundException e){}
		return implementsInterface(interfaceClass);
	}
	
	public boolean implementsInterface(Generic type){
		if(isNull()) return false;
		if(type == null) return false;
		Type[] interfaces = _class.getGenericInterfaces();
		for( Type iface : interfaces){
			if(type.equals(iface)) {
				return true;
			}
		}
		return false;
	}

	public List<MethodInfo> getMethodsByType(Class<?> returnType, Class<?>... params) {
		List<MethodInfo> list = new LinkedList<MethodInfo>();
		
		for(Method m : _class.getDeclaredMethods()){
			if(matchSignature(m, returnType, params))
				list.add(new MethodInfo(m));
		}
		
		return list;
	}
	
	// written so that types must match exactly; assignment compatibility is not enough
	private boolean matchSignature(Method m, Class<?> returnType, Class<?>... params) {
		Class<?>[] mParameterTypes = m.getParameterTypes();
		if (m.getReturnType().equals(returnType) && params.length == mParameterTypes.length) {
			boolean hasMethod = true;
			for (int i=0; i<params.length && hasMethod; i++) {
				hasMethod = hasMethod && mParameterTypes[i].equals(params[i]);
			}
			if (hasMethod) { return true; }
		}
		return false;
	}
	
	@Override
	public String toString() {
		if(_class == null)
			return "DynamicType(null)";
		return "DynamicType("+_class.getName()+")";
	}
	
	
}
