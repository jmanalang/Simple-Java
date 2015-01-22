package tests;

import code.BRStruct;
import code.BRStruct.IAlgo;

/**
 * @author alphonce
 * 
 * This visitor traverses a BRStruct looking for arg.
 * If arg is found, the BRStruct whose datum .equals arg
 * is returned.  Otherwise, the empty BRStruct where arg
 * would appear if it were in the tree is returned.
 *
 */
public class FindVisitor<E extends Comparable<E>> implements IAlgo<BRStruct<E>,E,E> {
	@Override
	public BRStruct<E> emptyCase(BRStruct<E> host, E arg) {
		return host;
	}
	@Override
	public BRStruct<E> nonEmptyCase(BRStruct<E> host, E arg) {
		if (arg.compareTo(host.getDatum()) < 0) {
			// arg comes before host.getDatum(), so look in left
			return host.getLeft().execute(this, arg);
		}
		else if (arg.compareTo(host.getDatum()) > 0) {
			// arg comes after host.getDatum(), so look in right
			return host.getRight().execute(this, arg);
		}
		else {
			// found arg in host tree
			return host;
		}
	}
}
