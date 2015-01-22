package code;

/**
 * This visitor operates on an LRStruct<String>, and computes the sum of the
 * lengths of all the Strings in the list.
 * 
 * Examples:
 * 
 * If the LRStruct<Integer> is			the correct answer is
 * ---------------------------			---------------------
 * ()									0
 * ("")									0
 * ("Fred")								4
 * ("Fred" "Wilma")						9
 * 
 * Recall that the length() method of a String returns its length.  For
 * example:
 *   "".length() is 0
 *   "Fred".length() is 4
 *   "Wilma".length() is 5
 */
public class Visitor implements LRStruct.IAlgo<Integer,String,Object> {

	/**
	 * This method gives the correct answer for an empty list
	 */
	@Override public Integer emptyCase(LRStruct<String> host, Object _) {
		return 0;
	}

	/**
	 * This method computes the correct answer for a non-empty list
	 */
	@Override public Integer nonEmptyCase(LRStruct<String> host, Object _) {
		String a = host.getDatum();
		return a.length() + host.getRest().execute(this, _);
	}
}
