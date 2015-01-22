package code;

/**
 * This is Carl Alphonce's Java5 implementation of the Binary Recursive Structure, 
 * an object-oriented state-based tree implementation due to D.X. Nguyen
 * and Stephen Wong.  Any errors in implementation are therefore due to Carl Alphonce.
 * 
 * Original papers about LRStruct, the Linear Recursive Structure (an object-oriented
 * state-based linked list implementation), which shares the basic design
 * of the BRStruct, is discussed here:
 * 
 * Nguyen, D. 1998. Design patterns for data structures.
 * SIGCSE Bull. 30, 1 (Mar. 1998), 336-340.
 * DOI=http://doi.acm.org.gate.lib.buffalo.edu/10.1145/274790.274325
 * 
 * Nguyen, D. and Wong, S. B. 1999. Patterns for decoupling data structures and algorithms.
 * SIGCSE Bull. 31, 1 (Mar. 1999), 87-91.
 * DOI=http://doi.acm.org.gate.lib.buffalo.edu/10.1145/384266.299693
 * 
 * 
 * A binary tree comes in two "flavors":
 *     an empty binary tree is a binary tree,
 *     a non-empty binary tree (a binary tree with two binary trees as children) is a binary tree.
 * 
 * This implementation is that of a mutable binary tree; the state of the tree (empty or
 * non-empty) is represented by its state.  The state of a tree is a disjoint union type
 * AState whose two variants are EmptyState and NonEmptyState.
 *     
 * This is a minimalist implementation of a binary tree which employs the Visitor
 * pattern to support extended functionality.  The visitor traverses the structure
 * via structural recursion.  A visitor is viewed as encapsulating an algorithm which
 * is executed throughout the structure.
 * 
 */
@SuppressWarnings("unchecked")
public class BRStruct<E> {

	/**
	 * The state of the LRStruct.
	 */
	private AState<E> _state;
	
	/**
	 * The sole public constructor for the BRStruct.
	 * Only an empty BRStruct can be constructed directly.
	 */
	public BRStruct() {
		_state = (AState<E>) (EmptyState.SINGLETON);
	}

	/**
	 * A method to insert a new element at the top of this BRStruct.
	 * @param item - the new element to be inserted
	 * @return a reference to this BRStruct (to allow chaining of method calls)
	 */
	public BRStruct<E> insertRoot(E item) { return _state.insertRoot(this, item); }
	
	/**
	 * A method to remove the root element from the front of this BRStruct.
	 * Permitted only if this is a leaf (both the left and right children of this
	 * BRStruct at empty).
	 * @return the removed element.
	 * @throws IllegalStateException if BRStruct is empty, or if this BRStruct
	 *         is not a leaf.
	 */
	public E removeRoot() { return _state.removeRoot(this); }

	/**
	 * A method to set the root datum of this BRStruct to the specified element.
	 * @param item - the new root element.
	 * @return a reference to this BRStruct (to allow chaining of method calls)
	 * @throws IllegalStateException if BRStruct is empty.
	 */
	public BRStruct<E> setDatum(E item) { return _state.setDatum(this, item); }

	/**
	 * A method to return the root datum of this BRStruct. 
	 * @return the first element.
	 * @throws IllegalStateException if BRStruct is empty.
	 */
	public E getDatum() { return _state.getDatum(this); }
	
	/**
	 * A method to set the left of this BRStruct to the specified list.
	 * @param rest - the new left.
	 * @return a reference to this BRStruct (to allow chaining of method calls)
	 * @throws IllegalStateException if BRStruct is empty.
	 */
	public BRStruct<E> setLeft(BRStruct<E> rest) { return _state.setLeft(this, rest); }

	/**
	 * A method to return the left of this BRStruct. 
	 * @return the left.
	 * @throws IllegalStateException if BRStruct is empty.
	 */
	public BRStruct<E> getLeft() { return _state.getLeft(this); }

	/**
	 * A method to set the  of this BRStruct to the specified list.
	 * @param rest - the new right.
	 * @return a reference to this BRStruct (to allow chaining of method calls)
	 * @throws IllegalStateException if BRStruct is empty.
	 */
	public BRStruct<E> setRight(BRStruct<E> rest) { return _state.setRight(this, rest); }

	/**
	 * A method to return the right of this BRStruct. 
	 * @return the right.
	 * @throws IllegalStateException if BRStruct is empty.
	 */
	public BRStruct<E> getRight() { return _state.getRight(this); }

	/**
	 * Visitor support.
	 * @param <I> The type of the visitor's argument.
	 * @param <O> The type of the value returned by the visitor.
	 * @param algo The visitor.
	 * @param arg The visitor's argument.
	 * @return some value, defined by the visitor.
	 */
	public <O,I> O execute(IAlgo<O,E,I> algo, I arg) { return _state.execute(algo, this, arg); }

	/*
	 * Package access method used by EmptyState and NonEmptyState classes
	 * to set the AState of the BRStruct.
	 */
	BRStruct<E> setState(AState<E> state) {
		_state = state;
		return this;
	}

	/*
	 * Package access method used by EmptyState and NonEmptyState classes
	 * to access the AState of the BRStruct.
	 */
	AState<E> getState() { return _state; }
	
	@Override
	public String toString() {
		return this.execute(new IAlgo<StringBuffer, E, StringBuffer>() {
			@Override
			public StringBuffer emptyCase(BRStruct<E> host, StringBuffer arg) {
				return arg.append("[]");
			}
			@Override
			public StringBuffer nonEmptyCase(BRStruct<E> host, StringBuffer arg) {
				host.getLeft().execute(this,arg.append("["));
				arg.append(" ");
				arg.append(host.getDatum().toString());
				arg.append(" ");
				host.getRight().execute(this,arg).append("]");
				return arg;
			}
		}, new StringBuffer()).toString();
	}

