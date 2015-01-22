package code;

import code.LRStruct.IAlgo;

/**
 * A visitor for an LRStruct<String> that computes the number of  Strings
 * in an LRStruct<String> that begin with the lower case letter 'a'.  A 
 * String s begins with the lower case letter 'a' if and only if 
 *     s.charAt(0) == 'a'
 */
public class Visitor implements IAlgo<Integer, String, Object> {
	
	@Override
	public Integer emptyCase(LRStruct<String> host, Object arg) {
		return 0;
	}

	@Override
	public Integer nonEmptyCase(LRStruct<String> host, Object _) {
		String s = host.getDatum();
		int count = 0;
		if(s.charAt(0) == 'a'){
			count++;
		}
		return count + host.getRest().execute(this, _);
	}
}
