package util.jrtl;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class MethodInfo {
	
	protected Method _method;
	
	public MethodInfo(Method m){
		_method = m;
	}

	private boolean checkModifier(int modifier){
		return (_method.getModifiers() & modifier) != 0;
	}
	
	public boolean isStatic(){
		return checkModifier(Modifier.STATIC);
	}
	
	public boolean isPublic(){
		return checkModifier(Modifier.PUBLIC);
	}
	
	public String getName(){
		return _method.getName();
	}

	public String getSignature() {
		return _method.toString();
	}
	
}