	/*
	 * Abstract state class
	 * Common supertype for EmptyState and NonEmptyState classes.
	 */
	private static abstract class AState<E> {
		public abstract <O,I> O execute(IAlgo<O,E,I> algo, BRStruct<E> host, I arg);
		public abstract BRStruct<E> insertRoot(BRStruct<E> owner, E item);
		public abstract E removeRoot(BRStruct<E> owner);
		public abstract BRStruct<E> setDatum(BRStruct<E> owner, E item);
		public abstract E getDatum(BRStruct<E> owner);
		public abstract BRStruct<E> setLeft(BRStruct<E> owner, BRStruct<E> left);
		public abstract BRStruct<E> getLeft(BRStruct<E> owner);
		public abstract BRStruct<E> setRight(BRStruct<E> owner, BRStruct<E> right);
		public abstract BRStruct<E> getRight(BRStruct<E> owner);
	}
	
	/*
	 * Concrete state class
	 * The EmptyState class - representing an empty BRStruct state.
	 */
	@SuppressWarnings("rawtypes")
	private static class EmptyState<E> extends AState<E> {
		public static final AState<?> SINGLETON;
		static { SINGLETON = new EmptyState(); }

		@Override public <O, I> O execute(IAlgo<O, E, I> algo, BRStruct<E> host, I arg) {
			return algo.emptyCase(host, arg);
		}

		@Override public BRStruct<E> setDatum(BRStruct<E> owner, E item) {
			throw new IllegalStateException("Cannot set the datum of an empty BRStruct.");
		}

		@Override public E getDatum(BRStruct<E> owner) {
			throw new IllegalStateException("Cannot get the datum of an empty BRStruct.");
		}

		@Override public BRStruct<E> insertRoot(BRStruct<E> owner, E item) {
			return owner.setState(new NonEmptyState<E>(item, new BRStruct<E>(), new BRStruct<E>()));
		}

		@Override public E removeRoot(BRStruct<E> owner) {
			throw new IllegalStateException("Cannot remove the root of an empty BRStruct.");
		}

		@Override public BRStruct<E> setLeft(BRStruct<E> owner, BRStruct<E> left) {
			throw new IllegalStateException("Cannot set the left of an empty BRStruct.");
		}

		@Override public BRStruct<E> getLeft(BRStruct<E> owner) {
			throw new IllegalStateException("Cannot get the left of an empty BRStruct.");
		}

		@Override public BRStruct<E> setRight(BRStruct<E> owner, BRStruct<E> right) {
			throw new IllegalStateException("Cannot set the right of an empty BRStruct.");
		}

		@Override public BRStruct<E> getRight(BRStruct<E> owner) {
			throw new IllegalStateException("Cannot get the right of an empty BRStruct.");
		}

	}

	private static class NonEmptyState<E> extends AState<E> {
		private E _datum;
		private BRStruct<E> _left;
		private BRStruct<E> _right;

		public NonEmptyState(E item, BRStruct<E> left, BRStruct<E> right) {
			_datum = item;
			_left = left;
			_right = right;
		}

		@Override public <O, I> O execute(IAlgo<O, E, I> algo, BRStruct<E> host, I arg) {
			return algo.nonEmptyCase(host, arg);
		}

		@Override public BRStruct<E> setDatum(BRStruct<E> owner, E item) {
			_datum = item;
			return owner;
		}

		@Override public E getDatum(BRStruct<E> owner) {
			return _datum;
		}

		@Override public BRStruct<E> insertRoot(BRStruct<E> owner, E item) {
			throw new IllegalStateException("Cannot insert into a non-empty BRStruct.");
		}

		/* (non-Javadoc)
		 * @see brstruct.BRStruct.AState#removeRoot(brstruct.BRStruct)
		 * We can remove the root only of a BRStruct which is a leaf.
		 * To determine whether an arbitrary non-empty BRStruct is a leaf 
		 * we need to consider its two children.  If they are both empty,
		 * then we got ourselves a leaf.  If either child is non-empty this
		 * tree is not a leaf, and an exception is thrown. 
		 */
		@Override public E removeRoot(final BRStruct<E> owner) {
			return owner.getLeft().execute(new IAlgo<E, E, BRStruct<E>>() {
				@Override public E emptyCase(BRStruct<E> host, BRStruct<E> sibling) {
					return sibling.execute(new IAlgo<E, E, BRStruct<E>>() {
						@Override public E emptyCase(BRStruct<E> host, BRStruct<E> arg) {
							owner.setState((AState<E>) EmptyState.SINGLETON);
							return _datum;
						}
						@Override public E nonEmptyCase(BRStruct<E> host, BRStruct<E> arg) {
							throw new IllegalStateException("Cannot remove the root of a non-leaf BRStruct.");
						}}, null);
				}
				@Override public E nonEmptyCase(BRStruct<E> host, BRStruct<E> sibling) {
					throw new IllegalStateException("Cannot remove the root of a non-leaf BRStruct.");
				}}, owner.getRight());
		}

		@Override public BRStruct<E> setLeft(BRStruct<E> owner, BRStruct<E> left) {
			_left = left;
			return owner;
		}

		@Override public BRStruct<E> getLeft(BRStruct<E> owner) {
			return _left;
		}

		@Override public BRStruct<E> setRight(BRStruct<E> owner, BRStruct<E> right) {
			_right = right;
			return owner;
		}

		@Override public BRStruct<E> getRight(BRStruct<E> owner) {
			return _right;
		}

	}

	public interface IAlgo<O, E, I> {
		public abstract O emptyCase(BRStruct<E> host,I arg);
		public abstract O nonEmptyCase(BRStruct<E> host, I arg);
	}
}
