package util.jrtl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class Generic {

	Object[] _args;
	Class<?> _class;

	private Generic(Class<?> type, Object... args){
		_class = type;
		_args = args;
	}
		
	public static Generic Type(Class<?> type, Object... args) {
		return new Generic(type,args);
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Generic) {
			Generic gtype = (Generic) o;
			if (_class == gtype._class && _args.length == gtype._args.length) {
				boolean ok = true;
				for (int i = 0; i < _args.length; i++) {
					if (!((_args[i] == null && gtype._args[i] == null) || (_args[i] != null && _args[i].equals(gtype._args[i])))) {
						ok = false;
						break;
					}
				}
				if (ok) return true;

			}
			return false;
		} else if (o instanceof ParameterizedType) {
			ParameterizedType ptype = (ParameterizedType) o;
			
			if (_class.equals(ptype.getRawType())) {
				Type[] types = ptype.getActualTypeArguments();
				if (_args.length == types.length) {
					boolean ok = true;
					for (int i = 0; i < _args.length; i++) {
						if (_args[i] != null && !_args[i].equals(types[i]))
							ok = false;
					}
					if (ok)
						return true;
				}
			}
			
			
			return false;
		} else {
			return super.equals(o);
		}
	}

}
