package code;

/**
 * This is Carl Alphonce's Java5 implementation of the Linear Recursive Structure, 
 * an object-oriented state-based linked list implementation due to D.X. Nguyen
 * and Stephen Wong.  Any errors in implementation are therefore due to Carl Alphonce.
 * 
 * Original papers about LRStruct:
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
 * A list comes in two "flavors":
 *     an empty list is a list,
 *     a non-empty list is a list.
 * 
 * This implementation is that of a mutable list; the state of the list (empty or
 * non-empty) is represented by its state.  The state of a list is a disjoint union type
 * AState whose two variants are EmptyState and NonEmptyState.
 *     
 * This is a minimalist implementation of a linked list which employs the Visitor
 * pattern to support extended functionality.  The visitor traverses the structure
 * via structural recursion.  A visitor is viewed as encapsulating an algorithm which
 * is executed throughout the structure.
 * 
 */
public class LRStruct<E> {

	/**
	 * The state of the LRStruct.
	 */
	private AState<E> _state;
	
	/**
	 * The sole public constructor for the LRStruct.
	 * Only an empty LRStruct can be constructed directly.
	 */
	public LRStruct() {
		_state = new EmptyState<E>();
//		_state = (AState<E>) (EmptyState.SINGLETON);
	}
	
	/**
	 * A private constructor to be used by the EmptyState and NonEmptyState
	 * classes to build an LRStruct in a particular state. 
	 * @param state
	 */
	private LRStruct(AState<E> state) {
		_state = state;
	}

	/**
	 * A method to insert a new element at the front of this LRStruct.
	 * @param item - the new element to be inserted
	 * @return a reference to this LRStruct (to allow chaining of method calls)
	 */
	public LRStruct<E> insertFront(E item) { return _state.insertFront(this, item); }
	
	/**
	 * A method to remove the first element from the front of this LRStruct.
	 * @return the removed element.
	 * @throws IllegalStateException if LRStruct is empty.
	 */
	public E removeFront() { return _state.removeFront(this); }

	/**
	 * A method to set the first datum of this LRStruct to the specified element.
	 * @param item - the new first element.
	 * @return a reference to this LRStruct (to allow chaining of method calls)
	 * @throws IllegalStateException if LRStruct is empty.
	 */
	public LRStruct<E> setDatum(E item) { return _state.setDatum(this, item); }

	/**
	 * A method to return the first datum of this LRStruct. 
	 * @return the first element.
	 * @throws IllegalStateException if LRStruct is empty.
	 */
	public E getDatum() { return _state.getDatum(this); }
	
	/**
	 * A method to set the tail of this LRStruct to the specified list.
	 * @param rest - the new tail.
	 * @return a reference to this LRStruct (to allow chaining of method calls)
	 * @throws IllegalStateException if LRStruct is empty.
	 */
	public LRStruct<E> setRest(LRStruct<E> rest) { return _state.setRest(this, rest); }

	/**
	 * A method to return the tail of this LRStruct. 
	 * @return the tail.
	 * @throws IllegalStateException if LRStruct is empty.
	 */
	public LRStruct<E> getRest() { return _state.getRest(this); }

	/**
	 * Visitor support.
	 * @param <I> The type of the visitor's argument.
	 * @param <O> The type of the value returned by the visitor.
	 * @param algo The visitor.
	 * @param arg The visitor's argument.
	 * @return some value, defined by the visitor.
	 */
	public <O,I> O execute(IAlgo<O,E,I> algo, I arg) {
		return _state.execute(algo, this, arg);
	}

	/*
	 * Private method used by EmptyState and NonEmptyState classes
	 * to set the AState of the LRStruct.
	 */
	private LRStruct<E> setState(AState<E> state) { _state = state; return this; }

	/*
	 * Private method used by EmptyState and NonEmptyState classes
	 * to access the AState of the LRStruct.
	 */
	private AState<E> getState() { return _state; }
	
	@Override
	public String toString() {
		return this.execute(new IAlgo<StringBuffer, E, StringBuffer>() {
			@Override
			public StringBuffer emptyCase(LRStruct<E> host, StringBuffer arg) {
				return arg.append("()");
			}

			/* (non-Javadoc)
			 * @see lrstruct.LRStruct.IAlgo#nonEmptyState(lrstruct.LRStruct, I)
			 */
			@Override
			public StringBuffer nonEmptyCase(LRStruct<E> host, StringBuffer arg) {
				arg.append("(");
				arg.append(host.getDatum());
				return host.getRest().execute(new IAlgo<StringBuffer,E,StringBuffer>() {
					@Override
					public StringBuffer emptyCase(LRStruct<E> host, StringBuffer arg) {
						return arg.append(")");
					}
					@Override
					public StringBuffer nonEmptyCase(LRStruct<E> host, StringBuffer arg) {
						arg.append(" ");
						arg.append(host.getDatum().toString());
						return host.getRest().execute(this, arg);
					}
				}, arg);
			}
		}, new StringBuffer()).toString();
	}

	/*
	 * Abstract state class
	 * [a nested static class - must be static to work with EmptyState]
	 * Common supertype for EmptyState and NonEmptyState classes.
	 */
	private static abstract class AState<E> {
		public LRStruct<E> insertFront(LRStruct<E> owner, E item) {
			return owner.setState(new NonEmptyState<E>(item,new LRStruct<E>(owner.getState())));
		}
		public abstract E removeFront(LRStruct<E> owner);
		public abstract LRStruct<E> setDatum(LRStruct<E> owner, E item);
		public abstract E getDatum(LRStruct<E> owner);
		public abstract LRStruct<E> setRest(LRStruct<E> owner, LRStruct<E> rest);
		public abstract LRStruct<E> getRest(LRStruct<E> owner);
		public abstract <O,I> O execute(IAlgo<O,E,I> algo, LRStruct<E> host, I arg);
	}
	
