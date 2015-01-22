package tests;

import code.BRStruct;
import code.BRStruct.IAlgo;

public class BST<E extends Comparable<E>> {
	private BRStruct<E> _brs;
	public BST() { _brs = new BRStruct<E>(); }
	public BRStruct<E> getTree() { return _brs; }
	public BST<E> insert(E item) {
		BRStruct<E> tree = _brs.execute(new FindVisitor<E>(), item);
		tree.execute(new IAlgo<Object,E,E>(){
			@Override public Object emptyCase(BRStruct<E> host, E item) {
				return host.insertRoot(item);
			}
			@Override public Object nonEmptyCase(BRStruct<E> host, E item) {
				return host;
			}
		}, item);
		return this;
	}
}
