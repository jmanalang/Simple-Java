package code;

import code.BRStruct.IAlgo;

/**
 * A visitor for a BRStruct<E> that computes its size (the number of items in it).
 * 
 * The size of an empty BRStruct<E> is zero.
 * 
 * The size of a non-empty BRStruct<E> is one more than the sum of the sizes of
 * its children.
 */
public class Visitor<E> implements IAlgo<Integer, E, Object> {
	
	@Override
	public Integer emptyCase(BRStruct<E> host, Object _) {
		return 0;
	}

	@Override
	public Integer nonEmptyCase(BRStruct<E> host, Object _) {
		return 1 + host.getLeft().execute(this, _) + host.getRight().execute(this, _);
	}
}