	/*
	 * Concrete state class
	 * [a nested static class - must be static due to SINGLETON]
	 * The EmptyState class - representing an empty LRStruct state.
	 */
	private static class EmptyState<E> extends AState<E> {
//		@SuppressWarnings("rawtypes")
//		public static final AState<?> SINGLETON = new EmptyState();

		/* (non-Javadoc)
		 * Private constructor to support SINGLETON pattern
		 */
		private EmptyState() {}
		
		/* (non-Javadoc)
		 * @see lrstruct.LRStruct.AState#execute(lrstruct.LRStruct.IAlgo, lrstruct.LRStruct, I)
		 */
		@Override
		public <O, I> O execute(IAlgo<O, E, I> algo, LRStruct<E> host, I arg) {
			return algo.emptyCase(host, arg);
		}

		/* (non-Javadoc)
		 * @see lrstruct.LRStruct.AState#removeFront(lrstruct.LRStruct)
		 */
		@Override
		public E removeFront(LRStruct<E> owner) {
			throw new IllegalStateException("Cannot remove an item from an empty LRStruct.");
		}

		/* (non-Javadoc)
		 * @see lrstruct.LRStruct.AState#setDatum(lrstruct.LRStruct, E)
		 */
		@Override
		public LRStruct<E> setDatum(LRStruct<E> owner, E item) {
			throw new IllegalStateException("Cannot set the datum of an empty LRStruct.");
		}

		/* (non-Javadoc)
		 * @see lrstruct.LRStruct.AState#getDatum(lrstruct.LRStruct)
		 */
		@Override
		public E getDatum(LRStruct<E> owner) {
			throw new IllegalStateException("Cannot get the datum of an empty LRStruct.");
		}

		/* (non-Javadoc)
		 * @see lrstruct.LRStruct.AState#setRest(lrstruct.LRStruct, lrstruct.LRStruct)
		 */
		@Override
		public LRStruct<E> setRest(LRStruct<E> owner, LRStruct<E> rest) {
			throw new IllegalStateException("Cannot set the rest of an empty LRStruct.");
		}

		/* (non-Javadoc)
		 * @see lrstruct.LRStruct.AState#getRest(lrstruct.LRStruct)
		 */
		@Override
		public LRStruct<E> getRest(LRStruct<E> owner) {
			throw new IllegalStateException("Cannot get the rest of an empty LRStruct.");
		}
	}

	/*
	 * Concrete state class
	 * [a nested static class - must be static to work with AState]
	 * The NonEmptyState class - representing a non-empty LRStruct state.
	 */
	private static class NonEmptyState<E> extends AState<E> {
		private E _datum;
		private LRStruct<E> _rest;

		/**
		 * @param item
		 * @param state
		 */
		public NonEmptyState(E item, LRStruct<E> rest) {
			_datum = item;
			_rest = rest;
		}

		/* (non-Javadoc)
		 * @see lrstruct.LRStruct.AState#execute(lrstruct.LRStruct.IAlgo, lrstruct.LRStruct, I)
		 */
		@Override
		public <O, I> O execute(IAlgo<O, E, I> algo, LRStruct<E> host, I arg) {
			return algo.nonEmptyCase(host, arg);
		}

		/* (non-Javadoc)
		 * @see lrstruct.LRStruct.AState#removeFront(lrstruct.LRStruct)
		 */
		@Override
		public E removeFront(LRStruct<E> owner) {
			owner.setState(_rest.getState());
			return _datum;
		}

		/* (non-Javadoc)
		 * @see lrstruct.LRStruct.AState#setDatum(lrstruct.LRStruct, E)
		 */
		@Override
		public LRStruct<E> setDatum(LRStruct<E> owner, E item) {
			_datum = item;
			return owner;
		}

		/* (non-Javadoc)
		 * @see lrstruct.LRStruct.AState#getDatum(lrstruct.LRStruct)
		 */
		@Override
		public E getDatum(LRStruct<E> owner) {
			return _datum;
		}

		/* (non-Javadoc)
		 * @see lrstruct.LRStruct.AState#setRest(lrstruct.LRStruct, lrstruct.LRStruct)
		 */
		@Override
		public LRStruct<E> setRest(LRStruct<E> owner, LRStruct<E> rest) {
			_rest = rest;
			return owner;
		}

		/* (non-Javadoc)
		 * @see lrstruct.LRStruct.AState#getRest(lrstruct.LRStruct)
		 */
		@Override
		public LRStruct<E> getRest(LRStruct<E> owner) {
			return _rest;
		}
		
	}

	public interface IAlgo<O, E, I> {

		/**
		 * This method handles the case when the host LRStruct<E> is empty.
		 * It represents the base case of a recursion.
		 * @param host The empty LRStruct<E> on which the visitor is being executed. 
		 * @param arg The argument passed to the visitor.
		 * @return The value returned by the visitor.
		 */
		public abstract O emptyCase(LRStruct<E> host, I arg);

		/**
		 * This method handles the case when the host LRStruct<E> is non-empty.
		 * It represents the recursive case of a recursion.
		 * @param host The non-empty LRStruct<E> on which the visitor is being executed. 
		 * @param arg The argument passed to the visitor.
		 * @return The value returned by the visitor.
		 */
		public abstract O nonEmptyCase(LRStruct<E> host, I arg);
	}

	
}
